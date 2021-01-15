import com.nag.routines.S.S17DC;
import com.nag.types.NAGComplex;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class S17DCJE{
  private static int n = 2;

  public static void main(String[] args){
    double fnu;
    int ifail, nz = 0;
    String scal;
    NAGComplex z;
    NAGComplex[] cwrk, cy;

    z = new NAGComplex();
    //initiate complex arrays like this to save looping through and doing it manually by looping through
    cwrk = NAGComplex.createArray(n);
    cy = NAGComplex.createArray(n);
    
    System.out.println("S17DCF Example Program Results");

    //Supply file path as argument
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      System.out.println();
      System.out.printf("Calling with N = %d\n", n);
      System.out.println();
      System.out.printf("FNU\tZ\t\t\tSCAL\tCY[0]\t\t\tCY[1]\t\t\tNZ");
      System.out.println();

      while(true){
        line = reader.readLine();
        if(line == null){
          break;
        }
        String[] sVal = line.split("\\s+");
        fnu = Double.parseDouble(sVal[0]);
        z.setRe(Double.parseDouble(sVal[1]));
        z.setIm(Double.parseDouble(sVal[2]));
        scal = sVal[3].substring(1,2);

        ifail = 0;
        S17DC s17dc = new S17DC(fnu, z, n, scal, cy, nz, cwrk, ifail);
        s17dc.eval();

        System.out.printf("%.4f\t(%.4f, %.4f)\t%s\t(%.4f, %.4f)\t(%.4f, %.4f)\t%d\n", fnu, z.getRe(), z.getIm(), scal, cy[0].getRe(), cy[0].getIm(), cy[1].getRe(), cy[1].getIm(), nz); 
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
    
