import com.nag.routines.G05.G05KF;
import com.nag.routines.G05.G05SA;
import com.nag.routines.Routine;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * G05KFJ example program text.
 * @author willa
 * @since 27.0.0.0
 */
public class G05KFJE{

  /**
   * G05KFJ example main program.
   */
  public static void main(String[] args){
    //genid, subid only initilised so they can be set in try{} without java throwing error
    int lseed = 1, nin = 5, nout = 6, genid = 0, n = 0, subid = 0;
    int  ifail, lstate;
    int[] seed, state;
    double x[];
    
    System.out.println("G05KFJ Example Program Results");
    System.out.println();

    //No file input given
    if(args.length != 1){
      G05KFJE.usage();
    }

    seed = new int[lseed];

    //Read in data from data fiel
    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      //Read in the base generator information and seed
      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      genid = Integer.parseInt(sVal[0]);
      subid = Integer.parseInt(sVal[1]);
      seed[0] = Integer.parseInt(sVal[2]);

      //Read in sample size
      line = reader.readLine();
      sVal = line.split("\\s+");
      n = Integer.parseInt(sVal[0]); 
      
    }
    catch(FileNotFoundException e){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException e){
      System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
    }
    
    lstate = 0;
    ifail = 0;
    state = new int[lstate];

    //Initial call to get size of STATE array
    G05KF g05kf = new G05KF(genid, subid, seed, lseed, state, lstate, ifail);
    g05kf.eval();

    //Update local variables
    lstate = g05kf.getLSTATE();
    state = g05kf.getSTATE();

    //Reallocate STATE
    state = new int[lstate];

    //Update object variables
    g05kf.setSTATE(state);
    g05kf.eval();

    //Update local variables
    state = g05kf.getSTATE();

    x = new double[n];

    //Generate the variates
    ifail = 0;
    G05SA g05sa = new G05SA(n, state, x, ifail);
    g05sa.eval();

    //Display the variates
    for(int i = 0; i < x.length; i++){
      System.out.printf("%.4f\n", x[i]);
    }
  }

  /**
   * Print usage information.
   */
  private static void usage(){
    System.err.println("Please specify the path to the data file.");
    System.exit(-1);
  }
}
