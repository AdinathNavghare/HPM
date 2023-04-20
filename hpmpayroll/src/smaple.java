
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import payroll.Core.ReportDAO;
import payroll.DAO.ConnectionManager;
import payroll.Model.LoanAppBean;



public class smaple {
	public static void update_loan_detail(int eno,int trncd,float amount,int k)
	{
		String S ="";
		float install_amount =0;
		float amt=0;
		amt=amount;
		String M ="";
		Connection con = null;
			
		try{
		con = ConnectionManager.getConnection();
	    Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		Statement st2 = con.createStatement();
		
		S="select * from loan_detail where empno="+eno+" and loan_code ='"+trncd+"' and Active='sanction'";
	     System.out.println("   amt=="+amt);

		ResultSet rs = st2.executeQuery(S);
     System.out.println("   s=="+S);

		while(rs.next())
		{
			install_amount=rs.getFloat("monthly_install");
		
			if (amt>=install_amount)
			{
					
		     M= "Update Loan_detail set total_paid="+rs.getFloat("monthly_install")+" where empno="+rs.getInt("EMPNO")+" and loan_no="+rs.getString("loan_no")+"and loan_code ='"+trncd+"' ";
		     System.out.println("   m=="+M);

		     st.executeUpdate(M);
		     amt=-install_amount;
		     System.out.println("   amt-=="+amt);

			}
			else
			{
				 M= "Update Loan_detail set total_paid=amt where empno = "+rs.getInt("EMPNO")+" and loan_no="+rs.getString("loan_no")+"and loan_code ='"+trncd+"' ";
			     System.out.println("   m1=="+M);

				 st.executeUpdate(M);
				 break;
			}
		 st.close();
	
		}
        rs.close();
		st2.close();
		con.close();
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
	}}
	
		


