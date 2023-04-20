package payroll.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import payroll.Core.ReportDAO;
import payroll.Core.Utility;
import payroll.DAO.CTCHandler;
import payroll.DAO.EmployeeHandler;
import payroll.DAO.OnAmtHandler;
import payroll.DAO.SlabHandler;
import payroll.DAO.TranHandler;
import payroll.Model.EmployeeBean;
import payroll.Model.OnAmtBean;
import payroll.Model.SlabBean;
import payroll.Model.TranBean;

/**
 * Servlet implementation class CTCServlet
 */
@WebServlet("/CTCServlet")
public class CTCServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CTCServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if(action.equalsIgnoreCase("noctc"))
		{
			try
			{
				StringBuilder result=TranHandler.getNoCTCEmp();
				PrintWriter out = response.getWriter();
				response.setContentType("text/html");
				out.write(result.toString());
			}catch(Exception e){e.printStackTrace();}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		int eno = (Integer)session.getAttribute("EMPNO");
		String[] employ = request.getParameter("EMPNO").split(":"); 
		int emp = Integer.parseInt(employ[2]);
		EmployeeBean empBean=new EmployeeBean();
		EmployeeHandler EmpH=new EmployeeHandler(); 
		CTCHandler CH=new CTCHandler();  
		String eff_date =request.getParameter("date");
		empBean=EmpH.getEmployeeInformation(Integer.toString(emp));
		String max_date=CH.getMAXDateinPaytran(Integer.toString(empBean.getEMPNO()));
		String min_date=CH.getMINDateinPaytran(Integer.toString(empBean.getEMPNO()));
		
		
		Date eff=new Date(eff_date);
		
		Date mx=new Date(max_date);
		Date mn=new Date(min_date);
		Date doj=new Date(empBean.getDOJ());
		
		if(eff.before(mn) || eff.after(mx) || eff.before(doj))
		{
			request.setAttribute("emp",empBean.getEMPNO());
		System.out.println("into ELSEeeeeeeeeee");
		response.sendRedirect("CTC.jsp?action=notsaved");
		//response.sendRedirect("NewCTCServlet?action=checkCTC&EMPNO="+empBean.getFNAME()+" "+empBean.getLNAME()+":"+empBean.getEMPCODE()+":"+empBean.getEMPNO());
		}
		else
		{
		
		
		
		
		
		
		int max = 14;
		String [] prefix = new String[max];	
		prefix[1]="basic";//101
		prefix[2]="da";//104		// Changed da to Medical
		prefix[3]="hra";//103
		prefix[4]="edu";//105
		prefix[5]="insu";//126
		prefix[6]="pfe";//201
		prefix[7]="addLess";//127
		prefix[8]="col";//128
		prefix[9]="conv";//108
		prefix[10]="spAllow";//107
		prefix[11]="bonus";//135
		prefix[12]="lev";//106
		prefix[13]="any";//145
		
		
		int[] codes = {199,101,104,103,105,126,201,127,128,108,107,135,106,145};
		int[] valType = new int[max];
		float[] value = new float[max];
		int[] depend = new int[max];
		float[] amount = new float[max];
		int empno = Integer.parseInt(request.getParameter("EMPNO").split(":")[2]);
		boolean pf = (request.getParameter("pf")==null?false:true);
		boolean pt = (request.getParameter("pt")==null?false:true);
		boolean esic = (request.getParameter("esic")==null?false:true);
		for(int i=1; i<max; i++)
		{
			System.out.println("@@@@@"+(prefix[i]+"ValType"));
			valType[i] = Integer.parseInt(request.getParameter(prefix[i]+"ValType"));
			value[i] = Float.parseFloat(request.getParameter(prefix[i])==""?"0":request.getParameter(prefix[i]));
			depend[i] = Integer.parseInt(request.getParameter(prefix[i]+"On"));
			amount[i] = Float.parseFloat(request.getParameter(prefix[i]+"Val")==""?"0":request.getParameter(prefix[i]+"Val"));
			System.out.println(empno+"\t"+prefix[i]+"\t"+valType[i]+"\t"+value[i]+"\t"+depend[i]+"\t"+amount[i]);
		}
		for(int i=1; i<max; i++)
		{
			valType[i] = Integer.parseInt(request.getParameter(prefix[i]+"ValType"));
			value[i] = Float.parseFloat(request.getParameter(prefix[i])==""?"0":request.getParameter(prefix[i]));
			depend[i] = Integer.parseInt(request.getParameter(prefix[i]+"On"));
			amount[i] = Float.parseFloat(request.getParameter(prefix[i]+"Val")==""?"0":request.getParameter(prefix[i]+"Val"));
			TranBean tb = new TranBean();
			TranHandler TH = new TranHandler();
			tb.setEMPNO(empno);
			tb.setTRNCD(codes[i]);
			tb.setSRNO(valType[i]);
			tb.setINP_AMT(value[i]);
			tb.setADJ_AMT(depend[i]);
			tb.setNET_AMT(amount[i]);
			tb.setUpdatedby(eno);
			tb.setUpdate(ReportDAO.getSysDate());
			
			if(pf)
				tb.setPf(1);
			else
				tb.setPf(0);
			if(pt)
				tb.setPt(1);
			else
				tb.setPt(0);
			if(esic)
				tb.setEsic(1);
			else
				tb.setEsic(0);
			TH.addCTCDISPLAY(tb);
			
			OnAmtHandler.ctc_change_OnAmt(empno);
			SlabHandler.ctc_change_Slab(empno);
		    TranHandler.ctc_change_PayTran(empno);
			
		}
		
		if(action.equalsIgnoreCase("create"))	// Create Salary Structure
		{
		
			TranHandler TH = new TranHandler();
			OnAmtHandler OAH = new OnAmtHandler();
			SlabHandler SH = new SlabHandler();
			SlabBean SB = null;
			OnAmtBean OAB = null;
			for(int i=1; i<max; i++)
			{
				if(valType[i] ==0 && i!=6 )	// value given as percentage
				{
					int check = SH.checkSlabPresent(empno, codes[i]);
					if(check != -1)
					{
						SH.endSlab(empno, codes[i], check);
					}
				//----- Entry to Slab Table	
					SB = new SlabBean();
					SB.setEMP_CAT(empno);
					SB.setTRNCD(codes[i]);
					SB.setEFFDATE("31-DEC-2099");
					SB.setSRNO(check+1);
					SB.setFRMAMT(0);
					SB.setTOAMT(9999999);
					SB.setPER(value[i]);
					SB.setMINAMT(0);
					SB.setMAXAMT(0);
					SB.setFIXAMT(0);
					SB.setON_AMT_CD(0);	
					SH.addSlab(SB);
				//----- Entry to OnAmt table
					
					OAH.deleteOnAmt(empno, codes[i], -1);
					OAB = new OnAmtBean();
					OAB.setEMP_CAT(empno);
					OAB.setTRNCD(codes[i]);
					OAB.setSRNO(1);
					OAB.setONAMTCD(depend[i]);
					OAB.setAMT_TYPE("C");
					OAH.addOnAmt(OAB);
				}
				else if(valType[i] == 1 && i!=6) // value given as Fixed Value
				{
					int total = Math.round(value[i]);
					if(i==7)
					{
						total = Math.round(amount[i] + amount[6]); //  pf + add less
					}
					
					TranBean TB = new TranBean();
					/*Date dt = new Date();
					String date = new SimpleDateFormat("dd-MMM-yyyy").format(dt);
					*/
					
					String date =request.getParameter("date")==null?ReportDAO.getSysDate():request.getParameter("date");
					TB.setTRNDT(date);
					TB.setEMPNO(empno);
					TB.setTRNCD(codes[i]);
					TB.setSRNO(0);
					TB.setINP_AMT(total);
					TB.setCAL_AMT(0);
					TB.setADJ_AMT(0);
					TB.setARR_AMT(0);
					TB.setNET_AMT(0);
					TB.setCF_SW("*");
					TB.setUSRCODE("System");
					TB.setUPDDT(date);
					TB.setSTATUS("N");//changed
					if(TH.getAvailability(empno, codes[i]))
					{
						TH.updatetranAmount(TB);
					}
					else
					{
						TH.addTransaction(TB);
					}
					OAH.deleteOnAmt(empno, codes[i], -1);
					OAB = new OnAmtBean();
					OAB.setEMP_CAT(empno);
					OAB.setTRNCD(codes[i]);
					OAB.setSRNO(1);
					OAB.setONAMTCD(codes[i]);
					OAB.setAMT_TYPE("C");
					OAH.addOnAmt(OAB);
					
				}	
			}
			
			TranBean TB = new TranBean();
			/*Date dt = new Date();
			String date = new SimpleDateFormat("dd-MMM-yyyy").format(dt);
			*/
			
			String date =request.getParameter("date")==null?ReportDAO.getSysDate():request.getParameter("date");
			TB.setTRNDT(date);
			TB.setEMPNO(empno);
			TB.setTRNCD(199);
			TB.setSRNO(0);
			TB.setINP_AMT(Math.round(Float.parseFloat(request.getParameter("monthly"))));
			TB.setCAL_AMT(0);
			TB.setADJ_AMT(0);
			TB.setARR_AMT(0);
			TB.setNET_AMT(0);
			TB.setCF_SW("*");
			TB.setUSRCODE("System");
			TB.setUPDDT(date);
			TB.setSTATUS("N");//changed
			if(TH.getAvailability(empno, 199))
			{
				TH.updatetranAmount(TB);
			}
			else
			{
				TH.addTransaction(TB);
			}
			
			if(pf)
			{
				OAH.deleteOnAmt(empno, 201, -1);
				
				OAB = new OnAmtBean();
				OAB.setEMP_CAT(empno);
				OAB.setTRNCD(201);
				OAB.setSRNO(1);
				OAB.setONAMTCD(101);
				OAB.setAMT_TYPE("C");
				//OAH.addOnAmt(OAB);
				
				// added for pf as special ear1 also take part with basic
				// on demand of kulkurni sir
				OAB = new OnAmtBean();
				OAB.setEMP_CAT(empno);
				OAB.setTRNCD(201);
				OAB.setSRNO(2);
				OAB.setONAMTCD(130);
				OAB.setAMT_TYPE("C");
				//OAH.addOnAmt(OAB);
			}
			if(pt)
			{
				//OAH.deleteOnAmt(empno, 202, -1);
				
				OAB = new OnAmtBean();
				OAB.setEMP_CAT(empno);
				OAB.setTRNCD(202);
				OAB.setSRNO(1);
				OAB.setONAMTCD(199);
				OAB.setAMT_TYPE("C");
				//OAH.addOnAmt(OAB);
			}
			if(esic)
			{
				OAH.deleteOnAmt(empno, 221, -1);
				
				OAB = new OnAmtBean();
				OAB.setEMP_CAT(empno);
				OAB.setTRNCD(221);  //ESIC code=221
				OAB.setSRNO(1);
				OAB.setONAMTCD(198);  //gross total code=198
				OAB.setAMT_TYPE("C");
				OAH.addOnAmt(OAB);
				
				OAH.deleteOnAmt(empno, 236, -1);
				
				OAB = new OnAmtBean();
				OAB.setEMP_CAT(empno);
				OAB.setTRNCD(236);  //EMPLOYER ESIC code=236
				OAB.setSRNO(1);
				OAB.setONAMTCD(199);  //gross total code=198
				OAB.setAMT_TYPE("C");
				OAH.addOnAmt(OAB);
				
				
				
			}
			response.sendRedirect("CTC.jsp?action=saved");
		}
		
	}
	}
}  