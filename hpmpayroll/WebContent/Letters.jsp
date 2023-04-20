<%@page import="payroll.DAO.EmpAttendanceHandler"%>
<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.DAO.EmployeeHandler"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy DTS3 </title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/datePicker.css" type="text/css" media="screen" title="default" />	

<script type="text/javascript" src="js/jquery.datePicker.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>

<script type="text/javascript">
//validation for letter report...
	function validate() {
		var EMPNO = document.getElementById("EMPNO").value;
         
		if (document.getElementById("EMPNO").value == "") {
			alert("Please Insert Employee Name");
			document.getElementById("EMPNO").focus();
			return false;
		}else if(document.getElementById("date").value ==""){
			alert("Please Select Date");
			document.getElementById("date").focus();
			return false;
		}else if(document.getElementById("Desgn").value =="0"){
			alert("Please Select Letter Type");
			document.getElementById("Desgn").focus();
			return false;
		}
		var atpos=EMPNO.indexOf(":");
		if (atpos<1)
		  {
		  alert("Please Select Correct Employee Name");
		  return false;
		  }
		}
	
</script>
<script>
	jQuery(function() {
          $("#EMPNO").autocomplete("searchList.jsp");
	});
</script>
</head>


<body style="overflow:hidden;"> 

<%  
// get current date from server...
	EmpAttendanceHandler EAH= new EmpAttendanceHandler();
	SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
	String DATE_FORMAT = "yyyy MM dd";
	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	Calendar c1 = Calendar.getInstance(); // today	 
	String today=EAH.getServerDate();    

%> 

<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:auto; max-height:83%; ">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Letters</h1>
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
			<div id="table-content">
			<!-- <a href="tranMaintainences.jsp" align="left"><b>Edit Salary Details</b></a> -->
			<center>
			<form action="LetterServlet"  method="post" onsubmit="return validate()">
				<table border="1" id="customers">	
					<tr  class="alt">
						<th colspan="3">Letters</th>
					</tr>
					
					<tr>
						<td> Select Employee :</td>
						<td colspan="2" bgcolor="#FFFFFF">	
							<input type="text" name="EMPNO" size="40" id="EMPNO"  title="Enter Employee Name" >
						</td>
					</tr>
						
					<tr>
						<td> Select Date :</td>
						<td bgcolor="#FFFFFF">
							<input name="date"  size="30" id="date" value="<%=today %>" readonly="readonly" type="text">&nbsp;			
						</td>
						<td>
							<img src="images/cal.gif" align="middle" style="vertical-align: middle; cursor: pointer;" onClick="javascript:NewCssCal('date', 'ddmmmyyyy')" />	
						</td>
					</tr>
					
					<tr>
						<td>Select Letter Type : </td>
						<td colspan="2" width="30">
								<select name="letterType"  id="letterType">  
		      					  	<option value="0">Select</option>
		      					  <!-- 	<option value="AL">Appointment Letter</option> -->
		      					  	<option value="OL">Offer Letter</option>
		      					  <!-- 	<option value="JL">Joining Letter</option>  -->
		      					  	<option value="APL">Appraisal Letter</option> 
		      					  	<option value="RL">Relieving Letter</option> 
		      					<!--   	<option value="RGL">Resignation Letter</option>  -->
		      					  	<option value="EL">Experience  Letter</option> 
		      					</select>
		     			  </td>	
					 <!-- <td>
								<input type="radio" name="format" id="Excel" value="Excel" >&nbsp;&nbsp;Excel&nbsp;
								<input type="radio" name="format" id="Pdf" value="Pdf" checked>&nbsp;&nbsp;Pdf
								<input type="radio" name="format" id="txt" value="txt">&nbsp;&nbsp;Txt					
						  </td>	 -->
					</tr>
					
					<tr>
						<td  colspan="3" align="center"><input type="submit" value="GET REPORT" /></td>
					</tr>
						
				</table>
			</form>
	</center>
	<br>

	<div id="viewPdf"  hidden="true"></div>
	
	<div id="process"  style="z-index: 1000;background-color:white;height: 100%;width: 100%;top: 0px;left: 0px;position: fixed;opacity:0.8;" hidden=true; >
		<div align="center" style="padding-top: 20%;">
			<img alt="" src="images/process.gif">
		</div>
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