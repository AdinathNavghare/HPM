package payroll.DAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import payroll.Core.Utility;

import payroll.Model.*;

public class GradeHandler 
{
	
	static int cnt = 0;
	public boolean addGrade(GradeBean GB)
	{
		boolean result=false;
		Connection con =null;
		try
		{
			con=ConnectionManager.getConnection();
			Statement st=con.createStatement();
			String sql="INSERT INTO GRADE VALUES("
						+GB.getPOSTCD()+","
						+"'"+GB.getALFACD()+"',"
						+"'"+GB.getDISC()+"',"
						+"'"+GB.getEFFDT()+"',"
						+GB.getBASIC()+","
						+GB.getINCR1()+","
						+GB.getNOY1()+","
						+GB.getINCR2()+","
						+GB.getNOY2()+","
						+GB.getINCR3()+","
						+GB.getNOY3()+","
						+GB.getINCR4()+","
						+GB.getNOY4()+","
						+GB.getINCR5()+","
						+GB.getNOY5()+","
						+GB.getINCR6()+","
						+GB.getNOY6()+","
						+GB.getEXG()+","
						+GB.getMED()+","
						+GB.getEDU()+","
						+GB.getLTC()+","
						+GB.getCLOSING()+","
						+GB.getCONV()+","
						+GB.getCASH()+","
						+GB.getCLG()+","
						+GB.getWASHING()+","
						+GB.getFLDWRK()
						+")";
			st.execute(sql);
			result=true;
			con.close();
		}
		catch (SQLException e)
		{
			try 
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return result;
	}
	
	public GradeBean getGrade(int postCode, String effDate)
	{
		GradeBean GB = null;
		Connection con=null;
		try
		{
			con = ConnectionManager.getConnection();
			Statement st= con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM GRADE WHERE POSTCD="+postCode+" AND EFFDT='"+effDate+"'");
			if(rs.next())
			{
				GB = new GradeBean();
				GB.setPOSTCD(rs.getString("POSTCD")==null?0:rs.getInt("POSTCD"));
				GB.setALFACD(rs.getString("ALFACD")==null?"":rs.getString("ALFACD"));
				GB.setDISC(rs.getString("DISC")==null?"":rs.getString("DISC"));
				GB.setEFFDT(rs.getDate("EFFDT")==null?"":Utility.dateFormat(rs.getDate("EFFDT")));
				GB.setBASIC(rs.getString("BASIC")==null?0:rs.getInt("BASIC"));
				GB.setINCR1(rs.getString("INCR1")==null?0:rs.getInt("INCR1"));
				GB.setNOY1(rs.getString("NOY1")==null?0:rs.getInt("NOY1"));
				GB.setINCR2(rs.getString("INCR2")==null?0:rs.getInt("INCR2"));
				GB.setNOY2(rs.getString("NOY2")==null?0:rs.getInt("NOY2"));
				GB.setINCR3(rs.getString("INCR3")==null?0:rs.getInt("INCR3"));
				GB.setNOY3(rs.getString("NOY3")==null?0:rs.getInt("NOY3"));
				GB.setINCR4(rs.getString("INCR4")==null?0:rs.getInt("INCR4"));
				GB.setNOY4(rs.getString("NOY4")==null?0:rs.getInt("NOY4"));
				GB.setINCR5(rs.getString("INCR5")==null?0:rs.getInt("INCR5"));
				GB.setNOY5(rs.getString("NOY5")==null?0:rs.getInt("NOY5"));
				GB.setINCR6(rs.getString("INCR6")==null?0:rs.getInt("INCR6"));
				GB.setNOY6(rs.getString("NOY6")==null?0:rs.getInt("NOY6"));
				GB.setEXG(rs.getString("EXG")==null?0:rs.getInt("EXG"));
				GB.setMED(rs.getString("MED")==null?0:rs.getInt("MED"));
				GB.setEDU(rs.getString("EDU")==null?0:rs.getInt("EDU"));
				GB.setLTC(rs.getString("LTC")==null?0:rs.getInt("LTC"));
				GB.setCLOSING(rs.getString("CLOSING")==null?0:rs.getInt("CLOSING"));
				GB.setCONV(rs.getString("CONV")==null?0:rs.getInt("CONV"));
				GB.setCASH(rs.getString("CASH")==null?0:rs.getInt("CASH"));
				GB.setCLG(rs.getString("CLG")==null?0:rs.getInt("CLG"));
				GB.setWASHING(rs.getString("WASHING")==null?0:rs.getInt("WASHING"));
				GB.setFLDWRK(rs.getString("FLDWRK")==null?0:rs.getInt("FLDWRK"));
			}
			con.close();
		}
		catch(SQLException e)
		{
			try 
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return GB;
	}
	
	public boolean deleteGrade(int post, String effdt)
	{
		boolean flag = false;
		Connection con=null;
		try
		{
			con = ConnectionManager.getConnection();
			Statement st= con.createStatement();
			st.execute("DELETE FROM GRADE WHERE POSTCD="+post+" AND EFFDT='"+effdt+"'");
			flag = true;
			con.close();
		}
		catch(SQLException e)
		{
			try 
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	
	public static String GetGrade(int grade,String alpha,RepoartBean repBean)
	{
		String result = "";
		ResultSet rs = null;
		Statement st;
		try
		{
			st= repBean.getCn().createStatement();
			rs = st.executeQuery("SELECT *  FROM GRADE WHERE POSTCD="+grade);
			if(rs.next())
			{
				if(alpha.equalsIgnoreCase("Y"))
				{
					result = rs.getString("ALFACD");
				}
				else
				{
					result = rs.getString("DISC");
				}
			}
			else
			{
				result = "NOT FOUND";
			}
			rs.close();
			st.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return result;
	}
}