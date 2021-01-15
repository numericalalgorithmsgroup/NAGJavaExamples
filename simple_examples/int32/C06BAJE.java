import com.nag.routines.C06.C06BA;
import com.nag.routines.X01.X01AA;

/**
 * C06BAJ example program text.
 * @author willa
 */
public class C06BAJE{

  public static void main(String[] args){
    int lwork = 16, nout = 6;
    int ifail, ncall;
    double abserr = 0.0, ans, error, pi = 0.0, r, result = 0.0, seqn, sig;
    double[] work;

    C06BA c06ba = new C06BA();
    work = new double[lwork];

    System.out.println("C06BAJ Example Program Results");
    System.out.println();

    X01AA x01aa = new X01AA(pi);
    pi = x01aa.eval();

    ans = Math.pow(pi, 2)/12.0;
    ncall = 0;
    sig = 1.0;
    seqn = 0.0;
    System.out.println("\t\t\t Estimated\t Actual");
    System.out.println("I\t SEQN \t RESULT\t abs error\t error");
    System.out.println();

    for(int i = 0; i < 10; i++){
      r = (double) i + 1;
      seqn = seqn + sig/Math.pow(r, 2);
      
      ifail = 0;
      c06ba.eval(seqn, ncall, result, abserr, work, lwork, ifail);

      //update variables
      ncall = c06ba.getNCALL();
      result = c06ba.getRESULT();
      abserr = c06ba.getABSERR();

      error = result - ans;
      sig = -sig;

      if(i <= 2){
        System.out.printf("%d\t %.4f\t %.4f\t -\t\t %.2e\n", (i + 1), seqn, result, error);
      }
      else{
        System.out.printf("%d\t %.4f\t %.4f\t %.2e\t %.2e\n", (i + 1), seqn, result, abserr, error);
      }
    }
  }
}
