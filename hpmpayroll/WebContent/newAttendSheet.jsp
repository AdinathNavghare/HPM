<%@page import="javax.management.relation.Role"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.jfree.ui.about.ProjectInfo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>

<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page import="payroll.Core.ReportDAO"%>
<%@page import="payroll.Core.Calculate"%>
<%@page import="java.awt.Desktop.Action"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="payroll.Model.*"%>
<%@page import="payroll.DAO.*"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy; DTS3</title>
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />
<link rel="stylesheet" href="css/datePicker.css" type="text/css"
	media="screen" title="default" />	
<script type="text/javascript" src="js/jquery.datePicker.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" >
function fn(id1,id2)
{

document.getElementById(id1).style.display='none';
document.getElementById(id2).style.display='block';

}

function approve(empNo,status,date) {
	
	
	var lastdate = document.getElementById("date").value;
	var j = lastdate.substring(0,2);
	
	for(var i=1; i<parseInt(j); i++ ){
		
		if(document.getElementById("_"+i).value=="")
			{
			alert("Please first fill all blank Attendance before " + lastdate+ " in Attendance sheet");
			return false;
			}
	}
	//alert(status+empNo+date);
	var r =confirm("Are you sure to transfer/left/SingleApprove from this date "+lastdate+" ?" );

	if(r == true){
		//alert(lastdate);
		if(status=="left")
			{
		window.location.href="RelievingNew.jsp?EMPNO="+empNo+"&flag=1&&status="+status+"&date1="+lastdate;
			}
		else
			{
			if(status=="transfer"||status=="Transfer")
				{
			window.location.href="EmpAttendServlet?action=empApproved&empNo="+empNo+"&status="+status+"&date1="+lastdate,"_self";
				}
			else
				{
				window.location.href="EmpAttendServlet?action=empApproved&empNo="+empNo+"&status="+status+"&date1="+lastdate,"_self";
				}
			}
		// window.location.href="RelievingNew.jsp?EMPNO="+empNo+"&flag=1&Leftdate="+date1+"&status="+status+"&date1="+lastdate+" ";
	 //window.location.href="EmpAttendServlet?action=empApproved&empNo="+empNo+"&status="+status+"&date1="+lastdate,"_self";
	/* window.location.href="EmpAttendServlet?action=empApproved&empNo="+empNo+"&status="+status+"&date1="+lastdate,"_self";*/
	
	} 
	
	}
	
	


function reject(empNo,status,date) {

	var r =confirm("Are you sure to Reject?");
	if(r == true){
	 window.location.href="EmpAttendServlet?action=empRejection&empNo="+empNo+"&status="+status+"&date1="+date,"_self";
	} 
}

function noadmin(){
	alert("You dont have the authentication to approve..!!");
}
function noadmin1(){
	alert("You dont have the authentication to reject..!!");
}
</script>

<%
RoleDAO obj1=new RoleDAO();
String roleId=obj1.getrole(Integer.parseInt(session.getAttribute("UID").toString()));

String date=request.getParameter("date");
float days=Calculate.getDays(date); 
String status=request.getParameter("status");
String event=request.getParameter("event");
int empNo=Integer.parseInt(request.getParameter("empno"));
/* int site_id=Integer.parseInt(request.getParameter("siteid"));
System.out.println("site in newatt..."+site_id); */
/* System.out.println("date1"+date);
System.out.println("status"+status);
System.out.println("event"+event);
System.out.println("empNo"+empNo); */
String empName;
Attend_bean ab=new Attend_bean();
LeaveMasterHandler obj=new LeaveMasterHandler();
EmpAttendanceHandler empAttend =new EmpAttendanceHandler();
String disableaprove = request.getParameter("aprove")==null?"":request.getParameter("aprove");
ArrayList attend = new ArrayList();
		attend= empAttend.getEmpAttendApproval(date, empNo, status, event);
/* System.out.println("EMP_AL"+attend.size()); */

%>



</head>
<body onLoad="checkFlag()">
	<%@include file="mainHeader.jsp"%>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp"%>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Employee Attendance</h1>
			</div>
			<!-- end page-heading -->

			<table border="0" width="100%" cellpadding="0" cellspacing="0"
				id="content-table">
				<tr>
					<th rowspan="3" class="sized"><img
						src="images/shared/side_shadowleft.jpg" width="20" height="300"
						alt="" /></th>
					<th class="topleft"></th>
					<td id="tbl-border-top">&nbsp;</td>
					<th class="topright"></th>
					<th rowspan="3" class="sized"><img
						src="images/shared/side_shadowright.jpg" width="20" height="300"
						alt="" /></th>
				</tr>
				<tr>
					<td id="tbl-border-left"></td>
					<td>
						<!--  start content-table-inner ...................................................................... START -->
						<div id="content-table-inner">

							<!--  start table-content  -->
							<div id="table-content" style="height: 500px; overflow: hidden;">
								<center>
								
								<table id="customers">
							<tr colspan="3">
							<th colspan="3">Employee Present Days Details</th>
							</tr>
							<tr>
							
							<!-- <td> Date :  --><%if(event.equalsIgnoreCase("left")||event.equalsIgnoreCase("Transfer")){ %>
							<td> Left / Transfer Date :
							<input type="text" id="date" name="date"  style="text-align: center;" readonly="readonly" value="<%=date%>"></td><%}
							else{ %>
							<td> Date :
							<input type="text" id="date" name="date"  style="text-align: center;" readonly="readonly" value="<%=date%>"> 
							<img src="images/cal.gif" align="middle"	style="vertical-align: middle; cursor: pointer;"	onClick="javascript:NewCssCal('date', 'ddmmmyyyy')" /></td><%} %>
							<!-- </td> -->
							</tr>	
							
								
						
								</table>
								<br><br>
								<center>
												<br>
												<h3>DAYS-
												<input type="button" onclick="fn('div2','div1')" value="1-15" title="Click on the button to view Result of date 1 to 15 !"/>
												<input type="button" onclick="fn('div1','div2')" value="16-above" title="Click on the button to view Result of date 16 to above  !"/>
												</h3>
												<div style="height: 400px;overflow-y:hidden; width: 1180px;">
												
												<%
												int s=0,e=0,s1=0,e1=0;
												for(int start=1;start<=2;start++)
												{
												
													if(start==1)
													{
														s=1;e=15;
													}
													if(start==2)
													{
														s=16;e=(int)days;
													}
													
												%>
												<center>
												<div id="div<%=start%>" align="center" style=" height: 390px;width: 1150px;">
												<div align="center" class="imptable" style="overflow:hidden; width: 102%;">
												<table  style="width: 90%"; >
												
												<tr bgcolor="#2f747e" style="height: 35px;">
															
																<td style="width: 6%;" > <input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:white; background-color: #2f747e ;text-align: center;"  name="SR.No." value="SR.No" ></td>
																<td style="width: 8%;" > <input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:white; background-color: #2f747e ;text-align: center;"  name="Emp Code" value="Emp Code" ></td>
																<td style="width: 22%;" > <input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:white; background-color: #2f747e ;text-align: center;"  name="Emp name" value="Employee Name" ></td>
																<%
																for(int i=s;i<=e;i++)
																{
																%>
															 	<th style="width: 4%;"> <input type="text" size="4" maxlength="2"  disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 80%;  border:none;color:white; background-color: #2f747e ;text-align: center;"  name="SR.No." value= <%=i<=9?"&nbsp;"+i:i%> ></th> 
																<%
																}
																
															%>
														</tr>
														</table>
													</div>
												<div align="center" class="imptable" style="overflow-y:auto; height: 330px;width: 102%;">
														
												<table  style="width: 90%;" >
												
														<%
																int srno=1;
																int index = 0;
															
															for(int j=0;j<attend.size();j++)	
																{
																ArrayList<Attend_bean> Emp_bean=(ArrayList<Attend_bean>)attend.get(j);
																
																
																%>
																<tr style="height: 40px;">
																<%
																if(start==1)
																{
																	s1=0;e1=14;
																}
																if(start==2)
																{
																	s1=15;e1=(int)days-1;
																}
																

																for(int c=s1;c<=e1;c++)
																{
																	
																	ab=Emp_bean.get(c);
																	
																	if(c==0||c==15)
																	{
																		empName = obj.getempName(Integer.parseInt(ab.getEmpno()));
																	
																		%>
																		<td style="width: 6%;"><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: center;"  value="<%=srno%>" ></td>
																		<td style="width: 8%;"><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: center;" value="<%=EmployeeHandler.getEmpcode(Integer.parseInt(ab.getEmpno()))%>" ></td>
																		<td style="width: 22%;" ><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: left;"  value="<%=empName%>"></td> 
																		
																		<%
																	
																	}
																	
																	%>
																	<td style="width: 4%;" style="width: 4%;" align="center">
																		
																		<input type="text" size="4"  name="_<%=ab.getEmpno()%>" id="_<%=c+1%>" maxlength="2" value="<%=ab.getVal()==null?"P":ab.getVal()%>"
																	 disabled="disabled" onfocus="this.select()" title="Date=   <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase;" onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																	 
																	<%
																}
																%>
																</tr>
															<%	
															srno++;
															}
															%>
														
															
														</table></br>
														<h3>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
													P:Present&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													A:Absent&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													H:Half Day&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													WO:Week Off&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													HD:Holiday&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													LT:Left Job&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													NJ:New Joined&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</h3>
														</br></br></br></br></br>
														<table style="border: none;">
														<tr style="border: none;">
														
														<%if(status.equals("approved")){ %>
														<td style="border: none;"><font color="green" size="5">Already Approved</font></td>
														<% }
														
														if(status.equals("rejected")){ %>
														<td style="border: none;"><font color="red" size="5">Already Rejected</font></td>
														<% }
														
														else if(status.equals("pending")){
															
															if(roleId.equals("1")){
														%>
														<td  style="border: none;"><%if(!disableaprove.equalsIgnoreCase("true")){ %><input type="button" value="Approved" onclick="return approve('<%=ab.getEmpno()%>','<%=ab.getStatus()%>','<%=request.getParameter("date")%>')"><%} %> 
																<input type="button" value="Reject" onclick="reject('<%=ab.getEmpno()%>','<%=ab.getStatus()%>','<%=request.getParameter("date")%>')"> </td>		
														<%}  
															else{%>
																<td  style="border: none;"><%if(!disableaprove.equalsIgnoreCase("true")){ %><input type="button" value="Approved" onclick="noadmin()"> <%} %>
																<input type="button" value="Reject" onclick="noadmin1()"> </td>		
															<%}	
														
														}%>
														</tr>
														</table>
														
														</div>
														</div>
														
														</center>
														<%
												}
														%>
														
														</div>
														</center>
						 <%-- <input type="hidden" name="siteid" id="siteid" value="<%=site_id%>">  --%>
								</div>
							<!--  end table-content  -->

							<div class="clear"></div>

						</div> <!--  end content-table-inner ............................................END  -->
					</td>
					<td id="tbl-border-right"></td>
				
				
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