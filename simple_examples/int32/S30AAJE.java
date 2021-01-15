import com.nag.routines.S.S30AA;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * S30AAJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class S30AAJE{

  /**
   * S30AAJ main program
   */
  public static void main(String[] args){
    double q = 0, r = 0, s = 0, sigma = 0;
    int ifail, ldp, m = 0, n = 0; 
    String calput = ""; //placeholders
    double[] p, t, x;

    //placeholders
    t = new double[0];
    x = new double[0];
    
    System.out.println("S30AAJ Example Program Results");

    //Supply file path as arugment
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      calput = sVal[0].substring(1,2);

      line = reader.readLine();
      sVal = line.split("\\s+");
      s = Double.parseDouble(sVal[0]);
      sigma = Double.parseDouble(sVal[1]);
      r = Double.parseDouble(sVal[2]);
      q = Double.parseDouble(sVal[3]);

      line = reader.readLine();
      sVal = line.split("\\s+");
      m = Integer.parseInt(sVal[0]);
      n = Integer.parseInt(sVal[1]);
      
      t = new double[n];
      x = new double[m];

      for(int i = 0; i < m; i++){
        line = reader.readLine();
        sVal = line.split("\\s+");
        x[i] = Double.parseDouble(sVal[0]);
      }
      for(int i = 0; i < n; i++){
        line = reader.readLine();
        sVal = line.split("\\s+");
        t[i] = Double.parseDouble(sVal[0]);
      }
    }
    catch(FileNotFoundException e){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException e){
      System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
    }

    ldp = m;
    p = new double[ldp * n];
    
    ifail = 0;
    S30AA s30aa = new S30AA(calput, m, n, x, s, t, sigma, r, q, p, ldp, ifail);
    s30aa.eval();

    System.out.println();
    System.out.println("Black-Scholes-Merton formula");

    if(calput.toLowerCase().equals("c")){
      System.out.println("European Call : ");
    }
    else if(calput.toLowerCase().equals("p")){
      System.out.println("European Put : ");
    }

    System.out.printf("  Spot       =\t%.4f\n", s);
    System.out.printf("  Volatility =\t%.4f\n", sigma);
    System.out.printf("  Rate       =\t%.4f\n", r);
    System.out.printf("  Dividend   =\t%.4f\n", q);

    System.out.println();
    System.out.printf("    Strike\tExpiry\tOption Price\n");
    for(int i = 0; i < m; i++){
      for(int j = 0; j < n; j++){
        System.out.printf("    %.4f\t%.4f\t%.4f\n", x[i], t[j], p[i + (j * 3)]);
      }
    }
  }
}
  
