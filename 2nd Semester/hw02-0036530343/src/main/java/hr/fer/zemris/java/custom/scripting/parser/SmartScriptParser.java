package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;

import java.util.Arrays;
import java.util.Objects;

public class SmartScriptParser {

    private DocumentNode documentNode;
    private ObjectStack stack;
    private SmartScriptLexer lexer;

    public SmartScriptParser(String docbody) {
        Objects.requireNonNull(docbody, "Text ne smije biti null");

        this.stack = new ObjectStack();
        this.lexer = new SmartScriptLexer(docbody);
        this.documentNode = new DocumentNode();
        this.stack.push(documentNode);

        try {
            parseDocbody();
        } catch(SmartScriptParserException exc) {
            throw new SmartScriptParserException(exc.getMessage());
        }
    }

    private void parseDocbody() {

        while(true) {
            if (lexer.getState() == SmartScriptLexerState.TEXT) {

                SmartScriptToken nextToken = lexer.nextToken();

                if (nextToken.getType() == SmartScriptTokenType.EOF) {
                    break;
                } else if (nextToken.getType() == SmartScriptTokenType.TEXT) {
                    TextNode textNode = new TextNode((String) nextToken.getValue());
                    Node stackNode = (Node) stack.peek();
                    stackNode.addChildnode(textNode);
                } else if (nextToken.getType() == SmartScriptTokenType.BEGINTAG) {
                    lexer.setState(SmartScriptLexerState.TAG);
                } else {
                    throw new SmartScriptParserException();
                }

            } else if (lexer.getState() == SmartScriptLexerState.TAG) {
                parseTag();
            } else {
                throw new SmartScriptParserException();
            }
        }

        if (stack.size() != 1) {
            throw new SmartScriptParserException();
        }
    }

    private void parseTag() {

        SmartScriptToken nextToken = lexer.nextToken();

        if (nextToken.getType() == SmartScriptTokenType.TEXT) {
            String name = (String) nextToken.getValue();

            if (name.equals("FOR")) {
                parseFor();
            } else if (name.equals("=")) {
                parseEcho();
            } else if (name.equals("END")) {
                SmartScriptToken endToken = lexer.nextToken();
                if (endToken.getType() == SmartScriptTokenType.ENDTAG) {
                    lexer.setState(SmartScriptLexerState.TEXT);
                    stack.pop();
                } else {
                    throw new SmartScriptLexerException();
                }
            }
        } else {
            throw new SmartScriptParserException("Nije zadano ime taga!");
        }

        lexer.setState(SmartScriptLexerState.TEXT);
    }

    private void parseFor() {

        SmartScriptToken variableToken = lexer.nextToken();
        if (variableToken.getType() != SmartScriptTokenType.TEXT) {
            throw new SmartScriptParserException();
        }
        String variableName = (String) variableToken.getValue();

        if (!Character.isLetter(variableName.charAt(0))) {
            throw new SmartScriptParserException();
        }

        ElementVariable variable = new ElementVariable(variableName);
        Element[] forElements = new Element[3];

        for (int i = 0; i < 4; i++) {
            SmartScriptToken token = lexer.nextToken();

            if (i == 3 && token.getType() != SmartScriptTokenType.ENDTAG) {
                throw new SmartScriptParserException();
            }
            if (token.getType() == SmartScriptTokenType.ENDTAG) {
                if(i > 1) {
                    break;
                } else {
                    throw new SmartScriptParserException();
                }
            } else {
                forElements[i] = parseElement(token);
            }

        }

        ForLoopNode forLoopNode = new ForLoopNode(variable, forElements[0], forElements[1], forElements[2]);

        Node stackNode = (Node) stack.peek();
        stackNode.addChildnode(forLoopNode);
        stack.push(forLoopNode);

    }

    private void parseEcho() {
        Collection elements = new ArrayIndexedCollection();

        // Parse elements until ENDTAG is occurred
        while (true) {
            SmartScriptToken token = lexer.nextToken();

            if (token.getType() == SmartScriptTokenType.ENDTAG) {
                if (elements.size() == 0) {
                    throw new SmartScriptParserException("Echo tag ne može " +
                            "biti prazan!");
                } else {
                    break;
                }
            }
            elements.add(parseElement(token));
        }

        // Create echo node and add it to stack
        Element[] echoElements = Arrays.copyOf(elements.toArray(), elements
                .size(), Element[].class);
        EchoNode echoNode = new EchoNode(echoElements);
        Node stackNode = (Node) stack.peek();
        stackNode.addChildnode(echoNode);

    }


    private Element parseElement(SmartScriptToken token) {
        if (token.getType() == SmartScriptTokenType.INTEGER) {
            int number = (int) token.getValue();
            return new ElementConstantInteger(number);
        } else if (token.getType() == SmartScriptTokenType.DOUBLE) {
            double number = (double) token.getValue();
            return new ElementConstantDouble(number);
        } else if (token.getType() == SmartScriptTokenType.TEXT) {
            String text = (String) token.getValue();

            if (text.equals("+") || text.equals("-") || text.equals("*") ||
                    text.equals("/") || text.equals("^")) {
                return new ElementOperator(text);
            } else if (text.startsWith("@")) {
                if (text.length() > 1 && Character.isLetter(text.charAt(1))) {
                    return new ElementFunction(text);
                }
            } else if (text.startsWith("\"")) {
                return new ElementString(text);
            } else {
                if (!Character.isLetter(text.charAt(0))) {
                    throw new SmartScriptParserException("Varijabla krivo " +
                            "započinje!");
                }

                return new ElementVariable(text);
            }
        }

        throw new SmartScriptParserException("Token nije dobrog tipa!");

    }

    public DocumentNode getDocumentNode() {
        return documentNode;
    }
}
