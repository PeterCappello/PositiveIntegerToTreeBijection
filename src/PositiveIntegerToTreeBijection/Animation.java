/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PositiveIntegerToTreeBijection;

import java.awt.Color;
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
    private static final int SLEEP_TIME = 1000;
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
        timer.start();
    }
    
    void newTimer() 
    { 
        timer.stop(); 
        timer = new Timer( SLEEP_TIME, this );
        timer.restart();
    }
}
