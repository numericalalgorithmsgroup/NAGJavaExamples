import com.nag.routines.D01.D01RL;
import java.util.Arrays;

/**
 * D01RL example program text.
 * @author Mo
 */
public class D01RLJE {

  public static void main(String[] args) {

    double a, b, epsabs, epsrel;
    double result = Double.NaN;
    double abserr = Double.NaN;
    double[] points, rinfo, ruser;
    int [] iinfo, iuser;
    int ifail, liinfo, lrinfo, maxsub, npts;
    long cpuser; // c_ptr
    F f = new F();

    /* Header */
    System.out.println(" D01RLJ Example Program Results");

    epsabs = 0.0;
    epsrel = 0.0001;
    a = 0.0;
    b = 1.0;
    npts = 1;
    maxsub = 20;
    liinfo = 2*Math.max(maxsub,npts) + npts + 4;
    lrinfo = 4*Math.max(maxsub,npts) + npts + 6;

    points = new double[npts];
    rinfo = new double[lrinfo];
    iinfo = new int[liinfo];
    iuser = new int[0];
    ruser = new double[21];

    points[0] = 1.0/7.0;
    iuser = new int[] {0};
    ruser = new double[] {0.0};
    cpuser = 0L;

    D01RL d01rl = new D01RL();
    ifail = -1;
    d01rl.eval(f, a, b, npts, points, epsabs, epsrel, maxsub, result, abserr,
               rinfo, iinfo, iuser, ruser, cpuser, ifail);
    points = d01rl.getPOINTS();
    result = d01rl.getRESULT();
    abserr = d01rl.getABSERR();
    ifail = d01rl.getIFAIL();
    
    if (ifail >= 0) {
      System.out.println();
      System.out.printf(" A        - lower limit of integration     = %9.4f\n",a);
      System.out.printf(" B        - upper limit of integration     = %9.4f\n",b);
      System.out.printf(" POINT(1) - given break-point              = %9.4f\n",points[0]);
      System.out.printf(" EPSABS   - absolute accuracy requested    = %9.2E\n",epsabs);
      System.out.printf(" EPSREL   - relative accuracy requested    = %9.2E\n",epsrel);
      System.out.printf(" MAXSUB   - maximum number of subintervals = %4d\n",maxsub);
      System.out.println();
      if (ifail <= 5) {
        System.out.printf(" RESULT   - approximation to the integral  = %9.5f\n",result);
        System.out.printf(" ABSERR   - estimate to the absolute error = %9.2E\n",abserr);
        System.out.printf(" IINFO(1) - number of subintervals used    = %4d\n",iinfo[0]);
        System.out.println();
        }
        else if (ifail == -1) {
        /* User requested exit */
          System.out.printf(" Exit requested from F");
          System.out.println();
        }

      }

    }

  public static class F implements D01RL.D01RL_F {

    private int NX, IFLAG;
    private double[] X, FV, RUSER;
    private int[] IUSER;
    private long CPUSER;

    @Override
    public void setX(double[] X) {
      this.X = X;
    }

    @Override
    public double[] getX() {
      return X;
    }

    @Override
    public void setNX(int NX) {
      this.NX = NX;
    }
 
    @Override
    public int getNX() {
      return NX;
    }
 
    @Override
    public void setFV(double[] FV) {
      this.FV = FV;
    }
 
    @Override
    public double[] getFV() {
      return FV;
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
    public void setCPUSER(long CPUSER) {
      this.CPUSER = CPUSER;
    }

    @Override
    public long getCPUSER() {
      return CPUSER;
    }

    @Override
    public void eval(double[] X, int NX, double[] FV, int IFLAG,
                     int[] IUSER, double[] RUSER, long CPUSER) {

      /*for (int i = 0; i < NX; i++) {*/
        /*FV[i] = 0.0;*/
      /*}*/

      for (int i = 0; i < NX; i++) {
        FV[i] = Math.abs(X[i] - 1.0/7.0);
      }

      for (int i = 0; i < NX; i++) {
        if (FV[i] == 0.0) {
          /* A singular point will be hit. */
          /* Record offending abscissae and abort computation. */
          IFLAG = 0;
          for (int k = 0; k < NX; k++) {
            if (FV[k] == 0.0) {
                IFLAG = IFLAG + 1;
                RUSER[IFLAG-1] = X[k];
            }
          }
        /* Store value of iflag in iuser */
        IUSER[0] = IFLAG;
        /* signal abort by setting iflag<0 */
        IFLAG = -IFLAG;
        }
      }
      if (IFLAG == 0) {
        /* Safe to evaluate. */
        for (int j = 0; j < NX; j++) { 
          FV[j] = 1.0/Math.sqrt(FV[j]);
        }
      }
      return;

    }

  }

}
