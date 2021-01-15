 E04MTJ Example Program Results

++++++++++ Use the Primal-Dual algorithm ++++++++++

 ----------------------------------------------
  E04MT, Interior point method for LP problems
 ----------------------------------------------

 Original Problem Statistics

   Number of variables          7
   Number of constraints        7
   Free variables               0
   Number of nonzeros          41


 Presolved Problem Statistics

   Number of variables         13
   Number of constraints        7
   Free variables               0
   Number of nonzeros          47


 ------------------------------------------------------------------------------
  it|    pobj    |    dobj    |  optim  |  feas   |  compl  |   mu   | mcc | I
 ------------------------------------------------------------------------------
   0 -7.86591E-02  1.71637E-02  1.27E+00  1.06E+00  8.89E-02  1.5E-01
   1  5.74135E-03 -2.24369E-02  6.11E-16  1.75E-01  2.25E-02  2.8E-02   0
   2  1.96803E-02  1.37067E-02  5.06E-16  2.28E-02  2.91E-03  3.4E-03   0
   3  2.15232E-02  1.96162E-02  7.00E-15  9.24E-03  1.44E-03  1.7E-03   0
   4  2.30321E-02  2.28676E-02  1.15E-15  2.21E-03  2.97E-04  3.4E-04   0
   5  2.35658E-02  2.35803E-02  1.32E-15  1.02E-04  8.41E-06  9.6E-06   0
   6  2.35965E-02  2.35965E-02  1.64E-15  7.02E-08  6.35E-09  7.2E-09   0
Iteration 7
     monit() reports good approximate solution (tol = 1.20E-08):
   7  2.35965E-02  2.35965E-02  1.35E-15  3.52E-11  3.18E-12  3.6E-12   0
 ------------------------------------------------------------------------------
 Status: converged, an optimal solution found
 ------------------------------------------------------------------------------
 Final primal objective value         2.359648E-02
 Final dual objective value           2.359648E-02
 Absolute primal infeasibility        4.168797E-15
 Relative primal infeasibility        1.350467E-15
 Absolute dual infeasibility          5.084353E-11
 Relative dual infeasibility          3.518607E-11
 Absolute complementarity gap         2.685778E-11
 Relative complementarity gap         3.175366E-12
 Iterations                                      7

 Primal variables:
   idx   Lower bound        Value      Upper bound
     1  -1.00000E-02   -1.00000E-02    1.00000E-02
     2  -1.00000E-01   -1.00000E-01    1.50000E-01
     3  -1.00000E-02    3.00000E-02    3.00000E-02
     4  -4.00000E-02    2.00000E-02    2.00000E-02
     5  -1.00000E-01   -6.74853E-02    5.00000E-02
     6  -1.00000E-02   -2.28013E-03         inf
     7  -1.00000E-02   -2.34528E-04         inf

 Box bounds dual variables:
   idx   Lower bound        Value      Upper bound        Value
     1  -1.00000E-02    3.30098E-01    1.00000E-02    0.00000E+00
     2  -1.00000E-01    1.43844E-02    1.50000E-01    0.00000E+00
     3  -1.00000E-02    0.00000E+00    3.00000E-02    9.09967E-02
     4  -4.00000E-02    0.00000E+00    2.00000E-02    7.66124E-02
     5  -1.00000E-01    3.51391E-11    5.00000E-02    0.00000E+00
     6  -1.00000E-02    3.42902E-11         inf       0.00000E+00
     7  -1.00000E-02    8.61040E-12         inf       0.00000E+00

 Constraints dual variables:
   idx   Lower bound        Value      Upper bound        Value
     1  -1.30000E-01    0.00000E+00   -1.30000E-01    1.43111E+00
     2       -inf       0.00000E+00   -4.90000E-03    4.00339E-10
     3       -inf       0.00000E+00   -6.40000E-03    1.54305E-08
     4       -inf       0.00000E+00   -3.70000E-03    3.80136E-10
     5       -inf       0.00000E+00   -1.20000E-03    4.72629E-11
     6  -9.92000E-02    1.50098E+00         inf       0.00000E+00
     7  -3.00000E-03    1.51661E+00    2.00000E-03    0.00000E+00

++++++++++ Use the Self-Dual algorithm ++++++++++

 ----------------------------------------------
  E04MT, Interior point method for LP problems
 ----------------------------------------------

 Original Problem Statistics

   Number of variables          7
   Number of constraints        7
   Free variables               0
   Number of nonzeros          41


 Presolved Problem Statistics

   Number of variables         13
   Number of constraints        7
   Free variables               0
   Number of nonzeros          47


 ------------------------------------------------------------------------------
  it|    pobj    |    dobj    |  p.inf  |  d.inf  |  d.gap  |   tau  | mcc | I
 ------------------------------------------------------------------------------
   0 -6.39941E-01  4.94000E-02  1.07E+01  2.69E+00  5.54E+00  1.0E+00
   1 -8.56025E-02 -1.26938E-02  2.07E-01  2.07E-01  2.07E-01  1.7E+00   0
   2  4.09196E-03  1.24373E-02  4.00E-02  4.00E-02  4.00E-02  2.8E+00   0
   3  1.92404E-02  2.03658E-02  6.64E-03  6.64E-03  6.64E-03  3.2E+00   1
   4  1.99631E-02  2.07574E-02  3.23E-03  3.23E-03  3.23E-03  2.3E+00   1
   5  2.03834E-02  2.11141E-02  1.68E-03  1.68E-03  1.68E-03  1.4E+00   0
   6  2.22419E-02  2.25057E-02  5.73E-04  5.73E-04  5.73E-04  1.4E+00   1
   7  2.35051E-02  2.35294E-02  6.58E-05  6.58E-05  6.58E-05  1.4E+00   6
   8  2.35936E-02  2.35941E-02  1.19E-06  1.19E-06  1.19E-06  1.4E+00   0
Iteration 9
     monit() reports good approximate solution (tol = 1.20E-08):
   9  2.35965E-02  2.35965E-02  5.37E-10  5.37E-10  5.37E-10  1.4E+00   0
Iteration 10
     monit() reports good approximate solution (tol = 1.20E-08):
  10  2.35965E-02  2.35965E-02  2.68E-13  2.68E-13  2.68E-13  1.4E+00   0
 ------------------------------------------------------------------------------
 Status: converged, an optimal solution found
 ------------------------------------------------------------------------------
 Final primal objective value         2.359648E-02
 Final dual objective value           2.359648E-02
 Absolute primal infeasibility        2.853383E-12
 Relative primal infeasibility        2.677658E-13
 Absolute dual infeasibility          1.485749E-12
 Relative dual infeasibility          2.679654E-13
 Absolute complementarity gap         7.228861E-13
 Relative complementarity gap         2.683908E-13
 Iterations                                     10

 Primal variables:
   idx   Lower bound        Value      Upper bound
     1  -1.00000E-02   -1.00000E-02    1.00000E-02
     2  -1.00000E-01   -1.00000E-01    1.50000E-01
     3  -1.00000E-02    3.00000E-02    3.00000E-02
     4  -4.00000E-02    2.00000E-02    2.00000E-02
     5  -1.00000E-01   -6.74853E-02    5.00000E-02
     6  -1.00000E-02   -2.28013E-03         inf
     7  -1.00000E-02   -2.34528E-04         inf

 Box bounds dual variables:
   idx   Lower bound        Value      Upper bound        Value
     1  -1.00000E-02    3.30098E-01    1.00000E-02    0.00000E+00
     2  -1.00000E-01    1.43844E-02    1.50000E-01    0.00000E+00
     3  -1.00000E-02    0.00000E+00    3.00000E-02    9.09967E-02
     4  -4.00000E-02    0.00000E+00    2.00000E-02    7.66124E-02
     5  -1.00000E-01    3.66960E-12    5.00000E-02    0.00000E+00
     6  -1.00000E-02    2.47652E-11         inf       0.00000E+00
     7  -1.00000E-02    7.82645E-13         inf       0.00000E+00

 Constraints dual variables:
   idx   Lower bound        Value      Upper bound        Value
     1  -1.30000E-01    0.00000E+00   -1.30000E-01    1.43111E+00
     2       -inf       0.00000E+00   -4.90000E-03    1.07904E-10
     3       -inf       0.00000E+00   -6.40000E-03    1.14799E-09
     4       -inf       0.00000E+00   -3.70000E-03    4.09190E-12
     5       -inf       0.00000E+00   -1.20000E-03    1.52421E-12
     6  -9.92000E-02    1.50098E+00         inf       0.00000E+00
     7  -3.00000E-03    1.51661E+00    2.00000E-03    0.00000E+00
