package payroll.DAO;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import payroll.Model.DeductBean;
import payroll.Model.NotificationBean;
import payroll.Core.ReportDAO;
import payroll.Core.Utility;

public class NotifyHandler 
{

	public static SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
	
	public static boolean getNotify(String uid)
	{
		boolean flag= false;
		Connection con=ConnectionManager.getConnection();
		try
		{
					
			Statement st = con.createStatement();
			ResultSet result=st.executeQuery("select * from notification where status='A' and  empno="+uid);
			if(result.next())
			{
				flag= true;
			}
			else
			{
				flag= false;
			}
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}

	
	
	
	public static ArrayList<NotificationBean> getEMPNotifications(String uid)
	{
		ArrayList<NotificationBean> list=new ArrayList<NotificationBean>();
		Connection con=ConnectionManager.getConnection();
		try
		{
			NotificationBean bean=null;	
			Statement st = con.createStatement();
			ResultSet result=st.executeQuery("select ROW_NUMBER() OVER(partition BY empno,created_date order by empno,created_date) as srno,* from notification where status='A' and  empno="+uid);
			while(result.next())
			{
				bean=new NotificationBean();
				bean.setDisc(result.getString("disc"));
				bean.setCreated_by(result.getString("created_by"));
				bean.setCreated_date(sdf.format(result.getDate("created_date")));
				list.add(bean);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	
	
	public void insertNotification(NotificationBean bean)
	{
	
		Connection con=ConnectionManager.getConnection();
		try
		{
			
			
			if(bean.getType().equalsIgnoreCase("emp"))
			{
			
			}
			else if(bean.getType().equalsIgnoreCase("proj"))
			{
				
			}
			else if(bean.getType().equalsIgnoreCase("All"))
			{
				
			
			PreparedStatement pst1 = 
			con.prepareStatement("select u.roleid,u.USERID ,u1.User_ID,u1.EMP_id ,u1.user_name "+ 
								"from userroles u  , HarshConstruction_MAY2015.dbo.Users  u1 "+
								" where u1.User_ID= u.USERID and   u.ROLEID="+bean.getRoleid());
			ResultSet rs=pst1.executeQuery();
			
			while (rs.next())
			{
			
			PreparedStatement pst = con.prepareStatement("INSERT INTO NOTIFICATION VALUES(?,?,?,?,?)");
				
				pst.setString(1,rs.getString("EMP_ID"));
				pst.setString(2,bean.getDisc());
				pst.setString(3,bean.getCreated_by());
				pst.setString(4,bean.getCreated_date());
				pst.setString(5,bean.getStatus());
				pst.setString(6,bean.getRoleid());
				pst.setString(7,bean.getApplno());
				
				
				pst.execute();
				
			}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	
	}
}
