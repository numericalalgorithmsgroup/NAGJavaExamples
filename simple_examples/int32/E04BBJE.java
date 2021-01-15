import com.nag.routines.E04.E04BB;

/**
 * E04BBJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class E04BBJE{

  /**
   * E04BBJE main program
   */
  public static void main(String[] args){
    double a, b, e1, e2, f = 0, g = 0, x = 0; //placeholders
    int ifail, maxcal;
    double[] ruser;
    int[] iuser;

    ruser = new double[1];
    iuser = new int[1];

    System.out.println("E04BBJ Example Program Results");

    //e1 and e2 are set to zero so that E04BBA will reset them to their default values
    
    e1 = 0;
    e2 = 0;

    //The minimum is known to lie in the range (3.5, 5.0)

    a = 3.5;
    b = 5.0;
    
    //Allow 30 calls of FUNCT

    maxcal = 30;

    ifail = -1;
    funct funct1 = new funct();
    E04BB e04bb = new E04BB(funct1, e1, e2, a, b, maxcal, x, f, g, iuser, ruser, ifail);
    e04bb.eval();

    //update
    a = e04bb.getA();
    b = e04bb.getB();
    maxcal = e04bb.getMAXCAL();
    x = e04bb.getX();
    f = e04bb.getF();
    g = e04bb.getG();
    ifail = e04bb.getIFAIL();

    switch(ifail){
      case 0:
        System.out.println();
        System.out.printf("The minimum lies in the interval %.8f to %.8f\n", a, b);
        System.out.printf("Its estimated position is %.8f,\n", x);
        System.out.printf("where the function value is %.4f\n", f);
        System.out.printf("and the gradient is %.1e (machine dependent)\n", g);
        System.out.printf("%d function evaluations were required\n", maxcal);
        break;
      case 2:
        System.out.println();
        System.out.printf("The minimum lies in the interval %.8f to %.8f\n", a, b);
        System.out.printf("Its estimated position is %.8f,\n", x);
        System.out.printf("where the function value is %.4f\n", f);
        System.out.printf("and the gradient is %.1e (machine dependent)\n", g);
        System.out.printf("%d function evaluations were required\n", maxcal);
        break;
      default:
        break;
    }
  }

  public static class funct extends E04BB.Abstract_E04BB_FUNCT{
    public void eval(){
      this.FC = Math.sin(this.XC) / this.XC;
      this.GC = (Math.cos(this.XC) - this.FC)/this.XC;
    }
  }
}
  
