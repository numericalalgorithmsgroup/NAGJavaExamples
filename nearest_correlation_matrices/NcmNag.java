import java.util.Arrays;
import java.lang.Math;

public class NcmNag {
    public static void main(String[] args) {

        double P[][] = { { 59.875, 42.734, 47.938, 60.359, 54.016, 69.625, 61.500, 62.125 },
                { 53.188, 49.000, 39.500, Double.NaN, 34.750, Double.NaN, 83.000, 44.500 },
                { 55.750, 50.000, 38.938, Double.NaN, 30.188, Double.NaN, 70.875, 29.938 },
                { 65.500, 51.063, 45.563, 69.313, 48.250, 62.375, 85.250, Double.NaN },
                { 69.938, 47.000, 52.313, 71.016, Double.NaN, 59.359, 61.188, 48.219 },
                { 61.500, 44.188, 53.438, 57.000, 35.313, 55.813, 51.500, 62.188 },
                { 59.230, 48.210, 62.190, 61.390, 54.310, 70.170, 61.750, 91.080 },
                { 61.230, 48.700, 60.300, 68.580, 61.250, 70.340, Double.NaN, Double.NaN },
                { 52.900, 52.690, 54.230, Double.NaN, 68.170, 70.600, 57.870, 88.640 },
                { 57.370, 59.040, 59.870, 62.090, 61.620, 66.470, 65.370, 85.840 } };

        int m = P.length;
        int n = P[0].length;

        double[][] G = cor_bar(P);
        printMatrix(G);

        
    }

    /**
     * Returns an approximate sample covariance matrix
     * 
     * @param P
     * @return
     */
    public static double[][] cov_bar(double[][] P) {

        double[] xi, xj, xim, xjm;
        boolean[] xib, xjb, notp;

        int n = P[0].length;
        int notpFalseCount;

        // Initialize an n-by-n zero matrix
        double[][] S = new double[n][n];
        for (double[] row : S) {
            Arrays.fill(row, 0);
        }

        for (int i = 0; i < n; i++) {
            // Take the ith column
            xi = getColumn(P, i);

            for (int j = 0; j < i + 1; j++) {
                // Take the jth column, where j <= i
                xj = getColumn(P, j);

                // Set mask such that all NaNs are true
                xib = getNanMask(xi);
                xjb = getNanMask(xj);

                notp = addBoolArrOr(xib, xjb);

                xim = subtractMean(xi, notp);
                xjm = subtractMean(xj, notp);

                S[i][j] = getVectorDotProduct(xim, xjm, notp);

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
        D = getMatrixFromDiag(vectorDivWithScalar(1.0, sqrtVector(getDiag(S))));
        return multiplyMatrices(D, multiplyMatrices(S, D));
    }

    public static double[] getColumn(double[][] array, int index) {
        double[] column = new double[array.length];
        for (int i = 0; i < column.length; i++) {
            column[i] = array[i][index];
        }
        return column;
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

    public static double[] subtractMean(double[] a, boolean[] b) {
        if (a.length != b.length) {
            System.out.println("Arrays a(" + a.length + ") and b(" + b.length + ") need to have the same length.");
            System.exit(-1);
        }

        double mean = getMean(a, b);
        double[] t = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            t[i] = a[i] - mean;
        }
        return t;
    }

    public static double getMean(double[] a, boolean[] b) {
        double sum = 0;
        int n = a.length;
        for (int i = 0; i < a.length; i++) {
            if (b[i]) {
                n--;
            } else {
                sum += a[i];
            }
        }
        return sum / n;
    }

    public static double getVectorDotProduct(double[] a, double[] b, boolean[] mask) {
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

    public static double[] getDiag(double[][] a) {
        if (a.length != a[0].length) {
            System.out.println("Array a must be a matrix and have the same number of rows(" + a.length
                    + ") and columns(" + a[0].length + ").");
            System.exit(-1);
        }

        double[] t = new double[a.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = a[i][i];
        }
        return t;
    }

    public static double[] sqrtVector(double[] a) {
        double[] t = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            t[i] = Math.sqrt(a[i]);
        }
        return t;
    }

    public static double[] vectorDivWithScalar(double s, double[] vec) {
        double[] t = new double[vec.length];
        for (int i = 0; i < vec.length; i++) {
            t[i] = s / vec[i];
        }
        return t;
    }

    public static double[][] getMatrixFromDiag(double[] diag) {
        double[][] t = new double[diag.length][diag.length];
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                if (i == j) {
                    t[i][j] = diag[i];
                } else {
                    t[i][j] = 0;
                }
            }
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

    public static double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }
        return cell;
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
