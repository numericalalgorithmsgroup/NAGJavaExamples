import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.Routine;
import com.nag.routines.C05.C05MB;
import com.nag.routines.F06.DNRM2;
import com.nag.routines.X02.X02AJ;

/**
 * C05MBJ Example Program Text
 * @author joed
 * @since 27.0
 */
 /* ** There have been at least 200*(n+1) calls to fcn.
 ** Consider restarting the calculation from the point held in x.
 ** ABNORMAL EXIT from NAG Library routine
 ** NAG soft failure - control returned
*/
public class C05MBJE {

  private static final int n = 4;

  private static class FCN extends C05MB.Abstract_C05MB_FCN {

    public void eval() {

      FVEC[0] = Math.cos(X[2]) - X[0];
      FVEC[1] = Math.sqrt(1.0 - Math.pow(X[3],2)) - X[1];
      FVEC[2] = Math.sin(X[0]) - X[2];
      FVEC[3] = Math.pow(X[1], 2) - X[3];

      // Set iflag negative to terminate execution for any reason
      IFLAG = 0;

      return;

    }

  }

  /**
   * C05MBJ Example Main Program
   */
  public static void main(String[] args) throws NAGBadIntegerException {

    C05MB c05mb = new C05MB();
    DNRM2 dnrm2 = new DNRM2();
    X02AJ x02aj = new X02AJ();
    FCN fcn = new FCN();
    long cpuser;
    double atol, cndtol, fnorm, machpr, rtol;
    long astart, ifail, m;
	int i;
    double[] fvec, x;
    double[] ruser = new double[1];
    long[] iuser = new long[1];

    System.out.println("C05MBJ Example Program Results\n");

    // Initialise NAG for Java
    Routine.init();

    // Get machine precision from X02AJ
    machpr = (new X02AJ()).eval();

    fvec = new double[n];
    x = new double[n];

    // The following starting values provide a rough solution
    x = new double[]{2.0, 0.5, 2.0, 0.5};

    m = 2;
    atol = Math.sqrt(machpr);
    rtol = Math.sqrt(machpr);
    cndtol = 0.0;
    astart = 0;
    cpuser = 0;

    ifail = -1;
    c05mb.eval(fcn, n, x, fvec, atol, rtol, m, cndtol, astart, iuser, ruser,
        cpuser, ifail);
    ifail = c05mb.getIFAIL();

    if (ifail==0 || ifail==8 || ifail==9) {
      if (ifail==0) {
        // The NAG name equivalent of dnrm2 is f06ej
        fnorm = dnrm2.eval(n, fvec, 1);
        System.out.println();
        System.out.printf("Final 2-norm of the residuals = %12.4e\n", fnorm);
        System.out.println();
        System.out.println("Final approximate solution");
      }
      else {
        System.out.println();
        System.out.println("Approximate solution");
      }
      System.out.println();
      for (i = 1; i <= n; i++) {
        System.out.printf("%12.4f", x[i-1]);
      }
      System.out.printf("\n");
    }

  }

}
