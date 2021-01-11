import com.nag.routines.F07.DGESVX;
import com.nag.routines.X04.X04CA;

/**
 * F07ABJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class F07ABJE{

  /**
   * F07ABJ example main program
   */
  public static void main(String[] args){
    double rcond = 0;
    int ifail, info = 0, lda, ldaf, ldb, ldx, n, nrhs; //placeholders
    String equed;
    double[] a, af, b, berr, c, ferr, r, work, x;
    int[] ipiv, iwork;

    System.out.println("F07ABJ Example Program Results");
    System.out.println();

    //Problem size
    n = 4;
    nrhs = 2;
    lda = n;
    ldaf = n;
    ldb = n;
    ldx = n;

    //Allocate
    a = new double[lda * n];
    af = new double[ldaf * n];
    b = new double[ldb * nrhs];
    berr = new double[nrhs];
    c = new double[n];
    ferr = new double[nrhs];
    r = new double[n];
    work = new double[4 * n];
    x = new double[ldx * nrhs];
    ipiv = new int[n];
    iwork = new int[n];
    equed = " ";

    //Matrices A and B
    a[0] = 1.8;
    a[1] = 525;
    a[2] = 1.58;
    a[3] = -1.11;
    a[4] = 2.88;
    a[5] = -295;
    a[6] = -2.69;
    a[7] = -0.66;
    a[8] = 2.05;
    a[9] = -95;
    a[10] = -2.9;
    a[11] = -0.59;
    a[12] = -0.89;
    a[13] = -380;
    a[14] = -1.04;
    a[15] = 0.8;
    b[0] = 9.52;
    b[1] = 2435;
    b[2] = 0.77;
    b[3] = -6.22;
    b[4] = 18.47;
    b[5] = 225;
    b[6] = -13.28;
    b[7] = -6.21;

    //Solve the equations AX = B for X
    //The NAG name equivalent of dgesvx is f07abf
    DGESVX dgesvx = new DGESVX("Equilibration", "No Transpose", n, nrhs, a, lda, af, ldaf, ipiv, equed, r,
                               c, b, ldb, x, ldx, rcond, ferr, berr, work, iwork, info);
    dgesvx.eval();

    //Update
    info = dgesvx.getINFO();
    equed = dgesvx.getEQUED();
    rcond = dgesvx.getRCOND();

    if(info == 0 || info == n + 1){
      //Print solution, error bounds, condition number, the form of equilibration and the pivot growth factor

      //ifail: behaviour on error exit
      //       =0 for hard exit, =1 for quiet-soft, =-1 for noisy-soft
      ifail = 0;
      X04CA x04ca = new X04CA("General", " ", n, nrhs, x, ldx, "Solution(s)", ifail);
      x04ca.eval();

      System.out.println();
      System.out.println("Backward errors (machine-dependent)");
      for(int i = 0; i < nrhs; i++){
        System.out.printf("  %.1e  ", berr[i]);
      }
      System.out.println();
      System.out.println();
      System.out.println("Estimated forward error bounds (machine-dependent)");
      for(int i = 0; i < nrhs; i++){
        System.out.printf("  %.1e  ", ferr[i]);
      }
      System.out.println();
      System.out.println();
      if(equed.equals("N")){
        System.out.println("A has not been equilibrated");
      }
      else if(equed.equals("R")){
        System.out.println("A has been row scaled as diag(R)*A");
      }
      else if(equed.equals("C")){
        System.out.println("A has been column scaled as A*diag(C)");
      }
      else if(equed.equals("B")){
        System.out.println("A has been row and column scaled as diag(R)*A*diag(C)");
      }
      System.out.println();
      System.out.println("Reciprocal condition number estimate of scaled matrix");
      System.out.printf("  %.1e\n", rcond);
      System.out.println();
      System.out.println("Estimate of reciprocal pivot growth factor");
      System.out.printf("  %.1e\n", work[0]);
      System.out.println();

      if(info == n + 1){
        System.out.println();
        System.out.println("The matrix A is singular to working precision");
      }
    }
    else{
      System.out.println("The (" + info + ", " + info + ")" + " element of the factor U is zero");
    }
  }
}
