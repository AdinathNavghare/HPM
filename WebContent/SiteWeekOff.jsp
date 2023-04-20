<%@page import="payroll.Core.ReportDAO"%>
<%@page import="payroll.Core.Calculate"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.Format"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.awt.Desktop.Action"%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="payroll.Model.*"%>
<%@page import="payroll.DAO.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy; DTS3</title>
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="default" />
 <link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" /> 
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<style type="text/css">

.ac_results {
	padding: 0px;
	border: 1px solid #cacaca;
	background-color: #f5f6fa;
	overflow: hidden;
	width: 58% !important;
}

</style>



<script>
	jQuery(function() {
		$("#pp").autocomplete("projlist.jsp");
	});
	
	jQuery(function() {
		$("input:text").blur(function(){
			if($(this).val()=="" || $(this).val()==" ")
			{
				 
				if($(this).attr('id')!="pp")
					{					
				alert("Please enter Some value !");
				$(this.value="0.0");
					}
					
			}
			
		});
	});
	
	
</script>

<%
try
{
LookupHandler lkph= new LookupHandler();
TranHandler trnh= new TranHandler();
EmpOffHandler empOffHldr = new EmpOffHandler();
EmployeeHandler emphdlr = new EmployeeHandler();
LookupHandler lkh=new LookupHandler();
CodeMasterHandler CMH = new CodeMasterHandler();
// newly added from ak....... 
ArrayList<EmpOffBean> list= new ArrayList<EmpOffBean>();

list=empOffHldr.getprojectCode();
RoleDAO obj1=new RoleDAO();
String roleId=obj1.getrole(Integer.parseInt(session.getAttribute("UID").toString()));
session.setAttribute("roleId1",roleId);

ArrayList<TranBean> Emp_bean = new ArrayList<TranBean>();
EmpAttendanceHandler EAH=new EmpAttendanceHandler();
Emp_bean=EAH.getAssignedSitesList(Integer.parseInt(session.getAttribute("EMPNO").toString()));
System.out.println("you r rol id is thst      "+roleId);

ArrayList<CodeMasterBean> TRNCODE=new ArrayList<CodeMasterBean>();   
ArrayList<WeekOffBean> tranlist=new ArrayList<WeekOffBean>();
ArrayList<WeekOffBean>  listbyEMPNO=new  ArrayList<WeekOffBean> ();
ArrayList<Integer> projEmpNolist1 = new ArrayList<Integer>();
ArrayList<Integer> projEmpNmlist1 = new ArrayList<Integer>();
WeekOffBean weekOffBean=new WeekOffBean();
LookupHandler lookupHandler=new LookupHandler();




/* String date1=trnh.getSalaryDate();
 */
 
 String date1=request.getParameter("date1")==null?ReportDAO.getSysDate():request.getParameter("date1");

 String[] d={"","",""};
float jdays=0.0f;
String joindate,leftdate;
float days=Calculate.getDays(date1);
System.out.println(date1);
d=date1.split("-");
String date_disp = d.length!=0?d[1]+"-"+d[2]:"";


int trncd=0;
String select=new String();
String sitename1="SELECT";
String selectCode = new String();
LeaveMasterHandler obj=new LeaveMasterHandler();
String empName;
String action = request.getParameter("action")==null?"":request.getParameter("action");
//System.out.println("action is"+action);
ArrayList<Lookup> emplist=new ArrayList<Lookup>(); 
emplist=lkh.getSubLKP_DESC("ET");	// ET is Code for Employee Type
int trn=0;
int keys=0;
int empno1=0;
int empno=0;
int sizeOfSaved=0;
int year=0;
int flag=-1;
String prjCode="";
String month = "";
SiteWeekOffHandler siteWeekOffHandler=new SiteWeekOffHandler(); 

try
{  
	
	try
	{
	flag=Integer.parseInt(request.getParameter("flag")==null?"":request.getParameter("flag")); 
	}catch(Exception e)
	{	
		System.out.println("no flag value"+flag);
		if( request.getParameter("PrjCode")==null)
		{
		session.setAttribute("prjCode", "");
		}
	}
	if(action.equalsIgnoreCase("getdetails"))
	
	{	
/*  		sitename=(String)session.getAttribute("selectvalue");
 */ 	System.out.println("hiiiiiiiiiiiiiiii"); 
 	String dateForTransactionDate=request.getParameter("date1");
 	sitename1=request.getParameter("proj");
		dateForTransactionDate=ReportDAO.EOM(dateForTransactionDate);
	   month= dateForTransactionDate.substring(3,6);
	    year=Integer.parseInt(dateForTransactionDate.substring(7,11));
		 prjCode = request.getParameter("PrjCode");
/* 			select=request.getParameter("selected");
 */		 System.out.println("project code is     "+prjCode);

	    session.setAttribute("prjCode1", prjCode);
	    //projEmpNolist = empOffHldr.getEmpList(prjCode);
	    projEmpNolist1 = siteWeekOffHandler.getEmpList_Status(prjCode,date_disp);
	    //session.setAttribute("projEmpNolist", projEmpNolist);
	    int i=0;
	    
	   sizeOfSaved=siteWeekOffHandler.getWeekOffOfSite(year,month,Integer.parseInt(prjCode));
	    

	     for(Integer variable: projEmpNolist1){
	    	
	    	
	    	projEmpNmlist1.add(variable);
	    	
	    } 
	     System.out.println("projEmpNmlist"+projEmpNmlist1.size()); 
	  }	    

	
	
	 

}
catch(Exception e)
{
	e.printStackTrace();
	System.out.println("First Time Loading");
}
%>



<script type="text/javascript">



function inputLimiter(e,allow) {
	  var AllowableCharacters = '';
	  if (allow == 'Numbers'){AllowableCharacters='.1234567890';}
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


function validate()
{
	if(document.getElementById("pp").value=="" || 0>(document.getElementById("pp").value).indexOf(":"))
		{
		alert("Please Select Project !");
		return false;
		}
	else
		{return true;}
}


	function getTranDetails()
	{
	var rid= "<%=roleId%>"; 
	if(rid==1)
		{
/* 		alert(rid);
 */		var proj=document.getElementById("pp").value;
	    var date1=document.getElementById("date1").value;

		/* var e = document.getElementById("p1");
		var strUser = e.options[e.selectedIndex].value;
		 */
		var res = proj.indexOf(":"); 
		if(proj==" ")
			{			
			//alert("Please Select Project !");
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
		//alert("prjcode="+prjCode);
		
		
			if(prjCode == ""){
				
			}
		
		else{
			//window.location.href = "EmpTranNew.jsp?action=getdetails&key="+ type + "&PrjCode="+prjCode+"&selected=" + selected;
			//alert("into else");
			
			proj=proj.replace(/ & /g," and ");
			window.location.href = "SiteWeekOff.jsp?action=getdetails&PrjCode="+prjCode+"&proj="+proj+"&date1="+date1;
		      }
				  }
		   }
		   }
	// this is for site admin
	else
		{
		var e = document.getElementById("p1");
	    var date2=document.getElementById("date2").value;

		var prjCode = e.options[e.selectedIndex].value;
		var proj = e.options[e.selectedIndex].text;
         if(prjCode == " "||proj == " "||proj == "SELECT")
         {
			    alert("Select proper site name.");
		    }
	
	       else{
	    	    // proj=proj.replace(/ & /g," and ");
	    	    //alert("this is"+prjCode+proj+date2);
	    	    window.location.href = "SiteWeekOff.jsp?action=getdetails&PrjCode="+prjCode+"&proj="+proj+"&date1="+date2;
	    	 
	    //alert(proj);
      }
		}
	}
	
	
	
/* 	function update(key) {
		window.showModalDialog("UpdateCodeMast.jsp?key=" + key,null,"dialogWidth:700px; dialogHeight:230px; scroll:off; status:off;dialogLeft:400px;dialogTop:300px; ");
		getTranCodes();
	} */

	function editTranAmount(str) {
		var flag = confirm("Do you want to Edit this Amount");
		if (flag) {

			window.location.href = "SiteWeekOff.jsp?action=edit&key1=" + str;
		}

	}

	function checkFlag() {
		var f = parseInt(document.getElementById("flag1").value);

		if (f == 1) {
			alert("Tranaction Saved Successfully..!\n\n\t Please Re-Calculate The salary of Updated Employees..! ");

		}
		if (f == 2) {
			alert("Employee Already Exist");
		}
		if (f == 3) {
			alert("Record updated Successfully..!\n\n\t Please Re-Calculate The salary of Updated Employees..! ");
		}
		
	}

	

	
	

	function showdiv() {
		var e = document.getElementById("addN");
		if (e.style.display == 'none') {
			e.style.display = 'block';
		} else if (e.style.display == 'block') {
			e.style.display = 'none';

		}

	}


	function askForSave () {
		var conf=confirm("Are you sure to save weekoffs?");
		if(conf){
			var check = prompt("Are you really sure to save weekoffs? Then type Yes ");	
			if(check == "yes" || check == "Yes" || check == "YES"){
				// do not enter to go forward
			} else {
				alert("Sorry ! Wrong Input ");
				return false;
			}
			
		}
	}
	
	
	
</script>
<%
	 String pageName = "SiteWeekOff.jsp";
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
<body onLoad="checkFlag()" style="overflow: hidden;">
	<%@include file="mainHeader.jsp"%>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp"%>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer" style="overflow-y: scroll; max-height: 85%;">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Employee Weekoff Details</h1>
			</div>
			<!-- end page-heading -->

			<table border="0" width="100%" cellpadding="0" cellspacing="0"
				id="content-table">
				<tr>
					<th rowspan="3" class="sized"><img
						src="images/shared/side_shadowleft.jpg" size="7" height="300"
						alt="" /></th>
					<th class="topleft"></th>
					<td id="tbl-border-top">&nbsp;</td>
					<th class="topright"></th>
					<th rowspan="3" class="sized"><img
						src="images/shared/side_shadowright.jpg" size="7" height="300"
						alt="" /></th>
				</tr>
				<tr>
					<td id="tbl-border-left"></td>
					<td>
						<!--  start content-table-inner ...................................................................... START -->
						<div id="content-table-inner">

							<!--  start table-content  -->
							<div id="table-content" style="height: 540px;">
								<center>
                                     
									<form action="SiteWeekOffServlet?action=saveWeekOffValues" method="post" onsubmit="return askForSave();">
                                       <% if(roleId.equals("1")){%>
										<table id="customers" style="width: 100% !important" align="center">
											<tr>
												<th>Employee Weekoff Details</th>
											</tr>

											<tr>
												<td  align="center">
												<table style="width: 100% !important" >
												<tr>
												
												
												<td style="width: 40% ">For Month: <input name="date1" id="date1"
													 type="text" value="<%=date1%>" readonly="readonly" 
													  onFocus="if(value=='dd-mmm-yyyy') {value=''}"
							                          onBlur="if(value=='') {value='dd-mmm-yyyy';}">&nbsp;<img
						                            	src="images/cal.gif" align="absmiddle"
							                            style="vertical-align: middle; cursor: pointer;"
							                            onClick="javascript:NewCssCal('date1', 'ddmmmyyyy')" />
							                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							                            <span id="sp1"><font size="+1"><%=date_disp%></font></span>
							                            </td>
												
												
												<td>Project :
<input type="text" id="pp" name="pp" style="width: 80%;" autofocus onClick="this.select();" value="<%=request.getParameter("proj")==null?"":request.getParameter("proj").replaceAll(" and ", " & ")%>" title="Enter a character to view the available project list (E.g - %) ">
												
                                          
												 <input type="Button"value="Submit"onclick="getTranDetails()"/>
												
											
															<font size="+1">
															
															
															</font>
															</tr> 

													</table>
													</td>
											</tr>

											<tr><td>
					<div  class="imptable" >
													<table >
														<tr  bgcolor="#2f747e"  >														
															<td style="width: 50px; text-align:center; color: white;"><font size="2"><b>Sr. No</b></font></td>
															<td style="width: 100px; text-align:center;  color: white;" ><font size="2"><b>Emp No</b></font></td>
															<td style="width: 800px; text-align:center;  color: white;"><font size="2"><b>Emp Name</b></font></td>
															<td style="width: 150px; text-align:center;  color: white;"><font size="2"><b>Day</b></font></td>
															
															
															
															
															
															
															
														</tr>
													</table>

											</div>
											</td>
											</tr>

											<tr>
												<td>
													<div   class="imptable" style="height: 400px;overflow-y:auto;width:102% ">
														


															<%
															
																int index=0;
																int srno=1;
															System.out.println("into display");
															System.out.println("Emp num list="+projEmpNmlist1.size());
															System.out.println("sizeOfSaved="+sizeOfSaved);
															
																  if(projEmpNolist1.size()<=sizeOfSaved){
																for (Integer variable: projEmpNmlist1) {
																	
																	EmployeeBean ebean=new EmployeeBean();
																	ebean=emphdlr.getEmployeeInformation(Integer.toString(variable));
																	empName = obj.getempName(variable);
																	weekOffBean=siteWeekOffHandler.getWeekOffOfEmployee(variable, year, month,Integer.parseInt(prjCode));		
																	
																	//trncd=tbean.getTRNCD();
															%>
																<table style="margin-left: 5px ;">
															<tr>
																<td style="width: 50px;text-align: center; "><%=srno%></td>
																<td style="width: 100px;text-align: center;  "><%=variable%></td>
																<td style="width: 800px;text-align: center; "><%=empName%></td>
																 <td style="width: 150px; text-align: center;  "><%=lookupHandler.getLKP_Desc("DAY",weekOffBean.getDay()==0?7:weekOffBean.getDay())%></td> 
							<!-- from adv_pay_requested advancehandler --> 			
							<% String dateForTransactionDate=request.getParameter("date1");
		    		//System.out.println("dateForTransactionDate :"+dateForTransactionDate);
		   		 	dateForTransactionDate=ReportDAO.EOM(dateForTransactionDate);  %>						
                												<%-- <td style="width: 67px;"><input type="text" size="7" id="advValue<%=index %>" name="advValue" value="<%=(AH.getEmpAdvance(Integer.toString(tbean.getEMPNO()),date1))%>" disabled="disabled" onClick="this.select();" onkeypress="return inputLimiter(event,'Numbers')"  onkeyup="calTotal('advValue')" onmouseup="calTotal('advValue')" onblur="calTotal('advValue')"></td>  
                												
                												  <td style="width: 67px;"><input type="text" size="7" id="advValue<%=index %>" readonly="readonly" name="advValue" value="<%=(trnh.getTranByEmpno1(weekOffBean.getEmpno(), 225,dateForTransactionDate)).getINP_AMT()%>" disabled="disabled" onClick="this.select();" onkeypress="return inputLimiter(event,'Numbers')"  onkeyup="calTotal('advValue')" onmouseup="calTotal('advValue')" onblur="calTotal('advValue')"></td>
                												--%>
															</tr> 
															<%
															index++;
															srno++;
																
															  
																}
																 }
																 else{
																	 for (Integer variable: projEmpNmlist1) {
																			int i=0;
																			EmployeeBean ebean=new EmployeeBean();
																			ebean=emphdlr.getEmployeeInformation(Integer.toString(variable));
																			empName = obj.getempName(variable);
																			
																			
																			//trncd=tbean.getTRNCD();
																	%>
																	<table style="margin-left: -30px ;">
																	<tr>
																		<td style="width: 50px; text-align: center;"><%=srno%></td>
																		<td style="width: 100px; text-align: center;" ><input type="text" id="empno<%=i%>" style="text-align: center;" name="empno" readonly="readonly" value="<%=variable%>"></td>
																		<td style="width: 800px; text-align: center;" ><%=empName%></td>
																		<td style="width: 150px; text-align: center;">
														<select class="form-control"  name="day" id="day<%=i%>">
									<option value="1" selected >Sunday</option>
									<option value="2"> Monday </option>
									<option value="3"> Tuesday</option>
									<option value="4"> Wedensday </option>
									<option value="5"> Thursday </option>
									<option value="6"> Friday </option>
									<option value="7"> Saturday</option>
									<!-- <option value='7'> Sunday</option>	 -->
									</select>
									</td>
									<!-- from adv_pay_requested advancehandler --> 			
															
		                											<%-- 	<td style="width: 67px;"><input type="text" size="7" id="advValue<%=index %>" name="advValue" value="<%=(AH.getEmpAdvance(Integer.toString(tbean.getEMPNO()),date1))%>" disabled="disabled" onClick="this.select();" onkeypress="return inputLimiter(event,'Numbers')"  onkeyup="calTotal('advValue')" onmouseup="calTotal('advValue')" onblur="calTotal('advValue')"></td>  
		                												
		                												  <td style="width: 67px;"><input type="text" size="7" id="advValue<%=index %>" readonly="readonly" name="advValue" value="<%=(trnh.getTranByEmpno1(weekOffBean.getEmpno(), 225,dateForTransactionDate)).getINP_AMT()%>" disabled="disabled" onClick="this.select();" onkeypress="return inputLimiter(event,'Numbers')"  onkeyup="calTotal('advValue')" onmouseup="calTotal('advValue')" onblur="calTotal('advValue')"></td>
		                												 --%>
																	</tr>
																	<%
																	i++;
																	index++;
																	srno++;
																		
																	  
																		}		 
															%>
														
															<% }%>
<!-- 															</table>
 -->														</table>

													

												</td>


											</tr>
											
											
											<tr>
												<td>
												<div  class="imptable" >
												<table>
												
												 <%
												 if(projEmpNmlist1.size()>0)
												 {
												 %>
											<!-- 	 <tr>
												 					
										
												
												</tr> -->
												<% }
												 else
												 {
													
													 if(action.equalsIgnoreCase("getdetails"))
													 {
														 %>
<!-- 														<center>
															<font color="red" size="4"> No records found !</font>
															</center>
 -->															
															 <%	
													 }
												 }
												 %>
												 </table>
												 </div>
												 </td>
											</tr>
											
											<tr style="height: 30px;">
												 <td align="center" bgcolor="#929292">
												 <%  if((projEmpNolist1.size()<=sizeOfSaved)){
													 %>
													 <h4 style="color: red;"><u>ALREADY SAVED</u> </h4>
													 <% }
												 else {
												 if(projEmpNmlist1.size()>0)
												 {
													 System.out.println("action on jsp : "+action);	 
										
												 
												 %>
												 <input type="submit" id="tranSave"
													value="Save"  height="150" width="150" onclick="return validate()" />
												<input type="reset" value="Cancel"  /></td></tr>
												<% }
												 }
												 %>
											
											
								 </td>
								 </tr>
											
										</table>
<!-- 										</div>
 -->

									
								
								<%} 
								               
					 
					else{ 	%>
										
										
                 									
												 <table id="customers" style="width: 100% !important" align="center">
											<tr>
												<th>Employee Weekoff Details</th>
											</tr>

											<tr>
												<td  align="center">
												<table style="width: 100% !important" >
												<tr>
												
												
												<td style="width: 40% ">For Month: <input name="date2" id="date2"
													 type="text" value="<%=date1%>" readonly="readonly" 
													  onFocus="if(value=='dd-mmm-yyyy') {value=''}"
							                          onBlur="if(value=='') {value='dd-mmm-yyyy';}">&nbsp;<img
						                            	src="images/cal.gif" align="absmiddle"
							                            style="vertical-align: middle; cursor: pointer;"
							                            onClick="javascript:NewCssCal('date1', 'ddmmmyyyy')" />
							                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							                            <span id="sp1"><font size="+1"><%=date_disp%></font></span>
							                            </td>
												
												
												<td align="center" style="width:280px">Project :
<!-- 								                    <input type="text" name="" style="width: 80%;" />
 -->								                    <select name="p1" id="p1" style="width:80%">
                                                        <option value="0" style="width:280px" disable selected> <%=sitename1%> </option>
                                                             	<%
	                                                         		for(TranBean tb:Emp_bean)

 							                                                            {
    					
    					                                                      		boolean check=false;
 					                                                         			for(EmpOffBean lkb :list)
      					                                                	      {
      						                                            	if(lkb.getPrj_srno()==Integer.parseInt(	tb.getSite_id()))
      						                                            	{
      						                                            		
      						                                          		check=true;
      						                                       		System.out.println("sisteghhf"+tb.getSite_id());
      						                                    	              		%>
                                                             
                                             
                                              <option style="width:280px" value="<%=tb.getSite_id()%>" title="<%=lkb.getSite_name()%>"><%=lkb.getPrj_name()%></option>
                                                                                      
                                                        
                                                              
												<%}
      						                                            	
      					                                                	    }
 					                                                         		      	}  %>
 	      					                                                        		 </select>
 	      					                                                        		 
												 <input type="Button"value="Submit"onclick="getTranDetails()"/>
                                                             
															<font size="+1">
															
															
															</font>
															</tr> 

													</table>
													</td>
											</tr>

											<tr><td>
											<div  class="imptable" >
													<table >
														<tr  bgcolor="#2f747e"  >														
															<td style="width: 50px; text-align:center; color: white;"><font size="2"><b>Sr. No</b></font></td>
															<td style="width: 100px; text-align:center;  color: white;" ><font size="2"><b>Emp No</b></font></td>
															<td style="width: 800px; text-align:center;  color: white;"><font size="2"><b>Emp Name</b></font></td>
															<td style="width: 150px; text-align:center;  color: white;"><font size="2"><b>Day</b></font></td>
															
															
															
														</tr>
													</table>

											</div>
											</td>
											</tr>

											<tr>
												<td>
													<div   class="imptable" style="height: 400px;overflow-y:auto;width:102% ">
														


															<%
															
																int index=0;
																int srno=1;
															System.out.println("into display");
															System.out.println("Emp num list="+projEmpNmlist1.size());
															System.out.println("sizeOfSaved="+sizeOfSaved);
															
																  if(projEmpNolist1.size()<=sizeOfSaved){
																for (Integer variable: projEmpNmlist1) {
																	
																	EmployeeBean ebean=new EmployeeBean();
																	ebean=emphdlr.getEmployeeInformation(Integer.toString(variable));
																	empName = obj.getempName(variable);
																	weekOffBean=siteWeekOffHandler.getWeekOffOfEmployee(variable, year, month,Integer.parseInt(prjCode));		
																	
																	//trncd=tbean.getTRNCD();
															%>
																<table style="margin-left: 5px ;">
															<tr>
																<td style="width: 50px;text-align: center; "><%=srno%></td>
																<td style="width: 100px;text-align: center;  "><%=variable%></td>
																<td style="width: 800px;text-align: center; "><%=empName%></td>
																 <td style="width: 150px; text-align: center;  "><%=lookupHandler.getLKP_Desc("DAY",weekOffBean.getDay()==0?7:weekOffBean.getDay())%></td> 
							<!-- from adv_pay_requested advancehandler --> 			
							<% String dateForTransactionDate=request.getParameter("date1");
		    		//System.out.println("dateForTransactionDate :"+dateForTransactionDate);
		   		 	dateForTransactionDate=ReportDAO.EOM(dateForTransactionDate);  %>						
                												<%-- <td style="width: 67px;"><input type="text" size="7" id="advValue<%=index %>" name="advValue" value="<%=(AH.getEmpAdvance(Integer.toString(tbean.getEMPNO()),date1))%>" disabled="disabled" onClick="this.select();" onkeypress="return inputLimiter(event,'Numbers')"  onkeyup="calTotal('advValue')" onmouseup="calTotal('advValue')" onblur="calTotal('advValue')"></td>  
                												
                												  <td style="width: 67px;"><input type="text" size="7" id="advValue<%=index %>" readonly="readonly" name="advValue" value="<%=(trnh.getTranByEmpno1(weekOffBean.getEmpno(), 225,dateForTransactionDate)).getINP_AMT()%>" disabled="disabled" onClick="this.select();" onkeypress="return inputLimiter(event,'Numbers')"  onkeyup="calTotal('advValue')" onmouseup="calTotal('advValue')" onblur="calTotal('advValue')"></td>
                												--%>
															</tr> 
															<%
															index++;
															srno++;
																
															  
																}
																 }
																 else{
																	 for (Integer variable: projEmpNmlist1) {
																			int i=0;
																			EmployeeBean ebean=new EmployeeBean();
																			ebean=emphdlr.getEmployeeInformation(Integer.toString(variable));
																			empName = obj.getempName(variable);
																			
																			
																			//trncd=tbean.getTRNCD();
																	%>
																	<table style="margin-left: -30px ;">
																	<tr>
																		<td style="width: 50px; text-align: center;"><%=srno%></td>
																		<td style="width: 100px; text-align: center;" ><input type="text" id="empno1<%=i%>" style="text-align: center;" name="empno1" readonly="readonly" value="<%=variable%>"></td>
																		<td style="width: 800px; text-align: center;" ><%=empName%></td>
																		<td style="width: 150px; text-align: center;">
														<select class="form-control"  name="day1" id="day1<%=i%>">
									<option value="1" selected >Sunday</option>
									<option value="2"> Monday </option>
									<option value="3"> Tuesday</option>
									<option value="4"> Wedensday </option>
									<option value="5"> Thursday </option>
									<option value="6"> Friday </option>
									<option value="7"> Saturday</option>
									<!-- <option value='7'> Sunday</option>	 -->
									</select>
									</td>
									<!-- from adv_pay_requested advancehandler --> 			
															
		                												<%-- <td style="width: 67px;"><input type="text" size="7" id="advValue<%=index %>" name="advValue" value="<%=(AH.getEmpAdvance(Integer.toString(tbean.getEMPNO()),date1))%>" disabled="disabled" onClick="this.select();" onkeypress="return inputLimiter(event,'Numbers')"  onkeyup="calTotal('advValue')" onmouseup="calTotal('advValue')" onblur="calTotal('advValue')"></td>  
		                												
		                												  <td style="width: 67px;"><input type="text" size="7" id="advValue<%=index %>" readonly="readonly" name="advValue" value="<%=(trnh.getTranByEmpno1(weekOffBean.getEmpno(), 225,dateForTransactionDate)).getINP_AMT()%>" disabled="disabled" onClick="this.select();" onkeypress="return inputLimiter(event,'Numbers')"  onkeyup="calTotal('advValue')" onmouseup="calTotal('advValue')" onblur="calTotal('advValue')"></td>
		                												--%>
																	</tr> 
																	<%
																	i++;
																	index++;
																	srno++;
																		
																	  
																		}		 
															%>
														
															<% }%>
															<!-- </table> -->
														</table>

													</div>

												</td>


											</tr>
											
											
											<tr>
												<td>
												<div  class="imptable" >
												<table>
												
												 <%
												 if(projEmpNmlist1.size()>0)
												 {
												 %>
												 <tr>
												 					
										
												
												</tr>
												<% }
												 else
												 {
													
													 if(action.equalsIgnoreCase("getdetails"))
													 {
														 %>
														<!-- <center>
															<font color="red" size="4"> No records found !</font>
															</center> -->
															
															 <%	
													 }
												 }
												 %>
												 </table>
												 </div>
												 </td>
											</tr>
											
											<tr style="height: 30px;">
												 <td align="center" bgcolor="#929292">
												 <%  if((projEmpNolist1.size()<=sizeOfSaved)){
													 %>
													 <h4 style="color: red;"><u>ALREADY SAVED</u> </h4>
													 <% }
												 else {
												 if(projEmpNmlist1.size()>0)
												 {
													 System.out.println("action on jsp : "+action);	 
										
												 
												 %>
												 <input type="submit" id="tranSave"
													value="Save"  height="150" width="150" onclick="return validate()" />
												<input type="reset" value="Cancel"  /></td></tr>
												<% }
												 }
												 %>
											
											
								 </td>
								 </tr>
											
										</table>

								
<%
                                     }%>
                                     
                               </form>
									<br>

									 
									<input type="hidden" name="flag1" id="flag1" value="<%=flag%>">
 
              </center>
 <%
     }
catch(Exception e)
{
	e.printStackTrace();
	%>
	
	<script type="text/javascript">
	
	window.location.href="login.jsp?action=0";
	</script>
	
	<%
}
%>
							</div>
							<!--  end table-content  -->

							<div class="clear"></div>

						</div> <!--  end content-table-inner ............................................END  -->
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
