<%@page import="payroll.DAO.*"%>
<%@page import="payroll.Model.*"%>
<%@page import="java.util.ArrayList"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy DTS3 </title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />


<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script src="js/filter.js"></script>



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
    </style>





<script type="text/javascript">


function getFilt()
{
	var month=document.getElementById("frmdate").value;
	var month1=document.getElementById("todate").value;
	if(month=="" && month1 == "")
	{
		alert("Please Select Fromdate and Todate !!");	
	}
	else
	{
		getFilter('leaveLedger',month);
	}
}
function addFilt()
{
	var month=document.getElementById("frmdate").value;
	var month1=document.getElementById("todate").value;
	if(month=="" && month1 == "")
	{
		alert("Please Select Fromdate and Todate !!");	
	}
	else
	{
		addMoreEmp('leaveLedger',month);
	}
}

function paycal()
{
	
		
		document.getElementById("list").value=numList;
		if(numList==""){
			alert("please select employee !!");
			return false;
		}
		else
			{
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
		 
			 
			
			url="ReportServlet?action=leaveLedger&emplist="+numList;
			xmlhttp.onreadystatechange=function()
			{
			if(xmlhttp.readyState==4)
				{
					var response = xmlhttp.responseText;
					
					initAll();
				}

			};
			
			xmlhttp.open("post",url, true);
			xmlhttp.send();
		  
	}
	
}




</script>

<script>
	jQuery(function() {
		$("#empNo").autocomplete("list.jsp");
	});
</script>
<script>
	jQuery(function() {
		$("#empNo1").autocomplete("list.jsp");
	});
</script>

</head>
<body style="overflow: hidden;"> 
<%
	String pageName = "Leave_Ledger_Report.jsp";
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

<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:scroll; max-height:80%; ">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Leave Ledger Report </h1>
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
			 
			
			
			<form  name="leaveLedgerform" action="ReportServlet" method="post" onSubmit="return paycal()">
				<table id="customers" width="553" align="center">
				
				   <tr> <th colspan="4">Leave Ledger Report</th></tr>
				  
				<tr class="alt"><td align="center">Fromdate    </td>
				 <td>  <input name="frmdate" id="frmdate" readonly="readonly" class="date-picker" placeholder="Click here for Calender"/>
				 </td>
							
				<td  align="center">Todate	</td>
				<td><input name="todate" id="todate" readonly="readonly" class="date-picker" placeholder="Click here for Calender"/>
					<input type="hidden" id="action" name="action" value="leaveLedger"></input>
					<input type="hidden" id="list" name="list">
				</td></tr>
			 
			 <tr class="alt" align="center" >
								<td  colspan="4">
									<div id="displayDiv" style="height: 300px"></div>
									<div id="countEMP">0 Employees Selected</div>
									<input type="button" value="Cancel All" onclick="cancelAll()">
									<input type="button" value="Select Employees" onclick="getFilt()">
									<input type="button" value="Add More Employee" onclick="addFilt()">
								</td>
								</tr>
			<tr class="alt"><td colspan="4" align="center"> <input type="submit" value="GetReport"/> &nbsp;&nbsp;<input type="reset" value="Cancel"/></td></tr>
								
			  </table>
			
			</form>
			
			
			
			
			
			  </center>
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