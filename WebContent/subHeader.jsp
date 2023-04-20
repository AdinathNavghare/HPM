<%@page import="payroll.DAO.NotifyHandler"%>
<%@page import="payroll.DAO.UsrHandler"%>
<%
String menu ="";
String name ="";
	try
	{	
		UsrHandler UH = new UsrHandler();
		if(session.getId().equals(UH.getSessionID(session.getAttribute("UID").toString())))
		{
			if(!session.getAttribute("myMenu").equals(null))
			{
				menu =session.getAttribute("myMenu").toString();
				name = session.getAttribute("name").toString();
			}
			else
			{
				session.invalidate();
				response.sendRedirect("login.jsp?action=0");
			}
		}
		else
		{
			session.invalidate();
			response.sendRedirect("login.jsp?action=3");
		}
	}
	catch(Exception e)
	{
		session.invalidate();
		
		%>
		<script type="text/javascript">
		
		window.location.href="login.jsp?action=0","_self";
		
		</script>

		<% 
		//response.sendRedirect("login.jsp?action=0");
	}
%>

<style>
.noti_Container {
position:relative;
border:1px solid blue; /* This is just to show you where the container ends */
width:16px;
height:16px;
cursor: pointer;
}
.noti_bubble {
position:absolute;
top: -8px;
right:-6px;
padding-right:2px;
background-color:red;
color:white;
font-weight:bold;
font-size:0.80em;

border-radius:2px;
box-shadow:1px 1px 1px gray;
}


#notify:HOVER	
{
background: white;
}
</style>

<style>
body {font-family: Arial, Helvetica, sans-serif;}

/* The Modal (background) */
.modal {
    display: none; /* Hidden by default */
    /* position: fixed; */ /* Stay in place */
    position: absolute;
    z-index: 99999; /* Sit on top */
    padding-top: 100px; /* Location of the box */
    left: 0;
    top: 0;
    width: 100px; /* Full width */
    height: 100px; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
	display: none; 
   background-color:  #F3F3F3;
    margin-top: -100px;
	margin-left: -80px;
    position: absolute;
    padding: 20px;
    top: 35%;
	left: 15.5%;
    width: 75%; /* Full width */
    height: 65%; /* Full height */
    border: 1px solid #888;
   	//z-index: 99999;
   /*  width: 80%; */
    
}


/* The Close Button */
.close {
    color: #aaaaaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}
</style>



<script>
function f1()
{
	//document.getElementById("notify").style.display='none';
var a=confirm("Do you want to open notification tab !");
if(a)
	{
	
window.location.href="notification.jsp";
	}
}

</script>


<div class="nav-outer-repeat" style="width: 100%"> 
<!--  start nav-outer -->
<div class="nav-outer"> 

		<!-- start nav-right -->
		<div id="nav-right">
		
			<div class="nav-divider">&nbsp;</div>
			
			<div class="nav-divider">&nbsp;</div>
			<div id="notify" onclick="return f1();" style="float: left">

<% boolean fl=NotifyHandler.getNotify(session.getAttribute("EMPNO").toString());
if(fl)
{
%>
<img src="images/notification.png" style=" cursor:pointer;border:none;float:left;width:40px;height:40px;" title="Click here to see the Notifications."/>
<%}%>
<!-- <div id='n1' style='float:left'> <blink>2</blink></div>
 -->
</div>
			<div style="font-size: 12px; color: #FFFFFF; margin-left:-10px; margin-top: 10px; text-transform: uppercase;">
			<b>Welcome  <%=name.substring(0, (name.length() <= 10?name.length():10))+"" %> </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			
			</div>
			
			<a href="Logout.jsp" id="logout" style="float: right; margin-top: -10px; margin-right:3%;">
			<img src="images/shared/nav/nav_logout.gif"  alt="" /></a>
			
			<div class="clear"></div>
		
			<!--  start account-content -->	
			<div class="account-content">
			<div class="account-drop-inner">
				
			</div>
			</div>
			<!--  end account-content -->
		
		</div>
		<!-- end nav-right -->


		<!--  start nav -->
		<div class="nav">
		
		<div id="horizontalmenu">
		
		<div class="nav-divider">&nbsp;</div>
		
		<%=menu %>
		
		<div class="clear"></div>
		
		</div>
		<div class="clear"></div>
		</div>
		<!--  start nav -->

</div>
<div class="clear"></div>
<!--  start nav-outer -->

</div>
<!--  start nav-outer-repeat................................................... END -->

  <div class="clear"></div>
  <div class="modal-content" id="myModal" >
  </div>