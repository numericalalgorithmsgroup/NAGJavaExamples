import com.nag.routines.C02.C02AB;
import com.nag.routines.X02.X02AJ;
import com.nag.routines.X02.X02AL;
import com.nag.routines.Routine;
import com.nag.types.NAGComplex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * C02AB
 */
public class C02ABJE {

    public static void main(String[] args) {

        final boolean polish_example = false;

        Routine.setComplex(new NAGComplex());

        System.out.println(" C02ABJ Example Program Results");

        ex1_basic(args);
        if (polish_example) {
            ex2_polishing(args);
        }
    }

    public static void ex1_basic(String[] args) {
        
        int i, ifail, itmax, n = 0, polish;

        NAGComplex[] z = null;
        double[] a = null, berr = null, cond = null;
        int[] conv = null;

        System.out.println("\n Basic Problem\n");

        // Specify path to data file
        if (args.length != 1) {
            System.err.println("Please specify the path to the data file.");
            System.exit(-1);
        }

        String filename = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Skip heading in data file
            String line = reader.readLine();
            line = reader.readLine();
            line = reader.readLine();

            // Read polynomial degree and allocate
            line = reader.readLine();
            String[] sVal = line.trim().split("\\s+");
            n = Integer.parseInt(sVal[0]);
            a = new double[n + 1];
            berr = new double[n];
            cond = new double[n];
            conv = new int[n];
            z = NAGComplex.createArray(n);

            // Read polynomial coefficients
            for (i = 0; i <= n; i++) {
                line = reader.readLine();
                sVal = line.trim().split("\\s+");
                a[i] = Double.parseDouble(sVal[0]);
            }

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        // Find roots of the polynomial
        itmax = 30;
        polish = 1;
        ifail = 0;
        C02AB c02ab = new C02AB();
        c02ab.eval(a ,n, itmax, polish, z, berr, cond, conv, ifail);

        // Print output
        System.out.println("  i    z                      conv  berr      cond");
        System.out.println(" -----------------------------------------------------");
        for (i = 0; i < n; i++) {
            System.out.printf(" %2d  (%10.2E, %9.2E)  %3d  %9.2E %9.2E\n", i+1, z[i].getRe(), z[i].getIm(), conv[i], berr[i], cond[i]);
        }
    }

    public static void ex2_polishing(String[] args) {
        NAGComplex pz = new NAGComplex();
        double delta, eps, err, fwderr, maxfwderr, maxrelerr, relerr, rmax;
        int i, ifail, itmax, j, k, n = 0, polish;

        NAGComplex[] z = null, zact = null;
        double[] a = null, berr = null, cond = null;
        int[] conv = null;
        boolean[] matched = null;

        System.out.println("\n Polishing Processes\n");

        // Specify path to data file
        if (args.length != 1) {
            System.err.println("Please specify the path to the data file.");
            System.exit(-1);
        }

        String filename = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Skip heading and previous example in data file
            for (i = 0; i <= 12; i++) {
                reader.readLine();
            }

            // Read polynomial degree and allocate
            String line = reader.readLine();
            String[] sVal = line.trim().split("\\s+");
            n = Integer.parseInt(sVal[0]);

            a = new double[n + 1];
            berr = new double[n];
            cond = new double[n];
            conv = new int[n];
            matched = new boolean[n];
            z = NAGComplex.createArray(n);
            zact = NAGComplex.createArray(n);
            
        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        // Set known roots
        for (i = 0; i < n; i++) {
            zact[i] = new NAGComplex((double) (i + 1), 0.0);
        }

        // Multiply out (z-1)(z-2)...(z-n) for coefficients
        for (i = 0; i < n; i++) {
            a[i] = 0.0;
        }
        a[n] = 1.0;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                a[j] = a[j + 1] - a[j] * zact[i].getRe();
            }
            a[n] = -a[n] * zact[i].getRe();
        }

        System.out.println("  polish  relerr    fwderr");
        System.out.println(" ----------------------------");

        // Use different polish modes
        X02AJ x02aj = new X02AJ();
        X02AL x02al = new X02AL();
        for (polish = 0; polish <= 2; polish++) {

            itmax = 30;  
            eps = x02aj.eval();      
            rmax = x02al.eval();

            // Find roots
            ifail = 0;
            C02AB c02ab = new C02AB();
            c02ab.eval(a, n , itmax, polish, z, berr, cond, conv, ifail);

            // Calculate the maximum relative errors of the roots, and the maximum
            // forward error evaluating the polynomial at those roots. Errors are
            // capped at machine precision.
            maxrelerr = eps;
            maxfwderr = eps;
            Arrays.fill(matched, false);

            for (i = 0; i < n; i++) {
                // Evaluate polynomial at this root
                pz = new NAGComplex(a[0], 0.0);
                for (j = 1; j <= n; j++) {
                    pz = z[i].multiply(pz).add(new NAGComplex(a[j], 0.0));
                }

                // Match to an expected root
                k = 0;
                err = rmax;
                for (j = 0; j < n; j++) {
                    if (!matched[j]) {
                        delta = z[i].subtract(zact[j]).abs();
                        if (delta <= err) {
                            err = delta;
                            k = j;
                        }
                    }
                }

                // Mark as matched and update max errors
                matched[k] = true;
                relerr = err/zact[k].abs();
                fwderr = pz.abs();
                maxrelerr = Math.max(maxrelerr, relerr);
                maxfwderr = Math.max(maxfwderr, fwderr);
            }

            // Print output
            System.out.printf(" %2d     %10.2E%10.2E\n", polish, maxrelerr, maxfwderr);
            
        }
    }
}
