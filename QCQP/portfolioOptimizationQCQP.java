import com.nag.routines.G02.G02BX;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RS;
import com.nag.routines.E04.E04RJ;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04PT;
import com.nag.routines.E04.E04PTU;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04RY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.io.FileWriter;

public class portfolioOptimizationQCQP {

    public static String dataFile = "data" + File.separator + "djia_close_price.csv";

    public static void main(String[] args) {

        int i, j;

        // Data Preparation

        // Load stock price data from djia_close_price.csv
        String[] dateIndex = new String[0];
        Map<String, double[]> closePrice = new LinkedHashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));

            String line = reader.readLine().substring(1);
            dateIndex = line.split(",");

            String[] data;
            String key;
            double[] values;

            while ((line = reader.readLine()) != null) {
                data = line.split(",");
                key = data[0];
                values = parseDoubleArr(Arrays.copyOfRange(data, 1, data.length));
                closePrice.put(key, values);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.err.println("***FATAL: Can't find " + dataFile);
            System.exit(-2);
        } catch (IOException e) {
            System.err.println("***FATAL: Can't read " + dataFile + "\n" + e.getMessage());
        }

        int m = dateIndex.length;
        int n = closePrice.size();

        double[][] data = new double[m][n];
        i = 0;
        double[] tempA;
        for (Map.Entry<String, double[]> entry : closePrice.entrySet()) {
            tempA = entry.getValue();
            for (j = 0; j < m; j++) {
                data[j][i] = tempA[j];
            }
            i++;
        }

        // Relative return
        double[][] relRtn = new double[m - 1][n];

        for (j = 0; j < m - 1; j++) {
            for (i = 0; i < n; i++) {
                relRtn[j][i] = (data[j + 1][i] - data[j][i]) / data[j][i];
            }
        }

        // Mean return
        double[] r = new double[n];
        double sum;
        for (j = 0; j < n; j++) {
            sum = 0;
            for (i = 0; i < m - 1; i++) {
                sum += relRtn[i][j];
            }
            r[j] = sum;
            r[j] /= m - 1;
        }

        // printVectorToFile(r, "r_java.txt");

        // Covariance matrix
        G02BX g02bx = new G02BX();
        String weight = "U";
        n = relRtn.length;
        m = relRtn[0].length;
        int ldx = n;
        double[] x1d = convert2DTo1D(relRtn);
        double[] wt = new double[0];
        double[] xbar = new double[m];
        double[] std = new double[m];
        int ldv = m;
        double[] v1d = new double[ldv * m];
        double[] r1d = new double[ldv * m];
        int ifail = 0;
        g02bx.eval(weight, n, m, x1d, ldx, wt, xbar, std, v1d, ldv, r1d, ifail);

        double[][] V = convert1DTo2D(v1d, m);

        // Classic Mean-Variance Model

        // Efficient Frontier

        int itemsDiag = V.length;
        int itemsAboveDiag = (int) (Math.pow(itemsDiag, 2) - itemsDiag) / 2 + itemsDiag;
        int[] irowq = new int[itemsAboveDiag];
        int[] icolq = new int[itemsAboveDiag];
        double[] vVal = new double[itemsAboveDiag];
        int c = 0;
        // Input for quadratic objective
        // Sparsity pattern of upper triangular V
        for (i = 0; i < V.length; i++) {
            for (j = i; j < V[0].length; j++) {
                vVal[c] = V[i][j];
                irowq[c] = i + 1;
                icolq[c] = j + 1;
                c++;
            }
        }

        // printVectorToFile(vVal, "vVal_java.txt");

        n = closePrice.size();
        // Sparsity pattern of r, which is actually dense in this application
        int[] idxr = new int[n];
        for (i = 0; i < n; i++) {
            idxr[i] = i + 1;
        }

        // Input for linear constraint: e'x = 1
        int[] irowa = new int[n];
        Arrays.fill(irowa, 1);
        int[] icola = new int[n];
        for (i = 0; i < n; i++) {
            icola[i] = i + 1;
        }
        double[] a = new double[n];
        Arrays.fill(a, 1.0);
        double[] bl = new double[1];
        bl[0] = 1.0;
        double[] bu = new double[1];
        bu[0] = 1.0;

        // Input for bound constraint: x >= 0
        double[] blx = new double[n];
        Arrays.fill(blx, 0.0);
        double[] bux = new double[n];
        Arrays.fill(bux, 1.0e20);

        // Set step for mu
        int step = 2001;

        // Initialize output data: absolute risk and return
        double[] abRisk = new double[2001];
        double[] abRtn = new double[2001];

        E04RA e04ra = new E04RA();
        E04RS e04rs = new E04RS();
        E04RJ e04rj = new E04RJ();
        E04RH e04rh = new E04RH();
        E04ZM e04zm = new E04ZM();
        E04PT e04pt = new E04PT();
        MONIT monit = new MONIT();
        E04RZ e04rz = new E04RZ();
        E04RY e04ry = new E04RY();

        int mu;
        long handle = 0;
        double[] q = new double[vVal.length];
        int idqc;
        int nnzr = nonZeroLength(r);
        int nnzq = q.length;
        int nclin = bl.length;
        int nnza = nonZeroLength(a);
        double[] x = new double[n];
        double[] u = new double[0];
        double[] uc = new double[0];
        double[] rinfo = new double[100];
        double[] stats = new double[100];
        int[] iuser = new int[2];
        double[] ruser = new double[1];
        long cpuser = 0;
        double[][] x2d;
        double[][] VX;
        double[][] XVX;
        double[][] r2d;
        double[][] RX;

        for (mu = 0; mu < step; mu++) {
            // Create problem handle
            e04ra.eval(handle, n, ifail);
            handle = e04ra.getHANDLE();

            // Set quadratic objective function
            // In qcqp standard form q should be 2*mu*V
            for (i = 0; i < q.length; i++) {
                q[i] = 2.0 * mu * vVal[i];
            }

            nnzq = nonZeroLength(q);
            idqc = -1;
            ifail = 0;
            e04rs.eval(handle, 0.0, nnzr, idxr, invertSignVector(r), nnzq, irowq, icolq, q, idqc, ifail);

            // Set linear constraint e'x = 1
            e04rj.eval(handle, nclin, bl, bu, nnza, irowa, icola, a, 0, ifail);

            // Set bound constraint
            e04rh.eval(handle, n, blx, bux, ifail);

            // set options
            e04zm.eval(handle, "Print Options = NO", ifail);
            e04zm.eval(handle, "Print Level = 1", ifail);
            e04zm.eval(handle, "Print File = -1", ifail);
            e04zm.eval(handle, "SOCP Scaling = A", ifail);

            // if (mu == 255){
            // // e04ry.eval(handle, 6, "Overview, Objective, Simple bounds, Linear
            // constraints bounds, Linear constraints detailed", ifail);
            // // e04zm.eval(handle, "Print Options = YES", ifail);
            // // e04zm.eval(handle, "Print Level = 3", ifail);
            // // e04zm.eval(handle, "Print File = 6", ifail);
            // // e04zm.eval(handle, "Print Solution = YES", ifail);
            // printVectorToFile(r, "r_java.log");
            // printVectorToFile(q, "q_java.log");
            // }

            // Call socp interior point solver
            ifail = -1;
            e04pt.eval(handle, n, x, 0, u, 0, uc, rinfo, stats, monit, iuser, ruser, cpuser, ifail);

            ifail = e04pt.getIFAIL();
            if (ifail != 0) {
                System.out.println("mu = " + mu);
                System.out.println("ifail = " + ifail);
            }

            // Compute risk and return from the portfolio
            x2d = convert1DTo2D(x, n);
            VX = multiplyMatrices(V, x2d);
            XVX = multiplyMatrices(invertRowColMatrix(x2d), VX);
            abRisk[mu] = Math.sqrt(XVX[0][0]);

            r2d = convert1DTo2D(r, n);
            RX = multiplyMatrices(invertRowColMatrix(r2d), x2d);
            abRtn[mu] = RX[0][0];

            // Destroy the handle:
            e04rz.eval(handle, ifail);
            handle = e04rz.getHANDLE();
        }

        // System.out.println(ab_risk[0]);
        // System.out.println(ab_risk[2000]);
        // System.out.println(ab_rtn[0]);
        // System.out.println(ab_rtn[2000]);

        // Maximizing the Sharpe ratio

        // Input for linear constraint: e'y = lambda
        irowa = new int[(n + 1) + n];
        icola = new int[(n + 1) + n];
        a = new double[(n + 1) + n];
        bl = new double[2];
        bu = new double[2];
        blx = new double[n + 1];
        bux = new double[n + 1];

        Arrays.fill(irowa, 0, n + 1, 1);
        for (i = 0; i <= n; i++) {
            icola[i] = i + 1;
        }
        Arrays.fill(a, 0, n, 1.0);
        a[n] = -1.0;
        bl[0] = 0.0;
        bu[0] = 0.0;

        // Input for linear constraint: r'y = 1
        Arrays.fill(irowa, n + 1, irowa.length, 2);
        for (i = 0; i < n; i++) {
            icola[(n + 1) + i] = i + 1;
        }
        for (i = 0; i < n; i++) {
            a[(n + 1) + i] = r[i];
        }
        bl[1] = 1.0;
        bu[1] = 1.0;

        // Input for bound constraint: x >= 0
        Arrays.fill(blx, 0.0);
        Arrays.fill(bux, 1.0e20);

        ifail = 0;

        // Create problem handle
        e04ra.eval(handle, n + 1, ifail);
        handle = e04ra.getHANDLE();

        // Set quadratic objective function
        // In qcqp standard form q should be 2*V
        for (i = 0; i < q.length; i++) {
            q[i] = 2.0 * vVal[i];
        }
        idqc = -1;
        nnzq = nonZeroLength(q);
        e04rs.eval(handle, 0.0, 0, idxr, r, nnzq, irowq, icolq, q, idqc, ifail);

        // Set linear constraints
        nclin = bl.length;
        nnza = nonZeroLength(a);
        e04rj.eval(handle, nclin, bl, bu, nnza, irowa, icola, a, 0, ifail);

        // Set bound constraint
        e04rh.eval(handle, blx.length, blx, bux, ifail);

        // Set options
        e04zm.eval(handle, "Print Options = YES", ifail);
        e04zm.eval(handle, "Print Level = 3", ifail);
        e04zm.eval(handle, "Print File = 6", ifail);
        e04zm.eval(handle, "SOCP Scaling = A", ifail);

        e04zm.eval(handle, "Print Solution = YES", ifail);

        // e04ry.eval(handle, 6,
        //         "Overview, Objective, Simple bounds, Linear constraints bounds, Linear constraints detailed", ifail);

        // Call socp interior point solver
        x = new double[n + 1];
        e04pt.eval(handle, n + 1, x, 0, u, 0, uc, rinfo, stats, monit, iuser, ruser, cpuser, ifail);

        printVector(x);

        x2d = convert1DTo2D(x, n);
        VX = multiplyMatrices(V, x2d);
        XVX = multiplyMatrices(invertRowColMatrix(x2d), VX);
        double srRisk = Math.sqrt(XVX[0][0]) / x[n];

        r2d = convert1DTo2D(r, n);
        RX = multiplyMatrices(invertRowColMatrix(r2d), x2d);
        double srRtn = RX[0][0] / x[n];

        double[] srX = vectorDivScalar(x, x[n]);

        System.out.println(srRisk);
        System.out.println(srRtn);
        printVector(srX);
    }

    public static class MONIT extends E04PT.Abstract_E04PT_MONIT {
        public void eval() {
            E04PTU e04ptu = new E04PTU();
            e04ptu.eval(this.HANDLE, this.RINFO, this.STATS, this.IUSER, this.RUSER, this.CPUSER, this.INFORM);
            this.INFORM = e04ptu.getINFORM();
        }
    }

    public static double[] vectorDivScalar(double[] a, double s) {
        double[] t = new double[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = a[i] / s;
        }
        return t;
    }

    public static double[][] invertRowColMatrix(double[][] a) {
        double[][] t = new double[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                t[j][i] = a[i][j];
            }
        }
        return t;
    }

    public static double[] invertSignVector(double[] a) {
        double[] t = new double[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = -a[i];
        }
        return t;
    }

    public static int nonZeroLength(double[] a) {
        int c = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                c++;
            }
        }
        return c;
    }

    public static double[] parseDoubleArr(String[] a) {
        double[] t = new double[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = Double.parseDouble(a[i]);
        }
        return t;
    }

    public static double[] convert2DTo1D(double[][] a) {
        double[] b = new double[a.length * a[0].length];
        int n = a.length;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i + j * n] = a[i][j];
            }
        }
        return b;
    }

    public static double[][] convert1DTo2D(double[] a, int n) {
        double[][] b = new double[n][a.length / n];

        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                b[i][j] = a[i + j * n];
            }
        }
        return b;
    }

    public static double sum(double[][] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                sum += a[i][j];
            }
        }
        return sum;
    }

    public static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];
        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
            }
        }
        return result;
    }

    private static double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }
        return cell;
    }

    public static void printArr(String[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%s ", a[i]);
        }
        System.out.println();
    }

    public static void printMap(Map<String, double[]> map) {
        for (Map.Entry<String, double[]> entry : map.entrySet()) {
            System.out.printf("%4s: ", entry.getKey());
            printVector(entry.getValue());
        }
    }

    public static void printMatrix(double[][] a) {
        printMatrix(a, a.length, a[0].length);
    }

    public static void printMatrix(double[][] a, int row, int col) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.printf("%11.4e ", a[i][j]);
            }
            System.out.println();
        }
    }

    public static void printVector(double[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%.3e ", a[i]);
        }
        System.out.println();
    }

    public static void printVector(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%4d ", a[i]);
        }
        System.out.println();
    }

    public static void printVectorToFile(double[] a, String fileName) {
        try {
            FileWriter writer = new FileWriter(new File(fileName));
            for (int i = 0; i < a.length; i++) {
                writer.write(a[i] + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
