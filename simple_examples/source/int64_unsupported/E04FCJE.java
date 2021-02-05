import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.E04.E04FC;
import com.nag.routines.F06.DDOT;
import com.nag.routines.F06.DGEMV;
import com.nag.routines.Routine;
import com.nag.routines.X02.X02AJ;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ludovic
 */
public class E04FCJE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        BufferedReader dataIn = null;
        try {
            Routine.init();
            E04FC e04fc = new E04FC();
            System.out.println(" E04FCJ Example Program Results");
            dataIn = new BufferedReader(new FileReader(args[0]));
            //skip header
            dataIn.readLine();

            long inc1 = 1, liw = 1, m, ldfjac = m = 15, n, ldv = n = 3, nt = 3,
                    lw = 6 * n + m * n + 2 * m + n * (n - 1) / 2;
            String trans = "T";

            double eta, fsumsq, stepmx, xtol;
            eta = fsumsq = stepmx = xtol = Double.NaN;
            long ifail, iprint, maxcal, nf, niter;
            ifail = iprint = maxcal = nf = niter = 0;
            double[] fjac = new double[(int)(m * n)],
                    fvec = new double[(int)m],
                    g = new double[(int)n],
                    s = new double[(int)n],
                    v = new double[(int)(ldv * n)],
                    w = new double[(int)lw],
                    x = new double[(int)n],
                    y = new double[(int)m],
                    t = new double[(int)(m * nt)];


            long[] iw = new long[(int)liw];

            for (int i = 0; i < m; ++i) {
                String[] line = dataIn.readLine().trim().split("\\s+");
                if (line.length != (int)nt + 1) {
                    System.err.println("Error in data file - only " + line.length + " records at line " + (i + 2) + " while expecting " + (nt + 1) + " elements");
                    System.exit(1);
                }
                y[i] = Double.parseDouble(line[0].replaceAll("D", "E")); // java doesn't know the D format
                for (int j = 1; j <= nt; ++j) {
                    t[i + (int)((j - 1) * m)] = Double.parseDouble(line[j].replaceAll("D", "E"));
                }
            }
            // Set IPRINT to 1 to obtain output from LSQMON at each iteration

            iprint = -1;

            maxcal = 400 * n;
            eta = 0.5;
            xtol = 10.0 * Math.sqrt((new X02AJ()).eval());

//      We estimate that the minimum will be within 10 units of the starting point

            stepmx = 10.0;

//   Set up the starting point

            x[0] = 0.5;
            x[1] = 1.0;
            x[2] = 1.5;

            ifail = -1;

            LSQFUN lsqfun = new LSQFUN();
            lsqfun.t = t;
            lsqfun.y = y;
            LSQMON lsqmon = new LSQMON();

            e04fc.eval(m, n, lsqfun, lsqmon, iprint, maxcal, eta, xtol, stepmx, x, fsumsq,
                    fvec, fjac, ldfjac, s, v, ldv, niter, nf, iw, liw, w, lw, ifail);

            ifail = e04fc.getIFAIL();


            switch ((int)ifail) {
                case (1):
                    System.err.println("Unexpected ifail = " + ifail);
                    break;
                default:
                    System.out.println();
                    System.out.printf(" On exit, the sum of squares is %12.4f\n", e04fc.getFSUMSQ());
                    System.out.printf(" at the point %12.4f %12.4f %12.4f\n", x[0], x[1], x[2]);
                    lsqgrd(m, n, fvec, fjac, ldfjac, g);
                    System.out.print(" The estimated gradient is ");
                    for (int i = 0; i < n; ++i) {
                        System.out.printf(" %12.3e\t", g[i]);
                    }
                    System.out.println();
                    System.out.println("                     (machine dependent)");
                    System.out.println(" and the residuals are");
                    for (int i = 0; i < m; ++i) {
                        System.out.printf(" %9.1e\n", fvec[i]);
                    }
            }



        } catch (Exception ex) {
            Logger.getLogger(E04FCJE.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dataIn.close();
            } catch (IOException ex) {
                Logger.getLogger(E04FCJE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

    public static void lsqgrd(long m, long n, double[] fvec, double[] fjac, long ldfjac, double[] g) {
	DGEMV dgemv = new DGEMV("T", m, n, 1.0, fjac, ldfjac, fvec, 1, 0.0, g, 1);
	dgemv.eval();
	for (int i = 0; i < g.length; ++i) {
	    g[i] = 2.0 * g[i];
	}
    }

    public static class LSQFUN implements E04FC.E04FC_LSQFUN {

        public double[] t, y;
        long IFLAG, M, N, LW, LIW;
        double[] XC, FVEC, W;
        long[] IW;

        // @Override
        public void setIFLAG(long IFLAG) {
            this.IFLAG = IFLAG;
        }

        // @Override
        public long getIFLAG() {
            return IFLAG;
        }

        // @Override
        public void setM(long M) {
            this.M = M;
        }

        // @Override
        public long getM() {
            return M;
        }

        // @Override
        public void setN(long N) {
            this.N = N;
        }

        // @Override
        public long getN() {
            return N;
        }

        // @Override
        public void setXC(double[] XC) {
            this.XC = XC;
        }

        // @Override
        public double[] getXC() {
            return XC;
        }

        // @Override
        public void setFVEC(double[] FVEC) {
            this.FVEC = FVEC;
        }

        // @Override
        public double[] getFVEC() {
            return FVEC;
        }

        // @Override
        public void setIW(long[] IW) {
            this.IW = IW;
        }

        // @Override
        public long[] getIW() {
            return IW;
        }

        // @Override
        public void setLIW(long LIW) {
            this.LIW = LIW;
        }

        // @Override
        public long getLIW() {
            return LIW;
        }

        // @Override
        public void setW(double[] W) {
            this.W = W;
        }

        // @Override
        public double[] getW() {
            return W;
        }

        // @Override
        public void setLW(long LW) {
            this.LW = LW;
        }

        // @Override
        public long getLW() {
            return LW;
        }

        // @Override
        public void eval(long IFLAG, long M, long N, double[] XC, double[] FVEC, long[] IW, long LIW, double[] W, long LW) {
            for (int i = 0; i < M; ++i) {
                FVEC[i] = XC[0] + t[i] / (XC[1] * t[(int)(i + M)] + XC[2] * t[i + 2 * (int)M]) - y[i];
            }
        }
    }

    public static class LSQMON implements E04FC.E04FC_LSQMON {

        long M, N, LDFJAC, NITER, NF, IGRADE, LIW, LW;
        long[] IW;
        double[] XC, FVEC, FJAC, S, W;

        // @Override
        public void setM(long M) {
            this.M = M;
        }

        // @Override
        public long getM() {
            return M;
        }

        // @Override
        public void setN(long N) {
            this.N = N;
        }

        // @Override
        public long getN() {
            return N;
        }

        // @Override
        public void setXC(double[] XC) {
            this.XC = XC;
        }

        // @Override
        public double[] getXC() {
            return XC;
        }

        // @Override
        public void setFVEC(double[] FVEC) {
            this.FVEC = FVEC;
        }

        // @Override
        public double[] getFVEC() {
            return FVEC;
        }

        // @Override
        public void setFJAC(double[] FJAC) {
            this.FJAC = FJAC;
        }

        // @Override
        public double[] getFJAC() {
            return FJAC;
        }

        // @Override
        public void setLDFJAC(long LDFJAC) {
            this.LDFJAC = LDFJAC;
        }

        // @Override
        public long getLDFJAC() {
            return LDFJAC;
        }

        // @Override
        public void setS(double[] S) {
            this.S = S;
        }

        // @Override
        public double[] getS() {
            return S;
        }

        // @Override
        public void setIGRADE(long IGRADE) {
            this.IGRADE = IGRADE;
        }

        // @Override
        public long getIGRADE() {
            return IGRADE;
        }

        // @Override
        public void setNITER(long NITER) {
            this.NITER = NITER;
        }

        // @Override
        public long getNITER() {
            return NITER;
        }

        // @Override
        public void setNF(long NF) {
            this.NF = NF;
        }

        // @Override
        public long getNF() {
            return NF;
        }

        // @Override
        public void setIW(long[] IW) {
            this.IW = IW;
        }

        // @Override
        public long[] getIW() {
            return IW;
        }

        // @Override
        public void setLIW(long LIW) {
            this.LIW = LIW;
        }

        // @Override
        public long getLIW() {
            return LIW;
        }

        // @Override
        public void setW(double[] W) {
            this.W = W;
        }

        // @Override
        public double[] getW() {
            return W;
        }

        // @Override
        public void setLW(long LW) {
            this.LW = LW;
        }

        // @Override
        public long getLW() {
            return LW;
        }

        // @Override
        public void eval(long M, long N, double[] XC, double[] FVEC, double[] FJAC, long LDFJAC, double[] S, long IGRADE, long NITER, long NF, long[] IW, long LIW, double[] W, long LW) {
            long ndec = 3;
            double fsumsq, gtg;
            double[] g = new double[(int)ndec];
			DDOT ddot = new DDOT(M, FVEC, 1, FVEC, 1);
			fsumsq = ddot.eval();

			lsqgrd(M, N, FVEC, FJAC, LDFJAC, g);

			gtg = ddot.eval(N, g, 1, g, 1);
			/*
				99998     FORMAT (1X,1P,E13.5,10X,1P,E9.1,10X,1P,E9.1)

			*/
			System.out.println();
			System.out.println("  Itn      F evals        SUMSQ             GTG        Grade");
			System.out.printf(" %4d      %5d      %13.5e      %9.1e      %3d\n", NITER, NF, fsumsq, gtg, IGRADE);
			System.out.println();
			System.out.println("       X                    G           Singular values");
			for (int j = 0; j < N; ++j) {
				System.out.printf(" %13.5e          %9.1e          %9.1e\n",XC[j], g[j], S[j]);
			}
        }
    }
}
