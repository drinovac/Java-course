package hr.fer.zemris.oprpp2.hw02.messages;

public class OutMessage extends Message {
    private long uid;
    private String text;

    public OutMessage(long messageNumber, long uid, String text) {
        super((byte) 4, messageNumber);
        this.uid = uid;
        this.text = text;
    }

    public long getUid() {
        return this.uid;
    }

    public String getText() {
        return this.text;
    }

    public String toString() {
        return "OUTMESSAGE(" + getMessageNumber() + ", uid=" + getUid() +", text=\"" + getText() +"\")";
    }
}