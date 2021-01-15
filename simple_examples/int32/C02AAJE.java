import com.nag.routines.C02.C02AA;
import com.nag.routines.Routine;
import com.nag.routines.X02.X02AJ;
import com.nag.routines.X02.X02AL;
import com.nag.types.NAGComplex;
import java.util.Arrays;

/**
 * C02AA example program text.
 * @author joed
 * @since 27.1.0.0
 */
public class C02AAJE {

  public static void main(String[] args) {

    System.out.println(" C02AAJ Example Program Results");

    Routine.setComplex(new NAGComplex());
    exampleBasic();
    examplePolishing();

    System.out.println();

  }

  /** Demonstrate a basic problem. */
  public static void exampleBasic() {

    C02AA c02aa = new C02AA();
    int ifail, itmax, n, polish;
    NAGComplex[] a, z;
    double[] berr, cond;
    int[] conv;

    System.out.println();
    System.out.println("Example 1: Basic Problem");
    System.out.println();

    // Set polynomial degree and instantiate arrays
    n = 5;
    a = new NAGComplex[n+1];
    berr = new double[n];
    cond = new double[n];
    conv = new int[n];
    z = NAGComplex.createArray(n);

    // Set polynomial coefficients
    a[0] = new NAGComplex(5.0, 6.0);
    a[1] = new NAGComplex(30.0, 20.0);
    a[2] = new NAGComplex(-0.2, -6.0);
    a[3] = new NAGComplex(50.0, 100000.0);
    a[4] = new NAGComplex(-2.0, 40.0);
    a[5] = new NAGComplex(10.0, 1.0);

    // Find roots of the polynomial
    itmax = 30;
    polish = 1;
    ifail = 0;
    c02aa.eval(a, n, itmax, polish, z, berr, cond, conv, ifail);

    // Print output
    System.out.println("  i   z                    conv  berr      cond     ");
    System.out.println(" ---------------------------------------------------");
    for (int i = 0; i < n; i++) {
      System.out.printf(" %2d  %9.2E, %9.2E  %3d  %9.2E %9.2E\n", i+1,
          z[i].getRe(), z[i].getIm(), conv[i], berr[i], cond[i]);
    }

  }

  /** Compare polishing processes on a Wilkinson-style polynomial. */
  public static void examplePolishing() {

    C02AA c02aa = new C02AA();
    X02AJ x02aj = new X02AJ();
    X02AL x02al = new X02AL();
    NAGComplex pz;
    double delta, eps, err, fwderr, maxfwderr, maxrelerr, relerr, rmax;
    int ifail, itmax, k, n, polish;
    NAGComplex[] a, z, zact;
    double[] berr, cond;
    int[] conv;
    boolean[] matched;

    System.out.println();
    System.out.println("Example 2: Polishing Processes");
    System.out.println();

    // Set polynomial degree and instantiate
    n = 10;
    a = new NAGComplex[n+1];
    berr = new double[n];
    cond = new double[n];
    conv = new int[n];
    matched = new boolean[n];
    z = new NAGComplex[n];
    zact = new NAGComplex[n];

    // Set known roots and (instantiate z)
    for (int i = 0; i < n; i++) {
      zact[i] = new NAGComplex((double) i+1, 0.0);
      z[i] = new NAGComplex();
    }

    // Multiply out (z-1)(z-2)...(z-n) for coefficients
    for (int i = 0; i < n; i++) {
      a[i] = new NAGComplex();
    }
    a[n] = new NAGComplex(1.0, 0.0);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        a[j] = a[j+1].subtract(a[j].multiply(zact[i]));
      }
      a[n] = a[n].negate().multiply(zact[i]);
    }

    System.out.println("  polish  relerr    fwderr   ");
    System.out.println(" ----------------------------");

    itmax = 30;
    eps = x02aj.eval();
    rmax = x02al.eval();

    for (polish = 0; polish <= 2; polish++) {

      // Find roots
      ifail = 0;
      c02aa.eval(a, n, itmax, polish, z, berr, cond, conv, ifail);

      /* Calculate the maximum relative errors of the roots, and the maximum
       * forward error evaluating the polynomial at those roots. Errors are
       * capped at machine precision. */
      maxrelerr = maxfwderr = eps;
      Arrays.fill(matched, false);

      for (int i = 0; i < n; i++) {

        // Evaluate polynomial at this root
        pz = a[0].clone();
        for (int j = 1; j <= n; j++) {
          pz = z[i].multiply(pz).add(a[j]);
        }

        // Match to an expected root
        k = 0;
        err = rmax;
        for (int j = 0; j < n; j++) {
          if (!matched[j]) {
            delta = z[i].subtract(zact[j]).abs();
            if (delta <= err) {
              err = delta;
              k = j;
            }
          }
        }

        // Mark as matched and update max errors
        matched[k] = true;
        relerr = err/zact[k].abs();
        fwderr = pz.abs();
        maxrelerr = Math.max(maxrelerr, relerr);
        maxfwderr = Math.max(maxfwderr, fwderr);

      }

      // Print output
      System.out.printf(" %2d     %10.2E%10.2E\n", polish, maxrelerr, maxfwderr);

    }

  }

}
