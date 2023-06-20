package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SimpleHashableTest {

    @Test
    public void testDefaultConstructor() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
    }

    @Test
    public void testConstructor() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);

        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
    }

    @Test
    public void testPut() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

        assertNull(table.put("Ante", 2));
        assertEquals(2, table.put("Ante", 3));
        assertThrows(NullPointerException.class, () -> table.put(null, null));
    }

    @Test
    public void testGet() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

        table.put("Ante", 2);
        table.put("Lena", 3);

        assertNull(table.get("LEna"));
        assertNull(table.get(null));
        assertEquals(3, table.get("Lena"));
    }

    @Test
    public void testContainsKey() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

        table.put("Ante", 2);
        table.put("Lena", 3);

        assertTrue(table.containsKey("Lena"));
        assertFalse(table.containsKey("LEna"));

    }

    @Test
    public void testContainsValue() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

        table.put("Ante", 2);
        table.put("Lena", 3);

        assertTrue(table.containsValue(3));
        assertFalse(table.containsValue(6));
    }

    @Test
    public void testRemove() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

        table.put("Ante", 2);
        table.put("Lena", 3);

        assertNull(table.remove("LEna"));
        assertEquals(3, table.remove("Lena"));
        assertFalse(table.containsKey("Lena"));
    }

    @Test
    public void testToString() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

        table.put("Ante", 2);
        table.put("Lena", 3);
        table.put("Borna", 5);
        table.put("Jakov", 4);

        String s = "[Borna=5, Jakov=4, Lena=3, Ante=2]";

        assertEquals(s, table.toString());
    }
    @Disabled
    @Test
    public void testToArray() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

        table.put("Ante", 2);
        table.put("Lena", 3);
        table.put("Borna", 5);
        table.put("Jakov", 4);

        SimpleHashtable.TableEntry<String,Integer>[] array = (SimpleHashtable.TableEntry<String,Integer>[]) new SimpleHashtable.TableEntry[4];
        array[0] = new SimpleHashtable.TableEntry<>("Borna", 5, null);
        array[1] = new SimpleHashtable.TableEntry<>("Jakov", 4, null);
        array[2] = new SimpleHashtable.TableEntry<>("Lena", 3, null);
        array[3] = new SimpleHashtable.TableEntry<>("Ante", 2, null);

        assertEquals(table.toArray()[0], array[0]);
    }

    @Test
    public void testClear() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

        table.put("Ante", 2);
        table.put("Lena", 3);
        table.put("Borna", 5);
        table.put("Jakov", 4);

        table.clear();

        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
        assertNull(table.get("Ante"));
    }

}
