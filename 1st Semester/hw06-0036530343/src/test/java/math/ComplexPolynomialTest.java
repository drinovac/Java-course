package math;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexPolynomialTest {

    @Test
    public void order() {
        ComplexPolynomial polynomial = new ComplexPolynomial(new Complex(1, 0), new Complex(5, 0), new Complex(2, 0),
                new Complex(7, 2));

        assertEquals(3, polynomial.order());
    }

    @Test
    public void multiply() {
        ComplexPolynomial polynomial = new ComplexPolynomial(new Complex(1, 0), new Complex(5, 0), new Complex(2, 0),
                new Complex(7, 2)).multiply(new ComplexPolynomial(new Complex(2, 2), new Complex(1, 1)));

        Complex[] factors = polynomial.factors;

        assertEquals(new Complex(2, 2), factors[0]);
        assertEquals(new Complex(11, 11), factors[1]);
        assertEquals(new Complex(9, 9), factors[2]);
        assertEquals(new Complex(12, 20), factors[3]);
        assertEquals(new Complex(5, 9), factors[4]);
    }

    @Test
    public void derive() {
        ComplexPolynomial polynomial = new ComplexPolynomial(new Complex(1, 0), new Complex(5, 0), new Complex(2, 0),
                new Complex(7, 2))
                .derive();

        Complex[] factors = polynomial.factors;

        assertEquals(new Complex(5, 0), factors[0]);
        assertEquals(new Complex(4, 0), factors[1]);
        assertEquals(new Complex(21, 6), factors[2]);

    }

    @Test
    public void apply() {
        Complex c = new ComplexPolynomial(new Complex(1, 0), new Complex(5, 0), new Complex(2, 0),
                new Complex(7, 2)).apply(new Complex(1, 1));

        assertEquals(new Complex(-12, 19), c);
    }

    @Test
    public void getFactors() {
        ComplexPolynomial polynomial = new ComplexPolynomial(new Complex(1, 0), new Complex(5, 0), new Complex(2, 0),
                new Complex(7, 2));

        Complex[] factors = polynomial.factors;

        assertEquals(new Complex(1, 0), factors[0]);
        assertEquals(new Complex(5, 0), factors[1]);
        assertEquals(new Complex(2, 0), factors[2]);
        assertEquals(new Complex(7, 2), factors[3]);

    }
}