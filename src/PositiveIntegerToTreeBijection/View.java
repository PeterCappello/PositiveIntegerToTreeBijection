package PositiveIntegerToTreeBijection;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author Peter Cappello
 */
public class View extends JPanel
{
    private Image image;
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        graphics.drawImage( image, 0, 0, Viewer.NUM_PIXELS, Viewer.NUM_PIXELS, this );
    }
    
    void setImage( Image image ) { this.image = image; }
}
