package hr.fer.zemris.oprpp2.hw02.messages;

public abstract class Message {
    private byte messageKey;
    private long messageNumber;

    public Message(byte messageKey, long messageNumber) {
        this.messageKey = messageKey;
        this.messageNumber = messageNumber;
    }

    public byte getMessageKey() {
        return messageKey;
    }

    public long getMessageNumber() {
        return this.messageNumber;
    }
}
