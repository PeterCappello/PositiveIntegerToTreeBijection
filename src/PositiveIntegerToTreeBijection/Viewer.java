/*
 * The MIT License
 *
 * Copyright 2015 peter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package PositiveIntegerToTreeBijection;

import static PositiveIntegerToTreeBijection.PositiveIntegerToTreeBijection.setPrimesArray;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The class used to "run" the Job.
 *
 * @author Peter Cappello
 */
public class Viewer extends JFrame {

    // graphical parameters
    static final int NUM_PIXELS = 1000;
    private static final int ELEMENT = 8;
    private static final int RADIUS = ELEMENT;
    private static final int PAD = 3 * ELEMENT;
    private static final int DELTA = 2 * (PAD + RADIUS);
    private static final int DIAMETER = 2 * RADIUS;

    // graphical components
    private final TreePanel treeView = new TreePanel();
    private final JPanel numberPanel = new JPanel();
        private final JLabel numberLabel = new JLabel("Enter a positive integer & click the return key ");
        private final JTextField numberTextField = new JTextField(20); 
    private final JTextArea stringView = new JTextArea();
    private final JScrollPane stringViewScrollPane = new JScrollPane( stringView );

    private Image image = new BufferedImage( NUM_PIXELS, NUM_PIXELS, BufferedImage.TYPE_INT_ARGB );

    // model components
    private int number = 1;
    private PositiveIntegerToTreeBijection tree = new PositiveIntegerToTreeBijection(399);

    public static void main(String[] args) 
    {
        long startTime = System.nanoTime();
        setPrimesArray(100);
        Viewer viewer = new Viewer();
        viewer.initialize();
        Logger.getLogger(viewer.getClass().getCanonicalName())
                .log(Level.INFO, "Job run time: {0} ms.", (System.nanoTime() - startTime) / 1000000);
    }

    final static String FRAME_TITLE = "Visualize map from Natural number to rooted tree";

    private void initialize() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(numberPanel, BorderLayout.NORTH );
        container.add(treeView, BorderLayout.CENTER );
        container.add(stringViewScrollPane, BorderLayout.SOUTH );

        numberPanel.setLayout( new GridLayout(1, 2) );
        numberPanel.add( numberLabel );
        numberPanel.add( numberTextField );

        Dimension dimension = new Dimension( NUM_PIXELS, NUM_PIXELS + this.getHeight() );
        setSize( dimension  );
        setPreferredSize( dimension );
        setVisible(true);

        tree = new PositiveIntegerToTreeBijection(399);
        view( NUM_PIXELS / 2, PAD );
        treeView.setImage( image );
        treeView.repaint();

        //  _______________________________________
        //  contoller TEMPLATE CODE for each action
        //  _______________________________________
        // Enter positive integer
        numberTextField.addActionListener(this::numberTextFieldActionPerformed);
    }

    //  _________________________
    //  contoller for each action
    //  _________________________
    private void numberTextFieldActionPerformed( ActionEvent actionEvent) 
    {
        String numberText = numberTextField.getText();
        try {
            number = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            Logger.getLogger(this.getClass().getCanonicalName())
                    .log(Level.INFO, "Not an integer: {0}", (number));
        }
        if (number < 1) {
            Logger.getLogger(this.getClass().getCanonicalName())
                    .log(Level.INFO, "Integer {0} < 1 is not allowed.", (number));
            return;
        }

        tree = new PositiveIntegerToTreeBijection( number );
        view( NUM_PIXELS / 2, PAD );
        treeView.setImage( image );
        treeView.repaint();
        stringView.append( tree.toString() );
    }

    void view(int x, int y) 
    {
        image = new BufferedImage(NUM_PIXELS, NUM_PIXELS, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);

        // coordinates of center of root
        int rootX = x + tree.rootX();
        int rootY = y + tree.rootY();

        // draw root
        drawDisk(graphics, rootX, rootY);

        // set 1st factor tree's x and y 
        int factorTreeX = x;
        int factorTreeY = y + DELTA;

        for (PositiveIntegerToTreeBijection factorTree : tree.factorTrees()) {
            // draw edge from this root to factor tree's root
            graphics.drawLine(rootX, rootY, factorTreeX + factorTree.rootX(), factorTreeY + factorTree.rootY());

            // draw factor tree
            factorTree.view(graphics, factorTreeX, factorTreeY);

            factorTreeX += DELTA * factorTree.width(); // set next factor tree's x ccordinate
        }
    }

    private void drawDisk(Graphics graphics, int x, int y) {
        graphics.fillOval(x - RADIUS, y - RADIUS, DIAMETER, DIAMETER);
    }
}
