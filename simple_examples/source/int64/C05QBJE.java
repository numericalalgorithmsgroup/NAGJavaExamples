import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.C05.C05QB;
import com.nag.routines.F06.DNRM2;
import com.nag.routines.Routine;
import com.nag.routines.X02.X02AJ;

/**
 *
 * @author ludovic
 */
public class C05QBJE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  throws NAGBadIntegerException {
        try {
            Routine.init();
            
			C05QB c05qb = new C05QB();
			
            System.out.println(" C05QBJ Example Program Results");

            int n = 9, ifail = -1;

            double xtol, fnorm;

            long[] IUSER = new long[1];
            double[] fvec = new double[n];
            double[] x = new double[n];
            double[] RUSER = new double[1];


            FCN fcn = new FCN();

            for (int i = 0; i < n; ++i) {
                x[i] = -1.0;
            }
			
            xtol = Math.sqrt((new X02AJ()).eval());
			
            c05qb.eval(fcn, n, x, fvec, xtol, IUSER, RUSER, ifail);

            ifail = (int)c05qb.getIFAIL();

            switch (ifail) {
                case (0):
					fnorm = (new DNRM2(n, fvec, 1)).eval();
                    System.out.println();
                    System.out.printf(" Final 2-norm of the residuals = %11.4E\n", fnorm);
                    System.out.println();
                    System.out.println(" Final approximate solution");
                    int count = 0;
                    for (int i = 0; i < n; ++i) {
                        System.out.printf(" %12.4f", x[i]);
                            ++count;
                        if(count == 3){
                            System.out.println();
                            count = 0;
                        }
                    }
                    break;
                case (2):
                case (3):
                case (4):
                    System.out.println("Approximate solution");
                    count = 0;
                    for (int i = 0; i < n; ++i) {
                        System.out.printf(" %12.4f", x[i]);
                        ++count;
                        if(count == 3){
                            System.out.println();
                            count = 0;
                        }
                    }
                    
                    break;
            }

        } catch (NAGBadIntegerException ex) {
            System.err.println("Something went wrong!\n" + ex.getMessage());
        }


    }

    public static class FCN implements C05QB.C05QB_FCN {

        private long N, IFLAG;
        private double[] X, FVEC, RUSER;
        private long[] IUSER;

        //@Override
        public void setN(long N) {
            this.N = N;
        }

        //@Override
        public long getN() {
            return N;
        }

        //@Override
        public void setX(double[] X) {
            this.X = X;
        }

        //@Override
        public double[] getX() {
            return X;
        }

        //@Override
        public void setFVEC(double[] FVEC) {
            this.FVEC = FVEC;
        }

        //@Override
        public double[] getFVEC() {
            return FVEC;
        }

        //@Override
        public void setIUSER(long[] IUSER) {
            this.IUSER = IUSER;
        }

        //@Override
        public long[] getIUSER() {
            return IUSER;
        }

        //@Override
        public void setRUSER(double[] RUSER) {
            this.RUSER = RUSER;
        }

        //@Override
        public double[] getRUSER() {
            return RUSER;
        }

        //@Override
        public void setIFLAG(long IFLAG) {
            this.IFLAG = IFLAG;
        }

        //@Override
        public long getIFLAG() {
            return IFLAG;
        }

        //@Override
        public void eval(long N, double[] X, double[] FVEC, long[] IUSER, double[] RUSER, long IFLAG) {
            /*
             * fvec(1:n) = (3.0_nag_wp-2.0_nag_wp*x(1:n))*x(1:n) + 1.0_nag_wp
             * fvec(2:n) = fvec(2:n) - x(1:(n-1)) 
             * fvec(1:(n-1)) = fvec(1:(n-1)) - 2.0_nag_wp*x(2:n)
             */
            for (int i = 0; i < N; ++i) {
                FVEC[i] = (3.0 - 2.0 * X[i]) * X[i] + 1.0;
                /*if (i >= 1) {
                    FVEC[i] = FVEC[i] - X[i - 1];
                }
                if (i < N - 1) {
                    FVEC[i] = FVEC[i] - 2.0 * X[i + 1];
                }*/
            }
            for(int i=1;i<N;++i){
                FVEC[i] = FVEC[i] - X[i - 1];
            }
            for(int i=0;i<N-1;++i){
                FVEC[i] = FVEC[i] - 2.0 * X[i + 1];
            }
            
        }
    }
}
