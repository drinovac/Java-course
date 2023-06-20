package hr.fer.oprpp1.hw05.shell;

/**
 * This class represents exception that is thrown if there is error during I/O.
 */
public class ShellIOException extends RuntimeException {

    /**
     * Default constructor.
     */
    public ShellIOException() {
        super();
    }

    /**
     * Constructor with string argument.
     * @param msg
     */
    public ShellIOException(String msg) {
        super(msg);
    }
}
