package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

public class ForLoopNode extends Node {

    private ElementVariable variable;
    private Element startExpression;
    private Element endExpression;
    private Element stepExpression;
    private ArrayIndexedCollection col;

    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }

    /**
     * @param child
     */
    @Override
    public void addChildnode(Node child) {
        if(col == null) {
            this.col = new ArrayIndexedCollection();
        }
        this.col.add(child);
    }

    /**
     * @return
     */
    @Override
    public int numberOfChildren() {
        return col.size();
    }

    /**
     * @param index
     * @return
     */
    @Override
    public Node getChild(int index) {
        return (Node) col.get(index);
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitForLoopNode(this);
    }
}
