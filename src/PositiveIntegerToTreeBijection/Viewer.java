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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.SwingConstants.RIGHT;

/**
 * Viewer for an integer as a rooted, unoriented tree.
 *
 * @author Peter Cappello
 */
public class Viewer extends JFrame 
{
    // graphical parameters
    static final int NUM_PIXELS = 1000;

    // graphical components
    private final ImagePanel imageView = new ImagePanel();
    private final JPanel numberPanel = new JPanel();
        private final JLabel numberLabel = new JLabel("Enter a positive integer & click the return key ", RIGHT);
        private final JTextField numberTextField = new JTextField(20); 
    private final JTextArea stringView = new JTextArea();
    private final JScrollPane stringViewScrollPane = new JScrollPane( stringView );
    private final JTextArea logView = new JTextArea( "1", 6, 50 );
    private final JScrollPane logViewScrollPane = new JScrollPane( logView );

    // model components
    private int number = 399;
    private PositiveIntegerToTreeBijection tree = new PositiveIntegerToTreeBijection( number );

    public static void main(String[] args) 
    {
        long startTime = System.nanoTime();
        PositiveIntegerToTreeBijection.initialize();
        Viewer viewer = new Viewer();
        viewer.initialize();
        Logger.getLogger( viewer.getClass().getCanonicalName() )
              .log(Level.INFO, "Initialization time: {0} ms.", (System.nanoTime() - startTime) / 1000000);
    }

    final static String FRAME_TITLE = "Visualize map from Natural number to rooted tree";

    private void initialize() 
    {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(numberPanel, BorderLayout.NORTH );
        container.add(imageView, BorderLayout.CENTER );
        container.add(stringViewScrollPane, BorderLayout.EAST );
        container.add(logViewScrollPane, BorderLayout.SOUTH );

        numberPanel.setLayout( new GridLayout(1, 2) );
        numberPanel.add( numberLabel );
        numberPanel.add( numberTextField );

        Dimension dimension = new Dimension( NUM_PIXELS, NUM_PIXELS + this.getHeight() );
        setSize( dimension );
        setPreferredSize( dimension );        
        stringView.setEditable( false );
        update( 399 );     
        setVisible(true);

        //  _______________________________________
        //  contoller TEMPLATE CODE for each action
        //  _______________________________________
        // Enter positive integer
        numberTextField.addActionListener(this::numberTextFieldActionPerformed);
    }

    private void update( int number )
    {
        tree = new PositiveIntegerToTreeBijection( number );
        imageView.setImage( tree.getImage() );
        imageView.repaint();
        stringView.setText( tree.viewString() );
    }

    //  _________________________
    //  contoller for each action
    //  _________________________
    private void numberTextFieldActionPerformed( ActionEvent unused ) 
    {
        String numberText = numberTextField.getText();
        try 
        {
            number = Integer.parseInt(numberText);
        } 
        catch ( NumberFormatException exception ) 
        {
            JOptionPane.showMessageDialog( this, numberText + " is not an integer.", "Input error", ERROR_MESSAGE );
        }
        update( number );
    }
}
