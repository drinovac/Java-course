package hr.fer.zemris.java.custom.collections;


/**
 * This interface represents processor. It defines single method which is used for some action.
 */
public interface Processor {
	
	/**
	 * This method is used for doing some action on given object.
	 * 
	 * @param value Object at which we want to do action
	 */
	public void process(Object value);
	
}
