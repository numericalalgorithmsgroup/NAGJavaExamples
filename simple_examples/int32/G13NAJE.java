import com.nag.routines.G13.G13NA;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * G13NAJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class G13NAJE{

  /**
   * G13NAJ example main program
   */
  public static void main(String[] args){
    double beta = 0;
    int ctype = 0, ifail, iparam = 0, minss = 0, n = 0, ntau = 0; //placeholder
    double[] param, sparam, y;
    int[] tau;

    param = new double[1];
    //Placeholder y to be read in from data file
    y = new double[0];

    System.out.println("G13NAJ Example Program Results");
    System.out.println();
    
    //Supply file path as terminal input
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      //Read in the problem size
      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      n = Integer.parseInt(sVal[0]);

      //Allocate enough size to hold the input series
      y = new double[n];

      //Read in the input series
      for(int i = 0; i < 10; i++){
        line = reader.readLine();
        sVal = line.split("\\s+");
        for(int j = 0; j < 10; j++){
          y[(i * 10) + j] = Double.parseDouble(sVal[j]);
        }
      }

      //Read in the type of change point, penalty and minimum segment size
      line = reader.readLine();
      sVal = line.split("\\s+");
      ctype = Integer.parseInt(sVal[0]);
      iparam = Integer.parseInt(sVal[1]);
      beta = Double.parseDouble(sVal[2]);
      minss = Integer.parseInt(sVal[3]);

      //Read in the distribution parameter (if required)
      if(iparam == 1){
        line = reader.readLine();
        sVal = line.split("\\s+");
        param[0] = Double.parseDouble(sVal[0]);
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

    //Output arrays
    tau = new int[n];
    sparam = new double[(2 * n) + 2];

    ifail = -1;

    //Call routine to detece change points
    G13NA g13na = new G13NA(ctype, n, y, beta, minss, iparam, param, ntau, tau, sparam, ifail);
    g13na.eval();

    //update
    ifail = g13na.getIFAIL();
    ctype = g13na.getCTYPE();
    ntau = g13na.getNTAU();

    if(ifail == 0 || ifail == 200 || ifail == 201){
      //Display the results
      if(ctype == 5 || ctype == 6){
        //Exponential or Poisson distribtion
        System.out.printf(" -- Charge Points --     Distribution\n");
        System.out.printf(" Number     Position      Parameter  \n");
        System.out.println("=====================================");
        for(int i = 0; i < ntau; i++){
          System.out.printf("   %d\t       %d\t%.2f", i + 1, tau[i], sparam[i]);
        }
          
      }
      else{
        //Normal of Gamma distribution
        System.out.printf(" -- Charge Points --         --- Distribution ---\n");
        System.out.printf(" Number     Position              Parameters\n");
        System.out.println("=================================================");
        for(int i = 0; i < ntau; i++){
          System.out.printf("   %d\t       %d\t     %.2f\t     %.2f\n", (i + 1), tau[i], sparam[2 * i], sparam[(2 * i) + 1]);
        }
      }
    }
    if(ifail == 200 || ifail == 201){
      System.out.println("Some truncation occured internally to avoid overflow");
    }
  }
}
