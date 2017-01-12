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

import java.util.Random;

/**
 *
 * @author Peter Cappello
 */
public class Game 
{
    private final boolean type;
    private final int limit;
    private final Random random = new Random();
    
    private int score = 0;
    private int maxInt;
    private int round = 0;
    
    Game( int level, boolean type, int limit )
    {
        this.type = type;
        this.limit = limit;
        maxInt = (int) Math.pow( 2, level );
        random.nextInt( maxInt ); // throw it away
    }
    
    boolean isOver() { return round >= limit; }
    
    int next() 
    { 
        round++;
        return 1 + random.nextInt( maxInt ); 
    }
    
    int processAnswer( boolean correct ) 
    { 
        score = ( correct ) ? score + 1 : score - 1;
        return score;
    }
    
    int round() { return round; }
}
