package payroll.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import payroll.Core.Calculate;
import payroll.Core.ReportDAO;
import payroll.DAO.ConnectionManager;
import payroll.DAO.EmpAttendanceHandler;
import payroll.DAO.RelieveInfoHandler;
import payroll.DAO.RoleDAO;
import payroll.Model.Attend_bean;
import payroll.Model.CompBean;
import payroll.Model.RelieveInfoBean;
import payroll.Model.TranBean;

/**
 * Servlet implementation class EmpAttendServlet
 */
@WebServlet("/EmpAttendServlet")
public class EmpAttendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmpAttendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session=request.getSession();
		String action=request.getParameter("action")==null?"":request.getParameter("action");
		System.out.println("do get action......"+action);
		String status1="";
		/*if(!action.equalsIgnoreCase("reject"))
		{
		 status1=request.getParameter("status")==null?"":request.getParameter("status");
		System.out.println("do get status......"+status1);
		}
		*/String date="";
		if(!action.equalsIgnoreCase("getChecked"))//only for other action need this attribute
		{
			if(!action.equalsIgnoreCase("reject")){
				
			if(request.getParameter("status").equalsIgnoreCase("Transfer")|| request.getParameter("status").equalsIgnoreCase("SingleApprove")||request.getParameter("status").equalsIgnoreCase("saved")||request.getParameter("status").equalsIgnoreCase("approved"))
			{
				System.out.println("ap...");
				status1=request.getParameter("status")==null?"":request.getParameter("status");
				date=request.getParameter("date")==null?request.getParameter("date1"):request.getParameter("date");
			}}
			else
			{
			 date=request.getParameter("date")==null?request.getParameter("lDate"):request.getParameter("date");
			}
		}
		System.out.println("do get date......"+date);
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		int eno=0;
		if(session.getAttribute("Prj_Srno")!=null)
		 eno = (Integer)session.getAttribute("EMPNO");
		int site_id=0;
		if(session.getAttribute("Prj_Srno")!=null || request.getParameter("prj")!=null)
		 site_id=request.getParameter("prj")==null?(Integer)session.getAttribute("Prj_Srno"):Integer.parseInt(request.getParameter("prj"));
		ArrayList<TranBean> tran = new ArrayList<TranBean>();
		
		tran=(ArrayList<TranBean>)session.getAttribute("emplist");
		if(action.equalsIgnoreCase("approval"))
		{
			
		
			String status="pending";
			System.out.println("sending to aprroval for att sheet");
					status=EAH.setAttendanceStatus(date,site_id,status,eno);
			if(status.equalsIgnoreCase("pending"))
			{
				
				response.sendRedirect("attendanceMain.jsp?status=pending");
			}
			else if(status.equalsIgnoreCase("rejected"))
			{	
				request.getRequestDispatcher("EmpPresentSeat.jsp?date="+date+"&flag=5").forward(request, response);
			}
			else
			{
				request.getRequestDispatcher("EmpPresentSeat.jsp?date="+date+"&flag=3").forward(request, response);
			}
		}
		
		if(action.equalsIgnoreCase("approved"))
		{
			
			
			String status="approved";
			System.out.println("going to approved sheet");
					status=EAH.setAttendanceStatus(date,site_id,status,eno);
			if(status.equalsIgnoreCase("approved"))
			{
				
				response.sendRedirect("approveAttendance.jsp?date="+date+"&flag=1");
			}
			if(status.equalsIgnoreCase("notMatch"))
			{
				
				response.sendRedirect("approveAttendance.jsp?date="+date+"&flag=100");
			}
			
		}
		
		if(action.equalsIgnoreCase("empApproved"))
		{
			
		String date1=request.getParameter("date1");
		String status=request.getParameter("status");
		System.out.println("first status...."+status);
		String checkstatus=request.getParameter("status");
		int empNo=Integer.parseInt(request.getParameter("empNo"));
		System.out.println("empno.....empaproved....."+empNo);
		String act=request.getParameter("act")==null?"ADD":request.getParameter("act");
		
		
		try {
			
			if(status.equals("transfer")){
			status=EAH.setAttendanceTransferApproved(date1,empNo,status,eno);
			}
			
			if(status.equals("left")){
				status=EAH.setAttendanceLeftApproved(date1,empNo,status,eno);
				}
			
			if(status.equals("SingleApprove")){
				System.out.println("status in sngleaprove..insering into payran for 301");
				status=EAH.setAttendanceSingleEmpApproved(date1,empNo,status,eno);
				}
				
			
			if(status.equalsIgnoreCase("approved"))
			{
				if(!checkstatus.equalsIgnoreCase("left"))
				{
					if(checkstatus.equalsIgnoreCase("SingleApprove"))
					{
						
						response.sendRedirect("attendanceMain.jsp?&flag=SingleApprove");
					}
					else
					{
						System.out.println("going to attmain....");
						response.sendRedirect("attendanceMain.jsp?&flag=emptransfer");
					}
				}
				/*else{
				response.sendRedirect("attendanceMain.jsp?date="+date1);
				}*/
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		if(action.equalsIgnoreCase("reject"))
		{
			
		
			String status="rejected";
					status=EAH.setAttendanceStatus(date,site_id,status,eno);
			
			if(status.equalsIgnoreCase("rejected"))
			{	
				response.sendRedirect("approveAttendance.jsp?date="+date+"&flag=2");
			}
			
		}
		
		if(action.equalsIgnoreCase("empRejection"))
		{
			
			String date1=request.getParameter("date1");
		String status=request.getParameter("status");
		int empNo=Integer.parseInt(request.getParameter("empNo"));
		
		
		try {
			status=EAH.setEmpStatusRejected(date1,empNo,status,eno);
			if(status.equalsIgnoreCase("rejected"))
			{
				
				response.sendRedirect("attendanceMain.jsp?date="+date1);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		
		
		
		if(action.equalsIgnoreCase("getChecked"))
		{
			System.out.println("inside getChecked");
			String emp=request.getParameter("empNo")==null?"":request.getParameter("empNo");
			String[] employ = emp.split(":");
		    int empNo = Integer.parseInt(employ[2].trim());
		    
			try {
			ArrayList<TranBean> Emp_bean = new ArrayList<TranBean>();
				Emp_bean=EAH.getAssignedSitesList(empNo);
				request.setAttribute("ProjectList", Emp_bean);
				 request.getRequestDispatcher("AssignSite.jsp?action=display&empno="+empNo).forward(request,response);
				//response.sendRedirect("AssignSite.jsp?action=display&empno="+empNo);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
		if(action.equalsIgnoreCase("getAssignWeek"))
		{
			System.out.println("inside getChecked");
			
			
			
			String prjCode =request.getParameter("prj")==null?"":request.getParameter("prj");
		   
			try {
			ArrayList<CompBean> Emp_bean = new ArrayList<CompBean>();
				Emp_bean=EAH.getAssignweek(prjCode);
				request.setAttribute("AssignWeek", Emp_bean);
				 request.getRequestDispatcher("WeekOffAssign.jsp?action=display&prj="+prjCode).forward(request,response);
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			}
	

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try
		{
		HttpSession session=request.getSession();
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String action=request.getParameter("action");
		System.out.println("post action......"+action);
		String date=request.getParameter("adddate")==null?request.getParameter("date1"):request.getParameter("adddate");;
		System.out.println("post empatt datttttttttttttttttttt..........."+date);
	
		
		int eno = (Integer)session.getAttribute("EMPNO");
		RoleDAO role=new RoleDAO();
		String roleId=role.getrole(Integer.parseInt(session.getAttribute("UID").toString()));

		int site_id=0;
		if(session.getAttribute("Prj_Srno")!=null){
		 site_id=(Integer)session.getAttribute("Prj_Srno");
			System.out.println("site id inside post"+site_id);
		}
		ArrayList<TranBean> tran = new ArrayList<TranBean>();
		
		tran=(ArrayList<TranBean>)session.getAttribute("emplist");
		
		if(action.equalsIgnoreCase("insert"))
		{
			
			
			
			String st=request.getParameter("st")==""?"firstTimeSaved":request.getParameter("st");
	
			ArrayList  Emp_al=new ArrayList();
		
			int index=0;
			int count=0;
			int a=0;
			int counter=1;
			float temp=0.0f;
			int K=0;
			int j=0;
			int totalDay=0;
			int totalEmp=0;
		
			if(st.equalsIgnoreCase("firstTimeSaved") /*|| roleId.equalsIgnoreCase("1")*/)
			{
				System.out.println("On first save");
				
				for (TranBean tbean : tran) 
				{	
					String	date1=ReportDAO.BOM(date);
					ArrayList<TranBean> Emp_bean=new ArrayList<TranBean>();
					String vals[] = request.getParameterValues("_"+tbean.getEMPNO());
					
					for(int i=0;i<vals.length;i++)
					{
						TranBean ab=new TranBean();
						ab.setEMPNO(tbean.getEMPNO());
						ab.setSite_id(Integer.toString(site_id));
					
						ab.setDate(date1);
						ab.setVal(vals[i].toUpperCase());
					
						Emp_bean.add(ab);
						
						SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
						Calendar c = Calendar.getInstance();    
						c.setTime(format.parse(date1));
						c.add(Calendar.DATE, 1);
						date1=format.format(c.getTime());
					
					}
					
					Emp_al.add(Emp_bean);
					
             
				}	
				boolean flag=EAH.insertEmpAttendance(Emp_al,eno,st,roleId);
				String status="";
			
				status="saved";
			
				status=EAH.setAttendanceStatus(date,site_id,status,eno);
				if(flag)
				{
					if(roleId.equals("1")){
						response.sendRedirect("EmpPresentSeat.jsp?date="+date+"&site_id="+site_id+"&flag=1");
				}
					else{
					response.sendRedirect("EmpPresentSeat.jsp?date="+date+"&site_id="+site_id+"&flag=6");
					}
				}
				else
				{
					request.getRequestDispatcher("EmpPresentSeat.jsp?date="+date+"&flag=2").include(request, response);
				}
			}
			
			else{
				System.out.println("for single date");
				for (TranBean tbean : tran) 
				{	
					String	date1=ReportDAO.BOM(date);
					String month= date1.substring(3,11); 
					ArrayList<TranBean> Emp_bean=new ArrayList<TranBean>();
					String vals[] = request.getParameterValues("_"+tbean.getEMPNO());
					String aDate = request.getParameter("column");
					
					String atDate="";
					if(Integer.parseInt(aDate)<10){
					atDate="0"+aDate+"-"+month; 
					}else{
						atDate=aDate+"-"+month;
					}
					 temp=Calculate.getDays(date1);
					 totalDay=(int)temp;
					 totalEmp=tran.size();
					int i=Integer.parseInt(aDate);
					K= (totalDay/2);
				//	 System.out.println("total days are"+totalDay);
					
					if(totalDay==28||totalDay==29)
						{
							K=K+1;
							
						}	
					
						int w=((totalEmp)*(K));
						
						int q=((((totalEmp)*(totalDay))));
					
						int y=(totalEmp*K)-16;
					
						if(i<=totalDay){
							
							if(i>15 && totalDay==30)
							{
						
							j=y+i;
						
							 for( j=j;j<q; j=j+K){ 
								
							//	 System.out.println("FOR MONTH HAVING 30 DAYS");
									
									TranBean ab=new TranBean();
									ab.setEMPNO(tbean.getEMPNO());
									ab.setSite_id(Integer.toString(site_id));
									ab.setDate(atDate);
									//ab.setVal(vals[i].toUpperCase());
									 ab.setVal(request.getParameter(Integer.toString(j)).toUpperCase());
								
									Emp_bean.add(ab);
								 
								
									}
						}
							else if(i>15 && totalDay==31)
							{
								j=y+i;
								
								for( j=j;j<q; j=j+K+1){ 
								
							//	 System.out.println("FOR MONTH HAVING 31 DAYS");
									
								 	TranBean ab=new TranBean();
									ab.setEMPNO(tbean.getEMPNO());
									ab.setSite_id(Integer.toString(site_id));
									ab.setDate(atDate);
									//ab.setVal(vals[i].toUpperCase());
									 ab.setVal(request.getParameter(Integer.toString(j)).toUpperCase());
							
									Emp_bean.add(ab);
								 
								 
								}
							}
							
							else if(i>15 && totalDay==28)
							{
								j=y+i;
									
								for( j=j;j<q; j=j+K-2){ 
							
							// System.out.println("FOR MONTH HAVING 28 DAYS");
									
							 	TranBean ab=new TranBean();
								ab.setEMPNO(tbean.getEMPNO());
								ab.setSite_id(Integer.toString(site_id));
								ab.setDate(atDate);
								//ab.setVal(vals[i].toUpperCase());
								 ab.setVal(request.getParameter(Integer.toString(j)).toUpperCase());
					
								Emp_bean.add(ab);
							 
								}
							}
							
							else if(i>15 && totalDay==29)
							{
				
								j=y+i;
								for( j=j;j<q; j=j+K-1){ 
								
							//		System.out.println("FOR MONTH HAVING 29 DAYS");
								
									
									 TranBean ab=new TranBean();
									ab.setEMPNO(tbean.getEMPNO());
									ab.setSite_id(Integer.toString(site_id));
									ab.setDate(atDate);
									//ab.setVal(vals[i].toUpperCase());
									 ab.setVal(request.getParameter(Integer.toString(j)).toUpperCase());
							
									Emp_bean.add(ab);
								 
								}
							}
							
							else{
								
								for( i=i-1;i<w; i=i+K){ 
									
								//	System.out.println("FOR MONTH HAVING OTHER NO OF DAYS  "+index++);
									
									 TranBean ab=new TranBean();
									ab.setEMPNO(tbean.getEMPNO());
									ab.setSite_id(Integer.toString(site_id));
									ab.setDate(atDate);
									//System.out.println("the attd val is "+request.getParameter(Integer.toString(i)).toUpperCase());
									 ab.setVal(request.getParameter(Integer.toString(i)).toUpperCase());
									Emp_bean.add(ab);
							
								}
							
						}
					
					}
					
		
					Emp_al.add(Emp_bean);
				}
				
				
						boolean flag=EAH.insertEmpAttendance(Emp_al,eno,st,roleId);
						String status="";
					
						status="saved";
					
						status=EAH.setAttendanceStatus(date,site_id,status,eno);
					
						if(flag)
						{
							
							response.sendRedirect("EmpPresentSeat.jsp?date="+date+"&site_id="+site_id+"&flag=1");
						}
						else
						{
							request.getRequestDispatcher("EmpPresentSeat.jsp?date="+date+"&flag=2").include(request, response);
						}
		
			}
			
			
		}//END OF ACTION INSERT
		
		
		if(action.equalsIgnoreCase("insertSingle"))
		{
			String st=request.getParameter("st")==""?"firstTimeSaved":request.getParameter("st");
	
			ArrayList  Emp_al=new ArrayList();
			ArrayList<Attend_bean> att = new ArrayList<Attend_bean>();
			
			att=(ArrayList<Attend_bean>)session.getAttribute("emplist");
			String singledate = request.getParameter("date");
		
			if(st.equalsIgnoreCase("firstTimeSaved") || roleId.equalsIgnoreCase("1"))
			{
				
				
				System.out.println("On first save");
				
				for (Attend_bean abean : att) 
				{	
					
					
					
					String	date1=ReportDAO.BOM(singledate);
					ArrayList<Attend_bean> Emp_bean=new ArrayList<Attend_bean>();
					String vals[] = request.getParameterValues("_"+abean.getEmpno());
					System.out.println(abean.getEmpno());
					for(int i=0;i<vals.length;i++)
					{
						
						Attend_bean ab=new Attend_bean();
						ab.setEmpno(abean.getEmpno());
					
						
						ab.setSite_id((abean.getSite_id()));
					
						ab.setDate(date1);
						ab.setVal(vals[i].toUpperCase());
					
						Emp_bean.add(ab);
						
						SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
						Calendar c = Calendar.getInstance();    
						c.setTime(format.parse(date1));
						c.add(Calendar.DATE, 1);
						date1=format.format(c.getTime());
						
					
					}
					
					Emp_al.add(Emp_bean);
					
             
				}	
				boolean flag=EAH.insertSingleEmpAttendance(Emp_al,eno,st,roleId);
				String status="";
			
				status="saved";
			
				status=EAH.setAttendanceStatus(singledate,site_id,status,eno);
				if(flag)
				{
					if(roleId.equals("1")){
						//response.sendRedirect("EmpPresentSeat.jsp?date="+date+"&site_id="+site_id+"&flag=1");
						response.sendRedirect("SelectEmpAtt.jsp?flag=1");
				}
					else{
					//response.sendRedirect("EmpPresentSeat.jsp?date="+date+"&site_id="+site_id+"&flag=6");
						response.sendRedirect("SelectEmpAtt.jsp?flag=2");
					}
				}
				else
				{
					request.getRequestDispatcher("SelectEmpAtt.jsp?flag=2").include(request, response);
				}
			}
			
			
			
			
		}
		
		if(action.equalsIgnoreCase("left"))
		{
			
		
			ArrayList  Emp_al=new ArrayList();

		
		
			ArrayList Emp_left =new ArrayList();
			
			String day=request.getParameter("adddate");    //here that date is come from selected left date on emppresentseat.jsp after click on left 
			
		
					String	date2=ReportDAO.BOM(day);		//now we have to change this ..bcz previously mark the left attendance 
																						// as left from BOM.
				
					ArrayList<Attend_bean> Atn_bean=new ArrayList<Attend_bean>();
					String checkbox[]=request.getParameterValues("list");
					
				for(int i=0;i<checkbox.length; i++){
			Attend_bean AB=new Attend_bean();
		 
		 AB.setEmpno(checkbox[i]);
			AB.setSite_id(Integer.toString(site_id));
		   AB.setDate(date2);
	
			Atn_bean.add(AB);
	
		 
		}
				Emp_left.add(Atn_bean);
				
			
			String st=request.getParameter("st")==null?"saved":request.getParameter("st");
			
			
			
			//response.sendRedirect("attendanceMain.jsp?status="+st);
			
	
			boolean flag=EAH.leftEmpAttendance(Emp_left,eno);
		
			String status="left";
			
					status=EAH.setLeftStatus(Emp_left,date,status,eno);
			if(flag)
			{
				
				response.sendRedirect("attendanceMain.jsp?status="+st+"&flag=leftrequestsaved");
			}
			else
			{
				request.getRequestDispatcher("EmpPresentSeat.jsp?date="+date+"&flag=2").include(request, response);
			}
			
		}
		
		
		
		//single employee approve
		
		if(action.equalsIgnoreCase("SingleApprove"))
		{
			
		
			ArrayList  Emp_al=new ArrayList();

		
		
			ArrayList Emp_apr =new ArrayList();
			
			String day=request.getParameter("adddate");
			
		
					String	date2=ReportDAO.BOM(day);
				
					ArrayList<Attend_bean> Atn_bean=new ArrayList<Attend_bean>();
					String checkbox[]=request.getParameterValues("list");
					int cnt=0;
					StringBuffer singlapprv_emp= new StringBuffer();
				for(int i=0;i<checkbox.length; i++){
					
					boolean fg = EAH.checksingleapprove(checkbox[i], date2);
					if(fg==true)
					{
						cnt++;
						if(i==0 ){
							singlapprv_emp.append(checkbox[i]);
						}else {
							singlapprv_emp.append(",");
							singlapprv_emp.append(checkbox[i]);
						}
							
					}
					else{
						Attend_bean AB=new Attend_bean();
					 
						AB.setEmpno(checkbox[i]);
						AB.setSite_id(Integer.toString(site_id));
						AB.setDate(date2);
				
						Atn_bean.add(AB);
					}
		 
		}
				Emp_apr.add(Atn_bean);
				
			
			String st=request.getParameter("st")==null?"saved":request.getParameter("st");
			
			
			
			//response.sendRedirect("attendanceMain.jsp?status="+st);
			
	
			boolean flag=EAH.singleEmpAttendance(Emp_apr,eno);
		
			String status="SingleApprove";
			
					status=EAH.setSingleApproveStatus(Emp_apr,date,status,eno);
			if(flag)
			{
				//it will work if uncment it 
				/*if(cnt>0)
					request.getRequestDispatcher("EmpPresentSeat.jsp?date="+date+"&flag=7&approved_emp="+singlapprv_emp).include(request, response);
				else*/
				response.sendRedirect("attendanceMain.jsp?status="+st);
			}
			else
			{
				request.getRequestDispatcher("EmpPresentSeat.jsp?date="+date+"&flag=2").include(request, response);
			}
			
		}
		
		
		

if(action.equalsIgnoreCase("transfer"))
		{
			
			
			

		
		
			ArrayList Emp_tran =new ArrayList();
			
			String day=request.getParameter("adddate");
			
		
					String	date2=ReportDAO.BOM(day);
				
					ArrayList<Attend_bean> Atn_bean=new ArrayList<Attend_bean>();
					String checkbox[]=request.getParameterValues("list");
					
					
				for(int i=0;i<checkbox.length; i++){
			Attend_bean AB=new Attend_bean();
		 
		 AB.setEmpno(checkbox[i]);
			AB.setSite_id(Integer.toString(site_id));
		   AB.setDate(date2);
	
			Atn_bean.add(AB);
	
		 
		}
				Emp_tran.add(Atn_bean);
				/*System.out.println("ATN_BEAN"+Atn_bean);
				System.out.println("EMP_LEFT"+Emp_left);
				
*/			
			
		
			
			String st=request.getParameter("st")==null?"saved":request.getParameter("st");
			
			
			
			//response.sendRedirect("attendanceMain.jsp?status="+st);
			
			
			boolean flag=EAH.transferEmpAttendance(Emp_tran,eno);
			//String status="saved";
			
		String status="transfer";
			
			status=EAH.setTransferStatus(Emp_tran,date,status,eno);
				
			if(flag)
			{
				
				response.sendRedirect("attendanceMain.jsp?status="+st+"&flag=transferrequestsaved");
			}
			else
			{
				request.getRequestDispatcher("EmpPresentSeat.jsp?date="+date+"&flag=2").include(request, response);
			}
			
		}
     

if(action.equalsIgnoreCase("assignSite"))
{
	System.out.println("inside assignSite");
	ArrayList Emp_tran =new ArrayList();
	
	String emp=request.getParameter("EMPNO")==null?"":request.getParameter("EMPNO");
	String[] employ = emp.split(":");
    int EmpNo = Integer.parseInt(employ[2].trim());
	

	
		
			ArrayList<TranBean> tranList=new ArrayList<TranBean>();
			String checkbox[]=request.getParameterValues("checkList");
			
			
		for(int i=0;i<checkbox.length; i++){
			TranBean tranBean=new TranBean();
 
			tranBean.setSite_id(checkbox[i]);
			
			tranBean.setEMPNO(EmpNo);

			tranList.add(tranBean);

 
}
		Emp_tran.add(tranList);
	boolean flag=EAH.assignSite(Emp_tran, eno);
		
	if(flag)
		{
		ArrayList<TranBean> Emp_bean = new ArrayList<TranBean>();
		Emp_bean=EAH.getAssignedSitesList(EmpNo);
	
		request.setAttribute("ProjectList", Emp_bean);
		
		 request.getRequestDispatcher("AssignSite.jsp?action=display&empno="+EmpNo+"&flag=1").forward(request,response);
			//response.sendRedirect("AssignSite.jsp?action=display&empno="+EmpNo+"&flag1=1");
		}
		else
		{
			request.getRequestDispatcher("AssignSite.jsp?&flag=4").forward(request, response);
		}
		
	
	
	
	}


if(action.equalsIgnoreCase("getWeek"))
{
	System.out.println("inside getweek");
	CompBean bn =new CompBean();
	boolean result = false;
	String prj=request.getParameter("pp")==null?"":request.getParameter("pp");
	
	String[] p= prj.split(":");
	String prjCode = p[3];
	
	bn.setCname(prjCode);
	
	String[] offday = request.getParameterValues("offday");
	String days = "";
	if(offday != null){
		for( String day: offday){
			days = days+" "+day;
		 }
	}
	bn.setWoffday(days);
	String[] alterSat = request.getParameterValues("altsatday");
	String week = "";
	if(alterSat != null){
		for( String day: alterSat){
			week = week+" "+day;
		 }
	}
	bn.setAltsat(week);
	result = EAH.weekoff(bn);
	if(result)
		response.sendRedirect("WeekOffAssign.jsp?flag=1");
		else
			response.sendRedirect("WeekOffAssign.jsp?flag=4");
	
    System.out.println("the code"+prj);
	
	}

if(action.equalsIgnoreCase("checkpendingrequest"))
	{
	String flag="";
		String fordate=request.getParameter("date");
		String siteid=request.getParameter("siteid");
		System.out.println("in checkpendingrequest..."+fordate);
		System.out.println("in checkpending...."+siteid);
		flag=EAH.checkpending(fordate,siteid);
		System.out.println("checkpending flag izs....."+flag);
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		out.write(flag);
   }

if(action.equalsIgnoreCase("checkattendance"))
{
	//System.out.println("checkblankattendance .............................................");
	String empno=request.getParameter("empno");
	String sheetdate=request.getParameter("sheetendate");
	String leftdate=request.getParameter("adddate");
	//System.out.println("in checkblankattendance...empno		"+empno);
	//System.out.println("in checkblankattendance....sheetdate		"+sheetdate);
	//System.out.println("in checkblankattendance....leftdate		"+leftdate);
	String flag=EAH.checkblankattendance(empno,sheetdate,leftdate);
	System.out.println("checkblankattendance flag izs....."+flag);
	PrintWriter out=response.getWriter();
	response.setContentType("text/html");
	out.write(flag);
}


if(action.equalsIgnoreCase("empApproved"))
{
	
String date1=request.getParameter("date1");
String status=request.getParameter("status");
System.out.println("first status...."+status);
String checkstatus=request.getParameter("status");
int empNo=Integer.parseInt(request.getParameter("empNo"));
String act=request.getParameter("act")==null?"ADD":request.getParameter("act");


try {
	
	if(status.equals("transfer")){
	status=EAH.setAttendanceTransferApproved(date1,empNo,status,eno);
	}
	
	if(status.equals("left")){
		status=EAH.setAttendanceLeftApproved(date1,empNo,status,eno);
		}
	
	if(status.equals("SingleApprove")){
		status=EAH.setAttendanceSingleEmpApproved(date1,empNo,status,eno);
		}
		
	
	if(status.equalsIgnoreCase("approved"))
	{
		if(checkstatus.equalsIgnoreCase("left"))
		{
			//old string url
			//response.sendRedirect("RelievingNew.jsp?EMPNO="+empNo+"&flag=1&Leftdate="+date1+"");
			
			/*EmployeeServlet?action=Relinfo&act="+act;*/
			
			System.out.println("in attendance...status needs .aproved..and it is.."+status);
			System.out.println("in attendance checkstatus..."+checkstatus);
			 
	RelieveInfoHandler relhr =new RelieveInfoHandler();
	RelieveInfoBean relbean =new RelieveInfoBean();
	//String EMPNO= (String) session.getAttribute("empno");
	String uid = session.getAttribute("UID").toString();
	System.out.println("uid::  "+uid);
	String EMPNO= request.getParameter("empnoo")==null?"":request.getParameter("empnoo");
	System.out.println("session setted"+EMPNO);
	relbean.setEMPNO(Integer.parseInt(EMPNO));
	relbean.setRESGN_DATE(request.getParameter("rDate")==null?"":request.getParameter("rDate"));
	relbean.setRESGN_ACCTD_DATE(request.getParameter("raDate")==null?"":request.getParameter("raDate"));
	relbean.setREASON(request.getParameter("aReason")==null?"":request.getParameter("aReason"));
	relbean.setNTC_PERIOD(request.getParameter("rPeriod")==null?"":request.getParameter("rPeriod"));
	relbean.setTERMINATE(request.getParameter("term")==null?"":request.getParameter("term"));
	relbean.setDEATH(request.getParameter("death")==null?"":request.getParameter("death"));
	relbean.setLEFT_DATE(request.getParameter("lDate")==null?"":request.getParameter("lDate"));
	relbean.setLEFT_BY(uid);

	String leftdate=request.getParameter("lDate")==null?"":request.getParameter("lDate");
	System.out.println("in attendance lftdate...."+leftdate);
	
	 String check="2";
	
	
	if(!leftdate.equalsIgnoreCase("")){
		System.out.println("we are in leftdate");
		
		
		
		
		
		
		String Empstatus="";
		Statement stransfer=null;
		ResultSet rstransfer=null;
		Connection con;
		con=ConnectionManager.getConnection();
            try {
				stransfer=con.createStatement();


				rstransfer=stransfer.executeQuery("select distinct appr_status from ATTENDANCE_STATUS where EMPNO="+EMPNO+" and tranevent='left' and att_month between '"+ReportDAO.BOM(leftdate)+"' and '"+ReportDAO.EOM(leftdate)+"' ");
				 System.out.println("select distinct appr_status from ATTENDANCE_STATUS where EMPNO="+EMPNO+" and tranevent='left' and att_month between '"+ReportDAO.BOM(leftdate)+"' and '"+ReportDAO.EOM(leftdate)+"' ");

				 
				 while(rstransfer.next()){
			
				Empstatus=rstransfer.getString(1);
				if(Empstatus.equalsIgnoreCase("approved")){
					break;
				}
				System.out.println("in attendance empstatus...."+Empstatus);
				 	}
            	} catch (SQLException e1) {
				// TODO Auto-generated catch block
            		e1.printStackTrace();
            	}
		
		
            
		if(Empstatus.equalsIgnoreCase("approved")){
		
	String Empleft="";
	Statement lstransfer=null;
	ResultSet lrtransfer=null;
	Connection conn;
	conn=ConnectionManager.getConnection();
        try {
        	lstransfer=conn.createStatement();


        	lrtransfer=lstransfer.executeQuery("select max(trndt) as empleft from paytran_stage where empno="+EMPNO+" ");
			 System.out.println(" select distinct max(trndt) from paytran_stage where empno="+EMPNO+"");

			 if(lrtransfer.next())
			 {
			
				 Empleft=lrtransfer.getString("empleft")==null||lrtransfer.getString("empleft").equalsIgnoreCase("null")?ReportDAO.getSysDate():lrtransfer.getString("empleft");
				 System.out.println("111111 "+Empleft);
				 
			}
			 if(Empleft.equalsIgnoreCase("null"))
			 {
				Empleft=ReportDAO.getSysDate();
				 System.out.println("111111sysdate "+Empleft);
			}
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 //Date date=sdf.parse(Empleft);
			  
			 Date ldate= new Date(leftdate);
			 
		
			  
			  
		
			 //String act = request.getParameter("act");
				System.out.println("actttt............"+act);
				if(act.equalsIgnoreCase("ADD")){
				System.out.println("in insert into Relinfo::  "+relbean.getLEFT_BY());
				relhr.insertreliev(relbean);
				check="1";
				}
				else {
					relhr.updateRelinfo(relbean);
					check="1";
				}
				try
				{
					String relieveinfo=request.getParameter("relieveinfo");
					
						ArrayList<RelieveInfoBean> Emprellist = new ArrayList<RelieveInfoBean>();
						session.setAttribute("emprellist",Emprellist);
						response.sendRedirect("RelievingNew.jsp?EMPNO="+EMPNO+"&check="+check);
					
				}
				catch (Exception e) 
				{
					check="2";
					request.getRequestDispatcher("RelievingNew.jsp?EMPNO="+EMPNO+"&check="+check).forward(request, response);
				}
			
			 
			
			 
        	} catch (SQLException e1) {
			// TODO Auto-generated catch block
        		e1.printStackTrace();
        	} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
		}
		
		else{
			 check="4";
				response.sendRedirect("RelievingNew.jsp?EMPNO="+EMPNO+"&check="+check);
		}
	}//closing of if
	
	
	if(leftdate.equalsIgnoreCase("")){
		
		System.out.println("we are in leftdate 2");

		// String act = request.getParameter("act");
			
			if(act.equalsIgnoreCase("ADD")){
			
			relhr.insertreliev(relbean);
			check="1";
			}
			else {
				relhr.updateRelinfo(relbean);
				check="1";
			}
			try
			{
				String relieveinfo=request.getParameter("relieveinfo");
				
					ArrayList<RelieveInfoBean> Emprellist = new ArrayList<RelieveInfoBean>();
					session.setAttribute("emprellist",Emprellist);
					response.sendRedirect("RelievingNew.jsp?EMPNO="+EMPNO+"&check="+check);
				
			}
			catch (Exception e) 
			{
				check="2";
				request.getRequestDispatcher("RelievingNew.jsp?EMPNO="+EMPNO+"&check="+check).forward(request, response);
			}
		 
		
	}
	


			
				
		}
		else{
		response.sendRedirect("attendanceMain.jsp?date="+date1);
		}
	}
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			request.getRequestDispatcher("EmpPresentSeat.jsp?flag=3").include(request, response);
		}
		
		
	
	}
	
	

}
