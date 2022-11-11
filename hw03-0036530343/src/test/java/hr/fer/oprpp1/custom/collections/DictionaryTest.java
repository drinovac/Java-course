package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DictionaryTest {

    @Test
    public void testDefaultConstructor() {
        Dictionary<String, Integer> dict = new Dictionary<>();

        assertTrue(dict.isEmpty());
    }

    @Test
    public void testGet() {
        Dictionary<String, Integer> dict = new Dictionary<>();

        dict.put("Ante", 5);
        dict.put("Lena", 4);
        dict.put("Borna", 2);

        assertEquals(4, dict.get("Lena"));
        assertEquals(5, dict.get("Ante"));
        assertEquals(2, dict.get("Borna"));

        assertEquals(null, dict.get("Jakov"));

    }

    @Test
    public void testPut() {
        Dictionary<String, Integer> dict = new Dictionary<>();

        dict.put("Ante", 5);
        dict.put("Lena", 4);
        dict.put("Borna", 2);

        dict.put("Lena", 6);

        assertEquals(6, dict.get("Lena"));
        assertEquals(3, dict.size());

    }

    @Test void testRemove() {
        Dictionary<String, Integer> dict = new Dictionary<>();

        dict.put("Ante", 5);
        dict.put("Lena", 4);
        dict.put("Borna", 2);

        dict.remove("Ante");
        dict.remove("Borna");

        assertEquals(1, dict.size());
        assertEquals(null, dict.get("Ante"));
    }
}
