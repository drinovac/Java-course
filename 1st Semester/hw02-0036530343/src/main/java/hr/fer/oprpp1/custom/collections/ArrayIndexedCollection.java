package hr.fer.oprpp1.custom.collections;


import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * This class represents collection implementation. It is implemented as array with specified length. It is used for storing Objects.
 */
public class ArrayIndexedCollection implements List {
	
	/**
	 * Current size of collection
	 */
	private int size;
	
	/**
	 * Object array in which elements are stored
	 */
	private Object[] elements;

	/**
	 * Number how many times collection has been changed
	 */
	private long modificationCount = 0;
	/**
	 * Constructor which initializes collection with given capacity and adds all elements of given collection to new collection.
	 *  
	 * @param size initial capatity
	 * @param elements Collection which elements will be added
	 */
	private ArrayIndexedCollection(int size, Object[] elements) {
		if(elements == null) {
			throw new NullPointerException();
		}
		if(size < elements.length) {
			this.size = elements.length;
		} else {
			this.size = size;
		}
		this.elements = elements;
	}
	
	/**
	 * Default constructor which initializes  collection with default capacity and calls another constructor.
	 */
	public ArrayIndexedCollection() {
		this(16);
	}
	
	
	/**
	 * Constructor which initializes collection with given capacity.
	 * 
	 * @param initialCapacity initial capacity
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		this.size = 0;
		this.elements = new Object[initialCapacity];
	}
	
	/**
	 * Constructor which initializes collection with given collection and calls another constructor.
	 * 
	 * @param collection given collection
	 */
	public ArrayIndexedCollection(Collection collection) {
		this(16, collection.toArray());
				
	}
	

	@Override
	public boolean isEmpty() {
		for(int i = 0, h = this.elements.length; i < h; i++) {
			if(this.elements[i] != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int size() {
		return this.size;
	}
	
	/**
	 * This method checks if collection contains given value.
	 * 
	 * @param value
	 * @return true if collection contains given value
	 */
	@Override
	public boolean contains(Object value) {

		for(int i = 0, h = this.elements.length; i < h; i++) {
			if(this.elements[i].equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method removes element with given value. Elements on bigger indexes are moved backward.
	 * 
	 * @param value
	 * @return true if collection contains given value
	 */
	@Override
	public boolean remove(Object value) {
		modificationCount++;
		for(int i = 0, h = this.elements.length; i < h; i++) {
			if(this.elements[i].equals(value)) {
				for(int j = i, k = this.elements.length; j < k-1; j++) {
					this.elements[j] = this.elements[j+1];
				}
				return true;
			}
		}
		return false;
	}


	@Override
	public Object[] toArray() {
		Object[] array = new Object[this.elements.length];
		for(int i = 0, h = this.size; i < h; i++) {
			array[i] = this.elements[i];
		}
		return array;
	}


	
	/**
	 * This method adds value to collection at the end.
	 * 
	 * @param value Element that will be added
	 */
	@Override
	public void add(Object value) {

		if(value == null) {
			throw new NullPointerException();
		}	
		try {
			this.elements[this.size] = value;
			this.size++;
			
		} catch(IndexOutOfBoundsException exc) {
			Object[] new_arr = new Object[this.elements.length * 2];
			for(int i = 0, h = this.elements.length; i < h; i++) {
				new_arr[i] = this.elements[i];
			}
			this.elements = new_arr;
			this.add(value);
		}
		modificationCount++;
	}
	
	/**
	 * This method gets element from collection with given index.
	 * 
	 * @param index Position of wanted element
	 * @return element on given position
	 */
	public Object get(int index) {
		if(index < 0 || index > this.size - 1) {
			throw new IllegalArgumentException();
		}
		return this.elements[index];
	}
	
	public void clear() {

		for(int i = 0, h = this.elements.length; i < h; i++) {
			this.elements[i] = null;
		}
		this.size = 0;

		modificationCount++;
	}
	
	
	/**
	 * This method inserts element into collection. Elements of bigger indexes are moved one position forward.
	 * 
	 * @param value Element that will be inserted
	 * @param position Index on which element will be inserted
	 */
	public void insert(Object value, int position) {

		//ovdje treba osigurat alociranje dvostruko veceg polja?
		
		if(position < 0 || position > this.size) {
			throw new IndexOutOfBoundsException();
		}
		
		if(position == this.size && this.elements.length == position) {
			Object[] new_arr = new Object[this.elements.length * 2];
			for(int i = 0, h = this.elements.length; i < h; i++) {
				new_arr[i] = this.elements[i];
			}
			this.elements = new_arr;
		}
		
		for(int i = this.elements.length - 2; i >= position; i--) {
			this.elements[i+1] = this.elements[i];
		}
		
		this.elements[position] = value;
		this.size += 1;

		modificationCount++;
	}
	
	/**
	 * This method is used for finding index of given value
	 * 
	 * @param value Element we are looking for
	 * @return Index of wanted element
	 */
	public int indexOf(Object value) {
		if(value == null) {
			return -1;
		}
		for(int i = 0, h = this.elements.length; i < h; i++) {
			if(this.elements[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * This method removes elements for collection on given index. Elements on bigger indexes are moved backward.
	 * 
	 * @param index Index of element that will be removed
	 */
	public void remove(int index) {
		if(index < 0 || index > this.size) {
			throw new IndexOutOfBoundsException();
		}
		
		for(int i = index, h = this.elements.length; i < h-1; i++) {
			this.elements[i] = this.elements[i+1];
		}
		this.elements[this.elements.length-1] = null;
		this.size -= 1;
		modificationCount++;
	}
	
	/**
	 * This local static class implements interface ElementsGetter which has two methods.
	 */
	private static class ElementsGetterClass implements ElementsGetter {
		/**
		 * Current index of collecton.
		 */
		private int ind = 0;
		/**
		 * Number how many times collection was changed before initializing ElementsGetterClass
		 */
		private final long savedModificationCount;
		/**
		 * Local instance of outer class object.
		 */
		ArrayIndexedCollection col;

		/**
		 * Constructor which initializes local instance of outer class and number of changes of collection.
		 * @param coll Reference to outer class object
		 * @param savedModificationCount Reference to outer class param "modificationCount"
		 */
		public ElementsGetterClass(ArrayIndexedCollection coll, long savedModificationCount) {
			col = coll;
			this.savedModificationCount = savedModificationCount;
		}

		/**
		 * This method checks if there is elements that are not printed yet.
		 * It throws ConcurrentModificationException if collection is changed after initializing ElementsGetter.
		 * @return True if collection has more elements, false otherwise
		 */
		@Override
		public boolean hasNextElement() {
			if(savedModificationCount != col.modificationCount) {
				throw new ConcurrentModificationException();
			}
			return col.elements[ind] != null;
		}

		/**
		 * This method gets next element from collection.
		 * It throws ConcurrentModificationException if collection is changed after initializing ElementsGetter.
		 * It throws NoSuchElementException if all elements are printed.
		 * @return Element that is nearest to last printed element
		 */
		@Override
		public Object getNextElement() {
			if(savedModificationCount != col.modificationCount) {
				throw new ConcurrentModificationException();
			}
			if(!hasNextElement()) {
				throw new NoSuchElementException();
			}
			return col.elements[ind++];
		}
		/**
		 * This method is doing some action on remaining elements of collection.
		 * @param p Proccesor that define target action
		 */
		@Override
		public void processRemaining(Processor p) {
			while(this.hasNextElement()) {
				p.process(col.elements[ind++]);
			}
		}
	}

	/**
	 * This method creates instance of ElemementsGetter class.
	 * @return ElementsGetter with two arguments. First is reference to this collection, second is number of collection changes
	 */
	@Override
	public ElementsGetterClass createElementsGetter() {
		// TODO Auto-generated method stub
		return new ElementsGetterClass(this, this.modificationCount);
	}
	
	
	
}
