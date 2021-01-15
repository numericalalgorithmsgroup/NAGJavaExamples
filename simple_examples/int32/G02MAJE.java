import com.nag.routines.G02.G02MA;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays; 

/**
 * G02MAJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class G02MAJE{

  /**
   * G02MAJ example main program
   */
  public static void main(String[] args){
    int ifail, ip, ldb, ldd, lisx, lropt, m, mnstep, mtype, n, nstep = 0, pred, prey;
    double[] b, d, fitsum, ropt, y;
    int[] isx;

    isx = new int[0]; //placeholder
    
    System.out.println("G02MAJ Example Program Results");
    System.out.println();

    //data (Could also read in from data file)
    //Problem size
    n = 20;
    m = 6;

    //Model Specification
    mtype = 1;
    pred = 3;
    prey = 1;
    mnstep = 6;
    lisx = 0;

    //Variable inclusion flags aren't needed in this example
    ip = m;

    //Optional arguments (using defaults)
    lropt = 0;
    ropt = new double[lropt];

    //D and Y
    ldd = n;
    y = new double[n];
    d = new double[ldd * m];

    //Read in D and Y from data file (too large to write out)
    //Supply file path as terminal input
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header
      line = reader.readLine(); //skip N, M
      line = reader.readLine(); //skip mtype, pred, prey, mnstep, lisx

      String sVal[];
      
      for(int i = 0; i < n; i++){
        line = reader.readLine();
        sVal = line.split("\\s+");
        for(int j = 0; j < m; j++){
          d[(j * n) + i] = Double.parseDouble(sVal[j]);
        }
        y[i] = Double.parseDouble(sVal[m]);
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
    
    //Allocate output arrays
    ldb = ip;
    b = new double[ldb * (mnstep + 2)];
    fitsum = new double[6 * (mnstep + 1)];

    //Call the model fitting routine
    ifail = -1;
    G02MA g02ma = new G02MA(mtype, pred, prey, n, m, d, ldd, isx, lisx, y, mnstep, ip, nstep, b, ldb,
                            fitsum, ropt, lropt, ifail);
    g02ma.eval();
    ifail = g02ma.getIFAIL();
    if(ifail != 0){
      if(ifail != 112 && ifail != 161 && ifail != 162 && ifail !=163){
        //ifail = 112, 161, 162, 163 are warnings, so no need to terminate
        System.exit(-2);
      }
    }

    //update
    nstep = g02ma.getNSTEP();
    
    //Display the parameter estimates
    System.out.printf(" Step\t\t\tParameter Estimate\n");
    System.out.println("------------------------------------------------------");
    for(int i = 0; i < nstep; i++){
      System.out.printf("  %d", (i + 1));
      for(int j = 0; j < ip; j++){
        System.out.printf("\t%.3f", b[j + (i * nstep)]);
      }
      System.out.printf("\n");
    }
    System.out.println();
    System.out.printf("alpha: %.3f\n", fitsum[nstep * 6]);
    System.out.println();
    System.out.printf(" Step    Sum\tRSS\t df\t  Cp\t  Ck\tStep Size\n");
    System.out.println("---------------------------------------------------------");
    for(int k = 0; k < nstep; k++){
      System.out.printf("  %d     %.3f\t%.3f %d\t %.3f\t %.3f   %.3f\n", (k + 1), fitsum[k * nstep], fitsum[(k * nstep) + 1],
                        (int)(Math.floor(fitsum[(k + nstep) + 2] + 0.5)), fitsum[(k * nstep) + 3],
                        fitsum[(k * nstep) + 4], fitsum[(k * nstep) + 5]);
    }
    System.out.println();
    System.out.printf("sigma^2: %.3f\n", fitsum[(nstep * 6) + 4]); 
  }
}
