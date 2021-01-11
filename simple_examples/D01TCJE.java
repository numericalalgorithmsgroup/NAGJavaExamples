import com.nag.routines.D01.D01TC;
import java.util.Arrays;

/**
 * D01TC example program text.
 * @author Mo
 */
public class D01TCJE {

  public static void main(String[] args) {

    double a, b, c, d;
    double[] abscis, weight;
    int n, ifail, itype;

    /* Header */
    System.out.println(" D01TCJ Example Program Results");

    n = 7;
    a = 0.0;
    b = 1.0;
    c = 0.0;
    d = 0.0;
    itype = -3;

    abscis = new double[n];
    weight = new double[n];

    D01TC d01tc = new D01TC();
    ifail = 0;
    d01tc.eval(itype, a, b, c, d, n, weight, abscis, ifail);
    abscis= d01tc.getABSCIS();
    weight = d01tc.getWEIGHT();
    
    System.out.println();
    System.out.printf(" Laguerre formula, %2d points\n",n);
    System.out.println();
    System.out.println("      Abscissae              Weights\n");
    for (int j = 0; j < n; j++) { 
      System.out.printf("%15.5E      %15.5E\n", abscis[j], weight[j]);
    }

  }

}
