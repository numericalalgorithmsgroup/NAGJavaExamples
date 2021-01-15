import java.util.Arrays;

import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.Routine;
import com.nag.routines.D02.D02MC;
import com.nag.routines.D02.D02MW;
import com.nag.routines.D02.D02NE;
import com.nag.routines.D02.D02NEZ;
import com.nag.routines.D02.D02NP;


/**
 * @author joed
 */
public class D02NEJE {

    public static final double ALPHA = 0.04;
    public static final double BETA = 1.0E4;
    public static final double GAMMA = 3.0E7;
    public static final int ML = 1;
    public static final int MU = 2;
    public static final int NEQ1 = 3;
    public static final int NEQ2 = 1;

    public static JAC1 jac1 = new JAC1();
    public static JAC2 jac2 = new JAC2();
    public static RES1 res1 = new RES1();
    public static RES2 res2 = new RES2();

    public static void main(String[] args) throws NAGBadIntegerException {

        // Initialise wrappers
        Routine.init();

        System.out.println(" D02NEJ Example Program Results");

        ex1();
        ex2();

    }

    private static void ex1() {
        D02MC d02mc = new D02MC();
        D02MW d02mw = new D02MW();
        D02NE d02ne = new D02NE();
        D02NP d02np = new D02NP();
        double h0, hmax, t, tout;
		long ifail, itask, maxord, ijac, itol, licom, neq, lcom;
        String jceval; // length 8
        double[] atol, com, rtol, y, ydot, ruser;
        long[] icom, iuser;

        ruser = new double[1];
        iuser = new long[3];

        System.out.println();
        System.out.println(" D02NEF Example 1");
        System.out.println();

        maxord = 5;

        neq = NEQ1;
        lcom = 40 + (maxord + 4) * neq + (2 * ML + MU + 1) * neq
             + 2 * (neq / (ML + MU + 1) + 1);
        licom = 50 + neq;

        atol = new double[(int)neq];
        com = new double[(int)lcom];
        rtol = new double[(int)neq];
        y = new double[(int)neq];
        ydot = new double[(int)neq];
        icom = new long[(int)licom];

        ijac = 1;
        itol = 1;
        Arrays.fill(rtol, 1.0E-3);
        Arrays.fill(atol, 1.0E-6);
        Arrays.fill(ydot, 0.0);

        // String length = 8
        jceval = (ijac == 1) ? "Analytic" : "Numeric ";

        // Set initial values
        y[0] = 1.0;
        y[1] = 0.0;
        y[2] = 0.0;

        // Initialize the problem, specifying that the Jacobian is to be
        // evaluated analytically using the provided routine jac.
        hmax = 0.0;
        h0 = 0.0;
        t = 0.0;
        tout = 0.02;
        ifail = 0;
        d02mw.eval(
            neq, maxord, jceval, hmax, h0, itol, icom, licom, com, lcom, ifail
        );
        ifail = d02mw.getIFAIL();

        // Specify that the Jacobian is banded.
        ifail = 0;
        d02np.eval(neq, ML, MU, icom, licom, ifail);
        ifail = d02np.getIFAIL();

        // Use the iuser array to pass the band dimensions through to jac.
        // An alternative would be to hard code values for ml and mu in jac.
        iuser[0] = ML;
        iuser[1] = MU;
        iuser[2] = ijac;

        System.out.printf("     t      ");
        for (int i = 1; i <= neq; i++) {
            System.out.printf("      Y(%1d)  ", i);
        }
        System.out.println();
        System.out.printf(" %8.4f   ", t);
        for (int i = 0; i < neq; i++) {
            System.out.printf("%12.6f", y[i]);
        }
        System.out.println();

        itask = 0;

        // Obtain the solution at 5 equally spaced values of T.
        for (int j = 0; j < 5; j++) {
            ifail = -1;
            d02ne.eval(
                neq, t, tout, y, ydot, rtol, atol, itask, res1, jac1, icom,
                com, lcom, iuser, ruser, ifail
            );
            itask = d02ne.getITASK();
            ifail = d02ne.getIFAIL();
            t = d02ne.getT();

            System.out.printf(" %8.4f   ", t);
            for (int i = 0; i < neq; i++) {
                System.out.printf("%12.6f", y[i]);
            }
            System.out.println();

            if (ifail != 0) {
                System.out.printf(
                    "  ** D02NEF returned with IFAIL = %5d\n", ifail
                );
                break;
            }

            tout += 0.02;
            d02mc.eval(icom);

        }

        System.out.println();
        System.out.printf(
            " The integrator completed task, ITASK = %4d\n", itask
        );

    }

    private static void ex2() {
        D02MC d02mc = new D02MC();
        D02MW d02mw = new D02MW();
        D02NE d02ne = new D02NE();
        double h0, hmax, t, tout;
        long ifail, ijac, itask, itol, lcom, licom, maxord, neq;
        String jceval; // length 8
        double[] atol, com, rtol, y, ydot, ruser;
        long[] icom, iuser;

        ruser = new double[1];
        iuser = new long[1];

        System.out.println();
        System.out.println(" D02NEF Example 2");
        System.out.println();

        maxord = 5;
        neq = NEQ2;
        lcom = 40 + (maxord + 4) * neq + neq * neq;
        licom = 50 + neq;

		atol = new double[(int)neq];
        com = new double[(int)lcom];
        rtol = new double[(int)neq];
        y = new double[(int)neq];
        ydot = new double[(int)neq];
        icom = new long[(int)licom];

        ijac = 1;
        itol = 1;
        rtol[0] = 0.0;
        atol[0] = 1.0E-8;
        ydot[0] = 0.0;

        // String length = 8
        jceval = (ijac == 1) ? "Analytic" : "Numeric ";

        // Initialize the problem, specifying that the Jacobian is to be
        // evaluated analytically using the provided routine jac.
        y[0] = 2.0;
        hmax = 0.0;
        h0 = 0.0;
        t = 0.0;
        tout = 0.2;

        ifail = 0;
        d02mw.eval(
            neq, maxord, jceval, hmax, h0, itol, icom, licom, com, lcom, ifail
        );
        ifail = d02mw.getIFAIL();

        // Use the iuser array to pass whether numerical or analytic Jacobian
        // is to be used.
        iuser[0] = ijac;

        System.out.printf("     t      ");
        for (int i = 1; i <= neq; i++) {
            System.out.printf("      y(%1d)  ", i);
        }
        System.out.println();
        System.out.printf(" %8.4f   ", t);
        for (int i = 0; i < neq; i++) {
            System.out.printf("%12.6f", y[i]);
        }
        System.out.println();

        itask = 0;

        for (int j = 0; j < 5; j++) {
            ifail = -1;
            d02ne.eval(
                neq, t, tout, y, ydot, rtol, atol, itask, res2, jac2, icom,
                com, lcom, iuser, ruser, ifail
            );
            itask = d02ne.getITASK();
            ifail = d02ne.getIFAIL();
            t = d02ne.getT();

            System.out.printf(" %8.4f   ", t);
            for (int i = 0; i < neq; i++) {
                System.out.printf("%12.6f", y[i]);
            }
            System.out.println();

            if (ifail != 0) {
                System.out.printf(
                    "  ** D02NEF returned with IFAIL = %5d\n", ifail
                );
                break;
            }

            tout += 0.2;
            d02mc.eval(icom);

        }

        System.out.println();
        System.out.printf(
            " The integrator completed task, ITASK = %4d\n", itask
        );

    }

    /**
     * Converts a 2D, 1-based Fortran index to its corresponding 1D, 0-based
     * Java index.
     *
     * Fortran array definition:
     *     a(dimX, *)
     *
     * Conversion:
     *     a(x, y) --> A[result]
     */
    private static long getIdx(long x, long y, long dimX) {
        return ((y-1) * dimX) + (x-1);
    }

    public static class RES1 extends D02NE.Abstract_D02NE_RES {

        public void eval() {

            this.R[0] = -(ALPHA * this.Y[0]) + (BETA * this.Y[1] * this.Y[2])
                      - this.YDOT[0];
            this.R[1] = (ALPHA * this.Y[0]) - (BETA * this.Y[1] * this.Y[2])
                      - (GAMMA * this.Y[1] * this.Y[1]) - this.YDOT[1];
            this.R[2] = (GAMMA * this.Y[1] * this.Y[1]) - this.YDOT[2];

        }

    }

    public static class JAC1 extends D02NE.Abstract_D02NE_JAC {

        private double[] myjac1(
            long neq, long ml, long mu, double t, double[] y, double[] ydot,
            double[] pd, double cj
        ) {

            long md, ms, pd_dim1;

            pd_dim1 = (2 * ml) + mu + 1;

            // Main diagonal pdfull(i,i), i=1, neq
            md = mu + ml + 1;
            pd[(int)getIdx(md, 1, pd_dim1)] = -ALPHA - cj;
            pd[(int)getIdx(md, 2, pd_dim1)] = (-BETA * y[2]) - (2.0 * GAMMA * y[1])
                                       - cj;
            pd[(int)getIdx(md, 3, pd_dim1)] = -cj;

            // 1 subdiagonal pdfull(i-1,i), i=2, neq
            ms = md + 1;
            pd[(int)getIdx(ms, 1, pd_dim1)] = ALPHA;
            pd[(int)getIdx(ms, 2, pd_dim1)] = 2.0 * GAMMA * y[1];

            // First superdiagonal pdfull(i-1,i), i=2, neq
            ms = md - 1;
            pd[(int)getIdx(ms, 2, pd_dim1)] = BETA * y[2];
            pd[(int)getIdx(ms, 3, pd_dim1)] = -BETA * y[1];

            // Second superdiagonal pdfull(i-2,i), i=3, neq
            ms = md - 2;
            pd[(int)getIdx(ms, 3, pd_dim1)] = BETA * y[1];

            return pd;

        }

        public void eval() {
            D02NEZ d02nez = new D02NEZ();
            long ijac, ml, mu;

            ml = this.IUSER[0];
            mu = this.IUSER[1];
            ijac = this.IUSER[2];

            if (ijac == 1) {
                myjac1(
                    this.NEQ, ml, mu, this.T, this.Y, this.YDOT, this.PD,
                    this.CJ
                );
            }
            else {
                d02nez.eval(
                    this.NEQ, this.T, this.Y, this.YDOT, this.PD, this.CJ,
                    this.IUSER, this.RUSER
                );
            }

        }

    }

    public static class RES2 extends D02NE.Abstract_D02NE_RES {

        public void eval() {
            this.R[0] = 4.0 - Math.pow(this.Y[0], 2.0)
                      + (this.T * 0.1E0 * Math.exp(this.Y[0]));
        }

    }

    public static class JAC2 extends D02NE.Abstract_D02NE_JAC {

        private void myjac2(
            long neq, double t, double[] y, double[] ydot, double[] pd,
            double cj
        ) {

            pd[0] = -(2.0 * y[0]) + (0.1E0 * t * y[0] * Math.exp(y[0]));

        }

        public void eval() {
            D02NEZ d02nez = new D02NEZ();
            long ijac;

            ijac = this.IUSER[0];

            if (ijac == 1) {
                myjac2(this.NEQ, this.T, this.Y, this.YDOT, this.PD, this.CJ);
            }
            else {
                d02nez.eval(
                    this.NEQ, this.T, this.Y, this.YDOT, this.PD, this.CJ,
                    this.IUSER, this.RUSER
                );
            }

        }

    }

}
