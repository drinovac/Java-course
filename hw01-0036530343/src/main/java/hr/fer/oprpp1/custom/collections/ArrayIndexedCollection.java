package hr.fer.oprpp1.custom.collections;


/**
 * This class represents collection implementation. It is implemented as array with specified length. It is used for storing Objects.
 */
public class ArrayIndexedCollection extends Collection {
	
	/**
	 * Current size of collection
	 */
	private int size;
	
	/**
	 * Object array in which elements are stored
	 */
	private Object[] elements;
	
	
	/**
	 * Constructor which initializes collection with given capacity and adds all elements of given collection to new collection.
	 *  
	 * @param size initial capatity
	 * @param elements Collection which elements will be added
	 */
	ArrayIndexedCollection(int size, Object[] elements) {
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

	@Override
	public void forEach(Processor processor) {
		for(int i = 0, h = this.elements.length; i < h; i++) {
			if(this.elements[i] != null) {
				processor.process(this.elements[i]);
			}
		}
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
	}
	
	
	
}
