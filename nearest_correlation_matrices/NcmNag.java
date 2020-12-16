import com.nag.routines.G02.G02AA;
import com.nag.routines.G02.G02AB;
import com.nag.routines.G02.G02AJ;
import com.nag.routines.G02.G02AN;
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

        // Initialize our P matrix of observations

        // Define a 2-d array and use Double.NaN to set elements as NaNs
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

        // Compute the approximate correlation matrix

        double[][] G = cor_bar(P);
        System.out.println("The approximate correlation matrix");
        printMatrix(G);
        printMatrixToFile(G, "G.d");

        System.out.println();

        // Compute the eigenvalues of our (indefinite) G.

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
        Arrays.sort(wr);

        System.out.print("Sorted eigenvalues of G: ");
        printVector(wr);
        printVectorToFile(wr, "G_eigen.d");

        System.out.println();

        // Nearest Correlation Matrices

        // Using G02AA to compute the nearest correlation matrix in the Frobenius norm

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
        printMatrixToFile(X, "X_G02AA.d");

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
        Arrays.sort(wr);

        System.out.print("Sorted eigenvalues of X: ");
        printVector(wr);
        printVectorToFile(wr, "X_eigen_G02AA.d");

        System.out.println();

        double[][] X_G = matrixSub(X, G);
        F06RC f06rc = new F06RC();
        String norm = "F";
        String uplo = "U";
        n = X_G[0].length;
        double[] X_G1d = convert2DTo1D(X_G);
        lda = X_G.length;
        work = new double[n];
        double X_G_norm = f06rc.eval(norm, uplo, n, X_G1d, lda, work);

        printDataToFile("g02aa.d", iter, X_G, X_G_norm);

        // Weighting rows and columns of elements

        // Use G02AB to compute the nearest correlation matrix with row and column weighting

        // Define an arrray of weights
        double[] W = new double[] { 10, 10, 10, 1, 1, 1, 1, 1 };

        // Set up and call the NAG routine using weights and a minimum eigenvalue
        G02AB g02ab = new G02AB();
        G1d = convert2DTo1D(G);
        ldg = G.length;
        n = G[0].length;
        String opt = "B";
        double alpha = 0.001;
        errtol = 0.0;
        maxits = 0;
        maxit = 0;
        ldx = n;
        X1d = new double[ldx * n];
        iter = 0;
        feval = 0;
        nrmgrd = 0;
        ifail = 0;
        g02ab.eval(G1d, ldg, n, opt, alpha, W, errtol, maxits, maxit, X1d, ldx, iter, feval, nrmgrd, ifail);

        X = convert1DTo2D(X1d, ldx);
        iter = g02ab.getITER();

        System.out.println("Nearest correlation matrix using row and column weighting");
        printMatrix(X);
        printMatrixToFile(X, "X_G02AB.d");

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
        Arrays.sort(wr);

        System.out.print("Sorted eigenvalues of X: ");
        printVector(wr);
        printVectorToFile(wr, "X_eigen_G02AB.d");

        System.out.println();

        X_G = matrixSub(X, G);
        norm = "F";
        uplo = "U";
        n = X_G[0].length;
        X_G1d = convert2DTo1D(X_G);
        lda = X_G.length;
        work = new double[n];
        X_G_norm = f06rc.eval(norm, uplo, n, X_G1d, lda, work);

        printDataToFile("g02ab.d", iter, X_G, X_G_norm);

        // Weighting Individual Elements

        // Use G02AJ to compute the nearest correlation matrix with element-wise weighting

        // Set up a matrix of weights
        n = P[0].length;
        double[][] H = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((i < 3) && (j < 3)) {
                    H[i][j] = 100.0;
                } else {
                    H[i][j] = 1;
                }
            }
        }
        printMatrix(H);
        printMatrixToFile(H, "H_G02AJ.d");

        System.out.println();

        // Call the NAG routine specifying a minimum eigenvalue
        G02AJ g02aj = new G02AJ();
        G1d = convert2DTo1D(G);
        ldg = G.length;
        n = G[0].length;
        alpha = 0.001;
        double[] H1d = convert2DTo1D(H);
        int ldh = H.length;
        errtol = 0;
        maxit = 0;
        ldx = n;
        X1d = new double[ldx * n];
        iter = 0;
        double norm2 = 0;
        ifail = 0;
        g02aj.eval(G1d, ldg, n, alpha, H1d, ldh, errtol, maxit, X1d, ldx, iter, norm2, ifail);

        X = convert1DTo2D(X1d, ldx);
        iter = g02aj.getITER();

        System.out.println("Nearest correlation matrix using element-wise weighting");
        printMatrix(X);
        printMatrixToFile(X, "X_G02AJ.d");

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
        Arrays.sort(wr);

        System.out.print("Sorted eigenvalues of X: ");
        printVector(wr);
        printVectorToFile(wr, "X_eigen_G02AJ.d");

        System.out.println();

        X_G = matrixSub(X, G);
        norm = "F";
        uplo = "U";
        n = X_G[0].length;
        X_G1d = convert2DTo1D(X_G);
        lda = X_G.length;
        work = new double[n];
        X_G_norm = f06rc.eval(norm, uplo, n, X_G1d, lda, work);

        printDataToFile("g02aj.d", iter, X_G, X_G_norm);

        // Fixing a Block of Elements

        // Use G02AN to compute the nearest correlation matrix with fixed leading block

        // Call the NAG routine fixing the top 3-by-3 block
        G02AN g02an = new G02AN();
        G1d = convert2DTo1D(G);
        ldg = G.length;
        n = G[0].length;
        int k = 3;
        errtol = 0;
        double eigtol = 0;
        ldx = n;
        X1d = new double[ldx * n];
        alpha = 0.001;
        iter = 0;
        double eigmin = 0;
        norm2 = 0;
        ifail = 0; 
        g02an.eval(G1d, ldg, n, k, errtol, eigtol, X1d, ldx, alpha, iter, eigmin, norm2, ifail);

        X = convert1DTo2D(X1d, ldx);
        iter = g02an.getITER();
        alpha = g02an.getALPHA();

        System.out.println("Nearest correlation matrix with fixed leading block");
        printMatrix(X);
        printMatrixToFile(X, "X_G02AN.d");

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
        Arrays.sort(wr);

        System.out.print("Sorted eigenvalues of X: ");
        printVector(wr);
        printVectorToFile(wr, "X_eigen_G02AN.d");

        System.out.printf("Value of alpha returned: %.4f\n", alpha);
        printVectorToFile(new double[]{alpha}, "alpha_G02AN.d");

        System.out.println();

        X_G = matrixSub(X, G);
        norm = "F";
        uplo = "U";
        n = X_G[0].length;
        X_G1d = convert2DTo1D(X_G);
        lda = X_G.length;
        work = new double[n];
        X_G_norm = f06rc.eval(norm, uplo, n, X_G1d, lda, work);

        printDataToFile("g02an.d", iter, X_G, X_G_norm);

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

                // S[i][j] = (xi - mean(xi)) * (xj - mean(xj))
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
        // D = 1.0 / SQRT(S)
        D = getMatrixFromDiag(vectorRightDiv(vectorSqrt(getMatrixDiag(S)), 1.0));

        // S_ = S * D
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

        // D_ = D * S_
        double[] D_ = new double[n * n];
        f01ck.eval(D_, D1d, S_, n, p, m, z, iz, opt, ifail);

        return convert1DTo2D(D_, n);
    }

    public static void printDataToFile(String fileName, int iter, double[][] X_G, double X_G_norm) {
        double[][] absX_G = matrixAbs(X_G);
        try {
            FileWriter writer = new FileWriter(new File(fileName));
            writer.write(iter + "\n");
            writer.write(X_G_norm + "\n");
            for (int i = 0; i < X_G.length; i++) {
                for (int j = 0; j < X_G[0].length; j++) {
                    writer.write(absX_G[i][j] + " ");
                }
                writer.write("\n");
            }
            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
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

    public static double[][] matrixSub(double[][] a, double[][] b) {
        if (a.length != b.length) {
            System.out.println("Arrays a(" + a.length + ") and b(" + b.length + ") need to have the same length.");
            System.exit(-1);
        }

        double[][] t = new double[a.length][a[0].length];
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                t[i][j] = a[i][j] - b[i][j];
            }
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
                System.out.printf("%8.4f ", a[i][j]);
            }
            System.out.println();
        }
    }

    public static void printMatrixToFile(double[][] a, String fileName) {
        try {
            FileWriter writer = new FileWriter(new File(fileName));
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
            FileWriter writer = new FileWriter(new File(fileName));
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

    public static void printVector(double[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%8.4f ", a[i]);
        }
        System.out.println();
    }
}
