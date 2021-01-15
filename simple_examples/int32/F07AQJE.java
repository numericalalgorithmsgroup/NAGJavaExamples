import com.nag.routines.F07.F07AQ;
import com.nag.routines.Routine;
import com.nag.types.NAGComplex;
import com.nag.types.NAGComplexF;

/**
 * F07AQ example program text.
 * @author joed
 * @since 27.1.0.0
 */
public class F07AQJE {

  public static void main(String[] args) {

    F07AQ f07aq = new F07AQ();
    int info, iter, lda, ldb, ldx, n, r;
    NAGComplex[] a, b, work, x;
    NAGComplexF[] swork;
    double[] rwork;
    int[] ipiv;

    System.out.println(" F07AQJ Example Program Results");
    System.out.println();

    // Set dimensions and instantiate arrays
    n = 4;
    r = 1;
    lda = ldb = ldx = n;
    a = new NAGComplex[lda*n];
    b = new NAGComplex[ldb*r];
    work = (NAGComplex[]) (new NAGComplex()).getArrayOfInstances(n*r);
    x = (NAGComplex[]) (new NAGComplex()).getArrayOfInstances(ldx*r);
    swork = (NAGComplexF[]) (new NAGComplexF()).getArrayOfInstances(n*(n+r));
    ipiv = new int[n];
    rwork = new double[n];

    // Set A
    a[0] = new NAGComplex(-1.34, 2.55);  // Column 1
    a[1] = new NAGComplex(-0.17, -1.41);
    a[2] = new NAGComplex(-3.29, -2.39);
    a[3] = new NAGComplex(2.41, 0.39);
    a[4] = new NAGComplex(0.28, 3.17);   // Column 2
    a[5] = new NAGComplex(3.31, -0.15);
    a[6] = new NAGComplex(-1.91, 4.42);
    a[7] = new NAGComplex(-0.56, 1.47);
    a[8] = new NAGComplex(-6.39, -2.20); // Column 3
    a[9] = new NAGComplex(-0.15, 1.34);
    a[10] = new NAGComplex(-0.14, -1.35);
    a[11] = new NAGComplex(-0.83, -0.69);
    a[12] = new NAGComplex(0.72, -0.92); // Column 4
    a[13] = new NAGComplex(1.29, 1.38);
    a[14] = new NAGComplex(1.72, 1.35);
    a[15] = new NAGComplex(-1.96, 0.67);

    // Set B
    b[0] = new NAGComplex(26.26,51.78);
    b[1] = new NAGComplex( 6.43,-8.68);
    b[2] = new NAGComplex(-5.75,25.31);
    b[3] = new NAGComplex( 1.16, 2.57);

    // Notify wrappers of complex types
    Routine.setComplex(new NAGComplex());
    Routine.setComplexF(new NAGComplexF());

    // Solve the equations Ax = b for x
    iter = info = 0;
    f07aq.eval(n, r, a, lda, ipiv, b, ldb, x, ldx, work, swork, rwork, iter, info);
    iter = f07aq.getITER();
    info = f07aq.getINFO();

    if (info == 0) {

      // Print solution

      System.out.println(" Solution");
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < r; j++) {
          System.out.printf("    (%7.4f,%7.4f)", x[j*n+i].getRe(), x[j*n+i].getIm());
        }
      }
      System.out.println();

      // Print pivot indices

      System.out.println();
      System.out.println(" Pivot indices");
      for (int i = 0; i < n; i++) {
        System.out.printf(" %11d", ipiv[i]);
      }
      System.out.println();

    }
    else {
      System.out.printf(" The (%3d,%3d) element of the factor U is zero", info, iter);
    }

  }

}
