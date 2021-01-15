import com.nag.routines.C09.C09AA;
import com.nag.routines.C09.C09CC;
import com.nag.routines.C09.C09CD;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * C09AAJ example program text.
 * @author willa
 * @since 27.1.0.0
 */
public class C09AAJE{

  /**
   * C09AAJ example main program
   */
  public static void main(String[] args){
    int ifail, lenc = 0, n = 0, nf = 0, nnz, nwc = 0, nwlmax = 0, ny; //placeholders
    String mode = "", wavnam = "", wtrans; //placeholders
    int[] dwtlev, icomm;
    double[] c, x, y;

    x = new double[0]; y = new double[0]; //placeholders

    //print error message if no data file given
    if(args.length != 1){
      C09AAJE.usage();
    }
    
    icomm = new int[100];
        
    System.out.println("C09AAJ Example Program Results");
    System.out.println();

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      n = Integer.parseInt(sVal[1]);

      line = reader.readLine();
      sVal = line.split("\\s+");
      wavnam = sVal[1];
      mode = sVal[2];
      
      x = new double[n];
      y = new double[n];

      System.out.printf("Parameters read from file :: \n \tWavelet : %s \tEnd mode : %s \t N = %d\n", wavnam, mode, n);

      System.out.println("Input data \tX : ");
      for(int i = 0; i < n; i++){
        line = reader.readLine();
        //Avoid errors with data file format and negative numbers
        line = " " + line;
        sVal = line.split("\\s+");
        x[i] = Double.parseDouble(sVal[1]);
        System.out.printf("%.3f\t", x[i]);
      }
      System.out.printf("\n");
    }
    catch(FileNotFoundException e){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException e){
      System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
    }

    //Query wavelet filter dimensions
    //For Multi-Resolution analysis ,decomposition, wrtrans = 'M'
    wtrans = "Multilevel";

    //ifail: behaviour on error exit
    //       =0 for hard exit, =1 for quiet-soft, =-1 for noisy-soft
    ifail = 0;
    C09AA c09aa = new C09AA(wavnam, wtrans, mode, n, nwlmax, nf, nwc, icomm, ifail);
    c09aa.eval();

    nwc = c09aa.getNWC();
    nwlmax = c09aa.getNWLMAX();
    lenc = nwc;
    c = new double[lenc];
    dwtlev = new int[nwlmax + 1];

    icomm = c09aa.getICOMM(); 
    ifail = 0;
    //Perform discrete wavelet transform
    C09CC c09cc = new C09CC(n, x, lenc, c, nwlmax, dwtlev, icomm, ifail);
    c09cc.eval();

    nf = c09aa.getNF();
    nwlmax = c09cc.getNWL();
    dwtlev = c09cc.getDWTLEV();
    c = c09cc.getC();

    System.out.println();
    System.out.printf("Length of wavelet filter : \t%d\n", nf);
    System.out.printf("Number of Levels : \t%d\n", nwlmax);
    System.out.printf("Number of coefficients in each level: \n\t\t");
    for(int i = 0; i <= nwlmax; i++){
      System.out.printf("%d\t ", dwtlev[i]);
    }
    System.out.printf("\n");
    System.out.printf("Total number of wavelength coefficients : %d\n", nwc);
    nnz = arraySum(dwtlev);
    System.out.println();
    System.out.println("Wavelet coefficients C:");
    for(int i = 0; i < nnz; i++){
      System.out.printf("%.3f ", c[i]);
    }
    System.out.printf("\n");

    //Reconstruct original data
    ny = n;

    ifail = 0;
    lenc = c09cc.getLENC();
    icomm = c09cc.getICOMM();
    C09CD c09cd = new C09CD(nwlmax, lenc, c, ny, y, icomm, ifail);
    c09cd.eval();

    y = c09cd.getY();
    System.out.println();
    System.out.printf("Reconstruction \tY : \n");
    for(int i = 0; i < ny; i++){
      System.out.printf("%.3f ", y[i]);
    }
    System.out.printf("\n");
  }

  /**
   * Finds the sum of all integers in an array
   * @param a
   *        Array to sum
   * @return total 
   */
  private static int arraySum(int[] a){
    int tot = 0;
    for(int i = 0; i < a.length; i++){
      tot += a[i];
    }
    return tot;
  }

  /**
   * No arguments supplied when exmaple runs
   */
  private static void usage(){
    System.err.println("Please specify the path to the data file.");
    System.exit(-1);
  }
}
