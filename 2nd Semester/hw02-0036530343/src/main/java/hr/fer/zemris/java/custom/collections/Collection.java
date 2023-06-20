package hr.fer.zemris.java.custom.collections;


/**
 * This interface represents collection.
 */
public interface Collection{
		
	/**
     * This method checks if collection is empty.
     *
     * @return true if empty, false otherwise.
     */
	public default boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
     * This method returns size of collection.
     *
     * @return Size of collection
     */
	public int size();
	
	/**
	 * This method adds elements to collection.
	 * 
	 * @param value Object that will be added to collection
	 */
	public void add(Object value);
	
	 /**
     * This method checks if collection contains given object.
     *
     * @param value Object that will be checked if it is in the collection
     * @return True if collection contains object, false otherwise
     */
	public boolean contains(Object value);
	
	/**
     * This method removes given object from collection.
     *
     * @param value Object to be removed
     * @return True if object is removed, false otherwise
     */
	public boolean remove(Object value);
	
	/**
     * This method converts collection to Object array.
     *
     * @return Object array representation of collection
     */
	public Object[] toArray();
	
	 /**
     * This method calls {@link Processor} for each element of collection.
     *
     * @param processor {@link Processor} that will be called
     */
	default void forEach(Processor processor) {
		ElementsGetter getter = this.createElementsGetter();

		while(getter.hasNextElement()){
			processor.process(getter.getNextElement());
		}
	}
	
	/**
     * This method adds all elements of given collection to this collection.
     *
     * @param other Collection which elements will be added.
     */
	public default void addAll(Collection other) {
		class LocalClass implements Processor {

			public void process(Object value) {
				add(value);
			}

		}

		other.forEach(new LocalClass());
	}
	
	/**
     * This method is used for clearing all elements from collection.
     */
	public void clear();
	
	/**
	 * This method is used for making instance of ElementsGetter class.
	 */
	public ElementsGetter createElementsGetter();

	/**
	 * This method adds all elements of another collection that pass tester tests
	 * @param col Collection which elements are checked
	 * @param tester Condition for selection
	 */
	default void addAllSatisfying(Collection col, Tester tester) {
		ElementsGetter getter = col.createElementsGetter();

		Object[] localArray = new Object[col.size()+1];
		int index = 0;
		while(getter.hasNextElement()) {
			Object obj = getter.getNextElement();
			localArray[index++] = obj;
		}

		for(int i = 0, h = col.size(); i < h; i++) {
			if(tester.test(localArray[i])) {
				this.add(localArray[i]);
			}
		}

	}
}
