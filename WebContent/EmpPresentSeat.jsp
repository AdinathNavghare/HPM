<%@page import="javax.print.attribute.standard.Severity"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.icu.util.GregorianCalendar"%>
<%@page import="com.ibm.icu.util.Calendar"%>
<%@page import="com.ibm.icu.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="payroll.Core.ReportDAO"%>
<%@page import="payroll.Core.Calculate"%>
<%@page import="java.awt.Desktop.Action"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="payroll.Model.CodeMasterBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="payroll.Model.*"%>
<%@page import="payroll.DAO.*"%>
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
<script type="text/javascript" src="js/datetimepicker.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.2.min.js"></script>
<script src="js/jquery/jquery.autocomplete.js"></script>
<script src="js/filter.js"></script>
<style type="text/css">

.ac_results {
	padding: 0px;
	border: 1px solid #cacaca;
	background-color: #f5f6fa;
	overflow: hidden;
	width: 58% !important;
}

</style>
<%
int site_id1=0;
String date="";
String sheetstatus="";
String currentdate="";
int lastday=-1;

try
{
	
	EmpAttendanceHandler eh=new EmpAttendanceHandler();
	site_id1=Integer.parseInt(request.getParameter("prj")==null?"0":request.getParameter("prj"));
	System.out.println("site in emppresent..."+site_id1);
	sheetstatus=request.getParameter("status")==null?"0":request.getParameter("status");
	System.out.println("sheetstatus........."+sheetstatus);
	date=ReportDAO.EOM(request.getParameter("date")==null?request.getParameter("date2"):request.getParameter("date"));
	System.out.println("sheet date...."+date);
	currentdate=eh.getServerDate();
	
LookupHandler lkph= new LookupHandler();
TranHandler trnh= new TranHandler();
EmpOffHandler empOffHldr = new EmpOffHandler();
EmployeeHandler emphdlr = new EmployeeHandler();
LookupHandler lkh=new LookupHandler();
CodeMasterHandler CMH = new CodeMasterHandler();
ArrayList<CodeMasterBean> TRNCODE=new ArrayList<CodeMasterBean>();   
ArrayList<TranBean> tranlist=new ArrayList<TranBean>();
ArrayList<TranBean>  listbyEMPNO=new  ArrayList<TranBean> ();
ArrayList<TranBean> projEmpNolist = new ArrayList<TranBean>();
ArrayList<TranBean> projEmpNmlist = new ArrayList<TranBean>();



TRNCODE=CMH.getNoAutocalCDList();

RoleDAO obj1=new RoleDAO();
String roleId=obj1.getrole(Integer.parseInt(session.getAttribute("UID").toString()));

int trncd=0;
String select=new String();
String selectCode = new String();
LeaveMasterHandler obj=new LeaveMasterHandler();
String empName;
String action = request.getParameter("action")==null?"":request.getParameter("action");
System.out.println("action is"+action);
ArrayList<Lookup> emplist=new ArrayList<Lookup>(); 
emplist=lkh.getSubLKP_DESC("ET");	// ET is Code for Employee Type
int trn=0;
int keys=0;
int empno1=0;
int empno=0;
int flag=-1;
int prjCode = 0;
//if u want alert then uncment
//String att_approved_emp="";
try
{  
	
	try
	{
		flag=Integer.parseInt(request.getParameter("flag")==null?"":request.getParameter("flag"));
		//if u want alert then uncemnt
		//att_approved_emp = request.getParameter("approved_emp")==null?"":request.getParameter("approved_emp");
	}catch(Exception e)
	{
		System.out.println("no flag value"+flag);
		if( request.getParameter("prj")==null)
		{
		session.setAttribute("prjCode", "");
		}
	}
	if(action.equalsIgnoreCase("getdetails"))
	{
		
		
		 prjCode = Integer.parseInt(request.getParameter("prj"));
		 System.out.println("theeeeeeeeeeeeee first prjcode"+prjCode);
	    session.setAttribute("prjCode", prjCode);
	  
	    session.setAttribute("projEmpNolist", projEmpNolist);
	    int i=0;
	    
	  
	    ArrayList<TranBean> arl=new ArrayList<TranBean>();
	    for(TranBean tbn : projEmpNolist){
	    	TranBean trbn = new TranBean();
	    	
	    	projEmpNmlist.add(trbn);
	    	
	   
	    	
	    } session.setAttribute("prjCode", prjCode);
	//    System.out.println("the employee are for selected project code as:"+prjCode+ " "+projEmpNmlist.size());
	}

}
catch(Exception e)
{
	e.printStackTrace();
	System.out.println("First Time Loading");
}
%>

<%


int eno = (Integer)session.getAttribute("EMPNO");
int z=0,n=0;
EmpOffHandler eoffhdlr = new EmpOffHandler();
EmpOffBean eoffbn = new EmpOffBean();
ArrayList<TranBean> tran = new ArrayList<TranBean>();
int site_id=0;
String state="";
int h11=0;
boolean check=false;
String string[]=null;

ArrayList <TranBean>siteList=new ArrayList<TranBean>();
EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
siteList=empAttendanceHandler.getAssignedSitesList(eno);
System.out.println("size of site list : "+siteList.size());
String date1="";

String date2=request.getParameter("date2");

date2=request.getParameter("date")==null?request.getParameter("date2"):request.getParameter("date2");

date1=request.getParameter("date")==null?request.getParameter("date2"):request.getParameter("date");



if(roleId.equals("1"))
{
eoffbn = eoffhdlr.getEmpOfficAddInfo(Integer.toString(eno));

if(request.getParameter("site_id")!=null)
{
	tran = eoffhdlr.getEmpListForAttendance(request.getParameter("site_id"),date1);
	prjCode=Integer.parseInt(request.getParameter("site_id")); 
	session.setAttribute("Prj_Srno", prjCode);
}
else {
	

if(request.getParameter("prj")==null)
{
	 
	
	
	state="edit";
	tran = eoffhdlr.getEmpListForAttendance(Integer.toString(eoffbn.getPrj_srno()),date1);
	prjCode = eoffbn.getPrj_srno();
	session.setAttribute("Prj_Srno", prjCode);
}
else
{
	state="view";
	tran = eoffhdlr.getEmpListForAttendance(request.getParameter("prj"),date1);
	
	prjCode=Integer.parseInt(request.getParameter("prj")); 
	session.setAttribute("Prj_Srno", prjCode);

	
}
}
}
else{
				eoffbn = eoffhdlr.getEmpOfficAddInfo(Integer.toString(eno));
				
				if(siteList.size()>0)
				{
				
				for(TranBean tranBean:siteList){
				
				if(request.getParameter("site_id")==null)
				{
					
					state="edit";
					tran = eoffhdlr.getEmpListForAttendance(Integer.toString(eoffbn.getPrj_srno()),date1);
					prjCode = eoffbn.getPrj_srno();
					session.setAttribute("Prj_Srno", prjCode);
				}
				else
				{
					prjCode = eoffbn.getPrj_srno();
					if(tranBean.getSite_id().equals(request.getParameter("site_id")) ||  (request.getParameter("site_id")).equals(Integer.toString(prjCode))  ){
					state="view";
					tran = eoffhdlr.getEmpListForAttendance(request.getParameter("site_id"),date1);
					
					prjCode=Integer.parseInt(request.getParameter("site_id")); 
					session.setAttribute("Prj_Srno", prjCode);
					}
				}
				}
				}
				else{
					state="edit";
				tran = eoffhdlr.getEmpListForAttendance(Integer.toString(eoffbn.getPrj_srno()),date1);
				prjCode = eoffbn.getPrj_srno();				
				session.setAttribute("Prj_Srno", prjCode);
			}
	
}



session.setAttribute("emplist", tran);



ArrayList<String> holidays= new ArrayList<String>();
ArrayList<String> weekdays= new ArrayList<String>();

SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");


EmpAttendanceHandler EAH=new EmpAttendanceHandler();
String today=EAH.getServerDate();


String Site_Id=request.getParameter("site_id");

if(request.getParameter("site_id")==null){
Site_Id=request.getParameter("prj");

}


//String st=EAH.getstatus(Site_Id,date1);


HolidayMasterHandler hmh = new HolidayMasterHandler();

String fromDate="";
String type=" ";

/* if(st.equals("saved") || st.equals("approved") || st.equals("rejected") ) */{	
weekdays=hmh.getweekoff(date1 , prjCode);

if(weekdays.size()==0)
{
	weekdays=hmh.getweekoff(date1); 
}

System.out.println("weekdays"+weekdays);
holidays = hmh.getHoldmast(date1,prjCode);
}
/* if(holidays.size()==0)
{
	//holidays = hmh.getHoldmast(date1,"ALL");
} */
		//System.out.println("HOLIDAY IS ON"+holidays);




int serverDt=Integer.parseInt(today.substring(0,2));
String serverMonth=today.substring(3,11); 



System.out.println("SERVER DATE"+today);

ArrayList Emp_al=new ArrayList();
System.out.println("THE PRJCODE IS :"+prjCode);
Emp_al=EAH.getEmpAttend(date1, prjCode, eno,state,tran);
System.out.println("EMPLOYEE_AL SIZE"+ Emp_al.size());
float days=Calculate.getDays(date1);



emplist=lkh.getSubLKP_DESC("ET");	// ET is Code for Employee Type
String status="";
if(date1!=null)
{
 status=EAH.getAttendanceStatus(date1, prjCode);
}
//System.out.println("this is status"+status);

try
{  
	
	try
	{
	flag=Integer.parseInt(request.getParameter("flag")==null?"":request.getParameter("flag")); 
	}catch(Exception e)
	{
		System.out.println("no flag value"+flag);
	}
	
}
catch(Exception e)
{
	System.out.println("First Time Loading");
}

 
%>

<script>
	jQuery(function() {
		$("#pp").autocomplete("projlist.jsp");
	});
	
	
	 function  noBack()
	{
		window.history.forward();
	}

	
	setTimeout("noBack()", 0);
	window.onunload = function() 
	{
	    null
	}; 
	
</script>
<style type="text/css">

.ac_results {
	padding: 0px;
	border: 1px solid #cacaca;
	background-color: #f5f6fa;
	overflow: hidden;
	width: 58% !important;
}

</style>

<script type="text/javascript">
function enableTextBoxes()
{
	var flag=document.getElementById("edit").value;
	var flag1;
	
	
		var r = confirm("Do you want to "+flag+" records!");
		if (r == true)
		{
			if(flag=="Edit")
			{
				flag1=false;
			document.getElementById("edit").value="Disable";
			}
		else
		{
			flag1=true;
			document.getElementById("edit").value="Edit";
		}
			

	
	
	
	<%
	
	if(Emp_al.size()!=0){
	for(int j=0;j<Emp_al.size();j++)
		
	{
		
		
		ArrayList<TranBean> Emp_bean=(ArrayList<TranBean>)Emp_al.get(j);
		for(int c=0;c<Emp_bean.size();c++)
		{
			TranBean ab=new TranBean();
			ab=Emp_bean.get(c);
		%>
		
			
		var adv = document.getElementsByName("_<%=ab.getEMPNO()%>");
		for(var i=0; i<adv.length; i++)
		{
			document.getElementById(adv[i].id).disabled=flag1;
			
		}
		
		<%
			
		}	
		} }%>
	}
}



$(document).ready(function () {
   
	
	
	$("#adminSave").click(function(){
		
		var r = confirm("Are you sure to Save Attendance Sheet?");
		
		if (r == true) {
			$('input:text').removeAttr("disabled");
			return true;
		
		} else{
			return false;
		}
	});
	

    $('#edit').click(function () {
    	
    	if($('input:text').attr("disabled"))
    		{
    	$('input:text').attr("disabled",false);
    	$('#edit').attr('value','Disable');
    		}
    	else
    		{
    		$('input:text').attr("disabled",true);
        	$('#edit').attr('value','Edit');
    		}
    });
/* 
    $('#tranSave').click(function () 
    		{
   	$('input:text').removeAttr("disabled"); 
    	
    	
    		}); */
   
    $('input:text').focus(function () 
    		{
    	
    		$(this).css("background-color", "yellow");
    
    		});
    		
     $('input:text').blur(function () 
    		{
    	
    		$(this).css("background-color","Bisque");
    
    		}); 
});


jQuery(function() {
	
	var checkboxes = $("input[type='checkbox']"),
    submitButt = $("input[type='submit']");

checkboxes.click(function() {
    submitButt.attr("disabled", !checkboxes.is(":checked"));
});


$("#send").click(function(){
	  
	var result=  updateCount();
	
	<%ArrayList<TranBean> Emp_bean1=null; 
	if(Emp_al.size()!=0){
	for(int j=0;j<Emp_al.size();j++)
	{
		
		
		 Emp_bean1=(ArrayList<TranBean>)Emp_al.get(j);

		for(int c=0;c<Emp_bean1.size();c++)
		{
			TranBean ab=new TranBean();
			ab=Emp_bean1.get(c);
		}
	}
	}
	int k=0;
	if(Emp_al.size()!=0){
	 k=(Emp_al.size()*Emp_bean1.size());
	}
		%>
 
	  function updateCount () {
	    var count = $("input[type=checkbox]:checked").size();
	  
	  
	    if(count==<%=Emp_al.size()%>){
	    	  
	     return true;
	     }
	     
	     else{
	    	 
	    	 alert(" Please select all if you want to send End Of Month");
	     	return false;
	     	
	     }
};

			var cnt=0;
			var sd=0;
			var a=0;
		
			
			
			/* 	sd=parseInt(sd); */
			for(sd=0;sd<<%=k%>;sd++)
				{
				if(document.getElementById(sd).value==" "||document.getElementById(sd).value==""  )
				{
				cnt++;
				 //alert("blank!!!!!!!!!!!!");
				 break;
				}else{
					a++;
					//alert(a);
				}
		
				}
			
			
			if(cnt>0 && result==true)
			{
			
				
			alert("Blank attendance can't be Send for Approval !\n\n\nPlease Fill some Value!");
			return false;
			}
			else if(cnt==0 && result==true)
			{
			
				var date2 = document.getElementById("adddate").value;
			var r = confirm("Are you sure to send for Approval... ?");
			if (r == true) {
				window.location.href="EmpAttendServlet?action=approval&status=saved&date="+date2;
			} 
			
			}

		});

	
	//old code for transfer .. new one is same in left code
	/*$("#tr").click(function(){
	
		

			var result=  updateCount();
		 
			  function updateCount () {
			    var count = $("input[type=checkbox]:checked").size();
			     if(count==0){
			    	alert("At least select one employee");
			    	 return false;
			     }else{
			    	 return true;
			     }
		};
		if(result==true)
		var r = confirm("Are you sure to send to transfer of these employee ?");
		 
		if (r == true && result==true) {
			return true;
	
		} else{
			return false;
		}
	
	});*/
	/* $("#Left").click(function(){
		
		
		
		var result=  updateCount();
	 
		  function updateCount () {
		    var count = $("input[type=checkbox]:checked").size();
		    
		     if(count==0 ){
		    	 alert("At least select one employee");
		    	 return false;
		     }else{
		    	 return true;
		     }
	};
	if(result==true){
		alert("Please be sure that you have filled entire attendance till the left date you would be entering.");
		var r = confirm("Are you sure to send to left of these employee ?");
		if (r == true && result==true) {
			return true;
	
		} else{
			return false;
		}
	}
	}); */
$(".Left").click(function(){
		
		
		 var val=( $(this).val() ); 
		 var chekval="";
		 var lf="";
		var sheetstatus=document.getElementById("sheetstatus").value;
		var sheetenddate=document.getElementById("sheetenddate").value;
 		var leftdate=document.getElementById("adddate").value;
 		var currentdate=document.getElementById("currentdate").value;
		var result =  updateCount();
		 var resp="";
		if(sheetstatus == "saved"||sheetstatus == "0" ||sheetstatus=="rejected")
		{
			if(result == true)
	 		{
	 		
	 					if(leftdate=="")
	 				{
	 					alert("please select the "+val+" date...then click on "+val+" button");
	 					return false;  //for no selecting left date
	 				}
	 				else												//for validating left date....@
	 				{
	 					
	 					 var livedate=currentdate.split("-");
	 					var blankattendance="";
	 					var checkcurrentdate=sheetenddate.split("-");
	 					var currentmonth=checkcurrentdate[1];
	 					var checkleftdate=leftdate.split("-");
	 					var leftmonth=checkleftdate[1];
	 					var leftyear=checkleftdate[2];
	 					
	 					var yeardiff=checkcurrentdate[2] - leftyear;
	 					
	 					var leftday=checkleftdate[0];
	 					var lastmonthno=parseInt(getlastmonthno(leftmonth));
	 					var currentmonthno=parseInt(getlastmonthno(currentmonth));
	 					var monthdiff=currentmonthno - lastmonthno;
	 					
	 					var lastday=parseInt(getlastday(checkleftdate[1],checkleftdate[2]));
	 					var checkedValue = $("input[type=checkbox]:checked").val();
	 					chekval=checkedValue;
	 					document.getElementById("DOL").value="";
	 					lf=leftcheck(chekval);
						 resp=getresponse(leftdate);
						 blankattendance = parseInt(checkblankattendance(checkedValue,sheetenddate,leftdate));
						 var doj	= getDOJ(checkedValue);
						
						var dj = new Date(document.getElementById("DOJ").value);
						var lt=new Date(checkleftdate[2]+"-"+lastmonthno+"-"+checkleftdate[0]);
						alert("Please Make Sure You Have Entered All Attendance Till "+val+" Date");
						dj=new Date(document.getElementById("DOJ").value);
						
						 
						if(lt.getTime()<=dj.getTime())
							{
							
							alert(val+" Date Should Be Greater Than DOJ......");
							document.getElementById("adddate").value="";
							document.getElementById("adddate").focus;
							return false;
							}
						else if(parseInt(document.getElementById("blankatt").value) >=1 && monthdiff >= 0)
							{
								if(monthdiff>0)
									{
									alert("You Have Not Filled Attendance In Previous Month Till "+val+" Date... Please First Fill The Attendance Till "+val+" Date.... ");
									document.getElementById("adddate").value="";
									document.getElementById("adddate").focus;
									result=false;
									}
								else{
									alert("You Have Not Filled Attendance Till "+val+" Date... Please First Fill The Attendance Till "+val+" Date.... ");
									document.getElementById("adddate").value="";
									document.getElementById("adddate").focus;
									result=false;
								}
							}
						else
						{
							
	 				if((monthdiff==0|| monthdiff==1)&&(yeardiff==0 || yeardiff==1)||(yeardiff==1 && monthdiff==-11))
	 				{
	 					//alert("leftdaysssss.."+leftday+"....lastday..."+lastday);
			 				if(((monthdiff==1 ||(monthdiff==-11 && yeardiff==1)) && (leftday<lastday)))
			 					{
			 						alert(""+val+" Date Should Not Earlier Than Last Day Of Prevoius Month... ");
			 						document.getElementById("adddate").value="";
									document.getElementById("adddate").focus;
			 						return false;
			 					}
			 				else   //checked whether sheet is in previous month or currrent ..if previous then leftdate is upto lastday of privous month and  EOM(sheetdate)..if it is i currnt month then retricted to select leftdate from lastday of previous month to currentday of current month
			 			{
			 					/* alert("livedate[1]..."+livedate[1]);
			 					alert("checkcurrentdate[1]..."+checkcurrentdate[1]);
			 					alert("livedate[2]..."+livedate[2]);
			 					alert("checkcurrentdate[2]..."+checkcurrentdate[2]);
			 					alert("leftday.."+leftday);
			 					alert("livedate[0]..."+livedate[0]);
			 					alert("monthdiff..."+monthdiff);
			 					alert("yeardiff..."+yeardiff);
			 					alert("leftday..."+leftday);
			 					alert("lastday.."+lastday); */
			 					
			 					if((livedate[1]==checkcurrentdate[1] && livedate[2]==checkcurrentdate[2] && leftday<lastday && monthdiff<0) || (yeardiff>=1 &&(monthdiff<=0 && (leftday<lastday))))			//if sheet is in currrent month then leftdate should be < current day
		 						{
		 						alert(""+val+" Date Should Be Atleast Between Last Day Of Previous Month & Current Date...!!!\nNot Greater Than Current Date...!!!");
		 						result=false;
		 						}
			 				else if(monthdiff==0  && leftday > checkcurrentdate[0])
								{
									alert(""+val+" Date Should Not Greater Than End  Date Of Sheet Created Month... ");
									return false;
								}
			 				else if(monthdiff==0 && leftday < checkcurrentdate[0] || monthdiff==1 && lastday==leftday)
			 					{
			 				
			 						if(leftday==01&&val=="Left")
			 							{
			 							alert("Do You Want To "+val+" This Employee On "+leftdate+" Then Please Select The Last Date Of Previous Month ");
			 							document.getElementById("adddate").value="";
			 							document.getElementById("adddate").focus;
			 							result= false;
			 							}
			 						else
			 							{
					 						/*var result1=confirm("Do You Really Want To Left This Employee On - "+leftdate);
					 						if(result1==true)
					 							{
					 							return true;
					 							}
					 						else
					 							{
					 							return false;
					 							}*/
					 							result=true;
			 							}
			 					}
			 			}
	 				}
	 				
	 				else if(monthdiff >=1 && (yeardiff==0||yeardiff>=1)&&yeardiff<=1)
	 					{
	 						//alert("yeardiff.."+yeardiff);
	 							if(monthdiff >=1 && (yeardiff==0||yeardiff>=1))
	 								{
	 								alert("You Can Not "+val+" This Employee In Previous Month...!!!\nEither Open Previous Month Attendance Sheet & Then "+val+" The Employee...!!!    Or \n Enter The "+val+" Date Between Last Day Of Previous Month & End  Date Of Sheet Created Month If You Want To "+val+" Him In This Month...!!!");
		 							document.getElementById("adddate").value="";
		 							document.getElementById("adddate").focus;
		 							result= false;
	 								}
	 							if(monthdiff<0 && (yeardiff==0||yeardiff>=0))
 								{
	 								//alert("monthdiff...."+monthdiff+"...yeardiff..."+yeardiff);
	 								if(monthdiff < 0 && (yeardiff>=1))
	 									{
		 									alert("You Can Not "+val+" This Employee In Previous Month...!!!\nEither Open Previous Month Attendance Sheet & Then "+val+" The Employee...!!!    Or \n Enter The "+val+" Date Between Last Day Of Previous Month & End  Date Of Sheet Created Month If You Want To "+val+" Him In This Month...!!!");
				 							document.getElementById("adddate").value="";
				 							document.getElementById("adddate").focus;
				 							result= false;
	 									}
	 								else
	 									{
		 									alert("You Can Not "+val+" This Employee In Next Month...!!!\nEither Open Next Month Attendance Sheet & Then "+val+" The Employee...!!!    Or \n Enter The "+val+" Date Between Last Day Of Previous Month & End  Date Of Sheet Created Month If You Want To "+val+" Him In This Month...!!!");
		 									document.getElementById("adddate").value="";
		 									document.getElementById("adddate").focus;
		 									result= false;
	 									}
 								}
	 					}
	 				else
	 					{
	 					alert("Please Select Correct Left Date !!!");
	 					document.getElementById("adddate").value="";
						document.getElementById("adddate").focus;
						result= false;
	 					}
						}//}
	 			}//@
	 		}
	 		else
	 			{
	 			result=false;
	 			}
		 }
		 else
		 {
		 	result= false;
		 }
			//var r = confirm("Are you sure to send to left of these employee ?");
			if ( result==false) {
				return false;
		
			} else{
				
				/* var resp=getresponse(leftdate); */
				if(parseInt(document.getElementById("response").value)==1)
					{
						alert(leftdate+" is Week Off Or Holiday You can not Mark as "+val+" !! \nPlease select another date !!..");
						document.getElementById("adddate").value="";
						document.getElementById("adddate").focus;
						return false;
					}
				else
					{
						
						 //document.getElementById("DOL").value="";
					if((lf=="left")||(document.getElementById("DOL").value=="left"))
							{
							alert("Employee Is Already Left,Cant Send Request Again...!");
							return false;
							}
						else{ 
							
						var result1=confirm("Do You Really Want To "+val+" This Employee On - "+leftdate);
							if(result1==true)
								{
								return true;
								}
							else
								{
								return false;
								}
						 } 
					}
			}
			
			
			function getlastmonthno(month)
			{
				if(month.toUpperCase()=="JAN" ){return "1";}
				else if(month.toUpperCase()=="FEB" ){return "2";}
				else if(month.toUpperCase()=="MAR" ){return "3";}
				else if(month.toUpperCase()=="APR" ){return "4";}
				else if(month.toUpperCase()=="MAY" ){return "5";}
				else if(month.toUpperCase()=="JUN" ){return "6";}
				else if(month.toUpperCase()=="JUL" ){return "7";}
				else if(month.toUpperCase()=="AUG" ){return "8";}
				else if(month.toUpperCase()=="SEP" ){return "9";}
				else if(month.toUpperCase()=="OCT" ){return "10";}
				else if(month.toUpperCase()=="NOV" ){return "11";}
				else if(month.toUpperCase()=="DEC" ){return "12";}
			};
			
			function getlastday(month,year)
			{
				var yr=parseInt(year);
				if(month.toUpperCase()=="JAN" ){return "31";}
				else if(month.toUpperCase()=="FEB" ){
					if(yr%4==0){return "29";}
					else {return "28";}}
				else if(month.toUpperCase()=="MAR" ){return "31";}
				else if(month.toUpperCase()=="APR" ){return "30";}
				else if(month.toUpperCase()=="MAY" ){return "31";}
				else if(month.toUpperCase()=="JUN" ){return "30";}
				else if(month.toUpperCase()=="JUL" ){return "31";}
				else if(month.toUpperCase()=="AUG" ){return "31";}
				else if(month.toUpperCase()=="SEP" ){return "30";}
				else if(month.toUpperCase()=="OCT" ){return "31";}
				else if(month.toUpperCase()=="NOV" ){return "30";}
				else if(month.toUpperCase()=="DEC" ){return "31";}
			};
			
			
			function getresponse(leftdate)
			{
				var xmlhttp=new XMLHttpRequest(); ;
			 	var url="";
			 	var rs=true;
			 	var rs1="";
			 	var response1 =" ";
			 	url="CoreServlet?action=leftDT&date="+leftdate;
				
						xmlhttp.onreadystatechange=function()
						{
							if(xmlhttp.readyState==4)
							{
								response1 = parseInt(xmlhttp.responseText);
								document.getElementById("response").value=response1;
							}
						};
						
						xmlhttp.open("POST", url, true);
						xmlhttp.send();
				return response1;
			};
			
			
			
			function getDOJ(empno)
			{
				var xmlhttp=new XMLHttpRequest(); ;
			 	var url="";
			 	var rs=true;
			 	var rs1="";
			 	var response1 ="";
			 	url="EmployeeServlet?action=getDOJ&empno="+empno;
				
						xmlhttp.onreadystatechange=function()
						{
							if(xmlhttp.readyState==4)
							{
								response1 = xmlhttp.responseText;
								document.getElementById("DOJ").value=response1;
							}
						};
						
						xmlhttp.open("POST", url, true);
						xmlhttp.send();
				return response1;
			};
			
			
			function leftcheck(empno)
			{
				
				var xmlhttp=new XMLHttpRequest(); ;
			 	var url="";
			 	var rs=true;
			 	var rs1="";
			 	var response1 =" ";
			 	url="EmployeeServlet?action=leftcheck&empno="+empno;
				
						xmlhttp.onreadystatechange=function()
						{
							if(xmlhttp.readyState==4)
							{
								response1 = xmlhttp.responseText;
								
								document.getElementById("DOL").value=response1;
								
								
							}
						
						};
						
						xmlhttp.open("POST", url, true);
						xmlhttp.send();
						
						
						
				return (document.getElementById("DOL").value);
			};
			
			function checkblankattendance(empno,sheetenddate,leftdate)
			{
				var xmlhttp=new XMLHttpRequest(); ;
				 	var url="";
			 		var response1 ="";
			 		
			 		url="EmpAttendServlet?action=checkattendance&empno="+empno+"&sheetendate="+sheetenddate+"&adddate="+leftdate;
				
						xmlhttp.onreadystatechange=function()
						{
							
							if(xmlhttp.readyState==4)
							{
								response1 = xmlhttp.responseText;
								document.getElementById("blankatt").value=response1;
								
							}
						
						};
						xmlhttp.open("POST", url, true);
						xmlhttp.send();
						
						
			 	
			 	return response1;
			};
		//}
		
		  
		  function updateCount (){
		    var count = $("input[type=checkbox]:checked").size();
		    
		     if(count==0 ){
		    	 alert("At least select one employee");
		    	 result = false;
		     }else{
		    	 	result= true;
		     }
		     return result;
	};
	
});
	
	
$("#apr").click(function(){
		
		
		var result=  updateCount();
	 
		  function updateCount () {
		    var count = $("input[type=checkbox]:checked").size();
		    
		    /*  if(count==0 ){
		    	 alert("Select one employee only");
		    	 return false;
		     } */
		     
		    if(count>1){
		    	 alert("Select one employee only");
		    	 return false;
		     }
		     else{
		    	 return true;
		     }
		     
	};
	if(result==true)
		var r = confirm("Are you sure to send to Approve for this employee ?");
		  
		if (r == true && result==true) {
			
			
			var checkedemp = null; 
			var inputElements = document.getElementsByClassName('empCheckbox');
			for(var i=0; inputElements[i]; ++i){
			      if(inputElements[i].checked){
			    	  checkedemp = inputElements[i].value;
			           break;
			      }
			}
			
		var textValue=document.getElementsByClassName("_"+checkedemp);
				
		for(var i=0; textValue[i]; ++i){
		      if(!textValue[i].value){
		    	  alert("blank attendance cann't be sent to approval");
		    	return false;
		      }
		     
		}
			
			
	
		} else{
			return false;
		}
		
	
		
	
	
	});
	
	
	
$("#tranSave").click(function(){
	<%
	if(status.equals("saved") || status.equals("approved") || status.equals("rejected") || status.equals("pending")  ){
	%>
	var date=document.getElementById("column").value;
	if(date=="" || date==" " || date==null){
		alert("Please enter the date beacuse you must save attendance of a single date only");
		document.forms["attdn"]["column"].focus();
		
		return false;
		
	}
	<%}%>
	<%if(prjCode==0||String.valueOf(prjCode)==""){%>
		alert("Please Select Project Site !!!");
		return false;
	<%}%>
	
	//for restriction of attendance to admin 
	<%String todaysDate=empAttendanceHandler.getServerDate();
	Calendar calendar=Calendar.getInstance();
	SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy");
	calendar.add(Calendar.MONTH, 1);
	calendar.set(Calendar.DATE, 1);
	String nextMonthDate=SDF.format(calendar.getTime());
	
	Calendar cal=Calendar.getInstance();
	
	cal.add(Calendar.MONTH, -1);
	cal.set(Calendar.DATE, 1);

	String previousMonth= SDF.format(cal.getTime());
	
	
	
	%>
	
	var todayDate="<%=todaysDate.substring(3,11)%>";
	var MonthDate="<%=nextMonthDate.substring(3,11)%>";
	var previousDate="<%=previousMonth.substring(3,11)%>";
	var adddate=document.getElementById("adddate").value;
	var date=adddate.substring(3,11);
	
	
	
	if(todayDate==date||MonthDate==date || previousDate==date){
		var r = confirm("Are you sure to create a new Attendance Sheet?");
		
		if (r == true) {
			$('input:text').removeAttr("disabled");
		document.getElementById("process").hidden=false;	
			return true;
		
		} else{
			return false;
		}
	}
	else{
		alert("You can not create the Attendance sheet of  "+date+"..!!!");
		return false;
	}
	});

	$("#btn").click(function(){

		  
		
		  
			
		
		var i= document.getElementById("column").value;
		if(i==""){
    alert("Please enter the date and then only click");
    return false;
		}
		 
		<%
		
			int K=0;
		int totalDay=0;
		int totalEmp=0;
			if(Emp_al.size()!=0){
			  totalEmp= Emp_al.size();
			
				 totalDay=	Emp_bean1.size();
			}	
			 K= (totalDay/2);
			 if(totalDay==28||totalDay==29)
				{
					K=K+1;
					
				}
		
		int w=((totalEmp)*(K));
				
		int q=((((totalEmp)*(totalDay))));
		//System.out.println("me k ahe         "+K +" " +w);		%>
	

		var y=<%=(totalEmp*K)-16%>;
		
		if(i<=<%=totalDay%>){
		<% if(roleId.equals("1"))
			{
		%>
		 document.getElementById("column").readOnly=true;
	
			
		
		if(i>15 &&<%=totalDay%>==30)
			{
		
			var j=parseInt(y)+parseInt(i);
		
	 for( j=j;j<<%=q%>; j=j+<%=K%>){ 
				
				if(document.getElementById(j).value=="" || document.getElementById(j).value==50 )

			document.getElementById(j).value="P";
				}
			}
		else if(i>15 &&<%=totalDay%>==31)
		{
	
		var j=parseInt(y)+parseInt(i);
	
	
	 for( j=j;j<<%=q%>; j=j+<%=K+1%>){ 
			
			if(document.getElementById(j).value=="" || document.getElementById(j).value==" "  )
		document.getElementById(j).value="P";
			}
		}
		
		else if(i>15 &&<%=totalDay%>==28)
		{
	
		var j=parseInt(y)+parseInt(i);
	
	
	 for( j=j;j<<%=q%>; j=j+<%=K-2%>){ 
			
			if(document.getElementById(j).value=="" || document.getElementById(j).value==" "  )

		document.getElementById(j).value="P";
			}
		}
		
				else if(i>15 &&<%=totalDay%>==29)
				{
	
					var j=parseInt(y)+parseInt(i);
					for( j=j;j<<%=q%>; j=j+<%=K-1%>){ 
					if(document.getElementById(j).value=="" || document.getElementById(j).value==" "  )
					document.getElementById(j).value="P";
					}
				}
			
				
			else{
				for( i=i-1;i<<%=w%>; i=i+<%=K%>){ 
				if(document.getElementById(i).value=="" || document.getElementById(i).value==" "  )
				document.getElementById(i).value="P";
				}
			}
		
	<%}
		else {%>
		 
	  				var date=<%=serverDt%>;
	  				var currentMonth="<%=serverMonth%>";
	  				var adddate=document.getElementById("adddate").value;
	  				var month=adddate.substring(3,11);
					
	  				var d1=date-1;
					var d2= d1-1;
					var d3=d2-1;
					var d4=d3-1;
					var d5=d4-1;
					if(i==date && month==currentMonth || i==d1&& month==currentMonth|| i==d2 && month==currentMonth
							|| i==d3 && month==currentMonth ||  i==d4 && month==currentMonth	 ||  i==d5 && month==currentMonth)
					{
							
						document.getElementById("column").readOnly=true;
						if(i>15 &&<%=totalDay%>==30)
							{
						
							var j=parseInt(y)+parseInt(i);
						
					
						 for( j=j;j<<%=q%>; j=j+<%=K%>){ 
								
								if(document.getElementById(j).value=="" || document.getElementById(j).value==" "||document.getElementById(j).value==null )
			
							document.getElementById(j).value="P";
								}
							}
						else if(i>15 &&<%=totalDay%>==31)
						{
					
						var j=parseInt(y)+parseInt(i);
					
					
					 for( j=j;j<<%=q%>; j=j+<%=K+1%>){ 
							
							if(document.getElementById(j).value=="" || document.getElementById(j).value==" "  )
						document.getElementById(j).value="P";
							}
						}
						
						else if(i>15 &&<%=totalDay%>==28)
						{
					
						var j=parseInt(y)+parseInt(i);
					
					
					 for( j=j;j<<%=q%>; j=j+<%=K-2%>){ 
							
							if(document.getElementById(j).value=="" || document.getElementById(j).value==" "  )
			
						document.getElementById(j).value="P";
							}
						}
						
								else if(i>15 &&<%=totalDay%>==29)
								{
					
									var j=parseInt(y)+parseInt(i);
									for( j=j;j<<%=q%>; j=j+<%=K-1%>){ 
									if(document.getElementById(j).value=="" || document.getElementById(j).value==" "  )
									document.getElementById(j).value="P";
									}
								}
							
								
							else{
								for( i=i-1;i<<%=w%>; i=i+<%=K%>){ 
								if(document.getElementById(i).value=="" || document.getElementById(i).value==" "  )
								document.getElementById(i).value="P";
								}
							}
						}
			else{
				alert("You dont have the authentication for date "+i+"-"+month+" ..! ");
				}
		<%}%>
		}
		else{
			alert(+i+" ..is Invalid Date..!");
			}
	});
	

	
  $("#btn2").click(function(){
   
	  var i= document.getElementById("column").value;
		if(i==""){
    alert("Please enter the date and then only click");
    return false;
		}
		<%
		
			int K1=0;
		int totalDay1=0;
		int totalEmp1=0;
			if(Emp_al.size()!=0){
			  totalEmp1= Emp_al.size();
			
				 totalDay1=	Emp_bean1.size();
			}	
			 K1= (totalDay1/2);
			 if(totalDay1==28||totalDay1==29)
				{
					K1=K1+1;
					
				}
		
		int w1=((totalEmp1)*(K1));
				
		int q1=((((totalEmp1)*(totalDay1))));
		//System.out.println("me k ahe         "+K +" " +w);		%>
	

		var y1=<%=(totalEmp1*K1)-16%>;
		
		if(i<=<%=totalDay1%> && i>0){
		<% if(roleId.equals("1"))
			{
		%>
		
		var flag=document.getElementById("btn2").value;
		var flag1="";
		
		
			var r = confirm("Do you want to "+flag+" records!");
			if (r == true)
			{
				if(flag=="Edit")
				{
					flag1=false;
				document.getElementById("btn2").value="Save";
				document.getElementById("btn").disabled=false;
				
				
				<%if(!status.equals("pending")){%>
				document.getElementById("checkAll").disabled=true;
				document.getElementById("send").disabled=true;
				<%}%>
				document.getElementById("selectProject").disabled=true;
				document.getElementById("pp").disabled=true;
			
				
				  document.getElementById("column").readOnly=true;
				
				}
			else if(flag=="Save")
			{
				flag1=true;
				document.getElementById("btn2").type="submit";
				document.getElementById("btn2").id="tranSave";
				document.getElementById("btn2").value="Edit";
			}
				
				if(i>15 &&<%=totalDay1%>==30)
			{
			
			var j=parseInt(y1)+parseInt(i);
		
			 for( j=j;j<<%=q1%>; j=j+<%=K1%>){ 
				
		 document.getElementById(j).disabled=flag1;
		 document.getElementById(j).style.background="Bisque";
				}
			}
			
		else if(i>15 &&<%=totalDay1%>==31)
		{
			var j=parseInt(y1)+parseInt(i);
		 for( j=j;j<<%=q1%>; j=j+<%=K1+1%>){ 
			
		 document.getElementById(j).disabled=flag1;
		 document.getElementById(j).style.background="Bisque";
			}
		}
		else if(i>15 &&<%=totalDay1%>==28)
		{
		var j=parseInt(y1)+parseInt(i);
	 for( j=j;j<<%=q1%>; j=j+<%=K1-2%>){ 
			
		 document.getElementById(j).disabled=flag1;
		 document.getElementById(j).style.background="Bisque";
			}
		}
		
				else if(i>15 &&<%=totalDay1%>==29)
				{
	
					var j=parseInt(y1)+parseInt(i);
					for( j=j;j<<%=q1%>; j=j+<%=K1-1%>){ 
						document.getElementById(j).disabled=flag1;
						document.getElementById(j).style.background="Bisque";
					}
				}
				
			else{
				
							for( i=i-1;i<<%=w%>; i=i+<%=K%>){ 
			
							document.getElementById(i).disabled=flag1;
							document.getElementById(i).style.background="Bisque";
							}
						
					}
				
			
			}
		
	<%} 
		
		// FOR SITE ADMIN
		else {%>
		
		
		var date=<%=serverDt%>;
			var currentMonth="<%=serverMonth%>";
			var adddate=document.getElementById("adddate").value;
			var month=adddate.substring(3,11);
		
			var d1=date-1;
		var d2= d1-1;
		var d3=d2-1;
		var d4=d3-1;
		var d5=d4-1;
		if(i==date && month==currentMonth /* || i==d1&& month==currentMonth || i==d2 && month==currentMonth || i==d3 && month==currentMonth
				|| i==d4 && month==currentMonth || i==d5 && month==currentMonth */)
		{
				
			var flag=document.getElementById("btn2").value;
			var flag1;
			
			
				var r = confirm("Do you want to "+flag+" records!");
				if (r == true)
				{
					if(flag=="Edit")
					{
						flag1=false;
					document.getElementById("btn2").value="Save";
					document.getElementById("btn").disabled=false;
					document.getElementById("checkAll").disabled=true;
					document.getElementById("send").disabled=true;
				
					document.getElementById("column").readOnly=true;
					}
				else if(flag=="Save")
				{
				flag1=true;
					document.getElementById("btn2").type="submit";
					document.getElementById("btn2").id="tranSave";
					document.getElementById("btn2").value="Edit";
				}
			if(i>15 &&<%=totalDay1%>==30)
				{
				
				var j=parseInt(y1)+parseInt(i);
			
		
			 for( j=j;j<<%=q1%>; j=j+<%=K1%>){ 
				 document.getElementById(j).disabled=flag1;
				 document.getElementById(j).style.background="Bisque";
				
					}
				}
			
			else if(i>15 &&<%=totalDay1%>==31)
			{
				
				
			var j=parseInt(y1)+parseInt(i);
		
		
		 for( j=j;j<<%=q1%>; j=j+<%=K1+1%>){ 
				
			 document.getElementById(j).disabled=flag1;
			 document.getElementById(j).style.background="Bisque";
				}
			}
		
			else if(i>15 &&<%=totalDay1%>==28)
			{
				
			var j=parseInt(y1)+parseInt(i);
		
		
		 for( j=j;j<<%=q1%>; j=j+<%=K1-2%>){ 
				
			 document.getElementById(j).disabled=flag1;
			 document.getElementById(j).style.background="Bisque";
				}
			}
		
					else if(i>15 &&<%=totalDay1%>==29)
					{
						
						var j=parseInt(y1)+parseInt(i);
						for( j=j;j<<%=q1%>; j=j+<%=K1-1%>){ 
							document.getElementById(j).disabled=flag1;
							document.getElementById(j).style.background="Bisque";
						}
					}
				
					
				else{
					for( i=i-1;i<<%=w1%>; i=i+<%=K1%>){ 
						document.getElementById(i).disabled=flag1;
						document.getElementById(i).style.background="Bisque";
					}
				}
			
		
			
			}
}
else{
	alert("You dont have the authentication for date "+i+"-"+month+" ..! ");
	}
<%}%>
		
		
		}
		else{
			alert(+i+" is Invalid Date..!");
			}
	});
	
});	
/* 
 function callback()
{
	if(xmlhttp.readyState==4)
	{
		var response = xmlhttp.responseText;
		document.getElementById("process").hidden=true;
	//	document.getElementById("process").style.display="block";
	}
}  */



function getTranDetails() {
	var date2=document.getElementById("adddate").value;
	var flag=true;
	if(date2=="")
		{
		alert("Please select Date To Open the Attendance Sheet of That Month...");
	return false;
		}
	
		var proj=document.getElementById("pp").value;
			var res = proj.indexOf(":"); 
			if(proj=="")
				{			
				alert("Please Select Project !");
				return false;
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
				window.location.href = "EmpPresentSeat.jsp?action=getdetails&prj="+prjCode+"&proj="+proj+"&date2="+date2,"_self";
				
			}
				}
			}
			
		}

function checkFlag() {
	
//	document.getElementById("process").hidden=true;
	for(var sd=0;sd<<%=k%>;sd++){
		
		if((document.getElementById(sd).value)=="LT" || document.getElementById(sd).value=="NJ" ){
	
			document.getElementById(sd).readOnly = true;
		}
	
	}
		var f = parseInt(document.getElementById("flag1").value);

		if (f == 1) {
			alert("Attendance saved Successfully");
			document.getElementById("process").hidden=true;
		}
		else if (f == 2) {
			alert("Something went wrong");
		}
		else if (f == 3) {
			alert("Something went wrong");
		}
		else if (f == 4) {
			alert("Successfully Sent for Approval!");
		}
		else if (f == 5) {
			alert("Rejected ! Please check the data and Resend for Approval!");
		}
		else if(f==6){
			alert("Attendance Sheet created successfully");
		}
		//it will work if uncment it also uncment from  singleapprove action on empattendservlet
		/* else if(f==7){
			var check_att_approved_emp = document.getElementById("check_att_approved_emp").value;
			alert("Attendance For "+check_att_approved_emp+" Is Already Approved (Single Approved), Cant send Single approve Request Again !!!");
		} */
		
		document.getElementById("div2").style.display='none';
	}

function fn(id1,id2)
{

document.getElementById(id1).style.display='none';
document.getElementById(id2).style.display='block';

}

/* function approve_att(prj,dt){

var r = confirm("Are you sure to Approve?");
if (r == true) 

window.location.href="EmpAttendServlet?action=approved&prjCode="+prj+"&date="+dt,"_self";

} */

function approve_att(prj,dt){
	
		var stat=checkpending(prj,dt);

	
	function checkpending(prj,fordate)
	{
		
		var siteid=document.getElementById("siteid").value;
	
		var xmlhttp = new XMLHttpRequest(); 
		url="EmpAttendServlet?action=checkpendingrequest&date="+fordate+"&siteid="+siteid;
		var flag="";
		xmlhttp.onreadystatechange=function()
		{
			if(xmlhttp.readyState==4)
			{
				var response1 = xmlhttp.responseText;
			
				if(response1=="true")
					{
						alert("There is pending request remains for this month... first  Approve Or Reject it & then approved attendance sheet...!!!");
						var cnfrm = confirm("Do you want to Approve/Reject This pending request.?");
						if(cnfrm == true)
							{
							window.location.href="attendanceMain.jsp?status=pending&date="+fordate+"&disableaprove=true&siteid="+siteid;
							}
						else
							{
								return false;
							}
					}
				else
					{
					//alert("single");
					var r = confirm("Are you sure to Approve?");
					if (r == true) 

					window.location.href="EmpAttendServlet?action=approved&status=approved&prjCode="+prj+"&date="+fordate,"_self";

					}
			
			}
			
		};
		xmlhttp.open("POST", url, true);
		xmlhttp.send();
		
	}
	
	return stat;
}

function reject_att(prj,dt){
var r = confirm("Are you sure to Reject ?");
if (r == true) 
window.location.href="EmpAttendServlet?action=reject&prj="+prj+"&date="+dt,"_self";

}

function inputLimiter(e,allow) {
		  var AllowableCharacters = '';
		  if (allow == 'Numbers'){AllowableCharacters='PpAaHhDd';}
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
		
function input(e,allow) {
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

	function check1()
	{
		var date1 = document.getElementById("date").value;
		if(date1=="")
			{
			alert("Please select date !");
			}
		else
			{
			window.location.href="EmpPresentSeat.jsp?date="+date1,"_self";
			}
		
	}

	
	function attd()
	{
		var r=confirm("Do you really want to exit?");
		if(r==true){
		window.location.href="attendanceMain.jsp?status=rejected","_self";
		}
		else{
			return false;
		}
	}
	function appr()
	{
		var r=confirm("Do you really want to exit?");
		if(r==true){
		window.location.href="approveAttendance.jsp?status=all","_self";
		}
		else{
			return false;
		}
	}

	
	
	function enableButton(){
		
		document.getElementById("send").disabled=false;
		document.getElementById("checkAll").disabled=false;
		
	}
	


	
</script>

<script type="text/javascript">
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

</script>
<!-- onunload="noBack()" -->
</head>
<body onLoad="checkFlag()" onunload="noBack()"  >
<%@include file="mainHeader.jsp"%>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<%@include file="subHeader.jsp"%>

<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer">
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Employees Attendance </h1>
	</div>
	<!-- end page-heading -->

	<table id="content-table" style="width: 100%;">
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

									<form name="attdn" action="EmpAttendServlet?action=insert&st=<%=status%>" method="post" onsubmit="validate()">

										
												

													<table id="customers" style="width: 95%;">
													<tr>
													<th colspan="3">Employee Present Days Details</th>
													</tr>
														<tr>
															<td>Project Name : 
															<%if(roleId.equals("1"))
																{
																if(request.getParameter("site_id")!=null){
																	ProjectListDAO pl=new ProjectListDAO();
																	ProjectBean pb=new ProjectBean();
																	pb=pl.getProjectInfo(Integer.parseInt(request.getParameter("site_id")));
																	
															%>	
																	<%=pb.getSite_Name() %>
															<% 	}
																
																else if(request.getParameter("prj")==null)
																	{
																%>
																<%=eoffbn.getPrj_name()%>
																<%
															}else
															{
																ProjectListDAO pl=new ProjectListDAO();
																ProjectBean pb=new ProjectBean();
																pb=pl.getProjectInfo(Integer.parseInt(request.getParameter("prj")));
																
																
																%>
																<%=pb.getSite_Name() %>
																<%
															}
																} else{
																	ProjectListDAO pl=new ProjectListDAO();
																	ProjectBean pb=new ProjectBean();
																	pb=pl.getProjectInfo(Integer.parseInt(request.getParameter("site_id")));
																	
																	%>
																	<%=pb.getSite_Name() %>
																	<%
																}%>
																
																
																
																</td>
															<%if(roleId.equals("1"))
									{%>
									<%-- <td>Select Date :
							
								<input name="adddate" size="20" id="adddate"  readonly="readonly" value="<%=date1 %>"  type="text">&nbsp;
								
							
								 </td> --%>
								 <td>Select Date :
									<%if(status.equalsIgnoreCase("pending")){ %>
								 <input name="adddate" size="20" id="adddate"  readonly="readonly" value="<%=date1 %>"  type="text">&nbsp; 
								 <%} else{%>
								<input name="adddate" size="20" id="adddate"  value="<%=date1%>" readonly="readonly"  type="text"  >&nbsp;
									<img src="images/cal.gif" align="middle"	style="vertical-align: middle; cursor: pointer;"	onClick="javascript:NewCssCal('adddate', 'ddmmmyyyy')" /> <%} %>
							
								 </td>
								<%}
								
								else{
								 %>
																<td> Date :
																<input name="adddate" size="20" id="adddate" value="<%=date1==null?"":date1%>"  readonly="readonly" type="text" onchange="check1()">&nbsp;
															
																</td>
														<%} %>			
															<td>
									
															
															<%if(status.equalsIgnoreCase("saved") || status.equalsIgnoreCase("rejected") || status.equalsIgnoreCase(""))
																{
																
																if(request.getParameter("prj")==null)
																{
																if(roleId.equals("1") && !status.equals("")){
																%>
																 <!-- 
																 <input type="button" id="edit"	value="Edit" onClick="enableTextBoxes()" height="150" width="150" /> 
																 <input type="submit" id="adminSave" value="Save"   /> -->
                                                             <%} 
                                                             if(status.equals("")){%>
                                                             
																<input type="submit" id="tranSave" value="Create Sheet"   />
														<% }
														else{%>
																<!-- <input type="submit" id="tranSave" value="Save"   /> -->
															<%
														}	
                                                           }
																else if(request.getParameter("prj")!=null)
																{
																
																	if(roleId.equals("1") && !status.equals("")){
																%>
																 
															<!--  <input type="button" id="edit"	value="Edit" onClick="enableTextBoxes()" height="150" width="150" /> 
															  <input type="submit" id="adminSave" value="Save" />  -->
                                                             <%}if(status.equals("")) {%>
                                                             	<input type="submit" id="tranSave" value="Create Sheet"/>
																 <%}else{ %>
																<!--  <input type="submit" id="adminSave" value="Save" />  -->
															
															<% }
                                                             }
																}
															else if(status.equalsIgnoreCase("pending")){
																
																
																
																
															if(request.getParameter("prj")!= null)
																{ if(roleId.equals("1")){
																
																%>
																 <!--  <input type="button" id="edit"	value="Edit" onClick="enableTextBoxes()" height="150" width="150" />
																    <input type="submit" id="adminSave" value="Save"/> -->
																    <input type="hidden" name="st" value='pending'>													
																<input type="button" id="" value="Approve" onclick="approve_att('<%=prjCode%>','<%=date1%>')">
																<input type="button" id="" value="Reject" onclick="reject_att('<%=prjCode%>','<%=date1%>')">
																<%}else{%>
																
																Already Send for Approval
																
																<%}
																}
															else if(request.getParameter("prj")== null)
															{ 
																
														if(roleId.equals("1")){	
																														
														 %>
																	<!-- 	<input type="button" id="edit"	value="Edit" onClick="enableTextBoxes()" height="150" width="150" />
																    	<input type="submit" id="adminSave" value="Save"/> -->
																
																	
																<input type="hidden" name="st" value='pending'>													
																<input type="button" id="" value="Approve" onclick="approve_att('<%=prjCode%>','<%=date1%>')">
																<input type="button" id="" value="Reject" onclick="reject_att('<%=prjCode%>','<%=date1%>')">
															
															<%}else{
														%>
														Already Send for Approval
														<%	
															
															}}
																else
																{%>
																Already Send for Approval
																<%	
																}
															}
															if(status.equalsIgnoreCase("approved"))
															{
																%>
																<!-- <input	type="button" value="Edit" id="edit" height="150" width="150" />
																<input type="submit" id="tranSave" value="Save"/> -->
																Successfully Approved
																<%	
															}
					
			 if(request.getParameter("prj")!=null &&  request.getParameter("proj")==null)
			 {
			%>
				
				<input type="Button" value="Back" onclick="appr()">
			<%}
			 
				
			 else  if(request.getParameter("prj")!=null && request.getParameter("proj")!=null)
			 {
			%>
				
				<input type="Button" value="Back" onclick="attd()">
			<%}
			 else
			{
				%>
				
				<input type="Button" value="Back" onclick="attd()">
			<%
			}
			
			%>
															 </td>
														</tr>
														
												<%if(roleId.equals("1"))
												{%>
												<tr>
												<td colspan="3">Select Project :
												<input type="text" id="pp" name="pp" style="width: 82%;"  onClick="this.select();" 
													placeholder="Enter a character to view the available project list (E.g - %)" 
												title="Enter a character to view the available project list (E.g - %) ">
												 <input type="Button" id="selectProject" value="Submit" onclick="getTranDetails()" />
												 </tr>
												
											<%}
											%>
												</table>
													
												
													
													
					<br>
					<%if(status.equals("saved") ||  status.equals("rejected") || status.equals("pending") ){	%>		
		<div align="left">											
	<font  style="color: maroon;" size="3"><b>	&nbsp;&nbsp;	 &nbsp;&nbsp;	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;	 &nbsp;&nbsp;	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	
	<%/* if(!roleId.equals("1")) */{ %>
	Enter the date: &nbsp;&nbsp;</b></font>
	<input type="text" id="column" name="column" placeholder="Enter Date"  style="border: solid; border-color: gray;"  maxlength="2" size="8"  onkeypress="return input(event,'Numbers')" >
	  	 &nbsp;&nbsp;	 
	    <input type="button" id="btn2"	value="Edit" onClick="" height="150" width="150" /> 
		 &nbsp;&nbsp;	 &nbsp;&nbsp;	
								
		<input type="button" id="btn" name="Button" value="Fill All Present" title="Click on the button to fill the present in all !" disabled="disabled" onclick=""/>			 																
								<% }
	if(status.equals("pending") &&(roleId.equals("1")))
								{%>
								<font  style="color: green;" size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									Came for Approval...!!</b></font>				
								<%}%>
						</div>		
					<%} else if(status.equals("pending") &&(!roleId.equals("1")))
								{%>
								<div align="left">											
						<font  style="color: maroon;" size="3"><b> &nbsp;&nbsp;	 &nbsp;&nbsp;	 &nbsp;
				</b></font>
							</div>
						
					<%} else if(status.equals("approved"))
					{%>
					<div align="left">											
			<font  style="color: maroon;" size="3"><b> &nbsp;&nbsp;	 &nbsp;&nbsp;	 &nbsp;
			</b></font>
				</div>
			
		<%} 
									else{ %>
											<div align="left">											
									<font  style="color: maroon;" size="3"><b> &nbsp;&nbsp;	 &nbsp;&nbsp;	 &nbsp;
										First create a new attendance sheet..! By "Create Sheet" Button </b></font>
										</div>
								<%} %>
											<center>
												<h3>
											
													
														&nbsp;&nbsp;&nbsp;&nbsp;
												DAYS-
												<input type="button" onclick="fn('div2','div1')" value="1-15" title="Click on the button to view Result of date 1 to 15 !"/>
												<input type="button" onclick="fn('div1','div2')" value="16-above" title="Click on the button to view Result of date 16 to above  !"/>
												</h3>
										
												<div style="height: 400px; width: 1180px;">
												
												<%
												int s=0,e=0,s1=0,e1=0;
												for(int start=1;start<=2;start++)
												{
												
													if(start==1)
													{
														s=1;e=15;
														
													}
													if(start==2)
													{
														s=16;e=(int)days;
													}
													
												%>
												<center>
											<div id="div<%=start%>" align="center" style=" height: 390px;width: 1150px;">
											<%if(Emp_al.size()<9){%>
												<div align="center" class="imptable" style="overflow:hidden; width: 102%;">
												<% }else{%>
												<div align="center" class="imptable" style="overflow:hidden; width: 100.5%;">
												<% }%>
												<table  style="width: 90%"; >
								
											
												
												<tr bgcolor="#2f747e" style="height: 35px;">
												
															
																<td style="width: 6%;" > <input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:white; background-color: #2f747e ;text-align: center;"  name="SR.No." value="SR.No" ></td>
																<td style="width: 8%;" > <input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:white; background-color: #2f747e ;text-align: center;"  name="Emp Code" value="Emp Code" ></td>
																		<td style="width: 22%;" > <input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:white; background-color: #2f747e ;text-align: center;"  name="Emp name" value="Employee Name" ></td>
																<%
																for(int i=s;i<=e;i++)
																{
																%>
															 	<th style="width: 4%;"> <input type="text" size="4" maxlength="3"  disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 80%;  border:none;color:white; background-color: #2f747e ;text-align: center;"  name="SR.No." value= <%=i<=9?"&nbsp;"+i:i%> ></th> 
																<%
																}
																
															%>
														</tr> 
														</table>
													</div>
												
												<div  align="center" class="imptable" style="overflow-y:auto; height: 330px;width: 102%;">
														
												<table  style="width: 90%;"  >
												
														<%
																int srno=1;
																int index = 0;
																if(Emp_al.size()!=0){
															for(int j=0;j<Emp_al.size();j++)
															{
																
																
																ArrayList<TranBean> Emp_bean=(ArrayList<TranBean>)Emp_al.get(j);
																%>
																<tr style="height: 35px;">
																
																<%
																if(start==1)
																{
																	s1=0;e1=14;
																}
																if(start==2)
																{
																	s1=15;e1=(int)days-1;
																}
																

																for(int c=s1;c<=e1;c++)
																{
																	check=false;
																	for(int l=0;l<holidays.size();l++ )
																	{
																	string=(holidays.get(l)).split("-");
																	h11=Integer.parseInt(string[0]);
																	if(h11==c+1)
																		check=true;
																	}
																	
																	TranBean ab= new TranBean();
																	
																	//Attend_bean ab=new Attend_bean();
																	ab=Emp_bean.get(c);
																	
															%>
																
																<%	if( c==0||c==15)
																	{
																		empName = obj.getempName(ab.getEMPNO());
																
																		%>
																		<%if(c==0){ %>
																		<td style="width: 1%;">  <input type="checkbox" class="empCheckbox" id="" value="<%=ab.getEMPNO()%>" name="list" onclick="enableButton()"> </td>
																		<td style="width: 4.2%;"><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: center;"  value="<%=srno%>" ></td>
																		<%} %>
																		<%if(c==15){ %>
																		<td style="width: 6%;"><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: center;"  value="<%=srno%>" ></td>
																		<%} %>
																		
																		<td style="width: 8%;"><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: center;" value="<%=EmployeeHandler.getEmpcode(ab.getEMPNO())%>" ></td>
																		<td style="width: 22%;" ><input type="text"   disabled="disabled"  onclick="this.click()" readonly="readonly" style="width: 100%;  border:none;color:black; background-color: white ;text-align: left;"  value="<%=empName%>"></td> 
																		
																		<%
																		
																	}
																	%>
																	
																	<%String DATE_FORMAT = "yyyy MM dd";
    																SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    																Calendar c1 = Calendar.getInstance(); // today
    																int d1=Integer.parseInt(date1.substring(0,2)),d2=d1-1,d3=d2-1,d4=d3-1,d5=d4-1,d6=d5-1;   // according to the selected date to admin
    																
    																//System.out.println("Today is " + sdf.format(c1.getTime()));
    																
    																//int d1=c1.getTime().getDate(),d2=d1-1,d3=d2-1; // if according to date attendance to admin also
    																
    																
    																//System.out.println("Welcome11111111111112222222222222222211111111111111");				
    																String years=date1.substring(7,11);
    																	
    																	int year=Integer.parseInt(years);
    																	String months=date1.substring(3,6);
    																	int month=Integer.parseInt(years);
    																	
    																	 int s11=0; int s22=0; int s33=0;  int s44=0;   int s55=0;
    	    															 int s66=0; int s77=0; int s88=0;  int s99=0; int s00=0;
    																									
    																/* 	// auto generated sundays code starts here
    																
    																	if(months.equals("Jan"))
    																		month=0;
    																	else if(months.equals("Feb"))
    																		month=1;
    																	else if(months.equals("Mar"))
    																		month=2;
    																	else if(months.equals("Apr"))
    																		month=3;
    																	else if(months.equals("May"))
    																		month=4;
    																	else if(months.equals("Jun"))
    																		month=5;
    																	else if(months.equals("Jul"))
    																		month=6;
    																	else if(months.equals("Aug"))
    																		month=7;
    																	else if(months.equals("Sep"))
    																		month=8;
    																	else if(months.equals("Oct"))
    																		month=9;
    																	else if(months.equals("Nov"))
    																		month=10;
    																	else
    																		month=11; 

    																//	System.out.println("MOnth"+month); 
    																	// year = Calendar.getInstance().get(Calendar.YEAR);
    																   //  month =Calendar.getInstance().get(Calendar.MONTH);   // current month
    															//put the month u want
    															     Calendar cal = new GregorianCalendar(year, month, 1);
    															    List<Integer> Sundays = new ArrayList<Integer>();
    															    List<Integer> otherDays = new ArrayList<Integer>();
    															    List<Integer> abc = new ArrayList<Integer>();
    															    do {
    															        int day = cal.get(Calendar.DAY_OF_WEEK);
    															        int dayInMonth = (cal.get(Calendar.DAY_OF_MONTH));
    															        if ( day == Calendar.SUNDAY) {
    															            Sundays.add(dayInMonth);
    															         //  System.out.println("SUNDAY"+dayInMonth);
    															            abc.add(dayInMonth);
    															        } else {
    															            otherDays.add(dayInMonth);
    															           // System.out.println(Calendar.DATE+"	"+dayInMonth);
    															           
    															        }
    															        cal.add(Calendar.DAY_OF_YEAR, 1);
    															    }  while (cal.get(Calendar.MONTH) == month);
    															
    															  
    															    if(abc.size()==5)
    															   {
    																   s11=abc.get(0); s22=abc.get(1); s33=abc.get(2);s44=abc.get(3);s55=abc.get(4);
    															   }
    															   
    															   if(abc.size()==4)
    															   {
    																   s11=abc.get(0); s22=abc.get(1); s33=abc.get(2);s44=abc.get(3);
    															   }

    															   if(abc.size()==0){}
    															   // auto generated sundays code ends here...........!!!!!!!!!!!! */
    												
    															   
    															    //to take sunday from  holdmast below code works
    															   if(weekdays.size()==10)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															    s77=Integer.parseInt(weekdays.get(6).substring(0,2));
														    		    s88=Integer.parseInt(weekdays.get(7).substring(0,2));
														    		    s99=Integer.parseInt(weekdays.get(8).substring(0,2));
														    		    s00=Integer.parseInt(weekdays.get(9).substring(0,2));
												    	  		    }	
    															    else   if(weekdays.size()==9)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															    s77=Integer.parseInt(weekdays.get(6).substring(0,2));
	    															    s88=Integer.parseInt(weekdays.get(7).substring(0,2));
														    		    s99=Integer.parseInt(weekdays.get(8).substring(0,2));
												    	  		    }	
    															    else    if(weekdays.size()==8)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															    s77=Integer.parseInt(weekdays.get(6).substring(0,2));
														    		    s88=Integer.parseInt(weekdays.get(7).substring(0,2));
														    		  
												    	  		    }	
    															    else   if(weekdays.size()==7)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															    s77=Integer.parseInt(weekdays.get(6).substring(0,2));
														    		   
												    	  		    }	
    															    else  if(weekdays.size()==6)
    			    													
													    		    {
	    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
	    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
														    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
														    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
														    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
														    		    s66=Integer.parseInt(weekdays.get(5).substring(0,2));
	    															   
												    	  		    }	
    															 				   else   if(weekdays.size()==5)
    													
    															    		    {
    			    															    s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    			    															    s22=Integer.parseInt(weekdays.get(1).substring(0,2));
    																    		    s33=Integer.parseInt(weekdays.get(2).substring(0,2));
    																    		    s44=Integer.parseInt(weekdays.get(3).substring(0,2));
    																    		    s55=Integer.parseInt(weekdays.get(4).substring(0,2));
															    	  		    }	
    															    		    else if(weekdays.size()==4)
    															    		    {
    															    		    	 s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    																	    		 s22=Integer.parseInt(weekdays.get(1).substring(0,2));
    																	    		 s33=Integer.parseInt(weekdays.get(2).substring(0,2));
    																	    		 s44=Integer.parseInt(weekdays.get(3).substring(0,2));
    																	        }
    															    		    else if(weekdays.size()==3)
    															    		    {
    															    		    	 s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    															    		    	 s22=Integer.parseInt(weekdays.get(1).substring(0,2));
    																	    		 s33=Integer.parseInt(weekdays.get(2).substring(0,2));
    															    		    } 
    															    		    else if(weekdays.size()==2)
    															    		    {
    															    		    	 s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    																	    		 s22=Integer.parseInt(weekdays.get(1).substring(0,2));
    																	        } 
    															    		    
    															    		    else if(weekdays.size()==1)
    															    		    {
    															    		    	  s11=Integer.parseInt(weekdays.get(0).substring(0,2));
    															     		    }   
    															    		    
    															    		    else if(weekdays.size()==0)
    															    		    	{
    															    		    	s11=0;
    															    		    	} 
    															    		    	
 																	
																	if(c+1==d1||c+1==d2||c+1==d3 || c+1==d4 || c+1==d5 || c+1==d6 )
																	{	
																		if(c+1==s11||c+1==s22||c+1==s33||c+1==s44||c+1==s55||c+1==s66||c+1==s77||c+1==s88||c+1==s99||c+1==s00)
																		{if(status.equals("saved")/* && !roleId.equals("1")  */||status.equals("approved") /* && !roleId.equals("1") */ ||status.equals("rejected") /* && !roleId.equals("1") */ || status.equals("pending") /* && !roleId.equals("1")  */) {%>	
																		<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEMPNO()%>" class="_<%=ab.getEMPNO()%>" maxlength="2" value="<%=ab.getVal()%>" readonly="readonly"	
																	 	disabled="disabled"  onfocus="this.select()" title="Date=<%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; "  onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		<%
																		}else{%>
																				<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>"  name="_<%=ab.getEMPNO()%>"  class="_<%=ab.getEMPNO()%>" maxlength="2" value="WO"   readonly="readonly"	
																	 	disabled="disabled" onfocus="this.select()" title="Date=<%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; "  onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		<%}
																	}
																		else{
												
																			if(check)
																			
																			{  if(status.equals("saved") /* && !roleId.equals("1") */ ||status.equals("approved") /* && !roleId.equals("1")  */|| status.equals("rejected") /* && !roleId.equals("1") */ || status.equals("pending") /* && !roleId.equals("1") */ ){
																				%>	
																				<td style="width: 4%;" align="center">
																				<input type="text" size="4"  id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEMPNO()%>" class="_<%=ab.getEMPNO()%>" maxlength="2" value="HD" readonly="readonly"	
																			 	disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																				</td>
																				<%
																			}
																			else{%>
																				<td style="width: 4%;" align="center">
																				<input type="text" size="4"  id="<%=z++%>" name="_<%=ab.getEMPNO()%>" class="_<%=ab.getEMPNO()%>" maxlength="3" value="HD" readonly="readonly"	
																			 	disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase;background-color: ;  " onkeypress="return inputLimiter(event,'Numbers')">
																				</td>
																			
																			<%}
																			}
																			else{if(status.equals("saved") /* && !roleId.equals("1") */ ||status.equals("approved") /* && !roleId.equals("1")  */||status.equals("rejected") /* && !roleId.equals("1") */  || status.equals("pending") /* && !roleId.equals("1") */ ){
																		%>
																
																		<td style="width: 4%;" style="width: 4%;" align="center">
																		
																		<input type="text" size="4" id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEMPNO()%>" class="_<%=ab.getEMPNO()%>" maxlength="2"  value="<%=ab.getVal() ==null ?"P":ab.getVal()%>"
																	  onfocus="this.select()" disabled="disabled"  title="Date=   <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																	
																		<%	}
																			else{%>
																				<td style="width: 4%;" style="width: 4%;" align="center">
																		
																		<input type="text" size="4" id="<%=z++%>"  name="_<%=ab.getEMPNO()%>" maxlength="2"  class="_<%=ab.getEMPNO()%>" value="<%=ab.getVal() ==null ?"P":ab.getVal()%>"
																	  onfocus="this.select()" disabled="disabled"  title="Date=   <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		<%	}
																			}
																		}
																	}
																	else
																	{
																		if(c+1==s11||c+1==s22||c+1==s33||c+1==s44||c+1==s55||c+1==s66||c+1==s77||c+1==s88||c+1==s99||c+1==s00)
																		{if(status.equals("saved")/*  && !roleId.equals("1") */ || status.equals("approved") /* && !roleId.equals("1") */ ||status.equals("rejected")/*  && !roleId.equals("1") */  || status.equals("pending") /* && !roleId.equals("1") */  ) {%>	
																		<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEMPNO()%>" class="_<%=ab.getEMPNO()%>" maxlength="2" value="<%=ab.getVal()%>"
																	  disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		<%
																		
																		}else{%>
																		<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>" name="_<%=ab.getEMPNO()%>"class="_<%=ab.getEMPNO()%>" maxlength="2" value="WO"
																	 readonly="readonly"	 disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black; text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		
																		<%}
																		
																		}
																		else{
																			if(check)
																			{	if(status.equals("saved") /* && !roleId.equals("1")  */ ||status.equals("approved") /* && !roleId.equals("1") */ ||status.equals("rejected") /* && !roleId.equals("1") */ || status.equals("pending") /* && !roleId.equals("1")  */){
																				%>	
																				<td style="width: 4%;" align="center">
																				<input type="text" size="4"  id="<%=z++%>" name="<%=n++%>" id="_<%=ab.getEMPNO()%>" class="_<%=ab.getEMPNO()%>" maxlength="2" value="<%=ab.getVal()%>"
																			 readonly="readonly"		disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black;text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																				</td>
																				<%
																			}else{%>
																				<td style="width: 4%;" align="center">
																				<input type="text" size="4"  id="<%=z++%>" name="_<%=ab.getEMPNO()%>" class="_<%=ab.getEMPNO()%>" maxlength="2" value="HD"
																			 readonly="readonly"		disabled="disabled" onfocus="this.select()" title="Date= <%=c+1%>" style="width: 80%;color:black;text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																				</td>
																			
																			<%}
																			
																			
																			}
																			else{
																				if(status.equals("saved") /* && !roleId.equals("1") */ ||status.equals("approved") /* && !roleId.equals("1") */ ||status.equals("rejected") /* && !roleId.equals("1")  */ || status.equals("pending") /* && !roleId.equals("1")  */ ){
																		%>
																		
																		<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>"  name="<%=n++%>" class="_<%=ab.getEMPNO()%>" id="_<%=ab.getEMPNO()%>" maxlength="2" value="<%=ab.getVal()==null?"P":ab.getVal()%>"
																	 disabled="disabled" onfocus="this.select()" title="Date=   <%=c+1%>" style="width: 80%;color:black;text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																		
																		<%
																		}else{%>
																			
																				<td style="width: 4%;" align="center">
																		<input type="text" size="4"  id="<%=z++%>"  name="_<%=ab.getEMPNO()%>" class="_<%=ab.getEMPNO()%>" maxlength="2" value="<%=ab.getVal()==null?"P":ab.getVal()%>"
																	 disabled="disabled" onfocus="this.select()" title="Date=   <%=c+1%>" style="width: 80%;color:black;text-align: center;text-transform: uppercase; background-color: ; " onkeypress="return inputLimiter(event,'Numbers')">
																		</td>
																			
																		<%}
																				
																			
																			}
																		}
																	} %>
																	<%
																}
																%>
																</tr>
															<%	
															srno++;
															}}
																else{%>
																<br><br>
																
																<tr><font size="4" color="red"> No record found ...!!</font></tr>
																<br>
																<%}
															%>
														</table>
														
														</div>
														</br>
													
														
													
														</div>
														
														</center>
														<%
												}
														%>
															<%if(status.equalsIgnoreCase("saved") || status.equalsIgnoreCase("rejected") ){ %>
																<h3>Send For Approval : 
																<input type=button id="checkAll" value="Check All" onClick="this.value=check5(this.form.list)"> 
															<input type="submit" class="Left" value="Transfer"   disabled="disabled" onclick="form.action='EmpAttendServlet?action=transfer';"/>
															 <input type="submit" class="Left" value="Left"   onclick="form.action='EmpAttendServlet?action=left';" disabled="disabled" />
              												<input type="submit" id="apr" value="Single Approve"   disabled="disabled" onclick="form.action='EmpAttendServlet?action=SingleApprove';"/>
              												
              												
              												<input type="button"  id="send" value="End Of Month"/>		</h3>
    														<%} 
														
														if(status.equalsIgnoreCase("pending") && !roleId.equals("1")){ %>
													<font style="color: olive; size: 3;"><b>Already sent for approval</b></font>
														<%} 
														
														if(status.equalsIgnoreCase("approved")){ %>
													<font style="color: green; size: 3;"><b>Successfully Approved</b></font>
														<%} %>
														</div>
														
																  </form> 
														</center>
														</br></br>
														<h3>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
													P:Present&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													A:Absent&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													H:Half Day&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													WO:Week Off&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													HD:Holiday&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													LT:Left Job&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													NJ:New Joined&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</h3>
														
							
									 							
              	
              		<!-- <input type="button" disabled="disabled" id="" value="Send for Approval"/> -->
              	
            
              					<!-- 	<input name="action" type="submit" value="Cancel" onclick="callback()"/> -->
									<input type="hidden" name="flag1" id="flag1" value="<%=flag%>">
										</center>
										<%
}

catch(Exception e)
{
	e.printStackTrace();
	%>
	
	<script type="text/javascript">
	
	window.location.href="login.jsp?action=0";
	</script>
	
	<%
}
%>
			</div>
							<!--  end table-content  -->
														<input type="hidden" name="siteid" id="siteid" value="<%=site_id1%>">
		  <input type="hidden" name="sheetstatus" id="sheetstatus" value="<%=sheetstatus%>">
		  <input type="hidden" name="sheetenddate" id="sheetenddate" value="<%=date%>">
		 <input type="hidden" name="currentdate" id="currentdate" value="<%=currentdate%>">
		  <input type="hidden" name="blankatt" id="blankatt" value="">
		    <input type="hidden" name="response" id="response" value="">
		    <input type="hidden" name="DOJ" id="DOJ" value="">
		    <input type="hidden" name="DOL" id="DOL" value="">
		   <%--  <input type="hidden" name="check_att_approved_emp" id="check_att_approved_emp" value="<%=att_approved_emp%>"> --%>
		    
		    
		<div id="process"  style="z-index: 1000;background-color:white;height: 100%;width: 100%;top: 0px;left: 0px;position: fixed;opacity:0.8;" hidden=true; >
				<div align="center" style="padding-top: 20%;">
				
				<h1> Please wait it takes Few Min....Be Patience</h1>
				
				<img alt="" src="images/process.gif">
				</div>
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
		
		
		<div id="process"  style="z-index: 1000;background-color:white;height: 100%;width: 100%;top: 0px;left: 0px;position: fixed;opacity:0.8;" hidden=true; >
				<div align="center" style="padding-top: 20%;">
				
				<h1> Please wait it takes Few Min....Be Patience</h1>
				
				<img alt="" src="images/process.gif">
				</div>
			</div>
		
		
		
		<!--  end content -->
		<div class="clear">&nbsp;</div>
	</div>
	<!--  end content-outer........................................................END -->

	<div class="clear">&nbsp;</div>

<script type="text/javascript" >
	
	document.getElementById("div2").style.display='none';
	
	
	</script>
	
	<!-- <script>
    $(document).ready(function() {
        function disableBack() { window.history.forward() }

        window.onload = disableBack();
        window.onpageshow = function(evt) { if (evt.persisted) disableBack()}
    });
</script> -->
</body>
</html>




   