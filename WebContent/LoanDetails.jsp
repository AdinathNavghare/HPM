<%@page import="java.util.ArrayList"%>
<%@ page import="payroll.DAO.LoanAppHandler"%>
<%-- <%@ page import="payroll.Core.ReportDAO"%> --%>
<%@ page import="payroll.Model.LoanAppBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
String action = request.getParameter("action");
//String flag	=request.getParameter("flag")==null?"":request.getParameter("flag");
String empno="";
ArrayList<LoanAppBean> LoanList = new ArrayList<LoanAppBean>();
if(action.equalsIgnoreCase("getdetails"))
{
	empno = request.getParameter("empno");
	System.out.println("empno==="+empno);
	LoanAppHandler loanHandler = new LoanAppHandler();
	LoanList=loanHandler.getLoanDetails(empno);
}
%>
<script type="text/javascript">
function inputLimiter(e,allow) {
	  var AllowableCharacters = '';
	  if (allow == 'Numbers'){AllowableCharacters='1234567890';}
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

	function checkFlag() {
		var f = parseInt(document.getElementById("flag1").value);

		if (f == 1) {
			alert("Tranaction Saved Successfully!!!");
			closediv();
			window.location.href = "checkLoan.jsp";

		}
		if (f == 2) {
			alert("Please Try Again!!!");
		}
		
	}

	
	/* function EditInstallment(Name,empno,loan_no,final_amt)
	{
		var flag = confirm("Do you want to Edit this Amount");
		if (flag) {
			alert(Name+";"+empno+";"+loan_no+";"+final_amt);
			//var key ="action:updateinstall:Name:"+Name+":empno:"+empno+":loan_no:"+loan_no+":amt:"+final_amt;
			//window.location.href = "empTranDetails.jsp?action=edit&key1=" + str;
			Name=Name.replace(/\s/g,'1');
			alert(Name);
			document.getElementById("myModal").style.display = 'Block';
			$("#myModal").load("updateLoanDetails.jsp?action=updateinstall"+"&empno="+empno+"&loan_no="+loan_no+"&amt="+final_amt+"&Name="+Name);
			$("#myModal").fadeTo('slow', 0.9);
			$('.nav-outer').fadeTo("slow", 0.5).css('pointer-events', 'none');
		}

	} */
	 function closediv() {
			
		 document.getElementById("myModal").innerHTML= "";
			document.getElementById("myModal").style.display = "none";
			jQuery(function(){
				  $("input[type=Submit]").removeAttr("disabled");
				  $("input[type=button]").attr("disabled", false);
			});

			$('.nav-outer').fadeTo("slow", 1).css('pointer-events', 'auto');
		//window.location.href="LoanDetails.jsp";
	} 
	
	function validation()
	{
		var loan_no	= document.getElementsByName("loan_no");
		var flag =true;
		debugger;
		for(var i=0;i<loan_no.length;i++)
			{
				//alert(loan_no[i].value);
				
				var actual_amt1 = document.getElementById("act_amt"+loan_no[i].value).value;
				var paid_Amt1   = document.getElementById("paid"+loan_no[i].value).value;
				var pri = document.getElementById("pri"+loan_no[i].value).value;
				var intr = document.getElementById("intr"+loan_no[i].value).value;
				var installment = document.getElementById("loan"+loan_no[i].value).value;
				var total= 0;
				if(installment==""||installment==null)
				{
					
					 total = parseFloat(pri)+parseFloat(intr);
					document.getElementById("loan"+loan_no[i].value).value=total.toFixed(2);
				}
				var remaining   = parseInt(actual_amt1)-parseInt(paid_Amt1);
				
				if(installment>remaining)
					{
						alert("Installment Amount Can't Be Greater Than Remaining Amount For Loan No :"+loan_no[i].value);
						flag =false;
						break;
					}
				if(pri==""||pri==null||intr==""||intr==null)
					{
						alert("Principle/Interest Amount Should Not Be Null, Enter Atleast 0 Amt For Loan No :"+loan_no[i].value);
						flag = false;
						break;
					}
				if(installment!=(parseFloat(pri)+parseFloat(intr)))
					{
						alert("New Installment Amount Should Be Equal To Principle+Interest For Loan No :"+loan_no[i].value);
						flag = false;
						break;
					}
			}
		return flag;
		
	}
	
function setval(ID)
{
	//alert(ID);
	var pri = document.getElementById("pri"+ID).value;
	var intr = document.getElementById("intr"+ID).value;
	//alert("pri=="+pri+"...intr=="+intr);
	if(pri==""||pri==null)
		pri=0;
	if(intr==""||intr==null)
		intr=0;
	var total = parseFloat(pri)+parseFloat(intr);
	document.getElementById("loan"+ID).value=total.toFixed(2);
	//parseFloat(yourString).toFixed(2)
	
	
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
	<img src='images/Close.png' style='float:right;' title='Remove' onclick="closediv()"><br>
	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer" style="overflow-y: scroll; max-height: 80%;">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>UPDATE INSTALLMENTS </h1>
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

									<form action="TransactionServlet?action=updateloanInstall&empno1=<%=empno%>" method="post" onsubmit="return validation()">
										
										<table id="customers" width="100%" align="center">
											
											<%-- <tr>
												<th>FOR MONTH:<input type="text" readonly="readonly"  name="date" style="width: 40px;" value="<%=ReportDAO.getServerDate()%>"></th>
											</tr> --%>
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
														
													<table width="1000">
														<tr>
															<th width="48" align="center">EMPCODE </th>
														    <th width="180" align="center">EMPLOYEE NAME </th>
														    <th width="40" align="center">LOAN NO</th>
														    <th width="120" align="center">SANCT AMT</th>
														    <th width="50" align="center">RATE(%)</th>
														    <th width="50" align="center">DURATION</th>
														    <th width="60" align="center">ACTUAL_PAY</th>
														    <th width="60" align="center">TOTAL_PAID</th>
														    <!-- <th width="80" align="center">PRI</th>
														     <th width="80" align="center">INT</th> -->
														     <th width="60" align="center">INTSALL_AMT</th>
														     <th width="100" align="center">PRI</th>
														     <th width="80" align="center">INTRT</th>
														     <th width="80" align="center">NEW_INSTALL</th>
														     <!-- <th width="80" align="center">ACTION</th> 	 -->																												
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
														<table width="1000"  align="center">
															
															<%
																int index=0;
																float total = 0;
																for(LoanAppBean lbn:LoanList){%>
																   
																   <tr id="val"align="center" border="1" valign="middle"  style="width: 70">
																  
																  
																  	<th width="58" align="center"><%=lbn.getEmpcode() %> </th>
																    <th width="100" align="center"><%=lbn.getName()%></th>
																    <th width="35" align="center"><input type="text" readonly="readonly"  name="loan_no" style="width: 40px;border:none;color:black; background-color: #f7f7f7 ;" value="<%=lbn.getLoan_no()%>"></th>
																    <th width="70" align="center"><%=lbn.getLoan_amt() %></th>
																    <th width="50" align="center"><%=lbn.getLoan_per() %></th>
																    <th width="65" align="center"><%=lbn.getTotal_month() %></th>
																    <th width="75" align="center"><input type="text" readonly="readonly" id="act_amt<%=lbn.getLoan_no() %>" name="act_amt" style="width: 60px;border:none;color:black; background-color: #f7f7f7 ;" style="background:black;" value="<%=lbn.getActual_pay() %>"></th>
																    <th width="70" align="center"><input type="text" readonly="readonly" id="paid<%=lbn.getLoan_no() %>" name="paid_amt" style="width: 60px;border:none;color:black; background-color: #f7f7f7 ;" style="background:black;" value="<%=lbn.getTotal_paid()%>"></th>
																    <%-- <th width="80" align="center"><%=0%></th>
																     <th width="80" align="center"><%=0 %></th> --%>
																     <th width="68" align="center"><%=lbn.getMonthly_install()%></th>
																     <th width="10" align="center"><input type="text"  id="pri<%=lbn.getLoan_no()%>" name="pri1<%=lbn.getLoan_no()%>" style="width: 60px;border:none;color:black; background-color: yellow ;" value="<%=lbn.getPrincipal()%>" onkeypress="return inputLimiter(event,'Numbers');" onkeyup="setval('<%=lbn.getLoan_no()%>')" oncopy="return false" onpaste="return false"></th>
																     <th width="43" align="center"><input type="text"  id="intr<%=lbn.getLoan_no()%>" name="intr1<%=lbn.getLoan_no()%>" style="width: 60px;border:none;color:black; background-color: yellow ;" value="<%=lbn.getIntrest()%>" onkeypress="return inputLimiter(event,'Numbers');" onkeyup="setval('<%=lbn.getLoan_no()%>') " oncopy="return false" onpaste="return false"></th>
																     <th width="70" align="center"><input type="text"  id="loan<%=lbn.getLoan_no()%>" readonly="readonly" name="loan1<%=lbn.getLoan_no()%>" style="width: 60px;border:none;color:black; background-color: #f7f7f7 ;" value="<%=lbn.getLast_paid_installment()%>" onkeypress="return inputLimiter(event,'Numbers');"></th>
																     <%-- <th width="68" align="center"><input type="button" id="<%=lbn.getEMPNO() %>" value="EDIT" onclick="EditInstallment('<%=lbn.getName() %>','<%=lbn.getEMPNO() %>','<%=lbn.getLoan_no()%>','<%=lbn.getMonthly_install()%>')"></th> --%> 
																   </tr>
																  					<%}%>
															
														</table>
													</div>
												</td>
											</tr>
											<tr align="center"  class="alt" >
	    										<td colspan="5" valign="middle">
											      <label><input type="submit" name="save" value="Update" />
											      </label>
											      <label><input type="button" name="clear" value="Cancel" onClick="closediv()"/>
											      </label>
											    </td>
    										</tr>
											</table>
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
											
									  </form> 
									<br>
									<%-- <input type="hidden" name="flag1" id="flag1" value="<%=flag%>">	 --%>	 			
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