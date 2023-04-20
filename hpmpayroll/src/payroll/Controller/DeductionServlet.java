package payroll.Controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import payroll.DAO.DeductHandler;
import payroll.Model.DeductBean;

/**
 * Servlet implementation class DeductionServlet
 */
@WebServlet("/DeductionServlet")
public class DeductionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeductionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String action = request.getParameter("action");
		if(action.equalsIgnoreCase("delete"))
		{
			DeductHandler DH = new DeductHandler();
			String[] key = request.getParameter("key").split("-");
			int empno = Integer.parseInt(key[0]);
			int trncd = Integer.parseInt(key[1]);
			int srno = Integer.parseInt(key[2]);
			boolean flag = DH.delDedction(empno, trncd, srno);
			if(flag)
			{
				ArrayList<DeductBean> Dlist = DH.getDeduction(empno);
				request.setAttribute("Dlist", Dlist);
				request.getRequestDispatcher("Deduction.jsp?action=showList&empno="+empno).forward(request, response);
			}
			else
			{
				System.out.println("Error in saving deduction");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String action= request.getParameter("action");
		if(action.equalsIgnoreCase("addnew"))
		{
			DeductBean DB = new DeductBean();
			DeductHandler DH = new DeductHandler();
			int empno = Integer.parseInt(request.getParameter("empno"));
			int trncd = Integer.parseInt(request.getParameter("select.trncd"));
			int srno = DH.getMaxSrno(empno, trncd)+1;
			DB.setEMPNO(empno);
			DB.setTRNCD(trncd);
			DB.setSRNO(srno);
			DB.setAMOUNT(Float.parseFloat(request.getParameter("install")));
			DB.setSUBSYS_CD(request.getParameter("subSysCode"));
			DB.setAC_NO(Long.parseLong(request.getParameter("acno")));
			DB.setBODSANCNO(request.getParameter("sancNo"));
			DB.setSANC_DATE(request.getParameter("sancDate"));
			DB.setSANC_AMT(Integer.parseInt(request.getParameter("sacnAmt")));
			DB.setINT_RATE(0.0f);
			DB.setREPAY_START(request.getParameter("startDate"));
			DB.setEND_DATE(request.getParameter("endDate"));
			DB.setCUMUYN(request.getParameter("select.cuml"));
			DB.setACTYN(request.getParameter("select.active"));
			DB.setNo_Of_Installment(Integer.parseInt(request.getParameter("Installments")));
			boolean flag = DH.addDeduction(DB);
			if(flag)
			{
				ArrayList<DeductBean> Dlist = DH.getDeduction(empno);
				request.setAttribute("Dlist", Dlist);
				request.getRequestDispatcher("Deduction.jsp?action=showList&empno="+empno).forward(request, response);
			}
			else
			{
				System.out.println("Error in saving deduction");
			}
		}
		else if(action.equalsIgnoreCase("list"))
		{
			try
			{
				String []empno1 = request.getParameter("EMPNO").split(":");
				int empno = Integer.parseInt(empno1[2].trim());
				DeductHandler DH = new DeductHandler();
				ArrayList<DeductBean> Dlist = DH.getDeduction(empno);
				request.setAttribute("Dlist", Dlist);
				request.getRequestDispatcher("Deduction.jsp?action=showList&empno="+empno).forward(request, response);
			}
			catch(Exception e)
			{
				response.sendRedirect("Deduction.jsp");
			}
		}
		else if(action.equalsIgnoreCase("details"))
		{
			String[] key = request.getParameter("key").split("-");
			int empno = Integer.parseInt(key[0]);
			int trncd = Integer.parseInt(key[1]);
			int srno = Integer.parseInt(key[2]);
			DeductHandler DH = new DeductHandler();
			DeductBean DB = DH.getSingleDed(empno, trncd, srno);
		}
		else if(action.equalsIgnoreCase("modify"))
		{
			int empno=Integer.parseInt(request.getParameter("empno"));
			int trncd=Integer.parseInt(request.getParameter("trncd"));
			int srno=Integer.parseInt(request.getParameter("srno"));
			DeductBean DB= new DeductBean();
			DeductHandler DH = new DeductHandler();
			DB.setAMOUNT(Float.parseFloat(request.getParameter("install")));
			DB.setSUBSYS_CD(request.getParameter("subSysCode"));
			DB.setAC_NO(Long.parseLong(request.getParameter("acno")));
			DB.setBODSANCNO(request.getParameter("sancNo"));
			DB.setSANC_DATE(request.getParameter("sancDate"));
			DB.setSANC_AMT(Integer.parseInt(request.getParameter("sacnAmt")));
			DB.setINT_RATE(Float.parseFloat(request.getParameter("intRate")));
			DB.setREPAY_START(request.getParameter("startDate"));
			DB.setEND_DATE(request.getParameter("endDate"));
			DB.setCUMUYN(request.getParameter("select.cuml"));
			DB.setACTYN(request.getParameter("select.active"));
			boolean flag = DH.updateDeduction(empno, trncd, srno, DB);
			if(flag)
			{
				response.sendRedirect("updateDeduction.jsp?action=close&key="+empno+"-"+trncd+"-"+srno);
			}
			else
			{
				response.sendRedirect("updateDeduction.jsp?action=keep&key="+empno+"-"+trncd+"-"+srno);
			}
		}
	}
}