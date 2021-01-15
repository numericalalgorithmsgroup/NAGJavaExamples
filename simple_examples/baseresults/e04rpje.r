E04RPJ Example Program Results

Passing SDP problem to solver

 Overview
   Status:                  Problem and option settings are editable.
   No of variables:         5
   Objective function:      linear
   Simple bounds:           not defined yet
   Linear constraints:      not defined yet
   Nonlinear constraints:   not defined yet
   Cone constraints:        not defined yet
   Matrix constraints:      2
 Matrix constraints
   IDblk =     1, size =      2 x     2, polynomial of order 2
   IDblk =     2, size =      2 x     2, linear
 E04SV, NLP-SDP Solver (Pennon)
 ------------------------------
 Number of variables             5                 [eliminated            0]
                            simple  linear  nonlin
 (Standard) inequalities         0       0       0
 (Standard) equalities                   0       0
 Matrix inequalities                     1       1 [dense    2, sparse    0]
                                                   [max dimension         2]

 Begin of Options
     Print File                    =                   6     * d
     Print Level                   =                   2     * d
     Print Options                 =                 Yes     * d
     Monitoring File               =                  -1     * d
     Monitoring Level              =                   4     * d
     Monitor Frequency             =                   0     * d

     Infinite Bound Size           =         1.00000E+20     * d
     Task                          =            Minimize     * d
     Stats Time                    =                  No     * d

     Dimacs Measures               =                  No     * S
     Hessian Density               =               Dense     * S
     Init Value P                  =         1.00000E+00     * d
     Init Value Pmat               =         1.00000E+00     * d
     Initial P                     =           Automatic     * d
     Initial U                     =           Automatic     * d
     Initial X                     =                User     * d
     Inner Iteration Limit         =                 100     * d
     Inner Stop Criteria           =           Heuristic     * d
     Inner Stop Tolerance          =         1.00000E-02     * d
     Linesearch Mode               =           Goldstein     * S
     Outer Iteration Limit         =                 100     * d
     P Min                         =         1.05367E-08     * d
     P Update Speed                =                  12     * d
     Pmat Min                      =         1.05367E-08     * d
     Preference                    =               Speed     * d
     Presolve Block Detect         =                 Yes     * d
     Stop Criteria                 =                Soft     * d
     Stop Tolerance 1              =         1.00000E-06     * d
     Stop Tolerance 2              =         1.00000E-07     * d
     Stop Tolerance Feasibility    =         1.00000E-07     * d
     Transform Constraints         =                  No     * S
     U Update Restriction          =         5.00000E-01     * d
     Umat Update Restriction       =         3.00000E-01     * d
 End of Options
 --------------------------------------------------------------
  it|  objective |  optim  |   feas  |  compl  | pen min |inner
 --------------------------------------------------------------
   0  0.00000E+00  1.82E+01  1.00E+00  4.00E+00  2.00E+00   0
   1  4.11823E+00  3.85E-03  0.00E+00  1.73E+00  2.00E+00   6
   2  2.58252E+00  5.36E-03  0.00E+00  4.93E-01  9.04E-01   4
   3  2.06132E+00  1.02E-03  0.00E+00  7.70E-02  4.08E-01   4
   4  2.00050E+00  3.00E-03  8.91E-03  1.78E-02  1.85E-01   3
   5  1.99929E+00  1.55E-03  3.16E-03  3.65E-03  8.34E-02   2
   6  1.99985E+00  1.03E-04  3.16E-04  7.19E-04  3.77E-02   4
   7  1.99997E+00  7.04E-04  5.76E-05  1.41E-04  1.70E-02   1
   8  2.00000E+00  1.32E-04  6.52E-06  2.76E-05  7.70E-03   1
   9  2.00000E+00  8.49E-06  7.86E-07  5.37E-06  3.48E-03   1
  10  2.00000E+00  5.88E-07  1.06E-07  1.04E-06  1.57E-03   1
  11  2.00000E+00  5.55E-08  4.87E-08  2.02E-07  7.11E-04   1
  12  2.00000E+00  5.34E-09  5.37E-09  3.93E-08  3.21E-04   1
  13  2.00000E+00  5.03E-10  5.45E-09  7.62E-09  1.45E-04   1
  14  2.00000E+00  4.45E-11  5.55E-09  1.48E-09  6.56E-05   1
 --------------------------------------------------------------
  it|  objective |  optim  |   feas  |  compl  | pen min |inner
 --------------------------------------------------------------
  15  2.00000E+00  4.36E-12  5.67E-09  2.87E-10  2.96E-05   1
  16  2.00000E+00  1.61E-11  5.82E-09  5.57E-11  1.34E-05   1
  17  2.00000E+00  3.13E-11  6.00E-09  1.08E-11  6.06E-06   1
  18  2.00000E+00  8.65E-11  6.22E-09  2.10E-12  2.74E-06   1
  19  2.00000E+00  1.31E-10  6.48E-09  4.07E-13  1.24E-06   1
 --------------------------------------------------------------
 Status: converged, an optimal solution found
 --------------------------------------------------------------
 Final objective value                2.000000E+00
 Relative precision                   8.141636E-16
 Optimality                           1.310533E-10
 Feasibility                          6.484489E-09
 Complementarity                      4.066867E-13
 Iteration counts
   Outer iterations                             19
   Inner iterations                             36
   Linesearch steps                             56
 Evaluation counts
   Augm. Lagr. values                           76
   Augm. Lagr. gradient                         56
   Augm. Lagr. hessian                          36
 --------------------------------------------------------------