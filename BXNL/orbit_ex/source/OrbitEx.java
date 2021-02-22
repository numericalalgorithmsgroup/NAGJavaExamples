import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RM;
import com.nag.routines.E04.E04RH;
import com.nag.routines.E04.E04ZM;
import com.nag.routines.E04.E04RX;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04GG;
import com.nag.routines.E04.E04GGU;
import com.nag.routines.E04.E04GGV;
import com.nag.routines.E04.E04FFU;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.awt.Color;
import java.util.Arrays;

public class OrbitEx {

    public static void main(String[] args) {

        int i;

        // Orbital Data Fitting

        double[] tx = new double[] { 441.23, 484.31, 265.15, 98.25, 180.66, 439.13, 596.54 };
        double[] ty = new double[] { 333.92, 563.46, 577.40, 379.23, 148.62, 100.28, 285.99 };
        double[] cc = new double[] { 355.00, 347.00 };
        double[] tr = new double[tx.length];

        for (i = 0; i < tr.length; i++) {
            tr[i] = Math.pow(tx[i] - cc[0], 2) + Math.pow(ty[i] - cc[1], 2);
        }

        try {
            // Open a JPEG file, load into a BufferedImage.
            BufferedImage img = ImageIO.read(new File("../img/earth.png"));

            // Obtain the Graphics2D context associated with the BufferedImage.
            Graphics2D g = img.createGraphics();

            // Set the Color to Yellow
            g.setColor(Color.YELLOW);

            // Draw the Points on the image
            for (i = 0; i < tx.length; i++) {
                drawPoint(g, (int) tx[i], (int) ty[i]);
            }

            // Save the image
            ImageIO.write(img, "PNG", new File("../img/dat_orbit.png"));

            // Clean up -- dispose the graphics context that was created.
            g.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // problem data
        // number of oservations
        int nres = tx.length;
        // observations
        //
        // number of parameter to fit
        int nvar = 1;

        // Initialize the model handle
        long handle = 0;
        int ifail = 0;

        E04RA e04ra = new E04RA();
        e04ra.eval(handle, nvar, ifail);

        handle = e04ra.getHANDLE();

        // Define a dense nonlinear least-squares objective function
        E04RM e04rm = new E04RM();
        ifail = 0;
        e04rm.eval(handle, nres, 0, 0, new int[] {}, new int[] {}, ifail);

        // Restrict parameter space (0 <= x)
        E04RH e04rh = new E04RH();
        double[] bl = new double[nvar];
        double[] bu = new double[nvar];
        Arrays.fill(bu, 1000.0);
        ifail = 0;
        e04rh.eval(handle, nvar, bl, bu, ifail);

        // Set some optional parameters to control the output of the solver
        E04ZM e04zm = new E04ZM();
        ifail = 0;

        e04zm.eval(handle, "Print Options = NO", ifail);
        e04zm.eval(handle, "Print Level = 1", ifail);
        e04zm.eval(handle, "Print Solution = X", ifail);
        e04zm.eval(handle, "Bxnl Iteration Limit = 100", ifail);

        // Define initial guess (starting point) Away from zero which is problematic
        double[] x = new double[nvar];
        Arrays.fill(x, 1.0);

        // Call the solver
        E04GG e04gg = new E04GG();
        LSQFUN lsqfun = new LSQFUN();
        LSQGRD lsqgrd = new LSQGRD();
        LSQHES lsqhes = new LSQHES();
        LSQHPRD lsqhprd = new LSQHPRD();
        MONIT monit = new MONIT();
        double[] rx = new double[nres];
        double[] rinfo = new double[100];
        double[] stats = new double[100];
        int[] iuser = new int[0];
        double[] ruser = tr;
        long cpuser = 0;
        ifail = 0;

        e04gg.eval(handle, lsqfun, lsqgrd, lsqhes, lsqhprd, monit, nvar, x, nres, rx, rinfo, stats, iuser, ruser,
                cpuser, ifail);

        // Optimal parameter values
        double rstar = x[0];
        System.out.printf("Optimal Orbit Height: %3.2f\n", rstar);

        try {
            // Open a JPEG file, load into a BufferedImage.
            BufferedImage img = ImageIO.read(new File("../img/dat_orbit.png"));

            // Obtain the Graphics2D context associated with the BufferedImage.
            Graphics2D g = img.createGraphics();

            // Set the Color to White
            g.setColor(Color.WHITE);

            // Draw the Circle around the points
            g.drawOval((int) (cc[0] - rstar), (int) (cc[1] - rstar), (int) (2 * rstar), (int) (2 * rstar));

            // Save the image
            ImageIO.write(img, "PNG", new File("../img/est_orbit.png"));

            // Clean up -- dispose the graphics context that was created.
            g.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add weights for each residual
        double[] weights = new double[] { 0.10, 0.98, 1.01, 1.00, 0.92, 0.97, 1.02 };
        double weights_sum = Arrays.stream(weights).sum();
        for (i = 0; i < weights.length; i++) {
            weights[i] /= weights_sum;
        }

        // Define the reliability of the measurements (weights)
        E04RX e04rx = new E04RX();
        ifail = 0;
        e04rx.eval(handle, "RW", 0, weights.length, weights, ifail);

        // Indicate to the solver that weights are to be used
        ifail = 0;
        e04zm.eval(handle, "Bxnl Use weights = Yes", ifail);

        // Define initial guess (starting point) Away from zero which is problematic
        Arrays.fill(x, 1.0);

        // Solve again
        e04gg.eval(handle, lsqfun, lsqgrd, lsqhes, lsqhprd, monit, nvar, x, nres, rx, rinfo, stats, iuser, ruser,
                cpuser, ifail);

        // Objective and solution
        rstar = x[0];
        System.out.printf("Optimal Orbit Height: %3.2f\n", rstar);

        try {
            // Open a JPEG file, load into a BufferedImage.
            BufferedImage img = ImageIO.read(new File("../img/dat_orbit.png"));

            // Obtain the Graphics2D context associated with the BufferedImage.
            Graphics2D g = img.createGraphics();

            // Set the Color to White
            g.setColor(Color.WHITE);

            // Draw the Circle around the points
            g.drawOval((int) (cc[0] - rstar), (int) (cc[1] - rstar), (int) (2 * rstar), (int) (2 * rstar));

            // Save the image
            ImageIO.write(img, "PNG", new File("../img/estw_orbit.png"));

            // Clean up -- dispose the graphics context that was created.
            g.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Optimal Orbit Height: %3.2f (should be between 244 and 256)\n", rstar);

        // Destroy the handle:
        E04RZ e04rz = new E04RZ();
        ifail = 0;
        e04rz.eval(handle, ifail);
    }

    // Define the least-square function and add first derivatives.
    /**
     * Objective function call back passed to the least squares solver. Return the
     * difference between the current estimated radius squared, r^2=x^2 and the
     * squared norm of the data point stored in tr[i] for i = 1 to nres: rx[i] = r^2
     * - tr[i], i = 1, ..., nres.
     */
    public static class LSQFUN extends E04GG.Abstract_E04GG_LSQFUN {
        public void eval() {
            for (int i = 0; i < this.NRES; i++) {
                this.RX[i] = Math.pow(this.X[0], 2) - this.RUSER[i];
            }
        }
    }

    /**
     * Computes the Jacobian of the least square residuals. Simply return rdx[i] =
     * 2r, i = 1, ..., nres.
     */
    public static class LSQGRD extends E04GG.Abstract_E04GG_LSQGRD {
        public void eval() {
            Arrays.fill(this.RDX, 2.0 * this.X[0]);
        }
    }

    public static class LSQHES extends E04GG.Abstract_E04GG_LSQHES {
        public void eval() {
            E04GGU e04ggu = new E04GGU();
            e04ggu.eval(this.NVAR, this.X, this.NRES, this.LAMBDA, this.HX, this.INFORM, this.IUSER, this.RUSER,
                    this.CPUSER);
            this.INFORM = e04ggu.getINFORM();
        }
    }

    public static class LSQHPRD extends E04GG.Abstract_E04GG_LSQHPRD {
        public void eval() {
            E04GGV e04ggv = new E04GGV();
            e04ggv.eval(this.NVAR, this.X, this.Y, this.NRES, this.HXY, this.INFORM, this.IUSER, this.RUSER,
                    this.CPUSER);
            this.INFORM = e04ggv.getINFORM();
        }
    }

    public static class MONIT extends E04GG.Abstract_E04GG_MONIT {
        public void eval() {
            E04FFU e04ffu = new E04FFU();
            e04ffu.eval(this.NVAR, this.X, this.INFORM, this.RINFO, this.STATS, this.IUSER, this.RUSER, this.CPUSER);
            this.INFORM = e04ffu.getINFORM();
        }
    }

    private static void drawPoint(Graphics2D g, int x, int y) {
        int width = 3;
        int length = 21;
        g.fillRect(x - (width / 2), y - (length / 2), width, length);
        g.fillRect(x - (length / 2), y - (width / 2), length, width);
    }

}
