import com.nag.routines.E04.E04FFU;
import com.nag.routines.E04.E04GG;
import com.nag.routines.E04.E04GGU;
import com.nag.routines.E04.E04GGV;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RM;
import com.nag.routines.E04.E04TB;
import com.nag.routines.E04.E04TC;
import com.nag.routines.E04.E04TD;
import com.nag.routines.E04.E04ZM;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;

/**
 * E04TC example program text.
 * @author Mo
 */
public class E04TCJE {

  public static void main(String[] args) {
    double[] rx, x, udt, udy;
    double[] rinfo = new double[100], ruser = new double[0],
             stats = new double[100];
    int ifail, isparse, nnzrd, nres, nvar, udnres;
    int[] icolrd = new int[0], irowrd = new int[0], iuser = new int[0];
    int[] idx;
    long cpuser, handle; // c_ptr
    LSQFUN lsqfun = new LSQFUN();
    LSQGRD lsqgrd = new LSQGRD();
    LSQHES lsqhes = new LSQHES();
    LSQHPRD lsqhprd = new LSQHPRD();
    MONIT monit = new MONIT();

    /* Header */
    System.out.printf(" E04TCJ Example Program Results\n\n");
    try {

      handle = 0L;
      cpuser = 0L;

      BufferedReader br = new BufferedReader(new FileReader(args[0]));
      String line = br.readLine(); // read the header
      line = br.readLine().trim();
      line.replaceAll("\\s+", " ");
      String[] data = line.split("\\s+");

      /* Read number of residuals */
      nres = Integer.parseInt(data[0]);

      udnres = nres;

      /* Allocate memory */
      udt = new double[nres];
      udy = new double[nres];

      /* Read observations */

      for (int ii = 0; ii < nres; ii += 3) {
        line = br.readLine().trim();
        line.replaceAll("\\s+", " ");
        data = line.split("\\s+");
        for (int jj = 0; jj < 3; ++jj) {
          udt[ii+jj] = Double.parseDouble(data[jj]);
        }
      }

      for (int ii = 0; ii < nres; ii += 3) {
        line = br.readLine().trim();
        line.replaceAll("\\s+", " ");
        data = line.split("\\s+");
        for (int jj = 0; jj < 3; ++jj) {
          udy[ii+jj] = Double.parseDouble(data[jj]);
        }
      }

      /* try to fit the model */
      /* f(t) = at^2 + bt + c + d sin(omega t) */
      /* To the data {(t_i, y_i)} */
      nvar = 5;

      /* Initialize the NAG optimization handle */
      E04RA e04ra = new E04RA();
      ifail = 0; // hard fail
      e04ra.eval(handle, nvar, ifail);
      handle = e04ra.getHANDLE();

      /* Define a dense nonlinear least-squares objective function */
      /* (isparse = 0 => the sparsity pattern of the Jacobian */
      /* doesn't need to be defined) */
      E04RM e04rm = new E04RM();
      isparse = 0;
      nnzrd = 1;
      e04rm.eval(handle, nres, isparse, nnzrd, irowrd, icolrd, ifail);

      /* Set some optional parameters to control the output of the solver */
      E04ZM e04zm = new E04ZM();
      e04zm.eval(handle, "Print Options = No", ifail);
      e04zm.eval(handle, "Print Solution = X", ifail);
      e04zm.eval(handle, "Print Level = 1", ifail);

      System.out.println("First solve the problem with the outliers");
      System.out.println();
      System.out.println("--------------------------------------------------------");

      /* Call the solver */
      E04GG e04gg = new E04GG();
      x = new double[nvar];
      for (int ii = 0; ii < nvar; ++ii) {
        x[ii] = 1.0;
      }
      rx = new double[nres];
      iuser = new int[] {udnres};
      ruser = new double[2 * udnres];;
      for (int ii = 0; ii < udnres; ii++) {
        ruser[ii] = udt[ii];
        ruser[udnres + ii] = udy[ii];
      }
      ifail = -1;
      e04gg.eval(handle, lsqfun, lsqgrd, lsqhes, lsqhprd, monit, nvar, x, nres,
                 rx, rinfo, stats, iuser, ruser, cpuser, ifail);

      System.out.println("--------------------------------------------------------");
      System.out.println();
      System.out.println("Now remove the outlier residuals from the problem handle");
      System.out.println();
      System.out.println("--------------------------------------------------------");

      /* Disable the two outlier residuals */
      E04TC e04tc = new E04TC();
      idx = new int[] {10, 20};
      e04tc.eval(handle, "NLS", 2, idx, ifail);

      /* Solve the problem again */
      for (int ii = 0; ii < nvar; ++ii) {
        x[ii] = 1.0;
      }
      ifail = -1;
      e04gg.eval(handle, lsqfun, lsqgrd, lsqhes, lsqhprd, monit, nvar, x, nres,
                 rx, rinfo, stats, iuser, ruser, cpuser, ifail);

      System.out.println("--------------------------------------------------------");
      System.out.println();
      System.out.println("Assuming the outliers points are measured again");
      System.out.println("we can enable the residuals and adjust the values");
      System.out.println();
      System.out.println("--------------------------------------------------------");

      /* Fix the first variable to its known value of 0.3 */
      /* enable the residuals and adjust the values in the data */
      E04TD e04td = new E04TD();
      e04td.eval(handle, "variable", 1, 0.3, 0.3, ifail);
      E04TB e04tb = new E04TB();
      e04tb.eval(handle, "NLS", 2, idx, ifail);
      udy[9] = -0.515629;
      udy[19] = 0.54920;
      
      /* Solve the problem */
      for (int ii = 0; ii < nvar; ++ii) {
        x[ii] = 1.0;
      }
      ifail = -1;
      e04gg.eval(handle, lsqfun, lsqgrd, lsqhes, lsqhprd, monit, nvar, x, nres,
                 rx, rinfo, stats, iuser, ruser, cpuser, ifail);

    }
    catch (Exception ex) {
      Logger.getLogger(E04TCJE.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public static class LSQFUN extends E04GG.Abstract_E04GG_LSQFUN {

    public void eval() {
      int udnres;
      double[] udt, udy;

      /* Unpack the user data from cpuser */
      udnres = this.IUSER[0];
      udt = Arrays.copyOfRange(this.RUSER, 0, udnres);
      udy = Arrays.copyOfRange(this.RUSER, udnres, udnres + udnres);

      for (int i = 0; i < this.NRES; i++) {
        this.RX[i] = 0.0;
      }

      for (int i = 0; i < this.NRES; i++) {
        this.RX[i] = (this.X[0] * Math.pow(udt[i],2) + this.X[1] * udt[i] + 
                      this.X[2] + this.X[3] * Math.sin(this.X[4] * udt[i])) -
                      udy[i];
      }

      this.INFORM = 0;

    }

  }

  public static class LSQGRD extends E04GG.Abstract_E04GG_LSQGRD {

    public void eval() {
      int udnres;
      double[] udt, udy;

      /* Unpack the user data from cpuser */
      udnres = this.IUSER[0];
      udt = Arrays.copyOfRange(this.RUSER, 0, udnres);
      udy = Arrays.copyOfRange(this.RUSER, udnres, udnres + udnres);

      for (int i = 1; i <= this.NRES; i++) {
        this.RDX[((i-1)*this.NVAR + 1) - 1] = Math.pow(udt[i-1],2);
        this.RDX[((i-1)*this.NVAR + 2) - 1] = udt[i-1];
        this.RDX[((i-1)*this.NVAR + 3) - 1] = 1.0;
        this.RDX[((i-1)*this.NVAR + 4) - 1] = Math.sin(this.X[4] * udt[i-1]);
        this.RDX[((i-1)*this.NVAR + 5) - 1] = this.X[3] * udt[i-1] * 
                                              Math.cos(this.X[4] * udt[i-1]);

      }

      this.INFORM = 0;

    }

  }

  public static class LSQHES extends E04GG.Abstract_E04GG_LSQHES {

    public void eval() {
      E04GGU e04ggu = new E04GGU();
      e04ggu.eval(this.NVAR, this.X, this.NRES, this.LAMBDA, this.HX,
          this.INFORM, this.IUSER, this.RUSER, this.CPUSER);
      this.INFORM = e04ggu.getINFORM();
    }

  }

  public static class LSQHPRD extends E04GG.Abstract_E04GG_LSQHPRD {

    public void eval() {
      E04GGV e04ggv = new E04GGV();
      e04ggv.eval(this.NVAR, this.X, this.Y, this.NRES, this.HXY, this.INFORM,
          this.IUSER, this.RUSER, this.CPUSER);
      this.INFORM = e04ggv.getINFORM();
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

}
