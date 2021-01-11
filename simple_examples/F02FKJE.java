import com.nag.routines.F02.F02FK;
import com.nag.routines.F12.F12FD;
import com.nag.routines.X04.X04AB;
import com.nag.routines.X04.X04CA;
import java.util.Arrays;

/**
 * F02FK example program text.
 * @author joed
 */
public class F02FKJE {

  public static MYMONIT mymonit = new MYMONIT();
  public static MYOPTION myoption = new MYOPTION();

  public static void main(String[] args) {
    F02FK f02fk = new F02FK();
    X04AB x04ab = new X04AB();
    X04CA x04ca = new X04CA();
    double h2, sigma;
    int iset = 1, ifail, imon, k, ldv, lo, maxit, mode, n, nconv, ncv, nev,
        nnz, nx, outchn, prtlvl;
    double[] a, dPrint, resid, v, w, ruser = new double[1];
    int[] icol, irow, iuser = new int[4];


    System.out.println(" F02FKJ Example Program Results");
    System.out.println();

    nx = 20;
    nev = 8;
    ncv = 20;
    sigma = 1.0;

    // Construct the matrix A in sparse form and store in A.
    // The main diagonal of A is full and there are two subdiagonals of A:
    // the first and the nx-th.

    n = nx * nx;
    nnz = (3 * n) - (2 * nx);
    a = new double[nnz];
    irow = new int[nnz];
    icol = new int[nnz];

    // Zero out A.
    Arrays.fill(a, 0.0);

    // Main diagonal of A.
    h2 = 1.0 / (double)((nx + 1) * (nx + 1));
    for (int i = 1; i <= n; i++) {
      irow[i - 1] = i;
      icol[i - 1] = i;
      a[i - 1] = 4.0 / h2;
    }

    // First subdiagonal of A.
    k = n;
    for (int i = 1; i <= nx; i++) {
      lo = (i - 1) * nx;
      for (int j = lo + 1; j <= lo + nx - 1; j++) {
        k += 1;
        irow[k - 1] = j + 1;
        icol[k - 1] = j;
        a[k - 1] = -1.0 / h2;
      }
    }

    // nx-th subdiagonal.
    for (int i = 1; i <= nx - 1; i++) {
      lo = (i - 1) * nx;
      for (int j = lo + 1; j <= lo + nx; j++) {
        k += 1;
        irow[k - 1] = j + nx;
        icol[k - 1] = j;
        a[k - 1] = -1.0 / h2;
      }
    }

    // Set some options via iuser array and routine argument OPTION.
    // iuser[0] = print level,
    // iuser[1] = iteration limit,
    // iuser[2]>0 means shifted-invert mode
    // iuser[3]>0 means print monitoring info

    prtlvl = 0;
    maxit = 500;
    mode = 1;
    imon = 0;

    ruser[0] = 1.0;
    iuser[0] = prtlvl;
    iuser[1] = maxit;
    iuser[2] = mode;
    iuser[3] = imon;

    // Find eigenvalues of largest magnitude and the corresponding
    // eigenvectors.

    ldv = n;
    w = new double[ncv];
    v = new double[ldv * ncv];
    resid = new double[n];

    nconv = 0; // placeholder

    ifail = -1;
    f02fk.eval(
        n, nnz, a, irow, icol, nev, ncv, sigma, mymonit, myoption, nconv,
        w, v, ldv, resid, iuser, ruser, ifail
    );
    ifail = f02fk.getIFAIL();
    nconv = f02fk.getNCONV();
    if (ifail != 0) {
      System.err.println("  ** F02FK returned with IFAIL = " + ifail);
    }



    // Print Eigenvalues and the residual norm  ||A*x - lambda*x||.
    dPrint = new double[nconv * 2];
    for (int i = 1; i <= nconv; i++) {
      dPrint[getIdx(i, 1, nconv)] = w[i - 1];
      dPrint[getIdx(i, 2, nconv)] = resid[i - 1];
    }

    System.out.println();

    ifail = 0;
    x04ca.eval(
        "G", "N", nconv, 2, dPrint, nconv, " Ritz values and residuals",
        ifail
    );

  }

  public static class MYOPTION extends F02FK.Abstract_F02FK_OPTION {

    public void eval() {
      F12FD f12fd = new F12FD();
      int ifail1;
      String rec = "                         "; // Required len = 25

      this.ISTAT = 0;

      if (this.IUSER[0] > 0) {
        System.out.printf(" Print Level=%5d\n", this.IUSER[0]);
        ifail1 = 1;
        f12fd.eval(rec, this.ICOMM, this.COMM, ifail1);
        ifail1 = f12fd.getIFAIL();
        this.ISTAT = Math.max(this.ISTAT, ifail1);
      }

      if (this.IUSER[1] > 100) {
        System.out.printf(" Iteration Limit=%5d\n", this.IUSER[1]);
        ifail1 = 1;
        f12fd.eval(rec, this.ICOMM, this.COMM, ifail1);
        ifail1 = f12fd.getIFAIL();
        this.ISTAT = Math.max(this.ISTAT, ifail1);
      }

      if (this.IUSER[2] > 0) {
        ifail1 = 1;
        f12fd.eval(
            "Shifted Inverse          ", this.ICOMM, this.COMM, ifail1
        );
        ifail1 = f12fd.getIFAIL();
        this.ISTAT = Math.max(this.ISTAT, ifail1);
      }

    }

  }

  public static class MYMONIT extends F02FK.Abstract_F02FK_MONIT {

    public void eval() {

      if (this.IUSER[3] > 0) {

        if (this.NITER == 1 && this.IUSER[2] > 0) {
          System.out.printf(
              "  Arnoldi basis vectors used:%4d\n", this.NCV
          );
          System.out.printf(
              " The following Ritz values (mu) are related to the\n"
              + " true eigenvalues (lambda) by lambda = sigma + 1/mu\n"
          );
        }

        System.out.println();
        System.out.printf("  Iteration number %4d\n", this.NITER);
        System.out.printf(
            " Ritz values converged so far(%4d) and their Ritz"
            + " estimates:\n", this.NCONV
        );

        for (int i = 0; i < this.NCONV; i++) {
          System.out.printf(
              "  %4d %13.5E %13.5E\n", i+1, this.W[i], this.RZEST[i]
          );
        }

        System.out.printf(" Next (uncoverged) Ritz value:\n");
        System.out.printf(
            "  %4d %13.5E\n", this.NCONV + 1, this.W[this.NCONV + 1]
        );

      }

      this.ISTAT = 0;

    }

  }

  /**
   * Converts a 2D, 1-based Fortran index to its corresponding 1D, 0-based
   * Java index.
   *
   * <p>Fortran array definition:
   *     a(dimX, *)
   *
   * <p>Conversion:
   *     a(x, y) --> A[result]
   */
  private static int getIdx(int x, int y, int dimX) {
    return ((y-1) * dimX) + (x-1);
  }

}
