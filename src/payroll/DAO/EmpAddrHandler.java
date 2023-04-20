package payroll.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import payroll.Core.ReportDAO;
import payroll.Model.EmpAddressBean;

public class EmpAddrHandler {

	Connection con=null;
	Statement st=null;
	ResultSet rs=null;
	
	public ArrayList<EmpAddressBean> getEmpAddress(String EMPNO)
	{
		con=ConnectionManager.getConnection();
		Statement st = null;
		ArrayList<EmpAddressBean> empaddrlist = new ArrayList<EmpAddressBean>();
		try
		{
			st = con.createStatement();
			rs=st.executeQuery("select * from EMPAUX where empno='"+EMPNO+"'");
			EmpAddressBean empaddbean;
			while(rs.next())
			{
				empaddbean=new EmpAddressBean();
				empaddbean.setADDRTYPE(rs.getString("ADDRTYPE")==null?"":rs.getString("ADDRTYPE"));
				empaddbean.setADDR1(rs.getString("ADDR1")==null?"----":rs.getString("ADDR1"));
				empaddbean.setADDR2(rs.getString("ADDR2")==null?"--":rs.getString("ADDR2"));
				empaddbean.setADDR3(rs.getString("ADDR3")==null?"--":rs.getString("ADDR3"));
				empaddbean.setCITY(Integer.parseInt(rs.getString("CITY") == null?"0":rs.getString("CITY")));
				empaddbean.setSTATE(Integer.parseInt(rs.getString("STATE")== null?"0":rs.getString("STATE")));
				empaddbean.setPIN(Integer.parseInt(rs.getString("PIN")==null?"0":rs.getString("PIN")));
				empaddbean.setTELNO(Long.parseLong(rs.getString("TELNO")==null?"0":rs.getString("TELNO")));
				
				empaddrlist.add(empaddbean);
			}
			con.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return empaddrlist;
	}
	
	public Long getEmpAddress1(int EMPNO)
	{
		con=ConnectionManager.getConnection();
		Statement st = null;
		long mobno = 0;
		try
		{
			st = con.createStatement();
			rs=st.executeQuery("select * from EMPAUX where empno="+EMPNO+" and ADDRTYPE='CA'" );
			
			while(rs.next())
			{
				mobno = Long.parseLong(rs.getString("TELNO")==null?"0":rs.getString("TELNO"));		
			}
			con.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return mobno;
	}
	public void insertAddress(EmpAddressBean presentaddrbean, EmpAddressBean parmntaddrbean)
	{
		try
		{
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			st.execute("insert into EMPAUX values("+presentaddrbean.getEMPNO()+",'"+presentaddrbean.getADDR1()+"','"+presentaddrbean.getADDR2()+"','"+presentaddrbean.getADDR3()+"','"+presentaddrbean.getCITY()+"',"+presentaddrbean.getPIN()+","+presentaddrbean.getTELNO()+",'"+presentaddrbean.getADDRTYPE()+"','"+presentaddrbean.getSTATE()+"')");
			st.execute("insert into EMPAUX values("+parmntaddrbean.getEMPNO()+",'"+parmntaddrbean.getADDR1()+"','"+parmntaddrbean.getADDR2()+"','"+parmntaddrbean.getADDR3()+"','"+parmntaddrbean.getCITY()+"',"+parmntaddrbean.getPIN()+",'"+parmntaddrbean.getTELNO()+"','"+parmntaddrbean.getADDRTYPE()+"','"+parmntaddrbean.getSTATE()+"')");
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void updateEmpAddress(EmpAddressBean empAddressbean)
	{
		Statement st = null;
		con = ConnectionManager.getConnection();
		try
		{
			st = con.createStatement();
			st.executeUpdate("update EMPAUX set ADDR1='"+empAddressbean.getADDR1()+"',ADDR2='"+empAddressbean.getADDR2()+"',ADDR3='"+empAddressbean.getADDR3()+"',TELNO="+empAddressbean.getTELNO()+",CITY='"+empAddressbean.getCITY()+"',STATE='"+empAddressbean.getSTATE()+"',PIN="+empAddressbean.getPIN()+" where EMPNO="+empAddressbean.getEMPNO()+" and ADDRTYPE='"+empAddressbean.getADDRTYPE()+"' ");
			
			CallableStatement callableStatement = null;
			 
			String updateUsersTableOnLeft = "{call SPC_EmailD_Update(?,?)}";
	 
			try {
				
				callableStatement = con.prepareCall(updateUsersTableOnLeft);
	 			callableStatement.setInt(1, empAddressbean.getEMPNO());
	 			callableStatement.setString(2, empAddressbean.getADDR3());
				// execute SPC_Users_Update store procedure
				callableStatement.executeUpdate();
	 
				 
			} catch (SQLException e) {
	 				System.out.println(e.getMessage());
	 		} 
			finally {	 
				if (callableStatement != null) {
					callableStatement.close();
				}	 
				if (con != null) {
					con.close();
				}
	 		}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public ArrayList<String> getEmailId(String date)
	{
		Connection con = ConnectionManager.getConnection();
		ArrayList<String> result = new ArrayList<String>();
		try
		{
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("Select distinct a.EMPNO,a.ADDR3 from empaux a ,EMPMAST e  where a.EMPNO=e.EMPNO and  a.addr3 <>'' and  (e.status='A'  ) and  (e.EMPNO in ( select distinct EMPNO from PAYTRAN_STAGE where TRNDT between  '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"'  and TRNCD=999 and status='R') )");
					
			/*rs.last();
			result = new String[rs.getRow()+1];
			rs.beforeFirst();
			*/int i =0;
			while(rs.next())
			{
				if(rs.getString("ADDR3")!= null || !(rs.getString("ADDR3").equalsIgnoreCase("")))
				{
				result.add(rs.getString("EMPNO")+":"+rs.getString("ADDR3"));
				}
				i++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
}
