package hr.fer.oprpp1.custom.collections;

/**
 * This interface extends original interface Collection and adds new methods.
 */
public interface List<T> extends Collection<T> {
    /**
     * This method gets elements on specified index.
     *
     * @param index Index of element are getting
     * @return Element of collection with that index
     */
    T get(int index);

    /**
     * This method inserts element on specified position.
     * @param value Value of element which is inserted
     * @param position Index of place where element is inserted
     */
    void insert(T value, int position);

    /**
     * This method returns index of specified element
     *
     * @param t Element we are looking for
     * @return Index of element we are looking for
     */
    int indexOf(T t);

    /**
     * This method removes element from list.
     * @param index Index of element which is removed
     */
    void remove(int index);
}
