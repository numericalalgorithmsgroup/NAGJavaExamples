import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import com.nag.routines.G03.G03GA;
import com.nag.routines.X04.X04CA;
import com.nag.routines.Routine; 

/**
 * G03GAJ example program text.
 * @author willa
 * @since 27.0.0.0
 */
public class G03GAJE{

  /**
   * G03GAJ example main program.
   */
  public static void main(String[] args){
    int i, ldx, lprob, riter, lds, sds, ifail = 0;
    double[] w, g, s, f;
    double tol, loglik = 0; 

    if(args.length != 1){
      G03GAJE.usage();
    }

    // Data file name as argument
    String filename = args[0];
    // DataHolder object to hold data read in from data file
    DataHolder data = new DataHolder();

    readDataFile(filename, data);

    System.out.println(" G03GAJ Example Program Results");
    System.out.println();

    // Leading dimensions
    ldx = data.n;
    lprob = data.n;

    
    switch(data.sopt){
      case 1:
        s = new double[data.nvar * data.nvar * data.ng];
        lds = data.nvar;
        sds = data.nvar;
        break;
      case 2:
        s = new double[data.nvar * data.nvar * 1];
        lds = data.nvar;
        sds = data.nvar;
        break;
      case 3:
        s = new double[data.nvar * data.ng * 1];
        lds = data.nvar;
        sds = data.ng;
        break;
      case 4:
        s = new double[data.nvar * 1 * 1];
        lds = data.nvar;
        sds = 1;
        break;
      default:
        s = new double[1 * 1 * 1];
        lds = 1;
        sds = 1;
        break;
    }

    // Allocate array size
    g = new double[data.nvar * data.ng];
    w = new double[data.ng];
    f = new double[data.n * data.ng];
    tol = 0.0;
    riter = 5;

    ifail = 0;

    // Create g03ga object with variables from data file
    G03GA g03ga = new G03GA(data.n, data.m, data.x, ldx, data.isx, data.nvar, data.ng, data.popt, data.prob,
                            lprob, data.niter, riter, w, g, data.sopt, s, lds, sds, f, tol, loglik, ifail);

    // Run routine
    g03ga.eval();

    // Update variables
    data.n = g03ga.getN(); 
    data.m = g03ga.getM();
    data.x = g03ga.getX();
    ldx = g03ga.getLDX();
    data.isx = g03ga.getISX();
    data.nvar = g03ga.getNVAR();
    data.ng = g03ga.getNG();
    data.popt = g03ga.getPOPT();
    data.prob = g03ga.getPROB();
    lprob = g03ga.getLPROB();
    data.niter = g03ga.getNITER();
    riter = g03ga.getRITER();
    w = g03ga.getW();
    g = g03ga.getG();
    data.sopt = g03ga.getSOPT();
    s = g03ga.getS();
    lds = g03ga.getLDS();
    sds = g03ga.getSDS();
    f = g03ga.getF(); 
    tol = g03ga.getTOL();
    loglik = g03ga.getLOGLIK();
    ifail = g03ga.getIFAIL();

    // Results
    X04CA x04ca = new X04CA();

    System.out.println();
    ifail = 0; 
    x04ca.eval("g", "n", 1, data.ng, w, 1, "Mixing proportions", ifail);

    System.out.println();
    ifail = 0;
    x04ca.eval("g", "n", data.nvar, data.ng, g, data.nvar, "Group means", ifail);

    System.out.println();
    switch(data.sopt){
      case 1:
        for(i = 0; i < data.ng; i++){
          ifail = 0;
          // Wrapper returns a 1-dimensional array so X04CA has to be called like this to mimic
          // calling X04CA along the 3rd dimension
          x04ca.eval("g", "n", data.nvar, data.nvar,
                     Arrays.copyOfRange(s, (i * (data.nvar * data.nvar)), (data.nvar * data.nvar * data.ng)),
                     lds, "Variance-cavariance matrix", ifail);
        }
        break;
      case 2:
        ifail = 0;
        x04ca.eval("g", "n", data.nvar, data.nvar, s, lds, "Pooled Variance-covariance matrix", ifail);
        break;
      case 3:
        ifail = 0;
        x04ca.eval("g", "n", data.nvar, data.ng, s, lds, "Groupwise Variance", ifail);
        break;
      case 4:
        ifail = 0;
        x04ca.eval("g", "n", data.nvar, 1, s, lds, "Pooled Variance", ifail);
        break;
      case 5:
        ifail = 0;
        x04ca.eval("g", "n", 1, 1, s, lds, "Overall Variance", ifail);
        break;
    }

    System.out.println();
    ifail = 0;
    x04ca.eval("g", "n", data.n, data.ng, f, data.n, "Densities", ifail);

    System.out.println();
    ifail = 0;
    x04ca.eval("g", "n", data.n, data.ng, data.prob, data.n, "Membership probabilities", ifail);

    System.out.println();
    System.out.println("No. iterations: " + data.niter);
    System.out.printf("Log-likelihood: %.04f\n", loglik); 
  }

  /**
   * Read data from given filename and puts into DataHolder object
   * @param filename 
   *              Name of data file (absolute or relative path)
   * @param data
   *              DataHolder object to store data from data file
   */
  public static void readDataFile(String filename, DataHolder data){
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      // Problem size
      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      data.setN(Integer.parseInt(sVal[0]));
      data.setM(Integer.parseInt(sVal[1]));
      data.setNvar(Integer.parseInt(sVal[2]));

      // Number of groups
      line = reader.readLine();
      sVal = line.split("\\s+");
      data.setNg(Integer.parseInt(sVal[0]));

      // Scaling option
      line = reader.readLine();
      sVal = line.split("\\s+");
      data.setSopt(Integer.parseInt(sVal[0]));

      // Initial probabilities option
      line = reader.readLine();
      sVal = line.split("\\s+");
      data.setPopt(Integer.parseInt(sVal[0]));

      // Maximum number of iterations
      line = reader.readLine();
      sVal = line.split("\\s+");
      data.setNiter(Integer.parseInt(sVal[0]));

      //
      data.x = new double[data.m * data.n];
      data.prob = new double[data.ng * data.n];
      data.isx = new int[data.m];

      //Data matrix X
      for(int i = 0; i < data.n; ++i){
        line = reader.readLine().trim();
        sVal = line.split("\\s+");
        for(int j = 0; j < data.m; ++j){
          data.x[i + (j * data.n)] = Double.parseDouble(sVal[j]);
        }
      }

      //Included variables
      if(data.nvar != data.m){
        line = reader.readLine().trim();
        sVal = line.split("\\s+");
        for(int i = 0; i < data.m; ++i){
          data.isx[i] = Integer.parseInt(sVal[i]);
        }
      }
        

      //Optionally read initial probabilities of group memebership (included in example data) 
      if(data.popt == 2){
        for(int i = 0; i < data.n; ++i){
          line = reader.readLine().trim();
          sVal = line.split("\\s+");
          for(int j = 0; j < data.ng; ++j){
            data.prob[i + (j * data.n)] = Double.parseDouble(sVal[j]);
          }
        }
      }
    }
    catch(FileNotFoundException e){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException e){
      System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
    }

    
  }

  /**
   * Stores data from data file for later reference
   */ 
  private static class DataHolder{
    private int n;
    private int m;
    private int nvar;
    private int ng;
    private int sopt;
    private int popt;
    private int niter;
    private int isx[];
    private double x[];
    private double prob[];

    public void setN(int n){
      this.n = n;
    }

    public int getN(){
      return n; 
    }

    public void setM(int m){
      this.m = m;
    }

    public int getM(){
      return m; 
    }

    public void setNvar(int nvar){
      this.nvar = nvar;
    }

    public int getNvar(){
      return nvar; 
    }

    public void setNg(int ng){
      this.ng = ng;
    }

    public int getNg(){
      return ng; 
    }

    public void setSopt(int sopt){
      this.sopt = sopt;
    }

    public int getSopt(){
      return sopt; 
    }

    public void setPopt(int popt){
      this.popt = popt;
    }

    public int getPopt(){
      return popt; 
    }

    public void setNiter(int niter){
      this.niter = niter;
    }

    public int getNiter(){
      return niter; 
    }

    public void setIsx(int[] isx){
      this.isx = isx;
    }

    public int[] getIsx(){
      return isx;
    }

    public void setX(double[] x){
      this.x = x;
    }

    public double[] getX(){
      return x;
    }

    public void setProb(double[] prob){
      this.prob = prob;
    }

    public double[] getProb(){
      return prob;
    }
  }

  /**
   * No arguments supplied when example runs
   */
  private static void usage() {
    System.err.println("Please specify the path to the data file.");
    System.exit(-1);
  }
}

