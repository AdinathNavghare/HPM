package payroll.Controller;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

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
import payroll.DAO.CodeMasterHandler;
import payroll.DAO.ConnectionManager;
import payroll.DAO.LookupHandler;
import payroll.Model.PFExcelBean;
import payroll.Model.RepoartBean;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.StringTokenizer;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
/**newpayreg
 * Servlet implementation class ReportServlet
 */
@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public ReportServlet() 
	{
		super();	
	}

	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{
		String action = request.getParameter("action");
		String type1 = request.getParameter("type")==null?"":request.getParameter("type");
		
		Properties prop = new Properties();
	     try
	     {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
	     }
	     catch (Exception e) {System.out.println("Error in constant properties Manager "+e);}
		
		
		 if(action.equalsIgnoreCase("pdfpayslip") && type1.equalsIgnoreCase("new"))
		{
		
			String date = request.getParameter("date");
			date="1-"+date;
			//String[] empno=request.getParameter("EMPNO").split(":");
			//String[] empno1=request.getParameter("EMPNO1").split(":");
			
			String empList = request.getParameter("list");
			//int EMPNO = Integer.parseInt(empno[1] );
			//int EMPNO1 = Integer.parseInt(empno1[1]);
			String filePath = getServletContext().getRealPath("")+ File.separator + "PaySlip.pdf";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			UtilityDAO.PaySlip(date, empList, filePath, imagepath);
			
			String filePath1 = getServletContext().getRealPath("")+ File.separator + "PaySlipNew.pdf";
			File file = new File(filePath1);
			if (file.exists()) 
			{
				file.delete();
			}
			try
			{
				Document doc  = new Document();
				PdfWriter.getInstance(doc, new FileOutputStream(filePath1));
				BufferedReader inp = new BufferedReader(new FileReader(filePath));
				String line;
				doc.setPageSize(PageSize.A4.rotate());
				
				doc.open();
				while((line = inp.readLine())!=null)
				{
					if(line.contains("AUTHORISED"))
					{
						doc.add(Chunk.NEWLINE);
					}
					
					Paragraph ph = new Paragraph(line,new Font(Font.COURIER,9));
					doc.add(ph);
					if(line.contains("P&A Dept. H.O") ){
						doc.newPage();
					}
				}
				doc.close();
				inp.close();
				System.out.println(filePath1);
				PrintWriter out = response.getWriter();
				response.setContentType("text/html");
				out.write("<iframe scrolling=\"auto\" src=\"PaySlipNew.pdf\" height=\"400px\" width=\"1000px\"></iframe>");
				
			}
			catch ( Exception e) 
			{
				e.printStackTrace();
			}
		
		}
		 
		 
		 
		 
			else if(action.equalsIgnoreCase("pdfpayslip") && type1.equalsIgnoreCase("old"))
			{
				java.util.Date convertedDate=null;
				java.util.Date startdate=null;
				SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yyyy");
				
				String date = request.getParameter("date");
				String empList = request.getParameter("list");
				String imagepath =getServletContext().getRealPath("/images/logo.png");
				String concat = ReportDAO.concation();
				String filePath = getServletContext().getRealPath("")+ File.separator + "PaySlip_"+concat+"_"+date+".pdf";
				File file = new File(filePath);
				if (file.exists()) 
				{
					file.delete();
				}
				
				
		        try {
					convertedDate = sourceFormat.parse(date);
				startdate= sourceFormat.parse("31-Jan-2016");  // 31-Jan-2016
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      
		        if (convertedDate.after(startdate)){
		        	System.out.println("after 1-feb");
				UtilityDAO.PaySlipPDF(date, empList, filePath,imagepath);  // method for not printing bday messages on payslip
		        }
		        else
		        {
		        	System.out.println("before 1-feb");
		        	UtilityDAO.PaySlipPDFOld(date, empList, filePath,imagepath);  
		        }  	
		        
			//	UtilityDAO.PaySlip(date, empList, filePath,imagepath); // method for not printing bday messages on payslip
				PrintWriter out = response.getWriter();
				response.setContentType("text/html");
				out.write("<iframe scrolling=\"auto\" src=\"PaySlip_"+concat+"_"+date+".pdf\" height=\"500px\" width=\"100%\"></iframe>");
			}
		 
		 
		 
		 else if (action.equalsIgnoreCase("emppflist")) 
			{
				String date = request.getParameter("date");
				date="01-"+date;
				String date1 = request.getParameter("date1");
				date1="01-"+date1;
				String type = request.getParameter("type");
				String filePath = getServletContext().getRealPath("")+ File.separator + "EmpList.txt";
				String filePath1 = getServletContext().getRealPath("")+ File.separator + "PF_List.pdf";
				String imagepath =getServletContext().getRealPath("/images/logo.png");
				 String table_name=request.getParameter("before")==null?"after":"before";
				if((type.equalsIgnoreCase("general")))
				{
					String filepath=getServletContext().getRealPath("")+ File.separator + "PF_List.xls";
				 ExcelDAO.pflistexcel(date,type, filepath,imagepath);
				 try
					{
					
						final int BUFSIZE = 4096;
						File file = new File(filepath);
						int length = 0;
						ServletOutputStream outStream = response.getOutputStream();
						ServletContext context = getServletConfig().getServletContext();
						String mimetype = context.getMimeType(filepath);
						if (mimetype == null) 
						{
							mimetype = "application/octet-stream";
						}
						response.setContentType(mimetype);
						response.setContentLength((int) file.length());
						String fileName = (new File(filepath)).getName();
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
					catch ( Exception e) 
					{
						e.printStackTrace();
					}
				}
				else
				{
					UtilityDAO.PFLISTPDF(date, filePath1,table_name);	
				//UtilityDAO.EMPLIST(date,date1, type, filePath1,imagepath);
				try
				{
					
					/*if(!(type.equalsIgnoreCase("J")||type.equalsIgnoreCase("L")||type.equalsIgnoreCase("C")))
					{
					Document doc  = new Document();
					PdfWriter.getInstance(doc, new FileOutputStream(filePath1));
					BufferedReader inp = new BufferedReader(new FileReader(filePath));
					String line;
					doc.setPageSize(PageSize.A4.rotate());
					
					doc.open();
					while((line = inp.readLine())!=null)
					{
						if(line.contains("AUTHORISED"))
						{
							doc.add(Chunk.NEWLINE);
							//doc.add(Chunk.NEWLINE);
						}
				
						Paragraph ph = new Paragraph(line,new Font(Font.COURIER,9));
						doc.add(ph);
						if(line.contains("P&A Dept. H.O"))
						{
							doc.newPage();
						}
					}
					doc.close();
					inp.close();
					}*/
					final int BUFSIZE = 4096;
					File file = new File(filePath1);
					int length = 0;
					ServletOutputStream outStream = response.getOutputStream();
					ServletContext context = getServletConfig().getServletContext();
					String mimetype = context.getMimeType(filePath1);
					if (mimetype == null) 
					{
						mimetype = "application/octet-stream";
					}
					response.setContentType(mimetype);
					response.setContentLength((int) file.length());
					String fileName = (new File(filePath1)).getName();
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
				catch ( Exception e) 
				{
					e.printStackTrace();
				}
				}
			}
		 
		 
		 
		 
		 
		
		else if (action.equalsIgnoreCase("payreg")) 
		{
			String date = request.getParameter("date");
			String Desgn = request.getParameter("Desgn");
			String filePath = getServletContext().getRealPath("")+ File.separator + "PayReg.txt";
			File file = new File(filePath);
			if (file.exists()) 
			{
				file.delete();
			}
			UtilityDAO.payreg(date,Desgn, filePath);
			String filePath1 = getServletContext().getRealPath("")+ File.separator + "PayReg.pdf";
			try
			{
				Document doc  = new Document();
				PdfWriter.getInstance(doc, new FileOutputStream(filePath1));
				BufferedReader inp = new BufferedReader(new FileReader(filePath));
				String line;
				doc.setPageSize(PageSize.A2);
				
				doc.open();
				while((line = inp.readLine())!=null)
				{
					Paragraph ph = new Paragraph(line,new Font(Font.COURIER,8));
					ph.setAlignment(Element.ALIGN_LEFT);
					doc.add(ph);
					
				}
				doc.close();
				inp.close();
			}
			catch ( Exception e) 
			{
				e.printStackTrace();
			}
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write("<iframe scrolling=\"auto\" src=\"PayReg.pdf\" height=\"400px\" width=\"100%\"></iframe>");
		}
	/*	if (action.equalsIgnoreCase("payslip")) 
		{
			String date = request.getParameter("date");
			int EMPNO = Integer.parseInt(request.getParameter("EMPNO"));
			int EMPNO1 = Integer.parseInt(request.getParameter("EMPNO1"));
			String filePath = getServletContext().getRealPath("")+ File.separator + "PaySlip.txt";
			UtilityDAO.PaySlip(date, EMPNO, EMPNO1, filePath);
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
		}*/
		 
		 
		 
		 
		/*else if (action.equalsIgnoreCase("pflist")) 
		{
			System.out.println("in pf list");
			String date = request.getParameter("date");
			date="01-"+date;
			String filename="";
			String format = "";
			//String reporttype="excelfile";
			String reporttype = request.getParameter("reporttype");    //==null?"after":"before";;
			if(reporttype.equalsIgnoreCase("")){
				reporttype="excelfile";
			}
			System.out.println("Filetype"+reporttype);
			String table_name=request.getParameter("before")==null?"after":"before";
			//if(reporttype.equalsIgnoreCase("textfile"))
			if(reporttype.equalsIgnoreCase("textfile"))
			{
				System.out.println("I Am in a PDF");
				String filePath = getServletContext().getRealPath("")+ File.separator + "PFLIST.pdf";
				UtilityDAO.PFLISTPDF(date, filePath,table_name);
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

				if(format.equalsIgnoreCase("txt")){
					filename="NewPayReg.txt";
				}			
				
				else{
					filename=format.equalsIgnoreCase("xls")?"NewPayReg.xls":"NewPayReg.pdf";
				}
				
				
				
				
				
				if (file.exists()) 
				{
					file.delete();
				}
			}
		if(reporttype.equalsIgnoreCase("excelfile"))
			{
				try
				{
					Date date1 = new Date() ;
					SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
					String dt = format.format(date1);
					
					SimpleDateFormat fromformat = new SimpleDateFormat("yyyy-MM-dd");
					fromformat.setLenient(false);
					HSSFWorkbook hwb=new HSSFWorkbook();
					HSSFSheet sheet =  hwb.createSheet("PFLIST");
					
					HSSFRow rowtitle=   sheet.createRow((short)0);
					rowtitle.createCell((short) 0).setCellValue(prop.getProperty("watermark")+"PFLIST : "+date);
					//rowtitle.createCell((short) 1).setCellValue("PFLIST : "+rdt);
					HSSFFont blueFont = hwb.createFont();
					blueFont.setColor(HSSFColor.BLUE.index);
					
					HSSFCellStyle style = hwb.createCellStyle();
					//style.setFont(blueFont);
					style.setFillForegroundColor(HSSFColor.BLUE.index);
					
					HSSFRow rowhead=   sheet.createRow((short)1);
					rowhead.createCell((short) 0).setCellValue("Branch");
					rowhead.createCell((short) 1).setCellValue("Employee No");
					rowhead.createCell((short) 2).setCellValue("Name");
					rowhead.createCell((short) 3).setCellValue("PFNO");
					rowhead.createCell((short) 4).setCellValue("Basic");
					rowhead.createCell((short) 5).setCellValue("DA");
					rowhead.createCell((short) 6).setCellValue("Gross");
					rowhead.createCell((short) 7).setCellValue("PF");
					rowhead.createCell((short) 8).setCellValue("@8.33%");
					rowhead.createCell((short) 9).setCellValue("@3.67%");
							
					//System.out.println("CAlling arraylist!");
					
					ArrayList<PFExcelBean> pflist = (ArrayList<PFExcelBean>) (UtilityDAO.PFExcel(date)==null?"":UtilityDAO.PFExcel(date));
					
				
					
					String branch="";
					double Basictotal = 0;
					double Grosstotal = 0;
					double PFtotal = 0;
					double DAtotal = 0;
					double Gr8total = 0;
					double Gr3total = 0;
					int i=2;
					
					for (PFExcelBean pfExcelBean : pflist) 
					{
						
						HSSFRow row = sheet.createRow((short)i);
						if(i==2)
						{
							branch=pfExcelBean.getBRANCH();
						}
						if(branch.equalsIgnoreCase(pfExcelBean.getBRANCH()))
						{
							row.createCell((short) 0).setCellValue(pfExcelBean.getBRANCH());
							row.createCell((short) 1).setCellValue(pfExcelBean.getEMPNo());
							row.createCell((short) 2).setCellValue(pfExcelBean.getEMPNAME());
							row.createCell((short) 3).setCellValue(pfExcelBean.getPFNO());
							row.createCell((short) 4).setCellValue(pfExcelBean.getBASIC());
							row.createCell((short) 5).setCellValue(pfExcelBean.getDA());
							row.createCell((short) 6).setCellValue(pfExcelBean.getGROSS());
							row.createCell((short) 7).setCellValue(pfExcelBean.getPF());
							row.createCell((short) 8).setCellValue(pfExcelBean.getGROSS8());
							row.createCell((short) 9).setCellValue(pfExcelBean.getGROSS3());
							branch=pfExcelBean.getBRANCH();
						}
						else
						{
							row.createCell((short) 0).setCellValue(pfExcelBean.getBRANCHTOTAL());
							row.createCell((short) 1).setCellValue("");
							row.createCell((short) 2).setCellValue("");
							row.createCell((short) 3).setCellValue("");
							row.createCell((short) 4).setCellValue(pfExcelBean.getBASICTOTAL());
							Basictotal= Basictotal+ Double.parseDouble( pfExcelBean.getBASICTOTAL());
							row.createCell((short) 5).setCellValue(pfExcelBean.getDATOTAL());
							DAtotal=DAtotal+ Double.parseDouble( pfExcelBean.getDATOTAL());
							row.createCell((short) 6).setCellValue(pfExcelBean.getGROSSTOTAL());
							Grosstotal=Grosstotal+ Double.parseDouble( pfExcelBean.getGROSSTOTAL());
							row.createCell((short) 7).setCellValue(pfExcelBean.getPFTOTAL());
							PFtotal=PFtotal+ Double.parseDouble( pfExcelBean.getPFTOTAL());
							row.createCell((short) 8).setCellValue(pfExcelBean.getGR8TOTAL());
							Gr8total=Gr8total+ Double.parseDouble( pfExcelBean.getGR8TOTAL());
							row.createCell((short) 9).setCellValue(pfExcelBean.getGR3TOTAL());
							Gr3total=Gr3total+ Double.parseDouble( pfExcelBean.getGR3TOTAL());
							
							branch=pfExcelBean.getBRANCH();
						}
						i++;
					}
					HSSFRow rowTotal = sheet.createRow((short)i);
					rowTotal.createCell((short) 0).setCellValue("ALL BRANCH TOTAL");
					rowTotal.createCell((short) 1).setCellValue("");
					rowTotal.createCell((short) 2).setCellValue("");
					rowTotal.createCell((short) 3).setCellValue("");
					rowTotal.createCell((short) 4).setCellValue(Basictotal);
					rowTotal.createCell((short) 5).setCellValue(DAtotal);
					rowTotal.createCell((short) 6).setCellValue(Grosstotal);
					rowTotal.createCell((short) 7).setCellValue(PFtotal);
					rowTotal.createCell((short) 8).setCellValue(Gr8total);
					rowTotal.createCell((short) 9).setCellValue(Gr3total);
					
					String filePath1 = getServletContext().getRealPath("")+ File.separator + "Harsh_Constuction_PFList.xls";
					
					FileOutputStream fileOut =  new FileOutputStream(filePath1);//"D:\\PFList.xls"
					hwb.write(fileOut);
					fileOut.close();
					final int BUFSIZE = 4096;
					File file = new File(filePath1);
					int length = 0;
					ServletOutputStream outStream = response.getOutputStream();
					ServletContext context = getServletConfig().getServletContext();
					String mimetype = context.getMimeType(filePath1);
					if (mimetype == null) 
					{
						mimetype = "application/octet-stream";
					}
					response.setContentType(mimetype);
					response.setContentLength((int) file.length());
					String fileName = (new File(filePath1)).getName();
					response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
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
					System.out.println("Your excel file has been generated!");
				} 
				catch (Exception ex ) 
				{
					ex.printStackTrace();
				}
			
			}
			
		}*/
		 
		 
		else if(action.equalsIgnoreCase("ecr")){
			
			String date = request.getParameter("date");
			date="01-"+date;
			String []dt = date.split("-");
			String reporttype = request.getParameter("reporttype");
			String table_name=request.getParameter("before")==null?"after":"before";
			if(reporttype.equalsIgnoreCase("pdffile")){
				
				final int BUFSIZE = 4096;
				String filePath = getServletContext().getRealPath("")+ File.separator + "ECR-Report_"+dt[1]+""+dt[2]+".pdf";

				try {
					UtilityDAO.EcrReport(date,filePath,table_name);
					
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
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			if(reporttype.equalsIgnoreCase("excelfile"))
			{
				try
				{
					if(table_name.equalsIgnoreCase("before")){
						table_name="PAYTRAN";
					}
					else
					{
						table_name="PAYTRAN_STAGE";
					}
					Connection Cn = null;
					ResultSet emp = null;
					String EmpSql = "";
					int epf_total = 0;
					int eps_total = 0;
					int epf_due_total = 0;
					int eps_due_total = 0;
					int diff_due_total = 0;
					int ncp_total = 0;
					int refund_total = 0;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
					SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
					SimpleDateFormat fromformat = new SimpleDateFormat("MM/yyyy");
					fromformat.setLenient(false);
					RepoartBean repBean  = new RepoartBean();
					HSSFWorkbook hwb=new HSSFWorkbook();
					HSSFSheet sheet =  hwb.createSheet("ECR Report");
					Calendar currentMonth = Calendar.getInstance();
			        currentMonth.setTime(format.parse(date));
			        currentMonth.add(Calendar.MONTH, 1);
			      
	                HSSFCellStyle my_style = hwb.createCellStyle();
	                HSSFFont my_font=hwb.createFont();
	                my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	                my_style.setFont(my_font);
	                
					HSSFRow rowtitle=   sheet.createRow((short)0);
					HSSFCell cell = rowtitle.createCell((short) 5);
					cell.setCellValue("EMPLOYEE'S PROVIDENT FUND ORGANISATION, NASHIK");
					cell.setCellStyle(my_style);
					HSSFRow rowtitle1=   sheet.createRow((short)1);
					HSSFCell cell1 = rowtitle1.createCell((short) 5);
					cell1.setCellValue("ELECTRONIC CHALLAN CUM RETURN (ECR)");
					cell1.setCellStyle(my_style);
					HSSFRow rowtitle2=   sheet.createRow((short)2);
					HSSFCell cell2 = rowtitle2.createCell((short) 5);
					cell2.setCellValue("FOR THE WAGE MONTH OF ("+fromformat.format(format.parse(date))+") AND RETURN MONTH ("+fromformat.format(currentMonth.getTime())+")");
					cell2.setCellStyle(my_style);
					HSSFRow rowtitle31=   sheet.createRow((short)3);
					rowtitle31.createCell((short) 0).setCellValue("");
					HSSFRow rowtitle3=   sheet.createRow((short)4);
					rowtitle3.createCell((short) 0).setCellValue("ESTABLISHMENT ID              : KDNSK0051492000");
					HSSFRow rowtitle4=   sheet.createRow((short)5);
					rowtitle4.createCell((short) 0).setCellValue("NAME OF ESTABLISHMENT  : "+prop.getProperty("watermark")+" 'BUILDER - CONTRACT OR'");
					HSSFRow rowtitle5=   sheet.createRow((short)6);
					rowtitle5.createCell((short) 0).setCellValue("TRRN                                    : 3141501006505");
					
					HSSFFont blueFont = hwb.createFont();
					blueFont.setColor(HSSFColor.BLUE.index);
					
					HSSFCellStyle style = hwb.createCellStyle();
					//style.setFont(blueFont);
					style.setFillForegroundColor(HSSFColor.BLUE.index);
					
					HSSFRow head=   sheet.createRow((short)7);
					head.createCell((short) 0).setCellValue("");
					HSSFRow heading=   sheet.createRow((short)8);
					HSSFCell cell3 = heading.createCell((short) 0); 
					cell3.setCellValue("PART A-MEMBER'S WAGE DETAILS");
					cell3.setCellStyle(my_style);
					HSSFRow head1=   sheet.createRow((short)9);
					head1.createCell((short) 0).setCellValue("");
					HSSFRow rowhead=   sheet.createRow((short)10);
					rowhead.createCell((short) 0).setCellValue("Sr No.");
					rowhead.createCell((short) 1).setCellValue("Member Id");
					rowhead.createCell((short) 3).setCellValue("Member Name");
					rowhead.createCell((short) 6).setCellValue("EPF Wages");
					rowhead.createCell((short) 7).setCellValue("EPS Wages");
					rowhead.createCell((short) 8).setCellValue("EPF Contribution (EE Share) due");
					rowhead.createCell((short) 9).setCellValue("EPF Contribution (EE Share) being remitted");
					rowhead.createCell((short) 10).setCellValue("EPS Contribution (EE Share) due");
					rowhead.createCell((short) 11).setCellValue("EPS Contribution (EE Share) being remitted");
					rowhead.createCell((short) 12).setCellValue("Diff EPF and EPS Contribution (ER Share) due");
					rowhead.createCell((short) 13).setCellValue("Diff EPF and EPS Contribution (ER Share) being remitted");
					rowhead.createCell((short) 14).setCellValue("NCP Days");
					rowhead.createCell((short) 15).setCellValue("Refund of Advances");
					
					ReportDAO.OpenCon("", "", "",repBean);
					Cn = repBean.getCn();
					
					EmpSql = "select e.salute ,e.doj,e.pfno, e.empno,e.empcode,e.fname,e.mname,e.lname, " +
							"(select cal_amt from "+table_name+" where trncd=136 and empno=e.empno and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"') basic," +
							"(select cal_amt from "+table_name+" where trncd=137 and empno=e.empno and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"') EPSWAGES," +
							"(select cal_amt from "+table_name+" where trncd=102 and empno=e.empno and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"') da, " +
							"(select cal_amt from "+table_name+" where trncd=201 and empno=e.empno and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"') PF, " +
							"(select cal_amt from "+table_name+" where trncd=231 and empno=e.empno and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"') EEPF, " +
							"(select cal_amt from "+table_name+" where trncd=232 and empno=e.empno and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"') EEPS, " +
							"(select cal_amt from "+table_name+" where trncd=233 and empno=e.empno and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"') EDLI, " +
							"(select cal_amt from "+table_name+" where trncd=234 and empno=e.empno and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"') EEPFADMIN, " +
							"(select cal_amt from "+table_name+" where trncd=235 and empno=e.empno and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"') EDLIADMIN " +
							"from empmast e ,SAL_DETAILS s where s.EMPNO = e.EMPNO  and s.SAL_MONTH = '"+dt[1]+"-"+dt[2]+"' and e.doj <='"+ReportDAO.EOM(date)+"' " +
									"and  e.EMPNO in(select distinct EMPNO from "+table_name+" where TRNCD=201) order by e.empno";
					Statement st;
					st = Cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					
					System.out.println("  "+EmpSql);
					
					emp = st.executeQuery(EmpSql);
					if(!emp.next()){
						HSSFRow row = sheet.createRow((short)12);
						HSSFCell cell11 = row.createCell((short) 5);
						cell11.setCellValue("Salary hasn't been finalized for the month "+dt[1]+"-"+dt[2]);
						cell11.setCellStyle(my_style);
					} else {
					int i=11;
					int count = 1;
					emp.previous();
					float ac22=0,ac21=0,ac10=0,ac02=0;
					while(emp.next()){
						
						HSSFRow row = sheet.createRow((short)i++);
						if(emp.getInt("basic")<=0)
						{
						emp.next();
						}
						
						row.createCell((short) 0).setCellValue(""+count++);
						int epf_wages = emp.getInt("basic")+emp.getInt("da");
						int eps_wages = emp.getInt("EPSWAGES");
						ac02+=Math.round(emp.getFloat("EEPFADMIN"));
						ac10+=Math.round(emp.getFloat("EEPS"));
						ac22+=Math.round(emp.getFloat("EDLIADMIN"));
						ac21+=Math.round(emp.getFloat("EDLI"));
						
						String []pfno = null;
						if(!emp.getString("pfno").equals("") ) {
							pfno = emp.getString("pfno").split("/");
							row.createCell((short) 1).setCellValue("000"+pfno[3]);
						} else {
							row.createCell((short) 1).setCellValue("");
						}
						if(emp.getString("MNAME").equals(null) || emp.getString("MNAME").equals(""))
						{
							row.createCell((short) 2).setCellValue(emp.getString("FNAME")+" "+emp.getString("MNAME")+" "+emp.getString("LNAME"));
						}
						else
						{
							row.createCell((short) 2).setCellValue(emp.getString("FNAME")+" "+emp.getString("MNAME")+" "+emp.getString("LNAME"));
						}
												
						row.createCell((short) 6).setCellValue(epf_wages);
						epf_total += epf_wages;
						
						float eps_cont = 0;
						row.createCell((short) 7).setCellValue(eps_wages);
							eps_total += eps_wages;
							eps_cont = Math.round(emp.getInt("EEPS"));
						
						float epf_cont = Math.round(emp.getInt("PF"));
						epf_due_total += epf_cont;
						
						row.createCell((short) 8).setCellValue(epf_cont);
						row.createCell((short) 9).setCellValue(epf_cont);
						eps_due_total += eps_cont;
						row.createCell((short) 10).setCellValue(eps_cont);
						row.createCell((short) 11).setCellValue(eps_cont);
						diff_due_total += epf_cont-eps_cont;
						row.createCell((short) 12).setCellValue(epf_cont-eps_cont);
						row.createCell((short) 13).setCellValue(epf_cont-eps_cont);
						row.createCell((short) 14).setCellValue(0);
						row.createCell((short) 15).setCellValue(0);
						
					}
					
					HSSFRow rowTotal = sheet.createRow((short)i);
					rowTotal.createCell((short) 0).setCellValue("");
					rowTotal.createCell((short) 1).setCellValue("");
					rowTotal.createCell((short) 2).setCellValue("");
					rowTotal.createCell((short) 3).setCellValue("");
					HSSFCell cell4 = rowTotal.createCell((short) 4); 
					cell4.setCellValue("GRAND TOTAL :");
					cell4.setCellStyle(my_style);
					rowTotal.createCell((short) 6).setCellValue(epf_total);
					rowTotal.createCell((short) 7).setCellValue(eps_total);
					rowTotal.createCell((short) 8).setCellValue(epf_due_total);
					rowTotal.createCell((short) 9).setCellValue(epf_due_total);
					rowTotal.createCell((short) 10).setCellValue(eps_due_total);
					rowTotal.createCell((short) 11).setCellValue(eps_due_total);
					rowTotal.createCell((short) 12).setCellValue(diff_due_total);
					rowTotal.createCell((short) 13).setCellValue(diff_due_total);
					rowTotal.createCell((short) 14).setCellValue(ncp_total);
					rowTotal.createCell((short) 15).setCellValue(refund_total);
					
					HSSFRow Totals = sheet.createRow((short)i+3);
					Totals.createCell((short) 0).setCellValue("");
					Totals.createCell((short) 4).setCellValue("A/C 01 EE + Refund of Advance");
					Totals.createCell((short) 7).setCellValue("A/C 01 ER");
					Totals.createCell((short) 8).setCellValue("A/C 02");
					Totals.createCell((short) 9).setCellValue("A/C 10");
					Totals.createCell((short) 10).setCellValue("A/C 21");
					Totals.createCell((short) 11).setCellValue("A/C 22");
					Totals.createCell((short) 12).setCellValue("TOTAL");
					for(int j=4; j<6; j++){
						HSSFRow Total1 = sheet.createRow((short)i+j);
						HSSFCell cell5 = Total1.createCell((short) 0); 
						if(j==4){
							cell5.setCellValue("TOTAL DUES AS PER ECR");
						} else {
							cell5.setCellValue("TOTAL AMOUNT BEING REMITTED");
						}
						cell5.setCellStyle(my_style);
						Total1.createCell((short) 6).setCellValue(epf_due_total);
						Total1.createCell((short) 7).setCellValue(diff_due_total);
						
						Total1.createCell((short) 8).setCellValue((int)ac02);
						Total1.createCell((short) 9).setCellValue(eps_due_total);
						
						Total1.createCell((short) 10).setCellValue((int)ac21);
						
						Total1.createCell((short) 11).setCellValue((int)ac22);
						float total = epf_due_total+diff_due_total+ac02+eps_due_total+ac21+ac22;
						Total1.createCell((short) 12).setCellValue((int)total);
					}
					
					String EmpSql1 = "select e.salute ,e.dob,e.gender,e.pfno, e.empno,e.empcode,e.fname,e.mname,e.lname " +
							"from empmast e where e.status = 'A' and e.doj between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' order by e.empno";
					Statement st1;
					st1 = Cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					ResultSet emp1 = st1.executeQuery(EmpSql1);
					if(!emp1.next()){
						System.out.println("No Any Record Found");
					}
					HSSFRow heading2=   sheet.createRow((short)i+8);
					HSSFCell cell6 = heading2.createCell((short) 0); 
					cell6.setCellValue("PART B-NEW MEMBER'S DETAILS");
					cell6.setCellStyle(my_style);
					HSSFRow rowhead2=   sheet.createRow((short)i+10);
					rowhead2.createCell((short) 0).setCellValue("Sr No.");
					rowhead2.createCell((short) 1).setCellValue("Member Id");
					rowhead2.createCell((short) 3).setCellValue("Member Name");
					rowhead2.createCell((short) 6).setCellValue("Father's/Spouse Name");
					rowhead2.createCell((short) 8).setCellValue("Relationship with the Member");
					rowhead2.createCell((short) 9).setCellValue("Date of Birth");
					rowhead2.createCell((short) 10).setCellValue("Gender");
					rowhead2.createCell((short) 11).setCellValue("Date of Joining EPF");
					rowhead2.createCell((short) 13).setCellValue("Date of Joining EPS");
					int count1 = 1;
					i=i+11;
					emp1.previous();
										
					while(emp1.next()){
						
						HSSFRow row = sheet.createRow((short)i++);
						
						row.createCell((short) 0).setCellValue(""+count1++);
				
						String []pfno = null;
						if(!emp1.getString("pfno").equals("") ) {
							pfno = emp1.getString("pfno").split("/");
							row.createCell((short) 1).setCellValue("000"+pfno[3]);
						} else {
							row.createCell((short) 1).setCellValue("");
						}
						if(emp1.getString("MNAME").equals(null) || emp1.getString("MNAME").equals(""))
						{
							row.createCell((short) 2).setCellValue(emp1.getString("FNAME")+" "+emp1.getString("MNAME")+" "+emp1.getString("LNAME"));
						}
						else
						{
							row.createCell((short) 2).setCellValue(emp1.getString("FNAME")+" "+emp1.getString("MNAME")+" "+emp1.getString("LNAME"));
						}
												
						row.createCell((short) 6).setCellValue(emp1.getString("MNAME"));
						if(!emp1.getString("MNAME").equals("")) {
							row.createCell((short) 8).setCellValue("Father");
						} else {	
							row.createCell((short) 8).setCellValue("");
						}
						Date dob = sdf.parse(emp1.getString("dob"));
						row.createCell((short) 9).setCellValue(format1.format(dob));
						if(emp1.getString("gender").contains("M")){
							row.createCell((short) 10).setCellValue("Male");
						} else {
							row.createCell((short) 10).setCellValue("Female");
						}
						Date jdate = format.parse(ReportDAO.BOM(date));						
						row.createCell((short) 11).setCellValue(format1.format(jdate));
						row.createCell((short) 13).setCellValue(format1.format(jdate));
						
					}
					
					String EmpSql2 = "select e.salute ,e.dob,e.dol,e.gender,e.pfno, e.empno,e.empcode,e.fname,e.mname,e.lname " +
							"from empmast e where e.dol between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' order by e.empno";
					Statement st2;
					st2 = Cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					ResultSet emp2 = st2.executeQuery(EmpSql2);
					if(!emp2.next()){
						System.out.println("No Any Record Found");
					}
					
					HSSFRow heading3=   sheet.createRow((short)i+2);
					HSSFCell cell7 = heading3.createCell((short) 0); 
					cell7.setCellValue("PART C-EXITING MEMBER'S DETAILS");
					cell7.setCellStyle(my_style);
					HSSFRow rowhead3=   sheet.createRow((short)i+4);
					rowhead3.createCell((short) 0).setCellValue("Sr No.");
					rowhead3.createCell((short) 1).setCellValue("Member Id");
					rowhead3.createCell((short) 3).setCellValue("Member Name");
					rowhead3.createCell((short) 6).setCellValue("Father's/Spouse Name");
					rowhead3.createCell((short) 8).setCellValue("Relationship with the Member");
					rowhead3.createCell((short) 9).setCellValue("Date of Birth");
					rowhead3.createCell((short) 10).setCellValue("Gender");
					rowhead3.createCell((short) 11).setCellValue("Date of Exit from EPF");
					rowhead3.createCell((short) 13).setCellValue("Date of Exit from EPS");
					rowhead3.createCell((short) 15).setCellValue("Reason for leaving");
					int count2 = 1;
					i=i+5;
					emp2.previous();
										
					while(emp2.next()){
						
						HSSFRow row = sheet.createRow((short)i++);
						
						row.createCell((short) 0).setCellValue(""+count2++);
				
						String []pfno = null;
						if(!emp2.getString("pfno").equals("") ) {
							pfno = emp2.getString("pfno").split("/");
							row.createCell((short) 1).setCellValue("000"+pfno[3]);
						} else {
							row.createCell((short) 1).setCellValue("");
						}
						if(emp2.getString("MNAME").equals(null) || emp2.getString("MNAME").equals(""))
						{
							row.createCell((short) 2).setCellValue(emp2.getString("FNAME")+" "+emp2.getString("MNAME")+" "+emp2.getString("LNAME"));
						}
						else
						{
							row.createCell((short) 2).setCellValue(emp2.getString("FNAME")+" "+emp2.getString("MNAME")+" "+emp2.getString("LNAME"));
						}
												
						row.createCell((short) 6).setCellValue(emp2.getString("MNAME"));
						if(!emp2.getString("MNAME").equals("")) {
							row.createCell((short) 8).setCellValue("Father");
						} else {	
							row.createCell((short) 8).setCellValue("");
						}	
						Date dob = sdf.parse(emp2.getString("dob"));
						row.createCell((short) 9).setCellValue(format1.format(dob));
						if(emp2.getString("gender").contains("M")){
							row.createCell((short) 10).setCellValue("Male");
						} else {
							row.createCell((short) 10).setCellValue("Female");
						}
						Date ldate = sdf.parse(emp2.getString("dol"));						
						row.createCell((short) 11).setCellValue(format1.format(ldate));
						row.createCell((short) 13).setCellValue(format1.format(ldate));
						row.createCell((short) 15).setCellValue("");
						
					}
					
					HSSFRow heading4=   sheet.createRow((short)i+2);
					HSSFCell cell8 = heading4.createCell((short) 0); 
					cell8.setCellValue("PART D-MEMBER'S ARREAR DETAILS");
					cell8.setCellStyle(my_style);
					HSSFRow rowhead4=   sheet.createRow((short)i+4);
					rowhead4.createCell((short) 2).setCellValue("--- NIL ---");
					}					
					String filePath1 = getServletContext().getRealPath("")+ File.separator + "ECR-Report_"+dt[1]+""+dt[2]+".xls";
					FileOutputStream fileOut =  new FileOutputStream(filePath1);//"D:\\PFList.xls"
					hwb.write(fileOut);
					fileOut.close();
					final int BUFSIZE = 4096;
					File file = new File(filePath1);
					int length = 0;
					ServletOutputStream outStream = response.getOutputStream();
					ServletContext context = getServletConfig().getServletContext();
					String mimetype = context.getMimeType(filePath1);
					if (mimetype == null) 
					{
						mimetype = "application/octet-stream";
					}
					response.setContentType(mimetype);
					response.setContentLength((int) file.length());
					String fileName = (new File(filePath1)).getName();
					response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
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
					//System.out.println("Your excel file has been generated!");
				} 
				catch (Exception ex ) 
				{
					ex.printStackTrace();
				}
			}
		}
		else if(action.equalsIgnoreCase("excelpf"))
		{
			String rdt = request.getParameter("date");
			try
			{
				Date date1 = new Date() ;
				SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
				String dt = format.format(date1);
				
				SimpleDateFormat fromformat = new SimpleDateFormat("yyyy-MM-dd");
				fromformat.setLenient(false);
				HSSFWorkbook hwb=new HSSFWorkbook();
				HSSFSheet sheet =  hwb.createSheet("PFLIST");
					
				HSSFRow rowtitle=   sheet.createRow((short)0);
				rowtitle.createCell((short) 0).setCellValue(prop.getProperty("watermark")+"PFLIST : "+rdt);
				//rowtitle.createCell((short) 1).setCellValue("PFLIST : "+rdt);
				HSSFFont blueFont = hwb.createFont();
				blueFont.setColor(HSSFColor.BLUE.index);
				
				HSSFCellStyle style = hwb.createCellStyle();
				//style.setFont(blueFont);
				style.setFillForegroundColor(HSSFColor.BLUE.index);
					
				HSSFRow rowhead=   sheet.createRow((short)1);
				rowhead.createCell((short) 0).setCellValue("Branch");
				rowhead.createCell((short) 1).setCellValue("Employee No");
				rowhead.createCell((short) 2).setCellValue("Name");
				rowhead.createCell((short) 3).setCellValue("PFNO");
				rowhead.createCell((short) 4).setCellValue("Basic");
				rowhead.createCell((short) 5).setCellValue("DA");
				rowhead.createCell((short) 6).setCellValue("Gross");
				rowhead.createCell((short) 7).setCellValue("PF");
				rowhead.createCell((short) 8).setCellValue("@8.33%");
				rowhead.createCell((short) 9).setCellValue("@3.67%");
				
				
				
				ArrayList<PFExcelBean> pflist = UtilityDAO.PFExcel(rdt);
				
				
				String branch="";
				double Basictotal = 0;
				double Grosstotal = 0;
				double PFtotal = 0;
				double DAtotal = 0;
				double Gr8total = 0;
				double Gr3total = 0;
				int i=2;
					
				for (PFExcelBean pfExcelBean : pflist) 
				{
					HSSFRow row = sheet.createRow((short)i);
					if(i==2)
					{
						branch=pfExcelBean.getBRANCH();
					}
					if(branch.equalsIgnoreCase(pfExcelBean.getBRANCH()))
					{
						row.createCell((short) 0).setCellValue(pfExcelBean.getBRANCH());
						row.createCell((short) 1).setCellValue(pfExcelBean.getEMPNo());
						row.createCell((short) 2).setCellValue(pfExcelBean.getEMPNAME());
						row.createCell((short) 3).setCellValue(pfExcelBean.getPFNO());
						row.createCell((short) 4).setCellValue(pfExcelBean.getBASIC());
						row.createCell((short) 5).setCellValue(pfExcelBean.getDA());
						row.createCell((short) 6).setCellValue(pfExcelBean.getGROSS());
						row.createCell((short) 7).setCellValue(pfExcelBean.getPF());
						row.createCell((short) 8).setCellValue(pfExcelBean.getGROSS8());
						row.createCell((short) 9).setCellValue(pfExcelBean.getGROSS3());
						branch=pfExcelBean.getBRANCH();
					}
					else
					{
						row.createCell((short) 0).setCellValue(pfExcelBean.getBRANCHTOTAL());
						row.createCell((short) 1).setCellValue("");
						row.createCell((short) 2).setCellValue("");
						row.createCell((short) 3).setCellValue("");
						row.createCell((short) 4).setCellValue(pfExcelBean.getBASICTOTAL());
						Basictotal= Basictotal+ Double.parseDouble( pfExcelBean.getBASICTOTAL());
						row.createCell((short) 5).setCellValue(pfExcelBean.getDATOTAL());
						DAtotal=DAtotal+ Double.parseDouble( pfExcelBean.getDATOTAL());
						row.createCell((short) 6).setCellValue(pfExcelBean.getGROSSTOTAL());
						Grosstotal=Grosstotal+ Double.parseDouble( pfExcelBean.getGROSSTOTAL());
						row.createCell((short) 7).setCellValue(pfExcelBean.getPFTOTAL());
						PFtotal=PFtotal+ Double.parseDouble( pfExcelBean.getPFTOTAL());
						row.createCell((short) 8).setCellValue(pfExcelBean.getGR8TOTAL());
						Gr8total=Gr8total+ Double.parseDouble( pfExcelBean.getGR8TOTAL());
						row.createCell((short) 9).setCellValue(pfExcelBean.getGR3TOTAL());
						Gr3total=Gr3total+ Double.parseDouble( pfExcelBean.getGR3TOTAL());
						
						branch=pfExcelBean.getBRANCH();
					}
					i++;
				}
				HSSFRow rowTotal = sheet.createRow((short)i);
				rowTotal.createCell((short) 0).setCellValue("ALL BRANCH TOTAL");
				rowTotal.createCell((short) 1).setCellValue("");
				rowTotal.createCell((short) 2).setCellValue("");
				rowTotal.createCell((short) 3).setCellValue("");
				rowTotal.createCell((short) 4).setCellValue(Basictotal);
				rowTotal.createCell((short) 5).setCellValue(DAtotal);
				rowTotal.createCell((short) 6).setCellValue(Grosstotal);
				rowTotal.createCell((short) 7).setCellValue(PFtotal);
				rowTotal.createCell((short) 8).setCellValue(Gr8total);
				rowTotal.createCell((short) 9).setCellValue(Gr3total);
				
				String filePath = getServletContext().getRealPath("")+ File.separator + "Harsh_Constuction_PFList.xls";
				
				FileOutputStream fileOut =  new FileOutputStream(filePath);//"D:\\PFList.xls"
				hwb.write(fileOut);
				fileOut.close();
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
				response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
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
				System.out.println("Your excel file has been generated!");
				//request.getRequestDispatcher("pfList.jsp").forward(request, response);
				
			}
			catch (Exception ex ) 
			{
				ex.printStackTrace();
			}
		}
		else if (action.equalsIgnoreCase("emplist")) 
		{
			String date = request.getParameter("date");
			date="01-"+date;
			String date1 = request.getParameter("date1");
			date1="01-"+date1;
			String type = request.getParameter("type");
			String filePath = getServletContext().getRealPath("")+ File.separator + "EmpList.txt";
			String filePath1 = getServletContext().getRealPath("")+ File.separator + "Emplist.pdf";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			if((type.equalsIgnoreCase("general")))
			{
				String filepath=getServletContext().getRealPath("")+ File.separator + "Generalised_Emplist.xls";
			 ExcelDAO.EMPLIST(date,date1, type, filepath,imagepath);
			 try
				{
					final int BUFSIZE = 4096;
					File file = new File(filepath);
					int length = 0;
					ServletOutputStream outStream = response.getOutputStream();
					ServletContext context = getServletConfig().getServletContext();
					String mimetype = context.getMimeType(filepath);
					if (mimetype == null) 
					{
						mimetype = "application/octet-stream";
					}
					response.setContentType(mimetype);
					response.setContentLength((int) file.length());
					String fileName = (new File(filepath)).getName();
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
				catch ( Exception e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				
			UtilityDAO.EMPLIST(date,date1, type, filePath1,imagepath);
			try
			{
				
				/*if(!(type.equalsIgnoreCase("J")||type.equalsIgnoreCase("L")||type.equalsIgnoreCase("C")))
				{
				Document doc  = new Document();
				PdfWriter.getInstance(doc, new FileOutputStream(filePath1));
				BufferedReader inp = new BufferedReader(new FileReader(filePath));
				String line;
				doc.setPageSize(PageSize.A4.rotate());
				
				doc.open();
				while((line = inp.readLine())!=null)
				{
					if(line.contains("AUTHORISED"))
					{
						doc.add(Chunk.NEWLINE);
						//doc.add(Chunk.NEWLINE);
					}
			
					Paragraph ph = new Paragraph(line,new Font(Font.COURIER,9));
					doc.add(ph);
					if(line.contains("P&A Dept. H.O"))
					{
						doc.newPage();
					}
				}
				doc.close();
				inp.close();
				}*/
				final int BUFSIZE = 4096;
				File file = new File(filePath1);
				int length = 0;
				ServletOutputStream outStream = response.getOutputStream();
				ServletContext context = getServletConfig().getServletContext();
				String mimetype = context.getMimeType(filePath1);
				if (mimetype == null) 
				{
					mimetype = "application/octet-stream";
				}
				response.setContentType(mimetype);
				response.setContentLength((int) file.length());
				String fileName = (new File(filePath1)).getName();
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
			catch ( Exception e) 
			{
				e.printStackTrace();
			}
			}
		}
		 
		 /**********************************************************PRASAD START*******************************************************************/
		 
		else if (action.equalsIgnoreCase("emplistpfreport"))
		{

			String date1 = request.getParameter("date1");
			date1="01-"+date1;
			String type = request.getParameter("type");
			String filePath = getServletContext().getRealPath("")+ File.separator + "EmpList.txt";
			String filePath1 = getServletContext().getRealPath("")+ File.separator + "Emplist.pdf";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			if((type.equalsIgnoreCase("general")))
			{
				String filepath=getServletContext().getRealPath("")+ File.separator + "Generalised_Emplist.xls";
			 ExcelDAO.EMPLISTPFREPORT(date1, type, filepath,imagepath);
			 try
				{
					final int BUFSIZE = 4096;
					File file = new File(filepath);
					int length = 0;
					ServletOutputStream outStream = response.getOutputStream();
					ServletContext context = getServletConfig().getServletContext();
					String mimetype = context.getMimeType(filepath);
					if (mimetype == null) 
					{
						mimetype = "application/octet-stream";
					}
					response.setContentType(mimetype);
					response.setContentLength((int) file.length());
					String fileName = (new File(filepath)).getName();
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
				catch ( Exception e) 
				{
					e.printStackTrace();
				}
			}
	
		
		}
		 
		 /**********************************************************PRASAD END*******************************************************************/
		 
		 //govi
		 
		else if (action.equalsIgnoreCase("CTCChange"))
		{

			String date1 = request.getParameter("date1");
			
			System.out.println("filepath type:"+date1);
			//date1="01-"+date1;
			String type = request.getParameter("type");
			System.out.println("filepath type:"+type);
			String empType = request.getParameter("empType");
			/*String filePath = getServletContext().getRealPath("")+ File.separator + "EmpList.txt";
			String filePath1 = getServletContext().getRealPath("")+ File.separator + "Emplist.pdf";*/
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			String filepath = "";
			if((type.equalsIgnoreCase("excel")))
			{
				filepath=getServletContext().getRealPath("")+ File.separator + "EmpCTCChange.xls";
			}
			else
			{
				filepath=getServletContext().getRealPath("")+ File.separator + "EmpCTCChange.pdf";
			}
				
			 try
				{
				 /*ExcelDAO.EMPLISTPFREPORT(date1, type, filepath,imagepath);*/
				 if(type.equalsIgnoreCase("excel"))
				 {
					 ExcelDAO.empCTCCHange(date1, type, filepath,imagepath,empType);
				 }
				 else
				 {
					 //ExcelDAO.empCTCCHange(date1, type, filepath,imagepath);
				 }
				System.out.println("filepath:"+filepath);
				 final int BUFSIZE = 4096;
					File file = new File(filepath);
					int length = 0;
					ServletOutputStream outStream = response.getOutputStream();
					ServletContext context = getServletConfig().getServletContext();
					String mimetype = context.getMimeType(filepath);
					if (mimetype == null) 
					{
						mimetype = "application/octet-stream";
					}
					response.setContentType(mimetype);
					response.setContentLength((int) file.length());
					String fileName = (new File(filepath)).getName();
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
				catch ( Exception e) 
				{
					e.printStackTrace();
				}
		
	
		
		}
		 
		 //emd g
		 
		 
		 
		else if (action.equalsIgnoreCase("incDedRpt")) 
		{
			String date = request.getParameter("date");
			date="01-"+date;
			String code = request.getParameter("trncd");
			String table = request.getParameter("tname");
			System.out.println("table = "+table);
			System.out.println("trncd = "+code);
			LookupHandler lookupHandler=new LookupHandler();
			String codeName=lookupHandler.getCode_Desc(code);
			String filePath = getServletContext().getRealPath("")+ File.separator +codeName+"_"+ request.getParameter("date")+".pdf";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			
			UtilityDAO.IncDedRpt(date, filePath,imagepath,code,table);
			try
			{
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
			catch ( Exception e) 
			{
				e.printStackTrace();
			}
		} 	
		else if (action.equalsIgnoreCase("leaveReport")) 
		{
			String frmdate = request.getParameter("fromdate");
			String todate = request.getParameter("todate");
			String empbrn = request.getParameter("empbrn");
			int days = Integer.parseInt(request.getParameter("days"));
			String filePath = getServletContext().getRealPath("")+ File.separator + "leaveReport.txt";
			UtilityDAO.Leave_report(frmdate, todate, empbrn, days, filePath);
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
		else if (action.equalsIgnoreCase("Incometaxreport")) 
		{

			String date = request.getParameter("date");
			date="01-"+date;
			String sheet = request.getParameter("sheet");
			String list=request.getParameter("list");
			
			/*String frmEMPNO = request.getParameter("EMPNO");
			StringTokenizer st = new StringTokenizer(frmEMPNO, ":");
			while (st.hasMoreTokens()) 
			{
				frmEMPNO = st.nextToken();
			}
			int FromEMPNO = Integer.parseInt(frmEMPNO);
			String toEMPNO = request.getParameter("EMPNO1");
			StringTokenizer st1 = new StringTokenizer(toEMPNO, ":");
			while (st1.hasMoreTokens()) 
			{
				toEMPNO = st1.nextToken();
			}
			int ToEMPNO = Integer.parseInt(toEMPNO);*/
			String filePath = getServletContext().getRealPath("")+ File.separator + "IncomeTax"+sheet+".pdf";
			if(!sheet.equalsIgnoreCase("yearlyearning"))
			{
					YearlyPDFReport.Incometax_Report(list, date, sheet,filePath);
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
				System.out.println("only inserting records.........yearlyearning....");
				YearlyPDFReport.Incometax_Report(list, date, sheet,filePath);
				response.sendRedirect("taxSheet.jsp");
			}
		
		}
		else if(action.equalsIgnoreCase("ANN_12BAreport"))
		{
			String date = request.getParameter("date");
			String frmEMPNO = request.getParameter("EMPNO");
			StringTokenizer st = new StringTokenizer(frmEMPNO, ":");
			while (st.hasMoreTokens()) 
			{
				frmEMPNO = st.nextToken();
			}
			int FromEMPNO = Integer.parseInt(frmEMPNO);
			String toEMPNO = request.getParameter("EMPNO1");
			StringTokenizer st1 = new StringTokenizer(toEMPNO, ":");
			while (st1.hasMoreTokens()) 
			{
				toEMPNO = st1.nextToken();
			}
			int ToEMPNO = Integer.parseInt(toEMPNO);
			String filePath = getServletContext().getRealPath("")+ File.separator + "ANN_12BA_Report.txt";
			TaxCalculation.ANN_12BA(FromEMPNO, ToEMPNO, date, filePath);
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
		else if(action.equalsIgnoreCase("pform24_report"))
		{
			String date = request.getParameter("date");
			date="01-"+date;
			String frmEMPNO = request.getParameter("EMPNO");
			StringTokenizer st = new StringTokenizer(frmEMPNO, ":");
			while (st.hasMoreTokens()) 
			{
				frmEMPNO = st.nextToken();
			}
			int FromEMPNO = Integer.parseInt(frmEMPNO);
			String toEMPNO = request.getParameter("EMPNO1");
			StringTokenizer st1 = new StringTokenizer(toEMPNO, ":");
			while (st1.hasMoreTokens()) 
			{
				toEMPNO = st1.nextToken();
			}
			int ToEMPNO = Integer.parseInt(toEMPNO);
			String filePath = getServletContext().getRealPath("")+ File.separator + "pform24Report.txt";
			TaxCalculation.pform24(FromEMPNO, ToEMPNO, date,filePath);
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
			response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
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
		else if(action.equalsIgnoreCase("bankStmnt"))
		{
			String date=request.getParameter("date");
			
			String  batchNumberString=(request.getParameter("batchNumber")==null?"0":request.getParameter("batchNumber"));
			int Fromrange=request.getParameter("fromRange")==null?0:Integer.parseInt(request.getParameter("fromRange"));
			
			int Torange=request.getParameter("toRange")==null?0:Integer.parseInt(request.getParameter("toRange"));
			int batchNumber=0;
			System.out.println("batchNumberString  "+batchNumberString); 
			if(!batchNumberString.equals(""))
			batchNumber=Integer.parseInt(batchNumberString);
             String type=request.getParameter("type");
             System.out.println("type is"+type);
             System.out.println("batchnumber inside report servlet is "+batchNumber);
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + "Bank_Statment.pdf";
			File file = new File(filePath);
			if (file.exists()) 
			{
				file.delete();
			}
			UtilityDAO.BankStatement(date, imagepath, filePath,batchNumber,type,Fromrange,Torange);
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write("<iframe scrolling=\"auto\" src=\"Bank_Statment.pdf\" height=\"400px\" width=\"100%\"></iframe>");
			
		
		}
		
		/*else if (action.equalsIgnoreCase("pflist")) 
		{
			System.out.println("in pl list");
			String date = request.getParameter("date");
			
			String date1 = request.getParameter("date1");
			String type = request.getParameter("type");
			String filePath = getServletContext().getRealPath("")+ File.separator + "EmpList.txt";
			String filePath1 = getServletContext().getRealPath("")+ File.separator + "emplist.pdf";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			if(!(type.equalsIgnoreCase("J")||type.equalsIgnoreCase("L")||type.equalsIgnoreCase("C")))
			{
				UtilityDAO.EMPLIST(date,date1 ,type, filePath,imagepath);
			}
			else
			{
				UtilityDAO.EMPLIST(date,date1,type, filePath1,imagepath);
			}
			try
			{
				
				if(!(type.equalsIgnoreCase("J")||type.equalsIgnoreCase("L")||type.equalsIgnoreCase("C")))
				{
				Document doc  = new Document();
				PdfWriter.getInstance(doc, new FileOutputStream(filePath1));
				BufferedReader inp = new BufferedReader(new FileReader(filePath));
				String line;
				doc.setPageSize(PageSize.A4.rotate());
				
				doc.open();
				while((line = inp.readLine())!=null)
				{
					if(line.contains("AUTHORISED"))
					{
						doc.add(Chunk.NEWLINE);
						//doc.add(Chunk.NEWLINE);
					}
			
					Paragraph ph = new Paragraph(line,new Font(Font.COURIER,9));
					doc.add(ph);
					if(line.contains("P&A Dept. H.O"))
					{
						doc.newPage();
					}
				}
				doc.close();
				inp.close();
				}
				final int BUFSIZE = 4096;
				File file = new File(filePath1);
				int length = 0;
				ServletOutputStream outStream = response.getOutputStream();
				ServletContext context = getServletConfig().getServletContext();
				String mimetype = context.getMimeType(filePath1);
				if (mimetype == null) 
				{
					mimetype = "application/octet-stream";
				}
				response.setContentType(mimetype);
				response.setContentLength((int) file.length());
				String fileName = (new File(filePath1)).getName();
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
			catch ( Exception e) 
			{
				e.printStackTrace();
			}
		}	*/
		else if (action.equalsIgnoreCase("salaryreg")) 
		{
			final int BUFSIZE = 4096;
			System.out.println("report printing ");
			String date = request.getParameter("date");
			String Desgn = request.getParameter("type");
			System.out.println("date is "+date);
			System.out.println("desgn id "+Desgn);
			String filePath = getServletContext().getRealPath("")+ File.separator + "SalaryRegister.pdf";
			try {
				
				SalaryRegisterDAO.getSalRegister(date, Desgn,filePath);
				
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
			} catch (Exception e) {
				
				e.printStackTrace();
			}	
		}
		else if (action.equalsIgnoreCase("newpayreg")) 
		{
			System.out.println("in new pay reg serv");
			String date = request.getParameter("date");
			String type = request.getParameter("typ");
			String format=request.getParameter("frmt");
			String Reportformat=request.getParameter("reptype");
			System.out.println(date);
			
			/*System.out.println("tt "+type);*/
			String filename="";
			
			if(format.equalsIgnoreCase("txt")){
				filename="NewPayReg.txt";
			}			
			
			else{
				filename=format.equalsIgnoreCase("xls")?"NewPayReg.xls":"NewPayReg.pdf";
			}
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
		
				File file = new File(filePath);
				
				if (file.exists())
				{
					file.delete();
				}
				try
				{
					Document doc  = new Document();
					PdfWriter.getInstance(doc, new FileOutputStream(filePath));
					
				}catch ( Exception e) 
				{
					e.printStackTrace();
				}
			
			if(format.equalsIgnoreCase("xls"))
			{
			ExcelDAO.newpayreg11(date,imagepath, filePath,type);
			}
			else if(format.equalsIgnoreCase("txt"))
			{
				//UtilityDAO.payreg(date,"0", filePath);
				UtilityDAO.payregWithBranchTotal(date,"0", filePath);
					
			}
			
			else{
				//UtilityDAO.newpayreg(date,imagepath, filePath,type);
				UtilityDAO.newpayregwithNewFormat(date,imagepath, filePath,type,Reportformat);
			}
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write("<iframe scrolling=\"auto\" src=\""+filename+"\" height=\"450px\" width=\"100%\"></iframe>");
		} 	
		 
		 
		 /************************************************Prasad****************************************************************/
		 
		else if (action.equalsIgnoreCase("newpayregPF")) 
		{
			System.out.println("in new pay reg serv newpayregPF");
			String date = request.getParameter("date");
			String type = request.getParameter("typ");
			String format=request.getParameter("frmt");
			String Reportformat=request.getParameter("reptype");
			System.out.println(date);
			
			/*System.out.println("tt "+type);*/
			String filename="";
			
			if(format.equalsIgnoreCase("txt")){
				filename="newpayregPF.txt";
			}			
			
			else{
				filename=format.equalsIgnoreCase("xls")?"newpayregPF.xls":"newpayregPF.pdf";
			}
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
		
				File file = new File(filePath);
				
				if (file.exists())
				{
					file.delete();
				}
				try
				{
					Document doc  = new Document();
					PdfWriter.getInstance(doc, new FileOutputStream(filePath));
					
				}catch ( Exception e) 
				{
					e.printStackTrace();
				}
			
			if(format.equalsIgnoreCase("xls"))
			{
			ExcelDAO.newpayreg11PF(date,imagepath, filePath,type);
			}
			else if(format.equalsIgnoreCase("txt"))
			{
				//UtilityDAO.payreg(date,"0", filePath);
				UtilityDAO.payregWithBranchTotalPF(date,"0", filePath);
					
			}
			
			else{
				//UtilityDAO.newpayreg(date,imagepath, filePath,type);
				UtilityDAO.newpayregwithNewFormatPF(date,imagepath, filePath,type,Reportformat);
			}
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write("<iframe scrolling=\"auto\" src=\""+filename+"\" height=\"450px\" width=\"100%\"></iframe>");
		}
		 
		 /************************************************Prasad****************************************************************/
		 
		 
		 
		else if(action.equalsIgnoreCase("newpfreg"))
		{

			System.out.println("in new pay reg serv");
			String date = request.getParameter("date");
			String type = request.getParameter("typ");
			String format=request.getParameter("frmt");
			System.out.println(date);
			
			/*System.out.println("tt "+type);*/
			String filename="";
			
			if(format.equalsIgnoreCase("txt")){
				filename="NewPFReg.txt";
			}			
			
			else{
				filename=format.equalsIgnoreCase("xls")?"NewPFReg.xls":"NewPFReg.pdf";
			}
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
		
				File file = new File(filePath);
				
				if (file.exists())
				{
					file.delete();
				}
				try
				{
					Document doc  = new Document();
					PdfWriter.getInstance(doc, new FileOutputStream(filePath));
					
				}catch ( Exception e) 
				{
					e.printStackTrace();
				}
			
			if(format.equalsIgnoreCase("xls"))
			{
			ExcelDAO.newpayreg11(date,imagepath, filePath,type);
			}
			else if(format.equalsIgnoreCase("txt"))
			{
				//UtilityDAO.payreg(date,"0", filePath);
				UtilityDAO.payregWithBranchTotal(date,"0", filePath);
					
			}
			
			else{
				//UtilityDAO.newpayreg(date,imagepath, filePath,type);
				UtilityDAO.newpayregwithNewFormat(date,imagepath, filePath,type,"");
			}
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write("<iframe scrolling=\"auto\" src=\""+filename+"\" height=\"450px\" width=\"100%\"></iframe>");
		
			
		}
		 
		 
		
		 	
		else if (action.equalsIgnoreCase("Fianlizedpayreg")) 
		{
			System.out.println("in new Fianlizedpayreg reg serv");
			String date = request.getParameter("date");
			String todate = request.getParameter("todate");
			
			System.out.println(date);
			
			
			String filename="";
			
			
					
			
			
			filename="OnHoldList.pdf";

			String imagepath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
		
			
				File file = new File(filePath);
				
				if (file.exists())
				{
					file.delete();
				}
				try
				{
				    
					Document doc  = new Document();
					PdfWriter.getInstance(doc, new FileOutputStream(filePath));
					
				}
				catch ( Exception e) 
				{
					e.printStackTrace();
				}
			
		
				UtilityDAO.Fianlizedpayreg(date,imagepath,filePath,todate);
		
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write("<iframe scrolling=\"auto\" src=\""+filename+"\" height=\"450px\" width=\"100%\"></iframe>");
		} 
		 	
// New ECR Report		 	
		else if(action.equalsIgnoreCase("ecrReportNew"))
		{
			String date = request.getParameter("date");
			date="01-"+date;
			String date1 = request.getParameter("date1");
			date1="01-"+date1;
	
			String filePath1 = getServletContext().getRealPath("")+ File.separator + "ecrReportNew.xls";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			
			ExcelDAO.ecrReportNew(date,date1,filePath1,imagepath);
			try
			{
				final int BUFSIZE = 4096;
				File file = new File(filePath1);
				int length = 0;
				ServletOutputStream outStream = response.getOutputStream();
				ServletContext context = getServletConfig().getServletContext();
				String mimetype = context.getMimeType(filePath1);
				
				if (mimetype == null) 
				{
					mimetype = "application/octet-stream";
				}
				
				response.setContentType(mimetype);
				response.setContentLength((int) file.length());
				String fileName = (new File(filePath1)).getName();
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
			catch ( Exception e) {e.printStackTrace();}
			}
		 
		else if (action.equalsIgnoreCase("sitewisepayreg")) 
		{
			//System.out.println("in new pay reg serv");
			String date = request.getParameter("date");
			int pp =Integer.parseInt(request.getParameter("pp"));
			
			//System.out.println(pp);
			
			/*System.out.println("tt "+type);*/
			String filename="NewPayReg.pdf";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
			
				UtilityDAO.sitewisepayreg(date,imagepath, filePath,pp);
			
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write("<iframe scrolling=\"auto\" src=\""+filename+"\" height=\"450px\" width=\"100%\"></iframe>");
		} 
	}

	
	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{
		String action = request.getParameter("action");
		System.out.println("action="+action);
		
		Properties prop = new Properties();
	  try
	    {
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream("constant.properties");
		prop.load(stream);
	    }catch (Exception e) {System.out.println("Error in constant properties Manager "+e);}
		
		
		if (action.equalsIgnoreCase("leaveLedger")) 
		{
			String list=request.getParameter("list");
			String frmdate = request.getParameter("frmdate");
			frmdate="01-"+frmdate;
			String todate = request.getParameter("todate");
			todate="01-"+todate;
			/*String frmEMPNO = request.getParameter("EMPNO");
			StringTokenizer st = new StringTokenizer(frmEMPNO, ":");
			while (st.hasMoreTokens()) 
			{
				frmEMPNO = st.nextToken();
			}
			int FromEMPNO = Integer.parseInt(frmEMPNO);
			String toEMPNO = request.getParameter("EMPNO1");
			StringTokenizer st1 = new StringTokenizer(toEMPNO, ":");
			while (st1.hasMoreTokens()) 
			{
				toEMPNO = st1.nextToken();
			}
			int ToEMPNO = Integer.parseInt(toEMPNO);*/
			
			String filePath = getServletContext().getRealPath("")+ File.separator + "leaveLedgerReport.pdf";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			UtilityDAO.Leave_Ledger_ReportPDF(list, frmdate, todate,filePath,imagepath);
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
		else if (action.equalsIgnoreCase("castwiseReport")) 
		{
			String date=request.getParameter("date");
			String EmpBRN = request.getParameter("empbrn");
			System.out.println(""+EmpBRN);
			String filePath = getServletContext().getRealPath("")+ File.separator + "CastWiseEmpReport.txt";
			if(EmpBRN.equalsIgnoreCase("C"))
			{
				UtilityDAO.caste_cate_EMPLIST(date, "C", filePath);
			}
			else
			{
				UtilityDAO.caste_cate_EMPLIST(date, "D", filePath);	
			}
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
		else if (action.equalsIgnoreCase("otherList")) 
		{
			String date=request.getParameter("date");
			int trancode=Integer.parseInt(request.getParameter("trancode"));
			String desc=CodeMasterHandler.getCDesc(trancode);
			String empbrn=request.getParameter("empbrn");
			String filePath = getServletContext().getRealPath("")+ File.separator + "OterEmpList.txt";
			UtilityDAO.OtherLIST(date, trancode, desc, empbrn, filePath);
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
	 
		else if(action.equalsIgnoreCase("professionTaxStmnt"))
		{
		
			final int BUFSIZE = 4096;
			String filePath = getServletContext().getRealPath("")+ File.separator + "professiontax.pdf";
			String date=request.getParameter("date");
			date="01-"+date;
			String table_name=request.getParameter("table").equalsIgnoreCase("after")?"after":"before";
			String state=request.getParameter("state");
//System.out.println("-------------------"+table_name);
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			try {
				UtilityDAO.getProfessionTaxStatment(filePath,date,imagepath,table_name,state);
				
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
			} catch (SQLException | DocumentException e) {
				
				e.printStackTrace();
			}
			 
		}
		
	
		else if(action.equalsIgnoreCase("challanTaxStmnt"))
		{
			
			final int BUFSIZE = 4096;
			String filePath = getServletContext().getRealPath("")+ File.separator + "challan.pdf";
			String date=request.getParameter("date2");
			date="01-"+date;
			String table_name=request.getParameter("table").equalsIgnoreCase("after")?"after":"before";
			String states=request.getParameter("states");
//System.out.println("-------------------"+table_name);
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			try {
				UtilityDAO.getChallanTaxStatment(filePath,date,imagepath,table_name,states);
				
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
			} catch (SQLException | DocumentException e) {
				
				e.printStackTrace();
			}
			 
		}
		
		
		
		
		
		else if(action.equalsIgnoreCase("getSalDetails"))
		{
			final int BUFSIZE = 4096;
			YearlyPDFReport yearly = new YearlyPDFReport();
			String date = request.getParameter("date");
			date="01-"+date;
			String list=request.getParameter("list");
			System.out.println("action==="+request.getParameter("action"));
			System.out.println("date==="+request.getParameter("date"));
			System.out.println("list==="+request.getParameter("list"));
			
			String imgpath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + "YearlySalaryReport-"+date+".pdf";
			try {
				
				yearly.printReport(list, date, imgpath, filePath);
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
			} catch (Exception e) {
				//UsrHandler.senderrormail(e,"ReportServlet.getSalDetails");
				e.printStackTrace();
			}
		}
		else if(action.equalsIgnoreCase("getCtcReport"))
		{
			final int BUFSIZE = 4096;
			YearlyPDFReport yearly = new YearlyPDFReport();
			String list=request.getParameter("list");
			String date = "01-Jun-"+request.getParameter("date");
			System.out.println("DATE"+date);
			String imgpath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + "YearlyCTCReport_"+date+".pdf";
			try {
				yearly.printCtcReport(list, date, imgpath, filePath);
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
			} catch (Exception e) {
				//UsrHandler.senderrormail(e,"ReportServlet.getSalDetails");
				e.printStackTrace();
			}
		}
		else if(action.equalsIgnoreCase("form10"))
		{
			String filePath = getServletContext().getRealPath("")+ File.separator + "Form10.pdf";
			String date=request.getParameter("date");
			date="01-"+date;
			String date1=request.getParameter("date1");
			date1="01-"+date1;
			String imagepath =getServletContext().getRealPath("/images/logo.png");
		
			try {
				UtilityDAO.getForm10(filePath,date,date1,imagepath);
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
			catch ( Exception e) 
			{
				e.printStackTrace();
			}
		
			
			
			
			 
		}
		else if(action.equalsIgnoreCase("form12A"))
		{
			final int BUFSIZE = 4096;
			String fromdate=request.getParameter("frmdate");
			String todate=request.getParameter("todate");
			//String emp = request.getParameter("EMPNO");
			//int empno = Integer.parseInt(emp.split(":")[1].trim());
			String filePath = getServletContext().getRealPath("")+ File.separator + "FORM-12A.pdf";
			try {
				//UtilityDAO.getProfessionTaxStatment(filePath,date,imagepath);
				Form12A.getForm12A(fromdate, todate, filePath);
				
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
			}catch (Exception e) {	
				e.printStackTrace();
			}
		}
		
		else if(action.equalsIgnoreCase("form6A"))
		{
			final int BUFSIZE = 4096;
			String fromdate=request.getParameter("frmdate");
			String todate=request.getParameter("todate");
			//String emp = request.getParameter("EMPNO");
			//int empno = Integer.parseInt(emp.split(":")[1].trim());
			String imagepath =getServletContext().getRealPath("/images/Form6A.jpg");
			String filePath = getServletContext().getRealPath("")+ File.separator + "FORM-6A.pdf";
			try {
				Form6A.getForm6A(filePath,fromdate,todate,imagepath);
				
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
			}catch (Exception e) {	
				e.printStackTrace();
			}
		}
		
		else if(action.equalsIgnoreCase("esiclist"))
		{
			final int BUFSIZE = 4096;
			
			String date=request.getParameter("date");
			date="01-"+date;
			String table_name=request.getParameter("before")==null?"after":"before";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
             //
			//String date = request.getParameter("date");
			//date="01-"+date;
			String date1 = request.getParameter("date1");
			date1="01-"+date1;
			String type = request.getParameter("type");
			//String filePath = getServletContext().getRealPath("")+ File.separator + "EmpList.txt";
			//String filePath1 = getServletContext().getRealPath("")+ File.separator + "PF_List.pdf";
			//String imagepath =getServletContext().getRealPath("/images/logo.png");
			// String table_name=request.getParameter("before")==null?"after":"before";
			if((type.equalsIgnoreCase("general")))
			{
				String filepath=getServletContext().getRealPath("")+ File.separator + "esic_List.xls";
			 ExcelDAO.ESIClistexcel(date,type, filepath,imagepath);
			 try
				{
				
					
					File file = new File(filepath);
					int length = 0;
					ServletOutputStream outStream = response.getOutputStream();
					ServletContext context = getServletConfig().getServletContext();
					String mimetype = context.getMimeType(filepath);
					if (mimetype == null) 
					{
						mimetype = "application/octet-stream";
					}
					response.setContentType(mimetype);
					response.setContentLength((int) file.length());
					String fileName = (new File(filepath)).getName();
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
				catch ( Exception e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
			//
			try {
				//UtilityDAO.getESICLIST(filePath,date,imagepath,table_name);
				String filePath = getServletContext().getRealPath("")+ File.separator + "esiclist.pdf";
				UtilityDAO.getESICLISTNEW(filePath,date,imagepath,table_name);
				
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
			} catch (SQLException | DocumentException e) {
				
				e.printStackTrace();
			}
			}
			 
		}
		else if(action.equalsIgnoreCase("from3a"))
		{
			final int BUFSIZE = 4096;
			String fromdate=request.getParameter("frmdate");
			fromdate="01-"+fromdate;
			String todate=request.getParameter("todate");
			todate="01-"+todate;
			String emp = request.getParameter("EMPNO");
			int empno = Integer.parseInt(emp.split(":")[2].trim());
			String filePath = getServletContext().getRealPath("")+ File.separator + "FORM3A-"+empno+".pdf";
			try {
				//UtilityDAO.getProfessionTaxStatment(filePath,date,imagepath);
				Form3A.getForm3A(fromdate, todate, empno, filePath);
				
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
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		
		else if (action.equalsIgnoreCase("travelreport")) 
		{
			System.out.println("report printing ");
			String trcode = request.getParameter("trvCode");
			String EMPNO = request.getParameter("EMPNO");
			String EMPNO1 = request.getParameter("EMPNO1");
			
			
			System.out.println("emp no "+EMPNO);
			System.out.println("name "+EMPNO1);
			System.out.println("travel code "+trcode);
			int emp = Integer.parseInt(EMPNO);
			int trc = Integer.parseInt(trcode);
			
			String filePath = getServletContext().getRealPath("")+ File.separator + "TravelReport.pdf";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			
			TravelReportDAO.PDFReport(emp, trc,filePath, imagepath);
			
			
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
		
		else if(action.equalsIgnoreCase("newAttendSheet"))
		{

			System.out.println("in new Attendantsheet  serv");
			String date = request.getParameter("date");
			String[] parts = date.split("-");
			String part1 = parts[0]; // 004
			String part2 = parts[1];
			date="01-"+date; 
			
			
			String filename="";
			filename=part1+"_"+part2+"_AttendanceReport.pdf";
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			
			UtilityDAO.newAttendSheet(date,imagepath, filePath);	
			
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
		
		
		else if (action.equalsIgnoreCase("pfchallan")) 
		{
		System.out.println("in pl list");
		String date = request.getParameter("date");
		date="01-"+date;
		String type = request.getParameter("type");
		String table=request.getParameter("before")==null?"after":"before";
		
		String filePath1 = getServletContext().getRealPath("")+ File.separator + "pfchallan.pdf";
		String imagepath =getServletContext().getRealPath("/images/logo.png");
		/*if(!(type.equalsIgnoreCase("J")||type.equalsIgnoreCase("L")||type.equalsIgnoreCase("C")))
		{
			PfChallanDAO.PfChallan(date, type, filePath,imagepath);
		}
		else
		{
			PfChallanDAO.PfChallan(date, type, filePath1,imagepath);
		}*/
		
		PfChallanDAO.PfChallanNew(date, type, filePath1,imagepath,table);
		
			final int BUFSIZE = 4096;
			File file = new File(filePath1);
			int length = 0;
			ServletOutputStream outStream = response.getOutputStream();
			ServletContext context = getServletConfig().getServletContext();
			String mimetype = context.getMimeType(filePath1);
			if (mimetype == null) 
			{
				mimetype = "application/octet-stream";
			}
			response.setContentType(mimetype);
			response.setContentLength((int) file.length());
			String fileName = (new File(filePath1)).getName();
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
			
		else if(action.equalsIgnoreCase("esicchallan"))
		{
			final int BUFSIZE = 4096;
			String fromdate=request.getParameter("frmdate");
			fromdate="01-"+fromdate;
			String table_name=request.getParameter("before")==null?"after":"before";
		//	String todate=request.getParameter("todate");
			//String emp = request.getParameter("EMPNO");
			//int empno = Integer.parseInt(emp.split(":")[1].trim());
			String filePath = getServletContext().getRealPath("")+ File.separator + "esicchallan.pdf";
			try {
				//UtilityDAO.getProfessionTaxStatment(filePath,date,imagepath);
				ESICChallanDAO.getesicchallan(fromdate, filePath,table_name);
				
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
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		else if(action.equalsIgnoreCase("salarycerti"))
		{
			final int BUFSIZE = 4096;
			String fromdate=request.getParameter("frmdate");
			String todate=request.getParameter("todate");
			String emp = request.getParameter("EMPNO");
			int empno = Integer.parseInt(emp.split(":")[2].trim());
			String filePath = getServletContext().getRealPath("")+ File.separator + "SalaryCerti-"+empno+".pdf";
			try {
				
				SalaryCertificateDAO.getCertificate(fromdate, todate, empno, filePath);
				
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
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		else if(action.equalsIgnoreCase("ECR"))
		{
			
			final int BUFSIZE = 4096;
			String filePath = getServletContext().getRealPath("")+ File.separator + "ECR-Report.pdf";
			String date=request.getParameter("date");

			try {
				System.out.println("----------------------ECR-----------------------");
				UtilityDAO.EcrReport(date,filePath,"before");
				
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
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			 
		}
		else if(action.equalsIgnoreCase("mlwfchallan"))
		{
			
			final int BUFSIZE = 4096;
			String filePath = getServletContext().getRealPath("")+ File.separator + "Mlwfchallan.pdf";
			String date=request.getParameter("date");
			date="01-"+date;
			String table_name=request.getParameter("before")==null?"after":"before";

			String imagepath =getServletContext().getRealPath("/images/logo.png");
			try {
				UtilityDAO.getMlwfchallan(filePath,date,imagepath,table_name);
				
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
			} catch (SQLException | DocumentException e) {
				
				e.printStackTrace();
			}
			 
		}
		
		
		else if(action.equalsIgnoreCase("reliffund"))
		{
			final int BUFSIZE = 4096;
			String filePath = getServletContext().getRealPath("")+ File.separator + "reliffund.pdf";
			String date=request.getParameter("date");
			String table_name=request.getParameter("before")==null?"after":"before";
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			try {
				UtilityDAO.getRelif_Fund(filePath,date,imagepath,table_name);
				
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
			} catch (SQLException | DocumentException e) {
				
				e.printStackTrace();
			}
			 
		}
		else if(action.equalsIgnoreCase("form5"))
		{
			
			final int BUFSIZE = 4096;
			String filePath = getServletContext().getRealPath("")+ File.separator + "Form5.pdf";
			String date=request.getParameter("date");
			date="01-"+date;
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			try {
				UtilityDAO.getForm5(filePath,date,imagepath);
				
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
			} catch (SQLException | DocumentException e) {
				
				e.printStackTrace();
			}
			 
		}
		
		else if(action.equalsIgnoreCase("getBonusReport"))
		{
			
			String date = "01-Jun-"+request.getParameter("date");
			String reporttype = request.getParameter("reporttype");
			String date1= "01-"+request.getParameter("date1")==null?"":"01-"+request.getParameter("date1");
			String applicable=request.getParameter("applicable")==null?"":request.getParameter("applicable");
			String status=request.getParameter("act");//==null?"nonactive":"active";
			//System.out.println("DATE"+date);
			//System.out.println("type"+reporttype);
			//System.out.println("month********"+date1);
			//System.out.println("applicable********"+applicable);
			//System.out.println("status***99999999999999999****"+status);

			if(reporttype.equalsIgnoreCase("pdf")){
			final int BUFSIZE = 4096;
			YearlyPDFReport yearly = new YearlyPDFReport();
			
			String imgpath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + "YearlyBonusReport_"+date+".pdf";
			try {
				HttpSession session = request.getSession();
				int eno = (Integer)session.getAttribute("EMPNO");
				
				
				yearly.printBonusReport( date,date1,applicable,imgpath, filePath, status,eno);
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
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
			if(reporttype.equalsIgnoreCase("excel"))
			{
				try
				{
					if(status.equalsIgnoreCase("active")){
						status="A";
					}
					else
					{
						status="N";
					}
					//System.out.println("*****"+status);
					Connection cn = ConnectionManager.getConnection();
					Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					
					ResultSet rs = null;
					SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
					
					HSSFWorkbook hwb=new HSSFWorkbook();
					HSSFSheet sheet =  hwb.createSheet("Bonus Report");
					 sheet.createFreezePane( 0, 11, 0, 11 );
					
					    HSSFCellStyle my_style = hwb.createCellStyle();
		                HSSFFont my_font=hwb.createFont();
		                my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		                my_style.setFont(my_font);

		
						
						HSSFRow rowtitle=   sheet.createRow((short)0);
						HSSFCell cell = rowtitle.createCell((short) 7);
						cell.setCellValue(prop.getProperty("companyName"));
						cell.setCellStyle(my_style);
						HSSFRow rowtitle1=   sheet.createRow((short)1);
						HSSFCell cell1 = rowtitle1.createCell((short) 5);
						cell1.setCellValue(prop.getProperty("addressForReport"));
						cell1.setCellStyle(my_style);
						HSSFRow rowtitle2=   sheet.createRow((short)2);
						HSSFCell cell2 = rowtitle2.createCell((short) 7);
						cell2.setCellValue(prop.getProperty("contactForReport"));
						cell2.setCellStyle(my_style);
						HSSFRow rowtitle3=   sheet.createRow((short)3);
						HSSFCell cell3 = rowtitle3.createCell((short) 7);
						cell3.setCellValue(prop.getProperty("mailForReport"));
						cell3.setCellStyle(my_style);
						HSSFRow rowtitle4=   sheet.createRow((short)4);
						HSSFCell cell4 = rowtitle4.createCell((short) 5);
						cell4.setCellValue("Employee's Yearly Bonus Report for Financial Year ("+ReportDAO.BoFinancialy(date).substring(7)+" - "+ReportDAO.EoFinancialy(date).substring(7));
						cell4.setCellStyle(my_style);
						
						
						
						sheet.setColumnWidth((short)0, (short)3000);
						sheet.setColumnWidth((short)1, (short)3000);
						sheet.setColumnWidth((short)2, (short)7000);
						sheet.setColumnWidth((short)3, (short)4000);
						sheet.setColumnWidth((short)4, (short)4000);
						sheet.setColumnWidth((short)5, (short)4000);
						sheet.setColumnWidth((short)6, (short)4000);
						sheet.setColumnWidth((short)7, (short)4000);
						sheet.setColumnWidth((short)8, (short)4000);
						sheet.setColumnWidth((short)9, (short)4000);
						sheet.setColumnWidth((short)10, (short)4000);
						sheet.setColumnWidth((short)11, (short)4000);
						sheet.setColumnWidth((short)12, (short)4000);
						sheet.setColumnWidth((short)13, (short)4000);
						sheet.setColumnWidth((short)14, (short)4000);
						sheet.setColumnWidth((short)15, (short)4000);
						sheet.setColumnWidth((short)16, (short)4000);
						sheet.setColumnWidth((short)17, (short)6000);
						sheet.setColumnWidth((short)18, (short)4000);
						
						HSSFRow head1=   sheet.createRow((short)9);
						head1.createCell((short) 0).setCellValue("");
						HSSFRow rowhead=   sheet.createRow((short)10);
						rowhead.createCell((short) 0).setCellValue("Sr No.");
						rowhead.createCell((short) 1).setCellValue("Emp Code");
						rowhead.createCell((short) 2).setCellValue("Employee Name");
						rowhead.createCell((short) 3).setCellValue("DOJ");
						rowhead.createCell((short) 4).setCellValue("DOL");
						rowhead.createCell((short) 5).setCellValue("Apr");
						rowhead.createCell((short) 6).setCellValue("May");
						rowhead.createCell((short) 7).setCellValue("Jun");
						rowhead.createCell((short) 8).setCellValue("Jul");
						rowhead.createCell((short) 9).setCellValue("Aug");
						rowhead.createCell((short) 10).setCellValue("sep");
						rowhead.createCell((short) 11).setCellValue("oct");
						rowhead.createCell((short) 12).setCellValue("Nov");
						rowhead.createCell((short) 13).setCellValue("Dec");
						rowhead.createCell((short) 14).setCellValue("Jan");
						rowhead.createCell((short) 15).setCellValue("Feb");
						rowhead.createCell((short) 16).setCellValue("Mar");
						rowhead.createCell((short) 17).setCellValue("Total");
						rowhead.createCell((short) 18).setCellValue("Bouns");

						
						int i=11;

						int srno=1; 
						  double totatlbonus=0;
					
							
					String sql2="with bonus as(select y.trncd,disc,e.empno, sum(case datepart(MM,trndt) when 04 then NET_AMT else  0 end) apr, " +
							"sum(case datepart(MM,trndt) when 05 then NET_AMT else  0 end) may, " +
							"sum(case datepart(MM,trndt) when 06 then NET_AMT else  0 end) jun, " +
							"sum(case datepart(MM,trndt) when 07 then NET_AMT else  0 end) jul, " +
							"sum(case datepart(MM,trndt) when 08 then NET_AMT else  0 end) aug, " +
							"sum(case datepart(MM,trndt) when 09 then NET_AMT else  0 end) sep, " +
							"sum(case datepart(MM,trndt) when 10 then NET_AMT else  0 end) oct, " +
							"sum(case datepart(MM,trndt) when 11 then NET_AMT else  0 end) nov, " +
							"sum(case datepart(MM,trndt) when 12 then NET_AMT else  0 end) dec, " +
							"sum(case datepart(MM,trndt) when 01 then NET_AMT else  0 end) jan, " +
							"sum(case datepart(MM,trndt) when 02 then NET_AMT else  0 end) feb, " +
							"sum(case datepart(MM,trndt) when 03 then NET_AMT else  0 end) mar  " +
							"from empmast e, cdmast c, ytdtran y where e.empno = y.empno " +
							"and y.trncd = c.trncd and e.status='"+status+"' and y.trndt between '"+ReportDAO.BoFinancialy(date)+"' and '"+ReportDAO.EoFinancialy(date)+"' " +
							"and y.trncd in(select onamtcd from onamt where trncd=138) and c.trncd in(select onamtcd from onamt where trncd=138) " +
							"group by  e.empno,fname,lname,disc,y.trncd ) " +
							"select b.empno,rtrim(e.fname)+' '+rtrim(e.lname) name,E.DOJ,E.DOL,Convert(INT,e.empcode) as empcode,SUM (b.apr) as apramt ," +
							"SUM (b.may) as mayamt, SUM (b.jun) as junamt, SUM (b.jul) as julamt,SUM (b.aug) as augamt, " +
							"SUM (b.sep) as sepamt ,SUM (b.oct) as octamt ,SUM (b.nov) as novamt ,SUM (b.dec) as decamt , " +
							"SUM (b.jan) as janamt,SUM (b.feb) as febamt ,SUM (b.mar) as maramt, " +
							"SUM(b.apr+b.may+b.jun+b.jul+b.aug+b.sep+b.oct+b.nov+b.dec+b.jan+b.feb+b.mar) as grand_total, " +
							"((SUM(b.apr+b.may+b.jun+b.jul+b.aug+b.sep+b.oct+b.nov+b.dec+b.jan+b.feb+b.mar))/100*( select per from slab where trncd=138)) as bonus_amt " +
							"from bonus b ,EMPMAST E where b.EMPNO=E.empno group by b.empno,E.fname,E.lname,E.DOJ,E.DOL,empcode order by empcode "; 
							
							rs = st.executeQuery(sql2);
							 System.out.println("excel quary   "+sql2);
								while(rs.next())
								{
									
									HSSFRow row = sheet.createRow((short)i++);
						       
									row.createCell((short) 0).setCellValue(""+srno);
									
									row.createCell((short) 1).setCellValue(""+rs.getString("empcode"));
									
									row.createCell((short) 2).setCellValue(""+rs.getString("name"));
									
									row.createCell((short) 3).setCellValue(""+rs.getDate("DOJ")==null?"":format.format(rs.getDate("DOJ")));

						            String dol=  rs.getString("DOL")==null || rs.getString("DOL")==""?"31-Dec-2099":format.format(rs.getDate("DOL"));
						            
						            dol=dol.equalsIgnoreCase("31-Dec-2099")?"":dol;

									row.createCell((short) 4).setCellValue(dol);
									row.createCell((short) 5).setCellValue(rs.getString("apramt"));
									row.createCell((short) 6).setCellValue(rs.getString("mayamt"));
									row.createCell((short) 7).setCellValue(rs.getString("junamt"));
									row.createCell((short) 8).setCellValue(rs.getString("julamt"));
									row.createCell((short) 9).setCellValue(rs.getString("augamt"));
									row.createCell((short) 10).setCellValue(rs.getString("sepamt"));
									row.createCell((short) 11).setCellValue(rs.getString("octamt"));
									row.createCell((short) 12).setCellValue(rs.getString("novamt"));
									row.createCell((short) 13).setCellValue(rs.getString("decamt"));
									row.createCell((short) 14).setCellValue(rs.getString("janamt"));
									row.createCell((short) 15).setCellValue(rs.getString("febamt"));
									row.createCell((short) 16).setCellValue(rs.getString("maramt"));
									row.createCell((short) 17).setCellValue(rs.getString("grand_total"));
									row.createCell((short) 18).setCellValue( Math.round(rs.getInt("bonus_amt")));
								
									 totatlbonus=totatlbonus + Math.round(rs.getInt("bonus_amt"));
									srno++;				
				}                
								HSSFRow row = sheet.createRow((short)i++);
								row.createCell((short) 17).setCellValue("TOTAL:-");
								row.createCell((short) 18).setCellValue(""+totatlbonus);
								String filePath1 = getServletContext().getRealPath("")+ File.separator + "YearlyBonusReport_"+date+".xls";
								FileOutputStream fileOut =  new FileOutputStream(filePath1);//"D:\\PFList.xls"
								hwb.write(fileOut);
								fileOut.close();
								final int BUFSIZE = 4096;
								File file = new File(filePath1);
								int length = 0;
								ServletOutputStream outStream = response.getOutputStream();
								ServletContext context = getServletConfig().getServletContext();
								String mimetype = context.getMimeType(filePath1);
								if (mimetype == null) 
								{
									mimetype = "application/octet-stream";
								}
								response.setContentType(mimetype);
								response.setContentLength((int) file.length());
								String fileName = (new File(filePath1)).getName();
								response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
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
			
				catch (Exception ex ) 
				{
					ex.printStackTrace();
				}
			
		   }
}
		
		else if(action.equalsIgnoreCase("CtcReportNew"))
		{
			
			System.out.println("Inside CtcReportNew");
			final int BUFSIZE = 4096;
			YearlyPDFReport yearly = new YearlyPDFReport();
			String list=request.getParameter("list");
			String date = request.getParameter("date");
			System.out.println("DATE"+date);
			String Format= request.getParameter("format");
			
			System.out.println("format  "+Format);
			String imgpath =getServletContext().getRealPath("/images/logo.png");
			String filePath="";
			
			 try {
				 if(Format.equalsIgnoreCase("Pdf")){
					filePath = getServletContext().getRealPath("")+ File.separator + "CTCReport_"+date+".pdf";
				yearly.CtcReportNew(list, date, imgpath, filePath);
				
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
				
				 // for excel report
				 else{
					 
					 DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
					 
					  Calendar calobj = Calendar.getInstance();
				       //System.out.println(df.format(calobj.getTime()));
				       
						Connection cn = ConnectionManager.getConnection();
						Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
						
						ResultSet rs = null;
						SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
						
						
						System.out.println( filePath);
						
						HSSFWorkbook hwb=new HSSFWorkbook();
						HSSFSheet sheet =  hwb.createSheet("CTC_REPORT");
						
					
						sheet.setColumnWidth((short)0, (short)3000);
						sheet.setColumnWidth((short)1, (short)3000);
						sheet.setColumnWidth((short)2, (short)7000);
						sheet.setColumnWidth((short)3, (short)4000);
						sheet.setColumnWidth((short)4, (short)4000);
						sheet.setColumnWidth((short)5, (short)4000);
						sheet.setColumnWidth((short)6, (short)4000);
						sheet.setColumnWidth((short)7, (short)4000);
						sheet.setColumnWidth((short)8, (short)4000);
						sheet.setColumnWidth((short)9, (short)4000);
						sheet.setColumnWidth((short)10, (short)4000);
						sheet.setColumnWidth((short)11, (short)4000);
						sheet.setColumnWidth((short)12, (short)4000);
						sheet.setColumnWidth((short)13, (short)4000);
						sheet.setColumnWidth((short)14, (short)4000);
						sheet.setColumnWidth((short)15, (short)4000);
						sheet.setColumnWidth((short)16, (short)4000);
						sheet.setColumnWidth((short)17, (short)4000);
						




				        HSSFCellStyle my_style = hwb.createCellStyle();
				        HSSFCellStyle my_style1 = hwb.createCellStyle();

				        HSSFFont my_font=hwb.createFont();
				        my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				        my_style.setFont(my_font);
				      
				        
				        HSSFRow rowtitle=   sheet.createRow((short)0);
						HSSFCell cell = rowtitle.createCell((short) 7);
						cell.setCellValue(prop.getProperty("companyName"));
						cell.setCellStyle(my_style);
						HSSFRow rowtitle1=   sheet.createRow((short)1);
						HSSFCell cell1 = rowtitle1.createCell((short) 5);
						cell1.setCellValue("  "+prop.getProperty("addressForReport"));
						cell1.setCellStyle(my_style);
						HSSFRow rowtitle2=   sheet.createRow((short)2);
						HSSFCell cell2 = rowtitle2.createCell((short) 7);
						cell2.setCellValue(prop.getProperty("contactForReport"));
						cell2.setCellStyle(my_style);
						HSSFRow rowtitle3=   sheet.createRow((short)3);
						HSSFCell cell3 = rowtitle3.createCell((short) 7);
						cell3.setCellValue("    "+prop.getProperty("mailForReport"));
						cell3.setCellStyle(my_style);
						HSSFRow rowtitle4=   sheet.createRow((short)5);
						HSSFCell cell5 = rowtitle4.createCell((short) 6);
						cell5.setCellValue("          Employee's CTC Report for Employees "+ReportDAO.getSysDate());
						cell5.setCellStyle(my_style);
					
								
						HSSFRow rowtitle5=   sheet.createRow((short)4);
						cell2=rowtitle5.createCell((short) 2);
						cell2.setCellValue("");
						cell2.setCellStyle(my_style);
						HSSFRow rowtitle6=   sheet.createRow((short)5);
						rowtitle6.createCell((short) 0).setCellValue("");
						HSSFRow rowtitle7=   sheet.createRow((short)6);
						rowtitle7.createCell((short) 0).setCellValue("");
						
						HSSFFont blueFont = hwb.createFont();
						blueFont.setColor(HSSFColor.BLUE.index);
						
						HSSFCellStyle style = hwb.createCellStyle();
						//style.setFont(blueFont);
						style.setFillForegroundColor(HSSFColor.BLUE.index);
						
						
						
						
						HSSFRow head=   sheet.createRow((short)7);
						head.createCell((short) 0).setCellValue("");
						HSSFRow heading=   sheet.createRow((short)8);
						HSSFCell cell4 = heading.createCell((short) 0); 

						cell4.setCellValue("");
						cell4.setCellStyle(my_style1);
						HSSFRow rowhead=   sheet.createRow((short)9);
				        sheet.createFreezePane( 0, 10, 0, 10 );
				       
				        my_style1.setAlignment((short) 2);
				        my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				        my_style1.setFont(my_font);
				        
				        cell4=rowhead.createCell((short) 0);
				        cell4.setCellValue("SR.NO");
				        cell4.setCellStyle(my_style1);
				        cell4=rowhead.createCell((short) 1);
				        cell4.setCellValue("Emp Code");
				        cell4.setCellStyle(my_style1);
				        cell4=rowhead.createCell((short) 2);
				        cell4.setCellValue("Employee Name");
				        cell4.setCellStyle(my_style1);
				        cell4=rowhead.createCell((short) 3);
				        cell4.setCellValue("DOJ");
				        cell4.setCellStyle(my_style1);
				        cell4=rowhead.createCell((short) 4);
				        cell4.setCellValue("Basic");
				        cell4.setCellStyle(my_style1);
				        cell4=rowhead.createCell((short) 5);
				        cell4.setCellValue("Medical");
				        cell4.setCellStyle(my_style1);
				        cell4=rowhead.createCell((short) 6);
				        cell4.setCellValue("HRA");
				        cell4.setCellStyle(my_style1);
				        cell4=rowhead.createCell((short) 7);
				        cell4.setCellValue("Education");
				        cell4.setCellStyle(my_style1);
				        cell4=rowhead.createCell((short)8);
				        cell4.setCellValue("Min Insuarance");
				        cell4.setCellStyle(my_style1);

				        cell4=rowhead.createCell((short) 9);
				        cell4.setCellValue("PF");
				        cell4.setCellStyle(my_style1);
				        cell4=rowhead.createCell((short) 10);
				        cell4.setCellValue("Add Less Amt");
				        cell4.setCellStyle(my_style1);
						
				        cell4=rowhead.createCell((short) 11);
						cell4.setCellValue("Col");
						cell4.setCellStyle(my_style1);
						cell4=rowhead.createCell((short) 12);
						cell4.setCellValue("CONVE");
						cell4.setCellStyle(my_style1);
						cell4=rowhead.createCell((short) 13);
						cell4.setCellValue("SPL ALLOWANCE");
						cell4.setCellStyle(my_style1);
						cell4=rowhead.createCell((short) 14);
						cell4.setCellValue("BONUS");
						cell4.setCellStyle(my_style1);
						cell4=rowhead.createCell((short) 15);
						cell4.setCellValue("PF");
						cell4.setCellStyle(my_style1);
						cell4=rowhead.createCell((short) 16);
						cell4.setCellValue("PT");
						cell4.setCellStyle(my_style1);
						cell4=rowhead.createCell((short) 17);
						cell4.setCellValue("ESIC");
						cell4.setCellStyle(my_style1);
						
						/*HSSFWorkbook hwb=new HSSFWorkbook();
						HSSFSheet sheet =  hwb.createSheet("CTC Report");
						 sheet.createFreezePane( 0, 11, 0, 11 );
						
						    HSSFCellStyle my_style = hwb.createCellStyle();
			                HSSFFont my_font=hwb.createFont();
			                my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			                my_style.setFont(my_font);

			
							
							HSSFRow rowtitle=   sheet.createRow((short)0);
							HSSFCell cell = rowtitle.createCell((short) 7);
							cell.setCellValue("  HARSH CONSTRUCTION PVT.LTD.");
							cell.setCellStyle(my_style);
							HSSFRow rowtitle1=   sheet.createRow((short)1);
							HSSFCell cell1 = rowtitle1.createCell((short) 5);
							cell1.setCellValue("  1 Sanskruti Apartment Murkute Lane, New Pandit Coloney,Gangapur Road,Nashik -422005");
							cell1.setCellStyle(my_style);
							HSSFRow rowtitle2=   sheet.createRow((short)2);
							HSSFCell cell2 = rowtitle2.createCell((short) 7);
							cell2.setCellValue("Tel : 0253-2317143,Fax : 0253-2317621");
							cell2.setCellStyle(my_style);
							HSSFRow rowtitle3=   sheet.createRow((short)3);
							HSSFCell cell3 = rowtitle3.createCell((short) 7);
							cell3.setCellValue("    Email : info@hcpl.co.in");
							cell3.setCellStyle(my_style);
							HSSFRow rowtitle4=   sheet.createRow((short)5);
							HSSFCell cell4 = rowtitle4.createCell((short) 6);
							cell4.setCellValue("          Employee's CTC Report for Employees "+ReportDAO.getSysDate()+" ");
							cell4.setCellStyle(my_style);
							
							
							
							sheet.setColumnWidth((short)0, (short)3000);
							sheet.setColumnWidth((short)1, (short)3000);
							sheet.setColumnWidth((short)2, (short)7000);
							sheet.setColumnWidth((short)3, (short)4000);
							sheet.setColumnWidth((short)4, (short)4000);
							sheet.setColumnWidth((short)5, (short)4000);
							sheet.setColumnWidth((short)6, (short)4000);
							sheet.setColumnWidth((short)7, (short)4000);
							sheet.setColumnWidth((short)8, (short)4000);
							sheet.setColumnWidth((short)9, (short)4000);
							sheet.setColumnWidth((short)10, (short)4000);
							sheet.setColumnWidth((short)11, (short)4000);
							sheet.setColumnWidth((short)12, (short)4000);
							sheet.setColumnWidth((short)13, (short)4000);
							sheet.setColumnWidth((short)14, (short)4000);
							sheet.setColumnWidth((short)15, (short)4000);
							sheet.setColumnWidth((short)16, (short)4000);
							sheet.setColumnWidth((short)17, (short)4000);
							
							HSSFRow head1=   sheet.createRow((short)9);
							head1.createCell((short) 0).setCellValue("");
							HSSFRow rowhead=   sheet.createRow((short)10);
							rowhead.createCell((short) 0).setCellValue("Sr No.");
							rowhead.createCell((short) 1).setCellValue("Emp Code");
							rowhead.createCell((short) 2).setCellValue("Employee Name");
							rowhead.createCell((short) 3).setCellValue("DOJ");
							rowhead.createCell((short) 4).setCellValue("Basic");
							rowhead.createCell((short) 5).setCellValue("Medical");
							rowhead.createCell((short) 6).setCellValue("HRA");
							rowhead.createCell((short) 7).setCellValue("Education");
							rowhead.createCell((short) 8).setCellValue("Min Insuarance");
							rowhead.createCell((short) 9).setCellValue("PF");
							rowhead.createCell((short) 10).setCellValue("Add Less Amt");
							rowhead.createCell((short) 11).setCellValue("col");
							rowhead.createCell((short) 12).setCellValue("CONVE");
							rowhead.createCell((short) 13).setCellValue("SPL ALLOWANCE");
							rowhead.createCell((short) 14).setCellValue("BONUS");
							rowhead.createCell((short) 15).setCellValue("PF");
							rowhead.createCell((short) 16).setCellValue("PT");
							rowhead.createCell((short) 17).setCellValue("ESIC");*/
							
							
							
						/*	String sql1 = "select empno from empmast where empno in ("+list+")";
							Statement st1 = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
							ResultSet rs1 = st1.executeQuery(sql1);
							
							while (rs1.next()){*/
							
							int i=11;

							int srno=1; 
							 
						
								
						String sql2="select e.empcode as empcode, e.doj as DOJ, e.empno as empno,rtrim(e.fname)+' '+rtrim(e.mname)+' '+rtrim(e.lname)as name, " +
								" p1.inp_amt as Basic,p2.inp_amt as Medical,p3.inp_amt as HRA, p4.inp_amt as Edu, " +
								" p5.inp_amt as Min_ins, p6.inp_amt as PF, p7.inp_amt as Add_less, p8.inp_amt as Col, " +
								" p9.inp_amt as Conv, p10.inp_amt as special_allow, p11.inp_amt as Bonus," +
								" p11.pf as pfv, p11.pt as pt, p11.esic as esic from EMPMAST e join ctcdisplay p1 on e.EMPNO=p1.empno " +
								" join  ctcdisplay p2 on e.EMPNO = p2.EMPNO " +
								" join  ctcdisplay p3 on e.EMPNO = p3.EMPNO " +
								" join  ctcdisplay p4 on e.EMPNO = p4.EMPNO " +
								" join  ctcdisplay p5 on e.EMPNO = p5.EMPNO " +
								" join  ctcdisplay p6 on e.EMPNO = p6.EMPNO " +
								" join  ctcdisplay p7 on e.EMPNO = p7.EMPNO " +
								" join  ctcdisplay p8 on e.EMPNO = p8.EMPNO " +
								" join  ctcdisplay p9 on e.EMPNO = p9.EMPNO " +
								" join  ctcdisplay p10 on e.EMPNO =p10.EMPNO " +
								" join  ctcdisplay p11 on e.EMPNO =p11.EMPNO " +
								" where p1.TRNCD = 101 and  p2.trncd=104 and " +
								" p3.trncd=103 and  p4.trncd=105 and " +
								" p5.trncd=126 and  p6.trncd =201 and " +
								" p7.trncd=127 and  p8.trncd=128 and " +
								" p9.trncd=108 and  p10.trncd=107 and" +
								" p11.trncd=135 and e.empno in("+list+") "; 
								
								rs = st.executeQuery(sql2);
								 //System.out.println("excel quary   "+sql2);
								HSSFRow row = sheet.createRow((short)i++);
								while(rs.next())
									{
										System.out.println("excel query   "+sql2);
										 row = sheet.createRow((short)i++);
							       
										row.createCell((short) 0).setCellValue(""+srno);
										
										row.createCell((short) 1).setCellValue(""+rs.getString("empcode"));
										
										row.createCell((short) 2).setCellValue(""+rs.getString("name"));
										
										row.createCell((short) 3).setCellValue(""+rs.getDate("DOJ")==null?"":format.format(rs.getDate("DOJ")));

							       
										row.createCell((short) 4).setCellValue(""+rs.getString("Basic"));
										row.createCell((short) 5).setCellValue(""+rs.getString("Medical"));
										row.createCell((short) 6).setCellValue(""+rs.getString("HRA"));
										row.createCell((short) 7).setCellValue(""+rs.getString("Edu"));
										row.createCell((short) 8).setCellValue(""+rs.getString("Min_ins"));
										row.createCell((short) 9).setCellValue(""+rs.getString("PF"));
										row.createCell((short) 10).setCellValue(""+rs.getString("Add_less"));
										row.createCell((short) 11).setCellValue(""+rs.getString("Col"));
										row.createCell((short) 12).setCellValue(""+rs.getString("Conv"));
										row.createCell((short) 13).setCellValue(""+rs.getString("special_allow"));
										row.createCell((short) 14).setCellValue(""+rs.getString("Bonus"));
										if(rs.getString("pfv").equals("1")){
										row.createCell((short) 15).setCellValue("Applicable");
										}
										else{
											row.createCell((short) 15).setCellValue("Not Applicable");
										}
										
										if(rs.getString("pt").equals("1")){
											row.createCell((short) 16).setCellValue("Applicable");
											}
											else{
												row.createCell((short) 16).setCellValue("Not Applicable");
											}
										
										if(rs.getString("esic").equals("1")){
											row.createCell((short) 17).setCellValue("Applicable");
											}
											else{
												row.createCell((short) 17).setCellValue("Not Applicable");
											}
										//row.createCell((short) 16).setCellValue(rs.getString("pt"));
										//row.createCell((short) 17).setCellValue( rs.getInt("esic"));
									
										
										srno++;				
					}    
								//HSSFRow row = sheet.createRow((short)i++);
							//}				
								    row = sheet.createRow((short) i);
								    
									row.createCell((short) 0).setCellValue(" ");
									 row = sheet.createRow((short) i+1);
									 row.createCell((short) 0).setCellValue("Report Date "+df.format(calobj.getTime()));
									String filePath1 = getServletContext().getRealPath("")+ File.separator + "CTCReport_"+date+".xls";
									FileOutputStream fileOut =  new FileOutputStream(filePath1);//"D:\\PFList.xls"
									hwb.write(fileOut);
									fileOut.close();
									
									File file = new File(filePath1);
									int length = 0;
									ServletOutputStream outStream = response.getOutputStream();
									ServletContext context = getServletConfig().getServletContext();
									String mimetype = context.getMimeType(filePath1);
									if (mimetype == null) 
									{
										mimetype = "application/octet-stream";
									}
									response.setContentType(mimetype);
									response.setContentLength((int) file.length());
									String fileName = (new File(filePath1)).getName();
									response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
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
			
			} catch (Exception e) {
				//UsrHandler.senderrormail(e,"ReportServlet.getSalDetails");
				e.printStackTrace();
			}
		}
		
		
		else if(action.equalsIgnoreCase("tdslist"))
		{

			System.out.println("in new Attendsheet  serv");
			String year = request.getParameter("year");
			String endyear = request.getParameter("endyear");
			year="01-Apr-"+year;
			endyear="31-Mar-"+endyear;
		
			
			
			
			
			String filename="";
			filename="NewTDSReport.pdf";
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			
			UtilityDAO.tdsList(year,endyear,imagepath, filePath);	
			
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
		
		else if(action.equalsIgnoreCase("loanReport"))
		{
			System.out.println("In loanReport 1");
			final int BUFSIZE = 4096;
			
			String emptype = request.getParameter("type");
			String loantype = request.getParameter("loantype");
		//	String sdate = "01-"+request.getParameter("startDate");
		//	String edate = "01-"+request.getParameter("endDate");
			
			//sdate = ReportDAO.BOM(sdate);
		//	edate = ReportDAO.EOM(edate);
			
			String empno = request.getParameter("EMPNO");
			
		/*	System.out.println("emptype"+emptype);
			System.out.println("DATE"+date);
			System.out.println("empno"+empno);
			*/
			String date = ReportDAO.getSysDate();
			String imgpath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + "LoanReport_"+date+".pdf";
			try 
			{
				UtilityDAO.loanReport(empno, emptype, loantype,  imgpath, filePath);	
				
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
			} catch (Exception e) {
				//UsrHandler.senderrormail(e,"ReportServlet.getSalDetails");
				e.printStackTrace();
			}
		}
		
		//Temporory comment for sumrized loan report..jsp is Summerizedloanreport.jsp
/*		else if(action.equalsIgnoreCase("SumloanReport"))
		{

			System.out.println("In SumloanReport 1");
			final int BUFSIZE = 4096;
			
			String emptype = request.getParameter("type");
			String loantype = request.getParameter("loantype");
		//	String sdate = "01-"+request.getParameter("startDate");
		//	String edate = "01-"+request.getParameter("endDate");
			
			//sdate = ReportDAO.BOM(sdate);
		//	edate = ReportDAO.EOM(edate);
			
			String empno = request.getParameter("EMPNO");
			
			System.out.println("emptype"+emptype);
			System.out.println("DATE"+date);
			System.out.println("empno"+empno);
			
			String date = ReportDAO.getSysDate();
			String imgpath =getServletContext().getRealPath("/images/logo.png");
			String filePath = getServletContext().getRealPath("")+ File.separator + "LoanReport_"+date+".pdf";
			try 
			{
				UtilityDAO.sumloanReport(empno, emptype, loantype,  imgpath, filePath);	
				
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
			} catch (Exception e) {
				//UsrHandler.senderrormail(e,"ReportServlet.getSalDetails");
				e.printStackTrace();
			}
		
		}
		
		
*/	}
}
