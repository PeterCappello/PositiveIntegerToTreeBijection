# PositiveIntegerToTreeBijection

Goal: Provide a visual representation of non-zero integers as rooted, unoriented, finite trees 
based on the bijection described in (1).  

This publication was based on course project in my first year of the computer science Ph.D. program in Princeton University. That course completed in May of 1979. In May of 2017, after starting this Readme, I noticed that this bijection had been published in August of 1980 by F. Gobel (2).  The publication was received by the editors on June 26, 1978, approximately 1 year before my course project report. In 1994, Y. Abe published the same bijection (3). 

### References
1. [Peter Cappello. A New Bijection between Natural Numbers and Rooted Trees. 4th SIAM Conf. on Discrete Mathematics, San Francisco, June 1988.](https://www.cs.ucsb.edu/~cappello/papers/1988SiamDM.html)
1. [F. Gobel, On a 1-1-correspondence between rooted trees and natural numbers, Journal of Combinatorial Theory, Series B, Volume 29, Issue 1, August 1980, Pages 141-143.](https://www.sciencedirect.com/science/article/pii/0095895680900490)
1. [Y. Abe, Tree representation of positive integers, Applied Mathematics Letters, Volume 7, Issue 1, January 1994, Page 57.](https://www.sciencedirect.com/science/article/pii/0893965994900531?via%3Dihub)

## Contents

1. The bijection
   * Tree presentations
1. The _explorer_ application
1. The _game_ application

## The bijection

The [bijection](https://en.wikipedia.org/wiki/Bijection) associated with this project is between the set of positive integers
{ 1, 2, 3, ... } 
and the set of finite, unoriented [rooted trees](https://en.wikipedia.org/wiki/Tree_(graph_theory)), 
where _unoriented_ means that its edges are unoriented (aka undirected).

The bijection uses the concept of a [prime number](https://en.wikipedia.org/wiki/Prime_number) and its rank or index, 
which is illustrated in the table below.

index   | prime           
 ---: | :---
1 | 2  
2 | 3
3 | 5
4 | 7
5 | 11 
6 | 13
7 | 17

Let **N**, **P**, and **T** denote the set of positive integers, prime numbers, and finite, rooted, undirected trees, respectively. 
Let function _p_ : **N** &map; **P** denote the _n_<sup>th</sup> prime (e.g., _p_( 4 ) = 7 ).
The inverse function of _p_, _p_<sup>-1</sup> : **P** &map; **N** maps a prime number to its index (e.g., _p_<sup>-1</sup> ( 7 ) = 4).

The invertible function &tau; : **N** &map; **T** is defined recursively as follows:

1. τ ( 1 ) is the rooted tree comprised of exactly one node, its root;

1. for 1 <  _n_ = p<sub>1</sub> p<sub>2</sub> ...  p<sub>k</sub>, 
 where each p<sub>i</sub> is 1 of _n_'s _k_ _prime factors_ 
  
 τ ( _n_ ) is the rooted, unoriented tree with _k_ _subtrees_ 
 
 τ ( _r_<sub>1</sub> ), τ ( _r_<sub>2</sub> ), ..., τ ( _r_<sub>k</sub> ), where
 
  _r_<sub>i</sub> = _p_<sup>-1</sup>( p<sub>i</sub> ) is the index of  p<sub>i</sub>, where 
  
  p<sub>i</sub> is the _i_<sup>th</sup> prime factor of _n_, for 1 &le; i &le; k.

The tree &tau; ( 399 ) is illustrated below with a conventional tree presentation.  
In that figure, the root is labeled with the number that corresponds to the tree;
Each node other than the root is labeled with numbers on its left and right.  
The number on a node's left side is a prime factor of the number corresponding to its _parent tree_ (e.g., 399 = 3 * 7 * 19); the number on the node's right is the _index_ of the prime number on its left (e.g., 19 is the 8<sup>th</sup> prime number) and corresponds to the number of the tree rooted at that node.

![1](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/399.png "Tree corresponding to 399")

The map τ is a bijection. Its inverse map,  &tau;<sup>-1</sup> : **T** &map; **N**, is defined recursively:

1. &tau;<sup>-1</sup> ( &bull; ) = 1;

2. For tree _t_ whose root is adjacent to subtrees _t_<sub>1</sub>, _t_<sub>2</sub>, · · · , _t_<sub>k</sub>, 

 &tau;<sup>-1</sup> ( _t_ ) = &Pi;<sub>_i_ = 1 to _k_</sub> _p_ ( &tau;<sup>-1</sup> ( _t_<sub>_i_</sub> ) ).


### Tree presentations

#### A conventional tree presentation

In a conventional tree presentation, the root is presented on top;
subtrees are presented below.  
For the bijection, each subtree represents the index of a prime factor of the number represented by the tree.
The trees representing these indices are presented smallest to largest from left to right.

(_Insert a schematic diagram of the conventional tree layout._)

&tau; ( _n_ ) is illustrated for 1 &le; _n_ &le; 32, presented as conventional trees.

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

#### A circular tree presentation

In a circular tree presentation, the root is presented at the center;
subtrees are presented around the root.
A tree that has _n_ subtrees is presented with its root at the center of a circle 
that is divided into _n_ sectors of equal angle, where each subtree is presented in exactly 1 of these _n_ sectors.
A _subtree_ that has _n_ prime factors is presented by a circle whose center is the root and which is divided into _n + 1_ sectors of equal angle.
One of the sectors is used to present the edge from the root of the subtree to the root of its parent.

For example, if _32_ has 5 prime factors of 2, whose index is 1.
Its circular tree has 5 subtrees, each occupying a sector of angle _2&pi; / 5_ radians.
In general, the subtrees are ordered from smallest to largest around the root, 
where the sector containing the tree corresponding to the smallest prime factor begins at 0 radians 
relative to the edge that connects the root of the subtree to its parent.

(_Insert a schematic diagram of the circular tree layout._)

&tau; ( _n_ ) is illustrated for 1 &le; _n_ &le; 32, presented as circular trees.

<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/1c.png" alt="Tree circular corresponding to 1" >
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/2c.png" alt="Tree circular corresponding to 2" >
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/3c.png" alt="Tree circular corresponding to 3" >
<img src="https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/4c.png" alt="Tree circular corresponding to 4" >


![1](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/1c.png "Tree circular corresponding to 1")
![2](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/2c.png "Tree circular corresponding to 2")
![3](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/3c.png "Tree circular corresponding to 3")
![4](https://github.com/PeterCappello/PositiveIntegerToTreeBijection/blob/master/src/images/4c.png "Tree circular corresponding to 4")

### An associated total ordering of finite rooted trees

A total order of rooted finite trees suggests itself: If _s_, _t_ &isin; **T**, then _s_ &#8828; _t_ if and only if &tau;<sup>-1</sup>( _s_ ) ≤ &tau;<sup>-1</sup>( _t_ ).

There also is a lattice based on containment of the multiset of prime factors.
It might be interesting to look at it for [1, _n_], for some fixed values of _n_.

### An associated canonical tree presentation

A prime factorization of a number is in _canonical_ order when the primes are presented in nondecreasing order. An analogue for rooted trees is offered below. Let τ ( _n_ ) = _t_, where the canonical order of the prime factorization of _n_ is _p_<sub>1</sub>, _p_<sub>2</sub>, . . . , _p_<sub>k</sub>. Tree _t_ is presented _canonically_ when:

1. The rooted trees _t_<sub>1</sub>, _t_<sub>2</sub>, . . . , _t_<sub>k</sub> corresponding to the factors _p_<sub>1</sub>, _p_<sub>2</sub>, . . . , _p_<sub>k</sub>, respectively, are presented from left to right.

2. Each rooted tree  _t_<sub>i</sub> is presented canonically, for 1 &le; _i_ &le; _k_.

The rooted trees in presented above that correspond to numbers 1, 2, ..., 32 are presented canonically.

## The _bijection explorer_ application
The project currently enables the tree visualization of non-zero integers 
whose largest prime factor is < 10,000,000.

### Running the project

From the project's root directory, run the command 
<pre><code>java -jar dist/PositiveIntegerToTreeBijection.jar</code></pre> 

### User interface functionality
...

### Architecture

It produces a map of the first n primes.
When n < 1,000,000, the process takes a few seconds. 

### Performance

It may take a couple of minutes to produce 10,000,000 primes.

### Feature roadmap
* Alternate visualizations (e.g., "astronomical", "string and mass")

### Presentation API
...

## The _bijection game_ application

To be elaborated as the game development progresses - although a bare bones version currently exists.

