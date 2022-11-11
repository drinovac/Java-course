package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * This class represents dictionary that stores pairs of elements.
 * Elements are stored as key -> value.
 * @param <K> type of key
 * @param <V> type of mapped values
 */
public class Dictionary<K,V> {

    /**
     * Array where dictionary elements are stored.
     */
    private ArrayIndexedCollection<Pair<K,V>> array;

    /**
     * Default constructor that initilazies array.
     */
    public Dictionary() {
        this.array = new ArrayIndexedCollection<>();
    }

    /**
     * This method checks if dictionary is empty.
     * @return true if dictionary is empty, false otherwise
     */
    public boolean isEmpty() {
        return array.isEmpty();
    }

    /**
     * This method returns size of dictionary
     * @return size of dictionary
     */
    public int size() {
        return array.size();
    }

    /**
     * This method clears dictionary.
     */
    public void clear() {
        array.clear();
    }

    /**
     * This method puts elements into dictionary.
     * If dictionary contains element with same key, overwrites value with new one.
     * @param key Key value
     * @param value Value
     * @return Overwrited value, null otherwise
     */
    public V put(K key, V value) {
        Objects.requireNonNull(key);

        Pair<K,V> new_par = new Pair<>(key, value);

        if(this.get(key) != null) {
            //System.out.println(this.get(key));
            // promijenit
            V val = this.get(key);
            this.remove(key);
            array.add(new_par);
            return val;
        } else {
            array.add(new_par);
            return null;
        }

    }

    /**
     * This method gets value with given key.
     * @param key Key to value
     * @return Value of key if key exists, null otherwise
     */
    public V get(Object key) {
        Objects.requireNonNull(key);

        Object[] pairs =  this.array.toArray();

        if(this.size() == 0) {
            return null;
        }

        for(Object par : pairs) {
            if(((Pair<K,V>) par).getKey().equals(key)) {
                return ((Pair<K,V>)par).getValue();
            }
        }


        return null;
    }

    /**
     * This method removes element from dictionary.
     *
     * @param key Key to value
     * @return Value of key if key exists, null otherwise
     */
    public V remove(K key) {
        Objects.requireNonNull(key);

        Object[] pairs = this.array.toArray();

        for(Object par : pairs) {
            if(((Pair<K,V>)par).getKey().equals(key)) {
                V value = ((Pair<K,V>)par).getValue();
                array.remove(par);
                return value;
            }
        }
        return null;
    }

    /**
     * This class represents pair elements that are stored in dictionary
     * @param <K> key type
     * @param <V> value type
     */
    private class Pair<K, V> {
        /**
         * Key value
         */
        private K key;
        /**
         * Value value
         */
        private V value;

        /**
         * Constructor
         * @param key Key value
         * @param value Value value
         */
        public Pair(K key, V value) {
            Objects.requireNonNull(key, "Kljuc ne smije biti null");

            this.key = key;
            this.value = value;
        }

        /**
         * This method returns key value.
         * @return Value of key
         */
        public K getKey() {
            return key;
        }

        /**
         * This method gets value.
         * @return Value of value
         */
        public V getValue() {
            return value;
        }

        /**
         * This method sets value of value.
         * @param value new value
         */
        public void setValue(V value) {
            this.value = value;
        }
    }

}
