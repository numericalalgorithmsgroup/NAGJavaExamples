 E04NFJ Example Program Results
                                                                                
 *** e04nf                                                                      
                                                                                
 Parameters                                                                     
 ----------                                                                     
                                                                                
 Problem type...........       QP2                                              
                                                                                
 Linear constraints.....         7       Feasibility tolerance..  1.05E-08      
 Variables..............         7       Optimality tolerance...  1.05E-08      
 Hessian rows...........         7       Rank tolerance.........  1.11E-14      
                                                                                
 Infinite bound size....  1.00E+20       COLD start.............                
 Infinite step size.....  1.00E+20       EPS (machine precision)  1.11E-16      
                                                                                
 Check frequency........        50       Expand frequency.......         5      
 Minimum sum of infeas..        NO       Crash tolerance........  1.00E-02      
                                                                                
 Max degrees of freedom.         7       Print level............        10      
 Feasibility phase itns.        70       Monitoring file........        -1      
 Optimality  phase itns.        70                                              
                                                                                
 Workspace provided is     IWORK(      17),  WORK(     189).                    
 To solve problem we need  IWORK(      17),  WORK(     189).                    
                                                                                                                        
                                                                                                                        
  Itn     Step Ninf Sinf/Objective  Norm Gz                                                                             
    0  0.0E+00    3   1.038000E-01  0.0E+00                                                                             
    1  4.1E-02    1   3.000000E-02  0.0E+00                                                                             
    2  4.2E-02    0   0.000000E+00  0.0E+00                                                                             
 Itn     2 -- Feasible point found.                                             
    2  0.0E+00    0   4.580000E-02  0.0E+00                                                                             
    3  1.3E-01    0   4.161596E-02  0.0E+00                                                                             
    4  1.0E+00    0   3.936227E-02  4.2E-17                                                                             
    5  4.1E-01    0   3.758935E-02  1.2E-02                                                                             
    6  1.0E+00    0   3.755377E-02  1.0E-17                                                                             
    7  1.0E+00    0   3.703165E-02  3.8E-17                                                                             
                                                                                
                                                                                
 Varbl State     Value       Lower Bound   Upper Bound    Lagr Mult   Slack     
                                                                                
 V   1    LL  -1.000000E-02 -1.000000E-02  1.000000E-02  0.4700         .       
 V   2    FR  -6.986465E-02 -0.100000      0.150000           .      3.0135E-02 
 V   3    FR   1.825915E-02 -1.000000E-02  3.000000E-02       .      1.1741E-02 
 V   4    FR  -2.426081E-02 -4.000000E-02  2.000000E-02       .      1.5739E-02 
 V   5    FR  -6.200564E-02 -0.100000      5.000000E-02       .      3.7994E-02 
 V   6    FR   1.380544E-02 -1.000000E-02      None           .      2.3805E-02 
 V   7    FR   4.066496E-03 -1.000000E-02      None           .      1.4066E-02 
                                                                                
                                                                                
 L Con State     Value       Lower Bound   Upper Bound    Lagr Mult   Slack     
                                                                                
 L   1    EQ  -0.130000     -0.130000     -0.130000      -1.908     -5.5511E-17 
 L   2    FR  -5.879898E-03      None     -4.900000E-03       .      9.7990E-04 
 L   3    UL  -6.400000E-03      None     -6.400000E-03 -0.3144      8.6736E-19 
 L   4    FR  -4.537323E-03      None     -3.700000E-03       .      8.3732E-04 
 L   5    FR  -2.915996E-03      None     -1.200000E-03       .      1.7160E-03 
 L   6    LL  -9.920000E-02 -9.920000E-02      None       1.955         .       
 L   7    LL  -3.000000E-03 -3.000000E-03  2.000000E-03   1.972      4.3368E-18 
                                                                                
 Exit e04nf  - Optimal QP solution.                                             
                                                                                
 Final QP objective value =   0.3703165E-01                                     
                                                                                
 Exit from QP problem after     7 iterations.                                   
