package hr.fer.zemris.oprpp2.hw02.messages;

public class AckMessage extends Message {
    private long uid;

    public AckMessage(long messageNumber, long uid) {
        super((byte) 2, messageNumber);
        this.uid = uid;
    }

    public long getUid() {
        return this.uid;
    }

    public String toString() {
        return "ACK(" + getMessageNumber() + ", uid=" + getUid() + ")";
    }
}
