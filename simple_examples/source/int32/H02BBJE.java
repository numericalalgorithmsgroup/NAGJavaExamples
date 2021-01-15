import com.nag.routines.H.H02BB;

/**
 * H02BBJ example program text
 * @author willa
 * @since 27.1.0.0
 */
public class H02BBJE{

  /**
   * H02BBJ example main program
   */
  public static void main(String[] args){
    double bigbnd, objmip = 0, tolfes, toliv;
    int ifail, intfst, itmax, lda, liwork, lrwork, m, maxdpt, maxnod, msglvl, n;
    double[] a, bl, bu, cvec, rwork, x;
    int[] intvar, iwork;

    System.out.println("H02BBJ Example Program Results");
    System.out.println();

    //Data (can be read in from data file)
    n = 2;
    m = 3;
    lda = m;
    
    a = new double[lda * n];
    bl = new double[m + n];
    bu = new double[m + n];
    cvec = new double[n];
    x = new double[n];
    intvar = new int[n];

    itmax = 0;
    msglvl = 10;
    maxnod = 0;
    intfst = 0;
    maxdpt = 4;
    tolfes = 0;
    toliv = 0;

    cvec[0] = -3;
    cvec[1] = -4;

    //A = ( 2,  5)
    //    ( 2, -2)
    //    ( 3,  2)
    a[0] = 2;
    a[1] = 2;
    a[2] = 3;
    a[3] = 5;
    a[4] = -2;
    a[5] = 2;

    bigbnd = 1E+20;

    bl[0] = 0;
    bl[1] = 0;
    bl[2] = -1E+20;
    bl[3] = -1E+20;
    bl[4] = 5;

    bu[0] = 1E+20;
    bu[1] = 1E+20;
    bu[2] = 15;
    bu[3] = 5;
    bu[4] = 1E+20;

    intvar[0] = 1;
    intvar[1] = 1;

    x[0] = 1;
    x[1] = 1;

    liwork = ((25 + n + m) * maxdpt) + (5 * n) + m + 4;
    //Math.pow() needs to be casted to int in java
    lrwork = (maxdpt * (n + 1)) + (2 * (int)Math.pow(Math.min(n, m + 1), 2)) + (14 * n) + (12 * m);
    iwork = new int[liwork];
    rwork = new double[lrwork];

    ifail = 0;

    H02BB h02bb = new H02BB(itmax, msglvl, n, m, a, lda, bl, bu, intvar, cvec, maxnod, intfst, maxdpt, toliv,
                            tolfes, bigbnd, x, objmip, iwork, liwork, rwork, lrwork, ifail);
    h02bb.eval();
  }
}




    
