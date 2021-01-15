import com.nag.routines.C05.C05AU;

/**
 * C05AUJ example program text.
 * @author willa
 * @since 27.1.0.0
 */ 
public class C05AUJE{

  /**
   * C05AUJ main program
   */
  public static void main(String[] args){
    double a = 0, b = 0, eps, eta, h, x; //placeholders
    int ifail;
    double[] ruser;
    int[] iuser;

    ruser = new double[1]; //need to initialise before passing to wrapper
    iuser = new int[1];

    System.out.println("C05AUJ Example Program Results");

    x = 1;
    h = 0.1;
    eps = 0.00001;
    eta = 0;

    //Instance of class f to pass to wrapper
    f f1 = new f(); 
    
    ifail = -1;
    C05AU c05au = new C05AU(x, h, eps, eta, f1, a, b, iuser, ruser, ifail);
    c05au.eval();

    //update values
    x = c05au.getX();
    a = c05au.getA();
    b = c05au.getB();
    ifail = c05au.getIFAIL();
    
    System.out.println();
    
    switch(ifail){
      case 0:
        System.out.printf("Root is : \t%.5f\n", x);
        System.out.printf("Interval searched is : \t[ %.5f, %.5f]\n", a, b);
        break;
      case 3:
      case 4:
        System.out.printf("Final value = %.5f\n", x);
        break;
    }
  }

  /**
   * Extends abstract class C05AU.Abstract_C05AU_F. eval() returns the value of the function at a given x.
   * eval() must be implemented by the user. 
   * @return x - e^x
   */ 
  public static class f extends C05AU.Abstract_C05AU_F{
    public double eval(){
      return(this.X - Math.exp(-this.X));
    }
  }
}

