import com.nag.routines.E04.E04RS;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04PT;
import com.nag.routines.E04.E04RZ;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * E04RS
 */
public class E04RSJE {

    public static void main(String[] args) {

        final int nout = 6;

        long cpuser, handle = 0;
        double s = 0.0;
        int idqc, ifail, n = 0, nnzq0 = 0, nnzq1 = 0, nnzu, nnzuc, x_idx;
        boolean verbose_output;

        double[] q0 = null, q1 = null, r0 = null, r1 = null, u, uc, x;
        double[] rinfo = new double[100];
        double[] ruser = new double[1];
        double[] stats = new double[100];
        int[] icolq0 = null, icolq1 = null, idxr0 = null, idxr1 = null, irowq0 = null, irowq1 = null;
        int[] iuser = new int[2];

        System.out.println(" E04RSJ Example Program Results");

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
            nnzq0 = Integer.parseInt(sVal[1]);
            nnzq1 = Integer.parseInt(sVal[2]);

            // Allocate memory to read data
            irowq0 = new int[nnzq0];
            icolq0 = new int[nnzq0];
            q0 = new double[nnzq0];
            irowq1 = new int[nnzq1];
            icolq1 = new int[nnzq1];
            q1 = new double[nnzq1];
            idxr0 = new int[n];
            r0 = new double[n];
            idxr1 = new int[n];
            r1 = new double[n];

            // Read problem data
            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < nnzq0; i++) {
                irowq0[i] = Integer.parseInt(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < nnzq0; i++) {
                icolq0[i] = Integer.parseInt(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < nnzq0; i++) {
                q0[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < nnzq1; i++) {
                irowq1[i] = Integer.parseInt(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < nnzq1; i++) {
                icolq1[i] = Integer.parseInt(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < nnzq1; i++) {
                q1[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < n; i++) {
                idxr0[i] = Integer.parseInt(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < n; i++) {
                r0[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < n; i++) {
                idxr1[i] = Integer.parseInt(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            for (int i = 0; i < n; i++) {
                r1[i] = Double.parseDouble(sVal[i]);
            }

            line = reader.readLine();
            sVal = line.trim().split("\\s+");
            s = Double.parseDouble(sVal[0]);

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + filename);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
        }

        // Compute size of multipliers
        // One quadratic constraint in the model will have
        // 2 multipliers for both bounds
        nnzu = 2;
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

        // Set objective function
        idqc = -1;
        ifail = 0;
        E04RS e04rs = new E04RS();
        e04rs.eval(handle, 0.0, n, idxr0, r0, nnzq0, irowq0, icolq0, q0, idqc, ifail);

        // Set quadratic constraint
        idqc = 0;
        ifail = 0;
        e04rs.eval(handle, s, n, idxr1, r1, nnzq1, irowq1, icolq1, q1, idqc, ifail);

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
