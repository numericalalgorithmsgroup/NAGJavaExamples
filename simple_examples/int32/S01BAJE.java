import com.nag.routines.S.S01BA;

/**
 * S01BAJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class S01BAJE{

  /**
   * S01BAJE main program
   */
  public static void main(String[] args){
    double y;
    int ifail;
    double[] xVal; //data

    //store data
    xVal = new double[6];
    xVal[0] = 2.5;
    xVal[1] = 0.125;
    xVal[2] = -0.906;
    xVal[3] = 0.00129;
    xVal[4] = -0.00000783;
    xVal[5] = 0.000000001;

    System.out.println("S01BAJ Example Program Results");
    System.out.println();
    System.out.printf("\tX\t\tY\n");

    for(double x : xVal){
      ifail = -1;
      S01BA s01ba = new S01BA(x, ifail);
      y = s01ba.eval();

      System.out.printf("\t%.4e\t%.4e\n", x, y);
    }
  }
}
