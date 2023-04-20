package payroll.Controller;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import payroll.DAO.EmpOffHandler;
import payroll.DAO.EmployeeHandler;
import payroll.DAO.LeaveEncashmentHandler;
import payroll.Model.EmpOffBean;
import payroll.Model.EmployeeBean;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;


import javax.servlet.ServletContext;

import javax.servlet.ServletOutputStream;


import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.StringTokenizer;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

import payroll.Core.Calculate;
import payroll.Core.ESICChallanDAO;
import payroll.Core.ExcelDAO;
import payroll.Core.Form12A;
import payroll.Core.Form3A;
import payroll.Core.Form6A;
import payroll.Core.PfChallanDAO;
import payroll.Core.ReportDAO;
import payroll.Core.SalaryCertificateDAO;
import payroll.Core.SalaryRegisterDAO;
import payroll.Core.TaxCalculation;
import payroll.Core.TravelReportDAO;
import payroll.Core.UtilityDAO;
import payroll.Core.YearlyPDFReport;

import payroll.DAO.AdvanceHandler;
import payroll.DAO.CodeMasterHandler;
import payroll.DAO.ConnectionManager;
import payroll.DAO.EncashmentHandler;
import payroll.DAO.LookupHandler;
import payroll.DAO.TranHandler;

import payroll.Model.EmpQualBean;
import payroll.Model.EncashmentBean;
import payroll.Model.PFExcelBean;
import payroll.Model.RepoartBean;
import payroll.Model.TripBean;
/**
 * Servlet implementation class EncashServlet
 */
@WebServlet("/EncashServlet")
public class EncashServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
    public EncashServlet() {
        super();
        }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		String action=request.getParameter("action");
		String projcode="";
		int status=0;
		String status1="";
		
		
		
		if(action.equalsIgnoreCase("getreport"))	
		{
			//EmployeeHandler emp=new EmployeeHandler();
			EncashmentHandler enh=new EncashmentHandler();
			ArrayList<EncashmentBean> list= new ArrayList<EncashmentBean>();
			  projcode =(String) session.getAttribute("projcode");
			 System.out.println("prrrooojjeecctt code "+projcode);
			//ArrayList<EmployeeBean> list= new ArrayList<EmployeeBean>();
			String reporttype = request.getParameter("reporttype");
			String date = request.getParameter("date");
			String foryear=(String)session.getAttribute("year");
			System.out.println("in getreport year is..."+foryear);
			String prjname="";
			int flag=-1;
			if(projcode.equalsIgnoreCase("ALL"))
			{
				 projcode ="";
				 prjname="All Sites";
				 flag=0;
			}else{
			
				System.out.println("in getreport...");
				
				 prjname = enh.getpname(projcode);
				System.out.println("pp..."+prjname);
				flag=1;
			}
				
				if(reporttype.equalsIgnoreCase("pdfFile"))
				{
					System.out.println("in pdfFile...");
					String filePath = getServletContext().getRealPath("")+ File.separator + "ENCASH_REPORT.pdf";
					UtilityDAO.encashPDF(date, filePath,prjname,projcode,flag,foryear);
					/*UtilityDAO.PFLISTPDF(date, filePath,prjname);*/
					final int BUFSIZE = 4096;
					File file = new File(filePath);
					int length = 0;
					ServletOutputStream outStream = response.getOutputStream();
					ServletContext context = getServletConfig().getServletContext();
					String mimetype = context.getMimeType(filePath);
					if (mimetype == null) 
					{
						mimetype = "application/octet-stream";
					}
					response.setContentType(mimetype);
					response.setContentLength((int) file.length());
					String fileName = (new File(filePath)).getName();
					response.setHeader("Content-Disposition", "attachment; filename=\""	+ fileName + "\"");
					byte[] byteBuffer = new byte[BUFSIZE];
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					while ((in != null) && ((length = in.read(byteBuffer)) != -1)) 
					{
						outStream.write(byteBuffer, 0, length);
					}
					in.close();
					outStream.close();
					if (file.exists()) 
					{
						file.delete();
					}
				}
				else
				{
				
					String imagepath = getServletContext().getRealPath(
							"/images/logo.jpg");
							String filepath = getServletContext().getRealPath("")
									+ File.separator + "Encash_ExcelReport.xls";
							UtilityDAO.EncashExcel(date, filepath,projcode, imagepath,prjname,flag,foryear);
							try {
								final int BUFSIZE = 4096;
								File file = new File(filepath);
								int length = 0;
								ServletOutputStream outStream = response.getOutputStream();
								ServletContext context = getServletConfig()
										.getServletContext();
								String mimetype = context.getMimeType(filepath);
								if (mimetype == null) {
									mimetype = "application/octet-stream";
								}
								response.setContentType(mimetype);
								response.setContentLength((int) file.length());
								String fileName = (new File(filepath)).getName();
								response.setHeader("Content-Disposition",
										"attachment; filename=\"" + fileName + "\"");
								byte[] byteBuffer = new byte[BUFSIZE];
								DataInputStream in = new DataInputStream(
										new FileInputStream(file));
								while ((in != null)
										&& ((length = in.read(byteBuffer)) != -1)) {
									outStream.write(byteBuffer, 0, length);
								}
								in.close();
								outStream.close();
								if (file.exists()) {
									file.delete();
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
		}			
	}		
			
		
		if(action.equalsIgnoreCase("updateEncash"))	
		{
			System.out.println(" in updateEncash..");
		
			HttpSession session1 = request.getSession();
			ArrayList<EncashmentBean> elist2=null;
			ArrayList<EncashmentBean> editlist=new ArrayList<EncashmentBean>();
			ArrayList<EncashmentBean> editedlist=new ArrayList<EncashmentBean>();
			//int day[][]=new int[5][5];
			int i=0,j=1,k=0,l=0;
			//int noofHOday1[]=new int[5];
			//int m=0,n=0;
			//int noofOsday1[]={0};
			String[] noofHOday =request.getParameterValues("noOfDaysHo");
			String[] noofOsday = request.getParameterValues("noOfDaysOs");
			System.out.println("no of record for  HO edit..."+ noofHOday.length);
			System.out.println("no of record for  OS edit..."+ noofOsday.length);
			
			EncashmentHandler enh=new EncashmentHandler();
			elist2 =(ArrayList<EncashmentBean>)session1.getAttribute("updateEncashlist");
			/*for( m=0;m<elist2.size();m++)
			{
				for(n=0;n<j;n++)
				{
					day[m][n]=Integer.parseInt(noofHOday[m]);
					//System.out.println("inday for os...."+day[m][n]);
				}
				day[m][n]=Integer.parseInt((noofOsday[m]));
				//System.out.println("inday for os...."+day[m][n]);
			}*/
			/*for( i=0;i<noofHOday.length;i++)
			{
				for( j=0;j<=k;j++)
				{
				noofdays[i][j]=Integer.parseInt(noofHOday[i]);
				
				System.out.println("HODAY to edit.."+noofHOday[i]);
				System.out.println("hoday to edit.."+noofdays[i][j]);
				}
				noofdays[i][j]=Integer.parseInt(noofOsday[i]);
				
				System.out.println("OSDAY to edit.."+noofOsday[i]);
				System.out.println("osday to edit.."+noofdays[i][j]);
			}
				*/
			
			for(EncashmentBean ebn:elist2)
			{
				
				EncashmentBean eBean = new EncashmentBean();
				eBean.setYear(ebn.getYear());
				eBean.setEmpno(ebn.getEmpno());
				eBean.setEmpCode(ebn.getEmpCode());
				eBean.setName(ebn.getName().toUpperCase());
				eBean.setOnHo(Integer.parseInt(noofHOday[i]));
				//System.out.println("inday for ho...."+day[i][l]);
				eBean.setLeaveOnHO(ebn.getLeaveOnHO());
				eBean.setOnOsite(Integer.parseInt(noofOsday[i]));
				//System.out.println("inday for os...."+day[i][l+1]);
				eBean.setLeaveOnOS(ebn.getLeaveOnOS());
				eBean.setBasic(ebn.getBasic());
				eBean.setEncashment(ebn.getEncashment());
				eBean.setStatus(ebn.getStatus());
				editlist.add(eBean);
				i++;
				//l=0;
				
			}
			for(i=0;i<noofHOday.length;i++)
			{
				System.out.println("values of ho...."+Integer.parseInt(noofHOday[i]));
				System.out.println("values of os...."+Integer.parseInt(noofOsday[i]));
			
			}
			
			System.out.println("going to encashhandler..");
			String foryear=(String)session.getAttribute("year");
			
			editedlist=enh.editencashment(editlist);
			
			System.out.println("back in updateEncash..");
			for(EncashmentBean ebn:editedlist)
			{
				System.out.println("edit year..."+ebn.getYear());
				System.out.println("edit empno..."+ebn.getEmpno());
				System.out.println("edit empcode..."+ebn.getEmpCode());
				System.out.println("edit HOday..."+ebn.getOnHo());
				System.out.println("edit leaveHO..."+ebn.getLeaveOnHO());
				System.out.println("edit OSday..."+ebn.getOnOsite());
				System.out.println("edit leaveOS..."+ebn.getLeaveOnOS());
				System.out.println("edit basic..."+ebn.getBasic());
				//System.out.println("Empname..."+ebn.getFNAME());
				//String eno="+ebn.getEMPNO()+";
				System.out.println("edit encashment..."+ebn.getEncashment());
				System.out.println("edit status..."+ebn.getStatus());
			}
			//enh.EditEncash(list2,recordsToUpdate);
			boolean flag=enh.editSaveTransaction(editedlist,foryear);
			if(flag==true)
			{
				System.out.println(" SaveTransaction");
				
				response.sendRedirect("encashment.jsp?action=getDetails&Save=save");
			}
			else
			{
				response.sendRedirect("encashment.jsp?action=getDetails&Save=nosave");
			}
				
			
			
		}
		
		
	
		if(action.equalsIgnoreCase("displayEmp"))	
		{
			ArrayList<EncashmentBean> list= new ArrayList<EncashmentBean>();
			//ArrayList<EmployeeBean> list1= new ArrayList<EmployeeBean>();
			System.out.println("in encash servlet");
			EncashmentHandler end=new EncashmentHandler();
			 String siteid=request.getParameter("pp")==null?"":request.getParameter("pp");
			 String year=request.getParameter("yy")==null?"":request.getParameter("yy");
			 int yy=Integer.parseInt(year);
			String y=String.valueOf(yy);
			 System.out.println("yearrr==="+year);
			 System.out.println("year==="+yy);
			 session.setAttribute("year", year);
			 session.setAttribute("year1", y);
			 String allval=request.getParameter("pp");
			 status=end.checkrecord(year);
			 status1=String.valueOf(status);
			 session.setAttribute("status",status1);
			 session.setAttribute("pname", allval);
			 if(status==1)
			 {
				 if(allval.equalsIgnoreCase("all"))
				 {
				list=end.getUpdaterecord(year);
				session.setAttribute("EmpEncashList",list);
				session.setAttribute("pname","ALL");
				session.setAttribute("projcode","ALL");
				 }
				 else{
					 System.out.println("pp...."+siteid);
					 projcode=end.getprojname(siteid);
					 list=end.getUpdaterecord1(projcode,year);
					
					String pname=end.getpname(projcode);
					session.setAttribute("pname",pname);
					session.setAttribute("projcode",projcode);
					session.setAttribute("EmpEncashList",list);
					}
					 
					if(list.isEmpty())
					{
						System.out.println("nothinggggg");
						response.sendRedirect("encashment.jsp?action=NoRecord");
					}
					else
					{
						System.out.println("in else");
						session.setAttribute("EmpEncashList",list);
						//request.getRequestDispatcher("encashment.jsp?action=getDetails").forward(request,response);
						response.sendRedirect("encashment.jsp?action=getDetails&status=1");
					}
			 }
			 else
			 {
				 response.sendRedirect("encashment.jsp?action=getDetails&status=0");
			 }
			
		}
		
		
		if(action.equalsIgnoreCase("SaveTransaction"))	
		{
			
			System.out.println(" in SaveTransaction....entered");
			boolean flag=false;
			ArrayList<EncashmentBean> list3=null;
			ArrayList<EncashmentBean> savelist=null;
			int i=0;
			list3 =(ArrayList<EncashmentBean>)session.getAttribute("list3");
			String statu=(String)session.getAttribute("status");
			String foryear=(String)session.getAttribute("year");
			int st=Integer.parseInt(statu);
			System.out.println("in save....status is.."+st);
			savelist=(ArrayList<EncashmentBean>)session.getAttribute("getcalculatedlist");
			EncashmentHandler enh=new EncashmentHandler();
			if(st==2)
			{
			 flag=enh.SaveTransaction(savelist);
			}
			else
			{
			 flag=enh.SaveTransaction(list3);
			}
			if(flag==true)
			{
				System.out.println(" SaveTransaction");
				/*status=3;
				status1=String.valueOf(status);
				System.out.println("in save newstst.."+status1);
				session.setAttribute("status",status1);*/
				response.sendRedirect("encashment.jsp?action=getDetails&Save=save");
			}
			else
			{
				response.sendRedirect("encashment.jsp?action=getDetails&Save=nosave");
			}
				
		/*for(EncashmentBean ebn:list3)
		{
			System.out.println("EmpCode..."+ebn.getEmpCode());
			System.out.println("EmpNO..."+ebn.getEmpno());
			System.out.println("LeaveHO..."+ebn.getLeaveOnHO());
			System.out.println("LeaveOS..."+ebn.getLeaveOnOS());
			System.out.println("Basic..."+ebn.getBasic());
			System.out.println("Encash..."+ebn.getEncashment());
			i++;
			
			
			
		}
		System.out.println("total record"+i);
		int leaveday[]={0,0};*/
		
		}
		
		if(action.equalsIgnoreCase("getencashment"))
		{
			int val=0;
			ArrayList<EncashmentBean> getlist=null;
			ArrayList<EncashmentBean> getlist1=null;
			EncashmentHandler enh=new EncashmentHandler();
			String forYear=(String)session.getAttribute("year");
			String prj=(String)session.getAttribute("prj");
			System.out.println("prjjjjjjj..."+prj);
			if(prj.equalsIgnoreCase("all"))
			{
				projcode="ALL";
			getlist=enh.getEncashEmpList(projcode);
			System.out.println("in get encashment...new and list size is"+getlist.size());
			session.setAttribute("getnewlist", getlist);
			}
			else
			{
				projcode=enh.getprojname(prj);
				getlist=enh.getEncashEmpList(projcode);
				 val=getlist.size();
				System.out.println("in get encashmentemplist...new and list size is"+getlist.size());
				session.setAttribute("getnewlist", getlist);
			}
			getlist1=enh.calculateEncashment(getlist,forYear);
			System.out.println("in get calculateencashmentlist...new size is....."+getlist1.size());
			System.out.println("get encashmentemplist...new and list size is"+getlist1.size());
			String year=(String)session.getAttribute("year");
			System.out.println("in  year...new"+year);
			
			//list3 =(ArrayList<EncashmentBean>)session.getAttribute("list3");
			for(EncashmentBean ebn:getlist1)
			{
				System.out.println("for new"+forYear);
				System.out.println("new empno..."+ebn.getEmpno());
				System.out.println("new empcode..."+ebn.getEmpCode());
				System.out.println("new HOday..."+ebn.getOnHo());
				System.out.println("new leaveHO..."+ebn.getLeaveOnHO());
				System.out.println("new OSday..."+ebn.getOnOsite());
				System.out.println("new leaveOS..."+ebn.getLeaveOnOS());
				System.out.println("new basic..."+ebn.getBasic());
				//System.out.println("Empname..."+ebn.getFNAME());
				//String eno="+ebn.getEMPNO()+";
				System.out.println("new encashment..."+ebn.getEncashment());
			}
			if(getlist1.isEmpty())
			{
				System.out.println("nothinggggg");
				response.sendRedirect("encashment.jsp?action=NoRecord");
			}
			else
			{
				System.out.println("in else");
				status=2;
				status1=String.valueOf(status);
				System.out.println("in getencash newstst.."+status1);
				session.setAttribute("status",status1);
				session.setAttribute("getcalculatedlist", getlist1);
				//request.getRequestDispatcher("encashment.jsp?action=getDetails").forward(request,response);
				response.sendRedirect("encashment.jsp?action=getDetails&statuss=calencash");
			}
			
		}
		
		if(action.equalsIgnoreCase("posttransaction"))	
		{
			ArrayList<EncashmentBean> postlist= new ArrayList<EncashmentBean>();
			//ArrayList<EmployeeBean> list1= new ArrayList<EmployeeBean>();
			System.out.println("in posttransaction of encash servlet");
			EncashmentHandler end=new EncashmentHandler();
			String forYear=(String)session.getAttribute("year");
			String prj=(String)session.getAttribute("prj");
			System.out.println("forYear..."+forYear);
			//postlist=(ArrayList<EncashmentBean>)session.getAttribute("list2");
			System.out.println("project is..."+prj);
			if(prj.equalsIgnoreCase("all"))
			{
				projcode="ALL";
				postlist=end.getUpdaterecord(forYear);
			System.out.println("in posttransaction encashment...new and list size is"+postlist.size()+"for year.."+forYear);
			boolean fg=end.posttransaction(postlist);
			if(fg)
			{
				status=2;
				status1=String.valueOf(status);
				System.out.println("in posttran newstst.."+status1);
				session.setAttribute("status",status1);
				System.out.println("record saved in paytran...with status p");
				response.sendRedirect("encashment.jsp?action=getDetails&Save=save");
				
			}
			session.setAttribute("postlist", postlist);
			}
		}
		else{
			System.out.println("noo action");
		}
	}

				
				
}