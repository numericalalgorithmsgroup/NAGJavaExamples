import com.nag.routines.G01.G01EW;
import com.nag.routines.G13.G13AW;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * G13AW example program text.
 */
public class G13AWJE {

  public static void main(String[] args) {

    int n = 30, type = 1, p = 1, ifail = 1, method = 1, nsamp = 0;
    double[] y = {-217.,-177.,-166.,-136.,-110.,-95.,-64.,-37.,-14.,-25.,-51.,
                  -62.,-73.,-88.,-113.,-120.,-83.,-33.,-19.,21.,17.,44.,44.,78.,
                  88.,122.,126.,114.,85.,64};
    int[] state = new int[1];
    G13AW g13aw = new G13AW(type, p, n, y, ifail);
    System.out.println(" G13AWJ Example Program Results\n");
    double ts = g13aw.eval();
    G01EW g01ew = new G01EW(method,type,n,ts,nsamp,state,ifail);
    double pvalue = g01ew.eval();
    ifail = g01ew.getIFAIL();
    if (ifail == 0 || ifail == 201) {
      System.out.printf("Dickey-Fuller test statistic     = %6.3f\n", ts);
      System.out.printf("associated p-value               = %6.3f\n", pvalue);
    }

  }

}
