package payroll.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.omg.CORBA.Request;

import payroll.Model.Lookup;
import payroll.Model.ShiftBean;

public class ShiftHandler 
{
	Connection conn;
	public void  insertValue(ShiftBean sb)
	{
		try
		{
			conn= ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			int srno=0;
			ResultSet rs= st.executeQuery("select max(SRNO) from SHIFTMAST");
			while(rs.next())
			{
				srno=rs.getString(1)!=null?rs.getInt(1):0;
			}
			srno=srno+1;
			st.execute("insert into SHIFTMAST values("+srno+",'"+sb.getShift()+"','"+sb.getStartTime()+"','"+sb.getEndTime()+"','"+sb.getStatus()+"')");
			conn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<ShiftBean> getshiftvalues()
	{
		ArrayList<ShiftBean> shiftlist = new ArrayList<ShiftBean>();
		ShiftBean sb ;
		try
		{
			conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = null;
			rs= st.executeQuery("select * from SHIFTMAST order by srno asc");
			while(rs.next())
			{
				sb = new ShiftBean();
				sb.setSrno(rs.getString(1)!=null?rs.getInt(1):0);
				sb.setShift(rs.getString(2)!=null?rs.getString(2):"");
				sb.setStartTime(rs.getString(3)!=null?rs.getString(3):"");
				sb.setEndTime(rs.getString(4)!=null?rs.getString(4):"");
				sb.setStatus(rs.getString(5)!=null?rs.getString(5):"");
				shiftlist.add(sb);
			}
			conn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return shiftlist;
	}
	
	public boolean update(ShiftBean shbean) 
	{
		boolean flag=false;
		conn = ConnectionManager.getConnection();
		String shiftUpdate="UPDATE SHIFTMAST SET shiftcode='"+shbean.getShift()+"',starttime='"+shbean.getStartTime()+"',endtime='"+shbean.getEndTime()+"',status='"+shbean.getStatus()+"' where srno='"+shbean.getSrno()+"'";
		try
		{
			Statement st = conn.createStatement();
			st.executeUpdate(shiftUpdate);
			flag=true;
			conn.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean insertOvertime(ShiftBean shbean1)
	{
		boolean flag=false;
		Connection conn=ConnectionManager.getConnection();
		try
		{
			int srno=0;
			Statement st2=conn.createStatement();
			Statement st3=conn.createStatement();
			ResultSet rs= st2.executeQuery("select max(SRNO) from OVERTIME");
		    ResultSet rs1=st3.executeQuery("select * from overtime where srno="+shbean1.getSrno()+"");
			if(rs.next())
			{
				srno=rs.getString(1)!=null?rs.getInt(1):0;
			}
			srno=srno+1;
			if(!rs1.next())
			{
				String query="INSERT INTO OVERTIME VALUES('"+srno+"','"+shbean1.getEmptype()+"','"+shbean1.getGrade()+"','"+shbean1.getShiftcode()+"','"+shbean1.getRate()+"')";
				st2.executeUpdate(query);
				flag=true;
			}
			conn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	public ArrayList<ShiftBean> getList() 
	{
		ArrayList<ShiftBean> list=new ArrayList<>();
		Connection conn=ConnectionManager.getConnection();
		String listQUERY="SELECT * FROM OVERTIME";
		try
		{
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(listQUERY);
			while(rs.next())
			{
				ShiftBean OVTbean = new ShiftBean();
				OVTbean.setSrno(rs.getString(1)!=null?rs.getInt(1):0);
				OVTbean.setEmptype(rs.getString(2)!=null?rs.getInt(2):0);
				OVTbean.setGrade(rs.getString(3)!=null?rs.getInt(3):0);
				OVTbean.setShiftcode(rs.getString(4)!=null?rs.getInt(4):0);
				OVTbean.setRate(rs.getString(5)!=null?rs.getInt(5):0);
				list.add(OVTbean);
			}
			conn.close();	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean UpdateOvertime(ShiftBean shbean)
	{
       boolean flag=false;
       Connection conn=ConnectionManager.getConnection();
       String updateOverT="UPDATE OVERTIME SET emptype='"+shbean.getEmptype()+"',grade='"+shbean.getGrade()+"',shiftcode='"+shbean.getShiftcode()+"',rate='"+shbean.getRate()+"' WHERE srno='"+shbean.getSrno()+"'";
       try
       {
			Statement st=conn.createStatement();
			flag=true;
			st.executeUpdate(updateOverT);
			st.close();
			conn.close();
       }
       catch (SQLException e) 
       {
    	   e.printStackTrace();
       }
       return flag;
	}
	
	public ArrayList<ShiftBean> getCalmastList() 
	{
		ArrayList<ShiftBean> calList=new ArrayList<>();
		Connection conn=ConnectionManager.getConnection();
		String query="SELECT * FROM CALMAST";
		try
		{
			Statement st=conn.createStatement();
			ResultSet rslt=st.executeQuery(query);
			while(rslt.next())
			{
				ShiftBean shbean1=new ShiftBean();
				shbean1.setEmptype(rslt.getString(1)!=null?rslt.getInt(1):0);
				shbean1.setDay(rslt.getString(2)!=null?rslt.getString(2):"");
				shbean1.setDaytype(rslt.getString(3)!=null?rslt.getString(3):"");
				shbean1.setDaydate(rslt.getString(4)!=null?dateFormat(rslt.getDate(4)):"");
				shbean1.setHoliday(rslt.getString(5)!=null?rslt.getString(5):"");
				shbean1.setDesc(rslt.getString(6)!=null?rslt.getString(6):"");
				calList.add(shbean1);
			}
			st.close();
			conn.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return calList;
	}
	
	public boolean addCallmast(ShiftBean shbean2) 
	{
		boolean flag=false;
		int emptype=shbean2.getEmptype();
		Connection conn=ConnectionManager.getConnection();
		String query="INSERT INTO CALMAST VALUES('"+emptype+"','"+shbean2.getDay()+"','"+shbean2.getDaytype()+"',CAST('"+shbean2.getDaydate()+"' AS DATETIME),'"+shbean2.getHoliday()+"', '"+shbean2.getDesc()+"')";
		try
		{
			Statement st=conn.createStatement();
			st.executeUpdate(query);
			flag=true;
			conn.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	public static String dateFormat(Date dates)
	{
		String result="";
		if(dates==null)
		{
			result="";
			return result;
		}
		SimpleDateFormat format= new SimpleDateFormat("dd-MMM-yyyy");
		result=format.format(dates);
		return result;
	}
	
	public boolean updateCalmast(ShiftBean shbean3) 
	{
		boolean flag=false;
		Connection conn=ConnectionManager.getConnection();
		String updateovtime="UPDATE CALMAST SET DAY='"+shbean3.getDay()+"', DAYTYPE='"+shbean3.getDaytype()+"', DAYDATE='"+shbean3.getDaydate()+"', HOLIDAY='"+shbean3.getHoliday()+"',DISC='"+shbean3.getDesc()+"' " +
				" EMPTYPE='"+shbean3.getEmptype()+"' ";
		try 
		{
			Statement stmt=conn.createStatement();
			stmt.executeUpdate(updateovtime);
			flag=true;
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	public ArrayList<ShiftBean> getTimesheet()
	{
		ArrayList<ShiftBean> list=new ArrayList<ShiftBean>();
		Connection conn=ConnectionManager.getConnection();
		String timesheet="SELECT * FROM TIMESHEET";
		ResultSet rslt=null;
		try
		{
			Statement stmt1=conn.createStatement();
			rslt=stmt1.executeQuery(timesheet);
			while(rslt.next())
			{
				ShiftBean shftbean=new ShiftBean();
				shftbean.setEMPNO(rslt.getString(1)!=null?Integer.parseInt(rslt.getString(1)):0);
				shftbean.setShift(rslt.getString(2)!=null?rslt.getString(2):"");
				shftbean.setDaydate(rslt.getString(3)!=null?dateFormat(rslt.getDate(3)):"");
				shftbean.setCheckin(rslt.getString(4)!=null?rslt.getString(4):"");
				shftbean.setCheckout(rslt.getString(5)!=null?rslt.getString(5):"");
				shftbean.setTotal(rslt.getString(6)!=null?rslt.getString(6):"");
				shftbean.setSrno(rslt.getString(7)!=null?Integer.parseInt(rslt.getString(7)):0);
				list.add(shftbean);
			}
			conn.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean addTimesheet(ShiftBean shfbean)
	{
		boolean flag=false;
		Connection conn=ConnectionManager.getConnection();
		String maxsrno="SELECT max(SRNO) FROM TIMESHEET ";
		int srno=1;
		try
		{
			Statement stmt2=conn.createStatement();
			ResultSet max=stmt2.executeQuery(maxsrno);
			if(max.next())
			{
				srno=max.getInt(1)+1;
			}
			String timesheet="INSERT INTO TIMESHEET VALUES('"+shfbean.getEMPNO()+"','"+shfbean.getShift()+"','"+shfbean.getDaydate()+"','"+shfbean.getCheckin()+"','"+shfbean.getCheckout()+"','"+shfbean.getTotal()+"','"+srno+"')";
			stmt2.executeUpdate(timesheet);
			flag=true;
			conn.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean UpdateTimesheet(ShiftBean shfbean2) 
	{
	     boolean flag=false;
	     Connection conn=ConnectionManager.getConnection();
	     String updatesheet="UPDATE TIMESHEET SET SHIFTCODE='"+shfbean2.getShift()+"',DAYDATE='"+shfbean2.getDaydate()+"',CHECKIN='"+shfbean2.getCheckin()+"',CHECKOUT='"+shfbean2.getCheckout()+"',TOTAL='"+shfbean2.getTotal()+"' WHERE EMPNO='"+shfbean2.getEMPNO()+"'AND SRNO='"+shfbean2.getSrno()+"'";
	     try
	     {
	    	 Statement stmt=conn.createStatement();
	    	 stmt.executeUpdate(updatesheet);
	    	 flag=true;
	    	 conn.close();
	     }
	     catch (SQLException e) 
	     {
	    	 e.printStackTrace();
	     }
	     return flag;
	}
}
