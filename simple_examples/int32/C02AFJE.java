import com.nag.routines.C02.C02AF;
import com.nag.routines.A02.A02AB;
import com.nag.routines.X02.X02AJ;
import com.nag.routines.X02.X02AL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * C02AF
 */
public class C02AFJE {

    public static final boolean scal = true;

    public static void main(String[] args) {

        System.out.println(" C02AFJ Example Program Results");

        ex1(args);

        ex2(args);
    }

    public static void ex1(String[] args) {

        int i, ifail, n = 0;

        double[][] a = null, z = null;
        double[] w = null;

        System.out.println("\n\n Example 1");

        // Specify path to data file
        if (args.length != 1) {
            System.err.println("Please specify the path to the data file.");
            System.exit(-1);
        }

        String filename = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Skip heading in data file
            reader.readLine();
            reader.readLine();
            reader.readLine();

            String line = reader.readLine();
            String[] sVal = line.trim().split("\\s+");
            n = Integer.parseInt(sVal[0]);

            a = new double[2][n + 1];
            w = new double[4 * (n + 1)];
            z = new double[2][n];

            for (i = 0; i <= n; i++) {
                line = reader.readLine();
                sVal = line.trim().split("\\s+");
                a[0][i] = Double.parseDouble(sVal[0]);
                a[1][i] = Double.parseDouble(sVal[1]);
            }

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        ifail = 0;
        C02AF c02af = new C02AF();
        double[] a1d = convert2DTo1D(a);
        double[] z1d = convert2DTo1D(z);
        c02af.eval(a1d, n, scal, z1d, w, ifail);

        z = convert1DTo2D(z1d, 2);

        System.out.println();
        System.out.printf(" Degree of polynomial = %4d\n", n);
        System.out.println();
        System.out.println(" Computed roots of polynomial");
        System.out.println();

        for (i = 0; i < n; i++) {
            System.out.printf(" z = %12.4E%+12.4E*i\n", z[0][i], z[1][i]);
        }

    }

    public static void ex2(String[] args) {

        double deltac, deltai, di, eps, epsbar, f, r1, r2, r3, rmax;
        int i, ifail, j, jmin = 0, n = 0;

        double[][] a = null, abar = null, z = null, zbar = null;
        double[] r = null, w = null;
        int[] m = null;

        System.out.println("\n\n Example 2");

        // Specify path to data file
        if (args.length != 1) {
            System.err.println("Please specify the path to the data file.");
            System.exit(-1);
        }

        String filename = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Skip heading and previous example in data file
            for (i = 0; i < 12; i++) {
                reader.readLine();
            }

            String line = reader.readLine();
            String[] sVal = line.trim().split("\\s+");
            n = Integer.parseInt(sVal[0]);
            a = new double[2][n + 1];
            abar = new double[2][n + 1];
            r = new double[n];
            w = new double[4 * (n + 1)];
            z = new double[2][n];
            zbar = new double[2][n];
            m = new int[n];

            // Read in the coefficients of the original polynomial.
            for (i = 0; i <= n; i++) {
                line = reader.readLine();
                sVal = line.trim().split("\\s+");
                a[0][i] = Double.parseDouble(sVal[0]);
                a[1][i] = Double.parseDouble(sVal[1]);
            }

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        // Compute the roots of the original polynomial.
        ifail = 0;
        C02AF c02af = new C02AF();
        double[] a1d = convert2DTo1D(a);
        double[] z1d = convert2DTo1D(z);
        c02af.eval(a1d, n, scal, z1d, w, ifail);

        z = convert1DTo2D(z1d, 2);

        // Form the coefficients of the perturbed polynomial.
        X02AJ x02aj = new X02AJ();
        eps = x02aj.eval();
        epsbar = 3.0 * eps;

        for (i = 0; i <= n; i++) {
            if (a[0][i] != 0.0E0) {
                f = 1.0E0 + epsbar;
                epsbar = -epsbar;
                abar[0][i] = f * a[0][i];

                if (a[1][i] != 0.0E0) {
                    abar[1][i] = f * a[1][i];
                } else {
                    abar[1][i] = 0.0E0;
                }

            } else {

                if (a[1][i] != 0.0E0) {
                    f = 1.0E0 + epsbar;
                    epsbar = -epsbar;
                    abar[1][i] = f * a[1][i];
                } else {
                    abar[1][i] = 0.0E0;
                }
            }
        }

        // Compute the roots of the perturbed polynomial.
        ifail = 0;
        double[] abar1d = convert2DTo1D(abar);
        double[] zbar1d = convert2DTo1D(zbar);
        c02af.eval(abar1d, n, scal, zbar1d, w, ifail);

        zbar = convert1DTo2D(zbar1d, 2);

        // Perform error analysis.

        // Initialize markers to 0 (unmarked).
        Arrays.fill(m, 0);
        X02AL x02al = new X02AL();
        rmax = x02al.eval();

        // Loop over all unperturbed roots (stored in Z).
        A02AB a02ab = new A02AB();
        for (i = 0; i < n; i++) {
            deltai = rmax;
            r1 = a02ab.eval(z[0][i], z[1][i]);

            // Loop over all perturbed roots (stored in ZBAR).
            for (j = 0; j < n; j++) {
                // Compare the current unperturbed root to all unmarked
                // perturbed roots.

                if (m[j] == 0) {
                    r2 = a02ab.eval(zbar[0][j], zbar[1][j]);
                    deltac = Math.abs(r1 - r2);

                    if (deltac < deltai) {
                        deltai = deltac;
                        jmin = j;
                    }
                }
            }

            // Mark the selected perturbed root.
            m[jmin] = 1;

            // Compute the relative error.
            if (r1 != 0.0E0) {
                r3 = a02ab.eval(zbar[0][jmin], zbar[1][jmin]);
                di = Math.min(r1, r3);
                r[i] = Math.max(deltai / Math.max(di, deltai / rmax), eps);
            } else {
                r[i] = 0.0E0;
            }
        }

        System.out.println();
        System.out.printf(" Degree of polynomial = %4d\n", n);
        System.out.println();
        System.out.println(" Computed roots of polynomial     Error estimates");
        System.out.println("                                (machine-dependent)");
        System.out.println();

        for (i = 0; i < n; i++) {
            System.out.printf(" z = %12.4E%+12.4E*i     %9.1E\n", z[0][i], z[1][i], r[i]);
        }

    }

    public static double[] convert2DTo1D(double[][] a) {
        double[] b = new double[a.length * a[0].length];
        int n = a.length;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i + j*n] = a[i][j];
            }
        }
        
        return b;
    }

    public static double[][] convert1DTo2D(double[] a, int n) {
        double[][] b = new double[n][a.length / n];

        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                b[i][j] = a[i + j*n];
            }
        }
        
        return b;
    }
}
