package payroll.Core;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.ibm.icu.util.Calendar;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class HeaderAndFooter extends PdfPageEventHelper {
	
	protected Phrase footer;
    protected Phrase header;
    PdfTemplate total;
	private String lable1;
	private String label2;
	
    
    private static Font headerFont = new Font(Font.COURIER, 9,
            Font.NORMAL);

    private static Font footerFont = new Font(Font.TIMES_ROMAN, 26,
            Font.BOLD);

    public void onOpenDocument(PdfWriter writer, Document document) {
    	total = writer.getDirectContent().createTemplate(50, 50);
    }
    
    HeaderAndFooter (String lbl,String lbl2)
    {
    	System.out.println("inside ctor"+lbl2);
    	lable1=lbl;
    	label2=lbl2;
    	
    }
    
    
    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        
   	 /*if(PdfNumber.NUMBER>1){
			 PdfPTable datatab1;
	    	 PdfPTable datatot1;*/
    	System.out.println("inside startpage");

		Rectangle rec = new  Rectangle(100,100);
		
		Font f = new Font(Font.HELVETICA,22);
		Font f1 = new Font(Font.HELVETICA,22);
		Font f2 = new Font(Font.HELVETICA,22);
		Font fbold = new Font(Font.HELVETICA, 22, Font.BOLD);	
		//PdfPTable main1 = new PdfPTable(18);	
		PdfPTable main1 = new PdfPTable(19);
		main1.setSpacingBefore(10);
				
		try {
			Paragraph para = new Paragraph();
			if(writer.getPageNumber()!=1 )
			{
				para = new Paragraph(new Phrase(label2,new Font(Font.TIMES_ROMAN,26)));
				para.setAlignment(Element.ALIGN_CENTER);
				para.setSpacingAfter(5);
				document.add(para);
			}

			/*main1.setWidthPercentage(new float[]{4, 15.5f, 5.3f, 5.3f,
					4.5f, 5.3f, 5.3f, 5.3f, 5.3f, 5.3f, 4.8f, 4.8f,
					5.3f, 5.3f, 5.3f, 4.8f, 4.8f, 4.8f}, rec);*/
			
			/*---------------------Prasad-----------------------------*/
			main1.setWidthPercentage(new float[]{4, 13.5f, 5.3f, 
					5.3f, 4.5f, 5.3f, 5.3f, 5.3f, 5.3f, 5.3f, 4.8f, 
					4.8f, 4.3f, 5.3f, 4.3f, 3.8f, 4.8f, 4.8f, 5.0f}, rec);
			
			
			
			} catch (DocumentException e) {e.printStackTrace();	}
				
				main1.setHorizontalAlignment(Element.ALIGN_LEFT);
				PdfPCell maincell1 ;
				
				maincell1 = new PdfPCell(new Phrase("CODE",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				maincell1.disableBorderSide(Rectangle.BOTTOM);
				maincell1.setFixedHeight(70);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("EMPNAME",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				maincell1.disableBorderSide(Rectangle.BOTTOM);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Basic",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("H.R.A",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Col",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Medical",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Conv All",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Education",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
							
								
				maincell1 = new PdfPCell(new Phrase("Min Ins.",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Spl All",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Earn 1",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Earn 2",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Add Income",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("LTA",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
		/*		maincell1 = new PdfPCell(new Phrase("Anonymous",f));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);*/
				maincell1 = new PdfPCell(new Phrase(" ",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase(" ",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("Tot income",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase(" ",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				main1.addCell(maincell1);
				
				maincell1 = new PdfPCell(new Phrase("EMP Status",fbold));
				maincell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				maincell1.disableBorderSide(Rectangle.BOTTOM);
				main1.addCell(maincell1);
				
				
			//	System.out.println("1"+writer.getCurrentDocumentSize());
				//System.out.println("2"+writer.get());
				
				
			try 
			{
				if(writer.getPageNumber()!=1 )
				{
					document.add(main1);
				
					PdfPTable datatab;
					  
					/*datatab = new PdfPTable(18);
				    datatab.setWidthPercentage(new float[]{4, 15.5f, 5.3f, 5.3f,
							4.5f, 5.3f, 5.3f, 5.3f, 5.3f, 5.3f, 4.8f, 4.8f,
							5.3f, 5.3f, 5.3f, 4.8f, 4.8f, 4.8f}, rec);*/
					
					/*-------------------------Prasad-----------------------------*/
					datatab = new PdfPTable(19);
				    datatab.setWidthPercentage(new float[]{4, 13.5f, 5.3f, 
				    		5.3f, 4.5f, 5.3f, 5.3f, 5.3f, 5.3f, 5.3f, 4.8f, 
				    		4.8f, 4.3f, 5.3f, 4.3f, 3.8f, 4.8f, 4.8f, 5.0f}, rec);
				    
				    datatab.setHorizontalAlignment(Element.ALIGN_LEFT);
			    		 
				         
			    		PdfPCell maincell111 = new PdfPCell(new Phrase("",fbold));
			    		maincell111.disableBorderSide(rec.TOP);
			    		maincell111.setFixedHeight(60);
			    		datatab.addCell(maincell111);
						
			    		
			    		PdfPCell maincell2 = new PdfPCell(new Phrase("",fbold));
			    		maincell2.disableBorderSide(rec.TOP);
			    		maincell2.setFixedHeight(60);
			    		datatab.addCell(maincell2);
			    		
			    		
			    		PdfPCell maincell3 = new PdfPCell(new Phrase("PF",fbold));			    	
			    		maincell3.setHorizontalAlignment(Element.ALIGN_CENTER);			    
			    		maincell3.setFixedHeight(60);
			    		datatab.addCell(maincell3);
						
			    		PdfPCell maincell4 = new PdfPCell(new Phrase("E.S.I.C",fbold));			    		
			    		maincell4.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell4);
						
			    		PdfPCell maincell5 = new PdfPCell(new Phrase("P.T",fbold));			    		
			    		maincell5.setHorizontalAlignment(Element.ALIGN_CENTER);			    	
			    		datatab.addCell(maincell5);
						
			    		PdfPCell maincell6 = new PdfPCell(new Phrase("T.D.S",fbold));			    		
			    		maincell6.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell6);
			    		
			    		PdfPCell maincell7 = new PdfPCell(new Phrase("Advance",fbold));			    		
			    		maincell7.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell7);
			    		
			    	
						
			    		PdfPCell maincell8 = new PdfPCell(new Phrase("Loan Ded",fbold));			    		
			    		maincell8.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell8);
						
			    		PdfPCell maincell9 = new PdfPCell(new Phrase("Mobile Ded",fbold));			    		
			    		maincell9.setHorizontalAlignment(Element.ALIGN_CENTER);			    	
			    		datatab.addCell(maincell9);
						
			    		PdfPCell maincell10 = new PdfPCell(new Phrase("Net Charges",fbold));			    	
			    		maincell10.setHorizontalAlignment(Element.ALIGN_CENTER);			    					    	
			    		datatab.addCell(maincell10);
						
			    		PdfPCell maincell11 = new PdfPCell(new Phrase("Current Advance",fbold));		    		
			    		maincell11.setHorizontalAlignment(Element.ALIGN_CENTER);			    					    		
			    		datatab.addCell(maincell11);
			    		
			    		PdfPCell maincell12 = new PdfPCell(new Phrase("Relif",fbold));			    		
			    		maincell12.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell12);
			    		
			    		PdfPCell maincell13 = new PdfPCell(new Phrase("MLWF",fbold));			    		
			    		maincell13.setHorizontalAlignment(Element.ALIGN_CENTER);			   
			    		datatab.addCell(maincell13);
			    		
			    		PdfPCell maincell81 = new PdfPCell(new Phrase("Other",fbold));			    		
			    		maincell81.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell81);
			    		
			    /*		PdfPCell maincell881 = new PdfPCell(new Phrase("",f));			    		
			    		maincell881.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell881);*/
			    		
			    		PdfPCell maincell881 = new PdfPCell(new Phrase("Med Ded",fbold));			    		
			    		maincell881.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell881);
			    		
			    		PdfPCell maincell8811 = new PdfPCell(new Phrase("ERP",fbold));			    		
			    		maincell8811.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell8811);
			    		
			    		PdfPCell maincell14 = new PdfPCell(new Phrase("Tot Ded",fbold));
			    		maincell14.setHorizontalAlignment(Element.ALIGN_CENTER);
			    		datatab.addCell(maincell14);
			    		
			    		PdfPCell maincell15 = new PdfPCell(new Phrase("Net Pay",fbold));
			    		maincell15.setHorizontalAlignment(Element.ALIGN_CENTER);			    		
			    		datatab.addCell(maincell15);
					  
			    	/*------------------------Prasad--------------------------------------------*/
			    		PdfPCell maincell16 = new PdfPCell(new Phrase("",fbold));
			    		maincell16.disableBorderSide(rec.TOP);
			    		maincell16.setFixedHeight(60);
			    		datatab.addCell(maincell16);
					  
			    		
			    		document.add(datatab);
					  }
					  
					  
					  
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
		 }
    	
    
    public void onEndPage(PdfWriter writer, Document document) {
    	
    	PdfPTable table = new PdfPTable(3);
    	try
    	{
    		DateFormat df = new SimpleDateFormat("HH:mm:ss");
    		Calendar calobj = Calendar.getInstance();
    		table.setWidths(new int[]{24,24,2});
    		table.setTotalWidth(2770);
    		table.setLockedWidth(true);
    		
    	//	writer.getPageSize();
    		System.out.println("In H/F.java"+writer.getPageSize().getHeight());
    		
    		table.getDefaultCell().setFixedHeight(30);
    		table.getDefaultCell().setBorder(Rectangle.BOTTOM);
    		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
    	
    		table.addCell(new Phrase(lable1+"          ("+ReportDAO.getSysDate()+" )     Time : " +df.format(calobj.getTime())+"",footerFont));
    		table.addCell(new Phrase(String.format("Page No."+ writer.getPageNumber()),footerFont));
 	
    		PdfPCell cell = new PdfPCell(Image.getInstance(total));
    		cell.setBorder(0);
    		table.addCell(cell);
    		table.writeSelectedRows(0, -1, 10, 60, writer.getDirectContent());
    		
    		
    		
    	} 
    	catch(DocumentException de){
    		throw new ExceptionConverter(de);
    	}
    	
        /*PdfContentByte cb = writer.getDirectContent();
        String headerContent = "";
        try{
	        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(headerContent,headerFont), 
	                document.leftMargin() - 1, document.top() + 30, 0);
	        System.out.println("end no "+writer.getPageNumber());
	        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, new Phrase(String.format("Page No : %d ", 
	                writer.getPageNumber()),footerFont), 
	                document.right() - 2 , document.bottom() - 20, 0);
        } catch(Exception e){
        	e.printStackTrace();
        }*/

    }
    
    public void onCloseDocument(PdfWriter writer, Document document) {
    	ColumnText.showTextAligned(total, Element.ALIGN_BOTTOM, new Phrase(String.valueOf(writer.getPageNumber()-1),footerFont),15,20,0);
    }
    
}