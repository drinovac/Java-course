package hr.fer.oprpp1.hw05.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtil {

    @Test
    public void testHextobyte() {

        String keytext = "01aE22";
        byte[] expected = {1, -82, 34};

        assertArrayEquals(Util.hextobyte(keytext), expected);
    }

    @Test
    public void testBytetohex() {

        byte[] array = {1, -82, 34};
        String keytext = "01ae22";

        assertEquals(Util.bytetohex(array), keytext);
    }
}
