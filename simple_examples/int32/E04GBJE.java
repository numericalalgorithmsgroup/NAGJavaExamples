import com.nag.routines.A00.A00AA;
import com.nag.routines.E04.E04GB;
import com.nag.routines.E04.E04HEV;
import com.nag.routines.E04.E04YA;
import com.nag.routines.F06.DDOT;
import com.nag.routines.F06.F06PA;
import com.nag.routines.X02.X02AJ;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * E04GB example program text.
 */
public class E04GBJE {

  public static final int inc1 = 1, liw = 1, m = 15, n = 3, nt = 3;
  public static final int ldfjac = m, ldv = n, lw = 7*n + m*n + 2*m + n*n;
  public static final String trans = "T";
  public static double[] y;
  public static double[] t;

  public static void main(String[] args) {

    double eta, fsumq = 0.0, stepmx, xtol;
    int ifail, iprint, maxcal, nf = 0, niter = 0;
    double[] fjac = new double[ldfjac * n];
    double[] fvec = new double[m];
    double[] g = new double[n];
    double[] s = new double[n];
    double[] v = new double[ldv * n];
    double[] w = new double[lw * n];
    double[] x = new double[n];
    int[] iw = new int[liw];

    System.out.println(" E04GBJ Example Program Results");

    // Data
    y = new double[] {
      0.14, 0.18, 0.22, 0.25, 0.29, 0.32, 0.35, 0.39, 0.37, 0.58, 0.73,
      0.96, 1.34, 2.10, 4.39
    };

    t = new double[m * nt];
    for (int i = 0; i < m; i++) {
      t[i] = i + 1.0;
      t[m + i] = 15.0 - i;
      t[2*m + i] = -Math.abs(i - 7.0) + 8.0;
    }

    // Check LSQFUN by calling E04YA at an arbitrary point
    E04YA e04ya = new E04YA();
    LSQFUN lsqfun = new LSQFUN();
    x[0] = 0.19;
    x[1] = -.34;
    x[2] = 0.88;
    ifail = 0;
    e04ya.eval(m, n, lsqfun, x, fvec, fjac, ldfjac, iw, liw, w, lw, ifail);

    // Continue setting parameters for E04GB
    E04GB e04gb = new E04GB();
    LSQMON lsqmon = new LSQMON();
    LSQLIN lsqlin = new LSQLIN();
    iprint = -1;     // Set to 1 to obtain output from LSQMON at each iteration
    maxcal = 50 * n;
    eta = 0.09;      // Since E04HEV is being used as LSQLIN
    xtol = 10.0 * Math.sqrt((new X02AJ()).eval());
    stepmx = 10.0;

    // Set up the starting point
    x[0] = 0.5;
    x[1] = 1.0;
    x[2] = 1.5;

    ifail = -1;
    e04gb.eval(m, n, lsqlin, lsqfun, lsqmon, iprint, maxcal, eta, xtol,
        stepmx, x, fsumq, fvec, fjac, ldfjac, s, v, ldv, niter, nf, iw,
        liw, w, lw, ifail);
    fsumq = e04gb.getFSUMSQ();
    x = e04gb.getX();
    ifail = e04gb.getIFAIL();

    if (ifail == 0 || ifail >= 2) {
      System.out.println();
      System.out.printf(" On exit, the sum of squares is %12.4f\n", fsumq);
      System.out.printf(" at the point ");
      for (int ii = 0; ii < n; ++ii) {
        System.out.printf("%12.4f", x[ii]);
      }
      System.out.println();

      lsqmon.lsqgrd(m, n, fvec, fjac, ldfjac, g);

      System.out.print(" The corresponding gradient is");
      for (int ii = 0; ii < n; ++ii) {
        System.out.printf("%12.3E", g[ii]);
      }
      System.out.println();
      System.out.println("                           (machine dependent)");
      System.out.println(" and the residuals are");
      for (int ii = 0; ii < m; ++ii) {
        System.out.printf(" %9.1E\n", fvec[ii]);
      }
      System.out.println();
    }

  }

  /** Routine to evaluate the residuals and their 1st derivatives. */
  public static class LSQFUN extends E04GB.Abstract_E04GB_LSQFUN implements E04YA.E04YA_LSQFUN {

    public void eval() {
      double denom, dummy;

      for (int i = 0; i < m; i++) {
        denom = this.XC[1] * t[i + this.M] + this.XC[2] * t[i + 2*this.M];
        this.FVEC[i] = this.XC[0] + t[i] / denom - y[i];

        if (this.IFLAG != 0) {
          this.FJAC[i] = 1.0;
          dummy = -1.0 / (denom * denom);
          this.FJAC[i + ldfjac] = t[i] * t[i + this.M] * dummy;
          this.FJAC[i + 2*ldfjac] = t[i] * t[i + 2*this.M] * dummy;
        }
      }

    }

  }

  public static class LSQMON extends E04GB.Abstract_E04GB_LSQMON {

    public static final int ndec = 3;

    /** Monitoring routine. */
    public void eval() {
      double fsumsq, gtg;
      double[] g = new double[ndec];
      DDOT ddot = new DDOT();

      fsumsq = ddot.eval(this.M, this.FVEC, inc1, this.FVEC, inc1);
      this.lsqgrd(this.M, this.N, this.FVEC, this.FJAC, this.LDFJAC, g);
      gtg = ddot.eval(this.N, g, inc1, g, inc1);

      System.out.println();
      System.out.println("  Itn      F evals        SUMSQ             GTG        Grade");
      System.out.printf(" %4d      %5d      %13.5E      %9.1E      %3d\n",
          this.NITER, this.NF, fsumsq, gtg, this.IGRADE);
      System.out.println();
      System.out.println("       X                    G           Singular values");
      for (int j = 0; j < n; j++) {
        System.out.printf(" %13.5E          %9.1E          %9.1E\n",
            this.XC[j], g[j], this.S[j]);
      }

    }

    /** Routine to evaluate gradient of the sum of squares. */
    public void lsqgrd(int m, int n, double[] fvec, double[] fjac, int ldfjac, double[] g) {
      F06PA f06pa = new F06PA(trans, m, n, 1.0, fjac, ldfjac, fvec, inc1, 0.0, g, inc1);
      f06pa.eval();

      for (int i = 0; i < n; ++i) {
        g[i] = 2.0 * g[i];
      }

    }

  }

  /** Using E04HEV as LSQLIN. */
  private static class LSQLIN extends E04HEV implements E04GB.E04GB_LSQLIN {

    public void eval() {
      super.eval();
    }

  }

}
