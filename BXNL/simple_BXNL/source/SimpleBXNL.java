import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RM;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04RX;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04GG;
import com.nag.routines.E04.E04GGU;
import com.nag.routines.E04.E04GGV;
import com.nag.routines.E04.E04FFU;

import java.lang.Math;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class SimpleBXNL {

    public final static String dataFolder = ".." + File.separator + "data";

    public static void main(String[] args) {

        int i;

        // problem data
        // number of observations
        int nres = 64;
        // ovservations
        int[] diameter = new int[nres];
        for (i = 0; i < nres; i++) {
            diameter[i] = i + 1;
        }
        double[] density = new double[] { 0.0722713864, 0.0575221239, 0.0604719764, 0.0405604720, 0.0317109145,
                0.0309734513, 0.0258112094, 0.0228613569, 0.0213864307, 0.0213864307, 0.0147492625, 0.0213864307,
                0.0243362832, 0.0169616519, 0.0095870206, 0.0147492625, 0.0140117994, 0.0132743363, 0.0147492625,
                0.0140117994, 0.0140117994, 0.0132743363, 0.0117994100, 0.0132743363, 0.0110619469, 0.0103244838,
                0.0117994100, 0.0117994100, 0.0147492625, 0.0110619469, 0.0132743363, 0.0206489676, 0.0169616519,
                0.0169616519, 0.0280235988, 0.0221238938, 0.0235988201, 0.0221238938, 0.0206489676, 0.0228613569,
                0.0184365782, 0.0176991150, 0.0132743363, 0.0132743363, 0.0088495575, 0.0095870206, 0.0073746313,
                0.0110619469, 0.0036873156, 0.0051622419, 0.0058997050, 0.0014749263, 0.0022123894, 0.0029498525,
                0.0014749263, 0.0007374631, 0.0014749263, 0.0014749263, 0.0007374631, 0.0000000000, 0.0000000000,
                0.0000000000, 0.0000000000, 0.0000000000 };

        // Define iuser and ruser to be passed to the callback functions
        int[] iuser = diameter;
        double[] ruser = density;

        // Print data to files for creating the figures in the doc
        printVectorToFile(diameter, "diameter.d");
        printVectorToFile(density, "density.d");

        // parameter vector: x = (a, b, Al, mu, sigma, Ag)
        int nvar = 6;

        // Initialize the model handle
        E04RA e04ra = new E04RA();
        long handle = 0;
        int ifail = 0;
        e04ra.eval(handle, nvar, ifail);

        handle = e04ra.getHANDLE();

        // Define a dense nonlinear least-squares objective function
        E04RM e04rm = new E04RM();
        ifail = 0;
        e04rm.eval(handle, nres, 0, 0, new int[] {}, new int[] {}, ifail);

        // Add weights for each residual
        double[] weights = new double[nres];
        Arrays.fill(weights, 1.0);
        for (i = 55; i < 63; i++) {
            weights[i] = 5.0;
        }
        double weights_sum = Arrays.stream(weights).sum();
        for (i = 0; i < weights.length; i++) {
            weights[i] /= weights_sum;
        }

        // Define the reliability of the measurements (weights)
        E04RX e04rx = new E04RX();
        ifail = 0;
        e04rx.eval(handle, "RW", 0, weights.length, weights, ifail);

        // Restrict parameter space (0 <= x)
        E04RH e04rh = new E04RH();
        double[] bl = new double[nvar];
        double[] bu = new double[nvar];
        Arrays.fill(bu, 100.0);
        ifail = 0;
        e04rh.eval(handle, nvar, bl, bu, ifail);

        // Set some optional parameters to control the output of the solver
        E04ZM e04zm = new E04ZM();
        ifail = 0;

        e04zm.eval(handle, "Print Options = NO", ifail);
        e04zm.eval(handle, "Print Level = 1", ifail);
        e04zm.eval(handle, "Print Solution = X", ifail);
        e04zm.eval(handle, "Bxnl Iteration Limit = 100", ifail);
        // Add cubic regularization term (avoid overfitting)
        e04zm.eval(handle, "Bxnl Use weights = YES", ifail);
        e04zm.eval(handle, "Bxnl Reg Order = 3", ifail);
        e04zm.eval(handle, "Bxnl Glob Method = REG", ifail);

        // Define initial guess (starting point)
        double[] x = new double[] { 1.63, 0.88, 1.0, 30, 1.52, 0.24 };

        // Call the solver
        E04GG e04gg = new E04GG();
        LSQFUN lsqfun = new LSQFUN();
        LSQGRD lsqgrd = new LSQGRD();
        LSQHES lsqhes = new LSQHES();
        LSQHPRD lsqhprd = new LSQHPRD();
        MONIT monit = new MONIT();
        double[] rx = new double[nres];
        double[] rinfo = new double[100];
        double[] stats = new double[100];
        long cpuser = 0;
        ifail = 0;

        e04gg.eval(handle, lsqfun, lsqgrd, lsqhes, lsqhprd, monit, nvar, x, nres, rx, rinfo, stats, iuser, ruser,
                cpuser, ifail);

        System.out.println();

        // Optimal parameter values
        // Al * log-Normal(a, b):
        double aopt = x[0];
        double bopt = x[1];
        double Alopt = x[2];

        // Ag * gaussian(mu, sigma):
        double muopt = x[3];
        double sigmaopt = x[4];
        double Agopt = x[5];

        // objective function value
        System.out.println("Objective Function Value: " + rinfo[0]);

        // Print data to files for creating the figures in the doc
        double[] dh = new double[10 * nres + 8];
        for (i = 0; i < dh.length; i++) {
            dh[i] = (i + 1) / 10.0;
        }
        double[] lopt = lognormal(dh, aopt, bopt, Alopt);
        double[] gopt = gaussian(dh, muopt, sigmaopt, Agopt);
        double[] w = new double[lopt.length];
        for (i = 0; i < w.length; i++) {
            w[i] = lopt[i] + gopt[i];
        }
        printVectorToFile(dh, "dh.d");
        printVectorToFile(lopt, "lopt.d");
        printVectorToFile(gopt, "gopt.d");
        printVectorToFile(w, "w.d");
        printVectorToFile(x, "x.d");

        // Destroy the handle:
        E04RZ e04rz = new E04RZ();
        ifail = 0;
        e04rz.eval(handle, ifail);
    }

    // Define Normal and log-Normal distributions
    public static double lognormal(int d, double a, double b, double Al) {
        return Al / (d * b * Math.sqrt(2 * Math.PI)) * Math.exp(-(Math.pow(Math.log(d) - a, 2)) / (2 * Math.pow(b, 2)));
    }

    public static double gaussian(int d, double mu, double sigma, double Ag) {
        return Ag * Math.exp(-0.5 * Math.pow((d - mu) / sigma, 2)) / (sigma * Math.sqrt(2 * Math.PI));
    }

    public static double[] lognormal(double[] d, double a, double b, double Al) {
        double[] result = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            result[i] = Al / (d[i] * b * Math.sqrt(2 * Math.PI)) * Math.exp(-(Math.pow(Math.log(d[i]) - a, 2)) / (2 * Math.pow(b, 2)));
        }
        return result;
    }

    public static double[] gaussian(double[] d, double mu, double sigma, double Ag) {
        double[] result = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            result[i] = Ag * Math.exp(-0.5 * Math.pow((d[i] - mu) / sigma, 2)) / (sigma * Math.sqrt(2 * Math.PI));
        }
        return result;
    }

    // Define the least-square function as a mixture of Normal and log-Normal
    // functions. Also add its first derivatives

    /**
     * Objective function callback passed to the least squares solver. x = (a, b,
     * Al, mu, sigma, Ag)
     */
    public static class LSQFUN extends E04GG.Abstract_E04GG_LSQFUN {
        public void eval() {
            int[] d = this.IUSER;
            double[] y = this.RUSER;
            double a = this.X[0];
            double b = this.X[1];
            double Al = this.X[2];
            double mu = this.X[3];
            double sigma = this.X[4];
            double Ag = this.X[5];

            for (int i = 0; i < this.NRES; i++) {
                this.RX[i] = lognormal(d[i], a, b, Al) + gaussian(d[i], mu, sigma, Ag) - y[i];
            }
        }
    }

    /**
     * Computes the Jacobian of the least square residuals. x = (a, b, Al, mu,
     * sigma, Ag)
     */
    public static class LSQGRD extends E04GG.Abstract_E04GG_LSQGRD {
        public void eval() {
            int n = this.X.length;
            int[] d = this.IUSER;
            double a = this.X[0];
            double b = this.X[1];
            double Al = this.X[2];
            double mu = this.X[3];
            double sigma = this.X[4];
            double Ag = this.X[5];
            for (int i = 0; i < this.NRES; i++) {
                // log-Normal derivatives
                double l = lognormal(d[i], a, b, Al);
                // dl/da
                this.RDX[i * n + 0] = (Math.log(d[i]) - a) / Math.pow(b, 2) * l;
                // dl/db
                this.RDX[i * n + 1] = (Math.pow(Math.log(d[i]) - a, 2) - Math.pow(b, 2)) / Math.pow(b, 3) * l;
                // dl/dAl
                this.RDX[i * n + 2] = lognormal(d[i], a, b, 1.0);
                // Gaussian derivatives
                double g = gaussian(d[i], mu, sigma, Ag);
                // dg/dmu
                this.RDX[i * n + 3] = (d[i] - mu) / Math.pow(sigma, 2) * g;
                // dg/dsigma
                this.RDX[i * n + 4] = (Math.pow(d[i] - mu, 2) - Math.pow(sigma, 2)) / Math.pow(sigma, 3) * g;
                // dg/dAg
                this.RDX[i * n + 5] = gaussian(d[i], mu, sigma, 1.0);
            }
        }
    }

    public static class LSQHES extends E04GG.Abstract_E04GG_LSQHES {
        public void eval() {
            E04GGU e04ggu = new E04GGU();
            e04ggu.eval(this.NVAR, this.X, this.NRES, this.LAMBDA, this.HX, this.INFORM, this.IUSER, this.RUSER,
                    this.CPUSER);
            this.INFORM = e04ggu.getINFORM();
        }
    }

    public static class LSQHPRD extends E04GG.Abstract_E04GG_LSQHPRD {
        public void eval() {
            E04GGV e04ggv = new E04GGV();
            e04ggv.eval(this.NVAR, this.X, this.Y, this.NRES, this.HXY, this.INFORM, this.IUSER, this.RUSER,
                    this.CPUSER);
            this.INFORM = e04ggv.getINFORM();
        }
    }

    public static class MONIT extends E04GG.Abstract_E04GG_MONIT {
        public void eval() {
            E04FFU e04ffu = new E04FFU();
            e04ffu.eval(this.NVAR, this.X, this.INFORM, this.RINFO, this.STATS, this.IUSER, this.RUSER, this.CPUSER);
            this.INFORM = e04ffu.getINFORM();
        }
    }

    public static void printVectorToFile(double[] a, String fileName) {
        try {
            FileWriter writer = new FileWriter(new File(dataFolder + File.separator + fileName));
            for (int i = 0; i < a.length; i++) {
                writer.write(a[i] + " ");
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void printVectorToFile(int[] a, String fileName) {
        try {
            FileWriter writer = new FileWriter(new File(dataFolder + File.separator + fileName));
            for (int i = 0; i < a.length; i++) {
                writer.write(a[i] + " ");
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
