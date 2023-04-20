<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.jfree.ui.about.ProjectInfo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page import="payroll.Core.ReportDAO"%>
<%@page import="payroll.Core.Calculate"%>
<%@page import="java.awt.Desktop.Action"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="payroll.Model.*"%>
<%@page import="payroll.DAO.*"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy; DTS3</title>
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />
<link rel="stylesheet" href="css/datePicker.css" type="text/css"
	media="screen" title="default" />	
<script type="text/javascript" src="js/jquery.datePicker.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>

<script src="js/jquery/jquery.autocomplete.js"></script>
<script type="text/javascript">
function getdate()
{
	var e = document.getElementById('dt').value;
	//var res=e.split(":");
	if(e=="")
		{
		alert("Please Enter Date...!");
		}
	else
		{
		window.location.href = "salInCalculation.jsp?dt="+e;
		
		}
}

</script> 

<title>Salary In Calculation</title>
</head>

<body onLoad="checkFlag()">
<%
String date=" ";
   date=request.getParameter("dt")==null?ReportDAO.getSysDate():request.getParameter("dt");
   date=ReportDAO.EOM(date);
 //list1=st.getsitelist(date);
 
 Connection conn=ConnectionManager.getConnection();
 String list="select distinct emp.empcode,emp.empno,rtrim(emp.fname)+' '+rtrim(emp.mname)+' '+rtrim(emp.lname) name ,"+
		 "EMP.DOJ"+  
         " from empmast emp,PAYTRAN p where emp.EMPNO = p.EMPNO AND p.TRNDT ='"+date+"' order by emp.EMPCODE ";

	  Statement stmt=conn.createStatement();
	 ResultSet emp=stmt.executeQuery(list);
     
 %>
	<%@include file="mainHeader.jsp"%>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp"%>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Salary In Calculation</h1>
			</div>
			<!-- end page-heading -->

			<table border="0" width="100%" cellpadding="0" cellspacing="0" id="content-table">
				<tr>
					<th rowspan="3" class="sized"><img
						src="images/shared/side_shadowleft.jpg" width="20" height="300"
						alt="" /></th>
					<th class="topleft"></th>
					<td id="tbl-border-top">&nbsp;</td>
					<th class="topright"></th>
					<th rowspan="3" class="sized"><img
						src="images/shared/side_shadowright.jpg" width="20" height="300"
						alt="" /></th>
				</tr>
				<tr>
					<td id="tbl-border-left"></td>
					<td>
						<!--  start content-table-inner ...................................................................... START -->
						<!--  <div id="content-table-inner">  -->
 						<div   id="content-table-inner" align="center" class="imptable" style="overflow-y:auto; height: 330px;width: 102%;">
 				     <center>
								
								<table id="customers">
                         <td>Select Date<font color="red"><b style="margin-left: 5px;"></b></font>
                         <td>
                         <input name="dt" size="20" id="dt" value="" disabled="disabled" type="text">&nbsp;
									<img src="images/cal.gif" align="middle"	style="vertical-align: middle; cursor: pointer;"	onClick="javascript:NewCssCal('dt', 'ddmmmyyyy')" />
								
                         
                        
				         <input  type="submit" value="Submit"   class="myButton"  id="ans" name="ans"  onclick="getdate()"> 			</td>
                         </td>
                         </table>
                       
                        
                         <div align="center">
     <center>                    
<table id="customers">
<th colspan="4"> Employee in Calculation for month : <%=date.substring(3,11)%></th>

<tr>
<th width="70">EMP CODE</th><th width="300">EMP NAME</th><th width="175">EMP JOINING DATE</th>
<!-- <th width="90">EMP BIRTH DATE</th>
 --></tr>
 
</table>
</center>
</div>
<!-- <div>
<table> -->
<%
while (emp.next()) 
{
	%>
	   <center>
 <table id="customers">
 <tr>
<td align="center"  width="70">  <%=emp.getInt("empcode") %></td>
<td align="center"  width="300">  <%=emp.getString("name") %></td>
<td align="center"  width="175"> <%=emp.getDate("DOJ") %></td>
<%-- <td align="center"  width="90"> <%=emp.getDate("DOB") %></td> --%>
</tr>

 </table>
 </center>
<!--  </div>
 -->



<%		    
}
emp.close();
stmt.close();
conn.close();
 %>
 
			
	

			
<!-- </div>
 -->							     
							
							
							<!--  end table-content  -->

							<div class="clear"></div>
                  </center>
						</div> 
						<!--  end content-table-inner ............................................END  -->
					</td>
					<td id="tbl-border-right"></td>
				
				
				<tr>
					<th class="sized bottomleft"></th>
					<td id="tbl-border-bottom">&nbsp;</td>
					<th class="sized bottomright"></th>
				</tr>
			</table>
			<div class="clear">&nbsp;</div>

		</div>
		<!--  end content -->
		<div class="clear">&nbsp;</div>
	</div>
	<!--  end content-outer........................................................END -->

	<div class="clear">&nbsp;</div>


</body>
</html>
