package payroll.Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import payroll.DAO.GradeHandler;
import payroll.Model.GradeBean;

/**
 * Servlet implementation class GradeServlet
 */
@WebServlet("/GradeServlet")
public class GradeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GradeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String action = request.getParameter("action");
		if(action.equalsIgnoreCase("details"))
		{
			int post = Integer.parseInt(request.getParameter("postcd"));
			String effdt = request.getParameter("effdt");
			GradeHandler GH = new GradeHandler();
			GradeBean GB = GH.getGrade(post, effdt);
			if(GB!=null)
			{
				request.setAttribute("detail", GB);
				request.getRequestDispatcher("Grade.jsp?action=show").forward(request, response);
			}
			else
			{
				response.sendRedirect("Grade.jsp");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String action = request.getParameter("action");
		if(action.equalsIgnoreCase("addnew"))
		{
			GradeHandler GH = new GradeHandler();
			GradeBean GB = new GradeBean();
			GB.setPOSTCD(Integer.parseInt(request.getParameter("postcd")));
			GB.setALFACD(request.getParameter("alpha"));
			GB.setDISC(request.getParameter("desc"));
			GB.setEFFDT(request.getParameter("effdt"));
			GB.setBASIC(Integer.parseInt(request.getParameter("basic")==""?"0":request.getParameter("basic")));
			GB.setINCR1(Integer.parseInt(request.getParameter("incr1")==""?"0":request.getParameter("incr1")));
			GB.setNOY1(Integer.parseInt(request.getParameter("noy1")==""?"0":request.getParameter("noy1")));
			GB.setINCR2(Integer.parseInt(request.getParameter("incr2")==""?"0":request.getParameter("incr2")));
			GB.setNOY2(Integer.parseInt(request.getParameter("noy2")==""?"0":request.getParameter("noy2")));
			GB.setINCR3(Integer.parseInt(request.getParameter("incr3")==""?"0":request.getParameter("incr3")));
			GB.setNOY3(Integer.parseInt(request.getParameter("noy3")==""?"0":request.getParameter("noy3")));
			GB.setINCR4(Integer.parseInt(request.getParameter("incr4")==""?"0":request.getParameter("incr4")));
			GB.setNOY4(Integer.parseInt(request.getParameter("noy4")==""?"0":request.getParameter("noy4")));
			GB.setINCR5(Integer.parseInt(request.getParameter("incr5")==""?"0":request.getParameter("incr5")));
			GB.setNOY5(Integer.parseInt(request.getParameter("noy5")==""?"0":request.getParameter("noy5")));
			GB.setINCR6(Integer.parseInt(request.getParameter("incr6")==""?"0":request.getParameter("incr6")));
			GB.setNOY6(Integer.parseInt(request.getParameter("noy6")==""?"0":request.getParameter("noy6")));
			GB.setEXG(Integer.parseInt(request.getParameter("exg")==""?"0":request.getParameter("exg")));
			GB.setMED(Integer.parseInt(request.getParameter("medical")==""?"0":request.getParameter("medical")));
			GB.setEDU(Integer.parseInt(request.getParameter("edu")==""?"0":request.getParameter("edu")));
			GB.setLTC(Integer.parseInt(request.getParameter("ltc")==""?"0":request.getParameter("ltc")));
			GB.setCLOSING(Integer.parseInt(request.getParameter("closing")==""?"0":request.getParameter("closing")));
			GB.setCONV(Integer.parseInt(request.getParameter("conv")==""?"0":request.getParameter("conv")));
			GB.setCASH(Integer.parseInt(request.getParameter("cash")==""?"0":request.getParameter("cash")));
			GB.setCLG(Integer.parseInt(request.getParameter("clearing")==""?"0":request.getParameter("clearing")));
			GB.setWASHING(Integer.parseInt(request.getParameter("washing")==""?"0":request.getParameter("washing")));
			GB.setFLDWRK(Integer.parseInt(request.getParameter("field")==""?"0":request.getParameter("field")));
			
			boolean flag = GH.addGrade(GB);
			if(flag)
			{
				response.sendRedirect("Grade.jsp?action=saved");
			}
			else
			{
				response.sendRedirect("Grade.jsp");
			}
		}
		else if(action.equalsIgnoreCase("delete"))
		{
			int post = Integer.parseInt(request.getParameter("postcd"));
			String effdt = request.getParameter("effdt");
			GradeHandler GH = new GradeHandler();
			boolean flag = GH.deleteGrade(post, effdt);
			if(flag)
			{
				response.sendRedirect("Grade.jsp?action=deleted");
			}
			else
			{
				GH = new GradeHandler();
				GradeBean GB = GH.getGrade(post, effdt);
				request.setAttribute("detail", GB);
				request.getRequestDispatcher("Grade.jsp?action=notDel").forward(request, response);
			}
		}
	}
}