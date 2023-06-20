package hr.fer.zemris.java.custom.scripting.nodes;

public abstract class Node {

    public abstract void addChildnode(Node child);

    public abstract int numberOfChildren();

    public abstract Node getChild(int index);

    public abstract void accept(INodeVisitor visitor);

}
