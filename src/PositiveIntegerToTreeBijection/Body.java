package PositiveIntegerToTreeBijection;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

/**
 *
 * @author Pete Cappello
 */
public class Body 
{

    /**
     *
     */
    public final static boolean SHOW_ORBIT = false;

    private final int diameter;       // diameter of this body
    private final double orbitRadius; // radius of its orbit
    private final double stepSize;    // amount of radians incremented per time step
    private final Color color;        // of body
    private final Body parent;        // this orbits around parent
    private final List<Body> satelliteList;
    private int x, y;                 // location of this body
    private double orbitPosition;     // orbit angular position in radians

    Body( Body parent, int diameter, double orbitRadius, double stepSize, Color color ) 
    {     
        this.satelliteList = new LinkedList<>();
        this.parent = parent;
        this.diameter = diameter;
        this.orbitRadius = orbitRadius;
        this.stepSize = stepSize;
        this.color = color;
    }

    // invoked only on root body
    Body( int x, int y, int diameter ) 
    { 
        this.satelliteList = new LinkedList<>();
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        orbitRadius = 0.0;
        stepSize = 0.0;
        color = Color.BLACK;
        parent = null;
    }

    /**
     * Add a satellite to the List of satellites for this Body.
     * @param parent 
     */
    void addSatellite( Body parent ) { satelliteList.add( parent ); }
    
    void move() 
    {
        // move this Body
        orbitPosition += stepSize;
        if ( orbitPosition > 2 * Math.PI )
        {
            orbitPosition -= 2 * Math.PI;
        }
        x = parent.x + parent.diameter/2 - diameter/2 + (int) ( orbitRadius * cos( orbitPosition ) );
        y = parent.y + parent.diameter/2 - diameter/2 + (int) ( orbitRadius * sin( orbitPosition ) );
        
        // move my sateillites
        satelliteList.forEach( Body::move );
    }

     public void draw( Graphics graphics )
     {
         // draw this Body
         if ( SHOW_ORBIT ) 
         {
             graphics.setColor( Color.WHITE );
             graphics.drawOval( parent.x - (int) orbitRadius + parent.diameter/2,
                                parent.y - (int) orbitRadius + parent.diameter/2,
                                2 * (int) orbitRadius,
                                2 * (int) orbitRadius
                              );
         }
         graphics.setColor( color );
         graphics.fillOval( x, y, diameter, diameter );
         
        // draw my satellites
        satelliteList.forEach( body -> body.draw( graphics ) );
     }
}
