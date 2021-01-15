import com.nag.routines.C02.C02AN;
import com.nag.routines.Routine;
import com.nag.types.NAGComplex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * C02AN
 */
public class C02ANJE {

    public static void main(String[] args) {

        Routine.setComplex(new NAGComplex());

        NAGComplex a = null, b = null, c = null, d = null, e = null;
        int i, ifail;

        double[] errest = new double[4];
        double[] zeroi = new double[4];
        double[] zeror = new double[4];

        System.out.println(" C02ANJ Example Program Results");

        // Specify path to data file
        if (args.length != 1) {
            System.err.println("Please specify the path to the data file.");
            System.exit(-1);
        }

        String filename = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            reader.readLine(); // Skip heading in data file
            
            String line = reader.readLine().replaceAll("[(),]", "");
            String[] sVal = line.trim().split("\\s+");
            e = new NAGComplex(Double.parseDouble(sVal[0]), Double.parseDouble(sVal[1]));
            
            line = reader.readLine().replaceAll("[(),]", "");
            sVal = line.trim().split("\\s+");
            a = new NAGComplex(Double.parseDouble(sVal[0]), Double.parseDouble(sVal[1]));

            line = reader.readLine().replaceAll("[(),]", "");
            sVal = line.trim().split("\\s+");
            b = new NAGComplex(Double.parseDouble(sVal[0]), Double.parseDouble(sVal[1]));

            line = reader.readLine().replaceAll("[(),]", "");
            sVal = line.trim().split("\\s+");
            c = new NAGComplex(Double.parseDouble(sVal[0]), Double.parseDouble(sVal[1]));

            line = reader.readLine().replaceAll("[(),]", "");
            sVal = line.trim().split("\\s+");
            d = new NAGComplex(Double.parseDouble(sVal[0]), Double.parseDouble(sVal[1]));

        } catch (FileNotFoundException e_exception) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e_exception) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e_exception.getMessage());
        }

        ifail = 0;
        C02AN c02an = new C02AN();
        c02an.eval(e, a, b, c, d, zeror, zeroi, errest, ifail);
        ifail = c02an.getIFAIL();

        System.out.println();
        System.out.println("  Roots of quartic equation           Error estimates");
        System.out.println("                                    (machine-dependent)");
        System.out.println();

        for (i = 0; i < 4; i++) {
            System.out.printf("  z = %12.4E%+12.4E*i        %9.1E\n", zeror[i], zeroi[i], errest[i]);
        }
    }
}
