package payroll.Controller;



import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import payroll.DAO.EmpAttendanceHandler;
import payroll.DAO.LeaveEncashmentHandler;
import payroll.Model.LeaveEncashmentBean;
import payroll.Model.TranBean;



/**
 * Servlet implementation class EmpAttendServlet
 */
@WebServlet("/LeaveServlet")
public class LeaveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LeaveServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session=request.getSession();
		
				
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		boolean result = false;
		HttpSession session=request.getSession();
		LeaveEncashmentHandler leaveEncashmentHandler =new LeaveEncashmentHandler();
		String action=request.getParameter("action");
		LeaveEncashmentBean leaveEncashmentBean=new LeaveEncashmentBean(); 
		int loggedEmployeeNo = (Integer)session.getAttribute("EMPNO");
		String emp=request.getParameter("EMPNO1")==null?"":request.getParameter("EMPNO1");
		
		String[] employ = emp.split(":");
	    String employeeNo= (employ[2].trim());
	    
	  //  String site=request.getParameter("proj")==null?"":request.getParameter("proj");	 
	    
	 /*   String[] site1 = site.split(":");
	    int siteNo= Integer.parseInt(site1[3].trim());*/
	  
	    
		if(action.equalsIgnoreCase("insert"))
		{
		leaveEncashmentBean.setEmpNo(Integer.parseInt(employeeNo));
		
		
		leaveEncashmentBean.setLeaveBal(Float.parseFloat(request.getParameter("lbal")));
		
		leaveEncashmentBean.setMaxLimit(Float.parseFloat(request.getParameter("limit")));
	
		leaveEncashmentBean.setEncashApplicable(Float.parseFloat(request.getParameter("encash")));
	
		leaveEncashmentBean.setLeaveEncashmentSanction(Float.parseFloat(request.getParameter("lsanction")));
		
		leaveEncashmentBean.setLeaveEncashmentDate(request.getParameter("edate"));
	
		leaveEncashmentBean.setFromDate (request.getParameter("frmdate"));
		
		leaveEncashmentBean.setToDate(request.getParameter("todate"));
	
		leaveEncashmentBean.setMonthlyGross(Float.parseFloat(request.getParameter("gross")));
		
		leaveEncashmentBean.setEsicAmt(Float.parseFloat(request.getParameter("esic")));
		
		leaveEncashmentBean.setEncashmentAmt(Float.parseFloat(request.getParameter("ENCASHAMT")));
	
		
		result = leaveEncashmentHandler.addLeaveEncash(leaveEncashmentBean,loggedEmployeeNo);
		if(result)
			response.sendRedirect("leaveEncashment.jsp?&flag=1");
			else
				response.sendRedirect("leaveEncashment.jsp?&flag=2");	
		}
		
		/*ACTION==> GETINFO*/
		
		else if(action.equalsIgnoreCase("getInfo"))
		{
	
			String EMPNO;
			EMPNO=request.getParameter("EMPNO");
			String emp1=request.getParameter("EMPNO")==null?"":request.getParameter("EMPNO");
			
			String[] employ1 = emp1.split(":");
		    String employeeNo1= (employ1[2].trim());
			    
			ArrayList<LeaveEncashmentBean> leaveEncashmentList = new ArrayList<LeaveEncashmentBean>();
			leaveEncashmentBean=leaveEncashmentHandler.getEmployeeInfo(employeeNo1);
          
		    
		  
		    request.setAttribute("leaveEncashmentBean",leaveEncashmentBean); 
		    request.setAttribute("empno1",EMPNO); 
		 
		//    session.setAttribute("trlist",trlist);
		    request.getRequestDispatcher("leaveEncashment.jsp?action=getdata").forward(request, response);
		    
		   // response.sendRedirect("tranMaintainences.jsp?action=getdata");
		       
		}
		
		
	}
	
	
	

}
