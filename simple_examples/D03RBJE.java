import com.nag.routines.D03.D03RB;
import com.nag.routines.D03.D03RZ;
import java.util.Arrays;

/**
 * D03RB example program text.
 * @author joed
 */
public class D03RBJE {

  public static final int ITRACE = -1;
  public static final int NPDE = 2;
  public static final double[] TWANT = {0.25, 1.0};

  public static boolean do_monitr;
  public static int print_stats = 0;

  public static BNDARY bndary = new BNDARY();
  public static INIDOM inidom = new INIDOM();
  public static MONITR monitr = new MONITR();
  public static PDEDEF pdedef = new PDEDEF();
  public static PDEIV  pdeiv  = new PDEIV();

  public static void main(String[] args) {
    D03RB d03rb = new D03RB();
    double tols, tolt, tout, ts;
    int ifail, ind, leniwk, lenlwk, lenrwk, maxlev, mxlev, npts;
    boolean[] lwk;
    double[] optr, rwk, dt = new double[3];
    int[] iwk, opti = new int[4];

    System.out.println(" D03RBJ Example Program Results");

    npts = 3000;
    mxlev = 7;

    leniwk = 10 * npts * (5 * mxlev + 14) + 2 + 7 * mxlev;
    lenlwk = 20 * npts;
    lenrwk = 20 * (npts * NPDE * (5 * mxlev + 9 + 18 * NPDE) + 2 * npts);

    rwk = new double[lenrwk];
    iwk = new int[leniwk];
    lwk = new boolean[lenlwk];
    optr = new double[3 * NPDE];

    // Specify that we are starting the integration in time
    // (ind = 0 normally).
    ind = 10;

    ts = 0.0;
    dt[0] = 0.001;
    dt[1] = 1.0E-7;
    dt[2] = 0.0;
    tols = 0.1;
    tolt = 0.05;
    opti[0] = mxlev;
    maxlev = opti[0];
    Arrays.fill(opti, 1, 4, 0);
    Arrays.fill(optr, 1.0);

    // Call main routine
    for (int iout = 1; iout <= 2; iout++) {
      do_monitr = (iout == 2);
      tout = TWANT[iout - 1];

      ifail = 0;
      d03rb.eval(
          NPDE, ts, tout, dt, tols, tolt, inidom, pdedef, bndary, pdeiv,
          monitr, opti, optr, rwk, lenrwk, iwk, leniwk, lwk, lenlwk,
          ITRACE, ind, ifail
      );
      ind = d03rb.getIND();
      ifail = d03rb.getIFAIL();
      ts = d03rb.getTS();

      if (print_stats != 0) {
        System.out.printf(" Statistics:\n");
        System.out.printf(" Time = %8.4f\n", ts);
        System.out.printf(
            " Total number of accepted timesteps =%5d\n", iwk[0]
        );
        System.out.printf(
            " Total number of rejected timesteps =%5d\n", iwk[1]
        );
        System.out.println(
            "             Total  number  of     "
            + "  maximum  number  of     "
        );
        System.out.println(
            "       Residual Jacobian    Newton         Newton   "
        );
        System.out.println(
            "        evals     evals     iters          iters   "
        );
        System.out.println(" Level ");

        maxlev = opti[0];
        for (int j = 0; j < maxlev; j++) {
          if (iwk[j+2] != 0) {
            System.out.printf(
                "%4d%10d%10d%10d%10d\n",
                j+1,
                iwk[j + 2 + 0*maxlev],
                iwk[j + 2 + 1*maxlev],
                iwk[j + 2 + 2*maxlev],
                iwk[j + 2 + 4*maxlev]
            );
          }
        }
        System.out.println();

      }

    }

  }

  public static class PDEIV extends D03RB.Abstract_D03RB_PDEIV {

    public void eval(int NPTS, int NPDE, double T, double[] X, double[] Y, double[] U) {
      this.setNPTS(NPTS);
      this.setNPDE(NPDE);
      this.setT(T);
      this.setX(X);
      this.setY(Y);
      this.setU(U);
      this.eval();
    }

    public void eval() {
      double eps = 0.001, a;

      for (int i = 1; i <= this.NPTS; i++) {
        a = (4.0 * (this.Y[i-1] - this.X[i-1]) - this.T) / (32.0 * eps);
        if (a <= 0.0) {
          this.U[getIdx(i, 1, this.NPTS)]
              = 0.75 - 0.25 / (1.0 + Math.exp(a));
          this.U[getIdx(i, 2, this.NPTS)]
              = 0.75 + 0.25 / (1.0 + Math.exp(a));
        }
        else {
          a = -a;
          this.U[getIdx(i, 1, this.NPTS)]
              = 0.75 - 0.25 * Math.exp(a) / (1.0 + Math.exp(a));
          this.U[getIdx(i, 2, this.NPTS)]
              = 0.75 + 0.25 * Math.exp(a) / (1.0 + Math.exp(a));
        }
      }

    }

  }

  public static class INIDOM extends D03RB.Abstract_D03RB_INIDOM {

    public void eval() {
      int ifail, leniwk;
      int[] icold, ilbndd, irowd, lbndd, llbndd, lrowd;
      int[] iwk = new int[122];
      String[] pgrid = new String[11];

      for (int i = 0; i < 11; i++) {
        pgrid[i] = "                                 ";
      }

      icold = new int[]{
        0,1,2,0,1,2,3,4,5,6,7,8,9,10,0,1,2,3,4,5,6,7,8,9,10,0,1,2,3,4,5,
        6,7,8,9,10,0,1,2,3,4,5,8,9,10,0,1,2,3,4,5,6,7,8,9,10,0,1,2,3,4,
        5,6,7,8,9,10,0,1,2,3,4,5,6,7,8,9,10,0,1,2,3,4,5,6,7,8,0,1,2,3,4,
        5,6,7,8,0,1,2,3,4,5,6,7,8
      };

      ilbndd = new int[]{
        1,2,3,4,1,4,1,2,3,4,3,4,1,2,12,23,34,41,14,41,12,23,34,41,43,14,
        21,32
      };

      irowd = new int[]{0,1,2,3,4,5,6,7,8,9,10};

      lbndd = new int[]{
        2,4,15,26,37,46,57,68,79,88,98,99,100,101,102,103,104,96,86,85,
        84,83,82,70,59,48,39,28,17,6,8,9,10,11,12,13,18,29,40,49,60,72,
        73,74,75,76,77,67,56,45,36,25,33,32,42,52,53,43,1,97,105,87,81,
        3,7,71,78,14,31,51,54,34
      };

      llbndd = new int[]{
        1,2,11,18,19,24,31,37,42,48,53,55,56,58,59,60,61,62,63,64,65,66,
        67,68,69,70,71,72
      };

      lrowd = new int[]{1,4,15,26,37,46,57,68,79,88,97};

      this.NX = 11;
      this.NY = 11;

      // Check MAXPTS against rough estimate of NPTS.
      this.NPTS = this.NX * this.NY;
      if (this.MAXPTS < this.NPTS) {
        this.IERR = -1;
        return;
      }

      this.XMIN = 0.0;
      this.YMIN = 0.0;
      this.XMAX = 1.0;
      this.YMAX = 1.0;

      this.NROWS = 11;
      this.NPTS = 105;
      this.NBNDS = 28;
      this.NBPTS = 72;

      for (int i = 0; i < this.NROWS; i++) {
        this.LROW[i] = lrowd[i];
        this.IROW[i] = irowd[i];
      }

      for (int i = 0; i < this.NBNDS; i++) {
        this.LLBND[i] = llbndd[i];
        this.ILBND[i] = ilbndd[i];
      }

      for (int i = 0; i < this.NBPTS; i++) {
        this.LBND[i] = lbndd[i];
      }

      for (int i = 0; i < this.NPTS; i++) {
        this.ICOL[i] = icold[i];
      }

    }

  }

  public static class PDEDEF extends D03RB.Abstract_D03RB_PDEDEF {

    public void eval() {

      double eps = 1E-3;
      int n = this.NPTS; // For concise getIdx calls

      for (int i = 1; i <= n; i++) {
        this.RES[getIdx(i, 1, n)]
            = -this.U[getIdx(i, 1, n)] * this.UX[getIdx(i, 1, n)]
            -  this.U[getIdx(i, 2, n)] * this.UY[getIdx(i, 1, n)]
            +  eps * (this.UXX[getIdx(i, 1, n)]
                + this.UYY[getIdx(i, 1, n)]);
        this.RES[getIdx(i, 2, n)]
            = -this.U[getIdx(i, 1, n)] * this.UX[getIdx(i, 2, n)]
            -  this.U[getIdx(i, 2, n)] * this.UY[getIdx(i, 2, n)]
            +  eps * (this.UXX[getIdx(i, 2, n)]
                + this.UYY[getIdx(i, 2, n)]);
        this.RES[getIdx(i, 1, n)]
            = this.UT[getIdx(i, 1, n)] - this.RES[getIdx(i, 1, n)];
        this.RES[getIdx(i, 2, n)]
            = this.UT[getIdx(i, 2, n)] - this.RES[getIdx(i, 2, n)];
      }

    }

  }

  public static class BNDARY extends D03RB.Abstract_D03RB_BNDARY {

    public void eval() {

      double a, eps = 1E-3;
      int i, n = this.NPTS;

      for (int k = this.LLBND[0]; k <= this.NBPTS; k++) {
        i = this.LBND[k - 1];
        a = (-4.0 * this.X[i - 1] + 4.0 * this.Y[i - 1] - this.T)
            / (32.0 * eps);

        if (a <= 0.0) {
          this.RES[getIdx(i, 1, n)]
              = 0.75 - 0.25 / (1.0 + Math.exp(a));
          this.RES[getIdx(i, 2, n)]
              = 0.75 + 0.25 / (1.0 + Math.exp(a));
        }
        else {
          a = -a;
          this.RES[getIdx(i, 1, n)]
              = 0.75 - (0.25 * Math.exp(a)) / (1.0 + Math.exp(a));
          this.RES[getIdx(i, 2, n)]
              = 0.75 + (0.25 * Math.exp(a)) / (1.0 + Math.exp(a));
        }

        this.RES[getIdx(i, 1, n)]
            = this.U[getIdx(i, 1, n)] - this.RES[getIdx(i, 1, n)];
        this.RES[getIdx(i, 2, n)]
            = this.U[getIdx(i, 2, n)] - this.RES[getIdx(i, 2, n)];

      }

    }

  }

  public static class MONITR extends D03RB.Abstract_D03RB_MONITR {

    public void eval() {

      D03RZ d03rz = new D03RZ();
      double aprxU, exctU, aprxV, exctV;
      int maxpts = 6000;
      int ifail, ipsol, npts;
      double[] uex = new double[105*2], x = new double[maxpts],
          y = new double[maxpts];

      for (int level = 0; level < this.NLEV; level++) {

        if (!this.TLAST) {
          break;
        }

        ipsol = this.LSOL[level];

        // Get grid information
        ifail = -1;
        npts = 0;
        d03rz.eval(
            level + 1, this.NLEV, this.XMIN, this.YMIN, this.DXB,
            this.DYB, this.LGRID, this.ISTRUC, npts, x, y, maxpts,
            ifail
        );
        ifail = d03rz.getIFAIL();
        npts = d03rz.getNPTS();

        if (ifail != 0) {
          this.IERR = 1;
          break;
        }

        // Skip printing?
        if (!do_monitr || (level != 0)) {
          continue;
        }

        // Get exact solution
        pdeiv.eval(npts, this.NPDE, this.T, x, y, uex);

        System.out.println();
        System.out.printf(
            " Solution at every 2nd grid point in level %d at"
            + " time %8.4f:\n\n", level + 1, this.T
        );
        System.out.print(
            "       x         y      approx u  exact u  approx v"
            + "  exact v\n\n"
        );

        ipsol = this.LSOL[level];

        for (int i = 0; i < npts; i += 2) {
          aprxU = this.SOL[ipsol + i];
          exctU = uex[getIdx(i+1, 1, npts)];
          aprxV = this.SOL[ipsol + npts + i];
          exctV = uex[getIdx(i+1, 2, npts)];
          System.out.printf(
              " %9.2f %9.2f %9.2f %9.2f %9.2f %9.2f\n",
              x[i], y[i], aprxU, exctU, aprxV, exctV
          );
        }
        System.out.println();

      }

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
