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
<%@page import="java.math.BigDecimal"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="payroll.Model.*"%>
<%@page import="payroll.DAO.*"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<%@ page import="java.text.NumberFormat" %>
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

<script src="js/jquery/jquery.autocomplete.js"></script>
<%
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);
%>
<%


String date=ReportDAO.getSysDate();
String date1=date.substring(7,11);
EncashmentHandler ofh1=new EncashmentHandler();
//EmpOffHandler eoffhdlr = new EmpOffHandler();
//EmpAttendanceHandler EAH=new EmpAttendanceHandler();


String action=request.getParameter("action")==null?"":request.getParameter("action");
String val=request.getParameter("Save")==null?"":request.getParameter("Save");
System.out.println("save value is..."+val);
//int stat1=Integer.parseInt("stat");
ArrayList<EncashmentBean> list2=null;
ArrayList<EncashmentBean> calculatedlist = new ArrayList<EncashmentBean>();
int yy1=0;
String status="";
String stat="";
int st=-1;
String yy="";
String prjname="";
String postcheck="";
 /*for holding leave days on HO and Other Site*/
ArrayList<Object> list4=null; /*for holding basic and encashment */
System.out.println("action is..."+action);
if(action.equalsIgnoreCase("getDetails")||action.equalsIgnoreCase("getreport"))
{
	
	
	 //int siteid = Integer.parseInt(request.getParameter("prj"));
	 System.out.println("theeeeeeeeeeeeee first prjcode");
	  list2 =(ArrayList<EncashmentBean>)session.getAttribute("EmpEncashList");
	  calculatedlist =(ArrayList<EncashmentBean>)session.getAttribute("getcalculatedlist");
	   //stat=request.getParameter("statuss")==null?"":request.getParameter("statuss");
   ArrayList<EmployeeBean> projEmpNmlist = new ArrayList<EmployeeBean>();
    yy=(String)session.getAttribute("year1");
	 status=(String)session.getAttribute("status");
	  prjname=(String)session.getAttribute("pname");
	 //stat=(String)session.getAttribute("statuss");
   yy1=Integer.parseInt(yy);
   st=Integer.parseInt(status);
   System.out.println("in session year is..."+yy);
   System.out.println("in session prjname is..."+prjname);
   System.out.println("status is..."+status);
   System.out.println("in st is..."+st);
   System.out.println("calculatedlist size is...");
   System.out.println("save value is..."+val);
    session.setAttribute("list2", list2);
    session.setAttribute("year",yy);
    session.setAttribute("prj",prjname );
    //session.setAttribute("savedlist",calculatedlist );
    int i=0;
    postcheck=EncashmentHandler.checkpost(yy);
  }



 
%>
<%  SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); // today
String today=format.format(c1.getTime());     /* -----for System date --------*/

%>


<script type="text/javascript">

jQuery(function() {

    $("#submit").click(function(){
    	if(true)
			{
			document.getElementById("process").hidden=false;
			var stat=document.getElementById("stat").value;
			if(stat==1)
			{
			document.getElementById("process").hidden=true;
			}
			return true;
			}
		
		
	});
    
$("#save").click(function(){
    	
		var r=confirm("Are you Sure to Save Transaction for this Year...");
		if(r==true)
			{
			document.getElementById("process").hidden=false;
			var fg=document.getElementById("fg").value;
			return true;
			}else{
				return false;
			}
		
		
	});
	
$("#getencash").click(function(){
	
	if(true)
		{
		document.getElementById("process").hidden=false;
		/*var stat=document.getElementById("stat").value;
		if(stat==2)
			{
			document.getElementById("process").hidden=true;
			}*/
		return true;
		}
	
	
});

$("#posttran").click(function(){
	
	if(true)
		{
		document.getElementById("process").hidden=false;
		/*var stat=document.getElementById("stat").value;
		if(stat==1)
			{
			confirm("Your Transaction Save Successfully...");
			document.getElementById("process").hidden=true;
			}*/
		return true;
		}
	
	
});

});


function checkFlag1() {
	
	var f =(document.getElementById("fg").value);
		alert("unload flag is.."+f);
		if (f =="save") {
		
		document.getElementById("process").hidden=true;
	}
		var stat =(document.getElementById("stat").value);
		/*if(stat==1)
		{
		document.getElementById("process").hidden=true;
		}*/
		/*if(stat==2)
		{
		document.getElementById("process").hidden=true;
		}*/
	/*else if (f == 2) {
		alert("Record Rejected Successfully");
	}*/
	
}

function checkFlag() {
	
		var f =(document.getElementById("fg").value);

			if (f =="save"&&stat!=2&&stat!=1) {
			alert("Record Saved Successfully");
			document.getElementById("process").hidden=true;
				}
			var stat=document.getElementById("stat").value;
			if(stat==1&&f=="save")
				{
				
				document.getElementById("process").hidden=true;
				}
			if(stat==2 && f!="save")
			{
			document.getElementById("process").hidden=true;
			}
			 if(f=="save"&&stat==2)
				{
				
				document.getElementById("process").hidden=true;
				}
		/*else if (f == 2) {
			alert("Record Rejected Successfully");
		}*/
		
	}/*

function act(date)
{
	
	
	var r = confirm("Are you sure to send for Approval ?");
	if (r == true) {
		window.location.href="EmpAttendServlet?action=approval&date="+date;
	} 
	

}
function view_att(site,status,dt)
{
	
	window.location.href="EmpPresentSeat.jsp?site_id="+site+"&status="+status+"&date="+dt,"_self";
	
}
function view_att1(status,dt,srno,name,code)
{
	window.location.href="EmpPresentSeat.jsp?action=getdetails&status="+status+"&prj="+srno+"&proj="+name+":"+code+"&date="+dt,"_self";
	
}

function view_att_2(status,empno,date,event){  
	window.location.href="newAttendSheet.jsp?status="+status+"&empno="+empno+"&date="+date+"&event="+event+" ";
}


		function enableButton(){
			
			document.getElementById("send").disabled=false;
			document.getElementById("checkAll").disabled=false;
			
		}
 */		
 
	</script>


</head>
<body onload="checkFlag()" onunload="checkFlag1()">
	<%@include file="mainHeader.jsp"%>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp"%>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Encashment</h1>
			</div>
			<!-- end page-heading -->
<form action="EncashServlet?action=displayEmp" method="post">
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
							<div id="table-content" style="height: 500px; overflow: auto;">
								<center>
								<div>
								<table id="customers">
								<tr>
								
										<td>For Year :</td>
								<td><select id="yy" name="yy"  style="width: 138px">
									<%if(action.equalsIgnoreCase("getDetails")||
											action.equalsIgnoreCase("NoRecord")||
											action.equalsIgnoreCase("getreport")){
											%>
											 <option value="<%=yy %>" selected="selected"><%= yy1%></option> 
											<%}else{ %>
											<option value="date1" selected="selected">select year</option>
											 <%}
											 for(int i=Integer.parseInt(date1)-1;i>=1970;i--) {%>
    							 <option value="<%=i %>" ><%=i %></option><%} %></select>
      						  
     							</td>
								
								 <%if(action.equalsIgnoreCase("getDetails")||action.equalsIgnoreCase("NoRecord") ){
									 %>
								 <td> Project :</td>
								<% if(val.equalsIgnoreCase("save"))
									{%>
									<td><input id="prj" type="text" readonly="readonly" style="width:400px"   value="ALL">
								<%}else{ %> 
								<td><input id="prj" type="text" readonly="readonly" style="width:400px"   value="<%=prjname.toUpperCase()%>"></td>
								<%} %>
								
								
								
								 <%}else if(action.equalsIgnoreCase("getreport"))
								 {%>
									 <td> Project :</td>
									
									
									
									 <td><input id="prj" type="text" readonly="readonly" style="width:400px"   value="<%=prjname%>">
									
									 <input id="back" type="submit" value="Back" onclick="form.action='encashment.jsp?'";  /></td>
										

							<%	 }
								  else{ %>
									<td>Select Project :</td>
									<td><select id="pp" name="pp"  style="width: 308px">
											 <option value="all" selected="selected">ALL</option> 
											 <%
    						ArrayList<EncashmentBean> list1= new ArrayList<EncashmentBean>();
    						
    						list1=ofh1.getprojectCode();
    						for(EncashmentBean lkb :list1)
 							{
    							
    							
 							%>
      						 <%--  <option value="<%=lkb.getPrj_code()%>" title="<%=lkb.getSite_name() %>"><%=lkb.getPrj_name()%> --%>
      						  <option value="<%=lkb.getPrj_srno()%>" title="<%=lkb.getSite_name() %>"><%=lkb.getSite_name() %>
     					 	<%
     					 	}
    							 	
     					 	%>
     			 </select></td>
										
								<%} 	
								 if(!action.equalsIgnoreCase("getreport")&& !action.equalsIgnoreCase("getDetails")&&!action.equalsIgnoreCase("NoRecord"))
								 {%>
								 <td align="center" ><input id="submit" type="submit" value="Submit"  /></td>
								 <%} %> 
							<%if(action.equalsIgnoreCase("getDetails")) {
							if(st==0){
								%>
								
								<!-- <td><input id="getencash" type="submit" value="GetEncashment"  onclick="form.action='EncashServlet?action=getencashment' ";/></td> -->
							<% }else{ %>
							<td align="center" ><%if(st!=2||val.equalsIgnoreCase("save")){ %><input id="getReport" type="submit" value="Get Report" onclick="form.action='encashment.jsp?action=getreport'";  />
							 <% if(postcheck.equalsIgnoreCase("enablepost")){%><input id="posttran" type="submit" value="PostTransaction" onclick="form.action='EncashServlet?action=posttransaction'";  /><%}} %>
							<%if(val.equalsIgnoreCase("Save")){%>
									<%
									}if(st==2&&!val.equalsIgnoreCase("save")){%>
							 <input id="save" type="submit" value="SaveTransaction" onclick="form.action='EncashServlet?action=SaveTransaction'";  />
							 	<%} %>
							 	<%if(st==1||val.equalsIgnoreCase("save")){%>
			 	 			<input id="edit" type="submit" value="Edit" name="listt" onclick="form.action='editEncash.jsp?action=editrecord'";  />
							 <% 
			   						}%>
							<input id="back" type="submit" value="Back" onclick="form.action='encashment.jsp?'";  /></td>
							<%} }%>
								</tr>
								</table>
							<%if(!action.equalsIgnoreCase("getreport")&&!action.equalsIgnoreCase("NoRecord")) {%>
								<table width="700" id="customers">
								<%if(st==0){
									}else{%>
									
				        <tr><%	if(action.equalsIgnoreCase("getDetails"))
						{
							
						
								if(val.equalsIgnoreCase("save")||st==1){%>
				         <th width="50"></th>
				         <%}} %>
				             <th width="50">Sr No</th>
				              <th width="50">Emp Code</th>
				             <th width="200">User Name</th>
				             <th width="100">On HO</th>
				             <th width="100">Leave on HO</th>
						 <th width="100">On Site</th>
						  <th width="100">Leave on Site</th>
						  <th width="100">Input_Amount</th>
					<th width="100">Encashment</th>
					           
				         </tr>
				      <%} %>
					
				
			 
			<%} %>
			 <%if(action.equalsIgnoreCase("NoRecord")) {%>
			 <br><center><b><h1>No Any Record For This Project..!!</h1></b></center>
			 <br><input type="submit" value="Go Back" onclick="form.action='encashment.jsp?'";/>
			 <%} %>
			
			<%if(action.equalsIgnoreCase("getreport")) {%>
			<br><br> <center>
			<table border="1" id="customers" width="100%" align="center">
			<tr>
				<th>Report Type</th>
			<tr>
			<tr class="alt">
				<td  align="center">
				
				<table align="center" width="50%" >
					<tr class="alt" height="30" align="center">
                     <input type="hidden" id="action" name="action" value="emppflist"></input>
						<table>
					
					<tr><td colspan="2"><input type="radio" name="reporttype" value="pdfFile" checked="checked">PDF File</td>
					<td colspan="2"><input type="radio" name="reporttype" value="excelFile" >Excel File</td>
						
				
						
					
				</table>
					<tr>
						<td colspan="4" align="center"><input type="submit"
							value="Go" onclick="form.action='EncashServlet?action=getreport'"; /></td>
					</tr>
				</table>

			  </td>
			</tr>
</table>
</center>
			 
		<%} %>	 
			 
			 
			
			 
			
			 
			 
			<%
			
			
			if(st==1)
			{
			if(action.equalsIgnoreCase("getDetails"))
			{
				
				if(!val.equalsIgnoreCase("save"))
				{
				int branch=0;
				ArrayList<EncashmentBean> list3=new ArrayList<EncashmentBean>(); 
				EmployeeBean trbn = new EmployeeBean(); 
				EncashmentBean ebn;
				TranHandler tbn1=new TranHandler();
				int i=0;
				int u=0;
				
				
				for(EncashmentBean tbn : list2){
					ebn=new EncashmentBean();
			    	%>
			
			<tr> 
			
			<td style="width: 1%;">  <input type="checkbox" class="empCheckbox" id="" value="<%=tbn.getEmpCode()%>" name="list"> </td>
																		
			<td width="100" align="center"><%=++i %>  </td>
			
			<%-- <%  float basic=tbn1.getBasic(tbn.getEMPNO()); %> --%>
			<td width="100" align="center"><%=tbn.getEmpCode() %></td>
			<td width="220"><%=tbn.getName() %>  </td> 
			 <%-- <%  int DaysInMonths[]=ofh1.getNoOfDays(tbn.getEMPNO());%> --%>
			<td width="100" align="center"><%=tbn.getOnHo() +" D"%></td>
				<td width="100" align="center"><%=tbn.getLeaveOnHO() +" D"%></td>
				<td width="100" align="center"><%=tbn.getOnOsite() +" D"%></td>
				<td width="100" align="center"><%=tbn.getLeaveOnOS() +" D"%></td>
				<td width="100" align="center"><%=tbn.getBasic() %></td>
				<td width="100" align="center"><%= tbn.getEncashment() %></td>
			
				<%-- <% double day= DaysInMonths[0];
				float  day1=(float)(day*19/365); 
				int hodayleav=Math.round(day1);
				
				
			System.out.println("dayssssss111..."+hodayleav);
			double day2= DaysInMonths[1];
				float  day11=(float)(day2*30/365); 
				int osdayleav=Math.round(day11); --%>
				
				<!--  /*for( m=h;m<=h;m++)
					{
						for( n=0;n<=0;n++)
						{
							leaveday[m][n]=hodayleav;
							System.out.println("values of leave for HO for empcode..."+tbn.getEMPCODE()+"is.."+leaveday[m][n]);
						}
						
						leaveday[m][n]=osdayleav;
						System.out.println("values of leave for OS for empcode..."+tbn.getEMPCODE()+"is.."+leaveday[m][n]);
					}*/
					
			        
			  
			
		    
				//leaveday[1]=osdayleav;
			 -->
				
			
				<%-- %>
			<td width="100" align="center"><%=hodayleav +" D"%></td>
			<td width="100" align="center"><%=DaysInMonths[1] +" D"%></td>
			<td width="100" align="center"><%=osdayleav  +" D"%></td>
			<td width="100" align="center"><%=basic %></td>
			
			 <% 
			 float encash=ofh1.getEncashmentAmnt1(tbn.getEMPNO(),basic,DaysInMonths); --%>
			 <%-- /* float k = (float) Math.round(encash * 100) / 100; */
			 
			 	BigDecimal bigd=new BigDecimal(encash);
		        bigd=bigd.setScale(2,BigDecimal.ROUND_HALF_EVEN);
		       /* for( s=h;s<=h;s++)
				{
					for( t=0;t<=0;t++)
					{
						basicencash[s][t]=basic;
						System.out.println("values of basic for empno..."+tbn.getEMPCODE()+"is.."+basicencash[s][t]);
					}
					
					basicencash[s][t]=encash;
					System.out.println("values of encashment for empno..."+tbn.getEMPCODE()+"is.."+basicencash[s][t]);
				}
				h++;*/
		        
			 %>
			 	<td width="100" align="center"><%= bigd %></td> --%>
			</tr>
				 
			 <%
			 	ebn.setEmpCode(tbn.getEmpCode());
				ebn.setEmpno(tbn.getEmpno());
				ebn.setName(tbn.getName());
				ebn.setOnHo(tbn.getOnHo() );
				ebn.setLeaveOnHO(tbn.getLeaveOnHO());
				ebn.setOnOsite(tbn.getOnOsite());
				ebn.setLeaveOnOS(tbn.getLeaveOnOS());
				ebn.setBasic(tbn.getBasic());
				ebn.setEncashment(tbn.getEncashment());
				list3.add(ebn);
			   }
				session.setAttribute("list3", list3);
				}
			
				else
				{
					EncashmentHandler ehn=new EncashmentHandler();
					System.out.println("while editing in else");
					ArrayList<EncashmentBean> list3=new ArrayList<EncashmentBean>(); 
					list3=ehn.getUpdaterecord(yy);
					//EmployeeBean trbn = new EmployeeBean(); 
					EncashmentBean ebn;
					TranHandler tbn1=new TranHandler();
					int i=0;
					int u=0;
					
					
					for(EncashmentBean tbn : list3){
					
				    	%>
				
				<tr> 
				
				<td style="width: 1%;">  <input type="checkbox" class="empCheckbox" id="" value="<%=tbn.getEmpCode()%>" name="list"> </td>
																			
				<td width="100" align="center"><%=++i %>  </td>
				
				
				<td width="100" align="center"><%=tbn.getEmpCode() %></td>
				<td width="220"><%=tbn.getName() %>  </td> 
				 
				<td width="100" align="center"><%=tbn.getOnHo() +" D"%></td>
				<td width="100" align="center"><%=tbn.getLeaveOnHO() +" D"%></td>
				<td width="100" align="center"><%=tbn.getOnOsite() +" D"%></td>
				<td width="100" align="center"><%=tbn.getLeaveOnOS() +" D"%></td>
				<td width="100" align="center"><%=tbn.getBasic() %></td>
				<td width="100" align="center"><%= tbn.getEncashment() %></td>
				</tr>
				<%}
				
				
				}
				 }} %>
								
	
			
			 <% 	if(action.equalsIgnoreCase("getDetails"))
				{
				
				if(st==2){
					int i=0;
					if(val.equalsIgnoreCase("save")){
						EncashmentHandler ehn=new EncashmentHandler();
						System.out.println("while editing in else");
						ArrayList<EncashmentBean> list3=new ArrayList<EncashmentBean>(); 
						list3=ehn.getUpdaterecord(yy);
						//EmployeeBean trbn = new EmployeeBean(); 
						int u=0;
						for(EncashmentBean tbn : list3){%>
					<tr> 
					
					<td style="width: 1%;">  <input type="checkbox" class="empCheckbox" id="" value="<%=tbn.getEmpCode()%>" name="list"> </td>
																				
					<td width="100" align="center"><%=++i %>  </td>
					<td width="100" align="center"><%=tbn.getEmpCode() %></td>
					<td width="220"><%=tbn.getName() %>  </td> 
					<td width="100" align="center"><%=tbn.getOnHo() +" D"%></td>
					<td width="100" align="center"><%=tbn.getLeaveOnHO() +" D"%></td>
					<td width="100" align="center"><%=tbn.getOnOsite() +" D"%></td>
					<td width="100" align="center"><%=tbn.getLeaveOnOS() +" D"%></td>
					<td width="100" align="center"><%=tbn.getBasic() %></td>
					<td width="100" align="center"><%= tbn.getEncashment() %></td>
					</tr>
					<%}
					
					}else{ 
					for(EncashmentBean tbn : calculatedlist){	%>
				
						<tr> 
						
						   <%-- <input type="checkbox" class="empCheckbox" id="" value="<%=tbn.getEmpCode()%>" name="list"> </td> --%> 
																					
						<td width="100" align="center"><%=++i %>  </td>
						
						
						<td width="100" align="center"><%=tbn.getEmpCode() %></td>
						<td width="220"><%=tbn.getName() %>  </td> 
						 
						<td width="100" align="center"><%=tbn.getOnHo() +" D"%></td>
						<td width="100" align="center"><%=tbn.getLeaveOnHO() +" D"%></td>
						<td width="100" align="center"><%=tbn.getOnOsite() +" D"%></td>
						<td width="100" align="center"><%=tbn.getLeaveOnOS() +" D"%></td>
						<td width="100" align="center"><%=tbn.getBasic() %></td>
						<td width="100" align="center"><%= tbn.getEncashment() %></td>
						</tr>
						<%}
							}
				}else if(st==0){ %>
				<br><h1>Sorry No Records for This Year...<br>
				First Calculate Encashment for This Year & Save Your Transaction...</h1><br>
				<input id="getencash" type="submit" value="GetEncashment"  onclick="form.action='EncashServlet?action=getencashment' ";/>
				<% }
				}
				%> 
			   		
					
			 </table>
				 
			</div>				
			</div>
			
			</div>
			<input id="stat" type="hidden" name="stat" value=<%=st %> />					
				<input id="fg" type="hidden" name="fg" value=<%=val %> />	
													
					<div id="process"  style="z-index: 1000;background-color:white;height: 100%;width: 100%;top: 0px;left: 0px;position: fixed;opacity:0.8;" hidden=true; >
				<div align="center" style="padding-top: 20%;">
				
				<h1> Please wait it takes Few Min....Be Patience</h1>
				
				<img alt="" src="images/process.gif">
				
				</div>
			</div>							
			
									
								
							<!--  end table-content  -->

							<div class="clear"></div>
					
						 <!--  end content-table-inner ............................................END  -->
					</td>
					<td id="tbl-border-right"></td>
				
				
				<tr>
					<th class="sized bottomleft"></th>
					<td id="tbl-border-bottom">&nbsp;</td>
					<th class="sized bottomright"></th>
				</tr>
			
			<div class="clear">&nbsp;</div>
</table>
 
</form>
		</div>
		<!--  end content -->
		<div class="clear">&nbsp;</div>
		
	</div>
	<!--  end content-outer........................................................END -->

	<div class="clear">&nbsp;</div>

 <div class="loader"></div>
</body>
</html>
								
