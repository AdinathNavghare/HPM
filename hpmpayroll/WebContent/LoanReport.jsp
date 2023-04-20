<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.DAO.EmployeeHandler"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.*"%>
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
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>

 <!--  <script>
var jq132 = jQuery.noConflict();
</script>
 -->
<script type="text/javascript">
	jQuery(function() {
          $("#EMPNO").autocomplete("searchList.jsp");
	});
</script>

<!-- <script src="js/MONTHPICK/jquery.js"></script> -->
 <script type="text/javascript" src="js/MONTHPICK/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="css/MONTHPICK/jquery-ui.css"> 

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
//to hide days from MONTHPICK calender
function onmonths()
{
	document.getElementById("type").selectedIndex = 0;
	document.getElementById("batchNumber").selectedIndex = 0;
	document.getElementById("batch").selectedIndex = 0;
}



function validation() {
	
	var EMPNO = document.getElementById("EMPNO").value;
	//var startdate = document.getElementById("startDate").value
	//var enddate = document.getElementById("endDate").value
	
	/* alert(startdate);
 	startdate = startdate.replace(/-/g, "/");
	enddate = enddate.replace(/-/g, "/");
	 
	var d1 = new Date(startdate);
    var d2 = new Date(enddate);
    alert(d1); */
/* 	if(document.getElementById("startDate").value == ""){
		alert("Please Select Start Date");
		document.getElementById("startDate").focus();
		 return false;
	} */
/* 	if(document.getElementById("endDate").value == ""){
		alert("Please Select End Date");
		document.getElementById("endDate").focus();
		 return false;
	} */
	
	//alert(d1.getTime());
	
	/* if (d1.getTime() > d2.getTime()) {
		alert("Invalid Date Range!\n\n From Date can't be greater than TO Date!");
		document.getElementById("startDate").focus();
		return false;
	} */
	
	if(document.getElementById("type").value =="S"){
		alert("Please Select Employee Type");
		document.getElementById("type").focus();
		return false;	
	} 
	
	if(document.getElementById("type").value =="E" || document.getElementById("type").value =="S"){
		
		if (document.getElementById("EMPNO").value == "") {
			alert("Please Insert Employee Name");
			document.getElementById("EMPNO").focus();
			return false;
		}
		var atpos=EMPNO.indexOf(":");
		if (atpos<1)
		  {
		  alert("Please Select Correct Employee Name");
		  return false;
		  }	
	}	
	
	if(document.getElementById("loantype").value=="S"){
		alert("Please Select Loan type");
		document.getElementById("loantype").focus();
		return false;
	}
		
		
	//alert("Validation ends here");
//	return false;
}
	
	


function showRow(str)
{
 	var type=document.getElementById("type").value;
	if(type=="ALL"){
		document.getElementById("empTR").style.display="none";
	}
	 if(type=="E"){
		document.getElementById("empTR").style.display='';
	 }
}



</script>
<style>
    .ui-datepicker-calendar {
        display: none;
        } 
 </style>
<%

	EmployeeHandler eh = new EmployeeHandler();
	EmployeeBean ebean = new EmployeeBean();
	ebean = eh.getMaxEmployeeInformation();
%>


<style type="text/css">

.ac_results {
	padding: 0px;
	border: 1px solid #cacaca;
	background-color: #f5f6fa;
	overflow: hidden;
	width: 22% !important;
}

</style>

<%
/* String pageName = "LoanReport.jsp";
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
 */
%>

</head>
<body style="overflow: hidden;"> 
<%@include file="mainHeader.jsp"%>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp"%>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:scroll; max-height:80%; ">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Employee</h1>
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
		<center>
		<form  action="ReportServlet?action=loanReport" method="Post" onSubmit="return validation()">
			<table border="2" id="customers">
				<tr class="alt">
					<th colspan="4">Loan Report</th>
				</tr>
				
			<!-- 	<tr >
					<td>Start Date &nbsp; <font color="red"><b>*</b></font></td>
					<td align="left"> 
   					 	<input name="startDate" id="startDate"  readonly="readonly" onclick="onmonths()" 
   					 		   class="date-picker" placeholder="Click here for Calender" />
   					</td>
   					
   					<td>End Date &nbsp; <font color="red"><b>*</b></font></td>
					<td align="left"> 
   					 	<input name="endDate" id="endDate"  readonly="readonly" onclick="onmonths()" 
   					 		   class="date-picker" placeholder="Click here for Calender" />
   					</td>
				</tr> -->	
				
				<tr>
					<td>Select Employee Type &nbsp; <font color="red"><b>*</b></font></td>					
					<td colspan="3"  align="left" >
							<select name="type" id="type" onchange="showRow(this.value)" style="width: 150px;">
								<option value="S" selected="selected">Select</option>
								<option value="ALL" >All </option>
								<option value="E" >Employee Name</option>			
							</select> 
					</td >
					
					
				</tr>
				
				<tr id="empTR">
					<td >Enter Employee Name :</td>
					<td colspan="4"  align="left" >
						<input type="text" name="EMPNO" size="55" id="EMPNO"  title="Enter Employee Name">
					</td>
				</tr>  
				
				<tr>
					<td>Loan Type &nbsp; <font color="red"><b>*</b></font></td>			
					<td colspan="3"  align="left" >
						<select name="loantype" id="loantype" style="width: 100px;">
							<option value="S" selected="selected">Select</option>
							<option value="ALL" >All </option>
							<option value="SANCTION" >ONGOING</option>	
							<option value="PENDING" >PENDING</option>
							<option value="CANCEL" >CANCEL</option>
							<option value="NIL" >NILL</option>		
						</select> 
					</td >
					
					
				</tr>
				
				
				<tr>
					<td colspan="4" align="center">
					<input type="submit" class="myButton" value="Get Report"/></td>
				</tr>
					
			</table>
		</form>
		</center>
	</div>
<div id="viewPdf"  hidden="true"></div>
<div id="process"  style="z-index: 1000;background-color:white;height: 100%;width: 100%;top: 0px;left: 0px;position: fixed;opacity:0.8;" hidden=true; >
	<div align="center" style="padding-top: 20%;">
		<img alt="" src="images/process.gif">
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