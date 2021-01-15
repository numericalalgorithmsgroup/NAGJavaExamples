import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.C05.C05AZ;
import com.nag.routines.Routine;
import java.util.logging.Level;
import java.util.logging.Logger;

public class C05AZJE {

    public static void main (String[] args)throws NAGBadIntegerException{
        double tolx = 0.00001, x = 0.0, y = 1.0, fx;
        long ir = 0, ind = 1, ifail = -1;
        double[] c = new double[17];
        boolean keepOn = true;
		
		Routine.init();
						
        C05AZ c05az = new C05AZ();
        fx = fun(x);
        int ite = 0;
		
        System.out.println(" C05AZJ Example Program Results\n");
        System.out.println(" Iterations\n");
        while (keepOn) {
            ++ite;
			
            c05az.eval(x,y,fx,tolx,ir,c,ind,ifail);
            
            x = c05az.getX();
            y = c05az.getY();
            tolx = c05az.getTOLX();
            ir = c05az.getIR();
            ind = c05az.getIND();
            ifail = c05az.getIFAIL();
            
			if(ind == 0){
				keepOn = false;
			}
			else{
				fx = fun(x);
				System.out.printf("  X = %7.5f   FX = %11.4E   IND = %1d\n", x, fx, ind);
			}
        }


        
		switch ((int)ifail) {
            case 0:
                System.out.println("\n Solution\n");
				System.out.printf(" X = %8.5f Y = %8.5f\n",x,y);
				break;
            case 4:
            case 5:
				System.out.printf(" X = %8.5f Y = %8.5f\n",x,y);
                break;
            default:
				System.out.printf("Unexpected error ifail=%d\n",ifail);
            }
    }

    private static double fun(double x) {
        double res = (Math.expm1(-x) + 1) - x;
		return res;
    }

}
