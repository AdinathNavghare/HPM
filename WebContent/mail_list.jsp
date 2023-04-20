<%@page import="payroll.Model.EmpAddressBean"%>
<%@page import="payroll.DAO.EmpAddrHandler"%>
<%@page import="payroll.DAO.ConnectionManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<html>
<body>
 <% 
	try{ 
		
		 
		 String mail = "";
		 
		
	    	
	    	EmpAddrHandler empAdd = new EmpAddrHandler();
	    	ArrayList<EmpAddressBean> list = new ArrayList<EmpAddressBean>();
	    	list = empAdd.getEmpAddress(request.getParameter("empno"));
	    	for(EmpAddressBean bean : list){
	    		mail=bean.getADDR3();
	    		break;
	    	}
 		
 		%>
	    	<%=mail.trim()%>	
<%
		} 
		catch(Exception e){ 
 			e.printStackTrace(); 
 		}

//www.java4s.com
 %>
 </body>
 </html>