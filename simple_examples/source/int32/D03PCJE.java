import com.nag.routines.D03.D03PC;
import com.nag.routines.D03.D03PZ;
import com.nag.routines.X01.X01AA;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.StringBuilder;

/**
 * D03PCJ example program text
 * @author willa
 * @since 27.1.0.0
 */
public class D03PCJE{

  /**
   * D03PCJE main program
   */
  public static void main(String[] args){
    int ifail, ind, intpts = 0, it, itask, itrace = 0, itype = 0, lisave, lrsave, m = 0, neqn, npts = 0, nwk, npde = 2;
    double hx, pi, piby2, tout = 0, ts = 0, acc = 0, alpha = 0;
    int[] isave, iuser, iwsav;
    double[] rsave, u, uout, x, xout, ruser, rwsav;
    boolean[] lwsav;
    String[] cwsav;

    xout = new double[0]; // placeholders
    ruser = new double[1];
    rwsav = new double[1100];
    iuser = new int[1];
    iwsav = new int[505];
    lwsav = new boolean[100];
    cwsav = new String[10];
    
    System.out.println("D03PCJ Example Program Results");

    //Specify path to data file
    if(args.length != 1){
      System.err.println("Please specify the path to the data file.");
      System.exit(-1);
    }

    String filename = args[0];
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine(); //skip header

      line = reader.readLine();
      String[] sVal = line.split("\\s+");
      intpts = Integer.parseInt(sVal[0]);
      npts = Integer.parseInt(sVal[1]);
      itype = Integer.parseInt(sVal[2]);

      xout = new double[intpts];
      line = reader.readLine(); 
      sVal = line.split("\\s+");
      for(int i = 0; i < intpts; i++){
        xout[i] = Double.parseDouble(sVal[i]);
      }

      line = reader.readLine();
      sVal = line.split("\\s+");
      acc = Double.parseDouble(sVal[0]);
      alpha = Double.parseDouble(sVal[1]);

      line = reader.readLine();
      sVal = line.split("\\s+");
      m = Integer.parseInt(sVal[0]);
      itrace = Integer.parseInt(sVal[1]);

      line = reader.readLine();
      sVal = line.split("\\s+");
      ts = Double.parseDouble(sVal[0]);
      tout = Double.parseDouble(sVal[1]);
    }
    catch(FileNotFoundException e){
      System.err.println("***FATAL: Can't find " + filename);
      System.exit(-2);
    }
    catch(IOException e){
      System.err.println("***FATAL: Can't read " + filename + "\n" + e.getMessage());
    }

    neqn = npde * npts;
    lisave = neqn + 24;
    nwk = (10 + (6 * npde)) * neqn;
    lrsave = nwk + ((21 + (3 * npde)) * npde) + (7 * npts) + 54;

    rsave = new double[lrsave];
    u = new double[npde * npts];
    uout = new double[npde * intpts * itype];
    x = new double[npts];
    isave = new int[lisave];
    ruser[0] = alpha;
    ind = 0;
    itask = 1;

    X01AA x01aa = new X01AA();
    pi = x01aa.eval();
    piby2 = 0.5 * pi;
    hx = piby2/(double)(npts - 1);
    x[0] = 0.0;
    x[npts - 1] = 1.0;
    for(int i = 1; i < (npts - 1); i++){
      x[i] = Math.sin(hx * (double)(i));
    }

    u = uinit(x, npts, iuser, ruser);

    //Character (80)     :: cwsav(10)
    //Surely a better way of doing this?
    for(int i = 0; i < 10; i++){
      StringBuilder builder = new StringBuilder("");
      for(int j = 0; j < 80; j++){
        builder.append(" ");
      }
      cwsav[i] = builder.toString(); 
    }

    for(int i = 0; i <5; i++){
      tout = 10 * tout;

      ifail = 0;
      pdedef pdedef1 = new pdedef();
      bndary bndary1 = new bndary();
      D03PC d03pc = new D03PC(npde, m, ts, tout, pdedef1, bndary1, u, npts, x, acc, rsave, lrsave,
                              isave, lisave, itask, itrace, ind, iuser, ruser, cwsav, lwsav, iwsav,
                              rwsav, ifail);
      d03pc.eval();

      //update ind
      ind = d03pc.getIND();

      if(i == 0){
        System.out.printf("Accuracy requirement = \t%.5e\n Parameter ALPHA = \t%.3e\n", acc, alpha);
        System.out.printf("T  /  X ");
        for(int j = 0; j < xout.length; j++){
          System.out.printf("\t%.4f", xout[j]);
        }
        System.out.printf("\n");
      }
      System.out.println();

      //Interpolate at required spatial points
      ifail = 0;

      D03PZ d03pz = new D03PZ(npde, m, u, npts, x, xout, intpts, itype, uout, ifail);
      d03pz.eval();

      System.out.printf("%.4f \tU(1)", tout);
      for(int j = 0; j < intpts; j++){
        System.out.printf("\t%.4f ", uout[j * 2]);
      }
      System.out.printf("\n");
      System.out.printf("\tU(2)");
      for(int j = 0; j < intpts; j++){
        System.out.printf("\t%.4f ", uout[j * 2 + 1]);
      }
      System.out.printf("\n");
      System.out.println();
    }

    System.out.printf("Number of Integration steps in time\t\t\t%d\n", isave[0]);
    System.out.printf("Number of residual evaluations of resulting ODE system\t%d\n", isave[1]);
    System.out.printf("Number of Jacobian evaluations\t\t\t\t%d\n", isave[2]);
    System.out.printf("Number of iterations of nonlinear solver\t\t%d\n", isave[4]);
  }

  /**
   * PDE initial condition
   */
  public static double[] uinit(double[] x, int npts, int[] iuser, double[] ruser){
    double alpha = ruser[0]; 
    double[] u = new double[2 * npts]; 
    for(int i = 0; i < npts; i++){
      u[2 * i] = 2 * alpha * x[i];
      u[(2 * i) + 1] = 1;
    }
    return u;
  }

  public static class pdedef extends D03PC.Abstract_D03PC_PDEDEF{
    public void eval(){
      double alpha = this.RUSER[0];
      this.Q[0] = 4 * alpha * (this.U[1] + (this.X * this.UX[1]));
      this.Q[1] = 0;
      this.R[0] = this.X * this.UX[0];
      this.R[1] = this.UX[1] - (this.U[0] * this.U[1]);
      this.P[0] = 0;
      this.P[1] = 0;
      this.P[2] = 0;
      this.P[3] = 1 - (this.X * this.X);
    }
  }

  public static class bndary extends D03PC.Abstract_D03PC_BNDARY{
    public void eval(){
      if(this.IBND == 0){
        this.BETA[0] = 0;
        this.BETA[1] = 1;
        this.GAMMA[0] = this.U[0];
        this.GAMMA[1] = -this.U[0] * this.U[1];
      }
      else{
        this.BETA[0] = 1;
        this.BETA[1] = 0;
        this.GAMMA[0] = -this.U[0];
        this.GAMMA[1] = this.U[1];
      }
    }
  }
          
}

  
      
