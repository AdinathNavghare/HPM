package payroll.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import payroll.Model.AdvanceBean;
import payroll.Model.LeaveEncashmentBean;
import payroll.Model.TranBean;
import payroll.Model.TransactionBean;

public class LeaveEncashmentHandler {

	EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
	String currentdate=empAttendanceHandler.getServerDate();
	
	
	public boolean addLeaveEncash(LeaveEncashmentBean leaveEncashmentBean,
			int loggedEmployeeNo) {

		boolean result = true;
	    
		
		
		
			String query = "select PRJ_SRNO from EMPTRAN where EMPNO="+leaveEncashmentBean.getEmpNo()+"" +
					"and  EFFDATE=(select MAX (effdate) from EMPTRAN where EMPNO="+leaveEncashmentBean.getEmpNo()+") ";
				Connection con = ConnectionManager.getConnection();
				ResultSet rs = null;
				int site=0;
				try
				{
					Statement st = con.createStatement();
					rs = st.executeQuery(query);
						
						
			
				if(rs.next())
				{
					site =	(rs.getInt("PRJ_SRNO"));
					
				}	
					
						
					
			
			String insertquery = "INSERT INTO LEAVE_ENCASHMENT (EMPNO,SITE_ID,LEAVE_BAL,MAX_LIMIT," +
					                          "ENCASH_APPLICABLE,MONTHLY_GROSS,ESIC_AMT,ENCASHMENT_AMT," +
					                          " CREATED_BY,CREATED_DATE,status, "+
					                          "LEAVE_ENCASHMENT_SANCTION,LEAVE_ENCASHMENT_DATE,"+
					                          "FROM_DATE,TO_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement Pstat = con.prepareStatement(insertquery);
			Pstat.setInt(1, leaveEncashmentBean.getEmpNo());
			Pstat.setInt(2, site);
			Pstat.setFloat(3, leaveEncashmentBean.getLeaveBal());
			Pstat.setFloat(4,leaveEncashmentBean.getMaxLimit());
			Pstat.setFloat(5,leaveEncashmentBean.getEncashApplicable());
			Pstat.setFloat(6, leaveEncashmentBean.getMonthlyGross());
			Pstat.setFloat(7, leaveEncashmentBean.getEsicAmt());
			Pstat.setFloat(8, leaveEncashmentBean.getEncashmentAmt());
			Pstat.setInt(9, loggedEmployeeNo);
			Pstat.setString (10, currentdate);
			Pstat.setString (11,"pending" );
		
			Pstat.setFloat(12, leaveEncashmentBean.getLeaveEncashmentSanction());
			Pstat.setString (13, leaveEncashmentBean.getLeaveEncashmentDate());
			Pstat.setString (14, leaveEncashmentBean.getFromDate());
			Pstat.setString (15, leaveEncashmentBean.getToDate());
			/*Pstat.setString(9, requestMacId);*/
			Pstat.executeUpdate();
			result = true;
			Pstat.close();

	
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			 result=false;
		}
		return result;

	
		
		
	}




	public LeaveEncashmentBean getEmployeeInfo(String empNo) {
		LeaveEncashmentBean  leaveEncashmentBean  = new LeaveEncashmentBean();
		float balance= 0.0f;
		int empnNumber=Integer.parseInt(empNo);
		try
		{
		        
			Connection connection = ConnectionManager.getConnection();
			connection= ConnectionManager.getConnection();
			Statement statement=connection.createStatement();
			ResultSet resultSet = null;
		/*	ResultSet resultSet1 = null;
			ResultSet resultSet2 = null;*/
			
			resultSet=statement.executeQuery("SELECT BAL FROM LEAVEBAL WHERE EMPNO='"+empNo+"' AND BALDT=(SELECT MAX(BALDT) FROM LEAVEBAL WHERE EMPNO='"+empNo+"' ) ");
			while(resultSet.next())
			{	
			leaveEncashmentBean.setLeaveBal(resultSet.getFloat("BAL"));
	   		}
			resultSet=statement.executeQuery("SELECT CAL_AMT FROM PAYTRAN WHERE TRNCD=199 AND  EMPNO="+empNo+"  ");
			while(resultSet.next())
			{				
			leaveEncashmentBean.setMonthlyGross(resultSet.getFloat("CAL_AMT"));
	   		}
			resultSet=statement.executeQuery("SELECT CAL_AMT FROM PAYTRAN WHERE TRNCD=221 AND  EMPNO="+empNo+"  ");
			while(resultSet.next())
			{				
			leaveEncashmentBean.setEsicAmt(resultSet.getFloat("CAL_AMT"));
	   		}
			connection.close();
		}
		catch(Exception e)
		{
		}
		return leaveEncashmentBean;
	
	}




	
	
}
