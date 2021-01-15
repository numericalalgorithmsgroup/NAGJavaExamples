import com.nag.routines.E04.E04NF;
import com.nag.routines.E04.E04NH;
import com.nag.routines.E04.E04WB;
import com.nag.routines.E04.E54NFU;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * E04NF example program text.
 */
public class E04NFJE {

  public static void main(String[] args)  {

    /* Local Scalars */
    double obj;
    int i, ifail, iter, lda, ldh, liwork, lwork, n, nclin, sda;
    /* Local Arrays */
    double[] a, ax, bl, bu, clamda, cvec, h, work, x;
    int[] istate, iwork;

    try {

      System.out.println(" E04NFJ Example Program Results");

      if (args.length == 0) {
        System.err.println("You need to provide the path to the data file e04nfae.d");
        System.exit(-1);
      }

      // Read data from file
      DataFile datafile = new DataFile(args[0]);
      datafile.read();

      liwork = 2 * datafile.n + 3;
      lda = Math.max(1, datafile.nclin);

      sda = (datafile.nclin > 0) ? datafile.n : 1;

      // This particular example problem is of type QP2 with H stored explicitly,
      // so we allocate CVEC(N) and H(LDH,N), and define LDH and LWORK as below
      ldh = datafile.n;

      if (datafile.nclin > 0) {
        lwork = 2 * datafile.n * datafile.n + 8 * datafile.n + 5 * datafile.nclin;
      }
      else {
        lwork = datafile.n * datafile.n + 8 * datafile.n;
      }

      istate = new int[datafile.n + datafile.nclin];
      ax = new double[Math.max(1, datafile.nclin)];
      iwork = new int[liwork];
      // h, bl, bu, cvec, x, a: already read from data file
      clamda = new double[datafile.n + datafile.nclin];
      work = new double[lwork];

      // Init routine
      E04WB e04wb = new E04WB();
      int lcwsav = 1, liwsav = 610, llwsav = 120, lrwsav = 475;
      String[] cwsav = new String[lcwsav];
      cwsav[0]
          = "                                                                                 ";
      int[] iwsav = new int[liwsav];
      boolean[] lwsav = new boolean[llwsav];
      double[] rwsav = new double[lrwsav];
      ifail = 0;
      e04wb.eval("E04NFA", cwsav, lcwsav, lwsav, llwsav, iwsav, liwsav, rwsav, lrwsav, ifail);

      // Set print level to match E04NFF example output
      E04NH e04nh = new E04NH();
      int inform = 0;
      e04nh.eval("Print Level = 10", lwsav, iwsav, rwsav, inform);

      // Solve the problem
      E04NF e04nf = new E04NF();
      double[] ruser = new double[1];
      int[] iuser = new int[1];
      iter = 0;
      obj = Double.NaN;
      ifail = 0;
      e04nf.eval(datafile.n, datafile.nclin, datafile.a, datafile.lda, datafile.bl,
          datafile.bu, datafile.cvec, datafile.h, datafile.ldh, new QPHESS(), istate,
          datafile.x, iter, obj, ax, clamda, iwork, liwork, work, lwork,
          iuser, ruser, lwsav, iwsav, rwsav, ifail);

      ifail = e04nf.getIFAIL();

    }
    catch (Exception ex) {
      Logger.getLogger(E04NFJE.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  /** Using E54NFU as a default. */
  public static class QPHESS extends E54NFU implements E04NF.E04NF_QPHESS {

    public void eval() {
      super.eval();
    }

  }

  public static class DataFile {

    public String filename;
    public int n, nclin, lda, sda, ldh;
    public double[] cvec, a, bl, bu, x, h;

    public DataFile(String filename) {
      this.filename = filename;
    }

    public void read() throws FileNotFoundException, IOException {
      BufferedReader dataIn = new BufferedReader(new FileReader(filename));
      String line = dataIn.readLine(); // skipping header
      line = dataIn.readLine().trim();
      String[] lines = line.split(":");//removing comments
      String[] dataLine = lines[0].split("\\s+");
      if (dataLine.length != 2) {
        System.err.println("Something went wrong when reading the data file"
            + " - can't read n and nclin!");
        System.exit(1);
      }
      n = Integer.parseInt(dataLine[0]);
      nclin = Integer.parseInt(dataLine[1]);

      lda = Math.max(1, nclin);

      if (nclin > 0) {
        sda = n;
      }
      else {
        sda = 1;
      }
      ldh = n;

      cvec = new double[n];
      a = new double[lda * sda];
      bl = new double[n + nclin];
      bu = new double[n + nclin];
      x = new double[n];
      h = new double[ldh * n];

      //reading cvec
      line = dataIn.readLine().trim().split(":")[0].trim();
      dataLine = line.split("\\s+");
      if (dataLine.length != n) {
        System.err.println("Something went wrong when reading the data file"
            + " - not enought or too many data for cvec");
        System.exit(1);
      }

      for (int i = 0; i < n; ++i) {
        cvec[i] = Double.parseDouble(dataLine[i].trim().replaceAll("D", "E"));
      }

      //reading a
      for (int i = 0; i < lda; ++i) {
        line = dataIn.readLine().trim().split(":")[0].trim();
        dataLine = line.split("\\s+");
        if (dataLine.length != sda) {
          System.err.println("Something went wrong when reading the data file"
              + " - not enought or too many data for a");
          System.exit(1);
        }
        for (int j = 0; j < sda; ++j) {
          a[i + j * lda] = Double.parseDouble(dataLine[j].trim().replaceAll("D", "E"));
        }
      }

      //reading bl
      //it's on two lines in the data file, so we need to merge them
      line = dataIn.readLine().trim().split(":")[0].trim() + " "
          + dataIn.readLine().trim().split(":")[0].trim();
      dataLine = line.split("\\s+");
      if (dataLine.length != n + nclin) {
        System.err.println("Something went wrong when reading the data file"
            + " - not enought or too many data for bl");
        System.exit(1);
      }
      for (int i = 0; i < n + nclin; ++i) {
        bl[i] = Double.parseDouble(dataLine[i].replaceAll("D", "E"));
      }

      //reading bu
      //it's on two lines in the data file, so we need to merge them
      line = dataIn.readLine().trim().split(":")[0].trim() + " "
          + dataIn.readLine().trim().split(":")[0].trim();
      dataLine = line.split("\\s+");
      if (dataLine.length != n + nclin) {
        System.err.println("Something went wrong when reading the data file"
            + " - not enought or too many data for bu");
        System.exit(1);
      }
      for (int i = 0; i < n + nclin; ++i) {
        bu[i] = Double.parseDouble(dataLine[i].replaceAll("D", "E"));
      }

      line = dataIn.readLine().trim().split(":")[0].trim();
      dataLine = line.split("\\s+");
      if (dataLine.length != n) {
        System.err.println("Something went wrong when reading the data file"
            + " - not enought or too many data for x");
        System.exit(1);
      }
      for (int i = 0; i < n; ++i) {
        x[i] = Double.parseDouble(dataLine[i].replaceAll("D", "E"));
      }

      for (int i = 0; i < ldh; ++i) {
        line = dataIn.readLine().trim().split(":")[0].trim();
        dataLine = line.split("\\s+");
        if (dataLine.length != n) {
          System.err.println("Something went wrong when reading the data file"
              + " - not enought or too many data for h at line " + i);
          System.exit(1);
        }
        for (int j = 0; j < n; ++j) {
          h[i + j * ldh] = Double.parseDouble(dataLine[j].replaceAll("D", "E"));
        }
      }


    }
  }

}
