import com.nag.routines.F01.F01CK;
import com.nag.routines.X04.X04CB;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * F01CK example program text.
 * @author ludovic
 */
public class F01CKJE {

  public static void main(String[] args) {

    double[] a, b, c, z;
    int n, p, m, iz, opt;
    int ifail;

    m = 3;
    n = p = 2;
    iz = 1;
    opt = 1;
    a = new double[n * p];
    b = new double[n * m];
    c = new double[m * p];
    z = new double[iz];
    ifail = 0;

    System.out.println(" F01CKJ Example Program Results");

    // Initialising b and c

    for (int i = 0; i < m; ++i) {
      for (int j = 0; j < n; ++j) {
        b[j+i*n] = (double) (i+j);
      }
      for (int j = 0; j < p; ++j) {
        c[i+j*m] = (double) (i+j);
      }
    }

    F01CK f01ck = new F01CK(a, b, c, n, p, m, z, iz, opt, ifail);
    f01ck.eval();
    ifail = f01ck.getIFAIL();
    String title = "Matrix A";
    System.out.println();
    System.out.flush();
    ifail = 0;

    String matrix = "G", diag = "N", nolabel = "N", form = "F7.1";
    String[] dummy = {" "};
    int ncols = 80;
    int indent = 0;

    (new X04CB()).eval(matrix,diag,n,p,a,n,form,title,nolabel,dummy,nolabel,
        dummy,ncols,indent, ifail);

  }

}
