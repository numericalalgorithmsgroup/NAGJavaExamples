import com.nag.routines.G02.G02AK;
import com.nag.routines.X04.X04CA;

/**
 * G02AK example program text.
 * @author joed
 * @since 27.0.0.0
 */
public class G02AKJE {

  /**
   * G02AKJ example main program.
   */
  public static void main(String[] args) {

    G02AK g02ak = new G02AK();
    X04CA x04ca = new X04CA();
    double errtol, f, rankerr, ranktol;
    int i, ifail, ldg, ldx, maxit, maxits, n, nsub, rank;
    double[] g, x;

    System.out.println("G02AKJ Example Program Results\n");

    // Problem size
    n = 4;
    ldg = n;
    ldx = n;

    // Rank constraint
    rank = 2;

    // Matrix G (column-major)
    g = new double[]{
      2.0, -1.0,  0.0,  0.0,
      -1.0,  2.0, -1.0,  0.0,
      0.0, -1.0,  2.0, -1.0,
      0.0,  0.0, -1.0,  2.0
    };

    // Use the defaults for errtol, ranktol, maxits, maxit
    errtol = 0.0;
    ranktol = 0.0;
    maxits = 0;
    maxit = 0;

    // Calculate rank constrained nearest correlation matrix
    ifail = 0;
    x = new double[ldx*n];
    f = 0.0;
    rankerr = 0.0;
    nsub = 0;
    g02ak.eval(g, ldg, n, rank, errtol, ranktol, maxits, maxit, x, ldx, f,
        rankerr, nsub, ifail);
    nsub = g02ak.getNSUB();
    f = g02ak.getF();
    rankerr = g02ak.getRANKERR();

    // Display results
    ifail = 0;
    x04ca.eval("General", " ", n, n, x, ldx, "NCM with rank constraint", ifail);
    System.out.println();
    System.out.printf("Number of subproblems solved: %12d\n\n", nsub);
    System.out.printf("Squared Frobenius norm of difference: %9.4f\n\n", f);
    System.out.printf("Rank error: %35.4f\n", rankerr);

  }

}
