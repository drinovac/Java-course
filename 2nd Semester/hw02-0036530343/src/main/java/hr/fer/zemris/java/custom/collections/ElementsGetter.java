package hr.fer.zemris.java.custom.collections;

/**
 * This interface has two methods and will be used for getting and printing elements from list.
 */
public interface ElementsGetter {

    public boolean hasNextElement();
    public Object getNextElement();

    /**
     * This method is doing some action on remaining elements of collection.
     * @param p Proccesor that define target action
     */
    default void processRemaining(Processor p) {

    }

}
