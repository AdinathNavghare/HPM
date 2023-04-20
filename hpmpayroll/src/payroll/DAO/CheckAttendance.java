package payroll.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckAttendance {

	
	public static void count(int empno){
		
		
		Connection con = null;
		 
		 Connection conn = null;

		
	try {
		java.sql.CallableStatement cst1;
		conn = ConnectionManager.getConnection();
		cst1 = conn.prepareCall("{call SPC_CHECKING_SUNDAY_ABSENT(?,?,?) }");
	
	
	
	
	cst1.setString(1,"01-jun-2016");
	cst1.setString(2,"30-jun-2016");
	cst1.setInt(3,empno)  ;  
	
	ResultSet rs = cst1.executeQuery();
	ResultSet name=null;
	Statement stname=null;
	
	
	
	    while (rs.next()) {
	    	
	    	
	    	
	    	
	    	stname = conn.createStatement();
	    	
	        String EMPLOYEENUMBER = rs.getString(1);
	        String ATTENDANCE_COUNT = rs.getString(2);
	        
	       // System.out.println("Generated employeeId: " + EMPLOYEENUMBER+" Attendance Count  "+ATTENDANCE_COUNT);
	        
	        System.out.println( ATTENDANCE_COUNT);
	        
	        
	        //for name only
	        
	        /*String query="SELECT E.EMPNO,CONVERT(NVARCHAR(100),E.LNAME+' '+ E.FNAME+' '+ E.MNAME) as name from empmast e where e.empno ="+EMPLOYEENUMBER+" ";
	      // System.out.println("query " +query);
	        name=stname.executeQuery(query);
	        
	        while(name.next())
	        {
	        	 System.out.println(name.getString("name"));
	        	
	        }
	        */
	        
	        
	        
	        
	        
	        
	      } 
	
	
	
	
	
	
	
	
	
	
	
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
		
		
		
		
		
		
	}
	
	
	
	
}
