import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.A00.A00AA;
import com.nag.routines.Routine;

public class A00AAJE {

    public static void main (String[] args) throws NAGBadIntegerException {
        Routine.init();
        A00AA a00aa = new A00AA();
        System.out.println(" A00AAJ Example Program Results\n");
        a00aa.eval();
    }

}
