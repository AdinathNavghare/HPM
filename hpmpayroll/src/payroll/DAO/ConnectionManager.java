package payroll.DAO;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ConnectionManager
{
	static Connection conn;

	public static Connection getConnection()
	{
		try
		{
			 Properties prop = new Properties();
	         try
	         {
					//prop.load(new FileInputStream("src/Config.properties"));
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				    InputStream stream = classLoader.getResourceAsStream("Config.properties");
				    prop.load(stream);
			 }
	         catch (Exception e) 
	         {
					// TODO Auto-generated catch block
	        	 System.out.println("Error in Connection Manager "+e);
	         }
	 
	         String url = prop.getProperty("db_url");
	         String uname = prop.getProperty("db_uname");
			 String pwd = prop.getProperty("db_pwd");
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			try
			{
				conn = DriverManager.getConnection(url);
			}
			catch (SQLException ex)
			{
				System.out.println("Error in Connection Manager "+ex);
			}
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Error in Connection Manager "+e);
		}
		return conn;
	}
	
	public static Connection getConnectionTech()
	{
		try
		{
			 Properties prop = new Properties();
	         try
	         {
					//prop.load(new FileInputStream("src/Config.properties"));
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				    InputStream stream = classLoader.getResourceAsStream("Config.properties");
				    prop.load(stream);
			 }
	         catch (Exception e) 
	         {
					// TODO Auto-generated catch block
	        	 System.out.println("Error in Connection Manager "+e);
	         }
	 
	         String url = prop.getProperty("db_url_tech");
	         String uname = prop.getProperty("db_uname");
			 String pwd = prop.getProperty("db_pwd");
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			try
			{

				conn = DriverManager.getConnection(url);

			}
			catch (SQLException ex)
			{
				System.out.println("Error in Connection Manager "+ex);
			}
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Error in Connection Manager "+e);
		}
		return conn;
	}
	
	/* public static Connection getConnection() throws Exception
	    {
	          return getPooledConnection();
	    }

	    public static Connection getPooledConnection() throws Exception{
	       Connection conn = null;

	        try{
	          Context ctx = new InitialContext();
	          if(ctx == null )
	              throw new Exception("No Context");

	         Context envText = (Context)ctx.lookup("java:comp/env");
	         DataSource ds = (DataSource) envText.lookup("jdbc/PooledDB");
	         System.out.println("DS is..."+ds);

	          if (ds != null) {
	             conn = ds.getConnection();
	            return conn;
	          }else{
	              return null;
	          }

	        }catch(Exception e) {
	            e.printStackTrace();
	            throw e;
	        }
	    }*/
	
}