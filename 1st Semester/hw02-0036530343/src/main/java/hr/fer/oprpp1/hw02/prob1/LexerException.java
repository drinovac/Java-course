package hr.fer.oprpp1.hw02.prob1;

/**
 * This class represents exception that extends RuntimeException.
 */
public class LexerException extends RuntimeException {

    /**
     * Default constructor.
     */
    public LexerException() {
        super();
    }

    /**
     * Constructor that calls super constructor.
     * @param msg String that is sent to super constructor
     */
    public LexerException(String msg) {
        super(msg);
    }
}
