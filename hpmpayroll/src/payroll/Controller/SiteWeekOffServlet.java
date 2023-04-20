package payroll.Controller;

import java.io.IOException;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import payroll.DAO.EmpAttendanceHandler;
import payroll.DAO.SiteWeekOffHandler;
import payroll.Model.WeekOffBean;


@WebServlet("/SiteWeekOffServlet")

public class SiteWeekOffServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SiteWeekOffServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session=request.getSession();
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String action=request.getParameter("action");
		SiteWeekOffHandler siteWeekOffHandler=new SiteWeekOffHandler();
		WeekOffBean weekOffBean=null;

	if(action.equals("saveWeekOffValues")){
		System.out.println("srvlet11111111111111111111111111111111111111111111111111111112222223333333333333"+action);
    	String roleI=(String)session.getAttribute("roleId1");
    	int roleId=Integer.parseInt(roleI);
    	//int roleId=(Integer)session.getAttribute("roleId1");
		System.out.println("rolei servlet"+roleId);
		String prjCode=(String)session.getAttribute("prjCode1");
		System.out.println("prjode servlet"+prjCode);
		String projectName;
		int proj;
		String date;
		String year;
    	String month;
    	String [] empno;
    	String [] day;
    	int sessionEmployee;
    	 String empnoValues[];
    	 String dayValues[];
    	
    	if(roleId==1) {
    		
	    projectName=request.getParameter("pp")==null?"":request.getParameter("pp");
		 String prj[]=projectName.split(":");

	     proj=Integer.parseInt(prj[3]);	
		
         date=request.getParameter("date1")==null?"":request.getParameter("date1");
     	 year=date.substring(7,11);
    	 month=date.substring(3,6);
    	 empno=request.getParameterValues("empno");
    	 day=request.getParameterValues("day");
    	 sessionEmployee=(Integer)session.getAttribute("EMPNO");
    	 empnoValues = new String[empno.length];
    	 dayValues = new String[day.length];
         }
		
    	 else
		
		{
		    projectName=request.getParameter("p1")==null?"":request.getParameter("p1");
	         date=request.getParameter("date2")==null?"":request.getParameter("date2");
             proj=Integer.parseInt(prjCode);
             year=date.substring(7,11);
        	 month=date.substring(3,6);
        	 empno=request.getParameterValues("empno1");
        	 day=request.getParameterValues("day1");
        	 sessionEmployee=(Integer)session.getAttribute("EMPNO");
        	  empnoValues= new String[empno.length];
        	  dayValues = new String[day.length];
		}
    	
  
    	try
    	{
    	//System.out.println("into servlet saveTranValues1");

		for(int i=0; i<empno.length; i++)
		{
			empnoValues[i] = (empno[i].trim());
			dayValues[i] = (day[i].trim());
			//System.out.println("empno : "+empnoValues[i]);
			//System.out.println("dayValues : "+dayValues[i]);
		}
		
		boolean flag=false;
		
		
		for(int i=0; i<empno.length; i++)
		{
		weekOffBean=new WeekOffBean();
		weekOffBean.setYear(Integer.parseInt(year));
		weekOffBean.setMonth(month);
		weekOffBean.setCreatedBy(sessionEmployee);
		weekOffBean.setUpdatedBy(sessionEmployee);
		weekOffBean.setCreatedDate(date);
		weekOffBean.setUpdatedDate(date);
		weekOffBean.setEmpno(Integer.parseInt(empnoValues[i]));
		weekOffBean.setDay(Integer.parseInt((dayValues[i])=="1"?"7":(dayValues[i])));
		weekOffBean.setSite_id(proj);
		flag=siteWeekOffHandler.updateSiteWeekOff(weekOffBean);
			
		}
		
	//	ArrayList<Float> li=new ArrayList<>(Arrays.asList(advvals));
	
		
		if(flag)
		{
			
			if(flag)
			{
			//proj=proj.replaceAll("&", "and");
			response.sendRedirect("SiteWeekOff.jsp?action=afterEdit&proj="+proj+"&date1="+date+"&flag=3");
			}
			else
			{
				response.sendRedirect("SiteWeekOff.jsp?flag=4");
			}
		}
		else
		{
			response.sendRedirect("SiteWeekOff.jsp?flag=4");
		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	//	proj=proj.replace("&", "and");
			response.sendRedirect("SiteWeekOff.jsp?action=afterEdit&proj="+proj+"&date1="+date+"&flag=4");
    		
    	}
	
	}
	
	}
	
}
	
	
	
	