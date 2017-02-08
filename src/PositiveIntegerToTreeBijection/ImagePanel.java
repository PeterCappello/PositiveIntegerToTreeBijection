package PositiveIntegerToTreeBijection;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Peter Cappello
 */
public class ImagePanel extends JPanel
{
    private BufferedImage bufferedImage;
    
//    ImagePanel() { addMouseListener( new Animation() ); }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        graphics.drawImage( bufferedImage, 0, 0, Viewer.IMAGE_VIEWPORT_SIZE, Viewer.IMAGE_VIEWPORT_SIZE, this );
        System.out.println("ImagePanel: paintComponent exiting");
    }
    
    BufferedImage image() { return bufferedImage; }
    
    void image( BufferedImage bufferedImage ) { this.bufferedImage = bufferedImage; }
}
