package PositiveIntegerToTreeBijection;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Recursively maps a natural potentialFactor to a rooted, un-oriented tree.
 * @author Pete Cappello
 */
public class PositiveIntegerToTreeBijection
{   
    private static final int PRIMES_INITIAL_CAPACITY = 100;

    /**
     * List of first PRIMES_INITIAL_CAPACITY prime numbers
     */
    public static List<Integer> primes = new ArrayList<>( PRIMES_INITIAL_CAPACITY );
    
    // cache of PositiveIntegerTree objects
    private static Map<Integer, PositiveIntegerToTreeBijection> integerToPositiveIntegerTreeMap = new HashMap<>();
    
    /**
     * Fill the primes array with the first PRIMES_INITIAL_CAPACITY prime numbers.
     * The index of a prime is its potentialFactorRank: primes.get( 0 ) is UNUSED.
     */
    public static void setPrimesArray()
    {
        primes.add( 1 );
        primes.add( 2 );
        primes.add( 3 );
        for ( int number = 5, rank = 3; rank < PRIMES_INITIAL_CAPACITY; number += 2 )
        {
            if ( isPrime( number ) )
            {
                primes.add( number);
                rank++;
            }
        }
    }
    
    /**
     * Determines whether ODD integer potentialFactor is prime.
     * @param number an ODD integer
     * @return true if and only if potentialFactor is prime
     */
    public static boolean isPrime( final int number )
    {
        final int maxFactor = (int) Math.sqrt( number );
        for ( int rank = 1; primes.get( rank ) <= maxFactor; rank++ )
        {
            if ( number % primes.get( rank ) == 0 )
            {
                return false;
            }
        }
        return true;
    }
    
    private final boolean isPositive;
    private final int positiveInteger;
    private List<PositiveIntegerToTreeBijection> factorTrees = new LinkedList<>();
    private int height;
    private int width;
    
    PositiveIntegerToTreeBijection( PositiveIntegerToTreeBijection positiveIntegerTree )
    {
        this.isPositive = positiveIntegerTree.isPositive;
        this.positiveInteger = positiveIntegerTree.positiveInteger;
        this.factorTrees     = positiveIntegerTree.factorTrees;
    }
    
    /**
     * Construct the tree that corresponds to a particular natural potentialFactor.
     * @param integer whose corresponding tree is being constructed 
     */
    PositiveIntegerToTreeBijection( int integer ) throws ArrayIndexOutOfBoundsException
    {
        isPositive = integer > 0;
        positiveInteger = ( integer > 0 ) ? integer : -integer;
        PositiveIntegerToTreeBijection cachedTree = integerToPositiveIntegerTreeMap.get( positiveInteger );
        if ( cachedTree != null )
        {
            factorTrees = cachedTree.factorTrees;
            height      = cachedTree.height;
            width       = cachedTree.width;
            return;
        }
        
        // for each factor, make a factor tree
        int subTreeWidthSum = 0; // initialize width calculation
        int potentialFactorRank = 1;
        int number = positiveInteger;
        for (int potentialFactor = primes.get(potentialFactorRank); potentialFactor <= number; potentialFactor = primes.get(++potentialFactorRank) )
        {
            for ( ; number % potentialFactor == 0; number /= potentialFactor )
            {
                PositiveIntegerToTreeBijection numberTree = integerToPositiveIntegerTreeMap.get(potentialFactorRank );
                if ( numberTree == null )
                {
                    // no cached tree for this potentialFactor, make one
                    numberTree = new PositiveIntegerToTreeBijection( potentialFactorRank );
                    integerToPositiveIntegerTreeMap.put( potentialFactorRank, numberTree );
                }
                factorTrees.add( numberTree );
                
                // update width & height calculation
                subTreeWidthSum += numberTree.width;
                if ( numberTree.height > height )
                {
                     height = numberTree.height;
                }
            }
        }
        
        // complete width & height calculation
        width = ( 1 < subTreeWidthSum ) ? subTreeWidthSum : 1;
        height++;
      
        // cache tree
        integerToPositiveIntegerTreeMap.put( positiveInteger, this );
    }
    
    List<PositiveIntegerToTreeBijection> factorTrees() { return factorTrees; }
    
    // refactor using binary search from sqrt to approximately number/log number
    private int rank( int number )
    {
        try
        {
           for ( int rank = 1; primes.get( rank ) <= number; rank++ )
            {
                if ( number == primes.get( rank ) )
                {
                    return rank;
                }
            } 
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        System.out.println("rank method failed.");
        return -1;
    }
    
    public int getPositiveInteger() { return positiveInteger; }
    
    public String viewString() { return new String( PositiveIntegerToTreeBijection.this.viewString( "   ") ); }

    private StringBuilder viewString( String pad )
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append( pad ).append( '\n' ).append( pad )
                .append( isPositive ? "" : "-")
                .append( positiveInteger ).append( " ")
                .append( positiveInteger < primes.size() ? primes.get( positiveInteger ) : "" );
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
        stringBuilder
                .append( pad ).append( '\n' ).append( pad ).append( "Tree of prime whose rank is ")
                .append( positiveInteger ).append( " ")
                .append( positiveInteger < primes.size() ? primes.get( positiveInteger ) : "" )
                .append( " height: ").append( height )
                .append( " width: ").append( width );
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
    private static final int ELEMENT  = 8; 
    private static final int RADIUS   = ELEMENT; 
            static final int PAD      = 3 * ELEMENT; 
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

        graphics.setColor( Color.BLACK );
           
        // set 1st potentialFactor tree's upperleft corner coordinates 
        int factorTreeX = x;
        int factorTreeY = y + DELTA;
        
        for ( PositiveIntegerToTreeBijection factorTree : factorTrees )
        {
            // draw edge from this root to potentialFactor tree's root
            graphics.drawLine( rootX, rootY, factorTreeX + factorTree.rootX(), factorTreeY + factorTree.rootY() );
            
            // draw potentialFactor tree
            factorTree.viewGraphics( graphics, factorTreeX, factorTreeY ); 
            
            // set next potentialFactor tree's upperleft corner's x coordinate
            factorTreeX += DELTA * factorTree.width; 
        }
        
        // draw root
        drawDisk( graphics, rootX, rootY );
    }

    /**
     *
     * @return width in pixels of rectangle enclosing image of tree
     */
    public int imageViewWidth() { return width() * DELTA; }
    
    private void drawDisk( Graphics graphics, int x, int y )
    {
        if ( isPositive )
        {
            graphics.fillOval( x - RADIUS, y - RADIUS, DIAMETER, DIAMETER );
        }
        else
        {
            graphics.setColor( Color.WHITE );
            graphics.fillOval( x - RADIUS, y - RADIUS, DIAMETER, DIAMETER );
            graphics.setColor( Color.BLACK );
            graphics.drawOval( x - RADIUS, y - RADIUS, DIAMETER, DIAMETER );
        }
    }
    
    int rootX() { return width * DELTA / 2 - RADIUS; }
    
    int rootY() { return PAD + RADIUS; }
    
    //_____________ Methods For Unit Testing ______________________
    public List<PositiveIntegerToTreeBijection> getFfactorTrees() { return factorTrees; } 
    
    int height() { return height; }
    
    int width() { return width; }
}
