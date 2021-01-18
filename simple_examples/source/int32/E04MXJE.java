import com.nag.routines.E04.E04MX;
import com.nag.routines.E04.E04NP;
import com.nag.routines.E04.E04NQ;
import com.nag.routines.E04.E04NS;
import com.nag.routines.E04.E04NT;
import com.nag.routines.X04.X04AC;
import com.nag.routines.X04.X04AD;
import java.util.Arrays;

/**
 * E04MX example program text.
 */
public class E04MXJE {

  public static void main(String[] args) {

    /* Parameters */
    int lencw = 600, leniw = 600, lenrw = 600, mpslst = 1, nin = 7, nout = 6;
    boolean readints = false;
    String fname = args[0];
    /* Local Scalars */
    double obj, objadd, sinf;
    int i, ifail, iobj, lenc, lintvar, m, maxlintvar, maxm, maxn, maxncolh,
        maxnnz, maxnnzh, minmax, mode, n, ncolh, ninf, nname, nnz, nnzh, ns;
    boolean verboseOutput;
    String start;
    /* Local Arrays */
    double[] a, bl, bu, c, h, pi, rc, ruser, rw, x;
    int[] helast, hs, iccola, iccolh, intvar, irowa, irowh, iuser, iw;
    String[] crname, cw;
    String[] cuser = new String[1], pnames = new String[5];
    cuser[0] = "        ";
    for (int j = 0; j < 5; j++) {
      pnames[j] = "        ";
    }

    System.out.println(" E04MXJ Example Program Results");

    // Initialize
    for (int j = 0; j < 5; j++) {
      pnames[j] = "        ";
    }
    maxm = 0;
    maxn = 0;
    maxnnz = 0;
    maxnnzh = 0;
    maxncolh = 0;
    maxlintvar = 0;

    // Open the data file for reading
    X04AC x04ac = new X04AC();
    mode = 0;
    ifail = 0;
    x04ac.eval(nin, fname, mode, ifail);

    // Prep call to E04MX in query mode
    E04MX e04mx = new E04MX();
    a = new double[1];
    irowa = new int[1];
    iccola = new int[1];
    bl = new double[1];
    bu = new double[1];
    crname = new String[]{"        "};
    h = new double[1];
    irowh = new int[1];
    iccolh = new int[1];
    intvar = new int[1];
    ifail = 0;

    // Placeholders for output only scalars
    n = 0;
    m = 0;
    nnz = 0;
    ncolh = 0;
    nnzh = 0;
    lintvar = 0;
    iobj = 0;
    nname = 0;
    minmax = 0;

    // Make call to E04MX
    e04mx.eval(nin, maxn, maxm, maxnnz, maxncolh, maxnnzh, maxlintvar,
        mpslst, n, m, nnz, ncolh, nnzh, lintvar, iobj, a, irowa,
        iccola, bl, bu, pnames, nname, crname, h, irowh, iccolh,
        minmax, intvar, ifail);

    // Get output scalar values
    n = e04mx.getN();
    m = e04mx.getM();
    nnz = e04mx.getNNZ();
    ncolh = e04mx.getNCOLH();
    nnzh = e04mx.getNNZH();
    lintvar = e04mx.getLINTVAR();
    iobj = e04mx.getIOBJ();
    nname = e04mx.getNNAME();
    minmax = e04mx.getMINMAX();
    ifail = e04mx.getIFAIL();

    // Close file
    X04AD x04ad = new X04AD();
    ifail = 0;
    x04ad.eval(nin, ifail);

    // Set maxm, maxn and maxnnz
    maxm = m;
    maxn = n;
    maxnnz = nnz;
    maxnnzh = nnzh;
    maxncolh = ncolh;
    maxlintvar = (readints) ? lintvar : 1;

    // Allocate memory
    irowa = new int[maxnnz];
    iccola = new int[maxn + 1];
    a = new double[maxnnz];
    bl = new double[maxn + maxm];
    bu = new double[maxn + maxm];
    crname = new String[maxn + maxm];
    for (int j = 0; j < (maxn + maxm); j++) {
      crname[j] = "        ";
    }
    irowh = new int[maxnnzh];
    iccolh = new int[maxncolh + 1];
    h = new double[maxnnzh];
    intvar = new int[maxlintvar];

    // Open the data file for reading
    mode = 0;
    ifail = 0;
    x04ac.eval(nin, fname, mode, ifail);

    // Call E04MX to read the problem
    ifail = 0;
    e04mx.eval(nin, maxn, maxm, maxnnz, maxncolh, maxnnzh, maxlintvar,
        mpslst, n, m, nnz, ncolh, nnzh, lintvar, iobj, a, irowa,
        iccola, bl, bu, pnames, nname, crname, h, irowh, iccolh,
        minmax, intvar, ifail);
    n = e04mx.getN();
    m = e04mx.getM();
    nnz = e04mx.getNNZ();
    ncolh = e04mx.getNCOLH();
    nnzh = e04mx.getNNZH();
    lintvar = e04mx.getLINTVAR();
    iobj = e04mx.getIOBJ();
    nname = e04mx.getNNAME();
    minmax = e04mx.getMINMAX();
    ifail = e04mx.getIFAIL();

    // Close the data file
    ifail = 0;
    x04ad.eval(nin, ifail);

    /* Data has been read. Set up and run the solver */

    iw = new int[leniw];
    rw = new double[lenrw];
    cw = new String[lencw];
    for (int j = 0; j < lencw; j++) {
      cw[j] = "        ";
    }

    // Call E04NP to initialize workspace
    E04NP e04np = new E04NP();
    ifail = 0;
    e04np.eval(cw, lencw, iw, leniw, rw, lenrw, ifail);

    // Call option setter E04NS to change the direction of optimization.
    // Minimization is assumed by default.
    E04NS e04ns = new E04NS();
    if (minmax == 1) {
      ifail = 0;
      e04ns.eval("Maximize", cw, iw, rw, ifail);
    }
    else if (minmax == 0) {
      ifail = 0;
      e04ns.eval("Feasible Point", cw, iw, rw, ifail);
    }

    // Set this to TRUE to cause E04NQ to produce intermediate progress output
    verboseOutput = false;

    if (verboseOutput) {
      // By default E04NQ does not print monitoring information. Set the print
      // file unit or the summary file unit to get information.
      E04NT e04nt = new E04NT();
      ifail = 0;
      e04nt.eval("Print file", nout, cw, iw, rw, ifail);
    }
    else {
      System.out.printf(" \n Problem contains %3d variables and %3d linear constraints\n", n, m);
    }

    // We have no explicit objective vector so set LENC = 0; the objective vector
    // is stored in row IOBJ of ACOL.
    lenc = 0;
    objadd = 0.0;
    start = "C";

    c = new double[Math.max(1, lenc)];
    helast = new int[n + m];
    x = new double[n + m];
    pi = new double[m];
    rc = new double[n + m];
    hs = new int[n + m];
    iuser = new int[ncolh + 1 + nnzh];
    ruser = new double[nnzh];

    if (ncolh > 0) {
      // Store the non zeros of H in ruser for use by qphx
      for (int j = 0; j < nnzh; j++) {
        ruser[j] = h[j];
      }
      // Store iccolh and irowh in iuser for use by qphx
      for (int j = 0; j < ncolh + 1; j++) {
        iuser[j] = iccolh[j];
      }
      for (int j = 0; j < nnzh; j++) {
        iuser[j + ncolh + 1] = irowh[j];
      }

    }

    // Call E04NQ to solve the problem
    E04NQ e04nq = new E04NQ();
    QPHX qphx = new QPHX();
    ns = 0;
    ninf = 0;
    sinf = 0.0;
    obj = 0.0;
    ifail = 0;
    e04nq.eval(start, qphx, m, n, nnz, nname, lenc, ncolh, iobj, objadd,
        pnames[0], a, irowa, iccola, bl, bu, c, crname, helast, hs,
        x, pi, rc, ns, ninf, sinf, obj, cw, lencw, iw, leniw, rw,
        lenrw,cuser,iuser,ruser,ifail);
    ns = e04nq.getNS();
    ninf = e04nq.getNINF();
    sinf = e04nq.getSINF();
    obj = e04nq.getOBJ();
    ifail = e04nq.getIFAIL();

    if (!verboseOutput) {
      System.out.println();
      System.out.printf(" Final objective value = %11.3E\n", obj);
      System.out.printf(" Optimal X = \n");
      System.out.printf("              ");
      for (int j = 0; j < n; j++) {
        System.out.printf("%9.2f", x[j]);
      }
      System.out.printf("\n");
    }

  }

  public static class QPHX extends E04NQ.Abstract_E04NQ_QPHX {

    /**
     * Subroutine to compute H*x.
     * Note: IUSER and RUSER contain the following data:
     *   RUSER(1:NNZH) = H(1:NNZH)
     *   IUSER(1:NCOLH+1) = ICCOLH(1:NCOLH+1)
     *   IUSER(NCOLH+2:NNZH+NCOLH+1) = IROWH(1:NNZH)
     */
    public void eval() {
      /* Local Scalars */
      int end, icol, idx, irow, start;

      Arrays.fill(HX, 0.0);

      for (icol = 0; icol < this.NCOLH + 1; ++icol) {

        start = this.IUSER[icol] - 1;
        end = IUSER[icol+1] - 2;

        for (idx = start; idx < end + 1; ++idx) {

          irow = this.IUSER[this.NCOLH + 1 + idx] - 1;
          this.HX[irow] += this.X[icol] * this.RUSER[idx];

          if (irow != icol) {
            this.HX[icol] += this.X[irow] * this.RUSER[idx];
          }

        }

      }

    }

  }


}
