import com.nag.routines.F06.F06UA;
import com.nag.routines.F06.F06BN;
import com.nag.routines.F06.ZGEMM;
import com.nag.routines.F08.ZGGESX;
import com.nag.routines.M01.M01DA;
import com.nag.routines.M01.M01ED;
import com.nag.routines.X02.X02AJ;
import com.nag.routines.X04.X04DB;
import com.nag.routines.Routine;
import com.nag.types.NAGComplex;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * F08XPJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class F08XPJE{

  public static boolean chkfac = false;
  public static boolean prcond = false;
  public static boolean prmat = false;
  public static int nb = 64;

  /*
   * F08XPJ Example main program
   */
  public static void main(String[] args){
    NAGComplex alph, bet;
    double abnorm, anorm, bnorm, eps, normd, norme, tol;
    int ifail, info = 0, lda, ldb, ldc, ldd, lde, ldvsl, ldvsr, liwork, lwork, n, sdim = 0;
    boolean factor;
    NAGComplex[] a, alpha, b, beta, c, d, e, vsl, vsr, work, dummy;
    double[] rconde, rcondv, rwork;
    int[] idum, iwork;
    boolean[] bwork;
    String[] clabs, rlabs;

    //Placeholders
    idum = new int[1];
    rconde = new double[2];
    rcondv = new double[2];
    dummy = NAGComplex.createArray(1); 

    System.out.println("F08XPJ Example Program Results");
    System.out.println();

    //Supply file path as terminal input
    if(args.length != 1){
      System.out.println("Please specify path to data file");
      System.exit(-1);
    }

    //Tell wrappers what time of complex type is going to be used
    Routine.setComplex(new NAGComplex()); 

    //n declared here for neater code but can be read in from data file
    n = 4;
    lda = n;
    ldb = n;
    ldc = n;
    ldd = n;
    lde = n;
    ldvsl = n;
    ldvsr = n;

    //Allocate
    a = NAGComplex.createArray(lda * n);
    alpha = NAGComplex.createArray(n);
    b = NAGComplex.createArray(ldb * n);
    beta = NAGComplex.createArray(n);
    c = NAGComplex.createArray(ldc * n);
    d = NAGComplex.createArray(ldd * n);
    e = NAGComplex.createArray(lde * n);
    vsl = NAGComplex.createArray(ldvsl * n);
    vsr = NAGComplex.createArray(ldvsr * n);
    rwork = new double[8 * n];
    bwork = new boolean[n];
    clabs = new String[1];
    clabs[0] = " ";
    rlabs = new String[1];
    rlabs[0] = " ";

    //Use routine workspace query to get optimal workspace
    lwork = -1;
    liwork = -1;
    //The NAG name equivalent of zggesx is f08xpf
    selctg selctg1 = new selctg();
    ZGGESX zggesx = new ZGGESX("Vectors (left)", "Vectors (right)", "Sort", selctg1, "Both reciprocal condition numbers",
                               n, a, lda, b, ldb, sdim, alpha, beta, vsl, ldvsl, vsr, ldvsr, rconde, rcondv, dummy, lwork,
                               rwork, idum, liwork, bwork, info);
    zggesx.eval();

    //Make sure that there is a enough workspace for block size nb
    lwork = Math.max((n * nb) + (n * n/2), (int) Math.rint(dummy[0].getRe()));
    liwork = Math.max(n + 2, idum[0]);
    work = NAGComplex.createArray(lwork);
    iwork = new int[liwork];

    //Read in matrices A and B
    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); // skip header
      line = reader.readLine(); //skip n (set manually for neater code)
      String[] sVal;

      for(int i = 0; i < 2; i++){
        for(int j = 0; j < n; j++){
          line = reader.readLine();
          sVal = line.split("\\)");
          for(int k = 0; k < n; k++){
            if(i == 0){
              a[(k * n) + j] = parseComplex(sVal[k]);
            }
            else{
              b[(k * n) + j] = parseComplex(sVal[k]);
            }
          }
        }
      }

    }
    catch(FileNotFoundException err){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException err){
      System.err.println("***FATAL: Can't read " + filename + "\n" + err.getMessage());
    }

    if(chkfac){
      for(int i = 0; i < a.length; i++){
        //Copy A and B into D and E respectively
        d[i] = a[i];
        e[i] = b[i];
      }
    }

    //Find the Forbenius norms of A and B
    //The NAG name equivalent of the LAPACK auxiliary zlange is f06uaf
    F06UA f06ua = new F06UA();
    anorm = f06ua.eval("Frobenius", n, n, a, lda, rwork);
    bnorm = f06ua.eval("Frobenius", n, n, b, ldb, rwork);

    if(prmat){
      //Print matrices A and B
      //ifail : behaviour on error exit
      //        =0 for hard exit, =1 for quiet-soft, =-1 for noisy-soft
      ifail = 0;
      X04DB x04db = new X04DB("General", " ", n, n, a, lda, "Bracketed", "F8.4", "Matrix A", "Integer",
                              rlabs, "Integer", clabs, 80, 0, ifail);
      x04db.eval();
      System.out.println();

      ifail = 0;
      x04db = new X04DB("General", " ", n, n, b, ldb, "Bracketed", "F8.4", "Matrix B", "Integer",
                              rlabs, "Integer", clabs, 80, 0, ifail);
      x04db.eval();
      System.out.println();
    }

    factor = true;
    //Find the generalized Schur form
    //The NAG name equivalent of zggesx is f08xpf
    zggesx = new ZGGESX("Vectors (left)", "Vectors (right)", "Sort", selctg1, "Both reciprocal condition numbers",
                        n, a, lda, b, ldb, sdim, alpha, beta, vsl, ldvsl, vsr, ldvsr, rconde, rcondv, work, lwork,
                        rwork, iwork, liwork, bwork, info);
    zggesx.eval();

    //update
    sdim = zggesx.getSDIM();

    if(info != 0 && info != (n + 2)){
      System.out.printf("Failure in ZGGESX. INFO = %d\n", info);
      factor = false;
    }
    else if(chkfac){
      //Compute A - Q*S*Z^H from the factorization of (A,B) and store in matrix D
      //The NAG name equivalent of zgemm is f06zaf
      alph = new NAGComplex(1, 0);
      bet = new NAGComplex(0, 0);
      ZGEMM zgemm = new ZGEMM("N", "N", n, n, n, alph, vsl, ldvsl, a, lda, bet, c, ldc);
      zgemm.eval();
      
      alph = new NAGComplex(-1, 0);
      bet = new NAGComplex(1, 0);
      zgemm = new ZGEMM("N", "C", n, n, n, alph, c, ldc, vsr, ldvsr, bet, d, ldd);
      zgemm.eval();

      //Compute B - Q*T*Z^H from the factorization of (A,B) and store in matrix E
      alph = new NAGComplex(1, 0);
      bet = new NAGComplex(0, 0);
      zgemm = new ZGEMM("N", "N", n, n, n, alph, vsl, ldvsl, b, ldb, bet, c, ldc);
      zgemm.eval();
      
      alph = new NAGComplex(-1, 0);
      bet = new NAGComplex(1, 0);
      zgemm = new ZGEMM("N", "C", n, n, n, alph, c, ldc, vsr, ldvsr, bet, e, lde);
      zgemm.eval();

      //Find norms of matrices D and E and warn if either is too large
      f06ua = new F06UA("0", ldd, n, d, ldd, rwork);
      normd = f06ua.eval();
      X02AJ x02aj = new X02AJ();
      if(normd > Math.pow(x02aj.eval(), 0.75)){
        System.out.println("Norm of A-(Q*S*Z^T) is much greater than 0.");
        factor = false;
        System.out.println("Schur factorization has failed");
      }
      f06ua = new F06UA("0", lde, n, e, lde, rwork);
      norme = f06ua.eval();
      if(norme > Math.pow(x02aj.eval(), 0.75)){
        System.out.println("Norm of B-(Q*T*Z^T is much greater than 0.");
        factor = false;
      }
    }

    if(factor){
      //Print eigenvalue details
      System.out.printf("Number of eigenvalues for which SELCTG is true = %d\n", sdim);
      System.out.println("(dimension of deflating subspaces)");
      
      System.out.println();
      //Print selected (finite) generalized eigenvalues
      System.out.println("Selected generalized eigenvalues");

      //Store absolute values of eigenvalues for ranking
      for(int i = 0; i < n; i++){
        work[i] = alpha[i].divide(beta[i]);
        rwork[i] = NAGComplex.abs(work[i]);
      }

      //Rank eigenvalues
      ifail = 0;
      M01DA m01da = new M01DA(rwork, 1, sdim, "Descending", iwork, ifail);
      m01da.eval();

      //Sort eigevalues in work
      M01ED m01ed = new M01ED(work, 1, sdim, iwork, ifail);
      m01ed.eval();
      for(int i = 0; i < sdim; i++){
        System.out.printf(" %d\t(%.2f, %.2f)\n", i + 1, work[i].getRe(), work[i].getIm());
      }

      if(info == n + 2){
        System.out.println("*** note that rounding errors mean that leading eigenvalues in");
        System.out.println("the generalized Schur form no longer satisfy SELCTG = TRUE");
        System.out.println();
      }

      if(prcond){
        //Compute the machine precision and sqrt(anorm^2 + bnorm^2)
        X02AJ x02aj = new X02AJ();
        eps = x02aj.eval();
        F06BN f06bn = new F06BN(anorm, bnorm);
        abnorm = f06bn.eval();
        tol = eps * abnorm;

        //Print out the reciprocal condition numbers and error bound for selected eigenvalues
        System.out.println("Reciprocal condition numbers for the average of the selected");
        System.out.println("eigenvalues and their asymptotic error bound");
        System.out.printf("rcond-left = %.1e, rcond-right = %.1e, error = %.1e\n", rcondv[0], rcondv[1], tol/rcondv[1]);
      }
    }
    else{
      System.out.println("Schur factorization has failed");
    }
    
  }

  /**
   * Converts String read in from data file to NAGComplex value.
   * @param s
   *      Complex number string
   * @return new NAGComplex representing s
   */
  public static NAGComplex parseComplex(String s){
    s = s.trim();
    double re = Double.parseDouble(s.substring(1, 7));
    double im = Double.parseDouble(s.substring(8));
    return new NAGComplex(re, im);
  }

  /**
   * Class representing function selctg implementing ZGGESX_SELCTG to pass to ZGGESX.
   */ 
  public static class selctg extends ZGGESX.Abstract_ZGGESX_SELCTG{
    public boolean eval(){
      return (NAGComplex.abs((NAGComplex)this.A) < (6 * NAGComplex.abs((NAGComplex)this.B)));
    }
  }
      
}
