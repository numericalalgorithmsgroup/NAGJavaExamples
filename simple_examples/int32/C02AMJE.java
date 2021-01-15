import com.nag.routines.C02.C02AM;
import com.nag.routines.Routine;
import com.nag.types.NAGComplex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * C02AM
 */
public class C02AMJE {

    public static void main(String[] args) {

        Routine.setComplex(new NAGComplex());

        NAGComplex r = null, s = null, t = null, u = null;
        int i, ifail;

        double[] errest = new double[3];
        double[] zeroi = new double[3];
        double[] zeror = new double[3];

        System.out.println(" C02AMJ Example Program Results");

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
            u = new NAGComplex(Double.parseDouble(sVal[0]), Double.parseDouble(sVal[1]));
            
            line = reader.readLine().replaceAll("[(),]", "");
            sVal = line.trim().split("\\s+");
            r = new NAGComplex(Double.parseDouble(sVal[0]), Double.parseDouble(sVal[1]));

            line = reader.readLine().replaceAll("[(),]", "");
            sVal = line.trim().split("\\s+");
            s = new NAGComplex(Double.parseDouble(sVal[0]), Double.parseDouble(sVal[1]));

            line = reader.readLine().replaceAll("[(),]", "");
            sVal = line.trim().split("\\s+");
            t = new NAGComplex(Double.parseDouble(sVal[0]), Double.parseDouble(sVal[1]));

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        ifail = 0;
        C02AM c02am = new C02AM();
        c02am.eval(u, r, s, t, zeror, zeroi, errest, ifail);

        System.out.println();
        System.out.println("  Roots of cubic equation             Error estimates");
        System.out.println("                                    (machine-dependent)");
        System.out.println();

        for (i = 0; i < 3; i++) {
            System.out.printf("  z = %12.4E%+12.4E*i        %9.1E\n", zeror[i], zeroi[i], errest[i]);
        }
    }
}
