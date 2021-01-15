import com.nag.routines.E04.E04FF;
import com.nag.routines.E04.E04FFU;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04RM;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04ZM;
import java.util.Arrays;

/**
 * E04FF example program text.
 * @author joed
 */
public class E04FFJE {

  public static void main(String[] args) {
    double infbnd = 1.0e20;
    double[] lx, rx, ux, x, ruser, pdy, pdz;
    double[] rinfo = new double[100], stats = new double[100];
    int ifail, isparse, nnzrd, nres, nvar, pdny, pdnz;
    int[] iuser;
    int[] icolrd = new int[1], irowrd = new int[1];
    long cpuser, handle; // c_ptr
    MONIT monit = new MONIT();
    OBJFUN objfun = new OBJFUN();

    /* Header */
    System.out.printf(" E04FFJ Example Program Results\n\n");

    /* Fill problem data structure */
    pdny = 11;
    pdnz = 11;
    pdy = new double[] {
      4.0e0, 2.0e0, 1.0e0, 5.0e-1, 2.5e-1, 1.67e-1, 1.25e-1, 1.0e-1,
      8.33e-2, 7.14e-2, 6.25e-2
    };
    pdz = new double[] {
      1.957e-1, 1.947e-1, 1.735e-1, 1.6e-1, 8.44e-2, 6.27e-2, 4.56e-2,
      3.42e-2, 3.23e-2, 2.35e-2, 2.46e-2
    };
    nvar = 4;
    nres = 11;

    /* Initialize handle */
    E04RA e04ra = new E04RA();
    handle = 0;
    ifail = 0; // hard fail
    e04ra.eval(handle, nvar, ifail);
    handle = e04ra.getHANDLE();

    /* Define residuals structure */
    E04RM e04rm = new E04RM();
    isparse = 0; // Dense => irowrd and icolrd are not accessed
    nnzrd = 1;
    ifail = 0;
    e04rm.eval(handle, nres, isparse, nnzrd, irowrd, icolrd, ifail);

    /* Set options */
    E04ZM e04zm = new E04ZM();
    ifail = 0;
    e04zm.eval(handle, "DFLS Trust Region Tolerance = 5.0e-6", ifail);
    ifail = 0;
    e04zm.eval(handle, "Print Solution = YES", ifail);

    /* Define starting point */
    x = new double[] {0.25, 0.39, 0.415, 0.39};
    rx = new double[nres];

    /* Define bounds for the second and the fourth variable */
    E04RH e04rh = new E04RH();
    lx = new double[] {-infbnd, 0.2, -infbnd, 0.3};
    ux = new double[] {infbnd, 1.0, infbnd, infbnd};
    ifail = 0;
    e04rh.eval(handle, nvar, lx, ux, ifail);

    /* Call the solver */
    E04FF e04ff = new E04FF();
    ifail = -1;
    iuser = new int[] {pdny, pdnz};
    ruser = new double[2 * nres];
    cpuser = 0L;
    for (int i = 0; i < nres; i++) {
      ruser[i] = pdy[i];
      ruser[nres + i] = pdz[i];
    }
    e04ff.eval(handle, objfun, monit, nvar, x, nres, rx, rinfo, stats,
        iuser, ruser, cpuser, ifail);

    /* Free handle memory */
    E04RZ e04rz = new E04RZ();
    ifail = 0;
    e04rz.eval(handle, ifail);

  }

  public static class OBJFUN extends E04FF.Abstract_E04FF_OBJFUN {

    public void eval() {
      int pdny, pdnz;
      double r1, r2;
      double[] pdy, pdz;

      /* Interrupt solver if the dimensions are incorrect */
      if (this.NRES != 11 || this.NVAR != 4) {
        this.INFORM = -1;
        return;
      }

      /* Extract the problem data structure from the workspaces */
      pdny = this.IUSER[0];
      pdnz = this.IUSER[1];
      if (pdny != this.NRES || pdnz != this.NRES) {
        this.INFORM = -1;
        return;
      }
      pdy = Arrays.copyOfRange(this.RUSER, 0, pdny);
      pdz = Arrays.copyOfRange(this.RUSER, pdny, pdny + pdnz);

      for (int i = 0; i < this.NRES; i++) {
        r1 = pdy[i] * (pdy[i] + this.X[1]);
        r2 = pdy[i] * (pdy[i] + this.X[2]) + this.X[3];
        this.RX[i] = pdz[i] - (this.X[0] * r1/r2);
      }

    }

  }

  public static class MONIT extends E04FF.Abstract_E04FF_MONIT {

    public void eval() {
      E04FFU e04ffu = new E04FFU();
      e04ffu.eval(this.NVAR, this.X, this.INFORM, this.RINFO, this.STATS,
          this.IUSER, this.RUSER, this.CPUSER);
      this.INFORM = e04ffu.getINFORM();
    }

  }

}
