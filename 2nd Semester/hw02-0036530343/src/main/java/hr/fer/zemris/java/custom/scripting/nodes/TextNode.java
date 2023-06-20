package hr.fer.zemris.java.custom.scripting.nodes;

import java.io.IOException;

public class TextNode extends Node {

    private String text;

    public TextNode(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
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
            visitor.visitTextNode(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
