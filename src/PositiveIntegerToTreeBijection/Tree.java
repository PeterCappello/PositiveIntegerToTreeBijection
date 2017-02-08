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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Recursively maps a natural possibleFactor to a rooted, un-oriented tree.
 * @author Pete Cappello
 */
public final class Tree
{   
    //___________________________
    //
    // class attributes
    //___________________________
    static private final int PRIMES_INITIAL_CAPACITY = 1 << 10;
    static private final double ONE_THIRD = 1.0 / 3.0;
    static private final double FRAME_RATE = 16;
    static private final double BASE_ANGLE = 1.0 / FRAME_RATE;
    
    /**
     * List of first PRIMES_INITIAL_CAPACITY prime numbers
     */
    static public List<Integer> primes = new ArrayList<>( PRIMES_INITIAL_CAPACITY );
    /**
     * Map of first ranks of first PRIMES_INITIAL_CAPACITY prime numbers
     */
    static public Map<Integer, Integer> ranks = new HashMap<>( PRIMES_INITIAL_CAPACITY );
    
    // cache of PositiveIntegerTree objects
    private static final Map<Integer, Tree> integerToPositiveIntegerTreeMap = new HashMap<>();
    
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
     * Determines whether ODD integer number is prime.
     * @param number an ODD integer
     * @return true if and only if number is prime
     */
    static public boolean isPrime( final int number )
    {
        final int maxFactor = (int) Math.sqrt( number );
        for ( int rank = 2, prime = 3; prime <= maxFactor; prime = primes.get( ++rank ) )
        {
            if ( number % prime == 0 )
            {
                return false;
            }
        }
        return true;
    }
    
    static int prime( int rank )
    {
        if ( rank >= primes.size() - 1 )
        {
            increasePrimesTo( rank );
        }
        return primes.get( rank );
    }
    
    static int rank( int prime ) throws IllegalArgumentException
    {
        if ( prime >= primes.get( primes.size() - 1 ) )
        {
            increaseRanksTo( prime );
        }
        Integer rank = ranks.get( prime );
        if ( rank == null )
        {
            throw new IllegalArgumentException();
        }
        return rank;
    }
    
    static private void increasePrimesTo( int upperRank )
    { 
        long startTime = System.nanoTime();
        int rank = primes.size();
        for ( int number = primes.get( rank - 1 ) + 2; rank <= upperRank; number += 2 )
        {
            rank = processPrimeCandidate( number, rank );
        }
        assert upperRank == primes.size() - 1;
        long stopTime = System.nanoTime();
        Logger.getLogger(Tree.class.getCanonicalName() )
              .log(Level.INFO, "Increased # of primes to {0} in {1} ms.", new Object[]{primes.size() - 1, (stopTime - startTime) / ( 1024 * 1024 )});
    }
    
    static private void increaseRanksTo( int upperPrime )
    {
        long startTime = System.nanoTime();
        int rank = primes.size();
        for ( int number = primes.get( rank - 1 ) + 2; number <= upperPrime; number += 2 )
        {
            rank = processPrimeCandidate( number, rank );
        }
        assert upperPrime == primes.get( primes.size() - 1 );
        long stopTime = System.nanoTime();
        Logger.getLogger(Tree.class.getCanonicalName() )
              .log(Level.INFO, "Increased primes to {0} in {1} ms.", new Object[]{primes.get( primes.size() - 1 ), (stopTime - startTime) / ( 1024 * 1024 )});
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
    // immutable object attributes
    //___________________________
    private final boolean isPositive;
    private final int positiveInteger;
    private final List<Tree> factorTrees;
    private final int height;
    private final int width;
    
    private int xTest;
    
    //___________________________
    //
    // planet attributes
    //___________________________
//    private final int diameter;       // diameter of this body
//    private final int orbitRadius; // radius of its orbit
//    private final double stepSize;    // amount of radians incremented per time step
//    private       Color color = Color.BLUE; // of body
////    private       Body parent = null;        // this orbits around parent
//    private       int x, y;                 // location of this body
//    private       double orbitPosition;     // orbit angular position in radians
    
//    Tree( Tree positiveIntegerTree )
//    {
//        this.isPositive      = positiveIntegerTree.isPositive;
//        this.positiveInteger = positiveIntegerTree.positiveInteger;
//        this.factorTrees     = positiveIntegerTree.factorTrees;
//        
//        // planet attributes
//        this.diameter = positiveIntegerTree.diameter;
//    }
    
    /**
     * Construct the tree that corresponds to a particular natural possibleFactor.
     * @param integer whose corresponding tree is being constructed 
     */
//    Tree( int integer ) throws ArrayIndexOutOfBoundsException
//    {
//        isPositive = integer > 0;
//        positiveInteger = ( isPositive ) ? integer : -integer;
//        Tree cachedTree = integerToPositiveIntegerTreeMap.get( positiveInteger );
//        if ( cachedTree != null )
//        {
//            factorTrees = cachedTree.factorTrees;
//            height      = cachedTree.height;
//            width       = cachedTree.width;
//            
//            // planet attributes
////            diameter = cachedTree.diameter;
////            orbitRadius = cachedTree.orbitRadius; // radius of its orbit
////            stepSize = cachedTree.stepSize;    // amount of radians incremented per time step
////            color = Color.BLUE; // of body
////            orbitPosition = cachedTree.orbitPosition;
//            return;
//        }
//        
//        // for each primeFactor, make its primeFactor tree
//        int possibleFactorRank = 1;
//        int number = positiveInteger;
//        int maxFactor = (int) Math.sqrt( number );
//        for (int possibleFactor = prime(possibleFactorRank); possibleFactor <= maxFactor; possibleFactor = prime(++possibleFactorRank) )
//        {
//            for ( ; number % possibleFactor == 0; number /= possibleFactor )
//            {
//                makeTree( possibleFactorRank );
//            }
//            maxFactor = (int) Math.sqrt( number );
//        }
//        // Is number prime and > sqrt maxFactor? (e.g., for 6 = 2 * 3, 3 > sqrt( 6/2 ) )
//        if ( number > 1 )
//        {
//            makeTree( rank( number ) );
//        }
//        
//        // complete width & height calculation
//        width = ( 1 < width ) ? width : 1;
//        height++;
//      
//        // planet attributes
////        diameter = (int) Math.pow( positiveInteger, ONE_THIRD );
////        stepSize = ( factorTrees.isEmpty() ) ? BASE_ANGLE : factorTrees.get( 0 ).stepSize / 2.0;    // amount of radians incremented per time step
////        color = Color.BLUE; // of body
//////        parent = cachedTree.parent; 
//////System.out.println( "number: " + positiveInteger );
////        int maxSatelliteOrbitRadius = ( factorTrees.isEmpty() ) 
////                ? 0 
////                : factorTrees.stream().map( tree -> tree.orbitRadius).max( ( radius1, radius2 ) -> radius2 - radius1 ).get(); 
////        orbitRadius = diameter + 2 + 4 * maxSatelliteOrbitRadius; 
////        x = cachedTree.x;
////        y = cachedTree.y;                 // location of this body
////        orbitPosition = cachedTree.orbitPosition;
////        int nFactors = factorTrees.size();
////        for ( int i = 0; i < nFactors; i++ )
////        {
////            factorTrees.get( i ).orbitPosition = 2.0 * Math.PI * ( ( double ) i ) / nFactors;
////        }
//        
//        // cache tree
//        integerToPositiveIntegerTreeMap.put( positiveInteger, this );
//    }
    
    Tree( int integer ) throws ArrayIndexOutOfBoundsException
    {
        isPositive = integer > 0;
        positiveInteger = ( isPositive ) ? integer : -integer;
        
        //__________________
        //
        // base case
        //___________________
        if ( positiveInteger == 1 )
        {
            height = width = 1;
            factorTrees = new LinkedList<>();
            return;
        }
        
        Tree cachedTree = integerToPositiveIntegerTreeMap.get( positiveInteger );
        if ( cachedTree != null )
        {         
            height      = cachedTree.height;
            width       = cachedTree.width;
            factorTrees = cachedTree.factorTrees;
            return;
        }
        
        //__________________
        //
        // recursive case
        //___________________        
        // !! Re: planet view, see issue w/ caching below.
        factorTrees = primeFactors( positiveInteger )
                .stream()
                .map( primeFactor -> new Tree( rank( primeFactor ) ) )
                .collect( Collectors.toList() );
        height = 1 + factorTrees
                .stream()
                .mapToInt( Tree::height )
                .max()
                .getAsInt();
        width = factorTrees
                .stream()
                .mapToInt( Tree::width )
                .sum();
        
        // cache this tree
        integerToPositiveIntegerTreeMap.put( positiveInteger, this );
    }
    
    Integer n() { return ( isPositive ) ? positiveInteger : -positiveInteger; }

        
            private List<Integer> primeFactors( int n )
    {
        /* add 1 to n before taking sqrt to avoid situation where sqrt( n^2 )
        * returns n - epsilon, (int) of which is n - 1 which could produce
        * an incorrect answer.
        */
        return primeFactors( n, 1, (int) Math.sqrt( n + 1 ), new LinkedList<>() );
    }
    
    private List<Integer> primeFactors( int n, int rank, int limit, List<Integer> primeFactors )
    {
        if ( n == 1 ) 
        {
            return primeFactors;
        }
        int primeFactor = prime( rank );
        if ( primeFactor > limit ) 
        {
            primeFactors.add( n );
            return primeFactors;
        }
        if ( n % primeFactor == 0 )
        {
            int newN = n / primeFactor;
            primeFactors.add( primeFactor );
            return primeFactors( newN, rank, (int) Math.sqrt( newN + 1 ), primeFactors );
        }
        return primeFactors( n, ++rank, limit, primeFactors );
    }
       
    private int height() { return height; }
    
    List<Tree> factorTrees() { return factorTrees; }
    
    public int getPositiveInteger() { return positiveInteger; }
    
    public String getStringView() { return new String( Tree.this.viewString( "   ") ); }

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
            for ( Tree factorTree : factorTrees )
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
    
    BufferedImage getImageView()
    {
        BufferedImage bufferedImage = new BufferedImage( imageViewWidth(), imageViewHeight(), BufferedImage.TYPE_INT_ARGB );
        viewGraphics( bufferedImage.getGraphics(), PAD, PAD );
        return bufferedImage;
    }
    
    BufferedImage getPlanetsView()
    {
        BufferedImage bufferedImage = new BufferedImage( imageViewWidth(), imageViewHeight(), BufferedImage.TYPE_INT_ARGB );
        viewPlanets( bufferedImage.getGraphics(), PAD, PAD );
        return bufferedImage;
    }

    /**
     *
     * @param graphics of image on which tree is rendered
     * @param x col of upper left corner of rectangle containing tree
     * @param y row of upper left corner of rectangle containing tree
     */
    public void viewGraphics( Graphics g, int x, int y )
    {
        Graphics graphics = g.create();
        graphics.setColor( Color.BLACK );
                       
        // coordinates of center of root
        int rootX = x + rootX();
        int rootY = y + rootY();
        
        graphics.setColor( Color.BLACK );
           
        // set 1st possibleFactor tree's upperleft corner coordinates 
        int factorTreeX = x;
        int factorTreeY = y + DELTA;
        
        for ( Tree factorTree : factorTrees )
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
     * @param graphics of image on which tree is rendered
     * @param x col of upper left corner of rectangle containing tree
     * @param y row of upper left corner of rectangle containing tree
     */
    public void viewPlanets( Graphics graphics, int x, int y )
    {                      
        // coordinates of center of root
        int rootX = x + rootX();
        int rootY = y + rootY();
        
        graphics.setColor( Color.BLACK );
           
        // set 1st possibleFactor tree's upperleft corner coordinates 
        int factorTreeX = x;
        int factorTreeY = y + DELTA;
        
        for ( Tree factorTree : factorTrees )
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
    
    //_______________________________
    //
    // planetary motion methods
    //_______________________________
//    void move( Tree parent ) 
//    {
//        // move this Body
//        orbitPosition += stepSize;
//        if ( orbitPosition > 2 * Math.PI )
//        {
//            orbitPosition -= 2 * Math.PI;
//        }
//        x = parent.x + parent.diameter/2 - diameter/2 + (int) ( orbitRadius * cos( orbitPosition ) );
//        y = parent.y + parent.diameter/2 - diameter/2 + (int) ( orbitRadius * sin( orbitPosition ) );
//        
//        // move my sateillites
//        factorTrees.forEach( satellite -> satellite.move( this ) );
//    }
//    
//    public void draw( Tree parent, Graphics graphics )
//     {
//         // draw this
//         if ( SHOW_ORBIT ) 
//         {
//             graphics.setColor( Color.WHITE );
//             graphics.drawOval( parent.x - (int) orbitRadius + parent.diameter/2,
//                                parent.y - (int) orbitRadius + parent.diameter/2,
//                                2 * (int) orbitRadius,
//                                2 * (int) orbitRadius
//                              );
//         }
//         graphics.setColor( color );
//         graphics.fillOval( x, y, diameter, diameter );
//         
//        // draw my satellites
//        factorTrees.forEach( satellite -> satellite.draw( this, graphics ) );
//     }
    
    //_____________ Methods For Unit Testing ______________________
    public List<Tree> getFfactorTrees() { return factorTrees; } 
        
    int width() { return width; }
}
