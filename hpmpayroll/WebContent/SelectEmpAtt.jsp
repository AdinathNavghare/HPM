<%@page import="payroll.DAO.EmpAttendanceHandler"%>
<%@page import="payroll.Model.LoanAppBean"%>
<%@page import="payroll.Model.LoanBean"%>
<%@page import="payroll.DAO.LoanAppHandler"%>
<%@page import="payroll.DAO.LoanHandler"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="payroll.DAO.CodeMasterHandler"%>
<%@page import="payroll.Core.ReportDAO"%>
<%@page import="payroll.Model.TranBean"%>
<%@page import="payroll.DAO.RoleDAO"%>
<%@page import="java.util.Calendar"%>
<%@page import="payroll.DAO.EmpOffHandler"%>
<%@page import="payroll.Model.EmpOffBean"%>
<%@page import="payroll.Model.ProjectBean"%>
<%@page import="payroll.DAO.ProjectListDAO"%>
<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.DAO.EmployeeHandler"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="payroll.Model.Lookup"%>
<%@page import="payroll.DAO.LookupHandler"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy Select Employee Attendance </title>

<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css" media="screen" title="default" />
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script type="text/javascript" src="js/Loan_Cal_Outer.js"></script>

<script type="text/javascript">
 
</script>

<script>
$(function()
{
	jQuery(function() {
		$("#imag").click(function(){
			document.getElementById("calBorder").style.display='block';
		});
		
		$("#imag2").click(function(){
			document.getElementById("calBorder").style.display='block';
		});
	});
});
</script>

<script type="text/javascript">

/* function  noBack()
{
	window.history.forward();
}

setTimeout("noBack()", 0);
window.onunload = function() 
{
    null;
}; */

jQuery(function() {
	
	$("#loanbtn").click(function(){
	});

}); 

function getTranDetails() {
	
	var proj=document.getElementById("pp").value;
	var res = proj.indexOf(":"); 
	
	if(proj=="")
	{			
		alert("Please Select Project !");
	}
	else
	{
		if(res<0)
		{
			//alert("Please Select Project !");
			document.getElementById("pp").value="";
			document.getElementById("pp").focus();
		}
		else
		{
			var p=proj.split(":");
			var prjCode = p[3];
			
			if(prjCode == ""){
					
			}
			else{
				proj=proj.replace(/ & /g," and ");
				window.location.href = "SelectEmpAtt.jsp?action=getdetails&prj="+prjCode;

			}
		}
	}
			
}

function getTranDetailssites() {
	
	var proj=document.getElementById("ppsite").value;
	var res = proj.indexOf(":"); 
	
	if(proj=="")
	{			
		//alert("Please Select Project !");
	}
	else
	{
		if(res<0)
		{
			//alert("Please Select Project !");
			document.getElementById("ppsite").value="";
			document.getElementById("ppsite").focus();
		}
		else
		{
			var p=proj.split(":");
			var prjCode = p[3];
			
			if(prjCode == ""){
					
			}
			else
			{		
				proj=proj.replace(/ & /g," and ");
				window.location.href = "SelectEmpAtt.jsp.jsp?action=getdetails&prj="+prjCode;
			}
		}
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




function redirect()
{
	
     var proj = document.getElementById("project").value;
     var empno = document.getElementById("emplist").value;
     var date = document.getElementById("startDate").value;
    
			
     
     
     if(proj=="")
    	 {
    	 alert("please select the project");
    	 return false;
    	 }
     
     
 
     if(empno=="0")
	 {
	 alert("please select the employee");
	 document.getElementById("emplist").focus();
	 return false;
	 }
     
     
     if(date=="")
	 {
	 alert("please select the date");
	 document.getElementById("startDate").focus();
	 return false;
	 }
     
     
     var p=proj.split("::");	
     var prjCode = p[1];
			
			
			if(prjCode == ""){
			}
			else{	
				
				
				//proj=proj.replace(/ & /g," and ");
				window.location.href = "EmployeeAttendance.jsp?site_id="+prjCode+"&date="+date+"&empno="+empno;
				
				
			}
		
	 
}




function validation() {

	

}


</script>

<script>
	jQuery(function() {
		$("#pp").autocomplete("projlist.jsp");
	});
	
	jQuery(function() {
		$("#ppsite").autocomplete("projlistsite.jsp");
	});
	
	

	
	<%
	LoanAppHandler loanAppHandler=new LoanAppHandler();
	String emp_no=request.getParameter("emp_info")==null?"":request.getParameter("emp_info");
	String proj=request.getParameter("prj")==null?"":request.getParameter("prj");
	EmpOffBean eoffbn = new EmpOffBean();

	
	
	
	
	
	RoleDAO obj1=new RoleDAO();
	String roleId=obj1.getrole(Integer.parseInt(session.getAttribute("UID").toString()));
	LookupHandler lookuph= new LookupHandler();

	ArrayList<LoanAppBean> LoanList = new ArrayList<LoanAppBean>();
	LoanAppHandler loanHandler = new LoanAppHandler();
	SimpleDateFormat dateFormat;

	ArrayList<TranBean> projEmpNolist = new ArrayList<TranBean>();
	ArrayList<TranBean> projEmpNmlist = new ArrayList<TranBean>();
	

	EmployeeHandler emph = new EmployeeHandler();
	EmployeeBean ebean = new EmployeeBean();
	EmployeeBean ebean1 = new EmployeeBean();
	String [] payable=null;
	ArrayList<LoanAppBean> list= new ArrayList<LoanAppBean>();
	
	if(!emp_no.equalsIgnoreCase(""))
	{
		
		 
	}
	
	int eno = (Integer)session.getAttribute("EMPNO");
	EmpOffHandler eoffhdlr = new EmpOffHandler();
	eoffbn = eoffhdlr.getEmpOfficAddInfo(Integer.toString(eno));

	int site_id=(eoffbn.getPrj_srno());
	String employeeNumberString=session.getAttribute("EMPNO").toString();
	int empNo = Integer.parseInt(session.getAttribute("EMPNO").toString());
	ebean = emph.getEmployeeInformation(session.getAttribute("EMPNO").toString());
	
	if(proj=="")
	{
	 proj=Integer.toString(eoffbn.getPrj_srno());
	 //System.out.println("project"+proj);
	}


	String action = request.getParameter("action")==null?"":request.getParameter("action");
	

	int flag=-1;
	int prjCode = 0;
	 
		try
		{
		flag=Integer.parseInt(request.getParameter("flag")==null?"":request.getParameter("flag")); 
		}catch(Exception e)
		{
	//		System.out.println("no flag value"+flag);
			if( request.getParameter("prj")==null)
			{
			session.setAttribute("prjCode", "");
			}
		}
		if(action.equalsIgnoreCase("getdetails"))
		{
			
			prjCode = Integer.parseInt(request.getParameter("prj"));
			// System.out.println("thr firssssssssssssssssst prjcode"+prjCode);
		    session.setAttribute("prjCode", prjCode);		  
		    session.setAttribute("projEmpNolist", projEmpNolist);
		    int i=0;		    
		   
		    ArrayList<TranBean> arl=new ArrayList<TranBean>();
		    for(TranBean tbn : projEmpNolist){
		    	TranBean trbn = new TranBean();	    	
		    	projEmpNmlist.add(trbn);
	
		    } session.setAttribute("prjCode", prjCode);
		//    System.out.println("the employee are for selected project code as:"+prjCode+ " "+projEmpNmlist.size());
		}


	%>
	
	function checkFlag() {
		
		 var proj = document.getElementById("project").value;
		    
	       var res = proj.indexOf(":"); 
			
			var p=proj.split("::");
			
			var prjCode = p[1];
		
		var f = parseInt(document.getElementById("flag").value);

	
		if (f == 1) {
			alert("Attendance saved Successfully");
		
		}
		if(f==2){
			alert("Attendance is not added.Please apply again");
			
			
		}
	
	}
	
</script>

<style type="text/css">

.ac_results {
	padding: 0px;
	border: 1px solid #cacaca;
	background-color: #f5f6fa;
	overflow: hidden;
	width: 35% !important;
}

</style>

</head>
<body onload="checkFlag()" >

<%@include file="mainHeader.jsp"%>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp"%>
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" >
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Employee Attendance</h1>
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
		<div id="table-content" align="center" style=" max-height:520px; ">
					
	
  		<table style="width:60%"  id="customers">
  			<tr style="width:100%"><th colspan="2"><font color="white" size="3">Select Project</font></th></tr> 
   			<tr>
			<% if(request.getParameter("prj")==null)
				{
			%>
				<td>Project Name : 
					<input type="text" id="project" name="project" readonly="readonly" style="width: 80%; border: none;"  value="<%=eoffbn.getPrj_name()%>::<%=eoffbn.getPrj_srno() %>">
			<%
				}else
				{
					ProjectListDAO  pl=new ProjectListDAO();
					ProjectBean pb=new ProjectBean();
					pb=pl.getProjectInfo(Integer.parseInt(request.getParameter("prj")));
			%>
				<td>Current Project  : 
					<input  class="form-control"   type="text" id="project" name="project" readonly="readonly" style="width: 80%; border: none;" value="<%= pb.getSite_Name()%>::<%=pb.getSite_ID() %>">

			<%
				}
	  		 %></td>
	   		<%
	   				 dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
					 Date date = new Date();
					 String currentdate = dateFormat.format(date);  
			%>
	    	</tr>
			<% if(roleId.equals("1"))
				{
			%>
			<tr>
				<td colspan="3">Project :
					<input class="form-control"   type="text" id="pp" name="pp" style="width: 80%;"  onClick="this.select();" value="<%=request.getParameter("proj")==null?"":request.getParameter("proj").replaceAll(" and ", " & ")%>" title="Enter a character to view the available project list (E.g - %) ">
					<input type="Button" value="Submit" title="To change Employees Of particular Project Click here" onclick="getTranDetails()" />
				</td>		
		    </tr>
			<% }
				else{
			%>
			<tr>
				<td colspan="3">Project :
					<input   class="form-control" type="text" id="ppsite" name="ppsite" style="width: 80%;"  onClick="this.select();" value="<%=request.getParameter("proj")==null?"":request.getParameter("proj").replaceAll(" and ", " & ")%>" title="Enter a character to view the available project list (E.g - %) ">
					 <input  type="Button" value="Submit" title="To change Employees Of particular Project Click here" onclick="getTranDetailssites()" />
				</td>	
			 </tr>
						
			<%} %>
  		</table>
		
		<br/>

		<div align="center">
		

		<div id="loan" >
		<table >
			<tr valign="top">
				<td align="center">
					<table id="customers">						
							<tr class="alt">
									<th colspan="6">Select Employee</th>
							</tr>
							<tr>
									<td>Select Employee&nbsp;<font color="red"><b>*</b></font></td>
									<td >
										<input type="hidden" name="proj" id="proj" value="<%=request.getParameter("prj")==null? Integer.toString(eoffbn.getPrj_srno()):request.getParameter("prj")%>">
										<select class="form-control"  style="width: 300px" name="emplist" id="emplist" >
												<option value="0" selected >Select</option>
														<%									
												    	 	list=loanAppHandler.getEmpList(request.getParameter("prj")==null? Integer.toString(eoffbn.getPrj_srno()):request.getParameter("prj"));
														%>
												    	
												    	<%
												    		for(LoanAppBean lkb :list)
				 				 							{  
												    	%> 						 															 								 
				 				     		
				 				      			<option value="<%=lkb.getEMPNO()%>"  title="<%=lkb.getEMPNO()%>"  ><%=lkb.getName()%>-<%=lkb.getEmpcode() %></option>
				 				     					<%																				
				 				     					//System.out.println("empname====="+lkb.getEMPNO()+lkb.getEmpcode() + lkb.getName() );													
																												
				 				 						}																
														    						 										
														%>
												</select>
									</td>																
							</tr>
							<tr>
									<td>Start Date&nbsp; <font color="red"><b>*</b></font></td>
									<td>
										<input size="14" name="startDate" class="form-control" id="startDate" type="text"  readonly="readonly">&nbsp;&nbsp;
										<img src="images/cal.gif"  id="imag2" style="vertical-align: middle; cursor: pointer;" 
											onClick="javascript:NewCssCal('startDate', 'ddmmmyyyy')"	 /> 
									</td>		
							</tr>												
							<tr align="center" valign="middle">
									<td colspan="6">									
										<input type="button" id="takeAttBtn" name="takeAttBtn" value="Take Attendance" onClick="javascript:redirect()"/> 										
									</td>
						  </tr>													
				</table>											
				<input type="hidden" name="flag" id="flag" value="<%=flag%>">						
		</td>
	</tr>
	<tr>
			<td align="center">
				<div class="loan_pmt" id="pmt"></div>
				<div class="loan_out" id="det"></div>
			</td>
	</tr>

			</table>
</div>
</div>

  
	
 
</div>	<!--  end table-content  -->
	
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
<div id="process"  style="z-index: 1000;background-color:white;height: 100%;width: 100%;top: 0px;left: 0px;position: fixed;opacity:0.8;" hidden=true; >
				<div align="center" style="padding-top: 20%;">
				
				<h1>Processing For Loan</h1>
				
				<img alt="" src="images/process.gif">
				</div>
</div>
<!--  end content -->
<div class="clear">&nbsp;</div>
</div>
<!--  end content-outer........................................................END -->

<div class="clear">&nbsp;</div>
    

</body>
</html>