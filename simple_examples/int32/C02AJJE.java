import com.nag.routines.C02.C02AJ;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * C02AJ
 */
public class C02AJJE {

    public static void main(String[] args) {

        double a = 0, b = 0, c = 0;
        int ifail;

        double[] zlg = new double[2];
        double[] zsm = new double[2];

        System.out.println(" C02AJJ Example Program Results");

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
            a = Double.parseDouble(sVal[0]);
            b = Double.parseDouble(sVal[1]);
            c = Double.parseDouble(sVal[2]);

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        ifail = -1;
        C02AJ c02aj = new C02AJ();
        c02aj.eval(a, b, c, zsm, zlg, ifail);
        ifail = c02aj.getIFAIL();

        if (ifail == 0) {
            System.out.println("\n Roots of quadratic equation\n");

            if (zsm[1] == 0.0E0) {
                // 2 real roots.
                System.out.printf(" z = %12.4E\n", zsm[0]);
                System.out.printf(" z = %12.4E\n", zlg[0]);
            } else {
                // 2 complex roots.
                System.out.printf(" z = %12.4E +/- %12.4E*i", zsm[0], Math.abs(zsm[1]));
            }
        }
    }
}
