import com.nag.routines.C05.C05BB;
import com.nag.types.NAGComplex;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * C05BBJ Example program text.
 * @author willa
 * @since 27.1.0.0
 */
public class C05BBJE{

  /**
   * C05BBJE main program text
   */
  public static void main(String[] args){
    NAGComplex w, z;
    double resid = 0;
    int branch = 0, ifail = 0; //placeholder
    boolean offset = false; //placeholder

    //need to initialise first
    z = new NAGComplex();
    w = new NAGComplex();

    System.out.println("C05BBJ Example Program Results");

    if(args.length != 1){
      System.out.println("Please specify path to data file as input");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      branch = Integer.parseInt(sVal[0]);

      line = reader.readLine();
      sVal = line.split("\\s+");
      offset = Boolean.parseBoolean(sVal[0]);

      System.out.printf("Branch = %d\n", branch);
      System.out.printf("Offset = %b\n", offset);

      System.out.println();
      System.out.printf("\t\tZ\t\t\tW(Z)\t\t\t\tRESID\tIFAIL\n");
      System.out.println();

      while(true){
        line = reader.readLine();
        if(line == null){
          break;
        } 
        sVal = line.split("\\s+");
        
        z.setRe(Double.parseDouble(sVal[0]));
        z.setIm(Double.parseDouble(sVal[1]));

        ifail = -1;
        C05BB c05bb = new C05BB(branch, offset, z, w, resid, ifail);
        c05bb.eval();

        z = (NAGComplex) c05bb.getZ();
        w = (NAGComplex) c05bb.getW();
        resid = c05bb.getRESID();
        ifail = c05bb.getIFAIL();

        if(ifail < 0){
          break;
        }

        System.out.printf("(%.5e, %.5e)\t (%.5e, %.5e)\t  %.5e\t   %d\n", z.getRe(), z.getIm(), w.getRe(), w.getIm(), resid, ifail);
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
