<%@page import="payroll.DAO.LookupHandler"%>
<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="payroll.DAO.EmployeeHandler"%>
<%@page import="payroll.Model.LoanAppBean"%>
<%@page import="payroll.DAO.LoanAppHandler"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.jfree.ui.about.ProjectInfo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page import="java.awt.Desktop.Action"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
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

<script src="js/jquery/jquery.autocomplete.js"></script>


 <%

	LoanAppHandler loanhandler=new LoanAppHandler();
	String Empno=request.getParameter("empno")==null?"":request.getParameter("empno");
	String ApplicationNo=request.getParameter("appNo")==null?"":request.getParameter("appNo");
	
	//System.out.println("what the hell "+ApplicationNo);
	
	
	

	LoanAppBean loanAppBean= new LoanAppBean();
	HashMap<String,Float> hashMap= new HashMap<String,Float>();
	
	loanAppBean=loanhandler.getLoanInfo(Empno,ApplicationNo);
	String fromDate="";
	String toDate="";
	fromDate=loanAppBean.getStart_date();
	if(loanAppBean.getACTIVE().equalsIgnoreCase("SANCTION") || 
	loanAppBean.getACTIVE().equalsIgnoreCase("NIL") )
	{
	if(loanAppBean.getACTIVE().equalsIgnoreCase("SANCTION")){
		toDate=loanAppBean.getEnd_date();
	}
	else if(loanAppBean.getACTIVE().equalsIgnoreCase("NIL")){
		toDate=loanAppBean.getLoanEndDate();
	}
	hashMap=loanhandler.getTransactions(Empno,fromDate,toDate,loanAppBean.getLoan_code());	
//	System.out.println(getTransactionsList.size());
	}
	session.setAttribute("loanAppBean", loanAppBean);

	%>
	
	
	
<script type="text/javascript" >


</script>


</head>
<body  >
	<%@include file="mainHeader.jsp"%>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp"%>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Loan Info</h1>
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
							<div id="table-content"  class="imptable"style="height: 400px; overflow: auto;">
							<center>
						       <a href="LoanAppMaster.jsp">Click Here To Go On Sanction Page</a>
								<table id="customers" >
								<tr ><th colspan="6"> Loan Details</th> </tr>
								<%
								EmployeeHandler emph = new EmployeeHandler();
								EmployeeBean ebean = new EmployeeBean();
								EmployeeBean ebean1 = new EmployeeBean();
								LookupHandler lookuph= new LookupHandler();
								
								
								ebean = emph.getEmployeeInformation(Integer.toString(loanAppBean.getEMPNO()));
								
								if(loanAppBean.getACTIVE().equalsIgnoreCase("Sanction") ||loanAppBean.getACTIVE().equalsIgnoreCase("Cancel") ||loanAppBean.getACTIVE().equalsIgnoreCase("nil") ){
								ebean1 = emph.getEmployeeInformation(loanAppBean.getSanctionby());
								}
								%>
								<tr>  <td>Empcode :</td> <td> <%=ebean.getEMPCODE()%></td> <td> Employee Name :</td> <td><%=lookuph.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getLNAME() %></td>   
								  <td>Loan No :</td> <td><%=loanAppBean.getLoan_no() %> </td>
								  </tr><tr>
								   <td> Loan Amount :</td> <td><%=loanAppBean.getLoan_amt() %> </td>   
								 <td>Start Date :</td> <td><%=loanAppBean.getStart_date()%> </td> <td> End Date :</td> <td> <%=loanAppBean.getEnd_date()%></td>   </tr>
								<tr>  <td>Monthly Installment :</td> <td> <%=loanAppBean.getMonthly_install() %></td> <td> Loan Percentage:</td> <td><%=loanAppBean.getLoan_per() %></td>  
								
								 <td>Guarantor Names :</td> <td><%=loanAppBean.getBank_name()%> </td> 
								</tr><tr>
								<td> Sanction By:</td>
								
								<% 	if(loanAppBean.getACTIVE().equalsIgnoreCase("Sanction")||loanAppBean.getACTIVE().equalsIgnoreCase("Cancel")||loanAppBean.getACTIVE().equalsIgnoreCase("nil")){%>
								 <td><%=(ebean1.getFNAME()==null)?"":lookuph.getLKP_Desc("SALUTE", ebean1.getSALUTE())+" "+ ebean1.getFNAME()+" "+ebean1.getLNAME() %> </td>   
								<%} else{%>
								<td><%=loanAppBean.getSanctionby() %></td>
								<%} %>
							
								  <td>Sanction Date :</td> <td><%=loanAppBean.getSanctiondate() %> </td> <td> Status:</td> <td><%=loanAppBean.getACTIVE() %></td>   </tr>
								<tr>  <td>Loan Code :</td> <td> <%=loanAppBean.getLoan_code() %></td> <td> Total Installments:</td> <td><%=loanAppBean.getTotal_month() %></td>   
								 <td>Actual Pay :</td> <td><%=loanAppBean.getActual_pay() %> </td> </tr><tr><td>Total Paid :</td> <td><%=loanAppBean.getTotal_paid() %></td>   
								
								  <td>Remaining Amount:</td> <td><%=loanAppBean.getRemaingingAmt() %> </td> 
								<%if (loanAppBean.getACTIVE().equalsIgnoreCase("nil")  ){%>
								<td>Loan ended On :</td> 
								<td><%=loanAppBean.getLoanEndDate()%></td>   </tr>
								<% }%>
								
							
								</table>
							</center>
					<%if (loanAppBean.getACTIVE().equalsIgnoreCase("nil") || 
							loanAppBean.getACTIVE().equalsIgnoreCase("sanction")){%>		
							<center>
							<table id="customers" >
				<tr><th colspan="4"> Loan Installments</th> </tr>
					<tr> 
					<th>Month</th>
					<th>Amount</th>
					</tr>
					<% Set keys = hashMap.keySet();
        Iterator itr = keys.iterator();
 
        String key;
        float value;
        while(itr.hasNext())
        {
            key = (String)itr.next();
            value = hashMap.get(key);
            %>
					<tr> 
					<td><%=key %></td>
					<td><%=value%></td>
					
					
					</tr>
					<%} %>		
							</table>
						</center>
							<% }%>
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
								
