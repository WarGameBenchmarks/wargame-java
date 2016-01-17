WarGame Java
============

This is an updated version of the longstanding WarGame project from [@ryanmr](http://twitter/ryanmr), the kinda-sorta benchmark.

**Disclaimer:** WarGame Java is a tiny kinda-sorta benchmark, and as such, it should not be taken too seriously.

Changelog
---------

Unfortunately, time has erased the progress of this project. Hopefully this brief explanation can fill in that gap.

It is worth noting that the original implementation of the WarGame was written in Java, while [@ryanmr](http://twitter.com/ryanmr) was a sophomore in high school, and every so often, he would revisit the project and expand on the progress.

In a sense this is the 4th major version:

1. Initial version written sometime in an air conditioned basement in 2009, designed to simply iterate to 1 million games.
2. The *variation* approach was implemented, where the game would continue for *speed seconds* until the speed did not change within 4 decimal places
3. A minor update to the second version, where the Java was Java-ized to be *pragmatic Java*, by which I mean, disgusting.
4. A major update to mirror support the Rust and Go versions, now implementing the standardized prime and sample time approaches.

Legend
------

You definitely should read the [legend](https://github.com/WarGameBenchmarks/wargame/blob/master/legend.md) to learn about the output.

How To Run
----------

With the executable *JAR* directly:

```
java -jar wargame-java.jar [number of threads] [multiplier]
```

If the `number of threads` are not specified, the default is 1 thread. Specify the `multiplier` for additional run time according to the [legend](https://github.com/WarGameBenchmarks/wargame/blob/master/legend.md).

How To Compile
--------------

To compile:

```
./compile.sh
```

To run likewise:

```
./run.sh [number of threads] [multiplier]
```

To bundle the class files into a jar, use the provided `bundle.sh` file:

```
./bundle.sh
```

Sample Output
------

```
➜  wargame-java ✗ java -jar wargame-java.jar    
WarGame Java
settings: threads = 1; multiplier = 1.00

4. done                                                                 	
---
Samples:      9981
Mean:	  20.26894
Median:	  20.46168
S.D.:	   0.41262
C.O.V.:	   0.02036
---
Min-Max:	 <  18.96038 -  20.66101 > Δ   1.70063
1-σ:		 <  19.85633 -  20.68156 > Δ   0.82523
μ-Median:	 <  20.26894 -  20.46168 > Δ   0.19273
99.9% CI:	 <  20.25535 -  20.28254 > Δ   0.02718
---
Threads: 1
Multiplier: 1.00
Speed: 20.49160 g/ms
Games: 1229615
Duration: 60.0s
---
Rank: (3/5) B
Rank Criteria: 1 | 2 | 4
---
Score: 20
```
