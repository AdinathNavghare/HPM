package payroll.DAO;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import payroll.Model.DeductBean;
import payroll.Core.Utility;
public class DeductHandler {
	
	
	public boolean addDeduction(DeductBean DB)
	{
		boolean result=false;
		Connection con=ConnectionManager.getConnection();
		try
		{
			Statement st = con.createStatement();
			String sql="INSERT INTO DEDMAST VALUES("
						+DB.getEMPNO()+","
						+DB.getTRNCD()+","
						+DB.getSRNO()+","
						+DB.getAMOUNT()+","
						+"'"+DB.getSUBSYS_CD()+"',"
						+DB.getAC_NO()+","
						+"'"+DB.getBODSANCNO()+"',"
						+"'"+DB.getSANC_DATE()+"',"
						+DB.getSANC_AMT()+","
						+DB.getINT_RATE()+","
						+"'"+DB.getREPAY_START()+"',"
						+"'"+DB.getEND_DATE()+"',"
						+"'"+DB.getCUMUYN()+"',"
						+"'"+DB.getACTYN()+"',"
						+DB.getNo_Of_Installment()
						+")";
			st.execute(sql);
			result=true;
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
		return result;
	}
	
	public boolean updateDeduction(int empno, int trncd, int srno, DeductBean DB)
	{
		boolean result=false;
		Connection con=ConnectionManager.getConnection();
		try
		{
			Statement st = con.createStatement();
			String sql="UPDATE DEDMAST SET "
						+"AMOUNT="+DB.getAMOUNT()+", "
						+"SUBSYS_CD='"+DB.getSUBSYS_CD()+"', "
						+"AC_NO="+DB.getAC_NO()+", "
						+"BODSANCNO='"+DB.getBODSANCNO()+"', "
						+"SANC_DATE='"+DB.getSANC_DATE()+"', "
						+"SANC_AMT="+DB.getSANC_AMT()+", "
						+"INT_RATE="+DB.getINT_RATE()+", "
						+"REPAY_START='"+DB.getREPAY_START()+"', "
						+"END_DATE='"+DB.getEND_DATE()+"', "
						+"CUMUYN='"+DB.getCUMUYN()+"', "
						+"ACTYN='"+DB.getACTYN()+"' "
						+"WHERE EMPNO="+empno+" AND TRNCD="+trncd+" AND SRNO="+srno;
			st.executeUpdate(sql);
			result=true;
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
		return result;
	}
	
	public ArrayList<DeductBean> getDeduction(int empno)
	{
		ArrayList<DeductBean> result = new ArrayList<DeductBean>();
		Connection con=ConnectionManager.getConnection();
		try
		{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM DEDMAST WHERE EMPNO="+empno+" ORDER BY TRNCD , SRNO DESC");
			DeductBean DB;
			while(rs.next())
			{
				DB = new DeductBean();
				DB.setEMPNO(rs.getString("EMPNO")==null?0:rs.getInt("EMPNO"));
				DB.setTRNCD(rs.getString("TRNCD")==null?0:rs.getInt("TRNCD"));
				DB.setSRNO(rs.getString("SRNO")==null?0:rs.getInt("SRNO"));
				DB.setAMOUNT(rs.getString("AMOUNT")==null?0:rs.getFloat("AMOUNT"));
				DB.setSUBSYS_CD(rs.getString("SUBSYS_CD")==null?"":rs.getString("SUBSYS_CD"));
				DB.setAC_NO(rs.getString("AC_NO")==null?0:rs.getLong("AC_NO"));
				DB.setBODSANCNO(rs.getString("BODSANCNO")==null?"":rs.getString("BODSANCNO"));
				DB.setSANC_DATE(rs.getDate("SANC_DATE")==null?"":Utility.dateFormat(rs.getDate("SANC_DATE")));
				DB.setSANC_AMT(rs.getString("SANC_AMT")==null?0:rs.getInt("SANC_AMT"));
				DB.setINT_RATE(rs.getString("INT_RATE")==null?0:rs.getFloat("INT_RATE"));
				DB.setREPAY_START(rs.getDate("REPAY_START")==null?"":Utility.dateFormat(rs.getDate("REPAY_START")));
				DB.setEND_DATE(rs.getDate("END_DATE")==null?"":Utility.dateFormat(rs.getDate("END_DATE")));
				DB.setCUMUYN(rs.getString("CUMUYN")==null?"":rs.getString("CUMUYN"));
				DB.setACTYN(rs.getString("ACTYN")==null?"":rs.getString("ACTYN"));
				DB.setNo_Of_Installment(rs.getString("No_Of_Installment")==null?0:rs.getInt("No_Of_Installment"));
				result.add(DB);
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
		return result;
	}
	
	public int getMaxSrno(int empno, int trncd)
	{
		int max=0;
		Connection con=ConnectionManager.getConnection();
		try
		{
			Statement st=con.createStatement();
			ResultSet rs= st.executeQuery("SELECT MAX(SRNO) FROM DEDMAST WHERE EMPNO ="+empno+" AND TRNCD="+trncd);
			if(rs.next())
			{
				max= rs.getInt(1);
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
		return max;
	}
	public boolean delDedction(int empno, int trncd, int srno)
	{
		boolean flag = false;
		Connection con=ConnectionManager.getConnection();
		try
		{
			Statement st=con.createStatement();
			st.execute("DELETE FROM DEDMAST WHERE EMPNO="+empno+" AND TRNCD="+trncd+" AND SRNO="+srno);
			flag=true;
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
	public DeductBean getSingleDed(int empno, int trncd, int srno)
	{
		DeductBean DB=new DeductBean();
		Connection con=ConnectionManager.getConnection();
		try
		{
			Statement st=con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM DEDMAST WHERE EMPNO="+empno+" AND TRNCD="+trncd+" AND SRNO="+srno);
			while(rs.next())
			{
				DB.setEMPNO(rs.getString("EMPNO")==null?0:rs.getInt("EMPNO"));
				DB.setTRNCD(rs.getString("TRNCD")==null?0:rs.getInt("TRNCD"));
				DB.setSRNO(rs.getString("SRNO")==null?0:rs.getInt("SRNO"));
				DB.setAMOUNT(rs.getString("AMOUNT")==null?0:rs.getInt("AMOUNT"));
				DB.setSUBSYS_CD(rs.getString("SUBSYS_CD")==null?"":rs.getString("SUBSYS_CD"));
				DB.setAC_NO(rs.getString("AC_NO")==null?0:rs.getLong("AC_NO"));
				DB.setBODSANCNO(rs.getString("BODSANCNO")==null?"":rs.getString("BODSANCNO"));
				DB.setSANC_DATE(rs.getDate("SANC_DATE")==null?"":Utility.dateFormat(rs.getDate("SANC_DATE")));
				DB.setSANC_AMT(rs.getString("SANC_AMT")==null?0:rs.getInt("SANC_AMT"));
				DB.setINT_RATE(rs.getString("INT_RATE")==null?0:rs.getFloat("INT_RATE"));
				DB.setREPAY_START(rs.getDate("REPAY_START")==null?"":Utility.dateFormat(rs.getDate("REPAY_START")));
				DB.setEND_DATE(rs.getDate("END_DATE")==null?"":Utility.dateFormat(rs.getDate("END_DATE")));
				DB.setCUMUYN(rs.getString("CUMUYN")==null?"":rs.getString("CUMUYN"));
				DB.setACTYN(rs.getString("ACTYN")==null?"":rs.getString("ACTYN"));
				DB.setNo_Of_Installment(rs.getString("No_Of_Installment")==null?0:rs.getInt("No_Of_Installment"));
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
		return DB;
	}
	
}
