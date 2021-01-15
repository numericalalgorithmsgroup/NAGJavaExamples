import com.nag.routines.F07.F07AA;
import com.nag.routines.X04.X04CA;
import java.util.Arrays;

/**
 * F07AA example program text.
 * @author joed
 */
public class F07AAJE {

  public static void main(String[] args) {

    int i, ifail, info, lda, ldb, n;
    double[] a, b;
    int[] ipiv;

    System.out.println(" F07AAJ Example Program Results");
    System.out.println();

    n = 4;
    lda = n;
    ldb = n;

    a = new double[n*n];
    b = new double[n];
    ipiv = new int[n];

    a[0+0*n] =  1.80;
    a[0+1*n] =  2.88;
    a[0+2*n] =  2.05;
    a[0+3*n] = -0.89;
    a[1+0*n] =  5.25;
    a[1+1*n] = -2.95;
    a[1+2*n] = -0.95;
    a[1+3*n] = -3.80;
    a[2+0*n] =  1.58;
    a[2+1*n] = -2.69;
    a[2+2*n] = -2.90;
    a[2+3*n] = -1.04;
    a[3+0*n] = -1.11;
    a[3+1*n] = -0.66;
    a[3+2*n] = -0.59;
    a[3+3*n] =  0.80;

    b[0] = 9.52;
    b[1] = 24.35;
    b[2] = 0.77;
    b[3] = -6.22;

    /* Solve the equations Ax = b for x */

    F07AA f07aa = new F07AA();
    info = 0;
    f07aa.eval(n, 1, a, lda, ipiv, b, ldb, info);
    info = f07aa.getINFO();

    if (info == 0) {

      /* Print solution */

      System.out.println(" Solution");
      for (i = 0; i < n; i++) {
        System.out.printf("   %11.4f", b[i]);
      }
      System.out.printf("\n");

      /* Print details of factorization */

      System.out.println();
      X04CA x04ca = new X04CA();
      ifail = 0;
      x04ca.eval("General", " ", n, n, a, lda, "Details of factorization", ifail);

      /* Print pivot indices */

      System.out.println();
      System.out.println(" Pivot indices");
      for (i = 0; i < n; i++) {
        System.out.printf("   %11d", ipiv[i]);
      }
      System.out.printf("\n");

    }
    else {
      System.err.printf(" The (%3d,%3d) element of the factor U is zero\n", info, info);
    }

  }

}
