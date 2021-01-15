import com.nag.routines.S.S10AC;

/**
 * S10ACJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class S10ACJE{

   /**
   * S10ACJ example main program
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
    
    System.out.println("S10ACJ Example Program Results");

    System.out.println();
    System.out.printf("\tX\t\tY");
    System.out.println();

    for(double x : xVal){
      ifail = -1;
      S10AC s10ac = new S10AC(x, ifail);
      y = s10ac.eval();
      ifail = s10ac.getIFAIL();

      System.out.printf("\t%.3e\t%.3e\n", x, y);
    }
  }
}
