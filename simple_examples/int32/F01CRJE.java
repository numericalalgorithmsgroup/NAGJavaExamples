import com.nag.routines.F01.F01CR;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * F01CR example program text.
 * @author ludovic
 */
public class F01CRJE {

  public static void main(String[] args) {
    System.out.println(" F01CRJ Example Program Results");
    double[] a;
    int m,n,mn,lmove;
    int[] move;
    int ifail;
    n = 7;
    m = 3;
    mn = m*n;
    lmove = (m + n)/2;
    a = new double[mn];
    move = new int[lmove];
    ifail = 1;

    for (int i = 0; i < mn; i++) {
      a[i] = i+1.0;
    }

    F01CR f01cr = new F01CR(a, m, n, mn, move, lmove, ifail);
    f01cr.eval();

    System.out.println();
    int index = 0;
    for (int i = 0; i < mn; ++i) {
      System.out.printf(" %7.1f",a[i]);
      ++index;
      if (index == 7) {
        System.out.println();
        index = 0;
      }
    }
    System.out.println();

  }

}
