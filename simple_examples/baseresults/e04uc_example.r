 E04UC_Example Example Program Results
                                                                                
 *** e04uc                                                                      
                                                                                
 Parameters                                                                     
 ----------                                                                     
                                                                                
 Linear constraints.....         1       Variables..............         4      
 Nonlinear constraints..         2                                              
                                                                                
 Infinite bound size....  1.00E+20       COLD start.............                
 Infinite step size.....  1.00E+20       EPS (machine precision)  1.11E-16      
 Step limit.............  2.00E+00       Hessian................        NO      
                                                                                
 Linear feasibility.....  1.05E-08       Crash tolerance........  1.00E-02      
 Nonlinear feasibility..  1.05E-08       Optimality tolerance...  3.26E-12      
 Line search tolerance..  9.00E-01       Function precision.....  4.37E-15      
                                                                                
 Derivative level.......         3       Monitoring file........        -1      
 Verify level...........         0                                              
                                                                                
 Major iterations limit.        50       Major print level......        10      
 Minor iterations limit.        50       Minor print level......         0      
                                                                                                                        
 Start point                                                                                                            
    1.000000E+00   5.000000E+00   5.000000E+00   1.000000E+00                                                           
                                                                                
 Workspace provided is     IWORK(      17),  WORK(     185).                    
 To solve problem we need  IWORK(      17),  WORK(     185).                    
                                                                                                                        
                                                                                                                        
 Verification of the constraint gradients.                                                                              
 -----------------------------------------                                                                              
                                                                                                                        
 The constraint Jacobian seems to be ok.                                                                                
                                                                                                                        
 The largest relative error was    2.29E-07  in constraint    2                                                         
                                                                                                                        
                                                                                                                        
                                                                                                                        
 Verification of the objective gradients.                                                                               
 ----------------------------------------                                                                               
                                                                                                                        
 The objective gradients seem to be ok.                                                                                 
                                                                                                                        
 Directional derivative of the objective    8.15250000E-01                                                              
 Difference approximation                   8.15249734E-01                                                              
                                                                                                                                    
                                                                                                                                    
  Maj  Mnr    Step Merit Function Norm Gz  Violtn Cond Hz                                                                           
    0    4 0.0E+00   1.738281E+01 7.1E-01 1.2E+01 1.0E+00                                                                           
    1    1 1.0E+00   1.703169E+01 4.6E-02 1.9E+00 1.0E+00                                                                           
    2    1 1.0E+00   1.701442E+01 2.1E-02 8.8E-02 1.0E+00                                                                           
    3    1 1.0E+00   1.701402E+01 3.1E-04 5.4E-04 1.0E+00                                                                           
    4    1 1.0E+00   1.701402E+01 7.0E-06 9.9E-08 1.0E+00                                                                           
    5    1 1.0E+00   1.701402E+01 1.1E-08 4.6E-11 1.0E+00                                                                           
                                                                                
 Exit from NP problem after     5 major iterations,                             
                                9 minor iterations.                             
                                                                                
                                                                                
 Varbl State     Value       Lower Bound   Upper Bound    Lagr Mult   Slack     
                                                                                
 V   1    LL    1.00000       1.00000       5.00000       1.088         .       
 V   2    FR    4.74300       1.00000       5.00000           .      0.2570     
 V   3    FR    3.82115       1.00000       5.00000           .       1.179     
 V   4    FR    1.37941       1.00000       5.00000           .      0.3794     
                                                                                
                                                                                
 L Con State     Value       Lower Bound   Upper Bound    Lagr Mult   Slack     
                                                                                
 L   1    FR    10.9436          None       20.0000           .       9.056     
                                                                                
                                                                                
 N Con State     Value       Lower Bound   Upper Bound    Lagr Mult   Slack     
                                                                                
 N   1    UL    40.0000          None       40.0000     -0.1615     -3.5264E-11 
 N   2    LL    25.0000       25.0000          None      0.5523     -2.8791E-11 
                                                                                
 Exit e04uc  - Optimal solution found.                                          
                                                                                
 Final objective value =    17.01402                                            

 Varbl  Istate   Value         Lagr Mult

 V   0   1          1.000000        1.0879
 V   1   0          4.743000        0.0000
 V   2   0          3.821150        0.0000
 V   3   0          1.379408        0.0000

 L Con  Istate   Value         Lagr Mult

 L   0   0         10.943558        0.0000

 Final objective value =      17.0140173
