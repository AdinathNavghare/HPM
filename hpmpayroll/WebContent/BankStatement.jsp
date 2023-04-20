<%@page import="payroll.DAO.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.collections.iterators.ArrayListIterator"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%>
<%@page import="payroll.Core.ReportDAO"%>
 <%@page errorPage="error.jsp" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy DTS3 </title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />

	<script src="js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>
	<script src="js/jquery/jquery-1.4.4.min.js" type="text/javascript"></script>
	
	
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script type="text/javascript">
var xmlHttp ;
	
	if (window.XMLHttpRequest) { // Mozilla, Safari, ...
		xmlHttp = new XMLHttpRequest();
	    } else if (window.ActiveXObject) { // IE
	      try {
	    	 xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
	      } 
	      catch (e) {
	        try {
	        	xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	        } 
	        catch (e) {}
	      }
	    }


	function inputLimiter(e,allow) {
		  var AllowableCharacters = '';
		  if (allow == 'Numbers'){AllowableCharacters='1234567890';}
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
	


function ViewReport()
{
	
	
	if (document.getElementById("startDate").value=="")
	{
	alert("Please Select Month");
	return false;
	}
	
	if (document.getElementById("type").value=="" || document.getElementById("type").value=="0")
	{
	alert("Please Select Report Type !");
	return false;
	}
	
	if (document.getElementById("type").value=="A")
	{
	if (document.getElementById("batchNumber").value=="")
	{
	alert("Please Select Batch Number !");
	return false;
	}
	}
	if (document.getElementById("type").value=="B")
	{   // checking for range
		var range=document.getElementById('fromRange').value;
		var range2=document.getElementById('toRange').value;
		if(range=="")
		{
		range="0";
		document.getElementById('fromRange').value="0";
		}
	else{
		if(range2=="")
			{
			alert("Please select To Range");
			return false;
			}
	}
	
	if(range2=="")
	{
		range2="0";
		document.getElementById('toRange').value="0";
	}
	
	else{
		if(range==""  )
			{
			alert("Please select From Range");
			return false;
			}
	}
if(parseInt(range)>parseInt(range2)){
		
		alert("TO range should be more than From Range");
		return false();
	}
	}

	
	var xmlHttp;
	var url="";
	if(window.XMLHttpRequest)
	{
		xmlhttp = new XMLHttpRequest;
	}
	else
	{
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	document.getElementById("viewPdf").hidden=true;
	document.getElementById("process").hidden=false;
	
	var date ="01-"+document.getElementById("startDate").value;
	//var month=document.getElementById("month").value;
	//var batchNumber=document.getElementById("batchNumber").value;
	 var type=document.getElementById("type").value;

	 if(type=="A"){
		
	url="ReportServlet?action=bankStmnt&date="+date+"&batchNumber="+document.getElementById("batchNumber").value+"&type="+type;
	 }else{
	
		 var fromRange=document.getElementById("fromRange").value;
		 var toRange=document.getElementById("toRange").value;
		 url="ReportServlet?action=bankStmnt&date="+date+"&type="+type+"&fromRange="+fromRange+"&toRange="+toRange;
	 }
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			var response=xmlhttp.responseText;
        	document.getElementById("viewPdf").innerHTML=response;
        	document.getElementById("process").hidden=true;
        	document.getElementById("viewPdf").hidden=false;
		}
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
	
}
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
    
    
    
    function onmonths()
    {
    	
    	document.getElementById("type").selectedIndex = 0;
    	document.getElementById("batchNumber").selectedIndex = 0;
    	document.getElementById("batch").selectedIndex = 0;
    	
    	
    }
    
    
    </script>
      <script type="text/javascript">
      
       function showPrBatches(str)
      {
  	  	 var type=document.getElementById("type").value;
  		if(type=="B"){
  			document.getElementById("batch").style.display="none";
  			document.getElementById("Range1").style.display='';
  	  		document.getElementById("Range2").style.display='';
  		}
  		
  	  if(type=="A"){
  		document.getElementById("batch").style.display='';
  		document.getElementById("Range1").style.display="none";
  		document.getElementById("Range2").style.display="none";
	  	 var startDate=document.getElementById("startDate").value;
	  	 if(startDate=="")
	  		 {
	  		 alert("Please select Month First");
	  		 document.getElementById("type").value=0;
	  		return false;
	  		 }
	  
  			var url="batch.jsp";
  			//alert(startDate);
    			url +="?date=" +startDate;
    		//	alert(url);
    			xmlHttp.onreadystatechange = stateChange;
    
    			xmlHttp.open("GET", url, true);
    			xmlHttp.send(null);
  	  }
    } 
      
       function stateChange()
       {   
       	if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete")
           	{   
       		document.getElementById("batchNumber").innerHTML=xmlHttp.responseText ;
       		}   
       }

       
  /*   	  function getDiffrentBatchNumbers(){
    	
	
		  	 
			  if(type=="A"){
			
				 window.location="BankStatement.jsp?action=getlist&date="+startDate+"&type="+type; 
			
			 }
			 else
				 window.location="BankStatement.jsp?date="+startDate+"&type="+type; 
		 } */
    	    
   <% 	  /*  String action=request.getParameter("action")==null?"":request.getParameter("action") ;
    	    String date=request.getParameter("date")==null?"":request.getParameter("date") ;
    	    ArrayList<Integer> releaseBatchList=new ArrayList<Integer>();
    	    ReleaseBatchHandler releaseBatchHandler = new ReleaseBatchHandler();
    	    System.out.println("date on bankstmt page"+date);
    	   if(action.equalsIgnoreCase("getlist"))
    	   {
    	    if(!date.equals("")){
    	    releaseBatchList=releaseBatchHandler.getReleaseBatchList("01-"+date);
    	    System.out.println("size of list on bank stmt page:"+releaseBatchList.size());
    	    }}  */
   %>
   
    </script>
    <style>
    .ui-datepicker-calendar {
        display: none;
        }
    </style>


</head>
<body style="overflow:hidden;"> 
<%	String pageName = "BankStatement.jsp";
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
	}%>
<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:scroll; max-height:80%; ">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Bank Statement Report </h1>
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
<h2>Bank Statement</h2>

<table border="1" id="customers">
		<tr class="alt">
			
				<td height="120" align="center">
				<input type="hidden" id="action" name="action" value="bankStmnt"></input>
				<table>
					 <tr class="alt">

						<th>Select Month :</th>
						<td align="left" bgcolor="#FFFFFF">
						
						 
    <input name="startDate" id="startDate"  readonly="readonly" onclick="onmonths()" class="date-picker" placeholder="Click here for Calender" /></td>
					</tr>
					
					<tr>
					<td>Select type:</td>
					<%String type=request.getParameter("type")==null?"":request.getParameter("type") ;%>
						<td colspan="2"  align="left" >
						<select name="type" id="type" onchange="showPrBatches(this.value)">
						<option value="0" selected="selected">Select</option>
					<option value="B" <%=type.equalsIgnoreCase("B")?"Selected":"" %> >Before Release List </option>
					<option value="A" <%=type.equalsIgnoreCase("A")?"Selected":"" %> >After Release List </option>
				
					</select>
					</td ></tr>
					
					

<%--     <%

    
 if(releaseBatchList.size()!=0)
 {%> --%>
	  <tr  id="batch" style="display: ''" >
					<td >Select Batch:</td>
					<td colspan="2"  align="left" >
						<select style="width: 80px;" name="batchNumber" id="batchNumber"  >
 <%-- <%for(int  batchNumber : releaseBatchList)
 { 
	  %> --%>
     
      <option  value="0">All</option>  
  <%--   
      <%
    
 }
   }
  	

     %> --%>

      
				<!-- 	<option value="1" >First Value </option>
					<option value="2" >Second Value </option> -->
						</select>
						</td>
					</tr>
					<tr id="Range1">
					<td >Range From:</td>
					<td colspan="2"  align="left" >
						<input type="text" style="width: 80px;" name="fromRange" id="fromRange" maxlength="9"
						onkeypress="return inputLimiter(event,'Numbers')" >
							</td></tr>  <tr  id="Range2" style="display: ''" >
					<td >Range To:</td>
					<td colspan="2"  align="left" >
						<input type="text" style="width: 80px;" name="toRange" id="toRange" maxlength="9"
						onkeypress="return inputLimiter(event,'Numbers')" >
						</td></tr>
					<tr rowspan="2">
						<td colspan="4" align="center"><input type="submit" class="myButton"
							value="Get Report" onclick="ViewReport()"/></td>
					</tr>
				</table>

			  </td>
			</tr>
</table>
<br>
<div id="viewPdf"  hidden="true">
			   </div>
			   <div id="process" align="center" hidden="true">
				<img alt="" src="images/process.gif">
				</div>
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