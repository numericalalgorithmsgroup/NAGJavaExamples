import com.nag.routines.F04.F04AM;
import com.nag.routines.X02.X02AJ;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * F04AMJ example program text.
 * @author willa
 * @since 27.1.0.0
 */
public class F04AMJE{

  /**
   * F04AMJ example main program.
   */
  public static void main(String[] args){
    int m = 0, n = 0;
    int ifail, ir, lda, ldb, ldqr, ldx;
    double eps; 
    double[] a, b, alpha, e, qr, r, x, y, z;
    int[] ipiv;
    
    System.out.println("F04AMJ Example Program Results");

    if(args.length != 1){
      F04AMJE.usage();
    }

    //declare so try/catch works
    a = new double[0];
    b = new double[0];
    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      m = Integer.parseInt(sVal[1]);
      n = Integer.parseInt(sVal[2]);

      a = new double[m * n];
      b = new double[m * 1];

      for(int i = 0; i < m; i++){
        line = reader.readLine().trim();
        sVal = line.split("\\s+");
        for(int j = 0; j < n; j++){
          a[i + (j * m) ] = Double.parseDouble(sVal[j]);
        }
        for(int j = n; j < m; j++){
          b[i + (j * (m - (n + 1)))] = Double.parseDouble(sVal[j]);
        }
      }
    }
    catch(FileNotFoundException err){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException err){
      System.err.println("***FATAL: Can't read " + filename + "\n" + err.getMessage());
    }
    //Setting these in try block is more robust but less neat
    //May be needed for array sizes~
    ir = 1;
    lda = m;
    ldb = m;
    ldqr = m;
    ldx = n;

    alpha = new double[n];
    e = new double[n];
    qr = new double[ldqr * n];
    r = new double[m];
    x = new double[ldx * ir];
    y = new double[n];
    z = new double[n];
    ipiv = new int[n];

    X02AJ x02aj = new X02AJ();
    eps = x02aj.eval();

    ifail = 0;

    F04AM f04am = new F04AM(a, lda, x, ldx, b, ldb, m, n, ir, eps, qr, ldqr, alpha, e, y, z, r, ipiv, ifail);
    f04am.eval();
    //update any values you want here
    x = f04am.getX();

    System.out.println("Solution");
    for(int i = 0; i < n; i++){
      for(int j = 0; j < ir; j++){
        System.out.printf("%.4f ", x[j + (i * ir)]);
      }
      System.out.printf("\n");
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





 
