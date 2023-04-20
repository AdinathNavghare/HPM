<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forgot Password</title>
<script type="text/javascript">

function show(a,b) {
	
document.getElementById(a).style.display = "block";
document.getElementById(b).style.display = "none";
}
</script>
<style type="text/css">
#UserName,#emailId  { display:none; }
.innercontainer
 {
	margin-left:auto;
	margin-right:auto;
	margin-top:3%;
	border:3px solid #cacaca;
	width:700px;
	height:250px;
	border-radius:10px; 
 }
 .forgotpswd
 {
	font-size:30px;
	text-align:center;
  }
 
 .labelfront {
    line-height: 78px;
    margin-bottom: 10px;
    margin-left: 40px;
    margin-right: 10px;
    margin-top: 10px;
}
</style>
</head>
<body>

<h3 class="forgotpswd">
 Forgot Password
</h3>
<div class="innercontainer">
<form action="LoginServlet" method="post">
<table>
 <tr>
 <input type="hidden" name="action" value="forgotpwd"/>
<td>
     <input type="radio" name="rad" id="r0" onclick="show('UserName','emailId')">
 </td>
  <td>
<label class="labelfront" for="radio1">I know my User Name</label>
</td>
 <td>
<input type="text"  name="UserName" id="UserName" onclick="this.value='';" onfocus="this.select()" />
</td>
</tr>
 <tr>

     <td>
<input type="radio" name="rad" id="r1" onclick="show('emailId','UserName')">
</td>
 <td>
     <label class="labelfront" for="radio2">I know my Email Id</label>
     </td>
<td>
 <input type="text"  name="emailId" id="emailId" onclick="this.value='';" onfocus="this.select()"/> 
   </td>
</tr>


   </table>
   <center>
   <input type="submit" value="SUBMIT" />
   </center>
      </form>
</div>

<%-- 
<form action="LoginServlet" method="post">
<table align="center">
<div align="center">
<span>Forgot Password?</span>
<span>Please Enter your User Name Or register Email Id.</span>
</div>
<tr><td>User Name</td><td><input type="text"  name="UserName" id="UserName"></input></td></tr>
</br>
<tr><td>Email </td><td><input type="text"  name="emailId" id="emailId"></input></td></tr>
<tr><td colspan="2"><input type="submit" value="Submit"/></td></tr>
<input type="hidden" name="action" value="forgotpwd"/>
<tr><td style="color: red"><c:out value="${param.status}" default=""/></td></tr>
</table>
</form> --%>



</body>
</html>