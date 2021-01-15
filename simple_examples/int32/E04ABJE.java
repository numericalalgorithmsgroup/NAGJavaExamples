import com.nag.routines.E04.E04AB;

/**
 * E04ABJ example program text
 * @author willa
 * @since 27.1.0.0
 */
public class E04ABJE{

  /**
   * E04ABJE main program
   */
  public static void main(String[] args){
    double a, b, e1, e2, f = 0, x = 0; //placeholders
    int ifail, maxcal;
    double[] ruser;
    int[] iuser;

    ruser = new double[1];
    iuser = new int[1];

    System.out.println("E04ABJ Example Program Results");

    //e1 and e2 are set to zero so that E04ABA will reset them to their default values

    e1 = 0;
    e2 = 0;

    //The minimum is known to lie in the range (3.5, 5.0)

    a = 3.5;
    b = 5.0;

    //Allow 30 calls of FUNCT

    maxcal = 30;

    ifail = -1;
    funct funct1 = new funct();
    E04AB e04ab = new E04AB(funct1, e1, e2, a, b, maxcal, x, f, iuser, ruser, ifail);
    e04ab.eval();

    //update
    ifail = e04ab.getIFAIL();
    a = e04ab.getA();
    b = e04ab.getB();
    x = e04ab.getX();
    f = e04ab.getF();
    maxcal = e04ab.getMAXCAL();

    switch(ifail){
      case 0:
        System.out.println();
        System.out.printf("The minimum lies in the interval %.8f to %.8f\n", a, b);
        System.out.printf("Its estimated position is %.8f,\n", x);
        System.out.printf("where the function value is %.4f\n", f);
        System.out.printf("%d function evaluations were required\n", maxcal);
        break;
      case 2:
        System.out.println();
        System.out.printf("The minimum lies in the interval %.8f to %.8f\n", a, b);
        System.out.printf("Its estimated position is %.8f,\n", x);
        System.out.printf("where the function value is %.4f\n", f);
        System.out.printf("%d function evaluations were required\n", maxcal);
        break;
      default:
        break;
    }
  }

  public static class funct extends E04AB.Abstract_E04AB_FUNCT{
    public void eval(){
      FC = Math.sin(this.XC) / this.XC;
    }
  }
}
