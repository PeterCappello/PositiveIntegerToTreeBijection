package PositiveIntegerToTreeBijection;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author Peter Cappello
 */
public class ImagePanel extends JPanel
{
    private Image image;
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        graphics.drawImage(image, 0, 0, Viewer.IMAGE_VIEWPORT_SIZE, Viewer.IMAGE_VIEWPORT_SIZE, this );
    }
    
    void setImage( Image image ) { this.image = image; }
}
