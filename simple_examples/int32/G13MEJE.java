import com.nag.routines.G13.G13ME;

/**
 * G13MEJ Example program text
 * @author willa
 * @since 27.1.0.0
 */
public class G13MEJE{

  /**
   * G13MEJ main program
   */
  public static void main(String[] args){
    double tau;
    int ifail, lrcomm, m, nb, pn;
    double[] iema, rcomm, sinit, t;
    int[] inter, nbVal;

    inter = new int[2];

    System.out.println("G13MEJ Example Program Results");
    System.out.println();

    //data (can read in from data file)
    //Number of iteration required
    m = 2;

    //Decay parameter and interpolation method
    tau = 2;
    inter[0] = 3;
    inter[1] = 2;

    //Initial values
    sinit = new double[m + 2];
    sinit[0] = 5;
    sinit[1] = 0.5;
    sinit[2] = 0.5;
    sinit[3] = 0.5;
    
    //Array of NB values in order
    nbVal = new int[3];
    nbVal[0] = 5;
    nbVal[1] = 10;
    nbVal[2] = 15;

    //First block
    double[] t1 = new double[nbVal[0]];
    double[] iema1 = new double[nbVal[0]];
    t1[0] = 7.5;
    t1[1] = 8.2;
    t1[2] = 18.1;
    t1[3] = 22.8;
    t1[4] = 25.8;
    iema1[0] = 0.6;
    iema1[1] = 0.6;
    iema1[2] = 0.8;
    iema1[3] = 0.1;
    iema1[4] = 0.2;

    //Second block
    double[] t2 = new double[nbVal[1]];
    double[] iema2 = new double[nbVal[1]];
    t2[0] = 26.8;
    t2[1] = 31.1;
    t2[2] = 38.4;
    t2[3] = 45.9;
    t2[4] = 48.2;
    t2[5] = 48.9;
    t2[6] = 57.9;
    t2[7] = 58.5;
    t2[8] = 63.9;
    t2[9] = 65.2;
    iema2[0] = 0.2;
    iema2[1] = 0.5;
    iema2[2] = 0.7;
    iema2[3] = 0.1;
    iema2[4] = 0.4;
    iema2[5] = 0.7;
    iema2[6] = 0.8;
    iema2[7] = 0.3;
    iema2[8] = 0.2;
    iema2[9] = 0.5;

    //Third block
    double[] t3 = new double[nbVal[2]];
    double[] iema3 = new double[nbVal[2]];
    t3[0] = 66.6;
    t3[1] = 67.4;
    t3[2] = 69.3;
    t3[3] = 69.9;
    t3[4] = 73.0;
    t3[5] = 75.6;
    t3[6] = 77.0;
    t3[7] = 84.7;
    t3[8] = 86.8;
    t3[9] = 88.0;
    t3[10] = 88.5;
    t3[11] = 91.0;
    t3[12] = 93.0;
    t3[13] = 93.7;
    t3[14] = 94.0;
    iema3[0] = 0.2;
    iema3[1] = 0.3;
    iema3[2] = 0.8;
    iema3[3] = 0.6;
    iema3[4] = 0.1;
    iema3[5] = 0.7;
    iema3[6] = 0.9;
    iema3[7] = 0.6;
    iema3[8] = 0.3;
    iema3[9] = 0.1;
    iema3[10] = 0.1;
    iema3[11] = 0.4;
    iema3[12] = 1.0;
    iema3[13] = 1.0;
    iema3[14] = 0.1;

    //Print some titles
    System.out.printf("\t\t\tIterated\n");
    System.out.println("\t\tTime\t   EMA\n");
    System.out.println("--------------------------------");
    
    lrcomm = 20 + m;
    rcomm = new double[lrcomm];

    pn = 0;

    //Loop through 3 blocks
    for(int i = 0; i < nbVal.length; i++){
      //nb for given block
      nb = nbVal[i];

      //Use data for current block
      if(i == 0){
        t = t1;
        iema = iema1;
      }
      else if(i == 1){
        t = t2;
        iema = iema2;
      }
      else{
        t = t3;
        iema = iema3;
      }
      
      ifail = 0;
      G13ME g13me = new G13ME(nb, iema, t, tau, m, sinit, inter, pn, rcomm, lrcomm, ifail);
      g13me.eval();
      pn = g13me.getPN();

      //Display results for this block of data
      for(int j = 0; j < nb; j++){
        System.out.printf("\t%d\t%.1f\t%.3f\n", pn - nb + (j + 1), t[j], iema[j]);
      }
      System.out.println();
    }
  }
}

