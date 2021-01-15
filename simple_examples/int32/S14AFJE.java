import com.nag.routines.S.S14AF;
import com.nag.routines.Routine;
import com.nag.types.NAGComplex;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * S14AFJ example program text
 * @author willa
 * @since 27.1.0.0
 */
public class S14AFJE{

  /**
   * S14AFJ Example main program
   */
  public static void main(String[] args){
    NAGComplex y, z;
    int ifail, k;

    //Tell the wrapper the type of complex being used before calling a routine
    Routine.setComplex(new NAGComplex()); 

    y = new NAGComplex();
    z = new NAGComplex();
    
    System.out.println("S14AFJ Example Program Results");
    
    //Supply file path as terminal input
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine();

      System.out.println();
      System.out.println("\tZ\t\tK\t(d^K/dz^K)psi(Z)");
      System.out.println();
      
      //loop until break...
      while(true){
        line = reader.readLine();

        //end of file reached
        if(line == null){
          break;
        }
        
        String[] sVal = line.split("\\s+");
        z.setRe(Double.parseDouble(sVal[0]));
        z.setIm(Double.parseDouble(sVal[1]));
        k = Integer.parseInt(sVal[2]);

        ifail = -1;
        S14AF s14af = new S14AF(z, k, ifail);
        y = (NAGComplex) s14af.eval();
        ifail = s14af.getIFAIL();

        //ifail < 0 => error
        if(ifail < 0){
          break;
        }
        System.out.printf("\t(%.1f, %.1f)\t%d\t(%.4e, %.4e)\n", z.getRe(), z.getIm(), k, y.getRe(), y.getIm());
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
