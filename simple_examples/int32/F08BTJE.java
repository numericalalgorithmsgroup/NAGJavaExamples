import static java.lang.Math.*;

import com.nag.routines.F06.F06JJ;
import com.nag.routines.F06.F06ZJ;
import com.nag.routines.F08.F08AU;
import com.nag.routines.F08.F08BT;
import com.nag.routines.Routine;
import com.nag.routines.X04.X04DB;
import com.nag.types.NAGComplex;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * F08BT example program text.
 * @author ludovic
 */
public class F08BTJE {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    int incl = 1, nb = 64;
    double tol;
    int ifail, info = 0, k, lda, ldb, lwork, m, n, nrhs;
    NAGComplex[] a, b, tau, work;
    double[] rnorm, rwork;
    int[] jpvt;
    String[] clabs = new String[]{" "}, rlabs = new String[]{" "};

    // Setup complex constants
    NAGComplex one = new NAGComplex();
    NAGComplex zero = new NAGComplex();
    one.setRe(1.0);
    one.setIm(0.0);
    zero.setRe(0.0);
    zero.setIm(0.0);

    // Inform the Routine class of the type of complex in use - can use any complex object
    Routine.complex = one;

    System.out.println(" F08BTJ Example Program Results");
    System.out.println();

    // Read values from data file
    BufferedReader br = new BufferedReader(new FileReader(args[0]));
    String line = br.readLine();
    line = br.readLine().trim();
    line = br.readLine().trim();
    String[] vals = line.split("\\s+");
    m = Integer.parseInt(vals[0].trim());
    n = Integer.parseInt(vals[1].trim());
    nrhs = Integer.parseInt(vals[2].trim());
    lda = ldb = m;
    lwork = (n + 1) * nb;
    a = new NAGComplex[lda * n];
    b = new NAGComplex[ldb * nrhs];
    tau = new NAGComplex[n];
    work = new NAGComplex[lwork];
    rnorm = new double[nrhs];
    rwork = new double[2 * n];
    jpvt = new int[n];

    // Actually initialize the complex arrays
    for (int i = 0; i < a.length; ++i) {
      a[i] = new NAGComplex();
    }
    for (int i = 0; i < b.length; ++i) {
      b[i] = new NAGComplex();
    }
    for (int i = 0; i < tau.length; ++i) {
      tau[i] = new NAGComplex();
    }
    for (int i = 0; i < work.length; ++i) {
      work[i] = new NAGComplex();
    }

    /* Read A and B from data file */

    readCompMatrix(br, a, lda, n);
    readCompMatrix(br, b, ldb, nrhs);
    br.close();

    /* Initialize JPVT to be zero so that all columns are free */

    for (int i = 0; i < n; ++i) {
      jpvt[i] = 0;
    }

    /* Compute the QR factorization of A */

    F08BT f08bt = new F08BT(m, n, a, lda, jpvt, tau, work, lwork, rwork, info);
    f08bt.eval();

    /* Compute C = (C1) = (Q**H)*B, storing the result in B (C2) */

    String side = "Left";
    String conjTrans = "Conjugate Transpose";
    F08AU f08au = new F08AU(side, conjTrans, m, nrhs, n, a, lda, tau, b, ldb, work, lwork, info);
    f08au.eval();
    a = (NAGComplex[])f08au.getA();

    /* Choose TOL to reflect the relative accuracy of the input data */

    tol = 0.01;

    /* Determine and print the rank, K, or R relative to TOL */

    for (k = 1; k < n + 1; ++k) {
      if (abs(a[k - 1 + lda * (k - 1)]) <= tol * abs(a[0])) {
        break;
      }
    }
    k = k - 1;

    System.out.println(" Tolerance used to estimate the rank of A");
    System.out.printf("   %11.2E\n", tol);
    System.out.println(" Estimated rank of A");
    System.out.printf(" %8d\n", k);

    /* Compute least squares solutions by back-substitution in R(1:K,1:K)*Y = C1
     * storing the result in B */

    String upDown = "Upper";
    String trans = "No transpose";
    String unit = "Non-Unit";
    F06ZJ f06zj = new F06ZJ(side, upDown, trans, unit, k, nrhs, one, a, lda, b, ldb);
    f06zj.eval();

    /* Compute estimates of the square roots of the residual sums of squares
     * (2-norm of each of the columns of C2) */

    int mMinusK = m - k;
    NAGComplex[] btmp = new NAGComplex[b.length];
    for (int i = 0; i < btmp.length; ++i) {
      btmp[i] = new NAGComplex();
    }
    System.arraycopy(b, k, btmp, 0, b.length - k);

    F06JJ f06jj = new F06JJ(mMinusK, btmp, incl);
    rnorm[0] = f06jj.eval(mMinusK, btmp, incl);
    System.arraycopy(btmp, 0, b, k, b.length - k);
    for (int j = 1; j < nrhs; ++j) {
      System.arraycopy(b, k  + j * ldb, btmp, 0, b.length - k - j * ldb);
      rnorm[j] = f06jj.eval(mMinusK, btmp, incl);
      System.arraycopy(btmp,0, b,  k  + j * ldb, b.length - k - j * ldb);
    }

    /* Set the remaining elements of the solutions to zero (to give the
     * basic solutions) */

    for (int i = k; i < n; ++i) {
      for (int j = 0; j < nrhs; ++j) {
        NAGComplex tmp = new NAGComplex();
        tmp.setRe(0.0);
        tmp.setIm(0.0);
        b[i + j * ldb] = tmp;
      }
    }

    /* Permute the least squares solution stored in B to give X = P*Y */

    for (int j = 0; j < nrhs; j++) {
      for (int i = 0; i < n; i++) {
        work[jpvt[i] - 1] = b[j*ldb + i];
      }
      for (int i = 0; i < n; i++) {
        b[j*ldb + i] = work[i];
      }
    }

    /* Print least squares solutions */

    X04DB x04db = new X04DB();
    ifail = 0;
    x04db.eval("General"," ",n,nrhs,b,ldb,"Bracketed","F7.4","Least squares solution(s)",
        "Integer",rlabs,"Integer",clabs,80,0,ifail);

    /* Print the square roots of the residual sums of squares */

    System.out.println();
    System.out.println(" Square root(s) of the residual sum(s) of squares");
    System.out.printf("   ");
    for (int i = 0; i < nrhs; ++i) {
      System.out.printf("%11.2E", rnorm[i]);
    }
    System.out.println();

  }

  private static void readCompMatrix(BufferedReader br, NAGComplex[] mat, int row,
      int col) throws IOException {

    // matches one complex number (without the brackets...)
    String complexPat = "\\(([,0-9\\.\\-\\+\\s]*)\\)";
    String fullPattern = complexPat;
    for (int i = 0; i < col -1; ++i) {
      fullPattern = fullPattern + "\\s*" + complexPat;
    }
    fullPattern = fullPattern + ".*";
    Pattern comppat = Pattern.compile(fullPattern);

    for (int i = 0; i < row; ++i) {
      String line = br.readLine().trim();
      if (line.equalsIgnoreCase("")) {
        line = br.readLine().trim();
      }
      Matcher m = comppat.matcher(line.trim());
      if (m.matches()) {
        for (int j = 0; j < col; ++j) {
          String[] numbers = m.group(j + 1).trim().split(",");
          mat[i + j * row].setRe(Double.parseDouble(numbers[0].trim()));
          mat[i + j * row].setIm(Double.parseDouble(numbers[1].trim()));
          // System.out.print("mat["+i+","+j+"]=("+numbers[0].trim()+","+numbers[1].trim()+") ");
        }
        // System.out.println();
      }


    }
  }

  private static double abs(NAGComplex z) {
    return sqrt(z.getRe() * z.getRe() + z.getIm() * z.getIm());
  }

}
