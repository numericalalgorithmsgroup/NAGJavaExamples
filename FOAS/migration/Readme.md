> ## Important Information
> You can view this page as a [webpage](https://numericalalgorithmsgroup.github.io/NAGJavaExamples/FOAS/migration) or access this as a regular github [repository](https://github.com/numericalalgorithmsgroup/NAGJavaExamples/tree/main/FOAS/migration).
>
> The source of this example can be found [here](https://numericalalgorithmsgroup.github.io/NAGJavaExamples/FOAS/migration/Migration_E04DG_E04KF.java) and the output [here](https://github.com/numericalalgorithmsgroup/NAGJavaExamples/tree/main/FOAS/migration/output.txt).
>
> See the top directory of this repository for instructions to set up the [NAG Library for Java](https://github.com/numericalalgorithmsgroup/NAGJavaExamples).

# Migrating from `E04DG` to `E04KF`

This page illustrates the steps required to upgrade from the solver `uncon_conjgrd_comp` (`E04DG`) to `handle_solve_bounds_foas` (`E04KF`) introduced at Mark 27 of the NAG Library.

From the usage perspective, the main difference between the solvers is the user call-backs,
`E04DG` has a single user call-back that can return *objective 
function* and *gradient* evaluations, while `E04KF` has two separate user call-backs, 
one for the *objective funtion* and one for the *objective gradient*.

On this page the 2d Rosenbrock problem is solved with both solvers and illustrates the changes necessary for the migration to `E04KF`. The solution to the problem is (1, 1).

```java
// Define E04DG user call-back
public static class OBJFUN_E04DG extends E04DG.Abstract_E04DG_OBJFUN {
    public void eval() {
        this.OBJF = Math.pow(1.0 - this.X[0], 2) + 100.0 * Math.pow(this.X[1] - Math.pow(this.X[0], 2), 2);

        if (this.MODE == 2) {
            this.OBJGRD[0] = 2.0 * this.X[0] - 400.0 * this.X[0] * (this.X[1] - Math.pow(this.X[0], 2)) - 2.0;
            this.OBJGRD[1] = 200.0 * (this.X[1] - Math.pow(this.X[0], 2));
        }
    }
}
```

</br>

```java
// Define user call-backs for E04KF
/**
 * Return the objective function value
 */
public static class OBJFUN_E04KF extends E04KF.Abstract_E04KF_OBJFUN {
    public void eval() {
        this.FX = Math.pow(1.0 - this.X[0], 2) + 100.0 * Math.pow(this.X[1] - Math.pow(this.X[0], 2), 2);
    }
}

/**
 * The objective's gradient. Note that fdx has to be updated IN-PLACE
 */
public static class OBJGRD_E04KF extends E04KF.Abstract_E04KF_OBJGRD {
    public void eval() {
        this.FDX[0] = 2.0 * this.X[0] - 400.0 * this.X[0] * (this.X[1] - Math.pow(this.X[0], 2)) - 2.0;
        this.FDX[1] = 200.0 * (this.X[1] - Math.pow(this.X[0], 2));
    }

/**
 * Dummy monit
 */
public static class MONIT_E04KF extends E04KF.Abstract_E04KF_MONIT {
    public void eval() {
        E04KFU e04kfu = new E04KFU();
        e04kfu.eval(this.NVAR, this.X, this.INFORM, this.RINFO, this.STATS, this.IUSER, this.RUSER, this.CPUSER);
        this.INFORM = e04kfu.getINFORM();
    }
}
```

</br>

```java
// The initial guess
double[] x = new double[] { -1.5, 1.9 };
```

## Solve the problem with `E04DG`

```java
E04WB e04wb = new E04WB();
String[] cwsav = new String[1];
Arrays.fill(cwsav, " ");
boolean[] lwsav = new boolean[120];
int[] iwsav = new int[610];
double[] rwsav = new double[475];
int ifail = 0;
e04wb.eval("e04dga", cwsav, 1, lwsav, 120, iwsav, 610, rwsav, 475, ifail);

// Solve the problem with E04DG

E04DG e04dg = new E04DG();
OBJFUN_E04DG objfun_e04dg = new OBJFUN_E04DG();
int n = x.length;
int iter = 0;
double objf = 0;
double[] objgrd = new double[n];
int[] iwork = new int[n + 1];
double[] work = new double[13 * n];
int[] iuser = new int[0];
double[] ruser = new double[0];
ifail = 0;
e04dg.eval(n, objfun_e04dg, iter, objf, objgrd, x, iwork, work, iuser, ruser, lwsav, iwsav, rwsav, ifail);
```

</br>

```
Solution: 1.0000067567705557 1.0000135365609837
```

## Now solve with the new solver `E04KF`

```java
// Now solve with the new solver E04KF

// The initial guess
x = new double[] { -1.5, 1.9 };

E04RA e04ra = new E04RA();
E04RG e04rg = new E04RG();
E04RH e04rh = new E04RH();
E04ZM e04zm = new E04ZM();
E04KF e04kf = new E04KF();

// Create an empty handle for the problem
int nvar = x.length;
long handle = 0;
ifail = 0;
e04ra.eval(handle, nvar, ifail);
handle = e04ra.getHANDLE();

// Define the nonlinear objective in the handle
// Setup a gradient vector of length nvar
int[] idxfd = new int[nvar];
for (int i = 0; i < nvar; i++) {
    idxfd[i] = i + 1;
}
ifail = 0;
e04rg.eval(handle, nvar, idxfd, ifail);

// Set some algorithmic options
ifail = 0;
e04zm.eval(handle, "Print Options = Yes", ifail); // print Options?
e04zm.eval(handle, "Print Solution = Yes", ifail); // print on the screen the solution point X
e04zm.eval(handle, "Print Level = 1", ifail); // print details of each iteration (screen)

// Solve the problem and print the solution
OBJFUN_E04KF obfun_e04kf = new OBJFUN_E04KF();
OBJGRD_E04KF objgrd_e04kf = new OBJGRD_E04KF();
MONIT_E04KF monit_e04kf = new MONIT_E04KF();
double[] rinfo = new double[100];
double[] stats = new double[100];
iuser = new int[0];
ruser = new double[0];
long cpuser = 0;
ifail = 0;
e04kf.eval(handle, obfun_e04kf, objgrd_e04kf, monit_e04kf, nvar, x, rinfo, stats, iuser, ruser, cpuser, ifail);

// Destroy the handle and free allocated memory
E04RZ e04rz = new E04RZ();
e04rz.eval(handle, ifail);
```

</br>

```
E04KF, First order method for bound-constrained problems                       
 Begin of Options                                                               
     Print File                    =                   6     * d                
     Print Level                   =                   1     * U                
     Print Options                 =                 Yes     * U                
     Print Solution                =                 All     * U                
     Monitoring File               =                  -1     * d                
     Monitoring Level              =                   4     * d                
     Foas Monitor Frequency        =                   0     * d                
     Foas Print Frequency          =                   1     * d                
 
     Infinite Bound Size           =         1.00000E+20     * d                
     Task                          =            Minimize     * d                
     Stats Time                    =                  No     * d                
     Time Limit                    =         1.00000E+06     * d                
     Verify Derivatives            =                  No     * d                
 
     Foas Estimate Derivatives     =                  No     * d                
     Foas Finite Diff Interval     =         1.05367E-08     * d                
     Foas Iteration Limit          =            10000000     * d                
     Foas Memory                   =                  11     * d                
     Foas Progress Tolerance       =         1.08158E-12     * d                
     Foas Rel Stop Tolerance       =         1.08158E-12     * d                
     Foas Restart Factor           =         6.00000E+00     * d                
     Foas Slow Tolerance           =         1.01316E-02     * d                
     Foas Stop Tolerance           =         1.00000E-06     * d                
     Foas Tolerance Norm           =            Infinity     * d                
 End of Options                                                                 
                                                                                
                                                                                                    
 Status: converged, an optimal solution was found                                                   
 Value of the objective             2.12807E-15                                                     
 Norm of gradient                   3.67342E-08                                                     
                                                                                
 Primal variables:                                                              
   idx   Lower bound       Value       Upper bound                              
     1       -inf        1.00000E+00        inf                                 
     2       -inf        1.00000E+00        inf                                 
```
