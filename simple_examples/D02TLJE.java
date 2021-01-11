import com.nag.routines.D02.D02TL;
import com.nag.routines.D02.D02TV;
import com.nag.routines.D02.D02TX;
import com.nag.routines.D02.D02TY;
import com.nag.routines.D02.D02TZ;
import java.util.Arrays;

/**
 * D02TL example program text.
 * @author joed
 */
public class D02TLJE {

  public static final int MMAX = 3, NEQ = 3, NLBC = 3, NRBC = 3;

  public static double omega, sqrofr;
  public static int[] m = {1, 3, 2};

  public static FFUN ffun = new FFUN();
  public static FJAC fjac = new FJAC();
  public static GAFUN gafun = new GAFUN();
  public static GAJAC gajac = new GAJAC();
  public static GBFUN gbfun = new GBFUN();
  public static GBJAC gbjac = new GBJAC();
  public static GUESS guess = new GUESS();

  public static void main(String[] args) {

    D02TL d02tl = new D02TL();
    D02TV d02tv = new D02TV();
    D02TX d02tx = new D02TX();
    D02TY d02ty = new D02TY();
    D02TZ d02tz = new D02TZ();
    double dx, ermx, r;
    int iermx, ifail, ijermx, licomm, lrcomm, mxmesh, ncol, ncont,
        nmesh;
    double[] mesh, rcomm, tol, y, ruser = new double[1];
    int[] icomm, ipmesh, iuser = new int[2];

    System.out.println(" D02TLJ Example Program Results");
    System.out.println();

    ncol = 7;
    nmesh = 11;
    mxmesh = 51;

    mesh = new double[mxmesh];
    tol = new double[NEQ];
    y = new double[NEQ * MMAX];
    ipmesh = new int[mxmesh];

    omega = 1.0;
    Arrays.fill(tol, 1.0E-4);

    dx = 1.0 / ((double) nmesh - 1);

    mesh[0] = 0.0;
    for (int i = 1; i < nmesh - 1; i++) {
      mesh[i] = mesh[i - 1] + dx;
    }
    mesh[nmesh - 1] = 1.0;

    ipmesh[0] = 1;
    Arrays.fill(ipmesh, 1, nmesh - 1, 2);
    ipmesh[nmesh - 1] = 1;

    // Workspace query to get size of rcomm and icomm
    ifail = 0;
    d02tv.eval(
        NEQ, m, NLBC, NRBC, ncol, tol, mxmesh, nmesh, mesh, ipmesh, ruser,
        0, iuser, 2, ifail
    );
    ifail = d02tv.getIFAIL();
    lrcomm = iuser[0];
    licomm = iuser[1];
    rcomm = new double[lrcomm];
    icomm = new int[licomm];

    // Initialise integrator for given problem
    ifail = 0;
    d02tv.eval(
        NEQ, m, NLBC, NRBC, ncol, tol, mxmesh, nmesh, mesh, ipmesh, rcomm,
        lrcomm, icomm, licomm, ifail
    );
    ifail = d02tv.getIFAIL();

    ncont = 3;
    r = 1.0E6;

    sqrofr = Math.sqrt(r);

    ermx = 0.0;
    iermx = 0;
    ijermx = 0;

    for (int j = 0; j < ncont; j++) {
      System.out.printf("\n Tolerance = %8.1E  R = %10.3E\n", tol[0], r);

      // Solve problem
      ifail = -1;
      d02tl.eval(
          ffun, fjac, gafun, gbfun, gajac, gbjac, guess, rcomm, icomm,
          iuser, ruser, ifail
      );
      ifail = d02tl.getIFAIL();
      if (ifail != 0) {
        System.err.println("D02TL failed with error code " + ifail);
      }

      // Extract mesh
      ifail = -1;
      d02tz.eval(
          mxmesh, nmesh, mesh, ipmesh, ermx, iermx, ijermx, rcomm, icomm,
          ifail
      );
      nmesh = d02tz.getNMESH();
      iermx = d02tz.getIERMX();
      ijermx = d02tz.getIJERMX();
      ermx = d02tz.getERMX();
      ifail = d02tz.getIFAIL();
      if (ifail == 1) {
        break;
      }

      // Print mesh, error stats
      System.out.printf(
          "\n"
          + " Used a mesh of %4d points\n"
          + " Maximum error = %10.2E  in interval %4d for component %4d\n"
          + "\n",
          nmesh, ermx, iermx, ijermx
      );
      System.out.printf("\n Mesh points:\n");
      for (int i = 0; i < nmesh; i++) {
        System.out.printf("%4d(%1d)%10.3E", i+1, ipmesh[i], mesh[i]);
        if ((i+1) % 4 == 0) {
          System.out.printf("\n");
        }
      }
      System.out.printf("\n");

      // Print solution components on mesh
      System.out.printf("\n      x        f        f\'       g\n");
      for (int i = 0; i < nmesh; i++) {
        ifail = 0;
        d02ty.eval(mesh[i], y, NEQ, MMAX, rcomm, icomm, ifail);
        ifail = d02ty.getIFAIL();
        System.out.printf(
            " %8.3f %9.4f%9.4f%9.4f\n", mesh[i], y[getIdx(1, 0, NEQ)],
            y[getIdx(2, 0, NEQ)], y[getIdx(3, 0, NEQ)]
        );
      }

      if (j == ncont - 1) {
        break;
      }

      // Modify continuation parameter
      r = 100.0 * r;
      sqrofr = Math.sqrt(r);

      // Select mesh for continuation
      Arrays.fill(ipmesh, 1, nmesh - 1, 2);

      // Call continuation primer routine
      ifail = 0;
      d02tx.eval(mxmesh, nmesh, mesh, ipmesh, rcomm, icomm, ifail);
      mxmesh = d02tx.getMXMESH();
      nmesh = d02tx.getNMESH();
      ifail = d02tx.getIFAIL();

    }

  }

  /**
   * Converts a 2D Fortran index to the 1D index for its corresponding Java
   * array. Assumes y is already zero-based.
   *
   * <p>Fortran array definition:
   *     a(dimX, 0:*)
   *
   * <p>Conversion:
   *     a(x, y) --> A[result]
   */
  private static int getIdx(int x, int y, int dimX) {
    return (y * dimX) + (x-1);
  }

  /**
   * Converts a 3D Fortran index to the 1D index for its corresponding Java
   * array. Assumes z is already zero-based.
   *
   * <p>Fortran array definition:
   *     a(dimX, dimY, 0:*)
   *
   * <p>Conversion:
   *     a(x, y, z) --> A[result]
   */
  private static int getIdx(int x, int y, int z, int dimX, int dimY) {
    return (z * dimY * dimX) + ((y-1) * dimX) + (x-1);
  }

  public static class FFUN extends D02TL.Abstract_D02TL_FFUN {

    public void eval() {
      F[0] =    Y[getIdx(2, 0, NEQ)];
      F[1] = -((Y[getIdx(1, 0, NEQ)] * Y[getIdx(2, 2, NEQ)])
          +   (Y[getIdx(3, 0, NEQ)] * Y[getIdx(3, 1, NEQ)])) * sqrofr;
      F[2] =  ((Y[getIdx(2, 0, NEQ)] * Y[getIdx(3, 0, NEQ)])
          -   (Y[getIdx(1, 0, NEQ)] * Y[getIdx(3, 1, NEQ)])) * sqrofr;
    }

  }

  public static class FJAC extends D02TL.Abstract_D02TL_FJAC {

    public void eval() {
      DFDY[getIdx(1, 2, 0, NEQ, NEQ)] = 1.0;
      DFDY[getIdx(2, 1, 0, NEQ, NEQ)] = -Y[getIdx(2, 2, NEQ)] * sqrofr;
      DFDY[getIdx(2, 2, 2, NEQ, NEQ)] = -Y[getIdx(1, 0, NEQ)] * sqrofr;
      DFDY[getIdx(2, 3, 0, NEQ, NEQ)] = -Y[getIdx(3, 1, NEQ)] * sqrofr;
      DFDY[getIdx(2, 3, 1, NEQ, NEQ)] = -Y[getIdx(3, 0, NEQ)] * sqrofr;
      DFDY[getIdx(3, 1, 0, NEQ, NEQ)] = -Y[getIdx(3, 1, NEQ)] * sqrofr;
      DFDY[getIdx(3, 2, 0, NEQ, NEQ)] =  Y[getIdx(3, 0, NEQ)] * sqrofr;
      DFDY[getIdx(3, 3, 0, NEQ, NEQ)] =  Y[getIdx(2, 0, NEQ)] * sqrofr;
      DFDY[getIdx(3, 3, 1, NEQ, NEQ)] = -Y[getIdx(1, 0, NEQ)] * sqrofr;
    }

  }

  public static class GAFUN extends D02TL.Abstract_D02TL_GAFUN {

    public void eval() {
      GA[0] = YA[getIdx(1, 0, NEQ)];
      GA[1] = YA[getIdx(2, 0, NEQ)];
      GA[2] = YA[getIdx(3, 0, NEQ)] - omega;
    }

  }

  public static class GBFUN extends D02TL.Abstract_D02TL_GBFUN {

    public void eval() {
      GB[0] = YB[getIdx(1, 0, NEQ)];
      GB[1] = YB[getIdx(2, 0, NEQ)];
      GB[2] = YB[getIdx(3, 0, NEQ)] + omega;
    }

  }

  public static class GAJAC extends D02TL.Abstract_D02TL_GAJAC {

    public void eval() {
      DGADY[getIdx(1, 1, 0, NLBC, NEQ)] = 1.0;
      DGADY[getIdx(2, 2, 0, NLBC, NEQ)] = 1.0;
      DGADY[getIdx(3, 3, 0, NLBC, NEQ)] = 1.0;
    }

  }

  public static class GBJAC extends D02TL.Abstract_D02TL_GBJAC {

    public void eval() {
      DGBDY[getIdx(1, 1, 0, NRBC, NEQ)] = 1.0;
      DGBDY[getIdx(2, 2, 0, NRBC, NEQ)] = 1.0;
      DGBDY[getIdx(3, 3, 0, NRBC, NEQ)] = 1.0;
    }

  }

  public static class GUESS extends D02TL.Abstract_D02TL_GUESS {

    public void eval() {
      Y[getIdx(1, 0, NEQ)] = -(X - 0.5) * Math.pow(X * (X - 1.0), 2.0);
      Y[getIdx(2, 0, NEQ)] = -X * (X - 1.0)
          * (5.0 * X * (X - 1.0) + 1.0);
      Y[getIdx(2, 1, NEQ)] = -(2.0 * X - 1.0)
          * (10.0 * X * (X - 1.0) + 1.0);
      Y[getIdx(2, 2, NEQ)] = -12.0 * (5.0 * X * (X - 1.0) + 1.0);
      Y[getIdx(3, 0, NEQ)] = -8.0 * omega * Math.pow(X - 0.5, 3.0);
      Y[getIdx(3, 1, NEQ)] = -24.0 * omega * Math.pow(X - 0.5, 2.0);
      DYM[0] = Y[getIdx(2, 0, NEQ)];
      DYM[1] = -120.0 * (X - 0.5);
      DYM[2] = -56.0 * omega * (X - 0.5);
    }

  }

}
