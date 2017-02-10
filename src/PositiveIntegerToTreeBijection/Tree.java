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
import java.awt.image.BufferedImage;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static PositiveIntegerToTreeBijection.Viewer.IMAGE_VIEWPORT_SIZE;

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
    static private final int UNIT = 8;
    
    static private final boolean SHOW_ORBIT = true;
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
    
    static void initialize() { setPrimesArray(); }
    
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
    private final boolean isRoot;
    private final boolean isPositive;
    private final int positiveInteger;
    private final Tree parent;
    private final List<Tree> factorTrees;
    private final int height;
    private final int width;
        
    //___________________________
    //
    // immutable planet attributes
    //___________________________
    private final int diameter;       // diameter of this body
    private final int orbitRadius;    // radius of its orbit around its PARENT.
    private final double stepSize;    // amount of radians incremented per time step
    private       Color color = Color.BLUE; // of body
    //___________________________
    //
    // mutable planet attributes
    //___________________________
    private       int x, y;              // location of this body
    private       double orbitAngle;     // orbit angular position in radians
    
    /**
     * Constructor for root.
     * @param integer 
     */
    Tree( int integer ) { this( integer, null ); }
        
    /**
     * Constructor for subtree.
     * @param integer
     * @param parent 
     */
    Tree( int integer, Tree parent )
    {
        isRoot = parent == null;
        isPositive = integer > 0;
        positiveInteger = ( isPositive ) ? integer : -integer;
        this.parent = parent;
        
        //__________________
        //
        // base case: leaf
        //___________________
        if ( positiveInteger == 1 )
        {
            height = width = 1;
            factorTrees = new LinkedList<>();
            diameter = 1;
            orbitRadius = isRoot ? 0 : parent.diameter + 1;
            stepSize = BASE_ANGLE;
            return;
        }
        
        Tree cachedTree = integerToPositiveIntegerTreeMap.get( positiveInteger );
        if ( cachedTree != null )
        {         
            height = cachedTree.height;
            width  = cachedTree.width;
            // copying subtrees; each tree node has its own animation state.
            factorTrees = cachedTree.factorTrees
                    .stream()
                    .map( t -> new Tree( t, this ) )
                    .collect( Collectors.toList());
            diameter = cachedTree.diameter;
            orbitRadius = cachedTree.orbitRadius;
            stepSize = cachedTree.stepSize;
            return;
        }
        
        //__________________
        //
        // recursive case
        //___________________        
        factorTrees = primeFactors( positiveInteger )
                .stream()
                .map( primeFactor -> new Tree( rank( primeFactor ), this ) )
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
        //__________________________
        //
        // planet attributes
        //__________________________
        diameter = (int) pow( positiveInteger, ONE_THIRD );
        stepSize = ( factorTrees.isEmpty() ) ? BASE_ANGLE : factorTrees.get( 0 ).stepSize / 2.0; // radians incremented per time step
        color = Color.BLUE; // of body
        int maxSatelliteOrbitRadius = ( factorTrees.isEmpty() ) 
                ? 0 
                : factorTrees.stream()
                        .mapToInt( Tree::orbitRadius )
                        .max()
                        .getAsInt();
        
        // compute distance from its parent
        int parentRadius = isRoot 
                ? 0 
                : ( parent.diameter < 2 ) ? 1 : parent.diameter / 2;
        orbitRadius = isRoot ? 0 : parentRadius + 2 + 4 * maxSatelliteOrbitRadius;
        
        orbitAngle = Math.random() * 2 * Math.PI;
        int nFactors = factorTrees.size();
        for ( int i = 0; i < nFactors; i++ )
        {
            factorTrees.get( i ).orbitAngle = 2.0 * Math.PI * ( ( double ) i ) / nFactors;
        }

        // cache this tree
        integerToPositiveIntegerTreeMap.put( positiveInteger, this );
    }
    
    /**
     * 
     * @param tree deep copy of tree.
     */
    Tree( Tree tree, Tree parent )
    {   isRoot = parent == null;
        this.parent = parent;
        isPositive = tree.isPositive;
        positiveInteger = tree.positiveInteger;
        height = tree.height;
        width = tree.width;
        factorTrees = tree.factorTrees()
                .stream()
                .map( t -> new Tree( t, this ) )
                .collect( Collectors.toList());
        diameter = tree.diameter;
        orbitRadius = tree.orbitRadius;
        stepSize = tree.stepSize;
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
    
    public String getStringView() { return new String( viewString( "   ") ); }
    
    public String toString() { return toString( "  " ).toString(); }
    
    private StringBuilder toString( String pad )
    {
         StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append( pad ).append( '\n' ).append( pad )
                .append( "isRoot: " + isRoot ). append( "  " )
                .append( isPositive ? "" : "-")
                .append( positiveInteger ).append( "  " )
                .append( positiveInteger ).append( "  " )
                .append( positiveInteger < primes.size() ? prime( positiveInteger ) : "" )
                .append( " diameter: " + diameter )
                .append( " orbitRadius: " + orbitRadius )
                .append( " orbitAngle: " + orbitAngle )
                .append( " x: " + x ).append( " y: " + y );
        if ( ! factorTrees.isEmpty() )
        {
            for ( Tree factorTree : factorTrees )
            {
                stringBuilder.append(factorTree.viewString( pad + "    ") );
            }
        }
        return stringBuilder;
    }

    private StringBuilder viewString( String pad )
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append( pad ).append( '\n' ).append( pad )
                .append( isPositive ? "" : "-")
                .append( positiveInteger ).append( "   " )
                .append( positiveInteger < primes.size() ? prime( positiveInteger ) : "" );
        if ( ! factorTrees.isEmpty() )
        {
            for ( Tree factorTree : factorTrees )
            {
                stringBuilder.append(factorTree.viewString( pad + "    " ) );
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
        viewPlanets( bufferedImage.getGraphics() );
        return bufferedImage;
    }

    /**
     *
     * @param g
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
     */
    public void viewPlanets( Graphics graphics )
    {                      
        move();
        draw( graphics );
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
    void move() 
    {
        // move this Body
        if ( isRoot )
        {
            x = y = 0;
        }
        else
        {
            orbitAngle += stepSize;
            if ( orbitAngle > 2 * Math.PI )
            {
                orbitAngle -= 2 * Math.PI;
            }
            x = parent.x + (int) ( orbitRadius * cos( orbitAngle ) );
            y = parent.y + (int) ( orbitRadius * sin( orbitAngle ) );
        }
     System.out.println("move: n: " + n() + " x: " + x + " y: " + y);
     
        // move my sateillites
        factorTrees.forEach( satellite -> satellite.move() );
        System.out.println( this );
    }
    
    public void draw( Graphics graphics )
     {
         // !! put OFFSET as a constanst
         int OFFESET = IMAGE_VIEWPORT_SIZE / 2;
        int parentX = isRoot ? 0 : parent.x;
        int parentY = isRoot ? 0 : parent.y;
        
         // draw this
         if ( SHOW_ORBIT ) 
         {
             graphics.setColor( Color.RED );
             graphics.drawOval( OFFESET + UNIT * ( parentX - (int) orbitRadius ),
                                OFFESET + UNIT * ( parentY - (int) orbitRadius ),
                                UNIT * 2 * (int) orbitRadius,
                                UNIT * 2 * (int) orbitRadius
                              );
            System.out.println("draw upper left ORBIT coordinates: n: " + n() + " x: " + (OFFESET + UNIT * ( parentX - (int) orbitRadius) ) + " y: " + ( OFFESET + UNIT * ( parentY - (int) orbitRadius ) ) + " orbit diameter: " + (UNIT * 2 * (int) orbitRadius) );
         }
         graphics.setColor( color );
         System.out.println("Actual upper left BODY coordinates: n: " + n() + " x: " + ( OFFESET + (int) ( UNIT * ( x - diameter / 2.0 ) ) ) + " y: " + ( OFFESET + (int) ( UNIT * ( y - diameter / 2.0 ) ) ) + " UNIT * diameter: " + (UNIT * diameter) );
         graphics.fillOval(  OFFESET + (int) ( UNIT * ( x - diameter / 2.0 ) ), OFFESET + (int) ( UNIT * ( y - diameter / 2.0 ) ), UNIT * diameter, UNIT * diameter );
         
        // draw my satellites
        factorTrees.forEach( satellite -> satellite.draw( graphics ) );
     }
    
    //_____________ Methods For Unit Testing ______________________
    public List<Tree> getFfactorTrees() { return factorTrees; } 
        
    int width() { return width; }
    
    int diameter() { return diameter; }
    
    int orbitRadius() { return orbitRadius; }
}
