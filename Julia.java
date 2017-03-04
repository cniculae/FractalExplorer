import java.awt.image.BufferedImage;

// models the Julia fractal, extending the original fractal model
public class Julia extends Fractal{
    public Julia(int width, int height, int iterations) {
        super(width, height, iterations, 2, true);
    }
    boolean isMandelbrot=true;
    boolean isBurningShip=false;
    // uses the Julia formula for the divergence test, to return a double for the desired colour
    @Override
    public double divergenceTest(Complex z, Complex c) {
        double modul=0;
        int k;

        //Mandelbrot Julia
        if(isMandelbrot) {
            for (k = 0; k <= this.iterations; k++) {
                modul = z.modulusSquared();
                if (modul > this.escape) {
                    break;
                }
                z.square();
                z.add(c);
            }
        }
        //BurningShip Julia
        else if(isBurningShip){
            for (k = 0; k <= this.iterations; k++) {
                modul = z.modulusSquared();
                if (modul>this.escape) {
                    break;
                }
                z.setIm(Math.abs(z.getIm()));
                z.setRe(Math.abs(z.getRe()));
                z.square();
                z.add(c);
            }
        }
        //Tricorn Julia
        else{
            for (k = 0; k <= this.iterations; k++) {
                modul = z.modulusSquared();
                if (modul>this.escape) {
                    break;
                }
                z.setIm(-1*z.getIm());
                z.square();
                z.add(c);
            }
        }
        modul=z.modulusSquared();
        return (k+1 - (Math.log((Math.log(modul)/Math.log(2)))))/iterations;
    }

    public void setBaseComplex(Complex c){
        this.juliaC=c;
    }

    public Complex getBaseComplex(){
        return this.juliaC;
    }

    public BufferedImage getImage(){
        return this.bfImg;
    }

    public void setIsMandelbrot(boolean isMandelbrot){
        this.isMandelbrot=isMandelbrot;
    }

    public void setIsBurningShip(boolean isBurningShip) {
        this.isBurningShip = isBurningShip;
    }

    public boolean isMandelbrot() {
        return isMandelbrot;
    }

    public boolean isBurningShip(){
        return isBurningShip;
    }
}
