<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.Model.LeaveEncashmentBean"%>
<%@page import="payroll.DAO.LookupHandler"%>
<%@page import="payroll.DAO.EmployeeHandler"%>
<%@page import="payroll.DAO.LeaveEncashmentHandler"%>
<%@page import="payroll.Model.ProjectBean"%>
<%@page import="payroll.DAO.ProjectListDAO"%>
<%@page import="payroll.DAO.*"%>
<%@page import="payroll.Model.*"%>
<%@page import="payroll.Model.TranBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="payroll.DAO.EmpAttendanceHandler"%>
<%@page import="payroll.DAO.EmpOffHandler"%>
<%@page import="payroll.Model.EmpOffBean"%>
<%@page import="payroll.DAO.RoleDAO"%>
<%@page import="payroll.Core.ReportDAO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page errorPage="error.jsp" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy Leave Encashment </title>
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="default" />
	<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<style type="text/css">
.row:hover {
	background-color: #CCCCCC;
	cursor: pointer;
}

.style1 {
	color: #FF0000
}


.ac_results {
	padding: 0px;
	border: 1px solid #cacaca;
	background-color: #f5f6fa;
	overflow: hidden;
	width: 15.9% !important;
}

</style>




<% 
RoleDAO obj1=new RoleDAO();
String roleId=obj1.getrole(Integer.parseInt(session.getAttribute("UID").toString()));
int eno = (Integer)session.getAttribute("EMPNO");
EmpOffHandler eoffhdlr = new EmpOffHandler();
EmpOffBean eoffbn = new EmpOffBean();
eoffbn = eoffhdlr.getEmpOfficAddInfo(Integer.toString(eno));
int site_id = eoffbn.getPrj_srno();
EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
String today=empAttendanceHandler.getServerDate();

%>

<script>
	
	<%if(roleId.equals("1")){%>
	jQuery(function() {
		
		
		$("#EMPNO").autocomplete("list.jsp");
	});
	
	$("#EMPNAME").autocomplete("list.jsp");
	
	
	jQuery(function() {
		$("#pp").autocomplete("projlist.jsp");
	});
		
	<%}
	else{
	%>
	jQuery(function() {
		var proj=<%=site_id%>;
		$("#EMPNO").autocomplete("ProjectEmpList.jsp?prj_srno="+proj);
		$("#EMPNAME").autocomplete("ProjectEmpList.jsp?prj_srno="+proj);
	});
	
	
	<%}%>
</script>
<script type="text/javascript">

function loancal() {

	window.location.href = "printEncashment.jsp";

}


function inputLimiter(e,allow) {
	  var AllowableCharacters = '';
	  if (allow == 'Numbers'){AllowableCharacters='1234567890.';}
	  var k;
	  k=document.all?parseInt(e.keyCode): parseInt(e.which);
	  if (k!=13 && k!=8 && k!=0){
	    if ((e.ctrlKey==false) && (e.altKey==false)) {
	      return (AllowableCharacters.indexOf(String.fromCharCode(k))!=-1);
	    } else {
	      return true;
	    }
	  } else {			  
	    return true;
	  }
	}	



function checkleave() {



	var f = parseInt(document.getElementById("flag1").value);

	if (f == 1) {
		alert(" Saved Successfully");

	}
	
	if (f == 3) {
		alert("Added Successfully");
		window.location.href = "leaveEncashment.jsp?action=getdetails&prj="
				+ prjCode;
	}
	if (f == 2) {
		alert("Record is not added.Please apply again");

	}

}


function getTranDetails() {
	
	var proj=document.getElementById("pp").value;
	
			var res = proj.indexOf(":"); 
			if(proj=="")
				{			
				alert("Please Select Project !");
				}
			else
				{
				if(res<0)
					{
					//alert("Please Select Project !");
					document.getElementById("pp").value="";
					document.getElementById("pp").focus();
					}
				else
					{
			var p=proj.split(":");
			var prjCode = p[3];
			
				if(prjCode == ""){
					
				}
			
			else{
				
		proj=proj.replace(/ & /g," and ");
				window.location.href = "leaveEncashment.jsp?action=getdetails&prj="+prjCode;

			}
				}
			}
			
		}

function validate()
{

	    var limit=document.forms["leaveform"]["limit"].value;
	    if(limit ==""){
	 	   alert("Enter the Max Limit.!");
	 	   document.forms["leaveform"]["limit"].focus();
	 	   return false;
	    }
	    
	    var lbal=document.forms["leaveform"]["lbal"].value;
	    if(lbal ==""){
	 	   alert("Enter the Leave Balance!");
	 	   document.forms["leaveform"]["lbal"].focus();
	 	   return false;
	    }
	    
	    var encash=document.forms["leaveform"]["encash"].value;
	    if(encash ==""){
	 	   alert("Enter The Encashment Applicable!");
	 	   document.forms["leaveform"]["encash"].focus();
	 	   return false;
	    }
	    
	    var lsanction=document.forms["leaveform"]["lsanction"].value;
	    if(lsanction ==""){
	 	   alert("Enter The Leave Encashment Sanction!");
	 	   document.forms["leaveform"]["lsanction"].focus();
	 	   return false;
	    }
	     
	    var date=document.leaveform.edate.value;
	    if(date == "" || date == null){
	 	   alert("Select The Encashment Date.!");
	 	  document.leaveform.edate.focus();
	 	   return false;
	    }
	    

	    var fromdate=document.leaveform.frmdate.value;
	    var todate=document.leaveform.todate.value;

	    fromdate = fromdate.replace(/-/g,"/");
	    todate = todate.replace(/-/g,"/");
	 
		
	     
	    fromdate = fromdate.replace(/-/g,"/");
	    todate= todate.replace(/-/g,"/");
	  
				if(fromdate == "" || todate== ""){
					 alert("please select Date.!");
					 return false;
			 }
				
	    
	    var d1 = new Date(fromdate);	 	
	 	var d2 =new  Date(todate);
	 	 if (d1.getTime() > d2.getTime())
		 {
			   alert("Invalid Date Range!\n FromDate can't be greater than TODate!");
			   document.leaveform.todate.focus();
			   return false;
		  }
	    
	    var gross=document.forms["leaveform"]["gross"].value;
	    if(gross ==""){
	 	   alert("Enter The Monthly Gross !");
	 	   document.forms["leaveform"]["gross"].focus();
	 	   return false;
	    }
	    
	    var esic=document.forms["leaveform"]["esic"].value;
	    if(esic ==""){
	 	   alert("Enter The Esic Amount !");
	 	   document.forms["leaveform"]["esic"].focus();
	 	   return false;
	    }
	    
	    var amt=document.forms["leaveform"]["ENCASHAMT"].value;
	    if(amt ==""){
	 	   alert("Enter The Encashment Amount !");
	 	   document.forms["leaveform"]["ENCASHAMT"].focus();
	 	   return false;
	    }
	    /* business logic validations     */
	    if(parseFloat(encash)>parseFloat(limit)){
	    	alert("Encashment Applicable should not exceed Max limit ");
	    	document.forms["leaveform"]["encash"].focus();
	 	   return false;
	    }
	    
	    if(parseFloat(lsanction)>parseFloat(limit)){
	    	alert("Leave Encashment Sanction  should not exceed Max limit ");
	    	document.forms["leaveform"]["lsanction"].focus();
	 	   return false;
	    }
}

<%int prjCode = 0;
int flag=-1;

try
{
flag=Integer.parseInt(request.getParameter("flag")==null?"":request.getParameter("flag")); 
}catch(Exception e)
{
	if( request.getParameter("prj")==null)
	{
	session.setAttribute("prjCode", "");
	}
}
String action = request.getParameter("action")==null?"":request.getParameter("action");
ArrayList<TranBean> projEmpNmlist = new ArrayList<TranBean>();
ArrayList<TranBean> projEmpNolist = new ArrayList<TranBean>();

String emp_no=request.getParameter("emp_info")==null?"":request.getParameter("emp_info");
String empno = "0";

LeaveEncashmentHandler empEncash = new  LeaveEncashmentHandler();
EmployeeHandler emp = new  EmployeeHandler();
EmpOffHandler off = new EmpOffHandler();
LookupHandler lookuph= new LookupHandler();
LeaveEncashmentBean lbean = new LeaveEncashmentBean();

EmployeeBean empbean = new EmployeeBean();
ArrayList<LeaveEncashmentBean> leaveList= null;

ArrayList<EmpOffBean> empofflist = null;
	
		
	if(action.equalsIgnoreCase("getEmpId")){
		
		empno = request.getParameter("EMPNO")==null?"":request.getParameter("EMPNO");
		
		String[] employ = empno.split(":");
		empno = employ[2].trim();
	    empbean = emp.getEmployeeInformation(empno);
	    
	   // leaveList= empEncash.getEmployeeBal(empno);
		lbean = empEncash.getEmployeeInfo(empno);
	    request.setAttribute("leaveEncashmentBean",lbean); 
		
	}
		%>
</script>

</head>
<body onload="checkleave()" style="overflow: hidden;"> 
<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:scroll; max-height:80%; ">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Leave Encashment </h1>
	</div>
	<!-- end page-heading -->
	<div align="center">
	<form action="leaveEncashment.jsp" method="POST">
	<table border="1">
		<tr>		
			<td>Enter Employee Name Or Emp-Id <input type="text" name="EMPNO" size="30"
				id="EMPNO" onClick="showHide()" title="Enter Employee Id / Name "> &nbsp;</td>
			<td valign="top">
				<input type="Submit" value="" class="form-submit" />
				<input type="hidden" name="action" id="action" value="getEmpId">
		    </td>
				
		</tr>
		<tr></tr>
	</table>
    </form>
    </div>
   <br>

	<table border="0" style="height: 250px;overflow: hidden; " width="100%" cellpadding="0" cellspacing="0" id="content-table">
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
			<div id="table-content">
				<% 
		if(action.equalsIgnoreCase("getEmpId")){
			
		%>
			<center>
			<%	
					LeaveEncashmentBean bean =new LeaveEncashmentBean();
					bean= (LeaveEncashmentBean)request.getAttribute("leaveEncashmentBean");
					empbean = emp.getEmployeeInformation(Integer.toString(bean.getEmpNo()));
					
			%>
			<form name="leaveform" action="LeaveServlet?action=insert"  method="post"  onsubmit="return validate()" >
		
			<table style="width: 90%" id ="customers">
			
			<tr style="height: 30px;" class ="alt"><th colspan="4"> <font size="4">Leave Encashment</font></th></tr>
			
	
			<tr style="height: 30px;" class ="alt">
					<td> <font size="3">Employee Name &nbsp;</font></td>
					<td ><input type="text" name="EMPNO1" size="40" id="EMPNO1" 
					value="<%=request.getParameter("EMPNO")%>" ></td>
					
												
					<td><font size="3">Max Limit :</font></td>
					<td><input style="height: 20px; width: 200px;" type="text" id="limit" name="limit" onkeypress="return inputLimiter(event,'Numbers')"/> </td>
			</tr>
			
			<tr style="height: 30px;" class ="alt">
					<td><font size="3">Today's Leave Balance:</font></td>
					<td><input style="height: 20px; width: 200px;" type="text" id="lbal" name="lbal" 
					value="<%=bean.getLeaveBal()%>" onkeypress="return inputLimiter(event,'Numbers')"/> </td>
					<td><font size="3">Encashment Applicable :</font></td>
					<td><input style="height: 20px; width: 200px;" type="text" id="encash" name="encash" onkeypress="return inputLimiter(event,'Numbers')"/> </td>
			</tr>
			
			<tr style="height: 30px;" class ="alt">
					<td><font size="3">Leave Encashment Sanction:</font></td>
					<td><input style="height: 20px; width: 200px;" type="text" id="lsanction" name="lsanction" onkeypress="return inputLimiter(event,'Numbers')"/> </td>
					<td><font size="3">Leave Encashment Date :</font>&nbsp; <font color="red"><b>*</b></font></td>
					<td><input style="height: 20px; width: 200px;"name="edate" size="20" id="edate"
					type="text" onBlur="if(value=='')" readonly="readonly">&nbsp;<img
					src="images/cal.gif" align="middle"
					style="vertical-align: middle; cursor: pointer;"
					onClick="javascript:NewCssCal('edate', 'ddmmmyyyy')" /></td>
			</tr>
			
			<tr style="height: 30px;" class="alt">
					<td><font size="3">FROM DATE</font>&nbsp; <font color="red"><b>*</b></font></td>
					<td><input style="height: 20px; width: 200px;" name="frmdate" size="20" id="frmdate"
					type="text" onBlur="if(value=='')" readonly="readonly"> &nbsp;<img
					src="images/cal.gif" align="middle" 
					style="vertical-align: middle; cursor: pointer;"
					onClick="javascript:NewCssCal('frmdate', 'ddmmmyyyy')" />
					</td>
					<td><font size="3">TO DATE</font>&nbsp; <font color="red"><b>*</b></font></td>
					<td><input style="height: 20px; width: 200px;"name="todate" size="20" id="todate"
					type="text" onBlur="if(value=='')" readonly="readonly">&nbsp;<img
					src="images/cal.gif" align="middle"
					style="vertical-align: middle; cursor: pointer;"
					onClick="javascript:NewCssCal('todate', 'ddmmmyyyy')" /></td>
			</tr>
			
			<tr style="height: 30px;" class ="alt">
				<td><font size="3">Monthly Gross:</font></td>
				<td><input style="height: 20px; width: 200px;" type="text" id="gross" name="gross" onkeypress="return inputLimiter(event,'Numbers')"
				value="<%=bean.getMonthlyGross()%>"/> </td>
				<td><font size="3">ESIC Amount :</font></td>
				<td><input style="height: 20px; width: 200px;" type="text" id="esic" name="esic" onkeypress="return inputLimiter(event,'Numbers')"
				value="<%=bean.getEsicAmt()%>"/> </td>
			</tr>
			
			<tr style="height: 30px;" class ="alt">
				<td colspan="4" align="center" ><font size="3">ENCASHMENT Amount : </font>&nbsp;&nbsp;&nbsp;&nbsp;
				<input style="height: 20px; width: 200px;" type="text" id="ENCASHAMT" name="ENCASHAMT" onkeypress="return inputLimiter(event,'Numbers')"/> </td>
			</tr>
			
			<tr style="height: 30px;" class ="alt">
					 <td colspan="4" align="center">
					 <input type="button" style="height: 30px; width: 150px;"  value="To Print Slip" id="print" name="print" onclick="loancal()"/> &nbsp;&nbsp;&nbsp;&nbsp; 
					<input type="submit" value ="Save" style="height: 30px; width: 60px;" id="print" name="print"/>&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="reset" value="Cancel" style="height: 30px; width: 60px;" id="print" name="print"/>  </td>
			</tr>
			
			</table>
			
									
			</form>
					
			
			</center>
		
			</div>
			<% }%>
			<!--  end table-content  -->
	<input type="hidden" name="flag1" id="flag1" value="<%=flag%>">
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