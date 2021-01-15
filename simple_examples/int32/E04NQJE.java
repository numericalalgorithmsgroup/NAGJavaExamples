import com.nag.routines.E04.E04NP;
import com.nag.routines.E04.E04NQ;
import com.nag.routines.E04.E04NT;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * E04NQ example program text.
 */
public class E04NQJE {

  public static final int LENCW = 600;
  public static final int LENIW = 600;
  public static final int LENRW = 600;
  public static final int NIN = 5;
  public static final int NOUT = 6;

  public static void main(String[] args) throws IOException {

    /* Local Scalars */
    double obj, objadd, sinf;
    int i, icol, ifail, iobj, jcol, lenc, m, n, ncolh, ne, ninf, nname, ns;
    boolean verboseOutput;
    String prob;                                               // Length = 8
    String start;                                              // Length = 1
    /* Local Arrays */
    double[] acol, bl, bu, c, pi, rc, x;
    double[] ruser = new double[1], rw = new double[LENRW];
    int[] helast, hs, inda, loca;
    int[] iuser = new int[1], iw = new int[LENIW];
    String[] cuser = new String[1], cw = new String[LENCW];    // Length = 8
    String[] names;                                            // Length = 8
    /* Init String arrays with same length*/
    Arrays.fill(cuser, "        ");
    Arrays.fill(cw, "        ");

    System.out.println(" E04NQJ Example Program Results");

    /* Read data file */

    if (args.length != 1) {
      usage();
    }
    BufferedReader dataIn = new BufferedReader(new FileReader(args[0]));
    String line;

    // Skip heading in data file
    line = dataIn.readLine();

    // Read n, m
    line = dataIn.readLine();
    String[] data = line.split(":")[0].trim().split("\\s+");
    if (data.length != 2) {
      System.out.println("Data file badly formatted - expected 2 ints separated by blank spaces.\n"
          + line + "\n"+Arrays.toString(data));
      System.exit(1);
    }
    n = Integer.parseInt(data[0]);
    m = Integer.parseInt(data[1]);

    // Read ne, iobj, ncolh, start, nname
    line = dataIn.readLine();
    data = line.split(":")[0].trim().split("\\s+");
    if (data.length != 5) {
      System.out.println("Data file badly formatted - expected 5 ints separated by blank spaces");
      System.exit(1);
    }
    ne = Integer.parseInt(data[0]);
    iobj = Integer.parseInt(data[1]);
    ncolh = Integer.parseInt(data[2]);
    start = data[3].replaceAll("'","");
    nname = Integer.parseInt(data[4]);

    // Allocate
    inda = new int[ne];
    loca = new int[n+1];
    helast = new int[n+m];
    hs = new int[n+m];
    acol = new double[ne];
    bl = new double[n+m];
    bu = new double[n+m];
    x = new double[n+m];
    pi = new double[m];
    rc = new double[n+m];
    names = new String[nname];

    // Read names
    line = dataIn.readLine(); // skipping blank line
    line = dataIn.readLine();
    data = line.split(":")[0].trim().split("\\s+");
    int nameIndex = 0;
    int dataIndex = 0;
    while (nameIndex < nname) {
      names[nameIndex] = data[dataIndex].replaceAll("'", "");
      ++nameIndex;
      ++dataIndex;
      if (nameIndex < nname && dataIndex == data.length) {
        //need to read one more line
        line = dataIn.readLine();
        data = line.split(":")[0].trim().split("\\s+");
        dataIndex = 0;
      }
    }

    // Read matrix
    line = dataIn.readLine(); // skipping blank line
    int locaIndex = 0;
    int currentCol = 0;
    for (i = 0; i < ne; ++i) {
      line = dataIn.readLine();
      data = line.split(":")[0].trim().split("\\s+");
      if (data.length != 3) {
        System.out.println("The line is not well formatted for input of A:\n\t"+line);
        System.exit(1);
      }
      acol[i] = Double.parseDouble(data[0].replaceAll("d", "e").replaceAll("D", "e"));
      inda[i] = Integer.parseInt(data[1]);
      if (i == 0) {
        loca[locaIndex] = 1;//storing for Fortran use: 1-based
        ++locaIndex;
        ++currentCol;
      }
      else {
        int tmp = Integer.parseInt(data[2]);
        if (tmp != currentCol) {
          loca[locaIndex] = i+1;
          ++currentCol;
          ++locaIndex;
        }
      }
    }
    loca[n] = ne + 1;

    // Read bl
    line = dataIn.readLine(); // skipping blank line
    int blindex = 0;
    dataIndex = 0;
    line = dataIn.readLine();
    data = line.split(":")[0].trim().split("\\s+");
    while (blindex < bl.length) {
      bl[blindex] = Double.parseDouble(data[dataIndex].replaceAll("d", "e").replaceAll("D", "e"));
      ++blindex;
      ++dataIndex;
      if (blindex < bl.length && dataIndex == data.length) {
        //need to read one more line
        line = dataIn.readLine();
        data = line.split(":")[0].trim().split("\\s+");
        dataIndex = 0;
      }
    }

    // Read bu
    line = dataIn.readLine(); // skipping blank line
    int buindex = 0;
    dataIndex = 0;
    line = dataIn.readLine();
    data = line.split(":")[0].trim().split("\\s+");
    while (buindex < bu.length) {
      bu[buindex] = Double.parseDouble(data[dataIndex].replaceAll("d", "e").replaceAll("D", "e"));
      ++buindex;
      ++dataIndex;
      if (buindex < bu.length && dataIndex == data.length) {
        //need to read one more line
        line = dataIn.readLine();
        data = line.split(":")[0].trim().split("\\s+");
        dataIndex = 0;
      }
    }

    // Read hs
    line = dataIn.readLine(); // skipping blank line
    line = dataIn.readLine();
    data = line.split(":")[0].trim().split("\\s+");
    if (start.equalsIgnoreCase("C")) {
      if (data.length != n) {
        System.out.println("Wrong format for HS data.\n");
        System.exit(1);
      }
    }
    else {
      if (data.length != n+m) {
        System.out.println("Wrong format for HS data.\n");
        System.exit(1);
      }
    }
    for (i = 0; i < data.length; ++i) {
      hs[i] = Integer.parseInt(data[i]);
    }

    // Read x
    line = dataIn.readLine();
    data = line.split(":")[0].trim().split("\\s+");
    if (data.length != n) {
      System.out.println("Wrong format for X data.");
      System.exit(1);
    }
    for (i = 0; i < data.length; ++i) {
      x[i] = Double.parseDouble(data[i].replaceAll("d", "e").replaceAll("D", "e"));
    }

    /* Done reading data file */

    System.out.printf(" \n QP problem contains %3d variables and %3d linear constraints\n", n, m);

    /* Call E04NP to initialize E04NQ. */
    E04NP e04np = new E04NP();
    ifail = 0;
    e04np.eval(cw,LENCW,iw,LENIW,rw,LENRW,ifail);

    /* Set this to .True. to cause e04nqf to produce intermediate
     * progress output. */
    verboseOutput = true;

    if (verboseOutput) {
      /* By default e04nqf does not print monitoring
       * information. Set the print file unit or the summary
       * file unit to get information. */
      E04NT e04nt = new E04NT();
      ifail = 0;
      e04nt.eval("Print file", NOUT, cw, iw, rw, ifail);
    }

    /* We have no explicit objective vector so set LENC = 0; the
     * objective vector is stored in row IOBJ of ACOL. */
    lenc = 0;
    c = new double[Math.max(1,lenc)];
    objadd = 0.0;
    sinf = 0.0;
    obj = Double.NaN;
    prob = "        ";

    /* Do not allow any elastic variables (i.e. they cannot be
     * infeasible). If we'd set optional argument "Elastic mode" to 0,
     * we wouldn't need to set the individual elements of array HELAST. */

    for (int j = 0; j < (n+m); j++) {
      helast[j] = 0;
    }

    /* Solve the QP problem. */

    E04NQ e04nq = new E04NQ();
    ifail = 0;
    ns = 0;
    ninf = 0;
    e04nq.eval(start,new QPHX(ncolh),m,n,ne,nname,lenc,ncolh,iobj,objadd,prob,acol,
        inda,loca,bl,bu,c,names,helast,hs,x,pi,rc,ns,ninf,sinf,obj,cw,LENCW,
        iw,LENIW,rw,LENRW,cuser,iuser,ruser,ifail);

    System.out.println();
    System.out.printf(" Final objective value = %11.3E\n",e04nq.getOBJ());
    System.out.print(" Optimal X = ");
    for (i = 0; i < n; ++i) {
      System.out.printf("%9.2f ", x[i]);
    }
    System.out.println();

  }

  private static void usage() {
    System.out.println("Usage:\n"
        + "\tjava -cp <path_to_NAGJava.jar>" + File.separator + "NAGJava.jar" + File.pathSeparator
        + ". E04NQJE <path_to_data_files_for_java>" + File.separator + "e04nqfe.d");
    System.exit(1);
  }

  public static class QPHX implements E04NQ.E04NQ_QPHX {

    private int NCOLHREF;

    public QPHX(int NCOLHREF) {
      this.NCOLHREF = NCOLHREF;
    }

    // @Override
    public void eval(int NCOLH, double[] X, double[] HX, int NSTATE,
        String[] CUSER, int[] IUSER, double[] RUSER) {

      if (NCOLH != NCOLHREF) {
        System.out.println("NCOLH value is wrong!");
        System.out.println("TEST FAILED");
        System.exit(1);
      }
      HX[0] = 2.0*X[0];
      HX[1] = 2.0*X[1];
      HX[2] = 2.0*(X[2]+X[3]);
      HX[3] = HX[2];
      HX[4] = 2.0*X[4];
      HX[5] = 2.0*(X[5]+X[6]);
      HX[6] = HX[5];

    }

    private String[] CUSER;
    private double[] HX,RUSER,X;
    private int[] IUSER;
    private int NCOLH, NSTATE;

    // @Override
    public String[] getCUSER() {
      return CUSER;
    }

    // @Override
    public double[] getHX() {
      return HX;
    }

    // @Override
    public int[] getIUSER() {
      return IUSER;
    }

    // @Override
    public int getNCOLH() {
      return NCOLH;
    }

    // @Override
    public int getNSTATE() {
      return NSTATE;
    }

    // @Override
    public double[] getRUSER() {
      return RUSER;
    }

    // @Override
    public double[] getX() {
      return X;
    }

    // @Override
    public void setCUSER(String[] arg0) {
      CUSER = arg0;

    }

    // @Override
    public void setHX(double[] arg0) {
      HX = arg0;

    }

    // @Override
    public void setIUSER(int[] arg0) {
      IUSER = arg0;

    }

    // @Override
    public void setNCOLH(int arg0) {
      NCOLH = arg0;

    }

    // @Override
    public void setNSTATE(int arg0) {
      NSTATE = arg0;

    }

    // @Override
    public void setRUSER(double[] arg0) {
      RUSER = arg0;

    }

    // @Override
    public void setX(double[] arg0) {
      X = arg0;

    }

  }




}
