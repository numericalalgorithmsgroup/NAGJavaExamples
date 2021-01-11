import com.nag.routines.F03.F03BA;
import com.nag.routines.F07.F07AD;
import com.nag.routines.X04.X04CA;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * F03BAJ example program text.
 * @author willa
 * @since 27.1.0.0
 */
public class F03BAJE{

  /**
   * F03BAJ example main program.
   */
  public static void main(String[] args){
    double d = 0;
    int id = 0, ifail, info = 0, lda = 0, n = 0;
    double[] a;
    int[] ipiv;

    //Should initialise values so java doesn't give any errors because of try/catch
    a = new double[n];
    ipiv = new int[n];

    System.out.println("F03BAJ Example Program Results");

    //If file name not given print usage info
    if(args.length != 1){
      F03BAJE.usage();
    }
    
    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      n = Integer.parseInt(sVal[1]);

      lda = n;

      //reallocate with values from data file
      a = new double[n * lda];
      ipiv = new int[n];

      //read in matrix
      for(int i = 0; i < n; i++){
        line = reader.readLine().trim();
        sVal = line.split("\\s+");
        for(int j = 0; j < n; j++){
          a[i + (j * n)] = Double.parseDouble(sVal[j]);
        }
      }
    }
    catch(FileNotFoundException e){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException e){
      System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
    }
    
    F07AD f07ad = new F07AD(n, n, a, lda, ipiv, info);
    f07ad.eval();
    
    ifail = 0;

    //Print out matrix
    System.out.println();
    X04CA x04ca = new X04CA("G", "N", n, n, a, lda, "Array A after factorization", ifail);
    x04ca.eval();

    System.out.println();
    System.out.println("Pivots");
    for(int i = 0; i < n; i++){
      System.out.printf("\t%d ", ipiv[i]);
    }
    System.out.printf("\n");
    System.out.println();

    ifail = 0;

    F03BA f03ba = new F03BA(n, a, lda, ipiv, d, id, ifail);
    f03ba.eval();
    d = f03ba.getD();
    id = f03ba.getID();

    System.out.printf("D = \t%.5f ID = \t%d\n", d, id);
    System.out.println();
    System.out.printf("Value of determinant = %.5e\n", d * Math.pow(2.0, id));  

  }

  /**
  * Print usage information.
  */
  private static void usage(){
    System.err.println("Please specify the path to the data file.");
    System.exit(-1);
  }
  
}
 
