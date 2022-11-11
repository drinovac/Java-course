package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerState;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptToken;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.Node;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;

public class SmartScriptParser {

    private DocumentNode documentNode;
    private ObjectStack stack;
    private SmartScriptLexer lexer;

    public SmartScriptParser(String docbody) {


        this.stack = new ObjectStack();
        this.lexer = new SmartScriptLexer(docbody);
        this.documentNode = new DocumentNode();
        this.stack.push(docbody);

        try {
            parseDocbody();
        } catch(SmartScriptParserException exc) {
            throw new SmartScriptParserException(exc.getMessage());
        }
    }

    private void parseDocbody() {

        while(true) {

        }
    }

    public DocumentNode getDocumentNode() {
        return documentNode;
    }
}
