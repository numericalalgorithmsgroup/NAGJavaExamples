import com.nag.routines.G01.G01AL;

/**
 * G01ALJ Example Program Text
 * @author willa
 * @since 27.1.0.0
 */
public class G01ALJE{

  /**
   * G01ALJ Example main program
   */
  public static void main(String[] args){
    int ifail, n;
    double[] x, res;
    int[] iwrk;

    res = new double[5];

    System.out.println("G01ALJ Example Program Results");
    System.out.println();

    //Problem size
    n = 12;

    //Allocate
    x = new double[n];
    iwrk = new int[n];

    //Data
    x[0] = 12;
    x[1] = 9;
    x[2] = 2;
    x[3] = 5;
    x[4] = 6;
    x[5] = 8;
    x[6] = 2;
    x[7] = 7;
    x[8] = 3;
    x[9] = 1;
    x[10] = 11;
    x[11] = 10;

    //Calculate summary statistics
    ifail = 0;
    G01AL g01al = new G01AL(n, x, iwrk, res, ifail);
    g01al.eval();

    System.out.printf("Maximum                      %.4f\n", res[4]);
    System.out.printf("Upper Hinge (75%% quantile)   %.4f\n", res[3]);
    System.out.printf("Median      (50%% quantile)   %.4f\n", res[2]);
    System.out.printf("Lower Hinge (25%% quantile)   %.4f\n", res[1]);
    System.out.printf("Minimum                      %.4f\n", res[0]);
  }
}
