import com.nag.routines.F07.F07FB;
import com.nag.routines.X04.X04CA;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * F07FB example program text.
 * @author ludovic
 */
public class F07FBJE {

  public static void main(String[] args) {
    String fact,uplo,equed;
    int n, nrhs,lda,ldaf,ldb,ldx;
    int[] iwork;
    double rcond;
    double[] a,af,s,b,x,ferr,berr,work;
    int info,ifail;
    lda = 4;
    ldaf = lda;
    ldb = lda;
    ldx = lda;
    nrhs = 2;
    n = 4;
    a = new double[lda*lda];
    af = new double[ldaf*ldaf];
    b = new double[ldb*nrhs];
    berr = new double[nrhs];
    ferr = new double[nrhs];
    s = new double[lda];
    work = new double[3*lda];
    x = new double[ldx*nrhs];
    iwork = new int[lda];
    info = 1;
    rcond = Double.NaN;
    fact = "E";
    uplo = "U";
    equed = "Z";//dummy value
    /*A = 4.16  -3.12   0.56  -0.10
      5.03  -0.83   1.18
      0.76   0.34
      1.18*/
    a[0] = 4.16;

    a[4] = -3.12;
    a[5] =  5.03;

    a[8] = 0.56;
    a[9] = -0.83;
    a[10] = 0.76;

    a[12] = -0.10;
    a[13] = 1.18;
    a[14] = 0.34;
    a[15] = 1.18;

    /*B=  8.70  8.30
      -13.35  2.13
      1.89  1.61
      - 4.14  5.00*/
    b[0] = 8.70;
    b[1] = -13.35;
    b[2] = 1.89;
    b[3] = -4.14;

    b[4] = 8.30;
    b[5] = 2.13;
    b[6] = 1.61;
    b[7] = 5.00;

    System.out.println(" F07FBJ Example Program Results");
    System.out.println();

    F07FB f07fb = new F07FB(fact, uplo, n, nrhs, a, lda, af, ldaf, equed, s, b,
        ldb, x, ldx, rcond, ferr, berr, work, iwork, info);
    f07fb.eval();
    fact = f07fb.getFACT();
    uplo = f07fb.getUPLO();
    equed = f07fb.getEQUED();
    rcond = f07fb.getRCOND();
    info = f07fb.getINFO();


    if ((info == 0) || (info == n+1)) {
      ifail = 0;
      (new X04CA()).eval("General"," ",n,nrhs,x,ldx,"Solution(s)",ifail);
      System.out.println();
      System.out.println(" Backward errors (machine-dependent)");
      System.out.print("   ");
      for (int ii = 0; ii < nrhs; ++ii) {
        System.out.printf(" %11.1e",berr[ii]);
      }
      System.out.println("\n");
      System.out.println(" Estimated forward error bounds (machine-dependent)");
      System.out.print("   ");
      for (int ii = 0; ii < nrhs; ++ii) {
        System.out.printf(" %11.1e",ferr[ii]);
      }
      System.out.println("\n");
      System.out.println(" Estimate of reciprocal condition number");
      System.out.printf("   %11.1e\n",rcond);
      System.out.println();
      if (equed.equalsIgnoreCase("N")) {
        System.out.println(" A has not been equilibrated");
      }
      else if (equed.equalsIgnoreCase("N")) {
        System.out.println(" A has been row and column scaled as diag(S)*A*diag(S)");
      }

      if (info == n+1) {
        System.out.println();
        System.out.println(" The matrix A is singular to working precision");
      }

    }
    else {
      System.out.printf(" The leading minor of order %3d is not positive definite\n");
    }

  }

}
