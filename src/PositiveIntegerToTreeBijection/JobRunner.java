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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * The class used to "run" the Job.
 * @author Peter Cappello
 * @param <T> type of value returned by value.
 */
public class JobRunner<T> extends JFrame
{
    public static void main( String[] args )
    {
        new JobRunner( FRAME_TITLE, args ).run( new JLabel( "Hello, world!" ) );
    }
    
    final static String FRAME_TITLE = "Hello, world!";
    final private long   startTime = System.nanoTime();
    
    /**
     *
     * @param title the String to be displaced on the JPanel containing the JLabel.
     * @param args command line args - 0th element is Space domain name.
     */
    public JobRunner( String title, String[] args ) 
    { 
        setTitle( title );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }
    
    /**
     * Run the Job: Generate the tasks, retrieve the results, compose a solution
     * to the original problem, and display the solution.
     * the remote service is not responding
     * @param jLbel
     */
    public void run( JLabel jLbel )
    {
        view( jLbel );
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
