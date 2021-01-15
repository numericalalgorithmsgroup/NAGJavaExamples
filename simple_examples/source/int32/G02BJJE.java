import com.nag.routines.G02.G02BJ;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * G02BJ example program text.
 * @author ludovic
 */
public class G02BJJE {

  public static void main(String[] args) {
    int i, ifail, ldcnt, ldr, ldssp, ldx, ncases = 0; //, m = 0, n = 0, nvars = 0;
    double[] cnt, r, ssp, std, xbar;//, xmiss;

    if (args.length != 1) {
      G02BJJE.usage();
    }

    String dataFile = args[0];
    DataHolder data = new DataHolder();
    readDataFile(dataFile, data);
    System.out.println(" G02BJJ Example Program Results");
    System.out.println();

    ldcnt = ldr = ldssp = data.nvars;
    ldx = data.n;
    cnt = new double[ldcnt * data.nvars];
    r = new double[ldr * data.nvars];
    ssp = new double[ldssp * data.nvars];
    std = new double[data.nvars];
    xbar = new double[data.nvars];


    //Display the data

    System.out.printf(" Number of variables (columns) = %5d\n", data.m);
    System.out.printf(" Number of cases     (rows)    = %5d\n", data.n);
    System.out.println();
    System.out.println(" Data matrix is:-");
    for (int ii = 0; ii < data.m; ++ii) {
      System.out.printf(" %12d",ii+1);
    }
    System.out.println();
    for (int ii = 0; ii < data.n; ++ii) {
      System.out.printf(" %3d ",ii+1);
      for (int jj = 0; jj < data.m; ++jj) {
        System.out.printf("%12.4f ",data.x[ii + jj * data.n]);
      }
      System.out.println();
    }

    ifail = 0;
    G02BJ g02bj = new G02BJ(data.n, data.m, data.x, ldx, data.miss, data.xmiss,
        data.nvars, data.kvar, xbar, std, ssp, ldssp, r, ldr, ncases, cnt,
        ldcnt, ifail);
    g02bj.eval();

    data.n = g02bj.getN();
    data.m = g02bj.getM();
    data.x = g02bj.getX();
    ldx = g02bj.getLDX();
    data.miss = g02bj.getMISS();
    data.xmiss = g02bj.getXMISS();
    data.nvars = g02bj.getNVARS();
    data.kvar = g02bj.getKVAR();
    xbar = g02bj.getXBAR();
    std = g02bj.getSTD();
    ssp = g02bj.getSSP();
    ldssp = g02bj.getLDSSP();
    r = g02bj.getR();
    ldr = g02bj.getLDR();
    ncases = g02bj.getNCASES();
    cnt = g02bj.getCNT();
    ldcnt = g02bj.getLDCNT();
    ifail = g02bj.getIFAIL();

    //Display results
    System.out.println();
    System.out.println(" Variable\tMean\tSt.dev.");
    for (int ii = 0; ii < data.nvars; ++ii) {
      System.out.printf(" %5d %11.4f %11.4f\n",data.kvar[ii], xbar[ii], std[ii]);
    }
    System.out.println();
    System.out.println(" Sums of squares and cross-products of deviations");
    for (int ii = 0; ii < data.nvars; ++ii) {
      System.out.printf(" %12d",data.kvar[ii]);
    }
    System.out.println();
    for (int ii = 0; ii < data.nvars; ++ii) {
      System.out.printf(" %3d",data.kvar[ii]);
      for (int jj = 0; jj < data.nvars; ++jj) {
        System.out.printf(" %12.4f",ssp[ii + ldssp * jj]);
      }
      System.out.println();
    }
    System.out.println();
    System.out.println(" Correlation coefficients");
    for (int ii = 0; ii < data.nvars; ++ii) {
      System.out.printf(" %12d",data.kvar[ii]);
    }
    System.out.println();
    for (int ii = 0; ii < data.nvars; ++ii) {
      System.out.printf(" %3d",data.kvar[ii]);
      for (int jj = 0; jj < data.nvars; ++jj) {
        System.out.printf(" %12.4f",r[ii + ldr * jj]);
      }
      System.out.println();
    }
    System.out.println();
    System.out.printf(" Minimum number of cases used for any pair of variables: %5d\n", ncases);
    System.out.println();
    System.out.println(" Numbers used for each pair are:");
    for (int ii = 0; ii < data.nvars; ++ii) {
      System.out.printf(" %12d",data.kvar[ii]);
    }
    System.out.println();
    for (int ii = 0; ii < data.nvars; ++ii) {
      System.out.printf(" %3d",data.kvar[ii]);
      for (int jj = 0; jj < data.nvars; ++jj) {
        System.out.printf(" %12.4f",cnt[ii + ldcnt * jj]);
      }
      System.out.println();
    }
    System.out.println();
  }

  private static void usage() {
    System.err.println("Please specify the path to the data file.");
    System.exit(-1);
  }

  /**
     G02BJF Example Program Data
     5  4  3                    :: N, M, NVARS
     3.0    3.0    1.0    2.0
     6.0    4.0   -1.0    4.0
     9.0    0.0    5.0    9.0
     12.0    2.0    0.0    0.0
     -1.0    5.0    4.0   12.0  :: End of X
     1      1      0      1   :: MISS
     -1.0    0.0    0.0    0.0  :: XMISS
     4  1  2                    :: KVAR
  */
  private static void readDataFile(String filename, DataHolder data) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line = br.readLine(); // skip header
      line = br.readLine();
      String[] sVal = line.split("\\s+");
      data.setN(Integer.parseInt(sVal[0]));
      data.setM(Integer.parseInt(sVal[1]));
      data.setNvars(Integer.parseInt(sVal[2]));

      data.x = new double[data.n * data.m];
      data.miss = new int[data.m];
      data.xmiss = new double[data.m];
      data.kvar = new int[data.nvars];

      for (int i = 0; i < data.n; ++i) {
        line = br.readLine().trim();
        sVal = line.split("\\s+");
        for (int j = 0; j < data.m; ++j) {
          data.x[i + j * data.n] = Double.parseDouble(sVal[j]);
        }
      }
      line = br.readLine().trim();
      sVal = line.split("\\s+");
      for (int j = 0; j < data.m; ++j) {
        data.miss[j] = Integer.parseInt(sVal[j]);
      }
      line = br.readLine().trim();
      sVal = line.split("\\s+");
      for (int j = 0; j < data.m; ++j) {
        data.xmiss[j] = Double.parseDouble(sVal[j]);
      }

      line = br.readLine().trim();
      sVal = line.split("\\s+");
      for (int j = 0; j < data.nvars; ++j) {
        data.kvar[j] = Integer.parseInt(sVal[j]);
      }
    }
    catch (FileNotFoundException ex) {
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch (IOException ex) {
      System.err.println("***FATAL: Can't Read " + filename + "\n" + ex.getMessage());
      System.exit(-3);
    }

  }

  private static class DataHolder {
    private int n;
    private int m;
    private int nvars;
    private double[] x;
    private int[] miss;
    private double[] xmiss;
    private int[] kvar;

    /**
     * @return the n
     */
    public int getN() {
      return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(int n) {
      this.n = n;
    }

    /**
     * @return the m
     */
    public int getM() {
      return m;
    }

    /**
     * @param m the m to set
     */
    public void setM(int m) {
      this.m = m;
    }

    /**
     * @return the nvars
     */
    public int getNvars() {
      return nvars;
    }

    /**
     * @param nvars the nvars to set
     */
    public void setNvars(int nvars) {
      this.nvars = nvars;
    }

    /**
     * @return the x
     */
    public double[] getX() {
      return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double[] x) {
      this.x = x;
    }

    /**
     * @return the miss
     */
    public int[] getMiss() {
      return miss;
    }

    /**
     * @param miss the miss to set
     */
    public void setMiss(int[] miss) {
      this.miss = miss;
    }

    /**
     * @return the xmiss
     */
    public double[] getXmiss() {
      return xmiss;
    }

    /**
     * @param xmiss the xmiss to set
     */
    public void setXmiss(double[] xmiss) {
      this.xmiss = xmiss;
    }

    /**
     * @return the kvar
     */
    public int[] getKvar() {
      return kvar;
    }

    /**
     * @param kvar the kvar to set
     */
    public void setKvar(int[] kvar) {
      this.kvar = kvar;
    }
  }
}
