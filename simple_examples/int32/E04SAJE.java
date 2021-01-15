import com.nag.routines.E04.E04RC;
import com.nag.routines.E04.E04SA;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04PT;
import com.nag.routines.E04.E04PTU;

/**
 * E04SA example program text.
 */
public class E04SAJE {

    public static final String fname = "e04saje.opt";
    public static void main(String[] args) {

        long cpuser, handle = 0;
        int ifail, n, nnzu, nnzuc, x_idx;
        boolean verbose_output;
        String ftype;

        double[] rinfo = new double[100];
        double[] ruser = new double[1];
        double[] stats = new double[100];
        double[] u, uc, x;
        int[] iuser = new int[1];
        int[] pinfo = new int[100];

        System.out.println(" E04SAJ Example Program Results");

        // Read mps file to a handle
        ifail = 0;
        ftype = "mps";
        E04SA e04sa = new E04SA();
        e04sa.eval(handle, fname, ftype, pinfo, ifail);
        handle = e04sa.getHANDLE();

        // Get problem size from pinfo
        pinfo = e04sa.getPINFO();
        n = pinfo[0];
        nnzu = pinfo[10];
        nnzuc = pinfo[11];

        // Set all variables as continuous
        ifail = 0;
        E04RC e04rc = new E04RC();

        int[] x_idx_Arr = new int[n];
        for (x_idx = 1; x_idx <= n; x_idx++) {
            x_idx_Arr[x_idx - 1] = x_idx;
        }

        e04rc.eval(handle, "CONT", n, x_idx_Arr, ifail);

        // Allocate memory
        x = new double[n];
        u = new double[nnzu];
        uc = new double[nnzuc];

        // Set this to .True. to cause e04ptf to produce intermediate progress output
        verbose_output = false;

        E04ZM e04zm = new E04ZM();
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
        ifail = -1;
        E04PT e04pt = new E04PT();
        MONIT monit = new MONIT();
        e04pt.eval(handle, n, x, nnzu, u, nnzuc, uc, rinfo, stats, monit, iuser, ruser, cpuser, ifail);
        ifail = e04pt.getIFAIL();

        // Print solution if optimal or suboptimal solution found
        if ((ifail == 0) || (ifail == 50)) {
            System.out.println(" Optimal X:");
            System.out.println("  x_idx   " + "    Value    ");
            for (x_idx = 0; x_idx < n; x_idx++) {
                System.out.printf("  %5d   %12.5e\n", x_idx, x[x_idx]);
            }
        }

        // Free the handle memory
        ifail = 0;
        E04RZ e04rz = new E04RZ();
        e04rz.eval(handle, ifail);
    }

    public static class MONIT extends E04PT.Abstract_E04PT_MONIT {

        public void eval() {
          E04PTU e04ptu = new E04PTU();
          e04ptu.eval(this.HANDLE, this.RINFO, this.STATS, this.IUSER, this.RUSER, this.CPUSER, this.INFORM);
          this.INFORM = e04ptu.getINFORM();
        }
    } 
}
