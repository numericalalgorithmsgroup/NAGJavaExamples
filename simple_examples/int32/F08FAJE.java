import com.nag.routines.F08.DDISNA;
import com.nag.routines.F08.F08FA;
import com.nag.routines.F16.F16JQ;
import com.nag.routines.X02.X02AJ;
import com.nag.routines.X04.X04CA;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * F08FA example program text.
 * @author ludovic
 */
public class F08FAJE {

  public static void main(String[] args) {
    int n = 4, LWork = n*64, lda = n;
    double[] a, w, work,zerrbd,rcondz, colVector;
    int info = 0, ifail = 0, k = 0;
    a = new double[n * n];
    w = new double[n];
    work = new double[LWork];
    zerrbd = new double[n];
    rcondz = new double[n];
    colVector = new double[n];

    System.out.println(" F08FAJ Example Program Results\n");

    //array a : Stored in column major way

    a[0] = 1.0;
    a[1] = 2.0;
    a[2] = 3.0;
    a[3] = 4.0;

    a[4] = 2.0;
    a[5] = 2.0;
    a[6] = 3.0;
    a[7] = 4.0;

    a[8] = 3.0;
    a[9] = 3.0;
    a[10] = 3.0;
    a[11] = 4.0;

    a[12] = 4.0;
    a[13] = 4.0;
    a[14] = 4.0;
    a[15] = 4.0;

    //initializing the info parameter
    info = 0;
    w[0] = Double.NaN;
    w[1] = Double.NaN;
    w[2] = Double.NaN;
    w[3] = Double.NaN;
    F08FA f08fa = new F08FA("V", "U", n, a, n, w, work, LWork, info);
    f08fa.eval();
    info = f08fa.getINFO();

    if (info == 0) {
      System.out.println(" Eigenvalues");
      System.out.print("   ");
      for (int i = 0; i < n; ++i) {
        System.out.printf(" %8.4f",w[i]);
      }
      System.out.println();

      // Normalize the eigenvectors: largest element positive
      F16JQ f16jq = new F16JQ(); // aka blas_damax_val
      for (int i = 0; i < n; i++) {
        System.arraycopy(a, 4*i, colVector, 0, 4);  // Form vector to evaluate
        f16jq.eval(n, colVector, 1, k, 0.0);        // Get index of largest (absolute) value
        k = f16jq.getK() - 1;                       // Make index zero based

        // Invert sign of column if largest element is negative
        if (a[(4*i)+k] < 0) {
          for (int j = 0; j < n; j++) {
            a[(4*i)+j] = (-1) * a[(4*i)+j];
          }
        }

      }

      (new X04CA()).eval("General"," ",n,n,a,lda,"Eigenvectors",ifail);

      double eps = (new X02AJ()).eval();
      double eerrbd = eps*Math.max(Math.abs(w[0]),Math.abs(w[n-1]));

      (new DDISNA()).eval("Eigenvectors",n,n,w,rcondz,info);
      for (int i = 0; i < n; ++i) {
        zerrbd[i] = eerrbd/rcondz[i];
      }

      System.out.println();
      System.out.println(" Error estimate for the eigenvalues");
      System.out.printf("    %11.1e\n",eerrbd);
      System.out.println();
      System.out.println(" Error estimates for the eigenvectors");
      for (int i = 0; i < n; ++i) {
        System.out.printf(" %11.1e",zerrbd[i]);
      }
      System.out.println();

    }
    else {
      System.out.printf(" Failure in DSYEV. INFO =%4d",info);
    }

  }

}
