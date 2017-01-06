# PositiveIntegerToTreeBijection

The goal for this project is to provide a visual representation of non-zero integers as rooted, unoriented, finite trees 
based on the following:

[Peter Cappello. A New Bijection between Natural Numbers and Rooted Trees. 4th SIAM Conf. on Discrete Mathematics, San Francisco, June 1988.](https://www.cs.ucsb.edu/~cappello/papers/1988SiamDM.html)

The project currently enables the tree visualization of non-zero integers 
whose largest prime factor < 16,000,000.

It produces a map of the first n primes.
When n > 1,000,000, the process takes a few seconds. 
It may take a couple of minutes to produce 16,000,000 primes.

## Running the project

From the project's root directory, run the command 
<pre><code>java -jar dist/PositiveIntegerToTreeBijection.jar</code></pre> 
