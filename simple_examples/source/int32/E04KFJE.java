import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04RG;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04KF;
import com.nag.routines.E04.E04KFU;
import com.nag.routines.E04.E04RX;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.X04.X04AC;

import java.lang.Math;

/**
 * E04KF example program text.
 */
public class E04KFJE {

    private static final int NMONIT = 9;
    private static final int NVAR = 2;

    public static void main(String[] args) {

        long cpuser = 0;
        int nu = 2 * NVAR;

        /* Header */
        System.out.println(" E04KFJ Example Program Results\n");

        // Define filename for monitoring output
        X04AC x04ac = new X04AC();
        int ifail = 0;
        x04ac.eval(NMONIT, "e04kffe.mon", 1, ifail);

        // Initialize handle
        E04RA e04ra = new E04RA();
        long handle = 0;
        ifail = 0;
        e04ra.eval(handle, NVAR, ifail);
        handle = e04ra.getHANDLE();

        // Define initial guess point
        double[] x = new double[] { -1.5, 1.9 };

        // Define Simple box bounds on X
        E04RH e04rh = new E04RH();
        double[] blx = new double[] { -1.0, -2.0 };
        double[] bux = new double[] { 0.8, 2.0 };
        ifail = 0;
        e04rh.eval(handle, NVAR, blx, bux, ifail);

        // Add nonlinear objective information
        E04RG e04rg = new E04RG();
        int iidx[] = new int[NVAR];
        for (int i = 0; i < NVAR; i++)
            iidx[i] = i + 1;
        ifail = 0;
        e04rg.eval(handle, NVAR, iidx, ifail);

        // Add options
        E04ZM e04zm = new E04ZM();
        ifail = 0;
        e04zm.eval(handle, "FOAS Print Frequency = 5", ifail);

        ifail = 0;
        e04zm.eval(handle, "Print Solution = yes", ifail);

        ifail = 0;
        e04zm.eval(handle, "Print Level = 1", ifail);

        ifail = 0;
        e04zm.eval(handle, "Monitoring File = " + NMONIT, ifail);

        ifail = 0;
        e04zm.eval(handle, "Monitoring Level = 3", ifail);

        // Solve the problem
        E04KF e04kf = new E04KF();
        OBJFUN objfun = new OBJFUN();
        OBJGRD objgrd = new OBJGRD();
        MONIT monit = new MONIT();
        double[] rinfo = new double[100];
        double[] stats = new double[100];
        int[] iuser = new int[0];
        double[] ruser = new double[0];
        ifail = -1;
        e04kf.eval(handle, objfun, objgrd, monit, NVAR, x, rinfo, stats, iuser, ruser, cpuser, ifail);

        ifail = e04kf.getIFAIL();

        // Print objective value at solution
        if ((ifail == 0) || (ifail == 50)) {
            System.out.printf("\n\n Solution found:\n  Objective function value at solution: %9.1E\n", rinfo[0]);
            // Retrieve Lagrange multipliers (FDX)
            E04RX e04rx = new E04RX();
            double[] u = new double[nu];
            e04rx.eval(handle, "U", 1, nu, u, ifail);
            if (ifail == 0) {
                System.out.printf("  Gradient at solution:                 %9.1E %9.1E\n\n", u[0] - u[1], u[2] - u[3]);
                System.out.printf("  Estimated Lagrange multipliers: blx   %9.1E %9.1E\n", u[0], u[2]);
                System.out.printf("  Estimated Lagrange multipliers: bux   %9.1E %9.1E\n", u[1], u[3]);
            }
        }

        System.out.println();

        // Clean up
        E04RZ e04rz = new E04RZ();
        ifail = 0;
        e04rz.eval(handle, ifail);
    }

    public static class OBJFUN extends E04KF.Abstract_E04KF_OBJFUN {
        public void eval() {
            // Rosenbrock function
            this.FX = Math.pow(1.0 - this.X[0], 2) + 100.0 * Math.pow(this.X[1] - Math.pow(this.X[0], 2), 2);
        }
    }

    public static class OBJGRD extends E04KF.Abstract_E04KF_OBJGRD {
        public void eval() {
            this.FDX[0] = 2.0 * this.X[0] - 400.0 * this.X[0] * (this.X[1] - Math.pow(this.X[0], 2)) - 2.0;
            this.FDX[1] = 200.0 * (this.X[1] - Math.pow(this.X[0], 2));
        }
    }

    public static class MONIT extends E04KF.Abstract_E04KF_MONIT {
        public void eval() {
            E04KFU e04kfu = new E04KFU();
            e04kfu.eval(this.NVAR, this.X, this.INFORM, this.RINFO, this.STATS, this.IUSER, this.RUSER, this.CPUSER);
            this.INFORM = e04kfu.getINFORM();
        }
    }
}
