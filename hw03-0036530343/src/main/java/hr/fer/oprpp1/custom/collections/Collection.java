package hr.fer.oprpp1.custom.collections;


/**
 * This interface represents collection.
 */
public interface Collection<T>{
		
	/**
     * This method checks if collection is empty.
     *
     * @return true if empty, false otherwise.
     */
	default boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
     * This method returns size of collection.
     *
     * @return Size of collection
     */
	int size();
	
	/**
	 * This method adds elements to collection.
	 * 
	 * @param t Object that will be added to collection
	 */
	void add(T t);
	
	 /**
     * This method checks if collection contains given object.
     *
     * @param o Object that will be checked if it is in the collection
     * @return True if collection contains object, false otherwise
     */
	 boolean contains(Object o);
	
	/**
     * This method removes given object from collection.
     *
     * @param o Object to be removed
     * @return True if object is removed, false otherwise
     */
	boolean remove(Object o);
	
	/**
     * This method converts collection to Object array.
     *
     * @return Object array representation of collection
     */
	Object[] toArray();
	
	 /**
     * This method calls {@link Processor} for each element of collection.
     *
     * @param processor {@link Processor} that will be called
     */
	default void forEach(Processor<T> processor) {
		ElementsGetter<T> getter = this.createElementsGetter();

		while(getter.hasNextElement()){
			processor.process(getter.getNextElement());
		}
	}
	
	/**
     * This method adds all elements of given collection to this collection.
     *
     * @param other Collection which elements will be added.
     */
	default void addAll(Collection<T> other) {
		class LocalClass implements Processor<T> {

			public void process(Object value) {
				add((T) value);
			}

		}

		other.forEach(new LocalClass());
	}
	
	/**
     * This method is used for clearing all elements from collection.
     */
	void clear();
	
	/**
	 * This method is used for making instance of ElementsGetter class.
	 */
	ElementsGetter<T> createElementsGetter();

	/**
	 * This method adds all elements of another collection that pass tester tests
	 * @param col Collection which elements are checked
	 * @param tester Condition for selection
	 */
	default void addAllSatisfying(Collection<? extends T> col, Tester<T> tester) {
		ElementsGetter<? extends T> getter = col.createElementsGetter();

		while(getter.hasNextElement()) {
			T t = getter.getNextElement();
			if (tester.test(t)) {
				this.add(t);
			}

		}
	}
}
