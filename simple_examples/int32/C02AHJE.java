import com.nag.routines.C02.C02AH;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * C02AH
 */
public class C02AHJE {

    public static void main(String[] args) {

        double ai = 0, ar = 0, bi = 0, br = 0, ci = 0, cr = 0;
        int ifail;

        double[] zlg = new double[2];
        double[] zsm = new double[2];

        System.out.println(" C02AHJ Example Program Results");

        // Specify path to data file
        if (args.length != 1) {
            System.err.println("Please specify the path to the data file.");
            System.exit(-1);
        }

        String filename = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            reader.readLine(); // Skip heading in data file
            
            String line = reader.readLine();
            String[] sVal = line.trim().split("\\s+");
            ar = Double.parseDouble(sVal[0]);
            ai = Double.parseDouble(sVal[1]);
            br = Double.parseDouble(sVal[2]);
            bi = Double.parseDouble(sVal[3]);
            cr = Double.parseDouble(sVal[4]);
            ci = Double.parseDouble(sVal[5]);

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        ifail = -1;
        C02AH c02ah = new C02AH();
        c02ah.eval(ar, ai, br, bi, cr, ci, zsm, zlg, ifail);
        ifail = c02ah.getIFAIL();

        if (ifail == 0) {
            System.out.println("\n Roots of quadratic equation\n");
            System.out.printf(" z = %12.4E%+14.4E*i\n", zsm[0], zsm[1]);
            System.out.printf(" z = %12.4E%+14.4E*i\n", zlg[0], zlg[1]);
        }
    }
}
