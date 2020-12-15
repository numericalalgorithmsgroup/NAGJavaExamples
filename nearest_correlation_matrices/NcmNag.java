import com.nag.routines.G02.G02AA;
import com.nag.routines.F01.F01CK;
import com.nag.routines.F08.F08NA;
import com.nag.routines.F06.F06RC;

import java.lang.Math;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class NcmNag {
    public static void main(String[] args) {

        double[][] P = new double[][] { { 59.875, 42.734, 47.938, 60.359, 54.016, 69.625, 61.500, 62.125 },
                { 53.188, 49.000, 39.500, Double.NaN, 34.750, Double.NaN, 83.000, 44.500 },
                { 55.750, 50.000, 38.938, Double.NaN, 30.188, Double.NaN, 70.875, 29.938 },
                { 65.500, 51.063, 45.563, 69.313, 48.250, 62.375, 85.250, Double.NaN },
                { 69.938, 47.000, 52.313, 71.016, Double.NaN, 59.359, 61.188, 48.219 },
                { 61.500, 44.188, 53.438, 57.000, 35.313, 55.813, 51.500, 62.188 },
                { 59.230, 48.210, 62.190, 61.390, 54.310, 70.170, 61.750, 91.080 },
                { 61.230, 48.700, 60.300, 68.580, 61.250, 70.340, Double.NaN, Double.NaN },
                { 52.900, 52.690, 54.230, Double.NaN, 68.170, 70.600, 57.870, 88.640 },
                { 57.370, 59.040, 59.870, 62.090, 61.620, 66.470, 65.370, 85.840 } };

        double[][] G = cor_bar(P);
        System.out.println("The approximate correlation matrix");
        printMatrix(G);

        System.out.println();

        F08NA f08na = new F08NA();
        String jobvl = "N";
        String jobvr = "N";
        int n = G[0].length;
        double[] G1d = convert2DTo1D(G);
        int lda = G.length;
        double[] wr = new double[n];
        double[] wi = new double[n];
        int ldvl = 1;
        double[] vl = new double[ldvl];
        int ldvr = 1;
        double[] vr = new double[ldvr];
        int lwork = 3 * n;
        double[] work = new double[lwork];
        int info = 0;
        f08na.eval(jobvl, jobvr, n, G1d, lda, wr, wi, vl, ldvl, vr, ldvr, work, lwork, info);

        System.out.print("Sorted eigenvalues of G: ");
        Arrays.sort(wr);
        printVector(wr);

        System.out.println();

        // Call NAG routine G02AA and print the result
        G02AA g02aa = new G02AA();
        G1d = convert2DTo1D(G);
        n = G.length;
        int ldg = n;
        int ldx = n;
        double errtol = 0.0;
        int maxits = 0;
        int maxit = 0;
        double[] X1d = new double[ldx * n];
        int iter = 0;
        int feval = 0;
        double nrmgrd = 0.0;
        int ifail = 0;
        g02aa.eval(G1d, ldg, n, errtol, maxits, maxit, X1d, ldx, iter, feval, nrmgrd, ifail);

        double[][] X = convert1DTo2D(X1d, ldx);

        iter = g02aa.getITER();

        System.out.println("Nearest correlation matrix");
        printMatrix(X);

        System.out.println();

        jobvl = "N";
        jobvr = "N";
        n = X[0].length;
        lda = X.length;
        wr = new double[n];
        wi = new double[n];
        ldvl = 1;
        vl = new double[ldvl];
        ldvr = 1;
        vr = new double[ldvr];
        lwork = 3 * n;
        work = new double[lwork];
        info = 0;
        f08na.eval(jobvl, jobvr, n, X1d, lda, wr, wi, vl, ldvl, vr, ldvr, work, lwork, info);

        System.out.print("Sorted eigenvalues of X: ");
        Arrays.sort(wr);
        printVector(wr);

        System.out.println();

        try {
            FileWriter writer = new FileWriter(new File("g02aa.d"));
            writer.write(iter + "\n");
            writer.write(X.sub(G).norm2() + "\n");
            for (int i = 0; i < X.rows; i++) {
                for (int j = 0; j < X.columns; j++) {
                    writer.write(matrixAbs(X.sub(G)).get(i, j) + " ");
                }
                writer.write("\n");
            }
            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Returns an approximate sample covariance matrix
     * 
     * @param P
     * @return
     */
    public static double[][] cov_bar(double[][] P) {

        double[] xi, xj;
        boolean[] xib, xjb, notp;
        int n = P[0].length;
        double[][] S = new double[n][n];
        int notpFalseCount;

        for (int i = 0; i < n; i++) {
            // Take the ith column
            xi = getMatrixColumn(P, i);
            for (int j = 0; j < i + 1; j++) {
                // Take the jth column, where j <= i
                xj = getMatrixColumn(P, j);

                // Set mask such that all NaNs are true
                xib = getNanMask(xi);
                xjb = getNanMask(xj);

                notp = addBoolArrOr(xib, xjb);

                S[i][j] = matrixMaskedDot(vectorSubScalar(xi, vectorMaskedMean(xi, notp)),
                        vectorSubScalar(xj, vectorMaskedMean(xj, notp)), notp);

                // Take the sum over !notp to normalize
                notpFalseCount = 0;
                for (boolean b : notp) {
                    if (!b) {
                        notpFalseCount++;
                    }
                }
                S[i][j] = 1.0 / (notpFalseCount - 1) * S[i][j];
                S[j][i] = S[i][j];
            }
        }
        return S;
    }

    /**
     * Returns an approximate sample correlation matrix
     * 
     * @param P
     */
    public static double[][] cor_bar(double[][] P) {
        double[][] S, D;
        S = cov_bar(P);
        D = getMatrixFromDiag(vectorRightDiv(vectorSqrt(getMatrixDiag(S)), 1.0));

        F01CK f01ck = new F01CK();
        double[] S_ = new double[S.length * S[0].length];
        double[] S1d = convert2DTo1D(S);
        double[] D1d = convert2DTo1D(D);
        int n = S.length;
        int p = n;
        int m = n;
        double[] z = new double[0];
        int iz = 0;
        int opt = 1;
        int ifail = 0;
        f01ck.eval(S_, S1d, D1d, n, p, m, z, iz, opt, ifail);

        double[] D_ = new double[n * n];
        f01ck.eval(D_, D1d, S_, n, p, m, z, iz, opt, ifail);

        return convert1DTo2D(D_, n);
    }

    public static double matrixMaskedDot(double[] a, double[] b, boolean[] mask) {
        if ((a.length != b.length) || (a.length != mask.length) || (b.length != mask.length)) {
            System.out.println("Arrays a(" + a.length + "), b(" + b.length + ") and mask(" + mask.length
                    + ") need to have the same length.");
            System.exit(-1);
        }

        double r = 0;
        for (int i = 0; i < a.length; i++) {
            if (!mask[i]) {
                r += a[i] * b[i];
            }
        }
        return r;
    }

    public static double[] vectorSqrt(double[] a) {
        double[] t = new double[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = Math.sqrt(a[i]);
        }
        return t;
    }

    public static double[] getMatrixDiag(double[][] a) {
        double[] diag = new double[a.length];
        for (int i = 0; i < diag.length; i++) {
            diag[i] = a[i][i];
        }
        return diag;
    }

    public static double[][] getMatrixFromDiag(double[] diag) {
        double[][] t = new double[diag.length][diag.length];
        for (int i = 0; i < t.length; i++) {
            t[i][i] = diag[i];
        }
        return t;
    }

    public static double[] vectorRightDiv(double[] a, double s) {
        double[] t = new double[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = s / a[i];
        }
        return t;
    }

    public static double[] getMatrixColumn(double[][] a, int col) {
        double[] t = new double[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = a[i][col];
        }
        return t;
    }

    public static double vectorMaskedMean(double[] a, boolean[] mask) {
        double sum = 0;
        int n = a.length;
        for (int i = 0; i < a.length; i++) {
            if (mask[i]) {
                n--;
            } else {
                sum += a[i];
            }
        }
        return sum / n;
    }

    public static double[] vectorSubScalar(double[] a, double s) {
        double[] t = new double[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = a[i] - s;
        }
        return t;
    }

    public static boolean[] getNanMask(double[] a) {
        boolean[] t = new boolean[a.length];
        for (int i = 0; i < t.length; i++) {
            if (Double.isNaN(a[i])) {
                t[i] = true;
            } else {
                t[i] = false;
            }
        }
        return t;
    }

    public static boolean[] addBoolArrOr(boolean[] a, boolean[] b) {
        if (a.length != b.length) {
            System.out.println("Arrays a(" + a.length + ") and b(" + b.length + ") need to have the same length.");
            System.exit(-1);
        }

        boolean[] t = new boolean[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = a[i] || b[i];
        }
        return t;
    }

    public static double[][] matrixAbs(double[][] a) {
        double[][] b = new double[a.length][a[0].length];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                b[i][j] = Math.abs(a[i][j]);
            }
        }
        return b;
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

    public static void printMatrix(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.printf("%7.4f ", a[i][j]);
            }
            System.out.println();
        }
    }

    public static void printVector(double[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%7.4f ", a[i]);
        }
        System.out.println();
    }

    public static void printVector(boolean[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%5b ", a[i]);
        }
        System.out.println();
    }
}
