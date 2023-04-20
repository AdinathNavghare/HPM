<%@page import="payroll.DAO.LookupHandler"%>
<%@page import="payroll.DAO.CodeMasterHandler"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="payroll.Model.DeductBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="payroll.DAO.DeductHandler"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Deduction Management</title>
	<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />

<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css" media="screen" title="default" />

<!--  date picker script -->
<link rel="stylesheet" href="css/datePicker.css" type="text/css" />
<script src="js/jquery/date.js" type="text/javascript"></script>
<script src="js/jquery/jquery.datePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>

<script type="text/javascript">
	function TakeCustId() {
		var EMPNO = document.getElementById("EMPNO").value;
         
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
	 function DeductValidation()
	 {
		 var EMPNO = document.getElementById("empno").value;
         
			if (document.getElementById("empno").value == "") {
				alert("Please Insert Employee Name");
				document.getElementById("empno").focus();
				return false;
			}
			
		 if(document.getElementById("select.trncd").selectedIndex==0)
			 {
			 alert("Please Select Transaction Code");
			 document.getElementById("select.trncd").focus();
			 return false;
			 }
		 if(document.getElementById("subSysCode").value =="")
		 {
		 alert("Please Enter SubSystem Code");
		 document.getElementById("subSysCode").focus();
		 return false;
		 }
		 if(document.getElementById("acno").value =="")
		 {
		 alert("Please Enter Account Number");
		 document.getElementById("acno").focus();
		 return false;
		 }
		 if(document.getElementById("sancDate").value =="" || document.getElementById("sancDate").value =="dd-mmm-yyyy")
		 {
		 alert("Please Select Sanction  Date");
		 document.getElementById("sancDate").focus();
		 return false;
		 }
		 if(isNaN(document.getElementById("sancNo").value)||document.getElementById("sancNo").value =="")
		 {
		 alert("Please Enter Sanction  Number");
		 document.getElementById("sancNo").focus();
		 return false;
		 }
		 if(isNaN(document.getElementById("sacnAmt").value)||document.getElementById("sacnAmt").value =="")
		 {
		 alert("Please Enter Sanction Amount");
		 document.getElementById("sacnAmt").focus();
		 return false;
		 }
		 if(document.getElementById("startDate").value =="" || document.getElementById("startDate").value =="dd-mmm-yyyy")
		 {
		 alert("Please Select Start  Date");
		 document.getElementById("startDate").focus();
		 return false;
		 }
		 if(document.getElementById("endDate").value =="" || document.getElementById("endDate").value =="dd-mmm-yyyy")
		 {
		 alert("Please Select End  Date");
		 document.getElementById("endDate").focus();
		 return false;
		 }
		 var startDate = document.getElementById("startDate").value;
	     var endDate = document.getElementById("endDate").value;
		  
			var d1 = new Date(startDate);
			var d2 = new Date(endDate);  
			
			if (d1.getTime() > d2.getTime())
			 {
				   alert("\t\tInvalid Date Range!\n FromDate can't be greater than TODate!");
				   document.getElementById("endDate").focus();
				   return false;
			  }
		 if(isNaN(document.getElementById("install").value)||document.getElementById("install").value =="")
		 {
		 alert("Please Enter Installment Amount");
		 document.getElementById("install").focus();
		 return false;
		 }
		 if(isNaN(document.getElementById("Installments").value)||document.getElementById("Installments").value =="")
		 {
		 alert("Please Enter Installments");
		 document.getElementById("intRate").focus();
		 return false;
		 }
		 
	 }
	 function calculate(){
		 
		var months;
		var startDate = document.getElementById("startDate").value;
	    var endDate = document.getElementById("endDate").value;
	    var principal = document.getElementById("sacnAmt").value;
	    //var rate = document.getElementById("intRate").value;
		var d1 = new Date(startDate);
		var d2 = new Date(endDate);  
		months = ((d2.getFullYear() - d1.getFullYear()) * 12)+(d2.getMonth() - d1.getMonth());
		if (d1.getTime() > d2.getTime())
		 {
			   alert("\t\tInvalid Date Range!\n FromDate can't be greater than TODate!");
			   document.getElementById("endDate").focus();
			   return false;
		  }
		//rate = rate/100;
		//var monthly = rate/12;
		//var payment = ((prinipal*monthly)/(1-Math.pow((1+monthly),-months)));
		var payment = (principal/months).toFixed(2);
		document.getElementById("install").value = payment;
		document.getElementById("Installments").value = months;
		alert("Monthly EMI : "+ payment+"/-");
	 }
	
</script>


<script type="text/javascript" src="js/datetimepicker.js"></script>

<%	LookupHandler lkp = new LookupHandler();
	CodeMasterHandler CMH = new CodeMasterHandler();
	ArrayList<CodeMasterBean> CMBList = CMH.getCDMAST(String.valueOf(2));
	ArrayList<DeductBean> Dlist = new ArrayList<DeductBean>();
	String empno="";
	String ename="";
	try
	{
		String action=request.getParameter("action");
		if(action.equalsIgnoreCase("showList"))
		{
			Dlist = (ArrayList<DeductBean>)request.getAttribute("Dlist");
			empno = request.getParameter("empno");
			ename = lkp.getLKP_Desc("ET", Integer.parseInt(empno));
		}
	}
	catch(Exception e)
	{
		
	}
	

%>
<script type="text/javascript">

	function deleteRec(key)
	{
		var flag= confirm("Are you sure to delete this Record?");
		if(flag)
		{	
			window.location.href="DeductionServlet?action=delete&key="+key;
		}
	}
	function modifyRec(key)
	{
		window.showModalDialog("updateDeduction.jsp?key="+key,null,"dialogWidth:690px; dialogHeight:229px; scroll:off; status:off;dialogLeft:400px;dialogTop:300px; ");
		document.form1.submit();
	}

</script>
<style type="text/css">
.row:hover {
	background-color: #CCCCCC;
	cursor: pointer;
}

.style1 {
	color: #FF0000;
}
</style>
<script>
	jQuery(function() {
          $("#EMPNO").autocomplete("list.jsp");
	});
</script>
</head>
<body style="overflow: hidden;">
   

<%@include file="mainHeader.jsp"%>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp"%>
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer"  style="overflow-y:scroll; max-height:80%; ">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1> Deduction Maintenance</h1>
	</div>
	<!-- end page-heading -->

	<table border="0" width="100%" cellpadding="0" cellspacing="0" id="content-table" >
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

<h3>Deduction Management</h3>
<table width="815" border="1" >
<tr bgcolor="#2f747e" ><td align="center" valign="middle">&nbsp;</td></tr>
<tr><td align="center" valign="middle">
   <form action="DeductionServlet?action=list" method="post" name="form1" id="form1" onsubmit="return TakeCustId()">
	<table width="519" height="26" id="customers" border="1">
	  <tr class="alt">
		<td width="84" height="20" align="left" bgcolor="#CCCCCC">Employee ID </td>
		<!-- <td align="left" valign="middle" bgcolor="#FFFFFF">
  			<input type="text" name="empno1" id="empno1" />
  			<label><input type="submit" value="Get List"></label>
  		</td> -->
  		<td><input type="text" name="EMPNO" size="30"
						id="EMPNO" onClick="showHide()" title="Enter Employee Id / Name "> &nbsp;
						<label><input type="submit" value="Get List"></label></td>
  		</tr>
  		<tr class="alt">
		<td width="84" bgcolor="#CCCCCC">Name</td>
		<td width="273" align="left" valign="middle" bgcolor="#FFFFFF"> <%= ename%> </td>
	</tr>
  </table>
</form>
<br/>
<div align="left">
<table width="670" height="44" border="1" id="customers">
  <tr align="center" valign="middle" bgcolor="#CCCCCC">
    <th width="40">Sr.NO.</th>
    <th width="88">Transaction Type</th>
    <th width="81">Account Number</th>
    <th width="87">Installment Amount</th>
    <th width="81">Start Date</th>
    <th width="84">End Date</th>
    <th width="89">No Of Install/Install Left</th>
	 <th width="114">&nbsp;</th>
   </tr>
</table>
</div>
<div style="height:150px; overflow-y:scroll; width:auto;" align="left">
<table width="780" border="1" align="center" id="customers">  
 <%
 	if(Dlist.size()>0)
 	{
 		int i=0;       
 		for(DeductBean DB: Dlist)
 		{
 			i++;
 			String str=DB.getEMPNO()+"-"+DB.getTRNCD()+"-"+DB.getSRNO();
 %>  	
  		<tr align="center" valign="middle" class="alt">
    		<td bgcolor="#F0F0F0" width="40"><%=i %></td>
    		<td width="89"><%=CMH.getCDesc(DB.getTRNCD()) %></td>
    		<td width="81"><%=DB.getAC_NO() %></td>
    		<td width="87"><%=DB.getAMOUNT() %></td>
    		<td width="81"><%=DB.getREPAY_START() %></td>
    		<td width="84"><%=DB.getEND_DATE() %></td>
    		<td width="89"><%=DB.getNo_Of_Installment() %></td>
			<td align="left" valign="middle" width="116">
				<label><input type="button" name="modify" value="Modify" onClick="modifyRec('<%=str %>')"></label>
			 	<label> <input type="button" name="delete" value="Delete" onClick="deleteRec('<%=str %>')"></label>
		    </td>
  		</tr>
<%
 		}
 	} else {
%>
<tr><td align="left" width="770" valign="middle" class="alt">No Records Available</td></tr>
<%} %>
</table>
</div>
<br/>

<tr bgcolor="#2f747e" ><td align="center" valign="middle">&nbsp;</td></tr>
<tr><td align="center" valign="middle">
<h4>Add Deduction</h4>
All fields are Mandatory
<form action="DeductionServlet?action=addnew" method="post" onsubmit="return DeductValidation()">
<table width="637" border="1" id="customers">
  <tr class="alt">
    <td width="120" align="right" valign="middle" bgcolor="#FFFFFF">Transaction Code </td>
    <td width="179" bgcolor="#FFFFFF"><label>
    <select name="select.trncd" id="select.trncd" style="width: 175px;">
    <option value="0" selected>Select</option>
    			<%
						for (CodeMasterBean temp1:CMBList)
						{
				%>			
							<option value="<%=temp1.getTRNCD()%>"><%=temp1.getDISC()%></option>
   				<%
						}
				%>
    </select>
  </label></td>
    <td width="12" bgcolor="#CCCCCC">&nbsp;</td>
    <td width="122" align="right" bgcolor="#FFFFFF">Subsystem Code </td>
    <td width="182" bgcolor="#FFFFFF"><input type="text" name="subSysCode" id="subSysCode"> </td>
  </tr>
  <tr class="alt">
    <td align="right" valign="middle" bgcolor="#FFFFFF">Account Number </td>
    <td bgcolor="#FFFFFF"><input type="text" name="acno" id="acno" maxlength="15"></td>
    <td bgcolor="#CCCCCC">&nbsp;</td>
    <td align="right" bgcolor="#FFFFFF">Sanction Date </td>
    <td bgcolor="#FFFFFF"><input name="sancDate" id="sancDate" type="text" value="dd-mmm-yyyy" onFocus="if(value=='dd-mmm-yyyy') {value=''}" onBlur="if(value=='') {value='dd-mmm-yyyy';}" >&nbsp;<img src="images/cal.gif" align="absmiddle" style="vertical-align:middle; cursor:pointer;" onClick="javascript:NewCssCal('sancDate', 'ddmmmyyyy')" /></td>
  </tr>
  <tr class="alt">
    <td align="right" valign="middle" bgcolor="#FFFFFF">BOD Sanction No.</td>
    <td bgcolor="#FFFFFF"><input type="text" name="sancNo" id="sancNo"> </td>
    <td bgcolor="#CCCCCC">&nbsp;</td>
    <td align="right" bgcolor="#FFFFFF">Sanction Amount </td>
    <td bgcolor="#FFFFFF"><input type="text" name="sacnAmt" id="sacnAmt"></td>
  </tr>
  <tr class="alt">
    <td align="right" valign="middle" bgcolor="#FFFFFF">Start Date</td>
    <td bgcolor="#FFFFFF"><input name="startDate" id="startDate" type="text" value="dd-mmm-yyyy" onFocus="if(value=='dd-mmm-yyyy') {value=''}" onBlur="if(value=='') {value='dd-mmm-yyyy';}" >&nbsp;<img src="images/cal.gif" align="absmiddle" style="vertical-align:middle; cursor:pointer;" onClick="javascript:NewCssCal('startDate', 'yyyymmdd')" /></td>
    <td bgcolor="#CCCCCC">&nbsp;</td>
    <td align="right" bgcolor="#FFFFFF">End Date </td>
    <td bgcolor="#FFFFFF"><input name="endDate" id="endDate" type="text" value="dd-mmm-yyyy" onFocus="if(value=='dd-mmm-yyyy') {value=''}" onBlur="if(value=='') {value='dd-mmm-yyyy';}" onchange="return calculate()">&nbsp;<img src="images/cal.gif" align="absmiddle" style="vertical-align:middle; cursor:pointer;" onClick="javascript:NewCssCal('endDate', 'yyyymmdd')" /></td>
  </tr>
  <tr class="alt">
  	<td align="right" bgcolor="#FFFFFF">Total Installments </td>
    <td bgcolor="#FFFFFF"><input type="text" name="Installments" id="Installments" ></td>
    <td bgcolor="#CCCCCC">&nbsp;</td>
    <td align="right" valign="middle" bgcolor="#FFFFFF">Installment Amt. </td>
    <td bgcolor="#FFFFFF"><input type="text" name="install" id="install" readonly></td>
  </tr>
  
  <tr class="alt">
    <td height="18" align="right" valign="middle" bgcolor="#FFFFFF">Cummulative</td>
    <td bgcolor="#FFFFFF">
      <select name="select.cuml" id="select.cuml">
        <option value="Y">YES</option>
        <option value="N" selected="selected">NO</option>
      </select>
  </td>
    <td bgcolor="#CCCCCC">&nbsp;</td>
    <td align="right" bgcolor="#FFFFFF">Active</td>
    <td bgcolor="#FFFFFF">
      <select name="select.active" id="select.active">
        <option value="Y" selected="selected">YES</option>
        <option value="N">NO</option>
      </select>
 </td>
  </tr>
  <tr align="center" class="alt">
    <td colspan="5" valign="middle">
      <label><input type="submit" name="save" value="Save" /></label>
      <label><input type="reset" name="clear" value="Clear" /></label>
    </td>
    </tr>
</table>
<input type="hidden" value="<%=empno%>" name="empno" id="empno">
</form>
<br/>
</td></tr>
<tr><td align="center" valign="middle" bgcolor="#2f747e">&nbsp;</td>
</tr>

</table>

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
