package payroll.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import payroll.Model.BranchBean;
import payroll.Model.EmpAddressBean;

public class BranchDAO {

	Connection con = null;
	Statement st=null;
	ResultSet rs=null;
	public void addBranch(BranchBean brnBean){
		try
		{
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			st.execute("INSERT INTO BRANCH(BRNAME,ADDR1,ADDR2,STATECD,CITYCD,ABBRV,PINCD) VALUES('"+brnBean.getBRNAME()+"','"+brnBean.getADDR1()+"'," +
					" '"+brnBean.getADDR2()+"',"+brnBean.getSTATECD()+","+brnBean.getCITYCD()+",'"+brnBean.getABBRV()+"',"+brnBean.getPin()+")");
			
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void updateBranchDetails(BranchBean brnBean)
	{
		Statement st = null;
		con = ConnectionManager.getConnection();
		try
		{
			//System.out.println("in update"+brnBean.getBRCD());
			st = con.createStatement();
			st.executeUpdate("update BRANCH set BRNAME='"+brnBean.getBRNAME()+"',ADDR1='"+brnBean.getADDR1()+"',ADDR2='"+brnBean.getADDR2()+"'," +
					"STATECD="+brnBean.getSTATECD()+",CITYCD='"+brnBean.getCITYCD()+"',PINCD="+brnBean.getPin()+" where brcd="+brnBean.getBRCD()+"  ");
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<BranchBean> getBranchDetails()
	{
		con=ConnectionManager.getConnection();
		Statement st = null;
		ArrayList<BranchBean> branList = new ArrayList<BranchBean>();
		try
		{
			st = con.createStatement();
			rs=st.executeQuery("select * from Branch");
			BranchBean brnBean;
			while(rs.next())
			{
				//System.out.println("in get Baranch");
				brnBean=new BranchBean();
				brnBean.setBRCD(rs.getInt("BRCD"));
				brnBean.setBRNAME(rs.getString("BRNAME"));
				brnBean.setADDR1(rs.getString("ADDR1"));
				brnBean.setADDR2(rs.getString("ADDR2"));
				brnBean.setSTATECD(rs.getInt("STATECD"));
				brnBean.setCITYCD(rs.getInt("CITYCD"));
				brnBean.setPin(rs.getInt("PINCD"));
				brnBean.setABBRV(rs.getString("ABBRV"));
				
				branList.add(brnBean);
			}
			con.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return branList;
	}
}
