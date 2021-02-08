import com.nag.routines.G05.G05SQ;
import com.nag.routines.G05.G05KG;
import com.nag.routines.S.S30AA;
import com.nag.routines.S.S30AC;
import com.nag.routines.C05.C05AW;

import java.util.Map;
import java.util.HashMap;

public class impVolDemo {

    public static void main(String[] args) {

        int i;

        

        // Let's generate some input data using a random number generator from the NAG
        // Library:
        G05KG g05kg = new G05KG();
        G05SQ g05sq = new G05SQ();
        int ifail = 0;
        int lstate = 17;
        int[] state = new int[lstate];
        int n = 10000; // This is the number of volatilities we will be computing

        double[] p = new double[n];
        double[] k = new double[n];
        double[] s0 = new double[n];
        double[] t = new double[n];
        double[] r = new double[n];

        g05kg.eval(1, 0, state, lstate, ifail);
        g05sq.eval(n, 3.9, 5.8, state, p, ifail);
        g05sq.eval(n, 271.5, 272.0, state, k, ifail);
        g05sq.eval(n, 259.0, 271.0, state, s0, ifail);
        g05sq.eval(n, 0.016, 0.017, state, t, ifail);
        g05sq.eval(n, 0.017, 0.018, state, r, ifail);


        int[] iuser = new int[5];
        double[] ruser = new double[5];
        int errorcount = 0;
        double sigma;

        C05AW c05aw = new C05AW();
        BlackScholes blackScholes = new BlackScholes();

        long tic = System.currentTimeMillis();
        for (i = 0; i < n; i++) {
            //System.out.println("Info: i = " + i);
            ruser[0] = p[i];
            ruser[1] = k[i];
            ruser[2] = s0[i];
            ruser[3] = t[i];
            ruser[4] = r[i];

            ifail = 1;
            c05aw.eval(0.15, 1.0e-14, 0.0, blackScholes, 500, iuser, ruser, ifail);

            sigma = c05aw.getX();
            ifail = c05aw.getIFAIL();

            if ((sigma < 0.0) || (ifail != 0)) {
                errorcount++;
            }
        }
        long toc = System.currentTimeMillis();

        System.out.println("Using a general purpose root finder:");
        System.out.printf("    Time taken: %.5f seconds\n", (toc-tic)/1000.0);
        System.out.printf("    There were %d failures\n", errorcount);

        System.out.println();


        S30AC s30ac = new S30AC();
        double[] sigma_arr = new double[n];
        int[] ivalid = new int[n];

        tic = System.currentTimeMillis();
        ifail = 0;
        s30ac.eval("C", n, p, k, s0, t, r, sigma_arr, 2, ivalid, ifail);
        toc = System.currentTimeMillis();

        System.out.println("S30AC with mode = 2 (Jäckel algorithm):");
        System.out.printf("    Time taken: %.5f seconds\n", (toc-tic)/1000.0);
        System.out.printf("    There were %d failures\n", nonZeroLength(ivalid));

        System.out.println();


        tic = System.currentTimeMillis();
        ifail = 0;
        s30ac.eval("C", n, p, k, s0, t, r, sigma_arr, 1, ivalid, ifail);
        toc = System.currentTimeMillis();

        System.out.println("S30AC with mode = 1 (Glau algorithm):");
        System.out.printf("    Time taken: %.5f seconds\n", (toc-tic)/1000.0);
        System.out.printf("    There were %d failures\n", nonZeroLength(ivalid));

        System.out.println();


        tic = System.currentTimeMillis();
        ifail = 0;
        s30ac.eval("C", n, p, k, s0, t, r, sigma_arr, 0, ivalid, ifail);
        toc = System.currentTimeMillis();

        System.out.println("S30AC with mode = 0 (lower accuracy Glau algorithm):");
        System.out.printf("    Time taken: %.5f seconds\n", (toc-tic)/1000.0);
        System.out.printf("    There were %d failures\n", nonZeroLength(ivalid));

        System.out.println();
    }

    public static class BlackScholes extends C05AW.Abstract_C05AW_F {
        public double eval() {
            double[] price = new double[1];
            int ifail = 1;
    
            S30AA s30aa = new S30AA();
            s30aa.eval("C", 1, 1, new double[]{this.getRUSER()[1]}, this.getRUSER()[2], new double[]{this.getRUSER()[3]}, this.getX(), this.getRUSER()[4], 0.0, price, 1, ifail);
    
            ifail = s30aa.getIFAIL();
    
            if (ifail != 0)
                price[0] = 0.0; 
    
            return price[0] - this.getRUSER()[0];
        }
    }

    public static int nonZeroLength(int[] a) {
        int c = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                c++;
            }
        }
        return c;
    }

    public static void printVector(double[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%8.4f ", a[i]);
        }
        System.out.println();
    }

    public static void printVector(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%5d ", a[i]);
        }
        System.out.println();
    }
}
