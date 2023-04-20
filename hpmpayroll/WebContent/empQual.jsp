<%@page import="payroll.Model.Lookup"%>
<%@page import="payroll.DAO.LookupHandler"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="payroll.Model.EmpQualBean"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee</title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css" media="screen" title="default" />
<script src="js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script type="text/javascript" src="js/datetimepicker_banking.js"></script>
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script src="js/DeleteRow.js"></script>
<script src="js/qualValidation.js"></script>
<script type="text/javascript">
	function Showhide()
	{
	document.getElementById("empQualEdit").hidden=true;
	document.getElementById("addempQual").hidden=false;
	}
</script>
<% 

String action=request.getParameter("action")!=null?request.getParameter("action"):"addemp";// Getting action from servlet for showing pages 
LookupHandler lh = new LookupHandler();

%>
</head>
<body style="overflow:hidden;"> 
	<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp" %>
 
<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer" style="overflow-y:scroll; max-height:70%; ">
<!-- start content -->
<% if(action.equalsIgnoreCase("addemp"))
{
		%>
	<div id="content"  ><!--  start page-heading -->
			<div id="step-holder">
			<div class="step-light-right">&nbsp;</div>
			<div class="step-no-off">1</div>
			<div class="step-light-left"><a href="employee.jsp" >Employee Detail</a></div>
			<div class="step-light-right">&nbsp;</div>
			<div class="step-no">2</div>
			<div class="step-dark-left"><a href="empQual.jsp">  Qualification </a></div>
			<div class="step-dark-right">&nbsp;</div>
			<div class="step-no-off">3</div>
			<div class="step-light-left"><a href="empAddress.jsp"> Address </a></div>
			<div class="step-light-right">&nbsp;</div>
			<div class="step-no-off">4</div>
			<div class="step-light-left"><a href="empFamily.jsp">Family </a></div>
            <div class="step-light-right">&nbsp;</div>
            <div class="step-no-off">5</div>
			<div class="step-light-left"><a href="empExper.jsp">Experience </a></div>
			<div class="step-light-right">&nbsp;</div>
			<div class="step-no-off">6</div>
			<div class="step-light-left"><a href="officialInfo.jsp">Official Info</a></div>
			<div class="step-light-right">&nbsp;</div>
		
			<div class="step-no-off">7</div>
			<div class="step-light-left"><a href="awardInfo.jsp">Award</a></div>
			<div class="step-light-right">&nbsp;</div>
			
			
			<div class="step-no-off">8</div>
			<div class="step-light-left"><a href="Attachment.jsp">Attachment</a></div>
			<div class="step-light-right">&nbsp;</div>
			
			<div class="step-no-off">9</div>
			<div class="step-light-left"><a href="otherDetail.jsp">Other Detail</a></div>
			<div class="step-light-round">&nbsp;</div>
			<div class="clear"></div>
		</div>
<%
	}
else if(action.equalsIgnoreCase("showemp"))
	{

	%>
<div id="content"  ><!--  start page-heading -->
			<div id="step-holder">
			<div class="step-light-right">&nbsp;</div>
			<div class="step-no-off">1</div>
			<div class="step-light-left"><a href="EmployeeServlet?action=employee" >Employee Detail</a></div>
			<div class="step-light-right">&nbsp;</div>
			<div class="step-no">2</div>
			<div class="step-dark-left"><a href="EmployeeServlet?action=empQual"> Qualification </a></div>
			<div class="step-dark-right">&nbsp;</div>
			<div class="step-no-off">3</div>
			<div class="step-light-left"><a href="EmployeeServlet?action=address"> Address </a></div>
			<div class="step-light-right">&nbsp;</div>
			<div class="step-no-off">4</div>
			<div class="step-light-left"><a href="EmployeeServlet?action=family">Family </a></div>
            <div class="step-light-right">&nbsp;</div>
            <div class="step-no-off">5</div>
			<div class="step-light-left"><a href="EmployeeServlet?action=experience">Experience </a></div>
			<div class="step-light-right">&nbsp;</div>
			<div class="step-no-off">6</div>
			<div class="step-light-left"><a href="EmployeeServlet?action=officialInfo">Official Info</a></div>
			<div class="step-light-right">&nbsp;</div>
		
			<div class="step-no-off">7</div>
			<div class="step-light-left"><a href="EmployeeServlet?action=awardInfo">Award</a></div>
			<div class="step-light-right">&nbsp;</div>
			
			<div class="step-no-off">8</div>
			<div class="step-light-left"><a href="Attachment.jsp?action=showemp">Attachment</a></div>
			<div class="step-light-right">&nbsp;</div>
			
			<div class="step-no-off">9</div>
			<div class="step-light-left"><a href="otherDetail.jsp?action=showemp">Other Detail</a></div>
			<div class="step-light-round">&nbsp;</div>
			<div class="clear"></div>
		</div>
<% 
	} 
%>
<div id="page-heading">
<h1>Employee Qualification Information</h1>
</div>
<!-- end page-heading -->


<%
if(action.equalsIgnoreCase("showemp"))
{
		String srNo = request.getParameter("srno")!=null?request.getParameter("srno"):"zero";
		String hidediv = request.getParameter("hidediv")!=null?request.getParameter("hidediv"):"";
		ArrayList<EmpQualBean> empQualList = (ArrayList<EmpQualBean>)session.getAttribute("empQualList");
	
%>		<form>
		<table border="0" width="auto" cellpadding="0" cellspacing="0">
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
	<form>	
		
			<table id="customers">
				<tr><td width="84" >Employee Code</td>
  		  			<td width="142" ><%=session.getAttribute("empcode")==null?"":session.getAttribute("empcode") %><input type="hidden" name="empNo" id="empNo" readonly="readonly" value="<%=session.getAttribute("empno") %>"></td>
					<td width="93" >Employee Name</td>
		  			<td width="300" ><input type="text" name="empName" id="empName" size="40" readonly="readonly" value="<%=session.getAttribute("empname") %>"></td>
		  		</tr>
			</table>
		<table width="1907" id="customers">
		<tr><th width="20">Sr.No</th>
		<th width="100">Degree</th>
		<th width="250">University</th>
		<th width="20">Percentage</th>
		<th >Grade of Passing</th>
		<th >Year Of Passing</th>
		<th width="20">Remark</th>
		<th width="100">Edit / Delete</th>
		</tr>
				<%
				if(empQualList.size()!=0)
				{
				for(EmpQualBean empQualbean :empQualList)
				{
					String str=String.valueOf(empQualbean.getSRNO());
					%>
				<tr align="center">
				<td><%=empQualbean.getSRNO() %></td>
				<td><%=lh.getLKP_Desc("ED",empQualbean.getDEGREE())%></td>
				<td><%=empQualbean.getINST() %></td>
				<td><%=empQualbean.getPERCENT() %></td>
				<td><%=empQualbean.getCLASS() %></td>
				<td><%=empQualbean.getPASSYEAR() %>
				</td><td><%=empQualbean.getREM() %></td>
				<td><input type="button" Value="Edit" onClick="window.location='empQual.jsp?action=showemp&hidediv=yes&srno=<%=empQualbean.getSRNO() %>'">
					&nbsp;<input type="button" value="Delete" onclick="deleteRow('emp','qual','<%=empQualbean.getSRNO() %>','EmployeeServlet?action=empQual')">
				</td>
				</tr>
				
				<% 
				}}
				else{
					%>
				<tr> <td colspan="8" height="30">There Is No Information</td> </tr>
					<%
				}
				%>
			<tr bgcolor="#2f747e"><td align="right" colspan="8"></td></tr>
		</table>
		
</form>
<br>

		<form name="employeeform1" id="employeeform1" action ="EmployeeServlet?action=addQualification&qual=addmore" method="post" onSubmit="return addQualvalidation()">
	<%
	if(hidediv.equalsIgnoreCase("yes"))
	{%>
	<div id="addempQual"  hidden="true" >
	<%}
	else{%>
	<div id="addempQual"  >
	<%}%>
	<h2>Add the Qualication Information of <%=session.getAttribute("empname") %> </h2>
<table width="677" id="customers">
		  <tr class="alt"><td width="111" >Employee Code</td>
	  		<td width="204" ><%=session.getAttribute("empcode")==null?"":session.getAttribute("empcode") %><input type="hidden" name="aempNo" id="aempNo" readonly="readonly" value="<%=(session.getAttribute("empno")==null?"":session.getAttribute("empno")) %>"></td>
				<td width="104" >Employee Name</td>
			<td width="215" ><input type="text" name="aempName" id="aempName" value="<%=(session.getAttribute("empname")==null?"":session.getAttribute("empname")) %>" readonly="readonly"></td></tr>
		<tr class="alt">
		<td >Degree</td>
		  <td >
		  <select name="aRDEGREE" id="aRDEGREE" >  
      					 <option value="0">Select....</option>  
    						<%
  							  ArrayList<Lookup> result3=new ArrayList<Lookup>();
    						  LookupHandler lkhp3= new LookupHandler();
    					      result3=lkhp3.getSubLKP_DESC("ED");
 							for(Lookup lkbean : result3){
     						%>
      			<option value="<%=lkbean.getLKP_SRNO()%>"><%=lkbean.getLKP_DESC()%></option>  
     					 <%}%>
     			 </select>
		  
		  </td>
		   <td>Year of passing </td>
			
			<td><input name="aPassyear" type="text" id="aPassyear" value="" maxlength="4" > 
			&nbsp;'YYYY'</td>
		  </tr>
	   <tr class="alt">	<td>University</td>
	     <td><input name="aUniName" type="text" id="aUniName" maxlength="40" ></td>
			<td>Percentage</td>
			<td><input name="aPercent" type="text" id="aPercent" maxlength="5" >
			&nbsp;%</td></tr>
		<tr class="alt">
		  <td>Grade of Passing</td>
		  <td><input name="aGrade" type="text" id="aGrade" value="" maxlength="20" ></td>
		   <td>Remark</td>
		  <td><input name="aRemark" type="text" id="aRemark" value="" maxlength="20" ></td>
		</tr>
		<tr>
		 
		</tr>
		<tr class="alt"><td colspan="4" align="center"><input type="submit" value="Submit"  /> 
		&nbsp; &nbsp;
		<input type="reset" value="Cancel" /></td></tr>
    </table>
    </div>
<div class="clear">&nbsp;</div>
</form>	

<%

for(EmpQualBean empQualbean :empQualList)
{
	
	if(srNo.equalsIgnoreCase(String.valueOf(empQualbean.getSRNO())))
	{
	
%>
<form name="employeeform" id="employeeform" action="EmployeeServlet?action=editempQual" method="post" onSubmit="return editQualvalidation()">


<div id="empQualEdit" >

<h2>Edit The Information of <%=session.getAttribute("empname") %> </h2>
<table width="677" id="customers">
		   <tr class="alt"><td width="120" >Employee Code</td>
	  		<td width="205" > <%=session.getAttribute("empcode")==null?"":session.getAttribute("empcode") %><input type="hidden" name="empNo" id="empNo" readonly="readonly" value="<%=session.getAttribute("empno") %>"></td>
				<td width="117" >Employee Name</td>
			<td width="215" ><input type="text" name="empName" readonly="readonly" id="empName" value="<%=session.getAttribute("empname") %>"></td></tr>
		<tr class="alt"><td>Sr No.</td><td><input type="text" name="srNo" readonly="readonly" id="srNo" value="<%=empQualbean.getSRNO() %>"></td>
		<td >Degree</td>
		  <td >
		  		<select name="EDEGREE" id="EDEGREE" >  
      					 <option value="none">Select</option>  
    						<%
    						
  							  ArrayList<Lookup> result2=new ArrayList<Lookup>();
    						LookupHandler lkhp2 = new LookupHandler();
    						result2=lkhp2.getSubLKP_DESC("ED");
    					
 							
    						for(Lookup lkbean : result2)
 							{
 									
    						
     						if(lkbean.getLKP_SRNO()== empQualbean.getDEGREE())
     						{
     							
     						%>
     							<option value="<%=lkbean.getLKP_SRNO()%>" selected="selected"><%=lkbean.getLKP_DESC()%></option>  
     							<%}
     						else {
     								%>
      						<option value="<%=lkbean.getLKP_SRNO()%>"><%=lkbean.getLKP_DESC()%></option>  
     					 		<%}
     					 		}%>
     			 </select>
		  
		  
		  
		  </td></tr>
	   <tr class="alt">	<td>University</td>
	     <td><input name="eUniversity" type="text" id="eUniversity" value="<%=empQualbean.getINST() %>" maxlength="40" ></td>
			
			
			
			<td>Year of passing </td>
			
			<td><input name="ePassyear" type="text" id="ePassyear" value="<%=empQualbean.getPASSYEAR() %>" maxlength="4"> 
			'YYYY'</td>
			
			</tr>
		<tr class="alt">
		<td class="alt">Percentage</td>
			<td><input name="ePercent" type="text" id="ePercent" value="<%=empQualbean.getPERCENT() %>" maxlength="5">
&nbsp; %</td>
		    <td>Grade of Passing</td>
		  <td><input name="eGrade" type="text" id="eGrade" value="<%=empQualbean.getCLASS() %>" maxlength="20"> </td>
		  
		</tr>
		<tr class="alt">
		  <td>Remark</td>
		  <td colspan="3"><input name="eRemark" type="text" id="eRemark" value="<%=empQualbean.getREM() %>" maxlength="20"></td>
		</tr>
		<tr class="alt"><td colspan="4" align="center"><input type="submit" value="Update" /> &nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onClick="Showhide()"/></td></tr>
    </table>
    
    </div>
	
   </form>
	<div class="clear">&nbsp;</div>
  <%
	}
	
}      //end of for loop
	if(srNo.equalsIgnoreCase("addinfo"))
	{	
%><br>


<%
	}
}
else if(action.equalsIgnoreCase("addemp"))
{
	ArrayList<EmpQualBean> empQualList2 = (ArrayList<EmpQualBean>)session.getAttribute("empQualList");
	String session_empno=(String )session.getAttribute("empno");
%>
<form name="employeeform" id="employeeform" action ="EmployeeServlet?action=addQualification" method="post" onSubmit="return addQualvalidation()">
		<table border="0" width="auto" cellpadding="0" cellspacing="0" >
	
<%	if(session_empno!= null)
	{
	%>
	
	<%
	
	}%>
	<tr>
		<th rowspan="3" class="sized"><img
			src="images/shared/side_shadowleft.jpg" width="15" height="200"
			alt="" /></th>
		<th class="topleft"></th>
		<td id="tbl-border-top">&nbsp;</td>
		<th class="topright"></th>
		<th rowspan="3" class="sized"><img
			src="images/shared/side_shadowright.jpg" width="15" height="200"
			alt="" /></th>
	</tr>
	<tr>
		<td id="tbl-border-left"></td>
		<td>
<form name="employeeform" id="employeeform" action ="EmployeeServlet?action=addQualification" method="post" onSubmit="return addQualvalidation()">
<div id="addempQuall">
<table width="677" id="customers">
		<tr ><td colspan="4">Please Note Down Below Employee Number</td><tr>
		   <tr class="alt"><td width="151" >Employee Code</td>
	  		<td width="177" ><%=session.getAttribute("empcode")==null?"":session.getAttribute("empcode") %><input type="hidden" name="aempNo" id="aempNo"  readonly="readonly" value="<%=(session.getAttribute("empno")==null?"":session.getAttribute("empno")) %>"></td>
				<td width="117" >Employee Name</td>
			<td width="212" ><input type="text" name="aempName" id="aempName" readonly="readonly" value="<%=(session.getAttribute("empname")==null?"":session.getAttribute("empname")) %>"></td></tr>
		<tr class="alt">
		<td >Degree</td>
		  <td >
		  
		    <select name="aRDEGREE" id="aRDEGREE" >  
      					 <option value="0">Select....</option>  
    						<%
  							  ArrayList<Lookup> result=new ArrayList<Lookup>();
    						  LookupHandler lkhp= new LookupHandler();
    					      result=lkhp.getSubLKP_DESC("ED");
 							for(Lookup lkbean : result){
     						%>
      			<option value="<%=lkbean.getLKP_SRNO()%>"><%=lkbean.getLKP_DESC()%></option>  
     					 <%}%>
     			 </select>
		  
		  </td>
		   <td>Year of passing </td>
			
			<td><input type="text" name="aPassyear" id="aPassyear" value="" > "YYYY"</td>
		  
		  </tr>
	   <tr class="alt">	<td>University</td>
	     <td><input name="aUniName" type="text" id="aUniName" maxlength="40" ></td>
			<td>Percentage</td>
			<td><input name="aPercent" type="text" id="aPercent" maxlength="5" > 
			&nbsp; % </td>
	   </tr>
		<tr class="alt">
		  <td >Grade of Passing</td>
		  <td><input name="aGrade" type="text" id="aGrade" value="" maxlength="20" ></td>
		 <td>Remark</td>
		  <td><input name="aRemark" type="text" id="aRemark" value="" maxlength="20" ></td>
		</tr>
		<tr class="alt">
		<td >Add More Qualification</td>
			<td valign="top" colspan="3"><input type="radio" name="moreAdd" id="moreAdd" value="YES" >YES &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="moreAdd" id="moreAdd" value="NO"  checked="checked"> NO</td>
		</tr>
		
		<tr class="alt"><td colspan="4" align="center"><input type="submit" value="Submit"  /> 
		&nbsp; &nbsp;
		<input type="reset" value="Cancel"  /></td></tr>
    </table>
	</div>
<div class="clear">&nbsp;</div>
</form>
<%

if(empQualList2!=null)
{
%>
<form>
	<table width="1907" id="customers">
		<tr><th width="20">Sr.No</th>
		<th width="200">Degree</th>
		<th width="300">University</th>
		<th width="20">Percentage</th>
		<th >Grade of Passing</th>
		<th >Year Of Passing</th>
		<th width="20">Remark</th>
		
		</tr>
				<%
				for(EmpQualBean empQualbean :empQualList2)
				{
					String str=String.valueOf(empQualbean.getSRNO());
					%>
				<tr><td><%=empQualbean.getSRNO() %></td><td><%=lh.getLKP_Desc("ED", empQualbean.getDEGREE())%></td><td><%=empQualbean.getINST() %></td><td><%=empQualbean.getPERCENT() %></td><td><%=empQualbean.getCLASS() %></td><td><%=empQualbean.getPASSYEAR() %></td><td><%=empQualbean.getREM() %></td>
				
				</tr>
				
				<% 
				}
				%>
			<tr bgcolor="#85A02F"><td align="right" colspan="8"></td></tr>
		</table>
		</form>
<%	
}
} 
%>
</td>
		<td id="tbl-border-right"></td>
	</tr>
	<tr>
		<th class="sized bottomleft"></th>
		<td id="tbl-border-bottom">&nbsp;</td>
		<th class="sized bottomright"></th>
	</tr>
	</table>

</form>
</div>

<!--  end content -->
<div class="clear">&nbsp;</div>
</div>
<
<!--  end content-outer........................................................END -->

<div class="clear">&nbsp;</div>
 </div>  

</body>
</html>