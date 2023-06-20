package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.java.math.Complex;
import hr.fer.zemris.java.math.ComplexPolynomial;
import hr.fer.zemris.java.math.ComplexRootedPolynomial;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is program that draws Newton-Raphson iteration.
 */
public class Newton {

    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int i = 1;
        List<Complex> roots = new ArrayList<>();

        while(true) {
            System.out.print("Root " + i++ + "> ");

            String input = br.readLine();
            if(input.equals("done")) {
                break;
            }
            try {
                roots.add(parse(input));
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid input");
                i--;
            }

        }

        if(roots.size() <= 1) {
            System.out.println("Please enter at least two roots");
            System.exit(1);
        } else {
            System.out.println("Image of fractal will appear shortly. Thank you.");
        }

        //ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(Complex.ONE, Complex.ONE, Complex.IM, Complex.IM_NEG, Complex.ONE_NEG);
        ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(Complex.ONE, roots);

        FractalViewer.show(new NewtonProducer(polynomial));
    }

    public static class NewtonProducer implements IFractalProducer {

        private ComplexRootedPolynomial rootedPolynomial;

        private ComplexPolynomial polynomial;

        public NewtonProducer(ComplexRootedPolynomial polynomial) {
            this.rootedPolynomial = polynomial;
            this.polynomial = polynomial.toComplexPolynom();
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Zapocinjem izracun...");
            int m = 16*16*16;
            int offset = 0;
            short[] data = new short[width * height];
            for(int y = 0; y < height; y++) {
                if(cancel.get()) break;
                for(int x = 0; x < width; x++) {
                    double cre = (double) x / (width-1.0) * (reMax - reMin) + reMin;
                    double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
                    Complex zn = new Complex(cre, cim);

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
                    } while(iters < m && module > 0.001);

                    int index = rootedPolynomial.indexOfClosestRootFor(zn, 0.002);

                    if(index == -1) {
                        data[offset++] = 0;
                    } else {
                        data[offset++] = (short) (index + 1);
                    }


                }
            }
            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short)(polynomial.order()+1), requestNo);
        }

        @Override
        public void setup() {

        }

        @Override
        public void close() {

        }
    }

    static Complex parse(String input) {
        String[] splitted = input.split(" ");
        double real = 0;
        double imag = 0;
        if(splitted.length == 1) {
            if(splitted[0].indexOf('i') == 0 || splitted[0].indexOf('i') == 1){
                String im = parseImag(splitted[0]);
                imag = Double.parseDouble(im);
            } else {
                try {
                    real = Double.parseDouble(splitted[0]);
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException();
                }
            }
        } else if (splitted.length == 3){
            real = Double.parseDouble(splitted[0]);
            imag = Double.parseDouble(splitted[1] + parseImag(splitted[2]));
        } else {
            throw new IllegalArgumentException();
        }
        return new Complex(real, imag);
    }

    private static String parseImag(String input) {
        char[] data = input.toCharArray();
        String im = "";
        int currentIndex = 0;

        while(currentIndex < input.length()) {
            if(data[currentIndex] != 'i') {
                im += data[currentIndex];
            }
            currentIndex++;
        }

        if(im.equals("-") || im.equals("")) {
            im += "1";
        }

        return im;
    }
}
