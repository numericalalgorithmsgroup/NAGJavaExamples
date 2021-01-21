import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.Routine;
import com.nag.routines.E04.E04MT;
import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RF;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04RJ;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04ZM;


public class E04MTJE {

    /**
     * @param args
     * @throws NAGBadIntegerException
     * @throws IOException
     */
    public static void main(String[] args) throws NAGBadIntegerException, IOException {
        Routine.init();
        System.out.println(" E04MTJ Example Program Results");
        int m = 7;
        int n = 7;
        int nnza = 41;
        int nnzc = 7;
        int nnzu = 2*n + 2*m;

        int[] cindex = {1,2,3,4,5,6,7};
        double[] c = {-0.02,-0.20,-0.20,-0.20,-0.20, 0.04, 0.04};
        int[] irowa = {1,1,1,1,1,1,1,2,2,2,2,2,2,2,3,3,3,3,3,3,
                       4,4,4,4,4,5,5,5,6,6,6,6,6,6,7,7,7,7,7,7,7};
        int[] icola = {1,2,3,4,5,6,7,1,2,3,4,5,6,7,1,2,3,4,5,6,
                       1,2,3,4,5,1,2,5,1,2,3,4,5,6,1,2,3,4,5,6,7};
        double[] a = {1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00,
                      0.15, 0.04, 0.02, 0.04, 0.02, 0.01, 0.03,
                      0.03, 0.05, 0.08, 0.02, 0.06, 0.01,0.02,
                      0.04, 0.01, 0.02, 0.02,0.02, 0.03, 0.01,0.70,
                      0.75, 0.80, 0.75, 0.80, 0.97,0.02, 0.06, 0.08,
                      0.12, 0.02, 0.01, 0.97};
        double[] bla = {-0.13,-1.0e20,-1.0e20,-1.0e20,-1.0e20,
                        -0.0992,-0.003};
        double[] bua = {-0.13,-0.0049,-0.0064,-0.0037,-0.0012,
                        1.0e20, 0.002};
        double[] xl = {-0.01,-0.1,-0.01,-0.04,-0.1,-0.01,-0.01};
        double[] xu = {0.01,0.15,0.03,0.02,0.05,1.0e20,1.0e20};
        double[] dArrData = new double[nnza+2*m+3*n+nnzc+nnzu];

        long handle = 0;
        int ifail = 0;
        E04RA e04ra = new E04RA(handle,n,ifail);
        e04ra.eval();

        handle = e04ra.getHANDLE();

        int[] icolh = new int[1];
        int[] irowh = new int[1];
        double[] h = new double[1];
        E04RF e04rf = new E04RF(handle,nnzc,cindex,c,0,irowh,icolh,h,ifail);
        e04rf.eval();
        handle = e04ra.getHANDLE();

        E04RH e04rh = new E04RH(handle,n,xl,xu,ifail);
        e04rh.eval();
        handle = e04rh.getHANDLE();

        int idlc = 0;
        E04RJ e04rj = new E04RJ(handle,m,bla,bua,nnza,irowa,icola,a,idlc,ifail);
        e04rj.eval();
        handle = e04rj.getHANDLE();

        E04ZM e04zm = new E04ZM(handle,"LPIPM Monitor Frequency = 1",ifail);
        e04zm.eval();
        e04zm.setOPTSTR("LPIPM Stop Tolerance = 1.0e-10");
        e04zm.eval();
        e04zm.setOPTSTR("Print Solution = YES");
        e04zm.eval();
        e04zm.setOPTSTR("Print Options = NO");
        e04zm.eval();
        e04zm.setOPTSTR("LPIPM Centrality Correctors = -6");
        e04zm.eval();
        handle = e04zm.getHANDLE();

        long cpuser = 2;
        int[] iuser = {1};
        double[] ruser = new double[1];
        ifail = -1;
        MONIT monit = new MONIT();
        double[] x = new double[n], u = new double[nnzu];
        double[] rinfo = new double[100], stats = new double[100];
        System.out.println();
        System.out.println("++++++++++ Use the Primal-Dual algorithm ++++++++++");
        E04MT e04mt = new E04MT(handle,n,x,nnzu,u,rinfo,stats,
                                monit,iuser,ruser,cpuser,ifail);
        e04mt.eval();
        System.out.println();
        System.out.println("++++++++++ Use the Self-Dual algorithm ++++++++++");
        e04zm.setOPTSTR("LPIPM Algorithm = Self-Dual");
        e04zm.eval();
        e04zm.setOPTSTR("LPIPM Stop Tolerance 2 = 1.0e-11");
        e04zm.eval();
        iuser[0] = 2;
        e04mt.setIFAIL(-1);
        e04mt.setHANDLE(e04zm.getHANDLE());
        e04mt.setIUSER(iuser);
        e04mt.eval();

    }
    public static class MONIT extends E04MT.Abstract_E04MT_MONIT{

        public void eval(){
            double tol = 1.2e-8;
            //this.setINFORM(-2);
            if(IUSER[0]==1){
                if(RINFO[4]<tol && RINFO[5]<tol && RINFO[6]<tol){
                    System.out.println("Iteration " + Integer.toString((int)STATS[0]));
                    System.out.printf("     monit() reports good approximate solution (tol = %5.2E):\n", tol);
                }
            }else{
                if(RINFO[14]<tol && RINFO[15]<tol && RINFO[16]<tol && RINFO[17]<tol){
                    System.out.println("Iteration "+
                                       Integer.toString((int)STATS[0]));
                    System.out.printf("     monit() reports good approximate solution (tol = %5.2E):\n", tol);
                }
            }
        }

    }
}
