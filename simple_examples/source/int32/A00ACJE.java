import com.nag.routines.A00.A00AC;

/**
 * A00AC example program text.
 * @author Mo
 */
public class A00ACJE {

  public static void main(String[] args) {
    A00AC a00ac = new A00AC();
    boolean lmok;

    System.out.println(" A00ACJ Example Program Results\n");
    System.out.println();

    a00ac.eval();

    lmok = a00ac.eval();

    if (lmok) {
      System.out.println("A valid licence key is available");
    }
    else {
      System.out.println("No valid licence key was found");
    }

  }

}
