import com.nag.routines.S.S17DG;
import com.nag.types.NAGComplex;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * S17DG example program text.
 */
public class S17DGJE {

  public static void main(String[] args) throws Exception {
    String deriv,scal;
    int ifail = 1;
    NAGComplex z,ai;
    int nz = -1;
    z = new NAGComplex();
    ai = new NAGComplex();
    ai.setRe(Double.NaN);
    ai.setIm(Double.NaN);

    System.out.println(" S17DGJ Example Program Results");
    BufferedReader dataIn = new BufferedReader(new FileReader(args[0]));

    dataIn.readLine();
    System.out.println();
    System.out.println(" DERIV           Z         SCAL           AI          NZ");
    System.out.println();
    String line = dataIn.readLine();
    Pattern linePatt = Pattern.compile("\\s*'([A-Z]{1})'\\s+\\(\\s*([0-9\\.\\-]+)\\s*,"
        + "\\s*([0-9\\.\\-]+)\\s*\\)\\s+'([A-Z]{1})'\\s*");
    Matcher m;
    S17DG s17dg  = new S17DG();
    while (line != null) {
      m = linePatt.matcher(line);
      if (m.matches()) {
        deriv = m.group(1);
        z.setRe(Double.parseDouble(m.group(2)));
        z.setIm(Double.parseDouble(m.group(3)));
        scal = m.group(4);
        ifail = 1;// SOFT AND SILENT FAILURE
        s17dg.eval(deriv,z,scal,ai,nz,ifail);
        if (s17dg.getIFAIL() == 0) {
          System.out.printf("   %s   (%8.4f,%8.4f)   %s   (%8.4f,%8.4f) %4d\n",
              s17dg.getDERIV(), s17dg.getZ().getRe(), s17dg.getZ().getIm(), s17dg.getSCAL(),
              s17dg.getAI().getRe(), s17dg.getAI().getIm(), s17dg.getNZ());
        }
        else {
          System.err.println("Something went wrong - S17DG returned IFAIL = "+s17dg.getIFAIL());
          System.exit(-1);
        }
      }
      else {
        System.out.println("Can't match:\n"+line);
      }
      line = dataIn.readLine();
    }

  }

}
