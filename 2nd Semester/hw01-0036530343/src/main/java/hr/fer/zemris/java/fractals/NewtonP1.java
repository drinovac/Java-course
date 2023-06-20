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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonP1 {
    public static void main(String[] args) throws IOException {

        int workers = Runtime.getRuntime().availableProcessors();
        int tracks = 4 * Runtime.getRuntime().availableProcessors();
        boolean workersInitilized = false;
        boolean tracksInitilized = false;

        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("--w") && !workersInitilized) {
                String[] splitted = args[i].split("=");
                workers = Integer.parseInt(splitted[1]);
                workersInitilized = true;
            } else if(args[i].startsWith("--t") && !tracksInitilized) {
                String[] splitted = args[i].split("=");
                tracks = Integer.parseInt(splitted[1]);
                tracksInitilized = true;
            } else if(args[i].equals("-w") && !workersInitilized) {
                workers = Integer.parseInt(args[(i++) + 1]);
                workersInitilized = true;
            } else if(args[i].equals("-t") && !tracksInitilized) {
                tracks = Integer.parseInt(args[(i++) + 1]);
                tracksInitilized = true;
            } else {
                System.out.println("Wrong argument input");
                System.exit(1);
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

        FractalViewer.show(new NewtonParallelProducer(workers, tracks, rootedPolynomial));
    }

    public static class PosaoIzracuna implements Runnable {
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

        public static PosaoIzracuna NO_JOB = new PosaoIzracuna();

        private PosaoIzracuna() {
        }

        public PosaoIzracuna(double reMin, double reMax, double imMin,
                             double imMax, int width, int height, int yMin, int yMax,
                             int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial rootedPolynomial) {
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
        }


        /**
         * Runs this operation.
         */
        @Override
        public void run() {

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

        private int workers;
        private int tracks;
        public ComplexPolynomial polynomial;
        public ComplexRootedPolynomial rootedPolynomial;
        public ExecutorService pool;

        public NewtonParallelProducer(int workers, int tracks, ComplexRootedPolynomial rootedPolynomial) {
            this.workers = workers;
            this.tracks = tracks;
            this.rootedPolynomial = rootedPolynomial;
            this.polynomial = rootedPolynomial.toComplexPolynom();

        }

        public int getWorkers() {
            return workers;
        }

        public int getTracks() {
            return tracks;
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Zapocinjem izracun...");
            int m = 16*16*16;
            short[] data = new short[width * height];
            final int brojTraka = Math.min(getTracks(), height);
            int brojYPoTraci = height / brojTraka;

            List<PosaoIzracuna> listPosao = new LinkedList<>();

            for(int i = 0; i < brojTraka; i++) {
                int yMin = i*brojYPoTraci;
                int yMax = (i+1)*brojYPoTraci-1;
                if(i == brojTraka-1) {
                    yMax = height-1;
                }
                listPosao.add(new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel, rootedPolynomial));
            }

            List<Future<?>> rezultati = new ArrayList<>();

            for(PosaoIzracuna p: listPosao) {
                rezultati.add(pool.submit(p));
            }

            for(Future<?> f : rezultati) {
                while(true) {
                    try {
                        f.get();
                        break;
                    } catch (InterruptedException | ExecutionException ignored) {}
                }
            }

            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short)(polynomial.order() + 1), requestNo);
        }

        @Override
        public void setup() {
            this.pool = Executors.newFixedThreadPool(workers);
        }

        @Override
        public void close() {
            this.pool.shutdown();
        }
    }
}
