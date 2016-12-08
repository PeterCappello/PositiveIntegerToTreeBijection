package PositiveIntegerToTreeBijection;

import static PositiveIntegerToTreeBijection.Viewer.NUM_PIXELS;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Recursively maps a natural number to a rooted, un-oriented tree.
 * @author Pete Cappello
 */
public class PositiveIntegerToTreeBijection
{   
    public static int[] primes;
    
    // cache of PositiveIntegerTree objects
    private static Map<Integer, PositiveIntegerToTreeBijection> integerToPositiveIntegerTreeMap = new HashMap<>();
    
    /**
     * Fill the primes array with the first numPrimes prime numbers.
     * The index of a prime is its rank: primes[ 0 ] is UNUSED.
     * @param numPrimes the number of elements in primes
     */
    public static void setPrimesArray( int numPrimes )
    {
        primes = new int[ numPrimes + 1 ];
        primes[ 1 ] = 2;
        primes[ 2 ] = 3;
        for ( int number = 5, rank = 3; rank < numPrimes; number += 2 )
        {
            if ( isPrime( number ) )
            {
                primes[ rank++ ] = number;
            }
        }
    }
    
    /**
     * Determines whether ODD integer number is prime.
     * @param number an ODD integer
     * @return true if and only if number is prime
     */
    public static boolean isPrime( final int number )
    {
        final int maxFactor = (int) Math.sqrt( number );
        for ( int rank = 1; primes[ rank ] <= maxFactor; rank++ )
        {
            if ( number % primes[ rank ] == 0 )
            {
                return false;
            }
        }
        return true;
    }
    
    private final int positiveInteger;
    private List<PositiveIntegerToTreeBijection> factorTrees = new LinkedList<>();
    private int height;
    private int width;

    List<PositiveIntegerToTreeBijection> factorTrees() { return factorTrees; }
    
    PositiveIntegerToTreeBijection( PositiveIntegerToTreeBijection positiveIntegerTree )
    {
        this.positiveInteger = positiveIntegerTree.positiveInteger;
        this.factorTrees     = positiveIntegerTree.factorTrees;
    }
    
    /**
     * Construct the tree that corresponds to a particular natural number.
     * @param positiveInteger whose corresponding tree is being constructed 
     */
    PositiveIntegerToTreeBijection( int positiveInteger ) throws ArrayIndexOutOfBoundsException
    {
        this.positiveInteger = positiveInteger;
        PositiveIntegerToTreeBijection cachedTree = integerToPositiveIntegerTreeMap.get( positiveInteger );
        if ( cachedTree != null )
        {
            factorTrees = cachedTree.factorTrees;
            height      = cachedTree.height;
            width       = cachedTree.width;
            return;
        }
        int subTreeWidthSum = 0; // initialize width calculation
        for ( int rank = 1; primes[ rank ] <= positiveInteger && positiveInteger > 1; rank++ )
        {
            // for each prime factor, create a subtree for the prime factor's rank
            for ( ; positiveInteger % primes[ rank ] == 0; positiveInteger /= primes[ rank ] )
            {
                PositiveIntegerToTreeBijection positiveIntegerTree = integerToPositiveIntegerTreeMap.get( rank );
                if ( positiveIntegerTree == null )
                {
                    // no cached tree for this factor, make one
                    positiveIntegerTree = new PositiveIntegerToTreeBijection( rank );
                }
                factorTrees.add( positiveIntegerTree );
                
                // update width & height calculation
                subTreeWidthSum += positiveIntegerTree.width;
                if ( positiveIntegerTree.height > height )
                {
                     height = positiveIntegerTree.height;
                }
            }
        }
        
        // complete width & height calculation
        width = ( 1 < subTreeWidthSum ) ? subTreeWidthSum : 1;
        height++;
      
        // cache tree
        integerToPositiveIntegerTreeMap.put( this.positiveInteger, this );
    }
    
    public int getPositiveInteger() { return positiveInteger; }
    
    public String stringView() { return new String( viewString( "   ") ); }

    private StringBuilder viewString( String pad )
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( pad ).append( '\n' ).append( pad ).append( positiveInteger ).append( " ");
        stringBuilder.append( positiveInteger < primes.length ? primes[ positiveInteger ] : "" );
        if ( ! factorTrees.isEmpty() )
        {
            for ( PositiveIntegerToTreeBijection factorTree : factorTrees )
            {
                stringBuilder.append(factorTree.viewString( pad + "   ") );
            }
        }
        return stringBuilder;
    }
    
    private StringBuilder debug( String pad )
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( pad ).append( '\n' ).append( pad ).append( "Tree of prime whose rank is ");
        stringBuilder.append( positiveInteger ).append( " ");
        stringBuilder.append( positiveInteger < primes.length ? primes[ positiveInteger ] : "" );
        stringBuilder.append( " height: ").append( height );
        stringBuilder.append( " width: ").append( width );
        if ( ! factorTrees.isEmpty() )
        {
            for ( PositiveIntegerToTreeBijection factorTree : factorTrees )
            {
                stringBuilder.append(factorTree.viewString( pad + "   ") );
            }
        }
        return stringBuilder;
    }
    
    // viewGraphics parameters, in pixels
    private static final int NUM_PIXELS = 1000;
    private static final int ELEMENT  = 8; 
    private static final int RADIUS   = ELEMENT; 
    private static final int PAD      = 3 * ELEMENT; 
    private static final int DELTA    = 2 * ( PAD + RADIUS );
    private static final int DIAMETER = 2 * RADIUS;

    /**
     *
     * @param graphics of image on which tree is rendered
     * @param x col of upper left corner of rectangle containing tree
     * @param y row of upper left corner of rectangle containing tree
     */
    public void viewGraphics( Graphics graphics, int x, int y )
    {
        graphics.setColor( Color.BLACK );
                       
        // coordinates of center of root
        int rootX = x + rootX();
        int rootY = y + rootY();

        // draw root
        drawDisk( graphics, rootX, rootY );
        
        // set 1st factor tree's x and y 
        int factorTreeX = x;
        int factorTreeY = y + DELTA;
        
        for ( PositiveIntegerToTreeBijection factorTree : factorTrees )
        {
            // draw edge from this root to factor tree's root
            graphics.drawLine( rootX, rootY, factorTreeX + factorTree.rootX(), factorTreeY + factorTree.rootY() );
            
            // draw factor tree
            factorTree.viewGraphics( graphics, factorTreeX, factorTreeY ); 
            
            factorTreeX += DELTA * factorTree.width; // set next factor tree's x ccordinate
        }
    }

    /**
     *
     * @return width in pixels of rectangle enclosing image of tree
     */
    public int imageViewWidth() { return width() * DELTA; }
    
    private void drawDisk( Graphics graphics, int x, int y )
    {
        graphics.fillOval( x - RADIUS, y - RADIUS, DIAMETER, DIAMETER );
    }
    
    int rootX() { return width * DELTA / 2 - RADIUS; }
    
    int rootY() { return PAD + RADIUS; }
    
    //_____________ Methods For Unit Testing ______________________
    public List<PositiveIntegerToTreeBijection> getFfactorTrees() { return factorTrees; } 
    
    int height() { return height; }
    
    int width() { return width; }
}
