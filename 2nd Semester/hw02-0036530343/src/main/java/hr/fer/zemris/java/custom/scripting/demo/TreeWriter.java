package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TreeWriter {

    public static void main(String[] args) throws IOException {

        /*String fileName = args[0];

        String docBody = Files.readString(Path.of(fileName));

        SmartScriptParser p = new SmartScriptParser(docBody);

        WriterVisitor visitor = new WriterVisitor();
        p.getDocumentNode().accept(visitor);*/

        System.out.println("\"a\"".endsWith("\""));
    }

    public static class WriterVisitor implements INodeVisitor {


        StringBuilder sb = new StringBuilder();

        @Override
        public void visitTextNode(TextNode node) {
            sb.append(node.getText());
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            sb.append("{$ FOR ");
            sb.append(node.getVariable().asText()).append(" ");
            sb.append(node.getStartExpression().asText()).append(" ");
            sb.append(node.getEndExpression().asText()).append(" ");
            if (node.getStepExpression() != null) {
                sb.append(node.getStepExpression().asText()).append(" ");
            }
            sb.append("$}");

            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
            sb.append("{$ END $}");
        }

        @Override
        public void visitEchoNode(EchoNode node) {

            sb.append("{$= ");

            Element[] echoElements = node.getElements();

            for(Element element: echoElements) {
                sb.append(element.asText()).append(" ");
            }

            sb.append("$}");
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
            System.out.println(sb.toString());
        }
    }

}
