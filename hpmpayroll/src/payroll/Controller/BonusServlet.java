package payroll.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import payroll.DAO.ConnectionManager;

/**
 * Servlet implementation class BonusServlet
 */
@WebServlet("/BonusServlet")
public class BonusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BonusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String action = request.getParameter("action")==null?"":request.getParameter("action");
	
		if(action.equalsIgnoreCase("postBonus"))
		{
			Connection cn = ConnectionManager.getConnection();
			try {
				Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			//	Statement s = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				Statement st2 = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				Statement st1 = cn.createStatement();
				//s.execute("insert into paytran Select * from bonustran");

				st.execute("insert into paytran_stage Select * from bonustran");
				st2.execute("insert into ytdtran Select * from bonustran");

				st1.execute("delete from bonustran ");		
				response.sendRedirect("YearlyBonusReport.jsp?flag=1");
				
				
			} catch (SQLException e) {
				response.sendRedirect("YearlyBonusReport.jsp?flag=2");
				e.printStackTrace();
			}
			
			
			
		
		
		}
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		
		String action = request.getParameter("action")==null?"":request.getParameter("action");
	
		if(action.equalsIgnoreCase("postBonus"))
		{
			String totalemp="";
			//System.out.println("inside post in bonus servlet");
			String checkbox[]=request.getParameterValues("list");
		
			for(int i=0;i<checkbox.length; i++){
			       totalemp += checkbox[i]+",";
				}
			
			
		     
		  	 if (totalemp.length() > 0 && totalemp.charAt(totalemp.length()-1)==',') {
		  		totalemp = totalemp.substring(0, totalemp.length()-1);
			    }
			
		  	 /*System.out.println("total employee "+totalemp);*/
		  	 
			Connection cn = ConnectionManager.getConnection();
			try {
				Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			//	Statement s = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

				Statement st2 = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

				Statement st1 = cn.createStatement();
				
				//s.execute("insert into paytran Select * from bonustran where  empno in ("+totalemp+") ");

				st.execute("insert into paytran_stage Select * from bonustran where  empno in ("+totalemp+") ");
				st2.execute("insert into ytdtran Select * from bonustran where  empno in ("+totalemp+") ");
				st1.execute("delete from bonustran where empno in ("+totalemp+") ");		
			    response.sendRedirect("YearlyBonusReport.jsp?flag=2");
				
				
	        	} catch (SQLException e) {
				response.sendRedirect("YearlyBonusReport.jsp?flag=3");
				e.printStackTrace();
	   	}
			
		}
		
		
		
	
	}

}
