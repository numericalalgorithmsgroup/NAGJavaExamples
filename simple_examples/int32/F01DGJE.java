import com.nag.routines.F01.F01DG;
import com.nag.routines.X04.X04CA;

/**
 * F01DG example program text.
 * @author joed
 * @since 27.0.0.0
 */
public class F01DGJE {

  /**
   * F01DG example main program.
   */
  public static void main(String[] args) {

    F01DG f01dg = new F01DG();
    X04CA x04ca = new X04CA();
    double alpha;
    int i, ifail, lda, ldb, n;
    String side, transa, uplo; // Length 1
    double[] a, b;

    System.out.println("F01DGJ Example Program Results\n");

    // Values for side, uplo and transa
    side = "L";
    uplo = "U";
    transa = "T";

    // Order of square matrices
    n = 4;
    lda = n;
    ldb = n;

    // Scaling constant alpha
    alpha = 0.4;

    // Set input matrices (column-major order)
    a = new double[]{
      1.5, 0.0, 0.0, 0.0,
      2.3, 3.4, 0.0, 0.0,
      6.7, 5.4, 8.1, 0.0,
      1.9, 8.6, 2.0, 5.9
    };
    b = new double[]{
      3.5, 0.0, 0.0, 0.0,
      2.1, 5.6, 0.0, 0.0,
      4.0, 2.1, 1.7, 0.0,
      2.1, 2.5, 1.1, 7.4
    };

    /* ifail: behaviour on error exit
     *       = 0 for hard exit, =1 for quiet-soft, =-1 for noisy-soft */
    ifail = 0;

    // Find B=alpha*A*B
    f01dg.eval(side, uplo, transa, n, alpha, a, lda, b, ldb, ifail);
    ifail = f01dg.getIFAIL();

    // Print the solution
    if (ifail == 0) {
      if (transa.equals("N")) {
        x04ca.eval(uplo, "N", n, n, b, n, "Solution matrix B", ifail);
      }
      else {
        x04ca.eval("G", "N", n, n, b, n, "Solution matrix B", ifail);
      }
    }

  }

}
