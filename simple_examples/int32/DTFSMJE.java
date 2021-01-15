import com.nag.routines.F01.DTRTTF;
import com.nag.routines.F06.DTFSM;
import com.nag.routines.X04.X04CA;

/**
 * DTFSM example program text. Adapted from f06wbfe.f90
 * @author joed
 */
public class DTFSMJE {

  public static void main(String[] args) {

    double alpha = 4.21;
    int ifail = 0, info = 0, m = 6, n = 4;
    String side = "L", trans = "N", transr = "N", uplo = "L";
    DTFSM dtfsm = new DTFSM();
    DTRTTF dtrttf = new DTRTTF();
    X04CA x04ca = new X04CA();

    System.out.println(" DTFSMJ Example Program Results\n");

    // Set lower triangle of matrix A
    double[] a = new double[m*m];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < m; j++) {
        a[i*m+j] = (j >= i) ? j+1 : 0;
      }
    }

    // Set matrix B
    double[] b = new double[] {
      3.22, 1.64, 1.87, 5.20, 1.83, -1.10,
      1.37, 1.80, 2.87, -2.99, -2.71, -0.63,
      2.31, 0.38, 2.02, -0.91, -2.81, -0.50,
      0.29, -1.52, -0.80, -3.87, -1.13, 0.81
    };

    // Convert A to rectangular full packed storage in ar
    double[] ar = new double[(m*(m+1))/2];
    info = 0;
    dtrttf.eval(transr, uplo, m, a, m, ar, info);
    info = dtrttf.getINFO();

    // Perform the matrix-matrix operation
    dtfsm.eval(transr, side, uplo, trans, "N", m, n, alpha, ar, b, m);

    // Print result
    ifail = 0;
    x04ca.eval("General", " ", m, n, b, m, "The Solution", ifail);
    ifail = x04ca.getIFAIL();

  }

}
