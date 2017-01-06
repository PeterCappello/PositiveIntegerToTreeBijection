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

import static PositiveIntegerToTreeBijection.PositiveIntegerToTreeBijection.primes;
import static PositiveIntegerToTreeBijection.PositiveIntegerToTreeBijection.ranks;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
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
    static final int IMAGE_VIEWPORT_SIZE = 800;

    // graphical components
    private final ImagePanel imageView = new ImagePanel();
    private final JScrollPane imageViewScrollPane = new JScrollPane( imageView );
    private final JPanel numberPanel = new JPanel();
        private final JLabel numberLabel = new JLabel("Enter an integer & click the return key ", RIGHT);
        private final JTextField numberTextField = new JTextField( 30 ); 
    private final JTextArea stringView = new JTextArea( 30, 20 );
    private final JScrollPane stringViewScrollPane = new JScrollPane( stringView );
    private final JPanel extras = new JPanel();
        private final JPanel primeAndRankPanel = new JPanel();
            private final JLabel rankLabel = new JLabel("What is the prime with rank (enter a rank) ", RIGHT);
            private final JTextField rankTextField = new JTextField( 30 );
            private final JTextField primeOfRankTextField = new JTextField( 30 );
            private final JLabel primeLabel = new JLabel("What is the rank of prime (enter a prime) ", RIGHT);
            private final JTextField primeTextField = new JTextField( 30 );
            private final JTextField rankOfPrimeTextField = new JTextField( 30 );
        private final JTextArea logView = new JTextArea( "1", 6, 50 );
        private final JScrollPane logViewScrollPane = new JScrollPane( logView );

    // model components
    private int number = 1;
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

    final static String FRAME_TITLE = "Visualize map from Natural numbers to rooted, unoriented trees";

    private void initialize() 
    {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container container = getContentPane();
        container.setLayout( new BorderLayout() );
        container.add(numberPanel, BorderLayout.NORTH );
        container.add(imageViewScrollPane, BorderLayout.CENTER );
        container.add(stringViewScrollPane, BorderLayout.EAST );
        container.add(extras, BorderLayout.SOUTH );
//        container.add(logViewScrollPane, BorderLayout.SOUTH );

        numberPanel.setLayout( new GridLayout( 1, 2) );
        numberPanel.add( numberLabel );
        numberPanel.add( numberTextField );
        
        extras.setLayout( new BorderLayout() );
        extras.add( primeAndRankPanel, BorderLayout.CENTER );
            primeAndRankPanel.setLayout( new GridLayout( 2, 3 ) );
            primeAndRankPanel.add( rankLabel );
            primeAndRankPanel.add( rankTextField );
            primeAndRankPanel.add( primeOfRankTextField );
            primeAndRankPanel.add( primeLabel );
            primeAndRankPanel.add( primeTextField );
            primeAndRankPanel.add( rankOfPrimeTextField );
        extras.add( logViewScrollPane, BorderLayout.SOUTH );

        Dimension dimension = new Dimension( IMAGE_VIEWPORT_SIZE, IMAGE_VIEWPORT_SIZE + this.getHeight() );
        setSize( dimension );
        setPreferredSize( dimension );        
        stringView.setEditable( false );
        update( 111111111 );     
        setVisible(true);

        //  _______________________________________
        //  contoller TEMPLATE CODE for each action
        //  _______________________________________
        // Enter an integer
        numberTextField.addActionListener(this::numberTextFieldActionPerformed);
        
        // Enter an integer > 0
        rankTextField.addActionListener(this::rankTextFieldActionPerformed);
        
        // Enter a prime
        primeTextField.addActionListener(this::primeTextFieldActionPerformed);
    }

    private void update( int number )
    {
        tree = new PositiveIntegerToTreeBijection( number );
        imageView.setImage( tree.getImageView() );
        imageView.repaint();
        stringView.setText( tree.getStringView() );
        
        imageViewScrollPane.setViewportView( new JLabel( new ImageIcon( tree.getImageView() ) ) );
    }

    //  _________________________
    //  contoller for each action
    //  _________________________
    private void numberTextFieldActionPerformed( ActionEvent unused ) 
    {
        number = getIntFromJTextField( numberTextField );
        update( number );
    }
    
    private void rankTextFieldActionPerformed( ActionEvent unused ) 
    {
        int rank = getIntFromJTextField( rankTextField );
        // !! make more robust: enlarge primes as is done elsewhere
        primeOfRankTextField.setText( primes.get( rank ).toString() );
    }
    
    private void primeTextFieldActionPerformed( ActionEvent unused ) 
    {
        int prime = getIntFromJTextField( primeTextField );
        // !! make more robust: enlarge primes as is done elsewhere
        rankOfPrimeTextField.setText( ranks.get( prime ).toString() );
    }
    
    private int getIntFromJTextField( JTextField jTextField )
    {
        String numberText = jTextField.getText();
        try 
        {
            return Integer.parseInt( numberText );
        } 
        catch ( NumberFormatException unused ) 
        {
            JOptionPane.showMessageDialog( this, numberText + " is not an integer.", "Input error", ERROR_MESSAGE );
        }
        return 0; // unreachable code to satisfy compiler.
    }
}
