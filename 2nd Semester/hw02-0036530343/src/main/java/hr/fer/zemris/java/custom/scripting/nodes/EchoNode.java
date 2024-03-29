package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

import java.io.IOException;

public class EchoNode extends Node {

    private Element[] elements;


    public EchoNode(Element[] elements) {
        this.elements = elements;
    }


    public Element[] getElements() {
        return elements;
    }

    /**
     * @param child
     */
    @Override
    public void addChildnode(Node child) {
        throw new RuntimeException();
    }

    /**
     * @return
     */
    @Override
    public int numberOfChildren() {
        return 0;
    }

    /**
     * @param index
     * @return
     */
    @Override
    public Node getChild(int index) {
        throw new RuntimeException();
    }

    @Override
    public void accept(INodeVisitor visitor) {
        try {
            visitor.visitEchoNode(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
