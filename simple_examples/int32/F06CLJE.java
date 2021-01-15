import com.nag.routines.F06.F06CL;
import com.nag.routines.Routine;
import com.nag.types.NAGComplex;

/**
 * F06CL example program text.
 * @author ludovic
 */
public class F06CLJE {

  public static void main(String[] args)  {
    boolean fail = false;
    NAGComplex z1 = new NAGComplex();
    NAGComplex z2 = new NAGComplex();
    NAGComplex z3 = new NAGComplex();

    System.out.println(" F06CLJ Example Program Results");
    System.out.println();

    Routine.complex = z1;

    z1.setRe(1.0);
    z1.setIm(1.0);
    z2.setRe(2.0);
    z2.setIm(2.0);

    F06CL f06cl = new F06CL(z1,z2,fail);
    z3 = (NAGComplex)f06cl.eval();
    fail = f06cl.getFAIL();

    if (fail) {
      System.err.println(" Something went wrong...");
    }
    else {
      System.out.println(" " + z1.toString() + "/"+ z2.toString() + " = " + z3.toString());
    }

  }

}
