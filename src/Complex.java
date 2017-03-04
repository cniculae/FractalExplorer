/**
 * Created by Cristian on 06/03/2016.
 */

// class that models the complex number the fractal is based on
public class Complex {
    // real and imaginary parts
    double re;
    double im;

    // basic constructor
    public Complex(double real, double imaginary){
        re=real;
        im=imaginary;
    }

    public void setIm(double im) {
        this.im = im;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public double getIm() {
        return im;
    }

    public double getRe() {
        return re;
    }

    // squares the complex number by real and imaginary components
    public void square(){
        double oldRe, oldIm;
        oldRe=re;
        oldIm=im;
        re=oldRe*oldRe - oldIm*oldIm;
        im=2*oldRe*oldIm;
    }

    // squares the modulus of the complex number and returns it as a double
    public double modulusSquared(){
        return im * im + re * re;
    }

    public void add(Complex d){
        if( d == null)
            return;
        im+=d.getIm();
        re+=d.getRe();
    }

}
