import com.nag.routines.S.S14AC;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * S14ACJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class S14ACJE{

  /**
   * S14ACJ example main program
   */
  public static void main(String[] args){
    double f, x;
    int ifail;

    System.out.println("S14ACJ Example Program Results");

    //Supply file path as terminal input
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    System.out.println();
    System.out.printf("\tX\tpsi(X)-log(X)\n");
    System.out.println();
    
    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      //Loop until break...
      while(true){
        line = reader.readLine();
        if(line == null){
          break;
        }
        String[] sVal = line.split("\\s+");

        x = Double.parseDouble(sVal[0]);

        ifail = -1;
        S14AC s14ac = new S14AC(x, ifail);
        f = s14ac.eval();
        ifail = s14ac.getIFAIL();

        if(ifail < 0){
          break;
        }

        System.out.printf("\t%.4f\t%.4f\n", x, f);
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
}
