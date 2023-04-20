package payroll.Controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import payroll.Core.LetterDAO;

/**
 * Servlet implementation class FinalSettlementServlet
 */
@WebServlet("/FinalSettlementServlet")
public class FinalSettlementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FinalSettlementServlet() {
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
		
		String empname = request.getParameter("EMPNO");
		
		String date = request.getParameter("date");
		
		
		


		System.out.println("in new finalsettlement FoolAndFinal letter  ");
		String filename="";
		filename="FoolandFinalLetter.pdf";
		String filePath = getServletContext().getRealPath("")+ File.separator + ""+filename;
		String imagepath =getServletContext().getRealPath("/images/logo.png");
		
		LetterDAO.FoolAndFinal(empname,"",imagepath,filePath, date);
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
