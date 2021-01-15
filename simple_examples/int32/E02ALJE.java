import com.nag.routines.E02.E02AL;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * E02ALJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class E02ALJE{

  /**
   * E02ALJ Example main program
   */
  public static void main(String[] args){
    double dxx, ref = 0, s, t, xx;
    int ifail, n = 0, m = 0, neval = 0; //placeholders
    double[] a, x, y;

    a = new double[0];
    x = new double[0];
    y = new double[0]; //placeholders

    System.out.println("E02ALJ Example Program Results");
    
    //Supply file path as terminal input
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      n = Integer.parseInt(sVal[1]);
      m = Integer.parseInt(sVal[2]);
      neval = Integer.parseInt(sVal[3]);

      a = new double[m + 1];
      x = new double[n];
      y = new double[n];

      for(int i = 0; i < n; i++){
        line = reader.readLine();
        sVal = line.split("\\s+");
        x[i] = Double.parseDouble(sVal[1]);
        y[i] = Double.parseDouble(sVal[2]);
      }
    }
    catch(FileNotFoundException e){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException e){
      System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
      System.exit(-2);
    }

    ifail = 0;
    E02AL e02al = new E02AL(n, x, y, m, a, ref, ifail);
    e02al.eval();

    //update
    ref = e02al.getREF();
    x = e02al.getX();
    y = e02al.getY();
    

    System.out.println();
    System.out.printf("   Polynomial coefficients\n");
    for(int i = 0; i <= m; i++){
      System.out.printf("\t%.4e\n", a[i]);
    }
    System.out.println();
    System.out.printf("   Reference deviation = %.2e\n", ref);
    System.out.println();
    System.out.printf("\tX\tFit\texp(x)\tResidual\n");

    dxx = 1/(double)(neval - 1);

    for(int j = 0; j < neval; j++){
      xx = (double) j * dxx;

      s = a[m];

      for(int i = m - 1; i >=0; i--){
        s = s * xx + a[i];
      }

      t = Math.exp(xx);
      System.out.printf("\t%.2f\t%.4f\t%.4f\t%.2e\n", xx, s, t, (s - t));
    }
  }
}

