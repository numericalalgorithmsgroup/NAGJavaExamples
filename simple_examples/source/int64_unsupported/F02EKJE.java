emacimport java.util.Arrays;

import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.F02.F02EK;
import com.nag.routines.F12.F12AD;
import com.nag.routines.X02.X02AJ;
import com.nag.routines.Routine;
import com.nag.types.*;


/**
 * @author joed
 */
public class F02EKJE {

    public static MYMONIT mymonit = new MYMONIT();
    public static MYOPTION myoption = new MYOPTION();

    public static void main(String[] args) throws NAGBadIntegerException {
        F02EK f02ek = new F02EK();
        X02AJ x02aj = new X02AJ();
        double h, rho, s, sigma;
        int ifail, imon, k, ldv, maxit, mode, n, nconv, ncv, nev, nnz, nx,
            prtlvl;
        NAGComplex complex = new NAGComplex();
        double[] a, resid, v, ruser = new double[1];
        int[] icolzp, irowix, iuser = new int[4];
        NAGComplex[] w;

        // Initialise wrapper library
        Routine.init();
        Routine.setComplex(complex);

        System.out.println(" F02EKJ Example Program Results");
        System.out.println();

        nx = 10;
        nev = 4;
        ncv = 20;
        rho = 10.0;
        sigma = 5.5;

        n = nx * nx;
        nnz = 3*n - 2;
        ldv = n;

        resid = new double[ncv];
        a = new double[nnz];
        icolzp = new int[n + 1];
        irowix = new int[nnz];
        w = (NAGComplex[]) complex.getArrayOfInstances(ncv);
        v = new double[ldv * ncv];

        // Construct A in compressed column storage (CCS) format where:
        //   A{ i ,  i } = 2 + i
        //   A{i+1,  i } = 3
        //   A{ i , i+1} = rho/(2n+2) - 1

        h = 1.0 / (double)(n+1);
        s = (rho * h / 2.0) - 1.0;

        a[0] = 2.0 + 1.0;
        a[1] = 3.0;
        icolzp[0] = 1;
        irowix[0] = 1;
        irowix[1] = 2;
        k = 3;

        for (int i = 1; i < n - 1; i++) {
            icolzp[i] = k;
            irowix[k - 1] = i;
            irowix[k] = i + 1;
            irowix[k + 1] = i + 2;
            a[k - 1] = s;
            a[k] = 2.0 + (double)(i + 1);
            a[k + 1] = 3.0;
            k += 3;
        }

        icolzp[n - 1] = k;
        icolzp[n] = k + 2;
        irowix[k - 1] = n - 1;
        irowix[k] = n;
        a[k - 1] = s;
        a[k] = 2.0 + (double)(n);

        // Set some options via iuser array and return routine argument OPTION
        // iuser[0] = print level
        // iuser[1] = iteration limit
        // iuser[2] > 0 means shifted-invert mode
        // iuser[3] > 0 means print monitoring info

        prtlvl = 0;
        maxit = 500;
        mode = 1;
        imon = 1;

        if (prtlvl > 0) {
            imon = 0;
        }

        iuser[0] = prtlvl;
        iuser[1] = maxit;
        iuser[2] = mode;
        iuser[3] = imon;

        nconv = 0; // placeholder value, nconv is output only
        ifail = 0; // hard exit on error

        f02ek.eval(
            n, nnz, a, icolzp, irowix, nev, ncv, sigma, mymonit, myoption,
            nconv, w, v, ldv, resid, iuser, ruser, ifail
        );
        ifail = f02ek.getIFAIL();
        nconv = f02ek.getNCONV();

        System.out.println();
        System.out.printf(
            " The %4d Ritz values of closest to %13.5E are:\n", nconv, sigma
        );

        // Get machine precision
        double mp = x02aj.eval();

        for (int i = 0; i < nconv; i++) {
            if (resid[i] > (double)(100*n*mp)) {
                System.out.printf(
                    " %8d     ( %13.5E , %13.5E )     %13.5E\n",
                    i + 1, w[i], resid[i]
                );
            }
            else {
                System.out.printf(
                    " %8d     ( %13.5E , %13.5E )\n",
                    i + 1, w[i].getRe(), w[i].getIm()
                );
            }
        }

    }

    public static class MYOPTION extends F02EK.Abstract_F02EK_OPTION {

        public void eval() {
            F12AD f12ad = new F12AD();
            int ifail1;
            String rec = "                         ";

            this.ISTAT = 0;

            if (this.IUSER[0] > 0) {
                System.out.printf("Print Level=%5d\n", this.IUSER[0]);
                ifail1 = 1;
                f12ad.eval(rec, this.ICOMM, this.COMM, ifail1);
                ifail1 = f12ad.getIFAIL();
                this.ISTAT = Math.max(this.ISTAT, ifail1);
            }

            if (this.IUSER[1] > 100) {
                System.out.printf("Iteration Limit=%5d\n", this.IUSER[1]);
                ifail1 = 1;
                f12ad.eval(rec, this.ICOMM, this.COMM, ifail1);
                ifail1 = f12ad.getIFAIL();
                this.ISTAT = Math.max(this.ISTAT, ifail1);
            }

            if (this.IUSER[2] > 0) {
                ifail1 = 1;
                f12ad.eval (
                    "Shifted Inverse Real     ", this.ICOMM, this.COMM, ifail1
                );
                ifail1 = f12ad.getIFAIL();
                this.ISTAT = Math.max(this.ISTAT, ifail1);
            }

        }

    }

    public static class MYMONIT extends F02EK.Abstract_F02EK_MONIT {

        public NAGComplexInterface[] getW(){
            return this.W;
        }

        public void eval() {

            if (this.IUSER[3] > 0) {

                if (this.NITER == 1 && this.IUSER[2] > 0) {
                    System.out.printf(
                        " Arnoldi basis vectors used:%4d\n", this.NCV
                    );
                    System.out.printf(
                        " The following Ritz values (mu) are related to the\n"
                      + " true eigenvalues (lambda) by lambda = sigma + 1/mu\n"
                    );
                }

                System.out.println();
                System.out.printf(" Iteration number %4d\n", this.NITER);
                System.out.printf(
                    " Ritz values converged so far (%4d) and their Ritz"
                  + " estimates:\n", this.NCONV
                );

                for (int i = 0; i < this.NCONV; i++) {
                    System.out.printf(
                        "  %4d (%13.5E,%13.5E) %13.5E\n",
                        i + 1, this.W[i].getRe(), this.W[i].getIm(),
                        this.RZEST[i]
                    );
                }

                System.out.printf(" Next (unconverged) Ritz value:\n");

                System.out.printf(
                    "  %4d (%13.5E,%13.5E)\n",
                    this.NCONV + 1, this.W[NCONV].getRe(), this.W[NCONV].getIm()
                );

            }

            this.ISTAT = 0;

        }

    }

}
