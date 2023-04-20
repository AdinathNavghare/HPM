<%@page import="payroll.Model.DeductBean"%>
<%@page import="payroll.DAO.DeductHandler"%>
<%@page import="payroll.DAO.CodeMasterHandler"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=iso-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="default" />

<script type="text/javascript" src="js/datetimepicker1.js"></script>

<title>Update Deduction</title>
<style type="text/css">
<!--
body,td,th {
	font-family: Georgia;
	font-size: 14px;
}
-->
</style>
<%
	int check=0;
	try
	{
		String action = request.getParameter("action");
		if(action.equalsIgnoreCase("close"))
		{
			check=1; //Record Modified
		}
		else if(action.equalsIgnoreCase("keep"))
		{
			check=2;	// Error Record not Modified
		}
	}
	catch(Exception e)
	{
		System.out.println("First Time Loading dialog");
	}
	CodeMasterHandler CMH = new CodeMasterHandler();
	ArrayList<CodeMasterBean> CMBList = CMH.getCDMAST(String.valueOf(2));  // Here 2 is for CDTYPE for Deductions
	
	String[] key= request.getParameter("key").split("-");
	int empno = Integer.parseInt(key[0]);
	int trncd= Integer.parseInt(key[1]);
	int srno= Integer.parseInt(key[2]);
	
	DeductHandler DH = new DeductHandler();
	DeductBean DB = DH.getSingleDed(empno, trncd, srno);
	
%>
<script type="text/javascript">

	function getClose()
	{
		window.close();
	}
	
	function checkVal()
	{
		var flag = document.getElementById("chek").value;
		if(flag == 1)
		{
			alert("Record Updated Successfully!");
			window.close();
		}
		else if(flag==2)
		{
			alert("Error in Modification of Record");
		}
		
	}
	function updateValidation()
	{
		if(document.getElementById("select.trncd").selectedIndex==0)
		 {
		 alert("Please Select Transaction Code");
		 document.getElementById("select.trncd").focus();
		 return false;
		 }
	 if(document.getElementById("subSysCode").value =="")
	 {
	 alert("Please Enter SubSystem Code");
	 document.getElementById("subSysCode").focus();
	 return false;
	 }
	 if(document.getElementById("acno").value =="")
	 {
	 alert("Please Enter Account Number");
	 document.getElementById("acno").focus();
	 return false;
	 }
	 if(document.getElementById("sancDate").value =="" || document.getElementById("sancDate").value =="dd-mmm-yyyy")
	 {
	 alert("Please Select Sanction  Date");
	 document.getElementById("sancDate").focus();
	 return false;
	 }
	 if(isNaN(document.getElementById("sancNo").value)||document.getElementById("sancNo").value =="")
	 {
	 alert("Please Enter Sanction  Number");
	 document.getElementById("sancNo").focus();
	 return false;
	 }
	 if(isNaN(document.getElementById("sacnAmt").value)||document.getElementById("sacnAmt").value =="")
	 {
	 alert("Please Enter Sanction Amount");
	 document.getElementById("sacnAmt").focus();
	 return false;
	 }
	 if(isNaN(document.getElementById("install").value)||document.getElementById("install").value =="")
	 {
	 alert("Please Enter Installment Amount");
	 document.getElementById("install").focus();
	 return false;
	 }
	 if(isNaN(document.getElementById("intRate").value)||document.getElementById("intRate").value =="")
	 {
	 alert("Please Enter Intresr Rate");
	 document.getElementById("intRate").focus();
	 return false;
	 }
	 if(document.getElementById("startDate").value =="" || document.getElementById("startDate").value =="dd-mmm-yyyy")
	 {
	 alert("Please Select Start  Date");
	 document.getElementById("startDate").focus();
	 return false;
	 }
	 if(document.getElementById("endDate").value =="" || document.getElementById("endDate").value =="dd-mmm-yyyy")
	 {
	 alert("Please Select End  Date");
	 document.getElementById("endDate").focus();
	 return false;
	 }
	}
</script>

</head>
<body onLoad="checkVal()">
	<div align="center"></div>
	<form id="f1" name="f1" action="DeductionServlet?action=modify"
		method="post" onSubmit="return updateValidation()">
		<table width="637" border="0" align="center" id="customers">
			<tr class="alt">
				<td colspan="5" align="center">Update Deduction</td>
			</tr>
			<tr class="alt">
				<td width="126" align="right" valign="middle">Transaction Code
				</td>
				<td width="177" bgcolor="#FFFFFF"><label> <select
						name="select.trncd" id="select.trncd" style="width: 175px;">
							<option value="0" selected>Select</option>
							<%
						for (CodeMasterBean temp1:CMBList)
						{
							if(temp1.getTRNCD()==DB.getTRNCD())
							{
				%>
							<option value="<%=temp1.getTRNCD()%>" selected="selected"><%=temp1.getDISC()%></option>
							<%
							}
							else
							{
				%>
							<option value="<%=temp1.getTRNCD()%>"><%=temp1.getDISC()%></option>
							<%				
							}
						}
				%>
					</select>
				</label></td>
				<td width="15" bgcolor="#CCCCCC">&nbsp;</td>
				<td width="114" align="right" bgcolor="#FFFFFF">Subsystem Code
				</td>
				<td width="183" bgcolor="#FFFFFF"><input type="text"
					name="subSysCode" id="subSysCode" value="<%=DB.getSUBSYS_CD()%>">
				</td>
			</tr>
			<tr class="alt">
				<td align="right" valign="middle">Account Number</td>
				<td bgcolor="#FFFFFF"><input type="text" name="acno" id="acno"
					value="<%=DB.getAC_NO()%>" maxlength="15"></td>
				<td bgcolor="#CCCCCC">&nbsp;</td>
				<td align="right" bgcolor="#FFFFFF">Sanction Date</td>
				<td bgcolor="#FFFFFF"><input name="sancDate" id="sancDate"
					type="text" value="<%=DB.getSANC_DATE() %>" readonly="readonly"
					onFocus="if(value=='dd-mmm-yyyy') {value=''}"
					onBlur="if(value=='') {value='dd-mmm-yyyy';}">&nbsp;<img
					src="images/cal.gif" align="middle"
					style="vertical-align: middle; cursor: pointer;"
					onClick="javascript:NewCssCal('startDate', 'ddmmmyyyy')" /></td>
			</tr>
			<tr class="alt">
				<td align="right" valign="middle" bgcolor="#FFFFFF">BOD
					Sanction No.</td>
				<td bgcolor="#FFFFFF"><input type="text" name="sancNo"
					id="sancNo" value="<%=DB.getBODSANCNO() %>"></td>
				<td bgcolor="#CCCCCC">&nbsp;</td>
				<td align="right" bgcolor="#FFFFFF">Sanction Amount</td>
				<td bgcolor="#FFFFFF"><input type="text" name="sacnAmt"
					id="sacnAmt" value="<%=DB.getSANC_AMT() %>"></td>
			</tr>
			<tr class="alt">
				<td align="right" valign="middle" bgcolor="#FFFFFF">Installment
					Amt.</td>
				<td bgcolor="#FFFFFF"><input type="text" name="install"
					id="install" value="<%=DB.getAMOUNT() %>"></td>
				<td bgcolor="#CCCCCC">&nbsp;</td>
				<td align="right" bgcolor="#FFFFFF">Installments</td>
				<td bgcolor="#FFFFFF"><input type="text" name="intRate"
					id="intRate" value="<%=DB.getNo_Of_Installment() %>"></td>
			</tr>
			<tr class="alt">
				<td align="right" valign="middle" bgcolor="#FFFFFF">Start Date</td>
				<td bgcolor="#FFFFFF"><input name="startDate" id="startDate"
					type="text" value="<%=DB.getREPAY_START() %>" readonly="readonly"
					onFocus="if(value=='dd-mmm-yyyy') {value=''}"
					onBlur="if(value=='') {value='dd-mmm-yyyy';}">&nbsp;<img
					src="images/cal.gif" align="middle"
					style="vertical-align: middle; cursor: pointer;"
					onClick="javascript:NewCssCal('startDate', 'ddmmmyyyy')" /></td>
				<td bgcolor="#CCCCCC">&nbsp;</td>
				<td align="right" bgcolor="#FFFFFF">End Date</td>
				<td bgcolor="#FFFFFF"><input name="endDate" id="endDate"
					type="text" value="<%=DB.getEND_DATE() %>" readonly="readonly"
					onFocus="if(value=='dd-mmm-yyyy') {value=''}"
					onBlur="if(value=='') {value='dd-mmm-yyyy';}">&nbsp;<img
					src="images/cal.gif" align="middle"
					style="vertical-align: middle; cursor: pointer;"
					onClick="javascript:NewCssCal('endDate', 'ddmmmyyyy')" /></td>
			</tr>
			<tr class="alt">
				<td height="18" align="right" valign="middle" bgcolor="#FFFFFF">Cummulative</td>
				<td bgcolor="#FFFFFF"><select name="select.cuml"
					id="select.cuml">
						<%
      		if((DB.getCUMUYN()==null?"N":DB.getCUMUYN()).equalsIgnoreCase("Y"))
      		{
      	%>
						<option value="Y" selected="selected">YES</option>
						<option value="N">NO</option>
						<%
      		}
      		else
      		{
        %>
						<option value="Y">YES</option>
						<option value="N" selected="selected">NO</option>

						<%
      		}
        %>
				</select></td>
				<td bgcolor="#CCCCCC">&nbsp;</td>
				<td align="right" bgcolor="#FFFFFF">Active</td>
				<td bgcolor="#FFFFFF"><select name="select.active"
					id="select.active">
						<%
      		if(DB.getACTYN().equalsIgnoreCase("Y"))
      		{
      	%>
						<option value="Y" selected="selected">YES</option>
						<option value="N">NO</option>
						<%
      		}
      		else
      		{
        %>
						<option value="Y">YES</option>
						<option value="N" selected="selected">NO</option>

						<%
      		}
        %>
				</select></td>
			</tr>
			<tr align="center" class="alt">
				<td colspan="5" valign="middle"><label><input
						type="submit" name="save" value="Update" /> </label> <label><input
						type="button" name="clear" value="Cancel" onClick="getClose()" />
				</label></td>
			</tr>
		</table>
		<input type="hidden" value="<%=empno%>" name="empno" id="empno">
		<input type="hidden" value="<%=trncd%>" name="trncd" id="trncd">
		<input type="hidden" value="<%=srno%>" name="srno" id="srno">
		<input type="hidden" value="<%=check%>" name="chek" id="chek">
	</form>
</body>
</html>