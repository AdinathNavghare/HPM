<%@page import="payroll.DAO.RoleDAO"%>
<%@page import="payroll.DAO.EmpOffHandler"%>
<%@page import="payroll.Model.EmpOffBean"%>
<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.DAO.EmployeeHandler"%>
<%@page import="payroll.Model.LMB"%>
<%@page import="payroll.DAO.LMH"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="payroll.Model.Lookup"%>
<%@page import="payroll.DAO.LookupHandler"%>
<%@page import="java.util.ArrayList"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Leave Application Form</title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css" media="screen" title="default" />
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script>
	jQuery(function() {
	
		var prj_srno=document.getElementById("prj_srno").value;
		var role_id=document.getElementById("role_id").value;
		
		if(role_id=="1"){
			$("#emp_no").autocomplete("list.jsp");
		}
		else{
		$("#emp_no").autocomplete("ProjectEmpList.jsp?prj_srno="+prj_srno);
		}
	});
</script>
<script type="text/javascript">

function getEmployee()
{
	var e = document.getElementById('emp_no').value;
	var res=e.split(":");
	if(e=="")
		{
		alert("Please Enter Employee...!");
		}
	else
		{
		window.location.href = "leave.jsp?empno="+res[2];
		
		}
}


function redirect_url(type )
{  
	if(type=="PENDING"){
		window.location = "leave.jsp?sList="+type;
	}else if(type=="SANCTION"){
		window.location = "leave.jsp?sList="+type;
	}else if(type=="CANCEL"){
		window.location = "leave.jsp?sList="+type;
	}else 
		window.location = "leave.jsp?sList="+type;

}

function editLeave(empno,appno){
	
	window.location.href = "leave.jsp?action=editLeaveApp&empno="+empno+"&appno="+appno;
}

function validate(){
	
	 if(document.leaveForm1.leavecode.value == "Default"){
		alert("Please select Leave Type.");
		return false;
		document.leaveform1.leavecode.focus();	
	} 
	   var date = new Date();
	   var fromDate=document.leaveForm1.frmdate.value;
	   var toDate=document.leaveForm1.todate.value;
	   fromDate = fromDate.replace(/-/g,"/");
		toDate = toDate.replace(/-/g,"/");
		
	if(fromDate == "" || toDate == ""){
			 alert("please select Date.!");
			 return false;
	 }
		var d1 = new Date(fromDate);	 	
	 	var d2 =new  Date(toDate);
	 	
	 /* if(d1.getTime() < date){
		 alert("Date must be greater than Today.!");
		 return false;
	 }	 */
	 
	 if (d1.getTime() > d2.getTime())
	 {
		   alert("Invalid Date Range!\n FromDate can't be greater than TODate!");
		   document.leaveForm1.todate.focus();
		   return false;
	  }
	 
	 var datediff = d2.getTime() - d1.getTime();
	 var radios = document.getElementById('halfday').checked;
	 if(radios == true && datediff != 0 && !(isNaN(datediff))){
	 	alert("Date Must Be Same To Apply For Half Day Leave !");
	 	return false;
	 }
	 
	 var mob = document.leaveForm1.telephone.value;
	 if(mob == "") {
		    return true;
		}
	 else if(mob.length != 10 ){
		 alert("Mobile No. must be of 10 digit.!");
		   document.leaveForm1.telephone.focus();
		   return false;
	 }
		
}
function validateForm(){
	
	 if(document.leaveForm.leavecode.value == "Default"){
			alert("Please select Leave Type.");
			return false;
			document.leaveForm.leavecode.focus();	
		} 
		   var date = new Date();	 
		   var fromDate=document.leaveForm.frmdate.value;
		   var toDate=document.leaveForm.todate.value;
		   fromDate = fromDate.replace(/-/g,"/");
			toDate = toDate.replace(/-/g,"/");
		
			var d1 = new Date(fromDate);	 	
		 	var d2 =new  Date(toDate);
		 	
		 if (d1.getTime() > d2.getTime())
		 {
			   alert("\t\t Invalid Date Range!\n\n FromDate can't be greater than TODate!");
			   document.leaveForm.todate.focus();
			   return false;
		  }
		 
		 /* if(d1.getTime() < date){
			 alert("Date must be greater than Today.!");
			 return false;
		 } */
		 var datediff = d2.getTime() - d1.getTime();
		 var radios = document.getElementById('halfday').checked;
		 if(radios == true && datediff != 0 && !(isNaN(datediff))){
		 	alert("Date Must Be Same To Apply For Half Day Leave !");
		 	return false;
		 }
		 
		 if(fromDate==""){
			 alert("Please select From Date");
			 return false;
		 }
		 
		 if(toDate==""){
			 alert("Please select To Date");
			 return false;
		 }
		 
		 
		 var mob = document.leaveForm.telephone.value;
		 if(mob == "" || mob == 0) {
			    return true;
			}
		 else if(mob.length != 10 ){
			 
			 alert("Mobile No. must be of 10 digit.!");
			   document.leaveForm.telephone.focus();
			   return false;
		 }
}
</script>
<script>
	
	$(document).ready(function() {
	    var f = document.getElementById('leave');
	    setInterval(function() {
	        f.style.display = (f.style.display == 'none' ? '' : 'none');
	    }, 500); 
	    
	});
	
</script>

<script>
function setDays(){
	var fromDate=document.getElementById("frmdate").value;
	var toDate=document.getElementById("todate").value;
	//alert(fromDate+"---"+toDate);
	if(
		((fromDate.substring(3,6)!=toDate.substring(3,6)|| (fromDate.substring(7,11)!=toDate.substring(7,11)))
			&& ((toDate!="") && (fromDate!="") ) )    ) 
			
	{
		alert("you can not select dates of different months");
		document.getElementById("frmdate").value="";
		document.getElementById("todate").value="";
		return false;
	}
	else if(fromDate.substring(0,2)>toDate.substring(0,2) && ((toDate!="") && (fromDate!="") ) ){
			alert("To date shall not be earlier than From Date");
			document.getElementById("frmdate").value="";
			document.getElementById("todate").value="";
			document.getElementById("days").value="";
			return false;
		}
		else if (((toDate!="") && (fromDate!="") ) ) {
			if(fromDate!=toDate && document.getElementById('halfday').checked ){
				alert("From Date and To Date must be same for Half day");
				document.getElementById("frmdate").value="";
				document.getElementById("todate").value="";
				document.getElementById("days").value="";
				return false;
			}
			
			if(fromDate==toDate && document.getElementById('halfday').checked ){
				document.getElementById("days").value=0.5;
			}else if(fromDate==toDate){
			document.getElementById("days").value=1;
			}
			if(fromDate!=toDate){
			var lowerLimit=fromDate.substring(0,2);
			var upperLimit=toDate.substring(0,2);
			document.getElementById("days").value=upperLimit-lowerLimit+1;
			}
			return true;
		}
}

function onSelection(){
	var fromDate=document.getElementById("frmdate").value;
	var toDate=document.getElementById("todate").value;

	if(fromDate==toDate){
		if (document.getElementById('halfday').checked){
			document.getElementById("days").value=0.5;
		}else{
			document.getElementById("days").value=1;
		}
		
	}
	else {
		if(document.getElementById('halfday').checked)
		{
			alert("You can not apply half days for a duration more than a day");
			document.getElementById("frmdate").value="";
			document.getElementById("todate").value="";
			document.getElementById("days").value="";
			return false;
		}
	}
/* 	else if((fromDate!=toDate) && document.getElementById('halfday').checked)
		{
		alert("You can not apply half days for a duration more than a day");
		returtn false;
		} */
	
}

</script>

</head>
<body>
<%
LookupHandler lookuph= new LookupHandler();
ArrayList<LMB> leaveBalList = new ArrayList<LMB>();
ArrayList<LMB> leaveList = new ArrayList<LMB>();
LMH leaveMasterHandler = new LMH();
SimpleDateFormat dateFormat;

EmployeeHandler emph = new EmployeeHandler();
EmployeeBean ebean = new EmployeeBean();
EmpOffHandler eoh=new EmpOffHandler();

int empNo = request.getParameter("empno")==null?Integer.parseInt(session.getAttribute("EMPNO").toString()):Integer.parseInt(request.getParameter("empno"));
RoleDAO obj1=new RoleDAO();
String roleId=obj1.getrole(Integer.parseInt(session.getAttribute("UID").toString()));

EmpOffBean eob= eoh.getEmpOfficAddInfo(Integer.toString(empNo));
int prjsrno=eob.getPrj_srno();
ebean = emph.getEmployeeInformation(Integer.toString(empNo));

leaveBalList = leaveMasterHandler.getList(empNo);
String searchList = request.getParameter("sList")==null?"ALL":request.getParameter("sList");
String action = request.getParameter("action")==null?"":request.getParameter("action");
String error = request.getParameter("error")==null?"":request.getParameter("error");
if(searchList.equalsIgnoreCase("PENDING")){
	leaveList = leaveMasterHandler.leaveDisplay(empNo,"PENDING");
}else if(searchList.equalsIgnoreCase("SANCTION")){
	leaveList = leaveMasterHandler.leaveDisplay(empNo,"SANCTION");
}else if(searchList.equalsIgnoreCase("CANCEL")){
	leaveList = leaveMasterHandler.leaveDisplay(empNo,"CANCEL");
}else{
	leaveList = leaveMasterHandler.leaveDisplay(empNo,"ALL");
}


LookupHandler lh=new  LookupHandler();
String empNumber="";
String str_disp="";
if(request.getParameter("empno")!=null){
	empNumber=request.getParameter("empno");
	 ebean=emph.getEmployeeInformation(empNumber);
	 str_disp=lh.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getMNAME()+" "+ebean.getLNAME();
	 
}


%>
<%@include file="mainHeader.jsp"%>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp"%>
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" >
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Leave Master</h1>
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
		<table id='customers'>
		<tr class="alt">
		<td> Enter Employee : </td>
		<td>
		 <input size='40' type="text" id="emp_no" name="emp_no"
		 value="<%=request.getParameter("empno")==null?"":str_disp%>">
		
   <input type="button"  value="SUBMIT" onclick="getEmployee()">
		</td>
		</table>	
		
	<br>
  <table id="customers">
  	<tr><th colspan="5"><font color="white" size="3">LEAVE DETAILS</font></th></tr>
    <tr>
      <td><b>Emp ID </b></td>
      <td><%=ebean.getEMPCODE()==null?"":ebean.getEMPCODE()%></td>
      <td><b>Employee Name </b></td>
      <td colspan="2"> 
     <%=(ebean.getFNAME()==null)?"":lh.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getMNAME()+" "+ebean.getLNAME()%></td>
    </tr>
    <tr>
      <th width="80" scope="col">Balance Date </th>
      <th scope="col">Leave Type </th>
      <th scope="col">Leaves Credited</th>
      <th scope="col">Leaves Taken</th>
      <th scope="col">Leave Balance</th>
      
    </tr>
     <% float bal = 0.0f;
    	if(leaveBalList.size()!=0){
    	 
    		for(LMB leaveBean : leaveBalList){
    			if(leaveBean.getLEAVECD() != 5){
    				bal =bal + leaveBean.getTOTCR();
    			}	%>
    <tr align="center">
	      <td><%=leaveBean.getBALDT() %></td>
	      <td><%=lh.getLKP_Desc("LEAVE",leaveBean.getLEAVECD()) %></td>
	      <td><%=leaveBean.getTOTCR() %></td>
	      <td><%=leaveBean.getTOTDR() %></td>
	      <td><%=leaveBean.getBAL() %></td>
    </tr>  	   
    <% }
	   if(LMH.displayEmpLeaves(empNo) - bal > 0){ %>
	<tr class="row">
		<%-- <td colspan="6" height="18"><font size="2"><div id="leave"><%=LMH.displayEmpLeaves(empNo) - bal %> leaves Remaining</div></font></td> --%>
	</tr>

   <%		}
	   }
       else {    %>
	  	<tr><td colspan="5" height="20"></td></tr>
    <% } %>
  </table>
<br/>

<%
		if(action.equalsIgnoreCase("editLeaveApp")){
			
			int empno = Integer.parseInt(request.getParameter("empno"));
			int appNo = Integer.parseInt(request.getParameter("appno"));
			
			ArrayList<LMB> leaveApp = leaveMasterHandler.getLeaveApp(empno, appNo);
			
			for(LMB leaveBean : leaveApp){
				
		%>
		<div align="center">
			<h3 style="color: white;"><%=error %></h3>
			<form  id="form" name="leaveForm" action="LMS?action=editleave" method="post" onSubmit=" return validateForm()">
		<input type="hidden" name='empno' value='<%=empno%>'>
		<input type="hidden" name="prj_srno" id="prj_srno" value="<%=prjsrno%>">
		<%System.out.println("role id he"+roleId) ;%>
		<input type="hidden" name="role_id" id="role_id" value="<%=roleId%>">
		
			<table id="customers" width="400" border="1" style="float: none;">
			<tr><td align="center" colspan="6" height="20"><h2>EDIT LEAVE</h2></td></tr>
		    <tr>
		      <td>&nbsp;Date</td>
		      <td><input name="tdate" id="tdate" type="text"  readonly="readonly" value="<%=leaveBean.getTRNDATE() %>" size="20"></td>
		    
		      <td>&nbsp;Leave Type <font color="red" size="3">*</font></td>
		      <td><select name="leavecode" id="leavecode" style="width: 128px">
							<option selected="selected" value="Default">Select</option>
							<%
							int srno = leaveBean.getLEAVECD();
							String selected = "selected";
							ArrayList<Lookup> result=new ArrayList<Lookup>();
									 
							result=lookuph.getSubLKP_DESC("LEAVE");
							for(Lookup lkbean : result)
							{
								if(srno == lkbean.getLKP_SRNO())
								{ %>
									<option value="<%=lkbean.getLKP_SRNO()%>" <%=selected%>><%=lkbean.getLKP_DESC()%></option>  
									<%								 
									}else {
									%>
				 							<option value="<%=lkbean.getLKP_SRNO()%>" ><%=lkbean.getLKP_DESC()%></option>  
									 	<%}
							}
									 	%>
					</select>
			  </td>
		    </tr>
		    <tr>
		    	<td>From Date <font color="red" size="3">*</font></td>
		     	<td><input name="frmdate" id="frmdate" type="text" value="<%=leaveBean.getFRMDT() %>" readonly="readonly" size="15" onchange="setDays()">&nbsp;<img
							src="images/cal.gif" align="middle"
							style="vertical-align: middle; cursor: pointer;"
							onClick="javascript:NewCssCal('frmdate', 'ddmmmyyyy')" /></td>
		   
		      	<td>To Date <font color="red" size="3">*</font></td>
		     	<td><input name="todate" id="todate" type="text" value="<%=leaveBean.getTODT() %>" readonly="readonly" size="14" onchange="setDays()">&nbsp;<img
							src="images/cal.gif" align="middle"
							style="vertical-align: middle; cursor: pointer;"
							onClick="javascript:NewCssCal('todate', 'ddmmmyyyy')" /></td>
		    </tr>
		    <tr>
		      	<td>Leave Reason</td>
		      	<td colspan="3"><input type="text" name="lreason" value="<%=leaveBean.getLREASON() %>" size="60"/></td>
		    </tr>
		    <tr>
		      	<td>Address </td>
		      	<td colspan="3"><input type="text" name="addDuringleave" value="<%=leaveBean.getLADDR() %>" size="60"/></td>
		    </tr>
		    <tr>
		      	<td>Mob No.</td>
		      	<td><input type="text" name="telephone" value="<%=leaveBean.getLTELNO()%>" />		      
		      	<td align="right">Half Day ?</td>
		      	<% if(leaveBean.getNODAYS() == 0.5f){ %>
	 	  		    <td>&nbsp;Yes&nbsp;<input type="radio" name="halfday" id="halfday" value="yes" onchange="onSelection()" checked/>
	 	  		    &nbsp;No&nbsp;<input type="radio" name="halfday" value="no"  onchange="onSelection()"/></td>
	 	  	  	<%}else{ %>
	 	  	  		<td>&nbsp;Yes&nbsp;<input type="radio" name="halfday" id="halfday" value="yes"  onchange="onSelection()"/>
	 	  		    &nbsp;No&nbsp;<input type="radio" name="halfday" value="no"  onchange="onSelection()" checked/></td>
	 	  	  	<%} %>
	 	  		
		    </tr>
		    <tr><td align="right">Total Days</td>
	 	  <td><input type="text" name="days" id="days" readonly="readonly" value='<%=leaveBean.getNODAYS()%>' /> </td>
	 	  </tr>
		    <tr >
		    <td height="31" colspan="4" align="center">
		    	<input type="submit" value="Save" />&nbsp;&nbsp;
		    	<input type="reset" value="Cancel" onclick="window.location.href='leave.jsp'"/>
		    	<input type="hidden" name="appNo" value="<%=leaveBean.getAPPLNO()%>"></td>
		    </tr>
		  </table>
		  </form>

		</div>
		<br/>
		<% 		
			}
		  
		}else{ %>
	
	<div align="center">
		<h3 style="color: red;"><%=error %></h3>
		<form  id="form1" name="leaveForm1" action="LMS?action=addleave" method="post" onSubmit=" return validate()">
		
		<table id="customers" width="400" border="1" style="float: none;">
	           <%
	   				 dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
					 Date date = new Date();
					 String currentdate = dateFormat.format(date);  
					%>
					<input type="hidden" name='empno' value='<%=empNo%>'>
					<input type="hidden" name="prj_srno" id="prj_srno" value="<%=prjsrno%>">
					<input type="hidden" name="role_id" id="role_id" value="<%=roleId%>">
		<tr><th align="center" colspan="6" height="16"><h2><font color="white" size="3">APPLY LEAVE</font></h2></th></tr>
	     <tr>
	      <td align="right">Date</td>
	      <td><input name="tdate" id="tdate" type="text"  readonly="readonly" value="<%=currentdate %>" size="20"></td>
	 
	      <td align="right">Leave Type <font color="red" size="3">*</font></td>
	      <td ><select name="leavecode" id="leavecode" style="width: 128px">
						<option selected="selected" value="Default">Select</option>
						<%
							  ArrayList<Lookup> result3=new ArrayList<Lookup>();
								  LookupHandler lkhp3= new LookupHandler();
							      result3=lkhp3.getSubLKP_DESC("LEAVE");
						for(Lookup lkbean : result3)
						{
							if(leaveBalList.isEmpty() && lkbean.getLKP_SRNO()==4){
								%>
	 							<option value="<%=lkbean.getLKP_SRNO()%>"><%=lkbean.getLKP_DESC()%></option>  
						 	<%
							}
							if( !leaveBalList.isEmpty() && lkbean.getLKP_SRNO()==4){
								%>
								<option value="<%=lkbean.getLKP_SRNO()%>"><%=lkbean.getLKP_DESC()%></option>  
								<%
							}
							for(LMB leaveBean : leaveBalList){
								
								if(lkbean.getLKP_SRNO() == leaveBean.getLEAVECD() && lkbean.getLKP_SRNO() != 4){
							
									%>
			 							<option value="<%=lkbean.getLKP_SRNO()%>"><%=lkbean.getLKP_DESC()%></option>  
								 	<%
								}
							}
						}
						 	%>
					</select>
		  </td>
	     </tr>
	     <tr>
	      <td align="right">From Date <font color="red" size="3">*</font></td>
	     <td><input name="frmdate" id="frmdate" type="text" readonly="readonly" size="15" onchange="setDays()">&nbsp;<img
						src="images/cal.gif" align="middle"
						style="vertical-align: middle; cursor: pointer;"
						onClick="javascript:NewCssCal('frmdate', 'ddmmmyyyy')" /></td>
	    
	      <td align="right">To Date <font color="red" size="3">*</font></td>
	     <td><input name="todate" id="todate" type="text" readonly="readonly" size="14" onchange="setDays()">&nbsp;<img
						src="images/cal.gif" align="middle"
						style="vertical-align: middle; cursor: pointer;"
						onClick="javascript:NewCssCal('todate', 'ddmmmyyyy')" /></td>
	    </tr>
	     <tr>
	      <td align="right">Leave Reason</td>
	      <td colspan="3"><input type="text" name="lreason" size="60"/></td>
	    </tr>
	    <tr> 
	      <td align="right">Address </td>
	      <td colspan="3"><input type="text" name="addDuringleave" size="60"/></td>
	    </tr>
	    <tr>
	      <td align="right">Mob No. </td>
	      <td><input type="text" name="telephone" id="telephone"/></td>
	 	  <td align="right">Half Day ?</td>
	 	  <td>&nbsp;Yes&nbsp;<input type="radio" name="halfday" id="halfday" value="yes"  onchange="onSelection()"/>
	 	  		&nbsp;No&nbsp;<input type="radio" name="halfday" value="no"  onchange="onSelection()" checked/></td>
		</tr>
		  <tr><td align="right">Total Days</td>
	 	  <td><input type="text" name="days" id="days" readonly="readonly" /> </td>
	 	  </tr>
	    <tr >
	    <td height="31" colspan="4" align="center">
	    	<input type="submit" value="Add" />&nbsp;&nbsp;
	    	<input type="reset" value="Cancel"  /></td></tr>
	  </table>
	  </form>
	  
	</div>
	<br/>

<%} %>
<div align="center">

<h2>Leave Transactions</h2>
<div>
<table id="customers" style="float: none;">
<tr><th>Select List</th><td><select name="type" onchange="redirect_url(this.value)">

<option value="SELECT" >SELECT</option>
<option value="ALL" >ALL</option>
<option value="PENDING" >PENDING</option>
<option value="SANCTION" >SANCTION</option>
<option value="CANCEL" >CANCELED</option></select></td></tr>
</table>

</div>
<table  id="customers" width="529" border="1" style="float: none;">

    <tr class="alt">
      <th scope="col">App Date</th>
      <th scope="col">Leave Type </th>
      <th scope="col">From Date </th>
      <th scope="col">To Date </th>
      <th scope="col">Total Days</th>
      <th scope="col">Leave Reason </th>
      <th scope="col">Address </th>
      <th scope="col">Tran Type</th>
      <th scope="col">Status </th>
      <th scope="col" width="50">Edit</th>
    </tr>
    <%
    if(leaveList.size()!=0){
    	for(LMB leaveBean : leaveList){%>
    <tr align="center">
    	<td><%=leaveBean.getTRNDATE() %></td>
    	<td><%=lookuph.getLKP_Desc("LEAVE",leaveBean.getLEAVECD())%></td>
    	<td><%=leaveBean.getFRMDT() %></td>
    	<td><%=leaveBean.getTODT() %></td>
    	<td><%=leaveBean.getNODAYS() %></td>
    	<td width="150"><%=leaveBean.getLREASON() %></td>
    	<td width="150"><%=leaveBean.getLADDR() %></td>
    	<% if(leaveBean.getTRNTYPE() == 'C') {%>
    	<td>CREDIT</td>
    	<%}else { %>
    	<td>DEBIT</td>
    	<%} %>
    	<td><%=leaveBean.getSTATUS() %></td>
    	<td>
    	<%if(leaveBean.getSTATUS().equalsIgnoreCase("PENDING")){
    		%>
    	
    	 <a href="javascript:{}" onclick="editLeave('<%=leaveBean.getEMPNO()%>','<%=leaveBean.getAPPLNO()%>')">Edit</a>
    	<input type="hidden" name="appNo" value="<%=leaveBean.getAPPLNO()%>">
    	<input type="hidden" name="empno" value="<%=leaveBean.getEMPNO()%>">
           
        
        <%} %>
    	</td>
    </tr>
    <%}
    } else {%>
    <tr><td colspan="10" height="20">No Records Found</td></tr>
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