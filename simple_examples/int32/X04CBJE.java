import com.nag.routines.X04.X04CB;

/**
 * X04CBJ example program text
 * @author willa
 * @since 27.1.0.0
 */ 
public class X04CBJE{

  /**
   * X04CBJ example main program
   */
  public static void main(String[] args){
    int nmax = 5, lda = nmax, ifail, indent, ncols;
    double[] a;

    a = new double[lda * nmax];

    String[] clabs = {"Un     ", "Deux   ", "Trois  ", "Quatre ", "Cinq   "};
    String[] rlabs = {"Uno    ", "Due    ", "Tre    ", "Quattro", "Cinque "};

    System.out.println("X04CBJ Example Program Results");
    System.out.println();

    //generate an array of data
    for(int i = 0; i < nmax; i++){
      for(int j = 0; j < lda; j++){
        a[(i * lda) + j] = (double) ((10 * (j + 1)) + i + 1);
      }
    }

    ncols = 80;
    indent = 0;

    //Print 3 by nmax rectangular matrix with default format and integer row and column labels
    ifail = 0;
    X04CB x04cb = new X04CB("General", " ", 3, nmax, a, lda, " ", "Example 1", "Integer", rlabs, "Integer",
                            clabs, ncols, indent, ifail);
    x04cb.eval();
    System.out.println();

    //Print nmax by namx upper triangular matrix with user-supplied format and row and column labels
    ifail = 0;
    x04cb.eval("Upper", "Non-unit", nmax, nmax, a, lda, "F8.2", "Example 2:", "Character", rlabs, "Character",
               clabs, ncols, indent, ifail);
    System.out.println();

    //Print 3 by nmax lower triangular matrix in MATLAB format
    //Row and column labelling is ignored
    ifail = 0;
    x04cb.eval("Lower", "Non-unit", 3, nmax, a, lda, "MATLABF8.2", "A", " ", rlabs, " ", clabs, ncols, indent, ifail);
  }
}
