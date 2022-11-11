package hr.fer.oprpp1.custom.scripting.lexer;

public class SmartScriptLexer {

    private char[] data;

    private SmartScriptToken token;

    private SmartScriptLexerState state;
    private int currentIndex;


    public SmartScriptLexer(String text) {
        this.data = text.toCharArray();

        this.currentIndex = 0;
        this.state = SmartScriptLexerState.TEXT;
    }

    public SmartScriptToken nextToken() {

        if(this.token != null && token.getType() == SmartScriptTokenType.EOF) {
            throw new SmartScriptLexerException();
        }

        if(data.length == currentIndex) {
            SmartScriptToken tok = new SmartScriptToken(SmartScriptTokenType.EOF, null);

            this.token = tok;
            currentIndex++;

            return tok;
        } else if (data.length - currentIndex == -1 || data[data.length -1] == '\\') {
            throw new SmartScriptLexerException();
        }


        SmartScriptTokenType type = null;
        Object value = null;
        String s = "";

        if(this.state == SmartScriptLexerState.TEXT) {


        } else {



        }

        return null;
    }

    public SmartScriptLexerState getState() {
        return state;
    }

    public void setState(SmartScriptLexerState state) {
        this.state = state;
    }
}
