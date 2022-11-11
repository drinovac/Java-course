package hr.fer.oprpp1.custom.scripting.lexer;

public class SmartScriptToken {

    private SmartScriptTokenType type;

    private Object value;

    public SmartScriptToken(SmartScriptTokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public SmartScriptTokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
