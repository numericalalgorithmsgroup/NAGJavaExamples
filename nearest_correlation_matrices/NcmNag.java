import java.lang.Math;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import com.nag.routines.G02.G02AA;

public class NcmNag {
    public static void main(String[] args) {

        DoubleMatrix P = new DoubleMatrix(
                new double[][] { { 59.875, 42.734, 47.938, 60.359, 54.016, 69.625, 61.500, 62.125 },
                        { 53.188, 49.000, 39.500, Double.NaN, 34.750, Double.NaN, 83.000, 44.500 },
                        { 55.750, 50.000, 38.938, Double.NaN, 30.188, Double.NaN, 70.875, 29.938 },
                        { 65.500, 51.063, 45.563, 69.313, 48.250, 62.375, 85.250, Double.NaN },
                        { 69.938, 47.000, 52.313, 71.016, Double.NaN, 59.359, 61.188, 48.219 },
                        { 61.500, 44.188, 53.438, 57.000, 35.313, 55.813, 51.500, 62.188 },
                        { 59.230, 48.210, 62.190, 61.390, 54.310, 70.170, 61.750, 91.080 },
                        { 61.230, 48.700, 60.300, 68.580, 61.250, 70.340, Double.NaN, Double.NaN },
                        { 52.900, 52.690, 54.230, Double.NaN, 68.170, 70.600, 57.870, 88.640 },
                        { 57.370, 59.040, 59.870, 62.090, 61.620, 66.470, 65.370, 85.840 } });

        DoubleMatrix G = cor_bar(P);
        System.out.println("The approximate correlation matrix");
        printMatrix(G.toArray2());

        System.out.println();

        System.out.print("Sorted eigenvalues of G: ");
        printVector(Eigen.symmetricEigenvalues(G).toArray());

        System.out.println();

        // Call NAG routine G02AA and print the result
        G02AA g02aa = new G02AA();
        int n = G.rows;
        int ldg = n;
        int ldx = n;
        double errtol = 0.0;
        int maxits = 0;
        int maxit = 0;
        double[] x_arr1d = new double[ldx * n];
        int iter = 0;
        int feval = 0;
        double nrmgrd = 0.0;
        int ifail = 0;
        g02aa.eval(G.toArray(), ldg, n, errtol, maxits, maxit, x_arr1d, ldx, iter, feval, nrmgrd, ifail);

        double[][] x_arr2d = convert1DTo2D(x_arr1d, ldx);
        DoubleMatrix X = new DoubleMatrix(x_arr2d);

        System.out.println("Nearest correlation matrix");
        printMatrix(X.toArray2());

        System.out.println();

        System.out.print("Sorted eigenvalues of X: ");
        printVector(Eigen.symmetricEigenvalues(X).toArray());

        System.out.println();

    }

    /**
     * Returns an approximate sample covariance matrix
     * 
     * @param P
     * @return
     */
    public static DoubleMatrix cov_bar(DoubleMatrix P) {

        DoubleMatrix xi, xj;
        boolean[] xib, xjb, notp;

        int n = P.columns;
        int notpFalseCount;

        DoubleMatrix S = DoubleMatrix.zeros(n, n);

        for (int i = 0; i < n; i++) {
            // Take the ith column
            xi = P.getColumn(i);
            for (int j = 0; j < i + 1; j++) {
                // Take the jth column, where j <= i
                xj = P.getColumn(j);

                // Set mask such that all NaNs are true
                xib = getNanMask(xi);
                xjb = getNanMask(xj);

                notp = addBoolArrOr(xib, xjb);

                S.put(i, j, matrixMaskedDot(xi.sub(matrixMaskedMean(xi, notp)), (xj.sub(matrixMaskedMean(xj, notp))),
                        notp));

                // Take the sum over !notp to normalize
                notpFalseCount = 0;
                for (boolean b : notp) {
                    if (!b) {
                        notpFalseCount++;
                    }
                }
                S.put(i, j, 1.0 / (notpFalseCount - 1) * S.get(i, j));
                S.put(j, i, S.get(i,j));
            }
        }

        return S;
    }

    /**
     * Returns an approximate sample correlation matrix
     * 
     * @param P
     */
    public static DoubleMatrix cor_bar(DoubleMatrix P) {
        DoubleMatrix S, D;
        S = cov_bar(P);
        D = DoubleMatrix.diag(getMatrixSQRT(S.diag()).rdiv(1.0));
        return D.mmul(S.mmul(D));
    }

    public static double matrixMaskedDot(DoubleMatrix a, DoubleMatrix b, boolean[] mask) {
        if ((a.length != b.length) || (a.length != mask.length) || (b.length != mask.length)) {
            System.out.println("Arrays a(" + a.length + "), b(" + b.length + ") and mask(" + mask.length
                    + ") need to have the same length.");
            System.exit(-1);
        }

        double r = 0;
        for (int i = 0; i < a.length; i++) {
            if (!mask[i]) {
                r += a.get(i) * b.get(i);
            }
        }
        return r;
    }

    public static DoubleMatrix getMatrixSQRT(DoubleMatrix a) {
        DoubleMatrix t = new DoubleMatrix();
        t.copy(a);
        for (int i = 0; i < t.rows; i++) {
            for (int j = 0; j < t.columns; j++) {
                t.put(i, j, Math.sqrt(t.get(i, j)));
            }
        }
        return t;
    }

    public static double matrixMaskedMean(DoubleMatrix a, boolean[] mask) {
        double sum = 0;
        int n = a.length;

        for (int i = 0; i < a.length; i++) {
            if (mask[i]) {
                n--;
            } else {
                sum += a.get(i);
            }
        }

        return sum / n;
    }

    public static boolean[] getNanMask(DoubleMatrix a) {
        boolean[] t = new boolean[a.length];
        for (int i = 0; i < t.length; i++) {
            if (Double.isNaN(a.get(i))) {
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

    public static double[][] convert1DTo2D(double[] a, int n) {
        double[][] b = new double[n][a.length / n];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                b[i][j] = a[i + j*n];
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
