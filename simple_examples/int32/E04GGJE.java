import com.nag.routines.E04.E04FFU;
import com.nag.routines.E04.E04GG;
import com.nag.routines.E04.E04GGV;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04RM;
import com.nag.routines.E04.E04RX;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04ZM;

/**
 * E04GG example program text.
 */
public class E04GGJE {

  public static void main(String[] args) {

    final double infbnd = 1.0e20;

    long cpuser = 0, handle = 0;
    int ifail, isparse, nnzrd, nres, nvar;

    double[] blx, bux, ruser, rx, x, z;
    double[] rinfo = new double[100], stats = new double[100];
    int[] icolrd = new int[1], irowrd = new int[1], iuser = new int[1];

    /* Header */
    System.out.println(" E04GGJ Example Program Results\n");

    nvar = 6;
    nres = 24;
    // ruser = new double[2*nres];
    // Data from Lanczos 3 Problem
    ruser = new double[] {
        // t(:) =
        0.0e0,  5.0e-2, 1.0e-1, 1.5e-1, 2.0e-1, 2.5e-1, 3.0e-1, 3.5e-1,
        4.0e-1, 4.5e-1, 5.0e-1, 5.5e-1, 6.0e-1, 6.5e-1, 7.0e-1, 7.5e-1,
        8.0e-1, 8.5e-1, 9.0e-1, 9.5e-1,  1.0e0, 1.05e0, 1.10e0, 1.15e0,
        // y(:) =
        2.5134, 2.0443, 1.6684, 1.3664, 1.1232, 0.9269, 0.7679, 0.6389,
        0.5338, 0.4479, 0.3776, 0.3197, 0.2720, 0.2325, 0.1997, 0.1723,
        0.1493, 0.1301, 0.1138, 0.1000, 0.0883, 0.0783, 0.0698, 0.0624
    };

    iuser[0] = 0;

    // Initialize handle
    ifail = 0;
    E04RA e04ra = new E04RA();
    e04ra.eval(handle, nvar, ifail);
    handle = e04ra.getHANDLE();

    // Define residuals structure, isparse=0 means the residual structure is
    // dense => irowrd and icolrd are not accessed
    isparse = 0;
    nnzrd = 0;
    E04RM e04rm = new E04RM();
    e04rm.eval(handle, nres, isparse, nnzrd, irowrd, icolrd, ifail);

    // Set options
    E04ZM e04zm = new E04ZM();
    e04zm.eval(handle, "BXNL Use Second Derivatives = Yes",ifail);
    e04zm.eval(handle, "BXNL Model = Gauss-Newton",ifail);
    e04zm.eval(handle, "BXNL Glob Method = Reg",ifail);
    // Change printed output verbosity
    e04zm.eval(handle, "Print Level = 1",ifail);

    // Define starting point
    rx = new double[nres];
    z = new double[nvar];
    // x = new double[nvar];
    x = new double[] {
        1.2, 0.3, 5.6, 5.5, 6.5, 7.6
    };

    // Define bounds
    blx = new double[nvar];
    bux = new double[nvar];
    blx[0] = 0.0;
    bux[0] = 1.0;
    blx[1] = -1.0;
    bux[1] = infbnd;
    blx[2] = -1.0;
    bux[2] = infbnd;
    blx[3] = -1.0;
    bux[3] = infbnd;
    blx[4] = -1.0;
    bux[4] = 1.0;
    blx[5] = -1.0;
    bux[5] = 10.0;
    E04RH e04rh = new E04RH();
    e04rh.eval(handle, nvar, blx, bux, ifail);

    // Call the solver
    ifail = -1;
    E04GG e04gg = new E04GG();
    LSQFUN lsqfun = new LSQFUN();
    LSQGRD lsqgrd = new LSQGRD();
    LSQHES lsqhes = new LSQHES();
    LSQHPRD lsqhprd = new LSQHPRD();
    MONIT monit = new MONIT();

    e04gg.eval(handle,lsqfun,lsqgrd,lsqhes,lsqhprd,monit,nvar,x,nres,rx,rinfo,stats,iuser,ruser,cpuser,ifail);
    ifail = e04gg.getIFAIL();

    // Recover latest iterate from handle if available
    if (ifail == 0) {
      ifail = -1;
      E04RX e04rx = new E04RX();
      e04rx.eval(handle,"X",1,nvar,z,ifail);
      ifail = e04rx.getIFAIL();
      if (ifail == 0) {
        System.out.println("\nSolver stored solution iterate in the handle");
        System.out.print("X:  ");
        for (int i = 0; i < nvar; i++){
          System.out.printf("%8.2E  ", z[i]);
        }
        System.out.println();
      }
    }

    // Free the handle memory
    ifail = 0;
    E04RZ e04rz = new E04RZ();
    e04rz.eval(handle, ifail);

  }

  public static class LSQFUN extends E04GG.Abstract_E04GG_LSQFUN {

    public void eval() {
      for (int i = 0; i < this.NRES; i++){
        this.RX[i] = 0.0;
      }

      for (int i = 0; i < this.NRES; i++){
        this.RX[i] = this.RUSER[this.NRES + i] - this.X[0] * Math.exp(-this.X[1] * this.RUSER[i]) -
        this.X[2] * Math.exp(-this.X[3] * this.RUSER[i]) - this.X[4] * Math.exp(-this.X[5] * this.RUSER[i]);
      }

      this.INFORM = 0;
    }
  }

  public static class LSQGRD extends E04GG.Abstract_E04GG_LSQGRD {

    public void eval() {
      int i;

      for (i = 0; i < this.RDX.length; i++) {
        this.RDX[i] = 0;
      }

      for (i = 0; i < this.NRES; i++) {
        this.RDX[i * this.NVAR + 0] = -Math.exp(-this.X[1] * this.RUSER[i]);
        this.RDX[i * this.NVAR + 1] = this.RUSER[i] * this.X[0] * Math.exp(-this.X[1] * this.RUSER[i]);
        this.RDX[i * this.NVAR + 2] = -Math.exp(-this.X[3] * this.RUSER[i]);
        this.RDX[i * this.NVAR + 3] = this.RUSER[i] * this.X[2] * Math.exp(-this.X[3] * this.RUSER[i]);
        this.RDX[i * this.NVAR + 4] = -Math.exp(-this.X[5] * this.RUSER[i]);
        this.RDX[i * this.NVAR + 5] = this.RUSER[i] * this.X[4] * Math.exp(-this.X[5] * this.RUSER[i]);
      }

      this.INFORM = 0;
    }
  }

  public static class LSQHES extends E04GG.Abstract_E04GG_LSQHES {

    public void eval() {
      for (int i = 0; i < this.NVAR * this.NVAR; i++) {
        this.HX[i] = 0.0;
      }

      double sum21 = 0.0, sum22 = 0.0, sum43 = 0.0, sum44 = 0.0, sum65 = 0.0, sum66 = 0.0; 

      for (int i = 0; i < this.NRES; i++){
        sum21 = sum21 + (this.LAMBDA[i] * this.RUSER[i] * Math.exp(-this.X[1] * this.RUSER[i]));
        sum22 = sum22 + (-this.LAMBDA[i] * Math.pow(this.RUSER[i], 2) * this.X[0] * Math.exp(-this.X[1] * this.RUSER[i]));
        sum43 = sum43 + (this.LAMBDA[i] * this.RUSER[i] * Math.exp(-this.X[3] * this.RUSER[i]));
        sum44 = sum44 + (-this.LAMBDA[i] * Math.pow(this.RUSER[i], 2) * this.X[2] * Math.exp(-this.X[3] * this.RUSER[i]));
        sum65 = sum65 + (this.LAMBDA[i] * this.RUSER[i] * Math.exp(-this.X[5] * this.RUSER[i]));
        sum66 = sum66 + (-this.LAMBDA[i] * Math.pow(this.RUSER[i], 2) * this.X[4] * Math.exp(-this.X[5] * this.RUSER[i]));
      }

      this.HX[(2-1) + (1-1) * this.NVAR] = sum21;
      this.HX[(1-1) + (2-1) * this.NVAR] = this.HX[(2-1) + (1-1) * this.NVAR];
      this.HX[(2-1) + (2-1) * this.NVAR] = sum22;
      this.HX[(4-1) + (3-1) * this.NVAR] = sum43;
      this.HX[(3-1) + (4-1) * this.NVAR] = this.HX[(4-1) + (3-1) * this.NVAR];
      this.HX[(4-1) + (4-1) * this.NVAR] = sum44;
      this.HX[(6-1) + (5-1) * this.NVAR] = sum65;
      this.HX[(5-1) + (6-1) * this.NVAR] = this.HX[(6-1) + (5-1) * this.NVAR];
      this.HX[(6-1) + (6-1) * this.NVAR] = sum66;

      this.INFORM = 0;
    }
  }

  public static class MONIT extends E04GG.Abstract_E04GG_MONIT {

    public void eval() {
      E04FFU e04ffu = new E04FFU();
      e04ffu.eval(this.NVAR, this.X, this.INFORM, this.RINFO, this.STATS,
          this.IUSER, this.RUSER, this.CPUSER);
      this.INFORM = e04ffu.getINFORM();
    }
  }

  public static class LSQHPRD extends E04GG.Abstract_E04GG_LSQHPRD {

    public void eval() {
      E04GGV e04ggv = new E04GGV();
      e04ggv.eval(this.NVAR, this.X, this.Y, this.NRES, this.HXY,
          this.INFORM, this.IUSER, this.RUSER, this.CPUSER);
      this.INFORM = e04ggv.getINFORM();
    }
  }

}
