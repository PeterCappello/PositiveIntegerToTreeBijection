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

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 * @author Peter Cappello
 */
public class Animation extends JPanel implements ActionListener 
{
    private static final int SLEEP_TIME = 20;
    private Timer timer;
    private Tree tree;
    
    public Animation() 
    {
      timer = new Timer( SLEEP_TIME, this );
      timer.start();
    }

    @Override
    public void paint( Graphics graphics ) 
    {
      super.paintComponent( graphics );
      tree.viewPlanets( graphics );
    }

    @Override
    public void actionPerformed( ActionEvent unused ) { repaint(); }
    
    /**
     * Not provably thread-safe. Stops sending repaint events for SLEEP_TIME ms.
     * Then sets tree to new tree. Then restarts timer.  There is no guarantee 
     * that previously sent events (which reference tree) have completed. If not,
     * there could be a race condition on tree.
     * 
     * To make it thread-safe, synchronize all references to tree.
     * @param tree 
     */
    void newAnimation( Tree tree ) 
    { 
        timer.stop();
        this.tree = tree;
//        timer.start();
        timer = new Timer( SLEEP_TIME, this );
        timer.restart();
    }
    
//    void newTimer() 
//    { 
//        timer.stop(); 
//        timer = new Timer( SLEEP_TIME, this );
//        timer.restart();
//    }
}
