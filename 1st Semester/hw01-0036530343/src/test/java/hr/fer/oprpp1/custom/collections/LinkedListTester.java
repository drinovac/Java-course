package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LinkedListTester {

	@Test
	public void testDefaultConstructor() {
		LinkedListIndexedCollection coll = new LinkedListIndexedCollection();

		assertEquals(0, coll.size());
	}

	@Test
	public void testAdd() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();

		col.add(15);
		col.add("Ante");
		col.add("Luka");

		Object[] expected = new Object[] { 15, "Ante", "Luka" };

		assertArrayEquals(expected, col.toArray());
	}

	@Test
	public void testAddNull() {

		assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection().add(null));

	}

	@Test
	public void testGetException() {

		assertThrows(IndexOutOfBoundsException.class, () -> new LinkedListIndexedCollection().get(-1));
	}

	@Test
	public void testGet() {

		LinkedListIndexedCollection col = new LinkedListIndexedCollection();

		col.add(15);
		col.add("Ante");
		col.add("Luka");

		assertEquals(15, col.get(0));
		assertEquals("Luka", col.get(2));
	}

	@Test
	public void testClear() {

		LinkedListIndexedCollection col = new LinkedListIndexedCollection();

		col.add(15);
		col.add("Ante");
		col.add("Luka");
		
		col.clear();
		
		assertEquals(0, col.size());
		assertTrue(col.isEmpty());
	}
	
	@Test
	public void testInsert() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		
		col.add(15);
		col.add("Ante");
		col.add("Luka");
		
		col.insert(2, 1);
		
		Object[] expected = new Object[] { 15, 2, "Ante", "Luka" };
		
		assertArrayEquals(expected, col.toArray());
	}
	
	@Test

	public void testIndexOf() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		
		col.add(15);
		col.add("Ante");
		col.add("Luka");
		col.add("45");
		col.add("45");
		col.add("Luka");
		
		assertEquals(1, col.indexOf("Ante"));
		assertEquals(-1, col.indexOf(null));
		assertEquals(-1, col.indexOf(45));
	}
	
	@Test
	public void testRemoveException() {
		
		assertThrows(IllegalArgumentException.class, () -> new LinkedListIndexedCollection().remove(-1));
		
	}
	
	@Test
	public void testRemove() {
		
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		
		col.add(15);
		col.add("Ante");
		col.add("Luka");
		col.add("45");
		col.add("45");
		col.add("Luka");
		
		col.remove(2);
		col.remove(2);
		
		Object[] expected = new Object[] { 15, "Ante", "45", "Luka"};
		
		assertArrayEquals(expected, col.toArray());
	}
}
