import com.nag.routines.C06.C06FK;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException; 

/**
 * C06FKJ example program text
 * @author willa
 * @since 27.1.0.0
 */
public class C06FKJE{

  /**
   * C06FKJE main program
   */
  public static void main(String[] args){
    int ifail = 0, n = 0; //placeholder
    double[] work, xa, xb, ya, yb;

    work = new double[0];
    xa = new double[0];
    xb = new double[0];
    ya = new double[0];
    yb = new double[0]; //placeholders

    System.out.println("C06FKJ Example Program Results");
    System.out.println();

    //Supply file path as argument
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      //Loop until break...
      while(true){
        line = reader.readLine();
        //Break when the end of the file is reached
        if(line == null){
          break;
        }
        String[] sVal = line.split("\\s+");
        n = Integer.parseInt(sVal[0]);

        work = new double[n];
        xa = new double[n];
        xb = new double[n];
        ya = new double[n];
        yb = new double[n];

        for(int i = 0; i < n; i++){
          line = reader.readLine();
          sVal = line.split("\\s+");
          xa[i] = Double.parseDouble(sVal[0]);
          xb[i] = xa[i];
          ya[i] = Double.parseDouble(sVal[1]);
          yb[i] = ya[i];
        }

        ifail = 0;

        C06FK c06fk1 = new C06FK(1, xa, ya, n, work, ifail);
        c06fk1.eval();
        xa = c06fk1.getX();
        C06FK c06fk2 = new C06FK(2, xb, yb, n, work, ifail);
        c06fk2.eval();
        xb = c06fk2.getX();

        System.out.printf("\tCovolution \tCorrelation\n");
        System.out.println();

        for(int i = 0; i < n; i++){
          System.out.printf("%d\t%.5f\t\t%.5f\n", i, xa[i], xb[i]);
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
  }
}
