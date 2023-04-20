package payroll.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import payroll.DAO.ConnectionManager;
import payroll.DAO.LMH;
import payroll.Model.LMB;


/**
 * Servlet implementation class LMS
 */
@WebServlet("/LMS")
public class LMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LMB leaveBean = new LMB();
		LMH leaveHandler=new LMH();
		HttpSession session = request.getSession();
		ArrayList<LMB> customSearchList= new ArrayList<LMB>();
		boolean result = false;
		RequestDispatcher rd;
		String action = request.getParameter("action")==null?"":request.getParameter("action");
		PrintWriter out = response.getWriter();
		
		if(action.equalsIgnoreCase("addLeave")){			
			leaveBean.setTRNDATE(request.getParameter("tdate")==null?"":request.getParameter("tdate"));
			leaveBean.setLEAVECD(request.getParameter("leavecode")==null?0:Integer.parseInt(request.getParameter("leavecode")));
			leaveBean.setFRMDT(request.getParameter("frmdate")==null?"":request.getParameter("frmdate"));
			leaveBean.setTODT(request.getParameter("todate")==null?"":request.getParameter("todate"));
			leaveBean.setLREASON(request.getParameter("lreason")==""?"---":request.getParameter("lreason"));
			leaveBean.setLADDR(request.getParameter("addDuringleave")==""?"---":request.getParameter("addDuringleave"));
			leaveBean.setLTELNO(request.getParameter("telephone")==""?0:Long.parseLong(request.getParameter("telephone")));
			leaveBean.setHALFDAY(request.getParameter("halfday")==null?"":request.getParameter("halfday"));
			leaveBean.setEMPNO(request.getParameter("empno")==null?0:Integer.parseInt(request.getParameter("empno")));
			leaveBean.setPrj_srno(request.getParameter("prj_srno")==null?"":request.getParameter("prj_srno"));
			result = leaveHandler.addLeave(leaveBean);
			if(result == false){
				rd = request.getRequestDispatcher("leave.jsp?error=Please Check Your Leave Balance");
			}
			else{
				rd = request.getRequestDispatcher("leave.jsp");
			}
			rd.forward(request, response);
			
		}
		if(action.equalsIgnoreCase("editleave")){
			leaveBean.setTRNDATE(request.getParameter("tdate")==null?"":request.getParameter("tdate"));
			leaveBean.setLEAVECD(request.getParameter("leavecode")==null?0:Integer.parseInt(request.getParameter("leavecode")));
			leaveBean.setFRMDT(request.getParameter("frmdate")==null?"":request.getParameter("frmdate"));
			leaveBean.setTODT(request.getParameter("todate")==null?"":request.getParameter("todate"));
			leaveBean.setLREASON(request.getParameter("lreason")==null?"--":request.getParameter("lreason"));
			leaveBean.setLADDR(request.getParameter("addDuringleave")==null?"--":request.getParameter("addDuringleave"));
			leaveBean.setLTELNO(request.getParameter("telephone")==""?0:Long.parseLong(request.getParameter("telephone")));
			leaveBean.setHALFDAY(request.getParameter("halfday")==null?"":request.getParameter("halfday"));
			leaveBean.setEMPNO(request.getParameter("empno")==null?0:Integer.parseInt(request.getParameter("empno")));
			leaveBean.setAPPLNO(request.getParameter("appNo")==null?"":request.getParameter("appNo"));
			leaveBean.setPrj_srno(request.getParameter("prj_srno")==null?"":request.getParameter("prj_srno"));
			result = leaveHandler.editLeave(leaveBean);
			if(result == false){
				 rd = request.getRequestDispatcher("leave.jsp?error=Please Check Your Leave Balance");
			}
			else{
			rd = request.getRequestDispatcher("leave.jsp");
			}
			rd.forward(request, response);
			
		}
		if(action.equalsIgnoreCase("ExtraLeave")){
			
			ArrayList<LMB> leaveBalList = new ArrayList<LMB>();
		    int EMPNO =Integer.parseInt(session.getAttribute("empno").toString());
		    System.out.println(EMPNO);
		    leaveBean.setEMPNO(EMPNO);
			leaveBean.setTRNDATE(request.getParameter("tdate")==null?"":request.getParameter("tdate"));
			leaveBean.setLEAVECD(request.getParameter("leavecode")==null?0:Integer.parseInt(request.getParameter("leavecode")));
			leaveBean.setFRMDT(request.getParameter("frmdate")==null?"":request.getParameter("frmdate"));
			leaveBean.setTODT(request.getParameter("todate")==null?"":request.getParameter("todate"));
			leaveBean.setLREASON(request.getParameter("lreason")==""?"---":request.getParameter("lreason"));
			leaveBean.setTRNTYPE(request.getParameter("leave").charAt(0));
			leaveBean.setHALFDAY(request.getParameter("halfday")==null?"":request.getParameter("halfday"));
			
			result = leaveHandler.addExtraLeave(leaveBean);
			leaveBalList = leaveHandler.getList(EMPNO);
			request.setAttribute("leaveBalList", leaveBalList);
			if(result){
				request.getRequestDispatcher("ExtraLeaves.jsp?action=getData").forward(request, response);
			}
		}
		if(action.equalsIgnoreCase("getLeaveApp")){
			int empno = request.getParameter("empno")==null?0:Integer.parseInt(request.getParameter("empno"));
			int appNo = request.getParameter("appNo")==null?0:Integer.parseInt(request.getParameter("appNo"));
			ArrayList<LMB> leaveApp = new ArrayList<LMB>();
			leaveApp = leaveHandler.getLeaveApp(empno, appNo);
			System.out.println("lapp="+leaveApp.toString());
			request.setAttribute("leaveApp", leaveApp);
			rd = request.getRequestDispatcher("leave.jsp?action=editLeaveApp");
		    rd.forward(request, response);
		}
		
		if(action.equalsIgnoreCase("sanctionLeaveApp")){	
			boolean flag = false;
			int empno = request.getParameter("empno")==null?0:Integer.parseInt(request.getParameter("empno"));
			int appNo = request.getParameter("appNo")==null?0:Integer.parseInt(request.getParameter("appNo"));
			try {
				flag = leaveHandler.setSanction(empno, appNo);
				if(flag == true){
					out.write("true");
				}				
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			leaveBean = (LMB)session.getAttribute("leaveSearchFilter");
			customSearchList = leaveHandler.getLeaveAppList(leaveBean, "custom");
			request.setAttribute("customSearchList", customSearchList);
			
			/*if(flag == false){
				 rd = request.getRequestDispatcher("sanctionLeave.jsp?action=customSearch&error=Please Check Leave Balance");
			}else{
				rd = request.getRequestDispatcher("sanctionLeave.jsp?action=customSearch&error=Leave Sanctioned Successfully");
			}
			rd.forward(request, response);*/
		}
		
		if(action.equalsIgnoreCase("cancelLeaveApp")){
			
			int empno = request.getParameter("empno")==null?0:Integer.parseInt(request.getParameter("empno"));
			int appNo = request.getParameter("appNo")==null?0:Integer.parseInt(request.getParameter("appNo"));
			
			leaveHandler.cancelLeaveApp(empno, appNo);
			out.write("true");
			leaveBean = (LMB) session.getAttribute("leaveSearchFilter");
			customSearchList = leaveHandler.getLeaveAppList(leaveBean, "custom");
			request.setAttribute("customSearchList", customSearchList);
			
			/*rd = request.getRequestDispatcher("sanctionLeave.jsp?action=customSearch&error=Leave Canceled");
		    rd.forward(request, response);*/
		}
		if(action.equalsIgnoreCase("customSearch")){
			
			String type=request.getParameter("type")==null?"":request.getParameter("type");
			String fromemp=request.getParameter("EMPNO")==null?"":request.getParameter("EMPNO");//display on next page
			String toemp=request.getParameter("EMPNO1")==null?"":request.getParameter("EMPNO1");//display on next page
			String[] employ = fromemp.split(":");
		    int frmEmpNo = Integer.parseInt(employ[2].trim());
		    employ = toemp.split(":");
		    int toEmpNo = Integer.parseInt(employ[2].trim());
			String fromdate=request.getParameter("frmdate")==null?"":request.getParameter("frmdate");
			String todate=request.getParameter("todate")==null?"":request.getParameter("todate");
			leaveBean.setEMPNO(frmEmpNo);
			leaveBean.setEMPNO2(toEmpNo);
			leaveBean.setFRMDT(fromdate);
			leaveBean.setTODT(todate);
			leaveBean.setSTATUS(type);
			session.setAttribute("leaveSearchFilter", leaveBean);
			customSearchList = leaveHandler.getLeaveAppList(leaveBean, "custom");
			request.setAttribute("customSearchList", customSearchList);
			rd = request.getRequestDispatcher("sanctionLeave.jsp?action=customSearch");
		    rd.forward(request, response);
		}
		if(action.equalsIgnoreCase("getLeave")){
			
			int EMPNO;
			ArrayList<LMB> leaveBalList = new ArrayList<LMB>();
			String list=request.getParameter("list")==null?"":request.getParameter("list");
			System.out.println(list);
			if(list.equalsIgnoreCase(""))
			{
				String[] employ = request.getParameter("EMPNO").split(":");
			    EMPNO = Integer.parseInt(employ[2].trim());
			}	
			else
			{
				EMPNO= Integer.parseInt(request.getParameter("EMPNO"));
			}
			session.setAttribute("empno", EMPNO);
			leaveBalList = leaveHandler.getList(EMPNO);
			request.setAttribute("leaveBalList", leaveBalList);
			
			rd = request.getRequestDispatcher("ExtraLeaves.jsp?action=getData");
		    rd.forward(request, response);
		}
		if(action.equalsIgnoreCase("DeleteLeave")){
			ArrayList<LMB> leaveBalList = new ArrayList<LMB>();
			int empno = Integer.parseInt(request.getParameter("empno"));
			int leavecd = Integer.parseInt(request.getParameter("leavecd"));
			Connection con = ConnectionManager.getConnection();
			try{
				Statement st = con.createStatement();
				st.execute("DELETE FROM LEAVEBAL WHERE EMPNO="+empno+"AND LEAVECD="+leavecd);
				st.execute("DELETE FROM LEAVETRAN WHERE EMPNO="+empno+"AND LEAVECD="+leavecd);
				
				st.close();
				con.close();
				
				leaveBalList = leaveHandler.getList(empno);
				request.setAttribute("leaveBalList", leaveBalList);
				rd = request.getRequestDispatcher("ExtraLeaves.jsp?action=getData");
			    rd.forward(request, response);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}	
	}
}
