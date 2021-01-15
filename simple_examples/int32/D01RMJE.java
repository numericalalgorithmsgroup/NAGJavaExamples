import com.nag.routines.D01.D01RM;
import java.util.Arrays;

/**
 * D01RM example program text.
 * @author Mo
 */
public class D01RMJE {

  public static void main(String[] args) {

    double bound, epsabs, epsrel;
    double result = Double.NaN;
    double abserr = Double.NaN;
    double[] rinfo, ruser;
    int [] iinfo, iuser;
    int ifail, inf, liinfo, lrinfo, maxsub;
    long cpuser; // c_ptr
    F f = new F();

    /* Header */
    System.out.println(" D01RMJ Example Program Results");

    bound = 0.0;
    inf = 1;
    epsabs = 0.0;
    epsrel = 0.0001;
    maxsub = 20;
    lrinfo = 4*maxsub;
    liinfo = Math.max(maxsub,4);

    rinfo = new double[lrinfo];
    iinfo = new int[liinfo];
    iuser = new int[0];
    ruser = new double[0];

    iuser = new int[] {0};
    ruser = new double[] {0.0};
    cpuser = 0L;

    D01RM d01rm = new D01RM();
    ifail = -1;
    d01rm.eval(f, bound, inf, epsabs, epsrel, maxsub, result, abserr,
               rinfo, iinfo, iuser, ruser, cpuser, ifail);
    result = d01rm.getRESULT();
    abserr = d01rm.getABSERR();
    ifail = d01rm.getIFAIL();
    
    if (ifail >= 0) {
      System.out.println();
      System.out.printf(" A        - lower limit of integration     = %9.4f\n",bound);
      System.out.println(" B        - upper limit of integration     = infinity\n");
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
          System.out.printf(" Exit requested from F \n");
          System.out.println();
        }

      }

    }

  public static class F implements D01RM.D01RM_F {

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

      for (int j = 0; j < NX; j++) { 
        FV[j] = 1.0/((X[j] + 1.0) * Math.sqrt(X[j]));
      }
      return;

    }

  }

}
