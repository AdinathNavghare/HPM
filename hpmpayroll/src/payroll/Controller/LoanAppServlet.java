package payroll.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import payroll.DAO.AdvanceHandler;
import payroll.DAO.LoanAppHandler;
import payroll.Model.LoanAppBean;

/**
 * Servlet implementation class LoanAppServlet
 */
@WebServlet("/LoanAppServlet")
public class LoanAppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoanAppServlet() {
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
		HttpSession session= request.getSession();	
		String action = request.getParameter("action")==null?"":request.getParameter("action");
		PrintWriter out = response.getWriter();
		LoanAppHandler loanhandler = new LoanAppHandler();
		ArrayList<LoanAppBean> customSearchList= new ArrayList<LoanAppBean>();
		LoanAppBean loanAppBean = new LoanAppBean() ;
		RequestDispatcher rd;
		int intFlag=0;
		if(action.equalsIgnoreCase("addTran"))
		{
			System.out.println("in addTran method of loanappservlet");
		
			String projectname = request.getParameter("project");
			
			
			System.out.println("PROJECT   :   "+projectname);
		
			String prj[]=projectname.split("::");
			
			String code=prj[1];
	
			boolean flag=false;	
			String EMPNO= request.getParameter("emplist");
			
			String[] parts = EMPNO.split(":");
			String part1 = parts[0]; 
			int empno=Integer.parseInt(part1);
			String guarntorname1=request.getParameter("gurantor_name")==null?"":request.getParameter("gurantor_name"); 
			String[] gurantor = guarntorname1.split(":");
			 String gurantorname = gurantor[0].trim();
			/*String gurantorcode = gurantor[1].trim();
		    String gurantorno = gurantor[2].trim();*/
		    
		    
		    
		String guarntorname2=request.getParameter("gurantor_name2")==null?"":request.getParameter("gurantor_name2"); 
		String gurantorname2="";
		
		if(!guarntorname2.equals("")){
			String[] gurantor2 = guarntorname2.split(":");
			  gurantorname2 = ","+gurantor2[0].trim();
		}
		
			loanAppBean = new LoanAppBean();
			//lb.setLoan_no(request.getParameter("loan_no")!=null?Integer.parseInt(request.getParameter("loan_no")):0);
			loanAppBean.setSite_id(Integer.parseInt(code));
			loanAppBean.setLoan_code(request.getParameter("loan_code")!=null?Integer.parseInt(request.getParameter("loan_code")):0);
			loanAppBean.setEMPNO(empno);
			loanAppBean.setLoan_amt(request.getParameter("loan_amt")!=null?Float.parseFloat(request.getParameter("loan_amt")):0);
			loanAppBean.setLoan_per(request.getParameter("loan_per")!=null?Float.parseFloat(request.getParameter("loan_per")):0);
			loanAppBean.setTotal_month(request.getParameter("pay_month")!=null?Integer.parseInt(request.getParameter("pay_month")):0);
			loanAppBean.setStart_date(request.getParameter("startDate")!=null?request.getParameter("startDate"):"");
			loanAppBean.setEnd_date(request.getParameter("EndDate")!=null?request.getParameter("EndDate"):"");
			loanAppBean.setMonthly_install((float) (request.getParameter("month_inst")!=null?Float.parseFloat(request.getParameter("month_inst")):0.00));
			loanAppBean.setBank_name(gurantorname+""+gurantorname2);
			loanAppBean.setSanctionby(request.getParameter("sancBy")!=null?request.getParameter("sancBy"):"");
			loanAppBean.setACTIVE(request.getParameter("active")!=null?request.getParameter("active"):"");
			loanAppBean.setActual_pay((request.getParameter("month_inst")!=null?Float.parseFloat(request.getParameter("month_inst")):0.00)*(request.getParameter("pay_month")!=null?Integer.parseInt(request.getParameter("pay_month")):0));
			System.out.println("setMonthly_install  : "+loanAppBean.getMonthly_install());
			loanAppBean.setAction_by((Integer)session.getAttribute("EMPNO"));
			
			if(loanAppBean.getLoan_code()==227){
			float salary =AdvanceHandler.getNetCtc(empno);
			float requestedLoanAmount=loanAppBean.getLoan_amt();
			
			if(requestedLoanAmount>salary){
				intFlag=1;
				response.sendRedirect("LoanApp.jsp?action=getdetails&prj="+code+"&flag=5");
				
			}
			}
			
		//	if(result>0)
		/*	{*/
			if(intFlag==0){
			flag=loanhandler.addloan(loanAppBean);
				if(flag){
					response.sendRedirect("LoanApp.jsp?action=getdetails&prj="+code+"&flag=3");
				}else{
						response.sendRedirect("LoanApp.jsp?action=getdetails&prj="+code+"&flag=4");
				}
				//System.out.println("query executed successfully"); 
			
			}
		/*}*/
	}
		
		if(action.equalsIgnoreCase("sanctionLoanApp")){	
			boolean flag = false;
			int empno = request.getParameter("empno")==null?0:Integer.parseInt(request.getParameter("empno"));
			int appNo = request.getParameter("appNo")==null?0:Integer.parseInt(request.getParameter("appNo"));
			String amount= request.getParameter("amt")==null?"":(request.getParameter("amt"));
			System.out.println(amount);
			int eno= request.getParameter("ENO")==null?0:Integer.parseInt(request.getParameter("ENO"));
		 float amt=Float.parseFloat(amount);
		 amt=Math.round(amt);
		 String Formonth=request.getParameter("forMonth")==null?"":(request.getParameter("forMonth"));
		 int loancode=request.getParameter("loancode")==null?0:Integer.parseInt(request.getParameter("loancode"));
				 

			try {
				flag = loanhandler.setSanction(empno, appNo, amt,eno, Formonth, loancode);
						if(flag){
					out.write("true");
						}
						else if(!flag){
							out.write("false");
						}
						
				}				
			
			catch (SQLException e) {
				e.printStackTrace();
			}
			loanAppBean = (LoanAppBean)session.getAttribute("loanSearchFilter");
			customSearchList = loanhandler.getLoanAppList(loanAppBean, "custom");
			request.setAttribute("customSearchList", customSearchList);
		
		}
		
		if(action.equalsIgnoreCase("cancelLoanApp")){
			
			int empno = request.getParameter("empno")==null?0:Integer.parseInt(request.getParameter("empno"));
			int appNo = request.getParameter("appNo")==null?0:Integer.parseInt(request.getParameter("appNo"));
			int eno= request.getParameter("ENO")==null?0:Integer.parseInt(request.getParameter("ENO"));
			
			boolean flag=loanhandler.cancelLoanApp(empno, appNo, eno);
			if (flag){
			out.write("true");
			}else{
				out.write("false");	
			}
			loanAppBean = (LoanAppBean) session.getAttribute("loanSearchFilter");
			customSearchList = loanhandler.getLoanAppList(loanAppBean, "custom");
			request.setAttribute("customSearchList", customSearchList);
			
			
		}
		
	if(action.equalsIgnoreCase("customSearch")){
			
			String type=request.getParameter("type")==null?"":request.getParameter("type");
			String fromemp=request.getParameter("EMPNO")==null?"":request.getParameter("EMPNO");//display on next page
			
			String[] employ = fromemp.split(":");
		    int frmEmpNo = Integer.parseInt(employ[2].trim());
		    System.out.println("from emp"+frmEmpNo);
		//	String fromdate=request.getParameter("frmdate")==null?"":request.getParameter("frmdate");
		//	String todate=request.getParameter("todate")==null?"":request.getParameter("todate");
			
			loanAppBean.setEMPNO(frmEmpNo);
			//loanAppBean.setStart_date(fromdate);
			//loanAppBean.setEnd_date(todate);
			loanAppBean.setACTIVE(type);
			session.setAttribute("loanSearchFilter", loanAppBean);
			customSearchList = loanhandler.getLoanAppList(loanAppBean, "custom");
			request.setAttribute("customSearchList", customSearchList);
			rd = request.getRequestDispatcher("LoanAppMaster.jsp?action=customSearch");
		    rd.forward(request, response);
		}
	
	if(action.equals("closeLoan"))
	{
		int loan_No = Integer.parseInt(request.getParameter("loanID"));
		
		String flag = loanhandler.closeLoan(loan_No);
		out = response.getWriter();
		response.setContentType("text/html");
		out.write(flag);
		
	}
	
	

}

}