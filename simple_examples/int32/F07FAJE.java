import com.nag.routines.F07.F07FA;
import com.nag.routines.X04.X04CA;

/**
 * F07FA example program text.
 * @author saraht
 */
public class F07FAJE {

  public static void main(String[] args) {

    int i, ifail, info, lda, n;
    double[] a, b;

    System.out.println(" F07FAJ Example Program Results");
    System.out.println();

    n = 4;
    lda = n;

    a = new double[n*n];
    b = new double[n];

    /* A =   4.16  -3.12   0.56  -0.10
                    5.03  -0.83   1.18
                           0.76   0.34
			          1.18    */

    a[0+0*n] =  4.16;
    a[0+1*n] = -3.12;
    a[0+2*n] =  0.56;
    a[0+3*n] = -0.10;

    a[1+1*n] =  5.03;
    a[1+2*n] = -0.83;
    a[1+3*n] =  1.18;

    a[2+2*n] =  0.76;
    a[2+3*n] =  0.34;

    a[3+3*n] =  1.18;

    b[0] =   8.70;
    b[1] = -13.35;
    b[2] =   1.89;
    b[3] =  -4.14;

    /* Solve the equations Ax = b for x */

    F07FA f07fa = new F07FA();
    info = 0;
    f07fa.eval("Upper", n, 1, a, lda, b, n, info);
    info = f07fa.getINFO();

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
      x04ca.eval("Upper", "Non-unit diagonal", n, n, a, lda, "Cholesky factor U", ifail);

    }
    else {
      System.err.printf(" The leading minor of order %d is not positive definite \n", info);
    }

  }

}
