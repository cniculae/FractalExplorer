import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// class that models the fractal JPanel
public abstract class Fractal extends JPanel {

    protected int width = 600, height = 600, iterations;
    protected double escape;
    protected boolean isJulia = false;
    protected double rangeW = 4;
    protected double rangeH = 3.2;
    protected double minW = -2;
    protected double minH = -1.6;
    protected Complex juliaC = new Complex(minW + rangeW / 2, minH + rangeH / 2);
    protected final static double ratio = 1;
    protected boolean zoom = false;
    protected boolean grab = false;
    protected int mouseMinimX, mouseMinimY;
    protected BufferedImage bfImg;

    // sets all the basic values for the fractal variables
    public Fractal(int width, int height, int iterations, int escape, boolean isJulia) {
        this.width = width;
        this.height = height;
        this.iterations = iterations;
        this.escape = escape * escape;
        this.isJulia = isJulia;
        this.setPreferredSize(new Dimension(width, height));
        this.setVisible(true);
        this.bfImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        drawFractal(g2);

    }

    // creates a BufferedImage and colours each individual pixel according to the complex number formula
    public void drawFractal(Graphics2D g) {
        BufferedImage bfImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double mu;
                Complex c = new Complex(scaleWidth(i), scaleHeight(j));
                Complex z = new Complex(scaleWidth(i), scaleHeight(j));
                if (!isJulia) {
                    mu = divergenceTest(z, c);
                    if (mu < 1 && Math.abs(scaleWidth(i)) + Math.abs(scaleHeight(j)) < 4)
                        bfImg.setRGB(i, j, Color.HSBtoRGB((float) (mu), 1.0f, (float) mu));
                } else {
                    mu = divergenceTest(z, juliaC);
                    if (mu < 1)
                        bfImg.setRGB(i, j, Color.HSBtoRGB(0.5f + (float) (mu), 1.0f, 1.5f * (float) mu));
                }
            }
        }
        g.drawImage(bfImg, 0, 0, null, null);
        this.bfImg = bfImg;
    }

    public void setScales(double xMin, double yMin, double xRange, double yRange) {
        rangeW = xRange;
        rangeH = yRange;
        minH = yMin;
        minW = xMin;
        repaint();
    }

    // rescales the fractal according to new coordinates
    public void zoom(int x1, int y1, int x2, int y2) {
        if (x1 > x2) {
            int aux;
            aux = x1;
            x1 = x2;
            x2 = aux;
        }
        if (y1 > y2) {
            int aux;
            aux = y1;
            y1 = y2;
            y2 = aux;
        }
        double newRangeH, newMinH, newMinW, newRangeW;

        newRangeH = (scaleHeight(y2) - scaleHeight(y1));
        newRangeW = (scaleWidth(x2) - scaleWidth(x1));
        newMinH = scaleHeight(y1);
        newMinW = scaleWidth(x1);
        double i = Math.min(rangeW / newRangeW, rangeH / newRangeH);
        double middleY = (newMinH + newRangeH / 2);
        double middleX = (newMinW + newRangeW / 2);

        //For zoom animation
        while (i > 1) {
            //  xMin  yMin  xRange  yRange
            if (newRangeH > newRangeW) {
                this.setScales(middleX - newRangeH * ratio * i / 2, middleY - newRangeH * i / 2, newRangeH * ratio * i, newRangeH * i);
            } else {
                this.setScales(middleX - newRangeW * i / 2, middleY - newRangeW * i * ratio / 2, newRangeW * i, newRangeW * i * ratio);

            }


            this.update(getGraphics());
            i /= 1.3;
            if (i < 1)
                i = 1;

        }

    }

    // draws the mouse selection for the zoom feature
    public void drawMouse(int x2, int y2) {
        int x1 = mouseMinimX;
        int y1 = mouseMinimY;
        Graphics2D g = (Graphics2D) this.getGraphics();
        g.setColor(Color.white);
        g.drawImage(bfImg, 0, 0, null, null);
        g.drawLine(x1, y1, x2, y1);
        g.drawLine(x1, y1, x1, y2);
        g.drawLine(x1, y2, x2, y2);
        g.drawLine(x2, y1, x2, y2);
    }

    // returns the complex number of a certain point
    public Complex getComplex(Point p) {
        return new Complex(this.scaleWidth(p.getX()), this.scaleHeight(p.getY()));
    }

    public void setMouseMinim(int minimX, int minimY) {
        mouseMinimX = minimX;
        mouseMinimY = minimY;
    }

    public void setIterations(int x) {
        this.iterations = x;
        this.repaint();
    }

    public double scaleWidth(double x) {
        return (x / width) * rangeW + minW;
    }

    public double scaleHeight(double x) {
        return (x / height) * rangeH + minH;
    }

    public void setZoom() {
        zoom = true;
        grab = false;
    }

    public void setGrab() {
        zoom = false;
        grab = true;
    }

    public boolean getGrab() {
        return grab;
    }


    public boolean getZoom() {
        return zoom;
    }

    public void setMinH(double minH) {
        this.minH += minH;
    }

    public void setMinW(double minW) {
        this.minW += minW;
    }

    // resets the fractal to the original size and specifications
    public void resetSize() {
        rangeW = 4;
        rangeH = 3.2;
        minW = -2;
        minH = -1.6;
        iterations = 100;
        repaint();
    }

    public abstract double divergenceTest(Complex z, Complex c);
}

