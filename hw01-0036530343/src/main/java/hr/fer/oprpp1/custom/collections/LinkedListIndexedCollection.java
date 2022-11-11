package hr.fer.oprpp1.custom.collections;


/**
 * This class represents linked list indexed collection. It is implemented with linked list.
 *
 */
public class LinkedListIndexedCollection extends Collection {
	
	/**
	 * Current list size
	 */
	private int size;
	
	/**
	 * First node of collection.
	 */
	private ListNode first;
	
	/**
	 * Last node of collection.
	 */
	private ListNode last;
	
	/**
	 * Default constructor
	 */
	public LinkedListIndexedCollection() {
		this.size = 0;
		this.first = null;
		this.last = null;
	}
	
	/**
	 * Constructor which initilazes collection and adds all elements of given collection to new collection.
	 * 
	 * @param collection Collection which elements will be added
	 */
	public LinkedListIndexedCollection(Collection collection) {
		if(collection instanceof ArrayIndexedCollection) {
			ArrayIndexedCollection array = (ArrayIndexedCollection) collection;
			for(int i = 0, h = collection.size(); i < h; i++) {
				this.add(array.toArray()[i]);
			}
		} else if(collection instanceof LinkedListIndexedCollection) {
			LinkedListIndexedCollection array = (LinkedListIndexedCollection) collection;
			for(ListNode left = array.first; left != null; left = left.next) {
				this.add(left.value);
			}
		}
	}
	
	/**
	 * This class represents list node. It is also list element.
	 */
	private static class ListNode {
		
		/**
		 * Previous list node.
		 */
		private ListNode previous;
		
		/**
		 * Next list node
		 */
		private ListNode next;
		/**
		 * Value of a node.
		 */
		private Object value;
		
		public ListNode(ListNode previous, ListNode next, Object value) {
			this.previous = previous;
			this.next = next;
			this.value = value;
		}
	}
	
	
	@Override
	public boolean isEmpty() {
		if(this.first == null && this.last == null) {
			return true;
		}
		return false;
		
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean contains(Object value) {
		
		for(ListNode current = this.first; current != this.last; current = current.next) {
			if(current.value.equals(value)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[this.size()];
		int cnt = 0;
		for(ListNode current = this.first; current != null; current = current.next) {
			array[cnt++] = current.value;
			
		}
		return array;
	}

	@Override
	public void forEach(Processor processor) {
		for(ListNode left = this.first; left != null; left = left.next) {
			processor.process(left.value);

}
	}

	@Override
	public void addAll(Collection other) {
		
		class LocalClass extends Processor{
			
			public void process(Object value) {
				add(value);
			}
			
		}
		
		other.forEach(new LocalClass());
		
	}


	@Override
	public void add(Object value) {
		
		if(value == null) {
			throw new NullPointerException();
		}
				
		if(this.first == null && this.last == null) {
			ListNode new_node = new ListNode(null, null, value);
			this.first = new_node;
			this.last = new_node;
			this.size = 1;
		} else {
			ListNode new_node = new ListNode(this.last, null, value);
			this.last.next = new_node;
			this.last = new_node;
			this.size += 1;
		}

	}
	
	/**
	 * This method removes element from collection at given index. 
	 * 
	 * @param index Index at which element will be removed
	 * @return True if removing is done, false otherwise
	 */
	public boolean remove(int index) {
		
		if(index < 0 || index > this.size -1) {
			throw new IllegalArgumentException();
		}
		
		int cnt = 0;
		
		for(ListNode curr = this.first; curr != null; curr = curr.next) {
			
			if(cnt++ == index) {
				curr.previous.next = curr.next;
				curr.next.previous = curr.previous;
				this.size -= 1;
				return true;
			}
		}
		
		return false;
		
	}

	@Override
	public void clear() {		
		this.size = 0;
		this.first = null;
		this.last = null;
	}
	
	/**
	 * This method is used for getting element on given index.
	 * 
	 * @param index Index of element which we want to get
	 * @return Element at given index
	 */
	public Object get(int index) {
		
		if(index < 0 || index > this.size -1) {
			throw new IndexOutOfBoundsException();
		}
				
		int cnt = 0; 
	
		for(ListNode left = this.first, right = this.last;
						(left.next !=  right.previous) || (left.next != right);
						left = left.next, right = right.previous) {
			if(cnt == index) {
				return left.value;
			} else if(this.size -1 - cnt == index) {
				return right.value;
			}
			cnt++;
		}
		return null;
	}
	
	/**
	 * This method insert element on given position. Elements with bigger index are moved forward.
	 * 
	 * @param value Value of element that will be inserted
	 * @param position Position on which we want to insert element
	 */
	public void insert(Object value, int position) {
		if(position < 0 || position > this.size) {
			throw new IllegalArgumentException();
		}
		if(this.size == 0) {
			ListNode new_node = new ListNode(null, null, value);
			this.first = new_node;
			this.last = new_node;
			this.size = 1;
		} else if(position == 0) {
			ListNode new_node = new ListNode(null, this.first, value);
			this.first = new_node;
			this.size += 1;
		} else if(position == this.size){
			ListNode new_node = new ListNode(this.last, null, value);
			this.last.next = new_node;
			this.last = new_node;
			this.size += 1;
		} else {
			ListNode node_on_position = new ListNode(null, null, null);
			int cnt = 0;
			for (ListNode left = this.first, right = this.last; (left.next != right.previous)
					|| (left.next != right); left = left.next, right = right.previous) {
				if (cnt == position) {
					node_on_position = left;
					break;
				} else if (this.size - 1 - cnt == position) {
					node_on_position = right;
					break;
				}
				cnt++;
			}
			ListNode new_node = new ListNode(node_on_position.previous, node_on_position, value);

			node_on_position.previous.next = new_node;
			node_on_position.previous = new_node;
			this.size += 1;
		}
	}
	
	public int indexOf(Object value) {
		
		if(value == null) {
			return -1;
		}
		//traženje s obje strane no ne izbacuje prvu najblizu
		int cnt = 0; 
		
		for(ListNode left = this.first, right = this.last;
						(left.next !=  right.previous) || (left.next != right);
						left = left.next, right = right.previous) {
			System.out.println(left.value + " " + right.value);
			if(left.value.equals(value)) {
				return cnt;
			} else if(right.value.equals(value)) {
				return this.size - 1 - cnt;
			} else if(cnt > this.size / 2 + 1) {
				break;
			}
			cnt++;
		}
		
		return -1;
		
		//traženje s jedne strane, vraca najlijeviji element
		/*
		 * for(ListNode left = this.first; left.next != null; left = left.next) {
			if(left.value.equals(value)) {
				return cnt;
			}
			cnt++;
		}
		
		return -1
		*/
			
	}
	
	
	
	
	
}
