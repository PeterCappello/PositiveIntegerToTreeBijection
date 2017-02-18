# PositiveIntegerToTreeBijection

Goal: Provide a visual representation of non-zero integers as rooted, unoriented, finite trees 
based on a bijection described in:

[Peter Cappello. A New Bijection between Natural Numbers and Rooted Trees. 4th SIAM Conf. on Discrete Mathematics, San Francisco, June 1988.](https://www.cs.ucsb.edu/~cappello/papers/1988SiamDM.html)

## The bijection

The [bijection](https://en.wikipedia.org/wiki/Bijection) associated with this project is between the set of positive integers
{ 1, 2, 3, ... } 
and the set of finite, rooted, unoriented trees, where:

* A tree is a graph with no cycles. 

* A tree is _rooted_ when exactly 1 of its vertices has been designated as the root.

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

Let **N**, **P**, **T** denote the sets of the natural numbers, the prime numbers, and the finite, rooted, undirected trees, respectively. 
Let function _p_ : **N** &map; **P** denote the _n_<sup>th</sup> prime (e.g., _p_( 4 ) = 7 ).
Then, the inverse function of _p_, _p_<sup>-1</sup> maps a prime number to its index or rank (e.g., _p_<sup>-1</sup> ( 7 ) = 4).

The bijection &tau; : **N** &map; **T** is defined recursively as follows:

1. τ ( 1 ) is the rooted tree comprised of exactly one node, its root;

1. for 1 <  _n_ = p<sub>1</sub>, p<sub>2</sub>, ... , p<sub>k</sub>, 
 where the p<sub>i</sub> are its prime factors, 
 _p_<sup>-1</sup>( p<sub>1</sub> ),  _p_<sup>-1</sup>( p<sub>2</sub> ), ... ,  _p_<sup>-1</sup>( p<sub>k</sub> ) are the ranks of _n_'s prime factors, 
   τ ( _n_ ) is the rooted, unoriented tree with _k_ subtrees,
   τ ( _p_<sup>-1</sup>( p<sub>1</sub> ) ), τ ( _p_<sup>-1</sup>( p<sub>2</sub> ) ), ..., τ ( _p_<sup>-1</sup>( p<sub>k</sub> ) ).

The tree &tau; ( 399 ) is illustrated below.  In that figure, the root is labelled with the number that corresponds to the tree;
Each node other than the root is labeled with numbers on the left and right.  The number on a node's left side is a prime factor of the number corresponding to its _parent tree_ (e.g., 399 = 3 * 7 * 19); the number on the node's right is the _rank_ of the prime number on its left (e.g., 19 is the 8<sup>th</sup> prime number) and represents the number of the subtree rooted at that node.

![1](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/399.png "Tree corresponding to 399")

The bijection is illustrated below for the integers 1 to 32.

![1](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/1.png "Tree corresponding to 1")
![2](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/2.png "Tree corresponding to 2")
![3](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/3.png "Tree corresponding to 3")
![4](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/4.png "Tree corresponding to 4")
![5](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/5.png "Tree corresponding to 5")

<hr />

![6](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/6.png "Tree corresponding to 6")
![7](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/7.png "Tree corresponding to 7")
![8](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/8.png "Tree corresponding to 8")
![9](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/9.png "Tree corresponding to 9")

<hr />

![10](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/10.png "Tree corresponding to 10")
![11](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/11.png "Tree corresponding to 11")
![12](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/12.png "Tree corresponding to 12")
![13](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/13.png "Tree corresponding to 13")

<hr />

![14](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/14.png "Tree corresponding to 14")
![15](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/15.png "Tree corresponding to 15")
![16](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/16.png "Tree corresponding to 16")

<hr />

![17](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/17.png "Tree corresponding to 17")
![18](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/18.png "Tree corresponding to 18")
![19](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/19.png "Tree corresponding to 19")

<hr />

![20](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/20.png "Tree corresponding to 20")
![21](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/21.png "Tree corresponding to 21")
![22](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/22.png "Tree corresponding to 22")

<hr />

![23](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/23.png "Tree corresponding to 23")
![24](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/24.png "Tree corresponding to 24")
![25](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/25.png "Tree corresponding to 25")

<hr />

![26](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/26.png "Tree corresponding to 26")
![27](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/27.png "Tree corresponding to 27")
![28](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/28.png "Tree corresponding to 28")

<hr />

![29](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/29.png "Tree corresponding to 29")
![30](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/30.png "Tree corresponding to 30")
![31](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/31.png "Tree corresponding to 31")

<hr />

![32](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/32.png "Tree corresponding to 32")

The map τ is a bijection. Its inverse is defined recursively:

1. &tau; ( &dot; ) = 1;

2. For tree _t_ adjacent to subtrees _t_<sub>1</sub>, _t_<sub>2</sub>, · · · , _t_<sub>k</sub>, 

<p style="text-align: center;">&tau;<sup>-1</sup> ( _t_ ) = &Pi;<sub>_t_ = 1 to _k_</sub> = _p_ ( &tau;<sup>-1</sup> ( _t_<sub>_k_</sub> ) ) .</p>

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
