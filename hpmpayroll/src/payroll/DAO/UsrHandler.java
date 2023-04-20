package payroll.DAO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import payroll.Controller.mailServlet;
import payroll.Model.UserMasterBean;
import payroll.Model.UsrMast;

public class UsrHandler 
{
	public boolean addUser(UsrMast user)
	{
		boolean result = false;
		Connection con = null;
		try
		{
			con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			String maxID ="(SELECT MAX(USERID)+1 FROM USRMAST)";
			String sql = "INSERT INTO USRMAST VALUES( "
					+maxID+", "
					+user.getEMPID()+", "
					+"'"+user.getUNAME()+"', "
					+"'"+user.getUPWD()+"', "
					+"'"+user.getUCREATEDATE()+"', "
					+"'"+user.getUMODDATE()+"', "
					+"'"+user.getUMODUID()+"', "
					+"'"+user.getUSTATUS()+"'"
					+")";				
				
			st.execute(sql);
			result = true;
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
			e.printStackTrace();
		}
		return result;
	}
	
	public	ArrayList<UsrMast> getUserList()
	{
		ArrayList<UsrMast> ulist=new ArrayList<>();
		ResultSet rst=null;
		String userdetails="SELECT * FROM Users";
		//String userdetails="select * from USRMAST  where USERID not in (select USERID from USERROLES)";
		Connection conn=null;
		try
		{
			conn = ConnectionManager.getConnectionTech();
			Statement st = conn.createStatement();
			rst=st.executeQuery(userdetails);
			while(rst.next())
			{
				String user_Name =rst.getString("User_FirstName")+" "+rst.getString("User_Lastname");
				//System.out.println(""+user_Name);
				UsrMast bean=new UsrMast();
				bean.setUSERID(rst.getString("User_ID")!=null?rst.getInt("User_ID"):0);
				bean.setEMPID(rst.getString("Emp_ID")!=null?rst.getInt("Emp_ID"):0);
				bean.setUNAME(user_Name);
				ulist.add(bean);
			}
			conn.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ulist;
	}	
	
	public void addUserLog(String uid,String sessionID)
	{
		try
		{
			Connection con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM USERLOGS WHERE USERID='"+uid+"'");
			if(rs.next())
			{
				st.executeUpdate("UPDATE USERLOGS SET SESSIONID='"+sessionID+"' WHERE USERID='"+uid+"'");
			}
			else
			{
				st.executeUpdate("INSERT INTO USERLOGS VALUES('"+uid+"', '"+sessionID+"')");
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getSessionID(String uid)
	{
		String result = "";
		try
		{
			Connection con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM USERLOGS WHERE USERID='"+uid+"'");
			if(rs.next())
			{
				result = rs.getString("SESSIONID");
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public void removeLog(String uid,String sessionID)
	{
		try
		{
			Connection con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			st.executeUpdate("DELETE FROM USERLOGS WHERE USERID='"+uid+"' AND SESSIONID='"+sessionID+"'");
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public  byte [] decryptBASE64 (String key) throws Exception {
		return (new BASE64Decoder()). decodeBuffer (key);
	}
		  
	public  String encryptBASE64 (byte [] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}
	
	/*
	String data = "swap";//.encryptBASE64 ("GetBytes ()");
	String data1 = A.encryptBASE64(data.getBytes("UTF-8"));
	byte [] ByteArray = A.decryptBASE64 (data1);
	*/
	
	public UserMasterBean updatePwdOnForgot(String uname,String emailId){
		Connection conn=null;
		String rdm = null;
		UserMasterBean usermasterbean = new UserMasterBean();
		try {
			conn = ConnectionManager.getConnectionTech();
			Statement st = conn.createStatement();
			rdm = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
			try {
				rdm = encryptBASE64(rdm.getBytes("UTF-8")).trim();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			st.executeUpdate("UPDATE USERS SET User_Password='"+rdm+"',User_Modified_Date = (SELECT GETDATE())  WHERE USER_ID = (SELECT USER_ID FROM USERS WHERE USER_NAME = '"+uname+"' OR User_Email_ID = '"+emailId+"' )");
			Statement stUsr = conn.createStatement();
			ResultSet rs = stUsr.executeQuery("SELECT * FROM USERS WHERE USER_ID = (SELECT USER_ID FROM USERS WHERE USER_NAME = '"+uname+"' OR User_Email_ID = '"+emailId+"' )");
			if(rs.next())
			{
				usermasterbean.setIsValid(true);
				usermasterbean.setUsername(rs.getString("User_Name"));
				
				try {
					byte[] pwd = decryptBASE64(rs.getString("User_Password"));
					usermasterbean.setUserpwd(new String(pwd));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				usermasterbean.setUser_Email_ID(rs.getString("User_Email_ID"));
			}else{
				usermasterbean.setIsValid(false);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usermasterbean;
		
	}
	
	public  String sendEmailForgotPwd(UserMasterBean userMBean) throws IOException
    {
    			 
		 // sets SMTP server properties
		 Properties properties = new Properties();
		 properties.put("mail.smtp.host", mailServlet.host);
		 properties.put("mail.smtp.port", mailServlet.port);
		 properties.put("mail.smtp.auth", "true");
		 properties.put("mail.smtp.starttls.enable", "true");
		 properties.put("mail.user", mailServlet.mailFrom);
		 properties.put("mail.password", mailServlet.password);
		 String Status = null;
		        // creates a new session with an authenticator
		 Authenticator auth = new Authenticator() 
		 {
			 public PasswordAuthentication getPasswordAuthentication() 
			 {
				 return new PasswordAuthentication(mailServlet.mailFrom, mailServlet.password);
			 }
		 };
		 Session session = Session.getInstance(properties, auth);
		 
		 // creates a new e-mail message
		 Message msg = new MimeMessage(session);
		 try
		 {
			 msg.setFrom(new InternetAddress(mailServlet.password));
			 InternetAddress[] toAddresses = { new InternetAddress(userMBean.getUser_Email_ID()) };
			 msg.setRecipients(Message.RecipientType.TO, toAddresses);
			 msg.setSubject("Password Details of HR HARSH CONSTRUCTION");
			 msg.setSentDate(new Date());
			 
			 // creates message part
			 MimeBodyPart messageBodyPart = new MimeBodyPart();
			 messageBodyPart.setContent("User Name is "+userMBean.getUsername()+"  Password Is "+userMBean.getUserpwd()+" Please Change Your Password After Login", "text/html");
			 	
			 // creates multi-part
			 Multipart multipart = new MimeMultipart();
			 multipart.addBodyPart(messageBodyPart);
			 
			 // adds attachments
			 
			 // sets the multi-part as e-mail's content
			 msg.setContent(multipart);
		        
			 // sends the e-mail
			 Transport.send(msg);
			
		       
			
			 Status = "Password Send Successfully On Your Email Id ";
		 }
		 catch (Exception ex) 
		 {
		     Status = "Error in Password Sending  ";
			 ex.printStackTrace();
		 }
		return Status;
    }
	public int updatePassword(String uname, String pass, String newPass, String empid)
	{
		int flag = 0;
		Connection con=ConnectionManager.getConnectionTech();
		try
		{
			Statement st=con.createStatement();
			Statement stUP=con.createStatement();
			
			try {
				pass = encryptBASE64(pass.getBytes("UTF-8")).trim();
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ResultSet rs = st.executeQuery("Select * from USERS where USER_NAME='"+uname+"' and User_Password='"+pass+"'");
			if(rs.next())
			{
				try {
					newPass = encryptBASE64(newPass.getBytes("UTF-8")).trim();
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stUP.executeUpdate("UPDATE USERS SET User_Password='"+newPass+"', User_Modified_Date = (SELECT GETDATE()) WHERE USER_NAME='"+uname+"' AND EMP_ID="+empid+"");
				flag = 1;	//Success
			}
			else
			{
				flag = 2;	// Wrong Current Password
			}
			con.close();
		} 
		catch (SQLException e) 
		{
			flag = 3;	//Error
			e.printStackTrace();
		}
		return flag;
	}
	public	ArrayList<UsrMast> getUser(String uid)
	{
		ArrayList<UsrMast> ulist=new ArrayList<>();
		ResultSet rst=null;
		
		String userdetails="SELECT * FROM Users WHERE USER_ID='"+uid+"'";
		//String userdetails="select * from USRMAST  where USERID not in (select USERID from USERROLES)";
		Connection conn=null;
		try
		{
			conn = ConnectionManager.getConnectionTech();
			Statement st = conn.createStatement();
			rst=st.executeQuery(userdetails);
			while(rst.next())
			{
				String user_Name =rst.getString("User_FirstName")+" "+rst.getString("User_Lastname");
				//System.out.println(""+user_Name);
				UsrMast bean=new UsrMast();
				bean.setUSERID(rst.getString("User_ID")!=null?rst.getInt("User_ID"):0);
				bean.setEMPID(rst.getString("Emp_ID")!=null?rst.getInt("Emp_ID"):0);
				bean.setUNAME(user_Name);
				ulist.add(bean);
			}
			conn.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ulist;
	}
}