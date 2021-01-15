import com.nag.routines.H.H02DA;
import com.nag.routines.H.H02ZK;
import com.nag.routines.H.H02ZL;
import com.nag.routines.X04.X04CA;
import java.util.Arrays;

/**
 * H02DAJ exmaple program text
 * @author willa
 * @since 27.1.0.0
 */
public class H02DAJE{

  /**
   * H02DAJ example main program
   */
  public static void main(String[] args){
    double acc, accqp = 0, objmip = 0;
    int ifail, ivalue = 0, lda, liopts, lopts, maxit, n, nclin, ncnln, optype = 0;
    String cvalue;
    double[] a, ax, bl, bu, c, cjac, d, objgrd, x, opts, ruser;
    int[] iopts, iuser, varcon;

    System.out.println("H02DAJ Example Program Results");
    System.out.println();

    opts = new double[100];
    ruser = new double[1];
    iopts = new int[200];
    iuser = new int[1];

    //Blank 40 character string to represent ```Character (40)                   :: cvalue```
    char[] ch = new char[40];
    Arrays.fill(ch, ' ');
    cvalue = new String(ch);

    n = 8;
    nclin = 5;
    ncnln = 2;
    lda = nclin;

    a = new double[lda * n];
    d = new double[nclin];
    ax = new double[nclin];
    bl = new double[n];
    bu = new double[n];
    varcon = new int[n + nclin + ncnln];
    x = new double[n];
    c = new double[ncnln];
    cjac = new double[ncnln * n];
    objgrd = new double[n];

    //Set variable types: continuous then binary
    varcon[0] = 0;
    varcon[1] = 0;
    varcon[2] = 0;
    varcon[3] = 0;
    varcon[4] = 1;
    varcon[5] = 1;
    varcon[6] = 1;
    varcon[7] = 1;

    //Set continuous variable bounds
    bl[0] = 0;
    bl[1] = 0;
    bl[2] = 0;
    bl[3] = 0;
    bu[0] = 1000;
    bu[1] = 1000;
    bu[2] = 1000;
    bu[3] = 1000;

    //Bounds for binary variables need not be provided
    bl[4] = 0;
    bl[5] = 0;
    bl[6] = 0;
    bl[7] = 0;
    bu[4] = 1;
    bu[5] = 1;
    bu[6] = 1;
    bu[7] = 1;

    //Set linear constraint, equality first
    varcon[n] = 3;
    varcon[n + 1] = 4;
    varcon[n + 2] = 4;
    varcon[n + 3] = 4;
    varcon[n + 4] = 4;

    //Set Ax=d then Ax>=d
    //( 1, 1, 1, 1, 0, 0, 0, 0)
    //(-1, 0, 0, 0, 1, 0, 0, 0)
    //( 0,-1, 0, 0, 0, 1, 0, 0)
    //( 0, 0,-1, 0, 0, 0, 1, 0)
    //( 0, 0, 0,-1, 0, 0, 0, 1)
    a[0] = 1;
    a[1] = -1;
    a[5] = 1;
    a[7] = -1;
    a[10] = 1;
    a[13] = -1;
    a[15] = 1;
    a[19] = -1;
    a[21] = 1;
    a[27] = 1;
    a[33] = 1;
    a[39] = 1;
    d[0] = 1;
    d[1] = 0;
    d[2] = 0;
    d[3] = 0;
    d[4] = 0;

    //Set constraints supplied by CONFUN, equality first
    varcon[n + nclin] = 3;
    varcon[n + nclin + 1] = 4;

    liopts = iopts.length;
    lopts = opts.length;

    //Initialize communication arrays
    ifail = 0;
    H02ZK h02zk = new H02ZK("Initialize = H02DAF", iopts, liopts, opts, lopts, ifail);
    h02zk.eval();
    

    //Optimization parameters
    maxit = 500;
    acc = 0.000001;

    //Initial estimate (binary variables need not be given)
    x[0] = 1;
    x[1] = 1;
    x[2] = 1;
    x[3] = 1;
    x[4] = 0;
    x[5] = 0;
    x[6] = 0;
    x[7] = 0;

    //Portfolio parameters p and rho
    iuser[0] = 3;
    ruser[0] = 10;

    ifail = 0;
    //Create objfun1, confun1 to pass to H02DA representing repsecitve subroutines
    objfun objfun1 = new objfun();
    confun confun1 = new confun();
    H02DA h02da = new H02DA(n, nclin, ncnln, a, lda, d, ax, bl, bu, varcon, x, confun1, c, cjac, objfun1,
                            objgrd, maxit, acc, objmip, iopts, opts, iuser, ruser, ifail);
    h02da.eval();
    
    //Results
    ifail = h02da.getIFAIL();
    if(ifail == 0){
      X04CA x04ca = new X04CA("G", "N", n, 1, x, n, "Final Esimate:", ifail);
      x04ca.eval();

      //Query the accuracy of the mixed integer QP Solver
      ifail = -1;
      H02ZL h02zl = new H02ZL("QP Accuracy", ivalue, accqp, cvalue, optype, iopts, opts, ifail);
      h02zl.eval();

      //Update values to print
      accqp = h02zl.getRVALUE();
      objmip = h02da.getOBJMIP();
      ifail = h02zl.getIFAIL();
      if(ifail == 0){
        System.out.printf("Requested accuracy of QP subproblems\t%.4e\n", accqp);
      }
      System.out.printf("Optimised value:\t%.3f\n", objmip);
    }
    else{
      System.out.printf("h02daf returns ifail = %d\n", ifail);
    }
  }

  /**
   * objfun class extending Abstract_H02DA_OBJFUN to represent objfun subroutine for passing to H02DA
   */
  public static class objfun extends H02DA.Abstract_H02DA_OBJFUN{
    public void eval(){
      if(this.MODE == 0){
        //Objective value
        this.OBJMIP = (this.X[0] * ((4 * this.X[0]) + (3 * this.X[1]) - this.X[2]))
          + (this.X[1] * ((3 * this.X[0]) + (6 * this.X[1]) + this.X[2]))
          + (this.X[2] * (this.X[1] - this.X[0] + (10 * this.X[2])));
      }
      else{
        //Objective gradients for continous varaiables
        this.OBJGRD[0] = (8 * this.X[0]) + (6 * this.X[1]) - (2 * this.X[2]);
        this.OBJGRD[1] = (6 * this.X[0]) + (12 * this.X[1]) + (2 * this.X[2]);
        this.OBJGRD[2] = (2 * (this.X[1] - this.X[0])) + (20 * this.X[2]);
        this.OBJGRD[3] = 0;
      }
    }
  }

  /**
   * confun class extending Abstract_H02DA_CONFUN to represent confun subroutine for passing to H02DA
   */
  public static class confun extends H02DA.Abstract_H02DA_CONFUN{
    public void eval(){
      if(this.MODE == 0){
        //Constraints
        int p = this.IUSER[0];
        double rho  = this.RUSER[0];

        //Mean return rho:
        this.C[0] = (8 * this.X[0]) + (9 * this.X[1]) + (12 * this.X[2]) + (7 * this.X[3]) - rho;
        //Maximum of p assets in portfolio
        this.C[1] = p - this.X[4] - this.X[5] - this.X[6] - this.X[7];
      }
      else{
        //Jacobian
        this.CJAC[0] = 8;
        this.CJAC[2] = 9;
        this.CJAC[4] = 12;
        this.CJAC[6] = 7;
        //c[2] does not include continuours varaibles which requries that their derivatives are zero
        this.CJAC[1] = 0;
        this.CJAC[3] = 0;
        this.CJAC[5] = 0;
        this.CJAC[7] = 0;
      }
    }
  }
}
