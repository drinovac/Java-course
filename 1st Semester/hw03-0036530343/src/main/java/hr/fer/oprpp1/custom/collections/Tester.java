package hr.fer.oprpp1.custom.collections;

/**
 * This interface has one method which is used for testing.
 */
public interface Tester<T> {
    /**
     * This method checks if some object is acceptable or not.
     * @param t Object we want to check
     * @return True if object is acceptable, false otherwise
     */
    boolean test(T t);
}
