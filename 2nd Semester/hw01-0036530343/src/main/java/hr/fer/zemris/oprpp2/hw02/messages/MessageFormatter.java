package hr.fer.zemris.oprpp2.hw02.messages;

import java.io.*;

@SuppressWarnings("DuplicatedCode")
public class MessageFormatter {

    public static Message toMessage(byte[] data, int offset, int length) {

        ByteArrayInputStream bis = new ByteArrayInputStream(data, offset, length);
        DataInputStream dis = new DataInputStream(bis);

        try {
            byte messageType = dis.readByte();
            return switch (messageType) {
                case 1 -> new HelloMessage(dis.readLong(), dis.readUTF(), dis.readLong());
                case 2 -> new AckMessage(dis.readLong(), dis.readLong());
                case 3 -> new ByeMessage(dis.readLong(), dis.readLong());
                case 4 -> new OutMessage(dis.readLong(), dis.readLong(), dis.readUTF());
                case 5 -> new InMessage(dis.readLong(), dis.readUTF(), dis.readUTF());
                default -> null;
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toByteArray(Message message) {
        return switch (message.getMessageKey()) {
            case 1 -> toByteHelloMessage((HelloMessage) message);
            case 2 -> toByteAckMessage((AckMessage) message);
            case 3 -> toByteByeMessage((ByeMessage) message);
            case 4 -> toByteOutMsgMessage((OutMessage) message);
            case 5 -> toByteInMsgMessage((InMessage) message);
            default -> null;
        };
    }

    private static byte[] toByteInMsgMessage(InMessage message) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(message.getMessageKey());
            dos.writeLong(message.getMessageNumber());
            dos.writeUTF(message.getName());
            dos.writeUTF(message.getText());
            dos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bos.toByteArray();
    }

    private static byte[] toByteOutMsgMessage(OutMessage message) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(message.getMessageKey());
            dos.writeLong(message.getMessageNumber());
            dos.writeLong(message.getUid());
            dos.writeUTF(message.getText());
            dos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bos.toByteArray();
    }


    private static byte[] toByteByeMessage(ByeMessage message) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(message.getMessageKey());
            dos.writeLong(message.getMessageNumber());
            dos.writeLong(message.getUid());
            dos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bos.toByteArray();
    }

    private static byte[] toByteAckMessage(AckMessage message) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(message.getMessageKey());
            dos.writeLong(message.getMessageNumber());
            dos.writeLong(message.getUid());
            dos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bos.toByteArray();
    }

    private static byte[] toByteHelloMessage(HelloMessage message) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(message.getMessageKey());
            dos.writeLong(message.getMessageNumber());
            dos.writeUTF(message.getName());
            dos.writeLong(message.getKey());
            dos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bos.toByteArray();
    }


}
