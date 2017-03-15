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
    /**
     * true if and only if orbit circles are displayed in planetary view.
     */
    static public final boolean SHOW_ORBIT = true;
    static public final boolean LABEL_NODES = false;
    static public       int CIRCULAR_TREE_WIDTH;
    
    static private final int PRIMES_INITIAL_CAPACITY = 1 << 10;
    static private final double ONE_THIRD = 1.0 / 3.0;
    static private final double FRAME_RATE = 16;
    static private final double G = 0.1; // Gravitational constant
    static private final double BASE_ANGLE = 1.0 / FRAME_RATE;
    static private final double SCALE = 8;
    static private final double OFFESET = IMAGE_VIEWPORT_SIZE / 2;
    static private List<Integer> primes = new ArrayList<>( PRIMES_INITIAL_CAPACITY );
    static private Map<Integer, Integer> ranks = new HashMap<>( PRIMES_INITIAL_CAPACITY );
    // cache of PositiveIntegerTree objects
    static private final Map<Integer, Tree> integerToPositiveIntegerTreeMap = new HashMap<>();
    
    /**
     * The prime number whose index is rank.
     * @param rank of the prime number returned.
     * @return the prime number whose index is rank.
     */
    static public int prime( int rank )
    {
        if ( rank >= primes.size() - 1 )
        {
            increasePrimesTo( rank );
        }
        return primes.get( rank );
    }
    
    /**
     * The rank (aka index) of the argument.
     * @param prime whose rank is to be returned.
     * @return The rank (aka index) of the argument.
     * @throws IllegalArgumentException
     */
    static public int rank( int prime ) throws IllegalArgumentException
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
    
    static void initialize() { setPrimesArray(); }
    
    /**
     * Initialize the primes List and the ranks map with the first 
     * PRIMES_INITIAL_CAPACITY prime numbers and their ranks.
     * The index of a prime is its rank: primes.get( 0 ) is UNUSED.
     */
    static void setPrimesArray()
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
    static private boolean isPrime( final int number )
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
    // tree attributes that are immutable AFTER construction completes.
    //___________________________
    private boolean isRoot;
    private boolean isPositive;
    private int positiveInteger;
    private Tree parent;
    private List<Tree> factorTrees;
    private int height;
    private int width;
    
    //___________________________
    //
    // circular tree attributes that are immutable AFTER construction completes.
    //___________________________
    private double circularTreeRadius;
        
    //___________________________
    //
    // planet view attributes that are immutable AFTER construction completes.
    //___________________________
    private double diameter;    // diameter of this body
    private double orbitRadius; // radius of its orbit around its PARENT.
    private double stepSize;    // radians incremented per time step
    private Color color = Color.BLUE; // of body
    //___________________________
    //
    // mutable planet view attributes
    //___________________________
    private double x, y;           // location of this body
    private double orbitAngle;     // orbit angular position in radians
    
    /**
     * Constructs tree that corresponds to integer argument.
     * @param integer 
     */
    public Tree( int integer ) { this( integer, null ); }
        
    /**
     * Constructor for subtree.
     * @param integer
     * @param parent 
     */
    Tree( int integer, Tree parent )
    {
        this.parent = parent;
        isRoot = parent == null;
        isPositive = integer > 0;
        positiveInteger = ( isPositive ) ? integer : -integer;
        
        //__________________
        //
        // base case: leaf
        //___________________
        if ( positiveInteger == 1 )
        {
            height = width = 1;
            factorTrees = new LinkedList<>();
            diameter = 1.0;
            circularTreeRadius = PAD;
            computeOrbitRadius();
            stepSize = BASE_ANGLE;
            return;
        }
        
        //__________________
        //
        // recursive case
        //___________________ 
        // Already have positive ersion of this tree?
//        Tree cachedTree = integerToPositiveIntegerTreeMap.get( positiveInteger );
//        if ( cachedTree != null )
//        { 
//            copy( cachedTree );
//            isPositive = integer > 0;
//            // ?? Why can't this be done in copy method?  Check for 1,2,3,4,5.
//            stepSize = isRoot
//                ? 0.0
//                : G * mass() * parent.mass() / Math.pow( orbitRadius, 2.0 ); // radians/time step
//            return;
//        }
                       
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
        // circularTree attributes
        //__________________________
        double sectorAngle = 2.0 * Math.PI / ( factorTrees.size() + ( isRoot ? 0 : 1 ) );
        circularTreeRadius = factorTrees
                .stream()
                .mapToDouble( factorTree -> rho( factorTree, sectorAngle ) )
                .max()
                .getAsDouble();
        //__________________________
        // planet attributes
        //__________________________
        diameter = Math.pow( 3.0 * Math.PI * mass(), ONE_THIRD );
        computeOrbitRadius();
        final int nFactors = factorTrees.size();
        for ( int i = 0; i < nFactors; i++ )
        {
            factorTrees.get( i ).orbitAngle = 2.0 * Math.PI * ( ( double ) i ) / nFactors;
        }
        double massProduct = mass() * ( isRoot ? 1.0 : parent.mass() );
        stepSize = isRoot
                ? 0.0
                : G * massProduct / Math.pow( orbitRadius, 2.0 ); // radians/time step

        // cache this tree
        integerToPositiveIntegerTreeMap.put( positiveInteger, this );
    }
    
    private double rho( Tree tree, double sectorAngle )
    {
        return ( tree.circularTreeRadius + PAD ) / ( factorTrees.size() == 1 ? 1.0 : Math.cos( ( Math.PI - sectorAngle ) / 2.0 ) );
    }
    
    /**
     * Constructs a deep copy of argument tree, apart from parent-related attributes.
     * @param tree to be copied
     */
    Tree( Tree tree, Tree parent )
    {   
        isRoot = parent == null;
        this.parent = parent;
        copy( tree );
        stepSize = isRoot
                ? 0.0
                : G * mass() * parent.mass() / Math.pow( orbitRadius, 2.0 ); // radians/time step
    }
        
    /**
     * Deep copy tree associated with positive integer, apart from: 
     * - parent-related attributes
     * - isPositve attribute
     * @param tree 
     */
    void copy( Tree tree )
    {
        isPositive = tree.isPositive;
        positiveInteger = tree.positiveInteger;
        height = tree.height;
        width = tree.width;
        factorTrees = tree.factorTrees()
                .stream()
                .map( t -> new Tree( t, this ) )
                .collect( Collectors.toList());
        diameter = tree.diameter;
        computeOrbitRadius();
        final int nFactors = factorTrees.size();
        for ( int i = 0; i < nFactors; i++ )
        {
            factorTrees.get( i ).orbitAngle = 2.0 * Math.PI * ( ( double ) i ) / nFactors;
        }
    }
    
    /**
     * The integer that corresponds to the tree. It may be negative.
     * @return the integer corresponding to the tree.
     */
    public Integer n() { return ( isPositive ) ? positiveInteger : -positiveInteger; }
    
    private void computeOrbitRadius()
    {
        double maxSatelliteOrbitRadius = ( factorTrees.isEmpty() ) 
                ? 0.0
                : factorTrees.stream()
                        .mapToDouble( Tree::orbitRadius )
                        .max()
                        .getAsDouble();
        
        // compute distance from its parent
        double parentRadius = isRoot 
                ? 0.0
                : ( parent.diameter < 2.0 ) ? 1.0 : parent.diameter / 2.0;
        orbitRadius = isRoot ? 0.0 : parentRadius + maxSatelliteOrbitRadius + ( 2 * parent.diameter + 1.0 ) + 5.0;
    }
        
    /**
     * List the prime factors of the argument.
     * @param n the number whose primes factors are sought.
     * @return
     */
    public List<Integer> primeFactors( int n )
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
       
    int height() { return height; }
    
    List<Tree> factorTrees() { return factorTrees; }
    
    int getPositiveInteger() { return positiveInteger; }
    
    String getStringView() { return new String( viewString( "   ") ); }
    
    @Override
    public String toString() { return toString( "  " ).toString(); }
    
    private StringBuilder toString( String pad )
    {
         StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append( pad ).append( '\n' ).append( pad )
                .append( "isRoot: " ).append(isRoot ). append( "  " )
                .append( isPositive ? "" : "-")
                .append( positiveInteger ).append( "  " )
                .append( positiveInteger < primes.size() ? prime( positiveInteger ) : "" )
                .append( " diameter: " + diameter )
                .append( " orbitRadius: " + orbitRadius )
//                .append( " orbitAngle: " + orbitAngle )
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
    
    // viewTree parameters, in pixels
    private static final int ELEMENT  = 8; 
    private static final int RADIUS   = ELEMENT; 
            static final int PAD      = 3 * ELEMENT; 
    private static final int DELTA    = 2 * ( PAD + RADIUS );
    private static final int DIAMETER = 2 * RADIUS;
    
    /**
     * A BufferedImage view of the tree.
     * @return a BufferedImage view of the tree.
     */
    public BufferedImage getTreeView()
    {
        BufferedImage bufferedImage = new BufferedImage( imageViewWidth(), imageViewHeight(), BufferedImage.TYPE_INT_ARGB );
        viewTree( bufferedImage.getGraphics(), PAD, PAD );
        return bufferedImage;
    }
    
    /**
     * A BufferedImage view of the circular tree.
     * @return a BufferedImage view of the tree.
     */
    public BufferedImage getCircularTreeView()
    {
//        BufferedImage bufferedImage = new BufferedImage( IMAGE_VIEWPORT_SIZE, IMAGE_VIEWPORT_SIZE, BufferedImage.TYPE_INT_ARGB );
        CIRCULAR_TREE_WIDTH = (int) circularTreeRadius * 8;
        BufferedImage bufferedImage = new BufferedImage( CIRCULAR_TREE_WIDTH, CIRCULAR_TREE_WIDTH, BufferedImage.TYPE_INT_ARGB );
        viewCircularTree( bufferedImage.getGraphics(), 0, 0, 0.0 );
        return bufferedImage;
    }
    
    BufferedImage getPlanetsView()
    {
        BufferedImage bufferedImage = new BufferedImage( imageViewWidth(), imageViewHeight(), BufferedImage.TYPE_INT_ARGB );
        viewPlanets( bufferedImage.getGraphics() );
        return bufferedImage;
    }

    /**
     * Draw the conventional tree view of the tree.
     * @param g
     * @param x col of upper left corner of rectangle containing tree
     * @param y row of upper left corner of rectangle containing tree
     */
    void viewTree( Graphics g, int x, int y )
    {
        Graphics graphics = g.create();
        graphics.setColor( Color.BLACK );
                       
        // coordinates of center of root
        int rootX = x + treeRootX();
        int rootY = y + treeRootY();
                   
        // set 1st factor tree's upperleft corner coordinates 
        int factorTreeX = x;
        int factorTreeY = y + DELTA;
        
        for ( Tree factorTree : factorTrees )
        {
            // draw edge from this root to factorTree root
            graphics.drawLine( rootX, rootY, factorTreeX + factorTree.treeRootX(), factorTreeY + factorTree.treeRootY() );
            
            // draw factorTree
            factorTree.viewTree( graphics, factorTreeX, factorTreeY );
            
            // set next factorTree's upperleft corner's x coordinate; y is unchanged
            factorTreeX += DELTA * factorTree.width; 
        }
        
        // draw this root
        drawDisk( graphics, rootX, rootY );
    }
    
    /**
     * Draw the circular tree view.
     * @param g
     * @param x col of upper left corner of rectangle containing tree
     * @param y row of upper left corner of rectangle containing tree
     */
    void viewCircularTree( Graphics g, int rootX, int rootY, double parentStartAngle )
    {
        Graphics graphics = g.create();
        graphics.setColor( Color.BLACK );
        
        // base case
        if ( positiveInteger == 1 )
        {
            drawNode( graphics, rootX, rootY ); // draw this root
            return;
        }
        
        // recursive case
        final double nSectors = ( isRoot ? 0 : 1 ) + factorTrees.size();
        final double sectorAngle = 2.0 * Math.PI / nSectors;
        double startAngle = isRoot ? 0.0 : parentStartAngle + Math.PI + sectorAngle;
        for ( Tree factorTree : factorTrees )
        {
            // set factorTree root coordinates
            int factorTreeRootX = rootX + (int) ( circularTreeRadius * Math.cos( startAngle ) );
            int factorTreeRootY = rootY + (int) ( circularTreeRadius * Math.sin( startAngle ) );
            
            // draw edge from this root to factorTree root
            drawLine( graphics, rootX, rootY, factorTreeRootX, factorTreeRootY );
            
            // draw factor tree
            factorTree.viewCircularTree(graphics, factorTreeRootX, factorTreeRootY, startAngle );
            
            // draw factor tree root
            factorTree.drawNode( graphics, factorTreeRootX, factorTreeRootY );
            
            // increment sectorStartAngle for next factorTree
            startAngle += sectorAngle; 
        }
        drawNode( graphics, rootX, rootY ); // draw this root
    }
    
    private void drawNode( Graphics graphics, int x, int y )
    {        
        // (xi, yi) is upper left corner of circumscribing square
        int xi = transformX( x - RADIUS );
        int yi = transformY( y + RADIUS );
        if ( ! isRoot )
        {
            graphics.fillOval( xi, yi, DIAMETER, DIAMETER );
        }
        else
        {
            graphics.setColor( Color.WHITE );
            graphics.fillOval( xi, yi, DIAMETER, DIAMETER );
            graphics.setColor( Color.BLACK );
            graphics.drawOval( xi, yi, DIAMETER, DIAMETER );
        }
        if ( LABEL_NODES )
        {
            graphics.drawString("" + n(), xi - 5, yi - 5);
        }
    }
    
    private void drawLine( Graphics graphics, int x1, int y1, int x2, int y2 )
    {        
        graphics.drawLine( transformX( x1 ), transformY( y1 ), transformX( x2 ), transformY( y2 ) );
    }
//    private int translate( int x ) { return x + IMAGE_VIEWPORT_SIZE / 2; }
    private int translate( int x ) { return x + CIRCULAR_TREE_WIDTH / 2; }
    private int transformX( int x ) { return translate( x ); }
    private int transformY( int y ) { return translate( - y ); }
            
    /**
     *
     * @param graphics of image on which tree is rendered
     */
    void viewPlanets( Graphics graphics )
    {                      
        move();
        draw( graphics );
    }
    
    /**
     * The width in pixels of the tree view.
     * @return width in pixels of rectangle enclosing image of tree
     */
    public int imageViewWidth() { return ( width() + 1 ) * DELTA; }
    
    /**
     * The height in pixels of the tree view.
     * @return
     */
    public int imageViewHeight() { return ( height() + 1 ) * DELTA; }
    
    private void drawDisk( Graphics graphics, int x, int y )
    {
        if ( ! isRoot )
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
        
    int treeRootX() { return width * DELTA / 2 - RADIUS; }
    
    int treeRootY() { return PAD + RADIUS; }
    
    //_______________________________
    //
    // planetary motion methods
    //_______________________________
    void move() 
    {
        // move this Body
        if ( isRoot )
        {
            x = y = 0.0;
        }
        else
        {
            orbitAngle += stepSize;
            if ( orbitAngle > 2 * Math.PI )
            {
                orbitAngle -= 2 * Math.PI;
            }
            x = parent.x + orbitRadius * cos( orbitAngle );
            y = parent.y + orbitRadius * sin( orbitAngle );
        }
        
        // move my sateillites
        factorTrees.forEach( satellite -> satellite.move() );
    }
    
    void draw( Graphics graphics )
     {
         // draw this
         if ( SHOW_ORBIT ) 
         {
            double parentX = isRoot ? 0 : parent.x;
            double parentY = isRoot ? 0 : parent.y;
            graphics.setColor( Color.RED );
            graphics.drawOval( (int) ( OFFESET + SCALE * ( parentX - orbitRadius ) ),
                               (int) ( OFFESET + SCALE * ( parentY - orbitRadius ) ),
                               (int) ( SCALE * 2.0 * orbitRadius ),
                               (int) ( SCALE * 2.0 * orbitRadius )
                             );
         }
         graphics.setColor( color );
         graphics.fillOval( (int) ( OFFESET + SCALE * ( x - diameter / 2.0 ) ), 
                            (int) ( OFFESET + SCALE * ( y - diameter / 2.0 ) ), 
                            (int) ( SCALE * diameter ), 
                            (int) ( SCALE * diameter ) 
                          );
         
        // draw my satellites
        factorTrees.forEach( satellite -> satellite.draw( graphics ) );
     }
    
    int width() { return width; }
 
    double mass() { return positiveInteger; }
    
    double orbitRadius() { return orbitRadius; }
}
