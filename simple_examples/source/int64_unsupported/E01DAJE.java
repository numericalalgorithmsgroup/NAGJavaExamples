/*
 * E01DA Example Program - NAG Copyright 2016
 */
 import com.nag.exceptions.NAGBadIntegerException;
 import com.nag.routines.E01.E01DA;
 import com.nag.routines.E02.E02DF;
 import com.nag.routines.Routine;

public class E01DAJE {

    public static void main (String[] args)throws NAGBadIntegerException {
        Routine.init();

        int mx = 7, my = 6, ifail = -1;
        double[] x = new double[mx];
        double[] y = new double[my];
        double[] f = new double[mx*my];
        double[] lamda = new double[mx+4];
        double[] mu = new double[my+4];
        double[] c = new double[mx*my];
        double[] wrk = new double[(mx+6)*(my+6)];

        int nx = 6, ny = 6, px = 0, py = 0;
        double xlo = 1.0, xhi = 2.0;
        double ylo = 0.0, yhi = 1.0;
        
        E01DA e01da = new E01DA();

        // Input X, Y and function values on X-Y grid
        x[0] = 1.00;
        x[1] = 1.10;
        x[2] = 1.30;
        x[3] = 1.50;
        x[4] = 1.60;
        x[5] = 1.80;
        x[6] = 2.00;
        
        y[0] = 0.00;
        y[1] = 0.10;
        y[2] = 0.40;
        y[3] = 0.70;
        y[4] = 0.90;
        y[5] = 1.00;

        // On entry: F[my*(q-1)+r-1] must contain f(q,r), for q=1,2,...,mx
        // and r=1,2,...,my.
        f[0] = 1.00;
        f[1] = 1.10;
        f[2] = 1.40;
        f[3] = 1.70;
        f[4] = 1.90;
        f[5] = 2.00;
        f[6] = 1.21;
        f[7] = 1.31;
        f[8] = 1.61;
        f[9] = 1.91;
        f[10] = 2.11;
        f[11] = 2.21;
        f[12] = 1.69;
        f[13] = 1.79;
        f[14] = 2.09;
        f[15] = 2.39;
        f[16] = 2.59;
        f[17] = 2.69;
        f[18] = 2.25;
        f[19] = 2.35;
        f[20] = 2.65;
        f[21] = 2.95;
        f[22] = 3.15;
        f[23] = 3.25;
        f[24] = 2.56;
        f[25] = 2.66;
        f[26] = 2.96;
        f[27] = 3.26;
        f[28] = 3.46;
        f[29] = 3.56;
        f[30] = 3.24;
        f[31] = 3.34;
        f[32] = 3.64;
        f[33] = 3.94;
        f[34] = 4.14;
        f[35] = 4.24;
        f[36] = 4.00;
        f[37] = 4.10;
        f[38] = 4.40;
        f[39] = 4.70;
        f[40] = 4.90;
        f[41] = 5.00;

        System.out.printf(" E01DAJ Example Program Results\n\n");
        e01da.eval(mx, my, x, y, f, px, py, lamda, mu, c, wrk, ifail);

        ifail = (int)e01da.getIFAIL();
        switch (ifail)
          {
          case 0:
            System.out.printf("   I    Knot LAMDA(I)  J     Knot MU(j)\n");
            for (int i = 3; i <= Math.max(mx,my); ++i)
              {
                if (i <= mx)
                  System.out.printf("%4d   %9.4f", i+1, lamda[i]);
                else
                  System.out.printf("                  ");
                if (i <= my)
                  System.out.printf("%8d   %9.4f\n", i+1, mu[i]);
                else
                  System.out.printf("\n");
              }
            System.out.printf("\n The B-Spline coefficients:\n");
            for (int i = 0; i < mx*my; ++i)
              {
                System.out.printf("%9.4f", c[i]);
                if ((i+1)%8 == 0)
                  System.out.printf("\n");
              }
            System.out.printf("\n");
            break;
          default:
            System.out.printf("\n Error detected by E01DA: %d\n", ifail);
          }

        /* Evaluate the spline on a regular rectangular grid at nx*ny points
           over the domain [xlo,xhi] x [ylo,yhi]. */
        px = (int)e01da.getPX();
        py = (int)e01da.getPY();
        int liwrk;
        int lwrk = Math.min(4*nx+px,4*ny+py);
        if (4*nx+px>4*ny+py)
          liwrk = ny + py - 4;
        else
          liwrk = nx + px - 4;
        double tx[] = new double[nx];
        double ty[] = new double[ny];
        double ff[] = new double[nx*ny];
        wrk = new double[lwrk];
        long iwrk[] = new long[liwrk];

        /* Generate nx/ny equispaced x/y co-ordinates */
        double step = (xhi-xlo)/(nx-1);
        tx[0] = xlo;
        for (int i = 1; i<nx-1; i++)
          tx[i] = tx[i-1] + step;
        tx[nx-1] = xhi;

        step = (yhi-ylo)/(ny-1);
        ty[0] = ylo;
        for (int i = 1; i<ny-1; i++)
          ty[i] = ty[i-1] + step;
        ty[ny-1] = yhi;

        /* Evaluate the spline. */
        E02DF e02df = new E02DF();
        ifail = 0;
        e02df.eval(nx,ny,px,py,tx,ty,lamda,mu,c,ff,wrk,lwrk,iwrk,liwrk,ifail);

        ifail = (int)e02df.getIFAIL();
        switch (ifail)
          {
          case 0:
            System.out.printf("\n Spline evaluated on a regular mesh (X across, Y down):\n");
            System.out.printf("           ");
            for (int i = 0; i < nx; ++i)
              System.out.printf("  %5.2f  ", tx[i]);
            System.out.printf("\n");
            for (int i = 0; i < ny; ++i)
              {
                System.out.printf("  %5.2f  ", ty[i]);
                for (int j = 0; j < nx; ++j)
                  System.out.printf(" %8.3f", ff[ny*j+i]);
                System.out.printf("\n");
              }
            break;
          default:
            System.out.printf("\n Error detected by E02DF: %d\n", ifail);
          }
    }
 }
