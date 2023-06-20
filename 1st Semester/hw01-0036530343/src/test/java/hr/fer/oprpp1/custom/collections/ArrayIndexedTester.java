package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ArrayIndexedTester {
	
	
	@Test
	public void testInitialCapacity() {
		
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(-1));
	}
	
	@Test
	public void testNullCollection() {
		
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
	}
		
	@Test
	public void testArrayLength() {
			
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		
		assertEquals(16, array.toArray().length);
	}
	

//	
//	@Test
//	public void testEmpty() {
//		
//		ArrayIndexedCollection array = new ArrayIndexedCollection();
//		
//		assertTrue(array.isEmpty());
//	}
//	
//	@Test
//	public void testEmptySize() {
//		ArrayIndexedCollection array = new ArrayIndexedCollection();
//		
//		assertEquals(0, array.size());
//	}
//	
//	@Test
//	public void testSize() {
//		ArrayIndexedCollection array = new ArrayIndexedCollection();
//
//		array.add(15);
//		array.add(34);
//		
//		assertEquals(2, array.size());
//	}
//	
//	@Test
//	public void testContain() {
//		ArrayIndexedCollection array = new ArrayIndexedCollection();
//
//		array.add(15);
//		array.add(34);
//		
//		assertTrue(array.contains(15));
//	}
//	
//	@Test
//	public void testToArray() {
//		ArrayIndexedCollection array = new ArrayIndexedCollection(2);
//
//		array.add(15);
//		array.add("Ante");
//
//		Object[] expected = new Object[] {15, "Ante"};
//		
//		assertArrayEquals(expected, array.toArray());
//		
//	}
	
	@Test
	public void testAdd() {
		Object[] expected = new Object[] {15, "Ante", "clan", 452};
		ArrayIndexedCollection array = new ArrayIndexedCollection(2);
		array.add(15);
		array.add("Ante");
		array.add("clan");
		array.add(452);
		
		assertArrayEquals(expected, array.toArray());
	}
	
	@Test
	public void testAddNullException() {
		
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection().add(null));
	}
	
	@Test
	public void testGet() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();

		array.add(15);
		array.add("Ante");
		
		assertEquals("Ante", array.get(1));
	}
	
	@Test
	public void testGetIllegalArgument() {
		
		//ArrayIndexedCollection array = new ArrayIndexedCollection();
		
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection().get(-1));
	}
	
	
	@Test
	public void testNullValue() {
		//ArrayIndexedCollection array = new ArrayIndexedCollection(2);
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection().add(null));
	}
	
	@Test
	public void testClear() {
		
		ArrayIndexedCollection array = new ArrayIndexedCollection();

		array.add(15);
		array.add("Ante");
	
		array.clear();
		
		ArrayIndexedCollection expected = new ArrayIndexedCollection();

		assertArrayEquals(expected.toArray(), array.toArray());
		
	}
	
	@Test
	public void testInsertException() {
		
		assertThrows(IndexOutOfBoundsException.class, () -> new ArrayIndexedCollection().insert(15, -1));
		assertThrows(IndexOutOfBoundsException.class, () -> new ArrayIndexedCollection().insert(15, 2));

	}
	
	@Test
	public void testInsert() {
		
		ArrayIndexedCollection array = new ArrayIndexedCollection(3);

		array.add(15);
		array.add("Ante");
		array.add("Luka");
		array.insert("Lopta", 3);
		
		Object[] expected = new Object[] {15, "Ante", "Luka", "Lopta", null, null};
		
		assertArrayEquals(array.toArray(), expected);
	}
	
	@Test
	public void testIndexOf() {

		ArrayIndexedCollection array = new ArrayIndexedCollection(5);
		
		array.add(15);
		array.add("Ante");
		array.add("Luka");
		
		assertEquals(-1, array.indexOf(null));
		assertEquals(1, array.indexOf("Ante"));
		
	}
	
	@Test
	public void testRemove() {
		
		ArrayIndexedCollection array = new ArrayIndexedCollection(3);
		
		array.add(15);
		array.add("Ante");
		array.add("Luka");
		
		assertThrows(IndexOutOfBoundsException.class, () -> array.remove(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> array.remove(4));
		
		array.remove(1);
	
		Object[] expected = new Object[] {15, "Luka", null};
		
		assertArrayEquals(expected, array.toArray());
	}
	

	
	
	
}
