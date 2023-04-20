package payroll.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import payroll.Core.ReportDAO;
import payroll.Model.WeekOffBean;

public class SiteWeekOffHandler {
	
	public boolean updateSiteWeekOff( WeekOffBean weekOffBean)
	{
		Connection con = ConnectionManager.getConnection();
		boolean result = false;
		int i = 0;
		
		try 
		{
			Statement st = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs1 = null;
			
				System.out.println("SELECT * FROM WEEKOFF WHERE EMPNO="+weekOffBean.getEmpno()+" AND YEAR_WO="+weekOffBean.getYear()+" AND MONTH_WO='"+weekOffBean.getMonth()+"' ");
						rs1 = st2.executeQuery("SELECT * FROM WEEKOFF WHERE EMPNO="+weekOffBean.getEmpno()+" AND YEAR_WO="+weekOffBean.getYear()+" AND MONTH_WO='"+weekOffBean.getMonth()+"' ");
						if(rs1.next())
						{
							String update = "UPDATE WEEKOFF set day="+weekOffBean.getDay()+", updated_date= GETDATE()," +
									"updated_by="+weekOffBean.getUpdatedBy()+" where " +
											"EMPNO="+weekOffBean.getEmpno()+" AND YEAR_WO="+weekOffBean.getYear()+" AND MONTH_WO='"+weekOffBean.getMonth()+"'  ";
							st.executeUpdate(update);
						}
						else
						{
							String insert = "INSERT INTO WEEKOFF(site_id,MONTH_WO,YEAR_WO,empno," +
									"day,created_by,created_date) VALUES " +
								"("+weekOffBean.getSite_id()+" ,'"+weekOffBean.getMonth()+"',"
								+weekOffBean.getYear()+"," +
								+weekOffBean.getEmpno()+","+weekOffBean.getDay()+"," +
								+weekOffBean.getCreatedBy()+",'"+weekOffBean.getCreatedDate()+"')";			
							System.out.println(insert);
							st.executeUpdate(insert);
						}
				
				
			
			result = true;
			rs1.close();
			st2.close();
			st.close();
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public  WeekOffBean getWeekOffOfEmployee(int empno,int year,String month,int site_id)
	{
		Connection con= ConnectionManager.getConnection();
		WeekOffBean weekOffBean = null;
		try
		{
			Statement st = con.createStatement();
			ResultSet rs= null;
			String query="Select * from WEEKOFF Where EMPNO="+empno+" AND SITE_ID="+site_id+" " +
					"AND YEAR_WO="+year+" AND MONTH_WO='"+month+"'";
			System.out.println(query);
			rs= st.executeQuery(query);
			if(rs.next())
			{
				weekOffBean = new WeekOffBean();
				weekOffBean.setMonth(rs.getString("MONTH_WO"));
				weekOffBean.setDay(rs.getInt("DAY"));
				weekOffBean.setYear(rs.getInt("YEAR_WO"));
				weekOffBean.setEmpno(empno);
			
			}
			/*else
			{
				weekOffBean = new WeekOffBean();
				weekOffBean.setEmpno(empno);
			}*/
			con.close();
		}
		catch(Exception e)
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
		return weekOffBean;
	}

public  ArrayList<Integer> getEmpList_Status(String prjCode,String month){
		
		ArrayList<Integer> list= new ArrayList<Integer>();
		Connection con = null;
		ResultSet rs = null;
		String dt="25-"+month;
		
		String query = " SELECT E.EMPNO FROM EMPMAST E,EMPTRAN T  WHERE E.EMPNO = T.EMPNO AND T.PRJ_SRNO ="+prjCode+
				" AND T.SRNO = (SELECT MAX(SRNO) FROM EMPTRAN T1 WHERE T1.EMPNO = E.EMPNO)"+
				" AND (( E.STATUS='A' AND E.DOJ <= '" +ReportDAO.EOM(dt)+"')   or" +
				" (E.STATUS ='N' And " +
				" E.DOL>='" +ReportDAO.BOM(dt)+"')) AND  " +
				" E.EMPNO not in (select Empno from sal_details S where S.SAL_STATUS = 'FINALIZED' AND S.SAL_MONTH='"+month+"')"+
				"   and E.EMPNO in (select distinct empno from paytran where trndt between '"+ReportDAO.BOM(dt)+"' and '"+ReportDAO.EOM(dt)+"' ) ORDER BY E.EMPNO";
		//System.out.println(query);
		
		con = ConnectionManager.getConnection();
		try
		{
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while(rs.next())
			{
				
				list.add(rs.getInt(1));
			}
			rs.close();
			st.close();
			con.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	
public  int getWeekOffOfSite(int year,String month,int site_id)
{
	Connection con= ConnectionManager.getConnection();
	ArrayList<WeekOffBean> weekList=new ArrayList<WeekOffBean>();
	WeekOffBean weekOffBean = null;
	try
	{
		Statement st = con.createStatement();
		ResultSet rs= null;
		String query="Select MONTH_WO,DAY,YEAR_WO from WEEKOFF Where  SITE_ID="+site_id+" " +
				"AND YEAR_WO="+year+" AND MONTH_WO='"+month+"'";
	//	System.out.println(query);
		rs= st.executeQuery(query);
		while(rs.next())
		{
			weekOffBean = new WeekOffBean();
			weekOffBean.setMonth(rs.getString("MONTH_WO"));
			weekOffBean.setDay(rs.getInt("DAY"));
			weekOffBean.setYear(rs.getInt("YEAR_WO"));
			
			weekList.add(weekOffBean);
		//	System.out.println("weekList.size()"+weekList.size());
		}
		/*else
		{
			weekOffBean = new WeekOffBean();
			weekOffBean.setEmpno(empno);
		}*/
		con.close();
	}
	catch(Exception e)
	{
		
			e.printStackTrace();
		
	}
	return weekList.size();
}
	
	public ArrayList<String> getWeekOffDays(int year,int month,int day){
		//System.out.println("date :"+day+"-"+month+"-"+year);
		 Calendar cal = new GregorianCalendar(year, month, 1);
		   SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
		   ArrayList<String> list=new ArrayList<String>();
		   for (int i = 0, inc = 1; i <31 && cal.get(Calendar.MONTH) ==month ; i+=inc) {

		   if (cal.get(Calendar.DAY_OF_WEEK) == day) {
			  list.add(format.format(cal.getTime()).substring(0, 2));
			  cal.add(Calendar.DAY_OF_MONTH, 7);   
		   } else {
			   cal.add(Calendar.DAY_OF_MONTH, 1);
		   }
		   }
		return list;
	}                                                                                                                                                                                                                                                                                                                                                                                                                                               
	
}
