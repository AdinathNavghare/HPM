<%@page import="payroll.DAO.EmployeeHandler"%>
<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.DAO.LookupHandler"%>
<%@page import="payroll.Model.LMB"%>
<%@page import="java.util.ArrayList"%>
<%@page import="payroll.DAO.LMH"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sanction Leave</title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script type="text/javascript">
jQuery(function() {
	$("#EMPNO").autocomplete("list.jsp");
});  
</script>

<script>
jQuery(function() {
	$("#EMPNO1").autocomplete("list.jsp");
});
</script>
<style type="text/css">
.ac_results {
	padding: 0px;
	border: 1px solid #84a10b;
	background-color: #84a10b;
	overflow: hidden;
}

.ac_results ul {
	width: 100%;
	list-style-position: outside;
	list-style: none;
	padding: 0;
	margin: 0;
}

.ac_results li {
	margin: 0px;
	padding: 2px 5px;
	cursor: default;
	display: block;
	color: #fff;
	font-family: verdana;
	cursor: pointer;
	font-size: 12px;
	line-height: 16px;
	overflow: hidden;
}

.ac_loading {
	background: white url('../images/indicator.gif') right center no-repeat;
}

.ac_odd {
	background-color: #84a10b;
	color: #ffffff;
}

.ac_over {
	background-color: #5a6b13;
	color: #ffffff;
}

.input_text {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	border: 1px solid #84a10b;
	padding: 2px;
	width: 150px;
	color: #000;
	background: white url(../images/search.png) no-repeat 3px 2px;
	padding-left: 17px;
}

#accordion {
	list-style: none;
	padding: 0 0 0 0;
	width: 170px;
}

#accordion li {
	display: block;
	background-color: #2c6da0;
	font-weight: bold;
	margin: 1px;
	cursor: pointer;
	padding: 35 35 35 7px;
	list-style: circle;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
	border-radius: 10px;
	height: 30px;
	text-decoration: none;
}

#accordion li:hover {
	background-color: #74ACFF;
}

#accordion ul {
	list-style: none;
	padding: 0 0 0 0;
	display: none;
}

#accordion ul li {
	font-weight: normal;
	cursor: auto;
	padding: 0 0 0 7px;
}

#accordion a {
	text-decoration: none;
	color: #FFFFFF;
}

#accordion a:hover {
	text-decoration: underline;
	text-shadow: #FF9927;
}

#accordion ul li.submenu {
	background-color: #66CCFF;
}

#accordion ul li.submenu:hover {
	background-color: #74ACFF;
}
</style>
<script language="javascript">
	function validate() {

		var fromDate = document.searchappl.frmdate.value;
		var toDate = document.searchappl.todate.value;
		fromDate = fromDate.replace(/-/g, "/");
		toDate = toDate.replace(/-/g, "/");
		var str1=document.searchappl.EMPNO.value;
		var str2=document.searchappl.EMPNO1.value;
		var num1 =str1.split(":");
		var num2 =str2.split(":");
		var sno1="";
		var sno2="";
		for (var i = 0; i < num1.length; i++)
		{
			sno1=num1[i];
		}
		for (var j = 0; j < num2.length; j++)
		{
			sno2=num2[j];
		}
		if (document.searchappl.frmdate.value == "") {
			alert("please select From Date");
			document.searchappl.frmdate.focus();
			return false;
		}
		if (document.searchappl.todate.value == "") {
			alert("please enter the To Date");
			document.searchappl.todate.focus();
			return false;
		}
		var d1 = new Date(fromDate);
	    var d2 = new Date(toDate);

		if (d1.getTime() > d2.getTime()) {
			alert("                       Invalid Date Range!\n\n From Date can't be greater than TO Date!");
			document.searchappl.todate.focus();
			return false;
		}
		var EMPNO = document.getElementById("EMPNO").value;
        if (document.getElementById("EMPNO").value == "") {
			alert("Please Enter Employee Name");
			document.getElementById("EMPNO").focus();
			return false;
		}
		var atpos=EMPNO.indexOf(":");
		if (atpos<1)
		  {
		  alert("Please Select Correct Employee Name");
		  return false;
		  }

		var EMPNO1 = document.getElementById("EMPNO1").value;
        
		if (document.getElementById("EMPNO1").value == "") {
			alert("Please Insert Employee Name");
			document.getElementById("EMPNO1").focus();
			return false;
		}
		var atpos=EMPNO1.indexOf(":");
		if (atpos<1)
		  {
		  alert("Please Select Correct Employee Name");
		  return false;
		  }
		if (document.searchappl.type.value == "Default") {
			alert("Please select Leave Detail.!");
			document.searchappl.type.focus();
			return false;
		}
	}

		var xmlhttp;
		var url="";
		if(window.XMLHttpRequest)
		{
			xmlhttp=new XMLHttpRequest;
		}
		else //if(window.ActivXObject)
		{   
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		function Sanction(action,empno,appno)
		{
			var flag = confirm("Are you Sure to Sanction ?");
			if(flag)
			{
				url="LMS?action="+action+"&empno="+empno+"&appNo="+appno;
				
				xmlhttp.onreadystatechange=function()
				{
					if(xmlhttp.readyState==4)
					{
						var response = xmlhttp.responseText;
						if(response=="true")
						{
							alert("Leave Sactioned Successfully.");
							window.location.href="sanctionLeave.jsp";
						}
						else
						{
							alert("Insufficient Leave Balance.!");
						}
					}
				};
				xmlhttp.open("POST", url, true);
				xmlhttp.send();
			}
		}
		
		function Cancel(action,empno,appno)
			{
				var flag = confirm("Are you Sure to Cancel ?");
				if(flag)
				{
					url="LMS?action="+action+"&empno="+empno+"&appNo="+appno;
					
					xmlhttp.onreadystatechange=function()
					{
						if(xmlhttp.readyState==4)
						{
							var response = xmlhttp.responseText;
							if(response=="true")
							{
								alert("Leave Canceled Successfully.");
								window.location.href="sanctionLeave.jsp";
							}
							else
							{
								alert("failed, try Again!");
							}
						}
					};
					xmlhttp.open("POST", url, true);
					xmlhttp.send();
				}
			}
</script>
</head>

<%
LMH leaveHandler = new LMH();
LMB lBean = new LMB();
LookupHandler lookuph= new LookupHandler();
ArrayList<LMB> getSearchList = new ArrayList<LMB>();
LMB lBeanSearchFilter = new LMB();
String action = request.getParameter("action")==null?"NA":request.getParameter("action");
String error = request.getParameter("error")==null?"":request.getParameter("error");
if(action.equalsIgnoreCase("NA")){
lBean.setSTATUS("PENDING");
session.setAttribute("leaveSearchFilter", lBean);
	getSearchList = (ArrayList<LMB>) leaveHandler.getLeaveAppList(lBean,"ALL");
}else{
	getSearchList = (ArrayList<LMB>) request.getAttribute("customSearchList");
	lBeanSearchFilter = (LMB)session.getAttribute("leaveSearchFilter");
}
%>
<body>
<%@include file="mainHeader.jsp"%>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp"%>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" >
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Leave Details</h1>
	</div>
	<!-- end page-heading -->

	<table border="0" width="100%" cellpadding="0" cellspacing="0" id="content-table">
	<tr>
		<th rowspan="3" class="sized"><img src="images/shared/side_shadowleft.jpg" width="20" height="300" alt="" /></th>
		<th class="topleft"></th>
		<td id="tbl-border-top">&nbsp;</td>
		<th class="topright"></th>
		<th rowspan="3" class="sized"><img src="images/shared/side_shadowright.jpg" width="20" height="300" alt="" /></th>
	</tr>
	<tr>
		<td id="tbl-border-left"></td>
		<td>
		<!--  start content-table-inner ...................................................................... START -->
		<div id="content-table-inner">
		
			<!--  start table-content  -->
			<div id="table-content" align="center" style="overflow-y:auto; max-height:520px; ">
			
								<form name="searchappl" action="LMS?action=customSearch"
										method="post" onSubmit="return validate()">
										<table id="customers" style="margin-top:10px;float: none;">
											<tr>
												<th colspan="4">Search Leave Application</th>
											</tr>
											<tr class="alt">
												<td>FROM DATE&nbsp; <font color="red"><b>*</b></font></td>
												<td><input name="frmdate" size="20" id="frmdate"
													type="text" onBlur="if(value=='')" readonly="readonly"> &nbsp;<img
													src="images/cal.gif" align="middle" value=<%=lBeanSearchFilter.getFRMDT() %>
													style="vertical-align: middle; cursor: pointer;"
													onClick="javascript:NewCssCal('frmdate', 'ddmmmyyyy')" />
												</td>
												<td>TO DATE&nbsp; <font color="red"><b>*</b></font></td>
												<td><input name="todate" size="20" id="todate"
													type="text" onBlur="if(value=='')" readonly="readonly">&nbsp;<img
													src="images/cal.gif" align="middle" value=<%=lBeanSearchFilter.getTODT() %>
													style="vertical-align: middle; cursor: pointer;"
													onClick="javascript:NewCssCal('todate', 'ddmmmyyyy')" /></td>
											</tr>

											<tr class="alt">
												<td>FROM EMPNO&nbsp; <font color="red"><b>*</b></font>	</td>
												<td><input type="text" name="EMPNO" id="EMPNO" value="<%=lBeanSearchFilter.getEMPNO() %>"
													size="40" title="Enter Employee No"></td>
												<td>TO EMPNO&nbsp; <font color="red"><b>*</b></font></td>
												<td><input type="text" name="EMPNO1" id="EMPNO1" value="<%=lBeanSearchFilter.getEMPNO2() %>"
													size="40" title="Enter Employee No"></td>
											</tr>
											<tr class="alt">
												<td>DETAILS&nbsp; <font color="red"><b>*</b></font></td>
												<td colspan="3"><select name="type">
														<option value="Default">Select</option>
														<option value="All">All</option>
														<option value="sanction">sanction</option>
														<option value="pending">pending</option>
												</select> &nbsp;&nbsp;&nbsp;&nbsp; <input type="submit"	value="Search" /></td>
												
											</tr>
										</table>
									</form>
								
								<div align="center">
								<br/>
								<h3 style="color: red;"><%=error %></h3>
								<h3>Leave Applications</h3>
										<table id="customers" style="float: none;">
											<tr>

														<th width="50">EMP ID</th>
														<th >EMP NAME</th>
														<th width="90">APP DATE</th>
														<th width="90">LEAVE TYPE</th>
														<th width="85">FROM DATE</th>
														<th width="95">TO DATE</th>
														<th width="50">DAYS</th>
														<th width="60">TRAN_TYPE</th>
														<th width="130">SANCTION / CANCEL</th>
											</tr>
											<%		EmployeeHandler emph = new EmployeeHandler();
													EmployeeBean ebean = new EmployeeBean();
											
															if (getSearchList.size() != 0) {

																	for (LMB sancb : getSearchList) {
																		ebean = emph.getEmployeeInformation(Integer.toString(sancb.getEMPNO()));
														%>
											<tr align="center">
															<td width="50"><%=ebean.getEMPCODE()%></td>
															<td ><%=lookuph.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getLNAME() %></td>
															<td width="85"><%=sancb.getTRNDATE()%></td>
															<td width="80"><%=lookuph.getLKP_Desc("LEAVE",sancb.getLEAVECD())%></td>
															<td width="80"><%=sancb.getFRMDT()%></td>
															<td width="80"><%=sancb.getTODT()%></td>
															<td width="50"><%=sancb.getNODAYS()%></td>
															<% if(sancb.getTRNTYPE() == 'C'){ %>
																<td>Credit</td>
																<%}else{ %>
																<td>Debit</td>
																<%} %>
															<td>
															<%
															if(sancb.getSTATUS().equalsIgnoreCase("PENDING")){ %>
															<%-- <form id="my_form" method="post" action="LMS">
															
																<input type="hidden" name="action" value="sanctionLeaveApp">
	    														<input type="hidden" name="appNo" value="<%=sancb.getAPPLNO()%>">
	    														<input type="hidden" name="empno" value="<%=sancb.getEMPNO()%>">
	    														<input style="float: left;" type="button" onclick="document.getElementById('my_form').submit();" value="Sanction">
														    	<!-- <a href="javascript:{}" onclick="document.getElementById('my_form').submit();">Sanction</a> -->
      													   </form> --%>		   
																
														   		<input type="button" value="Sanction" onclick="Sanction('sanctionLeaveApp','<%=sancb.getEMPNO()%>','<%=sancb.getAPPLNO()%>')"/>&nbsp;
														   		<input type="button" value="Cancel" onclick="Cancel('cancelLeaveApp','<%=sancb.getEMPNO()%>','<%=sancb.getAPPLNO()%>')"/>
														   		
														   <%-- <form id="cancel_Form" method="post" action="LMS">
															
																<input type="hidden" name="action" value="cancelLeaveApp">
	    														<input type="hidden" name="appNo" value="<%=sancb.getAPPLNO()%>">
	    														<input type="hidden" name="empno" value="<%=sancb.getEMPNO()%>">
	    														<input style=" margin-left:10px; float: left;" type="button" onclick="document.getElementById('cancel_Form').submit();" value="Cancel">
            													<!-- <a href="javascript:{}" onclick="document.getElementById('cancel_Form').submit();">Cancel</a> -->
      													   </form> --%>
															<%
															}else{
															%>
															<%= sancb.getSTATUS() %>
													    	</td>
														
												</tr>
													<%}}}else { %>
												<tr>
														<td colspan="9" align="left">No Applications To Display</td>
												</tr>
												<%} %>
										</table>
			                   </div>   							
			</div>
			<!--  end table-content  -->
	
			<div class="clear"></div>
		 
		</div>
		<!--  end content-table-inner ............................................END  -->
		</td>
		<td id="tbl-border-right"></td>
	</tr>
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