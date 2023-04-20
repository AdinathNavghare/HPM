<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@page import="java.util.ArrayList"%>
<%@page import="payroll.Model.*"%>
<%@page import="payroll.DAO.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />
<link rel="stylesheet" href="css/datePicker.css" type="text/css"
	media="screen" title="default" />	
</head>
<body>
<%String action=request.getParameter("action")==null?"":request.getParameter("action");
ArrayList<EmployeeBean> list2=null;
ArrayList<EncashmentBean> elist3=new ArrayList<EncashmentBean>() ;
String checkbox[]=request.getParameterValues("list");
list2 =(ArrayList<EmployeeBean>)session.getAttribute("EmpEncashList");
String year=(String)session.getAttribute("year");
year=year+"-06-01";
System.out.println("action is..."+action);
System.out.println("in editencashjsp...year is"+year);
System.out.println(" in check empno..."+Integer.parseInt(checkbox[0]));
System.out.println(" in checkbox lenth ..."+checkbox.length);
           if(action.equalsIgnoreCase("editrecord"))
			{ 
			%>
			
				
			<form action="EncashServlet?action=updateEncash" method="post">
				
				<br><br><br><br>
				<center>
				<h1>Edit Records For Encashment. </h1>
				
				<br><br>
				<table name="updatetable" width="700" id="customers">
		        <tr>
		         
		             <th width="50">Sr No</th>
		              <th width="50">Emp code</th>
		             <th width="200">User Name</th>
				 <th width="100">OnHO</th>
				  <th width="100">LeaveOnHO</th>
				   <th width="100">OnOSite</th>
				    <th width="100">LeaveOnOsite</th>
				  <th width="100">Input_Amount</th>
			<th width="100">Encashment</th>
			           
		         </tr>
		<%
	
		int i=0;
		EncashmentHandler enh=new EncashmentHandler();
		EncashmentBean ebn;
		for( int j=0;j<=checkbox.length-1; j++)
		{
			System.out.println(" in check ..");
			ebn=new EncashmentBean();
			ebn.setEmpCode(Integer.parseInt(checkbox[j]));
			ebn.setYear(year);
			elist3.add(ebn);
			System.out.println(" in check empno..."+Integer.parseInt(checkbox[j]));
		}
			elist3=enh.geteditrecord(elist3);
			session.setAttribute("updateEncashlist",elist3);	
			  /* System.out.println("value in check..."+checkbox[j]);
				for(EncashmentBean tbn : list2){
					
			String eno1=(checkbox[j]); 
			if(eno1.equalsIgnoreCase(eb.getEMPCODE()))
			{ */ 
				
				 /* eb=new EmployeeBean();
				System.out.print("tbnn.."+tbn.getEMPNO());
				eb.setEMPCODE(eno1);
	 			
	 			System.out.print("ebbbb..."+eb.getEMPNO());
	 			list3.add(eb);   */
	 			for(EncashmentBean tbn:elist3)
				{
	 	 		%>
			
				<tr> 
					<td width="50" align="center"><%=++i %>  </td>
					<td width="50" align="center"><%=tbn.getEmpCode() %></td>
					<td width="200"><%=tbn.getName() %>  </td> 
					 <td><input type="text" placeholder="YYYY-MM-DD" name="noOfDaysHo" value="<%=tbn.getOnHo() %>" /></td>
					<td width="80" align="center"><%=tbn.getLeaveOnHO() %></td>
					<td><input type="text" placeholder="YYYY-MM-DD" name="noOfDaysOs" value="<%=tbn.getOnOsite() %>" /></td>
					<td width="80" align="center"><%=tbn.getLeaveOnOS() %></td>
					<td width="80" align="center"><%=tbn.getBasic()%></td>
					<td width="80" align="center"><%=tbn.getEncashment()%></td>
			</tr>
		<% 
	 				
		 	
			}
			 
	 			
	 %>
		 </table>
		<input id="save" type="submit" value="Save"  />
			
		 </center>
		 </form>
 <%} %>

</body>
</html>