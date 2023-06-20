package hr.fer.zemris.java.math;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents complex number object
 */
public class Complex {

    /**
     * Real part.
     */
    private double re;
    /**
     * Imaginary part.
     */
    private double im;

    /**
     * 0 + i0
     */
    public static final Complex ZERO = new Complex(0,0);
    /**
     * 1 + i0
     */
    public static final Complex ONE = new Complex(1,0);
    /**
     * -1 + i0
     */
    public static final Complex ONE_NEG = new Complex(-1,0);
    /**
     * 0 + i
     */
    public static final Complex IM = new Complex(0,1);
    /**
     * 0 - i
     */
    public static final Complex IM_NEG = new Complex(0,-1);


    /**
     * Public constructor
     * @param re
     * @param im
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    /**
     * This method returns module of complex number
     */

    public double module() {
        return Math.sqrt(re * re + im * im);
    }

    /**
     * This method returns this complex number multipied by another.
     * @param c
     * @return
     */
    public Complex multiply(Complex c) {
        double real = this.re * c.getRe() - this.im * c.getIm();
        double imag = this.re * c.getIm() + this.im * c.getRe();

        return new Complex(real, imag);
    }

    /**
     * This method returns this complex number divided by another
     * @param c
     * @return
     */
    public Complex divide(Complex c) {
        double squaredDivisorNorm = c.module() * c.module();
        double real = (this.re * c.getRe() + this.im * c.getIm()) / squaredDivisorNorm;
        double imag = (this.im * c.getRe() - this.re * c.getIm()) / squaredDivisorNorm;

        return new Complex(real, imag);
    }

    /**
     * This method returns this complex number plus another.
     * @param c
     * @return
     */
    public Complex add(Complex c) {
        return new Complex(this.re + c.getRe(), this.im + c.getIm());
    }

    /**
     * This method returns this complex number minus another.
     * @param c
     * @return
     */
    public Complex sub(Complex c) {
        return new Complex(this.re - c.getRe(), this.im - c.getIm());
    }

    /**
     * This method returns -this complex number
     * @return
     */
    public Complex negate() {
        return new Complex(-this.re, -this.im);
    }

    /**
     * This method returns this complex numeber powered n times
     * @param n
     * @return
     */
    public Complex power(int n) {
        if(n == 0) {
            return Complex.ONE;
        }
        double module = this.module();
        double angle = Math.atan2(this.im, this.re);
        double real = Math.pow(module, n) * Math.cos(n * angle);
        double imag = Math.pow(module, n) * Math.sin(n * angle);

        return new Complex(real, imag);
    }

    /**
     * This method returns list of number roots
     * @param n
     * @return
     */
    public List<Complex> root(int n) {

        List<Complex> list = new ArrayList<>();

        double module = this.module();
        double angle = Math.atan2(this.im, this.re);

        System.out.println(angle);
        System.out.println(module);

        for(int i = 0; i < n; i++) {
            double real = Math.pow(module,  1.0/n) * Math.cos((angle + 2*i*Math.PI)/n);
            double imag = Math.pow(module,  1.0/n) * Math.sin((angle + 2*i*Math.PI)/n);

            list.add(new Complex(real, imag));
        }

        return list;
    }

    @Override
    public String toString() {
        if(this.getIm() < 0.0) {
            return "(" + this.getRe() + "-i" + Math.abs(this.getIm()) + ")";
        }
        return "(" + this.getRe()  + "+i" + Math.abs(this.getIm()) + ")";
    }

}
