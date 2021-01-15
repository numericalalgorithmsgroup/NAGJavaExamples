import com.nag.routines.M01.M01CC;
import java.io.*;

/**
 * M01CC example program text.
 */
public class M01CCJE {

  public static void main(String[] args) throws Exception {

    String[] ch;
    String order = "Reverse ASCII";
    int m1,m2,l1,l2;
    int ifail = 0;

    BufferedReader dataIn = new BufferedReader(new FileReader(args[0]));

    dataIn.readLine();
    m2 = Integer.parseInt(dataIn.readLine().trim().split(":+")[0].trim());
    ch = new String[m2];
    m1 = 1;
    for (int i = m1-1; i < m2; ++i) {
      ch[i] = dataIn.readLine();
    }
    dataIn.close();
    l1 = 7;
    l2 = 12;

    System.out.println(" M01CCJ Example Program Results");
    M01CC m01cc = new M01CC(ch, m1, m2, l1, l2, order, ifail);
    m01cc.eval();
    ifail = m01cc.getIFAIL();
    System.out.println();
    System.out.printf(" Records sorted on columns %2d to %2d\n",l1,l2);
    System.out.println();
    for (int i = m1-1; i < m2; ++i) {
      System.out.println(" "+ch[i]);
    }

  }

}
