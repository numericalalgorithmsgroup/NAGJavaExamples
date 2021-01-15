import com.nag.routines.C05.C05QB;
import com.nag.routines.F06.DNRM2;
import com.nag.routines.X02.X02AJ;

/**
 * C05QB example program text.
 * @author ludovic
 */
public class C05QBJE {

  public static void main(String[] args) {

    C05QB c05qb = new C05QB();

    System.out.println(" C05QBJ Example Program Results");

    int n = 9, ifail = -1;

    double xtol, fnorm;

    int[] IUSER = new int[1];
    double[] fvec = new double[n];
    double[] x = new double[n];
    double[] RUSER = new double[1];


    FCN fcn = new FCN();

    for (int i = 0; i < n; ++i) {
      x[i] = -1.0;
    }
    xtol = Math.sqrt((new X02AJ()).eval());

    c05qb.eval(fcn, n, x, fvec, xtol, IUSER, RUSER, ifail);

    ifail = c05qb.getIFAIL();

    switch (ifail) {
      case (0):
        fnorm = (new DNRM2(n, fvec, 1)).eval();
        System.out.println();
        System.out.printf(" Final 2-norm of the residuals = %11.4E\n", fnorm);
        System.out.println();
        System.out.println(" Final approximate solution");
        int count = 0;
        for (int i = 0; i < n; ++i) {
          System.out.printf(" %12.4f", x[i]);
          ++count;
          if (count == 3) {
            System.out.println();
            count = 0;
          }
        }
        break;
      case (2):
      case (3):
      case (4):
        System.out.println("Approximate solution");
        count = 0;
        for (int i = 0; i < n; ++i) {
          System.out.printf(" %12.4f", x[i]);
          ++count;
          if (count == 3) {
            System.out.println();
            count = 0;
          }
        }
        break;
    }

  }

  public static class FCN implements C05QB.C05QB_FCN {

    private int N, IFLAG;
    private double[] X, FVEC, RUSER;
    private int[] IUSER;

    @Override
    public void setN(int N) {
      this.N = N;
    }

    @Override
    public int getN() {
      return N;
    }

    @Override
    public void setX(double[] X) {
      this.X = X;
    }

    @Override
    public double[] getX() {
      return X;
    }

    @Override
    public void setFVEC(double[] FVEC) {
      this.FVEC = FVEC;
    }

    @Override
    public double[] getFVEC() {
      return FVEC;
    }

    @Override
    public void setIUSER(int[] IUSER) {
      this.IUSER = IUSER;
    }

    @Override
    public int[] getIUSER() {
      return IUSER;
    }

    @Override
    public void setRUSER(double[] RUSER) {
      this.RUSER = RUSER;
    }

    @Override
    public double[] getRUSER() {
      return RUSER;
    }

    @Override
    public void setIFLAG(int IFLAG) {
      this.IFLAG = IFLAG;
    }

    @Override
    public int getIFLAG() {
      return IFLAG;
    }

    @Override
    public void eval(int N, double[] X, double[] FVEC, int[] IUSER, double[] RUSER, int IFLAG) {

      /*
       * fvec(1:n) = (3.0_nag_wp-2.0_nag_wp*x(1:n))*x(1:n) + 1.0_nag_wp
       * fvec(2:n) = fvec(2:n) - x(1:(n-1))
       * fvec(1:(n-1)) = fvec(1:(n-1)) - 2.0_nag_wp*x(2:n)
       */
      for (int i = 0; i < N; ++i) {
        FVEC[i] = (3.0 - 2.0 * X[i]) * X[i] + 1.0;
        /*if (i >= 1) {
          FVEC[i] = FVEC[i] - X[i - 1];
          }
          if (i < N - 1) {
          FVEC[i] = FVEC[i] - 2.0 * X[i + 1];
          }*/
      }
      for (int i = 1; i < N; ++i) {
        FVEC[i] = FVEC[i] - X[i - 1];
      }
      for (int i = 0; i < N-1; ++i) {
        FVEC[i] = FVEC[i] - 2.0 * X[i + 1];
      }

    }

  }

}
