package hr.fer.zemris.java.math;


/**
 * This class represents comlpex polynom.
 */
public class ComplexPolynomial {

    // ...
    public Complex[] factors;
// constructor
    public ComplexPolynomial(Complex ...factors) {
        this.factors = factors;
    }
    // returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
    public short order() {
        return (short) (factors.length - 1);
    }
    // computes a new polynomial this*p
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        Complex[] factors = new Complex[this.order() + p.order() + 1];

        for(int i = 0; i < factors.length; i++) {
            factors[i] = Complex.ZERO;
        }

        for(int i = 0; i <= this.order(); i++) {
            for(int j = 0; j <= p.order(); j++) {
                factors[i + j] = factors[i + j].add(this.factors[i].multiply(p.factors[j]));
            }
        }
        return new ComplexPolynomial(factors);
    }
    // computes first derivative of this polynomial; for example, for
    // (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
    public ComplexPolynomial derive() {
        Complex[] factors = new Complex[this.order()];

        for(int i = 0; i < factors.length; i++) {
            factors[i] = this.factors[i + 1].multiply(new Complex(i+1, 0));
        }

        return new ComplexPolynomial(factors);
    }
    // computes polynomial value at given point z
    public Complex apply(Complex z) {
        Complex c = Complex.ZERO;

        for(int i = 0; i <= this.order(); i++) {
            c = c.add(z.power(i).multiply(this.factors[i]));
        }

        return new Complex(c.getRe(), c.getIm());
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if( this.order() > 1) {
            sb.append("" + this.factors[this.order()] + "*z^" + this.order() + "+");
        }
        for(int i = this.order() - 1; i > 1; i--) {
            sb.append("" + this.factors[i]);
            sb.append("*z^" + i + "+");
        }
        sb.append("" + this.factors[1]);
        sb.append("*z");
        sb.append("+" + this.factors[0] + "");

        return sb.toString();
    }




}
