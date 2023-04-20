package payroll.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import payroll.DAO.ShiftHandler;
import payroll.Model.ShiftBean;

/**
 * Servlet implementation class ShiftServlet
 */
@WebServlet("/ShiftServlet")
public class ShiftServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShiftServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action=request.getParameter("action")!=null?request.getParameter("action"):"";
		ShiftHandler shifthand1=new ShiftHandler();
		HttpSession session = request.getSession();
		
		if(action.equalsIgnoreCase("showtimesheet"))
		{
			ArrayList<ShiftBean> shftlist=new ArrayList<ShiftBean>();
			shftlist=shifthand1.getTimesheet();
			session.setAttribute("timesheet", shftlist);
			response.sendRedirect("timeSheet.jsp?action=showsheetlist");
		
		}
		if(action.equalsIgnoreCase("show"))
		{
			ArrayList<ShiftBean>ovtlist=new ArrayList<>(); 
			ovtlist=shifthand1.getList();
			session.setAttribute("ovtlist1", ovtlist);
		    response.sendRedirect("overtime.jsp?action=ovetimelist");
		}
		if(action.equalsIgnoreCase("showcalmast"))
		{
			ArrayList<ShiftBean> callist=new ArrayList<>(); 
			callist=shifthand1.getCalmastList();
			session.setAttribute("calmastlist", callist);
		    response.sendRedirect("calmast.jsp?action=mastlist");
		}
		
		if(action.equalsIgnoreCase("showshift"))
		{
			ShiftHandler sh = new ShiftHandler();
			ArrayList<ShiftBean> shiftlist = new ArrayList<ShiftBean>();
			shiftlist = sh.getshiftvalues();
			session.setAttribute("shiftlist",shiftlist);
			response.sendRedirect("shift.jsp?action=list");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		ShiftBean sb = new ShiftBean();
		ShiftHandler sh = new ShiftHandler();
		String action=request.getParameter("action")==null?"":request.getParameter("action");
		
		if(action.equalsIgnoreCase("insert"))
		{
	  	sb.setShift(request.getParameter("shiftcode")!=null?request.getParameter("shiftcode"):"");
		sb.setStartTime(request.getParameter("starttime")!=null?request.getParameter("starttime"):"");
		sb.setEndTime(request.getParameter("endtime")!=null?request.getParameter("endtime"):"");
		sb.setStatus(request.getParameter("status")!=null?request.getParameter("status"):"");
		
		sh.insertValue(sb);
		ArrayList<ShiftBean> shiftlist = new ArrayList<ShiftBean>();
		shiftlist = sh.getshiftvalues();
		session.setAttribute("shiftlist",shiftlist);
		response.sendRedirect("shift.jsp?action=list");
		}
		
		
		ShiftHandler shifthand=new  ShiftHandler();
		if(action.equalsIgnoreCase("updateshift"))
		{
			
			boolean flag=false;
			int srno=Integer.parseInt(request.getParameter("srno"));
			ShiftBean shbean=new ShiftBean();
			String shiftcode=request.getParameter("shiftcode");
			String starttime =request.getParameter("starttime");
			String endtime=request.getParameter("endtime");
			String status=request.getParameter("status");
			shbean.setSrno(srno);
			shbean.setShift(shiftcode);
			shbean.setStartTime(starttime);
			shbean.setEndTime(endtime);
			shbean.setStatus(status);
			flag=shifthand.update(shbean);
			ArrayList<ShiftBean> shiftlist = new ArrayList<ShiftBean>();
			shiftlist = sh.getshiftvalues();
			session.setAttribute("shiftlist",shiftlist);
			if(flag)
			{
				response.sendRedirect("shift.jsp?action=list");
			}
			else
			{
				//System.out.println("error in record inserting");
			}
		}
		ArrayList<ShiftBean> ovtlist=new ArrayList<ShiftBean>();
		if(action.equalsIgnoreCase("addovertime"))
		{
			boolean flag=false;
			ShiftBean shbean1=new ShiftBean();
			shbean1.setEmptype(Integer.parseInt(request.getParameter("EmpType")));
			shbean1.setGrade(Integer.parseInt(request.getParameter("grade")));
			shbean1.setRate(Integer.parseInt(request.getParameter("rate")));
			shbean1.setShiftcode(Integer.parseInt(request.getParameter("shiftcode")));
			flag=shifthand.insertOvertime(shbean1);	
			ovtlist=shifthand.getList();
			session.setAttribute("ovtlist1", ovtlist);
			if(flag)
		    {
		    	response.sendRedirect("overtime.jsp?action=ovetimelist");
		    }
		    else{
		    	System.out.println("error in record inserting");
		    }
		}
		if(action.equalsIgnoreCase("updateovertime"))
		{
			
			ShiftBean shbean=new ShiftBean();
			boolean flag=false;
			
			shbean.setEmptype( Integer.parseInt(request.getParameter("EmpType")));
			shbean.setShiftcode(Integer.parseInt(request.getParameter("shiftcode")));
			shbean.setGrade(Integer.parseInt(request.getParameter("grade")));
			shbean.setRate(Integer.parseInt(request.getParameter("rate")));
			shbean.setSrno(Integer.parseInt(request.getParameter("srno")));
			flag=shifthand.UpdateOvertime(shbean);
			ovtlist=shifthand.getList();
			session.setAttribute("ovtlist1", ovtlist);
			if( flag )
			{	
				response.sendRedirect("overtime.jsp?action=ovetimelist&flag="+flag);
			}
			else
			{
				response.sendRedirect("overtime.jsp?action=ovetimelist");
			}
		}
		
		if(action.equalsIgnoreCase("addcalmast"))
		{
			boolean flag=false;
			
			ShiftBean shbean2=new ShiftBean();
			shbean2.setDay(request.getParameter("day"));
			shbean2.setDaydate(request.getParameter("daydate"));
			shbean2.setDaytype(request.getParameter("daytype"));
			shbean2.setEmptype(Integer.parseInt(request.getParameter("EmpType")));
			shbean2.setHoliday(request.getParameter("holiday"));
			shbean2.setDesc(request.getParameter("desc"));
			flag=shifthand.addCallmast(shbean2);
			if(flag)
			{  
				ArrayList<ShiftBean> callist=new ArrayList<>();
				callist=shifthand.getCalmastList();
				session.setAttribute("calmastlist", callist);
			    response.sendRedirect("calmast.jsp?action=mastlist");
			}
		}
		if(action.equalsIgnoreCase("updatecalmast"))
		{
            boolean flag=false;	
			ShiftBean shbean3=new ShiftBean();
			shbean3.setDay(request.getParameter("day1"));
			shbean3.setDaydate(request.getParameter("daydate1"));
			shbean3.setDaytype(request.getParameter("daytype1"));
			shbean3.setEmptype(Integer.parseInt(request.getParameter("EmpType1")));
			shbean3.setHoliday(request.getParameter("holiday1"));
			shbean3.setDesc(request.getParameter("desc1"));
			
			flag=shifthand.updateCalmast(shbean3);
			ArrayList<ShiftBean> callist=new ArrayList<>(); 	
		    callist=shifthand.getCalmastList();
		    session.setAttribute("calmastlist", callist);
			if(flag)
			{
				 response.sendRedirect("calmast.jsp?action=mastlist");
			}
			else{
				 response.sendRedirect("calmast.jsp?action=mastlist");
				}
		}
		if(action.equalsIgnoreCase("addtimesheet"))
		{
			boolean flag=false;
			ShiftBean shfbean=new ShiftBean();
            String EMPNO=request.getParameter("EMPNO");
			StringTokenizer st = new StringTokenizer(EMPNO,":");
		    while(st.hasMoreTokens()){EMPNO=st.nextToken();}
		    int empno=Integer.parseInt(EMPNO);
			shfbean.setEMPNO(empno);
			shfbean.setShift((request.getParameter("shiftcode")));
			shfbean.setDaydate(request.getParameter("daydate"));
			shfbean.setCheckin(request.getParameter("checkin"));
			shfbean.setCheckout(request.getParameter("checkout"));
			shfbean.setTotal(request.getParameter("total"));
			flag=shifthand.addTimesheet(shfbean);
			ArrayList<ShiftBean> shftlist1=new ArrayList<ShiftBean>();
			shftlist1=shifthand.getTimesheet();
			session.setAttribute("timesheet", shftlist1);
	       if(flag)
			{
				response.sendRedirect("timeSheet.jsp?action=showsheetlist");
			}
			else
			{
				response.sendRedirect("timeSheet.jsp?action=sheetlist");
			}
		}
		if(action.equals("updatetimesht"))
		{
			boolean flag=false;
			ShiftBean shfbean2=new ShiftBean();
			shfbean2.setEMPNO(Integer.parseInt(request.getParameter("EMPNO1")));
			shfbean2.setSrno(Integer.parseInt(request.getParameter("srno")));
			shfbean2.setShift((request.getParameter("shiftcode1")));
			shfbean2.setDaydate(request.getParameter("daydate1"));
			shfbean2.setCheckin(request.getParameter("checkin1"));
			shfbean2.setCheckout(request.getParameter("checkout1"));
			shfbean2.setTotal(request.getParameter("total1"));
			flag=shifthand.UpdateTimesheet(shfbean2);
			ArrayList<ShiftBean> shftlist3=new ArrayList<ShiftBean>();
		    shftlist3=shifthand.getTimesheet();
			session.setAttribute("timesheet", shftlist3);
			if(flag)
			{
				response.sendRedirect("timeSheet.jsp?action=showsheetlist");
			}
			else
			{
				response.sendRedirect("timeSheet.jsp?action=showsheetlist");
			}
			
		}
	}

}
