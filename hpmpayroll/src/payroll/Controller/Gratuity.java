package payroll.Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import payroll.DAO.GratuityDAO;

/**
 * Servlet implementation class Gratuity
 */
@WebServlet("/Gratuity")
public class Gratuity extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Gratuity() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String action=request.getParameter("action");
		
		if(action.equalsIgnoreCase("postgratuity"))
		{	
			String empno=	request.getParameter("empno");
			GratuityDAO ob=new GratuityDAO(); 
			ob. getGratuityAMt(empno);	
			
			request.getRequestDispatcher("Gratuity.jsp?flag=1").forward(request, response);
	
		}			
	}
}
