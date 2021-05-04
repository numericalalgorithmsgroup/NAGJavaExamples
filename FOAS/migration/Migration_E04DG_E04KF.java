import com.nag.routines.E04.E04DG;
import com.nag.routines.E04.E04WB;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RG;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04KF;
import com.nag.routines.E04.E04KFU;

import java.lang.Math;
import java.util.Arrays;

public class Migration_E04DG_E04KF {
    public static void main(String[] args) {

        // The initial guess
        double[] x = new double[] { -1.5, 1.9 };

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

        System.out.println("Solution: " + x[0] + " " + x[1]);
        System.out.println();

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
    }

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
}
