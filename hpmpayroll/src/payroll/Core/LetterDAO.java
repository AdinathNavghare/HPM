package payroll.Core;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import payroll.Core.UtilityDAO.Footer1;
import payroll.DAO.ConnectionManager;
import payroll.DAO.EmpOffHandler;
import payroll.DAO.LookupHandler;
import payroll.DAO.TranHandler;
import payroll.Model.EmpOffBean;
import payroll.Model.TransactionBean;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfWriter;

public class LetterDAO {
	
	static String lable1="";
	static String label2="";


	public static void experienceLetter(String emp,String reptype, String imagepath, String filepath,String date){
		
		Properties prop = new Properties();
	    try
	    {
				//prop.load(new FileInputStream("src/Config.properties"));
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			    InputStream stream = classLoader.getResourceAsStream("constant.properties");
			    prop.load(stream);
		 }
	    catch (Exception e) 
	    {
				// TODO Auto-generated catch block
	   	 System.out.println("Error in constant Properties "+e);
	    }
		
	 
		
			UtilityDAO dao = new UtilityDAO();
			
			String[] employee = emp.split(":");
			String empname =employee[0].trim();
			String empcode =employee[1].trim();
			String empno =employee[2].trim();
			   TransactionBean trbn = new EmpOffHandler().getInfoEmpTran(empno);
			
			try {
				
				Font f = new Font(Font.TIMES_ROMAN,15);
				Font f1 = new Font(Font.TIMES_ROMAN,18);
				Font f2 = new Font(Font.TIMES_ROMAN,18);
				Font FONT = new Font(Font.TIMES_ROMAN,40, Font.NORMAL, new GrayColor(0.85f));
				
				Document doc = new Document(PageSize.A4);
				PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));
				 	
				System.out.println("Break point3.....");
				
				Footer1 ftr = dao.new Footer1(lable1);
				writer.setPageEvent(ftr);
				
				System.out.println("Break point4.....");
				
				doc.open();
				
				ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase(prop.getProperty("watermark"),FONT), 350f, 400f, 45);			
				Image image1 = Image.getInstance(imagepath);
				
				//Paragraph for company name...
				Phrase title = new Phrase(prop.getProperty("companyName"),new Font(FONT.TIMES_ROMAN,18,Font.BOLD));
				Paragraph para = new Paragraph(title);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(80);
				
				
				image1.scaleAbsolute(80f, 40f);
				image1.setAbsolutePosition(30f, 785f);
				doc.add(image1);
				doc.add(para);
				
				
				
				
				//Paragraph for date..
				para = new Paragraph(new Phrase("Date:"+date,f));
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				//Paragraph for  Name And salution..
				para = new Paragraph(new Phrase("Experience Letter",f));
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph(new Phrase("TO WHOMSOEVER IT MAY CONCERN",f));
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(30);
				doc.add(para);
				
				
				
				//Paragraph for main content of letter ..
				Rectangle rec = new  Rectangle(100,100);
				para = new Paragraph(new Phrase("This is to certify that "+trbn.getEmpname()+" bearing employee has worked with "+prop.getProperty("companyName")+" from " +
						""+trbn.getDOJ()+" to "+trbn.getDol()+". ",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(20);
				doc.add(para);
				
				para = new Paragraph(new Phrase(empname+" has joined as "+new LookupHandler().getLKP_Desc("DESIG", trbn.getDesg())+".  ",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(20);
				doc.add(para);
				
				para = new Paragraph(new Phrase("The employee's performance was satisfactory and we appreciate the services rendered to our organization.   ",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(20);
				doc.add(para);
				
				
				para = new Paragraph(new Phrase("We wish them all the best in their future career.  ",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(50);
				doc.add(para);
				
				
				//Paragraph for Regards..
				para = new Paragraph(new Phrase("Yours truly,",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setAlignment(Element.ALIGN_BOTTOM);
				para.setSpacingAfter(15);
				doc.add(para);
				
				
				
				//Paragraph for name of ..
				para = new Paragraph(new Phrase("For "+prop.getProperty("companyName")+"",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setAlignment(Element.ALIGN_BOTTOM);
				para.setSpacingAfter(30);
				doc.add(para);
				
				para = new Paragraph(new Phrase(prop.getProperty("director"),f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setAlignment(Element.ALIGN_BOTTOM);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Director ",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setAlignment(Element.ALIGN_BOTTOM);
				para.setSpacingAfter(0);
				doc.add(para);
				
				
				doc.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	public static void appraisalLetter(String emp,String reptype, String imagepath, String filepath,String date){
		
		Properties prop = new Properties();
	    try
	    {
				
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			    InputStream stream = classLoader.getResourceAsStream("constant.properties");
			    prop.load(stream);
		 }
	    catch (Exception e) 
	    {
				// TODO Auto-generated catch block
	   	 System.out.println("Error in constant Properties "+e);
	    }
		
	 
		
			UtilityDAO dao = new UtilityDAO();
			
			String[] employee = emp.split(":");
			String empname =employee[0].trim();
			String empcode =employee[1].trim();
			String empno =employee[2].trim();
			   TransactionBean trbn = new EmpOffHandler().getInfoEmpTran(empno);
			
			try {
				
				Font f = new Font(Font.TIMES_ROMAN,15);
				Font f1 = new Font(Font.TIMES_ROMAN,18);
				Font f2 = new Font(Font.TIMES_ROMAN,18);
				Font FONT = new Font(Font.TIMES_ROMAN,40, Font.NORMAL, new GrayColor(0.85f));
				
				Document doc = new Document(PageSize.A4);
				PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));
				 	
				
				
				Footer1 ftr = dao.new Footer1(lable1);
				writer.setPageEvent(ftr);
				
			
				
				doc.open();
				
				ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase(prop.getProperty("watermark"),FONT), 350f, 400f, 45);			
				Image image1 = Image.getInstance(imagepath);
				
				//Paragraph for company name...
				Phrase title = new Phrase(prop.getProperty("companyName"),new Font(FONT.TIMES_ROMAN,18,Font.BOLD));
				Paragraph para = new Paragraph(title);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(30);
				
				
				image1.scaleAbsolute(45f, 30f);
				image1.setAbsolutePosition(30f, 785f);
				doc.add(image1);
				doc.add(para);
				
				
				
				
				//Paragraph for date..
				para = new Paragraph(new Phrase("Date:"+date,f));
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				//Paragraph for  Name And salution..
				para = new Paragraph(new Phrase("Appraisal Letter",f));
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(30);
				doc.add(para);
				
				para = new Paragraph(new Phrase("To",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);

				para = new Paragraph(new Phrase("HR Manager",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);
										
				
				para = new Paragraph(new Chunk("Subject: Request For salary increment for "+trbn.getEmpname(),f).setUnderline(0.1f,-2f));
			
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(30);
				doc.add(para);
				
				//Paragraph for main content of letter ..
			
				para = new Paragraph(new Phrase("With respect to our internal discussion with senior management, we have agreed to give salary increment of (                         ) to "+trbn.getEmpname()+" from coming month.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph(new Phrase("I would request you to kindly process the same.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(30);
				doc.add(para);
				
				
				para = new Paragraph(new Phrase("Regards,",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(20);
				doc.add(para);
				
				
				para = new Paragraph(new Phrase("Project Manager/Respective Heads.",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph(new Chunk("                                                                                                                                        ",f).setUnderline(0.1f,-2f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(30);
				doc.add(para);
				
				//Paragraph for Regards..
				para = new Paragraph(new Phrase("And before making increment into salary, head dept of site must inform to HR manager in following manner:",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
			
				para.setSpacingAfter(10);
				doc.add(para);

				para = new Paragraph(new Phrase("		• Increment request letter via mail to HR manager and in cc mention Accountant.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
			
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph(new Phrase("		• Name of the employee and the actual amount to be increment mention in the mail.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
			
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph(new Phrase("		• HR will forward the mail to director for discussion for the increment as per his performance.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
			
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph(new Phrase("		• Once approved form director will forward the return copy via mail.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
			
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Same procedure should be implementing for advance policy also",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
			
				para.setSpacingAfter(5);
				doc.add(para);
				
				
				
				
				
				doc.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	public static void RelievingLetter(String emp,String reptype, String imagepath, String filepath,String date){
		
		Properties prop = new Properties();
	    try
	    {
				//prop.load(new FileInputStream("src/Config.properties"));
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			    InputStream stream = classLoader.getResourceAsStream("constant.properties");
			    prop.load(stream);
		 }
	    catch (Exception e) 
	    {
				// TODO Auto-generated catch block
	   	 System.out.println("Error in constant Properties "+e);
	    }
		
	 
		
			UtilityDAO dao = new UtilityDAO();
			
			String[] employee = emp.split(":");
			String empname =employee[0].trim();
			String empcode =employee[1].trim();
			String empno =employee[2].trim();
			   TransactionBean trbn = new EmpOffHandler().getInfoEmpTran(empno);
			
			try {
				
				Font f = new Font(Font.TIMES_ROMAN,15);
				Font f1 = new Font(Font.TIMES_ROMAN,18);
				Font f2 = new Font(Font.TIMES_ROMAN,18);
				Font FONT = new Font(Font.TIMES_ROMAN,40, Font.NORMAL, new GrayColor(0.85f));
				
				Document doc = new Document(PageSize.A4);
				PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));
				 	
				
				
				Footer1 ftr = dao.new Footer1(lable1);
				writer.setPageEvent(ftr);
				
			
				
				doc.open();
				
				ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase(prop.getProperty("watermark"),FONT), 350f, 400f, 45);			
				Image image1 = Image.getInstance(imagepath);
				
				//Paragraph for company name...
				Phrase title = new Phrase(prop.getProperty("companyName"),new Font(FONT.TIMES_ROMAN,18,Font.BOLD));
				Paragraph para = new Paragraph(title);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(30);
				
				
				image1.scaleAbsolute(45f, 30f);
				image1.setAbsolutePosition(30f, 785f);
				doc.add(image1);
				doc.add(para);
				
				
				
				
				//Paragraph for date..
				para = new Paragraph(new Phrase("Date:"+date,f));
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				//Paragraph for  Name And salution..
				para = new Paragraph(new Phrase("Relieving Letter",f));
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(30);
				doc.add(para);
				
				para = new Paragraph(new Phrase("To",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);

				para = new Paragraph(new Phrase(trbn.getEmpname(),f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph(new Phrase(new LookupHandler().getLKP_Desc("DESIG", trbn.getDesg()),f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(30);
				doc.add(para);
				
				
				para = new Paragraph(new Phrase("SUB:  Acceptance of relieving letter.",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(30);
				doc.add(para);
				
				//Paragraph for main content of letter ..
			
				para = new Paragraph(new Phrase("Dear "+trbn.getEmpname()+",",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph(new Phrase("This is with reference to your resignation letter that "+trbn.getEmpname()+" is working " +
						"with us as "+new LookupHandler().getLKP_Desc("DESIG", trbn.getDesg())+" from "+trbn.getDOJ()+" to till date. During the tenure of your employment, we found you hard working and intelligent. ",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(20);
				doc.add(para);
				
				para = new Paragraph(new Phrase("This is to inform you that your resignation has been accepted and you are relieved from the services of Name of the Company on the close of working hours on Date.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(20);
				doc.add(para);
				
				
				para = new Paragraph(new Phrase("We wish you all success in your future endeavors. ",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(50);
				doc.add(para);
				
				
				//Paragraph for Regards..
				para = new Paragraph(new Phrase("Yours Sincerely,",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setAlignment(Element.ALIGN_BOTTOM);
				para.setSpacingAfter(15);
				doc.add(para);
				
				
				
				//Paragraph for name of ..
				para = new Paragraph(new Phrase("For "+prop.getProperty("companyName")+"",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setAlignment(Element.ALIGN_BOTTOM);
				para.setSpacingAfter(30);
				doc.add(para);
				
							
				para = new Paragraph(new Phrase("Manager-Hr ",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setAlignment(Element.ALIGN_BOTTOM);
				para.setSpacingAfter(0);
				doc.add(para);
				
				
				doc.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	public static void offerLetter(String emp,String reptype, String imagepath, String filepath,String date){
		
		Properties prop = new Properties();
	    try
	    {
				
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			    InputStream stream = classLoader.getResourceAsStream("constant.properties");
			    prop.load(stream);
		 }
	    catch (Exception e) 
	    {
				// TODO Auto-generated catch block
	   	 System.out.println("Error in constant Properties "+e);
	    }
		
	 
		
			UtilityDAO dao = new UtilityDAO();
			
			String[] employee = emp.split(":");
			String empname =employee[0].trim();
			String empcode =employee[1].trim();
			String empno =employee[2].trim();
			   TransactionBean trbn = new EmpOffHandler().getInfoEmpTran(empno);
			
			try {
				
				Font f = new Font(Font.TIMES_ROMAN,13);
				Font f1 = new Font(Font.TIMES_ROMAN,18);
				Font f2 = new Font(Font.TIMES_ROMAN,18);
				Font fontbold =new Font(Font.TIMES_ROMAN,15,Font.BOLD);
				Font FONT = new Font(Font.TIMES_ROMAN,40, Font.NORMAL, new GrayColor(0.85f));
				
				Document doc = new Document(PageSize.A4);
				PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));
				 	
				
				
				Footer1 ftr = dao.new Footer1(lable1);
				writer.setPageEvent(ftr);
				
			
				
				doc.open();
				
				ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase(prop.getProperty("watermark"),FONT), 350f, 400f, 45);			
				Image image1 = Image.getInstance(imagepath);
				
				//Paragraph for company name...
				Phrase title = new Phrase(prop.getProperty("companyName"),new Font(FONT.TIMES_ROMAN,18,Font.BOLD));
				Paragraph para = new Paragraph(title);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(30);
				
				
				image1.scaleAbsolute(45f, 30f);
				image1.setAbsolutePosition(30f, 785f);
				doc.add(image1);
				doc.add(para);
				
				
				
				
				//Paragraph for date..
				para = new Paragraph(new Phrase("Date:"+date,f));
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(1);
				doc.add(para);
				
				//Paragraph for  Name And salution..
				para = new Paragraph(new Phrase("Welcome to "+prop.getProperty("companyName")+"",fontbold));
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(20);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Dear (EMployee Name)",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);

				para = new Paragraph(new Phrase("Welcome to "+prop.getProperty("companyName")+" Group. I am delighted you are joining our team.",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				//Paragraph for main content of letter ..
			
				para = new Paragraph(new Phrase("Enabling our employees to excel is at the heart of our core value.  As a member of the team, we expect you to deliver great service to our clients and join our passion for “bringing service to life”.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Our Organization is a service provider company which excels at improving the quality and efficiency of essential service that matter to our clients. Mahalakshmi currently has about 180 employees in Mumbai. We deliver labor management, material management and back office support",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph(new Phrase("Service and help us realize our group’s vision to be the greatest service company.",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(20);
				doc.add(para);
				
				
				para = new Paragraph(new Phrase(""+prop.getProperty("companyName")+" core values are:",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph(new Phrase("	•	We enable our people to excel",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(0);
				doc.add(para);
				
				para = new Paragraph(new Phrase("	•	We deliver our promises",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(0);
				doc.add(para);
				
				para = new Paragraph(new Phrase("	•	We built trust and respect",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(20);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Our success is a reflection of our people. We continuously invest our people and built on our reputation of service excellence and innovation. With us, you can look forward to a rewarding career and great opportunities to learn and grow.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph(new Phrase("I invite you to join me on this journey and work with me to built one of the best organizations in Mumbai.",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);

				para = new Paragraph(new Phrase("Warm Regards",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(40);
				doc.add(para);

				
				
				para = new Paragraph(new Phrase(prop.getProperty("director"),f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Director",f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(5);
				doc.add(para);
				
				//new page start here
				doc.newPage();
				
				para = new Paragraph(new Phrase("Date:"+date,fontbold));
				para.setSpacingBefore(20);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(20);
				para.setSpacingBefore(20);
				doc.add(para);
				
				para = new Paragraph("TO",f);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("(EMPLOYEE NAME)",f);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("With reference to the interview you have had with us in the recent past, " +
						"we are pleased to make you an offer of the Employment in our Company. You will be placed in " +
						"our designated as (DESIGNATION) for (SITE NAME). You are required to report to the Company’s office on  (JOINING DATE) at (TIME)",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("Your appointment will be under the following terms and conditions of employment:",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("1.		You shall be paid a total remuneration in hand Salary will be 13000/- per month.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("2.		Your remuneration is as per the enclosed annexure. All tax liabilities arising out of your entire compensation package, present or future, shall be borne by you.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("3.		While your initial place of posting will be at company’s office at Mumbai; you are liable to be transferred to any of the company’s offices in Mumbai site or to any other department in the organization. All such transfers shall be governed by Transfer Policy of the company.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("4.		Your appointment will be on probation for a period of 6 months from the date of your joining the Company. However, the period of probation may be extended in case it is found necessary. Please note that your confirmation shall be done in accordance with the Confirmation Policy of the Company and your service will be confirmed only when confirmation is done in writing by the Company. That-",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("				During the period of probation, your service can be terminated at any time without notice and without assigning any reason thereof.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph("				On confirmation of your services, your services are liable to be terminated by the company on giving you 30 days notice in writing or salary in lieu thereof.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph("				In case you decide to leave the service of the Company at any time, you will be required to give not less than 30 days notice in writing and the Company may at its sole discretion relieve you of your duties any time during the notice period, and in that event you will be paid salary up to the last working day.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph("				In case, where due to reasons beyond the employee’s control, the employee is required to give notice less than the notice period, the Company may at its sole discretion, relieve the employee of its duties before the completion of the mandatory notice period with or without payment of salary in lieu of such notice period or shortfall therein.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("5.		You will always maintain utmost  confidentially with regard to records, documents and other information relating to the business of the Company which may be known, provided or confided to you and you shall use the same only in a responsible manner, in the best interests of the company. Upon ceasing to be in the service of the Company for any reason, you shall immediately return any records, documents and other information of the Company which are in your possession and shall not retain or transmit any copies ( electronic or otherwise ) of the same.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("6.		You shall be bound by all the existing rules and regulations of the Company and those that may be framed from time to time including but not limited to Code of Conduct, Disciplinary Action Policy, Confidentially and Information Security Policy, Leave Policy amongst others.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("7.		You will automatically retire from the service of the Company on the last day of the calendar month in which you attain the age of 58 years. Your date of birth as per official records is that in the event of your becoming unfit for performance of your normal duties, you shall be liable to be discharged from the service without any notice.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("8.		This appointment is subject to satisfactory investigation of your credentials’ and if it is found at any time that you have made any false statement or suppressed any material information, it shall lead to immediate termination of your services by the Company without any notice or compensation.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("9.		You are required to maintain the highest order of discipline and secrecy a secrecy as regards the work of the company and/of it s subsidiaries or associate companies and in case of any breach of trust/discipline, your services may be terminated by the Company with immediate effect.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("10.		Please sign and return the duplicate copy of this letter as a token of your acceptance of this Letter of Employment.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("Welcome to "+prop.getProperty("companyName")+", here’s wishing you a rewarding career.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(20);
				doc.add(para);
				
				para = new Paragraph("Yours Truly",f);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(0);
				doc.add(para);
				
				para = new Paragraph("For "+prop.getProperty("companyName")+"",f);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(20);
				doc.add(para);
				
				para = new Paragraph(prop.getProperty("vicepresident"),fontbold);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph("Vice President",fontbold);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(0);
				doc.add(para);
				
				para = new Paragraph(prop.getProperty("hrmanager"),fontbold);
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph("HR Manager",fontbold);
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(20);
				doc.add(para);
				
				
				
				para = new Paragraph("Date: ------------------------------------ - (Signature):---------------------------------",f);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(0);
				doc.add(para);
				
				//new page start here
				doc.newPage();
				
				
				para =new Paragraph(""+new Chunk("ANNEXURE II").setUnderline(0.1f, -2f),fontbold);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingBefore(40);
				para.setSpacingAfter(0);
				doc.add(para);
				
				para = new Paragraph(""+new Chunk("DECLARATIOIN AND UNDERTAKING REGARDING NON-DISCLOSURE").setUnderline(0.1f, -2f),fontbold);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(20);
				doc.add(para);
				
				para = new Paragraph("I,.................... residing at............................................................. And working as ....................., do hereby declare and state as follows:-",f);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("1.		I do hereby declare that I will faithfully, truly and to the best of my skill and ability, execute and perform the duties required of me as an employee of Mahalakshmi Corporation, a Company registered under the Indian Partnership Act 1932 and having its office at 06 Shankar Sadan, Old Maneklal Estate, L.B.S. Ghatkopar (W), Mumbai- 400086.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("2.		I declare that in the performance of my employment and duties required of me, I will have to access documents, files, records, clients details, project plans, strategies, developments, execution process, contracts, billing information quality metrics, financial information about the Company etc relating to business of the company that is proprietary to it, or its clients (herein after called “Confidential Information”). I acknowledge that such information is a valuable, special and a unique asset of the Company, or of the Company’s clients.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("3.		I will hold such confidential Information strictly confidential by not, directly or indirectly, making known, or permitting such confidential Information to be disclosed or made known to any person or entity, either inside the company or otherwise. I shall faithfully and diligently hold such confidential Information from being disclosed to unauthorized persons. Such persons include, but are not necessarily limited to, persons who are not company’s employees, persons who are company’s employee’s but who do not have a need to know the Confidential Information in order to perform their duties, persons not under a written confidentiality agreement with the company in regard to the Confidential Information, persons not directly aware of the proprietary and trade secret nature of the Confidential Information.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("		The Company acknowledge that individual marketing packages, Web sites, and other communication that have been developed for a client may have been placed in the “public domain” once they have been distributed to the public and thus, may be no longer subject to client confidentiality provisions.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("4.		All Documents, files, records, Project plans, software tools as well as methods and techniques of doing business, including patents, trade secrets and other proprietary rights associated therewith, Strategies, Project details and items of information or equipment relating to company’s business are and shall remain the property of the company, including notes, documents, and files created in the performance of my duties of employment. I shall not under any circumstances remove such property from company’s premises without prior written consent. I further agree that all information relating to existing customers and potential customers of the Products, whether recorded in Company’s database or otherwise is confidential to the company and that any ownership in respect thereof resides in the company and that it cannot be used by employee for any purpose not specifically referred to in this employment.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("5.		I declare that notwithstanding the separation of my employment with the company for any reason whatsoever I will not communicate or allow to be communicated to any person not legally entitled thereto any information relating to Confidential Information and affairs of Intel net.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("6.		I undertake that I shall not offer, promise, give, accept, condone, approve or knowingly benefit from an improper business gratuity, a bribe, `kickback` or other improper advantage, benefit or reward, or otherwise apply inappropriate influence.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("7.		I undertake that I shall not make a facilitation payment. Facilitation payment refers to the practice of paying a small sum of money to 9usually) an official as a way of ensuring that they perform their duty.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("8.		I understand and acknowledge that the restraints contained herein are reasonable in all the circumstances of employment, and agree that they are necessary for the protection and maintenance of the company and its business.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("9.		I understand that my failure to comply with the declaration and undertaking will result in disciplinary action, which may include and up to immediate termination of employment with the company.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("10.		I also understand and agree that my service can be suspended pending disciplinary action/enquiry/investigation as per Company policy and I shall abide by decision of Investigation/Enquiry Committee constituted for such purposes.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("11.		I also acknowledge that the Company shall be entitled to seek an order for specific performance or injunctive or other equitable relief in case of my failure to observe or breach by me of any of the restraints herein.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("12.		I further undertake that I shall indemnify and keep indemnified the company for any loss, damages or injury suffered by the company for any breach of above conditions or any other clause or term of employment.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(30);
				doc.add(para);
				
				
				para = new Paragraph("Signature: -----------------",f);
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para = new Paragraph("Name: ----------------------------",f);
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(20);
				doc.add(para);
				
				
				//new page start here
				doc.newPage();
				
				
				para =new Paragraph(""+new Chunk("ANNEXURE III").setUnderline(0.1f, -2f),fontbold);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingBefore(40);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph(""+new Phrase("DECLARATION"),fontbold);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(30);
				doc.add(para);
				
				para = new Paragraph("Article I.",fontbold);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(20);
				doc.add(para);
				
				para = new Paragraph("I hereby certify that all statements made in the Mahalakshmi Corporation employment application form are true and complete. I understand that any omission or misrepresentation of any fact may result in refusal of employment or immediate dismissal.",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("I recognize that in connection with employment with the Mahalakshmi Corporation., I may be the subject of a background enquiry by the company or its representative, and I hereby authorize the same. I also authorized the Company to take action including penal action against me in case any fact is found contrary to what has been stated by me in the application form mentioned herein above",f);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(30);
				doc.add(para);
				
				para = new Paragraph("Signature: ----------------",f);
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(10);
				doc.add(para);
				
				para = new Paragraph("Name: --------------------",f);
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(20);
				doc.add(para);
				
				
				
				//new page start here
				doc.newPage();
				
				
				para =new Paragraph(""+new Chunk("Annexure I").setUnderline(0.1f, -2f),fontbold);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingBefore(40);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				para =new Paragraph(""+new Chunk("Compensation Details").setUnderline(0.1f, -2f),fontbold);
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(10);
				doc.add(para);
				
				
				
				
				
				
				
				
				
				
				doc.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
public static void FoolAndFinal(String emp,String reptype, String imagepath, String filepath,String date){
		
		Properties prop = new Properties();
	    try
	    {
				//prop.load(new FileInputStream("src/Config.properties"));
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			    InputStream stream = classLoader.getResourceAsStream("constant.properties");
			    prop.load(stream);
		 }
	    catch (Exception e) 
	    {
				// TODO Auto-generated catch block
	   	 System.out.println("Error in constant Properties "+e);
	    }
		
	 
		
			UtilityDAO dao = new UtilityDAO();
			
			String[] employee = emp.split(":");
			String empname =employee[0].trim();
			String empcode =employee[1].trim();
			String empno =employee[2].trim();
			   TransactionBean trbn = new EmpOffHandler().getInfoEmpTran(empno);
			   
			   EmpOffBean ebean = new EmpOffHandler().getEmpOfficAddInfo(empno);
			
			try {
				
				Font f = new Font(Font.TIMES_ROMAN,8);
			
				Font f1 = new Font(Font.TIMES_ROMAN,18);
				Font f2 = new Font(Font.TIMES_ROMAN,18);
				Font FONT = new Font(Font.TIMES_ROMAN,40, Font.NORMAL, new GrayColor(0.85f));
				
				Document doc = new Document(PageSize.A4);
				PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));
				Footer1 ftr = dao.new Footer1(lable1);
				writer.setPageEvent(ftr);
				
				
				
				doc.open();
				
				ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase(prop.getProperty("watermark"),FONT), 350f, 400f, 45);			
				Image image1 = Image.getInstance(imagepath);
				
				//Paragraph for company name...
				Phrase title = new Phrase(prop.getProperty("companyName"),new Font(FONT.TIMES_ROMAN,18,Font.BOLD));
				Paragraph para = new Paragraph(title);
				para.setAlignment(Element.ALIGN_CENTER);
				
				
				
				image1.scaleAbsolute(80f, 40f);
				image1.setAbsolutePosition(30f, 785f);
				doc.add(image1);
				doc.add(para);
					//Paragraph for date..
				para = new Paragraph(new Phrase("Fool and Final Settlememt Statement ",new Font(FONT.TIMES_ROMAN,12)));
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(10);
				doc.add(para);
				
				//Paragraph for  Name And salution..
				para = new Paragraph(new Chunk("PART - I",f).setUnderline(0.1f,-2f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(5);
				doc.add(para);
			
				//Paragraph for main content of letter ..
				
				para = new Paragraph(new Phrase("1. Full Name                                                            :        "+trbn.getEmpname()+"                 Emp Code    : "+trbn.getEMPCODE()+" ",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
			
				para = new Paragraph(new Phrase("2. Location                                                               :        "+ebean.getPrj_name()+"",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				
				para = new Paragraph(new Phrase("3. Designation                                                          :        "+new LookupHandler().getLKP_Desc("DESIG", trbn.getDesg())+"",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				para = new Paragraph(new Phrase("4. Date Of Joining                                                    :        "+trbn.getDOJ()+"",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				para = new Paragraph(new Phrase("5. Date Of Leaving                                                   :        "+trbn.getDol()+"",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				para = new Paragraph(new Phrase("6. Reason For Leaving                                             :       " +"Resignation",f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(1);
				doc.add(para);
				para = new Paragraph(new Chunk("                                                                                                                                              " +
						"                                                                                                     ",f).setUnderline(0.1f,-2f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(5);
				doc.add(para);
				int totalinc=0;
				
				para = new Paragraph(new Chunk("PART - I I - EARNINGS ",f).setUnderline(0.1f,-2f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(1);
				doc.add(para);
				para = new Paragraph(new Phrase("AMOUNT                                              ",f));
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(1);
				doc.add(para);
				int count=1;
				para = new Paragraph(new Phrase(""+count+". Salary - ",f));
				count++;
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				Connection con=null;
				con = ConnectionManager.getConnection();
				Statement st=con.createStatement();
				ResultSet rs = null;
				LookupHandler lkh= new LookupHandler();
				rs=st.executeQuery("Select trncd from paytran_stage where empno="+trbn.getEmpno()+" and net_amt!=0 and trndt=(select max(trndt) from paytran_stage where empno="+trbn.getEmpno()+"  ) and trncd<198 ");
				while(rs.next())
				{
				
				
				para = new Paragraph(new Phrase(""+lkh.getCode_Desc(rs.getString("trncd"))+"   :  Rs. "+LetterDAO.leftpaddedzero(TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),rs.getInt("trncd")))+"                           ",f));
				totalinc+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),101);
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(1);
				doc.add(para);
				}	
							

				para = new Paragraph(new Phrase("Total   :  Rs. "+LetterDAO.leftpaddedzero(totalinc)+"                           ",f));
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(5);
				doc.add(para);
				
				int bonus=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),135);
			
				if(bonus!=0)
				{
				para = new Paragraph(new Phrase(""+count+". Bonus - ",f));
				count++;
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Rs. "+LetterDAO.leftpaddedzero(TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),135))+"                           ",f));
				totalinc+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),128);
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(5);
				doc.add(para);
				
				}
				para = new Paragraph(new Phrase(""+count+". Leave Salary - ",f));
				count++;
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				para = new Paragraph(new Phrase("PL No of Days - ",f));
				count++;
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				para = new Paragraph(new Phrase(""+count+". Gratuity - (Basic/26)*15days*0yrs",f));
				count++;
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Last Salary (If Pending)",f));
				count++;
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Notice Pay-",f));
				count++;
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(4);
				doc.add(para);
				
				para = new Paragraph(new Phrase("Total Earnings-",f));
				count++;
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(1);
				doc.add(para);
				para = new Paragraph(new Chunk("                                                                                                                                              " +
						"                                                                                                     ",f).setUnderline(0.1f,-2f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph(new Chunk("PART - I I I- RECOVERY ",f).setUnderline(0.1f,-2f));
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				para.setSpacingAfter(1);
				doc.add(para);
				
				/*para = new Paragraph(new Phrase("AMOUNT                                              ",f));
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(1);
				doc.add(para);*/
				
				int totalded=0;
				para = new Paragraph(new Phrase("1. P.F. CONTRIBUTION                                                            :  Rs. "+LetterDAO.leftpaddedzero(TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),201))+"                           ",f));
				totalded+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),201);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				
				para = new Paragraph(new Phrase("2. PROF. TAX                                                                             :  Rs. "+LetterDAO.leftpaddedzero(TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),202))+"                           ",f));
				totalded+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),202);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				
				para = new Paragraph(new Phrase("3. ESI                                                                                           :  Rs. "+LetterDAO.leftpaddedzero(TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),221))+"                           ",f));
				totalded+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),221);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				
				para = new Paragraph(new Phrase("4. MLWF                                                                                     :  Rs. "+LetterDAO.leftpaddedzero(TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),211))+"                           ",f));
				totalded+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),211);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				
				para = new Paragraph(new Phrase("5. ADVANCE                                                                             :  Rs. "+LetterDAO.leftpaddedzero(TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),225))+"                           ",f));
				totalded+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),225);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				
				para = new Paragraph(new Phrase("6. IMPREST                                                                                :  Rs. "+"                           ",f));
				//totalded+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),code here);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				
				para = new Paragraph(new Phrase("7. NOTICE PAY                                                                          :  Rs. "+"                           ",f));
				//totalded+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),code here);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				
				para = new Paragraph(new Phrase("8. RELIEF FUND                                                                        :  Rs. "+LetterDAO.leftpaddedzero(TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),229))+"                           ",f));
				totalded+=TranHandler.LastSalaryFortdsDisplay((trbn.getEmpno()),229);
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				
				
				para = new Paragraph(new Phrase("9. OTHER RECOVERY like laptop                                       ",f));
				
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				
               para = new Paragraph(new Phrase(" Total Deduction  : Rs  "+LetterDAO.leftpaddedzero(totalded)+"                           ",f));
				para.setAlignment(Element.ALIGN_RIGHT);
				para.setSpacingAfter(1);
				doc.add(para);
				para = new Paragraph(new Chunk("                                                                                                                                              " +
						"                                                                                                     ",f).setUnderline(0.1f,-2f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(5);
				doc.add(para);
				
				para = new Paragraph(new Phrase(" Net Amount Due  : Rs  "+LetterDAO.leftpaddedzero(totalinc-totalded)+"                           ",f));
			    para.setAlignment(Element.ALIGN_RIGHT);
			    para.setSpacingAfter(1);
			    doc.add(para);
				
			    para = new Paragraph(new Phrase(" Amount in Words :-                "+originalNumToLetter.getInWords(""+(totalinc - totalded)),f));
			    para.setAlignment(Element.ALIGN_LEFT);
			    para.setSpacingAfter(1);
			    doc.add(para);
			    
			    para = new Paragraph(new Phrase("Please Check Advances / Loan / Tds And Other At Your End",f));
			    para.setAlignment(Element.ALIGN_LEFT);
			    para.setSpacingAfter(25);
			    doc.add(para);
			    
			    para = new Paragraph(new Phrase("        Prepared By                                                                              " +
			    														 "Checked By                                                                                " +
			    														 "Authorised By         ",f));
			    para.setAlignment(Element.ALIGN_LEFT);
			    para.setSpacingAfter(1);
			    doc.add(para);
			    para = new Paragraph(new Chunk("                                                                                                                                              " +
						"                                                                                                     ",f).setUnderline(0.1f,-2f));
				para.setAlignment(Element.ALIGN_LEFT);
				para.setSpacingAfter(1);
				doc.add(para);
				doc.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
public static String leftpaddedzero(int unpadded)
{
	String un=""+unpadded;
	
	//String padded = "";
	//padded = StringUtils.rightPad(un,12,"_"); //you can also look the rightPad function.
	  //System.out.println(padded);
	String padded = "000000000".substring(un.length()) + unpadded;
	//String padded=  "                                    ".substring(un.length())+unpadded;
	//System.out.println(padded);
	
	return padded;	
}
}
