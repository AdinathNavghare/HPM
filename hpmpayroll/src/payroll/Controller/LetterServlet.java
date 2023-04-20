package payroll.Controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import payroll.Core.LetterDAO;
import payroll.Core.UtilityDAO;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Servlet implementation class LetterServlet
 */
@WebServlet("/LetterServlet")
public class LetterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LetterServlet() {
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
		// TODO Auto-generated method stub
		
		String empname = request.getParameter("EMPNO");
		String repfor = request.getParameter("letterType");
		String date = request.getParameter("date");
		
		
		if(repfor.equalsIgnoreCase("EL"))
		{

			System.out.println("in new Experince letter  ");
			String filename="";
			filename="ExperienceLetter.pdf";
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			
			LetterDAO.experienceLetter(empname,repfor,imagepath,filePath, date);
			//UtilityDAO.letter(empname,repfor,imagepath,filepath, date);
			
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
		
		
		if(repfor.equalsIgnoreCase("RL"))
		{

			System.out.println("in new RelievingLetter letter  ");
			String filename="";
			filename="RelievingLetter.pdf";
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			
			LetterDAO.RelievingLetter(empname,repfor,imagepath,filePath, date);
			//UtilityDAO.letter(empname,repfor,imagepath,filepath, date);
			
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
		
		
		if(repfor.equalsIgnoreCase("APL"))
		{

			System.out.println("in new AppraisalLetter letter  ");
			String filename="";
			filename="AppraisalLetter.pdf";
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			
			LetterDAO.appraisalLetter(empname,repfor,imagepath,filePath, date);
			//UtilityDAO.letter(empname,repfor,imagepath,filepath, date);
			
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
		
		
		if(repfor.equalsIgnoreCase("OL"))
		{

			System.out.println("in new Experince letter  ");
			String filename="";
			filename="OfferLetter.pdf";
			String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
			String imagepath =getServletContext().getRealPath("/images/logo.png");
			
			LetterDAO.offerLetter(empname,repfor,imagepath,filePath, date);
			//UtilityDAO.letter(empname,repfor,imagepath,filepath, date);
			
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
		
		
		
		
		
		
		
	}
	
	
	
}
