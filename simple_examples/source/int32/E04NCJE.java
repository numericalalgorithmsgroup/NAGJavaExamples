import static java.lang.Math.max;

import com.nag.routines.E04.E04NC;
import com.nag.routines.E04.E04WB;
import com.nag.routines.F06.DGEMV;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * E04NC example program text.
 * @author ludovic
 */
public class E04NCJE {

  public static void main(String[] args) {

    double one = 1.0;
    double zero = 0.0;
    int inc1 = 1, lcwsav = 1, liwsav = 610,
        llwsav = 120, lrwsav = 475, nin = 5,
        nout = 6;
    double obj;
    int i, ifail, iter, lda, ldc, liwork,
        lwork, m, n, nclin, sdc;
    double[] a, b, bl, bu, c,
        clamda, cvec, work, x;
    double[] rwsav = new double[lrwsav];
    int[] istate, iwork, kx;
    int[] iwsav = new int[liwsav];
    boolean[] lwsav = new boolean[llwsav];
    String[] cwsav = new String[lcwsav];

    System.out.println(" E04NCJ Example Program Results");
    try {

      BufferedReader br = new BufferedReader(new FileReader(args[0]));
      String line = br.readLine(); // read the header
      line = br.readLine().trim();
      line.replaceAll("\\s+", " ");
      String[] data = line.split("\\s+");
      m = Integer.parseInt(data[0]);
      n = Integer.parseInt(data[1]);
      nclin = Integer.parseInt(data[2]);

      liwork = n;
      ldc = max(1,nclin);
      lda = max(1,m);

      if (nclin > 0) {
        sdc = n;
      }
      else {
        sdc = 1;
      }

      /* This particular example problem is of type LS1, so we allocate
       *  A(LDA,N), CVEC(1), B(M) and define LWORK as below
       */
      if (nclin > 0) {
        lwork = 2*n*n + 9*n + 6*nclin;
      }
      else {
        lwork = 9*n;
      }

      /*
       * 2Ds arrays (size [n,m]) are stored in 1D arrays of size n*m, ordered by column
       * ie:
       *  1 1 1
       *  2 2 2
       *  3 3 3
       *
       * is stored [1,2,3,1,2,3,1,2,3]
       */
      istate = new int[n+nclin];
      kx = new int[n];
      iwork = new int[liwork];
      c = new double[ldc*sdc];
      bl = new double[n+nclin];
      bu = new double[n+nclin];
      cvec = new double[1];
      x = new double[n];
      a = new double[lda * n];
      b = new double[m];
      clamda = new double[n+nclin];
      work = new double[lwork];

      /*
       * Extra arrays to initialize:
       */
      for (int ii = 0; ii < cwsav.length; ++ii) {
        // 80 characters long...
        cwsav[ii]
            = "                                                                              ";
      }

      for (int ii = 0; ii < lda; ++ii) {
        line = br.readLine().trim();
        line.replaceAll("\\s+", " ");
        data = line.split("\\s+");
        for (int jj = 0; jj < n; ++jj) {
          a[lda*jj+ii] = Double.parseDouble(data[jj]);
        }
      }

      line = br.readLine().trim();
      line.replaceAll("\\s+", " ");
      data = line.split("\\s+");

      for (int ii = 0; ii < m; ++ii) {
        b[ii] = Double.parseDouble(data[ii]);
      }

      for (int ii = 0; ii < ldc; ++ii) {
        line = br.readLine().trim();
        line.replaceAll("\\s+", " ");
        data = line.split("\\s+");
        for (int jj = 0; jj < sdc; ++jj) {
          c[ldc*jj+ii] = Double.parseDouble(data[jj]);
        }
      }

      //bl and bu to complicated to read properly -> hardcoded
      line = br.readLine();
      line = br.readLine();
      bl[0] = 0.0;
      bl[1] = 0.0;
      bl[2] = -1.0E+25;
      bl[3] = 0.0;
      bl[4] = 0.0;
      bl[5] = 0.0;
      bl[6] = 0.0;
      bl[7] = 0.0;
      bl[8] = 0.0;
      bl[9] = 2.0;
      bl[10] = -1.0E+25;
      bl[11] = 1.0;

      line = br.readLine();
      line = br.readLine();
      bu[0] = 2.0;
      bu[1] = 2.0;
      bu[2] = 2.0;
      bu[3] = 2.0;
      bu[4] = 2.0;
      bu[5] = 2.0;
      bu[6] = 2.0;
      bu[7] = 2.0;
      bu[8] = 2.0;
      bu[9] = 1.0E+25;
      bu[10] = 2.0;
      bu[11] = 4.0;

      line = br.readLine().trim();
      line.replaceAll("\\s+", " ");
      data = line.split("\\s+");
      for (int ii = 0; ii < n; ++ii) {
        x[ii] = Double.parseDouble(data[ii]);
      }

      // CALL TO E04WBF
      ifail = 0;
      String routname = "E04NCA";
      E04WB e04wb = new E04WB(routname, cwsav, lcwsav, lwsav, llwsav, iwsav, liwsav, rwsav,
          lrwsav, ifail);
      e04wb.eval();
      cwsav = e04wb.getCWSAV();
      iwsav = e04wb.getIWSAV();
      lwsav = e04wb.getLWSAV();
      rwsav = e04wb.getRWSAV();

      // CALL TO E04NCF
      ifail = -1;

      /* Java needs these to be initialized, so set it to an impossible value
       * to trap possible error later */
      iter = Integer.MIN_VALUE;
      obj = Double.NaN;

      E04NC e04nc = new E04NC(m,n,nclin,ldc,lda,c,bl,bu,cvec,istate,kx,x,a,b,iter,obj,
          clamda,iwork,liwork,work,lwork,lwsav,iwsav,rwsav,ifail);
      e04nc.eval();

      // Getting ifail's value back
      ifail = e04nc.getIFAIL();

      switch (ifail) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 7:
          System.out.println("\n Varbl  Istate   Value         Lagr Mult\n");

          for (i = 0; i < n; ++i) {
            System.out.printf(" V %3d %3d    %14.3E  %12.3E\n",(i+1), istate[i],x[i], clamda[i]);
          }

          if (nclin > 0) {
            DGEMV dgemv = new DGEMV();
            dgemv.eval("N",nclin,n,one,c,ldc,x,inc1,zero,work,inc1);
            System.out.println("\n\n L Con  Istate   Value         Lagr Mult\n");
            for (int ii = 0; ii < nclin; ++ii) {
              System.out.printf(" L %3d %3d    %14.3E  %12.3E\n",
                  ii+1,istate[ii+n],work[ii],clamda[ii+n]);
            }
          }

          System.out.printf("\n\n Final objective value = %15.3E\n",e04nc.getOBJ());
          break;
        default:
          if (ifail > 7) {
            System.out.println(" Varbl  Istate   Value         Lagr Mult");
            for (i = 0; i < n; ++i) {
              System.out.printf(" V %3d %3d    %14.3E  %12.3E)\n",(i+1), istate[i],x[i], clamda[i]);
            }

            if (nclin > 0) {
              DGEMV dgemv = new DGEMV();
              dgemv.eval("N",nclin,n,one,c,ldc,x,inc1,zero,work,inc1);
              System.out.println(" L Con  Istate   Value         Lagr Mult");
              for (int ii = 0; ii < nclin; ++ii) {
                System.out.printf(" L %3d %3d    %14.3E  %12.3E\n",
                    ii+1,istate[ii+n],work[ii],clamda[ii+n]);
              }
            }

            System.out.printf("Final objective value is: %15.7f\n",e04nc.getOBJ());
          }
          else {
            System.out.println("E04NC returned with IFAIL = "+e04nc.getIFAIL());
          }
      }
    }
    catch (Exception ex) {
      Logger.getLogger(E04NCJE.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

}
