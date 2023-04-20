 <%@page import="payroll.Core.Calculate"%>
<%@page import="payroll.Core.ReportDAO"%>
<%@page import="payroll.DAO.*"%>
<%@page import="payroll.Model.*"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java"
	import="java.sql.*" errorPage="error.jsp"  isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Yearly Bonus Details </title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />

<script src="js/filter.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>

<script type="text/javascript">

<%  
BonusHandler bonus = new BonusHandler();
ArrayList<TranBean> BonusList = new ArrayList<TranBean>();
BonusList=bonus.getBonusList();


%>


var checkflag="false";
function check5(field) {
	
	
	
  if (checkflag == "false") {
	  for (i = 0; field.length > i; i++) {
      field[i].checked = true;
    }
    checkflag = "true";
    return "Uncheck All";
  } else {
    for (i = 0; field.length > i; i++) {
      field[i].checked = false;
    }
    checkflag = "false";
    return "Check All";
  }
}




function validate(){
	var date = document.getElementById("date").value;
	if(date==0){
		alert("please select the Date !");
		document.getElementById("date").focus();
		return false;
	}
	else
		{
		document.getElementById("act").value="active";
		}
}

function validate2(){
	var date = document.getElementById("date").value;
	if(date==0){
		alert("Please Select The Date !");
		document.getElementById("date").focus();
		return false;
	}
	else
		{
		document.getElementById("act").value="nonactive";
		
		return true;
		}
}

function validate1(){
	
	var date = document.getElementById("date").value;
	if(date==0){
		alert("Please Select The Date !");
		document.getElementById("date").focus();
		return false;
	}
	
	var date1 = document.getElementById("date1").value;
	if(date1==0){
		alert("Please Select The Month For Applicable Bonus !");
		document.getElementById("date").focus();
		return false;
	}
	else{
		 var a=confirm("Are you sure");
		 
		 if(a==true){
			 
			 var b=prompt("Type YES for active And NO for Nonactive Employee(s)");	
				if(b == "yes" || b == "Yes" || b == "YES"){
					
					 document.getElementById("applicable").value="Y";	
					 document.getElementById("act").value="active";
				

				} else {
					document.getElementById("applicable").value="Y";	
					 document.getElementById("act").value="nonactive";
				}
			
	}
	}
}


/*  function postBonus() {
	
	 
	 var a=confirm("Are you sure");
	 
	 if(a==true){
		 
		 var b=prompt("Type YES For PostingThe Bonus");	
			if(b == "yes" || b == "Yes" || b == "YES"){
				
				

				window.location.href="BonusServlet?action=postBonus" ;

			} else {
				
			}
		
}
	 
} */
 
jQuery(function() {
$("#post").click(function(){

	var result=  updateCount();
 
	 	 function updateCount () {
	 		   var count = $("input[type=checkbox]:checked").size();
	  			   if(count==0){
	    							alert("At least select one employee");
	    				 			return false;
	     					}	else{
	    	 						return true;
	   								  }
						};
			if(result==true)
			var r = confirm("Are you sure to Post Bonus of these employee ?");
 
			if (r == true && result==true) {
	
	 	 		var b=prompt("Type YES for Posting the Bonus to Employee(s)");	
		
	 	 		if(b == "yes" || b == "Yes" || b == "YES"){
							return true;
							} 
							else{
								return false;
								}
				} 
else{
	return false;
}

});
 
 
});
 function checkBonus() {
		
	
	
	var f = parseInt(document.getElementById("flag").value);


	if (f == 2) {
		alert("Bonus Posted Successfully");
	
	}
	if(f==3){
		alert("Bonus  is not posted. Please apply again");
		
		
	}
	
	if(f==4){
		alert("Bonus  is Given to employees. Go for post and calculate salary");
		
		
	}

}
</script>
<%
    ArrayList <String> bonusMonths = new Sal_DetailsHandler().getBonusMonths(0, "all");
int eno = (Integer)session.getAttribute("EMPNO");
%>
 </head>
<body onload="checkBonus()" style="overflow: hidden;"> 
<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Bonus Report</h1>
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
			<!-- <h2>Profession Tax Statement</h2> -->
			<table><tr><td>	
<form name="bonus" action="ReportServlet" method="post">
				<table border="1" id="customers">
	<tr>
		<th>Bonus Report</th>
		<tr>
			<tr class="alt">
				<td height="120" align="center">
				<table>
					<tr class="alt">
					
						<td>Select Date</td>
					
														<td width="40" colspan="2">
									<select name="date" id="date" width="40">
									<option value="0">Select</option>
									<%
									String[] yyyy=ReportDAO.getSysDate().split("-");
									for (int yy=2010;yy<=Integer.parseInt(yyyy[2]);yy++)
									{
									%>
									<option value='<%=yy%>'><%=yy%></option>
									<%}%></select>
									</td>
                                   <input type="hidden" id="action" name="action" value="getBonusReport"></input></td>

					</tr>
					<tr>
								<td>Select Format</td>
									<td colspan="2"> <input type="radio" name="reporttype" value="pdf" checked="checked">Pdf File
									&nbsp;&nbsp;&nbsp;<input type="radio" name="reporttype" value="excel" >&nbsp;Excel </td>
								</tr>
					 <tr>
					 <td colspan="3">&nbsp;</td>
					 </tr>
					<tr>
						<td colspan="2" align="center">
						
						<input type="hidden" name="act" id="act" value=""  />
							<input type="submit" name="active" value="Active Emp list" onclick="return  validate()" /></td>
						<td>	<input type="submit" name="nonactive" value="Non-Active Emp list"  onclick="return  validate2()"/>							</td>	
					</tr>
				</table>

			  </td>
			</tr>
</table>


</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
		<table border="1" id="customers">
	<tr>
		<th>Bonus Applicable month</th>
		
			<tr class="alt">		<td> select Month: 
					<select name="date1" id="date1">
						<option value="">Select</option>
						<%
							for(String month:bonusMonths)
							{
						%>
						
							<option value="<%=month%>"><%=month%></option>
						<%
							}
						%>
						</select> 
						</td>
						</tr>
				<tr class="alt">
				
				<td><center><input type="hidden" name="applicable" id="applicable" value="S"  />
				<input type="submit" name="month" value="submit" onclick="return  validate1()" /> </center></td>
				</table>
			</form>
			</td></tr></table>
			</center>
			
			
			<br><br>
			<center>
			<form action="BonusServlet?action=postBonus" method="post">
			 <div align="center" class="imptable" style="overflow-y:hidden;  width: 100%;">
			<table id="customers" style="width: 70%">
			<tr style="position:static;"align="center" bgcolor="#2f747e">
			
			<td width="90" style="color: white;">SR NO</td> 
			<td width="90" style="color: white;">EMPCODE</td>
			<td width="180" style="color: white;">EMP NAME</td>
			<td width="90" style="color: white;">FOR MONTH</td>
			<td width="90" style="color: white;">BONUS AMOUNT</td>
			<td width="190" style="color: white;">POSTED BY</td>
			<td width="90" style="color: white;">POSTED DATE</td></tr>
		
			</table>
			</div>
			 <div align="center" class="imptable" style="overflow-y:auto; height:200px; width: 100%;">
			
			
			<%
			int flag=-1;
			flag=Integer.parseInt(request.getParameter("flag")==null?"-1":request.getParameter("flag"));
			int Srno=1;
			EmployeeHandler emph = new EmployeeHandler();
			EmployeeBean ebean = new EmployeeBean();
			LookupHandler lookuph= new LookupHandler();
			if(BonusList.size()!=0){ 
				%>
				<table style="margin-left: 16px ; width: 70%;" id="customers" >
				<%
			for(TranBean tb : BonusList){
				
				ebean = emph.getEmployeeInformation(Integer.toString(tb.getEMPNO()));
			%>
			
			
			<tr style="position:static;"align="center">
					
			
			<td width="90" > <input type="checkbox" id="list" value="<%=tb.getEMPNO()%>" name="list" onclick="enableButton()">&nbsp;<%=Srno%> </td>
			<td width="90"><%=ebean.getEMPCODE() %></td>
			<td width="180"><%=lookuph.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getLNAME() %> </td>
			<td width="90"> <%= tb.getTRNDT()%></td>
			<td width="90"> <%=tb.getNET_AMT() %></td>
			<%ebean = emph.getEmployeeInformation((tb.getUSRCODE())); %>
			<td width="190"> <%=lookuph.getLKP_Desc("SALUTE", ebean.getSALUTE())+" "+ ebean.getFNAME()+" "+ebean.getLNAME()%></td>
			<td width="90"> <%=tb.getUPDDT() %></td>
			
			
			</tr>
			<%
				Srno++;
				}
			
			}else{ %>
			<table style=" width: 70%;" id="customers" >
			<tr ><td colspan="6" align="center"><font color="red">No Pending Bonus</font>  </td></tr>
			<%} %>
			
			</table>
			</div>
			<br><br>
			<%if(BonusList.size()!=0){ %>
			<table style="width: 40%">
			<tr>
			<td align="center"><input type=button id="checkAll" value="Check All" onClick="this.value=check5(this.form.list)"></td> 
			<td align="center"><input type="submit" id="post" value="Post Bonus"></td>
				
			
			</tr>			
			</table>
			<%} %>
			</form>
			</center>
			
			</div>
			<!--  end table-content  -->
	<input type="hidden" name="flag" id="flag" value="<%=flag%>">
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
