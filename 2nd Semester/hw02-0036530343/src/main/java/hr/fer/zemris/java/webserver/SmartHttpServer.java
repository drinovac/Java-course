package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmartHttpServer {
    private String address;
    private String domainName;
    private int port;
    private int workerThreads;
    private int sessionTimeout;
    private Map<String,String> mimeTypes = new HashMap<String, String>();
    private ServerThread serverThread;
    private ExecutorService threadPool;
    private Path documentRoot;
    private Map<String, IWebWorker> workersMap = new HashMap<>();
    private Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();
    private Random sessionRandom = new Random();

    public SmartHttpServer(String configFileName) {

        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of(configFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String mimeTypesPath = null;
        String workersPath = null;

        for (String line: lines) {
            if(line.startsWith("#")) {
                continue;
            }
            String[] parts = line.split(" = ");

            String variable = parts[0].split("\\.")[1];

            switch (variable) {
                case ("address") -> this.address = parts[1];
                case ("domainName") -> this.domainName = parts[1];
                case ("port") -> this.port = Integer.parseInt(parts[1]);
                case ("workerThreads") -> this.workerThreads = Integer.parseInt(parts[1]);
                case ("documentRoot") -> this.documentRoot = Path.of(parts[1]);
                case ("timeout") -> this.sessionTimeout = Integer.parseInt(parts[1]);
                case ("mimeConfig") -> mimeTypesPath = parts[1];
                case ("workers") -> workersPath = parts[1];
            }

        }
        List<String> mimeTypesLines;
        try {
            mimeTypesLines = Files.readAllLines(Path.of(mimeTypesPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(String line: mimeTypesLines) {
            String[] parts = line.split(" = ");
            mimeTypes.put(parts[0], parts[1]);
        }

        List<String> workersLines;
        try {
            workersLines = Files.readAllLines(Path.of(workersPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String line: workersLines) {
            String[] parts = line.split(" = ");
            String path = parts[0];
            String fqcn = parts[1];

            try {
                Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
                Object newObject = referenceToClass.newInstance();
                IWebWorker iww = (IWebWorker)newObject;

                workersMap.put(path, iww);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }


    }
    protected synchronized void start() {
        this.serverThread = new ServerThread();
        this.serverThread.start();

        if (threadPool == null) {
            threadPool = Executors.newFixedThreadPool(workerThreads);
        }

        Thread clean = new Thread(() -> {
            while (true) {
                for (Map.Entry<String, SessionMapEntry> entry : sessions.entrySet()) {
                    String sid = entry.getKey();
                    SessionMapEntry session = entry.getValue();
                    if (session.validUntil < System.currentTimeMillis() / 1000) {
                        sessions.remove(sid);
                    }
                }
                try {
                    Thread.sleep(5 * 60 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        clean.setDaemon(true);
        clean.start();

    }
    protected synchronized void stop() {
        this.serverThread.interrupt();
        this.threadPool.shutdown();
    }

    protected class ServerThread extends Thread {
        @Override
        public void run() {

            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    ClientWorker clientWorker = new ClientWorker(client);
                    threadPool.submit(clientWorker);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
    private class ClientWorker implements Runnable, IDispatcher {
        private Socket csocket;
        private InputStream istream;
        private OutputStream ostream;
        private String version;
        private String method;
        private String host;
        private Map<String,String> params = new HashMap<String, String>();
        private Map<String,String> tempParams = new HashMap<String, String>();
        private Map<String,String> permPrams = new HashMap<String, String>();
        private List<RequestContext.RCCookie> outputCookies =
                new ArrayList<RequestContext.RCCookie>();
        private String SID;

        private RequestContext context = null;

        public ClientWorker(Socket csocket) {
            super();
            this.csocket = csocket;
        }
        @Override
        public void run() {

            try {
                this.istream = csocket.getInputStream();
                this.ostream = csocket.getOutputStream();

                List<String> request = readRequest();
                if(request.isEmpty()) {
                    String response = """
                            HTTP/1.1 400 Bad Request\r
                            Content-Type: text/plain\r
                            Content-Length: 0\r
                            \r
                            """;
                    ostream.write(response.getBytes(StandardCharsets.US_ASCII));
                    return;
                }
                String firstLine = request.get(0);
                String[] parts = firstLine.split(" ");
                this.method = parts[0];
                this.version = parts[2];

                for (String line : request) {
                    if(line.startsWith("Host: ")) {
                        this.host = line.split(":")[1].trim();
                    }
                }

                checkSession(request);

                String requestedPath;
                if(parts[1].contains("?")) {
                    String paramString = parts[1].split("\\?")[1];

                    for(String param: paramString.split("&")) {
                        params.put(param.split("=")[0], param.split("=")[1]);
                    }
                    requestedPath = parts[1].split("\\?")[0];
                } else {
                    requestedPath = parts[1];
                }

                internalDispatchRequest(requestedPath, true);


            } catch (Exception e) {
                e.printStackTrace();

            }

            try {
                csocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        private void checkSession(List<String> request) {
            String sidCandidate = null;
            for (String line : request) {
                if(line.startsWith("Cookie:")) {
                   String[] cookies = line.substring(8).split(";");
                   for (String cookie: cookies) {
                       if (cookie.startsWith("sid=")) {
                           sidCandidate = cookie.substring(4);
                       }
                   }
                }

            }
            if (sidCandidate != null) {
                SessionMapEntry session = sessions.get(sidCandidate);
                if (session != null) {
                    if (session.host.equals(host)) {
                        if (session.validUntil > System.currentTimeMillis() / 1000) {
                            session.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
                            permPrams = session.map;
                            return;
                        } else {
                            sessions.remove(sidCandidate);
                        }
                    }
                }
            } else {
                byte[] bytes = new byte[20];
                sessionRandom.nextBytes(bytes);
                sidCandidate = Base64.getEncoder().encodeToString(bytes).substring(0, 20);
            }
            SessionMapEntry newSession = new SessionMapEntry(sidCandidate, host, System.currentTimeMillis() / 1000 + sessionTimeout, new ConcurrentHashMap<>());
            sessions.put(sidCandidate, newSession);
            outputCookies.add(new RequestContext.RCCookie("sid", sidCandidate, host, "/", null));
            permPrams = newSession.map;
            this.SID = sidCandidate;
        }

        private List<String> readRequest() throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(istream));


            List<String> lines = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null && !line.equals("")) {
                lines.add(line);
            }

            return lines;

        }

        @Override
        public void dispatchRequest(String urlPath) throws Exception {
            internalDispatchRequest(urlPath, false);
        }

        private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception{

            this.context = context == null ? new RequestContext(ostream, params,tempParams, permPrams, outputCookies, this, SID) : context;

            if(urlPath.startsWith("/ext/")) {
                try {
                    String[] parts = urlPath.split("/");
                    Class<?> referenceToClass = this.getClass().getClassLoader().loadClass("hr.fer.zemris.java.webserver.workers." + parts[2]);
                    Object newObject = referenceToClass.newInstance();
                    IWebWorker iww = (IWebWorker) newObject;
                    iww.processRequest(this.context);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            if(checkPath(urlPath)) {
                IWebWorker worker = SmartHttpServer.this.workersMap.get(urlPath);
                worker.processRequest(this.context);
                return;
            }

            if (directCall && urlPath.startsWith("/private/")) {
                String response = "HTTP/1.1 404 Not Found\r\nContent-Type: " +
                        "text/plain\r\nContent-Length: 0\r\n\r\n";
                ostream.write(response.getBytes(StandardCharsets.US_ASCII));
                ostream.flush();
                return;
            }

            File requestedFile = new File(urlPath.substring(1));

            if(Path.of(urlPath).toAbsolutePath().startsWith(documentRoot)) {
                String response = "HTTP/1.1 403 Forbidden\r\nContent-Type: " +
                        "text/plain\r\nContent-Length: 0\r\n\r\n";
                ostream.write(response.getBytes());
                ostream.flush();
            }
            if(!(requestedFile.exists() && requestedFile.isFile() && requestedFile.canRead())) {
                String response = "HTTP/1.1 404 Not Found\r\nContent-Type: " +
                        "text/plain\r\nContent-Length: 0\r\n\r\n";
                ostream.write(response.getBytes(StandardCharsets.US_ASCII));
                ostream.flush();
            }

            String fileExtension = requestedFile.getName().split("\\.")[1];

            if(fileExtension.equals("smscr")) {
                smscrParse(requestedFile);
            } else {
                String mimeType = SmartHttpServer.this.mimeTypes.get(fileExtension);

                if(mimeType == null) {
                    mimeType = "application/octet-stream";
                }

                this.context.setMimeType(mimeType);
                this.context.setStatusCode(200);

                try(InputStream inputStream = new BufferedInputStream(new FileInputStream(requestedFile))) {

                    byte[] buff = new byte[1024];
                    while (true) {
                        int i = inputStream.read(buff);
                        if (i < 1) break;
                        this.context.write(Arrays.copyOfRange(buff, 0, i));
                    }
                }
            }


        }

        private boolean checkPath(String urlPath) {
            return SmartHttpServer.this.workersMap.containsKey(urlPath);
        }

        private void smscrParse(File requestedFile) {
            try {
                String documentBody = Files.readString(requestedFile.toPath());
                new SmartScriptEngine(
                        new SmartScriptParser(documentBody).getDocumentNode(),
                        new RequestContext(ostream, params,tempParams, permPrams, outputCookies, this, SID)
                ).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class SessionMapEntry {
        String sid;
        String host;
        long validUntil;
        Map<String,String> map;

        public SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
            this.sid = sid;
            this.host = host;
            this.validUntil = validUntil;
            this.map = map;
        }


    }

}
