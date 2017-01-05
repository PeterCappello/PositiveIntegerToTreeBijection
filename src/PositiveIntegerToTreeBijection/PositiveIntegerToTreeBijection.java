/*
 * The MIT License
 *
 * Copyright 2015 peter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package PositiveIntegerToTreeBijection;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Recursively maps a natural possibleFactor to a rooted, un-oriented tree.
 * @author Pete Cappello
 */
public class PositiveIntegerToTreeBijection
{   
    //___________________________
    //
    // class attributes
    //___________________________
    static private final int PRIMES_INITIAL_CAPACITY = 30000;

    /**
     * List of first PRIMES_INITIAL_CAPACITY prime numbers
     */
    static public List<Integer> primes = new ArrayList<>( PRIMES_INITIAL_CAPACITY );
    /**
     * Map of first ranks of first PRIMES_INITIAL_CAPACITY prime numbers
     */
    static public Map<Integer, Integer> ranks = new HashMap<>( PRIMES_INITIAL_CAPACITY );
    
    // cache of PositiveIntegerTree objects
    static private Map<Integer, PositiveIntegerToTreeBijection> integerToPositiveIntegerTreeMap = new HashMap<>();
    
    static void initialize()
    {
        setPrimesArray();
    }
    
    /**
     * Initialize the primes List and the ranks map with the first 
     * PRIMES_INITIAL_CAPACITY prime numbers and their ranks.
     * The index of a prime is its rank: primes.get( 0 ) is UNUSED.
     */
    static public void setPrimesArray()
    {
        primes.add( 1 ); 
        primes.add( 2 ); ranks.put( 2, 1 );
        primes.add( 3 ); ranks.put( 3, 2 );
        for ( int number = 5, rank = 3; rank < PRIMES_INITIAL_CAPACITY; number += 2 )
        {
            if ( isPrime( number ) )
            {
                primes.add( number); ranks.put( number, rank );
                rank++;
            }
        }
    }
    
    /**
     * Determines whether ODD integer possibleFactor is prime.
     * @param number an ODD integer
     * @return true if and only if possibleFactor is prime
     */
    static public boolean isPrime( final int number )
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
    
    static private int prime( int rank )
    {
        if ( rank >= primes.size() - 1 )
        {
            increasePrimesTo( rank );
        }
        return primes.get( rank );
    }
    
    static private int rank( int prime )
    {
        if ( prime >= primes.get( primes.size() - 1 ) )
        {
            increaseRanksTo( prime );
        }
        return ranks.get( prime );
    }
    
    static private void increasePrimesTo( int upperRank )
    { 
        int rank = primes.size();
        for ( int number = primes.get( rank - 1 ) + 2; rank <= upperRank; number += 2 )
        {
            rank = processPrimeCandidate( number, rank );
        }
        assert upperRank == primes.size() - 1;
        Logger.getLogger( PositiveIntegerToTreeBijection.class.getCanonicalName() )
              .log(Level.INFO, "Increased # of primes to {0}.", (primes.size() - 1 ) );
    }
    
    static private void increaseRanksTo( int upperPrime )
    {
        int rank = primes.size();
        for ( int number = primes.get( rank - 1 ) + 2; number <= upperPrime; number += 2 )
        {
            rank = processPrimeCandidate( number, rank );
        }
        assert upperPrime == primes.get( primes.size() - 1 );
        Logger.getLogger( PositiveIntegerToTreeBijection.class.getCanonicalName() )
              .log(Level.INFO, "Increased primes to {0}.", (primes.get( primes.size() - 1 ) ) );
    }
    
    static private int processPrimeCandidate( int primeCandidate, int rank )
    {
        if ( isPrime( primeCandidate ) )
        {
            primes.add( primeCandidate ); 
            ranks.put(  primeCandidate, rank );
            return rank + 1;
        }
        return rank;
    }
    
    //___________________________
    //
    // object attributes
    //___________________________
    private final boolean isPositive;
    private final int positiveInteger;
    private List<PositiveIntegerToTreeBijection> factorTrees = new LinkedList<>();
    private int height;
    private int width;
    
    PositiveIntegerToTreeBijection( PositiveIntegerToTreeBijection positiveIntegerTree )
    {
        this.isPositive      = positiveIntegerTree.isPositive;
        this.positiveInteger = positiveIntegerTree.positiveInteger;
        this.factorTrees     = positiveIntegerTree.factorTrees;
    }
    
    /**
     * Construct the tree that corresponds to a particular natural possibleFactor.
     * @param integer whose corresponding tree is being constructed 
     */
    PositiveIntegerToTreeBijection( int integer ) throws ArrayIndexOutOfBoundsException
    {
        isPositive = integer > 0;
        positiveInteger = ( isPositive ) ? integer : -integer;
        PositiveIntegerToTreeBijection cachedTree = integerToPositiveIntegerTreeMap.get( positiveInteger );
        if ( cachedTree != null )
        {
            factorTrees = cachedTree.factorTrees;
            height      = cachedTree.height;
            width       = cachedTree.width;
            return;
        }
        
        // for each factor, make its factor tree
        int possibleFactorRank = 1;
        int number = positiveInteger;
        int maxFactor = (int) Math.sqrt( number );
        for (int possibleFactor = prime(possibleFactorRank); possibleFactor <= maxFactor; possibleFactor = prime(++possibleFactorRank) )
        {
            for ( ; number % possibleFactor == 0; number /= possibleFactor )
            {
                makeTree( possibleFactorRank );
            }
            maxFactor = (int) Math.sqrt( number );
        }
        // Is number prime and > sqrt maxFactor? (e.g., for 6 = 2 * 3, 3 > sqrt( 6/2 ) )
        if ( number > 1 )
        {
            makeTree( rank( number ) );
        }
        
        // complete width & height calculation
        width = ( 1 < width ) ? width : 1;
        height++;
      
        // cache tree
        integerToPositiveIntegerTreeMap.put( positiveInteger, this );
    }
    
    private void makeTree( int rank )
    {
        PositiveIntegerToTreeBijection numberTree = integerToPositiveIntegerTreeMap.get(rank );
        if ( numberTree == null )
        {
            // no cached tree for this possibleFactor, make one
            numberTree = new PositiveIntegerToTreeBijection( rank );
            integerToPositiveIntegerTreeMap.put( rank, numberTree );
        }
        factorTrees.add( numberTree );

        // update width & height calculation
        width += numberTree.width;
        if ( numberTree.height > height )
        {
             height = numberTree.height;
        }
    }
    
    List<PositiveIntegerToTreeBijection> factorTrees() { return factorTrees; }
    
    public int getPositiveInteger() { return positiveInteger; }
    
    public String getStringView() { return new String( PositiveIntegerToTreeBijection.this.viewString( "   ") ); }

    private StringBuilder viewString( String pad )
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append( pad ).append( '\n' ).append( pad )
                .append( isPositive ? "" : "-")
                .append( positiveInteger ).append( "  ")
                .append( positiveInteger < primes.size() ? prime( positiveInteger ) : "" );
        if ( ! factorTrees.isEmpty() )
        {
            for ( PositiveIntegerToTreeBijection factorTree : factorTrees )
            {
                stringBuilder.append(factorTree.viewString( pad + "    ") );
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
    
    Image getImageView()
    {
        Image image = new BufferedImage( imageViewWidth(), imageViewHeight(), BufferedImage.TYPE_INT_ARGB );
        viewGraphics( image.getGraphics(), PAD, PAD );
        return image;
    }

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
           
        // set 1st possibleFactor tree's upperleft corner coordinates 
        int factorTreeX = x;
        int factorTreeY = y + DELTA;
        
        for ( PositiveIntegerToTreeBijection factorTree : factorTrees )
        {
            // draw edge from this root to possibleFactor tree's root
            graphics.drawLine( rootX, rootY, factorTreeX + factorTree.rootX(), factorTreeY + factorTree.rootY() );
            
            // draw possibleFactor tree
            factorTree.viewGraphics( graphics, factorTreeX, factorTreeY );
            
            // set next possibleFactor tree's upperleft corner's x coordinate
            factorTreeX += DELTA * factorTree.width; 
        }
        
        // draw root
        drawDisk( graphics, rootX, rootY );
    }

    /**
     *
     * @return width in pixels of rectangle enclosing image of tree
     */
    public int imageViewWidth() { return ( width() + 1 ) * DELTA; }
    
    public int imageViewHeight() { return ( height() + 1 ) * DELTA; }
    
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
