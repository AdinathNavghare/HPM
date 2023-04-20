<%@page import="payroll.Core.ReportDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java"
import="java.sql.*" errorPage="error.jsp"  isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy ECR Report</title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css" media="screen" title="default" />
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>

<script src="js/MONTHPICK/jquery.js"></script>
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
    </script>
    <style>
    .ui-datepicker-calendar {
        display: none;
        }
        
    .row:hover {
		background-color: #CCCCCC;
		cursor: pointer;
	}
	
	.style1 {
		color: #FF0000
	}
    </style>


<script>
	jQuery(function() {
		$("#EMPNO").autocomplete("list.jsp");
	});
</script>

<script type="text/javascript">
/* var xmlhttp;
var url="";

if(window.XMLHttpRequest)
{
	xmlhttp=new XMLHttpRequest;
}
else //if(window.ActivXObject)
{   
	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
} */

	function validation()
	{
		
		var fromDate=document.getElementById("date").value;
	   	var toDate=document.getElementById("date1").value;
	   	
		/* document.getElementById("viewPdf").hidden=true;
	   	document.getElementById("process").hidden=false; */
	   
	 	if(fromDate=="")
   		{
	   		alert("Please Select From Date");
	   		document.getElementById("date").focus();
	   		return false;
   		}
   	
	   	if(toDate=="")
		{
			alert("Please Select To Date");
			document.getElementById("date1").focus();
			return false;
		}
		
	   	fromDate = "01-"+fromDate;
	   	toDate = "01-"+toDate;
	   	
	  	
		
	
		fromDate = fromDate.replace(/-/g,"/");
		toDate = toDate.replace(/-/g,"/");
		
		var d1 = new Date(fromDate);
		var d2 =new  Date(toDate);
		
		
		 if (d1.getTime() > d2.getTime())
		 {
			   alert("Invalid Date Range!\n FromDate can't be greater than TODate!");
			   document.ecrForm.date1.focus();
			   return false;
		  }
// code for pregress bar
	/* 	 var date=document.getElementById("date").value;
		 var date1=document.getElementById("date1").value;
		
		 date = "01-"+date;
		 date1 = "01-"+date1;
		   	
		 	url="ReportServlet?action=ecrReportNew&date="+date+"&date1="+date1;
			
			xmlhttp.onreadystatechange=function()
			{
				if(xmlhttp.readyState==4  && xmlhttp.status==200)
				{
					var response=xmlhttp.responseText;
		        	if(response != "")
		        	{
		        		alert("found data in response");
		        		document.getElementById("viewPdf").innerHTML=response;
			        	document.getElementById("process").hidden=true;
			        	document.getElementById("viewPdf").hidden=false;
			        	alert("found data in response 1");
		        	}
				}
				
			};
			
		//	document.getElementById("process").hide();
			xmlhttp.open("GET", url, true);
			xmlhttp.send(); 
		 */
		
	}
</script>


</head>
<body style="overflow: hidden;"> 
<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:scroll; max-height:80%; ">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>ECR Report</h1>
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
			
	<form name="ecrForm" action="ReportServlet" onSubmit="return validation()">  
			<input type="hidden" id="action" name="action" value="ecrReportNew"></input> 
		<table border="1" id="customers" align="center">
			<tr>
				<th>ECR Report</th>
			<tr>
			<tr class="alt">
				<td  align="center">
				
					<table align="center">
						<tr class="alt" height="30" align="center">
							<td>From Date:</td>
							<td align="left">
								<input name="date" id="date" readonly="readonly" class="date-picker" placeholder="Click here for Calender"/>
							</td>
							<td>To Date:</td>
							<td  align="left">
								<input name="date1" id="date1" readonly="readonly" class="date-picker" placeholder="Click here for Calender"/>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center"><input type="submit" value="Submit" /></td>
						</tr>
					</table>
			   </td>
			</tr>
		</table>
		</form> 
				    
	</center>
	  <!-- <div id="viewPdf"  style="display: none"> </div>  
	
	  <div id="process"  style="z-index: 1000;background-color:white;height: 100%;width: 100%;top: 0px;left: 0px;position: fixed;opacity:0.8;" hidden=true; >
			<div align="center" style="padding-top: 20%;">
				<img alt="" src="images/process.gif">
			</div>
	</div> 
 -->
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