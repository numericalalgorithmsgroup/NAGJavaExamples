import com.nag.routines.G02.G02BU;
import com.nag.routines.G02.G02DA;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * G02DAJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class G02DAJE{

  /**
   * G02DAJ Example main program
   */
  public static void main(String[] args){
    double aic, arsq, en, mult, rsq, rss = 0, sw = 0, tol;
    int idf = 0, ifail, ip, irank = 0, ldq, ldx, lwt, m, n;
    boolean svd = false;
    String mean, weight;
    double[] b, cov, h, p, q, res, se, wk, wt, x, y, c, wmean;
    int[] isx;

    System.out.println("G02DAJ Example Program Results");
    System.out.println();

    c = new double[1];
    wmean = new double[1];

    //Problem size (can be read in from data file
    n = 12;
    m = 4;
    weight = "U";
    mean = "M";

    if(weight.toLowerCase().equals("w")){
      lwt = n;
    }
    else{
      lwt = 0;
    }
    ldx = n;

    x = new double[ldx * m];
    y = new double[n];
    wt = new double[n];
    isx = new int[m];

    //Supply file path as terminal input
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header
      line = reader.readLine(); //skip n, m, weight, mean

      //Read in data
      String[] sVal;
      for(int i = 0; i < n; i++){
        line = reader.readLine();
        sVal = line.split("\\s+");
        for(int j = 0; j < m; j++){
          x[(j * n) + i] = Double.parseDouble(sVal[j]);
        }
        y[i] = Double.parseDouble(sVal[m]);
        if(lwt > 0){
          wt[i] = Double.parseDouble(sVal[m + 1]);
        }
      }

      //Read in variable inclusion flags
      line = reader.readLine();
      sVal = line.split("\\s+");
      for(int i = 0; i < m; i++){
        isx[i] = Integer.parseInt(sVal[i]);
      }
    }
    catch(FileNotFoundException e){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException e){
      System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
      System.exit(-2);
    }

    //Calcluate IP
    ip = 0;
    for(int i = 0; i < m; i++){
      if(isx[i] == 1){
        ip++;
      }
    }
    if(mean.toLowerCase().equals("m")){
      ip = ip + 1;
    }

    //Allocate
    ldq = n;
    b = new double[ip];
    cov = new double[((ip * ip) + ip)/2];
    h = new double[n];
    p = new double[ip * ip * (ip + 2)];
    q = new double[ldq * (ip + 1)];
    res = new double[n];
    se = new double[ip];
    wk = new double[ip * ip + (5 * (ip - 1))];

    //Use suggested value for tolerance
    tol = 0.000001;

    //fit general linear regression model
    ifail = -1;
    G02DA g02da = new G02DA(mean, weight, n, x, ldx, m, isx, ip, y, wt, rss, idf, b, se, cov, res, h, q,
                            ldq, svd, irank, p, tol, wk, ifail);
    g02da.eval();
    ifail = g02da.getIFAIL();
    if(ifail != 0){
      if(ifail != 5){
        System.exit(-3);
      }
    }

    //Calculate (weighted) total sums of squares, adjusted for mean if required
    //If in G02DAF, an intercept is added to the regression by including a comlumn of
    //1's in X, rather than by using the MEAN argument then MEAN = "M" should be used
    //in this call to G02BUF
    ifail = 0;
    G02BU g02bu = new G02BU(mean, weight, n, 1, y, n, wt, sw, wmean, c, ifail);
    g02bu.eval();

    idf = g02da.getIDF();
    irank = g02da.getIRANK();
    //Get effective number of observations (=N if there are no zero weights)
    en = (double) idf + irank;

    rss = g02da.getRSS();
    //Calculate R-squared, corrected R-Squared and AIC
    rsq = 1 - rss/c[0];
    if(mean.toLowerCase().equals("m")){
      mult = (en - 1) / (en - irank);
    }
    else{
      mult = en / (en - irank);
    }
    arsq = 1 - mult * (1 - rsq);
    aic = en * Math.log(rss/en) + (2 * irank);

    svd = g02da.getSVD();
    //Disply results
    if(svd){
      System.out.printf("Model not of full rank, rank = %d", irank);
      System.out.println();
    }
    System.out.printf("Residual sum of squares = %.4e\n", rss);
    System.out.printf("Degrees of freedom      = %d\n", idf);
    System.out.printf("R-squared               = %.4e\n", rsq);
    System.out.printf("Adjusted R-squared      = %.4e\n", arsq);
    System.out.printf("AIC                     = %.4e\n", aic);
    System.out.println();
    System.out.printf("Variable\tParameter estimate\tStandard error\n");
    System.out.println();
    if(ifail == 0){
      for(int i = 0; i < ip; i++){
        System.out.printf("   %d\t\t  %.4e\t\t  %.4e\n", (i + 1), b[i], se[i]);
      }
    }
    else{
      for(int i = 0; i < ip; i++){
        System.out.printf("   %d\t\t  %.4e\n", (i + 1), b[i]);
      }
    }
    System.out.println();
    System.out.printf("   Obs\t\t    Residuals\t\t    H\n");
    System.out.println();
    for(int i = 0; i < n; i++){
      System.out.printf("   %d\t\t  %.4e\t\t  %.4e\n", (i + 1), res[i], h[i]);
    }
    
  }
}
