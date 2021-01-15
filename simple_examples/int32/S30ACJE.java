import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.nag.routines.S.S30AC;

/**
 * S30AC example program text.
 */
public class S30ACJE {

    public static void main(String[] args) {

        int i, ifail, mode = 0, n = 0;
        String calput;

        double[] k = null, p = null, r = null, s0 = null, sigma = null, t = null;
        int[] ivalid = null;

        // Strings must be length expected by Fortran
        calput = getBlankString(1);

        /* Header */
        System.out.println(" S30ACJ Example Program Results");

        // Specify path to data file
        if (args.length != 1) {
            System.err.println("Please specify the path to the data file.");
            System.exit(-1);
        }

        String filename = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine(); // skip header

            System.out.println();
            System.out.println("      SIGMA    IVALID");
            System.out.println();

            line = reader.readLine();
            String[] sVal = line.trim().split("\\s+");
            calput = sVal[0].replaceAll("\'", "");

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            n = Integer.parseInt(sVal[0]);
            mode = Integer.parseInt(sVal[1]);

            line = reader.readLine(); // skip empty line

            p = new double[n];
            k = new double[n];
            s0 = new double[n];
            t = new double[n];
            r = new double[n];
            sigma = new double[n];
            ivalid = new int[n];

            // Read p, k, s0, t and r from data file
            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < n; i++) {
                p[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < n; i++) {
                k[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < n; i++) {
                s0[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < n; i++) {
                t[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < n; i++) {
                r[i] = Double.parseDouble(sVal[i]);
            }

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        // Find the implied volatilities, sigma
        ifail = 0;
        S30AC s30ac = new S30AC();
        s30ac.eval(calput, n, p, k, s0, t, r, sigma, mode, ivalid, ifail);

        // Print solution
        for (i = 0; i < n; i++) {
            System.out.printf(" %12.3E%5d\n", sigma[i], ivalid[i]);
        }

    }

    /**
     * Returns a new String, filled with spaces to the specified length.
     *
     * @param len the required length of the String
     * @return a blank String of the specified length
     */
    public static String getBlankString(int len) {

        if (len > 0) {
            char[] arr = new char[len];
            Arrays.fill(arr, ' ');
            return new String(arr);
        }

        return "";

    }
}
