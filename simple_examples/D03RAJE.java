import com.nag.routines.D03.D03RA;
import com.nag.routines.D03.D03RA.D03RA_BNDARY;
import com.nag.routines.D03.D03RA.D03RA_MONITR;
import com.nag.routines.X01.X01AA;
import com.nag.routines.X02.X02AJ;
import java.util.Arrays;

/**
 * D03RA example program text.
 * @author joed
 */
public class D03RAJE {

  public static final double ALPHA = 50.0;
  public static final double BETA = 300.0;
  public static final double XMAX = 1.0;
  public static final double XMIN = 0.0;
  public static final double YMAX = 1.0;
  public static final double YMIN = 0.0;
  public static final int ITRACE = 0;
  public static final int NPDE = 1;

  public static D03RA d03ra = new D03RA();
  public static PDEDEF pdedef = new PDEDEF();
  public static BNDRY bndry = new BNDRY();
  public static PDEIV pdeiv = new PDEIV();
  public static MONIT monit = new MONIT();
  public static MONITDUMMY monitDummy = new MONITDUMMY();

  public static void main(String[] args) {
    double tols, tolt, tout, ts;
    int ifail, ind, leniwk, lenlwk, lenrwk, maxlev, npde, npts, nx, ny;
    double[] dt, twant, optr, rwk;
    int[] wklens, iwk, opti = new int[4];
    boolean[] lwk;

    // Run examples
    System.out.println(" D03RAJ Example Program Results");
    System.out.println();

    npts = 2000;
    npde = NPDE;

    dt = new double[] {0.1e-2, 0.0, 0.0};
    twant = new double[] {0.24, 0.25};
    ts = 0.0;

    ind = 10;
    nx = 41;
    ny = 41;
    tols = 0.5;
    tolt = 0.01;
    Arrays.fill(opti, 0);
    opti[0] = 6;
    maxlev = Math.max(opti[0], 3);

    wklens = computeWkspaceLens(maxlev, npde, npts);
    lenrwk = wklens[0];
    leniwk = wklens[1];
    lenlwk = wklens[2];
    rwk = new double[lenrwk];
    iwk = new int[leniwk];
    lwk = new boolean[lenlwk];

    optr = new double[3 * npde];
    Arrays.fill(optr, 1.0);

    for (int i = 0; i < 2; i++) {
      tout = twant[i];
      ifail = 0;
      if (i == 0) {
        // Dummy monitor used to avoid output on first call
        d03ra.eval(
            npde, ts, tout, dt, XMIN, XMAX, YMIN, YMAX, nx, ny, tols,
            tolt, pdedef, bndry, pdeiv, monitDummy, opti, optr,
            rwk, lenrwk, iwk, leniwk, lwk, lenlwk, ITRACE, ind, ifail
        );
      }
      else {
        d03ra.eval(
            npde, ts, tout, dt, XMIN, XMAX, YMIN, YMAX, nx, ny, tols,
            tolt, pdedef, bndry, pdeiv, monit, opti, optr, rwk,
            lenrwk, iwk, leniwk, lwk, lenlwk, ITRACE, ind, ifail
        );
      }

      ind = d03ra.getIND();
      ifail = d03ra.getIFAIL();
      ts = d03ra.getTS();

      printStatistics(ts, iwk, maxlev);

    }

  }

  public static class PDEIV extends D03RA.Abstract_D03RA_PDEIV {

    public void eval() {
      Arrays.fill(this.U, 1.0);
    }

  }

  public static class PDEDEF extends D03RA.Abstract_D03RA_PDEDEF {

    private static final double ACTIV_ENERGY = 20.0;
    private static final double DIFFUSION = 0.1;
    private static final double HEAT_RELEASE = 1.0;
    private static final double REACTION_RATE = 5.0;

    public void eval() {

      double damkohler;

      damkohler = REACTION_RATE * Math.exp(ACTIV_ENERGY)
          / (HEAT_RELEASE * ACTIV_ENERGY);

      for (int col = 0; col < this.NPDE; col++) {
        for (int row = 0; row < this.NPTS; row++) {
          int idx = (col * this.NPTS) + row;
          this.RES[idx] = this.UT[idx]
              - (DIFFUSION * (this.UXX[idx] + this.UYY[idx]))
              - (damkohler
                  * (1.0e0 + HEAT_RELEASE - this.U[idx])
                  * Math.exp(-ACTIV_ENERGY / this.U[idx]));
        }
      }

    }

  }

  public static class BNDRY extends D03RA.Abstract_D03RA_BNDARY {

    public void eval() {
      X02AJ x02aj = new X02AJ();
      double tol;

      // X02AJ returns machine precision
      tol = 10.0 * x02aj.eval();

      for (int i = 0; i < this.NBPTS; i++) {
        int j = this.LBND[i] - 1;

        if (Math.abs(this.X[j]) <= tol) {
          for (int col = 0; col < this.NPDE; col++) {
            int idx = (col * this.NPTS) + j;
            this.RES[idx] = this.UX[idx];
          }
        }
        else if (Math.abs(this.X[j] - 1.0) <= tol) {
          for (int col = 0; col < this.NPDE; col++) {
            int idx = (col * this.NPTS) + j;
            this.RES[idx] = this.U[idx] - 1.0;
          }
        }
        else if (Math.abs(this.Y[j]) <= tol) {
          for (int col = 0; col < this.NPDE; col++) {
            int idx = (col * this.NPTS) + j;
            this.RES[idx] = this.UY[idx];
          }
        }
        else if (Math.abs(this.Y[j] - 1.0) <= tol) {
          for (int col = 0; col < this.NPDE; col++) {
            int idx = (col * this.NPTS) + j;
            this.RES[idx] = this.U[idx] - 1.0;
          }
        }
      }

    }

  }

  public static class MONIT extends D03RA.Abstract_D03RA_MONITR {

    public void eval() {
      int ipsol, k, level, npts;

      if (TLAST) {
        // Print solution
        level = this.NLEV - 1;
        npts = this.NGPTS[level];
        ipsol = this.LSOL[level];
        k = 0;
        for (int i = 0; i < level; i++) {
          k += this.NGPTS[i];
        }

        System.out.printf(
            " Solution at every 4th grid point in level%10d"
            + " at time %8.4f:%n%n", this.NLEV, this.T
        );
        System.out.println(
            "        x          y        approx u\n"
        );
        for (int i = 0; i < npts; i += 4) {
          double ix = this.XPTS[k + i];
          double iy = this.YPTS[k + i];
          double isol = this.SOL[ipsol + i];
          System.out.printf(
              " %11.4E %11.3E %11.3E%n",
              ix, iy, isol
          );
        }
        System.out.println();

      }

    }

  }

  public static class MONITDUMMY extends D03RA.Abstract_D03RA_MONITR {

    public void eval() {
      return;
    }

  }

  public static int[] computeWkspaceLens(int maxlev, int npde, int maxpts) {
    int lenrwk, leniwk, lenlwk;

    lenrwk = (2 * maxpts * npde * ((5 * maxlev) + (18 * npde) + 9))
        + (2 * maxpts);
    leniwk = (2 * maxpts * (14 + (5 * maxlev)))
        + (7 * maxlev) + 2;
    lenlwk = (2 * maxpts) + 400;

    return new int[] {lenrwk, leniwk, lenlwk};

  }

  public static void printStatistics(double ts, int[] iwk, int maxlev) {
    int[] istats = new int[4];

    System.out.printf(" Statistics:%n");
    System.out.printf(" Time = %8.4f%n", ts);
    System.out.printf(
        " Total number of accepted timesteps =%5d%n", iwk[0]
    );
    System.out.printf(
        " Total number of rejected timesteps =%5d%n", iwk[1]
    );
    System.out.printf(
        "%n"
        + "              Total  number (rounded) of%n"
        + "           Residual  Jacobian    Newton   Lin sys%n"
        + "              evals     evals     iters     iters%n"
        + "  At level %n"
    );

    for (int j = 0; j < maxlev; j++) {
      if (iwk[j + 2] != 0) {
        int idx = 0;
        for (int i = j+2; i <= j+2+(3*maxlev); i += maxlev) {
          istats[idx++] = iwk[i];
        }
        istats = roundStatisics(istats);
        System.out.printf("%8d", j + 1);
        for (int i = 0; i < 4; i++) {
          System.out.printf("%10d", istats[i]);
        }
        System.out.printf("%n");
      }
    }

    System.out.printf(
        "%n"
        + "              Maximum  number of   %n"
        + "              Newton iters    Lin sys iters %n"
        + " At level %n"
    );

    for (int j = 0; j < maxlev; j++) {
      if (iwk[j+2] != 0) {
        System.out.printf("%8d", j+1);
        System.out.printf("%14d", iwk[j+2+(4*maxlev)]);
        System.out.printf("%14d", iwk[j+2+(5*maxlev)]);
        System.out.printf("%n");
      }
    }
    System.out.println();

  }

  public static int[] roundStatisics(int[] istat) {
    double lt;
    int k;

    lt = Math.log(10.0);
    for (int i = 0; i < istat.length; i++) {
      // istat = 0 leads to div by 0 error, doesn't need rounding anyway
      if (istat[i] != 0) {
        k = (int) (Math.log((double) istat[i]) / lt);
        k = (int) Math.pow(10, k);
        istat[i] = k * ((istat[i] + k/2)/k);
      }
    }

    return istat;

  }

}
