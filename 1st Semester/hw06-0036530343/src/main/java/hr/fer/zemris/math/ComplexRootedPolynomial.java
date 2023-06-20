package hr.fer.zemris.math;

import java.util.List;

/**
 * This class represents complex polynom.
 */
public class ComplexRootedPolynomial {

    /**
     * Start constant.
     */
    public Complex constant;
    /**
     * Complex number array.
     */
    public Complex[] roots;

    /**
     * Constructor with roots as Complex[].
     * @param constant
     * @param roots
     */
    public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
        this.constant = constant;
        this.roots = roots;
    }

    /**
     * Constructor with roots as List<Complex>.
     * @param constant
     * @param roots
     */
    public ComplexRootedPolynomial(Complex constant, List<Complex> roots) {
        this.constant = constant;
        this.roots = roots.toArray(new Complex[]{});
    }

    /**
     * This method computes polynomial value at given point z
     * @param z
     * @return
     */
    public Complex apply(Complex z) {
        return toComplexPolynom().apply(z);
    }
    /**
     * This method converts this representation to ComplexPolynomial type.
     */

    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial polynomial = new ComplexPolynomial(Complex.ONE);

        for(Complex complex: roots) {
            polynomial = polynomial.multiply(new ComplexPolynomial(complex.negate(), Complex.ONE));
        }

        polynomial = polynomial.multiply(new ComplexPolynomial(constant));

        return polynomial;

    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.constant.toString());

        for(Complex c: this.roots) {
            sb.append("*(z-");
            sb.append(c);
            sb.append(")");
        }
        return sb.toString();
    }

    /**
     * This method finds index of closest root for given complex number z that is within treshold
     * @param z
     * @param treshold
     * @return
     */
    public int indexOfClosestRootFor(Complex z, double treshold) {
        int index = -1;
        for(int i = 0; i < roots.length; i++) {
            if(z.sub(roots[i]).module() < treshold) {
                index = i;
            }
        }

        return index;
    }
}
