<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
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
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>

<%
LookupHandler lkph= new LookupHandler();
ArrayList<TranBean> tranlist=new ArrayList<TranBean>();
int eno = (Integer)session.getAttribute("EMPNO");
LeaveMasterHandler obj=new LeaveMasterHandler();
EmpOffHandler eoffhdlr = new EmpOffHandler();
EmpOffBean eoffbn = new EmpOffBean();

int site_id=0;

site_id=Integer.parseInt(request.getParameter("site_id")==null?"":request.getParameter("site_id"));

String state="";
eoffbn = eoffhdlr.getEmpOfficAddInfo(Integer.toString(eno));

int trncd=0;
String select=new String();
String empno="";
empno=request.getParameter("empno")==null?"":request.getParameter("empno");
String empName;
String date="";
date=request.getParameter("date")==null?ReportDAO.getSysDate():request.getParameter("date");
EmpAttendanceHandler EAH=new EmpAttendanceHandler();

ArrayList Emp_al=new ArrayList();
Emp_al=EAH.getEmpAttend(date, empno, eno,state);

ArrayList<Attend_bean> tran = new ArrayList<Attend_bean>();
tran = eoffhdlr.getEmpListForAttendance(request.getParameter("site_id"),date,empno);

for(Attend_bean abb : tran){
	System.out.print("dasdasdsa"+abb.getEmpno());
	
}

session.setAttribute("emplist", tran);

float days=Calculate.getDays(date);
String action = request.getParameter("action")==null?"":request.getParameter("action");
TranHandler trnh= new TranHandler();
LookupHandler lkh=new LookupHandler();
ArrayList<Lookup> emplist=new ArrayList<Lookup>(); 

ArrayList<String> holidays= new ArrayList<String>();
ArrayList<String> weekdays= new ArrayList<String>();
HolidayMasterHandler hmh = new HolidayMasterHandler();

String fromDate="";
String type=" ";

/* if(st.equals("saved") || st.equals("approved") || st.equals("rejected") ) */{	
//weekdays=hmh.getweekoff(date , site_id);
/* 
if(weekdays.size()==0)
{
	weekdays=hmh.getweekoff(date); 
} */

System.out.println("weekdays"+weekdays);
//holidays = hmh.getHoldmast(date,site_id);
}
emplist=lkh.getSubLKP_DESC("ET");	// ET is Code for Employee Type


int z=0,n=0;

RoleDAO obj1=new RoleDAO();
String roleId=obj1.getrole(Integer.parseInt(session.getAttribute("UID").toString()));
int h11=0;
boolean check=false;
String string[]=null;



String status="";
 if(date!=null)
{
 status=EAH.getAttendanceStatus(date, site_id);
} 

ArrayList<Attend_bean> att = new ArrayList<Attend_bean>();
att = eoffhdlr.getEmpListForAttendance(request.getParameter("site_id"),date,empno);


for(Attend_bean ab1: att){
	System.out.println("Emp no in jsp"+ ab1.getEMPNO());
}
System.out.println("Emp no in jsp");
session.setAttribute("emplist", att);



int flag=-1;
try
{  
	
	try
	{
	flag=Integer.parseInt(request.getParameter("flag")==null?"":request.getParameter("flag")); 
	}
	
	catch(Exception e)
	{
		System.out.println("no flag value"+flag);
	}
	
}
catch(Exception e)
{
	System.out.println("First Time Loading");
}

 
%>



<script type="text/javascript">


$(document).ready(function () {
    $('#edit').click(function () {
    	
    	if($('input:text').attr("disabled"))
    		{
    	$('input:text').attr("disabled",false);
    	$('#edit').attr('value','Disable');
    		}
    	else
    		{
    		$('input:text').attr("disabled",true);
        	$('#edit').attr('value','Edit');
    		}
    });
    
    $('#tranSave').click(function () 
    		{
    	$('input:text').removeAttr("disabled");
    	
    		});
   
    $('input:text').focus(function () 
    		{
    	
    		$(this).css("background-color", "yellow");
    
    		});
    		
    $('input:text').blur(function () 
    		{
    	
    		$(this).css("background-color","");
    
    		});
});


jQuery(function() {
	$("#send").click(function(){
		var cnt=0;
		<% for(int j=0;j<Emp_al.size();j++)
		{
			
			
			ArrayList<Attend_bean> Emp_bean=(ArrayList<Attend_bean>)Emp_al.get(j);
			for(int c=0;c<Emp_bean.size();c++)
			{
				Attend_bean ab=new Attend_bean();
				ab=Emp_bean.get(c);
			%>
			if(document.getElementById("_<%=ab.getEmpno()%>").value=="" || document.getElementById("_<%=ab.getEmpno()%>").value==" ")
				{
				cnt++;
				}
			
			<%
				
			}	
			} %>
			
			
		if(cnt>0)
			{
			alert("Blank attendance can't be Send for Approval !\n\n\nPlease Fill some Value!");
			}
			else
			{
			var date = document.getElementById("date").value;
			
			var r = confirm("Are you sure to send for Approval ?");
			if (r == true) {
				window.location.href="EmpAttendServlet?action=approval&date="+date;
			} 
			
			}
	});
});

function checkFlag() {
		var f = parseInt(document.getElementById("flag1").value);

		if (f == 1) {
			alert("Record updated Successfully");

		}
		else if (f == 2) {
			alert("Into servlet Else");
		}
		else if (f == 3) {
			alert("Something goes wronge");
		}
		else if (f == 4) {
			alert("Successfully Send for Approval!");
		}
		else if (f == 5) {
			alert("Rejected !Please check the data and Resend for Approval!");
		}
		
		document.getElementById("div2").style.display='none';
	}

function fn(id1,id2)
{

document.getElementById(id1).style.display='none';
document.getElementById(id2).style.display='block';

}

function approve_att(prj,dt)
{window.location.href="EmpAttendServlet?action=approved&prj="+prj+"&date="+dt,"_self";
}
function reject_att(prj,dt)
{window.location.href="EmpAttendServlet?action=reject&prj="+prj+"&date="+dt,"_self";

}

function inputLimiter(e,allow) {
		  var AllowableCharacters = '';
		  if (allow == 'Numbers'){AllowableCharacters='PpAaLl';}
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
	
	function check()
	{
		var date = document.getElementById("date").value;
		if(date=="")
			{
			alert("Please select date !");
			}
		else
			{
			window.location.href="EmpPresentSeat.jsp?date="+date,"_self";
			}
		
	}
	/* function act()
	{
		var date = document.getElementById("date").value;
		
		var r = confirm("Are you sure to send for Approval ?");
		if (r == true) {
			window.location.href="EmpAttendServlet?action=approval&date="+date;
		} 
		
	
	} */
	
	
	function attd()
	{
		window.location.href="attendanceMain.jsp?status=rejected","_self";
	}
	function appr()
	{
		window.location.href="approveAttendance.jsp?status=all","_self";
	}
</script>

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
		<h1>Employees Attendance </h1>
	</div>
	<!-- end page-heading -->

	<table id="content-table" style="width: 100%;">
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

									<form action="EmpAttendServlet?action=insertSingle&st=<%=status%>"method="post" onsubmit="validate()">

										
												

													<table id="customers" style="width: 95%;">
													<tr>
													<th colspan="3">Employee Present Days Details</th>
													</tr>
														<tr>
															<td>Project Name : 
															<% if(request.getParameter("site_id")==null)
															{
																%>
																<%=eoffbn.getPrj_name()%>
																<%
															}else
															{
																ProjectListDAO pl=new ProjectListDAO();
																ProjectBean pb=new ProjectBean();
																pb=pl.getProjectInfo(Integer.parseInt(request.getParameter("site_id")));
																
																
																%>
																<%=pb.getSite_Name() %>
																<%
															}
																%></td>
															<td> Date :
																<input name="date" size="20" id="date" value="<%=date==null?"":date%>" disabled="disabled" readonly="readonly" type="text" onchange="check()">&nbsp;
																<%-- <% if(request.getParameter("prj")==null)
																{%>
																<img src="images/cal.gif" align="absmiddle" style="vertical-align:middle; cursor:pointer;" onClick="javascript:NewCssCal('date', 'ddmmmyyyy')" />
																<% }%> --%>
																</td>
															<td>
															
															<%if(status.equalsIgnoreCase("saved") || status.equalsIgnoreCase("rejected"))
																{
																
																if(request.getParameter("prj")==null)
																{
																%>
															<input	type="button" value="Edit" id="edit" height="150" width="150" />
															<input type="submit" id="tranSave" value="Save"/>
															<!-- <input type="button" id="send" value="Send for Approval"/> -->
															<%
																}
																}
															else if(status.equalsIgnoreCase("pending")){
																if(request.getParameter("prj")!=null)
																{
																%>
															<%-- 	<input type="button" id="" value="Approved" onclick="approve_att('<%=site_id%>','<%=date%>')">
																<input type="button" id="" value="Reject" onclick="reject_att('<%=site_id%>','<%=date%>')">
																 --%>
																<%}
																else
																{%>
																Already Send for Approval
																<%	
																}
															}
															if(status.equalsIgnoreCase("approved"))
															{
																%>
																Successfully Approved
																<%	
															}
															
															
			
			
			if(request.getParameter("prj")!=null)
			{
			%>
				<input type="Button" value="Back" onclick="appr()">
			<%}else
			{
				%>
				<input type="Button" value="Back" onclick="attd()">
			<%
			}
			
			%>
															
															 </td>
														</tr>
													</table>
													<center>
												<br>
												<h3>DAYS-
												<input type="button" onclick="fn('div2','div1')" value="1-15" title="Click on the button to view Result of date 1 to 15 !"/>
												<input type="button" onclick="fn('div1','div2')" value="16-above" title="Click on the button to view Result of date 16 to above  !"/>
												</h3>
												<div style="height: 400px; width: 1180px;">
												
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
											<%if(Emp_al.size()<9){%>
												<div align="center" class="imptable" style="overflow:hidden; width: 102%;">
												<% }else{%>
												<div align="center" class="imptable" style="overflow:hidden; width: 100.5%;">
												<% }%>
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
												
												<div  align="center" class="imptable" style="overflow-y:auto; height: 330px;width: 102%;">
														
												<table  style="width: 90%;"  >
												
														<%
																int srno=1;
																int index = 0;
																if(Emp_al.size()!=0){
															for(int j=0;j<Emp_al.size();j++)
															{
																
																
																ArrayList<Attend_bean> Emp_bean=(ArrayList<Attend_bean>)Emp_al.get(j);
																
																%>
																<tr style="height: 35px;">
																
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
																	check=false;
																	for(int l=0;l<holidays.size();l++ )
																	{
																	string=(holidays.get(l)).split("-");
																	h11=Integer.parseInt(string[0]);
																	if(h11==c+1)
																		check=true;
																	}
																	
																	Attend_bean ab= new Attend_bean();
																	
																	//Attend_bean ab=new Attend_bean();
																	ab=Emp_bean.get(c);
																	
															%>
																
																<%	if( c==0||c==15)
																	{
																		empName = obj.getempName(Integer.parseInt(ab.getEmpno()));
																
																		%>
																		<%if(c==0){ %>
																		<td style="width: 1%;">  <input type="checkbox" class="empCheckbox" id="" value="<%=ab.getEmpno()%>" name="list" onclick="enableButton()"> </td>
																		<td style="width: 4.2%;"><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: center;"  value="<%=srno%>" ></td>
																		<%} %>
																		<%if(c==15){ %>
																		<td style="width: 6%;"><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: center;"  value="<%=srno%>" ></td>
																		<%} %>
																		
																		<td style="width: 8%;"><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: center;" value="<%=EmployeeHandler.getEmpcode(Integer.parseInt(ab.getEmpno()))%>" ></td>
																		<td style="width: 22%;" ><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: left;"  value="<%=empName%>"></td> 
																		
																		<%
																		
																	}
																	%>
																	
																	<%String DATE_FORMAT = "yyyy MM dd";
    																SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    																Calendar c1 = Calendar.getInstance(); // today
    																int d1=Integer.parseInt(date.substring(0,2)),d2=d1-1,d3=d2-1,d4=d3-1,d5=d4-1,d6=d5-1;   // according to the selected date to admin
    																
    																//System.out.println("Today is " + sdf.format(c1.getTime()));
    																
    																//int d1=c1.getTime().getDate(),d2=d1-1,d3=d2-1; // if according to date attendance to admin also
    																
    																
    																//System.out.println("Welcome11111111111112222222222222222211111111111111");				
    																String years=date.substring(7,11);
    																	
    																	int year=Integer.parseInt(years);
    																	String months=date.substring(3,6);
    																	int month=Integer.parseInt(years);
    																	
    																	 int s11=0; int s22=0; int s33=0;  int s44=0;   int s55=0;
    	    															 int s66=0; int s77=0; int s88=0;  int s99=0; int s00=0;
    																									
    																/* 	// auto generated sundays code starts here
    																
    																	if(months.equals("Jan"))
    																		month=0;
    																	else if(months.equals("Feb"))
    																		month=1;
    																	else if(months.equals("Mar"))
    																		month=2;
    																	else if(months.equals("Apr"))
    																		month=3;
    																	else if(months.equals("May"))
    																		month=4;
    																	else if(months.equals("Jun"))
    																		month=5;
    																	else if(months.equals("Jul"))
    																		month=6;
    																	else if(months.equals("Aug"))
    																		month=7;
    																	else if(months.equals("Sep"))
    																		month=8;
    																	else if(months.equals("Oct"))
    																		month=9;
    																	else if(months.equals("Nov"))
    																		month=10;
    																	else
    																		month=11; 

    																//	System.out.println("MOnth"+month); 
    																	// year = Calendar.getInstance().get(Calendar.YEAR);
    																   //  month =Calendar.getInstance().get(Calendar.MONTH);   // current month
    															//put the month u want
    															     Calendar cal = new GregorianCalendar(year, month, 1);
    															    List<Integer> Sundays = new ArrayList<Integer>();
    															    List<Integer> otherDays = new ArrayList<Integer>();
    															    List<Integer> abc = new ArrayList<Integer>();
    															    do {
    															        int day = cal.get(Calendar.DAY_OF_WEEK);
    															        int dayInMonth = (cal.get(Calendar.DAY_OF_MONTH));
    															        if ( day == Calendar.SUNDAY) {
    															            Sundays.add(dayInMonth);
    															         //  System.out.println("SUNDAY"+dayInMonth);
    															            abc.add(dayInMonth);
    															        } else {
    															            otherDays.add(dayInMonth);
    															           // System.out.println(Calendar.DATE+"	"+dayInMonth);
    															           
    															        }
    															        cal.add(Calendar.DAY_OF_YEAR, 1);
    															    }  while (cal.get(Calendar.MONTH) == month);
    															
    															  
    															    if(abc.size()==5)
    															   {
    																   s11=abc.get(0); s22=abc.get(1); s33=abc.get(2);s44=abc.get(3);s55=abc.get(4);
    															   }
    															   
    															   if(abc.size()==4)
    															   {
    																   s11=abc.get(0); s22=abc.get(1); s33=abc.get(2);s44=abc.get(3);
    															   }

    															   if(abc.size()==0){}
    															   // auto generated sundays code ends here...........!!!!!!!!!!!! */
    												
    															   
    															    //to take sunday from  holdmast below code works
    															   if(weekdays.size()==10)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															    s77=Integer.parseInt(weekdays.get(6).substring(0,2));
														    		    s88=Integer.parseInt(weekdays.get(7).substring(0,2));
														    		    s99=Integer.parseInt(weekdays.get(8).substring(0,2));
														    		    s00=Integer.parseInt(weekdays.get(9).substring(0,2));
												    	  		    }	
    															    else   if(weekdays.size()==9)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															    s77=Integer.parseInt(weekdays.get(6).substring(0,2));
	    															    s88=Integer.parseInt(weekdays.get(7).substring(0,2));
														    		    s99=Integer.parseInt(weekdays.get(8).substring(0,2));
												    	  		    }	
    															    else    if(weekdays.size()==8)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															    s77=Integer.parseInt(weekdays.get(6).substring(0,2));
														    		    s88=Integer.parseInt(weekdays.get(7).substring(0,2));
														    		  
												    	  		    }	
    															    else   if(weekdays.size()==7)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															    s77=Integer.parseInt(weekdays.get(6).substring(0,2));
														    		   
												    	  		    }	
    															    else  if(weekdays.size()==6)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															   
												    	  		    }	
    															 				   else   if(weekdays.size()==5)
    													
    															    		    {
    			    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    			    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
    																    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
    																    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
    																    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
															    	  		    }	
    															    		    else if(weekdays.size()==4)
    															    		    {
    															    		    	 s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    																	    		 s22=Integer.parseInt(weekdays.get(1).substring(0,2));
    																	    		 s33=Integer.parseInt(weekdays.get(2).substring(0,2));
    																	    		 s44=Integer.parseInt(weekdays.get(3).substring(0,2));
    																	        }
    															    		    else if(weekdays.size()==3)
    															    		    {
    															    		    	 s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    															    		    	 s22=Integer.parseInt(weekdays.get(1).substring(0,2));
    																	    		 s33=Integer.parseInt(weekdays.get(2).substring(0,2));
    															    		    } 
    															    		    else if(weekdays.size()==2)
    															    		    {
    															    		    	 s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    																	    		 s22=Integer.parseInt(weekdays.get(1).substring(0,2));
    																	        } 
    															    		    
    															    		    else if(weekdays.size()==1)
    															    		    {
    															    		    	  s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    															     		    }   
    															    		    
    															    		    else if(weekdays.size()==0)
    															    		    	{
    															    		    	s11=0;
    															    		    	} 
    															    		    	
 																	
																	if(c+1==d1||c+1==d2||c+1==d3 || c+1==d4 || c+1==d5 || c+1==d6 )
																	{	
																		if(c+1==s11||c+1==s22||c+1==s33||c+1==s44||c+1==s55||c+1==s66||c+1==s77||c+1==s88||c+1==s99||c+1==s00)
																		{if(status.equals("saved") && !roleId.equals("1")||status.equals("approved")  && !roleId.equals("1") ||status.equals("rejected") && !roleId.equals("1")  || status.equals("pending")  && !roleId.equals("1")  ) {%>	
																		<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEmpno()%>" class="_<%=ab.getEmpno()%>" maxlength="2" value="WO"  readonly="readonly"	
																	 	disabled="disabled"  onfocus="this.select()" title="Date=<%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; "  onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		<%
																		}else{%>
																				<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>"  name="_<%=ab.getEmpno()%>"  class="_<%=ab.getEmpno()%>" maxlength="2" value="WO"  readonly="readonly"	
																	 	disabled="disabled" onfocus="this.select()" title="Date=<%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; "  onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		<%}
																	}
																		else{
												
																			if(check)
																			
																			{  if(status.equals("saved")  && !roleId.equals("1") ||status.equals("approved") && !roleId.equals("1")  || status.equals("rejected")  && !roleId.equals("1") || status.equals("pending")  && !roleId.equals("1")  ){
																				%>	
																				<td style="width: 4%;" align="center">
																				<input type="text" size="4"  id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEmpno()%>" class="_<%=ab.getEmpno()%>" maxlength="2" value="HD" readonly="readonly"	
																			 	disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																				</td>
																				<%
																			}
																			else{%>
																				<td style="width: 4%;" align="center">
																				<input type="text" size="4"  id="<%=z++%>" name="_<%=ab.getEmpno()%>" class="_<%=ab.getEmpno()%>" maxlength="2" value="HD" readonly="readonly"	
																			 	disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase;background-color: ;  " onkeypress="return inputLimiter(event,'Numbers')">
																				</td>
																			
																			<%}
																			}
																			else{if(status.equals("saved")  && !roleId.equals("1")  ||status.equals("approved") && !roleId.equals("1")  ||status.equals("rejected")  && !roleId.equals("1")   || status.equals("pending")  && !roleId.equals("1")  ){
																		%>
																
																		<td style="width: 4%;" style="width: 4%;" align="center">
																		
																		<input type="text" size="4" id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEmpno()%>" class="_<%=ab.getEmpno()%>" maxlength="1"  value="<%=ab.getVal() ==null ?"P":ab.getVal()%>"
																	  onfocus="this.select()" disabled="disabled"  title="Date=   <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																	
																		<%	}
																			else{%>
																				<td style="width: 4%;" style="width: 4%;" align="center">
																		
																		<input type="text" size="4" id="<%=z++%>"  name="_<%=ab.getEmpno()%>" maxlength="1"  class="_<%=ab.getEmpno()%>" value="<%=ab.getVal() ==null ?"P":ab.getVal()%>"
																	  onfocus="this.select()" disabled="disabled"  title="Date=   <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		<%	}
																			}
																		}
																	}
																	else
																	{
																		if(c+1==s11||c+1==s22||c+1==s33||c+1==s44||c+1==s55||c+1==s66||c+1==s77||c+1==s88||c+1==s99||c+1==s00)
																		{if(status.equals("saved")  && !roleId.equals("1")  || status.equals("approved") && !roleId.equals("1") ||status.equals("rejected")  && !roleId.equals("1")   || status.equals("pending")  && !roleId.equals("1")   ) {%>	
																		<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEmpno()%>" class="_<%=ab.getEmpno()%>" maxlength="2" value="WO"
																	 readonly="readonly"	 disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		<%
																		
																		}else{%>
																		<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>" name="_<%=ab.getEmpno()%>"class="_<%=ab.getEmpno()%>" maxlength="2" value="WO"
																	 readonly="readonly"	 disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		
																		<%}
																		
																		}
																		else{
																			if(check)
																			{	if(status.equals("saved")  && !roleId.equals("1")  ||status.equals("approved")  && !roleId.equals("1")  ||status.equals("rejected")  && !roleId.equals("1")  || status.equals("pending") && !roleId.equals("1")  ){
																				%>	
																				<td style="width: 4%;" align="center">
																				<input type="text" size="4"  id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEmpno()%>" class="_<%=ab.getEmpno()%>" maxlength="2" value="HD"
																			 readonly="readonly"		disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black;text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																				</td>
																				<%
																			}else{%>
																				<td style="width: 4%;" align="center">
																				<input type="text" size="4"  id="<%=z++%>" name="_<%=ab.getEmpno()%>" class="_<%=ab.getEmpno()%>" maxlength="2" value="HD"
																			 readonly="readonly"		disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black;text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																				</td>
																			
																			<%}
																			
																			
																			}
																			else{
																				if(status.equals("saved")  && !roleId.equals("1")  ||status.equals("approved")  && !roleId.equals("1")  ||status.equals("rejected")  && !roleId.equals("1")   || status.equals("pending")  && !roleId.equals("1")   ){
																		%>
																		
																		<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>"  name="<%=n++%>" class="_<%=ab.getEmpno()%>" id="_<%=ab.getEmpno()%>" maxlength="1" value="<%=ab.getVal()==null?"P":ab.getVal()%>"
																	 disabled="disabled" onfocus="this.select()" title="Date=   <%=c+1%>" style="width: 80%;color:black;text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		
																		<%
																		}else{%>
																			
																				<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>"  name="_<%=ab.getEmpno()%>" class="_<%=ab.getEmpno()%>" maxlength="1" value="<%=ab.getVal()==null?"P":ab.getVal()%>"
																	 disabled="disabled" onfocus="this.select()" title="Date=   <%=c+1%>" style="width: 80%;color:black;text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																			
																		<%}
																				
																			
																			}
																		}
																	} %>
																	<%
																}
																%>
																</tr>
															<%	
															srno++;
															}}
																else{%>
																<br><br>
																
																<tr><font size="4" color="red"> No record found ...!!</font></tr>
																<br>
																<%}
															%>
														</table>
														
														</div>
														</br>
													
														
													
														</div>
														
														</center>
														<%
												}
														%>
															<%if(status.equalsIgnoreCase("saved") || status.equalsIgnoreCase("rejected") ){ %>
															<!-- 	<h3>Send For Approval : 
																<input type=button id="checkAll" value="Check All" onClick="this.value=check5(this.form.list)"> 
															<input type="submit" id="tr" value="Transfer"   disabled="disabled" onclick="form.action='EmpAttendServlet?action=transfer';"/>
															 <input type="submit" id="Left" value="Left"   onclick="form.action='EmpAttendServlet?action=left';" disabled="disabled" />
              												<input type="submit" id="apr" value="Single Approve"   disabled="disabled" onclick="form.action='EmpAttendServlet?action=SingleApprove';"/>
              												
              												
              												<input type="button"  id="send" value="End Of Month"/>		</h3> -->
    														<%} 
														
														if(status.equalsIgnoreCase("pending") && !roleId.equals("1")){ %>
													<font style="color: olive; size: 3;"><b>Already sent for approval</b></font>
														<%} 
														
														if(status.equalsIgnoreCase("approved")){ %>
													<font style="color: green; size: 3;"><b>Successfully Approved</b></font>
														<%} %>
														</div>
														</center>
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
														
									  </form> 
																

									<input type="hidden" name="flag1" id="flag1" value="<%=flag%>">
										</center>
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
