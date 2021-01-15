import com.nag.routines.E04.E04FC;
import com.nag.routines.F06.DDOT;
import com.nag.routines.F06.DGEMV;
import com.nag.routines.X02.X02AJ;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * E04FC example program text.
 * @author ludovic
 */
public class E04FCJE {

  public static void main(String[] args) {

    BufferedReader dataIn = null;

    try {
      E04FC e04fc = new E04FC();
      System.out.println(" E04FCJ Example Program Results");
      dataIn = new BufferedReader(new FileReader(args[0]));
      //skip header
      dataIn.readLine();

      int inc1 = 1, liw = 1, m, ldfjac = m = 15, n, ldv = n = 3, nt = 3,
          lw = 6 * n + m * n + 2 * m + n * (n - 1) / 2;
      String trans = "T";

      double eta, fsumsq, stepmx, xtol;
      eta = fsumsq = stepmx = xtol = Double.NaN;
      int ifail, iprint, maxcal, nf, niter;
      ifail = iprint = maxcal = nf = niter = 0;
      double[] fjac = new double[m * n],
          fvec = new double[m],
          g = new double[n],
          s = new double[n],
          v = new double[ldv * n],
          w = new double[lw],
          x = new double[n],
          y = new double[m],
          t = new double[m * nt];


      int[] iw = new int[liw];

      for (int i = 0; i < m; ++i) {
        String[] line = dataIn.readLine().trim().split("\\s+");
        if (line.length != nt + 1) {
          System.err.println("Error in data file - only " + line.length
              + " records at line " + (i + 2) + " while expecting " + (nt + 1)
              + " elements");
          System.exit(1);
        }
        y[i] = Double.parseDouble(line[0].replaceAll("D", "E")); // java doesn't know the D format
        for (int j = 1; j <= nt; ++j) {
          t[i + (j - 1) * m] = Double.parseDouble(line[j].replaceAll("D", "E"));
        }
      }

      // Set IPRINT to 1 to obtain output from LSQMON at each iteration
      iprint = -1;

      maxcal = 400 * n;
      eta = 0.5;
      xtol = 10.0 * Math.sqrt((new X02AJ()).eval());

      // We estimate that the minimum will be within 10 units of the starting point
      stepmx = 10.0;

      // Set up the starting point
      x[0] = 0.5;
      x[1] = 1.0;
      x[2] = 1.5;

      ifail = -1;

      LSQFUN lsqfun = new LSQFUN();
      lsqfun.t = t;
      lsqfun.y = y;
      LSQMON lsqmon = new LSQMON();

      e04fc.eval(m, n, lsqfun, lsqmon, iprint, maxcal, eta, xtol, stepmx, x, fsumsq,
          fvec, fjac, ldfjac, s, v, ldv, niter, nf, iw, liw, w, lw, ifail);

      ifail = e04fc.getIFAIL();


      switch (ifail) {
        case (1):
          System.err.println("Unexpected ifail = " + ifail);
          break;
        default:
          System.out.println();
          System.out.printf(" On exit, the sum of squares is %12.4f\n", e04fc.getFSUMSQ());
          System.out.printf(" at the point %12.4f %12.4f %12.4f\n", x[0], x[1], x[2]);
          lsqgrd(m, n, fvec, fjac, ldfjac, g);
          System.out.print(" The estimated gradient is ");
          for (int i = 0; i < n; ++i) {
            System.out.printf(" %12.3e\t", g[i]);
          }
          System.out.println();
          System.out.println("                     (machine dependent)");
          System.out.println(" and the residuals are");
          for (int i = 0; i < m; ++i) {
            System.out.printf(" %9.1e\n", fvec[i]);
          }
      }



    }
    catch (Exception ex) {
      Logger.getLogger(E04FCJE.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally {
      try {
        dataIn.close();
      }
      catch (IOException ex) {
        Logger.getLogger(E04FCJE.class.getName()).log(Level.SEVERE, null, ex);
      }
    }


  }

  public static void lsqgrd(int m, int n, double[] fvec, double[] fjac, int ldfjac, double[] g) {
    DGEMV dgemv = new DGEMV("T", m, n, 1.0, fjac, ldfjac, fvec, 1, 0.0, g, 1);
    dgemv.eval();
    for (int i = 0; i < g.length; ++i) {
      g[i] = 2.0 * g[i];
    }
  }

  public static class LSQFUN implements E04FC.E04FC_LSQFUN {

    public double[] t, y;
    int IFLAG, M, N, LW, LIW;
    double[] XC, FVEC, W;
    int[] IW;

    @Override
    public void setIFLAG(int IFLAG) {
      this.IFLAG = IFLAG;
    }

    @Override
    public int getIFLAG() {
      return IFLAG;
    }

    @Override
    public void setM(int M) {
      this.M = M;
    }

    @Override
    public int getM() {
      return M;
    }

    @Override
    public void setN(int N) {
      this.N = N;
    }

    @Override
    public int getN() {
      return N;
    }

    @Override
    public void setXC(double[] XC) {
      this.XC = XC;
    }

    @Override
    public double[] getXC() {
      return XC;
    }

    @Override
    public void setFVEC(double[] FVEC) {
      this.FVEC = FVEC;
    }

    @Override
    public double[] getFVEC() {
      return FVEC;
    }

    @Override
    public void setIW(int[] IW) {
      this.IW = IW;
    }

    @Override
    public int[] getIW() {
      return IW;
    }

    @Override
    public void setLIW(int LIW) {
      this.LIW = LIW;
    }

    @Override
    public int getLIW() {
      return LIW;
    }

    @Override
    public void setW(double[] W) {
      this.W = W;
    }

    @Override
    public double[] getW() {
      return W;
    }

    @Override
    public void setLW(int LW) {
      this.LW = LW;
    }

    @Override
    public int getLW() {
      return LW;
    }

    @Override
    public void eval(int IFLAG, int M, int N, double[] XC, double[] FVEC,
        int[] IW, int LIW, double[] W, int LW) {
      for (int i = 0; i < M; ++i) {
        FVEC[i] = XC[0] + t[i] / (XC[1] * t[i + M] + XC[2] * t[i + 2 * M]) - y[i];
      }
    }

  }

  public static class LSQMON implements E04FC.E04FC_LSQMON {

    int M, N, LDFJAC, NITER, NF, IGRADE, LIW, LW;
    int[] IW;
    double[] XC, FVEC, FJAC, S, W;

    @Override
    public void setM(int M) {
      this.M = M;
    }

    @Override
    public int getM() {
      return M;
    }

    @Override
    public void setN(int N) {
      this.N = N;
    }

    @Override
    public int getN() {
      return N;
    }

    @Override
    public void setXC(double[] XC) {
      this.XC = XC;
    }

    @Override
    public double[] getXC() {
      return XC;
    }

    @Override
    public void setFVEC(double[] FVEC) {
      this.FVEC = FVEC;
    }

    @Override
    public double[] getFVEC() {
      return FVEC;
    }

    @Override
    public void setFJAC(double[] FJAC) {
      this.FJAC = FJAC;
    }

    @Override
    public double[] getFJAC() {
      return FJAC;
    }

    @Override
    public void setLDFJAC(int LDFJAC) {
      this.LDFJAC = LDFJAC;
    }

    @Override
    public int getLDFJAC() {
      return LDFJAC;
    }

    @Override
    public void setS(double[] S) {
      this.S = S;
    }

    @Override
    public double[] getS() {
      return S;
    }

    @Override
    public void setIGRADE(int IGRADE) {
      this.IGRADE = IGRADE;
    }

    @Override
    public int getIGRADE() {
      return IGRADE;
    }

    @Override
    public void setNITER(int NITER) {
      this.NITER = NITER;
    }

    @Override
    public int getNITER() {
      return NITER;
    }

    @Override
    public void setNF(int NF) {
      this.NF = NF;
    }

    @Override
    public int getNF() {
      return NF;
    }

    @Override
    public void setIW(int[] IW) {
      this.IW = IW;
    }

    @Override
    public int[] getIW() {
      return IW;
    }

    @Override
    public void setLIW(int LIW) {
      this.LIW = LIW;
    }

    @Override
    public int getLIW() {
      return LIW;
    }

    @Override
    public void setW(double[] W) {
      this.W = W;
    }

    @Override
    public double[] getW() {
      return W;
    }

    @Override
    public void setLW(int LW) {
      this.LW = LW;
    }

    @Override
    public int getLW() {
      return LW;
    }

    @Override
    public void eval(int M, int N, double[] XC, double[] FVEC, double[] FJAC,
        int LDFJAC, double[] S, int IGRADE, int NITER, int NF, int[] IW,
        int LIW, double[] W, int LW) {

      int ndec = 3;
      double fsumsq, gtg;
      double[] g = new double[ndec];
      DDOT ddot = new DDOT(M, FVEC, 1, FVEC, 1);
      fsumsq = ddot.eval();

      lsqgrd(M, N, FVEC, FJAC, LDFJAC, g);

      gtg = ddot.eval(N, g, 1, g, 1);
      // 99998 Format (1X,1P,E13.5,10X,1P,E9.1,10X,1P,E9.1)
      System.out.println();
      System.out.println("  Itn      F evals        SUMSQ             GTG        Grade");
      System.out.printf(" %4d      %5d      %13.5e      %9.1e      %3d\n",
          NITER, NF, fsumsq, gtg, IGRADE);
      System.out.println();
      System.out.println("       X                    G           Singular values");
      for (int j = 0; j < N; ++j) {
        System.out.printf(" %13.5e          %9.1e          %9.1e\n",XC[j], g[j], S[j]);
      }

    }

  }

}
