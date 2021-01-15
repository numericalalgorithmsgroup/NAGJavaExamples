import com.nag.routines.F02.F02WG;
import java.util.Arrays;

/**
 * F02WG example program text.
 * @author joed
 */
public class F02WGJE {

  public static AV av = new AV();

  public static void main(String[] args) {
    int ifail, k, ldu, ldv, m, n, nconv, ncv;
    double[] resid, sigma, u, v;
    double[] ruser = new double[1];
    int[] iuser = new int[1];

    /* Set up wrappers */
    F02WG f02wg = new F02WG();

    System.out.println(" F02WGJ Example Program Results\n");

    /* Set F02WG args */
    m = 100;
    n = 500;
    k = 4;
    ncv = 10;
    nconv = 0;
    ldu = m;
    ldv = n;
    resid = new double[ncv];
    sigma = new double[ncv];
    u = new double[ldu * ncv];
    v = new double[ldv * ncv];
    ifail = 0;

    /* Call routine */
    f02wg.eval(
        m,      // Rows in A
        n,      // Cols in A
        k,      // Num. of singular values to be computed
        ncv,    // Dimension of sigma and resid
        av,     // Subroutine that returns Ax or (A^T)x
        nconv,  // [Output]
        sigma,  // [Output]
        u,      // [Output]
        ldu,    // 1st dimension of u
        v,      // [Output]
        ldv,    // 1st dimension of v
        resid,  // [Output]
        iuser,
        ruser,
        ifail
    );

    /* Get non-array output args */
    ifail = f02wg.getIFAIL();
    nconv = f02wg.getNCONV();

    /* Print results */
    System.out.println("  Singular Value    Residual");
    for (int i = 0; i < nconv; i++) {
      System.out.printf(
          " %10.5f        %10.2G\n", sigma[i], resid[i]
      );
    }

  }

  public static class AV extends F02WG.Abstract_F02WG_AV {

    public void eval() {

      double H = 1.0 / ((double)(this.M + 1));
      double K = 1.0 / ((double)(this.N + 1));

      if (this.IFLAG == 1) {

        for (int i = 0; i < this.M; i++) {
          this.AX[i] = 0.0;
        }

        double S = 0.0;
        double T = 0.0;

        for (int j = 0; j < this.N; j++) {
          T += K;
          S = 0.0;

          for (int i = 0; i < Math.min(j+1, this.M); i++) {
            S += H;
            this.AX[i] += K * S * (T - 1.0) * this.X[j];
          }
          for (int i = j + 1; i < this.M; i++) {
            S += H;
            this.AX[i] += K * T * (S - 1.0) * this.X[j];
          }

        }

      }
      else {

        for (int i = 0; i < this.N; i++) {
          this.AX[i] = 0.0;
        }

        double S = 0.0;
        double T = 0.0;

        for (int j = 0; j < this.N; j++) {
          T += K;
          S = 0.0;

          for (int i = 0; i < Math.min(j+1, this.M); i++) {
            S += H;
            this.AX[j] += K * S * (T - 1.0) * this.X[i];
          }
          for (int i = j + 1; i < this.M; i++) {
            S += H;
            this.AX[j] += K * T * (S - 1.0) * this.X[i];
          }

        }

      }

    }

  }

}
