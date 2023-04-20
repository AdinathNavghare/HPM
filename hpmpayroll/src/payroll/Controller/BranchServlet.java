package payroll.Controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import payroll.DAO.BranchDAO;
import payroll.Model.BranchBean;

/**
 * Servlet implementation class BranchServlet
 */
@WebServlet("/BranchServlet")
public class BranchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BranchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action")==null?"":request.getParameter("action");
		ArrayList<BranchBean> branchList = new ArrayList<>();
		BranchDAO brnDAO = new BranchDAO();
		HttpSession session = request.getSession();
		if(action.equalsIgnoreCase("showBranch")){
			//System.out.println("in get");
			branchList = brnDAO.getBranchDetails();
			request.getRequestDispatcher("Branch.jsp?action=show").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action")==null?"":request.getParameter("action");
		ArrayList<BranchBean> branchList = new ArrayList<>();
		HttpSession session = request.getSession();
		BranchDAO brnDAO = new BranchDAO();
		if(action.equalsIgnoreCase("AddBranch")){
			BranchBean brnchBean = new BranchBean();
			brnchBean.setBRNAME(request.getParameter("brname")==null?"":request.getParameter("brname"));
			brnchBean.setADDR1(request.getParameter("addr1")==null?"":request.getParameter("addr1"));
			brnchBean.setADDR2(request.getParameter("addr2")==null?"":request.getParameter("addr2"));
			brnchBean.setSTATECD(request.getParameter("prstate")==null?0:Integer.parseInt(request.getParameter("prstate")));
			brnchBean.setCITYCD(request.getParameter("prCity")==null?0:Integer.parseInt(request.getParameter("prCity")));
			brnchBean.setABBRV(request.getParameter("ABBRV")==null?"":request.getParameter("ABBRV"));
			brnchBean.setPin(request.getParameter("pin")==null?0:Integer.parseInt(request.getParameter("pin")));
			brnDAO.addBranch(brnchBean);
			branchList = brnDAO.getBranchDetails();
			session.setAttribute("brnList", branchList);
			request.getRequestDispatcher("Branch.jsp?action=show").forward(request, response);
			
		}
		if(action.equalsIgnoreCase("EditBrnch")){
			BranchBean brnchBean = new BranchBean();
			brnchBean.setBRCD(request.getParameter("brcd")==null?0:Integer.parseInt(request.getParameter("brcd")));
			brnchBean.setBRNAME(request.getParameter("brname")==null?"":request.getParameter("brname"));
			brnchBean.setADDR1(request.getParameter("addr1")==null?"":request.getParameter("addr1"));
			brnchBean.setADDR2(request.getParameter("addr2")==null?"":request.getParameter("addr2"));
			brnchBean.setSTATECD(request.getParameter("prstate")==null?0:Integer.parseInt(request.getParameter("prstate")));
			brnchBean.setCITYCD(request.getParameter("prCity")==null?0:Integer.parseInt(request.getParameter("prCity")));
			brnchBean.setABBRV(request.getParameter("ABBRV")==null?"":request.getParameter("ABBRV"));
			brnchBean.setPin(request.getParameter("pin")==null?0:Integer.parseInt(request.getParameter("pin")));
			brnDAO.updateBranchDetails(brnchBean);
			branchList = brnDAO.getBranchDetails();
			session.setAttribute("brnList", branchList);
			request.getRequestDispatcher("Branch.jsp?action=show").forward(request, response);
			
		}
	}

}
