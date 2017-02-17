# PositiveIntegerToTreeBijection

Goal: Provide a visual representation of non-zero integers as rooted, unoriented, finite trees 
based on a bijection described in:

[Peter Cappello. A New Bijection between Natural Numbers and Rooted Trees. 4th SIAM Conf. on Discrete Mathematics, San Francisco, June 1988.](https://www.cs.ucsb.edu/~cappello/papers/1988SiamDM.html)

## The bijection

The [bijection](https://en.wikipedia.org/wiki/Bijection) associated with this project is between the set of positive integers
{ 1, 2, 3, ... } 
and the set of finite, rooted, unoriented trees, where:

* A tree is a graph with no cycles. 

* A tree is _rooted_ when one of its vertices has been designated as the root.

* A tree is _unoriented_ when its edges are unoriented (aka undirected).

The bijection makes use of the concept of a [prime number](https://en.wikipedia.org/wiki/Prime_number)
and its index or rank, which is illustrated in the table below.

rank   | prime           
 ---: | :---
1 | 2  
2 | 3
3 | 5
4 | 7
5 | 11 
6 | 13
7 | 17

Let _r( n )_ denote the r<sup>th</sup> prime (e.g., _r_( 4 ) = 7 ).

The bijection is defined recursively as follows:

1. 1 maps to the tree consisting of only a root;
2. Let 1 <  _n_ = p<sub>1</sub>, p<sub>2</sub>, ... , p<sub>k</sub>, where the p<sub>i</sub> are its prime factors, and
_r_( p<sub>1</sub> ), _r_( p<sub>2</sub> ), ... , _r_( p<sub>k</sub> ) be the ranks of _n_'s prime factors.
Then n maps to the tree with a root and a subtree associated with _r_( p<sub>k</sub> ), for each of its _k_ prime factors.

The bijection is illustrated for integers 1 to 32.

<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/1.png" alt="Tree corresponding to 1">
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/2.png" alt="Tree corresponding to 2">
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/3.png" alt="Tree corresponding to 3">
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/4.png" alt="Tree corresponding to 1">
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/5.png" alt="Tree corresponding to 1">
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/6.png" alt="Tree corresponding to 1">
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/7.png" alt="Tree corresponding to 1">
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/8.png" alt="Tree corresponding to 1">

![7](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/7.png "Tree corresponding to 1")


There is a "natural" presentation order of the subtrees, based on the natural order of the prime factors.

The bijection defines a total ordering on this set of trees.
However, there is a lattice based on containment of the multiset of prime factors.
It might be interesting to look at it for [1, _n_], for some fixed values of _n_.

## The project
The project currently enables the tree visualization of non-zero integers 
whose largest prime factor is < 10,000,000.

It produces a map of the first n primes.
When n < 1,000,000, the process takes a few seconds. 
It may take a couple of minutes to produce 10,000,000 primes.

## Running the project

From the project's root directory, run the command 
<pre><code>java -jar dist/PositiveIntegerToTreeBijection.jar</code></pre> 
