import com.nag.routines.F05.F05AA;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 

/**
 * F05AAJ example program text.
 * @author willa
 * @since 27.1.0.0
 */
public class F05AAJE{

  /**
   * F05AAJ example main program.
   */
  public static void main(String[] args) {
    //Should initialise values to avoid issues with try/catch
    int icol = 0, ifail = 0, lda = 0, m = 0, n1 = 0, n2 = 0;
    double cc = 0;
    double[] a, s;

    //same as ints
    a = new double[1];
    s = new double[1];
    System.out.println("F05AAJ Example Program Results");
    System.out.println();

    //supply data 
    if(args.length != 1){
      F05AAJE.usage();
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      line = reader.readLine();
      String[] sVal = line.split("\\s+");;
      m = Integer.parseInt(sVal[1]);
      n1 = Integer.parseInt(sVal[2]);
      n2 = Integer.parseInt(sVal[3]);
      lda = m;
      
      a = new double[lda*n2];
      s = new double[n2];

      //read in a from data
      for(int i = 0; i < n2; i++){
        line = reader.readLine().trim();
        sVal = line.split("\\s+");
        for(int j = 0; j < m; j++){
          a[i + (j * n2)] = Double.parseDouble(sVal[j]);
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

    //create object then eval
    F05AA f05aa = new F05AA(a, lda, m, n1, n2, s, cc, icol, ifail);
    f05aa.eval();

    //update values
    n1 = f05aa.getN1();
    n2 = f05aa.getN2();
    cc = f05aa.getCC();
    icol = f05aa.getICOL();
    a = f05aa.getA();

    System.out.printf("N1 = %d N2 = %d\n", n1, n2);
    System.out.println();
    System.out.printf("CC = %.4f ICOL = %d\n", cc, icol);
    System.out.println();
    System.out.println("Final Vectors");

    for(int i = 0; i < n2; i++){
      for(int j = 1; j < m; j++){
        System.out.printf("%.4f ", a[i +(j * n2)]);
      }
      System.out.printf("\n");
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
    
