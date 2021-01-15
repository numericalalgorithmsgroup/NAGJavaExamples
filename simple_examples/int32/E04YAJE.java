import com.nag.routines.E04.E04YA;


/**
 * E04YA example program text.
 * @author joed
 */
public class E04YAJE {

  public static LSQFUN lsqfun = new LSQFUN();
  public static final int LIW = 1, MDEC = 15, NDEC = 3;
  public static final int LDFJAC = MDEC;
  public static final int LW = 3*NDEC + MDEC + MDEC*NDEC;
  public static double[] t = new double[MDEC*NDEC], y = new double[MDEC];

  /** Routine to evaluate the residuals and their 1st derivatives. */
  public static class LSQFUN extends E04YA.Abstract_E04YA_LSQFUN {

    public void eval() {
      double denom, dummy;
      int i;

      for (i = 0; i < M; i++) {
        denom = (XC[1] * t[MDEC + i]) + (XC[2] * t[2*MDEC + i]);

        if (IFLAG != 1) {
          FVEC[i] = XC[0] + (t[i]/denom) - y[i];
        }

        if (IFLAG != 0) {
          FJAC[i] = 1.0;
          dummy = -1.0/(denom*denom);
          FJAC[MDEC + i] = t[i] * t[MDEC + i] * dummy;
          FJAC[2*MDEC + i] = t[i] * t[2*MDEC + i] * dummy;
        }

      }
    }

  }

  public static void main(String[] args) {
    int i, ifail, m, n;
    double[] fjac = new double[LDFJAC*NDEC], fvec = new double[MDEC],
        w = new double[LW], x = new double[NDEC];
    int[] iw = new int[LIW];

    System.out.println(" E04YAJ Example Program Results");

    n = NDEC;
    m = MDEC;

    /* Observations of TJ (J = 1, 2, ..., n) are held in T(I, J)
     * (I = 1, 2, ..., m) */

    y = new double[]{0.14, 0.18, 0.22, 0.25, 0.29, 0.32, 0.35, 0.39, 0.37,
                     0.58, 0.73, 0.96, 1.34, 2.10, 4.39};
    for (i = 0; i < m; i++) {
      t[i] = i + 1.0;
      t[m + i] = 15.0 - i;
      t[2*m + i] = -Math.abs(i - 7.0) + 8.0;
    }

    /* Set up an arbitrary point at which to check the 1st derivatives */

    x[0] =  0.19;
    x[1] = -1.34;
    x[2] =  0.88;

    System.out.println();
    System.out.println("The test point is");
    System.out.printf(" ");
    for (i = 0; i < n; i++) {
      System.out.printf("%10.5f", x[i]);
    }
    System.out.printf("\n");

    E04YA e04ya = new E04YA();
    ifail = -1;
    e04ya.eval(m, n, lsqfun, x, fvec, fjac, LDFJAC, iw, LIW, w, LW, ifail);
    ifail = e04ya.getIFAIL();

    if (ifail >= 0 && ifail != 1) {

      switch (ifail) {
        case 0:
          System.out.println();
          System.out.println("1st derivatives are consistent with residual values");
          break;
        case 2:
          System.out.println();
          System.out.println("Probable error in calculation of 1st derivatives");
          break;
        default:
      }

      System.out.println();
      System.out.println("At the test point, LSQFUN gives");
      System.out.println();
      System.out.println("      Residuals                   1st derivatives");
      for (i = 0; i < m; i++) {
        System.out.printf(" %15.3E", fvec[i]);
        System.out.printf("%15.3E", fjac[i]);
        System.out.printf("%15.3E", fjac[LDFJAC + i]);
        System.out.printf("%15.3E\n", fjac[2*LDFJAC + i]);
      }

    }

  }

}
