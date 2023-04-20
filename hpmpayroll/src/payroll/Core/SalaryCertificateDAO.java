package payroll.Core;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import payroll.DAO.ConnectionManager;
import payroll.DAO.LookupHandler;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class SalaryCertificateDAO extends PdfPageEventHelper
{
	 
	public static void getCertificate(String date1,String date2,int empno,String filepath) 
		{
		
		Properties prop = new Properties();
	     try
	     {
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream("constant.properties");
		prop.load(stream);
	     }
	     catch (Exception e) {System.out.println("Error in constant properties Manager "+e);}
		
			Document doc = new Document();
			try
			{	
				Connection con = ConnectionManager.getConnection();
				SimpleDateFormat fromf = new SimpleDateFormat("dd-MMM-yyyy");
				SimpleDateFormat tof = new SimpleDateFormat("yyyy-MM-dd");
				Date dt = fromf.parse(date1);
				String dt1 = tof.format(dt);
				dt = fromf.parse(date2);
				String dt2 = tof.format(dt);
				Date sysdate = new Date();
				String sys = fromf.format(sysdate);
				PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));
				//writer.setPageEvent(new SalaryCertificate()); //for writing in the background
				//writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
				doc.open();
				doc.setPageSize(PageSize.A4);
							
				PdfPTable tab1 = new PdfPTable(1);
				tab1.setWidthPercentage(new float[]{100},new Rectangle(100,100));
				PdfPCell cell = new PdfPCell();
				cell.setBorderWidth(0.7F);
				Font f_under = new Font(Font.HELVETICA,14,Font.BOLD|Font.UNDERLINE);
				Font f1 = new Font(Font.HELVETICA,8);
				Font f2 = new Font(Font.HELVETICA,7.5F);
				
				cell.addElement(Chunk.NEWLINE);
				cell.addElement(createPara("Date:-"+sys,f1, Element.ALIGN_RIGHT));
				cell.addElement(Chunk.NEWLINE);
				cell.addElement(createPara("Certificate",f_under, Element.ALIGN_CENTER));
				cell.addElement(Chunk.NEWLINE);
				cell.addElement(createDirectorPhrase("Subject:-","To Whom so ever it may concern.",Element.ALIGN_CENTER));
				cell.addElement(Chunk.NEWLINE);
				
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM EMPMAST WHERE EMPNO="+empno);
				if(rs.next())
				{
					LookupHandler lkph = new LookupHandler();
					String name = lkph.getLKP_Desc("SALUTE", rs.getInt("SALUTE"))+" "+rs.getString("FNAME")+" "+rs.getString("MNAME")+" "+rs.getString("LNAME");
					cell.addElement(createPara("Name : "+name, new Font(Font.HELVETICA,10,Font.BOLD), Element.ALIGN_CENTER));				
					cell.addElement(Chunk.NEWLINE);
				}
				cell.addElement(createPara("PERIOD FROM : "+date1+"  TO  "+date2, f1, Element.ALIGN_CENTER));
				cell.addElement(Chunk.NEWLINE);
				
				cell.addElement(salarystructure(empno));
				cell.addElement(Chunk.NEWLINE);
				
				cell.addElement(createPara("This is to certify that amount of earning is correct and computed from " , f1, Element.ALIGN_CENTER));
				cell.addElement(createPara("salary Record of above employes.", f1, Element.ALIGN_CENTER));
				cell.addElement(Chunk.NEWLINE);
				
				cell.addElement(createPara("For,"+prop.getProperty("companyName"), f1, Element.ALIGN_RIGHT));
				cell.addElement(Chunk.NEWLINE);
				cell.addElement(Chunk.NEWLINE);
				cell.addElement(footer());
				cell.addElement(Chunk.NEWLINE);
				cell.addElement(Chunk.NEWLINE);
				
				tab1.addCell(cell);
				doc.add(tab1);
				
			}
			catch(Exception e)
			{
				doc.close();
				e.printStackTrace();
			}
			doc.close();
		}
		
	
		public static Paragraph createPara(String value,Font f,int alignment)
		{
			Paragraph para = new Paragraph(value,f);
			para.setAlignment(alignment);
			para.setSpacingAfter(0);
			para.setSpacingBefore(0);
			return para;
			
		}
		 public static Phrase createDirectorPhrase(String arg1,String arg2,int alig){
		       
			  Font f_under = new Font(Font.HELVETICA,10,Font.BOLD|Font.UNDERLINE);
				Font f1 = new Font(Font.HELVETICA,10,Font.BOLD);
				Paragraph para = new Paragraph();
				Phrase director = new Phrase();
				director.add(new Chunk(new String(""+arg1),f_under));
			    director.add(new Chunk(""+arg2, f1));
			    para.add(director);
			    para.setAlignment(alig);
			    return para;
			    }
		
		public static PdfPTable salarystructure(int empno)
		{
			PdfPTable tab3 = new PdfPTable(3);
			Font f1 = new Font(Font.HELVETICA,8);
			Font fb = new Font(Font.HELVETICA,10,Font.BOLD);
			try
			{		
					Connection con = ConnectionManager.getConnection();
					Statement st = con.createStatement();
					
					
					
					
					
					
					tab3.setWidthPercentage(new float[]{65,5,30},new Rectangle(100,100));
					PdfPCell cell = new PdfPCell();
					cell.setBorder(0);
					
					ResultSet rs = st.executeQuery("SELECT TOP(1)INP_AMT FROM YTDTRAN WHERE trncd=101 and EMPNO="+empno+"ORDER BY TRNDT DESC");
					if(rs.next())
					tab3.addCell((createCell("SALARY :"+rs.getInt("INP_AMT")+" / - Per Month X 1 months", f1, Element.ALIGN_CENTER,0)));
					
					tab3.addCell((createCell("=", f1, Element.ALIGN_CENTER,0)));
					int total = rs.getInt("INP_AMT");
					System.out.println("basic= "+total);
					tab3.addCell((createCell(""+rs.getInt("INP_AMT"), fb, Element.ALIGN_CENTER,0)));
					
					tab3.addCell((createCell("HRA", f1, Element.ALIGN_RIGHT,0)));
					tab3.addCell((createCell("=", f1, Element.ALIGN_CENTER,0)));
					
					ResultSet rs1 = st.executeQuery("SELECT TOP(1)INP_AMT FROM YTDTRAN WHERE trncd=103 and EMPNO="+empno+"ORDER BY TRNDT DESC");
					if(rs1.next())
					total+=rs1.getInt("INP_AMT");
					System.out.println("Hra+basic="+total);
					tab3.addCell((createCell(""+rs1.getInt("INP_AMT"), f1, Element.ALIGN_CENTER,0)));
					
					
					tab3.addCell((createCell("CONVEYANCE ALLOWANCE", f1, Element.ALIGN_RIGHT,0)));
					tab3.addCell((createCell("=", f1, Element.ALIGN_CENTER,0)));
					
					ResultSet rs2 = st.executeQuery("SELECT TOP(1)INP_AMT FROM YTDTRAN WHERE trncd=108 and EMPNO="+empno+"ORDER BY TRNDT DESC");
					if(rs2.next())
					total+=rs2.getInt("INP_AMT");
					System.out.println("Hra+basic+convall="+total);
					tab3.addCell((createCell(""+rs2.getInt("INP_AMT"), f1, Element.ALIGN_CENTER,0)));
					tab3.addCell((createCell("Total Allowances", fb, Element.ALIGN_RIGHT,0)));
					tab3.addCell((createCell("=", f1, Element.ALIGN_CENTER,0)));
					tab3.addCell((createCell(""+total, fb, Element.ALIGN_CENTER,0)));
					
					tab3.addCell((createCell("P.F", f1, Element.ALIGN_RIGHT,0)));
					tab3.addCell((createCell("=", f1, Element.ALIGN_CENTER,0)));
					int dec = 0;
					ResultSet rs3 = st.executeQuery("SELECT TOP(1)INP_AMT FROM YTDTRAN WHERE trncd=201 and EMPNO="+empno+"ORDER BY TRNDT DESC");
					
					if(rs3.next()) 
						dec = rs3.getInt("INP_AMT");
					tab3.addCell((createCell(""+dec, f1, Element.ALIGN_CENTER,0)));
					tab3.addCell((createCell("Total Deduction", fb, Element.ALIGN_RIGHT,0)));
					tab3.addCell((createCell("=", f1, Element.ALIGN_CENTER,0)));
					System.out.println("pf is="+dec);
					tab3.addCell((createCell(""+dec, fb, Element.ALIGN_CENTER,0)));
					tab3.addCell((createCell("Net Salary :", fb, Element.ALIGN_RIGHT,0)));
					tab3.addCell((createCell("=", f1, Element.ALIGN_CENTER,0)));
					total-=dec;
					System.out.println("Hra+basic+convall-pf="+total);
					tab3.addCell((createCell(""+total, fb, Element.ALIGN_CENTER,0.5f)));
					
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return tab3;
		}
		
		private static PdfPTable footer() {
			PdfPTable tab3 = new PdfPTable(2);
			Font fb = new Font(Font.HELVETICA,10,Font.BOLD);
			try
			{
					tab3.setWidthPercentage(new float[]{50,50},new Rectangle(100,100));
					PdfPCell cell = new PdfPCell();
					cell.setBorder(0);
					tab3.addCell((createCell("     DATE :____________", fb, Element.ALIGN_LEFT,0)));
					tab3.addCell((createCell("         Authorize Signature       ", fb, Element.ALIGN_RIGHT,0)));
					
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return tab3;
		}
		
		
	
		
		public static PdfPCell createCell(String value,Font f,int alignment,float w)
		{
			PdfPCell cell = new PdfPCell(new Phrase(value,f));
			cell.addElement(createPara(value, f, alignment));
			cell.setBorderWidth(w);
			
			return cell;
		}
		
		public void xyz(PdfWriter writer, Document document)
		{
			Properties prop = new Properties();
		     try
		     {
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		     }
		     catch (Exception e) {System.out.println("Error in constant properties Manager "+e);}
		     
		     
			Font f = new Font(Font.HELVETICA,8,Font.BOLD, new GrayColor(0.60f));
			Font FONT = new Font(Font.HELVETICA, 52, Font.BOLD, new GrayColor(0.90f));
			Rectangle rect = writer.getBoxSize("art");
			try
			{
				ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase(prop.getProperty("companyName"),FONT), 297.5f, 421, 45);
				ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase("| Page : "+writer.getPageNumber(),f), rect.getRight()-30, rect.getBottom()-30, 0);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	
}



