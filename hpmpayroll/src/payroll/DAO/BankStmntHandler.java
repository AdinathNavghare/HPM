package payroll.DAO;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import payroll.Core.ReportDAO;
import payroll.Model.BankStmntBean;

public class BankStmntHandler 
{
	public static ArrayList<BankStmntBean> getBankStmntlist(String date,Connection con,int batchNumber,String type)
	{
		ArrayList<BankStmntBean> bslist = new ArrayList<BankStmntBean>();
		ResultSet rs;
		String tableName="";
		String sql="";
		if(type.equalsIgnoreCase("B")){
			tableName="paytran_stage";
			 sql="select  m.empno,m.fname,m.mname,m.lname,t.acno,t.bank_name,p.net_amt,o.F3 from " +
						"Empmast m  full outer join OTHERDETAIL o on o.empno = m.empno , "+tableName+" p , EMPTRAN t where"+
	   " m.empno=p.empno and m.empno=t.empno and p.trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and p.trncd=999 and p.STATUS='F' "+
						" and t.srno = (SELECT MAX(E2.srno) FROM EMPTRAN E2 WHERE E2.EMPNO = m.EMPNO AND E2.EFFDATE <= '"+ReportDAO.EOM(date)+"') order by t.bank_name,m.empno";
		}
		else if(type.equalsIgnoreCase("A")){
			tableName="paytran_stage";
			
			if(batchNumber==0){ //for all batch
				
				sql="select  m.empno,m.fname,m.mname,m.lname,t.acno,t.bank_name,p.net_amt,o.F3 from  " +
				 		"Empmast m  full outer join OTHERDETAIL o on o.empno = m.empno , "+tableName+" p join RELEASE_BATCH r on r.EMPNO=p.EMPNO, " +
				 		"EMPTRAN t where  r.TRNDT='"+ReportDAO.EOM(date)+"' and m.empno=p.empno and m.empno=t.empno and p.trndt  " +
				 		"between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and p.trncd=999 and p.STATUS='R' and t.srno = (SELECT MAX(E2.srno) " +
				 		" FROM EMPTRAN E2 WHERE E2.EMPNO =m.EMPNO AND E2.EFFDATE <= '"+ReportDAO.EOM(date)+"') order by t.bank_name,m.empno";
				
			}
			
			else{
			 sql="select  m.empno,m.fname,m.mname,m.lname,t.acno,t.bank_name,p.net_amt,o.F3 from  " +
			 		"Empmast m  full outer join OTHERDETAIL o on o.empno = m.empno , "+tableName+" p join RELEASE_BATCH r on r.EMPNO=p.EMPNO, " +
			 		"EMPTRAN t where r.BATCHNO= "+batchNumber+"  and r.TRNDT='"+ReportDAO.EOM(date)+"' and m.empno=p.empno and m.empno=t.empno and p.trndt  " +
			 		"between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and p.trncd=999 and p.STATUS='R' and t.srno = (SELECT MAX(E2.srno) " +
			 		" FROM EMPTRAN E2 WHERE E2.EMPNO =m.EMPNO AND E2.EFFDATE <= '"+ReportDAO.EOM(date)+"') order by t.bank_name,m.empno";
			}	 
		}
		try
		{
			//System.out.println("date===="+date);
			Statement st =con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			/*String sql="select  m.empno,m.fname,m.mname,m.lname,t.acno,t.bank_name,p.net_amt from Empmast m,ytdtran p,Emptran t " +
					"where m.empno=p.empno and m.empno=t.empno and p.trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and p.trncd=999 order by t.bank_name,m.empno";*/
			
			rs=st.executeQuery(sql);
			System.out.println(sql);
			float total=0;
			while(rs.next())
			{
				BankStmntBean bsb=new BankStmntBean();
				bsb.setEMPNAME(rs.getString("fname")+"  "+rs.getString("mname")+"  "+rs.getString("lname") );
				bsb.setACCNO(rs.getString("acno")==null?"":rs.getString("acno"));
				bsb.setEMPNO(rs.getString("empno"));
				bsb.setBANK_NAME(rs.getString("bank_name")==null?"4":rs.getString("bank_name"));
				bsb.setNETPAY(rs.getString("net_amt"));
				bsb.setIFSCCODE(rs.getString("F3")==null?"":rs.getString("F3"));
				bslist.add(bsb);
				total+=Float.parseFloat(rs.getString("net_amt"));
			}
			//System.out.println("******"+bslist.size());
			//System.out.println("//////"+total);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return bslist;
	}
} 
