package hr.fer.oprpp1.custom.collections;

/**
 * This interface has two methods and will be used for getting and printing elements from list.
 */
public interface ElementsGetter<T> {

    boolean hasNextElement();
    T getNextElement();

    /**
     * This method is doing some action on remaining elements of collection.
     * @param p Proccesor that define target action
     */
    default void processRemaining(Processor<T> p) {

    }

}
