import com.nag.routines.X03.X03AA;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * X03AAJ example program text.
 * @author willa
 * @since 27.1.0.0
 */
public class X03AAJE{

  /**
   * X03AAJ example main program.
   */
  public static void main(String[] args){
    int n = 3;
    double c1 = 0, c2 = 0, d1 = 0, d2 = 0; //placeholder
    int ifail, isizea, isizeb, istepa, istepb;
    boolean sw;
    double[] a, b;

    a = new double[n * n];
    b = new double[n];
   
    System.out.println("X03AAF Example Program Results");
    System.out.println();
    
    if(args.length != 1){
      System.err.println("Please specify the path to the data file.");
      System.exit(-1);
    }
    
    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      //stored column wise
      //a = (-2, -3,  7)
      //    ( 2, -5,  3)
      //    (-9,  1,  0)
      for(int i = 0; i < n; i++){
        line = reader.readLine();
        String[] sVal = line.split("\\s+");
        for(int j = 0; j < n; j++){
          a[i + (j * n)] = Double.parseDouble(sVal[j + 1]);
        }
      }

      line = reader.readLine();
      for(int i = 0; i < n; i++){
        String[] sVal = line.split("\\s+");
        b[i] = Double.parseDouble(sVal[i + 1]);
      }
    }
    catch(FileNotFoundException e){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException e){
      System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
    }

    
    c1 = 1;
    c2 = 0;
    isizea = n;
    isizeb = n;
    istepa = 1;
    istepb = 1;
    sw = true;

    ifail = 0;
    X03AA x03aa = new X03AA(a , isizea, b, isizeb, n, istepa, istepb, c1, c2, d1, d2, sw, ifail);
    x03aa.eval();

    //update
    c1 = x03aa.getC1();
    c2 = x03aa.getC2();
    d1 = x03aa.getD1();
    d2 = x03aa.getD2();

    System.out.printf("D1 = %.1f D2 = %.1f\n", d1, d2);
  }
}
