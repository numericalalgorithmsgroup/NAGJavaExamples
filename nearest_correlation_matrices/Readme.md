# Nearest Correlation Matrices

This notebook looks at computing *nearest correlation matrices* using the NAG Library for *Java*.

The source of this example is in [NcmNag.java](./NcmNag.java).

## Correlation Matrices

* An $n$ by $n$ matrix is a correlation matrix if:
  * it is symmetric
  * it has ones on the diagonal 
  * its eigenvalues are non-negative (positive semidefinite)           
  
  
  $$ \Large Ax = \lambda x, \quad x \neq 0$$


* The element in the $i$th row and $j$th column is the correlation between the $i$th and $j$th variables. This could be stock process, for example.
