import com.nag.routines.E04.E04RA;
import com.nag.routines.E04.E04RE;
import com.nag.routines.E04.E04RN;
import com.nag.routines.E04.E04RP;
import com.nag.routines.E04.E04RY;
import com.nag.routines.E04.E04RZ;
import com.nag.routines.E04.E04SV;

/**
 * E04RPJ Example Program Text
 * @author willa
 * @since 27.1.0.0
 */

public class E04RPJE{

  /**
   * E04RPJ Example main program
   */
  public static void main(String[] args){
    int blkidx, dimaq, idblk, idlc, idx, idxend, ifail, inform, midx, nblk, nclin,
      nnzasum, nnzb, nnzc, nnzh, nnzqsum, nnzu, nnzua, nnzuc, nq, nvar;
    double[] a, b, bl, bu, cvec, h, q, x, rdummy, rinfo, stats;
    int[] icola, icolb, icolh, icolq, idxc, irowa, irowb, irowh, irowq, nnza,
      nnzq, qi, qj, idummy;
    long cpuser, handle;

    rdummy = new double[1];
    rinfo = new double[32];
    stats = new double[32];
    idummy = new int[1];

    System.out.println("E04RPJ Example Program Results");
    System.out.println();

    //Problem size
    nvar = 5;
    nnzh = 0;
    nclin = 0;
    nnzb = 0;
    nblk = 2;

    //Initialize handle to an empty problem
    E04RA e04ra = new E04RA();
    handle = 0;
    ifail = 0;
    e04ra.eval(handle, nvar, ifail);
    handle = e04ra.getHANDLE();

    //Linear part of the objective function
    cvec = new double[nvar];
    cvec[0] = 1;
    cvec[1] = 0;
    cvec[2] = 1;
    cvec[3] = 0;
    cvec[4] = 0;

    //Add the linear objetive function to the problem formulation
    ifail = 0;
    E04RE e04re = new E04RE(handle, nvar, cvec, ifail);
    e04re.eval();
    
    //Matrix inequalities
    //block 1
    dimaq = 2;
    nnzasum = 9; nnzqsum = 8;
    idblk = 0;
    
    nnza = new int[nvar + 1];
    irowa = new int[nnzasum];
    icola = new int[nnzasum];
    a = new double[nnzasum];

    nnza[0] = 2;
    nnza[1] = 2;
    nnza[2] = 3;
    nnza[3] = 2;
    nnza[4] = 0;
    nnza[5] = 0;

    a[0] = 1.0; irowa[0] = 1; icola[0] = 1;
    a[1] = 1.0; irowa[1] = 2; icola[1] = 2;
    a[2] = 2.0; irowa[2] = 1; icola[2] = 1;
    a[3] = -2.0; irowa[3] = 1; icola[3] = 2;
    a[4] = 6.0; irowa[4] = 1; icola[4] = 1;
    a[5] = 5.0; irowa[5] = 1; icola[5] = 2;
    a[6] = -4.0; irowa[6] = 2; icola[6] = 2;
    a[7] = 3.0; irowa[7] = 1; icola[7] = 2;
    a[8] = 8.0; irowa[8] = 2; icola[8] = 2;

    idblk = 0;
    ifail = 0;
    //Add the linear matrix inequality to the problem formulation
    E04RN e04rn = new E04RN(handle,nvar,dimaq,nnza,nnzasum,irowa,icola,a,1,
                            idummy,idblk,ifail);
    e04rn.eval();
    //update
    idblk = e04rn.getIDBLK();

    nq = 6;

    qi = new int[nq];
    qj = new int[nq];
    nnzq = new int[nq];
    irowq = new int[nnzqsum];
    icolq = new int[nnzqsum];
    q = new double[nnzqsum];
    
    qi[0] = 1; qj[0] = 4;
    qi[1] = 2; qj[1] = 4;    
    qi[2] = 3; qj[2] = 4;
    qi[3] = 1; qj[3] = 5;
    qi[4] = 2; qj[4] = 5;
    qi[5] = 3; qj[5] = 5;

    nnzq[0] = 1;
    nnzq[1] = 2;
    nnzq[2] = 1;
    nnzq[3] = 1;
    nnzq[4] = 2;
    nnzq[5] = 1;

    q[0] = 2.0; irowq[0] = 1; icolq[0] = 1;
    q[1] = 2.0; irowq[1] = 1; icolq[1] = 1;
    q[2] = 1.0; irowq[2] = 1; icolq[2] = 2;
    q[3] = 1.0; irowq[3] = 1; icolq[3] = 2;
    q[4] = 1.0; irowq[4] = 1; icolq[4] = 2;
    q[5] = 1.0; irowq[5] = 1; icolq[5] = 2;
    q[6] = 2.0; irowq[6] = 2; icolq[6] = 2;
    q[7] = 2.0; irowq[7] = 2; icolq[7] = 2;

    ifail = 0;
    //Expand the existing linear matrix inequality with bilnear term
    E04RP e04rp = new E04RP(handle,nq,qi,qj,dimaq,nnzq,nnzqsum,irowq,icolq,
                            q,idblk,ifail);
    e04rp.eval();

    //block 2
    dimaq = 2;
    nnzasum = 5;
    nnza = new int[nvar + 1];
    irowa = new int[nnzasum];
    icola = new int[nnzasum];
    a = new double[nnzasum];

    nnza[0] = 2;
    nnza[1] = 1;
    nnza[2] = 1;
    nnza[3] = 1;
    nnza[4] = 0;
    nnza[5] = 0;    

    a[0] = 1.0; irowa[0] = 1; icola[0] = 1;
    a[1] = 1.0; irowa[1] = 2; icola[1] = 2;
    a[2] = 1.0; irowa[2] = 1; icola[2] = 1;
    a[3] = 1.0; irowa[3] = 1; icola[3] = 2;
    a[4] = 1.0; irowa[4] = 2; icola[4] = 2;

    idblk = 0;
    ifail = 0;
    //Add the linear matrix inequality to the problem formulation
    e04rn = new E04RN(handle,nvar,dimaq,nnza,nnzasum,irowa,icola,a,1,
                            idummy,idblk,ifail);
    e04rn.eval();

    System.out.println("Passing SDP problem to solver");
    System.out.println();

    ifail = 0;
    //Print overview of handle
    //nout = 6 is default output for fortran 
    E04RY e04ry = new E04RY(handle,6,"Overview,Matrix Constraints",ifail);
    e04ry.eval();

    //Allocate memory for the solver
    x = new double[nvar];
    nnzu = 0;
    nnzuc = 0;
    nnzua = 0;
    inform = 0;
    for(int i = 0; i < nvar; i++){
      x[i] = 0.0;
    }

    ifail = 0;
    E04SV e04sv = new E04SV(handle,nvar,x,nnzu,rdummy,nnzuc,rdummy,nnzua,rdummy,
                            rinfo,stats,inform,ifail);
    e04sv.eval();

    //Destroy handle
    ifail = 0;
    E04RZ e04rz = new E04RZ(handle,ifail);
    e04rz.eval();
  }

}

      
      
