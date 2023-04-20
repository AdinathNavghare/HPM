 package payroll.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import payroll.Core.ReportDAO;
import payroll.DAO.CodeMasterHandler;
import payroll.DAO.ConnectionManager;
import payroll.DAO.EmpOffHandler;
import payroll.DAO.TranHandler;
import payroll.Model.TranBean;
import payroll.Model.TransactionBean;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * Servlet implementation class TransctionServlet
 */
@WebServlet("/TransactionServlet")
public class TransactionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransactionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
   
		
		HttpSession session= request.getSession();	
		TransactionBean trbn = new TransactionBean();
		TranBean tb = new TranBean();
		EmpOffHandler emp = new EmpOffHandler();
	    TranHandler trn = new TranHandler();
	    ArrayList<TranBean> trlist = new ArrayList<TranBean>();
	    String EMPNO = request.getParameter("no");
	   
		trbn=emp.getInfoEmpTran(EMPNO);
	    trlist=trn.getTranInfo(EMPNO,"tran");//tran :-fire query to tran table
	    
	    System.out.println("trbn set"+trbn.getEmpno());
	    request.setAttribute("empno", EMPNO);
	    request.setAttribute("trbn",trbn); 
	    session.setAttribute("trlist",trlist);
		request.getRequestDispatcher("tranMaintainences.jsp?action=getdata").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session= request.getSession();	
		
		String action = request.getParameter("action")==null?"":request.getParameter("action");
		TransactionBean trbn = new TransactionBean();
		TranBean tb = new TranBean();
		EmpOffHandler emp = new EmpOffHandler();
	    TranHandler trn = new TranHandler();
		if(action.equalsIgnoreCase("update"))//TRNDT EMPNO SRNO TRNCD 
		{
			int empno=Integer.parseInt(request.getParameter("empno"));
			int trncd =Integer.parseInt(request.getParameter("trncd"));
			int srno =Integer.parseInt(request.getParameter("srno"));
			String trdate = request.getParameter("trndate");
			System.out.println("tr date is "+trdate);
			tb.setADJ_AMT(Float.parseFloat(request.getParameter("adjamt")==null?"0":request.getParameter("adjamt")));
			//tb.setUPDDT(request.getParameter(""));
			tb.setCF_SW(request.getParameter("cryfwd"));
			tb.setINP_AMT(Float.parseFloat(request.getParameter("inpamnt")==null?"0":request.getParameter("inpamnt")));
			tb.setNET_AMT(Float.parseFloat(request.getParameter("netamnt")==null?"0":request.getParameter("netamnt")));
			//tb.setCAL_AMT(request.getParameter(""));
			
		    tb.setEMPNO(empno);
		    tb.setSRNO(srno);
		    tb.setTRNCD(trncd);
		    tb.setTRNDT(trdate);
			boolean flag = trn.UpdateTransaction(tb);
			if(flag==true)
			{
				System.out.println("Record Modified " + request.getParameter("select.trncd"));
				response.sendRedirect("updateTransaction.jsp?action=close&key="+empno+":"+trncd+":"+srno+":"+trdate);
			}
			else
			{
				System.out.println("Error in modifying Record");
				response.sendRedirect("updateTransaction.jsp?action=keep&key="+empno+":"+trncd+":"+srno+":"+trdate);
			}
			
			
		}
		else if(action.equalsIgnoreCase("addnewTran"))
		{

			System.out.println("now in addnewtran");
			String flag = null;
			TranHandler tranhandler=new TranHandler();
			TranBean tranobj=new TranBean();
			tranobj.setTRNDT(request.getParameter("trandate"));
			tranobj.setTRNCD(Integer.parseInt(request.getParameter("trancd"))==0?Integer.parseInt(request.getParameter("trancd1")):Integer.parseInt(request.getParameter("trancd")));
            String EMPNO= request.getParameter("EMPNO");
            System.out.println("empno is"+EMPNO);
            StringTokenizer st1 = new StringTokenizer(EMPNO,":");
 		    while(st1.hasMoreTokens())
 		    {
 		    	EMPNO=st1.nextToken();
 		    }
			
			tranobj.setEMPNO(Integer.parseInt(EMPNO));
			//tranobj.setINP_AMT(Integer.parseInt(request.getParameter("inpamnt")));
			tranobj.setINP_AMT(Float.parseFloat(request.getParameter("inpamnt")));
			tranobj.setADJ_AMT(Integer.parseInt("0"));
			tranobj.setNET_AMT(Integer.parseInt("0"));
			tranobj.setARR_AMT(Integer.parseInt("0"));
			tranobj.setCF_SW(request.getParameter("cryfwd"));
			tranobj.setCAL_AMT(Integer.parseInt("0"));
		
			String uid=session.getAttribute("UID").toString();
			tranobj.setUSRCODE(uid);
			tranobj.setUPDDT(request.getParameter("trandate"));
			tranobj.setSTATUS("A");
			flag = tranhandler.addTransaction(tranobj);
			if(flag.equals("true"))
			{
				// request.getRequestDispatcher("addNewTransaction.jsp?action=close").forward(request, response);
			    response.sendRedirect("addNewTransaction.jsp?action=close"); 
				System.out.println("query executed successfully");
			}
			else if (flag.equals("present"))
			{
				response.sendRedirect("addNewTransaction.jsp?action=present");
				System.out.println("Record Alredy Present");	
			}
			else if(flag.equals("false"))
			{
				//response.sendRedirect("addNewTransaction.jsp?action=keep");
				System.out.println("error in record inserting");	
			}
			else 
			{
				//response.sendRedirect("addNewTransaction.jsp?action=keep");
				System.out.println("error in record inserting");	
			}
			
			
		
		}
		else if(action.equalsIgnoreCase("trnlist"))
		{
	
			String EMPNO;
			String list=request.getParameter("list")==null?"":request.getParameter("list");
			if(list.equalsIgnoreCase(""))
			{
				String[] employ = request.getParameter("EMPNO").split(":");
			    EMPNO = employ[2].trim();
			}	
			else if(list.equalsIgnoreCase("negative"))
			{
				EMPNO=request.getParameter("EMPNO");
			}
			else 
			{
				EMPNO=request.getParameter("EMPNO");
				String[] employ= EMPNO.split(":");
			 	EMPNO=employ[2];
			}
			//old before if else
			//EMPNO=request.getParameter("EMPNO");
			//String[] employ= EMPNO.split(":");
		 	//EMPNO=employ[2];
			    
			
			ArrayList<TranBean> trlist = new ArrayList<TranBean>();
			 trbn=emp.getInfoEmpTran(EMPNO);
		    trlist=trn.getTranInfo(EMPNO,"tran");// tran :-fire query to tran table
		    
		    
		    request.setAttribute("trbn",trbn); 
		    request.setAttribute("empno1",EMPNO); 
		 
		    session.setAttribute("trlist",trlist);
		    request.getRequestDispatcher("tranMaintainences.jsp?action=getdata").forward(request, response);
		    
		   // response.sendRedirect("tranMaintainences.jsp?action=getdata");
		       
		}
		//additional method for negative salary disp
		/*else if(action.equalsIgnoreCase("nlist"))
		{
	
			String EMPNO;
			EMPNO=request.getParameter("EMPNO");
			
			ArrayList<TranBean> trlist = new ArrayList<TranBean>();
			 trbn=emp.getInfoEmpTran(EMPNO);
		    trlist=trn.getTranInfo(EMPNO,"tran");// tran :-fire query to tran table
		    
		    
		    request.setAttribute("trbn",trbn); 
		    request.setAttribute("empno1",EMPNO); 
		 
		    session.setAttribute("trlist",trlist);
		    request.getRequestDispatcher("tranMaintainences.jsp?action=getdata").forward(request, response);
		    
		   // response.sendRedirect("tranMaintainences.jsp?action=getdata");
		       
		}	*/	
		if(action.equalsIgnoreCase("addtran"))
		{
			
			System.out.println("now in addnewtran");
			TranHandler tranhandler=new TranHandler();
			TranBean tranobj=new TranBean();
			
			
			  DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		       //get current date time with Date()
		       Date date = new Date();
		       String dt= dateFormat.format(date);
			  System.out.println("date is"+dt);
			//***************************
			
			//boolean flag=false;
			int flag=0;
			
            String EMPNO= request.getParameter("EMPNO");
            System.out.println("empno is"+EMPNO);
            StringTokenizer st1 = new StringTokenizer(EMPNO,":");
 		    while(st1.hasMoreTokens())
 		    {
 		    	EMPNO=st1.nextToken();
 		    	System.out.println("empno is"+EMPNO);
 		    }
 		   
			
 		    
 		    
 		    tranobj.setTRNDT(ReportDAO.BOM(dt));
 		    String trncd=request.getParameter("trancd");
 		    System.out.println("transaction code"+trncd);
 		   System.out.println("date BOM"+ReportDAO.BOM(dt));
 		    tranobj.setTRNCD(Integer.parseInt(request.getParameter("trancd")));
			tranobj.setEMPNO(Integer.parseInt(EMPNO));
			tranobj.setINP_AMT(Integer.parseInt(request.getParameter("amount")));
			tranobj.setADJ_AMT(0);
			tranobj.setNET_AMT(0);
			tranobj.setARR_AMT(0);
			tranobj.setCF_SW("*");
			tranobj.setCAL_AMT(0);
		
			String uid=session.getAttribute("UID").toString();
			tranobj.setUSRCODE(uid);
			tranobj.setUPDDT(ReportDAO.BOM(dt));
			tranobj.setSTATUS("A");
			flag = tranhandler.addNewTransaction(tranobj);
		   if(flag==1)
		   {
			  System.out.println("Record inserted successfully");
			  response.sendRedirect("empTranDetails.jsp?flag="+flag);
			   
		   }
		   else
		   {
			      System.out.println("Error in record inserting");
				  response.sendRedirect("empTranDetails.jsp?flag="+flag);
		   }
			//**************************
			
			
		}
		
		if(action.equalsIgnoreCase("postTran"))
		{
			int empno = Integer.parseInt(request.getParameter("empno").toString());
			TranHandler TH = new TranHandler();
			boolean flag = TH.addToYTDTran(empno);
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write(String.valueOf(flag));
		}
		if(action.equalsIgnoreCase("finalize"))
		{
			//String user = session.getAttribute("name").toString();
			int empno=Integer.parseInt(session.getAttribute("EMPNO").toString());
			String date = request.getParameter("sancDate");
			String dt ="01-"+request.getParameter("sancDate");
			String emplist = request.getParameter("empList");
			System.out.println("emplistssss "+emplist);
			TranHandler TH = new TranHandler();
		
			
		if(emplist.contains(","))
		{
			boolean check=false;
			String status="";
			
			String sql1=" select appr_status from ATTENDANCE_STATUS where site_id in " +
					"  ( SELECT distinct t.prj_srno FROM EMPMAST emp,EMPTRAN t WHERE t.EMPNO = emp.EMPNO " +
					"  AND emp.EMPNO in ("+emplist+") AND t.SRNO = (SELECT MAX(E2.SRNO) FROM EMPTRAN E2  " +
					"  WHERE E2.EMPNO = emp.EMPNO AND E2.EFFDATE <= '"+ReportDAO.EOM(dt)+"') ) " +
					"  and att_month between '"+ReportDAO.BOM(dt)+"' and '"+ReportDAO.EOM(dt)+"' and appr_status!='approved'   ";
		
			Connection con = ConnectionManager.getConnection();
			Statement st1;
			try {
				st1 = con.createStatement();
		
			System.out.println(sql1);
			ResultSet appr_status = st1.executeQuery(sql1);
			
			while(appr_status.next())
			{
				status= appr_status.getString(1)==null?"":appr_status.getString(1);
			}
			
			//System.out.println("status foe appppakpsovcsdfiudsvnfdv:- "+status);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(status.equalsIgnoreCase("") || status.equalsIgnoreCase(" ")  )
			{
				 check=true;
			}
			
			if(check){
					boolean flag = TH.finalizeTran(emplist,date,empno);
					
					if(flag){
						response.sendRedirect("FinalizeTran.jsp?flag=1");
					}
					else
					{	
						response.sendRedirect("FinalizeTran.jsp?flag=2");
					}
			
			}
			else{
				response.sendRedirect("FinalizeTran.jsp?flag=3");
			}
		}	
		
		else
		{ 
			boolean check=false;
			String status="";
			String sitestatus="";
			String empstatus="";
			
			
			
			String Sitesql1=" select appr_status from ATTENDANCE_STATUS where site_id in " +
					"  ( SELECT distinct t.prj_srno FROM EMPMAST emp,EMPTRAN t WHERE t.EMPNO = emp.EMPNO " +
					"  AND emp.EMPNO in ("+emplist+") AND t.SRNO = (SELECT MAX(E2.SRNO) FROM EMPTRAN E2  " +
					"  WHERE E2.EMPNO = emp.EMPNO AND E2.EFFDATE <= '"+ReportDAO.EOM(dt)+"') ) " +
					"  and att_month between '"+ReportDAO.BOM(dt)+"' and '"+ReportDAO.EOM(dt)+"'   ";
		
			Connection con = ConnectionManager.getConnection();
			Statement sitest1;
			try {
				sitest1 = con.createStatement();
		
			System.out.println("site query "+Sitesql1);
			ResultSet siteappr_status = sitest1.executeQuery(Sitesql1);
			
			while(siteappr_status.next())
			{
				sitestatus= siteappr_status.getString(1)==null?"":siteappr_status.getString(1);
			}
			
			//System.out.println("status foe appppakpsovcsdfiudsvnfdv:- "+status);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("sitestatus "+sitestatus);
			
			String sql1=" select appr_status from ATTENDANCE_STATUS where empno in ("+emplist+") " +
								"  and ( tranevent = 'left' and empno in ("+emplist+") ) or ( TRANEVENT='SingleApprove' " +
								"    and empno in ("+emplist+")  ) and att_month between '"+ReportDAO.BOM(dt)+"' and '"+ReportDAO.EOM(dt)+"'    ";
		
		 con = ConnectionManager.getConnection();
		 System.out.println(sql1);
			
			/*String query1 = "select empno from ATTENDANCE_STATUS where empno in ("+emplist+")  " +
					" and att_month between '"+ReportDAO.BOM(dt)+"' and '"+ReportDAO.EOM(dt)+"'  ";*/
			Statement st1;
			try {
				st1 = con.createStatement();
		
			/*System.out.println(query1);
			
			ResultSet emp_status = st1.executeQuery(query1);
			
			while(emp_status.next())
			{
				empstatus= emp_status.getString(1)==null?"":emp_status.getString(1);
			}
			System.out.println("emp_status"+empstatus);
			System.out.println("statust "+ status);
			
			if(empstatus.equalsIgnoreCase("") || status.equalsIgnoreCase(" "))
			{
				
				System.out.println("inside wmpstatus and stsaus 1");
				 
				check=false;
				
				 
			}
			
			else{
				
				check = true;
			}
			 
			*/
				System.out.println(sql1);
				ResultSet appr_status = st1.executeQuery(sql1);
				while(appr_status.next())
				{
					status= appr_status.getString(1)==null?"":appr_status.getString(1);
				}
				
				
			/* if(sitestatus.equalsIgnoreCase(" ")){
					
					System.out.println("inside wmpstatus and stsaus 2");
					
					check = true;
				}
				else{
					check=  false;
				}*/
				
				if(status.equalsIgnoreCase("approved") ||sitestatus.equalsIgnoreCase("approved"))
				{
					 check=true;
				}
				
				else{
					check= false;
				}
			if(check){
			
			/*System.out.println(sql1);
			ResultSet appr_status = st1.executeQuery(sql1);
			while(appr_status.next())
			{
				status= appr_status.getString(1)==null?"":appr_status.getString(1);
			}*/
			
			//System.out.println("status foe appppakpsovcsdfiudsvnfdv:- "+status);
			
			/*if(status.equalsIgnoreCase("") || status.equalsIgnoreCase(" ")  ||sitestatus.equalsIgnoreCase(" "))
			{
				 check=true;
			}*/
			
			if(check){
					boolean flag = TH.finalizeTran(emplist,date,empno);
					
					if(flag){
						response.sendRedirect("FinalizeTran.jsp?flag=1");
					}
					else
					{	
						response.sendRedirect("FinalizeTran.jsp?flag=2");
					}
			
			}
			
		}
			else{
				response.sendRedirect("FinalizeTran.jsp?flag=3");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}	
		
		
		
		}
		
		if(action.equalsIgnoreCase("edittran"))
		{
			boolean flag=false;
			int empno=Integer.parseInt(request.getParameter("empno"));
			int trncd=Integer.parseInt(request.getParameter("trncd"));
			int amount =Integer.parseInt(request.getParameter("amount"));
			TranBean tb1=new TranBean();
			TranHandler TH = new TranHandler();
			tb1.setEMPNO(empno);
			tb1.setTRNCD(trncd);
			tb1.setINP_AMT(amount);
			flag=TH.updatetranAmount(tb1);
			System.out.println("flag after edit"+flag);
			if(flag)
			{
			  response.sendRedirect("empTranDetails.jsp?flag=3");	
			}
			else
			{
				response.sendRedirect("empTranDetails.jsp?flag=4");
				
			}
		}
		
		else if (action.equalsIgnoreCase("editTranValues")) 
		  {
			System.out.println("inside the editTranValues");
		   int trncd = Integer.parseInt(request.getParameter("trncd"));
		   String date = request.getParameter("date1");
		   
		   String[] values = request.getParameterValues("tranValue");
		   Float vals[] = new Float[values.length];
		   for(int i=0; i<values.length; i++)
		   {
		    vals[i] = Float.parseFloat(values[i].trim());
		   }
		   TranHandler thr = new TranHandler();
		   boolean flag= thr.updateEmpTran(trncd, vals,date);
		   
		   
		   
		   if(flag)
		   {
			  
					   
		    response.sendRedirect("empTranDetails.jsp?action=getdetails&key="+trncd+"&selected="+CodeMasterHandler.getCDesc(trncd)+"&date="+date+"&flag=3");
		   }
		   else
		   {
		    response.sendRedirect("empTranDetails.jsp?flag=4");
		   }
		  }
		
		else if (action.equalsIgnoreCase("editTranValues1")) 
		{
			int trncd = Integer.parseInt(request.getParameter("trncd"));
	
			String[] values = request.getParameterValues("tranValue");
			Float vals[] = new Float[values.length];
			for(int i=0; i<values.length; i++)
			{
				vals[i] = Float.parseFloat(values[i].trim());
			}
			TranHandler thr = new TranHandler();
			//boolean flag = thr.updateEmpTran(trncd, vals);
			@SuppressWarnings("unchecked")
			boolean flag = thr.updateEmpTranNew1(trncd, vals, (ArrayList<TranBean>)session.getAttribute("projEmpNolist"));
			if(flag)
			{
				response.sendRedirect("EmpTranNew.jsp?action=afterEdit&flag=3");
			}
			else
			{
				response.sendRedirect("EmpTranNew.jsp?flag=4");
			}
		}
		else if (action.equalsIgnoreCase("editSiteTranValues")) 
		{
			/*String[] mobded = request.getParameterValues("mobded");
			Float values[] = new Float[mobded.length];
			for(int i=0; i<mobded.length; i++)
			{
				values[i] = Float.parseFloat(mobded[i].trim());
			}*/
			
			String[] lopded = request.getParameterValues("lopded");
			Float vals[] = new Float[lopded.length];
			for(int i=0; i<lopded.length; i++)
			{
				vals[i] = Float.parseFloat(lopded[i].trim());
			}
			
			TranHandler thr = new TranHandler();
			String uid = session.getAttribute("UID").toString();
			String user = session.getAttribute("name").toString();
			String empno = session.getAttribute("EMPNO").toString();
			String updatename = empno+"-"+uid+"-"+user;
			System.out.println("name"+updatename);
			@SuppressWarnings("unchecked")
			//boolean flag = thr.updateSiteEmpTranNew(values, vals, (ArrayList<TranBean>)session.getAttribute("emplist"), updatename);
			boolean flag = thr.updateSiteEmpTranNew(vals, (ArrayList<TranBean>)session.getAttribute("emplist"), updatename);
			if(flag)
			{
				response.sendRedirect("SiteEmpTran.jsp?action=afterEdit&flag=3");
			}
			else
			{
				response.sendRedirect("SiteEmpTran.jsp?flag=4");
			}
		}
		else if (action.equalsIgnoreCase("editMobileTranValues")) 
		{
			int trncd = Integer.parseInt(request.getParameter("trncd"));
				
				String[] values = request.getParameterValues("tranValue");
			   String[] values1 = request.getParameterValues("emnp");
			   String vals1[]= new  String[values1.length];
			   Float vals[] = new Float[values.length];
			   for(int i=0; i<values.length; i++)
			   {
				  
			    vals[i] = Float.parseFloat(values[i].trim());
			    vals1[i]= values1[i].trim();
			   }
			   
			   
			   
			   TranHandler thr = new TranHandler();
			   boolean flag = thr.updateEmpTran_mob(trncd, vals,vals1);
			if(flag)
			{
				response.sendRedirect("MobNo.jsp?action=afterEdit&flag=3");
			}
			else
			{
				response.sendRedirect("MobNo.jsp?flag=4");
			}
		}
		
		else if (action.equalsIgnoreCase("editMedInsuTranValues")) 
		{
			int trncd = Integer.parseInt(request.getParameter("trncd"));
				System.out.println("editMedInsuTranValues....trncd ..."+trncd);
				String[] values = request.getParameterValues("tranValue");
			   String[] values1 = request.getParameterValues("emnp");
			   String vals1[]= new  String[values1.length];
			   Float vals[] = new Float[values.length];
			   for(int i=0; i<values.length; i++)
			   {
				  
			    vals[i] = Float.parseFloat(values[i].trim());
			    vals1[i]= values1[i].trim();
			   }
			   
			   
			   
			   TranHandler thr = new TranHandler();
			   boolean flag = thr.updateEmpTran_MedINSU(trncd, vals,vals1);
			if(flag)
			{
				response.sendRedirect("MedInsurance.jsp?action=afterEdit&flag=3");
			}
			else
			{
				response.sendRedirect("MedInsurance.jsp?flag=4");
			}
		}
		
		
		else if (action.equalsIgnoreCase("release")) 
		{
			   TranHandler thr = new TranHandler();
			   String date = request.getParameter("date");
			   int range =Integer.parseInt( request.getParameter("salrange")==""?"0":request.getParameter("salrange"));
			   int range2 = Integer.parseInt(request.getParameter("tosalrange")==""?"0":request.getParameter("tosalrange"));


			   String proj = request.getParameter("projlist");
			   proj = proj.replaceAll("&", " and ");
			   String[] values = request.getParameterValues("chk");
			   String vals = "";
			   for(int i=0; i<values.length; i++)
			   {
				   if(i==(values.length)-1) {
					   vals += values[i];
				   } else {
					   vals += values[i]+",";
				   }
			   }
			boolean flag=   thr.releaseSalary(vals,date);
			   //response.sendRedirect("SalaryDetails.jsp?action=details&proj="+proj+"&date="+date);
			 if(flag)
				{
					response.sendRedirect("SalaryDetails.jsp?action=details&proj="+proj+"&date="+date+"&rng="+range+"&torng="+range2+"&flag=1");
				}
				else
				{
					response.sendRedirect("SalaryDetails.jsp?action=details&proj="+proj+"&date="+date+"&rng="+range+"&torng="+range2+"&flag=2");
				}
			   
		}
		else if (action.equalsIgnoreCase("updateSal")) 
		{
			ArrayList<TranBean> result = new ArrayList<TranBean>();
			ArrayList<TranBean> updated = new ArrayList<TranBean>();
			TranHandler hdlr = new TranHandler();
			TranBean bean;
			float ded = 0;
			int count =(Integer)session.getAttribute("deduct_counter");
		   String date = request.getParameter("month");
		   String empno = request.getParameter("empno");
		   float totDed = Float.parseFloat(request.getParameter("total_deduct"));
		   float netPay = Float.parseFloat(request.getParameter("net_pay"));
		   //System.out.println(date+" "+empno);
		   result = (ArrayList<TranBean>)session.getAttribute("list");
		   for(TranBean tbn : result) {
			   if(tbn.getTRNCD()==999) {
				   ded = tbn.getCAL_AMT();
			   }
			   /*if(tbn.getTRNCD()>200 && tbn.getTRNCD()<300 && tbn.getTRNCD()!=999){
				   count++;
			   }*/
		   }
		   session.removeAttribute("list");
	
		   for(int i=0;i<count;i++){
			   //System.out.println(request.getParameter("deduct"+i)+""+request.getParameter("dedcode"+i));
			   bean = new TranBean();

			   bean.setINP_AMT(request.getParameter("deduct"+i)==""?0:Float.parseFloat(request.getParameter("deduct"+i)));
			   bean.setCAL_AMT(request.getParameter("deduct"+i)==""?0:Float.parseFloat(request.getParameter("deduct"+i)));
			   bean.setNET_AMT(request.getParameter("deduct"+i)==""?0:Float.parseFloat(request.getParameter("deduct"+i)));
			   bean.setTRNCD(Integer.parseInt(request.getParameter("dedcode"+i)));
			   updated.add(bean);
		   }
		   session.removeAttribute("deduct_counter");
		   /*if(ded==totDed) {
			   response.sendRedirect("salaryChange.jsp?action=nochange&eno="+empno+"&date="+date);
		   } else {*/
			   boolean flag = hdlr.updateSalChange(empno, date, updated, totDed, netPay);
			   if(flag){
				   response.sendRedirect("salaryChange.jsp?action=close&eno="+empno+"&date="+date);
			   }else{
				   response.sendRedirect("salaryChange.jsp?action=keep&eno="+empno+"&date="+date);
			   }
		   /*}*/
		   		   
		}
		else if(action.equalsIgnoreCase("updateloanInstall"))
		{
			String empno = request.getParameter("empno1");
			System.out.println(empno);
			String[] loan_no =(request.getParameterValues("loan_no"));
			System.out.println("length=="+loan_no.length);
			int[] loan_amt = new int[loan_no.length];
			int[] pri_amt = new int[loan_no.length];
			int[] intr_amt = new int[loan_no.length];
			String uid = session.getAttribute("UID").toString();
			int i=0;
			for(String a:loan_no)
			{
			
				loan_amt[i]	=	(int) Math.round(Float.parseFloat(request.getParameter("loan1"+a)));
				pri_amt[i]	=	(int) Math.round(Float.parseFloat(request.getParameter("pri1"+a)));
				intr_amt[i]	=	(int) Math.round(Float.parseFloat(request.getParameter("intr1"+a)));
				System.out.println("loan_no=="+a+" and amt=="+loan_amt[i]);
				i++;
			}
			TranHandler hdlr = new TranHandler();
			boolean flag = hdlr.updateLoanInstallment(empno,loan_no,loan_amt,pri_amt,intr_amt,uid);
			if(flag)
				response.sendRedirect("checkLoan.jsp?flag=1");
			else
				response.sendRedirect("checkLoan.jsp?flag=2");
		}

		
	}

}
