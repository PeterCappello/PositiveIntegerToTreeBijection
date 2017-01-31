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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Peter Cappello
 */
public class PrimeTimeGame extends JFrame
{
    // constants
    static final int TREE = 0;
    static final int PLANETS = 1;
    static final ViewAs[] VIEWS = { ViewAs.TREE, ViewAs.PLANETS };
    
    // graphical parameters
    static final int IMAGE_VIEWPORT_SIZE = 1000;

    // graphical components
    private final ImagePanel imageView = new ImagePanel();
    private final JPanel controlPanel = new JPanel();
    
    private final JLabel levelLabel = new JLabel( "  Level" );
    private final String[] levelArray = { "1: 1 - 2", "2: 1 - 4", "3: 1 - 8", 
        "4: 1 - 16", "5: 1 - 32", "6: 1 - 64", "7: 1 - 128", "8: 1 - 256",
        "9: 1 - 512", "10: 1 - 1024"};
    private final JComboBox<String> levelComboBox = new JComboBox<>( levelArray );
    
    private final JLabel typeLabel = new JLabel( "  Type" );
    private final String[] typeArray = { "Rounds", "Timed" };
    private final JComboBox<String> typeComboBox = new JComboBox<>( typeArray );
    
    private final JLabel limitLabel = new JLabel( "  Limit" );
    private final JTextField limitTextField = new JTextField( "  3", 10 );
    
    private final JLabel viewAsLabel = new JLabel( "  View as" );
    private final String[] viewAsArray = { "a tree", "an n-body system" };
    private final JComboBox<String> viewAsComboBox = new JComboBox<>( viewAsArray );
    
    private final JButton newGameButton = new JButton( "New game" );
    private final JButton goButton = new JButton( "GO!" );
    
    private final JLabel yourAnswerLabel = new JLabel( "  Your answer" );
    private final JTextField yourAnswerTextField = new JTextField( 10 );
    
    private final JLabel correctAnswerLabel = new JLabel( "  Correct answer" );
    private final JTextField correctAnswerTextField = new JTextField( 10 );
    
    private final JLabel roundLabel = new JLabel( "  Round" );
    private final JTextField roundTextField = new JTextField( 10 );
    
    private final JLabel scoreLabel = new JLabel( "  Score" );
    private final JTextField scoreTextField = new JTextField( 10 );
    
    private final JScrollPane imageViewScrollPane = new JScrollPane( imageView );
    
//    private final JTextArea logView = new JTextArea( "1", 6, 50 );
//    private final JScrollPane logViewScrollPane = new JScrollPane( logView );
    
    // model
    private Game game;
    private int number;
    private PositiveIntegerToTreeBijection tree;
    private int level = 4;
    private int limit = 3;
    private ViewAs viewAs = ViewAs.TREE;
    
    // media
    private URL dundunUrl;
    private URL dingSoundUrl;
    
    // animation
    private final Animation animation = new Animation();

    public static void main( String[] args ) throws UnsupportedAudioFileException, IOException 
    {
        long startTime = System.nanoTime();
        PositiveIntegerToTreeBijection.initialize();
        PrimeTimeGame primeTimeGame = new PrimeTimeGame();
        primeTimeGame.initialize();
        Logger.getLogger( primeTimeGame.getClass().getCanonicalName() )
              .log(Level.INFO, "Initialization time: {0} ms.", ( System.nanoTime() - startTime ) / 1000000 );
    }

    final static String FRAME_TITLE = "Prime Tme!";

    private void initialize() throws UnsupportedAudioFileException, IOException 
    {
        setTitle( FRAME_TITLE );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        final Container container = getContentPane();
        container.setLayout( new BorderLayout() );
        
        container.add( imageViewScrollPane, BorderLayout.CENTER );
        container.add( controlPanel, BorderLayout.NORTH );
            controlPanel.setLayout( new GridLayout( 9, 2) );
            controlPanel.add( levelLabel );
            controlPanel.add( levelComboBox );
                levelComboBox.setSelectedIndex( 2 );
                typeComboBox.setSelectedIndex( 0 );
            
            controlPanel.add( typeLabel );
            controlPanel.add( typeComboBox );
            
            controlPanel.add( limitLabel );
            controlPanel.add( limitTextField );
            
            controlPanel.add( limitLabel );
            controlPanel.add( limitTextField );
            
            controlPanel.add( viewAsLabel );
            controlPanel.add( viewAsComboBox );
            
            controlPanel.add( newGameButton );
            controlPanel.add( goButton );
            
            controlPanel.add( yourAnswerLabel );
            controlPanel.add( yourAnswerTextField );
            
            controlPanel.add( correctAnswerLabel );
            controlPanel.add( correctAnswerTextField );
            
            controlPanel.add( roundLabel );
            controlPanel.add( roundTextField );
            
            controlPanel.add( scoreLabel );
            controlPanel.add( scoreTextField );
            
            goButton.setEnabled( false );

        Dimension dimension = new Dimension( IMAGE_VIEWPORT_SIZE, IMAGE_VIEWPORT_SIZE + this.getHeight() );
        setSize( dimension );
        setPreferredSize( dimension );        
        setVisible(true);
 
        //  _______________________________________
        //  add ActionListener for each action
        //  _______________________________________
        limitTextField.addActionListener( this::limitTextFieldActionPerformed );
        newGameButton.addActionListener( this::newGameButtonActionPerformed );
        goButton.addActionListener( this::goButtonActionPerformed );        
        yourAnswerTextField.addActionListener( this::yourAnswerTextFieldActionPerformed );
        levelComboBox.addActionListener( this::levelComboBoxActionPerformed) ;
        viewAsComboBox.addActionListener( this::viewAsComboBoxActionPerformed) ;
        
        // Audio
        dundunUrl = this.getClass().getClassLoader().getResource("sounds/dun_dun.wav");
        dingSoundUrl = this.getClass().getClassLoader().getResource("sounds/196106__aiwha__ding.wav");
    }

    private void displayAsTree( int number )
    {
        tree = new PositiveIntegerToTreeBijection( number );
        imageView.setImage( tree.getImageView() );
        imageView.repaint();
//        stringView.setText( tree.getStringView() );
        
        imageViewScrollPane.setViewportView( new JLabel( new ImageIcon( tree.getImageView() ) ) );
    }
    
    private void displayAsPlanets( int number )
    {
        tree = new PositiveIntegerToTreeBijection( number );
        imageView.setImage( tree.getPlanetsView() );
        imageView.repaint();
//        stringView.setText( tree.getStringView() );
        
        imageViewScrollPane.setViewportView( new JLabel( new ImageIcon( tree.getImageView() ) ) );
    }

    //  _________________________
    //  contoller for each action
    //  _________________________
    private void levelComboBoxActionPerformed( ActionEvent unused ) 
    {
        level = 1 + levelComboBox.getSelectedIndex();
    }
    
    
    private void limitTextFieldActionPerformed( ActionEvent unused ) 
    {
        try 
        {
            limit = getIntFromJTextField( limitTextField );
        }
        catch ( IllegalArgumentException ex ) {}
    }
    
    private void viewAsComboBoxActionPerformed( ActionEvent unused ) 
    {
        viewAs = VIEWS[ viewAsComboBox.getSelectedIndex() ];
    }
    
    private void newGameButtonActionPerformed( ActionEvent unused ) 
    {
        game = new Game( level, true, limit );
        roundTextField.setText( String.valueOf( 0 ) );
        scoreTextField.setText( String.valueOf( 0 ) );
        goButton.setText( "Go!" );
        goButton.setEnabled( true );
        yourAnswerTextField.setText( "" );
        correctAnswerTextField.setText( "" );
        Image image = new BufferedImage( 800, 800, BufferedImage.TYPE_INT_ARGB );
        imageView.setImage( image );
        imageView.repaint();
        imageViewScrollPane.setViewportView( new JLabel( new ImageIcon( image ) ) );
        goButton.requestFocusInWindow();
        
        // conversion from mp3 to wav: http://audio.online-convert.com/convert-to-wav
        try
        {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(dundunUrl );
            Clip clip = AudioSystem.getClip();
            clip.open( audioIn );
            clip.start();
        }
        catch( IOException | LineUnavailableException | UnsupportedAudioFileException ex )
        {
            // !! log not print.
            ex.printStackTrace();
        }
    }
    
    private void goButtonActionPerformed( ActionEvent unused ) 
    {
        number = game.next();
        displayAsTree( number );
//        displayAsPlanets( number );
        roundTextField.setText( String.valueOf( game.round() ) );
        yourAnswerTextField.setText( "" );
        correctAnswerTextField.setText( "" );
        yourAnswerTextField.requestFocusInWindow();
        try
        {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream( dingSoundUrl );
            Clip clip = AudioSystem.getClip();
            clip.open( audioIn );
            clip.start();
        }
        catch( IOException | LineUnavailableException | UnsupportedAudioFileException ex )
        {
            // !! log not print.
            ex.printStackTrace();
        }
    }
    
    private void yourAnswerTextFieldActionPerformed( ActionEvent unused ) 
    {
        try 
        {
            int yourAnswer = getIntFromJTextField( yourAnswerTextField );
            correctAnswerTextField.setText( String.valueOf( number ) );
            scoreTextField.setText( String.valueOf( game.processAnswer( yourAnswer == number ) ));
            if ( game.isOver() )
            {
                goButton.setText( "Game over" );
                goButton.setEnabled( false );
                newGameButton.requestFocusInWindow();
            }
            else
            {
                goButton.requestFocusInWindow();
            }
        }
        catch ( IllegalArgumentException ex ) {}
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
}
