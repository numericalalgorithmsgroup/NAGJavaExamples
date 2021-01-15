import com.nag.routines.F04.F04BA;
import com.nag.routines.X04.X04CA;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * F04BAJ example program text.
 * @author willa
 * @since 27.1.0.0
 */
public class F04BAJE{

  /**
   * F04BAJ example main program.
   */
  public static void main(String[] args){
    int n = 0, nrhs = 0, lda = 0, ldb = 0; //placeholder
    int ierr, ifail;
    double errbnd, rcond; 
    double a[], b[];
    int ipiv[]; 

    a = new double[0]; b = new double[0]; //placeholder

    if(args.length != 1){
      F04BAJE.usage();
    }

    System.out.println("F04BAJ Example Program Results");
    System.out.println();

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      line = reader.readLine();
      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      n = Integer.parseInt(sVal[1]);
      nrhs = Integer.parseInt(sVal[2]);

      lda = n;
      ldb = n;

      a = new double[lda * n];
      b = new double[ldb * nrhs];

      line = reader.readLine();
      for(int i = 0; i < lda; i++){
        line = reader.readLine();
        sVal = line.split("\\s+");
        for(int j = 0; j < n; j++){
          a[i + (j * n)] = Double.parseDouble(sVal[j + 1]);
        }
      }

      line = reader.readLine();
      for(int i = 0; i < ldb; i++){
        line = reader.readLine();
        sVal = line.split("\\s+");
        for(int j = 0; j < nrhs; j++){
          b[i + (j * n)] = Double.parseDouble(sVal[j + 1]);
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

    ifail = 1;
    ipiv = new int[n];
    rcond = 0;
    errbnd = 0;
    F04BA f04ba = new F04BA(n, nrhs, a, lda, ipiv, b, ldb, rcond, errbnd, ifail);
    f04ba.eval();

    ifail = f04ba.getIFAIL();
    rcond = f04ba.getRCOND();
    errbnd = f04ba.getERRBND();
    ipiv = f04ba.getIPIV();
    a = f04ba.getA();
    b = f04ba.getB();

    if(ifail == 0){
      //Print solution, estimate of condition number and approximate error bound.
      ierr = 0;
      X04CA x04ca = new X04CA("General", " ", n, nrhs, b, ldb, "Solution", ierr);
      x04ca.eval();

      System.out.println();
      System.out.println("Estimate of condition number");
      System.out.printf("%.1e\n", 1/rcond);
      System.out.println();
      System.out.println("Esttimate of error bound for computed solutions");
      System.out.printf("%.1e\n", errbnd);
    }
    else if(ifail == (n + 1)){
      //Matrix A is numerically singular. Print estimate of reciprocal of condition number and solution.
      System.out.println();
      System.out.println("Estimate of reciprocal of condition number");
      System.out.printf("%.1e\n", rcond);
      System.out.println();

      ierr = 0;
      X04CA x04ca = new X04CA("General", " ", n, nrhs, b, ldb, "Solution", ierr);
      x04ca.eval();
    }
    else if(ifail > 0 && ifail <= n){
      //The upper triangular matrix U is exactly singular. Print details of factorization.
      System.out.println();

      ierr = 0;
      X04CA x04ca = new X04CA("General", " ", n, n, a, lda, "Details of factorization", ierr);
      x04ca.eval();

      System.out.println();
      System.out.println("Pivot indices");
      for(int i = 0; i < n; i++){
        System.out.printf("%d ", ipiv[i]);
      }
      System.out.printf("\n");
    }
    else{
      System.out.printf(" ** F04BAF returned with IFAIL = %d\n", ifail);
    }
   
  }

  /**
  * No arguments supplied when example runs
  */
  private static void usage() {
    System.err.println("Please specify the path to the data file.");
    System.exit(-1);
  }
}
