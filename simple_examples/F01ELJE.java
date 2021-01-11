import com.nag.routines.F01.F01EL;
import com.nag.routines.Routine;
import com.nag.routines.X04.X04CA;
import com.nag.types.NAGComplex;
import com.nag.types.NAGComplexInterface;
import java.util.Arrays;

/**
 * F01EL example program text.
 * @author joed
 */
public class F01ELJE {

  public static void main(String[] args) {

    F01EL f01el = new F01EL();
    X04CA x04ca = new X04CA();

    FCos2 fcos2 = new FCos2();
    double imnorm;
    int i, ifail, iflag, lda, n;
    double[] a, ruser = new double[1];
    int[] iuser = new int[1];

    Routine.setComplex(new NAGComplex());

    System.out.println(" F01ELJ Example Program Results");
    System.out.println();

    // Problem data
    n = 4;
    a = new double[] {
      3.0, -1.0, 0.0, 2.0, 0.0, 1.0, 2.0, 1.0, 1.0, 3.0, 2.0, -1.0, 2.0, 1.0, 1.0, 1.0
    };

    // Find f(A)
    lda = n;
    iflag = 0;
    imnorm = 0;
    ifail = 0;
    f01el.eval(n, a, lda, fcos2, iuser, ruser, iflag, imnorm, ifail);

    // Print solution
    ifail = 0;
    x04ca.eval("G", "N", n, n, a, lda, "F(A) = COS(2A)", ifail);

    // Print the norm of the imaginary part to check it is small
    System.out.println();
    System.out.println(" Imnorm = " + imnorm);

  }

  public static class FCos2 extends F01EL.Abstract_F01EL_F {

    /* These methods should really be part of an extension of NAGComplex. */

    private NAGComplex complexCos(NAGComplexInterface z) {
      NAGComplex cosz = new NAGComplex();
      cosz.setRe(Math.cos(z.getRe()) * Math.cosh(z.getIm()));
      cosz.setIm(-Math.sin(z.getRe()) * Math.sinh(z.getIm()));
      return cosz;
    }

    private NAGComplex complexMult(NAGComplexInterface a, NAGComplexInterface b) {
      NAGComplex ab = new NAGComplex();
      ab.setRe((a.getRe()*b.getRe()) - (a.getIm()*b.getIm()));
      ab.setIm((a.getRe()*b.getIm()) + (a.getIm()*b.getRe()));
      return ab;
    }

    /* Calculate F(A) = COS(2A) */

    public void eval() {

      NAGComplex two = new NAGComplex();
      two.setRe(2.0);
      two.setIm(0.0);

      for (int i = 0; i < this.NZ; i++) {
        this.FZ[i] = (NAGComplex) complexCos(complexMult(two, this.Z[i]));
      }

      this.IFLAG = 0;

    }

  }

}
