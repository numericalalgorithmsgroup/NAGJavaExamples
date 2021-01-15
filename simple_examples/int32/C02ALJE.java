import com.nag.routines.C02.C02AL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * C02AL
 */
public class C02ALJE {

    public static void main(String[] args) {

        double a = 0, b = 0, c = 0, d = 0, e = 0;
        int i, ifail;

        double[] errest = new double[4];
        double[] zeroi = new double[4];
        double[] zeror = new double[4];

        System.out.println(" C02ALJ Example Program Results");

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
            e = Double.parseDouble(sVal[0]);
            a = Double.parseDouble(sVal[1]);
            b = Double.parseDouble(sVal[2]);
            c = Double.parseDouble(sVal[3]);
            d = Double.parseDouble(sVal[4]);

        } catch (FileNotFoundException e_exception) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e_exception) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e_exception.getMessage());
        }

        ifail = 0;
        C02AL c02al = new C02AL();
        c02al.eval(e, a, b, c, d, zeror, zeroi, errest, ifail);
        ifail = c02al.getIFAIL();

        System.out.println();
        System.out.println("  Roots of quartic equation           Error estimates");
        System.out.println("                                    (machine-dependent)");
        System.out.println();

        for (i = 0; i < 4; i++) {
            System.out.printf("  z = %12.4E%+12.4E*i        %9.1E\n", zeror[i], zeroi[i], errest[i]);
        }
    }
}
