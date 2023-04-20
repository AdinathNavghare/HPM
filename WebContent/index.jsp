<%@page import="payroll.DAO.EmployeeHandler"%>
<%@page import="payroll.Model.EmployeeBean"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="payroll.Core.ReportDAO"%>
<%@page import="payroll.Model.GraphBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>&copy DTS3 </title>
		<!-- <link rel="stylesheet" href="css/commonGraph.css"> -->
<!-- 		<link rel="stylesheet" href="css/empSalaryChart.css">
		<link rel="stylesheet" href="css/empYearwiseSalaryChart.css"> -->
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<!--[if IE]>
<link rel="stylesheet" media="all" type="text/css" href="css/pro_dropline_ie.css" />
<![endif]-->
<noscript>
        <meta http-equiv="refresh" content="0.0;url=javascriptError.html">
    </noscript>
<%
 EmployeeBean emp=new EmployeeBean();
 ArrayList<EmployeeBean> emplist=new ArrayList<EmployeeBean>();
 EmployeeHandler emph=new EmployeeHandler();
 emplist=emph.getBirthEmplist();
 ArrayList<EmployeeBean> emplist1=new ArrayList<EmployeeBean>();
 emplist1=emph.getday();
 ArrayList<EmployeeBean> emplist2=new ArrayList<EmployeeBean>();
 emplist2=emph.getTombd();    
 	  java.util.Date d = new java.util.Date();
	  SimpleDateFormat sdf = new SimpleDateFormat("dd");
	  SimpleDateFormat sqlsdf = new SimpleDateFormat("yyyy-MM-dd");
	  SimpleDateFormat getsdf = new SimpleDateFormat("dd-MMM-yyyy");
	  //sdf.applyPattern("EEEE");
      String MyDate1 = sdf.format(d);
      
	  
%>
<script type="text/javascript">

 
$("marquee").hover(function () 
{ 
    this.stop();
}, function () {
    this.start();
});
 
</script>
  <link href="css/style.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="js/jquery-1.6.3.js"></script>
<script src="js/raphael-min.js" type="text/javascript"></script>
<script src="js/elycharts.js" type="text/javascript"></script>


<%
ArrayList<GraphBean> grlist = ReportDAO.getProjectwiseEmployee();

ArrayList<GraphBean> grlistdesg = ReportDAO.getDesignationwiseEmployee();

JSONArray jsona = JSONArray.fromObject(grlist);
JSONArray jsonadesg = JSONArray.fromObject(grlistdesg);

%>
<script type="text/javascript">

var jsonarray = <%=jsona%>
var json_projectcode=[];
var json_totalemp=[];


var jsonarraydesg = <%=jsonadesg%>
var json_desg=[];
var json_totalempdesg=[];


json_data = eval(jsonarray);
json_datadesg = eval(jsonarraydesg);

var projectcode=new Array();
var totalemp=new Array();
var totalemp_tooltip=new Array();

var desgnation=new Array();
var totalempdesg=new Array();
var totalempdesg_tooltip=new Array();


var j=0;
var j1=0;

for(var i=json_data.length-1;i>=0;i--){
	projectcode[j]=json_data[i].prj_Code;
	totalemp[j]=json_data[i].totalemp;
	j++;
	
}

for(var i=json_datadesg.length-1;i>=0;i--){
	desgnation[j]=json_datadesg[i].designation;
	totalempdesg[j]=json_datadesg[i].totalemp;
	j++;
	
}

for (var i=json_data.length-1;i>=0;i--){
	totalemp_tooltip[i]="<div class='label'><p class='charlab'>Total Emp: " + totalemp[i] + " </p></div>";
	
}

for (var i=json_datadesg.length-1;i>=0;i--){
	totalempdesg_tooltip[i]="<div class='label'><p class='charlab'>"+desgnation[i]+":<br>" + totalempdesg[i] + " </p></div>";
	
}

	 $(function() {
		  $("#chart").chart({
		  template: "line_basic_6",
		  tooltips: {
		    serie1: totalemp_tooltip
		   
		  },
		  values: {
		    serie1: totalemp
		   
		   
		  },
		  labels: projectcode,
		  defaultSeries: {
		    type: "bar",
		    stacked: true
		  },
		  axis: {
		    r: {
		      max: 100,
		      suffix: "%"
		    }
		  }
		});

		});

		$.elycharts.templates['line_basic_6'] = {
		  type: "line",
		  margins: [50, 60, 60, 50],
		  defaultSeries: {
		    highlight: {
		      newProps: {
		        r: 8,
		        opacity: 1
		      },
		      overlayProps: {
		        fill: "white",
		        opacity: 0.2
		      }
		    }
		  },
		  series: {
		    serie1: {
		      color: "90-#003000-#009000",
		      tooltip: {
		        frameProps: {
		          stroke: "green"
		        }
		      }
		    }
		  },
		  defaultAxis: {
		    labels: true,
		    
		  },
		  
		  axis: {
		    x: {
		      labelsRotate: 45,
		      labelsProps: {
		        font: "15px Verdana"
		      }
		    }
		  },
		  
		  features: {
		    grid: {
		      draw: true,
		      forceBorder: true,
		      ny: 4
		    }
		  },
		  barMargins: 23
		};

		 
		
		 $(function() {
			  $("#chartdiv").chart({
			  template: "line_basic_2",
			  tooltips: {
			    serie1: totalempdesg_tooltip,
			   
			  },
			  values: {
			    serie1: totalempdesg,
			    
			  },
			  labels: desgnation,
			  defaultSeries: {
			    fill: true,
			    stacked: false,
			    highlight: {
			      scale: 1
			    },
			    startAnimation: {
			      active: true,
			      type: "grow",
			      easing: "bounce"
			    }
			  }
			});

			});

			$.elycharts.templates['line_basic_2'] = {
			  type: "line",
			  margins: [50, 50, 80, 50],
			  defaultSeries: {
			    plotProps: {
			      "stroke-width": 6
			    },
			    dot: true,
			    dotProps: {
			      stroke: "white",
			      "stroke-width": 3
			    }
			  },
			  series: {
			    serie1: {
			      color: "green"
			    },
			    serie2: {
			      color: "blue"
			    }
			  },
			  defaultAxis: {
			    labels: true,
			  },
			  axis: {
				    x: {
				      labelsRotate: 90,
				      
				      labelsProps: {
				        font: "8px Verdana",
				        	
				      }
				    }
				  },
			  features: {
				 
			    grid: {
			      draw: [true, false],
			      ny: 10,
			      props: {
			        "stroke-dasharray": "-"
			      }				  
			    },
			    legend: {
			      horizontal: false,
			      width: 300,
			      height: 300,
			      x: 220,
			      y: 250,
			      dotType: "circle",
			      dotProps: {
			        stroke: "white",
			        "stroke-width": 50
			      },
			      borderProps: {
			        opacity: 0.3,
			        fill: "#008800",
			        "stroke-width": 0
			      }
			    }
			  }
			};


			
			function Clickheretoprint(panel)
			{ 
				
					var disp_setting="toolbar=yes,location=no,directories=yes,menubar=yes,"; 
					    disp_setting+="scrollbars=yes,width=1000, height=800, left=200, top=10"; 
					var content_value = document.getElementById(panel).innerHTML; 
					
					var docprint=window.open("","",disp_setting); 
						docprint.document.open(); 
						docprint.document.write('<html><head><title>Inel Power System</title>'); 
						docprint.document.write("<style type=\"text/css\">	body,td,th { font-family: Times New Roman; font-size: 14px;}	</style></head><body onLoad=\"self.print()\" style=\"font-family:Times New Roman; font-size:9px;\" ><center>");          
						docprint.document.write(content_value);          
						docprint.document.write('</center></body></html>'); 
						docprint.document.close(); 
						docprint.focus(); 
			}
		
</script>

</head>
<body style="overflow: hidden;"> 
<%@include file="mainHeader.jsp" %>
<!--  start nav-outer-repeat................................................................................................. START -->
<%@include file="subHeader.jsp" %>

<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer" style="overflow-y:auto; max-height:80%;">


<!-- <div class="dtslogo">
	<a class="dtsfloatlogo" href="#"><img src="images/shared/dishalogo.png" ></a>
   
</div>
 -->




<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Payroll.... </h1>
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
			 <!--  <div style="float:left;width: 895px; background-color: #F5F6FA; height: 350px; border-radius:15px;"  >
			   <p style="font: italic;color: #626262; margin-left: 15px;margin-top: 15px">Payroll software can be used to standardize the way a company handles the calculation and processing of its payroll. <br>
			It can be used not only to calculate money due to employees, but also to provide for the easy keeping of accurate records. <br>
			Essentially, these programs can help to streamline the entire process, making the task of processing payroll less time-consuming and cumbersome.
			<br />
			<br />
			The software starts off from formation of a Company. After filling up the entire Employee related information and 
			the group or department he is recruited, assign the employee a salary structure. Now we can start generating and managing payroll processes for that particular Employee. 
			Also the end user has the option of creating his own salary structures and salary formulae. The pay slip is generated after the attendance is 
			marked for that month. The software can also manage the Company and the Bank holidays
			<br />
			<br />
			Payroll software can generate all the reports related to company, employee, attendance/leave, payroll, 
			Government forms-16. Management will get a clear view about the Payroll and Attendance of the Employees through the MIS (Management Information Reports)
			generated by the software. For giving restricted access to the front end user and full access to the main HR authorized person it 
			is possible to configure the software by the user side.
			 
			  </p> -->
			  
			   <iframe id="ch11" src="empYearwiseSalaryChart.jsp" style="float:left;width: 865px; background-color: #f5f6fa; height: 450px; border-radius:15px;position: relative;" scrolling="no"></iframe>
			  <!-- </div> -->
			  
			  <div class="birthday" >
			  
			  <h3 align="center">Birthday of this Month</h3>
			
			  <marquee direction="up"  style="background-color: #FBFBFB; height: 330px; padding: 20px;" scrolldelay="250"  onmouseover="this.stop();" onmouseout="this.start();">
			  <%if(emplist!=null) 
			    {
				  for(EmployeeBean eb:emplist)
				  {
					 
			  %>
      			
      			 <p style="color:#626262;"><%=eb.getFNAME()%></p><p style="color: #FF591D;"><%=getsdf.format(sqlsdf.parse(eb.getDOB())).substring(0, 6)%></p><br> 
      			    
		       <%}
				  
				  }%>
			</marquee>
			
			   <%
				   	/* if(emplist!=null) */
				   	if(emplist1!=null) 
			    {
				  
				%>	   
				   <h3 align="center">Todays Birthday</h3>
				  <!--   <marquee direction="up"  style="background-color: #FBFBFB; height: 50px; padding: 20px;" scrolldelay="250"  onmouseover="this.stop();" onmouseout="this.start();"> -->
					<% /*  for(EmployeeBean eb:emplist) */
					
						for(EmployeeBean eb:emplist1)
					  {	
						 /* String s = eb.getDOB();
						  String  day = emph.getday(s); 
						 
						  if(Integer.parseInt(MyDate1)==Integer.parseInt(day))
						  { */ 
				 	%>
      				 <p style="color:#626262; padding-left:10px; "><%=eb.getFNAME()%></p><p style="color: #FF591D; padding-left:10px;"><%=getsdf.format(sqlsdf.parse(eb.getDOB())).substring(0, 6) %></p><br> 
		       	<%
					  }   
			    }
				%>
				<!-- </marquee> -->
			
			   
			  <%if(emplist2!=null) 
			    {
			  %>
					<h3 align="center">Tommorow Birthday</h3> 
					<!--  <marquee direction="up"  style="background-color: #FBFBFB; height: 50px; padding: 20px;" scrolldelay="250"  onmouseover="this.stop();" onmouseout="this.start();"> -->
			  	  <%	 
				  	for(EmployeeBean eb:emplist2)
				  	{
			  	  %>
      				 <p style="color:#626262; padding-left:10px;"><%=eb.getFNAME()%></p><p style="color: #FF591D; padding-left:10px;"><%=getsdf.format(sqlsdf.parse(eb.getDOB())).substring(0, 6)%></p><br>   
		       	  <%
		       	 	 }
				  }				  
			  %>  
			<!--   </marquee> -->
			    
			</div>  
			
			<div  style="float: left;   border-radius:15px;">
			
			<table align="center" cellspacing="20">
           <tr><td>  </td></tr>
            <tr><td width="300px">
          <div id="barchart">
          <br><br>
          <font size="3" style="font: bolder;">Project wise Employees  </font> <a href="javascript:Clickheretoprint('barchart')">	Click here to print</a>  
          <br><font size="2" style="font: bolder;">Graph on Project Code(x) Vs Number of Employees(y)  </font>
          <div id="chart" style="width: 600px; height: 400px;padding-left: 20px;" ></div></div>
            </td></tr>
            <tr>
                
              </tr>
              <tr>  
                </tr>
                 <tr><td>  </td></tr>
                <tr><td width="600px">
          <div id="linechart"><font size="3" style="font: bolder;">Designation wise Employess  </font>  
          <br><font size="2" style="font: bolder;">Graph on Designation(x) Vs Number of Employees(y)  </font> <a href="javascript:Clickheretoprint('linechart')">	Click here to print</a>
          <div id="chartdiv" style="width: 900px; height: 400px;padding-left: 20px;" ></div></div>
            </td></tr>
               
            
        </table>
			
			</div>
			     
			 </div>
			   
			 
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
<!--  end content -->

<div class="clear">&nbsp;</div>
</div>
<!--  end content-outer........................................................END -->

<div class="clear">&nbsp;</div>
    

</body>
</html>