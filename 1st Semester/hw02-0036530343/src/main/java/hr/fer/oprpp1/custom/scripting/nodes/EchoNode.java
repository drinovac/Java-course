package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

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
}
