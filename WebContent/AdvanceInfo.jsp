<%@page import="payroll.Model.AdvanceBean"%>
<%@page import="payroll.DAO.AdvanceHandler"%>
<%@page import="payroll.DAO.LookupHandler"%>
<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.DAO.EmployeeHandler"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.jfree.ui.about.ProjectInfo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page import="java.awt.Desktop.Action"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
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


 <%

	AdvanceHandler advanceHandler=new AdvanceHandler();
	String Empno=request.getParameter("empno")==null?"":request.getParameter("empno");
	String ApplicationNo=request.getParameter("appNo")==null?"":request.getParameter("appNo");
	
	ArrayList<AdvanceBean> GetAdvanceList= new ArrayList<AdvanceBean>();
	
	GetAdvanceList=advanceHandler.getAdvanceInfo(Empno,ApplicationNo);
	session.setAttribute("GetAdvanceList",GetAdvanceList);

	%>
	
	
	
<script type="text/javascript" >


</script>


</head>
<body  >
	<%@include file="mainHeader.jsp"%>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp"%>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Advance Info</h1>
			</div>
			<!-- end page-heading -->

			<table border="0" width="100%" cellpadding="0" cellspacing="0"
				id="content-table">
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
						<div id="content-table-inner">

							<!--  start table-content  -->
							<div id="table-content"  class="imptable"style="height: 300px; overflow: auto;">
							<center>
						       <a href="sanctionAdvance.jsp">Click Here To Go On Advance Approval Page</a>
								<table id="customers" >
								<tr ><th colspan="4"> Advance Detail</th> </tr>
								<%
								EmployeeHandler emph = new EmployeeHandler();
								EmployeeBean ebean = new EmployeeBean();
								EmployeeBean ebean1 = new EmployeeBean();
								EmployeeBean ebean2 = new EmployeeBean();
								LookupHandler lookuph= new LookupHandler();
								String monthyear="";
								for(AdvanceBean advanceBean : GetAdvanceList) {
								
								ebean = emph.getEmployeeInformation(Integer.toString(advanceBean.getEmpNo()));
								String mon=advanceBean.getForMonth();
					    		String year=mon.substring(0,4);
					    		String month=mon.substring(5,7);
					    		if(month.equals("01"))
					    			month="Jan";
					    		else if(month.equals("02"))
					    			month="Feb";
					    		else if(month.equals("03"))
					    			month="Mar";
					    		else if(month.equals("04"))
					    			month="Apr";
					    		else if(month.equals("05"))
					    			month="May";
					    		else if(month.equals("06"))
					    			month="Jun";
					    		else if(month.equals("07"))
					    			month="Jul";
					    		else if(month.equals("08"))
					    			month="Aug";
					    		else if(month.equals("09"))
					    			month="Sep";
					    		else if(month.equals("10"))
					    			month="Oct";
					    		else if(month.equals("11"))
					    			month="Nov";
					    		else
					         		month="Dec";
					    		
					    	monthyear=month+"-"+year;
					    
								if(advanceBean.getRequestStatus().equalsIgnoreCase("Sanction") ||advanceBean.getRequestStatus().equalsIgnoreCase("Cancel") ){
								ebean1 = emph.getEmployeeInformation(Integer.toString(advanceBean.getSanctionBy()));
								}
								ebean2 = emph.getEmployeeInformation(Integer.toString(advanceBean.getCreatedBy()));
								%>
								
								<tr><td>Application No :</td> <td> <%=advanceBean.getApplNo()%></td> <td> Employee Name :</td> <td><%=lookuph.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getLNAME() %></td>  
								 </tr>	
								<tr><td> Requested Amount :</td> <td><%=advanceBean.getAdvanceAmtRequested()%> </td>   </tr>
								<tr><td>Request Status :</td> <td><%=advanceBean.getRequestStatus()%> </td> <td> For Month :</td> <td> <%=monthyear%></td></tr>
								<tr><td>Request Date :</td> <td> <%=advanceBean.getRequestDate() %></td> 
								<td>Request By:</td> <td><%=lookuph.getLKP_Desc("SALUTE", ebean2.getSALUTE())+" "+ ebean2.getFNAME()+" "+ebean2.getLNAME()%> </td>   </tr>
								
								<tr><td>Sanctioned Amount:</td> <td><%=advanceBean.getSanctionAmt()%> </td> 
								
								<td> Action By:</td>
								
								<% 	if(advanceBean.getRequestStatus().equalsIgnoreCase("SANCTION")||advanceBean.getRequestStatus().equalsIgnoreCase("CANCEL")){%>
								 <td><%=lookuph.getLKP_Desc("SALUTE", ebean1.getSALUTE())+" "+ ebean1.getFNAME()+" "+ebean1.getLNAME()%> </td>  
								<%} else{%>
								<td></td>
								<%} %>
								 </tr>
								<tr>  <td>Action Date :</td> <td><%=advanceBean.getSanctionDate()%> </td> <td> </td> <td></td>   </tr>
								
								
								<%} %>
								</table>
							</center>
						</div>		
							<!--  end table-content  -->

							<div class="clear"></div>

						</div> <!--  end content-table-inner ............................................END  -->
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
								
