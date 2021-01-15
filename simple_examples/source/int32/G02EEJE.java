import com.nag.routines.G02.G02EE;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * G02EE example program text.
 */
public class G02EEJE {

  public static void main(String[] args) throws Exception {
    int vnlen = 3;
    double chrss, f, fin, rss;
    rss = chrss = f = Double.NaN;
    int  idf = 0, ifail = 0, ifr = 0, istep = 0, ldq, ldx, lwt, m, maxip, n, nterm = 0;
    boolean addvar = false;
    String mean, weight, newvar;
    newvar = "   ";
    double[] exss, p, q, wk, wt, x, y;
    int[] isx;
    String[] free, model, vname;

    System.out.println(" G02EEJ Example Program Results\n");

    BufferedReader dataIn = new BufferedReader(new FileReader(args[0]));

    String line = dataIn.readLine();

    String[] data = dataIn.readLine().trim().split("::")[0].trim().split("\\s+");

    n = Integer.parseInt(data[0]);
    m = Integer.parseInt(data[1]);
    mean = data[2].substring(1,2);
    weight = data[3].substring(1,2);
    fin = Double.parseDouble(data[4]);

    if (weight.equalsIgnoreCase("W")) {
      lwt = n;
    }
    else {
      lwt = 0;
    }
    ldx = n;

    x = new double[ldx*m];
    y = new double[n];
    wt = new double[lwt];
    isx = new int[m];
    vname = new String[m];

    for (int i = 0; i < vname.length;++i) {
      StringBuilder tmp = new StringBuilder();
      for (int j = 0; j < vnlen; ++j) {
        tmp.append(" ");
      }
      vname[i] = tmp.toString();
    }

    if (lwt > 0) {
      for (int i = 0; i < n; ++i) {
        data = dataIn.readLine().trim().split("::")[0].trim().split("\\s+");
        for (int j = 0; j < m; ++j) {
          x[i+j*ldx] = Double.parseDouble(data[j]);
        }
        y[i] = Double.parseDouble(data[m]);
        wt[i] = Double.parseDouble(data[m+1]);
      }
    }
    else {
      for (int i = 0; i < n; ++i) {
        data = dataIn.readLine().trim().split("::")[0].trim().split("\\s+");
        for (int j = 0; j < m; ++j) {
          x[i+j*ldx] = Double.parseDouble(data[j]);
        }
        y[i] = Double.parseDouble(data[m]);
      }
    }
    data = dataIn.readLine().trim().split("::")[0].trim().split("\\s+");
    for (int i = 0; i < m; ++i) {
      isx[i] = Integer.parseInt(data[i]);
    }

    data = dataIn.readLine().trim().split("::")[0].trim()
        .replaceAll("^'","").replaceAll("'$","").split("'\\s+'");
    for (int i = 0; i < m; ++i) {
      vname[i] = data[i].substring(0,3);
    }

    dataIn.close();

    maxip = 0;
    for (int i = 0; i < m; ++i) {
      if (isx[i] > 0) {
        ++maxip;
      }
    }

    if (mean.equalsIgnoreCase("M")) {
      maxip += 1;
    }

    ldq = n;
    model = new String[maxip];
    free = new String[maxip];
    exss = new double[maxip];
    q = new double[ldq*(maxip+2)];
    p = new double[maxip+1];
    wk = new double[2*maxip];

    for (int i = 0; i < model.length; ++i) {
      StringBuilder tmp = new StringBuilder();
      for (int j = 0; j < vnlen; ++j) {
        tmp.append(" ");
      }
      model[i] = tmp.toString();
    }
    for (int i = 0; i < free.length; ++i) {
      StringBuilder tmp = new StringBuilder();
      for (int j = 0; j < vnlen; ++j) {
        tmp.append(" ");
      }
      free[i] = tmp.toString();
    }
    istep = 0;
    ifail = -1;


    G02EE g02ee = new G02EE(istep,mean,weight,n,m,x,ldx,vname,isx,maxip,y,wt,fin,
        addvar,newvar,chrss,f,model,nterm,rss,idf,ifr,free,exss,q,ldq,p,
        wk,ifail);
    for (int i = 0; i < m; ++i) {
      g02ee.setIFAIL(0);
      g02ee.eval();

      System.out.printf(" Step %2d\n",g02ee.getISTEP());
      if (!g02ee.getADDVAR()) {
        System.out.printf(" No further variables added maximum F =%7.2f\n",g02ee.getF());
        System.out.print(" Free variables:    ");
        for (int j = 0; j < g02ee.getIFR(); ++j) {
          System.out.print("      "+free[j]);
        }
        System.out.println();
        System.out.println(" Change in residual sums of squares for free variables:");
        for (int j = 0; j <  g02ee.getIFR(); ++j) {
          System.out.printf(" %9.4f",g02ee.getEXSS()[j]);
        }
        System.out.println();
        break;
      }
      else {
        System.out.println(" Added variable is "+g02ee.getNEWVAR());
        System.out.printf(" Change in residual sum of squares = %12.4E\n",g02ee.getCHRSS());
        System.out.printf(" F Statistic = %7.2f\n",g02ee.getF());
        System.out.println();
        System.out.print(" Variables in model:");
        for (int j = 0; j < g02ee.getNTERM(); ++j) {
          System.out.print(" "+g02ee.getMODEL()[j]);
        }
        System.out.println("\n");
        System.out.printf(" Residual sum of squares = %13.4E\n",g02ee.getRSS());
        System.out.printf(" Degrees of freedom = %2d\n",g02ee.getIDF());
        System.out.println();
        if (g02ee.getIFR() == 0) {
          System.out.println(" No free variables remaining");
          break;
        }
        System.out.print(" Free variables:    ");
        for (int j = 0; j < g02ee.getIFR(); ++j) {
          System.out.print("      "+free[j]);
        }
        System.out.println();
        System.out.println(" Change in residual sums of squares for free variables:");
        for (int j = 0; j < g02ee.getIFR(); ++j) {
          System.out.printf(" %9.4f",g02ee.getEXSS()[j]);
        }
        System.out.println();

      }

    }

  }

}
