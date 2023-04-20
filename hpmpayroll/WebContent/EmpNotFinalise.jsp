<%@page import="payroll.Model.ExtraFieldBean"%>
<%@page import="payroll.Core.UtilityDAO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="payroll.Core.ReportDAO"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>On Hold Report</title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script type="text/javascript">
	function ViewReport1()
	{	
		if (document.getElementById("date").value=="")
		{
			alert("please Select From Month");
			return false;
		}
		
		/* if (document.getElementById("todate").value=="")
		{
			alert("please Select To Month");
			return false;
		} */
		var date ="01-"+document.getElementById("date").value;
	//	var date1 ="01-"+document.getElementById("todate").value;

	//	window.location.href="EmpNotFinalise.jsp?action=empNotFinalise&date="+date+"&date1="+date1;	
		window.location.href="EmpNotFinalise.jsp?action=empNotFinalise&date="+date;	   
	}

	
<%
	try
	{
		String action = request.getParameter("action")==null?"":request.getParameter("action");
		String date="";
		String date1="";
		ArrayList <ExtraFieldBean> list= new ArrayList<ExtraFieldBean>();
%>
	
</script>

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


</head>
<body style="overflow:hidden;"> 
<%--  <%
	 String pageName = "EmpNotFinalise.jsp";
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
		response.sendRedirect("login.jsp?action=0");
	} 

%> --%>


<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:auto; max-height:83%; ">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Not Finalize Employee List</h1>
	</div>
	<!-- end page-heading -->
	<center>
			<table border="1" id="customers">
					<tr class="alt">
						<th>Select From Date</th>
						<td bgcolor="#FFFFFF">
							<input name="date" id="date" readonly="readonly" class="date-picker" placeholder="Click here for Calender"/>
						</td>
						<!-- <th>Select To Date</th>
						<td bgcolor="#FFFFFF">			
						 	<input name="todate" id="todate" readonly="readonly" class="date-picker" placeholder="Click here for Calender"/>		
						</td>  -->
	
						<td align="center">
							<input type="submit"	value="GET REPORT" onclick="ViewReport1()"/>
						</td>
					</tr>
				
		  </table>
	</center>
	
	<br>
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
			<div align="center">
			<div align="center" class="imptable" style="overflow-y:hidden; width: 100%;" >		
				<table style="margin-left: 16px ; position:static;" border="1" id="customers" >			
					<tr class="alt"  >
						<th align="center" colspan="5" >Employee List That Are Not Finalize</th>
					</tr>		
					<tr>
						<th width="100">EMPNO</th>
						<th width="100">CODE</th>					
						<!-- <th width="100">TOTAL INCOME</th>
						<th width="200">TOTAL DEDUCTION</th>
						<th width="100">NET PAY</th> -->
						<th width="200">EMPNAME</th>
						<th width="100">DATE</th>
					</tr>	
				</table>	
			</div>
				   <% 
					if(action.equalsIgnoreCase("empNotFinalise")){
					
						date = request.getParameter("date")==null?""+ReportDAO.getSysDate()+"":request.getParameter("date");				
					//	date1=  request.getParameter("date1");
						
					//	list =  UtilityDAO.emplistToBeFinalise(date,date1);
					
						list =  UtilityDAO.emplistToBeFinalise(date);
						 
						if(list.size() > 0){
				   %>
							
					<div class="imptable" style="overflow-y:auto; height:350px; width: 100%;" >				
						<table style="margin-left:16px ; position:static;" border="1" id="customers"  width="500">
							<% for(ExtraFieldBean EFB : list)
								{
							%>
							<tr>
									<td  width="100" ><%=EFB.getEmpno()%></td>
									<td  width="100" ><%=EFB.getCode()%></td>
									<td  width="200" ><%=EFB.getEmpName()%></td> 
								<%--<td  width="100"><%=EFB.getTotalIncome()%></td>
									<td  width="200"><%=EFB.getTotaldec()%></td>
									<td  width="100"><%=EFB.getNetPay()%></td> --%>
									<td  width="100"><%=EFB.getDate()%></td>
						   	</tr>								
						
							<% 
								}	
							%>
							
						</table>
					</div>
					<% }
						else 
						{
					%>
					<div style="overflow-y:auto; height:350px; width: 100%;" >				
						<table border="1" id="customers">
							<tr>
								<td align="center" colspan="5"><font  style ="color:red ">There Is No Records For Selected Month</font></td>							
				   			</tr>
						</table>
					</div>		
						<%}
				   }		   
				   %>
				  
				
		 
	</div>
<br>
<% 
}catch(Exception e){e.printStackTrace();}
%>
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