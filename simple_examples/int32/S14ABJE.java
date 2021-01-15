import com.nag.routines.S.S14AB;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * S14ABJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class S14ABJE{

  /**
   * S14ABJ example main program
   */
  public static void main(String[] args){
    double x, y;
    int ifail;

    System.out.println("S14ABJ Example Program Results");

    //Supply file path as argument
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    System.out.println();
    System.out.printf("\tX\t\tY");
    System.out.println();
    
    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      //loop until break
      while(true){
        line = reader.readLine();

        //if finished looping through file break
        if(line == null){
          break;
        }
        String[] sVal = line.split("\\s+");

        x = Double.parseDouble(sVal[1]);

        ifail = -1;
        S14AB s14ab = new S14AB(x, ifail);
        y = s14ab.eval();
        //update ifail
        ifail = s14ab.getIFAIL();

        if(ifail < 0){
          break;
        }
        System.out.printf("\t%.3e\t%.3e\n", x, y);
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
      
