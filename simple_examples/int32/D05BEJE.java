import com.nag.routines.D05.D05BE;
import com.nag.routines.X01.X01AA;
import com.nag.routines.X02.X02AJ;

/**
 * D05BEJ example program text.
 * @author willa
 */
public class D05BEJE{

  private static final int iorder = 4;
  private static final int nmesh = (int)(Math.pow(2, 6) + (2 * iorder) - 1);
  private static final int nout = 6;
  private static final int lct = nmesh / 32 + 1;
  private static final int lwk = ((2 * iorder) + 6) * nmesh + (8 * iorder * iorder) - (16 * iorder) + 1;

  public static void main(String[] args){
    double err, errmax, h, hi1, soln = 0, t = 0, tlim, tolnl;
    int ifail;
    double[] work, yn;
    int[] nct;

    work = new double[lwk];
    yn = new double[nmesh];
    nct = new int[lct];

    System.out.println("D05BEJ Example Program Results");

    X02AJ x02aj = new X02AJ();
    tlim = 7;
    tolnl = Math.sqrt(x02aj.eval());
    h = tlim /(double) (nmesh - 1);
    yn[0] = 0;
    
    ifail = 0;

    D05BE d05be = new D05BE();
    ck1 k1 = new ck1();
    cf1 f1 = new cf1();
    cg1 g1 = new cg1();
    d05be.eval(k1, f1, g1, "Initial", iorder, tlim, tolnl, nmesh, yn, work, lwk, nct, ifail);

    //UPDATE
    yn = d05be.getYN();
    
    System.out.println();
    System.out.println("Example 1");
    System.out.println();
    System.out.printf("The stepsize h = %.4f\n", h);
    System.out.println();
    System.out.println("\tT\tApproximate");
    System.out.println("\t\tSolution");
    System.out.println();

    errmax = 0;

    for(int i = 1; i < nmesh; i++){
      hi1 = (double) i * h;
      err = Math.abs(yn[i] - sol1(hi1));

      if(err > errmax){
        errmax = err;
        t = hi1;
        soln = yn[i];
      }

      if((i > 4) && (i % 5 == 0)){
        System.out.printf("\t%.4f\t%.4f\n", hi1, yn[i]);
      }
    }

    System.out.println();
    System.out.printf("The maximum absolute error, %.2e, occured at T = %.4f with solution %.4f\n", errmax, t, soln);
    System.out.println();

    tlim = 5;
    h = tlim /(double) (nmesh - 1);
    yn[0] = 1;

    ifail = 0;

    ck2 k2 = new ck2();
    cf2 f2 = new cf2();
    cg2 g2 = new cg2();
    d05be.eval(k2, f2, g2, "Subsequent", iorder, tlim, tolnl, nmesh, yn, work, lwk, nct, ifail);

    //UPDATE
    yn = d05be.getYN();
    
    System.out.println();
    System.out.println("Example 2");
    System.out.println();
    System.out.printf("The stepsize h = %.4f\n", h);
    System.out.println();
    System.out.println("\tT\tApproximate");
    System.out.println("\t\tSolution");
    System.out.println();

    errmax = 0;
    
    for(int i = 0; i < nmesh; i++){
      hi1 = (double) i * h;
      err = Math.abs(yn[i] - sol2(hi1));
      if(err > errmax){
        errmax = err;
        t = hi1;
        soln = yn[i];
      }

      if((i > 6) && (i % 7 == 0)){
        System.out.printf("\t%.4f\t%.4f\n", hi1, yn[i]);
      }
    }

    System.out.println();
    System.out.printf("The maximum absolute error, %.2e, occured at T = %.4f with solution %.4f\n", errmax, t, soln);
  }

  private static double sol1(double t){
    double c, pi, t1, x = 0;

    //x is dummy variable
    X01AA x01aa = new X01AA(x);
    pi = x01aa.eval();
  
    t1 = 1 + t;
    c = 1 / Math.sqrt(2 * pi);

    return (c * (1 / Math.pow(t, 1.5)) * Math.exp((-t1 * t1) / (2 * t)));  
  }

  private static double sol2(double t){
    return (1 / (1 + t));
  }

  private static class ck1 extends D05BE.Abstract_D05BE_CK{
    public double eval(){
      return (Math.exp(-0.5 * this.T));
    }
  }

  private static class ck2 extends D05BE.Abstract_D05BE_CK{
    double pi, x = 0;

    public double eval(){
      X01AA x01aa = new X01AA(x);
      pi = x01aa.eval();
      return Math.sqrt(pi);
    }
  }

  private static class cf1 extends D05BE.Abstract_D05BE_CF{
    double a, pi, t1, x = 0;

    public double eval(){
      X01AA x01aa = new X01AA(x);
      pi = x01aa.eval();

      t1 = 1 + this.T;
      a = 1 / Math.sqrt(pi * this.T);
      return (-a * Math.exp((-0.5 * t1 * t1) / this.T)); 
    }
  }

  private static class cf2 extends D05BE.Abstract_D05BE_CF{
    double st1;

    public double eval(){
      st1 = Math.sqrt(1 + this.T);
      return ((-2 * Math.log(st1 + Math.sqrt(this.T))) / st1); 
    }
  }

  private static class cg1 extends D05BE.Abstract_D05BE_CG{
    public double eval(){
      return this.Y; 
    }
  }

  private static class cg2 extends D05BE.Abstract_D05BE_CG{
    public double eval(){
      return this.Y; 
    }
  }
}
