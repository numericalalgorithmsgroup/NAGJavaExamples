import com.nag.routines.F01.F01AD;
import com.nag.routines.X04.X04CA;


/**
 * F01AD example program text.
 * @author joed
 */
public class F01ADJE {

  public static void main(String[] args) {

    int i, ifail, lda, n;
    double[] a;

    System.out.println(" F01ADJ Example Program Results");
    System.out.println();

    n = 4;
    lda = n + 1;
    a = new double[] {
      5.0,  7.0,  6.0,  5.0,  0.0,
      7.0, 10.0,  8.0,  7.0,  0.0,
      6.0,  8.0, 10.0,  9.0,  0.0,
      5.0,  7.0,  9.0, 10.0,  0.0
    };

    F01AD f01ad = new F01AD();
    ifail = -1;
    f01ad.eval(n, a, lda, ifail);
    ifail = f01ad.getIFAIL();

    /* Print the result matrix A */
    X04CA x04ca = new X04CA();
    x04ca.eval("L", "B", lda, n, a, lda, "Lower triangle of inverse", ifail);

  }

}
