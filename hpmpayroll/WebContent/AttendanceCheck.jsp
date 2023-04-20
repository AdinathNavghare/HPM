<%@page import="payroll.DAO.CheckAttendance"%>
<%@page import="payroll.DAO.ConnectionManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

 <% 
	try{      
		 String s[] = null;
		 String fname = "";
		 String EMPNO = "";
		 String EMPCODE = "";
		 String LNAME = "";
		 String STATUS = "";
		 String MNAME="";
		 Connection con = null;
		 
		 Connection conn = null;
		 con = ConnectionManager.getConnection();
		 Statement stcheck = con.createStatement(); 
		 ResultSet rscheck = stcheck.executeQuery("select EMPNO from EMPMAST where dol between '01-jun-2016' and '30-jun-2016'    or dol is null order by empno");
		
	   	 String date = "01-jun-2016";
	   
	   //System.out.println( date.substring(3,11));
	    
			while(rscheck.next()) 
 			{
				// method to call storeProcedure.  CheckAttendance.java
				CheckAttendance.count(rscheck.getInt("EMPNO"));
				
 			}  
			
			
			
			//jQuery related end	
		
			
 		rscheck.close(); 
 		stcheck.close(); 
		con.close();

		} 
		catch(Exception e){ 
 			e.printStackTrace(); 
 		}

//www.java4s.com
 %>