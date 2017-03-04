// models the Burning Ship fractal, extending the original fractal model
public class BurningShip extends Fractal {
    public BurningShip(int width, int height, int iterations) {
        super(width, height, iterations, 2, false);
        System.out.println("@@" + width + " " + height);
    }

    // uses the Burning Ship formula for the divergence test, to return a double for the desired colour
    @Override
    public double divergenceTest(Complex z, Complex c) {
        double modul = 0;
        int k;
        for (k = 0; k <= this.iterations; k++) {
            modul = z.modulusSquared();
            if (modul > this.escape) {
                break;
            }
            z.setIm(Math.abs(z.getIm()));
            z.setRe(Math.abs(z.getRe()));
            z.square();
            z.add(c);
        }
        modul = z.modulusSquared();
        return (k + 1 - (Math.log((Math.log(modul) / Math.log(2))))) / iterations;
    }
}
