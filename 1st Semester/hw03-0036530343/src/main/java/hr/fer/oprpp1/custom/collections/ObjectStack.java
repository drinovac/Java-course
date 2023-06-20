package hr.fer.oprpp1.custom.collections;


/**
 * This class represents object stack. It uses {@link ArrayIndexedCollection} for storing elements
 */
public class ObjectStack<T> {

	/**
	 * Collection in which elements will be stored.
	 */
	private ArrayIndexedCollection<T> array;
	
	/**
	 * Default constructor.
	 */
	public ObjectStack() {
		this.array = new ArrayIndexedCollection<T>();
	}
	
	/**
	 * This method checks if stack if empty
	 * 
	 * @return True if stack is empty, false otherwise
	 */
	public boolean isEmpty() {
		return this.array.isEmpty();
	}
	
	/**
	 * This method returns stack size.
	 * 
	 * @return Size of stack
	 */
	public int size() {
		return this.array.size();
	}
	
	/**
	 * This method adds element to stack.
	 * 
	 * @param value Element that will be added to stack
	 */
	public void push(T value) {
		this.array.add(value);
	}
	
	/**
	 * This method removes element from stack.
	 * 
	 * @return Element that is on top of stack
	 */
	public T pop() {
		if(this.size() == 0) {
			throw new EmptyStackException();
		}
		T peek = this.array.get(this.size() - 1);
		this.array.remove(this.size() - 1);
		return peek;
	}
	
	/**
	 * This method return element from top of the stack.
	 * 
	 * @return Element that is on top of stack
	 */
	public T peek() {
		if(this.size() == 0) {
			throw new EmptyStackException();
		}
		return this.array.get(this.size() - 1);
	}
	/**
	 * This method clears all elements from stack.
	 */
	public void clear() {
		this.array.clear();
	}
}
