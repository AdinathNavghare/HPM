<%@page import="payroll.Model.CompBean"%>
<%@page import="payroll.DAO.EmpOffHandler"%>
<%@page import="payroll.Model.EmpOffBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="payroll.Core.ReportDAO"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy Assign Week Off </title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script type="text/javascript">
jQuery(function() {
	$("#EMPNO").autocomplete("list.jsp");
});  
</script>

<script>
jQuery(function() {
	$("#EMPNO1").autocomplete("list.jsp");
});
</script>


<script>

jQuery(function() {
	$("#pp").autocomplete("projlist.jsp");
	
	$("#sat").click(function(){
		document.getElementById('rowSubOrders').style.display = (this.checked) ? "none":"";
	});
	
});






function validate(){
	
	
	
	

	var result=  updateCount();
	 
	  function updateCount () {
	    var count = $("input[type=checkbox]:checked").size();
	     if(count==0){
	    	alert("At least select one Day..!");
	    	 return false;
	     }else{
	    	 return true;
	     }
	};
	

	
	
	if(result==true)
		var r = confirm("Are you sure to assign the week off to this sites ?");
		 
		if (r == true && result==true) {
			return true;
	
		} else{
			return false;
		}
}




function KeepCount() {
			
	
	
	
	var NewCount = 0;
	
	if (document.assign_site.sun.checked)
	{NewCount = NewCount + 1;}

	if (document.assign_site.mon.checked)
	{NewCount = NewCount + 1;}

	if (document.assign_site.tue.checked)
	{NewCount = NewCount + 1;}

	if (document.assign_site.wed.checked)
	{NewCount = NewCount + 1;}

	if (document.assign_site.thu.checked)
	{NewCount = NewCount + 1;}
		
	if (document.assign_site.fri.checked)
	{NewCount = NewCount + 1;}

	if (document.assign_site.sat.checked)
	{
	NewCount = NewCount + 1;
	
	}
	if ((NewCount == 3) && (document.assign_site.sat.checked==true))
	{
		
		alert('Pick Just Two Please...!');
		document.assign_site.sat.checked==false;
		document.getElementById('rowSubOrders').style.display = (this.checked) ? "":"";	
		return false;
	}
	
	else if(NewCount==3){
		alert('Pick Just Two Please');
		return false;
	}
	
	
	 document.forms["assign_site"]["offday"].focus(); return false; 
} 

function checkSite() {
	
	 
	
	var f = parseInt(document.getElementById("flag1").value);

	if (f == 1) {
		alert("Week off Assigned Successfully");

	}

	if(f==4){
		alert("Weekoff is not assigned. Please try again");
		}

}


var expanded = false;
function showCheckboxes() {
    var checkboxes = document.getElementById("checkboxes");
    if (!expanded) {
        checkboxes.style.display = "block";
        expanded = true;
    } else {
        checkboxes.style.display = "none";
        expanded = false;
    }
}


function getassign() {
	
	
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
window.location.href="EmpAttendServlet?action=getAssignWeek&prj="+prjCode+"&proj="+proj;

	}
		}
	}
}

<%int flag=-1;
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
		}%>
</script>
</head>
<body  onLoad="checkSite()" > 
 


<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:auto; max-height:83%; ">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Week OFF Assign </h1>
	</div>
	<!-- end page-heading -->

	<table border="0" width="100%" style="height: 550px;"cellpadding="0" cellspacing="0" id="content-table">
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
			
			
			<form name="assign_site" action="EmpAttendServlet?action=getWeek" method="post" onsubmit="return validate()">
			
		<table  id="customers">

					<tr style="height: 30px;"  class="alt">

						<th colspan="4"><font size="3">Week Off Assign</font></th>
					
		</tr>
					
					<%
					String action= request.getParameter("action")==null?"":request.getParameter("action");
					String project="";
					if(action.equalsIgnoreCase("display"))
					{ 
						 project=request.getParameter("proj");
					}
					
					%>
			
		
			
			<tr style="height: 30px;"  class="alt">
												<td ><font size="3">Project :</font></td>
												<td colspan="3" style="width: 550px;"><input type="text" id="pp" name="pp"
												 value="<%=project%>" 
												
												style="height: 20px; width: 80%;"  onClick="this.select();" value="<%=request.getParameter("proj")==null?"":request.getParameter("proj").replaceAll(" and ", " & ")%>" title="Enter a character to view the available project list (E.g - %) ">
												 <input type="Button" value="Get Week Off" title="To change Employees Of particular Project Click here" onclick="getassign()" />
										</td>		 </tr>
			
				<%		
					ArrayList <CompBean>weekList=new ArrayList<CompBean>();
					
					boolean sun= false; boolean mon= false; boolean tue= false;
					boolean wed= false; boolean thu= false; boolean fri= false;
					boolean satur= false;
					boolean alt1= false;boolean alt2= false; boolean alt3= false;
					boolean alt4= false; boolean alt5= false;
					String sunday = ""; 	String monday = ""; 	String tuesday = "";
					String wednesday = "";  String thursday = ""; 	String friday = "";
					String Saturday ="";
					String sat1 =""; String sat2 =""; String sat3 =""; String sat4 ="";
					String sat5 ="";
    				if(action.equalsIgnoreCase("display"))
    						{	
    					
    					/* if(request.getAttribute("AssignWeek") != null) */
    						weekList=(ArrayList <CompBean>)request.getAttribute("AssignWeek");
    					System.out.println("The size of weeklist     :"+weekList.size());
    					for( CompBean cb  : weekList){
    						
    						if(cb.getWoffday().equals("sun"))
    						{
    							sunday=cb.getWoffday();
    							sun=true;
    						}
    						
    						if(cb.getWoffday().equals("mon"))
    						{
    							monday=cb.getWoffday();
    							mon=true;
    						}
    						
    						if(cb.getWoffday().equals("tue"))
    						{
    							tuesday=cb.getWoffday();
    							tue=true;
    						}
    						
    						if(cb.getWoffday().equals("wed"))
    						{
    							wednesday=cb.getWoffday();
    							wed=true;
    						}
    						
    						if(cb.getWoffday().equals("thu"))
    						{
    							thursday=cb.getWoffday();
    							thu=true;
    						}
    						if(cb.getWoffday().equals("fri"))
    						{
    							friday=cb.getWoffday();
    							fri=true;
    						}
    						if(cb.getWoffday().equals("sat"))
    						{
    							Saturday=cb.getWoffday();
    							satur=true;
    						}
    						
    						if(cb.getAltsat().equals("1"))
    						{
    							sat1=cb.getAltsat();
    							alt1= true;
    						}
    						if(cb.getAltsat().equals("2"))
    						{
    							sat2=cb.getAltsat();
    							alt2= true;
    						}
    						if(cb.getAltsat().equals("3"))
    						{
    							sat3=cb.getAltsat();
    							alt3= true;
    						}
    						if(cb.getAltsat().equals("4"))
    						{
    							sat4=cb.getAltsat();
    							alt4= true;
    						}
    						if(cb.getAltsat().equals("5"))
    						{
    							sat5=cb.getAltsat();
    							alt5= true;
    						}
    					
    					}
    					
    						
    					
    					    	%>
						
			<tr style="height: 30px;"  class="alt"><td><font size="3">Select Week Off Day :</font><br /><span style="font-size:75%">(At max 2)</span></td>
							<td colspan="3">
				<%if(sun && sunday.equals("sun")){ %>
							<input type="checkbox" checked="checked" name="offday" id="sun" value="sun" onClick="return KeepCount()"/>&nbsp;&nbsp;<font size="3">Sunday</font>
				<%}else { %>
							<input type="checkbox" name="offday" id="sun" value="sun" onClick="return KeepCount()"/>&nbsp;&nbsp;<font size="3">Sunday</font>
				<%} %>	
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%if(mon && monday.equals("mon")){ %>						
							<input type="checkbox" checked="checked" name="offday" id="mon" value="mon" onClick="return KeepCount()" />&nbsp;&nbsp;<font size="3">Monday</font>
				<%}else { %>	
							<input type="checkbox" name="offday" id="mon" value="mon" onClick="return KeepCount()" />&nbsp;&nbsp;<font size="3">Monday</font>		
				<%} %>				
											&nbsp;&nbsp;
				<%if(tue && tuesday.equals("tue")){ %>	
						<input type="checkbox"  checked="checked" name="offday" id="tue" value="tue" onClick="return KeepCount()" />&nbsp;&nbsp;<font size="3">Tuesday</font>
				<%}else { %>
						<input type="checkbox" name="offday" id="tue" value="tue" onClick="return KeepCount()" />&nbsp;&nbsp;<font size="3">Tuesday</font>
					
				<%} %>
											&nbsp;&nbsp;
				<%if(wed && wednesday.equals("wed")){ %>	
							<input type="checkbox" checked="checked" name="offday" id="wed" value="wed" onClick="return KeepCount()"/>&nbsp;&nbsp;<font size="3">Wednesday<br></font>
				<%}else { %>
							<input type="checkbox" name="offday" id="wed" value="wed" onClick="return KeepCount()"/>&nbsp;&nbsp;<font size="3">Wednesday<br></font>
									
				<%} %>			
				<%if(thu && thursday.equals("thu")){ %>			
							<input type="checkbox" checked="checked" name="offday" id="thu" value="thu" onClick="return KeepCount()" />&nbsp;&nbsp;<font size="3">Thursday</font>
				<%}else { %>
							<input type="checkbox" name="offday" id="thu" value="thu" onClick="return KeepCount()" />&nbsp;&nbsp;<font size="3">Thursday</font>
				<%} %>							
											&nbsp;&nbsp;
				<%if(fri && friday.equals("fri")){ %>		
					<input type="checkbox" checked="checked" name="offday" id="fri" value="fri" onClick="return KeepCount()" />&nbsp;&nbsp;<font size="3">Friday</font>
				<%}else { %>							
					<input type="checkbox" name="offday" id="fri" value="fri" onClick="return KeepCount()" />&nbsp;&nbsp;<font size="3">Friday</font>
				<%} %>								
											&nbsp;&nbsp;&nbsp;&nbsp;
				<%if(satur && Saturday.equals("sat")){ %>								
				<input type="checkbox"  checked="checked" name="offday" id="sat"value="sat" onclick="return KeepCount()">&nbsp;&nbsp;<font size="3">Saturday</font>
				<%}else { %>
				<input type="checkbox" name="offday" id="sat"value="sat" onclick="return KeepCount();">&nbsp;&nbsp;<font size="3">Saturday</font>
				<%} %>
							</td>
			</tr>
			<tr style="height: 30px;" class="alt" id="rowSubOrders" > <td><font size="3">Alternate Saturday ?</font></td>
												<td colspan="3">
			<%if(alt1 && sat1.equals("1"))  {%>							
				<input type="checkbox" checked="checked" name="altsatday" id="one" value="1" />&nbsp;<font size="3">1</font> &nbsp;&nbsp;
			<%} else{ %>
				<input type="checkbox"  name="altsatday" id="one" value="1" />&nbsp;<font size="3">1</font> &nbsp;&nbsp;
			<%} if(alt2 && sat2.equals("2"))  {%>								
				<input type="checkbox" checked="checked" name="altsatday" id="two" value="2" />&nbsp;<font size="3">2</font> &nbsp;&nbsp;
			<%} else{ %>
				<input type="checkbox"  name="altsatday" id="two" value="2" />&nbsp;<font size="3">2</font> &nbsp;&nbsp;
			<%} if(alt3 && sat3.equals("3"))  {%>	
				<input type="checkbox" checked="checked" name="altsatday" id="three" value="3" />&nbsp;<font size="3">3</font> &nbsp;&nbsp;
			<%} else{ %>
				<input type="checkbox" name="altsatday" id="three" value="3" />&nbsp;<font size="3">3</font> &nbsp;&nbsp;
			<%} if(alt4 && sat4.equals("4"))  {%>		
			<input type="checkbox" checked="checked" name="altsatday" id="four" value="4" />&nbsp;<font size="3">4</font> &nbsp;&nbsp;
			<%} else{ %>
				<input type="checkbox" name="altsatday" id="four" value="4" />&nbsp;<font size="3">4</font> &nbsp;&nbsp;
			
			<%} if(alt5 && sat5.equals("5"))  {%>
				<input type="checkbox" checked="checked" name="altsatday" id="five" value="5" />&nbsp;<font size="3">5</font><br></td>
			<%} else{ %>
				<input type="checkbox"  name="altsatday" id="five" value="5" />&nbsp;<font size="3">5</font><br></td>
			
			<%} %>
			</tr>
		
		
	
						
						<tr style="height: 30px;" class="alt"><td  align="center" colspan="4"><input type="submit" value="Save" name="Save">&nbsp;&nbsp;
												<input type="reset" value="cancel">&nbsp;&nbsp;
												</td></tr>									
					<%} %>
				
		</table>
		
		</form>
</center>
<br>

			  
</div>
  <input type="hidden" name="flag1" id="flag1" value="<%=flag%>">
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