import com.nag.routines.F07.DGETRF;
import com.nag.routines.X04.X04CA;

/**
 * F07ADJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class F07ADJE{

  /**
   * F07ADJ Example main program
   */
  public static void main(String[] args){
    int ifail, info = 0, lda, m, n;
    double[] a;
    int[] ipiv;

    System.out.println("F07ADJ Example Program Results");
    System.out.println();

    //Problem size (can be read from data file)
    m = 4;
    n = 4;
    lda = m;

    //Data
    ipiv = new int[n];
    a = new double[m * n];
    a[0] = 1.8;
    a[1] = 5.25;
    a[2] = 1.58;
    a[3] = -1.11;
    a[4] = 2.88;
    a[5] = -2.95;
    a[6] = -2.69;
    a[7] = -0.66;
    a[8] = 2.05;
    a[9] = -0.95;
    a[10] = -2.9;
    a[11] = -0.59;
    a[12] = -0.89;
    a[13] = -3.8;
    a[14] = -1.04;
    a[15] = 0.8;

    //Factorize A
    //The NAG name equivalent of dgetrf is F07ADF
    DGETRF dgetrf = new DGETRF(m, n, a, lda, ipiv, info);
    dgetrf.eval();

    //Update
    info = dgetrf.getINFO();

    //ifail: behaviour on error exit
    //       =0 for hard exit, =1 for quiet-soft, =-1 for noisy-soft 
    ifail = 0;
    X04CA x04ca = new X04CA("General", " ", m, n, a, lda, "Details of factorization", ifail);
    x04ca.eval();

    //Print pivot indices
    System.out.println("IPIV");
    for(int i = 0; i < Math.min(m, n); i++){
      System.out.printf("\t%d", ipiv[i]);
    }
    System.out.println();

    if(info != 0){
      System.out.println("The factor U is singular");
    }
  }
}
