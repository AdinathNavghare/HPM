<%@page import="payroll.DAO.BranchDAO"%>
<%@page import="payroll.Model.BranchBean"%>
<%@page import="payroll.Model.Lookup"%>
<%@page import="payroll.DAO.LookupHandler"%>

<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="defaulrt" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />


<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<title>Branch Management</title>
<script language="javascript" type="text/javascript">  
    	 	var xmlHttp ;
      		
    	 	if (window.XMLHttpRequest) { // Mozilla, Safari, ...
    	 		xmlHttp = new XMLHttpRequest();
    	 	    } else if (window.ActiveXObject) { // IE
    	 	      try {
    	 	    	 xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
    	 	      } 
    	 	      catch (e) {
    	 	        try {
    	 	        	xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    	 	        } 
    	 	        catch (e) {}
    	 	      }
    	 	    }

        function showPrCity(str)
        {
				
    			var url="city.jsp";
      			url +="?count=" +str;
      			xmlHttp.onreadystatechange = stateChange;
      			xmlHttp.open("GET", url, true);
      			xmlHttp.send(null);
      }

      function stateChange()
      {   
      	if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete")
          	{   
      		document.getElementById("prCity").innerHTML=xmlHttp.responseText ;
      		}   
      }


      function showParCity(str)
      {
		
  	     var url="city.jsp";
    	      url +="?count=" +str;
    	      xmlHttp.onreadystatechange = stateChange2;
    	      xmlHttp.open("GET", url, true);
    	      xmlHttp.send(null);
    	   }

      function stateChange2()
      { 
        	      
    	     if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete"){   
    	      document.getElementById("parCity").innerHTML=xmlHttp.responseText ;
    	      }   
      }
      
     
  </script>  

</head>
<body>
<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:scroll; max-height:80%; ">
<!-- start content -->
<div id="content">
<div id="page-heading">
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
			<div id="table-content" >
 			<center>
 <%
 BranchDAO brnDAO = new BranchDAO();
 String action = request.getParameter("action")==null?"":request.getParameter("action");
 if(!action.equalsIgnoreCase("editBranch")){
	%> 

			<form action="BranchServlet?action=AddBranch" method="post">
			<table width="472"  id="customers">
			<h3>Add New Branch</h3>
			
			<tr><td>Branch Name</td><td><input type="text" id="brname" name="brname" required size="30"></td></tr>
			<tr><td>Abbreviation</td><td><input type="text" id="ABBRV" name="ABBRV" required size="30"/></td></tr>
			<tr><td>Address Line 1</td><td><input type="text" id="addr1" name="addr1" size="50"/></td></tr>
			<tr><td>Address Line 2</td><td><input type="text" id="addr2" name="addr2" size="50"/></td></tr>
			 <tr>
            
            <td width="225">State</td>
              <td width="235">
               <select name="prstate" id="prstate" onChange="showPrCity(this.value)">  
		       <option value="0">Select</option>  
			    <%
			    ArrayList<Lookup> result=new ArrayList<Lookup>();
			    LookupHandler lkhp = new LookupHandler();
			    result=lkhp.getSubLKP_DESC("STATE");
			    for(Lookup lkbean : result){
			     %>
				      <option value="<%=lkbean.getLKP_SRNO()%>"><%=lkbean.getLKP_DESC()%></option>  
				      <%
				 }
				     %>
		      </select></td></tr>
		     <tr> 
		     <td>City</td>
		              <td>
		              <select name="prCity" id="prCity" style="width: 150px;">  
		      <option value='0'>Select</option>  
		      </select>  
              </td>
              
            </tr>
            <tr><td>Pin Code</td><td><input type="text" id="pin" name="pin" required maxlength="6" size="6"/></td></tr>
            <tr><td colspan="2" align="center"><input type="submit" value="Save"> <input type="reset" value="Clear"></td></tr>
			</table>

</form>
<br/>
<%
 }
 


if(action.equalsIgnoreCase("editBranch")){
 int brncd = request.getParameter("brncd")==null?0:Integer.parseInt(request.getParameter("brncd"));
	ArrayList<BranchBean> brnList = brnDAO.getBranchDetails();
	 LookupHandler lh = new LookupHandler(); 


if((brnList.size()!=0))
{
		for(BranchBean brnBean : brnList)
       {
			if(brnBean.getBRCD() == brncd ){
	
%>
<div align="center">
<form action="BranchServlet?action=EditBrnch" method="post">
			<table width="469"  id="customers" style="font-size: 1">
			<h3>Edit Branch</h3>
			<input type="hidden" name="brcd" id="brcd" value="<%=brnBean.getBRCD()%>">
			<tr><td align="left" valign="middle">Branch Name</td>
			<td align="left" valign="middle"><input type="text" id="brname" name="brname" value="<%=brnBean.getBRNAME()%>" size="30"/></td></tr>
			<tr><td align="left" valign="middle">Abbreviation</td>
			<td align="left" valign="middle"><input type="text" id="ABBRV" name="ABBRV" value="<%=brnBean.getABBRV()%>" size="30"/></td></tr>
			<tr><td align="left" valign="middle">Address Line 1</td>
			<td align="left" valign="middle"><input type="text" id="addr1" name="addr1" value="<%=brnBean.getADDR1()%>" size="50"/></td></tr>
			<tr><td align="left" valign="middle">Address Line 2</td>
			<td align="left" valign="middle"><input type="text" id="addr2" name="addr2" value="<%=brnBean.getADDR2()%>" size="50"/></td></tr>
			 <tr>
            
            <td width="224" align="left" valign="middle">State</td>
              <td width="233" align="left" valign="middle">
               <select name="prstate" id="prstate" onChange="showPrCity(this.value)">  
		       <option value="0">Select</option>  
    						<%
    						 ArrayList<Lookup> result=new ArrayList<Lookup>();
    						
    						result=lh.getSubLKP_DESC("STATE");
    						for(Lookup lkbean : result)
 							{
 									
    						
     						if(lkbean.getLKP_SRNO()== brnBean.getSTATECD())
     						{
     							
     						%>
     							<option value="<%=lkbean.getLKP_SRNO()%>" selected="selected"><%=lkbean.getLKP_DESC()%></option>  
     							<%}
     						else {
     								%>
      						<option value="<%=lkbean.getLKP_SRNO()%>"><%=lkbean.getLKP_DESC()%></option>  
     					 		<%}
     					 		}%>
     			 </select></td></tr>
		     <tr> 
		     <td align="left" valign="middle">City</td>
		              <td align="left" valign="middle">
		              <select name="prCity" id="prCity" style="width: 150px;">  
		      <option value="-1">Select</option>  
    						<%
    						ArrayList<Lookup> result2=new ArrayList<Lookup>();
    						result2=lh.getSubLKP_DESC("CITY");
    						for(Lookup lkbean : result2)
 							{
 							if(lkbean.getLKP_SRNO()== brnBean.getCITYCD())
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
              </td>
              
            </tr>
            <tr><td align="left" valign="middle">Pin Code</td>
            <td align="left" valign="middle"><input type="text" id="pin" name="pin" value="<%=brnBean.getPin()%>" maxlength="6" size="6"/></td></tr>
            
			<tr align="center" valign="middle"><td colspan="2"><input type="submit" value="Save">&nbsp;<input type="button" value="Cancel" onClick="window.location='BranchServlet?action=showBranch'"></td></tr>
			</table>

</form><br/>
</div>
<%
			}}}}
	%>

<div>
<form>
	<table width="1848" id="customers">
				 
			<tr><th >Branch Name</th>
			<th width="50">Abbreviation</th>
			<th width="264">Addr Line1</th>
			<th width="240">Addr Line2</th>
			<th>City</th>
			<th width="60">State</th>
			<th width="45">Pin </th>
			<th>Edit</th>
			
			</tr>
			<% 
			
			
				
				 ArrayList<BranchBean> brnList = brnDAO.getBranchDetails();
				 LookupHandler lh = new LookupHandler(); 
			
			
			if((brnList.size()!=0))
			{
					for(BranchBean brnBean : brnList)
			{
				
				
					
				%>
		<tr >
			<td><%=brnBean.getBRNAME() %></td>
			<td><%=brnBean.getABBRV() %></td>
			<td><%=brnBean.getADDR1() %></td>
			<td><%=brnBean.getADDR2() %></td>
			<td><%=lh.getLKP_Desc("CITY", brnBean.getCITYCD()) %></td>
			<td><%=lh.getLKP_Desc("STATE",brnBean.getSTATECD()) %></td>
			<td><%=brnBean.getPin() %></td>
			<td><input type="button" value=" Edit " onClick="window.location='Branch.jsp?action=editBranch&brncd=<%=brnBean.getBRCD() %>'"></td>
			<%} %>
		</tr>
	<%
	
			}	
			else
			{
				
				%>
				<tr><td height="30" colspan="9">There is No Information</td></tr>
				<%
			}
			%>
		<tr bgcolor="#85A02F"><td align="right" colspan="9" height="20px"></td></tr>
		
		<%
			
		%></table>
	</form>


</div>
</div>
		<center>	   
			 
			<!--  end table-content  -->
	
	    </div>
		<!--  end content-table-inner ............................................END  -->
		</td>
		<td id="tbl-border-right">
		   </td>
		 </tr>
	<tr>
		<th class="sized bottomleft"></th>
		<td id="tbl-border-bottom">&nbsp;</td>
		<th class="sized bottomright"></th>
	</tr>
	</table>
	<div class="clear">&nbsp;</div>
	<div class="clear"></div>
		    
	

</div>

</div>

</body>
</html>