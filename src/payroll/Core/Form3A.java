package payroll.Core;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import payroll.DAO.ConnectionManager;
import payroll.DAO.LookupHandler;
import com.ibm.icu.text.NumberFormat;
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

public class Form3A extends PdfPageEventHelper
{
	public static void getForm3A(String date1,String date2,int empno,String filepath) 
		{
			Document doc = new Document();
			try
			{
				SimpleDateFormat fromf = new SimpleDateFormat("dd-MMM-yyyy");
				SimpleDateFormat tof = new SimpleDateFormat("yyyy-MM-dd");
				Date dt = fromf.parse(date1);
				String dt1 = tof.format(dt);
				dt = fromf.parse(date2);
				String dt2 = tof.format(dt);
				PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));
				writer.setPageEvent(new Form3A());
				writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
				doc.open();
				doc.setPageSize(PageSize.A4);
							
				PdfPTable tab1 = new PdfPTable(1);
				tab1.setWidthPercentage(new float[]{100},new Rectangle(100,100));
				PdfPCell cell = new PdfPCell();
				cell.setBorderWidth(0.7F);
				Font f1 = new Font(Font.HELVETICA,8);
				Font f2 = new Font(Font.HELVETICA,7.5F);
				cell.addElement(createPara("FORM - 3 A",new Font(Font.HELVETICA,10,Font.BOLD), Element.ALIGN_CENTER));
				cell.addElement(createPara("( Unexempted Establishment Only )", f1, Element.ALIGN_CENTER));
				cell.addElement(createPara("THE EMPLOYEE'S PROVIDENT FUND SCHEME, 1952", f1, Element.ALIGN_CENTER));
				cell.addElement(createPara("( Paras 35 and 42 ) and", f1, Element.ALIGN_CENTER));
				cell.addElement(createPara("THE EMPLOYEES PENSION SCHEME, 1995", f1, Element.ALIGN_CENTER));
				cell.addElement(createPara("Para 20", f1, Element.ALIGN_CENTER));
				cell.addElement(Chunk.NEWLINE);
				cell.addElement(createPara("Contribution Card for the currency period from "+date1.substring(3)+" to "+date2.substring(3), new Font(Font.HELVETICA,7.5F,Font.BOLD), Element.ALIGN_LEFT));
				tab1.addCell(cell);
				
				cell = new PdfPCell();cell.setBorderWidth(0.7F);
				cell.addElement(createMainInfoTab(empno));
				tab1.addCell(cell);
				doc.add(tab1);
				
				doc.add(createDataTable(empno, dt1, dt2));
				
				PdfPTable tab2 = new PdfPTable(1);
				tab2.setWidthPercentage(new float[]{100},new Rectangle(100,100));
				cell = new PdfPCell();cell.setBorderWidth(0.7F);
				cell.addElement(createPara("Certified That the difference between the total of the contribution shown under the columns (3) & (4) of the above table and that arrived at on the total", f2, Element.ALIGN_LEFT));
				cell.addElement(createPara("Wages shown in Column (2) at the Prescribed rate is soley due to the rounding of the contributions to the nearest rupee under the rule.", f2, Element.ALIGN_LEFT));
				cell.addElement(createPara("* Contribution for the month of March paid in April", f1, Element.ALIGN_CENTER));
				cell.addElement(Chunk.NEWLINE);
				cell.addElement(createPara("IN THE CASE OF AN EMPLOYEE WHO HAS LEFT THE SERVICE OF THE COMPANY EARLIER, HIS CONTRIBUTION HAS BEEN", f2, Element.ALIGN_LEFT));
				cell.addElement(createPara("SHOWN IN THE SUBSEQUENT MONTH WHICH HIS FINAL SETTLEMENT OF SALARY HAS BEEN MADE.", f2, Element.ALIGN_LEFT));
				
				PdfPTable tab3 = new PdfPTable(2);
				tab3.setWidthPercentage(new float[]{70,30},new Rectangle(100,100));
				PdfPCell c1 = new PdfPCell();
				c1.setBorderWidth(0);
				c1.addElement(createPara("Date : ", f2,Element.ALIGN_LEFT));
				tab3.addCell(c1);
				c1 = new PdfPCell();
				c1.setBorderWidth(0);
				c1.addElement(createPara("Signature of Employer", f2, Element.ALIGN_CENTER));
				c1.addElement(createPara("(Office Seal)", f2, Element.ALIGN_CENTER));
				tab3.addCell(c1);
				tab3.setSpacingAfter(5);
				tab3.setSpacingBefore(8);
				cell.addElement(tab3);
				tab2.addCell(cell);
				cell = new PdfPCell();cell.setBorderWidth(0.7F);
				cell.addElement(createPara("Note : 1) In respect of the Form (3A) sent to the Regional office during the course of the currency period for the purpose of final", f2, Element.ALIGN_LEFT));
				cell.addElement(createPara("Settelment of the accounts of the members who had left service details of date and reasons for leaving service and", f2, Element.ALIGN_LEFT));
				cell.addElement(createPara("certificate as shown in the remark column should be added.", f2, Element.ALIGN_LEFT));
				cell.addElement(createPara("2) If there is no period of NCS, the word \"NIL\" to be mentioned against the total column.", f2, Element.ALIGN_LEFT));
				cell.addElement(createPara("3) Wherever no wages are shown against any month the period should be shown as NCS under Col 6.", f2,Element.ALIGN_LEFT));
				tab2.addCell(cell);
				
				doc.add(tab2);
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
		
		public static PdfPTable createMainInfoTab(int empno)
		{
			
			Properties prop = new Properties();
		     try
		     {
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		     }
		     catch (Exception e) {System.out.println("Error in constant properties Manager "+e);}
		     
		     
			PdfPTable tab = new PdfPTable(2);
			Font f1 = new Font(Font.HELVETICA,8);
			Connection con = ConnectionManager.getConnection();
			try
			{
				tab.setWidthPercentage(new float[]{65,35},new Rectangle(100, 100));
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM EMPMAST WHERE EMPNO="+empno);
				if(rs.next())
				{
					PdfPCell cell = new PdfPCell();
					cell.setBorder(0);
					cell.addElement(createPara("1. Account No.: "+rs.getString("PFNO"), f1, Element.ALIGN_LEFT));
					LookupHandler lkph = new LookupHandler();
					String name = lkph.getLKP_Desc("SALUTE", rs.getInt("SALUTE"))+" "+rs.getString("FNAME")+" "+rs.getString("MNAME")+" "+rs.getString("LNAME");
					
					PdfPTable tab3 = new PdfPTable(2);
					tab3.setWidthPercentage(new float[]{15,50},new Rectangle(65,100));
					PdfPCell c1 = new PdfPCell();
					c1.setBorderWidth(0);
					c1.addElement(createPara("2. Name/Surname: ", f1,Element.ALIGN_LEFT));
					c1.setHorizontalAlignment(Element.ALIGN_LEFT);
					tab3.addCell(c1);
					c1 = new PdfPCell();
					c1.setBorderWidth(0);
					c1.addElement(createPara(name, new Font(Font.HELVETICA,8,Font.BOLD), Element.ALIGN_LEFT));
					tab3.addCell(c1);
					cell.addElement(tab3);
					
					cell.addElement(createPara("3. Father / Husband's Name: "+(rs.getString("FHNAME")==null?"":rs.getString("FHNAME")), f1, Element.ALIGN_LEFT));
					cell.addElement(createPara("4. Name & Address of the Establishment : ", f1, Element.ALIGN_LEFT));
					cell.addElement(createPara(prop.getProperty("companyName"), new Font(Font.HELVETICA,9,Font.BOLD),Element.ALIGN_LEFT));
					cell.addElement(createPara(prop.getProperty("addressForReport"), f1, Element.ALIGN_LEFT));
					cell.addElement(createPara(prop.getProperty("contactForReport"), f1, Element.ALIGN_LEFT));
					cell.addElement(createPara(prop.getProperty("mailForReport"), f1, Element.ALIGN_LEFT));
					tab.addCell(cell);
					
					cell = new PdfPCell();
					cell.setBorder(0);
					cell.addElement(createPara("5. Statutory Rate of Contribution : 12.00%", f1, Element.ALIGN_LEFT));
					cell.addElement(createPara("6. Voluntary higher rate od Employees Contribution, if any", f1, Element.ALIGN_LEFT));
					cell.addElement(createPara("7. Whether, opted to contribute on full salary for pension? Yes / No", f1, Element.ALIGN_LEFT));
					tab.addCell(cell);
				}
				con.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return tab;
		}
		
		public static PdfPTable createDataTable(int empno,String from, String to)
		{
			PdfPTable tab = new PdfPTable(9);
			Font f1 = new Font(Font.HELVETICA,8,Font.BOLD);
			Font f2 = new Font(Font.HELVETICA,8);
			NumberFormat format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
			
			try 
			{
				tab.setWidthPercentage(new float[]{15,10,10,10,10,10.6F,10,10,14.4F}, new Rectangle(100,100));
				tab.addCell(createCell("Month", f1, Element.ALIGN_CENTER));
				tab.addCell(createCell("Amount of Wages Rs.", f1, Element.ALIGN_CENTER));
				tab.addCell(createCell("Worker's Share EPF", f1, Element.ALIGN_CENTER));
				tab.addCell(createMiddleCell());
				tab.addCell(createCell("Refund of Advances", f1, Element.ALIGN_CENTER));
				tab.addCell(createCell("Non cont. services From..... To....", f1, Element.ALIGN_CENTER));
				tab.addCell(createCell("Remarks", f1, Element.ALIGN_CENTER));
				
				tab.addCell(createCell("1", f1, Element.ALIGN_CENTER));
				tab.addCell(createCell("2", f1, Element.ALIGN_CENTER));
				tab.addCell(createCell("3", f1, Element.ALIGN_CENTER));
				tab.addCell("");
				tab.addCell(createCell("4", f1, Element.ALIGN_CENTER));
				tab.addCell("");
				tab.addCell(createCell("5", f1, Element.ALIGN_CENTER));
				tab.addCell(createCell("6", f1, Element.ALIGN_CENTER));
				tab.addCell(createCell("7", f1, Element.ALIGN_CENTER));
				
				Connection con = ConnectionManager.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT DATENAME(MONTH, trndt) + ' ' + DATENAME(YEAR, trndt) as mon,NET_AMT FROM YTDTRAN " +
											   " WHERE EMPNO = "+empno+" AND TRNCD=101 AND TRNDT BETWEEN '"+from+"' AND '"+to+"' ORDER BY TRNDT ");
				int totalAmt=0,total3=0,total4a=0,total4b=0;
				int val3=0,val4a=0,val4b=0;
				while(rs.next())
				{
					int amt = rs.getInt("NET_AMT");
					val3 = (int)Math.round(amt * 12.00 / 100);
					val4a = (int) Math.round(amt * 3.67 / 100);
					val4b = (int) Math.round(amt * 8.33 / 100);
							
					tab.addCell(createCell(rs.getString("MON"), f2, Element.ALIGN_LEFT));
					tab.addCell(createCell(format.format(amt).substring(4), f2, Element.ALIGN_RIGHT));
					tab.addCell(createCell(format.format(val3).substring(4), f2, Element.ALIGN_RIGHT));
					tab.addCell(createCell(format.format(val4a).substring(4), f2, Element.ALIGN_RIGHT));
					tab.addCell(createCell(format.format(val4b).substring(4), f2, Element.ALIGN_RIGHT));
					tab.addCell(createCell(format.format(val3).substring(4), f2, Element.ALIGN_RIGHT));
					tab.addCell(createCell("", f2, Element.ALIGN_RIGHT));
					tab.addCell(createCell("", f2, Element.ALIGN_RIGHT));
					tab.addCell(createCell("", f2, Element.ALIGN_LEFT));
					
					totalAmt+= amt;
					total3+= val3;
					total4a+= val4a;
					total4b+= val4b;
				}
				tab.addCell(createCell("Total", f1, Element.ALIGN_CENTER));
				tab.addCell(createCell(format.format(totalAmt).substring(4), f1, Element.ALIGN_RIGHT));
				tab.addCell(createCell(format.format(total3).substring(4), f1, Element.ALIGN_RIGHT));
				tab.addCell(createCell(format.format(total4a).substring(4), f1, Element.ALIGN_RIGHT));
				tab.addCell(createCell(format.format(total4b).substring(4), f1, Element.ALIGN_RIGHT));
				tab.addCell(createCell(format.format(total3).substring(4), f1, Element.ALIGN_RIGHT));
				tab.addCell(createCell("", f1, Element.ALIGN_RIGHT));
				tab.addCell(createCell("", f1, Element.ALIGN_RIGHT));
				tab.addCell(createCell("", f1, Element.ALIGN_LEFT));
				con.close();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			return tab;
		}
		
		public static PdfPCell createCell(String value,Font f,int alignment)
		{
			PdfPCell cell = new PdfPCell(new Phrase(value,f));
			cell.addElement(createPara(value, f, alignment));
			cell.setBorderWidth(0.5F);
			
			return cell;
		}
		public static PdfPCell createMiddleCell()
		{
			PdfPCell cell = new PdfPCell();
			cell.setColspan(3);
			Font f = new Font(Font.HELVETICA,8);
			cell.addElement(createPara("Employer Share", new Font(Font.HELVETICA,8,Font.BOLD), Element.ALIGN_CENTER));
			try
			{
				PdfPTable tab = new PdfPTable(3);
				tab.setSpacingBefore(5);
				tab.setSpacingAfter(0);
				tab.setWidthPercentage(new float[]{10,10,10.8F}, new Rectangle(30,30));
				tab.addCell(createCell("EPF", f, Element.ALIGN_CENTER));
				tab.addCell(createCell("EPS", f, Element.ALIGN_CENTER));
				tab.addCell(createCell("TOTAL", f, Element.ALIGN_CENTER));
				tab.addCell(createCell("a", f, Element.ALIGN_CENTER));
				tab.addCell(createCell("b", f, Element.ALIGN_CENTER));
				tab.addCell(createCell("c", f, Element.ALIGN_CENTER));
				cell.addElement(tab);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return cell;
		}
		
		public void onEndPage(PdfWriter writer, Document document)
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


