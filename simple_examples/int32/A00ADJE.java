import com.nag.routines.A00.A00AD;
import com.nag.routines.X05.X05AA;
import java.util.Arrays;

/**
 * A00AD example program text.
 */
public class A00ADJE {

  public static void main(String[] args) {

    A00AD a00ad = new A00AD();
    X05AA x05aa = new X05AA();
    int i, mkmaj, mkmin;
    boolean licval;
    String fcomp, hdware, impl, opsys, pcode, prec, vend;
    int[] itime = new int[7];

    // Instantiate arguments
    mkmaj = 0;
    mkmin = 0;
    licval = false;

    // Strings must be length expected by Fortran
    fcomp = getBlankString(80);
    hdware = getBlankString(80);
    impl = getBlankString(80);
    opsys = getBlankString(80);
    pcode = getBlankString(80);
    prec = getBlankString(80);
    vend = getBlankString(80);

    System.out.println(" A00ADJ Example Program Results\n");

    a00ad.eval(impl, prec, pcode, mkmaj, mkmin, hdware, opsys, fcomp, vend, licval);

    // Output scalars must be retrieved manually
    impl = a00ad.getIMPL();
    prec = a00ad.getPREC();
    pcode = a00ad.getPCODE();
    mkmaj = a00ad.getMKMAJ();
    mkmin = a00ad.getMKMIN();
    hdware = a00ad.getHDWARE();
    opsys = a00ad.getOPSYS();
    fcomp = a00ad.getFCOMP();
    vend = a00ad.getVEND();
    licval = a00ad.getLICVAL();

    // Print implementation details

    System.out.println("*** Start of NAG Library implementation details ***");
    System.out.println();
    System.out.println("Implementation title: " + impl.trim());
    System.out.println("           Precision: " + prec.trim());
    System.out.println("        Product code: " + pcode.trim());

    if (mkmin < 10) {
      System.out.printf("                Mark: %2d.%1d\n", mkmaj, mkmin);
    }
    else {
      System.out.printf("                Mark: %2d.%2d\n", mkmaj, mkmin);
    }

    if (vend.trim().equals("(self-contained)")) {
      System.out.println("      Vendor Library: None");
    }
    else {
      System.out.println("      Vendor Library: " + vend.trim());
    }

    System.out.println("Applicable to:");
    System.out.println("            hardware: " + hdware.trim());
    System.out.println("    operating system: " + opsys.trim());
    System.out.println("    Fortran compiler: " + fcomp.trim());
    System.out.println("and compatible systems.");

    if (!licval) {
      System.out.println("       Licence query: Unsuccessful");
    }
    else {
      System.out.println("       Licence query: Successful");
    }

    System.out.println();
    System.out.println("*** End of NAG Library implementation details ***");

  }

  /**
  * Returns a new String, filled with spaces to the specified length.
  *
  * @param   len  the required length of the String
  * @return  a blank String of the specified length
  */
  public static String getBlankString(int len) {

    if (len > 0) {
      char[] arr = new char[len];
      Arrays.fill(arr, ' ');
      return new String(arr);
    }

    return "";

  }

}
