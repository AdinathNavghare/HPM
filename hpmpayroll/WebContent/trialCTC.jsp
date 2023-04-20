<%@page import="payroll.DAO.LookupHandler"%>
<%@page import="payroll.DAO.CTCHandler"%>
<%@page import="payroll.Core.ReportDAO"%>
<%@page import="payroll.DAO.EmployeeHandler"%>
<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.DAO.TranHandler"%>
<%@page import="payroll.Model.TranBean"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="payroll.DAO.CodeMasterHandler"%>
<%@page import="java.util.Locale"%>
<%@page import="com.ibm.icu.text.NumberFormat"%>
<%@page import="payroll.DAO.PostingHandler"%>
<%@page import="payroll.Model.AutopostBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=iso-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>CTC  &copy; DTS3 </title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css" media="screen" title="default" />
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>


<style type="text/css">
<!--
body,td,th {
	font-family: Times New Roman;
	font-size: 16px;
}
.style3 {font-size: 1.5em}
-->
</style>
<script>
	jQuery(function() {
          $("#EMPNO").autocomplete("list.jsp");
	});
	
	
	$(document).ready(function () {
	    $('#edit').click(function () {
	    	
	    	if($('input:text').attr("disabled"))
	    		{
	    	$('input:text').attr("disabled",false);
	    	$('#edit').attr('value','Disable');
	    	$('#createSalary').attr("disabled",false);
	    	$('input:checkbox').attr("disabled",false);
	    		}
	    	else
	    		{
	    		$('#createSalary').attr("disabled",true);
	    		$('input:text').attr("disabled",true);
	        	$('#edit').attr('value','Edit');
	        	$('input:checkbox').attr("disabled",true);
	    		}
	    });
	    
	});   
	
	
	function chk()
	{ var empno=document.getElementById("EMPNO").value;
	var res=empno.indexOf(":");
	if(res>0)
		{
		return true;
		}
	else
		{
		alert("Please select Employee !");
		document.getElementById("EMPNO").value="";
		document.getElementById("EMPNO").focus();
		return false;}
	
	}
	
	
</script>
<%
	CodeMasterHandler CMH = new CodeMasterHandler();
EmployeeBean empBean=new EmployeeBean();
EmployeeHandler EmpH=new EmployeeHandler(); 
CTCHandler CH=new CTCHandler();
	String action = "";
	String emp="";
	TranHandler TH = new TranHandler();	
	
	
	
	
	
	ArrayList<TranBean> incTranVals = new ArrayList<TranBean>();
	try
	{
		action = request.getParameter("action")==null?"":request.getParameter("action").toString();
		if(action.equalsIgnoreCase("2"))	//return from Save
		{
			emp = request.getAttribute("emp").toString();
			incTranVals = (ArrayList<TranBean>)request.getAttribute("incTranVals");
			
		}
		else
		{
			System.out.println("INTO ELSE..............");
			System.out.println("EMPNO="+session.getAttribute("EMPNO").toString());
			 incTranVals = TH.getCTCDISPLAY(Integer.parseInt(session.getAttribute("EMPNO").toString()));
		}
		
	}
	catch(Exception e)
	{
	e.printStackTrace();	
	}
		

%>
<script type="text/javascript">
var month="";
var xmlhttp;
var url="";
var date;
if(window.XMLHttpRequest)
{
	xmlhttp=new XMLHttpRequest;
}
else //if(window.ActivXObject)
{   
	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
}

function getNegtvSalMonths()
{
	url="CTCServlet?action=noctc";
	
	xmlhttp.onreadystatechange=function()
	{
		if(xmlhttp.readyState==4)
		{
			var response1 = xmlhttp.responseText;
			document.getElementById("nsalary").innerHTML = response1;
			$("#atree").treeview({
				collapsed: false,
				animated: "medium",
				control:"#sidetreecontrol1",
				persist: "location"
			});
			if(response1.length!=0)
			document.getElementById("ctc_div").hidden=false;
			
		}
		
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
	
}



	function calcMonthly(flag)
	{
		
		document.getElementById("monthly").value = Math.round((document.getElementById("annual").value / 12));
		calcTotal();
		
	/* 	if(document.getElementById("basic").value!="")
		{ */
			if(flag == 0)
			{
		
				calcValue('basic','basicOn','basicVal','yearlyBasicVal','basicValType');
		
			}
		
			calcValue('da','daOn','daVal','yearlyDaVal','daValType');
			calcValue('hra','hraOn','hraVal','yearlyHraVal','hraValType');
			calcValue('edu','eduOn','eduVal','yearlyEduVal','eduValType');
			calcValue('insu','insuOn','insuVal','yearlyInsuVal','insuValType');
			calcValue('conv','convOn','convVal','yearlyConvVal','convValType');
			calcValue('bonus','bonusOn','bonusVal','yearlyBonusVal','bonusValType');
			calcValue('spAllow','spAllowOn','spAllowVal','yearlySpAllowVal','spAllowValType');
			calcValue('col','colOn','colVal','yearlyColVal','colValType');
			calcValue('lev','levOn','levVal','yearlyLevVal','levValType');
			calcValue('any','anyOn','anyVal','yearlyAnyVal','anyValType');
			calcValue('pfe','pfeOn','pfeVal','yearlyPfeVal','pfeValType');
			calcValue('addLess','addLessOn','addLessVal','yearlyAddLessVal','addLessValType');
			
		/* }
		else
		{
			document.getElementById("basic").value=0;
		}
		 */
	}
	
	function calcValue(per,based,view,yearly,type)
	{
		var percent = document.getElementById(per).value;
		if(percent!="")
		{
			
			var code = document.getElementById(based).value;
			var basedAmt = 0;
			if(code==101)
			{
				basedAmt = document.getElementById("basicVal").value;
			}
			else if(code==199)
			{
				basedAmt = document.getElementById("monthly").value;
			}
			
			var typeVal = document.getElementById(type).value;
			
			if(typeVal ==0)
			{
				
				document.getElementById(view).value = Math.round(((basedAmt * percent)/100.00));
				document.getElementById(yearly).value = Math.round((document.getElementById(view).value * 12.00));
			}
			else
			{
				
				document.getElementById(view).value = percent;
				document.getElementById(yearly).value = Math.round((document.getElementById(view).value * 12.00));
			}
			
			if(per == "basic")
			{
				calcMonthly(1);
			}
		}
		else
		{
			
			document.getElementById(per).value = 0.0;
			document.getElementById(view).value = 0.0;
			document.getElementById(yearly).value = 0.0;
		}
		calcTotal();
	}
	
	function calcTotal()
	{
		
		var a = document.getElementById("basicVal").value==""?0.0:(document.getElementById("basicVal").value);
		var b = document.getElementById("daVal").value==""?0.0:(document.getElementById("daVal").value);
		var c = document.getElementById("hraVal").value==""?0.0:(document.getElementById("hraVal").value);
		var d = document.getElementById("eduVal").value==""?0.0:(document.getElementById("eduVal").value);
		var e = document.getElementById("insuVal").value==""?0.0:(document.getElementById("insuVal").value);
		var h = document.getElementById("colVal").value==""?0.0:(document.getElementById("colVal").value);
		var m = document.getElementById("levVal").value==""?0.0:(document.getElementById("levVal").value);
		var n = document.getElementById("anyVal").value==""?0.0:(document.getElementById("anyVal").value);
		
		var j = document.getElementById("bonusVal").value==""?0.0:(document.getElementById("bonusVal").value);
		var k = document.getElementById("spAllowVal").value==""?0.0:(document.getElementById("spAllowVal").value);
		var i = document.getElementById("convVal").value==""?0.0:(document.getElementById("convVal").value);
		
		var total_Ern = parseFloat(a)+parseFloat(b)+parseFloat(c)+parseFloat(d)+parseFloat(e)+parseFloat(h)+parseFloat(i)+parseFloat(j)+parseFloat(k)+parseFloat(m)+parseFloat(n);
		total_Ern = Math.round(total_Ern);
		document.getElementById("total_Ern").value=total_Ern;
		
		var f = document.getElementById("pfeVal").value==""?0.0:(document.getElementById("pfeVal").value);
		var g = document.getElementById("addLessVal").value==""?0.0:(document.getElementById("addLessVal").value);
		
		var total_Ded = parseFloat(f)+parseFloat(g);
		total_Ded = Math.round(total_Ded);
		document.getElementById("total_Ded").value=total_Ded; 
		
		var total = parseFloat(a)+parseFloat(b)+parseFloat(c)+parseFloat(d)+parseFloat(e)+parseFloat(f)+parseFloat(g)+parseFloat(h)+parseFloat(i)+parseFloat(j)+parseFloat(k)+parseFloat(m)+parseFloat(n);
		total = Math.round(total);
		
		document.getElementById("total").value=total; 
		
		//Total of yearly amount
		
		var yearlyBasic = document.getElementById("yearlyBasicVal").value==""?0.0:(document.getElementById("yearlyBasicVal").value);
		var yearlyDA = document.getElementById("yearlyDaVal").value==""?0.0:(document.getElementById("yearlyDaVal").value);
		var yearlyHra = document.getElementById("yearlyHraVal").value==""?0.0:(document.getElementById("yearlyHraVal").value);
		var yearlyEdu = document.getElementById("yearlyEduVal").value==""?0.0:(document.getElementById("yearlyEduVal").value);
		var yearlyInsu = document.getElementById("yearlyInsuVal").value==""?0.0:(document.getElementById("yearlyInsuVal").value);
		var yearlyCOL = document.getElementById("yearlyColVal").value==""?0.0:(document.getElementById("yearlyColVal").value);
		var yearlyLEV = document.getElementById("yearlyLevVal").value==""?0.0:(document.getElementById("yearlyLevVal").value);
		var yearlyANY = document.getElementById("yearlyAnyVal").value==""?0.0:(document.getElementById("yearlyAnyVal").value);
		var yearlyBonus = document.getElementById("yearlyBonusVal").value==""?0.0:(document.getElementById("yearlyBonusVal").value);
		var yearlySplAllow = document.getElementById("yearlySpAllowVal").value==""?0.0:(document.getElementById("yearlySpAllowVal").value);
		var yearlyConv = document.getElementById("yearlyConvVal").value==""?0.0:(document.getElementById("yearlyConvVal").value);
		
		var yearlyTotalErn = parseFloat(yearlyBasic) + parseFloat(yearlyDA) + parseFloat(yearlyHra) + parseFloat(yearlyEdu) + parseFloat(yearlyInsu) + parseFloat(yearlyCOL)
							+ parseFloat(yearlyConv) + parseFloat(yearlyBonus) + parseFloat(yearlySplAllow)+ parseFloat(yearlyLEV)+ parseFloat(yearlyANY);
		yearlyTotalErn = Math.round(yearlyTotalErn);
		document.getElementById("yearlyTotalErn").value = yearlyTotalErn;
		
		var yearlyPf = document.getElementById("yearlyPfeVal").value==""?0:document.getElementById("yearlyPfeVal").value;
		var yearlyAddLessAmt = document.getElementById("yearlyAddLessVal").value==""?0.0:(document.getElementById("yearlyAddLessVal").value);
		
		var yearlyTotalDed = parseFloat(yearlyPf) + parseFloat(yearlyAddLessAmt);
		yearlyTotalDed = Math.round(yearlyTotalDed);
		document.getElementById("yearlyTotalDed").value = yearlyTotalDed; 
		
		var yearlyTotal = parseFloat(yearlyBasic) + parseFloat(yearlyDA) + parseFloat(yearlyHra) + parseFloat(yearlyEdu) + parseFloat(yearlyInsu) + parseFloat(yearlyCOL)
							+ parseFloat(yearlyConv) + parseFloat(yearlyBonus) + parseFloat(yearlySplAllow) + parseFloat(yearlyPf) + parseFloat(yearlyAddLessAmt)+ parseFloat(yearlyLEV)+ parseFloat(yearlyANY);
		yearlyTotal = Math.round(yearlyTotal);
		document.getElementById("yearlyTotal").value = yearlyTotal; 
		
	}
	
	function validate() 
	{
		if(document.getElementById("EMPNO").value == "")
		{
			alert("Invalid Employee");
			return false;
		}
		
		var EMPNO = document.getElementById("EMPNO").value;
		var atpos=EMPNO.indexOf(":");
		if (atpos<1)
		  {
		  alert("Please Select Correct Employee Name");
		  return false;
		  }
		if(document.getElementById("annual").value == "" )
		{
			alert("No Annual CTC entered, Can't create Salary Structure");
			return false;
		}
		
		var annual = document.getElementById("annual").value;
		if(annual <=0.0 )
		{
			alert("Annual CTC Should Not be Zero, Can't create Salary Structure");
			return false;
		}
		
		if(document.getElementById("basic").value == "")
		{
			alert("No BASIC value entered, Can't create Salary Structure");
			return false;
		}
		
		var monthly_gross = document.getElementById("monthly").value;
		var total = document.getElementById("total").value;
		
		
		
		if(parseFloat(monthly_gross) != parseFloat(total)){
			alert("Salary Structure is not Match");
			return false;
		}
		
		if(document.getElementById("date").value == "")
			{
			alert("Please Select Effective Date !");
			return false;
			}
		
	
		if(document.getElementById("pf").checked){
			var pf=confirm("Are you sure to apply pf ?");
			if(pf){				
				// do'not enter to go forward
			}
			else{
				return false;	
			}			
		}
		
		if(document.getElementById("pt").checked){
			var pt=confirm("Are you sure to apply PT ?");
			if(pt){
				// do'not enter to go forward
			}
			else{
				return false;	
			}			
		}
		
		if(document.getElementById("esic").checked){

			var esic=confirm("Are you sure to apply esic ?");
			if(esic){
				var monthly = document.getElementById("monthly").value;
				monthly = parseInt(monthly);
				
				if(monthly>21000){
					var check = prompt("Are you really sure for esic greater than 21000 ? Then type Yes ");	
					if(check == "yes" || check == "Yes" || check == "YES"){
						// do not enter to go forward
					} else {
						alert("Sorry ! Wrong Input ");
						return false;
					}
					
					
				}
				else{
					// do'not enter to go forward
				}
				
			}
			else{
				return false;	
			}			
		}
		
		var ans=confirm("Are you sure to Create Employee CTC ?");
		
		
		
		if(ans)
			{
			var c = prompt("Are you really sure to Create Employee CTC ? Then type Yes ");	
			if(c == "yes" || c == "Yes" || c == "YES"){
				return true;
			} else {
				alert("Sorry ! Wrong Input ");
				return false;
			}
			}
		
		else{
			return false;
		}
	
	
	
	}
	
	
	function empCTC()
	{
		var empno=document.getElementById("EMPNO").value;
		
		var res=empno.indexOf(":"); 
		
		if(res>0)
			{
			
		
			window.location.href = "NewCTCServlet?action=checkCTC&EMPNO="+empno;
		
			}
		else
			{
			alert("Please select Employee !");
			document.getElementById("EMPNO").value="";
			document.getElementById("EMPNO").focus();
			
			}
		
	}
	
	function checkFlag() 
	{
		
		var action = document.getElementById("action").value;
		if(action == "saved")
		{
			alert("Salary Structure Created Successfully.\n"+
					"You can verify it through Slab Management,"+
					" On Amount and Salary transaction Menu");
		}
		if(action == "notsaved")
			{
			alert("Please Select Proper Effective Date and then Create Salary Structure again !"); 
			
			}
		
	}
	
	function print()
	{ 
			var disp_setting="toolbar=yes,location=no,directories=yes,menubar=yes,"; 
			    disp_setting+="scrollbars=yes,width=1000, height=800, left=200, top=10"; 
			var content_vlue = document.getElementById("table-content").innerHTML; 
			
			var docprint=window.open("","",disp_setting); 
				docprint.document.open(); 
				docprint.document.write('<html><head><title>Inel Power System</title>'); 
				docprint.document.write("<style type=\"text/css\">	body,td,th { font-family: Times New Roman; font-size: 14px;}	</style></head><body onLoad=\"self.print()\" style=\"font-family:Times New Roman; font-size:9px;\" ><center>");          
				docprint.document.write(content_vlue);          
				docprint.document.write('</center></body></html>'); 
				docprint.document.close(); 
				docprint.focus(); 
	}
	
	
	
	function compareDate()
	{
		
		var E_date=document.getElementById("date").value;
		var dt=document.getElementById("doj").value;
		var max_date=document.getElementById("max_date").value;
		var min_date=document.getElementById("min_date").value;
		
	    o_Date = dt.replace(/-/g,"/");
	    e_date=E_date.replace(/-/g,"/");
	    
	    mx=max_date.replace(/-/g,"/");
	    mn=min_date.replace(/-/g,"/");
	    
	    
	     
	    var d1 = new Date(e_date);

	    var d2 =new  Date(o_Date);
	   
	    var d_mx = new Date(mx);
		var d_mn =new  Date(mn);
		if (d1.getTime()<d2.getTime()) 
	    {
			
	    		alert("Effective Date must be Greater or within the month of Joining !\n\t Please Select Effective Date again !");
	    		document.getElementById('date').value="";
	    		
	    }
		else if(d1.getTime() < d_mn.getTime() || d1.getTime() > d_mx.getTime())
			{
			
			alert("Effective Date must be within the Current Salary month !\n\t Please Select Effective Date again !");
    		document.getElementById('date').value="";
			
			}
		
		
		
		
	}
</script>

</head>
<body style="overflow: hidden;" onLoad="checkFlag()"> 
<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer"  >
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Display Cost To Company (CTC)</h1>
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
		<td align="center">
		<!--  start content-table-inner ...................................................................... START -->
		<div id="content-table-inner">
		
			<!--  start table-content  -->
			<div id="table-content" style="overflow-y:auto; max-height:570px; ">
			<table width="850" border="1" align="center">
			  <tr><td align="center"><br/>
			  <form action="CTCServlet?action=create" method="post" onKeyPress="return event.keyCode != 13;" onSubmit="return validate()">
			  <table style="width: 90%;" height="66" border="1">
			   <tr bgcolor="#eaeaea">
			   <%	
			   
			   empBean=EmpH.getEmployeeInformation(emp==null || emp.equalsIgnoreCase("")?session.getAttribute("EMPNO").toString():emp);
			   
			   
			   
			   String max_date=CH.getMAXDateinPaytran(Integer.toString(empBean.getEMPNO()));
			   String min_date=CH.getMINDateinPaytran(Integer.toString(empBean.getEMPNO()));
			   
				emp=empBean.getFNAME()+" "+empBean.getLNAME()+":"+empBean.getEMPCODE()+":"+empBean.getEMPNO();
			   
			   
			   %>
			     <td height="33" colspan="2">&nbsp;Type Employee Name or Number 
			       <input name="EMPNO" type="text" id="EMPNO"  onClick="this.select();" value="<%=emp%>" disabled="disabled" size="40">
			       <input type="button" value="Check" onclick="empCTC()"/>
			    	  <% String employeeString="97";
				
				if (employeeString.equals(session.getAttribute("EMPNO").toString())){
					%>
					<input type="button" value="EDIT" id="edit">
			       <%} %>
			     </td>
			   </tr>
			   <tr>
			   <%
			   double sum = 0.0d;
			   double gross = 0.0d;
			   float pf=0.0f;
			   float pt=0.0f;
			   float esic=0.0f;
			   for(TranBean tb : incTranVals){
				  sum = sum+tb.getNET_AMT();
				  pf=tb.getPf();
				  pt=tb.getPt();
				  esic=tb.getEsic();
				  
			   }
			   gross = 12*sum;
			   System.out.println("SUM="+sum);
			   System.out.println("SUM*12="+sum*12);
			   %>
			     <td width="261">&nbsp;Annual Total  
			       <input name="annual" type="text" id="annual"  onKeyUp="calcMonthly(0)" value="<%=gross %>" dir="rtl" tabindex="1" disabled="disabled">
			       Rs.</td>
			     <td width="272">&nbsp;Monthly Gross 
			       <input name="monthly" type="text" id="monthly" disabled="disabled" value="<%= sum %>" style="background-color:silver;" dir="rtl">
			       Rs.</td>
			   </tr>
			   
			   <tr>
			   <td width="261">
			   Joining Date : <%=empBean.getDOJ()==null?ReportDAO.getSysDate():empBean.getDOJ()%> 
			   <input type="hidden" id='doj' value="<%=empBean.getDOJ()==null?ReportDAO.getSysDate():empBean.getDOJ()%>">
			   <input type="hidden" id='max_date' value="<%=max_date%>">
			   <input type="hidden" id='min_date' value="<%=min_date%>">
			      
			   </td> 
			   <td width="272">
			       Effective Date : 
			       <input name="date" size="20"
							id="date" type="text" value="" readonly="readonly"
							onFocus="if(value=='dd-mmm-yyyy') {value=''}"
							onBlur="if(value=='') {value='';}" onchange="compareDate()">&nbsp;<img
							src="images/cal.gif" align="absmiddle"
							style="vertical-align: middle; cursor: pointer;"
							onClick="javascript:NewCssCal('date', 'ddmmmyyyy')" />
			       </td>
			   </tr>
			   <tr height="25">
			      <td colspan="2">&nbsp;Eligible for PF &nbsp;
			     
			      				
			      				<%if(pf==1){%>
			       				<input name="pf" type="checkbox" id="pf" value="true" checked="checked" disabled="disabled">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			       				<%}else{ %>
			       				<input name="pf" type="checkbox" id="pf" value="true" disabled="disabled">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			       				<%} %>
			       				Eligible for PT &nbsp;
			       				<%if(pt==1){ %>
			       				<input name="pt" type="checkbox" id="pt" value="true" checked="checked" disabled="disabled">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			       				<%}else{ %>
			       				<input name="pt" type="checkbox" id="pt" value="true" disabled="disabled">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			       				<%} %>
			   					Eligible for ESIC &nbsp;
			   					<%if(esic==1){ %>
			   					<input name="esic" type="checkbox" id="esic" value="true" checked="checked" disabled="disabled">
			   					<%}else{ %>
			      				<input name="esic" type="checkbox" id="esic" value="true" disabled="disabled">
			      				<%} %>
			      				
			      				
			      				
			      </td>
			   </tr>
			   </table>
			   
			   <p>&nbsp;</p>
			   <table style="line-height: 20px;" width="850" border="1">
               	<tr align="center" bgcolor="#eaeaea">
                  <td width="50" align="center"><b>Sr. </b></td>
                  <td width="132"><b>Type</b></td>
                  <td width="80"><b>Value Type</b> </td>
                  <td width="70"><b>Value</b></td>
                  <td width="70"><b>Based On</b></td>
                  <td width="130"><b>Monthly Amount</b></td>
                  <td width="120"><b>Yearly Amount</b></td>
                  
                </tr>
                <tr>
                <% float addition=0.0f;
			   		for(TranBean tb : incTranVals){
						if(tb.getTRNCD()==101){%>
				 <td align="center" bgcolor="#FFFFFF">1</td>
			     <td align="left" bgcolor="#FFFFFF">&nbsp;Basic </td>
			     <td align="center"><select name="basicValType" id="basicValType" onChange="calcValue('basic','basicOn','basicVal','yearlyBasicVal','basicValType')">
			     
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
			     </select></td>
			     <td align="center">&nbsp;<input name="basic" type="text" value="<%= tb.getINP_AMT() %>" id="basic" size="8" onkeyup="calcValue('basic','basicOn','basicVal','yearlyBasicVal','basicValType')" disabled="disabled" autocomplete="off"  dir="rtl" tabindex="2" autocomplete="false"></td>
			     <td align="center" bgcolor="#FFFFFF"><select name="basicOn" id="basicOn">
			     <option value="199">Gross</option>
			     </select></td>
			     <%addition=addition+tb.getNET_AMT(); %>
			     <td bgcolor="#FFFFFF">&nbsp;<input name="basicVal" readonly="readonly" type="text" size="15" id="basicVal" dir="rtl" value="<%=tb.getNET_AMT()%>" disabled="disabled" > Rs.</td>
			    <td bgcolor="#FFFFFF">&nbsp;<input name="yearlyBasicVal" type="text"  value="<%= tb.getNET_AMT()*12 %>" disabled="disabled" id="yearlyBasicVal" readonly="readonly" size="13" dir="rtl"> Rs.</td>
			     </tr>
				<%}		  
			   	}
                	for(TranBean tb : incTranVals){
                		if(tb.getTRNCD()==104){	%>
                <tr>
                  <td align="center" bgcolor="#FFFFFF">2</td>
                  <td align="left" bgcolor="#FFFFFF">&nbsp;Medical Allowance </td>
                  <td align="center"><select name="daValType" id="daValType" onChange="calcValue('da','daOn','daVal','yearlyDaVal','daValType')">
                   
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="da" type="text" value="<%= tb.getINP_AMT() %>" id="da" size="8" onkeyup="calcValue('da','daOn','daVal','yearlyDaVal','daValType')" disabled="disabled" dir="rtl" tabindex="3"></td>
                  <td align="center" bgcolor="#FFFFFF"><select name="daOn" id="daOn" onChange="calcValue('da','daOn','daVal','yearlyDaVal','daValType')">
                    
                    <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                    
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="daVal"  readonly="readonly" type="text" id="daVal" size="15" value="<%= tb.getNET_AMT() %>" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input  readonly="readonly" name="yearlyDaVal" type="text" id="yearlyDaVal" value="<%= tb.getNET_AMT()*12 %>" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
                	}
                for(TranBean tb : incTranVals){
                if(tb.getTRNCD()==103){%>
                
                <tr>
                  <td align="center" bgcolor="#FFFFFF">3</td>
                  <td align="left" bgcolor="#FFFFFF">&nbsp;HRA </td>
                  <td align="center"><select name="hraValType" id="hraValType" onChange="calcValue('hra','hraOn','hraVal','yearlyHraVal','hraValType')">
                    
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="hra" type="text" id="hra" value="<%= tb.getINP_AMT() %>" onKeyUp="calcValue('hra','hraOn','hraVal','yearlyHraVal','hraValType')" size="8" autocomplete="off" disabled="disabled" dir="rtl" tabindex="4"></td>
                  <td align="center" bgcolor="#FFFFFF"><select name="hraOn" id="hraOn" onChange="calcValue('hra','hraOn','hraVal','yearlyHraVal','hraValType')">
                    <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="hraVal" readonly="readonly" type="text" size="15" value="<%= tb.getNET_AMT() %>" id="hraVal" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input  readonly="readonly" name="yearlyHraVal" type="text" value="<%= tb.getNET_AMT()*12 %>" id="yearlyHraVal" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
                	}
                for(TranBean tb : incTranVals){
                if(tb.getTRNCD()==105){%>
                
                <tr>
                  <td align="center" bgcolor="#FFFFFF">4</td>
                  <td align="left" bgcolor="#FFFFFF">&nbsp;Education  </td>
                  <td align="center"><select name="eduValType" id="eduValType" onChange="calcValue('edu','eduOn','eduVal','yearlyEduVal','eduValType')">
                    
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="edu" type="text" id="edu" disabled="disabled" value="<%= tb.getINP_AMT() %>" onKeyUp="calcValue('edu','eduOn','eduVal','yearlyEduVal','eduValType')" size="8" autocomplete="off"  dir="rtl" tabindex="5"></td>
                  <td align="center" bgcolor="#FFFFFF"><select name="eduOn" id="eduOn" onChange="calcValue('edu','eduOn','eduVal','yearlyEduVal','eduValType')">
                     <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="eduVal" readonly="readonly" type="text" value="<%= tb.getNET_AMT() %>" size="15" id="eduVal" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input  readonly="readonly" name="yearlyEduVal" type="text" value="<%= tb.getNET_AMT()*12 %>" id="yearlyEduVal" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
                	}
                for(TranBean tb : incTranVals){
                if(tb.getTRNCD()==126){%>
                
                <tr>
                  <td align="center" bgcolor="#FFFFFF">5</td>
                  <td align="left" bgcolor="#FFFFFF">&nbsp;Min Insurance </td>
                  <td align="center"><select name="insuValType" id="insuValType" onChange="calcValue('insu','insuOn','insuVal','yearlyInsuVal','insuValType')">
                    
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="insu" type="text" value="<%= tb.getINP_AMT() %>" id="insu" onKeyUp="calcValue('insu','insuOn','insuVal','yearlyInsuVal','insuValType')" size="8" disabled="disabled" autocomplete="off"  dir="rtl" tabindex="6"></td>
                  <td align="center" bgcolor="#FFFFFF"><select name="insuOn" id="insuOn" onChange="calcValue('insu','insuOn','insuVal','yearlyInsuVal','insuValType')">
                    <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="insuVal" readonly="readonly" type="text" size="15" value="<%= tb.getNET_AMT() %>" id="insuVal" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="yearlyInsuVal"  readonly="readonly" type="text" value="<%= tb.getNET_AMT()*12 %>" id="yearlyInsuVal" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
                	}
                for(TranBean tb : incTranVals){
                if(tb.getTRNCD()==108){%>
                
                <tr bgcolor="#FFFFFF">
                  <td align="center">6</td>
                  <td align="left">&nbsp;Conveyance</td>
                  <td align="center"><select name="convValType" id="convValType" onChange="calcValue('conv','convOn','convVal','yearlyConvVal','convValType')">
                   
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="conv" type="text" value="<%= tb.getINP_AMT() %>" id="conv" onKeyUp="calcValue('conv','convOn','convVal','yearlyConvVal','convValType')" size="8" autocomplete="off" disabled="disabled"  dir="rtl" tabindex="11"></td>
                  <td align="center"><select name="convOn" id="convOn" onChange="calcValue('conv','convOn','convVal','yearlyConvVal','convValType')">
                    <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td>&nbsp;<input name="convVal" type="text" id="convVal"  readonly="readonly" size="15" value="<%= tb.getNET_AMT() %>" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input  readonly="readonly" name="yearlyConvVal" type="text" value="<%= tb.getNET_AMT()*12 %>" id="yearlyConvVal" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
                	}
                for(TranBean tb : incTranVals){
                if(tb.getTRNCD()==107){%>
                
                <tr bgcolor="#FFFFFF">
                  <td align="center">7</td>
                  <td align="left">&nbsp;Special Allowance</td>
                  <td align="center"><select name="spAllowValType" id="spAllowValType" onChange="calcValue('spAllow','spAllowOn','spAllowVal','yearlySpAllowVal','spAllowValType')">
                  
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="spAllow" type="text" value="<%= tb.getINP_AMT() %>" id="spAllow"  onKeyUp="calcValue('spAllow','spAllowOn','spAllowVal','yearlySpAllowVal','spAllowValType')" size="8" autocomplete="off" disabled="disabled" dir="rtl" tabindex="11"></td>
                  <td align="center"><select name="spAllowOn" id="spAllowOn" onChange="calcValue('spAllow','spAllowOn','spAllowVal','yearlySpAllowVal','spAllowValType')">
                    <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td>&nbsp;<input name="spAllowVal" type="text" id="spAllowVal" readonly="readonly" size="15" disabled="disabled" value="<%= tb.getNET_AMT() %>" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input   readonly="readonly" name="yearlySpAllowVal" type="text" id="yearlySpAllowVal" value="<%= tb.getNET_AMT()*12 %>" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
                	}
                for(TranBean tb : incTranVals){
                if(tb.getTRNCD()==135){%>
                
                <tr bgcolor="#FFFFFF">
                  <td align="center">8</td>
                  <td align="left">&nbsp;Bonus</td>
                  <td align="center"><select name="bonusValType" id="bonusValType" onChange="calcValue('bonus','bonusOn','bonusVal','yearlyBonusVal','bonusValType')">
                   
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="bonus" type="text" value="<%= tb.getINP_AMT() %>" id="bonus" size="8" autocomplete="off"  onKeyUp="calcValue('bonus','bonusOn','bonusVal','yearlyBonusVal','bonusValType')" disabled="disabled" dir="rtl" tabindex="11"></td>
                  <td align="center"><select name="bonusOn" id="bonusOn" onChange="calcValue('bonus','bonusOn','bonusVal','yearlyBonusVal','bonusValType')">
                     <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td>&nbsp;<input name="bonusVal" type="text" id="bonusVal" readonly="readonly" size="15" disabled="disabled" value="<%= tb.getNET_AMT() %>" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input   readonly="readonly" name="yearlyBonusVal" type="text" id="yearlyBonusVal" disabled="disabled" value="<%= tb.getNET_AMT()*12 %>" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
                	}
                for(TranBean tb : incTranVals){
                if(tb.getTRNCD()==128){%>
                
                <tr bgcolor="#FFFFFF">
                  <td align="center">9</td>
                  <td align="left">&nbsp;COL</td>
                  <td align="center"><select name="colValType" id="colValType" onChange="calcValue('col','colOn','colVal','yearlyColVal','colValType')">
                   
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="col" type="text" value="<%= tb.getINP_AMT() %>" id="col" size="8" onKeyUp="calcValue('col','colOn','colVal','yearlyColVal','colValType')" autocomplete="off" disabled="disabled" dir="rtl" tabindex="10"></td>
                  <td align="center"><select name="colOn" id="colOn" onChange="calcValue('col','colOn','colVal','yearlyColVal','colValType')">
                     <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td>&nbsp;<input name="colVal" type="text" id="colVal"  readonly="readonly" value="<%= tb.getNET_AMT() %>" size="15" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input  readonly="readonly" name="yearlyColVal" value="<%= tb.getNET_AMT()*12 %>" type="text" id="yearlyColVal" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                
                
                <%}
                }
                
                /* dfgkj&&&&&&&&&&&& */
              for(TranBean tb : incTranVals){
             	  
                if(tb.getTRNCD()==106){%>
                
                <tr bgcolor="#FFFFFF">
                  <td align="center">10</td>
                  <td align="left">&nbsp;LTA</td>
                  <td align="center"><select name="levValType" id="levValType" onChange="calcValue('lev','levOn','levVal','yearlyLevVal','levValType')">
                   
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="lev" type="text" value="<%= tb.getINP_AMT() %>" id="lev" size="8" onKeyUp="calcValue('lev','levOn','levVal','yearlyLevVal','levValType')" autocomplete="off" disabled="disabled" dir="rtl" tabindex="10"></td>
                  <td align="center"><select name="levOn" id="levOn" onChange="calcValue('lev','levOn','levVal','yearlyLevVal','levValType')">
                     <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td>&nbsp;<input name="levVal" type="text" id="levVal"  readonly="readonly" value="<%= tb.getNET_AMT() %>" size="15" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input  readonly="readonly" name="yearlyLevVal" value="<%= tb.getNET_AMT()*12 %>" type="text" id="yearlyLevVal" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
               	} 
               	
               	for(TranBean tb : incTranVals){
             	  
                if(tb.getTRNCD()==145){%>
                
                <tr bgcolor="#FFFFFF">
                  <td align="center">11</td>
                  <td align="left">&nbsp;ANONYMOUS</td>
                  <td align="center"><select name="anyValType" id="anyValType" onChange="calcValue('any','anyOn','anyVal','yearlyAnyVal','anyValType')">
                   
			     <option value="0" <%=tb.getSRNO()==0?"selected":"" %>>Percentage</option>
			     <option value="1" <%=tb.getSRNO()==1?"selected":"" %>>Fixed Value</option>
			     
                     </select></td>
                  <td align="center">&nbsp;<input name="any" type="text" value="<%= tb.getINP_AMT() %>" id="any" size="8" onKeyUp="calcValue('any','anyOn','anyVal','yearlyAnyVal','anyValType')" autocomplete="off" disabled="disabled" dir="rtl" tabindex="10"></td>
                  <td align="center"><select name="anyOn" id="anyOn" onChange="calcValue('any','anyOn','anyVal','yearlyAnyVal','anyValType')">
                     <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%addition=addition+tb.getNET_AMT(); %>
                  <td>&nbsp;<input name="anyVal" type="text" id="anyVal"  readonly="readonly" value="<%= tb.getNET_AMT() %>" size="15" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input  readonly="readonly" name="yearlyAnyVal" value="<%= tb.getNET_AMT()*12 %>" type="text" id="yearlyAnyVal" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
               	} %>
               	
               	
               	
                 <tr>
                  <td colspan="5" align="right">Total Rs.&nbsp;&nbsp;</td>
                  <td>&nbsp;<input name="total_Ern"  readonly="readonly" type="text" disabled="disabled" value="<%=addition %>" size="15" id="total_Ern" dir="rtl" style="background-color:#7fe5ff">/- </td>
                  <td bgcolor="#FFFFFF">&nbsp;<input  readonly="readonly" name="yearlyTotalErn" type="text" value="<%=addition *12%>" id="yearlyTotalErn" disabled="disabled" size="13" dir="rtl" style="background-color:#eaeaea"> /-</td>
                </tr>
                
                <tr>
                	<td colspan="7"><b>Deductions</b></td>
                </tr>
                <%
                   /* dfgkj&&&&&&&&&&&& */
                float deduction=0.0f;
                for(TranBean tb : incTranVals){
                if(tb.getTRNCD()==201){%>
                
                <tr>
                  <td align="center" bgcolor="#FFFFFF">12</td>
                  <td align="left" bgcolor="#FFFFFF">&nbsp;PF Employer 12% </td>
                  <td align="center"><select name="pfeValType" id="pfeValType" onChange="calcValue('pfe','pfeOn','pfeVal','yearlyPfeVal','pfeValType')">
                    <option value="0" selected>Percentage</option>
                  </select></td>
                  <td align="center">&nbsp;<input name="pfe" type="text" value="<%= tb.getINP_AMT() %>" id="pfe" size="8" autocomplete="off" onKeyUp="calcValue('pfe','pfeOn','pfeVal','yearlyPfeVal','pfeValType')" value="12" disabled="disabled" dir="rtl" tabindex="8"></td>
                  <td align="center" bgcolor="#FFFFFF"><select name="pfeOn" id="pfeOn" onChange="calcValue('pfe','pfeOn','pfeVal','yearlyPfeVal','pfeValType')">
                     <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                  <%deduction =deduction + tb.getNET_AMT(); %>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="pfeVal" readonly="readonly" type="text" id="pfeVal" value="<%= tb.getNET_AMT() %>" size="15" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="yearlyPfeVal" readonly="readonly" type="text" value="<%= tb.getNET_AMT()*12 %>" id="yearlyPfeVal" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%} 
                	}
                for(TranBean tb : incTranVals){
                if(tb.getTRNCD()==127){%>
                <tr bgcolor="#FFFFFF">
                  <td align="center">13</td>
                  <td align="left">&nbsp;Add Less Amount </td>
                  <td align="center"><select name="addLessValType" id="addLessValType" onChange="calcValue('addLess','addLessOn','addLessVal','yearlyAddLessVal','addLessValType')">
                    <option value="1">Fixed Value</option>
                  </select></td>
                  <td align="center">&nbsp;<input name="addLess" type="text" value="<%= tb.getINP_AMT() %>" id="addLess" size="8" autocomplete="off" onKeyUp="calcValue('addLess','addLessOn','addLessVal','yearlyAddLessVal','addLessValType')" disabled="disabled" dir="rtl" tabindex="9"></td>
                  <td align="center"><select name="addLessOn" id="addLessOn" onChange="calcValue('addLess','addLessOn','addLessVal','yearlyAddLessVal','addLessValType')">
                     <option value="101" <%=tb.getADJ_AMT()==101?"selected":""%>>Basic</option>
                    <option value="199" <%=tb.getADJ_AMT()==199?"selected":""%>>Gross</option>
                  </select></td>
                   <%deduction =deduction + tb.getNET_AMT(); %>
                  <td>&nbsp;<input name="addLessVal" type="text" id="addLessVal" readonly="readonly" size="15" value="<%= tb.getNET_AMT() %>" disabled="disabled" dir="rtl"> Rs.</td>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="yearlyAddLessVal"  readonly="readonly"type="text" value="<%= tb.getNET_AMT()*12 %>" id="yearlyAddLessVal" disabled="disabled" size="13" dir="rtl"> Rs.</td>
                </tr>
                <%}} %>
                
               <tr>
                  <td colspan="5" align="right">Total Rs.&nbsp;&nbsp;</td>
                  <td>&nbsp;<input name="total_Ded" readonly="readonly" type="text" disabled="disabled" size="15" value="<%=deduction %>" id="total_Ded" dir="rtl" style="background-color:#7fe5ff"> /- </td>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="yearlyTotalDed" readonly="readonly" type="text" value="<%=deduction*12 %>" id="yearlyTotalDed" disabled="disabled" size="13" dir="rtl" style="background-color:#eaeaea"> /-</td>
                </tr>
                
                <tr bgcolor="#CCCCCC">
                  <td align="center">&nbsp;</td>
                  <td align="left">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="5" align="right">Total Rs.&nbsp;&nbsp;</td>
                  <td>&nbsp;<input name="total" type="text" disabled="disabled" size="15" readonly="readonly" id="total" value="<%=sum%>" dir="rtl" style="background-color:#7fe5ff"> /- </td>
                  <td bgcolor="#FFFFFF">&nbsp;<input name="yearlyTotal"  readonly="readonly"type="text" id="yearlyTotal" value="<%=gross%>" disabled="disabled" size="13" dir="rtl" style="background-color:#eaeaea"> /-</td>
                </tr>	
              </table>
              
              <a href="javascript:print()"> <input type="button" name="Print" value="Print"></a>
			  <% 
			
				
				if (employeeString.equals(session.getAttribute("EMPNO").toString())){
			%>
			   <input type="submit" id="createSalary" disabled="disabled" name="Submit" value="Create Salary Structure">
			   <% }else {%>
			   <input type="submit" name="Submit" disabled="disabled" value="Create Salary Structure">
			   <% }%>
			   <input type="reset" name="reset" value="Clear">
			   
              
			   <br/></form></td>
			  </tr>
			 
			  
			  
			    <% for(TranBean tb : incTranVals){
			    	EmployeeHandler emph = new EmployeeHandler();
					EmployeeBean ebean = new EmployeeBean();
					LookupHandler lookuph= new LookupHandler();
					ebean = emph.getEmployeeInformation(Integer.toString(tb.getUpdatedby()));
                if(tb.getTRNCD()==101){ %>
                 <tr><td>Updated By : 
                <%=lookuph.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getLNAME() %>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                Updated Date : <%=tb.getUpdate()%></td>
                <%}
                } %>
                </tr>
			  
			  </table>
			</div>
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
