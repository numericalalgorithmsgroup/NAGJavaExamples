import com.nag.routines.G02.G02BX;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RS;
import com.nag.routines.E04.E04RJ;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04PT;
import com.nag.routines.E04.E04PTU;
import com.nag.routines.E04.E04RZ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileWriter;

public class portfolioOptimizationQCQP {

    public static String dataFile = "data" + File.separator + "djia_close_price.csv";

    public static void main(String[] args) {

        int i, j;

        E04RA e04ra = new E04RA();
        E04RS e04rs = new E04RS();
        E04RJ e04rj = new E04RJ();
        E04RH e04rh = new E04RH();
        E04ZM e04zm = new E04ZM();
        E04PT e04pt = new E04PT();
        MONIT monit = new MONIT();
        E04RZ e04rz = new E04RZ();

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
        for (Map.Entry<String, double[]> entry : closePrice.entrySet()) {
            double[] tempA = entry.getValue();
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
        for (j = 0; j < n; j++) {
            double sum = 0;
            for (i = 0; i < m - 1; i++) {
                sum += relRtn[i][j];
            }
            r[j] = sum;
            r[j] /= m - 1;
        }

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

        int itemsDiagLength = V.length;
        int itemsAboveDiagLength = (int) (Math.pow(itemsDiagLength, 2) - itemsDiagLength) / 2 + itemsDiagLength;
        int[] irowq = new int[itemsAboveDiagLength];
        int[] icolq = new int[itemsAboveDiagLength];
        double[] vVal = new double[itemsAboveDiagLength];
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

        n = closePrice.size();
        // Sparsity pattern of r, which is actually dense in this application
        int[] idxr = new int[n];
        for (i = 0; i < n; i++) {
            idxr[i] = i + 1;
        }

        // Input for linear constraint: e'x = 1
        int[] irowa = new int[n];
        int[] icola = new int[n];
        double[] a = new double[n];
        double[] bl = new double[1];
        double[] bu = new double[1];
        double[] blx = new double[n];
        double[] bux = new double[n];

        Arrays.fill(irowa, 1);    
        for (i = 0; i < n; i++) {
            icola[i] = i + 1;
        }   
        Arrays.fill(a, 1.0);    
        bl[0] = 1.0;
        bu[0] = 1.0;

        // Input for bound constraint: x >= 0
        Arrays.fill(blx, 0.0); 
        Arrays.fill(bux, 1.0e20);

        // Set step for mu
        int step = 2001;

        // Initialize output data: absolute risk and return
        ArrayList<Double> abRisk = new ArrayList<>();
        ArrayList<Double> abRtn = new ArrayList<>();

        int mu;
        long handle = 0;
        double[] q = new double[vVal.length];
        int idqc;
        double[] invertSignR = invertSignVector(r);
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
            ifail = 0;

            // Create problem handle
            e04ra.eval(handle, n, ifail);
            handle = e04ra.getHANDLE();

            // Set quadratic objective function
            // In qcqp standard form q should be 2*mu*V
            for (i = 0; i < q.length; i++) {
                q[i] = 2.0 * mu * vVal[i];
            }

            idqc = -1;
            e04rs.eval(handle, 0.0, nonZeroLength(invertSignR), idxr, invertSignR, nonZeroLength(q), irowq, icolq, q, idqc, ifail);

            // Set linear constraint e'x = 1
            e04rj.eval(handle, bl.length, bl, bu, nonZeroLength(a), irowa, icola, a, 0, ifail);

            // Set bound constraint
            e04rh.eval(handle, n, blx, bux, ifail);

            // set options
            e04zm.eval(handle, "Print Options = NO", ifail);
            e04zm.eval(handle, "Print Level = 1", ifail);
            e04zm.eval(handle, "Print File = -1", ifail);
            e04zm.eval(handle, "SOCP Scaling = A", ifail);

            // Call socp interior point solver
            ifail = 1;
            e04pt.eval(handle, n, x, 0, u, 0, uc, rinfo, stats, monit, iuser, ruser, cpuser, ifail);

            ifail = e04pt.getIFAIL();
            if (ifail == 0) {
                // Compute risk and return from the portfolio
                x2d = convert1DTo2D(x, x.length);
                VX = multiplyMatrices(V, x2d);
                XVX = multiplyMatrices(invertRowColMatrix(x2d), VX);

                abRisk.add(Math.sqrt(XVX[0][0]));

                r2d = convert1DTo2D(r, r.length);
                RX = multiplyMatrices(invertRowColMatrix(r2d), x2d);

                abRtn.add(RX[0][0]);
            }

            // Destroy the handle:
            e04rz.eval(handle, ifail);
            handle = e04rz.getHANDLE();
        }

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
        e04rs.eval(handle, 0.0, 0, idxr, r, nonZeroLength(q), irowq, icolq, q, idqc, ifail);

        // Set linear constraints
        e04rj.eval(handle, bl.length, bl, bu, nonZeroLength(a), irowa, icola, a, 0, ifail);

        // Set bound constraint
        e04rh.eval(handle, blx.length, blx, bux, ifail);

        // Set options
        e04zm.eval(handle, "Print Options = NO", ifail);
        e04zm.eval(handle, "Print Level = 1", ifail);
        e04zm.eval(handle, "Print File = -1", ifail);
        e04zm.eval(handle, "SOCP Scaling = A", ifail);

        // Call socp interior point solver
        x = new double[n + 1];
        e04pt.eval(handle, n + 1, x, 0, u, 0, uc, rinfo, stats, monit, iuser, ruser, cpuser, ifail);

        x2d = convert1DTo2D(x, n);
        VX = multiplyMatrices(V, x2d);
        XVX = multiplyMatrices(invertRowColMatrix(x2d), VX);

        double srRisk = Math.sqrt(XVX[0][0]) / x[n];

        r2d = convert1DTo2D(r, n);
        RX = multiplyMatrices(invertRowColMatrix(r2d), x2d);

        double srRtn = RX[0][0] / x[n];

        double[] srX = new double[n];
        for (i = 0; i < srX.length; i++) {
            srX[i] = x[i] / x[n];
        }

        // Portfolio optimization with tracking-error constraint

        // Generate a benchmark portfolio from efficient portfolio that maximize the
        // Sharpe ratio
        // Perturb x
        double[] b = new double[n];
        double sumB = 0;
        for (i = 0; i < b.length; i++) {
            b[i] = srX[i] + 1.0e-1;
            sumB += b[i];
        }

        // Normalize b
        for (i = 0; i < b.length; i++) {
            b[i] /= sumB;
        }

        // Set limit on tracking-error
        double tev = 0.000002;

        // Compute risk and return at the benchmark
        double[][] b2d = convert1DTo2D(b, n);
        double[][] VB = multiplyMatrices(V, b2d);
        double[][] BVB = multiplyMatrices(invertRowColMatrix(b2d), VB);

        double bRisk = Math.sqrt(BVB[0][0]);

        r2d = convert1DTo2D(r, n);
        double[][] RB = multiplyMatrices(invertRowColMatrix(r2d), b2d);

        double bRtn = RB[0][0];

        irowa = new int[n];
        icola = new int[n];
        a = new double[n];
        bl = new double[1];
        bu = new double[1];

        // Input for linear constraint: e'x = 0
        Arrays.fill(irowa, 1);
        for (i = 0; i < icola.length; i++) {
            icola[i] = i + 1;
        }
        Arrays.fill(a, 1.0);
        bl[0] = 0;
        bu[0] = 0;

        // Input for bound constraint: x >= -b
        blx = invertSignVector(b);
        Arrays.fill(bux, 1.0e20);

        // Initialize output data: TEV risk and return
        ArrayList<Double> tevRisk = new ArrayList<>();
        ArrayList<Double> tevRtn = new ArrayList<>();

        double[] rMu = new double[n];
        double[][] Vb;
        double[] Vb1d;
        x = new double[n];
        double[] xb;
        double[][] xb2d;
        double[][] xbVxb;

        for (mu = 0; mu < step; mu++) {
            ifail = 0;

            // Create problem handle
            e04ra.eval(handle, n, ifail);
            handle = e04ra.getHANDLE();

            // Set quadratic objective function
            // In qcqp standard form q should be 2*mu*V
            for (i = 0; i < q.length; i++) {
                q[i] = 2.0 * mu * vVal[i];
            }
            Vb = multiplyMatrices(V, b2d);
            Vb1d = convert2DTo1D(Vb);
            for (i = 0; i < rMu.length; i++) {
                rMu[i] = 2.0 * mu * Vb1d[i] - r[i];
            }
            idqc = -1;
            e04rs.eval(handle, 0.0, nonZeroLength(rMu), idxr, rMu, nonZeroLength(q), irowq, icolq, q, idqc, ifail);

            // Set quadratic constraint
            // In qcqp standard form q should be 2*V
            for (i = 0; i < q.length; i++) {
                q[i] = 2.0 * vVal[i];
            }
            idqc = 0;
            e04rs.eval(handle, -tev, 0, idxr, rMu, nonZeroLength(q), irowq, icolq, q, idqc, ifail);

            // Set linear constraint e'x = 1
            e04rj.eval(handle, bl.length, bl, bu, nonZeroLength(a), irowa, icola, a, 0, ifail);

            // Set bound constraint
            e04rh.eval(handle, blx.length, blx, bux, ifail);

            // Set options
            e04zm.eval(handle, "Print Options = NO", ifail);
            e04zm.eval(handle, "Print Level = 1", ifail);
            e04zm.eval(handle, "Print File = -1", ifail);
            e04zm.eval(handle, "SOCP Scaling = A", ifail);

            // Call socp interior point solver
            // Mute warnings and do not count results from warnings
            ifail = -1;
            e04pt.eval(handle, n, x, 0, u, 0, uc, rinfo, stats, monit, iuser, ruser, cpuser, ifail);

            ifail = e04pt.getIFAIL();
            if (ifail == 0) {
                // Compute risk and return from the portfolio
                xb = addVectors(x, b);
                xb2d = convert1DTo2D(xb, xb.length);
                xbVxb = multiplyMatrices(invertRowColMatrix(xb2d), multiplyMatrices(V, xb2d));
                tevRisk.add(Math.sqrt(xbVxb[0][0]));

                tevRtn.add(multiplyMatrices(invertRowColMatrix(r2d), xb2d)[0][0]);
            }

            // Destroy the handle:
            e04rz.eval(handle, ifail);
            handle = e04rz.getHANDLE();
        }
    }

    public static class MONIT extends E04PT.Abstract_E04PT_MONIT {
        public void eval() {
            E04PTU e04ptu = new E04PTU();
            e04ptu.eval(this.HANDLE, this.RINFO, this.STATS, this.IUSER, this.RUSER, this.CPUSER, this.INFORM);
            this.INFORM = e04ptu.getINFORM();
        }
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

    public static double[] addVectors(double[] a, double[] b) {
        double[] t = new double[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = a[i] + b[i];
        }
        return t;
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
