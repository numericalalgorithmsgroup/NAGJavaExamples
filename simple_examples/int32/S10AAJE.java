import com.nag.routines.S.S10AA;

/**
 * S10AAJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class S10AAJE{

  /**
   * S10AAJ example main program
   */
  public static void main(String[] args){
    double y;
    double[] xVal;
    int ifail;

    //x values (can be read in from data file)
    xVal = new double[4];
    xVal[0] = -20;
    xVal[1] = -5.0;
    xVal[2] = 0.5;
    xVal[3] = 5.0;
    
    System.out.println("S10AAJ Example Program Results");

    System.out.println();
    System.out.printf("\tX\t\tY");
    System.out.println();

    for(double x : xVal){
      ifail = -1;
      S10AA s10aa = new S10AA(x, ifail);
      y = s10aa.eval();
      ifail = s10aa.getIFAIL();

      System.out.printf("\t%.3e\t%.3e\n", x, y);
    }
  }
}
