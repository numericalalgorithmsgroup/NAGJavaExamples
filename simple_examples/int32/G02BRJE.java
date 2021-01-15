import com.nag.routines.G02.G02BR;

/**
 * G02BRJ Example Program Text
 * @author willa
 * @since 27.1.0.0
 */
public class G02BRJE{

  /**
   * G02BRJ Example main program
   */
  public static void main(String[] args){
    int ifail, itype, ldrr, ldx, m, n, ncases = 0; //placeholder
    double[] rr, work1, work2, x, xmiss;
    int[] incase, kworka, kworkb, kworkc, miss;

    System.out.println("G02BRJ Example Program Results");
    System.out.println();

    //Problem size
    n = 9;
    m = 3;
    itype = 0;

    ldrr = m;
    ldx = n;

    //Allocate
    rr = new double[ldrr * m];
    work1 = new double[n];
    work2 = new double[n];
    x = new double[ldx * m];
    xmiss = new double[m];
    incase = new int[n];
    kworka = new int[n];
    kworkb = new int[n];
    kworkc = new int[n];
    miss = new int[m];

    //Data
    //X = (1.70, 1.00, 0.50)
    //    (2.80, 4.00, 3.00)
    //    (0.60, 6.00, 2.50)
    //    (1.80, 9.00, 6.00)
    //    (0.99, 4.00, 2.50)
    //    (1.40, 2.00, 5.50)
    //    (1.80, 9.00, 7.50)
    //    (2.50, 7.00, 0.00)
    //    (0.99, 5.00, 3.00)
    x[0] = 1.7;
    x[1] = 2.8;
    x[2] = 0.6;
    x[3] = 1.8;
    x[4] = 0.99;
    x[5] = 1.4;
    x[6] = 1.8;
    x[7] = 2.5;
    x[8] = 0.99;
    x[9] = 1;
    x[10] = 4;
    x[11] = 6;
    x[12] = 9;
    x[13] = 4;
    x[14] = 2;
    x[15] = 9;
    x[16] = 7;
    x[17] = 5;
    x[18] = 0.5;
    x[19] = 3;
    x[20] = 2.5;
    x[21] = 6;
    x[22] = 2.5;
    x[23] = 5.5;
    x[24] = 7.5;
    x[25] = 0;
    x[26] = 3;

    //Missing value flags
    miss[0] = 1;
    miss[1] = 0;
    miss[2] = 1;
    xmiss[0] = 0.99;
    xmiss[1] = 0;
    xmiss[2] = 0;

    //Display data
    System.out.printf("Number of variables (columns) = %d\n", m);
    System.out.printf("Number of case      (rows)    = %d\n", n);
    System.out.println();
    System.out.println("Data matrix is:-\n");
    System.out.println();
    for(int i = 0; i < m; i++){
      System.out.printf("\t  %d", i + 1);
    }
    System.out.println();
    for(int i = 0; i < n; i++){
      System.out.printf("  %d\t", i + 1);
      for(int j = 0; j < m; j++){
        System.out.printf("%.4f\t", x[(j * n) + i]);
      }
      System.out.println();
    }
    System.out.println();

    //Calculate correlation coefficients
    ifail = 0;
    G02BR g02br = new G02BR(n, m, x, ldx, miss, xmiss, itype, rr, ldrr, ncases, incase, kworka, kworkb, kworkc,
                            work1, work2, ifail);
    g02br.eval();

    //Update
    ncases = g02br.getNCASES();

    //Display results
    System.out.println("Matrix of rank correlation coefficients:");
    System.out.println("Upper triangle -- Spearman's");
    System.out.println("Lower triangle -- Kendall's tau");
    System.out.println();
    for(int i = 0; i < m; i++){
      System.out.printf("\t  %d", i + 1);
    }
    System.out.println();
    for(int i = 0; i < m; i++){
      System.out.printf("  %d\t", i + 1);
      for(int j = 0; j < m; j++){
        System.out.printf("%.4f\t", rr[(j * m) + i]);
      }
      System.out.println();
    }
    System.out.println();
    System.out.printf("Number of cases actually used: %d\n", ncases);
  }
}
