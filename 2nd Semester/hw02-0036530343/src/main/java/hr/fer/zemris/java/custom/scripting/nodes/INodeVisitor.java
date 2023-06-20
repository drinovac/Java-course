package hr.fer.zemris.java.custom.scripting.nodes;

import java.io.IOException;

public interface INodeVisitor {
    public void visitTextNode(TextNode node) throws IOException;
    public void visitForLoopNode(ForLoopNode node);
    public void visitEchoNode(EchoNode node) throws IOException;
    public void visitDocumentNode(DocumentNode node);

}
