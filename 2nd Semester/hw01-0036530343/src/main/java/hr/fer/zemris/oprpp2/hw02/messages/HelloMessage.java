package hr.fer.zemris.oprpp2.hw02.messages;

public class HelloMessage extends Message {
    private String name;
    private long key;

    public HelloMessage(long messageNumber, String name, long key) {
        super((byte) 1, messageNumber);
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public long getKey() {
        return this.key;
    }

    public String toString() {
        return "HELLO(" + getMessageNumber() +", \"" + getName() +  "\", " +getKey() +")";
    }
}