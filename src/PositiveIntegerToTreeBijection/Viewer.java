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

import static PositiveIntegerToTreeBijection.Tree.prime;
import static PositiveIntegerToTreeBijection.Tree.rank;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
 * Viewer for an integer as a rooted tree.
 *
 * @author Peter Cappello
 */
public class Viewer extends JFrame 
{
    // graphical parameters
    static final int IMAGE_VIEWPORT_SIZE = 800;

    // graphical components
    private final Animation timerBasedAnimation = new Animation();
    private final JScrollPane animationScrollPane = new JScrollPane( timerBasedAnimation );
    private final ImagePanel imageView = new ImagePanel();
    private final JScrollPane imageViewScrollPane = new JScrollPane( imageView );
    private final JPanel numberPanel = new JPanel();
        private final JLabel numberLabel = new JLabel("Enter an integer & click the return key ", RIGHT);
        private final JTextField numberTextField = new JTextField( 30 );
        private JButton saveButton = new JButton( "Save" );
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
    private int number;
    private Tree tree;
    
    public static void main(String[] args) 
    {
        long startTime = System.nanoTime();
        Tree.initialize();
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
        
        // Animation does not display w/o these 2 statements.
        timerBasedAnimation.setPreferredSize( new Dimension( IMAGE_VIEWPORT_SIZE, IMAGE_VIEWPORT_SIZE ) );
        animationScrollPane.setPreferredSize( new Dimension( IMAGE_VIEWPORT_SIZE, IMAGE_VIEWPORT_SIZE ) );
        
        container.add( animationScrollPane, BorderLayout.WEST );
        container.add(extras, BorderLayout.SOUTH );

        numberPanel.setLayout( new GridLayout( 1, 3 ) );
        numberPanel.add( numberLabel );
        numberPanel.add( numberTextField );
        numberPanel.add( saveButton );

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

        Dimension dimension = new Dimension( 2 * IMAGE_VIEWPORT_SIZE + this.getHeight(), 2 * IMAGE_VIEWPORT_SIZE );
        setSize( dimension );
        setPreferredSize( dimension );        
        stringView.setEditable( false );
//        update( 111111111 ); 
        update( -7 );
        setVisible(true);

        //  _______________________________________
        //  contoller TEMPLATE CODE for each action
        //  _______________________________________
        // Enter a non-zero integer
        numberTextField.addActionListener(this::numberTextFieldActionPerformed);
        
        // Enter a non-zero integer
        saveButton.addActionListener(this::saveButtonActionPerformed);
        
        // Enter an integer > 0
        rankTextField.addActionListener(this::rankTextFieldActionPerformed);
        
        // Enter a prime
        primeTextField.addActionListener(this::primeTextFieldActionPerformed);
    }

    private void update( int number )
    {
        tree = new Tree( number );
        stringView.setText( tree.getStringView() );
        imageView.image( tree.getCircularTreeView() );
//        imageView.image( tree.getTreeView() );
        imageViewScrollPane.setViewportView( new JLabel( new ImageIcon( imageView.image() ) ) );
//        timerBasedAnimation.newAnimation( tree );
    }

    //  _________________________
    //  contoller for each action
    //  _________________________
    private void numberTextFieldActionPerformed( ActionEvent unused ) 
    {
        try
        {
            number = getIntFromJTextField( numberTextField );
        } 
        catch ( IllegalArgumentException ex ) { return; }
        update( number );
    }
    
    private void rankTextFieldActionPerformed( ActionEvent unused ) 
    {
        try
        {
            int rank = getIntFromJTextField( rankTextField );
            if ( rank > 0 )
            {
                primeOfRankTextField.setText( Integer.toString( prime( rank ) ) );
            }
            else
            {
                JOptionPane.showMessageDialog( this, rank + " is not positive; it must be > 0.", "Input error", ERROR_MESSAGE );
            }
        }
        catch ( IllegalArgumentException ex ) { return; }
    }
    
    private void primeTextFieldActionPerformed( ActionEvent unused ) 
    {
        try 
        {
            int prime = getIntFromJTextField( primeTextField );
            try
            {
                rankOfPrimeTextField.setText( Integer.toString( rank( prime ) ) );
            }
            catch ( IllegalArgumentException ex ) 
            { 
                JOptionPane.showMessageDialog( this, prime + " is not a prime number.", "Input error", ERROR_MESSAGE );
            }
        }
        catch ( IllegalArgumentException ex ) { return; }
    }
    
    private int getIntFromJTextField( JTextField jTextField ) throws IllegalArgumentException
    {
        String numberText = jTextField.getText();
        try 
        {
            return Integer.parseInt( numberText );
        } 
        catch ( NumberFormatException unused ) 
        {
            JOptionPane.showMessageDialog( this, numberText + " is not an integer.", "Input error", ERROR_MESSAGE );
            throw new IllegalArgumentException();
        }
    }
    
    private void saveButtonActionPerformed( ActionEvent actionEvent )
    {
        BufferedImage bufferedImage = labelImage();
 
        File file = null;
        JFileChooser fileChooser = new JFileChooser( file );
        int returnValue = fileChooser.showDialog( this, "Save");
        if ( returnValue == JFileChooser.APPROVE_OPTION )
        {
            File imageFile = fileChooser.getSelectedFile();
            try
            {
                ImageIO.write( (RenderedImage)imageView.image(), "png", imageFile );
            }
            catch ( IOException ioException )
            {
                ioException.printStackTrace();
            }
        }
        imageView.image( bufferedImage ); // ?? Unnecessaary
        imageView.repaint();
    }
    
    private BufferedImage labelImage()
    {
        // augment image with number of imaged tree.
        BufferedImage bufferedImage = imageView.image();
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor( Color.BLACK );
        Map<TextAttribute, Object> textAttributes = new HashMap<>();
        textAttributes.put(TextAttribute.FAMILY, graphics.getFont().getFamily());
        textAttributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        textAttributes.put(TextAttribute.SIZE, (int) (graphics.getFont().getSize() * 1.4));
        Font font = Font.getFont( textAttributes );
        graphics.setFont( font );
        String numberString = tree.n().toString();
        graphics.drawString( numberString, 0, 20 );
        return bufferedImage;
    }
}
