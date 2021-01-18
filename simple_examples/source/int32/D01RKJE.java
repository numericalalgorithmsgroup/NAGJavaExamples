import com.nag.routines.D01.D01RK;
import com.nag.routines.X01.X01AA;
import java.util.Arrays;

/**
 * D01RK example program text.
 * @author Mo
 */
public class D01RKJE {

  public static void main(String[] args) {

    double pi = 0.0;
    double a, b, epsabs, epsrel;
    double result = Double.NaN;
    double abserr = Double.NaN;
    double[] rinfo, ruser;
    int [] iinfo, iuser;
    int ifail, key, liinfo, lrinfo, maxsub;
    long cpuser; // c_ptr
    F f = new F();

    /* Header */
    System.out.println(" D01RKJ Example Program Results");

    key = 6;
    X01AA x01aa = new X01AA(pi);
    pi = x01aa.eval();
    a = 0.0;
    b = 2.0*pi;
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
    ruser = new double[] {4.0*Math.pow(pi,2)};
    cpuser = 0L;

    D01RK d01rk = new D01RK();
    ifail = -1;
    d01rk.eval(f, a, b, key, epsabs, epsrel, maxsub, result, abserr,
                rinfo, iinfo, iuser, ruser, cpuser, ifail);
    result = d01rk.getRESULT();
    abserr = d01rk.getABSERR();
    ifail = d01rk.getIFAIL();
    
    if (ifail >= 0) {
      System.out.println();
      System.out.printf(" A        - lower limit of integration     = %9.4f\n",a);
      System.out.printf(" B        - upper limit of integration     = %9.4f\n",b);
      System.out.printf(" KEY      - choice of Gaussian rule        = %4d\n",key);
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

  public static class F implements D01RK.D01RK_F {

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

      for (int i = 0; i < NX; i++) {
        FV[i] = 0.0;
      }

      for (int j = 0; j < NX; j++) { 
        FV[j] = X[j] * Math.sin(30.0 * X[j]) * Math.cos(X[j]);
      }
      return;

    }

  }

}
