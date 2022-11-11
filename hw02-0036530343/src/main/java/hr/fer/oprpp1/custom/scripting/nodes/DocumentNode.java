package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

    public class DocumentNode extends Node {

    private ArrayIndexedCollection col;

    @Override
    public void addChildnode(Node child){

        if(col == null) {
            this.col = new ArrayIndexedCollection();
        }

        col.add(child);
    }
    @Override
    public int numberOfChildren() {
        return this.col.size();
    }
    @Override
    public Node getChild(int index) {
        return (Node) this.col.get(index);
    }

}
