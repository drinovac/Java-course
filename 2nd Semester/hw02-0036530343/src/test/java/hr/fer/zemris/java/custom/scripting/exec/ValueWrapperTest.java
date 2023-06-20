package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ValueWrapperTest {


    @Test
    public void testStringInputInteger() {
        ValueWrapper v1 = new ValueWrapper("3");
        assertEquals(v1.getValue(), 3);
    }

    @Test
    public void testStringInputDouble() {
        ValueWrapper v1 = new ValueWrapper("3.0");
        assertEquals(v1.getValue(), 3.0);
    }

    @Test
    public void testIntInput() {
        ValueWrapper v1 = new ValueWrapper(3);
        assertEquals(v1.getValue(), 3);
    }
    @Test
    public void testDoubleInput() {
        ValueWrapper v1 = new ValueWrapper(3.0);
        assertEquals(v1.getValue(), 3.0);
    }

    @Test
    public void testAddingNull() {
        ValueWrapper v1 = new ValueWrapper(3.0);
        v1.add(null);
        assertEquals(v1.getValue(), 3.0);
    }

    @Test
    public void testAddingOnNull() {
        ValueWrapper v1 = new ValueWrapper(null);
        v1.add(3);
        assertEquals(v1.getValue(), 3.0);
    }

    @Test
    public void testAddingBoolean() {
        ValueWrapper vv1 = new ValueWrapper(Boolean.valueOf(true));
        assertThrows(RuntimeException.class, () -> vv1.add(Integer.valueOf(5)));
    }
}
