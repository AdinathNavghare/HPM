
<%@ page import="payroll.DAO.LoanAppHandler"%>
<%@ page import="payroll.Model.LoanAppBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="java.util.ArrayList"%>
<%@page import="payroll.Model.*"%>
<%@page import="payroll.DAO.*"%>
<%@ page import="payroll.Core.ReportDAO"%>
<%@page errorPage="error.jsp" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy DTS3</title>
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="default" />
<link rel="stylesheet" href="css/dropdown.css" type="text/css"
	media="screen" title="default" />
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>

<%
String flag	=request.getParameter("flag")==null?"":request.getParameter("flag");
ArrayList<LoanAppBean> LoanList = new ArrayList<LoanAppBean>();
LoanAppHandler loanHandler = new LoanAppHandler();
LoanList=loanHandler.getLoanDetailsemp();
%>
<script type="text/javascript">
function checkFlag() {
		var f = parseInt(document.getElementById("flag1").value);

		if (f == 1) {
			alert("Tranaction Saved Successfully!!!");

		}
		if (f == 2) {
			alert("Something Went Wrong... Please Try Again Later!!!");
		}
		
	}

	
	function EditInstallment(empno)
	{
		var flag = confirm("Do you want to Edit this Amount");
		if (flag) {
			
			//var key ="action:updateinstall:Name:"+Name+":empno:"+empno+":loan_no:"+loan_no+":amt:"+final_amt;
			//window.location.href = "empTranDetails.jsp?action=edit&key1=" + str;
			//Name=Name.replace(/\s/g,'1');
			document.getElementById("myModal").style.display = 'Block';
			//$("#myModal").load("updateLoanDetails.jsp?action=updateinstall"+"&empno="+empno+"&loan_no="+loan_no+"&amt="+final_amt+"&Name="+Name);
			$("#myModal").load("LoanDetails.jsp?action=getdetails&empno="+empno);
			$("#myModal").fadeTo('slow', 0.9);
			$('.nav-outer').fadeTo("slow", 0.5).css('pointer-events', 'none');
		}

	}

</script>

<!-- <script src="js/MONTHPICK/jquery.js"></script>
    <script type="text/javascript" src="js/MONTHPICK/jquery-ui.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="css/MONTHPICK/jquery-ui.css">
    <script type="text/javascript">
    $(function() {
        $('.date-picker').datepicker( {
            changeMonth: true,
            changeYear: true,
            showButtonPanel: true,
            dateFormat: 'M-yy',
            onClose: function(dateText, inst) { 
                var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                $(this).datepicker('setDate', new Date(year, month, 1));
                $(':focus').blur();
                
            },
            beforeShow : function(input, inst) {
                var datestr;
                if ((datestr = $(this).val()).length > 0) {
                    year = datestr.substring(datestr.length-4, datestr.length);
                    month = jQuery.inArray(datestr.substring(0, datestr.length-5), $(this).datepicker('option', 'monthNamesShort'));
                    $(this).datepicker('option', 'defaultDate', new Date(year, month, 1));
                    $(this).datepicker('setDate', new Date(year, month, 1));
                    
                }
            }
        });
        
    });
    </script> -->
    <style>
    .ui-datepicker-calendar {
        display: none;
        }
    </style>


</head>
<body onLoad="checkFlag()" style="overflow: hidden;">
	<%@include file="mainHeader.jsp"%>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp"%>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer" style="overflow-y: scroll; max-height: 80%;">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>LOAN DETAILS </h1>
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
							<div id="table-content">
								<center>

									<!-- <form action="TransactionServlet?action=editMedInsuTranValues" method="post"> -->

										<table id="customers" width="100%" align="center">
											<tr>
												<th><%-- FOR MONTH : <input disabled="disabled" readonly="readonly"  name="date" style="width: 70px;border:none;color:white; background-color: #2F747E ;" value="<%=ReportDAO.getServerDate()%>"> --%></th>
											</tr>
											<tr>
												<th>LOAN DETAILS</th>
											</tr>
											<%-- <tr>
												<td align="center">
												<table>
												
											
														<tr>
															<td>Project Name : <%=eoffbn.getPrj_name()%></td>
															<td align="center" >
																<input name="date" id="date" readonly="readonly" class="date-picker" placeholder="Click here for Calender"/>
															
															</td>
															<td><input type="Button" value="Submit" onclick="getEMPLIST()" /> </td>
															<td><h2><%=date_disp%></h2></td>
														</tr>
													
												
												
												
												
												
												
												
												<!-- <tr><td width="385" align="center" >Emp Name:<input type="text" id="emn" name="emn" style="width: 80%;" autofocus onClick="this.select();" ></td>
												
												<td width="385">Mob No:<input type="text" id="emo" name="emo" style="width: 80%;" autofocus onClick="this.select();" value=""></td>
												
												 <td align="center" bgcolor="#929292"><input type="Button" value="Submit" onclick="getInfo()" /></td>
												
												<td><%=date_disp%></td>
												</tr> -->
												</table>
												</td>												
											</tr> --%>
											<tr>
												<td>
													<div class="imptable">
														
													<table width="998">
														<tr>
															<th width="48" align="center">EMPCODE </th>
														    <th width="180" align="center">EMPLOYEE NAME </th>
														    <th width="100" align="center">AMOUNT SANCT </th>
														    <!-- <th width="50" align="center">RATE(%)</th>
														    <th width="50" align="center">DURATION</th> -->
														    <th width="60" align="center">ACTUAL_PAY</th>
														    <!-- <th width="80" align="center">PRI</th>
														     <th width="80" align="center">INT</th> -->
														     <!-- <th width="80" align="center">INTSALL_AMT</th>
														     <th width="80" align="center">FINAL_AMT</th> -->
														     <th width="80" align="center">ACTION</th> 																													
														</tr>
													</table>
													</div>
												</td>
											</tr></table>
											<table id="customer" width="100%" ><tr>
												<td>
													<div id="scrolling" class="imptable"
														style="height: 300px; overflow-y: scroll; max-width: 1000px; background-color: #FFFFFF;"
														align="center">
														<table width="998"  align="center">
															
															<%
																int index=0;
																float total = 0;
																for(LoanAppBean lbn:LoanList){%>
																   
																   <tr id="val"align="center" border="1" valign="middle"  style="width: 70">
																  
																  
																  	<th width="51" align="center"><%=lbn.getEmpcode() %> </th>
																    <th width="150" align="center"><%=lbn.getName()%></th>
																    <th width="88" align="center"><%=lbn.getLoan_amt() %></th>
																    <%-- <th width="53" align="center"><%=lbn.getLoan_per() %></th>
																    <th width="55" align="center"><%=lbn.getTotal_month() %></th> --%>
																    <th width="70" align="center"><%=lbn.getActual_pay() %></th>
																    <%-- <th width="80" align="center"><%=0%></th>
																     <th width="80" align="center"><%=0 %></th> --%>
																    <%--  <th width="75" align="center"><%=lbn.getMonthly_install()%></th>
																     <th width="70" align="center"><input type="text"  id="a" style="width: 60px;" value="0.00"></th> --%>
																     <th width="68" align="center"><input type="button" id="<%=lbn.getEMPNO() %>" value="EDIT" onclick="EditInstallment('<%=lbn.getEMPNO() %>')"></th> 
																   </tr>
																  					<%}%>
															
														</table>
													</div>
												</td>
											</tr></table>
											<%-- <tr>
												<td>
													<table width="930">
														<tr>
															<td width="3600" align="right" bgcolor="#2f747e"><font color="white"> &nbsp;<!--  TOTAL :--></font></td>
															<!-- <td width="400" align="center"> --> &nbsp;<!-- <input type="text" align="right" value="<%=total%>"> --></td>																													
														</tr>
														
														<%
														
														if(tran.size()>0)
														{
														
														
														%>
														
														
														
														<tr><td align="center" bgcolor="#2f747e"><input type="button"
													value="Edit" onClick="enableTextBoxes()" height="100" width="150" />
												<input type="submit" id="tranSave" value="Save" disabled="disabled" /></td></tr>
												
												
												<%}
														else
														{
															%>
															
															
															
															<tr><td align="center" bgcolor="#929292" colspan="2"> 
															
															<font color="WHITE" size="3">
															NO RECORDS FOUND ! </font></td></tr>
													
													
													<%
														
														}%>
													</table>
												</td>
											</tr> --%>								
										<!-- </table> -->
									<!--  </form> -->
									<br>
									 <input type="hidden" name="flag1" id="flag1" value="<%=flag%>">	
								</center>
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