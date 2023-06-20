package hr.fer.zemris.oprpp2.hw02.server;

import hr.fer.zemris.oprpp2.hw02.messages.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Main {


    public static void main(String[] args) {

        if(args.length == 0) {
            System.out.println("Morate zadati port na kojem će poslužitelj slušati!");
        }

        int port = Integer.parseInt(args[0]);

        if(port < 1 || port > 65535) {
            throw new RuntimeException("Port mora biti u intervalu 1 do 65535!");
        }

        Main main = new Main();
        main.setPort(port);
        main.serve();

    }

    private int port;
    private Random random = new Random();
    private AtomicLong nextUid;
    private List<Connection> connections;
    private ExecutorService pool;
    private DatagramSocket serverSocket;

    public Main() {
        this.nextUid = new AtomicLong(this.random.nextInt());
        this.connections = new ArrayList<>();
        this.pool = Executors.newFixedThreadPool(2);
    }

    private void serve() {
        try {
            this.serverSocket = new DatagramSocket(this.port);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Poslužitelj je pokrenut na portu: " + this.port);

        while(true) {
            DatagramPacket datagramPacket;

            while(true) {
                byte[] buff = new byte[1024];
                datagramPacket = new DatagramPacket(buff, buff.length);

                try {
                    this.serverSocket.receive(datagramPacket);
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Primljen paket od: " + datagramPacket.getSocketAddress());

            Message message = MessageFormatter.toMessage(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());

            System.out.println("Paket je: " + message);

            try {
                switch (message.getMessageKey()) {
                    case 1 -> this.handleHelloMessage(datagramPacket, (HelloMessage) message);
                    case 2 -> this.handleAckMessage(datagramPacket, (AckMessage) message);
                    case 3 -> this.handleByeMessage(datagramPacket, (ByeMessage) message);
                    case 4 -> this.handleOutMessage(datagramPacket, (OutMessage) message);
                    case 5 -> this.handleInMessage(datagramPacket, (InMessage) message);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private Connection findConnectionByKey(long key) {
        for(Connection connection: this.connections) {
            if(connection.getKey() == key) {
                return connection;
            }
        }

        return null;
    }

    private Connection findConnectionByUID(long uid) {
        for(Connection connection: this.connections) {
            if(connection.getUid() == uid) {
                return connection;
            }
        }

        return null;
    }

    private void handleHelloMessage(DatagramPacket datagramPacket, HelloMessage message) throws IOException {

        Connection connection = this.findConnectionByKey(message.getKey());

        if (connection == null) {
            connection = new Connection(0L, message.getKey(), this.nextUid.incrementAndGet(), message.getName(),
                    datagramPacket.getAddress(), datagramPacket.getPort());

            this.connections.add(connection);
            this.pool.submit(new Posao(connection));

        }

        sendAck(message, connection);

    }

    private void handleByeMessage(DatagramPacket datagramPacket, ByeMessage message) throws IOException {

        Connection connection = this.findConnectionByUID(message.getUid());
        if (connection == null) {
            return;
        }

        ++connection.counter;
        this.connections.remove(connection);

        sendAck(message, connection);
    }

    private void handleAckMessage(DatagramPacket datagramPacket, AckMessage message) {
        Connection connection = this.findConnectionByUID(message.getUid());
            if (connection == null) {
                return;
            }

        connection.receivedQueue.add(message);
    }

    private void handleInMessage(DatagramPacket datagramPacket, InMessage message) {
        System.out.println("Nije moguce da klijent posalje InMessage");
    }

    private void handleOutMessage(DatagramPacket datagramPacket, OutMessage message) throws IOException {
        Connection connection = findConnectionByUID(message.getUid());
        if(connection == null) {
            return;
        }

        if (!connection.wrong) {
            for(Connection currentConnection: this.connections) {
                try {
                    currentConnection.outgoingQueue.put(new InMessage(++currentConnection.counter, connection.getName(), message.getText()));
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }

            sendAck(message, connection);

        }
    }

    private void sendAck(Message message, Connection connection) throws IOException {
        AckMessage ackMessage = new AckMessage(message.getMessageNumber(), connection.uid);
        byte[] byteMessage = MessageFormatter.toByteArray(ackMessage);
        DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length);
        packet.setAddress(connection.address);
        packet.setPort(connection.port);
        this.serverSocket.send(packet);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    class Posao implements Runnable {

        public Connection connection;

        public Posao(Connection connection) {
            this.connection = connection;
        }


        /**
         * Runs this operation.
         */
        @Override
        public void run() {
            while(true) {
                Message message = null;
                try {
                    message = connection.outgoingQueue.take();
                } catch (InterruptedException e) {
                    continue;
                }

                byte[] byteMessage = MessageFormatter.toByteArray(message);
                DatagramPacket datagramPacket = new DatagramPacket(byteMessage, byteMessage.length);
                datagramPacket.setAddress(connection.getAddress());
                datagramPacket.setPort(connection.getPort());

                int retransmissions = 0;
                while(retransmissions++ < 10) {

                    try {
                        System.out.println("Šaljem: " + message);
                        Main.this.serverSocket.send(datagramPacket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Message receivedMessage;

                    try {
                        receivedMessage = connection.receivedQueue.poll(5L, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (receivedMessage != null) {
                        if (receivedMessage.getMessageKey() == 2 && receivedMessage.getMessageNumber() == message.getMessageNumber()) {
                            break;
                        }
                        connection.wrong = true;
                        System.out.println("Različiti broj potvrde.");
                    }
                }

                if(connection.wrong) {
                    Main.this.connections.remove(connection);
                }
            }
        }
    }

    private static class Connection {
        private boolean wrong = false;
        private long counter;
        private long key;
        private long uid;
        private String name;
        private InetAddress address;
        private int port;
        private BlockingQueue<Message> receivedQueue = new LinkedBlockingQueue<>();
        private BlockingQueue<Message> outgoingQueue = new LinkedBlockingQueue<>();

        public Connection(long counter, long key, long uid, String name, InetAddress address, int port) {
            this.counter = counter;
            this.key = key;
            this.uid = uid;
            this.name = name;
            this.address = address;
            this.port = port;
        }


        public long getKey() {
            return key;
        }

        public InetAddress getAddress() {
            return address;
        }

        public long getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public int getPort() {
            return port;
        }

    }
}
