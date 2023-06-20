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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonP2 {
    public static void main(String[] args) throws IOException {

        int minTracks = 16;

        if(args.length != 0) {
            if(args[0].startsWith("--mintracks")) {
                minTracks = Integer.parseInt(args[0].split("=")[1]);
            } else if("-m".equals(args[0])) {
                minTracks = Integer.parseInt(args[1]);
            }
        }


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
                roots.add(Newton.parse(input));
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid input");
                i--;
            }

        }

        roots.add(new Complex(1,0));
        roots.add(new Complex(0,1));
        roots.add(new Complex(-1,0));
        roots.add(new Complex(0,-1));

        if(roots.size() <= 1) {
            System.out.println("Please enter at least two roots");
            System.exit(1);
        } else {
            System.out.println("Image of fractal will appear shortly. Thank you.");
        }

        //ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(Complex.ONE, Complex.ONE, Complex.IM, Complex.IM_NEG, Complex.ONE_NEG);
        ComplexRootedPolynomial rootedPolynomial = new ComplexRootedPolynomial(Complex.ONE, roots);

        FractalViewer.show(new NewtonP2.NewtonParallelProducer(minTracks, rootedPolynomial));
    }

    public static class Posao extends RecursiveAction {
        double reMin;
        double reMax;
        double imMin;
        double imMax;
        int width;
        int height;
        int yMin;
        int yMax;
        int m;
        short[] data;
        AtomicBoolean cancel;
        ComplexRootedPolynomial rootedPolynomial;
        ComplexPolynomial polynomial;
        int minTracks;

        public static Posao NO_JOB = new Posao();

        private Posao() {
        }

        public Posao(double reMin, double reMax, double imMin,
                     double imMax, int width, int height, int yMin, int yMax,
                     int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial rootedPolynomial, int minTracks) {
            super();
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.cancel = cancel;
            this.rootedPolynomial = rootedPolynomial;
            this.polynomial = rootedPolynomial.toComplexPolynom();
            this.minTracks = minTracks;
        }


        /**
         * Runs this operation.
         */
        @Override
        protected void compute() {

            if((yMax - yMin) < minTracks) {
                computeDirect();
                return;
            }

            Posao p1 = new Posao(reMin, reMax, imMin, imMax, width, height, yMin, (yMax + yMin)/2, m, data, cancel, rootedPolynomial, minTracks);
            Posao p2 = new Posao(reMin, reMax, imMin, imMax, width, height, yMax - (yMax - yMin)/2, yMax, m, data, cancel, rootedPolynomial, minTracks);
            invokeAll(p1,p2);

        }

        private void computeDirect() {
            yMax = yMax == height ? yMax - 1 : yMax;
            for(int y = yMin; y <= yMax; y++) {
                if(cancel.get()) break;

                for(int x = 0; x < width; x++) {
                    double cre = (double) x / (width-1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

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
                        data[y * width + x] = 0;
                    } else {
                        data[y * width + x] = (short) (index + 1);
                    }
                }
            }
        }
    }

    public static class NewtonParallelProducer implements IFractalProducer {

        private int minTracks;
        public ComplexPolynomial polynomial;
        public ComplexRootedPolynomial rootedPolynomial;
        public ForkJoinPool pool;

        public NewtonParallelProducer(int tracks, ComplexRootedPolynomial rootedPolynomial) {
            this.minTracks = tracks;
            this.rootedPolynomial = rootedPolynomial;
            this.polynomial = rootedPolynomial.toComplexPolynom();

        }

        public int getMinTracks() {
            return minTracks;
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Zapocinjem izracun...");
            int m = 16*16*16;
            short[] data = new short[width * height];

            Posao p = new Posao(reMin, reMax, imMin, imMax, width, height, 0, height, m, data, cancel, rootedPolynomial, minTracks);
            pool.invoke(p);


            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short)(polynomial.order() + 1), requestNo);
        }

        @Override
        public void setup() {
            this.pool = new ForkJoinPool();
        }

        @Override
        public void close() {
            this.pool.shutdown();
        }
    }
}
