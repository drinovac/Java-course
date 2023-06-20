package hr.fer.zemris.oprpp2.hw02.messages;

public class InMessage extends Message {
    private String name;
    private String text;

    public InMessage(long messageNumber, String name, String text) {
        super((byte) 5, messageNumber);
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return this.name;
    }

    public String getText() {
        return this.text;
    }

    public String toString() {
        return "INMESSAGE(" + getMessageNumber() +", name=\"" + getName() + "\", text=\"" + getText()+ "\")";
    }
}