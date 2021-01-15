import com.nag.routines.X05.X05AB;

/**
 * X05AB example program text.
 */
public class X05ABJE {

  public static void main(String[] args) {

    int[] itime = new int[7];

    itime[0] = 1789;
    itime[1] = 7;
    itime[2] = 14;
    itime[3] = 13;
    itime[4] = 11;
    itime[5] = 48;
    itime[6] = 320;

    System.out.println(" X05ABJ Example Program Results");
    System.out.println((new X05AB(itime)).eval());

  }

}
