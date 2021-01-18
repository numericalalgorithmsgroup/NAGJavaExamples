import com.nag.routines.E04.E04CB;
import com.nag.routines.E04.E04CBK;
import com.nag.routines.X02.X02AJ;

/**
 * E04CBJ Example Program text
 * @author willa
 * @since 27.1.0.0
 */
public class E04CBJE{

  /**
   * E04CBJE main program
   */
  public static void main(String[] args){
    int n = 2, ifail, maxcal;
    double f = 0, tolf, tolx; //placeholders
    boolean monitoring;
    int[] iuser;
    double[] ruser, x;

    iuser = new int[1];
    ruser = new double[1];
    x = new double[n];

    System.out.println("E04CBJ Example Program Results");

    //Set monitoring to true to obtain monitoring information
    monitoring = false;

    x[0] = -1.0;
    x[1] = 1.0;
    X02AJ x02aj = new X02AJ();
    tolf = Math.sqrt(x02aj.eval());
    tolx = Math.sqrt(tolf);
    maxcal = 100;

    ifail = 0;

    funct funct1 = new funct();
    if(!monitoring){
      defMonit monit = new defMonit();
      E04CB e04cb = new E04CB(n, x, f, tolf, tolx, funct1, monit, maxcal, iuser, ruser, ifail);
      e04cb.eval();
    }
    else{
      myMonit monit = new myMonit();
      E04CB e04cb = new E04CB(n, x, f, tolf, tolx, funct1, monit, maxcal, iuser, ruser, ifail);
      e04cb.eval();
    }

    System.out.println();
    System.out.printf("The final function value is \t%.4f\n", f);
    System.out.printf("at the point\t");
    for(int i = 0; i < n; i++){
      System.out.printf("%.4f\t", x[i]);
    }
    System.out.printf("\n");
  }

  public static class funct extends E04CB.Abstract_E04CB_FUNCT{
    public void eval(){
      this.FC = Math.exp(this.XC[0]) * ((4 * this.XC[0] * (this.XC[0] + this.XC[1])) + (2 * this.XC[1] * (this.XC[1] + 1) + 1));
    }
  }

  public static class myMonit extends E04CB.Abstract_E04CB_MONIT{
    public void eval(){
      System.out.println();
      System.out.printf("There have been %d function calls\n", this.NCALL);
      System.out.printf("The smallest function value is %.4f\n", this.FMIN);
      System.out.printf("The simplex is\n");
      for(int i = 0; i <= this.N; i++){
        for(int j = 0; j < this.N; j++){
          System.out.printf("%.4f\t", this.SIM[(j * (this.N + 1)) + i]);
        }
        System.out.printf("\n");
      }
      System.out.printf("The standard deviation in function values of the vertices of the simplex is %.4f\n", this.SERROR);
      System.out.printf("The linearized volume ratio of the current simplex to the starting one is %.4f\n", this.VRATIO);
    }
  }

  //This is how to use NAG supplied function as argument
  public static class defMonit extends E04CBK implements E04CB.E04CB_MONIT{
    public void eval(){
      super.eval();
    }
  }
}
