import com.nag.routines.S.S10AB;

/**
 * S10ABJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class S10ABJE{

  /**
   * S10ABJ example main program
   */
  public static void main(String[] args){
    double y;
    double[] xVal;
    int ifail;

    //x values (can be read in from data file)
    xVal = new double[5];
    xVal[0] = -10;
    xVal[1] = -0.5;
    xVal[2] = 0;
    xVal[3] = 0.5;
    xVal[4] = 25.0;
    
    System.out.println("S10ABJ Example Program Results");

    System.out.println();
    System.out.printf("\tX\t\tY");
    System.out.println();

    for(double x : xVal){
      ifail = -1;
      S10AB s10ab = new S10AB(x, ifail);
      y = s10ab.eval();
      ifail = s10ab.getIFAIL();

      System.out.printf("\t%.3e\t%.3e\n", x, y);
    }
  }
}
