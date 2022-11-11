package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

public abstract class Node {



    public abstract void addChildnode(Node child);

    public abstract int numberOfChildren();

    public abstract Node getChild(int index);
}
