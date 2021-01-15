import com.nag.routines.E04.E04PT;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RB;
import com.nag.routines.E04.E04RE;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04RJ;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.F08.DSYEVD;
import java.util.Arrays;

/**
 * E04PTJ example program text.
 * @author joed
 * @since 27.0.0.0
 */
public class E04PTJE {


  /**
   * Monitoring function can be used to monitor the progress
   * or, for example, to implement bespoke stopping criteria.
   */
  private static class MONIT extends E04PT.Abstract_E04PT_MONIT {

    public void eval() {

      double tol = RUSER[0];
      int tolReached = IUSER[1];

      // If x is close to the solution, print a message
      if ((RINFO[14] < tol) && (RINFO[15] < tol) && (RINFO[16] < tol) && (RINFO[17] < tol)) {
        if (tolReached == 0) {
          System.out.println();
          System.out.printf("monit() reports good approximate solution (tol = %9.2E)\n", tol);
          IUSER[1] = 1;
        }
      }

    }

  }


  /**
   * E04PTJ example main program.
   */
  public static void main(String[] args) {

    DSYEVD dsyevd = new DSYEVD();
    E04PT e04pt = new E04PT();
    E04RA e04ra = new E04RA();
    E04RB e04rb = new E04RB();
    E04RE e04re = new E04RE();
    E04RH e04rh = new E04RH();
    E04RJ e04rj = new E04RJ();
    E04RZ e04rz = new E04RZ();
    E04ZM e04zm = new E04ZM();
    MONIT monit = new MONIT();

    final int nqc = 1;
    long cpuser, handle;
    double r1;
    int i, idgroup, idlc, idxa, ifail, j, liwork, lwork, m, n, na, nnza, nnzp0,
        nnzp1, nnzu, nnzuc, nu, nv, nvarc1, nvarc2, rptr, xIdx;
    boolean verboseOutput;
    String ctype1, ctype2; // Length must be 8
    double[] a, bla, bua, c, f0, f1, lambda0, lambda1, p0, p1, q0, q1, u, uc,
        work, x, xl, xu;
    double[] rinfo = new double[100], ruser = new double[1], stats = new double[100];
    int[] icola, icolp0, icolp1, idxc1, idxc2, irowa, irowp0, irowp1, iwork;
    int[] iuser = new int[2];

    System.out.println("E04PTJ Example Program Results\n");

    // Dimensions of the problem
    n = 3;
    nnzp0 = 6;
    nnzp1 = 6;

    // Initialize size of linear constraints in SOCP
    m = nqc;
    na = n + nqc + 1;
    nnza = nqc + n;

    // Initialize size of cone constraints
    nvarc1 = 2;
    nvarc2 = 2;

    // Set problem data
    lwork = Math.max(2*n*n+6*n+1, 120+9*n);
    liwork = 5*n + 3;
    irowp0 = new int[]{1, 2, 3, 2, 3, 3};
    icolp0 = new int[]{1, 1, 1, 2, 2, 3};
    p0 = new double[]{0.493, 0.382, 0.270, 0.475, 0.448, 0.515};
    irowp1 = new int[]{1, 2, 3, 2, 3, 3};
    icolp1 = new int[]{1, 1, 1, 2, 2, 3};
    p1 = new double[]{0.737, 0.453, 1.002, 0.316, 0.635, 1.590};
    q0 = new double[]{0.847, 0.08, 0.505};
    q1 = new double[]{0.065, 0.428, 0.097};
    r1 = 1.276;
    f0 = new double[n*n];
    f1 = new double[n*n];
    lambda0 = new double[n];
    lambda1 = new double[n];
    work = new double[lwork];
    iwork = new int[liwork];

    // Store full P0 and P1 in F0 and F1
    Arrays.fill(f0, 0.0);
    for (i = 0; i < nnzp0; i++) {
      f0[getIdx(irowp0[i], icolp0[i], n)] = p0[i];
    }
    Arrays.fill(f1, 0.0);
    for (i = 0; i < nnzp1; i++) {
      f1[getIdx(irowp1[i], icolp1[i], n)] = p1[i];
    }

    // Factorize P0 and P1 via eigenvalue decomposition
    ifail = 0;
    dsyevd.eval("V", "L", n, f0, n, lambda0, work, lwork, iwork, liwork, ifail);
    dsyevd.eval("V", "L", n, f1, n, lambda1, work, lwork, iwork, liwork, ifail);

    // Fomulate F0 and F1 in P0 = F0'*F0, P1 = F1'*F1
    nu = 0;
    nv = 0;
    for (i = 1; i <= n; i++) {
      if (lambda0[i-1] > 0) {
        for (j = 1; j <= n; j++) {
          f0[getIdx(j,i,n)] *= Math.sqrt(lambda0[i-1]);
        }
        m++;
        nu++;
        nnza += n;
      }
      if (lambda1[i-1] > 0) {
        for (j = 1; j <= n; j++) {
          f1[getIdx(j,i,n)] *= Math.sqrt(lambda1[i-1]);
        }
        m++;
        nv++;
        nnza += n;
      }
    }
    nnza += nu + nv;
    na += nu+ nv;
    nvarc1 += nu;
    nvarc2 += nv;

    // Add two fixed variable for two rotated quadratic cones
    na += 2;
    m += 2;
    nnza += 2;

    // Compute size of multipliers
    nnzu = 2*na + 2*m;
    nnzuc = nvarc1 + nvarc2;

    // Initialize arrays to build SOCP
    icola = new int[nnza];
    irowa = new int[nnza];
    a = new double[nnza];
    bla = new double[m];
    bua = new double[m];
    xl = new double[na];
    xu = new double[na];
    c = new double[na];
    x = new double[na];
    u = new double[nnzu];
    uc = new double[nnzuc];
    idxc1 = new int[nvarc1];
    idxc2 = new int[nvarc2];

    // Build objective function parameter c
    // [x, t1, u, v, y1, y2, t0]
    Arrays.fill(c, 0.0);
    for (j = 0; j < n; j++) {
      c[j] = q0[j];
    }
    c[na-1] = 1.0;

    // Build linear constraints parameter A
    idxa = 0;
    rptr = 0;
    // q1 in First row
    rptr++;
    Arrays.fill(irowa, 0, n, rptr);
    for (j = 0; j < n; j++) {
      icola[j] = j+1;
      a[j] = q1[j];
    }
    idxa += n;

    // F0 in F0*x-u=0 row
    for (i = 1; i <= n; i++) {
      if (lambda0[i-1] > 0) {
        rptr += 1;
        for (j = 0; j < n; j++) {
          irowa[idxa+j] = rptr;
          icola[idxa+j] = j+1;
          a[idxa+j] = f0[getIdx(j+1,i,n)];
        }
        idxa += n;
      }
    }
    // F1 in F1*x-v=0 row
    for (i = 1; i <= n; i++) {
      if (lambda1[i-1] > 0) {
        rptr += 1;
        for (j = 0; j < n; j++) {
          irowa[idxa+j] = rptr;
          icola[idxa+j] = j+1;
          a[idxa+j] = f1[getIdx(j+1,i,n)];
        }
        idxa += n;
      }
    }
    // Rest of A, a diagonal matrix
    for (j = 0; j < m; j++) {
      irowa[idxa+j] = j+1;
      icola[idxa+j] = n+j+1;
      a[idxa+j] = 1.0;
    }
    for (j = 1; j < (nu+nv+1); j++) {
      a[idxa+j] = -1.0;
    }
    // RHS in linear constraints
    Arrays.fill(bla, 0.0);
    Arrays.fill(bua, 0.0);
    bla[0] = -r1;
    bua[0] = -r1;
    for (j = (m-nqc); j <= m; j++) {
      bla[j-1] = 1.0;
      bua[j-1] = 1.0;
    }

    // Box constraints, all variables are free
    Arrays.fill(xl, -1E+20);
    Arrays.fill(xu, 1E+20);

    // Cone constraints
    // First cone
    idxc1[0] = na;
    idxc1[1] = n + 1 + nu + nv + 1;
    for (j = 2; j < nvarc1; j++) {
      idxc1[j] = n + j;
    }
    ctype1 = "RQUAD   ";
    // Second cone
    idxc2[0] = n + 1;
    idxc2[1] = n + 1 + nu + nv + 2;
    for (j = 2; j < nvarc2; j++) {
      idxc2[j] = n + nu + j;
    }
    ctype2 = "RQUAD   ";

    // Create the problem handle
    handle = 0;
    ifail = 0;
    e04ra.eval(handle, na, ifail);
    handle = e04ra.getHANDLE();

    // Set objective function
    ifail = 0;
    e04re.eval(handle, na, c, ifail);

    // Set box constraints
    ifail = 0;
    e04rh.eval(handle, na, xl, xu, ifail);

    // Set linear constraints
    ifail = 0;
    idlc = 0;
    e04rj.eval(handle, m, bla, bua, nnza, irowa, icola, a, idlc, ifail);
    idlc = e04rj.getIDLC();

    // Set first cone constraint
    ifail = 0;
    idgroup = 0;
    e04rb.eval(handle, ctype1, nvarc1, idxc1, idgroup, ifail);
    idgroup = e04rb.getIDGROUP();

    // Set first cone constraint
    ifail = 0;
    idgroup = 0;
    e04rb.eval(handle, ctype2, nvarc2, idxc2, idgroup, ifail);
    idgroup = e04rb.getIDGROUP();

    // Turn on monitoring
    ifail = 0;
    e04zm.eval(handle, "SOCP Monitor Frequency = 1", ifail);

    /* Set this to true to cause e04pt to produce intermediate
     * progress output */
    verboseOutput = false;

    if (verboseOutput) {
      // Require printing of primal and dual solutions at the end of the solve
      ifail = 0;
      e04zm.eval(handle, "Print Solution = YES", ifail);
    }
    else {
      // Turn off printing of intermediate progress output
      ifail = 0;
      e04zm.eval(handle, "Print Level = 1", ifail);
    }

    // Call SOCP interior point solver
    cpuser = 0;
    iuser[0] = 0; // unused in this example
    iuser[1] = 0;
    ruser[0] = 1.0E-7;
    ifail = -1;
    e04pt.eval(handle, na, x, nnzu, u, nnzuc, uc, rinfo, stats, monit, iuser,
        ruser, cpuser, ifail);
    ifail = e04pt.getIFAIL();

    // Print solution if optimal or suboptimal solution found
    if (ifail == 0 || ifail == 50) {
      System.out.println(" Optimal X:");
      System.out.println("  x_idx       Value    ");
      for (xIdx = 1; xIdx <= n; xIdx++) {
        System.out.printf("  %5d   %11.3E\n", xIdx, x[xIdx-1]);
      }
    }

    // Free the handle memory
    ifail = 0;
    e04rz.eval(handle, ifail);

  }


  /**
   * Convert from 2D Fortran index to 1D-column major Java index.
   * @param x 1-based row index for a 2D array
   * @param y 1-based column index for a 2D array
   * @return the corresponding 0-based index for a 1D column-major array
   */
  private static int getIdx(int x, int y, int dimX) {
    return ((y-1) * dimX) + (x-1);
  }

}
