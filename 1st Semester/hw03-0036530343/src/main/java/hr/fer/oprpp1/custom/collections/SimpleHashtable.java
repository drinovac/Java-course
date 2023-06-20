package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class represents simple hash table.
 * @param <K> type of keys
 * @param <V> type of values
 */
public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K,V>> {

    /**
     * Table for elements storage.
     */
    private TableEntry<K,V>[] table;
    /**
     * Number of elements
     */
    private int size;

    private int modificationCount;
    /**
     * Constructor that initilazies table size to 16.
     */
    public SimpleHashtable() {

        this.table = (TableEntry<K, V>[]) new TableEntry[16];
        this.size = 0;
    }

    /**
     * Constructor that initilazies table size to first number 2^i > capacity.
     * @param capacity number between 2^(n-1) and 2^n
     */
    public SimpleHashtable(int capacity) {
        if(capacity < 1) {
            throw new IllegalArgumentException();
        }
        int i = 1;
        while(i < capacity) {
            i *= 2;
        }
        this.table = (TableEntry<K, V>[]) new TableEntry[i];
        this.size = 0;
    }

    /**
     * This method puts element to table
     * @param key Key value of new table element
     * @param value Value value of new table element
     * @return null if element with that key already exists in table, old value otherwise
     */
    public V put(K key, V value) {

        Objects.requireNonNull(key);

        int place = Math.abs(key.hashCode()) % table.length;

        if(!this.containsKey(key)) {

            TableEntry<K,V> new_entry = new TableEntry<>(key, value, null);

            if(table[place] == null) {
                table[place] = new_entry;

            } else {
                TableEntry<K,V> help = table[place];
                while(help.next != null) {
                    help = help.next;
                }
                help.next = new_entry;
            }
            this.size++;
            this.modificationCount++;

            if(((double)size / table.length) >= 0.75) {

                povecajTable();
            }

            return null;

        } else {
            /**
             * Case if element with given key exists in table
             */
            TableEntry<K,V>  entry = table[place];

            while(entry != null) {

                if(entry.getKey().equals(key)) {
                    V old_value = entry.getValue();
                    entry.setValue(value);

                    return old_value;
                }
                entry = entry.next;
            }


        }

        return null;
    }



    /**
     * This method gets element from table.
     * @param key Element's key
     * @return if element with given key exists returns Value, else null
     */
    public V get(Object key) {

        if(key == null) {
            return null;
        }

        int hash =  Math.abs(key.hashCode()) % table.length;

        TableEntry<K,V>  entry = table[hash];

        while(entry != null) {

            if(entry.getKey().equals(key)) {
                return entry.getValue();
            }

            entry = entry.next;
        }
        return null;
    }

    /**
     * This method returns table size
     * @return size of table
     */
    public int size() {
        return this.size;
    }

    /**
     * This method checks if table contains given key.
     * @param key Value of key
     * @return true if contains, false otherwise
     */
    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    /**
     * This method checks if table contains given value.
     * @param value Value of value
     * @return true if contains, false otherwise
     */
    public boolean containsValue(Object value) {
        TableEntry<K,V>[] array = this.toArray();

        for(TableEntry<K,V>  entry : array) {
            if(entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method removes element from table.
     * @param key Element's key
     * @return if element with given key exists returns Value, else null
     */
    public V remove(Object key) {

        if(!this.containsKey(key)) {
            return null;
        } else {
            this.size--;
            this.modificationCount++;

            int place =  Math.abs(key.hashCode()) % table.length;

            if(table[place].getKey().equals(key)) {
                V old_value = table[place].getValue();
                table[place] = table[place].next;
                return old_value;
            }

            TableEntry<K,V>  entry = table[place];

            while(entry.next != null) {

                if(entry.next.getKey().equals(key)) {
                    V old_value = entry.next.getValue();
                    entry.next = entry.next.next;
                    return old_value;
                } else{
                    entry = entry.next;
                }

            }
        }
        return null;
    }

    /**
     * This method checks if table is empty.
     * @return True if it is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * This method returns string with all table elements. [key1=value1, key2=value2]
     * @return String with all table elements
     */
    public String toString() {

        StringBuilder s = new StringBuilder("[");

        TableEntry<K,V>[] array = this.toArray();

        for(TableEntry<K,V>  entry : array) {
            s.append(entry.toString() + ", ");

        }
        s = new StringBuilder(s.substring(0, s.length() - 2));
        s.append("]");

        return s.toString();
    }

    /**
     * This method makes array of table.
     * @return TableEntry<K,V>[] array
     */
    public TableEntry<K,V>[] toArray() {
        TableEntry<K,V>[] array = (TableEntry<K,V>[]) new TableEntry[this.size];
        int ind = 0;
        for (TableEntry<K, V> kvTableEntry : table) {
            if (kvTableEntry != null) {

                array[ind++] = kvTableEntry;
                TableEntry<K, V> help = kvTableEntry;

                while (help.next != null) {
                    help = help.next;
                    array[ind++] = help;
                }
            }
        }
        return array;
    }

    /**
     * This method removes all elements from table.
     */
    public void clear() {
        TableEntry<K,V>[] array = this.toArray();

        for(TableEntry<K,V>  entry : array) {
            this.remove(entry.getKey());
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl();
    }

    /**
     * This class represents HashTable elements
     * @param <K> key type
     * @param <V> value type
     */
    public static class TableEntry<K,V> {
        /**
         * Key value
         */
        private K key;
        /**
         * Value value
         */
        private V value;
        /**
         * Reference to next TableEntry
         */
        private TableEntry<K,V> next;

        /**
         * Contructor that initilizes new TableEntry
         * @param key Key value
         * @param value Value value
         * @param next Reference to next element
         */
        public TableEntry(K key, V value, TableEntry<K, V> next) {
            Objects.requireNonNull(key);

            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * This method gets key of element
         * @return Key value
         */
        public K getKey() {
            return key;
        }

        /**
         * This method gets value of element
         * @return Value of value
         */
        public V getValue() {
            return value;
        }

        /**
         * This method sets new value od element
         * @param value new value
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Returns a string representation of the object.
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    /**
     * This method is used for making table with double size of original.
     * Copies all elements to new table.
     */
    private void povecajTable() {
        TableEntry<K,V>[] array = this.toArray();

        this.table = (TableEntry<K, V>[]) new TableEntry[table.length  * 2];

        for(TableEntry<K,V> entry: array) {
            this.put(entry.getKey(), entry.getValue());
        }

    }

    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {

        /**
         * Indicator if table is changed after iterator was initilazed
         */
        private int iteratorModificationCount = SimpleHashtable.this.modificationCount;

        /**
         * Current index in table
         */
        private int currIndex = 0;

        /**
         * Current index in linked list of each table slot
         */
        private int currentIndexInArray = 0;

        /**
         * Last returned element from table
         */
        private TableEntry<K,V> lastReturned;

        /**
         * Indicator if current element is already removed
         */
        private boolean removedCurrElem = false;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {

            if(iteratorModificationCount != modificationCount){
                throw new ConcurrentModificationException("Kolekcija je modificirana za vrijeme iteriranja");
            }

            return currIndex < table.length || lastReturned.next != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public TableEntry<K, V> next() {
            if(iteratorModificationCount != modificationCount) {
                throw new ConcurrentModificationException("Kolekcija je modificirana za vrijeme iteriranja");
            }

            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            removedCurrElem = false;

            if(currentIndexInArray != 0) {

                currentIndexInArray++;
                lastReturned = lastReturned.next;

            } else {
                while(table[currIndex] == null) {
                    currIndex++;
                }

                if(table[currIndex].next != null) {
                    currentIndexInArray = 1;
                }

                lastReturned = table[currIndex++];

            }
            return lastReturned;

        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.
         * <p>
         * The behavior of an iterator is unspecified if the underlying collection
         * is modified while the iteration is in progress in any way other than by
         * calling this method, unless an overriding class has specified a
         * concurrent modification policy.
         * <p>
         * The behavior of an iterator is unspecified if this method is called
         * after a call to the {@link #forEachRemaining forEachRemaining} method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         * @implSpec The default implementation throws an instance of
         * {@link UnsupportedOperationException} and performs no other action.
         */
        @Override
        public void remove() {

            if(removedCurrElem) {
                throw new IllegalStateException("Nije moguce ukloniti isti element dvaput.");
            }

            SimpleHashtable.this.remove(lastReturned.getKey());
            removedCurrElem = true;
            iteratorModificationCount++;

        }
    }
}
