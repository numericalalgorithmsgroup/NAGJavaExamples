import com.nag.routines.E04.E04NK;
import com.nag.routines.E04.E04WB;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException; 
import java.util.Arrays;

/**
 * E04NKJ Example Program Text
 * @author willa
 * @since 27.1.0.0
 */
public class E04NKJE{
  private static int lcwsav = 1;
  private static int liwsav = 380;
  private static int llwsav = 20;
  private static int lrwsav = 285;

  /** 
   * E04NKJ Example main program.
   */
  public static void main(String[] args){
    double obj, sinf;
    int icol, ifail, iobj, jcol, leniz, lenz, m, miniz, minz, n, ncolh, ninf, nname, nnz, ns, i;
    String start;
    double[] a, bl, bu, clamda, xs, z, ruser, rwsav;
    int[] ha, istate, iz, ka, iuser, iwsav;
    boolean[] lwsav;
    String[] crname, cwsav, names;

    System.out.println("E04NKJ Example Program Results");
    
    ruser = new double[1];
    rwsav = new double[lrwsav];
    iuser = new int[1];
    iwsav = new int[liwsav];

    cwsav = new String[lcwsav];
    //equivalent to ```Character (80)                   :: cwsav(lcwsav)```
    for(i = 0; i < lcwsav; i++){
      char[] chars = new char[80];
      Arrays.fill(chars, ' ');
      cwsav[i] = new String(chars);
    }
    names = new String[5];
    //names all blank anyway this time
    for(i = 0; i < 5; i++){
      char[] chars = new char[8];
      Arrays.fill(chars, ' ');
      names[i] = new String(chars);
    }

    //dummy arrays
    crname = new String[0];

    n = 7;
    m = 8;
    nnz = 48;
    iobj = 8;
    ncolh = 7;
    start = "C";
    nname = 15;

    ha = new int[nnz];
    ka = new int[n + 1];
    istate = new int[n + m];
    a = new double[nnz];
    bl = new double[n + m];
    bu = new double[n + m];
    xs = new double[n + m];
    clamda = new double[n + m];
    crname = new String[nname];
    lwsav = new boolean[llwsav];

    names[0] = "        ";
    names[1] = "        ";
    names[2] = "        ";
    names[3] = "        ";
    names[4] = "        ";

    crname[0] = "...X1...";
    crname[1] = "...X2...";
    crname[2] = "...X3...";
    crname[3] = "...X4...";
    crname[4] = "...X5...";
    crname[5] = "...X6...";
    crname[6] = "...X7...";
    crname[7] = "..ROW1..";
    crname[8] = "..ROW2..";
    crname[9] = "..ROW3..";
    crname[10] = "..ROW4..";
    crname[11] = "..ROW5..";
    crname[12] = "..ROW6..";
    crname[13] = "..ROW7..";
    crname[14] = "..COST..";

    //Matrix A. Set up KA
    jcol = 1;
    ka[0] = 1;
    
    a[0] = 0.02; ha[0] = 7;
    a[1] = 0.02; ha[1] = 5;
    a[2] = 0.03; ha[2] = 3;
    a[3] = 1.00; ha[3] = 1;
    a[4] = 0.70; ha[4] = 6;
    a[5] = 0.02; ha[5] = 4;
    a[6] = 0.15; ha[6] = 2;
    a[7] = -200.00; ha[7] = 8;
    a[8] = 0.06; ha[8] = 7;
    a[9] = 0.75; ha[9] = 6;
    a[10] = 0.03; ha[10] = 5;
    a[11] = 0.04; ha[11] = 4;
    a[12] = 0.05; ha[12] = 3;
    a[13] = 0.04; ha[13] = 2;
    a[14] = 1.00; ha[14] = 1;
    a[15] = -2000.00; ha[15] = 8;
    a[16] = 0.02; ha[16] = 2;
    a[17] = 1.00; ha[17] = 1;
    a[18] = 0.01; ha[18] = 4;
    a[19] = 0.08; ha[19] = 3;
    a[20] = 0.08; ha[20] = 7;
    a[21] = 0.80; ha[21] = 6;
    a[22] = -2000.00; ha[22] = 8;
    a[23] = 1.00; ha[23] = 1;
    a[24] = 0.12; ha[24] = 7;
    a[25] = 0.02; ha[25] = 3;
    a[26] = 0.02; ha[26] = 4;
    a[27] = 0.75; ha[27] = 6;
    a[28] = 0.04; ha[28] = 2;
    a[29] = -2000.00; ha[29] = 8;
    a[30] = 0.01; ha[30] = 5;
    a[31] = 0.80; ha[31] = 6;
    a[32] = 0.02; ha[32] = 7;
    a[33] = 1.00; ha[33] = 1;
    a[34] = 0.02; ha[34] = 2;
    a[35] = 0.06; ha[35] = 3;
    a[36] = 0.02; ha[36] = 4;
    a[37] = -2000.00; ha[37] = 8;
    a[38] = 1.00; ha[38] = 1;
    a[39] = 0.01; ha[39] = 2;
    a[40] = 0.01; ha[40] = 3;
    a[41] = 0.97; ha[41] = 6;
    a[42] = 0.01; ha[42] = 7;
    a[43] = 400.00; ha[43] = 8;
    a[44] = 0.97; ha[44] = 7;
    a[45] = 0.03; ha[45] = 2;
    a[46] = 1.00; ha[46] = 1;
    a[47] = 400.00; ha[47] = 8;

    ka[1] = 9;
    ka[2] = 17;
    ka[3] = 24;
    ka[4] = 31;
    ka[5] = 39;
    ka[6] = 45;
    ka[7] = 49;

    bl[0] = 0.0;
    bl[1] = 0.0;
    bl[2] = 4.0e2;
    bl[3] = 1.0e2;
    bl[4] = 0.0;
    bl[5] = 0.0;
    bl[6] = 0.0;
    bl[7] = 2.0e3;
    bl[8] = -1.0e25;
    bl[9] = -1.0e25;
    bl[10] = -1.0e25;
    bl[11] = -1.0e25;
    bl[12] = 1.5e3;
    bl[13] = 2.5e2;
    bl[14] = -1.0e25;

    bu[0] = 2.0e2;
    bu[1] = 2.5e3;
    bu[2] = 8.0e2;
    bu[3] = 7.0e2;
    bu[4] = 1.5e3;
    bu[5] = 1.0e25;
    bu[6] = 1.0e25;
    bu[7] = 2.0e3;
    bu[8] = 6.0e1;
    bu[9] = 1.0e2;
    bu[10] = 4.0e1;
    bu[11] = 3.0e1;
    bu[12] = 1.0e25;
    bu[13] = 3.0e2;
    bu[14] = 1.0e25;

    istate[0] = 0;
    istate[1] = 0;
    istate[2] = 0;
    istate[3] = 0;
    istate[4] = 0;
    istate[5] = 0;
    istate[6] = 0;
    istate[7] = 0;

    xs[0] = 0.0;
    xs[1] = 0.0;
    xs[2] = 0.0;
    xs[3] = 0.0;
    xs[4] = 0.0;
    xs[5] = 0.0;
    xs[6] = 0.0;
    xs[7] = 0.0;

    ifail = 0;
    E04WB e04wb = new E04WB("E04NKA",cwsav,lcwsav,lwsav,llwsav,iwsav,liwsav,rwsav,
                            lrwsav,ifail);
    e04wb.eval();

    leniz = 1;
    lenz = 1;
    iz = new int[leniz];
    z = new double[lenz];

    QPHX qphx = new QPHX();
    
    ifail = 1;

    //placeholders
    ns = 0;
    miniz = 0;
    minz = 0;
    ninf = 0;
    sinf = 0.0;
    obj = 0.0;
   
    E04NK e04nk = new E04NK(n,m,nnz,iobj,ncolh,qphx,a,ha,ka,bl,bu,start,names,nname,
                            crname,ns,xs,istate,miniz,minz,ninf,sinf,obj,clamda,iz,
                            leniz,z,lenz,iuser,ruser,lwsav,iwsav,rwsav,ifail);
    e04nk.eval();
    ifail = e04nk.getIFAIL();

    minz = e04nk.getMINZ();
    miniz = e04nk.getMINIZ();
    
    lenz = minz;
    leniz = miniz;

    iz = new int[leniz];
    z = new double[lenz];

    ifail = -1;

    e04nk.eval(n,m,nnz,iobj,ncolh,qphx,a,ha,ka,bl,bu,start,names,nname,crname,ns,xs,
               istate,miniz,minz,ninf,sinf,obj,clamda,iz,leniz,z,lenz,iuser,ruser,
               lwsav,iwsav,rwsav,ifail);

    //update
    ifail = e04nk.getIFAIL();
    obj = e04nk.getOBJ();

    if(ifail == 0){
      System.out.println();
      System.out.println("\tVariable     Istate     Value     Lagr Mult");
      System.out.println();

      for(i = 0; i < n; i++){
        System.out.printf("\t%s\t%d\t%.6g\t%12.4g\n", crname[i], istate[i], xs[i], clamda[i]);
      }

      if(m > 0){
        System.out.println();
        System.out.println();
        System.out.println("\tConstrnt     Istate     Value\t\t  Lagr Mult");
        System.out.println();

        for(i = n; i < n + m - 1; i++){
          System.out.printf("\t%s\t%d\t%.6g\t\t%12.4g\n", crname[i], istate[i], xs[i], clamda[i]);
        }
        System.out.printf("\t%s\t%d\t%.6g\t%12.4g\n", crname[i], istate[i], xs[i], clamda[i]);
      }
      
      System.out.println();
      System.out.println();
      System.out.printf("Final objetive value = %15.7g\n", obj);
    }
    
  }

  public static class QPHX extends E04NK.Abstract_E04NK_QPHX{
    public void eval(){
      this.HX[0] = 2 * this.X[0];
      this.HX[1] = 2 * this.X[1];
      this.HX[2] = 2 * (this.X[2] + this.X[3]);
      this.HX[3] = this.HX[2];
      this.HX[4] = 2 * this.X[4];
      this.HX[5] = 2 * (this.X[5] + this.X[6]);
      this.HX[6] = this.HX[5];
    }
  }
}
