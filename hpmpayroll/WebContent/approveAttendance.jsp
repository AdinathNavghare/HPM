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
<script src="js/MONTHPICK/jquery.js"></script>
    <script type="text/javascript" src="js/MONTHPICK/jquery-ui.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="css/MONTHPICK/jquery-ui.css">

<%

EmployeeHandler emph = new EmployeeHandler();
EmployeeBean ebean = new EmployeeBean();
LookupHandler lookuph= new LookupHandler();
LookupHandler lkph= new LookupHandler();
ArrayList<TranBean> tranlist=new ArrayList<TranBean>();
int eno = (Integer)session.getAttribute("EMPNO");
String empName;
String date="";
String status="pending";
date=request.getParameter("date")==null?ReportDAO.getSysDate():request.getParameter("date");
status=request.getParameter("status")==null?status="pending":request.getParameter("status");
LeaveMasterHandler obj=new LeaveMasterHandler();
EmpOffHandler eoffhdlr = new EmpOffHandler();
EmpAttendanceHandler EAH=new EmpAttendanceHandler();
EmpOffBean eoffbn = new EmpOffBean();
eoffbn = eoffhdlr.getEmpOfficAddInfo(Integer.toString(eno));

ArrayList<EmpOffBean> projlist= new ArrayList<EmpOffBean>();
ArrayList<AttendStatusBean> proj_attend_state = new ArrayList<AttendStatusBean>();
projlist=eoffhdlr.getprojectCode();
proj_attend_state =EAH.getAllProjectAttendanceStatus(projlist,status,date);
session.setAttribute("proj_list", projlist);

int site_id = eoffbn.getPrj_srno();
session.setAttribute("Prj_Srno", site_id);

String select=new String();


float days=Calculate.getDays(date);
String action = request.getParameter("action")==null?"":request.getParameter("action");

RoleDAO obj1=new RoleDAO();
String roleId=obj1.getrole(Integer.parseInt(session.getAttribute("UID").toString()));

TranHandler trnh= new TranHandler();
LookupHandler lkh=new LookupHandler();
ArrayList<Lookup> emplist=new ArrayList<Lookup>(); 
emplist=lkh.getSubLKP_DESC("ET");	// ET is Code for Employee Type

int flag=-1;
try
{  
	
	try
	{
	flag=Integer.parseInt(request.getParameter("flag")==null?"":request.getParameter("flag")); 
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
  <style>
    .ui-datepicker-calendar {
        display: none;
        }
    </style>


<style type="text/css">
.row:hover {
	background-color: #CCCCCC;
	cursor: pointer;
}

.style1 {
	color: #FF0000
}
</style>
<script type="text/javascript">


$(function() {
    $('.date-picker').datepicker( {
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        dateFormat: 'M-yy',
        onClose: function(dateText, inst) { 
            var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
            var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
            $(this).datepicker('setDate', new Date(year, month, 1));
            $(':focus').blur();
            
        },
        beforeShow : function(input, inst) {
            var datestr;
            if ((datestr = $(this).val()).length > 0) {
                year = datestr.substring(datestr.length-4, datestr.length);
                month = jQuery.inArray(datestr.substring(0, datestr.length-5), $(this).datepicker('option', 'monthNamesShort'));
                $(this).datepicker('option', 'defaultDate', new Date(year, month, 1));
                $(this).datepicker('setDate', new Date(year, month, 1));
                
            }
        }
    });
    
});

function checkFlag() {
		var f = parseInt(document.getElementById("flag1").value);

		if (f == 1) {
			alert("Record Approved Successfully");

		}
		else if (f == 2) {
			alert("Record Rejected Successfully");
		}
		else if (f == 100) {
			alert("Salary Calculation Of This Month Has't Started Yet !");
		}
		
		
	}
	
function view_att(prj,dt)
{
	
	window.location.href="EmpPresentSeat.jsp?prj="+prj+"&date="+dt,"_self";
	
}

function approve_att(prj,dt)
{
	/* var r = confirm("Are you sure to Approval ?");
	if (r == true) {
		window.location.href="EmpAttendServlet?action=approved&prj="+prj+"&date="+dt,"_self";
	}  */

checkpending(prj,dt);


	
	function checkpending(prj,fordate)
	{
		var flag="";
		var xmlhttp = new XMLHttpRequest(); 
		url="EmpAttendServlet?action=checkpendingrequest&date="+fordate+"&siteid="+prj;
		
		xmlhttp.onreadystatechange=function()
		{
			if(xmlhttp.readyState==4)
			{
				var response1 = xmlhttp.responseText;
			
				if(response1=="true")
					{
						alert("There is pending request remains for this month... first Approve Or Reject it & then approved attendance sheet...!!!");
						var cnfrm = confirm("Do you want to Approve/Reject This pending request.?");
						if(cnfrm == true)
							{
							//flag ="true";
							window.location.href="attendanceMain.jsp?status=pending&date="+fordate+"&disableaprove=true&siteid="+prj;
							}
						else
						{
							return flag;;
							}
						
					}else
					{
						flag ="false";
						var r = confirm("Are you sure to Approve?");
						if (r == true) 

						window.location.href="EmpAttendServlet?action=approved&status=approved&prj="+prj+"&date="+fordate,"_self";

					}
				
			}
			
		};
		xmlhttp.open("POST", url, true);
		xmlhttp.send();
		
		/* return flag; */
	};	
				
			
			
/* 	if(stat=="true")
	{

	//window.location.href="attendanceMain.jsp?status=pending&date="+fordate+"&disableaprove=true&siteid="+prj;
	}
	else */
	/* alert("site.."+prj+"..stat"+stat);
	if(stat=="false")
	{

	var r = confirm("Are you sure to Approve?");
	if (r == true) 

	window.location.href="EmpAttendServlet?action=approved&status=approved&prjCode="+prj+"&date="+fordate,"_self";

	} */
	
	
}	

function reject_att(prj,dt)
{
	var r = confirm("Are you sure to Reject ?");
	if (r == true) {
		window.location.href="EmpAttendServlet?action=reject&prj="+prj+"&date="+dt,"_self";
	} 

}	

function view_att_2(status,empno,date,event){
	window.location.href="newAttendSheet.jsp?status="+status+"&empno="+empno+"&date="+date+"&event="+event+" ";
}


function validate()
{
	/* var dt=document.getElementById("date").value; */
	var st=document.getElementById("state").value;
	var date=document.getElementById("date").value;
	var flag=true;

	if(date=="")
		{
		alert("Please select Date");
		flag= false;
		} 
	if(document.getElementById("state").value=="")
	{
	alert("Please select Status");
	flag= false;
	}
	
	if(flag==true)
		{
		window.location.href="approveAttendance.jsp?status="+st+"&date="+date;
		}
}
	</script>
<%
	 String pageName = "approveAttendance.jsp";
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
				<h1>Attendance Approval</h1>
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
									
								<td>For Month : &nbsp; <font color="red"><b>*</b></font></td>
												<td><input name="date" value="<%=request.getParameter("date")==null?ReportDAO.getSysDate():request.getParameter("date") %>" size="20" id="date"
													type="text" onBlur="if(value=='')" readonly="readonly" onchange="validate()">&nbsp;<img
													src="images/cal.gif" 
													style="vertical-align: middle; cursor: pointer;"
													onClick="javascript:NewCssCal('date', 'ddmmmyyyy')" /></td>
												
									<td>Select Status :</td>
									<td><select id="state" name="state" onchange="validate()">
											<option value="">Select</option>
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
								<%if(status.equals("rejected") || status.equals("saved") ||status.equals("pending") || status.equals("approved")  ){
								%>
								<tr>
									
									<th>Site Code</th>
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
						if(status.equals("left") || status.equals("transfer")  || status.equals("SingleApprove") ){
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
								for(EmpOffBean lkb :projlist)
								{
									for(AttendStatusBean asb :proj_attend_state)
									{
									
									
									
									
										if(asb.getSite_id()== lkb.getPrj_srno() && asb.getAppr_status().equalsIgnoreCase(status))
										{
										
											
										%>
								<tr>
									
									<td><%=lkb.getPrj_code()%></td>
									<td><%=lkb.getPrj_name() %></td>
									<td><%=asb.getAtt_month() %></td>
									<td><%=asb.getAppr_status()%></td>
									<td>
									<%if(asb.getAppr_status().equalsIgnoreCase("pending"))
									{
										count++;
									%>	<input type="button" id="" value="View" onclick="view_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
										<%if(roleId.equals("1"))
										{%>
										<input type="button" id="" value="Reject" onclick="reject_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
										<input type="button" id="" value="Approved" onclick="approve_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
									<%	
									}}
									else if( asb.getAppr_status().equalsIgnoreCase("")||asb.getAppr_status().equalsIgnoreCase("saved"))
									{
										%>
										
										<%	
									}
									
									else if( asb.getAppr_status().equalsIgnoreCase("approved"))
									{
										count++;
										%>
										<input type="button" id="" value="View" onclick="view_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
										 <font color="green">Approved</font>
										<%	
									}
									else if(asb.getAppr_status().equalsIgnoreCase("rejected"))
									{
										count++;
										%>
										<input type="button" id="" value="View" onclick="view_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
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
												<td><%=asb.getAppr_status()%></td>
												<td>
												<%if(asb.getAppr_status().equalsIgnoreCase("pending"))
												{
													count++;
												%>	<input type="button" id="" value="View" onclick="view_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
													<%if(roleId.equals("1"))
														{%>
													<input type="button" id="" value="Reject" onclick="reject_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
													<input type="button" id="" value="Approved" onclick="approve_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
												<%	
												}}
												else if( asb.getAppr_status().equalsIgnoreCase("")||asb.getAppr_status().equalsIgnoreCase("saved"))
												{
													%>
													
													<%	
												}
												
												else if( asb.getAppr_status().equalsIgnoreCase("approved"))
												{
													count++;
													%>
													<input type="button" id="" value="View" onclick="view_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
													 <font color="green">Approved</font>
													<%	
												}
												else if(asb.getAppr_status().equalsIgnoreCase("rejected"))
												{
													count++;
													%>
													<input type="button" id="" value="View" onclick="view_att('<%=lkb.getPrj_srno()%>','<%=asb.getAtt_month()%>')">
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

									 if(asb.getSite_id()==0 && asb.getTranEvent().equalsIgnoreCase("left") ||asb.getSite_id()==0 && asb.getTranEvent().equalsIgnoreCase("transfer") || asb.getSite_id()==0 && asb.getTranEvent().equalsIgnoreCase("SingleApprove") )
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
								if(count<1)
								{
									%>
									
									<tr style="width: 50%;">
									
									<td style="width: 50%;" colspan="5"> Data not found !</td>
									
								</tr>
									<%
								}
								%>
								
								
								
								
								
								
								</table>
								</center>
								<input type="hidden" name="flag1" id="flag1" value="<%=flag%>">
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
								