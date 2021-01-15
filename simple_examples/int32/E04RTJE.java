import com.nag.routines.E04.E04RT;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RJ;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04PT;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04RH;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * E04RT example program text.
 */
public class E04RTJE {

    public static void main(String[] args) {

        final int nout = 6;

        long cpuser, handle = 0;
        int i, idlc, idqc, ifail, j, m = 0, n = 0, nnza = 0, nnzu, nnzuc, x_idx;
        boolean verbose_output;

        double[] a = null, b = null, r0 = null, u, uc, x, xl = null, xu = null;
        double[] lc = new double[3];
        double[] lc_rhs = new double[1];
        double[] rinfo = new double[100];
        double[] ruser = new double[1];
        double[] stats = new double[100];
        int[] icola = null, idxr0 = null, irowa = null;
        int[] icollc = new int[3];
        int[] irowlc = new int[3];
        int[] iuser = new int[2];

        System.out.println(" E04RTJ Example Program Results");

        // Specify path to data file
        if (args.length != 1) {
            System.err.println("Please specify the path to the data file.");
            System.exit(-1);
        }

        String filename = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine(); // skip header

            // Read dimensions of the problem
            line = reader.readLine();
            String[] sVal = line.trim().split("\\s+");
            n = Integer.parseInt(sVal[0]);
            m = Integer.parseInt(sVal[1]);
            nnza = Integer.parseInt(sVal[2]);

            // Allocate memory to read data
            a = new double[nnza];
            icola = new int[nnza];
            irowa = new int[nnza];
            idxr0 = new int[n];
            r0 = new double[n];
            b = new double[m];
            xl = new double[n];
            xu = new double[n];

            // Read problem data
            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < nnza; i++) {
                irowa[i] = Integer.parseInt(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < nnza; i++) {
                icola[i] = Integer.parseInt(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < nnza; i++) {
                a[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < m; i++) {
                b[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < n; i++) {
                xl[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (i = 0; i < n; i++) {
                xu[i] = Double.parseDouble(sVal[i]);
            }

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        // Compute -2*b'A as linear term in quadratic function
        for (j = 0; j < n; j++) {
            r0[j] = 0.0;
            idxr0[j] = j + 1;
        }
        for (i = 0; i < nnza; i++) {
            r0[icola[i] - 1] = r0[icola[i] - 1] + a[i] * b[irowa[i] - 1];
        }

        // Compute size of multipliers
        // One linear constraint in the model will have
        // 2 multipliers for both bounds
        nnzu = 2 * n + 2;
        // No cone constraint in the model, so set nnzuc to 0
        nnzuc = 0;

        // Allocate memory for final results
        x = new double[n];
        u = new double[nnzu];
        uc = new double[nnzuc];

        // Create the problem handle
        ifail = 0;
        E04RA e04ra = new E04RA();
        e04ra.eval(handle, n, ifail);
        handle = e04ra.getHANDLE();

        // Set quadratic objective function
        idqc = -1;
        ifail = 0;
        E04RT e04rt = new E04RT();
        e04rt.eval(handle, 0.0, n, idxr0, r0, m, nnza, irowa, icola, a, idqc, ifail);

        // Set box constraints
        ifail = 0;
        E04RH e04rh = new E04RH();
        e04rh.eval(handle, n, xl, xu, ifail);

        // Set linear constraint: x1 + x2 + x3 = 1
        for (j = 0; j < n; j++) {
            irowlc[j] = 1;
            icollc[j] = j + 1;
            lc[j] = 1.0;
        }
        lc_rhs[0] = 1.0;
        ifail = 0;
        idlc = 0;
        E04RJ e04rj = new E04RJ();
        e04rj.eval(handle, 1, lc_rhs, lc_rhs, 3, irowlc, icollc, lc, idlc, ifail);

        // Turn on monitoring
        ifail = 0;
        E04ZM e04zm = new E04ZM();
        e04zm.eval(handle, "SOCP Monitor Frequency = 1", ifail);

        // Set this to True to cause e04ptf to produce intermediate progress output
        verbose_output = false;

        if (verbose_output) {
            // Require printing of primal and dual solutions at the end of the solve
            ifail = 0;
            e04zm.eval(handle, "Print Solution = YES", ifail);
        } else {
            // Turn off printing of intermediate progress output
            ifail = 0;
            e04zm.eval(handle, "Print Level = 1", ifail);
        }

        // Call SOCP interior point solver
        cpuser = 0;
        iuser[0] = nout;
        iuser[1] = 0;
        ruser[0] = 1.0e-07;
        ifail = -1;
        E04PT e04pt = new E04PT();
        MONIT monit = new MONIT();
        e04pt.eval(handle, n, x, nnzu, u, nnzuc, uc, rinfo, stats, monit, iuser, ruser, cpuser, ifail);
        ifail = e04pt.getIFAIL();

        // Print solution if optimal or suboptimal solution found
        if ((ifail == 0) || (ifail == 50)) {
            System.out.println(" Optimal X:");
            System.out.println("  x_idx       Value    ");
            for (x_idx = 0; x_idx < n; x_idx++) {
                System.out.printf("  %5d   %11.3e\n", x_idx, x[x_idx]);
            }
        }

        // Free the handle memory
        ifail = 0;
        E04RZ e04rz = new E04RZ();
        e04rz.eval(handle, ifail);
    }

    public static class MONIT extends E04PT.Abstract_E04PT_MONIT {

        public void eval() {
            double tol;
            int nout, tol_reached;

            nout = this.IUSER[0];
            tol_reached = this.IUSER[1];
            tol = this.RUSER[0];

            // If x is close to the solution, print a message
            if ((this.RINFO[14] < tol) && (this.RINFO[15] < tol) && (this.RINFO[16] < tol) && (this.RINFO[17] < tol)) {
                if (tol_reached == 0) {
                    System.out.printf("\n     monit() reports good approximate solution (tol =%9.2e)\n", tol);
                    this.IUSER[1] = 1;
                }
            }
        }
    }
 }
