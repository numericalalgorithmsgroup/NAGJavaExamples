import com.nag.routines.D05.D05BA;
import com.nag.routines.X02.X02AJ;

/**
 * D05BAJ example program text
 * @author willa
 * @since 27.1.0.0
 */
public class D05BAJE{

  /**
   * D05BAJE main program
   */
  public static void main(String[] args){
    double alim, h, hi, si, thresh, tlim, tol;
    int ifail, iorder, lwk, nmesh = 6;
    String method;
    double[] errest, yn, work;

    errest = new double[nmesh];
    yn = new double[nmesh];

    System.out.println("D05BAJ Example Program Results");

    method = "A";
    iorder = 6;
    alim = 0;
    tlim = 20;
    h = (tlim - alim) / (double) nmesh;
    tol = 0.001;
    X02AJ x02aj = new X02AJ();
    thresh = x02aj.eval();
    lwk = 10 * nmesh + 6;
    work = new double[lwk];

    //Loop until the supplied workspace is big enough
    //breakflag used to exit loop
    boolean breakflag = false; 
    while(true){
      ifail = 1;

      ck ck1 = new ck();
      cf cf1 = new cf();
      cg cg1 = new cg();
      
      D05BA d05ba = new D05BA(ck1, cg1, cf1, method, iorder, alim, tlim, yn, errest, nmesh,
                              tol, thresh, work, lwk, ifail);
      d05ba.eval();
      
      //update
      ifail = d05ba.getIFAIL();
      lwk = d05ba.getLWK();
      work = d05ba.getWORK();

      switch(ifail){
        case 5:
          lwk = (int) work[0];
          work = new double[lwk];
          break;
        case 6:
          lwk = (int) work[0];
          work = new double[lwk];
          break;
        default:
          breakflag = true;
      }
      
      if(breakflag == true){
        break;
      } 
    }

    if(ifail != 0){
      System.out.printf("D05BAJ exited with IFAIL = %d\n", ifail);
    }

    System.out.println();
    System.out.printf("Size of workplace = %d\n", lwk);
    System.out.printf("Tolerance         = %.4e\n", tol);
    System.out.println();
    System.out.print("T\tApprox. Sol.\tTrue Sol.\tEst. Error\tActual Error\n");
    for(int i = 0; i < nmesh; i++){
      hi = (double) (i + 1) * h;
      si = sol(hi);
      System.out.printf("%.2f\t%.5f\t\t%.5f\t\t%.5e\t%.5e\n", (alim + hi), yn[i], si, errest[i],  Math.abs((yn[i] - si) / si));
    }
  }

  private static double sol(double t){
    return Math.log(t + Math.exp(1));
  }

  public static class ck extends D05BA.Abstract_D05BA_CK{
    public double eval(){
      return Math.exp(-this.T);
    }
  }

  public static class cf extends D05BA.Abstract_D05BA_CF{
    public double eval(){
      return Math.exp(-this.T);
    }
  }

  public static class cg extends D05BA.Abstract_D05BA_CG{
    public double eval(){
      return (this.Y + Math.exp(-this.Y));
    }
  }
}
    
