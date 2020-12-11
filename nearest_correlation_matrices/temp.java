import org.jblas.DoubleMatrix;

public class temp {
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

        //P.print();

        DoubleMatrix x = new DoubleMatrix(new double[][]{{1d, 2d, 3d}, {4d, 5d, 6d}, {7d, 8d, 9d}});

        //System.out.println(x.toString().replace(';', '\n'));
        x.diag().div(1).print();
        x.diag().rdiv(1).print();
    }
}
