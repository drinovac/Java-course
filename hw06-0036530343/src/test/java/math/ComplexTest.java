package math;

import hr.fer.zemris.math.Complex;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexTest {

    private static final double DELTA = 1E-7;

    @Test
    public void module() {
        Complex c = new Complex(3, 4);
        assertEquals(5, c.module(), DELTA);
    }

    @Test
    public void multiply() {
        Complex c = new Complex(3, 4).multiply(new Complex(5, 6));
        assertEquals(-9, c.getRe(), DELTA);
        assertEquals(38, c.getIm(), DELTA);
    }

    @Test
    public void divide() {
        Complex c = new Complex(3, 4).divide(new Complex(5, 6));
        assertEquals(39.0 / 61, c.getRe(), DELTA);
        assertEquals(2.0 / 61, c.getIm(), DELTA);
    }


    @Test
    public void add() {
        Complex c = new Complex(3, 4).add(new Complex(5, 6));
        assertEquals(8, c.getRe(), DELTA);
        assertEquals(10, c.getIm(), DELTA);
    }

    @Test
    public void sub() {
        Complex c = new Complex(3, 4).sub(new Complex(5, 6));
        assertEquals(-2, c.getRe(), DELTA);
        assertEquals(-2, c.getIm(), DELTA);
    }

    @Test
    public void negate() {
        Complex c = new Complex(3, 4).negate();
        assertEquals(-3, c.getRe(), DELTA);
        assertEquals(-4, c.getIm(), DELTA);
    }

    @Test
    public void power() {
        Complex c = new Complex(3, 4).power(6);
        assertEquals(11753, c.getRe(), DELTA);
        assertEquals(-10296, c.getIm(), DELTA);
    }


    @Test
    public void getReImMagAng() {
        Complex c = new Complex(3, 4);
        assertEquals(3, c.getRe(), DELTA);
        assertEquals(4, c.getIm(), DELTA);
        assertEquals(5, c.module(), DELTA);
    }

    @Test
    public void root() {
        List<Complex> roots = new Complex(3, 4).root(4);

        assertEquals(1.455346690225355, roots.get(0).getRe(), DELTA);
        assertEquals(0.34356074972251244, roots.get(0).getIm(), DELTA);
        assertEquals(-0.34356074972251244, roots.get(1).getRe(), DELTA);
        assertEquals(1.455346690225355, roots.get(1).getIm(), DELTA);
        assertEquals(-1.455346690225355, roots.get(2).getRe(), DELTA);
        assertEquals(-0.34356074972251244, roots.get(2).getIm(), DELTA);
        assertEquals(0.34356074972251244, roots.get(3).getRe(), DELTA);
        assertEquals(-1.455346690225355, roots.get(3).getIm(), DELTA);
    }


}