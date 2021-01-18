import com.nag.routines.C02.C02AK;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * C02AK
 */
public class C02AKJE {

    public static void main(String[] args) {

        double r = 0, s = 0, t = 0, u = 0;
        int i, ifail;

        double[] errest = new double[3];
        double[] zeroi = new double[3];
        double[] zeror = new double[3];

        System.out.println(" C02AKJ Example Program Results");

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
            u = Double.parseDouble(sVal[0]);
            r = Double.parseDouble(sVal[1]);
            s = Double.parseDouble(sVal[2]);
            t = Double.parseDouble(sVal[3]);

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        ifail = 0;
        C02AK c02ak = new C02AK();
        c02ak.eval(u, r, s, t, zeror, zeroi, errest, ifail);

        System.out.println();
        System.out.println("  Roots of cubic equation             Error estimates");
        System.out.println("                                    (machine-dependent)");
        System.out.println();

        for (i = 0; i < 3; i++) {
            System.out.printf("  z = %12.4E%+12.4E*i        %9.1E\n", zeror[i], zeroi[i], errest[i]);
        }
    }
}
