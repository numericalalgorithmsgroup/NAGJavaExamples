import com.nag.routines.F01.F01EM;
import com.nag.routines.X04.X04CA;
import com.nag.routines.Routine;
import com.nag.types.NAGComplex;

/**
 * F01EMJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class F01EMJE{

  /**
   * F01EMJ Example main program
   */
  public static void main(String[] args){
    double imnorm = 0;
    int ifail, iflag = 0, lda, n; //placeholders
    double[] a, ruser;
    int[] iuser;

    ruser = new double[1];
    iuser = new int[1];

    System.out.println("F01EMJ Example Program Results");
    System.out.println();

    //Tell wrapper what type of complex type is going to be used
    Routine.setComplex(new NAGComplex());

    //Problem size
    n = 4;
    lda = n;

    //Allocate
    a = new double[lda * n];

    //Data
    //A = ( 1, 0,-2, 1)
    //    (-1, 2, 0, 1)
    //    ( 2, 0, 1, 0)
    //    ( 1, 0,-2, 2)
    a[0] = 1;
    a[1] = -1;
    a[2] = 2;
    a[3] = 1;
    a[4] = 0;
    a[5] = 2;
    a[6] = 0;
    a[7] = 0;
    a[8] = -2;
    a[9] = 0;
    a[10] = 1;
    a[11] = -1;
    a[12] = 1;
    a[13] = 1;
    a[14] = 0;
    a[15] = 2;

    //Find f(A)
    ifail = 0;
    //Create fexp2 object to pass to wrapper
    fexp2 f = new fexp2();
    F01EM f01em = new F01EM(n, a, lda, f, iuser, ruser, iflag, imnorm, ifail);
    f01em.eval();

    //Print solution
    ifail = 0;
    X04CA x04ca = new X04CA("G", "N", n, n, a, lda, "F(A) = EXP(2A)", ifail);
    x04ca.eval();

    //Print the norm oof the imaginary part to check if it small
    System.out.println();
    System.out.printf("Imnorm = %.2f\n", imnorm);
  }

  /**
   * fexp2 class representing f routine argument
   */
  public static class fexp2 extends F01EM.Abstract_F01EM_F{
    public void eval(){
      NAGComplex two = new NAGComplex(2, 0);
      NAGComplex twoPowM = new NAGComplex(2, 0);

      twoPowM.setRe(Math.pow(2, this.M));
          
      for(int i = 0; i < this.NZ; i++){
        this.FZ[i] = NAGComplex.multiply(twoPowM, this.complexExp(NAGComplex.multiply(two, (NAGComplex) this.Z[i])));
      }

      //Set iflag nonzero to terminate exectuion for any reason
      this.IFLAG = 0;
    }

    //Raises e ^ z where z is a complex number
    //Uses eulers formula;
    //c ^ (a + bi) = c^a * ((cos(bln(c))) + isin(bln(c)))
    public NAGComplex complexExp(NAGComplex x){
      NAGComplex tmp = new NAGComplex();
      tmp.setRe(Math.cos(x.getIm()));
      tmp.setIm(Math.sin(x.getIm()));
      NAGComplex ans = NAGComplex.multiply(new NAGComplex(Math.exp(x.getRe()), 0), tmp); 
      return ans;
    }
  }
}
