 E04TCJ Example Program Results

 First solve the problem with the outliers

 --------------------------------------------------------
 E04GG, Nonlinear least squares method for bound-constrained problems
 Status: converged, an optimal solution was found
 Value of the objective             1.05037E+00
 Norm of gradient                   8.78014E-06
 Norm of scaled gradient            6.05781E-06
 Norm of step                       1.47886E-01
 
 Primal variables:
   idx   Lower bound       Value       Upper bound
     1       -inf        3.61301E-01        inf
     2       -inf        9.10227E-01        inf
     3       -inf        3.42138E-03        inf
     4       -inf       -6.08965E+00        inf
     5       -inf        6.24881E-04        inf
 --------------------------------------------------------

 Now remove the outlier residuals from the problem handle

 --------------------------------------------------------
 E04GG, Nonlinear least squares method for bound-constrained problems
 Status: converged, an optimal solution was found
 Value of the objective             5.96811E-02
 Norm of gradient                   1.19914E-06
 Norm of scaled gradient            3.47087E-06
 Norm of step                       3.49256E-06
 
 Primal variables:
   idx   Lower bound       Value       Upper bound
     1       -inf        3.53888E-01        inf
     2       -inf        1.06575E+00        inf
     3       -inf        1.91383E-03        inf
     4       -inf        2.17299E-01        inf
     5       -inf        5.17660E+00        inf
 --------------------------------------------------------

 Assuming the outliers points are measured again
 we can enable the residuals and adjust the values

 --------------------------------------------------------
 E04GG, Nonlinear least squares method for bound-constrained problems
 Status: converged, an optimal solution was found
 Value of the objective             6.51802E-02
 Norm of gradient                   2.57338E-07
 Norm of scaled gradient            7.12740E-07
 Norm of step                       1.56251E-05
 
 Primal variables:
   idx   Lower bound       Value       Upper bound
     1   3.00000E-01    3.00000E-01    3.00000E-01
     2       -inf       1.06039E+00         inf
     3       -inf       2.11765E-02         inf
     4       -inf       2.11749E-01         inf
     5       -inf       5.16415E+00         inf
