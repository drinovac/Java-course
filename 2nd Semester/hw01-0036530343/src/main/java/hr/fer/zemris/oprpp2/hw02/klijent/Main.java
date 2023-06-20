package hr.fer.zemris.oprpp2.hw02.klijent;

import hr.fer.zemris.oprpp2.hw02.messages.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    private InetAddress ipAddress;
    private int port;
    private DatagramSocket socket;
    private long counter = 0L;
    private long uid;
    private String name;
    private BlockingQueue<Message> receivedQueue = new LinkedBlockingQueue<>();
    private JTextArea textArea;
    private JTextField textField;

    public Main(InetAddress ipAddress, int port, DatagramSocket socket, long uid, String name) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.socket = socket;
        this.uid = uid;
        this.name = name;
        initGUI();
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 3) {
            System.out.println("Očekivani parametri oblika: IP port imeKorisnika");
            return;
        }

        InetAddress inetAddress = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        if(port < 1 || port > 65535) {
            System.out.println("Port mora biti u rasponu 1 do 65535");
            return;
        }
        String username = args[2].trim();

        DatagramSocket datagramSocket = new DatagramSocket();
        HelloMessage helloMessage = new HelloMessage(0L, username, new Random().nextLong());


        int retransmissions = 0;
        while(retransmissions++ < 5) {
            byte[] byteMessage = MessageFormatter.toByteArray(helloMessage);
            DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length);
            packet.setSocketAddress(new InetSocketAddress(inetAddress, port));
            datagramSocket.send(packet);

            byte[] recvBuffer = new byte[2000];
            DatagramPacket recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);

            datagramSocket.setSoTimeout(5000);

            try {
                datagramSocket.receive(recvPacket);
            } catch (IOException e) {
                System.out.println("Timeout");
                continue;
            }

            Message message = null;

            try {
                message = MessageFormatter.toMessage(recvPacket.getData(), recvPacket.getOffset(), recvPacket.getLength());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            if(message.getMessageKey() == 2) {
                System.out.println("Uspjesna uspostava veze. Uid: " + ((AckMessage)message).getUid());
                Message finalMessage = message;
                SwingUtilities.invokeLater(() -> {
                    new Main(inetAddress, port, datagramSocket, ((AckMessage) finalMessage).getUid(), username);
                });
                break;
            }

        }
    }

    private void initGUI() {
        JFrame jFrame = new JFrame("Chat client: " + this.getName());
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             * @param e
             */
            @Override
            public void windowClosing(WindowEvent e) {
                long counter = getCounter();
                setCounter(++counter);
                ByeMessage message = new ByeMessage(getCounter(), getUid());
                byte[] byteMessage = MessageFormatter.toByteArray(message);
                DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length);
                packet.setSocketAddress(new InetSocketAddress(getIpAddress(), getPort()));

                try {
                    System.out.println("Šaljem: " + message);
                    socket.send(packet);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                socket.close();
                System.exit(1);
            }
        });
        jFrame.setSize(500,300);
        jFrame.setVisible(true);


        jFrame.getContentPane().setLayout(new BorderLayout());
        this.textField = new JTextField();
        this.textArea = new JTextArea();

        this.textArea.setEnabled(false);
        this.textArea.setDisabledTextColor(Color.BLACK);

        this.textField.addActionListener(e -> {
            String input = this.textField.getText().trim();
            this.textField.setText("");
            this.sendDatagram(input);
        });

        new Thread(() -> {
            try {
                this.workingThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        jFrame.getContentPane().add(this.textField, BorderLayout.NORTH);
        jFrame.getContentPane().add(new JScrollPane(this.textArea), BorderLayout.CENTER);
    }

    private void workingThread() throws InterruptedException {
        while (true) {
            byte[]  recvBuffer = new byte[2000];
            DatagramPacket recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);

            try {
                socket.receive(recvPacket);
            } catch (IOException e) {
                continue;
            }

            Message message = MessageFormatter.toMessage(recvPacket.getData(), recvPacket.getOffset(), recvPacket.getLength());

            if(message.getMessageKey() == 2) {
                System.out.println("Primam paket: " + message);
                this.receivedQueue.put(message);
            } else if(message.getMessageKey() == 5) {
                System.out.println("Primam paket " + message);
                InMessage inMessage = (InMessage) message;
                this.textArea.setEnabled(true);
                this.textArea.append("[" + recvPacket.getAddress() +":"+ recvPacket.getPort() + "] Poruka od korisnika: " + inMessage.getName() + "\n");
                this.textArea.append(inMessage.getText() + "\n\n");;
                this.textArea.setEnabled(false);

                AckMessage ackMessage = new AckMessage(message.getMessageNumber(), getUid());
                byte[] byteAckMessage = MessageFormatter.toByteArray(ackMessage);
                DatagramPacket ackPacket = new DatagramPacket(byteAckMessage, byteAckMessage.length);
                ackPacket.setSocketAddress(new InetSocketAddress(getIpAddress(), getPort()));
                System.out.println("Šaljem: " + ackMessage);

                try {
                    socket.send(ackPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void sendDatagram(String input) {

        OutMessage message = new OutMessage(getCounter(), getUid(), input);
        byte[] byteMessage = MessageFormatter.toByteArray(message);
        DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length);
        packet.setSocketAddress(new InetSocketAddress(getIpAddress(), getPort()));

        try {
            System.out.println("Šaljem: " + message);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public long getCounter() {
        return counter;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }
}
