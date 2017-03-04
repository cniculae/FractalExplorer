
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

// GUI that displays the fractal explorer
public class FractalFrame extends JFrame {
    // defines the basic sizes for various components
    static final int DEFAULT_HEIGHT = 650;
    static final int DEFAULT_WIDTH = 1060;
    static final int DEFAULT_M_HEIGHT = 621;
    static final int DEFAULT_M_WIDTH = 754;
    static final int DEFAULT_J_HEIGHT = 300;
    static final int DEFAULT_J_WIDTH = 300;
    Fractal fractalPanel;
    Julia juliaPanel;
    JLabel coordinates;
    boolean dynamic = false;
    JPanel menuPanel;
    ArrayList<Complex> favoriteJulia = new ArrayList<Complex>();

    // sets all the components of the JFrame and lays the layout
    public FractalFrame() {

        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        //
        //Beginning of adding components to the frame
        //

        //CENTER
        //Adding the center panel which includes only the panel with the fractal
        fractalPanel = new Mandelbrot(DEFAULT_M_WIDTH, DEFAULT_M_HEIGHT, 100);
        this.add(fractalPanel, BorderLayout.CENTER);

        //EAST
        //Adding the right panel which includes the panel for julia and the panel for the menu:
        JPanel menuAndJulia = new JPanel();
        menuAndJulia.setLayout(new BorderLayout());
        juliaPanel = new Julia(DEFAULT_J_WIDTH, DEFAULT_J_HEIGHT, 100);
        menuAndJulia.add(juliaPanel, BorderLayout.NORTH);
        this.add(menuAndJulia, BorderLayout.EAST);

        //MENU
        //Adding the menu panel which includes all the buttons/text fields/drop-down lists
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(15, 1));

        //Adding a border for the menu panel:
        TitledBorder menuTitle;
        menuTitle = BorderFactory.createTitledBorder("Menu");
        menuPanel.setBorder(menuTitle);

        //JLabel with the complex number
        coordinates = new JLabel("", null, 10);
        menuPanel.add(coordinates);

        //JTextField and JButton to change the number of iterations
        menuPanel.add(new JLabel("Number Of Iterations:"));
        JTextField numberOfIterations = new JTextField("", 20);
        numberOfIterations.setText("100");
        menuPanel.add(numberOfIterations);

        JButton changeIterations = new JButton("Change");
        menuPanel.add(changeIterations);

        // action listener for the button that confirms the change in the number of iterations
        changeIterations.addActionListener(e -> {
            try {
                int newIterations = Integer.parseInt(numberOfIterations.getText());
                if (newIterations < 50 || newIterations > 100000)
                    throw new Exception();
                fractalPanel.setIterations(newIterations);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number of iterations! (bigger than 50 and smaller than 100000)", "Invalid number of iterations", JOptionPane.ERROR_MESSAGE);
            }
        });
        JComboBox<ImageIcon> juliaComboBox = new JComboBox<>();

        JButton saveJulia = new JButton("Save Julia");
        // action listener for the feature that saves the Julia fractal as an ImageIcon
        saveJulia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //We save the ImageIcon of the favorite Julia, but we use the complex numbered stored into an array
                //to remake the favorite julia when selected.
                favoriteJulia.add(juliaPanel.getBaseComplex());
                juliaComboBox.addItem(new ImageIcon(juliaPanel.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));
            }
        });
        // action listener that defines the selection of the ComboBox
        juliaComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                juliaPanel.setBaseComplex(favoriteJulia.get(juliaComboBox.getSelectedIndex()));
                juliaPanel.repaint();
            }
        });
        menuPanel.add(new JLabel("Favorites:"));
        JPanel juliaSavePanel = new JPanel();
        juliaSavePanel.setLayout(new GridLayout(1, 2));
        juliaSavePanel.add(saveJulia);
        juliaSavePanel.add(juliaComboBox);
        menuPanel.add(juliaSavePanel);

        //JButton which resets the fractal to default.
        JButton reset = new JButton("Reset Fractal");

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractalPanel.resetSize();
                numberOfIterations.setText("100");
            }
        });

        //Adding the Zoom/Grab options to the GUI
        menuPanel.add(new JPanel());
        menuPanel.add(reset);
        JPanel mouseSettings = new JPanel();
        JRadioButton zoomRadioButton = new JRadioButton("Zoom");
        JRadioButton grabRadioButton = new JRadioButton("Grab");
        ButtonGroup buttonGrup = new ButtonGroup();
        buttonGrup.add(zoomRadioButton);
        buttonGrup.add(grabRadioButton);

        // action listeners that aid in the selection of the mode you want to operate on the fractal

        zoomRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractalPanel.setZoom();
            }
        });
        grabRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractalPanel.setGrab();
            }
        });
        mouseSettings.setLayout(new GridLayout(1, 2));
        mouseSettings.add(zoomRadioButton);
        mouseSettings.add(grabRadioButton);
        menuPanel.add(mouseSettings);

        //Adding the menu panel to the East panel
        menuAndJulia.add(menuPanel, BorderLayout.SOUTH);
        menuPanel.setPreferredSize(new Dimension(300, 300));

        //Adding Listener to the fractal panel
        ShowComplexListener complexListener = new ShowComplexListener();
        fractalPanel.addMouseListener(complexListener);
        fractalPanel.addMouseMotionListener(complexListener);
        //Note: I made a ShowComplexListener class which implements both MouseListener and MouseMotionListener
        //because I want my listeners to be able to communicate easily between them.

        //Adding a space:
        menuPanel.add(new JPanel());

        //Adding the options for "Dynamic Julia" and "Click Julia":
        JRadioButton clickRadioButton = new JRadioButton("Click for Julia");
        JRadioButton dynamicRadioButton = new JRadioButton("Dynamic Julia");
        ButtonGroup buttonGroupJulia = new ButtonGroup();
        buttonGroupJulia.add(clickRadioButton);
        buttonGroupJulia.add(dynamicRadioButton);

        JPanel juliaButtonsPanel = new JPanel();
        juliaButtonsPanel.setLayout(new GridLayout(1, 2));
        juliaButtonsPanel.add(clickRadioButton);
        juliaButtonsPanel.add(dynamicRadioButton);

        clickRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dynamic = false;
            }
        });

        dynamicRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dynamic = true;
            }
        });

        menuPanel.add(juliaButtonsPanel);

        // JComboBox which selects the desired fractal:
        JComboBox<String> changeFractal = new JComboBox<>(new String[]{"Mandelbrot", "BurningShip", "Tricorn"});
        changeFractal.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                numberOfIterations.setText("100");
                if (e.getItem().equals("Mandelbrot")) {
                    juliaPanel.setIsMandelbrot(false);
                    juliaPanel.setIsBurningShip(true);
                    boolean isZoom;
                    isZoom = fractalPanel.getZoom();
                    fractalPanel = new Mandelbrot(DEFAULT_M_WIDTH, DEFAULT_M_HEIGHT, 100);
                    if (fractalPanel.getZoom() != isZoom)
                        fractalPanel.setZoom();
                    FractalFrame.this.add(fractalPanel, BorderLayout.CENTER);
                } else if (e.getItem().equals("BurningShip")) {
                    juliaPanel.setIsMandelbrot(false);
                    juliaPanel.setIsBurningShip(true);
                    boolean isZoom;
                    isZoom = fractalPanel.getZoom();
                    fractalPanel = new BurningShip(DEFAULT_M_WIDTH, DEFAULT_M_HEIGHT, 100);
                    if (fractalPanel.getZoom() != isZoom)
                        fractalPanel.setZoom();
                    FractalFrame.this.add(fractalPanel, BorderLayout.CENTER);
                } else if (e.getItem().equals("Tricorn")) {
                    juliaPanel.setIsMandelbrot(false);
                    juliaPanel.setIsBurningShip(false);
                    boolean isZoom;
                    isZoom = fractalPanel.getZoom();
                    fractalPanel = new Tricorn(DEFAULT_M_WIDTH, DEFAULT_M_HEIGHT, 100);
                    if (fractalPanel.getZoom() != isZoom)
                        fractalPanel.setZoom();
                    FractalFrame.this.add(fractalPanel, BorderLayout.CENTER);
                }
                fractalPanel.repaint();
                fractalPanel.update(fractalPanel.getGraphics());
                FractalFrame.this.repaint();
            }
        });

        menuPanel.add(changeFractal);
        this.setVisible(true);
        this.setResizable(false);
    }

    // mouse listener that defines all the motions or actions of the mouse and sets appropriate outcomes
    // for each case
    public class ShowComplexListener implements MouseMotionListener, MouseListener {

        int maximX, minimX, maximY, minimY;

        @Override
        public void mouseDragged(MouseEvent e) {
            //for the zoom box
            if (fractalPanel.getZoom()) {
                fractalPanel.drawMouse(e.getX(), e.getY());
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //For showing the cursor coordinates
            double x = fractalPanel.scaleWidth(e.getX());
            double y = fractalPanel.scaleHeight(e.getY());
            if (y > 0)
                coordinates.setText(new DecimalFormat("#.#######").format(x) + " - i * " + new DecimalFormat("#.#######").format(y));
            else
                coordinates.setText(new DecimalFormat("#.#######").format(x) + " + i * " + new DecimalFormat("#.#######").format(y * -1));
            //For showing the Julia dynamically
            if (dynamic) {
                juliaPanel.setBaseComplex(new Complex(x, y));
                juliaPanel.repaint();
            }

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //For showing the julia on click
            if (!dynamic) {
                juliaPanel.setBaseComplex(fractalPanel.getComplex(e.getPoint()));
                juliaPanel.repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //Start of the zoom:
            minimX = e.getX();
            minimY = e.getY();
            if (fractalPanel.getZoom()) {
                fractalPanel.setMouseMinim(minimX, minimY);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //For zooming / grabing
            maximX = e.getX();
            maximY = e.getY();
            if (fractalPanel.getZoom()) {
                //Zoom
                if (Math.abs(maximX - minimX) > 5 && Math.abs(minimY - maximY) > 5)
                    fractalPanel.zoom(minimX, minimY, maximX, maximY);
                fractalPanel.repaint();
            } else if (fractalPanel.getGrab()) {
                //Grab
                fractalPanel.setMinH(fractalPanel.scaleHeight(minimY) - fractalPanel.scaleHeight(maximY));
                fractalPanel.setMinW(fractalPanel.scaleWidth(minimX) - fractalPanel.scaleWidth(maximX));
                fractalPanel.repaint();
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public static void main(String[] args) {
        FractalFrame GUI = new FractalFrame();
    }

}
