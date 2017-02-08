/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class TimerBasedAnimation extends JPanel implements ActionListener 
{
    private final Timer timer;
    private Tree tree = new Tree( 36 );
    private int xx = 0;
    private int yy = 0;

    public TimerBasedAnimation() 
    {
      timer = new Timer( 500, this );
      timer.setInitialDelay( 0 );
      timer.start();
    }

    @Override
    public void paint( Graphics graphics ) 
    {
      super.paintComponent( graphics );
      tree.viewPlanets( graphics, xx, yy);
    }

    @Override
    public void actionPerformed( ActionEvent unused ) 
    {
        xx += 10;
        yy += 20;
      repaint();
    }
    
    void newAnimation( Tree tree )
    {
        this.tree = tree;
    }
}
