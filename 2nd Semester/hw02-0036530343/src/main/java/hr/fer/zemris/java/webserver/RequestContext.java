package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

public class RequestContext {

    private OutputStream outputStream;
    private Charset charset;

    public String encoding = "UTF-8";
    public int statusCode = 200;
    public String statusText = "OK";
    public String mimeType = "text/html";
    public Long contentLength;

    private Map<String, String> parameters;
    private Map<String, String> temporaryParameters;
    private Map<String, String> persistentParameters;
    private List<RCCookie> outputCookies;
    private boolean headerGenerated = false;
    public IDispatcher dispatcher;
    public String sessionID;

    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
        Objects.requireNonNull(outputStream);
        this.outputStream = outputStream;
        if(parameters != null) this.parameters = parameters;
        if(persistentParameters != null) this.persistentParameters = persistentParameters;
        if(outputCookies != null) this.outputCookies = outputCookies;
    }

    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> temporaryParameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies, IDispatcher dispatcher, String sessionID) {
        this(outputStream,parameters,persistentParameters,outputCookies);
        this.temporaryParameters = temporaryParameters;
        this.dispatcher = dispatcher;
        this.sessionID = sessionID;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public Set<String> getParameterNames() {
        return new HashSet<>(parameters.keySet());
    }

    public String getPersistentParameter(String name) {
        return persistentParameters.get(name);
    }

    public Set<String> getPersistentParameterNames() {
        return new HashSet<>(persistentParameters.keySet());
    }

    public void setPersistentParameter(String name, String value) {
        persistentParameters.put(name, value);
    }

    public void removePersistentParameter(String name) {
        persistentParameters.remove(name);
    }

    public String getTemporaryParameter(String name) {
        return temporaryParameters.getOrDefault(name, null);
    }

    public Set<String> getTemporaryParameterNames() {
        return new HashSet<>(temporaryParameters.keySet());
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setTemporaryParameter(String name, String value) {
        if(temporaryParameters == null) {
            this.temporaryParameters = new HashMap<>();
        }
        temporaryParameters.put(name, value);
    }

    public void removeTemporaryParameter(String name) {
        temporaryParameters.remove(name);
    }

    public void addRCCookie(RCCookie cookie) {
        outputCookies.add(cookie);
    }

    public RequestContext write(byte[] data) throws IOException {
        if(!headerGenerated) generateHeader();

        outputStream.write(data);
        return this;
    }

    public RequestContext write(byte[] data, int offset, int len) throws IOException {
        if(!headerGenerated) generateHeader();

        outputStream.write(data, offset, len);
        return this;
    }
    public RequestContext write(String text) throws IOException {
        if(!headerGenerated) generateHeader();

        outputStream.write(text.getBytes(encoding));
        return this;
    }

    public void setEncoding(String encoding) {
        if(headerGenerated) throw new RuntimeException();
        this.encoding = encoding;
    }

    public void setStatusCode(int statusCode) {
        if(headerGenerated) throw new RuntimeException();
        this.statusCode = statusCode;
    }

    public void setStatusText(String statusText) {
        if(headerGenerated) throw new RuntimeException();
        this.statusText = statusText;
    }

    public void setMimeType(String mimeType) {
        if(headerGenerated) throw new RuntimeException();
        this.mimeType = mimeType;
    }

    public void setContentLength(Long contentLength) {
        if(headerGenerated) throw new RuntimeException();
        this.contentLength = contentLength;
    }

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    private void generateHeader() throws IOException {
        this.charset = Charset.forName(encoding);

        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 ");
        sb.append(this.statusCode + " " + this.statusText);
        sb.append("\r\n");
        sb.append("Content-Type: " + this.mimeType);
        if(this.mimeType.startsWith("text/")) sb.append("; charset=" + encoding);
        sb.append("\r\n");
        if(contentLength != null) sb.append("Content-Length: " + contentLength + "\r\n");
        if(!outputCookies.isEmpty()) {
            for (RCCookie cookie: outputCookies) {
                sb.append("Set-Cookie: ");
                sb.append(cookie.getName() + "=" + cookie.getValue());
                if(cookie.getDomain() != null) sb.append("; Domain=" + cookie.getDomain());
                if(cookie.getPath() != null) sb.append("; Path=" + cookie.getPath());
                if(cookie.getPath() != null) sb.append("; Max-Age=" + cookie.getMaxAge());
                sb.append("\r\n");
            }
        }
        sb.append("\r\n");
        headerGenerated = true;
        outputStream.write(sb.toString().getBytes(encoding));

    }
    public static class RCCookie {
        String name;
        String value;
        String domain;
        String path;
        Integer maxAge;

        public RCCookie(String name, String value, String domain, String path, Integer maxAge) {
            this.name = name;
            this.value = value;
            this.domain = domain;
            this.path = path;
            this.maxAge = maxAge;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getDomain() {
            return domain;
        }

        public String getPath() {
            return path;
        }

        public Integer getMaxAge() {
            return maxAge;
        }
    }

}
