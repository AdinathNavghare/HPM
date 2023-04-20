<%@page import="payroll.Core.ReportDAO"%>
<%@page import="payroll.DAO.TranHandler"%>
<%@page import="payroll.Model.RelieveInfoBean"%>
<%@page import="payroll.DAO.RelieveInfoHandler"%>
<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.DAO.EmployeeHandler"%>
<%@page import="payroll.DAO.BranchDAO"%>
<%@page import="payroll.Model.BranchBean"%>
<%@page import="payroll.DAO.CodeMasterHandler"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="payroll.Model.Lookup"%>
<%@page import="java.util.ArrayList"%>
<%@page import="payroll.DAO.LookupHandler"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
  
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  
<html>
<head>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />

<link rel="stylesheet" href="css/dropdown.css" type="text/css" media="screen" title="default" />
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script src="js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/datetimepicker1.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Relieving Information</title>


<%
	//String EMPNO =  session.getAttribute("EMPNO").toString();
	String EMPNO = request.getParameter("EMPNO");
	int flag1=-1;
	String Leftdate= request.getParameter("date1")==null?"":request.getParameter("date1");

	flag1=Integer.parseInt(request.getParameter("flag")==null?"-1":request.getParameter("flag")); 
	String status=request.getParameter("status")==null?"left":request.getParameter("status");
	String url="";
	//old string url
  	//String url = "EmployeeServlet?action=Relinfo&";
	if(status.equalsIgnoreCase("left"))
	{
	 url ="EmpAttendServlet?action=empApproved&empNo="+EMPNO+"&status="+status+"&date1="+Leftdate+"&"; 
	}
	
  	String act = "";
	EmployeeHandler emph = new EmployeeHandler();
	TranHandler trnh=new TranHandler();
	EmployeeBean empbn = emph.getEmployeeInformation(EMPNO);
	LookupHandler lookuph= new LookupHandler();
	RelieveInfoHandler relh = new RelieveInfoHandler();
	RelieveInfoBean rbn = relh.getRelievInfo(EMPNO);
	//String date1=ReportDAO.BOM(trnh.getSalaryDate());
	String date1=ReportDAO.BOM(trnh.getSalaryDate(EMPNO));
	//String date1=ReportDAO.EOM(trnh.getSalaryDateForLeft(EMPNO));

	session.setAttribute("empno",empbn.getEMPNO());
	
	
	if(rbn.getLEFT_DATE()==null){
		act="ADD";
		url=url+"act=ADD";
	}
	else
	{
		act="EDIT";
		url=url+"act=EDIT";
	}
	System.out.println(url);
%>

<script type="text/javascript">

if(window.XMLHttpRequest)
{
	xmlhttp=new XMLHttpRequest;
}
else 
{   
	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
}

function chk()
{
	
	var jnDate=document.getElementById('jnDate').value;
	 var lvDate=document.getElementById('lDate').value;
	 var sal_date=document.getElementById('sal_date').value;
	 
  var arr = lvDate.split("-");
  if(arr[0]==01)
	{
	alert("Are You Sure Want to Left Employee on : "+lvDate+"\n You Must Select last date of Previous Month !!..");
	document.getElementById('lDate').value="";
	}
  else 
	  {
 		var xmlhttp=new XMLHttpRequest(); ;
	 	var url="";
	 	var response1="";
	 	url="CoreServlet?action=leftDT&date="+lvDate;
		
		xmlhttp.onreadystatechange=function()
		{
			if(xmlhttp.readyState==4)
			{
			response1 = xmlhttp.responseText;
			
			if(response1==1)
				{
					alert("This is Week Off Or Holiday You can not Mark as Left !! \nPlease select another date !!..");
					document.getElementById('lDate').value="";
				}
			 }
		};
		xmlhttp.open("POST", url, true);
		xmlhttp.send();
	  }
	
	    jnDate = jnDate.replace(/-/g,"/");
	    lvDate = lvDate.replace(/-/g,"/");
	    sal_date=sal_date.replace(/-/g,"/");
		
	    var d1 = new Date(jnDate);

	    var d2 =new  Date(lvDate);
	    
	    var d3=new  Date(sal_date);
	
	    d3.setDate(d3.getDate()-1);

	      if (d2.getTime()!==null && d2.getTime() < d3.getTime()) 
	    {
	    	  
	    		alert("Leaving Date must be Greater or within  Salary Month  !!");
	    		document.getElementById('lDate').value="";
	    		document.getElementById('rDate').value="";
	    		document.getElementById('raDate').value="";
	    		 return false;
	    }
	     
	      
	  }
	  
	  

function SendRedirect()
{
	window.location.href = "approveAttendance.jsp";
	
	}

function  noBack()
{
	window.history.forward();
}


setTimeout("noBack()", 0);
window.onunload = function() 
{
    null;
};

	function getClose()
	{
		window.close();
	}
	
	 function cancel()
	 {
		 var res=confirm("Do You Not Want To Left This Employee... Are You Sure ? ");
		 if(res==true)
			 {
			 window.location.href="attendanceMain.jsp";
			 }
	 }
	 
	 
	 
	 
	function checkVal()
	{
		
		 var f = parseInt(document.getElementById("flag1").value);

			
			if (f == 1) {
				alert("Left request approved Successfully Please save the Left date of employee....");
				noBack();
					}
			
		
		var flag = document.getElementById("chek").value;
		if(flag == 1)
		{
			alert("Employee Left Successfully....Record Saved Successfully !");
			window.location.href="attendanceMain.jsp";
		}
		else if(flag==2)
		{
			alert("Error in Modification of Record");
		}
		else if(flag==3)
		{
			alert("Left date should be from current Salary month ");
		}
		
		else if(flag==4)
		{
			alert("Firstly approve the left Attendance of employee from Attendance");
		}
		
		
		
		
	}
	 function validation()
	{
		
		if(document.getElementById("lDate").value =="")
		{
			alert("Please Select Left Date");
			document.getElementById("lDate").focus();
			return false;
		}
		

		var joiningDate=document.getElementById('jnDate').value;
		 var leavingDate=document.getElementById('lDate').value;
		 
		 joiningDate = joiningDate.replace(/-/g,"/");
		 leavingDate = leavingDate.replace(/-/g,"/");
		    

		    var d1 = new Date(joiningDate);

		    var d2 =new  Date(leavingDate);
		    
		    if(d1.getTime() > d2.getTime()){
		    	alert("Leaving Date must be Greater than joining date");
	    		document.getElementById('lDate').value="";
	    		document.getElementById('rDate').value="";
	    		document.getElementById('raDate').value="";
	    		return false;
		    }
		    
		
		
		
		/* if(document.getElementById("desc").value =="")
		{
			alert("Please Enter Description of Transaction ");
			document.getElementById("desc").focus();
			return false;
		}
		if(document.getElementById("sdesc").value =="")
		{
			alert("Please Enter Description of Transaction ");
			document.getElementById("sdesc").focus();
			return false;
		}
		if(document.getElementById("subsystem").value =="")
		{
			alert("Please Enter Sub System Code ");
			document.getElementById("subsystem").focus();
			return false;
		}
		if(document.getElementById("acno").value =="")
		{
			alert("Please Enter Account Number ");
			document.getElementById("acno").focus();
			return false;
		} */
		} 
	 
	
	
	 
</script>

<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
</head>
<body onLoad="checkVal()" onunload="noBack()">
<center>
<br/>
<form action="<%=url%>" method="POST" ><!-- onSubmit="return validation()" -->
          
    <table border="1" id="customers">
    <tr class="alt"><th colspan="4">Add Relieving Information</th></tr>
	<tr align="left" class="alt">
	<td width="130" >Employee Code</td>
	  			<td width="180" ><%=empbn.getEMPCODE() %><input type="hidden" name="aempNo" id="aempNo" readonly="readonly" value="<%=(session.getAttribute("empno")==null?"":session.getAttribute("empno")) %>"></td>
				<td width="150" >Employee Name</td>
			<td width="150" ><%=lookuph.getLKP_Desc("SALUTE", empbn.getSALUTE())+" "+ empbn.getFNAME()+" "+empbn.getLNAME() %></td></tr>
		<tr class="alt">
		 
	
		<td >Left Date </td>
		  <td >
		  <input type="text" name="lDate" id="lDate" value="<%=rbn.getLEFT_DATE()==null?Leftdate:rbn.getLEFT_DATE()%>"  readonly="readonly">
		  <%-- <input type="text" name="lDate" id="lDate" value="<%=rbn.getLEFT_DATE()==null?Leftdate:rbn.getLEFT_DATE()%>" onchange="chk()" readonly="readonly">
		  <img src="images/cal.gif" style="vertical-align: middle; cursor: pointer;"
				onClick="javascript:NewCssCal('lDate', 'ddmmmyyyy')" /> --%>
		<div id="process" class='hide'> </div>
		  </td>
	   
			<td>Branch </td>
			<td>
			<select name="branch" id="branch" >
					<option value="0">All</option>  
			    <%
			    	ArrayList<BranchBean> result = new ArrayList<BranchBean>();
			    	BranchDAO bdao = new BranchDAO();
			    	result = bdao.getBranchDetails();
			    	for(BranchBean brBean : result){
			    		
			
			    	%>
					<option value="<%=brBean.getBRNAME()%>"><%=brBean.getBRNAME()%></option>  
				     <%
					}
				     %>
				     </select></td></tr>
				    
				     <tr class="alt">
		  <td >Join Date </td>
		  <td ><input type="text" name="jnDate" id="jnDate" value="<%=empbn.getDOJ()%>">
		 </td>
		  <td >Resignation Date </td>
		  <td ><input type="text" name="rDate" id="rDate" value="<%=rbn.getRESGN_DATE()==null?"":rbn.getRESGN_DATE()%>" onchange="chk()" readonly="readonly">
		  <img src="images/cal.gif" style="vertical-align: middle; cursor: pointer;"
				onClick="javascript:NewCssCal('rDate', 'ddmmmyyyy')" />
		  </td></tr>
				     
				     
			
		<tr class="alt">
		<td >Resignation Accepted Date </td>
		  <td ><input type="text" name="raDate" id="raDate" value="<%=rbn.getRESGN_ACCTD_DATE()==null?"":rbn.getRESGN_ACCTD_DATE()%>" onchange="chk()" readonly="readonly">
		  <img src="images/cal.gif" style="vertical-align: middle; cursor: pointer;"
				onClick="javascript:NewCssCal('raDate', 'ddmmmyyyy')" />
		  </td>
		  <td valign="top">Reason </td>
		  <td align="left" colspan="3">
		  <textarea name="aReason" id="aReason" ><%=rbn.getREASON()==null?"":rbn.getREASON() %>
		  </textarea>
		  </td>
		</tr>

			
				<tr class="alt">
			<td>Notice Period</td>
				<td>
					<select name="rPeriod" id="rPeriod">
					<option value="0" selected="selected">Select</option>
					<%
					if(rbn.getNTC_PERIOD()!=null){
						System.out.println("inside outer if");
						if(rbn.getNTC_PERIOD().equalsIgnoreCase("A"))
						{
							System.out.println("inside inner if");
					%>
							<option value="A" selected="selected"> Yes </option>
							<option value="NA"> No </option>
					<%
						}
						else
						{
					%>
					<option value="A"> Yes </option>
					<option value="NA" selected="selected"> No </option>
					<%
						}
					}
						else
						{
					%>
					<option value="A"> Yes </option>
					<option value="NA" selected="selected"> No </option>
					<%
						}
				
					%>
					</select>
				</td>
				<td> 
					Is Terminated	
					<%
						String flag= "";
					if(rbn.getTERMINATE()!=null)
						if(rbn.getTERMINATE().equalsIgnoreCase("Yes"))
							flag= "checked='checked'";
					%>
					<input type="checkbox" <%=flag %> name="term" value="Yes">
				</td>
				<td>
					<%
						flag="";
					if(rbn.getDEATH()!=null)
					if(rbn.getDEATH().equalsIgnoreCase("Yes"))
						flag= "checked='checked'";
					%>
					Is Death <input type="checkbox"  <%=flag %> name="death" value="Yes">
				</td>
			</tr>
				
		
		<tr class="alt"><td colspan="4" align="center">
		<%
			if(act.equalsIgnoreCase("ADD")){
				%>
		<input type="submit" value="Save" />
		<%
			}
			else{
				%>
		
		<input type="submit" value="Update" /> &nbsp;&nbsp;&nbsp;
		<%
			}
		%>
		<input type="reset" value="Cancel" onclick="cancel()" />

		<!-- <input type="button" value="Back To Attendance" onclick="SendRedirect()" /> --></td>
		
		
		</tr>
		
		
		</table>
	
		<input type="hidden" id='sal_date' name='sal_date' value='<%=date1%>'>
			<input type="hidden" name="flag1" id="flag1" value="<%=flag1%>">
			<input type="hidden" name="empnoo" id="empnoo" value="<%=empbn.getEMPNO()%>">
	
	
</form>

</center>
 <input type="hidden" value="<%=request.getParameter("check")==null?"":request.getParameter("check")%>" name="chek" id="chek">
 
<%--  Name : <%= ((String)request.getAttribute("flg"))%>   --%>
</body>
</html>