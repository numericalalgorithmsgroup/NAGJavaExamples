import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.E04.E04UC;
import com.nag.routines.E04.E04WB;
import com.nag.routines.F06.DGEMV;
import com.nag.routines.Routine;
import java.util.Arrays;

/**
 * @author joed
 */
public class E04UCJE {

    public static final double ONE = 1.0, ZERO = 0.0;
    public static final long INC1 = 1, LCWSAV = 1, LIWSAV = 610, LLWSAV = 120, LRWSAV = 475;
    public static CONFUN confun = new CONFUN();
    public static OBJFUN objfun = new OBJFUN();

    public static void main(String[] args) throws NAGBadIntegerException {
        /* Local Scalars */
        double objf;
        long i, ifail, iter, j, lda, ldcj, ldr, liwork, lwork, n, nclin, ncnln, sda, sdcjac;
        /* Local Arrays */
        double[] a, bl, bu, c, cjac, clamda, objgrd, r, work, x;
        double[] ruser = new double[1], rwsav = new double[(int)LRWSAV];
        long[] istate, iwork;
        long[] iuser = new long[1], iwsav = new long[(int)LIWSAV];
        boolean[] lwsav = new boolean[(int)LLWSAV];
        String[] cwsav = new String[(int)LCWSAV];
        Arrays.fill(cwsav, "                                                                                ");

        System.out.println(" E04UCJ Example Program Results");
        Routine.init();

        /* Set scalars */
        n = 4;
        nclin = 1;
        ncnln = 2;
        liwork = 3*n + nclin + 2*ncnln;
        lda = Math.max(1, nclin);
        sda = (nclin > 0) ? n : 1;
        ldcj = Math.max(1, ncnln);
        sdcjac = (ncnln > 0) ? n : 1;
        ldr = n;

        if (ncnln == 0 && nclin > 0) {
            lwork = 2*n*n + 20*n + 11*nclin;
        }
        else if (ncnln > 0 && nclin >= 0) {
            lwork = 2*n*n + n*nclin + 2*n*ncnln + 20*n + 11*nclin + 21*ncnln;
        }
        else {
            lwork = 20*n;
        }

        /* Set arrays */
        istate = new long[(int)(n + nclin + ncnln)];
        iwork = new long[(int)liwork];
        c = new double[(int)(Math.max(1, ncnln))];
        cjac = new double[(int)(ldcj * sdcjac)];
        clamda = new double[(int)(n + nclin + ncnln)];
        objgrd = new double[(int)n];
        r = new double[(int)(ldr * n)];
        work = new double[(int)lwork];

        a = new double[]{1.0, 1.0, 1.0, 1.0};
        bl = new double[]{1.0, 1.0, 1.0, 1.0, -1.0E+25, -1.0E+25, 25.0};
        bu = new double[]{5.0, 5.0, 5.0, 5.0, 20.0, 40.0, 1.0E+25};
        x = new double[]{1.0, 5.0, 5.0, 1.0};

        /* Initialise E04UC */
        E04WB e04wb = new E04WB();
        ifail = 0;
        e04wb.eval("E04UCA", cwsav, LCWSAV, lwsav, LLWSAV, iwsav, LIWSAV, rwsav, LRWSAV, ifail);

        /* Solve the problem */
        E04UC e04uc = new E04UC();
        iter = 0;
        objf = 0.0;
        ifail = -1;
        e04uc.eval(n, nclin, ncnln, lda, ldcj, ldr, a, bl, bu, confun, objfun, iter, istate, c,
                   cjac, clamda, objf, objgrd, r, x, iwork, liwork, work, lwork, iuser, ruser,
                   lwsav, iwsav, rwsav, ifail);
        iter = e04uc.getITER();
        objf = e04uc.getOBJF();
        ifail = e04uc.getIFAIL();

        if ((0 >= ifail && ifail <= 6) || ifail == 8) {
            System.out.println();
            System.out.println(" Varbl  Istate   Value         Lagr Mult");
            System.out.println();

            for (i = 0; i < n; i++) {
                System.out.printf(" V %3d %3d %13.6G %13.4G\n", i+1, istate[(int)i], x[(int)i], clamda[(int)i]);
            }

            if (nclin > 0) {

                /* A*x --> work.
                 * The NAG name equivalent of DGEMV is F06PA */
                DGEMV dgemv = new DGEMV();
                dgemv.eval("N", nclin, n, ONE, a, lda, x, INC1, ZERO, work, INC1);

                System.out.println();
                System.out.println();
                System.out.println(" L Con  Istate   Value         Lagr Mult");
                System.out.println();

                for (i = n; i < n+nclin; i++) {
                    j = i - n;
                    System.out.printf(" L %3d %3d %13.6G %13.4G\n", j+1, istate[(int)i], work[(int)j], clamda[(int)i]);
                }

            }

            if (ncnln > 0) {
                System.out.println();
                System.out.println();
                System.out.println(" N Con  Istate   Value         Lagr Mult");
                System.out.println();

                for (i = n+nclin; i < n+nclin+ncnln; i++) {
                    j = i - n - nclin;
                    System.out.printf(" N %3d %3d %13.6G %13.4G\n", j+1, istate[(int)i], c[(int)j], clamda[(int)i]);
                }

            }

            System.out.println();
            System.out.println();
            System.out.printf(" Final objective value = %11.7G\n", objf);

        }

    }

    /** Routine to evaluate objective function and its 1st derivatives */
    public static class OBJFUN extends E04UC.Abstract_E04UC_OBJFUN {

        public void eval() {

            if (MODE == 0 || MODE == 2) {
               OBJF = X[0] * X[3] * (X[0]+X[1]+X[2]) + X[2];
            }

            if (MODE == 1 || MODE == 2) {
                OBJGRD[0] = X[3] * (X[0]+X[0]+X[1]+X[2]);
                OBJGRD[1] = X[0] * X[3];
                OBJGRD[2] = X[0] * X[3] + ONE;
                OBJGRD[3] = X[0] * (X[0]+X[1]+X[2]);
            }

        }

    }

    /** Routine to evaluate the nonlinear constraints and their 1st derivatives */
    public static class CONFUN extends E04UC.Abstract_E04UC_CONFUN {

        public void eval() {

            if (NSTATE == 1) {

                /* First call to CONFUN.  Set all Jacobian elements to zero.
                 * Note that this will only work when 'Derivative Level = 3'
                 * (the default; see Section 11.2). */

                for (int i = 0; i < CJAC.length; ++i) {
                    CJAC[i] = 0;
                }

            }

            if (NEEDC[0] > 0) {

                if (MODE == 0 || MODE == 2) {
                    C[0] = X[0]*X[0] + X[1]*X[1] + X[2]*X[2] + X[3]*X[3];
                }

                if (MODE == 1 || MODE == 2) {
                    CJAC[0] = X[0] + X[0];
                    CJAC[(int)LDCJ] = X[1] + X[1];
                    CJAC[(int)(2*LDCJ)] = X[2] + X[2];
                    CJAC[(int)(3*LDCJ)] = X[3] + X[3];
                }

            }

            if (NEEDC[1]>0) {

                if (MODE == 0 || MODE == 2) {
                    C[1] = X[0]*X[1]*X[2]*X[3];
                }

                if (MODE == 1 || MODE == 2) {
                    CJAC[1] = X[1]*X[2]*X[3];
                    CJAC[(int)(1+LDCJ)] = X[0]*X[2]*X[3];
                    CJAC[(int)(1+2*LDCJ)] = X[0]*X[1]*X[3];
                    CJAC[(int)(1+3*LDCJ)] = X[0]*X[1]*X[2];
                }

            }

        }

    }

}
