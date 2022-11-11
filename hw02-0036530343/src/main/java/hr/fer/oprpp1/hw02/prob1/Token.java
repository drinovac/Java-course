package hr.fer.oprpp1.hw02.prob1;

/**
 * This class represents token object
 */
public class Token {

    /**
     * Type of token.
     */
    private TokenType type;
    /**
     * Value of token.
     */
    private Object value;

    /**
     * Constructor that initilizes class attributes.
     * @param type Token type
     * @param value Value of token
     */
    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * This method returns token value.
     * @return Token value
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * This method returns this token type.
     * @return Token type
     */
    public TokenType getType() {
        return this.type;
    }
}
