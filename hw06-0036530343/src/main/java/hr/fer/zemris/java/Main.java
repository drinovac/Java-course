package hr.fer.zemris.java;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

public class Main {
    public static void main(String[] args) {

        Complex c1 = new Complex(2,3);
        Complex c2 = new Complex(3,4);
        Complex c3 = new Complex(2,5);

        Complex c4 = new Complex(2,3);
        Complex c5 = new Complex(3,4);

        Complex[] factors1 = new Complex[5];
        factors1[0] = c1;
        factors1[1] = c2;
        factors1[2] = c3;
        factors1[3] = c4;
        factors1[4] = c5;


        Complex[] factors2 = new Complex[2];
        factors2[0] = c4;
        factors2[1] = c5;

        //ComplexRootedPolynomial complexRootedPolynomial = new ComplexRootedPolynomial(new Complex(2,3), factors1);

        //ComplexPolynomial result = complexRootedPolynomial.toComplexPolynom();

        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );
        ComplexPolynomial cp = crp.toComplexPolynom();
        System.out.println(crp);
        System.out.println(cp);
        System.out.println(cp.derive());


        ComplexRootedPolynomial rootedPolynomial = new ComplexRootedPolynomial(Complex.ONE, Complex.ONE, Complex.IM, Complex.IM_NEG, Complex.ONE_NEG);
        ComplexPolynomial polynomial = rootedPolynomial.toComplexPolynom();
        Complex zn = new Complex(0.1, 0.1);
        ComplexPolynomial derived = polynomial.derive();
        double module = 0;
        int iters = 0;
        do {
            Complex numerator = polynomial.apply(zn);
            Complex denominator = derived.apply(zn);
            Complex znold = zn;
            Complex fraction = numerator.divide(denominator);

            zn = zn.sub(fraction);


            module = znold.sub(zn).module();
            iters++;
        } while(iters < 16 && module > 0.001);
        //System.out.println(zn);

        int index = rootedPolynomial.indexOfClosestRootFor(zn, 0.002);
        Complex c = new Complex(1,1);
        System.out.println(c.power(4).add(Complex.ONE));


    }
}