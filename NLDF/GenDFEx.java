import com.nag.routines.E04.E04GN; // nagf_opt_handle_solve_nldf
import com.nag.routines.E04.E04GNU; // monit
import com.nag.routines.E04.E04GNX; // confun dummy
import com.nag.routines.E04.E04GNY; // congrd dummy
import com.nag.routines.E04.E04RA; // Handle init
import com.nag.routines.E04.E04RH; // box bounds
import com.nag.routines.E04.E04RJ; // linear constraints
import com.nag.routines.E04.E04RM; // add model and residual sparsity structure
import com.nag.routines.E04.E04RZ; // destroy handle
import com.nag.routines.E04.E04ZM; // optional parameters

import java.lang.Math;
import java.util.Arrays;


public class GenDFEx {

    public static void main (String[] args) {

        E04GN e04gn = new E04GN(); // the solver
        E04RA e04ra = new E04RA(); // the handle initializer
        E04RM e04rm = new E04RM(); // for setting model and residual sparsity structure
        E04ZM e04zm = new E04ZM(); // for setting optional parameters
        E04RZ e04rz = new E04RZ(); // handle destroyer


        MONIT monit = new MONIT(); // defined below using E04GNU
        CONFUN confun = new CONFUN(); // defined below using E04GNX (dummy)
        CONGRD congrd = new CONGRD(); // defined below using E04GNY (dummy)


        // Set up data and initial handle parameters
        double [] t = linspace(0.5, 2.5, 21);
        double [] ruser1 = toydata1(t); // For Example 1
        double [] ruser2 = toydata2(t); // For Example 2

        double [] x = new double [2]; // instantiate an array for as many variable you need 
        long handle = 0;
        int nvar = x.length;
        int ifail;
        int nres = t.length;

        // Init for sparsity structure
        int isparse = 0;
        int nnzrd = 0;
        int [] irowrd = new int [nnzrd];
        int [] icolrd = new int [nnzrd];



        // Get handle
        ifail = 0;
        e04ra.eval(handle, nvar, ifail);
        handle = e04ra.getHANDLE();

        // Define the residual functions and sparsity structure
        ifail = 0;
        e04rm.eval(handle, nres, isparse, nnzrd, irowrd, icolrd, ifail);

        // Set options
        ifail = 0;
        e04zm.eval(handle, "NLDF Loss Function Type = L2", ifail);
        e04zm.eval(handle, "Print Level = 1", ifail);
        e04zm.eval(handle, "Print Options = No", ifail);
        e04zm.eval(handle, "Print Solution = Yes", ifail);

        // Initialize all the remaining parameters
        LSQFUN lsqfun = new LSQFUN();
        LSQGRD lsqgrd = new LSQGRD();
        double [] rx = new double[nres];
        double [] rinfo = new double[100];
        double [] stats = new double [100];
        int [] iuser = new int[0];
        long cpuser = 0;

        // Solve
        System.out.println("\n----Solving Toy Dataset #1 with L2 Loss Function----");
        ifail = 0;
        x = init_x(); //give x the initial guess you want to start from
                      // NOTE: x will be changed during solve
        e04gn.eval(handle, lsqfun, lsqgrd, confun, congrd, monit, nvar, x, nres, rx, rinfo,
            stats, iuser, ruser1, cpuser, ifail);

        System.out.println("\n----Solving Toy Dataset #1 with L1 Loss Function----");
        ifail = 0;
        x = init_x();
        e04zm.eval(handle, "NLDF Loss Function Type = L1", ifail);
        e04gn.eval(handle, lsqfun, lsqgrd, confun, congrd, monit, nvar, x, nres, rx, rinfo,
            stats, iuser, ruser1, cpuser, ifail);



        // The trade-off of a loss function
        // The handle can keep getting used. We are only changing the data passed to the
        // solver using ruser2 (first 3 and last 3 data points different from middle)
        System.out.println("\n----Solving Toy Dataset #2 with L2 Loss Function----");
        ifail = 0;
        x = init_x();
        e04zm.eval(handle, "NLDF Loss Function Type = L2", ifail);
        e04gn.eval(handle, lsqfun, lsqgrd, confun, congrd, monit, nvar, x, nres, rx, rinfo,
            stats, iuser, ruser2, cpuser, ifail);

        System.out.println("\n----Solving Toy Dataset #2 with L1 Loss Function----");
        ifail = 0;
        x = init_x();
        e04zm.eval(handle, "NLDF Loss Function Type = L1", ifail);
        e04gn.eval(handle, lsqfun, lsqgrd, confun, congrd, monit, nvar, x, nres, rx, rinfo,
            stats, iuser, ruser2, cpuser, ifail);

        System.out.println("\n----Solving Toy Dataset #2 with ATAN Loss Function----");
        ifail = 0;
        x = init_x();
        e04zm.eval(handle, "NLDF Loss Function Type = ATAN", ifail);
        e04gn.eval(handle, lsqfun, lsqgrd, confun, congrd, monit, nvar, x, nres, rx, rinfo,
            stats, iuser, ruser2, cpuser, ifail);

        e04rz.eval(handle,ifail); // Destroy the handle
        
    }


    public static class LSQFUN extends E04GN.Abstract_E04GN_LSQFUN {
        public void eval() {
            for(int i = 0; i < NRES; i++){
                this.RX[i] = RUSER[NRES + i] - X[0] * Math.sin(X[1] * RUSER[i]);
            }
        }
    }

    public static class LSQGRD extends E04GN.Abstract_E04GN_LSQGRD {
        public void eval() {
            for(int i = 0; i < NRES; i++){
                this.RDX[i * NVAR] = (-1 * Math.sin(X[1]*RUSER[i]));
                this.RDX[i* NVAR + 1] = (-1 * RUSER[i] * X[0] * Math.cos(X[1] * RUSER[i]));
            }
        }
    }

    // Dummy Functions required for NLDF solver
    public static class CONFUN extends E04GN.Abstract_E04GN_CONFUN {
        public void eval(){
            this.eval();
        }
    }

    public static class CONGRD extends E04GN.Abstract_E04GN_CONGRD {
        public void eval(){
            this.eval();
        }
    }

    public static class MONIT extends E04GN.Abstract_E04GN_MONIT {
        public void eval(){
            this.eval();
        }
    }
    
    // Utilities for setting up data for problem
    public static double[] linspace(double startPoint, double endPoint, int length) {
        double[] a = new double[length];
        double step = (endPoint - startPoint) / (length - 1);
        a[0] = startPoint;
        a[length - 1] = endPoint;
        for (int i = 1; i < length - 1; i++) {
            a[i] = startPoint + i * step;
        }
        return a;
    }

    public static double[] toydata1(double [] t) {
        double [] y = new double[t.length * 2];
        for(int i = 0; i < t.length * 2; i++){
            if(i < t.length){
                y[i] = t[i];
            }
            else{
                y[i] = Math.sin(t[i-t.length]);
                if(i -  t.length == 10){
                    y[i] = 5 * y[i];
                }
            }
        }
        return y;
    }
    
    public static double[] toydata2(double [] t) {
        double [] y = new double[t.length * 2];
        for(int i = 0; i < t.length * 2; i++){
            if(i < t.length){
                y[i] = t[i];
            }
            else{
                y[i] = Math.sin(t[i-t.length]);
                if((i -  t.length >= 3) && (i - t.length < 18)){
                    y[i] = 5 * y[i];
                }
            }
        }
        return y;
    }

    // For resetting the initial guess
    public static double[] init_x() {
        double [] x = new double [] {2.1,1.4};
        return x;
    }
}