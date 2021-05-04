import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RG;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04KF;
import com.nag.routines.E04.E04RX;
import com.nag.routines.E04.E04RZ;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Rosenbrock2d {

    public final static String dataFolder = "data";

    private static List<Double>[] steps = new ArrayList[3];

    public static void main(String[] args) {

        for (int i = 0; i < steps.length; i++) {
            steps[i] = new ArrayList<Double>();
        }

        // Specify initial guess
        double[] x = new double[] { -1.0, -1.5 };

        int nvar = x.length;
        long handle = 0;
        int ifail;
        int[] idxfd = new int[nvar];
        for (int i = 0; i < nvar; i++) {
            idxfd[i] = i + 1;
        }

        E04RA e04ra = new E04RA();
        E04RG e04rg = new E04RG();
        E04RH e04rh = new E04RH();
        E04ZM e04zm = new E04ZM();
        E04KF e04kf = new E04KF();

        ifail = 0;
        e04ra.eval(handle, nvar, ifail);
        handle = e04ra.getHANDLE();

        ifail = 0;
        e04rg.eval(handle, nvar, idxfd, ifail);

        double[] bl = new double[] { -1.0, -2.0 };
        double[] bu = new double[] { 0.8, 2.0 };
        ifail = 0;
        e04rh.eval(handle, nvar, bl, bu, ifail);

        ifail = 0;
        e04zm.eval(handle, "FOAS Print Frequency = 1", ifail);
        e04zm.eval(handle, "Print Solution = yes", ifail);
        e04zm.eval(handle, "FOAS Monitor Frequency = 1", ifail);
        e04zm.eval(handle, "Print Level = 2", ifail);
        e04zm.eval(handle, "Monitoring Level = 1", ifail);

        // Solve the problem
        OBJFUN objfun = new OBJFUN();
        OBJGRD objgrd = new OBJGRD();
        MONIT monit = new MONIT();
        double[] rinfo = new double[100];
        double[] stats = new double[100];
        int[] iuser = new int[0];
        double[] ruser = new double[0];
        long cpuser = 0;
        ifail = 0;
        e04kf.eval(handle, objfun, objgrd, monit, nvar, x, rinfo, stats, iuser, ruser, cpuser, ifail);

        // Add last step
        steps[0].add(x[0]);
        steps[1].add(x[1]);
        steps[2].add(rinfo[0]);

        System.out.println();

        double[] mult = new double[2 * nvar];
        Arrays.fill(mult, 0.0);

        E04RX e04rx = new E04RX();
        ifail = 0;
        e04rx.eval(handle, "Dual Variables", 1, 2 * nvar, mult, ifail);

        double[] mult_t = new double[mult.length / 2];
        for (int i = 0; i < mult.length; i += 2) {
            mult_t[i / 2] = mult[i] - mult[i + 1];
        }
        System.out.print("Lagrange multipliers: [ ");
        for (double num : mult_t) {
            System.out.print((double) Math.round(num * 10d) / 10d + " ");
        }
        System.out.println("]");

        // Destroy the handle
        E04RZ e04rz = new E04RZ();
        e04rz.eval(handle, ifail);

        // Evaluate the funtion over the domain
        double[] x_m = linspace(bl[0] - 0.5, bu[0] + 0.5, 101);
        double[] y_m = linspace(bl[1] - 0.5, bu[1] + 0.5, 101);
        double[][] z_m = new double[101][101];

        for (int i = 0; i < 101; i++) {
            for (int j = 0; j < 101; j++) {
                z_m[i][j] = objfunEval(x_m[i], y_m[j]);
            }
        }
        int inform = 1;

        int nb = 25;
        double[] x_box = linspace(bl[0], bu[0], nb);
        double[] y_box = linspace(bl[1], bu[1], nb);

        double[][] box = new double[2][100];

        for (int i = 0; i < nb; i++) {
            box[0][i] = x_box[i];
            box[0][nb + i] = bu[0];
            box[0][nb * 2 + i] = x_box[nb - 1 - i];
            box[0][nb * 3 + i] = bl[0];

            box[1][i] = bl[1];
            box[1][nb + i] = y_box[i];
            box[1][nb * 2 + i] = bu[1];
            box[1][nb * 3 + i] = y_box[nb - 1 - i];
        }

        double[] z_box = new double[box[0].length];

        for (int i = 0; i < z_box.length; i++) {
            z_box[i] = objfunEval(box[0][i], box[1][i]);
        }

        double[][] X = new double[x_m.length][x_m.length];
        double[][] Y = new double[y_m.length][y_m.length];

        for (int i = 0; i < X.length; i++) {
            Arrays.fill(X[i], x_m[i]);
            for (int j = 0; j < Y[0].length; j++)
                Y[i][j] = y_m[j];
        }

        // Plot
        printMatrixToFile(box, "box.d");
        printVectorToFile(z_box, "z_box.d");
        printVectorToFile(bl, "bl.d");
        printVectorToFile(bu, "bu.d");
        printMatrixToFile(X, "X.d");
        printMatrixToFile(Y, "Y.d");
        printMatrixToFile(z_m, "z_m.d");
        printVectorToFile(toArray(steps[0]), "steps[0].d");
        printVectorToFile(toArray(steps[1]), "steps[1].d");
        printVectorToFile(toArray(steps[2]), "steps[2].d");
    }

    /**
     * The objective's function.
     */
    public static class OBJFUN extends E04KF.Abstract_E04KF_OBJFUN {
        public void eval() {
            this.FX = objfunEval(this.X[0], this.X[1]);
        }
    }

    /**
     * The objective's gradient.
     */
    public static class OBJGRD extends E04KF.Abstract_E04KF_OBJGRD {
        public void eval() {
            this.FDX[0] = 2.0 * this.X[0] - 400.0 * this.X[0] * (this.X[1] - Math.pow(this.X[0], 2)) - 2.0;
            this.FDX[1] = 200.0 * (this.X[1] - Math.pow(this.X[0], 2));
        }
    }

    /**
     * The monitor function.
     */
    public static class MONIT extends E04KF.Abstract_E04KF_MONIT {
        public void eval() {
            steps[0].add(this.X[0]);
            steps[1].add(this.X[1]);
            steps[2].add(this.RINFO[0]);
        }
    }

    private static double objfunEval(double x0, double x1) {
        return Math.pow(1 - x0, 2) + 100.0 * Math.pow(x1 - Math.pow(x0, 2), 2);
    }

    private static double[] linspace(double startPoint, double endPoint, int length) {
        double[] a = new double[length];
        double step = (endPoint - startPoint) / (length - 1);
        a[0] = startPoint;
        a[length - 1] = endPoint;
        for (int i = 1; i < length - 1; i++) {
            a[i] = startPoint + i * step;
        }
        return a;
    }

    public static void printMatrixToFile(double[][] a, String fileName) {
        try {
            FileWriter writer = new FileWriter(new File(dataFolder + File.separator + fileName));
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    writer.write(a[i][j] + " ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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

    public static double[] toArray(List<Double> list) {
        double[] t = new double[list.size()];
        for (int i = 0; i < t.length; i++) {
            t[i] = (double) list.get(i);
        }
        return t;
    }
}
