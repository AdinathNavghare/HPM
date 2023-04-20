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

EmployeeHandler emph = new EmployeeHandler();
EmployeeBean ebean = new EmployeeBean();
LookupHandler lookuph= new LookupHandler();

LookupHandler lkph= new LookupHandler();
ArrayList<TranBean> tranlist=new ArrayList<TranBean>();
int eno = (Integer)session.getAttribute("EMPNO");
String empName;
String date="";
String status="rejected";
date=request.getParameter("date")==null?ReportDAO.getSysDate():request.getParameter("date");
System.out.println("SIZE"+ReportDAO.getSysDate());
status=request.getParameter("status")==null?status="rejected":request.getParameter("status");
System.out.println("Statusss..."+status);
LeaveMasterHandler obj=new LeaveMasterHandler();
EmpOffHandler eoffhdlr = new EmpOffHandler();
EmpAttendanceHandler EAH=new EmpAttendanceHandler();
EmpOffBean eoffbn = new EmpOffBean();
eoffbn = eoffhdlr.getEmpOfficAddInfo(Integer.toString(eno));
RoleDAO obj1=new RoleDAO();
String roleId=obj1.getrole(Integer.parseInt(session.getAttribute("UID").toString()));

int site_id = eoffbn.getPrj_srno();
System.out.println("site is........."+site_id);
System.out.println("roleId is........."+roleId);

String siteName=eoffbn.getPrj_name();
session.setAttribute("Prj_Srno", site_id);
String disableaprove = request.getParameter("disableaprove")==null?"":request.getParameter("disableaprove");
String siteid = request.getParameter("siteid")==null?"":request.getParameter("siteid");
ArrayList<EmpOffBean> projlist= new ArrayList<EmpOffBean>();
ArrayList<AttendStatusBean> proj_attend_state = new ArrayList<AttendStatusBean>();
ArrayList<AttendStatusBean> proj_attend_state_site_admin = new ArrayList<AttendStatusBean>();

ArrayList <TranBean>siteList=new ArrayList<TranBean>();
EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
siteList=empAttendanceHandler.getAssignedSitesList(eno);
projlist=eoffhdlr.getprojectCode();

proj_attend_state =EAH.getAllAttendanceStatus(projlist,status,date,disableaprove,siteid);
System.out.println("SIZE"+proj_attend_state.size());
session.setAttribute("proj_list", projlist);

proj_attend_state_site_admin=EAH.getAllAttendanceStatus(projlist, status, eno, site_id, "0");
System.out.println("SIZE2"+proj_attend_state_site_admin.size()+" for status  "+status);

String select=new String();


float days=Calculate.getDays(date);
String action = request.getParameter("action")==null?"":request.getParameter("action");
System.out.println("action is..."+action);

TranHandler trnh= new TranHandler();
LookupHandler lkh=new LookupHandler();
ArrayList<Lookup> emplist=new ArrayList<Lookup>(); 
emplist=lkh.getSubLKP_DESC("ET");	// ET is Code for Employee Type

int flag=-1;
try
{  
	
	try
	{
	flag=Integer.parseInt(request.getParameter("flag")==null?"0":request.getParameter("flag")); 
	System.out.println("fg..."+flag);
	}catch(Exception e)
	{
		System.out.println("no flag value"+flag);
	}
	
}
catch(Exception e)
{
	System.out.println("First Time Loading");
}

 
%>
<%  SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); // today
// String today=format.format(c1.getTime());     /* -----for System date --------*/
	 
String today=EAH.getServerDate();     /* -----for Server date --------*/






%>


<script type="text/javascript">
function checkFlag() {
		var f = parseInt(document.getElementById("flag1").value);
		var leftrequest=document.getElementById("leftrequestsave").value;
		if (f == 1) {
			alert("Record Approved Successfully..!!!");

		}
		else if (f == 2) {
			alert("Record Rejected Successfully..!!!");
		}
		else if(leftrequest=="leftrequestsaved")
			{
			alert("Left Request Is Send Successfully To Admin ..!!!");
			}
		else if(leftrequest=="transferrequestsaved")
		{
		alert("Transfer Request Is Send Successfully To Admin ..!!!");
		}
		else if(leftrequest=="emptransfer")
			{
			alert("Transfer Request Approved Successfully..!!!");
			}
		else if(leftrequest=="SingleApprove")
		{
		alert("SingleApprove Request Approved Successfully..!!!");
		}
		
	}

function act(date)
{
	
	
	var r = confirm("Are you sure to send for Approval ?");
	if (r == true) {
		window.location.href="EmpAttendServlet?action=approval&date="+date;
	} 
	

}
function view_att(site,status,dt)
{
	
	window.location.href="EmpPresentSeat.jsp?site_id="+site+"&status="+status+"&date="+dt,"_self";
	
}
function view_att1(status,dt,srno,name,code)
{
	
	window.location.href="EmpPresentSeat.jsp?action=getdetails&status="+status+"&leftflag=0&prj="+srno+"&proj="+name+":"+code+"&date="+dt,"_self";
	
}

function view_att_2(status,empno,date,event){  
	var disableaprove=document.getElementById("disableapprove").value;
	
	if(disableaprove=="true")
		{
		window.location.href="newAttendSheet.jsp?status="+status+"&empno="+empno+"&date="+date+"&event="+event+"&aprove="+disableaprove+" ";
		}
	else
		{
	window.location.href="newAttendSheet.jsp?status="+status+"&empno="+empno+"&date="+date+"&event="+event+" ";
		}
}



function addNew()
{
	var $radio = $('input[name=selectSite]:checked');
	var updateDay = $radio.val();
	var id = $radio.attr('id');
	var site=<%=site_id%>;
	
	
	
	<%
	String todaysDate=empAttendanceHandler.getServerDate();
	Calendar calendar=Calendar.getInstance();
	SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy");
	calendar.add(Calendar.MONTH, 1);
	calendar.set(Calendar.DATE, 1);
	String nextMonthDate=SDF.format(calendar.getTime());
	
	
	Calendar cal=Calendar.getInstance();
	cal.add(Calendar.MONTH, -1);
	cal.set(Calendar.DATE, 1);

	String previousMonth= SDF.format(cal.getTime());
	
	%>
	
	var todayDate="<%=todaysDate.substring(3,11)%>";
	var MonthDate="<%=nextMonthDate.substring(3,11)%>"
		var previousDate="<%=previousMonth.substring(3,11)%>";
	var date=document.getElementById("adddate").value;
	var adddate=date.substring(3,11);
	
	
	

var flag=true;
if(date=="")
	{
	alert("Please select Date");
	flag= false;
	}
document.getElementById("process").hidden=false;
//alert(todayDate+adddate+MonthDate+previousDate);

if(todayDate==adddate|| MonthDate==adddate || previousDate==adddate){
if(flag==true)
{
<%	if(roleId.equals("1"))
{%>
window.location.href="EmpPresentSeat.jsp?date="+date+"&site_id="+site,"_self";
	<%}else{%>
window.location.href="EmpPresentSeat.jsp?date="+date+"&site_id="+id,"_self";
<%}%>
}
}
else{
	alert("You can not create the Attendance sheet of  "+date+"..!!!");
	return false;
}

}


function validate()
{
	/* var dt=document.getElementById("date").value; */
	var st=document.getElementById("state").value;
	var flag=true;
	var date=document.getElementById("date").value;
	if(date=="")
		{
		alert("Please select Date");
		document.getElementById("date").focus();
		flag= false;
		} 
	if(document.getElementById("state").value=="")
	{
	alert("Please select Status");
	flag= false;
	}
	
	if(flag==true)
		{
		window.location.href="attendanceMain.jsp?status="+st+"&date="+date;
		}
}
	</script>
<%
	 String pageName = "attendanceMain.jsp";
	try
	{
		ArrayList<String> urls = (ArrayList<String>)session.getAttribute("urls");
		if(!urls.contains(pageName))
		{
			response.sendRedirect("NotAvailable.jsp");
		}
	}
	catch(Exception e)
	{
		//response.sendRedirect("login.jsp?action=0");
	}
 

%>

</head>
<body onLoad="checkFlag()">
	<%@include file="mainHeader.jsp"%>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp"%>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Add Attendance</h1>
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
							<div id="table-content" style="height: 500px; overflow: auto;">
								<center>
								<div>
								<table id="customers">
								<tr>
								
										<td>For Month :</td>
								<td>
								<input name="date" size="20" id="date" value="<%=request.getParameter("date")==null?today:request.getParameter("date") %>" readonly="readonly"  type="text" onchange="validate()" >&nbsp;
									<img src="images/cal.gif" align="middle"	style="vertical-align: middle; cursor: pointer;"	onClick="javascript:NewCssCal('date', 'ddmmmyyyy')" />
								
								 </td>
									<td>Select Status :</td>
									<td><select id="state" name="state" onchange="validate()">
											<option value="">Select</option>
											<option value="saved" <%=status.equalsIgnoreCase("saved")?"Selected":"" %>>Saved </option>
											<option value="pending" <%=status.equalsIgnoreCase("pending")?"Selected":"" %>>Pending </option>
											<option value="approved" <%=status.equalsIgnoreCase("approved")?"Selected":"" %>>Approved </option>
											<option value="rejected" <%=status.equalsIgnoreCase("rejected")?"Selected":"" %>>Rejected </option>
												<option value="left" <%=status.equalsIgnoreCase("left")?"Selected":"" %>>Left</option>
													<option value="transfer" <%=status.equalsIgnoreCase("transfer")?"Selected":"" %>>Transfer</option>
													<option value="SingleApprove" <%=status.equalsIgnoreCase("SingleApprove")?"Selected":"" %>>Single Approve</option>
											<option value="all" <%=status.equalsIgnoreCase("all")?"Selected":"" %>>All </option>
										</select></td>
										
									
								</tr>
								
								</table>
								</div>
								<br><br>
								
								<table id="customers">
								<%if((status.equalsIgnoreCase("rejected")) || (status.equalsIgnoreCase("saved")) || (status.equalsIgnoreCase("pending")) || (status.equalsIgnoreCase("approved")) ){
								%>
								<tr>
									
									<%if(disableaprove.equalsIgnoreCase("true")){ %>
									<th>Emp Code</th><%}else{ %><th>Site Code</th><%} %>
									<th>Site Name</th>
									<th>Month</th>
									<th>Status</th>
									<th>Action</th>
								</tr>
								
								<%}
								if(status.equals("all")  ){
									%>
									<tr>
										
										<th>Site Code/Emp Code</th>
										<th>Site Name/Emp Name</th>
										<th>Month</th>
										<th>Status</th>
										<th>Action</th>
									</tr>
									<%}
						if(status.equals("left") || status.equals("transfer")  || status.equals("SingleApprove")){
							%>
							<tr>
									
									<th>Emp Code</th>
									<th>Employee Name</th>
									<th>Month</th>
									<th>Status</th>
									<!-- <th>Submit Date</th> -->
									<th>Action</th>
								</tr>
							<%
						}	
								int count=0;
								if(roleId.equals("1"))
								{
									for(EmpOffBean lkb :projlist)
									{
								
										for(AttendStatusBean asb :proj_attend_state)
										{
								
											if((asb.getSite_id()== lkb.getPrj_srno()) && (asb.getAppr_status().equalsIgnoreCase(status)))
											{
											
											%>
									<tr>
										
										<td><%=lkb.getPrj_code()%></td>
										<td><%=lkb.getPrj_name() %></td>
										<td><%=asb.getAtt_month() %></td>
										<td><%=asb.getAppr_status() %></td>
										
										<td>
									
										
										<%if(asb.getAppr_status().equalsIgnoreCase("pending"))
										{
											count++;
										%>	<input type="button" id="" value="View" onclick="view_att1('<%=asb.getAppr_status() %>','<%=asb.getAtt_month()%>','<%=lkb.getPrj_srno()%>','<%=lkb.getPrj_name() %>','<%=lkb.getPrj_code()%>')">
											<%	
										}
										else if( asb.getAppr_status().equalsIgnoreCase("")||asb.getAppr_status().equalsIgnoreCase("saved"))
										{
											count++;										
											%>
											<input type="button" id="" value="View" onclick="view_att1('<%=asb.getAppr_status() %>','<%=asb.getAtt_month()%>','<%=lkb.getPrj_srno()%>','<%=lkb.getPrj_name() %>','<%=lkb.getPrj_code()%>')">
											
											<%	
										}
										else if( asb.getAppr_status().equalsIgnoreCase("approved"))
										{
											count++;
											%>
											<input type="button" id="" value="View" onclick="view_att1('<%=asb.getAppr_status() %>','<%=asb.getAtt_month()%>','<%=lkb.getPrj_srno()%>','<%=lkb.getPrj_name() %>','<%=lkb.getPrj_code()%>')">
											 <font color="green">Approved</font>
											<%	
										}
										else if(asb.getAppr_status().equalsIgnoreCase("rejected"))
										{
											count++;
											%>
											<input type="button" id="" value="View" onclick="view_att1('<%=asb.getAppr_status() %>','<%=asb.getAtt_month()%>','<%=lkb.getPrj_srno()%>','<%=lkb.getPrj_name() %>','<%=lkb.getPrj_code()%>')">
											 <font color="red">Rejected</font>
											<%	
										}
										%></td>
									</tr>
										
										<%
										
										}
											else if(status.equalsIgnoreCase("all") && asb.getSite_id()== lkb.getPrj_srno())
											{						
												%>
												<tr>
													
													<td><%=lkb.getPrj_code()%></td>
													<td><%=lkb.getPrj_name() %></td>
													<td><%=asb.getAtt_month() %></td>
													<td><%=asb.getAppr_status() %></td>
													<td>
													<%if(asb.getAppr_status().equalsIgnoreCase("pending"))
														{
														count++;
														%>	<input type="button" id="" value="View" onclick="view_att1('<%=asb.getAppr_status() %>','<%=asb.getAtt_month()%>','<%=lkb.getPrj_srno()%>','<%=lkb.getPrj_name() %>','<%=lkb.getPrj_code()%>')">
											<%	
														}
													else if( asb.getAppr_status().equalsIgnoreCase("")||asb.getAppr_status().equalsIgnoreCase("saved"))
													{												
														count++;
														%>
														<input type="button" id="" value="View" onclick="view_att1('<%=asb.getAppr_status() %>','<%=asb.getAtt_month()%>','<%=lkb.getPrj_srno()%>','<%=lkb.getPrj_name() %>','<%=lkb.getPrj_code()%>')">
											
														<%	
													}
													
													else if( asb.getAppr_status().equalsIgnoreCase("approved"))
													{
														count++;
														%>
														<input type="button" id="" value="View" onclick="view_att1('<%=asb.getAppr_status() %>','<%=asb.getAtt_month()%>','<%=lkb.getPrj_srno()%>','<%=lkb.getPrj_name() %>','<%=lkb.getPrj_code()%>')">
											 <font color="green">Approved</font>
														<%	
													}
													else if(asb.getAppr_status().equalsIgnoreCase("rejected"))
													{
														count++;
														%>
														<input type="button" id="" value="View" onclick="view_att1('<%=asb.getAppr_status() %>','<%=asb.getAtt_month()%>','<%=lkb.getPrj_srno()%>','<%=lkb.getPrj_name() %>','<%=lkb.getPrj_code()%>')">
												 
														 <font color="red">Rejected</font>
														<%	
													}
													%></td>
												</tr>
												<%							}
											
										
											
										}
											
										
									}
									
									for(AttendStatusBean asb :proj_attend_state)
									{

										 if(asb.getSite_id()==0 && asb.getTranEvent().equalsIgnoreCase("left") ||asb.getSite_id()==0 && asb.getTranEvent().equalsIgnoreCase("transfer") ||asb.getSite_id()==0 && asb.getTranEvent().equalsIgnoreCase("SingleApprove"))
										 {
																					count++;
																					ebean = emph.getEmployeeInformation(Integer.toString(asb.getEmpno()));
																					
																					%>
																				
																			<tr>
																		
																		<td align="center"><%=ebean.getEMPCODE()%></td>
																		<td><%=lookuph.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getLNAME() %></td>
																		<td><%=asb.getAtt_month() %></td>
																	<%-- 	<td><%=asb.getSubmit_date() %></td> --%>
																		<td><%=asb.getAppr_status() %></td>
																		<td>	
																		<%
																						
																						 if(asb.getAppr_status().equalsIgnoreCase("pending")){	
																							 count++;
																					%><input type="button" id="" value="View" onclick="view_att_2('<%=asb.getAppr_status() %>','<%=asb.getEmpno() %>','<%=asb.getAtt_month() %>','<%=asb.getTranEvent() %>')" >
																				
																				
																				<%} else	if(asb.getAppr_status().equalsIgnoreCase("approved")){
																					count++;
																						%>
																						<input type="button" id="" value="View" onclick="view_att_2('<%=asb.getAppr_status() %>','<%=asb.getEmpno() %>','<%=asb.getAtt_month() %>','<%=asb.getTranEvent() %>')" >
																						 <font color="green">Approved</font>
																					<%}
																					else if(asb.getAppr_status().equalsIgnoreCase("rejected")){
																						count++;
																						%>
																					<input type="button" id="" value="View" onclick="view_att_2('<%=asb.getAppr_status() %>','<%=asb.getEmpno() %>','<%=asb.getAtt_month() %>','<%=asb.getTranEvent() %>')" >
																						 <font color="red">Rejected</font>
																				<%}
																						
												}
									}
								}
				
								else
								{
									for(EmpOffBean lkb :projlist)
									{
								
										for(AttendStatusBean asb :proj_attend_state_site_admin)
										{
								
											if(asb.getSite_id()== lkb.getPrj_srno() && asb.getAppr_status().equalsIgnoreCase(status))
											{
										
											%>
									<tr>
										
										<td><%=lkb.getPrj_code()%></td>
										<td><%=lkb.getPrj_name() %></td>
										<td><%=asb.getAtt_month() %></td>
										<td><%=asb.getAppr_status() %></td>
										
										<td>
									
										
										<%if(asb.getAppr_status().equalsIgnoreCase("pending"))
										{
											count++;
										%>	<input type="button" id="" value="View" onclick="view_att('<%=asb.getSite_id() %>','<%=asb.getAppr_status()%>','<%=asb.getAtt_month()%>')">
											<%	
										}
										else if( asb.getAppr_status().equalsIgnoreCase("")||asb.getAppr_status().equalsIgnoreCase("saved"))
										{
											count++;
											%>
											<input type="button" id="" value="View" onclick="view_att('<%=asb.getSite_id() %>','<%=asb.getAppr_status()%>','<%=asb.getAtt_month()%>')">
											
											<%	
										}
										else if( asb.getAppr_status().equalsIgnoreCase("approved"))
										{
											count++;
											%>
											<input type="button" id="" value="View" onclick="view_att('<%=asb.getSite_id() %>','<%=asb.getAppr_status()%>','<%=asb.getAtt_month()%>')">
											 <font color="green">Approved</font>
											<%	
										}
										else if(asb.getAppr_status().equalsIgnoreCase("rejected"))
										{
											count++;
											%>
											<input type="button" id="" value="View" onclick="view_att('<%=asb.getSite_id() %>','<%=asb.getAppr_status()%>','<%=asb.getAtt_month()%>')">
											 <font color="red">Rejected</font>
											<%	
										}
										%></td>
									</tr>
										
										<%
										
										}
											else if(status.equalsIgnoreCase("all") && asb.getSite_id()== lkb.getPrj_srno())
											{
												
												%>
												<tr>
													
													<td><%=lkb.getPrj_code()%></td>
													<td><%=lkb.getPrj_name() %></td>
													<td><%=asb.getAtt_month() %></td>
													<td><%=asb.getAppr_status() %></td>
													<td>
													<%if(asb.getAppr_status().equalsIgnoreCase("pending"))
														{
														count++;
														%>	<input type="button" id="" value="View" onclick="view_att('<%=asb.getSite_id() %>','<%=asb.getAppr_status()%>','<%=asb.getAtt_month()%>')">
											<%	
														}
													else if( asb.getAppr_status().equalsIgnoreCase("")||asb.getAppr_status().equalsIgnoreCase("saved"))
													{
														count++;
														%>
													<input type="button" id="" value="View" onclick="view_att('<%=asb.getSite_id() %>','<%=asb.getAppr_status()%>','<%=asb.getAtt_month()%>')">
											
														<%	
													}
													
													else if( asb.getAppr_status().equalsIgnoreCase("approved"))
													{
														count++;
														%>
											<input type="button" id="" value="View" onclick="view_att('<%=asb.getSite_id() %>','<%=asb.getAppr_status()%>','<%=asb.getAtt_month()%>')">
											 <font color="green">Approved</font>
														<%	
													}
													else if(asb.getAppr_status().equalsIgnoreCase("rejected"))
													{
														count++;
														%>
												<input type="button" id="" value="View" onclick="view_att('<%=asb.getSite_id() %>','<%=asb.getAppr_status()%>','<%=asb.getAtt_month()%>')">
												 
														 <font color="red">Rejected</font>
														<%	
													}
													%></td>
												</tr>
												<%							}
											
										
											
										}
											
										
									}
									
									for(AttendStatusBean asb :proj_attend_state_site_admin)
									{

										 if(asb.getSite_id()==0 && asb.getTranEvent().equalsIgnoreCase("left") ||asb.getSite_id()==0 && asb.getTranEvent().equalsIgnoreCase("transfer") ||asb.getSite_id()==0 && asb.getTranEvent().equalsIgnoreCase("SingleApprove"))
										 {
																					count++;
																					ebean = emph.getEmployeeInformation(Integer.toString(asb.getEmpno()));
																					
																					%>
																				
																			<tr>
																		
																		<td align="center"><%=ebean.getEMPCODE()%></td>
																		<td><%=lookuph.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getLNAME() %></td>
																		<td><%=asb.getAtt_month() %></td>
																	<%-- 	<td><%=asb.getSubmit_date() %></td> --%>
																		<td><%=asb.getAppr_status() %></td>
																		<td>	
																		<%
																						
																						 if(asb.getAppr_status().equalsIgnoreCase("pending")){	
																							 count++;
																					%><input type="button" id="" value="View" onclick="view_att_2('<%=asb.getAppr_status() %>','<%=asb.getEmpno() %>','<%=asb.getAtt_month() %>','<%=asb.getTranEvent() %>')" >
																				
																				
																				<%} else	if(asb.getAppr_status().equalsIgnoreCase("approved")){
																					count++;
																						%>
																						<input type="button" id="" value="View" onclick="view_att_2('<%=asb.getAppr_status() %>','<%=asb.getEmpno() %>','<%=asb.getAtt_month() %>','<%=asb.getTranEvent() %>')" >
																						 <font color="green">Approved</font>
																					<%}
																					else if(asb.getAppr_status().equalsIgnoreCase("rejected")){
																						count++;
																						%>
																					<input type="button" id="" value="View" onclick="view_att_2('<%=asb.getAppr_status() %>','<%=asb.getEmpno() %>','<%=asb.getAtt_month() %>','<%=asb.getTranEvent() %>')" >
																						 <font color="red">Rejected</font>
																				<%}
																						
												}
									}
								}
								
								if(count<1 )
								{
									%>
									
									<tr style="width: 50%;">
									
									<td style="width: 50%;" colspan="5"> Data not found !</td>
									
								</tr>
									
									
								<%	}%>
								
						
								</table>
								<div>
								<br><br>
								<table id="customers">
								<tr>
								<th colspan="2">ADD New Attendance Sheet</th>
								</tr>
								
								<tr>
							
								<%if(roleId.equals("1"))
									{%>
									<td>Select Date :</td>
								<td>
								<input name="adddate" size="20" id="adddate" value="<%=today %>" disabled="disabled" type="text">&nbsp;
									<img src="images/cal.gif" align="middle"	style="vertical-align: middle; cursor: pointer;"	onClick="javascript:NewCssCal('adddate', 'ddmmmyyyy')" />
								
								 </td>
								<%}
								else{
								 %><td align="center" "><font size="3">Today's Date : </font> <input name="adddate" size="20" id="adddate"  style="text-align: center;"  value="<%=today %>" disabled="disabled" type="text">&nbsp;</td></tr>
							
								
								<!-- <select name="selectSite" size="4" id="selectSite" > -->
							
									
										<%
										ArrayList<EmpOffBean> list= new ArrayList<EmpOffBean>();
										EmpOffHandler ofh = new EmpOffHandler();
			    						list=ofh.getprojectCode();
			    						int counter=1;
			    						int selected=0;
										
										
                                        siteList=empAttendanceHandler.getAssignedSitesList(eno);
                                       if(siteList.size()!=0){
                                    	   %>
                                    	   	<tr>	
                                    	   <td ><font size="3">Select&nbsp;Site : </font>   <br>  <br> 
                                    	    
                                    	    	<input type="radio"   name="selectSite"  id="<%=site_id%>" value="Official Site" checked="checked" size="4" value="<%=site_id%>"/>&nbsp;	<%=siteName %> 
                                    	   	<%
										for(EmpOffBean lkb :list)
    							{
    						for(TranBean tranBean:siteList){
    							if(lkb.getPrj_srno()==Integer.parseInt(	tranBean.getSite_id())){
    								if(lkb.getPrj_srno()!=site_id){  							
    							%> <br> <br>
    						<input type="radio" name="selectSite" size="4" id="<%=lkb.getPrj_srno()%>" value="<%=lkb.getPrj_name()%>"/>&nbsp;	<%=lkb.getPrj_name()%><br>
    				<%-- 	 	<input type="radio"   style="display: none;" name="selectSite"  id="<%=site_id%>" value="Official Site" checked="checked" size="4" value="<%=site_id%>"/>  --%>
    						<br>	
    							<%-- <option  title="<%=lkb.getPrj_code()%>" ><%=lkb.getPrj_name()%></option> --%>
    					<% 	}} }}
    					
                                    }
    					else{%> 
    						
    				
    						<input type="radio"   style="display: none;" name="selectSite"  id="<%=site_id%>"  checked="checked" size="4" value="<%=site_id%>"/>
    						<br>	
    					<%}%>
    					
									</select>
									
									</td>
									</tr>
								 <%} %>
								 
								</tr>
								
								<tr>
								<td align="center" colspan="2"><input type="button" value="ADD" onclick="addNew()" /></td>
							
								</tr>
								</table>
								
								</div>
								
								
								</center>
								<input type="hidden" name="disableapprove" id="disableapprove" value="<%=disableaprove%>">
								<input type="hidden" name="flag1" id="flag1" value="<%=flag%>">
								</div>
								
								
								<input type="hidden" name="leftrequestsave" id="leftrequestsave" value="<%=request.getParameter("flag")==null?"NOSAVED":request.getParameter("flag")%>">
								
								<div id="process"  style="z-index: 1000;background-color:white;height: 100%;width: 100%;top: 0px;left: 0px;position: fixed;opacity:0.8;" hidden=true; >
								<div align="center" style="padding-top: 20%;">
				
								<h1> Calculation takes 5 to 10 Min.....For First Time of Create Attendance Be Patience </h1>
				
								<img alt="" src="images/process.gif">
								</div>
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
								
