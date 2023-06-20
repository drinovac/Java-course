package hr.fer.zemris.oprpp2.hw02.messages;

public class ByeMessage extends Message {
    private long uid;

    public ByeMessage(long messageNumber, long uid) {
        super((byte) 3, messageNumber);
        this.uid = uid;
    }

    public long getUid() {
        return this.uid;
    }

    public String toString() {
        return "BYE(" + getMessageNumber() + ", uid=" + getUid() + ")";
    }
}
