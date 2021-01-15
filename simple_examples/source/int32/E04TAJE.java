import com.nag.routines.E04.E04TA;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RE;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04RJ;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04MT;
import com.nag.routines.E04.E04MTU;
import com.nag.routines.E04.E04TD;
import com.nag.routines.E04.E04TE;
import com.nag.routines.E04.E04TJ;
import com.nag.routines.E04.E04RW;
import com.nag.routines.E04.E04RZ;

import java.util.Arrays;

/**
 * E04TA
 */
public class E04TAJE {

    public static void main(String[] args) {

        final double infbnd = 1.0e20;

        long cpuser, handle;
        int idlc, ifail, ioflag, liarr, nclin, nnza, nnzu, nvar;

        double[] a, bla, bua, cvec, ulag, x, xl, xu;

        double[] rinfo = new double[100];
        double[] ruser = new double[1];
        double[] stats = new double[100];
        double[] u = new double[1];

        int[] icola, irowa;

        int[] iuser = new int[1];
        int[] pinfo = new int[100];

        System.out.println(" E04TAJ Example Program Results\n");
        System.out.println(" Solve the first LP\n");

        handle = 0;
        cpuser = 0;

        // Initialize the handle
        nvar = 2;
        ifail = 0;
        E04RA e04ra = new E04RA();
        e04ra.eval(handle, nvar, ifail);
        handle = e04ra.getHANDLE();

        // Define the objective function
        cvec = new double[nvar];
        cvec[0] = 2.0;
        cvec[1] = 4.5;
        ifail = 0;
        E04RE e04re = new E04RE();
        e04re.eval(handle, nvar, cvec, ifail);

        // Box constraints
        xl = new double[nvar];
        xu = new double[nvar];
        Arrays.fill(xl, 0.0);
        xu[0] = infbnd;
        xu[1] = 100.0;
        ifail = 0;
        E04RH e04rh = new E04RH();
        e04rh.eval(handle, nvar, xl, xu, ifail);

        // Set the linear constraints
        idlc = 0;
        nclin = 3;
        nnza = 6;
        bla = new double[nclin];
        bua = new double[nclin];
        irowa = new int[nnza];
        icola = new int[nnza];
        a = new double[nnza];
        Arrays.fill(bla, -infbnd);
        bua = new double[] {
            1500.0, 6000.0, 16000.0
        };
        irowa = new int[] {
            1, 1, 2, 2, 3, 3
        };
        icola = new int[] {
            1, 2, 1, 2, 1, 2
        };
        a = new double[] {
            1.2, 3.0, 6.0, 10.0, 40.0, 80.0
        };
        ifail = 0;
        E04RJ e04rj = new E04RJ();
        e04rj.eval(handle, nclin, bla, bua, nnza, irowa, icola, a , idlc, ifail);

        // Optional parameters
        E04ZM e04zm = new E04ZM();
        e04zm.eval(handle, "Task = Max", ifail);
        e04zm.eval(handle, "Print Options = No", ifail);
        e04zm.eval(handle, "Print Level = 1", ifail);
        e04zm.eval(handle, "Print Solution = X", ifail);

        // Call the LP solver
        x = new double[nvar + 1];
        nnzu = 0;
        ifail = -1;
        E04MT e04mt = new E04MT();
        MONIT monit = new MONIT();
        e04mt.eval(handle, nvar, x ,nnzu, u, rinfo, stats, monit, iuser, ruser, cpuser, ifail);

        // Add a variable
        ifail = 0;
        E04TA e04ta = new E04TA();
        e04ta.eval(handle, 1, nvar, ifail);
        nvar = e04ta.getNVAR();

        // Box constraint on the new variable
        ifail = 0;
        E04TD e04td = new E04TD();
        e04td.eval(handle, "variable", nvar, 0.0, 50.0, ifail);

        // Add the linear objective component
        ifail = 0;
        E04TE e04te = new E04TE();
        e04te.eval(handle, 3, 7.0, ifail);

        // Add linear constraints coefficients
        E04TJ e04tj = new E04TJ();
        ifail = 0;
        e04tj.eval(handle, 1, 3, 5.0, ifail);
        ifail = 0;
        e04tj.eval(handle, 2, 3, 12.0, ifail);
        ifail = 0;
        e04tj.eval(handle, 3, 3, 120.0, ifail);

        System.out.println("\n The new variable has been added, solve the handle again\n");

        // Solve the problem again
        ifail = -1;
        e04mt.eval(handle, nvar, x, nnzu, u, rinfo, stats, monit, iuser, ruser, cpuser, ifail);

        // Add a linear constraint
        nclin = 1;
        bla[0] = -infbnd;
        bua[0] = 100.0;
        nnza = 2;
        irowa[0] = 1;
        irowa[1] = 1;
        icola[0] = 2;
        icola[1] = 3;
        a[0] = 1.0;
        a[1] = 1.0;
        idlc = 0;
        e04rj.eval(handle, nclin, bla, bua ,nnza, irowa, icola, a ,idlc, ifail);

        System.out.println("\n The new constraint has been added, solve the handle again\n");

        // Query the problem sizes to request the Lagrangian multipliers for the
        // last solve
        ioflag = 1;
        liarr = 100;
        E04RW e04rw = new E04RW();
        e04rw.eval(handle, "pinfo", ioflag, liarr, pinfo, ifail);
        nnzu = pinfo[10];
        ulag = new double[nnzu];

        // Solve the problem again
        ifail = -1;
        e04mt.eval(handle, nvar, x ,nnzu, ulag, rinfo, stats, monit, iuser, ruser, cpuser, ifail);

        // Free the memory
        ifail = 0;
        E04RZ e04rz = new E04RZ();
        e04rz.eval(handle, ifail);
    }

    public static class MONIT extends E04MT.Abstract_E04MT_MONIT {

        public void eval() {
          E04MTU e04mtu = new E04MTU();
          e04mtu.eval(this.HANDLE, this.RINFO, this.STATS, this.IUSER, this.RUSER,
              this.CPUSER, this.INFORM);
          this.INFORM = e04mtu.getINFORM();
        }
      }
}
