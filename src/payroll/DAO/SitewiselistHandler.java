package payroll.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import payroll.Model.AttendanceBean;
import payroll.Model.Listbean;



public class SitewiselistHandler {

	
	public  ArrayList<Listbean> getsitelist(String date1)  
	{
		ArrayList<Listbean> list =new ArrayList<Listbean>(); 
		// System.out.println("this is handlerrrrrr");
		
	    	 Connection conn=ConnectionManager.getConnection();
	    	 
	    			 //"Select Site_Name from Project_Sites where Site_ID ='" +list.Site_id()+"'";
	    
	    	 String lis="select emp.empcode,emp.salute,emp.empno,rtrim(emp.fname)+' '+rtrim(emp.mname)+' '+rtrim(emp.lname) name ,"+
					" EMP.DOB,EMP.DOJ,t.DESIG,t.PRJ_SRNO  "+
	                " from empmast emp,emptran t where emp.status = 'A' AND"+
	                 " t.srno = (SELECT MAX(E2.srno) FROM EMPTRAN E2 "+ 
		 "WHERE E2.EMPNO = emp.EMPNO AND E2.EFFDATE "+
		 "<= '"+date1+"')  and emp.empno = t.empno order by t.PRJ_SRNO,emp.empcode";
	    	System.out.println(lis);
	    	 
	    	 Statement stmt=null;
			try {
						stmt = conn.createStatement();
					 
			            ResultSet rs=stmt.executeQuery(lis);
			     while(rs.next())
			        {
		         Listbean l1=new Listbean();
	   //     System.out.println(date1);
	        l1.setEmpName(rs.getString("name")==null?"":rs.getString("name"));
		    l1.setEmpJd(rs.getDate("DOJ")==null?"":dateFormat(rs.getDate("DOJ")));
		    l1.setEmpdb(rs.getDate("DOB")==null?"":dateFormat(rs.getDate("DOB")));
		   // l1.setSiteName(rs.getString("sname")==null?"":rs.getString("sname"));
		    l1.setEmpCode (rs.getString("empcode") == null ? 0 :rs.getInt("empcode"));
		    l1.setPR(rs.getInt("PRJ_SRNO"));
		    //System.out.println("a :  thid is 2nd handler");
		  
	
		    list.add(l1);
	              }    
			     conn.close();	
			}
			 catch (SQLException e) 
			    {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 return list;
	}

	
	public  Listbean getsitelist1(int a)  
	{
		//System.out.println("this is 3rd handlerrrrrr");
		ArrayList<Listbean> list2 =new ArrayList<Listbean>();
		 Connection conn1=ConnectionManager.getConnectionTech();
     	 Statement stmt1=null;
   	     String lis1="Select Site_Name,Site_ID from Project_Sites where Site_ID="+a+"";
	//	System.out.println(lis1);
		 Listbean l2=null;
		 l2= new Listbean();
   	  try {
			stmt1 = conn1.createStatement();
		 
          ResultSet rs1=stmt1.executeQuery(lis1);
         
   while(rs1.next())
      {
	 
//System.out.println(date1);
   l2.setSiteName(rs1.getString("Site_Name")==null?"":rs1.getString("Site_Name")); 

   l2.setSite_id(rs1.getInt("Site_ID"));
   //System.out.println("a : "+l2.getSite_id());
//l1.setEmpno(rs.getString("empno")==null?0:rs.getInt("empno"));

   list2.add(l2);
    }    
   conn1.close();	
}
catch (SQLException e) 
  {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   	  
   	  
   	  
   	  
   	Iterator<Listbean> iterator = list2.iterator(); 
    while (iterator.hasNext()) {
        System.out.println(iterator.next());
    }
    
    
    
    
    
    
    
 
   	return l2;
   	     
   	     
	}
	
	
	
	private String dateFormat(Date date) {
		// TODO Auto-generated method stub
		String result="";
		if(date==null)
		{
			return "";
		}
		SimpleDateFormat format=new SimpleDateFormat("dd-MMM-yyyy");
		result=format.format(date);
		return result;
		//return null;
	}
	
}
	/*select emp.empcode,emp.salute,emp.empno,rtrim(emp.fname)+' '+rtrim(emp.mname)+' '+rtrim(emp.lname) name ,
				 EMP.DOB,EMP.DOJ,t.DESIG,t.PRJ_SRNO  
                 from empmast emp,emptran t where emp.status = 'A' AND
                  t.srno = (SELECT MAX(E2.srno) FROM EMPTRAN E2 
	 WHERE E2.EMPNO = emp.EMPNO AND E2.EFFDATE 
	 <= '31-oct-2016')  and emp.empno = t.empno order by t.PRJ_SRNO,emp.empcode */
