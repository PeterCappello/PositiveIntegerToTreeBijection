# PositiveIntegerToTreeBijection

Goal: Provide a visual representation of non-zero integers as rooted, unoriented, finite trees 
based on a bijection described in:

[Peter Cappello. A New Bijection between Natural Numbers and Rooted Trees. 4th SIAM Conf. on Discrete Mathematics, San Francisco, June 1988.](https://www.cs.ucsb.edu/~cappello/papers/1988SiamDM.html)

## The bijection

Before defining the bijection, we define some basic terms.

A bijection is a 1-to-1 correspondence between 2 sets:
It is a map from a set A to a set B that is injective and surjective, 
where:

* A map is injective when distinct elements in the domain (set A) map to distinct
elements in the codomain (set B).

* A map is surjective when every element in the codomain is the image of
some element in the domain.

The bijection associated with this project is between the set of positive integers
{ 1, 2, 3, ... } 
and the set of finite, rooted, unoriented trees, where:

* A tree is a graph with no cycles. 

* A tree is _rooted_ when one of its vertices has been designated as the root.

* A tree is _unoriented_ when its edges are unoriented (aka undirected).

The bijection makes use of the concept of a prime number.
From [Wikipedia](https://en.wikipedia.org/wiki/Prime_number), 
A prime number (or a prime) is a natural number greater than 1 that has no 
positive divisors other than 1 and itself. 

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
