package hr.fer.zemris.java.gui.layout;

/**
 * This class represents exception for out CalcLayout
 */
public class CalcLayoutException extends RuntimeException {

    /**
     * Default constructor.
     */
    public CalcLayoutException(){
        super();
    }

    /**
     * Constructor.
     * @param msg
     */
    public CalcLayoutException(String msg){
        super(msg);
    }
}
