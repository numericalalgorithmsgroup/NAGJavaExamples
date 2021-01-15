import com.nag.routines.C05.C05AW;

public class C05AWJE{

  public static void main(String[] args){
    double eps, eta, x;
    int ifail, nfmax;
    double[] ruser;
    int[] iuser;

    ruser = new double[1];
    iuser = new int[1];

    System.out.println("C05AWJ Example Program Results");
    System.out.println();

    boolean finished = false; 
    for(int i = 3; i <= 4; i++){
      eps = Math.pow(10, -i);
      x = 1;
      eta = 0;
      nfmax = 200;

      ifail = -1;
      f f1 = new f();
      C05AW c05aw = new C05AW(x, eps, eta, f1, nfmax, iuser, ruser, ifail);
      c05aw.eval();

      ifail = c05aw.getIFAIL();
      x = c05aw.getX();
      eps = c05aw.getEPS();
      
      switch(ifail){
        case(0):
          System.out.printf("With eps = %.2e   root = %.5f\n", eps, x);
          break;
        case(-1):
          //exit loop
          finished = true;
          break;
        case(3):
        case(4):
          System.out.printf("With eps = %.2e   root = %.5f\n", eps, x);
          break;
      }
      if(finished){
        break;
      }
    }
  }

  public static class f extends C05AW.Abstract_C05AW_F{
    public double eval(){
      return(Math.exp(-X) - X);
    }
  }
}
  
    
