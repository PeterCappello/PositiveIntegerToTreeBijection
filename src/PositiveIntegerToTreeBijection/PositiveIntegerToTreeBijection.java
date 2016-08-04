package PositiveIntegerToTreeBijection;


import java.awt.Color;
import java.awt.Graphics;
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
    // shamelessly declaring view parameters, all are numbers of pixels
    private static final int ELEMENT  = 8; 
    private static final int RADIUS   = ELEMENT; 
    private static final int PAD      = 3 * ELEMENT; 
    private static final int DELTA    = 2 * ( PAD + RADIUS );
    private static final int DIAMETER = 2 * RADIUS;
    
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
    public static boolean isPrime( int number )
    {
        for ( int rank = 1; primes[ rank ] <= Math.sqrt( number ); rank++ )
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
    
    @Override
    public String toString() { return new String( toString( "   ") ); }
    
    private StringBuilder toString( String pad )
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( pad ).append( '\n' ).append( pad ).append( "Natural number tree: rank: ");
        stringBuilder.append( positiveInteger ).append( " ");
        stringBuilder.append( positiveInteger < primes.length ? primes[ positiveInteger ] : "" );
        stringBuilder.append( "\n").append( pad );
        stringBuilder.append( "height: ").append( height );
        stringBuilder.append( " width: ").append( width );
        stringBuilder.append( " Its factor trees are:" );
        for ( PositiveIntegerToTreeBijection factorTree : factorTrees )
        {
            stringBuilder.append( factorTree.toString( pad + "   ") );
        }
        return stringBuilder;
    }
    
    void view( Graphics graphics, int x, int y )
    {
        graphics.setColor( Color.RED );
        
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
            factorTree.view( graphics, factorTreeX, factorTreeY ); 
            
            factorTreeX += DELTA * factorTree.width; // set next factor tree's x ccordinate
        }
    }
    
    private void drawDisk( Graphics graphics, int x, int y )
    {
        graphics.fillOval( x - RADIUS, y - RADIUS, DIAMETER, DIAMETER );
    }
    
    private int rootX() { return width * DELTA / 2 - RADIUS; }
    
    private int rootY() { return PAD + RADIUS; }
    
    //_____________ Methods For Unit Testing ______________________
    public List<PositiveIntegerToTreeBijection> getFfactorTrees() { return factorTrees; } 
    
    public int getHeight() { return height; }
    
    public int getWidth() { return width; }
}
