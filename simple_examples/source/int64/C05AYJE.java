import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.C05.C05AY;
import com.nag.routines.Routine;


public class C05AYJE {

  public static void main(String[] args) throws NAGBadIntegerException {

      C05AY c05ay = new C05AY();
      OBJFUN f = new OBJFUN();
      double a = 0.0, b = 1.0, eps = 1.0e-5, eta = 0.0, x = 0.0;
      long ifail = 0;
      long[] iuser = new long[1];
      double[] ruser = new double[1];

      System.out.println("C05AYJ Example Program Results");
      System.out.println();

      Routine.init();
      c05ay.eval(a, b, eps, eta, f, x, iuser, ruser, ifail);
      x = c05ay.getX();

      switch ((int)ifail) {
        case 0:
          System.out.printf("Zero at x = %12.5f\n", x);
          break;
        case 2: case 3:
          System.out.printf("Final point = %12.5f\n", x);
          break;
        default:
          System.out.println("Unexpected ifail = " + ifail);
      }

  }

  private static class OBJFUN extends C05AY.Abstract_C05AY_F {

    public double eval() {
        return Math.exp(-X) - X;
    }

  }

}
