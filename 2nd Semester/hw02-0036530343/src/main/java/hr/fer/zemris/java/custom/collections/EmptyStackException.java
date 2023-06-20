package hr.fer.zemris.java.custom.collections;


/**
 * This class represents empty stack exception. It is thrown when acessing element from empty stack.
 *
 */
public class EmptyStackException extends RuntimeException {
	
	/**
	 * Constructor with message which will be shown when exception is thrown.
	 * 
	 * @param msg Message that will be shown
	 */
	public EmptyStackException(String msg) {
		super(msg);
	}
	
	/**
	 * Basic constructor
	 */
	public EmptyStackException() {
		
	}
	
}
