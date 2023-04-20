package payroll.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import payroll.Core.Calculate;
import payroll.Core.ReportDAO;
import payroll.DAO.EmpAttendanceHandler;
import payroll.DAO.TranHandler;
import payroll.Model.AttendanceBean;
import payroll.Model.TranBean;

/**
 * Servlet implementation class CoreServlet
 */
@WebServlet("/CoreServlet")
public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CoreServlet() {
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
		String action = request.getParameter("action");
		
		
		if(action.equalsIgnoreCase("leftDT"))
		{
			Integer flag = null;
			String DT= request.getParameter("date");
			
			DateFormat readFormat = new SimpleDateFormat( "dd-MMM-yyyy");

		    DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd");
		    Date date = null;
		    try {
		       date = readFormat.parse(DT);
		    } catch ( Exception e ) {
		        e.printStackTrace();
			} /*
				 * catch (java.text.ParseException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

		    String formattedDate = "";
		    if( date != null ) {
		    formattedDate = writeFormat.format( date );
		    }

			System.out.println("5555 : " +formattedDate);
			String LFT_date =request.getParameter("lDate");
			String WeekOF_dt = null;
			
				ArrayList<AttendanceBean> WOFFList=new ArrayList<AttendanceBean>();
			  EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
			  AttendanceBean getattBean =  new AttendanceBean();
			
			  try {
				  flag=empAttendanceHandler.getLeftWOFFList(formattedDate);
				  
				 /* HttpSession session = request.getSession(true);
				  session.setAttribute("flg", flag);
					System.out.println("CheckFlag :"+flag);*/
				
					PrintWriter out = response.getWriter();
					response.setContentType("text/html");
					out.write(flag.toString());// return flag to ajax in RelievingNew.jsp.
					
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			    
		}
		
		
		if(action.equalsIgnoreCase("calc"))
		{	
			String empList = request.getParameter("list");
			/*int[] empList = new int[vals.length];
			for(int i = 0; i<vals.length; i++)
			{
				empList[i] = Integer.parseInt(vals[i]);
			}*/
			String dt = request.getParameter("date");

			//String empList = request.getParameter("list");

			dt = "25-"+dt;
			String date=ReportDAO.EOM(dt);
			HttpSession session = request.getSession();
			String uid = session.getAttribute("UID").toString();
			int flag =Calculate.pay_cal(date, empList,uid);
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			switch(flag)
			{
				case -2:out.write("Some Error has occurred while Calculation, Please try again!");
						break;
				case -1:out.write("No Employees found for Calculation");
						break;
				case -0:out.write("Payroll Calculation is done Successfully");
						break;
				default:
					ArrayList<TranBean> nslist = TranHandler.getNagtiveSalaryList(date);
					String negList="";
					for(TranBean tb:nslist)
					{
					negList +="\n"+tb.getEMPNAME()+",";
					}
					out.write("Salary of Employee(s)  "+negList+" \n has gone Negative, Please correct it and Process the Calculation again!");
					
			}	
			
		}
		else if(action.equalsIgnoreCase("negtvsal"))
		{
			String dt = request.getParameter("date");
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");//yyyy-MM-dd'T'HH:mm:ss
		    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
			String result="";
			try
			{
				 Date data = sdf.parse(dt);
				 ArrayList<TranBean> nslist = TranHandler.getNagtiveSalaryList( output.format(data));
				// result=result+"VIJAY KULKARNI "+"51  <a onclick=\"getTrnsation(51)\" style=\"cursor: pointer;\"/><font color=\"red\">Get Detail<font></a><br>";
			for(TranBean trbn: nslist)
			{
				result=result+trbn.getEMPNO()+" "+trbn.getEMPNAME()+"  <a onclick=\"getTrnsation("+trbn.getEMPNO()+")\" style=\"cursor: pointer;\"/><font color=\"red\">Get Detail<font></a><br>";
			}
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write(result);
			}
			catch(Exception e){e.printStackTrace();}
		}
		else if(action.equalsIgnoreCase("negtvsalmnth"))
		{
			try
			{
				StringBuilder result=TranHandler.getNagtiveSalaryMonth();
				PrintWriter out = response.getWriter();
				response.setContentType("text/html");
				out.write(result.toString());
			}catch(Exception e){e.printStackTrace();}
		}
		else if(action.equalsIgnoreCase("notfinalized"))
		{
			try
			{
				StringBuilder result=TranHandler.getNotFinalized();
				PrintWriter out = response.getWriter();
				response.setContentType("text/html");
				out.write(result.toString());
			}catch(Exception e){e.printStackTrace();}
		}
		if(action.equalsIgnoreCase("import"))
		{	
			String dt = request.getParameter("date");
			String dt1 = request.getParameter("next");
			int flag = TranHandler.nextMonthImport(dt,dt1);
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			switch(flag)
			{
				case -2:out.write("Salary Of all employees has not been Finalize Please try again!");
						break;
				case -1:out.write("No Employees found for Calculation");
						break;
				case -0:out.write("Data Imported Successfully");
						break;
				default:
						out.write("Salary of Employee Number "+flag+" has gone Negative, Please correct it and Process the Calculation again!"); 
			}	
		}
	}
}