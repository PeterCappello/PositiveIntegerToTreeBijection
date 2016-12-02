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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * The class used to "run" the Job.
 * @author Peter Cappello
 */
public class Viewer extends JFrame
{
    private static final int NUM_PIXELS = 1000;
    private static final int ELEMENT  = 8; 
    private static final int RADIUS   = ELEMENT; 
    private static final int PAD      = 3 * ELEMENT; 
    private static final int DELTA    = 2 * ( PAD + RADIUS );
    private static final int DIAMETER = 2 * RADIUS;

    private int height;
    private int width;

    public static void main( String[] args )
    {
        setPrimesArray( 100 );
        PositiveIntegerToTreeBijection tree = new PositiveIntegerToTreeBijection( 399 );
        Viewer viewer = new Viewer( tree );
        
        final Image image = new BufferedImage( NUM_PIXELS, NUM_PIXELS, BufferedImage.TYPE_INT_ARGB );
        final Graphics graphics = image.getGraphics();
        graphics.setColor( Color.black );
        viewer.view( graphics, tree, NUM_PIXELS / 2, NUM_PIXELS / 2 );
        final ImageIcon imageIcon = new ImageIcon( image );
        viewer.run( new JLabel( imageIcon ) );
//        new Viewer( FRAME_TITLE, args ).run( new JLabel( imageIcon ) );
    }
    
    final static String FRAME_TITLE = "Visualize map from Natural number to rooted tree";
    final private long   startTime = System.nanoTime();
    
    /**
     *
     * @param tree
     */
    public Viewer( PositiveIntegerToTreeBijection tree ) 
    { 
        setTitle( FRAME_TITLE );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }

    void view( Graphics graphics, PositiveIntegerToTreeBijection tree, int x, int y )
    {
        graphics.setColor( Color.BLACK );
                
        // coordinates of center of root
        int rootX = x + tree.rootX();
        int rootY = y + tree.rootY();
        
        // draw root
        drawDisk( graphics, rootX, rootY );
        
        // set 1st factor tree's x and y 
        int factorTreeX = x;
        int factorTreeY = y + DELTA;
        
        for ( PositiveIntegerToTreeBijection factorTree : tree.factorTrees() )
        {
            // draw edge from this root to factor tree's root
            graphics.drawLine( rootX, rootY, factorTreeX + factorTree.rootX(), factorTreeY + factorTree.rootY() );
            
            // draw factor tree
            factorTree.view( graphics, factorTreeX, factorTreeY ); 
            
            factorTreeX += DELTA * factorTree.width(); // set next factor tree's x ccordinate
        }
    }

    private void drawDisk( Graphics graphics, int x, int y )
    {
        graphics.fillOval( x - RADIUS, y - RADIUS, DIAMETER, DIAMETER );
    }
      
    /**
     * Run the Job: Generate the tasks, retrieve the results, compose a solution
     * to the original problem, and display the solution.
     * the remote service is not responding
     * @param jLabel
     */
    public void run( JLabel jLabel )
    {
        view( jLabel );
        Logger.getLogger( this.getClass().getCanonicalName() )
              .log( Level.INFO, "Job run time: {0} ms.", ( System.nanoTime() - startTime ) / 1000000 );
    }
    
    private void view( final JLabel jLabel )
    {
        final Container container = getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( new JScrollPane( jLabel ), BorderLayout.CENTER );
        pack();
        setVisible( true );
    }
}
