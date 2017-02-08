package PositiveIntegerToTreeBijection;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static java.lang.Thread.sleep;
import javax.swing.JPanel;

/**
 *
 * @author Peter Cappello
 */
public class ImagePanel extends JPanel
{
    private BufferedImage bufferedImage;
    
    ImagePanel()
    {
        addMouseListener( new Animation() );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        graphics.drawImage( bufferedImage, 0, 0, Viewer.IMAGE_VIEWPORT_SIZE, Viewer.IMAGE_VIEWPORT_SIZE, this );
        System.out.println("ImagePanel: paintComponent exiting");
    }
    
    BufferedImage image() { return bufferedImage; }
    
    void image( BufferedImage bufferedImage ) { this.bufferedImage = bufferedImage; }
    
    class Animation extends MouseAdapter implements Runnable
    {    
        private int SLEEP_TIME = 500;
        private Tree tree;
        private ImagePanel imagePanel;
        private AnimationRunnable animationRunnable;

        void init( Tree tree, ImagePanel imagePanel )
        {
            this.tree = tree;
            this.imagePanel = imagePanel;
            System.out.println("Animation constructed and started.");
        }
        
        @Override
        public void mousePressed(MouseEvent e) 
        {                       
                animationRunnable = new AnimationRunnable();
        }

        @Override
        public void run() {

            while ( true ) 
            {
                image( tree.getPlanetsView() );
                repaint();
                try 
                {
                    Thread.sleep( 500 );
                } catch (InterruptedException ex) {}
            }
        }
        
//        @Override
//        public void run()
//        {
//            System.out.println("Animation run method entered.");
//            while ( true )
//            {
//                System.out.println("Iteration started.");
//                imagePanel.image( tree.getPlanetsView() );
//                System.out.println("Animation: imagePanel set.");
//                imagePanel.repaint();
//                System.out.println("Animation: repaint finished.");
//                try 
//                {
//                    sleep( SLEEP_TIME );
//                } catch ( InterruptedException ignore ) { System.out.println( "InterruptedException caught");}
//            }
//        }
    }
    
    class AnimationRunnable implements Runnable {

        private Thread runner;

        public AnimationRunnable() 
        {
            initThread();
        }
        
        private void initThread() {
            
            runner = new Thread(this);
            runner.start();
        }

        @Override
        public void run() {

            while ( true) 
            {                
                repaint();
                try 
                {
                    Thread.sleep( 500 );
                } catch (InterruptedException ex) {}
            }
        }
    }
}
