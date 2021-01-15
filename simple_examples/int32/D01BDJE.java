import com.nag.routines.D01.D01BD;

/**
 * D01BD example program text.
 */
public class D01BDJE {

  public static void main(String[] args) {

    double a = 0.0, b = 1.0;
    double epsabs = 0.0, epsrel = 0.0001;
    double result = Double.NaN;
    double abserr = Double.NaN;
    FUN fun = new FUN();
    D01BD d01bd = new D01BD(fun, a, b, epsabs, epsrel, result, abserr);

    d01bd.eval();
    result = d01bd.getRESULT();
    abserr = d01bd.getABSERR();

    System.out.println(" D01BDJ Example Program Results");

    System.out.println();
    System.out.printf(" A      - lower limit of integration = %10.4f\n",a);
    System.out.printf(" B      - upper limit of integration = %10.4f\n",b);
    System.out.printf(" EPSABS - absolute accuracy requested = %9.2E\n",epsabs);
    System.out.printf(" EPSREL - relative accuracy requested = %9.2E\n",epsrel);
    System.out.println();
    System.out.printf(" RESULT - approximation to the integral = %9.5f\n",result);
    System.out.printf(" ABSERR - estimate to the absolute error = %9.2E\n",abserr);
    System.out.println();

    if (abserr > Math.max(epsabs,epsrel*Math.abs(result))) {
      System.out.println("Warning - requested accuracy may not have been achieved");
    }

  }


  public static class FUN implements D01BD.D01BD_F {

    private double x;

    public double eval(double x) {
      return (x * x * Math.sin(10.0 * Math.PI * x));
    }

    public double getX() {
      return x;
    }

    public void setX(double d) {
      x = d;
    }

  }

}
