package payroll.Core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import payroll.DAO.ConnectionManager;
import payroll.DAO.LookupHandler;
import payroll.Model.RepoartBean;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Calendar;
import com.lowagie.text.Rectangle;

public class ExcelDAO {

	static String lable1 = "";

	// public static void newpayreg11(String PAYREGDT,String imgpath,String
	// filepath,String type)
	public static void newpayreg11(String PAYREGDT, String imgpath, String filepath, String type) {

		System.out.println("in new pay regdao");

		// this code is for constant property see constant.properties
		Properties prop = new Properties();
		try {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		} catch (Exception e) {
			System.out.println("Error in constant properties Manager " + e);
		}

		RepoartBean repBean = new RepoartBean();

		Connection con = null;
		String BomDt = "";
		String EomDt = "";
		String StartDt = "";
		StartDt = "01-Dec-1900";
		int lastdat = 0;
		// String dt = PAYREGDT;
		String table_name = null;
		/*
		 * if(TName.equals("before") ) { table_name = "PAYTRAN";
		 * lable1="Before Finalize"; } else if(TName.equals("after") ) {
		 * table_name="YTDTRAN"; lable1="Final After Release"; } else {
		 * table_name="PAYTRAN_STAGE"; lable1="Pending for Release"; }
		 */
		// System.out.println(dt);
		lastdat = (int) Calculate.getDays(PAYREGDT);
		System.out.println("maxdt" + lastdat);
		BomDt = ReportDAO.BOM(PAYREGDT);
		// System.out.println("bomdt "+BomDt);
		EomDt = ReportDAO.EOM(PAYREGDT);
		// System.out.println("eomdt"+EomDt);

		String temp = PAYREGDT.substring(3);
		ResultSet emp = null;
		String EmpSql = "";
		String pBrcd1 = "";
		int tot_no_emp = 0;
		int br_tot_no_emp = 0;
		float tot_absents = 0.0f;
		float totmthsal1 = 0.0f;
		float totearning1 = 0.0f;
		float totearning2 = 0.0f;
		float totactualpay = 0.0f;
		float totmobded = 0.0f;
		float totadvanc = 0.0f;
		float totloan = 0.0f;
		float tottds = 0.0f;

		try {
			if (type.equalsIgnoreCase("G")) {
				ReportDAO.OpenCon("", "", "", repBean);
				con = repBean.getCn();
				Statement st = con.createStatement();

				FileOutputStream out1 = new FileOutputStream(new File(filepath));
				HSSFWorkbook hwb = new HSSFWorkbook();
				HSSFSheet sheet = hwb.createSheet("PayRegister");
				Calendar currentMonth = Calendar.getInstance();

				HSSFCellStyle my_style = hwb.createCellStyle();
				HSSFFont my_font = hwb.createFont();
				my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				my_style.setFont(my_font);

				HSSFRow rowtitle = sheet.createRow((short) 0);
				HSSFCell cell = rowtitle.createCell((short) 5);
				cell.setCellValue(prop.getProperty("companyName"));
				cell.setCellStyle(my_style);
				HSSFRow rowtitle1 = sheet.createRow((short) 1);
				HSSFCell cell1 = rowtitle1.createCell((short) 3);
				cell1.setCellValue(prop.getProperty("addressForReport"));
				cell1.setCellStyle(my_style);
				HSSFRow rowtitle2 = sheet.createRow((short) 2);
				HSSFCell cell2 = rowtitle2.createCell((short) 5);
				cell2.setCellValue(prop.getProperty("contactForReport"));
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle31 = sheet.createRow((short) 3);
				cell2 = rowtitle31.createCell((short) 5);
				cell2.setCellValue(prop.getProperty("mailForReport"));
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle3 = sheet.createRow((short) 4);
				cell2 = rowtitle3.createCell((short) 4);
				cell2.setCellValue("Salary Sheet For The Month Of :- " + PAYREGDT);
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle4 = sheet.createRow((short) 5);
				rowtitle4.createCell((short) 0).setCellValue("");
				HSSFRow rowtitle5 = sheet.createRow((short) 6);
				rowtitle5.createCell((short) 0).setCellValue("");

				HSSFFont blueFont = hwb.createFont();
				blueFont.setColor(HSSFColor.BLUE.index);

				HSSFCellStyle style = hwb.createCellStyle();
				// style.setFont(blueFont);
				style.setFillForegroundColor(HSSFColor.BLUE.index);

				HSSFRow head = sheet.createRow((short) 7);
				head.createCell((short) 0).setCellValue("");
				HSSFRow heading = sheet.createRow((short) 8);
				HSSFCell cell3 = heading.createCell((short) 0);
				cell3.setCellValue("");
				cell3.setCellStyle(my_style);
				sheet.setColumnWidth((short) 0, (short) 3000);
				sheet.setColumnWidth((short) 1, (short) 7000);
				sheet.setColumnWidth((short) 4, (short) 4000);
				sheet.setColumnWidth((short) 5, (short) 3000);
				sheet.setColumnWidth((short) 6, (short) 4000);
				sheet.setColumnWidth((short) 7, (short) 3000);
				sheet.setColumnWidth((short) 8, (short) 4000);
				sheet.setColumnWidth((short) 9, (short) 3000);
				sheet.setColumnWidth((short) 10, (short) 3000);
				sheet.setColumnWidth((short) 11, (short) 3000);
				sheet.setColumnWidth((short) 12, (short) 3000);
				sheet.setColumnWidth((short) 13, (short) 3000);

				HSSFRow head1 = sheet.createRow((short) 9);
				head1.createCell((short) 0).setCellValue("");
				HSSFRow rowhead = sheet.createRow((short) 10);
				sheet.createFreezePane(0, 11, 0, 11);

				rowhead.createCell((short) 0).setCellValue("Emp Code.");
				rowhead.createCell((short) 1).setCellValue("Employee Name");
				rowhead.createCell((short) 4).setCellValue("CTC");
				rowhead.createCell((short) 5).setCellValue("LOP Days");
				rowhead.createCell((short) 6).setCellValue("Earning 1");
				rowhead.createCell((short) 7).setCellValue("Earning 2");
				rowhead.createCell((short) 8).setCellValue("Mobile Deduction");
				rowhead.createCell((short) 9).setCellValue("Advance Given");
				rowhead.createCell((short) 10).setCellValue("Loan");
				rowhead.createCell((short) 11).setCellValue("TDS");
				rowhead.createCell((short) 12).setCellValue("Net Pay");
				rowhead.createCell((short) 13).setCellValue("");

				// EmpSql = "select distinct p.empno,e.empcode from
				// "+table_name+" p right join EMPMAST e on e.EMPNO = p.EMPNO
				// where TRNDT BETWEEN '" + BomDt + "' AND '" + EomDt + "' order
				// by p.EMPNO";
				EmpSql = "select distinct p.empno,CONVERT(INT, e.empcode) as empcode,t.PRJ_SRNO,t.PRJ_CODE from paytran p right join EMPMAST e on e.EMPNO = p.EMPNO join EMPTRAN t on p.EMPNO = t.EMPNO where TRNDT BETWEEN '"
						+ BomDt + "' AND '" + EomDt + "' and e.STATUS = 'A'"
						+ "and T.EFFDATE = (SELECT E2.EFFDATE FROM EMPTRAN E2 WHERE E2.EMPNO = e.EMPNO AND E2.EFFDATE <= '"
						+ ReportDAO.EOM(PAYREGDT)
						+ "' and E2.srno=(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = e.EMPNO AND E2.EFFDATE <= '"
						+ ReportDAO.EOM(PAYREGDT) + "')) order by t.PRJ_CODE,empcode";
				System.out.println(EmpSql);
				emp = st.executeQuery(EmpSql);

				List<Integer> results = new ArrayList<Integer>();

				while (emp.next()) {

					results.add(emp.getInt("empno"));
					// System.out.println(emp.getInt("empno"));
				}

				int i = 11;
				for (int empp : results) {

					EmpSql = "select empmast.EMPCODE ,rtrim(empmast.fname)+' '+rtrim(empmast.lname)as  name, doj,t.PRJ_CODE,t.PRJ_SRNO, "
							+ "(select inp_AMT from paytran where trncd = 199 and TRNDT BETWEEN '" + BomDt + "'and '"
							+ EomDt + "' and empno=" + empp + ") ctc, "
							+ "(select cal_amt from paytran where trncd = 301 and TRNDT BETWEEN '" + BomDt + "'and '"
							+ EomDt + "' and empno=" + empp + ") abs_cnt,"
							+ "(select net_amt as earning1 from paytran where trncd = 130 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ") earning1,"
							+ "(select net_amt as earning2 from paytran where trncd = 131 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")earning2,"
							+ "(select net_amt as mobded from paytran where trncd = 223 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")mobded,"
							+ "(select net_amt as added from paytran where trncd = 225 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")added,"
							+ "(select net_amt as loan from paytran where trncd = 227 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")loan,"
							+ "(select net_amt as tds from paytran where trncd = 228 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")tds,"
							+ "(select net_amt as payable from paytran where trncd = 999 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")payable "
							+ "from empmast,EMPTRAN t  where empmast.empno=" + empp + " and t.empno = " + empp + " " +

							"  UNION  "
							+ "select empmast.EMPCODE ,rtrim(empmast.fname)+' '+rtrim(empmast.lname)as  name, doj,t.PRJ_CODE,t.PRJ_SRNO, "
							+ "(select inp_AMT from paytran_stage where trncd = 199 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ") ctc, "
							+ "(select cal_amt from paytran_stage where trncd = 301 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ") abs_cnt,"
							+ "(select net_amt as earning1 from paytran_stage where trncd = 130 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ") earning1,"
							+ "(select net_amt as earning2 from paytran_stage where trncd = 131 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ")earning2,"
							+ "(select net_amt as mobded from paytran_stage where trncd = 223 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ")mobded,"
							+ "(select net_amt as added from paytran_stage where trncd = 225 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ")added,"
							+ "(select net_amt as loan from paytran_stage where trncd = 227 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")loan,"
							+ "(select net_amt as tds from paytran_stage where trncd = 228 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")tds,"
							+ "(select net_amt as payable from paytran_stage where trncd = 999 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ")payable "
							+ "from empmast,EMPTRAN t  where empmast.empno=" + empp + " and t.empno = " + empp;

					// System.out.println(EmpSql);
					// System.out.println(empp);
					emp = st.executeQuery(EmpSql);

					while (emp.next()) {

						HSSFRow row = sheet.createRow((short) i++);
						row.createCell((short) 0).setCellValue("" + emp.getString("empcode"));
						row.createCell((short) 1).setCellValue("");

						row.createCell((short) 1).setCellValue("" + emp.getString("name"));

						float mthsal = 0.0f;
						float absent = 0.0f;

						/*
						 * EmpSql="select inp_AMT from "
						 * +table_name+" where trncd = 199 and TRNDT BETWEEN '"
						 * +BomDt+"'and '"+EomDt+"' and empno="+empp; emp =
						 * st.executeQuery(EmpSql);
						 * //System.out.println(EmpSql); while(emp.next()){
						 * mthsal = emp.getFloat("inp_AMT");
						 * 
						 * //System.out.println("mth salary "+emp.getString(
						 * "inp_AMT")); }
						 */
						mthsal = emp.getFloat("ctc");
						if (mthsal == 0) {
							mthsal = 0;

							// mthsal = 0.0f;
						} else {

							totmthsal1 = totmthsal1 + mthsal;
						}
						// ctc
						row.createCell((short) 4).setCellValue(mthsal);

						/*
						 * EmpSql = "select cal_amt from "
						 * +table_name+" where trncd = 301 and TRNDT BETWEEN '"
						 * +BomDt+"'and '"+EomDt+"' and empno="+empp; emp =
						 * st.executeQuery(EmpSql); if(emp.next()){
						 * 
						 * absent = emp.getFloat("cal_AMT"); }
						 */
						absent = emp.getFloat("abs_cnt");
						// L.O.P days
						row.createCell((short) 5).setCellValue(absent);
						tot_absents += absent;
						float ear1 = 0.0f;
						/*
						 * EmpSql="select net_amt as earning1 from "
						 * +table_name+" where trncd = 130 and TRNDT BETWEEN '"
						 * +BomDt+"'and '"+EomDt+"' and empno="+empp;
						 * //System.out.println(EmpSql); emp =
						 * st.executeQuery(EmpSql); while(emp.next()){ ear1 =
						 * emp.getFloat("earning1");
						 * 
						 * //System.out.println("earning1"+emp.getString(
						 * "earning1")); }
						 */
						ear1 = emp.getFloat("earning1");
						if (ear1 == 0.0) {
							ear1 = 0;

						} else {
							// earning1 = ear1;
							totearning1 = totearning1 + ear1;
						}

						// earning 1
						row.createCell((short) 6).setCellValue(ear1);

						float ear2 = 0.0f;
						/*
						 * EmpSql="select net_amt as earning2 from "
						 * +table_name+" where trncd = 131 and TRNDT BETWEEN '"
						 * +BomDt+"'and '"+EomDt+"' and empno="+empp;
						 * //System.out.println(EmpSql); emp =
						 * st.executeQuery(EmpSql); while(emp.next()){ ear2 =
						 * emp.getFloat("earning2");
						 * 
						 * //System.out.println("earning2"+emp.getString(
						 * "earning2")); }
						 */
						ear2 = emp.getFloat("earning2");
						if (ear2 == 0.0) {
							ear2 = 0;

						} else {

							totearning2 = totearning2 + ear2;
						}

						// earning 2

						row.createCell((short) 7).setCellValue(ear2);

						float mobded = 0.0f;
						/*
						 * EmpSql="select net_amt as mobded from "
						 * +table_name+" where trncd = 223 and TRNDT BETWEEN '"
						 * +BomDt+"'and '"+EomDt+"' and empno="+empp;
						 * //System.out.println(EmpSql); emp =
						 * st.executeQuery(EmpSql); while(emp.next()){ mobded =
						 * emp.getFloat("mobded");
						 * 
						 * //System.out.println("mobded"+emp.getString("mobded")
						 * ); }
						 */
						mobded = emp.getFloat("mobded");
						if (mobded == 0.0) {
							mobded = 0;

						} else {

							totmobded = totmobded + mobded;
						}

						// mobile ded

						row.createCell((short) 8).setCellValue(mobded);

						float advanc = 0.0f;
						/*
						 * EmpSql="select net_amt as added from "
						 * +table_name+" where trncd = 225 and TRNDT BETWEEN '"
						 * +BomDt+"'and '"+EomDt+"' and empno="+empp;
						 * //System.out.println(EmpSql); emp =
						 * st.executeQuery(EmpSql); while(emp.next()){ advanc =
						 * emp.getFloat("added");
						 * 
						 * //System.out.println("advance"+emp.getString("added")
						 * ); }
						 */
						advanc = emp.getFloat("added");
						if (advanc == 0.0) {
							advanc = 0;

						} else {

							totadvanc = totadvanc + advanc;
						}

						// advance given

						row.createCell((short) 9).setCellValue(advanc);

						float loan = 0.0f;
						/*
						 * EmpSql="select net_amt as loan from "
						 * +table_name+" where trncd = 227 and TRNDT BETWEEN '"
						 * +BomDt+"'and '"+EomDt+"' and empno="+empp;
						 * //System.out.println(EmpSql); emp =
						 * st.executeQuery(EmpSql); while(emp.next()){ loan =
						 * emp.getFloat("loan");
						 * 
						 * //System.out.println("loan "+emp.getString("loan"));
						 * }
						 */
						loan = emp.getFloat("loan");
						if (loan == 0.0) {
							loan = 0;

						} else {

							totloan = totloan + loan;
						}

						// loan

						row.createCell((short) 10).setCellValue(loan);

						float tds = 0.0f;
						/*
						 * EmpSql="select net_amt as tds from "
						 * +table_name+" where trncd = 228 and TRNDT BETWEEN '"
						 * +BomDt+"'and '"+EomDt+"' and empno="+empp;
						 * //System.out.println(EmpSql); emp =
						 * st.executeQuery(EmpSql); while(emp.next()){ tds =
						 * emp.getFloat("tds");
						 * 
						 * //System.out.println("tds "+emp.getString("tds")); }
						 */
						tds = emp.getFloat("tds");
						if (tds == 0.0) {
							tds = 0;

						} else {

							tottds = tottds + tds;
						}

						// tds

						row.createCell((short) 11).setCellValue(tds);

						int payable = 0;
						/*
						 * EmpSql="select net_amt as payable from "
						 * +table_name+" where trncd = 999 and TRNDT BETWEEN '"
						 * +BomDt+"'and '"+EomDt+"' and empno="+empp; emp =
						 * st.executeQuery(EmpSql);
						 * //System.out.println(EmpSql); while(emp.next()){
						 * payable = emp.getInt("payable");
						 * 
						 * //System.out.println("payable "+emp.getString(
						 * "payable")); }
						 */
						payable = emp.getInt("payable");
						if (payable == 0) {
							payable = 0;
						} else {

							totactualpay = totactualpay + payable;

						}
						// actual pay

						row.createCell((short) 12).setCellValue(payable);

					}

				}

				NumberFormat format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
				String totpay = format.format(totmthsal1).substring(4);
				String eard1 = format.format(totearning1).substring(4);
				String eard2 = format.format(totearning2).substring(4);
				String totnetpay = format.format(totactualpay).substring(4);
				String tomobded = format.format(totmobded).substring(4);
				String toadv = format.format(totadvanc).substring(4);
				String toloan = format.format(totloan).substring(4);
				String totds = format.format(tottds).substring(4);

				rowhead = sheet.createRow((short) i++);
				rowhead.createCell((short) 0).setCellValue("");
				rowhead.createCell((short) 1).setCellValue("");
				rowhead.createCell((short) 2).setCellValue("TOTAL PAY");
				rowhead.createCell((short) 4).setCellValue(totpay);
				rowhead.createCell((short) 5).setCellValue(tot_absents);
				rowhead.createCell((short) 6).setCellValue(eard1);
				rowhead.createCell((short) 7).setCellValue(eard2);
				rowhead.createCell((short) 8).setCellValue(tomobded);
				rowhead.createCell((short) 9).setCellValue(toadv);
				rowhead.createCell((short) 10).setCellValue(toloan);
				rowhead.createCell((short) 11).setCellValue(totds);
				rowhead.createCell((short) 12).setCellValue(totnetpay);
				rowhead.createCell((short) 14).setCellValue("");

				DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

				Calendar calobj = Calendar.getInstance();
				HSSFRow row = sheet.createRow((short) i);
				row.createCell((short) 0).setCellValue(" ");
				row = sheet.createRow((short) i + 1);
				row.createCell((short) 0).setCellValue("Report Date And Time " + df.format(calobj.getTime()));

				hwb.write(out1);
				out1.close();
				/*
				 * final int BUFSIZE = 4096; File file = new File(filepath); int
				 * length = 0;
				 */

				System.out.println("Your excel file has been generated!");

				st.close();
				con.close();
			}

			else if (type.equalsIgnoreCase("I")) {

				ReportDAO.OpenCon("", "", "", repBean);
				con = repBean.getCn();
				Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

				System.out.println("in income head");
				FileOutputStream out1 = new FileOutputStream(new File(filepath));

				HSSFWorkbook hwb = new HSSFWorkbook();
				HSSFSheet sheet = hwb.createSheet("PayRegister");
				Calendar currentMonth = Calendar.getInstance();

				HSSFCellStyle my_style = hwb.createCellStyle();
				HSSFCellStyle my_style1 = hwb.createCellStyle();

				HSSFFont my_font = hwb.createFont();
				my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				my_style.setFont(my_font);

				HSSFRow rowtitle = sheet.createRow((short) 0);
				HSSFCell cell = rowtitle.createCell((short) 8);
				cell.setCellValue(prop.getProperty("companyName"));
				cell.setCellStyle(my_style);
				HSSFRow rowtitle1 = sheet.createRow((short) 1);
				HSSFCell cell1 = rowtitle1.createCell((short) 6);
				cell1.setCellValue(prop.getProperty("addressForReport"));
				cell1.setCellStyle(my_style);
				HSSFRow rowtitle2 = sheet.createRow((short) 2);
				HSSFCell cell2 = rowtitle2.createCell((short) 8);
				cell2.setCellValue(prop.getProperty("contactForReport"));
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle31 = sheet.createRow((short) 3);
				cell2 = rowtitle31.createCell((short) 8);
				cell2.setCellValue(prop.getProperty("mailForReport"));
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle3 = sheet.createRow((short) 4);
				cell2 = rowtitle3.createCell((short) 7);
				cell2.setCellValue("Salary Sheet For The Month Of :- " + PAYREGDT);
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle4 = sheet.createRow((short) 5);
				rowtitle4.createCell((short) 0).setCellValue("");
				HSSFRow rowtitle5 = sheet.createRow((short) 6);
				rowtitle5.createCell((short) 0).setCellValue("");

				HSSFFont blueFont = hwb.createFont();
				blueFont.setColor(HSSFColor.BLUE.index);

				HSSFCellStyle style = hwb.createCellStyle();
				// style.setFont(blueFont);
				style.setFillForegroundColor(HSSFColor.BLUE.index);

				/*
				 * if(TName.equalsIgnoreCase("after")) { HSSFRow headin =
				 * sheet.createRow((short)8); HSSFCell cell4 = headin
				 * .createCell((short) 6); cell4.
				 * setCellValue("Salary Sheet For The Income Heads for the Month Of :- "
				 * +PAYREGDT+" "); cell4.setCellStyle(my_style);
				 * 
				 * } else if(TName.equalsIgnoreCase("before")) { HSSFRow headin
				 * = sheet.createRow((short)8); HSSFCell cell4 = headin
				 * .createCell((short) 6); cell4.
				 * setCellValue("Salary Sheet For The Income Heads for the Month Of :-  "
				 * +PAYREGDT+" (Before Finalize) ");
				 * cell4.setCellStyle(my_style);
				 * 
				 * } else { HSSFRow headin = sheet.createRow((short)8); HSSFCell
				 * cell4 = headin .createCell((short) 6); cell4.
				 * setCellValue("Salary Sheet For The Income Heads for the Month Of :- "
				 * +PAYREGDT+" (Pending for Release)");
				 * cell4.setCellStyle(my_style);
				 * 
				 * }
				 */

				int i = 10;
				float basic_total = 0.0f;
				float lstbasic_total = 0.0f;
				float currda_total = 0.0f;
				float lstcurrda_total = 0.0f;
				float hra_total = 0.0f;
				float lsthra_total = 0.0f;
				float medical_total = 0.0f;
				float lstmedical_total = 0.0f;
				float edu_total = 0.0f;
				float lstedu_total = 0.0f;
				float splall_total = 0.0f;
				float lstsplall_total = 0.0f;
				float convall_total = 0.0f;
				float lstconvall_total = 0.0f;
				float washingall_total = 0.0f;
				float lstwashingall_total = 0.0f;
				float bonus_total = 0.0f;
				float lstbonus_total = 0.0f;
				float minins_total = 0.0f;
				float lstminins_total = 0.0f;
				float addlss_total = 0.0f;
				float lstaddlss_total = 0.0f;
				float col_total = 0.0f;
				float lstcol_total = 0.0f;
				float special_total = 0.0f;
				float lstspecial_total = 0.0f;
				float addinc_total = 0.0f;
				float lstaddinc_total = 0.0f;
				float totinc_total = 0.0f;
				float lsttotinc_total = 0.0f;
				float totded_total = 0.0f;
				float lsttotded_total = 0.0f;
				float netpay_total = 0.0f;
				float lstnetpay_total = 0.0f;
				float eepf_total = 0.0f;

				EmpSql = "select CONVERT(INT, empmast.empcode) as EMPCODE ,rtrim(empmast.fname)+' '+rtrim(empmast.mname)+' '+rtrim(empmast.lname)as name,empmast.empno,t.PRJ_SRNO,"
						+ "p1.NET_AMT as basic,p2.NET_AMT as payable,p3.NET_AMT as hra,p4.NET_AMT as medical,"
						+ "p5.NET_AMT as education,p6.NET_AMT as convall,p7.NET_AMT as min_ins," + "p10.NET_AMT as col,"
						+ "(select net_amt from  PAYTRAN where TRNCD = 127 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as addless," +
						// "(select net_amt from PAYTRAN where TRNCD = 129 and
						// trndt BETWEEN '"+BomDt+"'and '"+EomDt+"' and EMPNO =
						// empmast.EMPNO) as special, " +
						"(select net_amt from  PAYTRAN where TRNCD = 107 and trndt BETWEEN '" + BomDt + "'and '" + EomDt
						+ "' and EMPNO = empmast.EMPNO) as splall,"
						+ "(select net_amt from  PAYTRAN where TRNCD = 102 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as curda,"
						+ "(select net_amt from  PAYTRAN where TRNCD = 115 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as washall,"
						+ "(select net_amt from  PAYTRAN where TRNCD = 135 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as bonusall,"
						+ "(select SUM(net_amt) from  PAYTRAN where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198) and TRNCD not in(101,103,104,105,108,126,127,128,107,102,115,135) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as addinc,"
						+ "(select SUM(net_amt) from  PAYTRAN where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as totded, "
						+ "(select SUM(net_amt) from  PAYTRAN where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as totear "
						+ "from EMPMAST empmast join "
						+ " PAYTRAN p1 on empmast.EMPNO = p1.EMPNO join  PAYTRAN p2 on empmast.EMPNO = p2.EMPNO "
						+ "join  PAYTRAN p3 on empmast.EMPNO = p3.EMPNO "
						+ "join  PAYTRAN p4 on empmast.EMPNO = p4.EMPNO "
						+ "join  PAYTRAN p5 on empmast.EMPNO = p5.EMPNO "
						+ "join  PAYTRAN p6 on empmast.EMPNO = p6.EMPNO "
						+ "join  PAYTRAN p7 on empmast.EMPNO = p7.EMPNO "
						+ "join  PAYTRAN p10 on empmast.EMPNO = p10.EMPNO "
						+ "join EMPTRAN t on t.EMPNO = empmast.EMPNO where " + "p1.TRNCD = 101 and p1.TRNDT BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' " + "and p2.TRNCD = 999 and p2.TRNDT BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' " + "and p3.TRNCD = 103 and p3.TRNDT BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' " + "and p4.TRNCD = 104 and p4.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p5.TRNCD = 105 and p5.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p6.TRNCD = 108 and p6.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p7.TRNCD = 126 and p7.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p10.TRNCD = 128 and p10.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' " +
						// "and empmast.STATUS = 'A'" +
						" and T.srno =(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = empmast.EMPNO AND E2.EFFDATE <= '"
						+ ReportDAO.EOM(PAYREGDT) + "') " +

						" UNION  select CONVERT(INT, empmast.empcode) as EMPCODE ,rtrim(empmast.fname)+' '+rtrim(empmast.mname)+' '+rtrim(empmast.lname)as name,empmast.empno,t.PRJ_SRNO,"
						+ "p1.NET_AMT as basic,p2.NET_AMT as payable,p3.NET_AMT as hra,p4.NET_AMT as medical,"
						+ "p5.NET_AMT as education,p6.NET_AMT as convall,p7.NET_AMT as min_ins," + "p10.NET_AMT as col,"
						+ "(select net_amt from   paytran_stage  where TRNCD = 127 and trndt BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as addless," +
						// "(select net_amt from paytran_stage where TRNCD = 129
						// and trndt BETWEEN '"+BomDt+"'and '"+EomDt+"' and
						// EMPNO = empmast.EMPNO) as special, " +
						"(select net_amt from   paytran_stage  where TRNCD = 107 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as splall,"
						+ "(select net_amt from   paytran_stage  where TRNCD = 102 and trndt BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as curda,"
						+ "(select net_amt from   paytran_stage  where TRNCD = 115 and trndt BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as washall,"
						+ "(select net_amt from   paytran_stage  where TRNCD = 135 and trndt BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as bonusall,"
						+ "(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198) and TRNCD not in(101,103,104,105,108,126,127,128,107,102,115,135) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as addinc,"
						+ "(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as totded, "
						+ "(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as totear "
						+ "from EMPMAST empmast join "
						+ "  paytran_stage  p1 on empmast.EMPNO = p1.EMPNO join   paytran_stage  p2 on empmast.EMPNO = p2.EMPNO "
						+ "join   paytran_stage  p3 on empmast.EMPNO = p3.EMPNO "
						+ "join   paytran_stage  p4 on empmast.EMPNO = p4.EMPNO "
						+ "join   paytran_stage  p5 on empmast.EMPNO = p5.EMPNO "
						+ "join   paytran_stage  p6 on empmast.EMPNO = p6.EMPNO "
						+ "join   paytran_stage  p7 on empmast.EMPNO = p7.EMPNO "
						+ "join   paytran_stage  p10 on empmast.EMPNO = p10.EMPNO "
						+ "join EMPTRAN t on t.EMPNO = empmast.EMPNO where " + "p1.TRNCD = 101 and p1.TRNDT BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' " + "and p2.TRNCD = 999 and p2.TRNDT BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' " + "and p3.TRNCD = 103 and p3.TRNDT BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' " + "and p4.TRNCD = 104 and p4.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p5.TRNCD = 105 and p5.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p6.TRNCD = 108 and p6.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p7.TRNCD = 126 and p7.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p10.TRNCD = 128 and p10.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' " +
						// "and empmast.STATUS = 'A'" +
						" and T.srno =(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = empmast.EMPNO AND E2.EFFDATE <= '"
						+ ReportDAO.EOM(PAYREGDT) + "') " +

						"order by t.PRJ_SRNO,EMPCODE";
				System.out.println(EmpSql);
				ResultSet rs = st.executeQuery(EmpSql);

				while (rs.next()) {

					String prj_name = null;
					String prj_code = null;
					Connection conn = ConnectionManager.getConnectionTech();
					Statement stmt = conn.createStatement();
					String prjquery = "select Site_Name,Project_Code from Project_Sites where SITE_ID = '"
							+ rs.getString("PRJ_SRNO") + "'";
					// System.out.println(prjquery);
					ResultSet prj = stmt.executeQuery(prjquery);
					if (prj.next()) {
						prj_name = prj.getString("Site_Name");
						prj_code = prj.getString("Project_Code");
					}
					pBrcd1 = rs.getString("PRJ_SRNO");
					br_tot_no_emp = 0;

					HSSFRow head1 = sheet.createRow((short) i++);
					HSSFCell cell4 = head1.createCell((short) 0);
					cell4.setCellValue(
							" Employee's Salary List For Project Site : " + prj_name + " (" + prj_code + ")");
					cell4.setCellStyle(my_style);

					sheet.setColumnWidth((short) 0, (short) 3000);
					sheet.setColumnWidth((short) 1, (short) 7000);
					sheet.setColumnWidth((short) 4, (short) 4000);
					sheet.setColumnWidth((short) 5, (short) 4000);
					sheet.setColumnWidth((short) 6, (short) 4000);
					sheet.setColumnWidth((short) 7, (short) 4000);
					sheet.setColumnWidth((short) 8, (short) 4000);
					sheet.setColumnWidth((short) 9, (short) 4000);
					sheet.setColumnWidth((short) 10, (short) 4000);
					sheet.setColumnWidth((short) 11, (short) 4000);
					sheet.setColumnWidth((short) 12, (short) 4000);
					sheet.setColumnWidth((short) 13, (short) 4000);
					sheet.setColumnWidth((short) 14, (short) 4000);
					sheet.setColumnWidth((short) 15, (short) 4000);
					sheet.setColumnWidth((short) 16, (short) 4000);
					sheet.setColumnWidth((short) 17, (short) 4000);
					sheet.setColumnWidth((short) 18, (short) 4000);
					sheet.setColumnWidth((short) 19, (short) 4000);

					prj.close();
					stmt.close();

					/*
					 * my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					 * my_style1.setFont(my_font);
					 */

					head1 = sheet.createRow((short) i++);
					cell4 = head1.createCell((short) 0);
					cell4.setCellValue("EMP CODE");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 1);
					cell4.setCellValue("EMPNAME");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 4);
					cell4.setCellValue("Basic");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 5);
					cell4.setCellValue("Current D.A");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 6);
					cell4.setCellValue("H.R.A");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 7);
					cell4.setCellValue("Medical");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 8);
					cell4.setCellValue("Education");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 9);
					cell4.setCellValue("Spl All");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 10);
					cell4.setCellValue("Conv All");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 11);
					cell4.setCellValue("Washing All");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 12);
					cell4.setCellValue("Bonus");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 13);
					cell4.setCellValue("Min Insurance");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 14);
					cell4.setCellValue("Add less Amt");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 15);
					cell4.setCellValue("Col");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 16);
					cell4.setCellValue("Add income");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 17);
					cell4.setCellValue("Tot income");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 18);
					cell4.setCellValue("Tot deduction");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 19);
					cell4.setCellValue("Net pay");
					cell4.setCellStyle(my_style1);

					while (pBrcd1.equals(rs.getString("PRJ_SRNO"))) {
						basic_total = basic_total + rs.getFloat("basic");

						head1 = sheet.createRow((short) i++);
						cell4 = head1.createCell((short) 0);
						cell4.setCellValue("" + rs.getString("EMPCODE"));
						cell4 = head1.createCell((short) 1);
						cell4.setCellValue("" + rs.getString("name"));
						cell4 = head1.createCell((short) 4);
						cell4.setCellValue(rs.getFloat("basic"));

						if (rs.getFloat("curda") == 0.0) {
							cell4 = head1.createCell((short) 5);
							cell4.setCellValue("");
						} else {
							float curda = rs.getFloat("curda");
							currda_total = currda_total + curda;
							cell4 = head1.createCell((short) 5);
							cell4.setCellValue(curda);
						}
						hra_total = hra_total + rs.getFloat("hra");

						cell4 = head1.createCell((short) 6);
						cell4.setCellValue(rs.getFloat("hra"));

						medical_total = medical_total = rs.getFloat("medical");
						cell4 = head1.createCell((short) 7);
						cell4.setCellValue(rs.getFloat("medical"));

						edu_total = edu_total = rs.getFloat("education");
						cell4 = head1.createCell((short) 8);
						cell4.setCellValue(rs.getFloat("education"));

						if (rs.getFloat("splall") == 0.0) {
							cell4 = head1.createCell((short) 9);
							cell4.setCellValue("");

						} else {
							float splall1 = rs.getFloat("splall");
							splall_total = splall_total + splall1;
							cell4 = head1.createCell((short) 9);
							cell4.setCellValue(splall1);
						}

						convall_total = convall_total + rs.getFloat("convall");
						cell4 = head1.createCell((short) 10);
						cell4.setCellValue(rs.getFloat("convall"));

						if (rs.getFloat("washall") == 0.0) {
							cell4 = head1.createCell((short) 11);
							cell4.setCellValue("");
						} else {
							float washing = rs.getFloat("washall");
							washingall_total = washingall_total = rs.getFloat("washall");
							cell4 = head1.createCell((short) 11);
							cell4.setCellValue(washing);
						}

						if (rs.getFloat("bonusall") == 0.0) {

							cell4 = head1.createCell((short) 12);
							cell4.setCellValue("");
						} else {
							float bonus = rs.getFloat("bonusall");
							bonus_total = bonus_total + rs.getFloat("bonusall");

							cell4 = head1.createCell((short) 12);
							cell4.setCellValue(bonus);
						}

						minins_total = minins_total + +rs.getFloat("min_ins");
						cell4 = head1.createCell((short) 13);
						cell4.setCellValue(rs.getFloat("min_ins"));

						if (rs.getFloat("addless") == 0.0) {
							cell4 = head1.createCell((short) 14);
							cell4.setCellValue("");
						} else {
							float addless = rs.getFloat("addless");
							addlss_total = addlss_total + addless;
							cell4 = head1.createCell((short) 14);
							cell4.setCellValue(addless);
						}

						col_total = col_total + +rs.getFloat("col");
						cell4 = head1.createCell((short) 15);
						cell4.setCellValue(rs.getFloat("col"));

						/*
						 * if(rs.getFloat("special")==0.0){ PdfPCell cell15 =
						 * new PdfPCell(new Phrase("",f1));
						 * datatab.addCell(cell15); } else{ float spcl =
						 * rs.getFloat("special"); special_total = special_total
						 * + spcl; PdfPCell cell15 = new PdfPCell(new
						 * Phrase(spcl,f1));
						 * cell15.setHorizontalAlignment(Element.ALIGN_RIGHT);
						 * datatab.addCell(cell15); }
						 */

						if (rs.getFloat("addinc") == 0.0) {
							cell4 = head1.createCell((short) 16);
							cell4.setCellValue("");
						} else {
							float add_inc = rs.getFloat("addinc");
							addinc_total = addinc_total + add_inc;
							cell4 = head1.createCell((short) 16);
							cell4.setCellValue(add_inc);
						}

						totinc_total = totinc_total + rs.getFloat("totear");
						cell4 = head1.createCell((short) 17);
						cell4.setCellValue(rs.getFloat("totear"));

						totded_total = totded_total + rs.getFloat("totded");
						cell4 = head1.createCell((short) 18);
						cell4.setCellValue(rs.getFloat("totded"));

						netpay_total = netpay_total + rs.getFloat("payable");
						cell4 = head1.createCell((short) 19);
						cell4.setCellValue(rs.getFloat("payable"));

						tot_no_emp = tot_no_emp + 1;
						br_tot_no_emp = br_tot_no_emp + 1;
						if (!rs.next()) {
							break;
						}
						if (!pBrcd1.equals(rs.getString("PRJ_SRNO"))) {
							rs.previous();
							break;
						}
					}

					head1 = sheet.createRow((short) i++);
					cell4 = head1.createCell((short) 0);
					cell4.setCellValue("");
					cell4 = head1.createCell((short) 1);
					cell4.setCellValue("");
					cell4 = head1.createCell((short) 2);
					cell4.setCellValue("TOTAL :");
					cell4.setCellStyle(my_style);
					cell4 = head1.createCell((short) 4);
					cell4.setCellValue(basic_total);

					lstbasic_total = lstbasic_total + basic_total;
					basic_total = 0.0f;

					cell4 = head1.createCell((short) 5);
					cell4.setCellValue(currda_total);
					lstcurrda_total = lstcurrda_total + currda_total;
					currda_total = 0.0f;

					cell4 = head1.createCell((short) 6);
					cell4.setCellValue(hra_total);
					lsthra_total = lsthra_total + hra_total;
					hra_total = 0.0f;

					cell4 = head1.createCell((short) 7);
					cell4.setCellValue(medical_total);
					lstmedical_total = lstmedical_total + medical_total;
					medical_total = 0.0f;

					cell4 = head1.createCell((short) 8);
					cell4.setCellValue(edu_total);
					lstedu_total = lstedu_total + edu_total;
					edu_total = 0.0f;

					cell4 = head1.createCell((short) 9);
					cell4.setCellValue(splall_total);
					lstsplall_total = lstsplall_total = splall_total;
					splall_total = 0.0f;

					cell4 = head1.createCell((short) 10);
					cell4.setCellValue(convall_total);
					lstconvall_total = lstconvall_total = convall_total;
					convall_total = 0.0f;

					cell4 = head1.createCell((short) 11);
					cell4.setCellValue(washingall_total);
					lstwashingall_total = lstwashingall_total + washingall_total;
					washingall_total = 0.0f;

					cell4 = head1.createCell((short) 12);
					cell4.setCellValue(bonus_total);
					lstbonus_total = lstbonus_total + bonus_total;
					bonus_total = 0.0f;

					cell4 = head1.createCell((short) 13);
					cell4.setCellValue(minins_total);
					lstminins_total = lstminins_total + minins_total;
					minins_total = 0.0f;

					cell4 = head1.createCell((short) 14);
					cell4.setCellValue(addlss_total);
					lstaddlss_total = lstaddlss_total + addlss_total;
					addlss_total = 0.0f;

					cell4 = head1.createCell((short) 15);
					cell4.setCellValue(col_total);
					lstcol_total = lstcol_total + col_total;
					col_total = 0.0f;

					/*
					 * PdfPCell cell15 = new PdfPCell(new
					 * Phrase(special_total,f1));
					 * cell15.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 * datatot.addCell(cell15); lstspecial_total =
					 * lstspecial_total + special_total ; special_total = 0.0f;
					 */

					cell4 = head1.createCell((short) 16);
					cell4.setCellValue(addinc_total);
					lstaddinc_total = lstaddinc_total + addinc_total;
					addinc_total = 0.0f;

					cell4 = head1.createCell((short) 17);
					cell4.setCellValue(totinc_total);
					lsttotinc_total = lsttotinc_total + totinc_total;
					totinc_total = 0.0f;

					cell4 = head1.createCell((short) 18);
					cell4.setCellValue(totded_total);
					lsttotded_total = lsttotded_total + totded_total;
					totded_total = 0.0f;

					cell4 = head1.createCell((short) 19);
					cell4.setCellValue(netpay_total);
					lstnetpay_total = lstnetpay_total + netpay_total;
					netpay_total = 0.0f;

					head1 = sheet.createRow((short) i++);
					head1.createCell((short) 0).setCellValue("Total Employee(s) : " + br_tot_no_emp);
					head1 = sheet.createRow((short) i++);
					head1.createCell((short) 0).setCellValue("");
					head1 = sheet.createRow((short) i++);
					head1.createCell((short) 0).setCellValue("");
				}

				HSSFRow head1 = sheet.createRow((short) i++);
				HSSFCell cell4;
				head1.createCell((short) 0).setCellValue("Total Employee(s) : " + tot_no_emp);

				System.out.println("Total No Of Employee :- " + tot_no_emp);

				head1 = sheet.createRow((short) i++);
				head1 = sheet.createRow((short) i++);
				cell4 = head1.createCell((short) 2);
				cell4.setCellValue("SUMMARY");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 4);
				cell4.setCellValue("Basic");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 5);
				cell4.setCellValue("Current D.A");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 6);
				cell4.setCellValue("H.R.A");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 7);
				cell4.setCellValue("Medical");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 8);
				cell4.setCellValue("Education");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 9);
				cell4.setCellValue("Spl All");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 10);
				cell4.setCellValue("Conv All");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 11);
				cell4.setCellValue("Washing All");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 12);
				cell4.setCellValue("Bonus");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 13);
				cell4.setCellValue("Min Insurance");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 14);
				cell4.setCellValue("Add less Amt");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 15);
				cell4.setCellValue("Col");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 16);
				cell4.setCellValue("Add income");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 17);
				cell4.setCellValue("Tot income");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 18);
				cell4.setCellValue("Tot deduction");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 19);
				cell4.setCellValue("Net pay");
				cell4.setCellStyle(my_style);

				head1 = sheet.createRow((short) i++);
				cell4 = head1.createCell((short) 2);
				cell4.setCellValue("Gross Total:");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 4);
				cell4.setCellValue(lstbasic_total);
				cell4 = head1.createCell((short) 5);
				cell4.setCellValue(lstcurrda_total);
				cell4 = head1.createCell((short) 6);
				cell4.setCellValue(lsthra_total);
				cell4 = head1.createCell((short) 7);
				cell4.setCellValue(lstmedical_total);
				cell4 = head1.createCell((short) 8);
				cell4.setCellValue(lstedu_total);
				cell4 = head1.createCell((short) 9);
				cell4.setCellValue(lstsplall_total);
				cell4 = head1.createCell((short) 10);
				cell4.setCellValue(lstconvall_total);
				cell4 = head1.createCell((short) 11);
				cell4.setCellValue(lstwashingall_total);
				cell4 = head1.createCell((short) 12);
				cell4.setCellValue(lstbonus_total);
				cell4 = head1.createCell((short) 13);
				cell4.setCellValue(lstminins_total);
				cell4 = head1.createCell((short) 14);
				cell4.setCellValue(lstaddlss_total);
				cell4 = head1.createCell((short) 15);
				cell4.setCellValue(lstcol_total);
				cell4 = head1.createCell((short) 16);
				cell4.setCellValue(lstaddinc_total);
				cell4 = head1.createCell((short) 17);
				cell4.setCellValue(lsttotinc_total);
				cell4 = head1.createCell((short) 18);
				cell4.setCellValue(lsttotded_total);
				cell4 = head1.createCell((short) 19);
				cell4.setCellValue(lstnetpay_total);

				DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

				Calendar calobj = Calendar.getInstance();
				HSSFRow row = sheet.createRow((short) i);
				row.createCell((short) 0).setCellValue(" ");
				row = sheet.createRow((short) i + 1);
				row.createCell((short) 0).setCellValue("Report Date And Time " + df.format(calobj.getTime()));

				hwb.write(out1);
				out1.close();
				/*
				 * final int BUFSIZE = 4096; File file = new File(filepath); int
				 * length = 0;
				 */

				System.out.println("last hra = " + lsthra_total);
				System.out.println("last medical = " + lstmedical_total);
				System.out.println("last education = " + lstedu_total);
				System.out.println("last splall = " + lstsplall_total);
				System.out.println("last convall = " + lstconvall_total);
				System.out.println("last washing = " + lstwashingall_total);
				System.out.println("last bonus = " + lstbonus_total);
				System.out.println("last minins = " + lstminins_total);
				System.out.println("last addless = " + lstaddlss_total);
				System.out.println("last col = " + lstcol_total);
				// System.out.println("last special = "+lstspecial_total);
				System.out.println("last addinc = " + lstaddinc_total);
				System.out.println("last totinc = " + lsttotinc_total);
				System.out.println("last totded = " + lsttotded_total);
				System.out.println("last totnetpay = " + lstnetpay_total);

				st.close();
				con.close();

			} else if (type.equalsIgnoreCase("ID")) {

				ReportDAO.OpenCon("", "", "", repBean);
				con = repBean.getCn();
				Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String report_type[] = { "t.PRJ_SRNO", "t.DEPT" };

				FileOutputStream out1 = new FileOutputStream(new File(filepath));

				HSSFWorkbook hwb = new HSSFWorkbook();
				for (int x = 0; x < report_type.length; x++) {
					tot_no_emp = 0;
					HSSFSheet sheet = hwb.createSheet(x == 0 ? "Project" : "Depertment");
					Calendar currentMonth = Calendar.getInstance();

					HSSFCellStyle my_style = hwb.createCellStyle();
					HSSFFont my_font = hwb.createFont();
					my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					my_style.setFont(my_font);

					HSSFRow rowtitle = sheet.createRow((short) 0);
					HSSFCell cell = rowtitle.createCell((short) 11);
					cell.setCellValue(prop.getProperty("companyName"));
					cell.setCellStyle(my_style);
					HSSFRow rowtitle1 = sheet.createRow((short) 1);
					HSSFCell cell1 = rowtitle1.createCell((short) 9);
					cell1.setCellValue(prop.getProperty("addressForReport"));
					cell1.setCellStyle(my_style);
					HSSFRow rowtitle2 = sheet.createRow((short) 2);
					HSSFCell cell2 = rowtitle2.createCell((short) 11);
					cell2.setCellValue(prop.getProperty("contactForReport"));
					cell2.setCellStyle(my_style);
					HSSFRow rowtitle31 = sheet.createRow((short) 3);
					cell2 = rowtitle31.createCell((short) 11);
					cell2.setCellValue(prop.getProperty("mailForReport"));
					cell2.setCellStyle(my_style);
					HSSFRow rowtitle3 = sheet.createRow((short) 4);
					cell2 = rowtitle3.createCell((short) 10);
					cell2.setCellValue("Salary Sheet For The Month Of :- " + PAYREGDT);
					cell2.setCellStyle(my_style);
					HSSFRow rowtitle4 = sheet.createRow((short) 5);
					rowtitle4.createCell((short) 0).setCellValue("");
					HSSFRow rowtitle5 = sheet.createRow((short) 6);
					rowtitle5.createCell((short) 0).setCellValue("");

					HSSFFont blueFont = hwb.createFont();
					blueFont.setColor(HSSFColor.BLUE.index);

					HSSFCellStyle style = hwb.createCellStyle();
					// style.setFont(blueFont);
					style.setFillForegroundColor(HSSFColor.BLUE.index);

					/*
					 * if(TName.equalsIgnoreCase("after")) { HSSFRow headin =
					 * sheet.createRow((short)7); HSSFCell cell4 = headin
					 * .createCell((short) 9); cell4.
					 * setCellValue("Salary Sheet For The Income & Deduction Heads for the Month Of :- "
					 * +PAYREGDT+" "); cell4.setCellStyle(my_style);
					 * 
					 * } else if(TName.equalsIgnoreCase("before")) { HSSFRow
					 * headin = sheet.createRow((short)7); HSSFCell cell4 =
					 * headin .createCell((short) 9); cell4.
					 * setCellValue("Salary Sheet For The Income & Deduction Heads for the Month Of :-  "
					 * +PAYREGDT+" (Before Finalize) ");
					 * cell4.setCellStyle(my_style);
					 * 
					 * } else { HSSFRow headin = sheet.createRow((short)7);
					 * HSSFCell cell4 = headin .createCell((short) 9); cell4.
					 * setCellValue("Salary Sheet For The Income & Deduction Heads for the Month Of :- "
					 * +PAYREGDT+" (Pending for Release)");
					 * cell4.setCellStyle(my_style);
					 * 
					 * }
					 */

					int i = 10;

					Rectangle rec = new Rectangle(100, 100);
					float lsterp_total = 0.0f;
					float lstmed_total = 0.0f;
					float lstother_total = 0.0f;
					float lstCurrAdv_total = 0.0f;
					float lstleavetc_total = 0.0f;
					float lstloan_total = 0.0f;
					float erp_total = 0.0f;
					float med_total = 0.0f;
					float other_total = 0.0f;
					float CurrAdv_total = 0.0f;
					float leavetc_total = 0.0f;
					float loan_total = 0.0f;
					float basic_total = 0.0f;
					float lstbasic_total = 0.0f;
					float lic_total = 0.0f;
					float lstlic_total = 0.0f;
					float hra_total = 0.0f;
					float lsthra_total = 0.0f;
					float medical_total = 0.0f;
					float lstmedical_total = 0.0f;
					float edu_total = 0.0f;
					float lstedu_total = 0.0f;
					float splall_total = 0.0f;
					float lstsplall_total = 0.0f;
					float convall_total = 0.0f;
					float lstconvall_total = 0.0f;
					float pf_total = 0.0f;
					float lstpf_total = 0.0f;
					float pt_total = 0.0f;
					float lstpt_total = 0.0f;
					float mlwf_total = 0.0f;
					float lstmlwf_total = 0.0f;
					float esic_total = 0.0f;
					float lstesic_total = 0.0f;
					float mob_total = 0.0f;
					float lstmob_total = 0.0f;
					float adv_total = 0.0f;
					float lstadv_total = 0.0f;
					float tds_total = 0.0f;
					float lsttds_total = 0.0f;
					float relif_total = 0.0f;
					float lstrelif_total = 0.0f;
					float netc_total = 0.0f;
					float lstnetc_total = 0.0f;
					float minins_total = 0.0f;
					float lstminins_total = 0.0f;
					float addlss_total = 0.0f;
					float lstaddlss_total = 0.0f;
					float col_total = 0.0f;
					float lstcol_total = 0.0f;
					float special_total = 0.0f;
					float lstspecial_total = 0.0f;
					float addinc_total = 0.0f;
					float lstaddinc_total = 0.0f;
					float totinc_total = 0.0f;
					float lsttotinc_total = 0.0f;
					float addded_total = 0.0f;
					float lstaddded_total = 0.0f;
					float totded_total = 0.0f;
					float lsttotded_total = 0.0f;
					float netpay_total = 0.0f;
					float lstnetpay_total = 0.0f;
					float eepf_total = 0.0f;
					float lsteepf_total = 0.0f;
					float eeps_total = 0.0f;
					float lsteeps_total = 0.0f;
					float eedli_total = 0.0f;
					float lsteedli_total = 0.0f;
					float eepfadmin_total = 0.0f;
					float lsteepfadmin_total = 0.0f;
					float eedliadmin_total = 0.0f;
					float lsteedliadmin_total = 0.0f;
					float eesic = 0.0f;
					float lsteesic_total = 0.0f;
					float absentdays_total = 0.0f;
					
					
					/*String[] s=EomDt.split("-");
					System.out.println("Days of Month:"+s[0]);
					int a=Integer.parseInt(s[0]);
					*/
					/*
					 * String EmpSqlNew = "" +
					 * "select STATUS AS st from EMPMAST where STATUS='N' AND DOL>='"
					 * + BomDt + "' AND DOL<='31-Dec-2018'" + EomDt;
					 */

					// EmpSql = " SELECT empcode , RTRIM(EMPMAST.FNAME)+'
					// '+RTRIM(EMPMAST.MNAME)+' '+RTRIM(EMPMAST.LNAME)AS name,"
					// + " EMPMAST.empno, T.PRJ_SRNO,T.DEPT,"
					EmpSql = "         SELECT  empcode , RTRIM(EMPMAST.FNAME)+' '+RTRIM(EMPMAST.MNAME)+' '+RTRIM(EMPMAST.LNAME)AS name, "
							+ "     	EMPMAST.empno,  empmast.DOL, empmast.STATUS, T.PRJ_SRNO,T.DEPT,"

							/*
							 * +
							 * "       CASE WHEN EXISTS(SELECT STATUS FROM EMPMAST WHERE DOL BETWEEN '"
							 * + BomDt + "' AND '" + EomDt +
							 * "' AND empno = EMPMAST.empno)   " +
							 * "		THEN	(SELECT STATUS AS stat  FROM EMPMAST WHERE DOL BETWEEN '"
							 * + BomDt + "' AND '" + EomDt +
							 * "' AND empno = EMPMAST.empno)" +
							 * "		ELSE	(SELECT 0 AS stat) END AS 'stat',"
							 */

							+ "       CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 101 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)   "
							+ "		THEN	(SELECT NET_AMT AS basic  FROM   PAYTRAN WHERE TRNCD = 101 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE	(SELECT 0 AS basic) END AS 'basic',"
							+ "	    CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 999 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS payable  FROM   PAYTRAN WHERE TRNCD = 999 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS payable) END AS 'payable',"
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 103 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS hra  FROM   PAYTRAN WHERE TRNCD = 103 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS hra) END AS 'hra',"
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 104 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS medical  FROM   PAYTRAN WHERE TRNCD = 104 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS medical) END AS 'medical',   " +

							"       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 106 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)		"
							+ "	    THEN		(SELECT NET_AMT AS leavetc  FROM   PAYTRAN WHERE TRNCD = 106 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    ELSE 	(SELECT 0 AS leavetc) END AS 'leavetc', " +

							"       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 105 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)		"
							+ "	    THEN		(SELECT NET_AMT AS education  FROM   PAYTRAN WHERE TRNCD = 105 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    ELSE 	(SELECT 0 AS education) END AS 'education',           " +

							" CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 108 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    THEN			(SELECT NET_AMT AS convall  FROM   PAYTRAN WHERE TRNCD = 108 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS convall) END AS 'convall',"
							+ "       CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 126 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN		(SELECT NET_AMT AS min_ins  FROM   PAYTRAN WHERE TRNCD = 126 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS min_ins) END AS 'min_ins',"
							+ "         CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 128 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN			(SELECT NET_AMT AS col  FROM   PAYTRAN WHERE TRNCD = 128 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS col) END AS 'col',"
							+ "   CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 131 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS er2  FROM   PAYTRAN WHERE TRNCD = 131 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			ELSE				(SELECT 0 AS er2) END AS 'er2',"
							+ "				 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 130 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS er1  FROM   PAYTRAN WHERE TRNCD = 130 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE				(SELECT 0 AS er1) END AS 'er1',  CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 107 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "				THEN (SELECT NET_AMT AS splall  FROM   PAYTRAN WHERE TRNCD = 107  AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)    			"
							+ "	ELSE (SELECT 0 AS splall) END AS 'splall'," +

							"				 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 145 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS ANYm  FROM   PAYTRAN WHERE TRNCD = 145 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE				(SELECT 0 AS ANYm) END AS 'ANY', " +

							"			 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 201 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS pf  FROM   PAYTRAN WHERE TRNCD = 201 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)    			"
							+ "		ELSE		(SELECT 0 AS er2) END AS 'pf',	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 202 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS pt  FROM   PAYTRAN WHERE TRNCD = 202 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt
							+ "' AND empno = EMPMAST.empno)		ELSE			(SELECT 0 AS pt) END AS 'pt', "
							+ "   CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 227 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN			(SELECT NET_AMT AS CurrAdv  FROM   PAYTRAN WHERE TRNCD = 227 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "				ELSE		(SELECT 0 AS CurrAdv) END AS 'CurrAdv',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 221 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS esic  FROM   PAYTRAN WHERE TRNCD = 221 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS esic) END AS 'esic',"
							+ "		 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 223 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN		(SELECT NET_AMT AS mob  FROM   PAYTRAN WHERE TRNCD = 223 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE  (SELECT 0 AS mob) END AS 'mob',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 225 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS adv  FROM   PAYTRAN WHERE TRNCD = 225 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE	(SELECT 0 AS adv) END AS 'adv',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 228 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS tds  FROM   PAYTRAN WHERE TRNCD = 228 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS tds) END AS 'tds'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 211 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS mlwf  FROM   PAYTRAN WHERE TRNCD = 211 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS mlwf) END AS 'mlwf'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 207 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS med  FROM   PAYTRAN WHERE TRNCD = 207 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS med) END AS 'med'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 208 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS erp  FROM   PAYTRAN WHERE TRNCD = 208 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS erp) END AS 'erp'," +

							"	CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 229 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS relif  FROM   PAYTRAN WHERE TRNCD = 229 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)	"
							+ "	ELSE	(SELECT 0 AS relif) END AS 'relif',	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 230 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS netc  FROM   PAYTRAN WHERE TRNCD = 230 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS netc) END AS 'netc',"
							+ " 	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 226 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS loan  FROM   PAYTRAN WHERE TRNCD = 226 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	  ELSE		(SELECT 0 AS loan) END AS 'loan',"
							+ "    case when EXISTS(select SUM(net_amt) from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "    and TRNCD not in(101,103,104,105,106,108,126,128,107,130,131,145)"
							+ "    and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno =  EMPMAST.empno )"
							+ "	 THEN	(select SUM(net_amt) as addinc from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "    and TRNCD not in(101,103,104,105,106,108,126,128,107,130,131,145)"
							+ "    and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno =  EMPMAST.empno) "
							+ "	 ELSE		(SELECT 0 AS addinc) END AS 'addinc',"
							+ "      case when EXISTS(select SUM(net_amt)   from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 201 and 299)"
							+ "     and TRNCD not in(201,202,205,221,223,225,228,229,230,226,211,227,207,208) and trndt BETWEEN '"
							+ BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  THEN	(select SUM(net_amt)  as other from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 201 and 299)"
							+ "     and TRNCD not in(201,202,205,221,223,225,228,229,230,226,211,227,207,208) and trndt BETWEEN '"
							+ BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  ELSE	(SELECT 0 AS other) END AS 'other' ,"
							+ "	  case when EXISTS(select SUM(net_amt) from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 201 and 299)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  THEN			(select SUM(net_amt) as totded from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 201 and 299) "
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt
							+ "' and empno = empmast.empno)	" + "	  ELSE 	(SELECT 0 AS totded) END AS 'totded' ,"
							+ "	  case when EXISTS(select SUM(net_amt) from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno) "
							+ "	  THEN	(select SUM(net_amt) from   PAYTRAN  where TRNCD in    (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  ELSE		(SELECT 0 AS totear) END AS 'totear',"
							
+ " 	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 301 AND TRNDT BETWEEN '"
+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
+ "	THEN	(SELECT NET_AMT AS absentdays  FROM   PAYTRAN WHERE TRNCD = 301 AND TRNDT BETWEEN '"
+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
+ "	  ELSE		(SELECT 0 AS absentdays) END AS 'absentdays'"
							
							
							
							
							+ "     from EMPMAST empmast     join   EMPTRAN t on t.empno = empmast.empno"
							+ "     where      T.srno =(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.empno = empmast.empno AND E2.EFFDATE <= '"
							+ EomDt + "'" + "     and (( empmast.STATUS='A' AND	empmast.DOJ <= '" + EomDt
							+ "') or  (empmast.STATUS ='N' And empmast.DOL>='" + BomDt + "' )) )"
							+ "     AND T.empno in (SELECT DISTINCT empno FROM PAYTRAN WHERE TRNDT BETWEEN '" + BomDt
							+ "' and '" + EomDt + "') " +

						
							
							"    UNION "
							+ "         SELECT  empcode , RTRIM(EMPMAST.FNAME)+' '+RTRIM(EMPMAST.MNAME)+' '+RTRIM(EMPMAST.LNAME)AS name,"
							+ "     	EMPMAST.empno, empmast.DOL, empmast.STATUS, T.PRJ_SRNO,T.DEPT,"

							/*
							 * +
							 * "       CASE WHEN EXISTS(SELECT STATUS FROM EMPMAST WHERE DOL BETWEEN '"
							 * + BomDt + "' AND '" + EomDt +
							 * "' AND empno = EMPMAST.empno)   " +
							 * "		THEN	(SELECT STATUS AS stat  FROM EMPMAST WHERE DOL BETWEEN '"
							 * + BomDt + "' AND '" + EomDt +
							 * "' AND empno = EMPMAST.empno)" +
							 * "		ELSE	(SELECT 0 AS stat) END AS 'stat',"
							 */

							+ "       CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 101 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN	(SELECT NET_AMT AS basic  FROM   PAYTRAN_STAGE WHERE TRNCD = 101 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE	(SELECT 0 AS basic) END AS 'basic',"
							+ "	    CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 999 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS payable  FROM   PAYTRAN_STAGE WHERE TRNCD = 999 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS payable) END AS 'payable',"
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 103 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS hra  FROM   PAYTRAN_STAGE WHERE TRNCD = 103 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS hra) END AS 'hra',"
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 104 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS medical  FROM   PAYTRAN_STAGE WHERE TRNCD = 104 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS medical) END AS 'medical',   " +

							"       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 106 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)		"
							+ "	    THEN		(SELECT NET_AMT AS leavetc  FROM   PAYTRAN_STAGE WHERE TRNCD = 106 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    ELSE 	(SELECT 0 AS leavetc) END AS 'leavetc',  "
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 105 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)		"
							+ "	    THEN		(SELECT NET_AMT AS education  FROM   PAYTRAN_STAGE WHERE TRNCD = 105 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    ELSE 	(SELECT 0 AS education) END AS 'education',  " +

							"          CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 108 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    THEN			(SELECT NET_AMT AS convall  FROM   PAYTRAN_STAGE WHERE TRNCD = 108 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS convall) END AS 'convall',"
							+ "       CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 126 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN		(SELECT NET_AMT AS min_ins  FROM   PAYTRAN_STAGE WHERE TRNCD = 126 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS min_ins) END AS 'min_ins',"
							+ "         CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 128 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN			(SELECT NET_AMT AS col  FROM   PAYTRAN_STAGE WHERE TRNCD = 128 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS col) END AS 'col',"
							+ "   CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 131 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS er2  FROM   PAYTRAN_STAGE WHERE TRNCD = 131 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			ELSE				(SELECT 0 AS er2) END AS 'er2',"
							+ "				 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 130 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS er1  FROM   PAYTRAN_STAGE WHERE TRNCD = 130 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE				(SELECT 0 AS er1) END AS 'er1',  CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 107 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "				THEN (SELECT NET_AMT AS splall  FROM   PAYTRAN_STAGE WHERE TRNCD = 107  AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)    			"
							+ "	ELSE (SELECT 0 AS splall) END AS 'splall'," +

							"				 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 145 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS ANYm  FROM   PAYTRAN WHERE TRNCD = 145 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE				(SELECT 0 AS ANYm) END AS 'ANY', " +

							"			 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 201 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS pf  FROM   PAYTRAN_STAGE WHERE TRNCD = 201 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)    			"
							+ "		ELSE		(SELECT 0 AS er2) END AS 'pf',	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 202 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS pt  FROM   PAYTRAN_STAGE WHERE TRNCD = 202 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt
							+ "' AND empno = EMPMAST.empno)		ELSE			(SELECT 0 AS pt) END AS 'pt', "
							+ "   CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 227 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN			(SELECT NET_AMT AS CurrAdv  FROM   PAYTRAN_STAGE WHERE TRNCD = 227 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "				ELSE		(SELECT 0 AS CurrAdv) END AS 'CurrAdv',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 221 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS esic  FROM   PAYTRAN_STAGE WHERE TRNCD = 221 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS esic) END AS 'esic',"
							+ "		 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 223 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN		(SELECT NET_AMT AS mob  FROM   PAYTRAN_STAGE WHERE TRNCD = 223 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE  (SELECT 0 AS mob) END AS 'mob',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 225 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS adv  FROM   PAYTRAN_STAGE WHERE TRNCD = 225 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE	(SELECT 0 AS adv) END AS 'adv',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 228 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS tds  FROM   PAYTRAN_STAGE WHERE TRNCD = 228 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS tds) END AS 'tds'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 211 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS mlwf  FROM   PAYTRAN_STAGE WHERE TRNCD = 211 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS mlwf) END AS 'mlwf'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 207 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS med  FROM   PAYTRAN_STAGE WHERE TRNCD = 207 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS med) END AS 'med'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 208 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS erp  FROM   PAYTRAN_STAGE WHERE TRNCD = 208 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS erp) END AS 'erp'," +

							"	CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 229 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS relif  FROM   PAYTRAN_STAGE WHERE TRNCD = 229 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)	"
							+ "	ELSE	(SELECT 0 AS relif) END AS 'relif',	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 230 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS netc  FROM   PAYTRAN_STAGE WHERE TRNCD = 230 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS netc) END AS 'netc',"
							+ " 	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 226 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS loan  FROM   PAYTRAN_STAGE WHERE TRNCD = 226 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	  ELSE		(SELECT 0 AS loan) END AS 'loan',"
							+ "    case when EXISTS(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "    and TRNCD not in(101,103,104,105,106,108,126,128,107,130,131,145)"
							+ "    and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno =  EMPMAST.empno )"
							+ "	 THEN	(select SUM(net_amt) as addinc from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "    and TRNCD not in(101,103,104,105,106,108,126,128,107,130,131,145)"
							+ "    and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno =  EMPMAST.empno) "
							+ "	 ELSE		(SELECT 0 AS addinc) END AS 'addinc',"
							+ "      case when EXISTS(select SUM(net_amt)   from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299)"
							+ "     and TRNCD not in(201,202,205,221,223,225,228,229,230,226,211,227,207,208) and trndt BETWEEN '"
							+ BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  THEN	(select SUM(net_amt)  as other from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299)"
							+ "     and TRNCD not in(201,202,205,221,223,225,228,229,230,226,211,227,207,208) and trndt BETWEEN '"
							+ BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  ELSE	(SELECT 0 AS other) END AS 'other' ,"
							+ "	  case when EXISTS(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  THEN			(select SUM(net_amt) as totded from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299) "
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt
							+ "' and empno = empmast.empno)	" + "	  ELSE 	(SELECT 0 AS totded) END AS 'totded' ,"
							+ "	  case when EXISTS(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno) "
							+ "	  THEN	(select SUM(net_amt) from   paytran_stage  where TRNCD in    (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  ELSE		(SELECT 0 AS totear) END AS 'totear',"
							
+ " 	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 301 AND TRNDT BETWEEN '"
+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
+ "	THEN	(SELECT NET_AMT AS absentdays  FROM   PAYTRAN_STAGE WHERE TRNCD = 301 AND TRNDT BETWEEN '"
+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
+ "	  ELSE		(SELECT 0 AS absentdays) END AS 'absentdays'"
							
							
							
							
							+ "     from EMPMAST empmast     join   EMPTRAN t on t.empno = empmast.empno"
							+ "     where      T.srno =(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.empno = empmast.empno AND E2.EFFDATE <= '"
							+ EomDt + "'" + "     and (( empmast.STATUS='A' AND	empmast.DOJ <= '" + EomDt
							+ "') or  (empmast.STATUS ='N' And empmast.DOL>='" + BomDt + "' )) )"
							+ "     AND T.empno in (SELECT DISTINCT empno FROM PAYTRAN_STAGE WHERE TRNDT BETWEEN '"
							+ BomDt + "' and '" + EomDt + "') " + "     order by   t.PRJ_SRNO,empno  ";
					System.out.println(EmpSql);

					System.out.println(EmpSql);
					ResultSet rs = st.executeQuery(EmpSql);

					while (rs.next()) {
						i++;
						String prj_name = null;
						String prj_code = null;
						/*int absentdays=(int)rs.getFloat("absentdays");
						
						int presentdays=a-absentdays;
						System.out.println("Present Days:"+ presentdays);
			*/
						
						int dept = 0;
						Connection conn = ConnectionManager.getConnectionTech();

						if (report_type[x].equalsIgnoreCase("t.prj_srno")) {
							// System.out.println("into if");
							String prjquery = "select Site_Name,Project_Code from Project_Sites where SITE_ID = '"
									+ rs.getString("PRJ_SRNO") + "'";
							// System.out.println(prjquery);
							Statement stmt = conn.createStatement();
							ResultSet prj = stmt.executeQuery(prjquery);
							if (prj.next()) {
								prj_name = prj.getString("Site_Name");
								prj_code = prj.getString("Project_Code");
							}
							pBrcd1 = rs.getString("PRJ_SRNO");
							br_tot_no_emp = 0;

							HSSFRow head1 = sheet.createRow((short) i++);
							HSSFCell cell4 = head1.createCell((short) 0);
							cell4.setCellValue(
									" Employee's Salary List For Project Site : " + prj_name + " (" + prj_code + ")");
							cell4.setCellStyle(my_style);
							prj.close();
						} else {

							Connection ccn = ConnectionManager.getConnection();
							Statement sts = ccn.createStatement();
							String stmt = "select distinct(lkp_srno),lkp_disc from lookup where lkp_code='DEPT' and lkp_srno="
									+ rs.getInt("DEPT") + "";
							ResultSet rrs = sts.executeQuery(stmt);
							// System.out.println(stmt);
							if (rrs.next()) {
								dept = rrs.getInt("lkp_srno");
								prj_name = rrs.getString("lkp_disc");
							}
							// LookupHandler lkp=new LookupHandler();
							// int lk=rs.getInt("DEPT");
							// prj_name=
							// lkp.getLKP_Desc("DEPT",rs.getInt("DEPT"));
							pBrcd1 = Integer.toString(rs.getInt("DEPT"));
							br_tot_no_emp = 0;

							HSSFRow head1 = sheet.createRow((short) i++);
							HSSFCell cell4 = head1.createCell((short) 0);
							cell4.setCellValue(" Employee's Salary List For Deptartment : " + prj_name + "");
							cell4.setCellStyle(my_style);

							ccn.close();
						}

						sheet.setColumnWidth((short) 0, (short) 3000);
						sheet.setColumnWidth((short) 1, (short) 7000);
						sheet.setColumnWidth((short) 4, (short) 4000);
						sheet.setColumnWidth((short) 5, (short) 4000);
						sheet.setColumnWidth((short) 6, (short) 4000);
						sheet.setColumnWidth((short) 7, (short) 4000);
						sheet.setColumnWidth((short) 8, (short) 4000);
						sheet.setColumnWidth((short) 9, (short) 4000);
						sheet.setColumnWidth((short) 10, (short) 4000);
						sheet.setColumnWidth((short) 11, (short) 4000);
						sheet.setColumnWidth((short) 12, (short) 4000);
						sheet.setColumnWidth((short) 13, (short) 4000);
						sheet.setColumnWidth((short) 14, (short) 4000);
						sheet.setColumnWidth((short) 15, (short) 4000);
						sheet.setColumnWidth((short) 16, (short) 4000);
						sheet.setColumnWidth((short) 17, (short) 4000);
						sheet.setColumnWidth((short) 18, (short) 4000);
						sheet.setColumnWidth((short) 19, (short) 4000);
						sheet.setColumnWidth((short) 20, (short) 4000);
						sheet.setColumnWidth((short) 21, (short) 4000);
						sheet.setColumnWidth((short) 22, (short) 4000);
						sheet.setColumnWidth((short) 23, (short) 4000);
						sheet.setColumnWidth((short) 24, (short) 4000);
						sheet.setColumnWidth((short) 25, (short) 4000);
						sheet.setColumnWidth((short) 26, (short) 4000);
						sheet.setColumnWidth((short) 27, (short) 4000);
						sheet.setColumnWidth((short) 28, (short) 4000);
						sheet.setColumnWidth((short) 29, (short) 4000);
						sheet.setColumnWidth((short) 30, (short) 4000);
						sheet.setColumnWidth((short) 31, (short) 4000);
						sheet.setColumnWidth((short) 32, (short) 4000);

						/*---------Prasad----------*/
						sheet.setColumnWidth((short) 33, (short) 4000);
						
						sheet.setColumnWidth((short) 34, (short) 4000);
						/*sheet.setColumnWidth((short) 35, (short) 4000);
*/
						/*
						 * sheet.setColumnWidth((short)33, (short)4000);
						 * sheet.setColumnWidth((short)34, (short)4000);
						 * sheet.setColumnWidth((short)35, (short)4000);
						 * sheet.setColumnWidth((short)36, (short)4000);
						 */

						conn.close();

						HSSFRow rowhead = sheet.createRow((short) i++);

						rowhead.createCell((short) 0).setCellValue("EMP CODE.");
						rowhead.createCell((short) 1).setCellValue("EMPLOYEE NAME");
						rowhead.createCell((short) 4).setCellValue("BASIC");
						rowhead.createCell((short) 5).setCellValue("HRA");
						rowhead.createCell((short) 6).setCellValue("COL");
						rowhead.createCell((short) 7).setCellValue("MEDICAL");
						rowhead.createCell((short) 8).setCellValue("CONV ALL");
						rowhead.createCell((short) 9).setCellValue("EDUCATION");
						rowhead.createCell((short) 10).setCellValue("MIN INS");
						rowhead.createCell((short) 11).setCellValue("SPL All");
						rowhead.createCell((short) 12).setCellValue("EARN 1");
						rowhead.createCell((short) 13).setCellValue("EARN 2");
						rowhead.createCell((short) 14).setCellValue("ADD INCOME");
						rowhead.createCell((short) 15).setCellValue("LTA");
						rowhead.createCell((short) 16).setCellValue("TOTAL INCOME");
						rowhead.createCell((short) 17).setCellValue("PF");
						rowhead.createCell((short) 18).setCellValue("ESIC");
						rowhead.createCell((short) 19).setCellValue("PT");
						rowhead.createCell((short) 20).setCellValue("TDS");
						rowhead.createCell((short) 21).setCellValue("ADVANCE");
						rowhead.createCell((short) 22).setCellValue("LOAN DED");
						rowhead.createCell((short) 23).setCellValue("MOB DED");
						rowhead.createCell((short) 24).setCellValue("NET CHARGES");
						rowhead.createCell((short) 25).setCellValue("CURRENT ADVANCE");
						rowhead.createCell((short) 26).setCellValue("RELEAF FUND");
						rowhead.createCell((short) 27).setCellValue("MLWF");
						rowhead.createCell((short) 28).setCellValue("OTHER");
						rowhead.createCell((short) 29).setCellValue("MED DED");
						rowhead.createCell((short) 30).setCellValue("ERP");
						rowhead.createCell((short) 31).setCellValue("Tot DED");
						rowhead.createCell((short) 32).setCellValue("NET PAY");

						/*---------Prasad----------*/
						rowhead.createCell((short) 33).setCellValue("EMP STATUS");
						rowhead.createCell((short) 34).setCellValue("ABSENT DAYS");
						/*rowhead.createCell((short) 34).setCellValue("PRESENT DAYS");
*/
						/*
						 * rowhead.createCell((short)
						 * 33).setCellValue("Employee's PF");
						 * rowhead.createCell((short) 31).setCellValue(" EPF");
						 * rowhead.createCell((short) 32).setCellValue(" EPS");
						 * rowhead.createCell((short) 33).setCellValue(" EDLI");
						 * rowhead.createCell((short)
						 * 34).setCellValue(" EPF-Admin Chr");
						 * rowhead.createCell((short)
						 * 35).setCellValue(" EDLI-Admin Chr");
						 * rowhead.createCell((short) 36).setCellValue("");
						 * rowhead.createCell((short) 37).setCellValue(" ESIC");
						 * rowhead.createCell((short)
						 * 38).setCellValue("Employer ESIC");
						 */

						while (report_type[x].equalsIgnoreCase("t.prj_srno") ? pBrcd1.equals(rs.getString("PRJ_SRNO"))
								: dept == rs.getInt("DEPT")) {

							rowhead = sheet.createRow((short) i++);
							rowhead.createCell((short) 0).setCellValue("" + rs.getString("EMPCODE"));

							rowhead.createCell((short) 1).setCellValue("" + rs.getString("name"));

							basic_total = basic_total + rs.getFloat("basic");
							rowhead.createCell((short) 4).setCellValue((int) rs.getFloat("basic"));

							hra_total = hra_total + rs.getFloat("hra");
							rowhead.createCell((short) 5).setCellValue((int) rs.getFloat("hra"));

							col_total = col_total + +rs.getFloat("col");
							rowhead.createCell((short) 6).setCellValue((int) rs.getFloat("col"));

							medical_total = medical_total + rs.getFloat("medical");
							rowhead.createCell((short) 7).setCellValue((int) rs.getFloat("medical"));

							convall_total = convall_total + rs.getFloat("convall");
							rowhead.createCell((short) 8).setCellValue((int) rs.getFloat("convall"));

							edu_total = edu_total + rs.getFloat("education");
							rowhead.createCell((short) 9).setCellValue((int) rs.getFloat("education"));

							minins_total = minins_total + +rs.getFloat("min_ins");
							rowhead.createCell((short) 10).setCellValue((int) rs.getFloat("min_ins"));

							if (rs.getFloat("splall") == 0.0) {
								rowhead.createCell((short) 11).setCellValue(0.00);

							} else {
								float splall1 = rs.getFloat("splall");
								splall_total = splall_total + splall1;

								rowhead.createCell((short) 11).setCellValue((int) splall1);
							}

							if (rs.getFloat("er1") == 0.0) {

								rowhead.createCell((short) 12).setCellValue(0.00);
							} else {
								float addless = rs.getFloat("er1");
								addlss_total = addlss_total + addless;

								rowhead.createCell((short) 12).setCellValue((int) addless);
							}

							if (rs.getFloat("er2") == 0.0) {
								rowhead.createCell((short) 13).setCellValue(0.00);
							} else {
								float spcl = rs.getFloat("er2");
								special_total = special_total + spcl;
								rowhead.createCell((short) 13).setCellValue((int) spcl);

							}

							if (rs.getFloat("addinc") == 0.0) {
								rowhead.createCell((short) 14).setCellValue(0.00);

							} else {
								float add_inc = rs.getFloat("addinc");
								addinc_total = addinc_total + add_inc;

								rowhead.createCell((short) 14).setCellValue((int) add_inc);
							}

							if (rs.getFloat("leavetc") == 0.0) {
								rowhead.createCell((short) 15).setCellValue(0.00);

							} else {
								float leavetc = rs.getFloat("leavetc");
								leavetc_total = leavetc_total + leavetc;

								rowhead.createCell((short) 15).setCellValue((int) leavetc);
							}

							totinc_total = totinc_total + rs.getFloat("totear");

							rowhead.createCell((short) 16).setCellValue((int) rs.getFloat("totear"));

							if (rs.getFloat("pf") == 0.0) {
								rowhead.createCell((short) 17).setCellValue(0.00);

							} else {
								float pf = rs.getFloat("pf");
								pf_total = pf_total + pf;

								rowhead.createCell((short) 17).setCellValue((int) pf);
							}

							if (rs.getFloat("esic") == 0.0f) {
								rowhead.createCell((short) 18).setCellValue(0.00);

							} else {
								float esic = rs.getFloat("esic");
								esic_total = esic_total + esic;
								rowhead.createCell((short) 18).setCellValue((int) esic);

							}

							if (rs.getFloat("pt") == 0.0) {
								rowhead.createCell((short) 19).setCellValue(0.00);

							} else {
								float pt = rs.getFloat("pt");
								pt_total = pt_total + pt;

								rowhead.createCell((short) 19).setCellValue((int) pt);

							}

							if (rs.getFloat("tds") == 0.0f) {
								rowhead.createCell((short) 20).setCellValue(0.00);
							} else {
								float tds = rs.getFloat("tds");
								tds_total = tds_total + tds;
								rowhead.createCell((short) 20).setCellValue((int) tds);

							}

							if (rs.getFloat("adv") == 0.0f) {
								rowhead.createCell((short) 21).setCellValue(0.00);

							} else {
								float adv = rs.getFloat("adv");
								adv_total = adv_total + adv;

								rowhead.createCell((short) 21).setCellValue((int) adv);

							}

							if (rs.getFloat("loan") == 0.0) {
								rowhead.createCell((short) 22).setCellValue(0.00);

							} else {
								float loan = rs.getFloat("loan");
								loan_total = loan_total + loan;

								rowhead.createCell((short) 22).setCellValue((int) loan);

							}

							if (rs.getFloat("mob") == 0.0f) {
								rowhead.createCell((short) 23).setCellValue(0.00);

							} else {
								float mob = rs.getFloat("mob");
								mob_total = mob_total + mob;
								rowhead.createCell((short) 23).setCellValue((int) mob);

							}

							if (rs.getFloat("netc") == 0.0f) {
								rowhead.createCell((short) 24).setCellValue(0.00);
							} else {
								float netc = rs.getFloat("netc");
								netc_total = netc_total + netc;
								rowhead.createCell((short) 24).setCellValue((int) netc);
							}

							if (rs.getFloat("CurrAdv") == 0.0) {
								rowhead.createCell((short) 25).setCellValue(0.00);
							} else {
								float CurrAdv = rs.getFloat("CurrAdv");
								CurrAdv_total = CurrAdv_total + CurrAdv;
								rowhead.createCell((short) 25).setCellValue((int) CurrAdv);

							}

							if (rs.getFloat("relif") == 0.0f) {
								rowhead.createCell((short) 26).setCellValue(0.00);
							} else {
								float relif = rs.getFloat("relif");
								relif_total = relif_total + relif;

								rowhead.createCell((short) 26).setCellValue((int) relif);
							}

							if (rs.getFloat("mlwf") == 0.0) {
								rowhead.createCell((short) 27).setCellValue(0.00);
							} else {
								float mlwf = rs.getFloat("mlwf");
								mlwf_total = mlwf_total + mlwf;

								rowhead.createCell((short) 27).setCellValue((int) mlwf);

							}

							if (rs.getFloat("other") == 0.0) {
								rowhead.createCell((short) 28).setCellValue(0.00);
							} else {
								float other = rs.getFloat("other");
								other_total = other_total + other;

								rowhead.createCell((short) 28).setCellValue((int) other);

							}

							if (rs.getFloat("med") == 0.0) {
								rowhead.createCell((short) 29).setCellValue(0.00);
							} else {
								float med = rs.getFloat("med");
								med_total = med_total + med;

								rowhead.createCell((short) 29).setCellValue((int) med);

							}

							if (rs.getFloat("erp") == 0.0) {
								rowhead.createCell((short) 30).setCellValue(0.00);
							} else {
								float erp = rs.getFloat("erp");
								erp_total = erp_total + erp;

								rowhead.createCell((short) 30).setCellValue((int) erp);

							}

							totded_total = totded_total + rs.getFloat("totded");

							rowhead.createCell((short) 31).setCellValue((int) rs.getFloat("totded"));

							netpay_total = netpay_total + rs.getFloat("payable");

							rowhead.createCell((short) 32).setCellValue((int) rs.getFloat("payable"));

							/*---------------Prasad---------------------*/

							if (rs.getString("DOL") != null)
							// if(rs.getString("stat")=="A")
							{
								rowhead.createCell((short) 33).setCellValue("Non Active");
							}

							else {
								rowhead.createCell((short) 33).setCellValue("Active");
							}
							
							rowhead.createCell((short) 34).setCellValue((int) rs.getFloat("absentdays"));
							/*rowhead.createCell((short) 35).setCellValue(presentdays);*/
							/*
							 * rowhead.createCell((short)
							 * 30).setCellValue((int)rs.getFloat("pf"));
							 * rowhead.createCell((short)
							 * 31).setCellValue((int)rs.getFloat("eepf"));
							 * rowhead.createCell((short)
							 * 32).setCellValue((int)rs.getFloat("eeps"));
							 * rowhead.createCell((short)
							 * 33).setCellValue((int)rs.getFloat("edli"));
							 * rowhead.createCell((short)
							 * 34).setCellValue((int)rs.getFloat("epfadmin"));
							 * rowhead.createCell((short)
							 * 35).setCellValue((int)rs.getFloat("edliadmin"));
							 * rowhead.createCell((short) 36).setCellValue("");
							 * rowhead.createCell((short)
							 * 37).setCellValue((int)rs.getFloat("esic"));
							 * rowhead.createCell((short)
							 * 38).setCellValue((int)rs.getFloat("eesic"));
							 */

							/*
							 * eepf_total+=rs.getFloat("eepf");
							 * 
							 * eeps_total+=rs.getFloat("eeps");
							 * 
							 * eedli_total+=rs.getFloat("edli");
							 * 
							 * eepfadmin_total+=rs.getFloat("epfadmin");
							 * 
							 * eedliadmin_total+=rs.getFloat("edliadmin");
							 * 
							 * eesic+=rs.getFloat("eesic");
							 */

							tot_no_emp = tot_no_emp + 1;
							br_tot_no_emp = br_tot_no_emp + 1;
							if (!rs.next()) {
								break;
							}
							if (report_type[x].equalsIgnoreCase("t.prj_srno")) {

								if (!pBrcd1.equals(rs.getString("PRJ_SRNO"))) {
									rs.previous();
									break;
								}
							} else {
								if (dept != rs.getInt("DEPT")) {
									rs.previous();

									break;
								}
							}

						}
						rowhead = sheet.createRow((short) i++);
						rowhead.createCell((short) 0).setCellValue("");

						rowhead.createCell((short) 1).setCellValue("");
						rowhead.createCell((short) 3).setCellValue("TOTAL :--");

						rowhead.createCell((short) 4).setCellValue((int) basic_total);
						rowhead.createCell((short) 5).setCellValue(hra_total);
						rowhead.createCell((short) 6).setCellValue(col_total);
						rowhead.createCell((short) 7).setCellValue(medical_total);
						rowhead.createCell((short) 8).setCellValue((int) convall_total);
						rowhead.createCell((short) 9).setCellValue((int) edu_total);
						rowhead.createCell((short) 10).setCellValue((int) minins_total);

						lstbasic_total = lstbasic_total + basic_total;
						basic_total = 0.0f;

						lsthra_total = lsthra_total + hra_total;
						hra_total = 0.0f;
						lstcol_total = lstcol_total + col_total;
						col_total = 0.0f;
						lstmedical_total = lstmedical_total + medical_total;
						medical_total = 0.0f;

						lstconvall_total = lstconvall_total + convall_total;
						convall_total = 0.0f;

						lstedu_total = lstedu_total + edu_total;
						edu_total = 0.0f;

						lstminins_total = lstminins_total + minins_total;
						minins_total = 0.0f;

						rowhead.createCell((short) 11).setCellValue((int) splall_total);
						rowhead.createCell((short) 12).setCellValue((int) addlss_total);
						rowhead.createCell((short) 13).setCellValue((int) special_total);
						rowhead.createCell((short) 14).setCellValue((int) addinc_total);
						rowhead.createCell((short) 15).setCellValue((int) leavetc_total);
						rowhead.createCell((short) 16).setCellValue((int) totinc_total);
						rowhead.createCell((short) 17).setCellValue((int) pf_total);

						lstleavetc_total = lstleavetc_total + leavetc_total;
						leavetc_total = 0.0f;

						lstsplall_total = lstsplall_total + splall_total;
						splall_total = 0.0f;

						lstaddlss_total = lstaddlss_total + addlss_total;
						addlss_total = 0.0f;

						lstspecial_total = lstspecial_total + special_total;
						special_total = 0.0f;

						lstaddinc_total = lstaddinc_total + addinc_total;
						addinc_total = 0.0f;

						lsttotinc_total = lsttotinc_total + totinc_total;
						totinc_total = 0.0f;

						lstpf_total = lstpf_total + pf_total;

						rowhead.createCell((short) 18).setCellValue((int) esic_total);
						rowhead.createCell((short) 19).setCellValue((int) pt_total);
						rowhead.createCell((short) 20).setCellValue((int) tds_total);
						rowhead.createCell((short) 21).setCellValue((int) adv_total);

						lstesic_total = lstesic_total + esic_total;

						lstpt_total = lstpt_total + pt_total;
						pt_total = 0.0f;

						lsttds_total = lsttds_total + tds_total;
						tds_total = 0.0f;

						lstadv_total = lstadv_total + adv_total;
						adv_total = 0.0f;

						rowhead.createCell((short) 22).setCellValue((int) loan_total);
						rowhead.createCell((short) 23).setCellValue((int) mob_total);
						rowhead.createCell((short) 24).setCellValue((int) netc_total);
						rowhead.createCell((short) 25).setCellValue((int) CurrAdv_total);
						rowhead.createCell((short) 26).setCellValue((int) relif_total);
						rowhead.createCell((short) 27).setCellValue((int) mlwf_total);
						lstCurrAdv_total = lstCurrAdv_total + CurrAdv_total;
						CurrAdv_total = 0.0f;

						lstloan_total = lstloan_total + loan_total;
						loan_total = 0.0f;

						lstmlwf_total = lstmlwf_total + mlwf_total;
						mlwf_total = 0.0f;

						lstmob_total = lstmob_total + mob_total;
						mob_total = 0.0f;

						lstnetc_total = lstnetc_total + netc_total;
						netc_total = 0.0f;

						lstlic_total = lstlic_total + lic_total;
						lic_total = 0.0f;

						lstrelif_total = lstrelif_total + relif_total;
						relif_total = 0.0f;

						rowhead.createCell((short) 28).setCellValue((int) other_total);
						rowhead.createCell((short) 29).setCellValue((int) med_total);
						rowhead.createCell((short) 30).setCellValue((int) erp_total);

						lstother_total = lstother_total + other_total;
						other_total = 0.0f;

						lstmed_total = lstmed_total + med_total;
						med_total = 0.0f;

						lsterp_total = lsterp_total + erp_total;
						erp_total = 0.0f;

						// lstaddded_total = lstaddded_total + addded_total ;
						// addded_total = 0.0f;

						rowhead.createCell((short) 31).setCellValue((int) totded_total);
						rowhead.createCell((short) 32).setCellValue((int) netpay_total);
						// rowhead.createCell((short)
						// 32).setCellValue((int)eeps_total);

						/*
						 * rowhead.createCell((short)
						 * 33).setCellValue((int)eedli_total);
						 * rowhead.createCell((short)
						 * 34).setCellValue((int)eepfadmin_total);
						 * rowhead.createCell((short)
						 * 35).setCellValue((int)eedliadmin_total);
						 * rowhead.createCell((short) 36).setCellValue("");
						 * rowhead.createCell((short)
						 * 37).setCellValue((int)esic_total);
						 * rowhead.createCell((short)
						 * 38).setCellValue((int)eesic);
						 */

						lsttotded_total = lsttotded_total + totded_total;
						totded_total = 0.0f;

						lstnetpay_total = lstnetpay_total + netpay_total;
						netpay_total = 0.0f;

						pf_total = 0.0f;
						esic_total = 0.0f;
						lsteepf_total += eepf_total;
						eepf_total = 0;

						lsteeps_total += eeps_total;
						eeps_total = 0;

						lsteedli_total += eedli_total;
						eedli_total = 0;

						lsteepfadmin_total += eepfadmin_total;
						eepfadmin_total = 0;

						lsteedliadmin_total += eedliadmin_total;
						eedliadmin_total = 0;

						lsteesic_total += eesic;
						eesic = 0;

						rowhead = sheet.createRow((short) i++);

						rowhead.createCell((short) 0).setCellValue("Total Employee(s) : " + br_tot_no_emp);

						rowhead = sheet.createRow((short) i++);

						rowhead.createCell((short) 0).setCellValue("");
					}

					HSSFRow rowhead = sheet.createRow((short) i++);

					rowhead.createCell((short) 0).setCellValue("Total Employee(s) : " + tot_no_emp);

					// System.out.println("Total No Of Employee :-
					// "+tot_no_emp);

					rowhead = sheet.createRow((short) i++);
					HSSFCell cell4;
					rowhead.createCell((short) 10).setCellValue("  ");
					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("------- SUMMARY -------");
					cell4.setCellStyle(my_style);

					rowhead = sheet.createRow((short) i++);

					rowhead.createCell((short) 10).setCellValue("  ");
					rowhead = sheet.createRow((short) i++);

					rowhead.createCell((short) 10).setCellValue("  ");

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("BASIC Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstbasic_total);

					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("HRA Total:");
					cell4.setCellStyle(my_style);

					rowhead.createCell((short) 8).setCellValue(lsthra_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("Medical Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue(lstmedical_total);
					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("Conv Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lstconvall_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("MIN_INS Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 23).setCellValue((int) lstminins_total);

					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("SPECIAL ALLOW Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstsplall_total);
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("EARN 1 Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue((int) lstaddlss_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("EARN 2 Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstspecial_total);

					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("ADD Income Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lstaddinc_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("Total INCOME:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 23).setCellValue((int) lsttotinc_total);

					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("PF Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstpf_total);
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("ESIC Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue((int) lstesic_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("P.T Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstpt_total);
					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("T.D.S Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lsttds_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("ADVANCE Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 23).setCellValue((int) lstadv_total);

					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("LOAN Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstloan_total);
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("MOB Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue((int) lstmob_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("NETC  Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstnetc_total);

					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("LTA Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lstleavetc_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("RELEAFE Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 23).setCellValue((int) lstrelif_total);

					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("MED Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstmed_total);
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("ERP Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue((int) lsterp_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("CURRENT ADV  Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstCurrAdv_total);

					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("MLWF Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lstmlwf_total);
					/*
					 * cell4 = rowhead .createCell((short) 21);
					 * cell4.setCellValue("EDLI-Admin Chr Total:");
					 * cell4.setCellStyle(my_style);
					 * rowhead.createCell((short)23).setCellValue((int)
					 * lsteedliadmin_total);
					 */

					rowhead = sheet.createRow((short) i++);
					/*
					 * cell4 = rowhead .createCell((short) 1);
					 * cell4.setCellValue("ESIC Total:");
					 * cell4.setCellStyle(my_style); rowhead.createCell((short)
					 * 3).setCellValue((int)lsteesic_total);
					 */
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue("");
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("ADDED Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstaddded_total);
					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("Total Deduction:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lsttotded_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("NET PAY Total:");
					cell4.setCellStyle(my_style);

					float tttt = 0.0f;
					try {
						// System.out.println("date===="+date);
						Statement sttt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
						rs = st.executeQuery("select sum(net_amt) from paytran where trndt between '"
								+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
								+ "'  and trncd=999  union  "
								+ "select sum(net_amt) from paytran_stage where trndt between '"
								+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "'  and trncd=999 ");

						while (rs.next()) {
							tttt += rs.getFloat(1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					rowhead.createCell((short) 23).setCellValue((int) tttt);
					DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

					Calendar calobj = Calendar.getInstance();
					HSSFRow row = sheet.createRow((short) i);
					row.createCell((short) 0).setCellValue(" ");
					row = sheet.createRow((short) i + 1);
					row.createCell((short) 0).setCellValue("Report Date And Time " + df.format(calobj.getTime()));

					/*
					 * FileOutputStream fileOut = new
					 * FileOutputStream(filepath);//"D:\\PFList.xls"
					 */
					/*
					 * final int BUFSIZE = 4096; File file = new File(filepath);
					 * int length = 0;
					 */

					/*
					 * if(TName.equalsIgnoreCase("after")) { para = new
					 * Paragraph(new
					 * Phrase("Prepared By                         Checked By                        Approved By                 "
					 * ,new Font(Font.TIMES_ROMAN,35,Font.BOLD)));
					 * para.setAlignment(Element.ALIGN_RIGHT); doc.add(para); }
					 */

					/*
					 * System.out.println("last basic = "+lstbasic_total);
					 * System.out.println("last hra = "+lsthra_total);
					 * System.out.println("last medical = "+lstmedical_total);
					 * System.out.println("last education = "+lstedu_total);
					 * System.out.println("last splall = "+lstsplall_total);
					 * System.out.println("last convall = "+lstconvall_total);
					 * System.out.println("last minins = "+lstminins_total);
					 * System.out.println("last addless = "+lstaddlss_total);
					 * System.out.println("last col = "+lstcol_total);
					 * System.out.println("last special = "+lstspecial_total);
					 * System.out.println("last pf = "+lstpf_total);
					 * System.out.println("last pt = "+lstpt_total);
					 * System.out.println("last lic = "+lstlic_total);
					 * System.out.println("last mlwf = "+lstmlwf_total);
					 * System.out.println("last esic = "+lstesic_total);
					 * System.out.println("last mob = "+lstmob_total);
					 * System.out.println("last adv = "+lstadv_total);
					 * System.out.println("last tds = "+lsttds_total);
					 * System.out.println("last relif = "+lstrelif_total);
					 * System.out.println("last netc = "+lstnetc_total);
					 * System.out.println("last addinc = "+lstaddinc_total);
					 * System.out.println("last addded = "+lstaddded_total);
					 * System.out.println("last totinc = "+lsttotinc_total);
					 * System.out.println("last totded = "+lsttotded_total);
					 * System.out.println("last totnetpay = "+lstnetpay_total);
					 */

					/* doc.close(); */

				}
				hwb.write(out1);
				out1.close();
				st.close();
				con.close();
			}
		} catch (Exception e) {
			System.out.println("into excel type");
			e.printStackTrace();
		}

	}

	/***************************************************** Prasad ******************************************************************************/

	public static void newpayreg11PF(String PAYREGDT, String imgpath, String filepath, String type) {
		System.out.println("in new pay regdao");

		// this code is for constant property see constant.properties
		Properties prop = new Properties();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		} catch (Exception e) {
			System.out.println("Error in constant properties Manager " + e);
		}

		RepoartBean repBean = new RepoartBean();

		Connection con = null;
		String BomDt = "";
		String EomDt = "";
		String StartDt = "";
		StartDt = "01-Dec-1900";
		int lastdat = 0;
		String table_name = null;

		lastdat = (int) Calculate.getDays(PAYREGDT);
		System.out.println("maxdt" + lastdat);
		BomDt = ReportDAO.BOM(PAYREGDT);
		EomDt = ReportDAO.EOM(PAYREGDT);

		String temp = PAYREGDT.substring(3);
		ResultSet emp = null;
		String EmpSql = "";
		String pBrcd1 = "";
		int tot_no_emp = 0;
		int br_tot_no_emp = 0;
		float tot_absents = 0.0f;
		float totmthsal1 = 0.0f;
		float totearning1 = 0.0f;
		float totearning2 = 0.0f;
		float totactualpay = 0.0f;
		float totmobded = 0.0f;
		float totadvanc = 0.0f;
		float totloan = 0.0f;
		float tottds = 0.0f;

		try {
			if (type.equalsIgnoreCase("G")) {
				ReportDAO.OpenCon("", "", "", repBean);
				con = repBean.getCn();
				Statement st = con.createStatement();

				FileOutputStream out1 = new FileOutputStream(new File(filepath));
				HSSFWorkbook hwb = new HSSFWorkbook();
				HSSFSheet sheet = hwb.createSheet("PayRegister");
				Calendar currentMonth = Calendar.getInstance();

				HSSFCellStyle my_style = hwb.createCellStyle();
				HSSFFont my_font = hwb.createFont();
				my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				my_style.setFont(my_font);

				HSSFRow rowtitle = sheet.createRow((short) 0);
				HSSFCell cell = rowtitle.createCell((short) 5);
				cell.setCellValue(prop.getProperty("companyName"));
				cell.setCellStyle(my_style);
				HSSFRow rowtitle1 = sheet.createRow((short) 1);
				HSSFCell cell1 = rowtitle1.createCell((short) 3);
				cell1.setCellValue(prop.getProperty("addressForReport"));
				cell1.setCellStyle(my_style);
				HSSFRow rowtitle2 = sheet.createRow((short) 2);
				HSSFCell cell2 = rowtitle2.createCell((short) 5);
				cell2.setCellValue(prop.getProperty("contactForReport"));
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle31 = sheet.createRow((short) 3);
				cell2 = rowtitle31.createCell((short) 5);
				cell2.setCellValue(prop.getProperty("mailForReport"));
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle3 = sheet.createRow((short) 4);
				cell2 = rowtitle3.createCell((short) 4);
				cell2.setCellValue("Salary Sheet For The Month Of :- " + PAYREGDT);
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle4 = sheet.createRow((short) 5);
				rowtitle4.createCell((short) 0).setCellValue("");
				HSSFRow rowtitle5 = sheet.createRow((short) 6);
				rowtitle5.createCell((short) 0).setCellValue("");

				HSSFFont blueFont = hwb.createFont();
				blueFont.setColor(HSSFColor.BLUE.index);

				HSSFCellStyle style = hwb.createCellStyle();
				style.setFillForegroundColor(HSSFColor.BLUE.index);

				HSSFRow head = sheet.createRow((short) 7);
				head.createCell((short) 0).setCellValue("");
				HSSFRow heading = sheet.createRow((short) 8);
				HSSFCell cell3 = heading.createCell((short) 0);
				cell3.setCellValue("");
				cell3.setCellStyle(my_style);
				sheet.setColumnWidth((short) 0, (short) 3000);
				sheet.setColumnWidth((short) 1, (short) 7000);
				sheet.setColumnWidth((short) 4, (short) 4000);
				sheet.setColumnWidth((short) 5, (short) 3000);
				sheet.setColumnWidth((short) 6, (short) 4000);
				sheet.setColumnWidth((short) 7, (short) 3000);
				sheet.setColumnWidth((short) 8, (short) 4000);
				sheet.setColumnWidth((short) 9, (short) 3000);
				sheet.setColumnWidth((short) 10, (short) 3000);
				sheet.setColumnWidth((short) 11, (short) 3000);
				sheet.setColumnWidth((short) 12, (short) 3000);
				sheet.setColumnWidth((short) 13, (short) 3000);

				HSSFRow head1 = sheet.createRow((short) 9);
				head1.createCell((short) 0).setCellValue("");
				HSSFRow rowhead = sheet.createRow((short) 10);
				sheet.createFreezePane(0, 11, 0, 11);

				rowhead.createCell((short) 0).setCellValue("Emp Code.");
				rowhead.createCell((short) 1).setCellValue("Employee Name");
				rowhead.createCell((short) 4).setCellValue("CTC");
				rowhead.createCell((short) 5).setCellValue("LOP Days");
				rowhead.createCell((short) 6).setCellValue("Earning 1");
				rowhead.createCell((short) 7).setCellValue("Earning 2");
				rowhead.createCell((short) 8).setCellValue("Mobile Deduction");
				rowhead.createCell((short) 9).setCellValue("Advance Given");
				rowhead.createCell((short) 10).setCellValue("Loan");
				rowhead.createCell((short) 11).setCellValue("TDS");
				rowhead.createCell((short) 12).setCellValue("Net Pay");
				rowhead.createCell((short) 13).setCellValue("");

				EmpSql = "select distinct p.empno,CONVERT(INT, e.empcode) as empcode,t.PRJ_SRNO,t.PRJ_CODE from paytran p right join EMPMAST e on e.EMPNO = p.EMPNO join EMPTRAN t on p.EMPNO = t.EMPNO where TRNDT BETWEEN '"
						+ BomDt + "' AND '" + EomDt + "' and e.STATUS = 'A'"
						+ "and T.EFFDATE = (SELECT E2.EFFDATE FROM EMPTRAN E2 WHERE E2.EMPNO = e.EMPNO AND E2.EFFDATE <= '"
						+ ReportDAO.EOM(PAYREGDT)
						+ "' and E2.srno=(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = e.EMPNO AND E2.EFFDATE <= '"
						+ ReportDAO.EOM(PAYREGDT) + "')) order by t.PRJ_CODE,empcode";
				System.out.println(EmpSql);
				emp = st.executeQuery(EmpSql);

				List<Integer> results = new ArrayList<Integer>();

				while (emp.next()) {

					results.add(emp.getInt("empno"));
				}

				int i = 11;
				for (int empp : results) {

					EmpSql = "select empmast.EMPCODE ,rtrim(empmast.fname)+' '+rtrim(empmast.lname)as  name, doj,t.PRJ_CODE,t.PRJ_SRNO, "
							+ "(select inp_AMT from paytran where trncd = 199 and TRNDT BETWEEN '" + BomDt + "'and '"
							+ EomDt + "' and empno=" + empp + ") ctc, "
							+ "(select cal_amt from paytran where trncd = 301 and TRNDT BETWEEN '" + BomDt + "'and '"
							+ EomDt + "' and empno=" + empp + ") abs_cnt,"
							+ "(select net_amt as earning1 from paytran where trncd = 130 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ") earning1,"
							+ "(select net_amt as earning2 from paytran where trncd = 131 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")earning2,"
							+ "(select net_amt as mobded from paytran where trncd = 223 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")mobded,"
							+ "(select net_amt as added from paytran where trncd = 225 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")added,"
							+ "(select net_amt as loan from paytran where trncd = 227 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")loan,"
							+ "(select net_amt as tds from paytran where trncd = 228 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")tds,"
							+ "(select net_amt as payable from paytran where trncd = 999 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")payable "
							+ "from empmast,EMPTRAN t  where empmast.empno=" + empp + " and t.empno = " + empp + " " +

							"  UNION  "
							+ "select empmast.EMPCODE ,rtrim(empmast.fname)+' '+rtrim(empmast.lname)as  name, doj,t.PRJ_CODE,t.PRJ_SRNO, "
							+ "(select inp_AMT from paytran_stage where trncd = 199 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ") ctc, "
							+ "(select cal_amt from paytran_stage where trncd = 301 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ") abs_cnt,"
							+ "(select net_amt as earning1 from paytran_stage where trncd = 130 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ") earning1,"
							+ "(select net_amt as earning2 from paytran_stage where trncd = 131 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ")earning2,"
							+ "(select net_amt as mobded from paytran_stage where trncd = 223 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ")mobded,"
							+ "(select net_amt as added from paytran_stage where trncd = 225 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ")added,"
							+ "(select net_amt as loan from paytran_stage where trncd = 227 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")loan,"
							+ "(select net_amt as tds from paytran_stage where trncd = 228 and TRNDT BETWEEN '" + BomDt
							+ "'and '" + EomDt + "' and empno=" + empp + ")tds,"
							+ "(select net_amt as payable from paytran_stage where trncd = 999 and TRNDT BETWEEN '"
							+ BomDt + "'and '" + EomDt + "' and empno=" + empp + ")payable "
							+ "from empmast,EMPTRAN t  where empmast.empno=" + empp + " and t.empno = " + empp;

					emp = st.executeQuery(EmpSql);

					while (emp.next()) {

						HSSFRow row = sheet.createRow((short) i++);
						row.createCell((short) 0).setCellValue("" + emp.getString("empcode"));
						row.createCell((short) 1).setCellValue("");

						row.createCell((short) 1).setCellValue("" + emp.getString("name"));

						float mthsal = 0.0f;
						float absent = 0.0f;

						mthsal = emp.getFloat("ctc");
						if (mthsal == 0) {
							mthsal = 0;

						} else {

							totmthsal1 = totmthsal1 + mthsal;
						}

						row.createCell((short) 4).setCellValue(mthsal);

						absent = emp.getFloat("abs_cnt");

						row.createCell((short) 5).setCellValue(absent);
						tot_absents += absent;
						float ear1 = 0.0f;

						ear1 = emp.getFloat("earning1");
						if (ear1 == 0.0) {
							ear1 = 0;
						} else {
							totearning1 = totearning1 + ear1;
						}

						row.createCell((short) 6).setCellValue(ear1);

						float ear2 = 0.0f;
						ear2 = emp.getFloat("earning2");
						if (ear2 == 0.0) {
							ear2 = 0;

						} else {

							totearning2 = totearning2 + ear2;
						}

						row.createCell((short) 7).setCellValue(ear2);

						float mobded = 0.0f;

						mobded = emp.getFloat("mobded");
						if (mobded == 0.0) {
							mobded = 0;

						} else {

							totmobded = totmobded + mobded;
						}

						row.createCell((short) 8).setCellValue(mobded);

						float advanc = 0.0f;
						advanc = emp.getFloat("added");
						if (advanc == 0.0) {
							advanc = 0;

						} else {

							totadvanc = totadvanc + advanc;
						}

						row.createCell((short) 9).setCellValue(advanc);

						float loan = 0.0f;
						loan = emp.getFloat("loan");
						if (loan == 0.0) {
							loan = 0;

						} else {

							totloan = totloan + loan;
						}

						row.createCell((short) 10).setCellValue(loan);

						float tds = 0.0f;
						tds = emp.getFloat("tds");
						if (tds == 0.0) {
							tds = 0;

						} else {

							tottds = tottds + tds;
						}
						row.createCell((short) 11).setCellValue(tds);

						int payable = 0;
						payable = emp.getInt("payable");
						if (payable == 0) {
							payable = 0;
						} else {

							totactualpay = totactualpay + payable;

						}
						row.createCell((short) 12).setCellValue(payable);

					}

				}

				NumberFormat format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
				String totpay = format.format(totmthsal1).substring(4);
				String eard1 = format.format(totearning1).substring(4);
				String eard2 = format.format(totearning2).substring(4);
				String totnetpay = format.format(totactualpay).substring(4);
				String tomobded = format.format(totmobded).substring(4);
				String toadv = format.format(totadvanc).substring(4);
				String toloan = format.format(totloan).substring(4);
				String totds = format.format(tottds).substring(4);

				rowhead = sheet.createRow((short) i++);
				rowhead.createCell((short) 0).setCellValue("");
				rowhead.createCell((short) 1).setCellValue("");
				rowhead.createCell((short) 2).setCellValue("TOTAL PAY");
				rowhead.createCell((short) 4).setCellValue(totpay);
				rowhead.createCell((short) 5).setCellValue(tot_absents);
				rowhead.createCell((short) 6).setCellValue(eard1);
				rowhead.createCell((short) 7).setCellValue(eard2);
				rowhead.createCell((short) 8).setCellValue(tomobded);
				rowhead.createCell((short) 9).setCellValue(toadv);
				rowhead.createCell((short) 10).setCellValue(toloan);
				rowhead.createCell((short) 11).setCellValue(totds);
				rowhead.createCell((short) 12).setCellValue(totnetpay);
				rowhead.createCell((short) 14).setCellValue("");

				DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

				Calendar calobj = Calendar.getInstance();
				HSSFRow row = sheet.createRow((short) i);
				row.createCell((short) 0).setCellValue(" ");
				row = sheet.createRow((short) i + 1);
				row.createCell((short) 0).setCellValue("Report Date And Time " + df.format(calobj.getTime()));

				hwb.write(out1);
				out1.close();

				System.out.println("Your excel file has been generated!");

				st.close();
				con.close();
			}

			else if (type.equalsIgnoreCase("I")) {

				ReportDAO.OpenCon("", "", "", repBean);
				con = repBean.getCn();
				Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

				System.out.println("in income head");
				FileOutputStream out1 = new FileOutputStream(new File(filepath));

				HSSFWorkbook hwb = new HSSFWorkbook();
				HSSFSheet sheet = hwb.createSheet("PayRegister");
				Calendar currentMonth = Calendar.getInstance();

				HSSFCellStyle my_style = hwb.createCellStyle();
				HSSFCellStyle my_style1 = hwb.createCellStyle();

				HSSFFont my_font = hwb.createFont();
				my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				my_style.setFont(my_font);

				HSSFRow rowtitle = sheet.createRow((short) 0);
				HSSFCell cell = rowtitle.createCell((short) 8);
				cell.setCellValue(prop.getProperty("companyName"));
				cell.setCellStyle(my_style);
				HSSFRow rowtitle1 = sheet.createRow((short) 1);
				HSSFCell cell1 = rowtitle1.createCell((short) 6);
				cell1.setCellValue(prop.getProperty("addressForReport"));
				cell1.setCellStyle(my_style);
				HSSFRow rowtitle2 = sheet.createRow((short) 2);
				HSSFCell cell2 = rowtitle2.createCell((short) 8);
				cell2.setCellValue(prop.getProperty("contactForReport"));
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle31 = sheet.createRow((short) 3);
				cell2 = rowtitle31.createCell((short) 8);
				cell2.setCellValue(prop.getProperty("mailForReport"));
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle3 = sheet.createRow((short) 4);
				cell2 = rowtitle3.createCell((short) 7);
				cell2.setCellValue("Salary Sheet For The Month Of :- " + PAYREGDT);
				cell2.setCellStyle(my_style);
				HSSFRow rowtitle4 = sheet.createRow((short) 5);
				rowtitle4.createCell((short) 0).setCellValue("");
				HSSFRow rowtitle5 = sheet.createRow((short) 6);
				rowtitle5.createCell((short) 0).setCellValue("");

				HSSFFont blueFont = hwb.createFont();
				blueFont.setColor(HSSFColor.BLUE.index);

				HSSFCellStyle style = hwb.createCellStyle();
				style.setFillForegroundColor(HSSFColor.BLUE.index);

				int i = 10;
				float basic_total = 0.0f;
				float lstbasic_total = 0.0f;
				float currda_total = 0.0f;
				float lstcurrda_total = 0.0f;
				float hra_total = 0.0f;
				float lsthra_total = 0.0f;
				float medical_total = 0.0f;
				float lstmedical_total = 0.0f;
				float edu_total = 0.0f;
				float lstedu_total = 0.0f;
				float splall_total = 0.0f;
				float lstsplall_total = 0.0f;
				float convall_total = 0.0f;
				float lstconvall_total = 0.0f;
				float washingall_total = 0.0f;
				float lstwashingall_total = 0.0f;
				float bonus_total = 0.0f;
				float lstbonus_total = 0.0f;
				float minins_total = 0.0f;
				float lstminins_total = 0.0f;
				float addlss_total = 0.0f;
				float lstaddlss_total = 0.0f;
				float col_total = 0.0f;
				float lstcol_total = 0.0f;
				float special_total = 0.0f;
				float lstspecial_total = 0.0f;
				float addinc_total = 0.0f;
				float lstaddinc_total = 0.0f;
				float totinc_total = 0.0f;
				float lsttotinc_total = 0.0f;
				float totded_total = 0.0f;
				float lsttotded_total = 0.0f;
				float netpay_total = 0.0f;
				float lstnetpay_total = 0.0f;
				float eepf_total = 0.0f;

				EmpSql = "select CONVERT(INT, empmast.empcode) as EMPCODE ,rtrim(empmast.fname)+' '+rtrim(empmast.mname)+' '+rtrim(empmast.lname)as name,empmast.empno,t.PRJ_SRNO,"
						+ "p1.NET_AMT as basic,p2.NET_AMT as payable,p3.NET_AMT as hra,p4.NET_AMT as medical,"
						+ "p5.NET_AMT as education,p6.NET_AMT as convall,p7.NET_AMT as min_ins," + "p10.NET_AMT as col,"
						+ "(select net_amt from  PAYTRAN where TRNCD = 127 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as addless," +
						// "(select net_amt from PAYTRAN where TRNCD = 129 and
						// trndt BETWEEN '"+BomDt+"'and '"+EomDt+"' and EMPNO =
						// empmast.EMPNO) as special, " +
						"(select net_amt from  PAYTRAN where TRNCD = 107 and trndt BETWEEN '" + BomDt + "'and '" + EomDt
						+ "' and EMPNO = empmast.EMPNO) as splall,"
						+ "(select net_amt from  PAYTRAN where TRNCD = 102 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as curda,"
						+ "(select net_amt from  PAYTRAN where TRNCD = 115 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as washall,"
						+ "(select net_amt from  PAYTRAN where TRNCD = 135 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as bonusall,"
						+ "(select SUM(net_amt) from  PAYTRAN where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198) and TRNCD not in(101,103,104,105,108,126,127,128,107,102,115,135) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as addinc,"
						+ "(select SUM(net_amt) from  PAYTRAN where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as totded, "
						+ "(select SUM(net_amt) from  PAYTRAN where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as totear "
						+ "from EMPMAST empmast join "
						+ " PAYTRAN p1 on empmast.EMPNO = p1.EMPNO join  PAYTRAN p2 on empmast.EMPNO = p2.EMPNO "
						+ "join  PAYTRAN p3 on empmast.EMPNO = p3.EMPNO "
						+ "join  PAYTRAN p4 on empmast.EMPNO = p4.EMPNO "
						+ "join  PAYTRAN p5 on empmast.EMPNO = p5.EMPNO "
						+ "join  PAYTRAN p6 on empmast.EMPNO = p6.EMPNO "
						+ "join  PAYTRAN p7 on empmast.EMPNO = p7.EMPNO "
						+ "join  PAYTRAN p10 on empmast.EMPNO = p10.EMPNO "
						+ "join EMPTRAN t on t.EMPNO = empmast.EMPNO where " + "p1.TRNCD = 101 and p1.TRNDT BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' " + "and p2.TRNCD = 999 and p2.TRNDT BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' " + "and p3.TRNCD = 103 and p3.TRNDT BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' " + "and p4.TRNCD = 104 and p4.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p5.TRNCD = 105 and p5.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p6.TRNCD = 108 and p6.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p7.TRNCD = 126 and p7.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p10.TRNCD = 128 and p10.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' " +
						// "and empmast.STATUS = 'A'" +
						" and T.srno =(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = empmast.EMPNO AND E2.EFFDATE <= '"
						+ ReportDAO.EOM(PAYREGDT) + "') " +

						" UNION  select CONVERT(INT, empmast.empcode) as EMPCODE ,rtrim(empmast.fname)+' '+rtrim(empmast.mname)+' '+rtrim(empmast.lname)as name,empmast.empno,t.PRJ_SRNO,"
						+ "p1.NET_AMT as basic,p2.NET_AMT as payable,p3.NET_AMT as hra,p4.NET_AMT as medical,"
						+ "p5.NET_AMT as education,p6.NET_AMT as convall,p7.NET_AMT as min_ins," + "p10.NET_AMT as col,"
						+ "(select net_amt from   paytran_stage  where TRNCD = 127 and trndt BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as addless," +
						// "(select net_amt from paytran_stage where TRNCD = 129
						// and trndt BETWEEN '"+BomDt+"'and '"+EomDt+"' and
						// EMPNO = empmast.EMPNO) as special, " +
						"(select net_amt from   paytran_stage  where TRNCD = 107 and trndt BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' and EMPNO = empmast.EMPNO) as splall,"
						+ "(select net_amt from   paytran_stage  where TRNCD = 102 and trndt BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as curda,"
						+ "(select net_amt from   paytran_stage  where TRNCD = 115 and trndt BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as washall,"
						+ "(select net_amt from   paytran_stage  where TRNCD = 135 and trndt BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as bonusall,"
						+ "(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198) and TRNCD not in(101,103,104,105,108,126,127,128,107,102,115,135) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as addinc,"
						+ "(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as totded, "
						+ "(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198) and trndt BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' and EMPNO = empmast.EMPNO) as totear "
						+ "from EMPMAST empmast join "
						+ "  paytran_stage  p1 on empmast.EMPNO = p1.EMPNO join   paytran_stage  p2 on empmast.EMPNO = p2.EMPNO "
						+ "join   paytran_stage  p3 on empmast.EMPNO = p3.EMPNO "
						+ "join   paytran_stage  p4 on empmast.EMPNO = p4.EMPNO "
						+ "join   paytran_stage  p5 on empmast.EMPNO = p5.EMPNO "
						+ "join   paytran_stage  p6 on empmast.EMPNO = p6.EMPNO "
						+ "join   paytran_stage  p7 on empmast.EMPNO = p7.EMPNO "
						+ "join   paytran_stage  p10 on empmast.EMPNO = p10.EMPNO "
						+ "join EMPTRAN t on t.EMPNO = empmast.EMPNO where " + "p1.TRNCD = 101 and p1.TRNDT BETWEEN '"
						+ BomDt + "'and '" + EomDt + "' " + "and p2.TRNCD = 999 and p2.TRNDT BETWEEN '" + BomDt
						+ "'and '" + EomDt + "' " + "and p3.TRNCD = 103 and p3.TRNDT BETWEEN '" + BomDt + "'and '"
						+ EomDt + "' " + "and p4.TRNCD = 104 and p4.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p5.TRNCD = 105 and p5.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p6.TRNCD = 108 and p6.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p7.TRNCD = 126 and p7.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' "
						+ "and p10.TRNCD = 128 and p10.TRNDT BETWEEN '" + BomDt + "'and '" + EomDt + "' " +
						// "and empmast.STATUS = 'A'" +
						" and T.srno =(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = empmast.EMPNO AND E2.EFFDATE <= '"
						+ ReportDAO.EOM(PAYREGDT) + "') " + "order by t.PRJ_SRNO,EMPCODE";
				System.out.println(EmpSql);
				ResultSet rs = st.executeQuery(EmpSql);

				while (rs.next()) {

					String prj_name = null;
					String prj_code = null;
					Connection conn = ConnectionManager.getConnectionTech();
					Statement stmt = conn.createStatement();
					String prjquery = "select Site_Name,Project_Code from Project_Sites where SITE_ID = '"
							+ rs.getString("PRJ_SRNO") + "'";
					// System.out.println(prjquery);
					ResultSet prj = stmt.executeQuery(prjquery);
					if (prj.next()) {
						prj_name = prj.getString("Site_Name");
						prj_code = prj.getString("Project_Code");
					}
					pBrcd1 = rs.getString("PRJ_SRNO");
					br_tot_no_emp = 0;

					HSSFRow head1 = sheet.createRow((short) i++);
					HSSFCell cell4 = head1.createCell((short) 0);
					cell4.setCellValue(
							" Employee's Salary List For Project Site : " + prj_name + " (" + prj_code + ")");
					cell4.setCellStyle(my_style);

					sheet.setColumnWidth((short) 0, (short) 3000);
					sheet.setColumnWidth((short) 1, (short) 7000);
					sheet.setColumnWidth((short) 4, (short) 4000);
					sheet.setColumnWidth((short) 5, (short) 4000);
					sheet.setColumnWidth((short) 6, (short) 4000);
					sheet.setColumnWidth((short) 7, (short) 4000);
					sheet.setColumnWidth((short) 8, (short) 4000);
					sheet.setColumnWidth((short) 9, (short) 4000);
					sheet.setColumnWidth((short) 10, (short) 4000);
					sheet.setColumnWidth((short) 11, (short) 4000);
					sheet.setColumnWidth((short) 12, (short) 4000);
					sheet.setColumnWidth((short) 13, (short) 4000);
					sheet.setColumnWidth((short) 14, (short) 4000);
					sheet.setColumnWidth((short) 15, (short) 4000);
					sheet.setColumnWidth((short) 16, (short) 4000);
					sheet.setColumnWidth((short) 17, (short) 4000);
					sheet.setColumnWidth((short) 18, (short) 4000);
					sheet.setColumnWidth((short) 19, (short) 4000);

					prj.close();
					stmt.close();

					head1 = sheet.createRow((short) i++);
					cell4 = head1.createCell((short) 0);
					cell4.setCellValue("EMP CODE");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 1);
					cell4.setCellValue("EMPNAME");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 4);
					cell4.setCellValue("Basic");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 5);
					cell4.setCellValue("Current D.A");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 6);
					cell4.setCellValue("H.R.A");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 7);
					cell4.setCellValue("Medical");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 8);
					cell4.setCellValue("Education");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 9);
					cell4.setCellValue("Spl All");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 10);
					cell4.setCellValue("Conv All");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 11);
					cell4.setCellValue("Washing All");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 12);
					cell4.setCellValue("Bonus");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 13);
					cell4.setCellValue("Min Insurance");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 14);
					cell4.setCellValue("Add less Amt");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 15);
					cell4.setCellValue("Col");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 16);
					cell4.setCellValue("Add income");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 17);
					cell4.setCellValue("Tot income");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 18);
					cell4.setCellValue("Tot deduction");
					cell4.setCellStyle(my_style1);
					cell4 = head1.createCell((short) 19);
					cell4.setCellValue("Net pay");
					cell4.setCellStyle(my_style1);

					while (pBrcd1.equals(rs.getString("PRJ_SRNO"))) {
						basic_total = basic_total + rs.getFloat("basic");

						head1 = sheet.createRow((short) i++);
						cell4 = head1.createCell((short) 0);
						cell4.setCellValue("" + rs.getString("EMPCODE"));
						cell4 = head1.createCell((short) 1);
						cell4.setCellValue("" + rs.getString("name"));
						cell4 = head1.createCell((short) 4);
						cell4.setCellValue(rs.getFloat("basic"));

						if (rs.getFloat("curda") == 0.0) {
							cell4 = head1.createCell((short) 5);
							cell4.setCellValue("");
						} else {
							float curda = rs.getFloat("curda");
							currda_total = currda_total + curda;
							cell4 = head1.createCell((short) 5);
							cell4.setCellValue(curda);
						}
						hra_total = hra_total + rs.getFloat("hra");

						cell4 = head1.createCell((short) 6);
						cell4.setCellValue(rs.getFloat("hra"));

						medical_total = medical_total = rs.getFloat("medical");
						cell4 = head1.createCell((short) 7);
						cell4.setCellValue(rs.getFloat("medical"));

						edu_total = edu_total = rs.getFloat("education");
						cell4 = head1.createCell((short) 8);
						cell4.setCellValue(rs.getFloat("education"));

						if (rs.getFloat("splall") == 0.0) {
							cell4 = head1.createCell((short) 9);
							cell4.setCellValue("");

						} else {
							float splall1 = rs.getFloat("splall");
							splall_total = splall_total + splall1;
							cell4 = head1.createCell((short) 9);
							cell4.setCellValue(splall1);
						}

						convall_total = convall_total + rs.getFloat("convall");
						cell4 = head1.createCell((short) 10);
						cell4.setCellValue(rs.getFloat("convall"));

						if (rs.getFloat("washall") == 0.0) {
							cell4 = head1.createCell((short) 11);
							cell4.setCellValue("");
						} else {
							float washing = rs.getFloat("washall");
							washingall_total = washingall_total = rs.getFloat("washall");
							cell4 = head1.createCell((short) 11);
							cell4.setCellValue(washing);
						}

						if (rs.getFloat("bonusall") == 0.0) {

							cell4 = head1.createCell((short) 12);
							cell4.setCellValue("");
						} else {
							float bonus = rs.getFloat("bonusall");
							bonus_total = bonus_total + rs.getFloat("bonusall");

							cell4 = head1.createCell((short) 12);
							cell4.setCellValue(bonus);
						}

						minins_total = minins_total + +rs.getFloat("min_ins");
						cell4 = head1.createCell((short) 13);
						cell4.setCellValue(rs.getFloat("min_ins"));

						if (rs.getFloat("addless") == 0.0) {
							cell4 = head1.createCell((short) 14);
							cell4.setCellValue("");
						} else {
							float addless = rs.getFloat("addless");
							addlss_total = addlss_total + addless;
							cell4 = head1.createCell((short) 14);
							cell4.setCellValue(addless);
						}

						col_total = col_total + +rs.getFloat("col");
						cell4 = head1.createCell((short) 15);
						cell4.setCellValue(rs.getFloat("col"));

						if (rs.getFloat("addinc") == 0.0) {
							cell4 = head1.createCell((short) 16);
							cell4.setCellValue("");
						} else {
							float add_inc = rs.getFloat("addinc");
							addinc_total = addinc_total + add_inc;
							cell4 = head1.createCell((short) 16);
							cell4.setCellValue(add_inc);
						}

						totinc_total = totinc_total + rs.getFloat("totear");
						cell4 = head1.createCell((short) 17);
						cell4.setCellValue(rs.getFloat("totear"));

						totded_total = totded_total + rs.getFloat("totded");
						cell4 = head1.createCell((short) 18);
						cell4.setCellValue(rs.getFloat("totded"));

						netpay_total = netpay_total + rs.getFloat("payable");
						cell4 = head1.createCell((short) 19);
						cell4.setCellValue(rs.getFloat("payable"));

						tot_no_emp = tot_no_emp + 1;
						br_tot_no_emp = br_tot_no_emp + 1;
						if (!rs.next()) {
							break;
						}
						if (!pBrcd1.equals(rs.getString("PRJ_SRNO"))) {
							rs.previous();
							break;
						}
					}

					head1 = sheet.createRow((short) i++);
					cell4 = head1.createCell((short) 0);
					cell4.setCellValue("");
					cell4 = head1.createCell((short) 1);
					cell4.setCellValue("");
					cell4 = head1.createCell((short) 2);
					cell4.setCellValue("TOTAL :");
					cell4.setCellStyle(my_style);
					cell4 = head1.createCell((short) 4);
					cell4.setCellValue(basic_total);

					lstbasic_total = lstbasic_total + basic_total;
					basic_total = 0.0f;

					cell4 = head1.createCell((short) 5);
					cell4.setCellValue(currda_total);
					lstcurrda_total = lstcurrda_total + currda_total;
					currda_total = 0.0f;

					cell4 = head1.createCell((short) 6);
					cell4.setCellValue(hra_total);
					lsthra_total = lsthra_total + hra_total;
					hra_total = 0.0f;

					cell4 = head1.createCell((short) 7);
					cell4.setCellValue(medical_total);
					lstmedical_total = lstmedical_total + medical_total;
					medical_total = 0.0f;

					cell4 = head1.createCell((short) 8);
					cell4.setCellValue(edu_total);
					lstedu_total = lstedu_total + edu_total;
					edu_total = 0.0f;

					cell4 = head1.createCell((short) 9);
					cell4.setCellValue(splall_total);
					lstsplall_total = lstsplall_total = splall_total;
					splall_total = 0.0f;

					cell4 = head1.createCell((short) 10);
					cell4.setCellValue(convall_total);
					lstconvall_total = lstconvall_total = convall_total;
					convall_total = 0.0f;

					cell4 = head1.createCell((short) 11);
					cell4.setCellValue(washingall_total);
					lstwashingall_total = lstwashingall_total + washingall_total;
					washingall_total = 0.0f;

					cell4 = head1.createCell((short) 12);
					cell4.setCellValue(bonus_total);
					lstbonus_total = lstbonus_total + bonus_total;
					bonus_total = 0.0f;

					cell4 = head1.createCell((short) 13);
					cell4.setCellValue(minins_total);
					lstminins_total = lstminins_total + minins_total;
					minins_total = 0.0f;

					cell4 = head1.createCell((short) 14);
					cell4.setCellValue(addlss_total);
					lstaddlss_total = lstaddlss_total + addlss_total;
					addlss_total = 0.0f;

					cell4 = head1.createCell((short) 15);
					cell4.setCellValue(col_total);
					lstcol_total = lstcol_total + col_total;
					col_total = 0.0f;

					cell4 = head1.createCell((short) 16);
					cell4.setCellValue(addinc_total);
					lstaddinc_total = lstaddinc_total + addinc_total;
					addinc_total = 0.0f;

					cell4 = head1.createCell((short) 17);
					cell4.setCellValue(totinc_total);
					lsttotinc_total = lsttotinc_total + totinc_total;
					totinc_total = 0.0f;

					cell4 = head1.createCell((short) 18);
					cell4.setCellValue(totded_total);
					lsttotded_total = lsttotded_total + totded_total;
					totded_total = 0.0f;

					cell4 = head1.createCell((short) 19);
					cell4.setCellValue(netpay_total);
					lstnetpay_total = lstnetpay_total + netpay_total;
					netpay_total = 0.0f;

					head1 = sheet.createRow((short) i++);
					head1.createCell((short) 0).setCellValue("Total Employee(s) : " + br_tot_no_emp);
					head1 = sheet.createRow((short) i++);
					head1.createCell((short) 0).setCellValue("");
					head1 = sheet.createRow((short) i++);
					head1.createCell((short) 0).setCellValue("");
				}

				HSSFRow head1 = sheet.createRow((short) i++);
				HSSFCell cell4;
				head1.createCell((short) 0).setCellValue("Total Employee(s) : " + tot_no_emp);

				System.out.println("Total No Of Employee :- " + tot_no_emp);

				head1 = sheet.createRow((short) i++);
				head1 = sheet.createRow((short) i++);
				cell4 = head1.createCell((short) 2);
				cell4.setCellValue("SUMMARY");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 4);
				cell4.setCellValue("Basic");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 5);
				cell4.setCellValue("Current D.A");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 6);
				cell4.setCellValue("H.R.A");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 7);
				cell4.setCellValue("Medical");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 8);
				cell4.setCellValue("Education");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 9);
				cell4.setCellValue("Spl All");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 10);
				cell4.setCellValue("Conv All");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 11);
				cell4.setCellValue("Washing All");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 12);
				cell4.setCellValue("Bonus");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 13);
				cell4.setCellValue("Min Insurance");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 14);
				cell4.setCellValue("Add less Amt");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 15);
				cell4.setCellValue("Col");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 16);
				cell4.setCellValue("Add income");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 17);
				cell4.setCellValue("Tot income");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 18);
				cell4.setCellValue("Tot deduction");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 19);
				cell4.setCellValue("Net pay");
				cell4.setCellStyle(my_style);

				head1 = sheet.createRow((short) i++);
				cell4 = head1.createCell((short) 2);
				cell4.setCellValue("Gross Total:");
				cell4.setCellStyle(my_style);
				cell4 = head1.createCell((short) 4);
				cell4.setCellValue(lstbasic_total);
				cell4 = head1.createCell((short) 5);
				cell4.setCellValue(lstcurrda_total);
				cell4 = head1.createCell((short) 6);
				cell4.setCellValue(lsthra_total);
				cell4 = head1.createCell((short) 7);
				cell4.setCellValue(lstmedical_total);
				cell4 = head1.createCell((short) 8);
				cell4.setCellValue(lstedu_total);
				cell4 = head1.createCell((short) 9);
				cell4.setCellValue(lstsplall_total);
				cell4 = head1.createCell((short) 10);
				cell4.setCellValue(lstconvall_total);
				cell4 = head1.createCell((short) 11);
				cell4.setCellValue(lstwashingall_total);
				cell4 = head1.createCell((short) 12);
				cell4.setCellValue(lstbonus_total);
				cell4 = head1.createCell((short) 13);
				cell4.setCellValue(lstminins_total);
				cell4 = head1.createCell((short) 14);
				cell4.setCellValue(lstaddlss_total);
				cell4 = head1.createCell((short) 15);
				cell4.setCellValue(lstcol_total);
				cell4 = head1.createCell((short) 16);
				cell4.setCellValue(lstaddinc_total);
				cell4 = head1.createCell((short) 17);
				cell4.setCellValue(lsttotinc_total);
				cell4 = head1.createCell((short) 18);
				cell4.setCellValue(lsttotded_total);
				cell4 = head1.createCell((short) 19);
				cell4.setCellValue(lstnetpay_total);

				DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

				Calendar calobj = Calendar.getInstance();
				HSSFRow row = sheet.createRow((short) i);
				row.createCell((short) 0).setCellValue(" ");
				row = sheet.createRow((short) i + 1);
				row.createCell((short) 0).setCellValue("Report Date And Time " + df.format(calobj.getTime()));

				hwb.write(out1);
				out1.close();

				System.out.println("last hra = " + lsthra_total);
				System.out.println("last medical = " + lstmedical_total);
				System.out.println("last education = " + lstedu_total);
				System.out.println("last splall = " + lstsplall_total);
				System.out.println("last convall = " + lstconvall_total);
				System.out.println("last washing = " + lstwashingall_total);
				System.out.println("last bonus = " + lstbonus_total);
				System.out.println("last minins = " + lstminins_total);
				System.out.println("last addless = " + lstaddlss_total);
				System.out.println("last col = " + lstcol_total);
				// System.out.println("last special = "+lstspecial_total);
				System.out.println("last addinc = " + lstaddinc_total);
				System.out.println("last totinc = " + lsttotinc_total);
				System.out.println("last totded = " + lsttotded_total);
				System.out.println("last totnetpay = " + lstnetpay_total);

				st.close();
				con.close();

			}

			else if (type.equalsIgnoreCase("ID")) {

				ReportDAO.OpenCon("", "", "", repBean);
				con = repBean.getCn();
				Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String report_type[] = { "t.PRJ_SRNO", "t.DEPT" };

				FileOutputStream out1 = new FileOutputStream(new File(filepath));

				HSSFWorkbook hwb = new HSSFWorkbook();
				for (int x = 0; x < report_type.length; x++) {
					tot_no_emp = 0;
					HSSFSheet sheet = hwb.createSheet(x == 0 ? "Project" : "Depertment");
					Calendar currentMonth = Calendar.getInstance();

					HSSFCellStyle my_style = hwb.createCellStyle();
					HSSFFont my_font = hwb.createFont();
					my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					my_style.setFont(my_font);

					HSSFRow rowtitle = sheet.createRow((short) 0);
					HSSFCell cell = rowtitle.createCell((short) 11);
					cell.setCellValue(prop.getProperty("companyName"));
					cell.setCellStyle(my_style);
					HSSFRow rowtitle1 = sheet.createRow((short) 1);
					HSSFCell cell1 = rowtitle1.createCell((short) 9);
					cell1.setCellValue(prop.getProperty("addressForReport"));
					cell1.setCellStyle(my_style);
					HSSFRow rowtitle2 = sheet.createRow((short) 2);
					HSSFCell cell2 = rowtitle2.createCell((short) 11);
					cell2.setCellValue(prop.getProperty("contactForReport"));
					cell2.setCellStyle(my_style);
					HSSFRow rowtitle31 = sheet.createRow((short) 3);
					cell2 = rowtitle31.createCell((short) 11);
					cell2.setCellValue(prop.getProperty("mailForReport"));
					cell2.setCellStyle(my_style);
					HSSFRow rowtitle3 = sheet.createRow((short) 4);
					cell2 = rowtitle3.createCell((short) 10);
					cell2.setCellValue("Salary Sheet For The Month Of :- " + PAYREGDT);
					cell2.setCellStyle(my_style);
					HSSFRow rowtitle4 = sheet.createRow((short) 5);
					rowtitle4.createCell((short) 0).setCellValue("");
					HSSFRow rowtitle5 = sheet.createRow((short) 6);
					rowtitle5.createCell((short) 0).setCellValue("");

					HSSFFont blueFont = hwb.createFont();
					blueFont.setColor(HSSFColor.BLUE.index);

					HSSFCellStyle style = hwb.createCellStyle();
					style.setFillForegroundColor(HSSFColor.BLUE.index);

					int i = 10;

					Rectangle rec = new Rectangle(100, 100);
					float lsterp_total = 0.0f;
					float lstmed_total = 0.0f;
					float lstother_total = 0.0f;
					float lstCurrAdv_total = 0.0f;
					float lstleavetc_total = 0.0f;
					float lstloan_total = 0.0f;
					float erp_total = 0.0f;
					float med_total = 0.0f;
					float other_total = 0.0f;
					float CurrAdv_total = 0.0f;
					float leavetc_total = 0.0f;
					float loan_total = 0.0f;
					float basic_total = 0.0f;
					float lstbasic_total = 0.0f;
					float lic_total = 0.0f;
					float lstlic_total = 0.0f;
					float hra_total = 0.0f;
					float lsthra_total = 0.0f;
					float medical_total = 0.0f;
					float lstmedical_total = 0.0f;
					float edu_total = 0.0f;
					float lstedu_total = 0.0f;
					float splall_total = 0.0f;
					float lstsplall_total = 0.0f;
					float convall_total = 0.0f;
					float lstconvall_total = 0.0f;
					float pf_total = 0.0f;
					float lstpf_total = 0.0f;
					float pt_total = 0.0f;
					float lstpt_total = 0.0f;
					float mlwf_total = 0.0f;
					float lstmlwf_total = 0.0f;
					float esic_total = 0.0f;
					float lstesic_total = 0.0f;
					float mob_total = 0.0f;
					float lstmob_total = 0.0f;
					float adv_total = 0.0f;
					float lstadv_total = 0.0f;
					float tds_total = 0.0f;
					float lsttds_total = 0.0f;
					float relif_total = 0.0f;
					float lstrelif_total = 0.0f;
					float netc_total = 0.0f;
					float lstnetc_total = 0.0f;
					float minins_total = 0.0f;
					float lstminins_total = 0.0f;
					float addlss_total = 0.0f;
					float lstaddlss_total = 0.0f;
					float col_total = 0.0f;
					float lstcol_total = 0.0f;
					float special_total = 0.0f;
					float lstspecial_total = 0.0f;
					float addinc_total = 0.0f;
					float lstaddinc_total = 0.0f;
					float totinc_total = 0.0f;
					float lsttotinc_total = 0.0f;
					float addded_total = 0.0f;
					float lstaddded_total = 0.0f;
					float totded_total = 0.0f;
					float lsttotded_total = 0.0f;
					float netpay_total = 0.0f;
					float lstnetpay_total = 0.0f;
					float eepf_total = 0.0f;
					float lsteepf_total = 0.0f;
					float eeps_total = 0.0f;
					float lsteeps_total = 0.0f;
					float eedli_total = 0.0f;
					float lsteedli_total = 0.0f;
					float eepfadmin_total = 0.0f;
					float lsteepfadmin_total = 0.0f;
					float eedliadmin_total = 0.0f;
					float lsteedliadmin_total = 0.0f;
					float eesic = 0.0f;
					float lsteesic_total = 0.0f;
					float absentdays_total = 0.0f;

					EmpSql = "         SELECT  empcode , RTRIM(EMPMAST.FNAME)+' '+RTRIM(EMPMAST.MNAME)+' '+RTRIM(EMPMAST.LNAME)AS name,"
							+ "     	EMPMAST.empno,   T.PRJ_SRNO,T.DEPT,"
							+ "       CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 101 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)   "
							+ "		THEN	(SELECT NET_AMT AS basic  FROM   PAYTRAN WHERE TRNCD = 101 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE	(SELECT 0 AS basic) END AS 'basic',"
							+ "	    CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 999 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS payable  FROM   PAYTRAN WHERE TRNCD = 999 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS payable) END AS 'payable',"
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 103 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS hra  FROM   PAYTRAN WHERE TRNCD = 103 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS hra) END AS 'hra',"
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 104 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS medical  FROM   PAYTRAN WHERE TRNCD = 104 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS medical) END AS 'medical',   " +

							"       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 106 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)		"
							+ "	    THEN		(SELECT NET_AMT AS leavetc  FROM   PAYTRAN WHERE TRNCD = 106 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    ELSE 	(SELECT 0 AS leavetc) END AS 'leavetc', " +

							"       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 105 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)		"
							+ "	    THEN		(SELECT NET_AMT AS education  FROM   PAYTRAN WHERE TRNCD = 105 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    ELSE 	(SELECT 0 AS education) END AS 'education',           " +

							" CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 108 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    THEN			(SELECT NET_AMT AS convall  FROM   PAYTRAN WHERE TRNCD = 108 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS convall) END AS 'convall',"
							+ "       CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 126 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN		(SELECT NET_AMT AS min_ins  FROM   PAYTRAN WHERE TRNCD = 126 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS min_ins) END AS 'min_ins',"
							+ "         CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 128 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN			(SELECT NET_AMT AS col  FROM   PAYTRAN WHERE TRNCD = 128 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS col) END AS 'col',"
							+ "   CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 131 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS er2  FROM   PAYTRAN WHERE TRNCD = 131 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			ELSE				(SELECT 0 AS er2) END AS 'er2',"
							+ "				 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 130 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS er1  FROM   PAYTRAN WHERE TRNCD = 130 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE				(SELECT 0 AS er1) END AS 'er1',  CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 107 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "				THEN (SELECT NET_AMT AS splall  FROM   PAYTRAN WHERE TRNCD = 107  AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)    			"
							+ "	ELSE (SELECT 0 AS splall) END AS 'splall'," +

							"				 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 145 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS ANYm  FROM   PAYTRAN WHERE TRNCD = 145 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE				(SELECT 0 AS ANYm) END AS 'ANY', " +

							"			 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 201 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS pf  FROM   PAYTRAN WHERE TRNCD = 201 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)    			"
							+ "		ELSE		(SELECT 0 AS er2) END AS 'pf',	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 202 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS pt  FROM   PAYTRAN WHERE TRNCD = 202 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt
							+ "' AND empno = EMPMAST.empno)		ELSE			(SELECT 0 AS pt) END AS 'pt', "
							+ "   CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 227 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN			(SELECT NET_AMT AS CurrAdv  FROM   PAYTRAN WHERE TRNCD = 227 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "				ELSE		(SELECT 0 AS CurrAdv) END AS 'CurrAdv',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 221 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS esic  FROM   PAYTRAN WHERE TRNCD = 221 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS esic) END AS 'esic',"
							+ "		 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 223 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN		(SELECT NET_AMT AS mob  FROM   PAYTRAN WHERE TRNCD = 223 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE  (SELECT 0 AS mob) END AS 'mob',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 225 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS adv  FROM   PAYTRAN WHERE TRNCD = 225 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE	(SELECT 0 AS adv) END AS 'adv',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 228 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS tds  FROM   PAYTRAN WHERE TRNCD = 228 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS tds) END AS 'tds'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 211 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS mlwf  FROM   PAYTRAN WHERE TRNCD = 211 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS mlwf) END AS 'mlwf'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 207 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS med  FROM   PAYTRAN WHERE TRNCD = 207 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS med) END AS 'med'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 208 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS erp  FROM   PAYTRAN WHERE TRNCD = 208 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS erp) END AS 'erp'," +

							"	CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 229 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS relif  FROM   PAYTRAN WHERE TRNCD = 229 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)	"
							+ "	ELSE	(SELECT 0 AS relif) END AS 'relif',	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 230 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS netc  FROM   PAYTRAN WHERE TRNCD = 230 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS netc) END AS 'netc',"
							+ " 	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 226 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS loan  FROM   PAYTRAN WHERE TRNCD = 226 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	  ELSE		(SELECT 0 AS loan) END AS 'loan',"
							+ "    case when EXISTS(select SUM(net_amt) from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "    and TRNCD not in(101,103,104,105,106,108,126,128,107,130,131,145)"
							+ "    and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno =  EMPMAST.empno )"
							+ "	 THEN	(select SUM(net_amt) as addinc from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "    and TRNCD not in(101,103,104,105,106,108,126,128,107,130,131,145)"
							+ "    and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno =  EMPMAST.empno) "
							+ "	 ELSE		(SELECT 0 AS addinc) END AS 'addinc',"
							+ "      case when EXISTS(select SUM(net_amt)   from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 201 and 299)"
							+ "     and TRNCD not in(201,202,205,221,223,225,228,229,230,226,211,227,207,208) and trndt BETWEEN '"
							+ BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  THEN	(select SUM(net_amt)  as other from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 201 and 299)"
							+ "     and TRNCD not in(201,202,205,221,223,225,228,229,230,226,211,227,207,208) and trndt BETWEEN '"
							+ BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  ELSE	(SELECT 0 AS other) END AS 'other' ,"
							+ "	  case when EXISTS(select SUM(net_amt) from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 201 and 299)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  THEN			(select SUM(net_amt) as totded from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 201 and 299) "
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt
							+ "' and empno = empmast.empno)	" + "	  ELSE 	(SELECT 0 AS totded) END AS 'totded' ,"
							+ "	  case when EXISTS(select SUM(net_amt) from   PAYTRAN  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno) "
							+ "	  THEN	(select SUM(net_amt) from   PAYTRAN  where TRNCD in    (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  ELSE		(SELECT 0 AS totear) END AS 'totear',"
							
+ " 	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 301 AND TRNDT BETWEEN '"
+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
+ "	THEN	(SELECT NET_AMT AS absentdays  FROM   PAYTRAN WHERE TRNCD = 301 AND TRNDT BETWEEN '"
+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
+ "	  ELSE		(SELECT 0 AS loan) END AS 'absentdays'"


							
							+ "     from EMPMAST empmast     join   EMPTRAN t on t.empno = empmast.empno "
							+ "     where      T.srno =(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.empno = empmast.empno AND E2.EFFDATE <= '"
							+ EomDt + "'" + "     and (( empmast.STATUS='A' AND	empmast.DOJ <= '" + EomDt
							+ "') or  (empmast.STATUS ='N' And empmast.DOL>='" + BomDt + "' )) )"
							//+ "     AND T.empno in (SELECT DISTINCT empno FROM PAYTRAN WHERE TRNDT BETWEEN '" + BomDt
							+ "     AND T.empno in (SELECT DISTINCT empno FROM PAYTRAN WHERE TRNCD=201 AND NET_AMT !=0 AND TRNDT BETWEEN '" + BomDt
							+ "' and '" + EomDt + "') " +

							"    UNION "
							+ "         SELECT  empcode , RTRIM(EMPMAST.FNAME)+' '+RTRIM(EMPMAST.MNAME)+' '+RTRIM(EMPMAST.LNAME)AS name,"
							+ "     	EMPMAST.empno,   T.PRJ_SRNO,T.DEPT,"
							+ "       CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 101 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN	(SELECT NET_AMT AS basic  FROM   PAYTRAN_STAGE WHERE TRNCD = 101 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE	(SELECT 0 AS basic) END AS 'basic',"
							+ "	    CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 999 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS payable  FROM   PAYTRAN_STAGE WHERE TRNCD = 999 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS payable) END AS 'payable',"
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 103 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS hra  FROM   PAYTRAN_STAGE WHERE TRNCD = 103 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS hra) END AS 'hra',"
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 104 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN			(SELECT NET_AMT AS medical  FROM   PAYTRAN_STAGE WHERE TRNCD = 104 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS medical) END AS 'medical',   " +

							"       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 106 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)		"
							+ "	    THEN		(SELECT NET_AMT AS leavetc  FROM   PAYTRAN_STAGE WHERE TRNCD = 106 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    ELSE 	(SELECT 0 AS leavetc) END AS 'leavetc',  "
							+ "       CASE  WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 105 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)		"
							+ "	    THEN		(SELECT NET_AMT AS education  FROM   PAYTRAN_STAGE WHERE TRNCD = 105 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    ELSE 	(SELECT 0 AS education) END AS 'education',  " +

							"          CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 108 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	    THEN			(SELECT NET_AMT AS convall  FROM   PAYTRAN_STAGE WHERE TRNCD = 108 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS convall) END AS 'convall',"
							+ "       CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 126 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN		(SELECT NET_AMT AS min_ins  FROM   PAYTRAN_STAGE WHERE TRNCD = 126 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS min_ins) END AS 'min_ins',"
							+ "         CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 128 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN			(SELECT NET_AMT AS col  FROM   PAYTRAN_STAGE WHERE TRNCD = 128 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS col) END AS 'col',"
							+ "   CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 131 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS er2  FROM   PAYTRAN_STAGE WHERE TRNCD = 131 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			ELSE				(SELECT 0 AS er2) END AS 'er2',"
							+ "				 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 130 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS er1  FROM   PAYTRAN_STAGE WHERE TRNCD = 130 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE				(SELECT 0 AS er1) END AS 'er1',  CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 107 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "				THEN (SELECT NET_AMT AS splall  FROM   PAYTRAN_STAGE WHERE TRNCD = 107  AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)    			"
							+ "	ELSE (SELECT 0 AS splall) END AS 'splall'," +

							"				 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN WHERE TRNCD = 145 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS ANYm  FROM   PAYTRAN WHERE TRNCD = 145 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE				(SELECT 0 AS ANYm) END AS 'ANY', " +

							"			 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 201 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS pf  FROM   PAYTRAN_STAGE WHERE TRNCD = 201 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)    			"
							+ "		ELSE		(SELECT 0 AS er2) END AS 'pf',	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 202 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS pt  FROM   PAYTRAN_STAGE WHERE TRNCD = 202 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt
							+ "' AND empno = EMPMAST.empno)		ELSE			(SELECT 0 AS pt) END AS 'pt', "
							+ "   CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 227 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN			(SELECT NET_AMT AS CurrAdv  FROM   PAYTRAN_STAGE WHERE TRNCD = 227 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "				ELSE		(SELECT 0 AS CurrAdv) END AS 'CurrAdv',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 221 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS esic  FROM   PAYTRAN_STAGE WHERE TRNCD = 221 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE		(SELECT 0 AS esic) END AS 'esic',"
							+ "		 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 223 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		THEN		(SELECT NET_AMT AS mob  FROM   PAYTRAN_STAGE WHERE TRNCD = 223 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "		ELSE  (SELECT 0 AS mob) END AS 'mob',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 225 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "			THEN		(SELECT NET_AMT AS adv  FROM   PAYTRAN_STAGE WHERE TRNCD = 225 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE	(SELECT 0 AS adv) END AS 'adv',"
							+ "	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 228 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS tds  FROM   PAYTRAN_STAGE WHERE TRNCD = 228 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS tds) END AS 'tds'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 211 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS mlwf  FROM   PAYTRAN_STAGE WHERE TRNCD = 211 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS mlwf) END AS 'mlwf'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 207 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS med  FROM   PAYTRAN_STAGE WHERE TRNCD = 207 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS med) END AS 'med'," +

							"	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 208 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS erp  FROM   PAYTRAN_STAGE WHERE TRNCD = 208 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS erp) END AS 'erp'," +

							"	CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 229 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS relif  FROM   PAYTRAN_STAGE WHERE TRNCD = 229 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)	"
							+ "	ELSE	(SELECT 0 AS relif) END AS 'relif',	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 230 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN		(SELECT NET_AMT AS netc  FROM   PAYTRAN_STAGE WHERE TRNCD = 230 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	ELSE		(SELECT 0 AS netc) END AS 'netc',"
							+ " 	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 226 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	THEN	(SELECT NET_AMT AS loan  FROM   PAYTRAN_STAGE WHERE TRNCD = 226 AND TRNDT BETWEEN '"
							+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
							+ "	  ELSE		(SELECT 0 AS loan) END AS 'loan',"
							+ "    case when EXISTS(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "    and TRNCD not in(101,103,104,105,106,108,126,128,107,130,131,145)"
							+ "    and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno =  EMPMAST.empno )"
							+ "	 THEN	(select SUM(net_amt) as addinc from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "    and TRNCD not in(101,103,104,105,106,108,126,128,107,130,131,145)"
							+ "    and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno =  EMPMAST.empno) "
							+ "	 ELSE		(SELECT 0 AS addinc) END AS 'addinc',"
							+ "      case when EXISTS(select SUM(net_amt)   from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299)"
							+ "     and TRNCD not in(201,202,205,221,223,225,228,229,230,226,211,227,207,208) and trndt BETWEEN '"
							+ BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  THEN	(select SUM(net_amt)  as other from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299)"
							+ "     and TRNCD not in(201,202,205,221,223,225,228,229,230,226,211,227,207,208) and trndt BETWEEN '"
							+ BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  ELSE	(SELECT 0 AS other) END AS 'other' ,"
							+ "	  case when EXISTS(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  THEN			(select SUM(net_amt) as totded from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 200 and 299) "
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt
							+ "' and empno = empmast.empno)	" + "	  ELSE 	(SELECT 0 AS totded) END AS 'totded' ,"
							+ "	  case when EXISTS(select SUM(net_amt) from   paytran_stage  where TRNCD in (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno) "
							+ "	  THEN	(select SUM(net_amt) from   paytran_stage  where TRNCD in    (select TRNCD from CDMAST where PSLIPYN = 'Y' and TRNCD between 100 and 198)"
							+ "     and trndt BETWEEN '" + BomDt + "' and '" + EomDt + "' and empno = empmast.empno)"
							+ "	  ELSE		(SELECT 0 AS totear) END AS 'totear',"
							
							
+ " 	 CASE WHEN EXISTS(SELECT NET_AMT FROM   PAYTRAN_STAGE WHERE TRNCD = 301 AND TRNDT BETWEEN '"
+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
+ "	THEN	(SELECT NET_AMT AS absentdays  FROM   PAYTRAN_STAGE WHERE TRNCD = 301 AND TRNDT BETWEEN '"
+ BomDt + "' AND '" + EomDt + "' AND empno = EMPMAST.empno)"
+ "	  ELSE		(SELECT 0 AS loan) END AS 'absentdays'"
							
							
							
							+ "     from EMPMAST empmast     join   EMPTRAN t on t.empno = empmast.empno"
							+ "     where      T.srno =(SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.empno = empmast.empno AND E2.EFFDATE <= '"
							+ EomDt + "'" + "     and (( empmast.STATUS='A' AND	empmast.DOJ <= '" + EomDt
							+ "') or  (empmast.STATUS ='N' And empmast.DOL>='" + BomDt + "' )) )"
							//+ "     AND T.empno in (SELECT DISTINCT empno FROM PAYTRAN_STAGE WHERE TRNDT BETWEEN '"
							+ "     AND T.empno in (SELECT DISTINCT empno FROM PAYTRAN_STAGE WHERE TRNCD=201 AND NET_AMT!=0 AND TRNDT BETWEEN '"
							+ BomDt + "' and '" + EomDt + "') " + "     order by   t.PRJ_SRNO,empno  ";
					System.out.println("newpayreg11PF :" +EmpSql);

					//System.out.println(EmpSql);
					ResultSet rs = st.executeQuery(EmpSql);

					while (rs.next()) {
						i++;
						String prj_name = null;
						String prj_code = null;
					//	int ab=(int)rs.getFloat("absentdays");
					//	System.out.println("absent "+ab);
					//	int presentday=lastdat-ab;
					//	System.out.println("dyas"+presentday);
						int dept = 0;
						Connection conn = ConnectionManager.getConnectionTech();

						if (report_type[x].equalsIgnoreCase("t.prj_srno")) {
							String prjquery = "select Site_Name,Project_Code from Project_Sites where SITE_ID = '"
									+ rs.getString("PRJ_SRNO") + "'";
					
							Statement stmt = conn.createStatement();
							ResultSet prj = stmt.executeQuery(prjquery);
							if (prj.next()) {
								prj_name = prj.getString("Site_Name");
								prj_code = prj.getString("Project_Code");
							}
							pBrcd1 = rs.getString("PRJ_SRNO");
							br_tot_no_emp = 0;

							HSSFRow head1 = sheet.createRow((short) i++);
							HSSFCell cell4 = head1.createCell((short) 0);
							cell4.setCellValue(
									" Employee's Salary List For Project Site : " + prj_name + " (" + prj_code + ")");
							cell4.setCellStyle(my_style);
							prj.close();
						} else {

							Connection ccn = ConnectionManager.getConnection();
							Statement sts = ccn.createStatement();
							String stmt = "select distinct(lkp_srno),lkp_disc from lookup where lkp_code='DEPT' and lkp_srno="
									+ rs.getInt("DEPT") + "";
							ResultSet rrs = sts.executeQuery(stmt);
						
							if (rrs.next()) {
								dept = rrs.getInt("lkp_srno");
								prj_name = rrs.getString("lkp_disc");
							}
							pBrcd1 = Integer.toString(rs.getInt("DEPT"));
							br_tot_no_emp = 0;

							HSSFRow head1 = sheet.createRow((short) i++);
							HSSFCell cell4 = head1.createCell((short) 0);
							cell4.setCellValue(" Employee's Salary List For Deptartment : " + prj_name + "");
							cell4.setCellStyle(my_style);

							ccn.close();
						}

						sheet.setColumnWidth((short) 0, (short) 3000);
						sheet.setColumnWidth((short) 1, (short) 7000);
						sheet.setColumnWidth((short) 4, (short) 4000);
						sheet.setColumnWidth((short) 5, (short) 4000);
						sheet.setColumnWidth((short) 6, (short) 4000);
						sheet.setColumnWidth((short) 7, (short) 4000);
						sheet.setColumnWidth((short) 8, (short) 4000);
						sheet.setColumnWidth((short) 9, (short) 4000);
						sheet.setColumnWidth((short) 10, (short) 4000);
						sheet.setColumnWidth((short) 11, (short) 4000);
						sheet.setColumnWidth((short) 12, (short) 4000);
						sheet.setColumnWidth((short) 13, (short) 4000);
						sheet.setColumnWidth((short) 14, (short) 4000);
						sheet.setColumnWidth((short) 15, (short) 4000);
						sheet.setColumnWidth((short) 16, (short) 4000);
						sheet.setColumnWidth((short) 17, (short) 4000);
						sheet.setColumnWidth((short) 18, (short) 4000);
						sheet.setColumnWidth((short) 19, (short) 4000);
						sheet.setColumnWidth((short) 20, (short) 4000);
						sheet.setColumnWidth((short) 21, (short) 4000);
						sheet.setColumnWidth((short) 22, (short) 4000);
						sheet.setColumnWidth((short) 23, (short) 4000);
						sheet.setColumnWidth((short) 24, (short) 4000);
						sheet.setColumnWidth((short) 25, (short) 4000);
						sheet.setColumnWidth((short) 26, (short) 4000);
						sheet.setColumnWidth((short) 27, (short) 4000);
						sheet.setColumnWidth((short) 28, (short) 4000);
						sheet.setColumnWidth((short) 29, (short) 4000);
						sheet.setColumnWidth((short) 30, (short) 4000);
						sheet.setColumnWidth((short) 31, (short) 4000);
						sheet.setColumnWidth((short) 32, (short) 4000);
						
						sheet.setColumnWidth((short) 33, (short) 4000);
						sheet.setColumnWidth((short) 34, (short) 4000);
					
						conn.close();

						HSSFRow rowhead = sheet.createRow((short) i++);

						rowhead.createCell((short) 0).setCellValue("EMP CODE.");
						rowhead.createCell((short) 1).setCellValue("EMPLOYEE NAME");
						rowhead.createCell((short) 4).setCellValue("BASIC");
						rowhead.createCell((short) 5).setCellValue("HRA");
						rowhead.createCell((short) 6).setCellValue("COL");
						rowhead.createCell((short) 7).setCellValue("MEDICAL");
						rowhead.createCell((short) 8).setCellValue("CONV ALL");
						rowhead.createCell((short) 9).setCellValue("EDUCATION");
						rowhead.createCell((short) 10).setCellValue("MIN INS");
						rowhead.createCell((short) 11).setCellValue("SPL All");
						rowhead.createCell((short) 12).setCellValue("EARN 1");
						rowhead.createCell((short) 13).setCellValue("EARN 2");
						rowhead.createCell((short) 14).setCellValue("ADD INCOME");
						rowhead.createCell((short) 15).setCellValue("LTA");
						rowhead.createCell((short) 16).setCellValue("TOTAL INCOME");
						rowhead.createCell((short) 17).setCellValue("PF");
						rowhead.createCell((short) 18).setCellValue("ESIC");
						rowhead.createCell((short) 19).setCellValue("PT");
						rowhead.createCell((short) 20).setCellValue("TDS");
						rowhead.createCell((short) 21).setCellValue("ADVANCE");
						rowhead.createCell((short) 22).setCellValue("LOAN DED");
						rowhead.createCell((short) 23).setCellValue("MOB DED");
						rowhead.createCell((short) 24).setCellValue("NET CHARGES");
						rowhead.createCell((short) 25).setCellValue("CURRENT ADVANCE");
						rowhead.createCell((short) 26).setCellValue("RELEAF FUND");
						rowhead.createCell((short) 27).setCellValue("MLWF");
						rowhead.createCell((short) 28).setCellValue("OTHER");
						rowhead.createCell((short) 29).setCellValue("MED DED");
						rowhead.createCell((short) 30).setCellValue("ERP");
						rowhead.createCell((short) 31).setCellValue("Tot DED");
						rowhead.createCell((short) 32).setCellValue("NET PAY");
						
						rowhead.createCell((short) 33).setCellValue("Absent Days");
						rowhead.createCell((short) 34).setCellValue("Present Days");
						
						while (report_type[x].equalsIgnoreCase("t.prj_srno") ? pBrcd1.equals(rs.getString("PRJ_SRNO"))
								: dept == rs.getInt("DEPT")) {

							rowhead = sheet.createRow((short) i++);
							rowhead.createCell((short) 0).setCellValue("" + rs.getString("EMPCODE"));

							rowhead.createCell((short) 1).setCellValue("" + rs.getString("name"));

							basic_total = basic_total + rs.getFloat("basic");
							rowhead.createCell((short) 4).setCellValue((int) rs.getFloat("basic"));

							hra_total = hra_total + rs.getFloat("hra");
							rowhead.createCell((short) 5).setCellValue((int) rs.getFloat("hra"));

							col_total = col_total + +rs.getFloat("col");
							rowhead.createCell((short) 6).setCellValue((int) rs.getFloat("col"));

							medical_total = medical_total + rs.getFloat("medical");
							rowhead.createCell((short) 7).setCellValue((int) rs.getFloat("medical"));

							convall_total = convall_total + rs.getFloat("convall");
							rowhead.createCell((short) 8).setCellValue((int) rs.getFloat("convall"));

							edu_total = edu_total + rs.getFloat("education");
							rowhead.createCell((short) 9).setCellValue((int) rs.getFloat("education"));

							minins_total = minins_total + +rs.getFloat("min_ins");
							rowhead.createCell((short) 10).setCellValue((int) rs.getFloat("min_ins"));

							if (rs.getFloat("splall") == 0.0) {
								rowhead.createCell((short) 11).setCellValue(0.00);

							} else {
								float splall1 = rs.getFloat("splall");
								splall_total = splall_total + splall1;

								rowhead.createCell((short) 11).setCellValue((int) splall1);
							}

							if (rs.getFloat("er1") == 0.0) {

								rowhead.createCell((short) 12).setCellValue(0.00);
							} else {
								float addless = rs.getFloat("er1");
								addlss_total = addlss_total + addless;

								rowhead.createCell((short) 12).setCellValue((int) addless);
							}

							if (rs.getFloat("er2") == 0.0) {
								rowhead.createCell((short) 13).setCellValue(0.00);
							} else {
								float spcl = rs.getFloat("er2");
								special_total = special_total + spcl;
								rowhead.createCell((short) 13).setCellValue((int) spcl);

							}

							if (rs.getFloat("addinc") == 0.0) {
								rowhead.createCell((short) 14).setCellValue(0.00);

							} else {
								float add_inc = rs.getFloat("addinc");
								addinc_total = addinc_total + add_inc;

								rowhead.createCell((short) 14).setCellValue((int) add_inc);
							}

							if (rs.getFloat("leavetc") == 0.0) {
								rowhead.createCell((short) 15).setCellValue(0.00);

							} else {
								float leavetc = rs.getFloat("leavetc");
								leavetc_total = leavetc_total + leavetc;

								rowhead.createCell((short) 15).setCellValue((int) leavetc);
							}

							totinc_total = totinc_total + rs.getFloat("totear");

							rowhead.createCell((short) 16).setCellValue((int) rs.getFloat("totear"));

							if (rs.getFloat("pf") == 0.0) {
								rowhead.createCell((short) 17).setCellValue(0.00);

							} else {
								float pf = rs.getFloat("pf");
								pf_total = pf_total + pf;

								rowhead.createCell((short) 17).setCellValue((int) pf);
							}

							if (rs.getFloat("esic") == 0.0f) {
								rowhead.createCell((short) 18).setCellValue(0.00);

							} else {
								float esic = rs.getFloat("esic");
								esic_total = esic_total + esic;
								rowhead.createCell((short) 18).setCellValue((int) esic);

							}

							if (rs.getFloat("pt") == 0.0) {
								rowhead.createCell((short) 19).setCellValue(0.00);

							} else {
								float pt = rs.getFloat("pt");
								pt_total = pt_total + pt;

								rowhead.createCell((short) 19).setCellValue((int) pt);

							}

							if (rs.getFloat("tds") == 0.0f) {
								rowhead.createCell((short) 20).setCellValue(0.00);
							} else {
								float tds = rs.getFloat("tds");
								tds_total = tds_total + tds;
								rowhead.createCell((short) 20).setCellValue((int) tds);

							}

							if (rs.getFloat("adv") == 0.0f) {
								rowhead.createCell((short) 21).setCellValue(0.00);

							} else {
								float adv = rs.getFloat("adv");
								adv_total = adv_total + adv;

								rowhead.createCell((short) 21).setCellValue((int) adv);

							}

							if (rs.getFloat("loan") == 0.0) {
								rowhead.createCell((short) 22).setCellValue(0.00);

							} else {
								float loan = rs.getFloat("loan");
								loan_total = loan_total + loan;

								rowhead.createCell((short) 22).setCellValue((int) loan);

							}

							if (rs.getFloat("mob") == 0.0f) {
								rowhead.createCell((short) 23).setCellValue(0.00);

							} else {
								float mob = rs.getFloat("mob");
								mob_total = mob_total + mob;
								rowhead.createCell((short) 23).setCellValue((int) mob);

							}

							if (rs.getFloat("netc") == 0.0f) {
								rowhead.createCell((short) 24).setCellValue(0.00);
							} else {
								float netc = rs.getFloat("netc");
								netc_total = netc_total + netc;
								rowhead.createCell((short) 24).setCellValue((int) netc);
							}

							if (rs.getFloat("CurrAdv") == 0.0) {
								rowhead.createCell((short) 25).setCellValue(0.00);
							} else {
								float CurrAdv = rs.getFloat("CurrAdv");
								CurrAdv_total = CurrAdv_total + CurrAdv;
								rowhead.createCell((short) 25).setCellValue((int) CurrAdv);

							}

							if (rs.getFloat("relif") == 0.0f) {
								rowhead.createCell((short) 26).setCellValue(0.00);
							} else {
								float relif = rs.getFloat("relif");
								relif_total = relif_total + relif;

								rowhead.createCell((short) 26).setCellValue((int) relif);
							}

							if (rs.getFloat("mlwf") == 0.0) {
								rowhead.createCell((short) 27).setCellValue(0.00);
							} else {
								float mlwf = rs.getFloat("mlwf");
								mlwf_total = mlwf_total + mlwf;

								rowhead.createCell((short) 27).setCellValue((int) mlwf);

							}

							if (rs.getFloat("other") == 0.0) {
								rowhead.createCell((short) 28).setCellValue(0.00);
							} else {
								float other = rs.getFloat("other");
								other_total = other_total + other;

								rowhead.createCell((short) 28).setCellValue((int) other);

							}

							if (rs.getFloat("med") == 0.0) {
								rowhead.createCell((short) 29).setCellValue(0.00);
							} else {
								float med = rs.getFloat("med");
								med_total = med_total + med;

								rowhead.createCell((short) 29).setCellValue((int) med);

							}

							if (rs.getFloat("erp") == 0.0) {
								rowhead.createCell((short) 30).setCellValue(0.00);
							} else {
								float erp = rs.getFloat("erp");
								erp_total = erp_total + erp;

								rowhead.createCell((short) 30).setCellValue((int) erp);

							}

							totded_total = totded_total + rs.getFloat("totded");

							rowhead.createCell((short) 31).setCellValue((int) rs.getFloat("totded"));

							netpay_total = netpay_total + rs.getFloat("payable");

							rowhead.createCell((short) 32).setCellValue((int) rs.getFloat("payable"));
							
							
							rowhead.createCell((short) 33).setCellValue((int) rs.getFloat("absentdays"));
							
							int ab=(int)rs.getFloat("absentdays");
								System.out.println("absent "+ab);
								int presentday=lastdat-ab;
								System.out.println("dyas"+presentday);
							rowhead.createCell((short) 34).setCellValue(presentday);
							
							

												tot_no_emp = tot_no_emp + 1;
							br_tot_no_emp = br_tot_no_emp + 1;
							if (!rs.next()) {
								break;
							}
							if (report_type[x].equalsIgnoreCase("t.prj_srno")) {

								if (!pBrcd1.equals(rs.getString("PRJ_SRNO"))) {
									rs.previous();
									break;
								}
							} else {
								if (dept != rs.getInt("DEPT")) {
									rs.previous();

									break;
								}
							}

						}
						rowhead = sheet.createRow((short) i++);
						rowhead.createCell((short) 0).setCellValue("");

						rowhead.createCell((short) 1).setCellValue("");
						rowhead.createCell((short) 3).setCellValue("TOTAL :--");

						rowhead.createCell((short) 4).setCellValue((int) basic_total);
						rowhead.createCell((short) 5).setCellValue(hra_total);
						rowhead.createCell((short) 6).setCellValue(col_total);
						rowhead.createCell((short) 7).setCellValue(medical_total);
						rowhead.createCell((short) 8).setCellValue((int) convall_total);
						rowhead.createCell((short) 9).setCellValue((int) edu_total);
						rowhead.createCell((short) 10).setCellValue((int) minins_total);

						lstbasic_total = lstbasic_total + basic_total;
						basic_total = 0.0f;

						lsthra_total = lsthra_total + hra_total;
						hra_total = 0.0f;
						lstcol_total = lstcol_total + col_total;
						col_total = 0.0f;
						lstmedical_total = lstmedical_total + medical_total;
						medical_total = 0.0f;

						lstconvall_total = lstconvall_total + convall_total;
						convall_total = 0.0f;

						lstedu_total = lstedu_total + edu_total;
						edu_total = 0.0f;

						lstminins_total = lstminins_total + minins_total;
						minins_total = 0.0f;

						rowhead.createCell((short) 11).setCellValue((int) splall_total);
						rowhead.createCell((short) 12).setCellValue((int) addlss_total);
						rowhead.createCell((short) 13).setCellValue((int) special_total);
						rowhead.createCell((short) 14).setCellValue((int) addinc_total);
						rowhead.createCell((short) 15).setCellValue((int) leavetc_total);
						rowhead.createCell((short) 16).setCellValue((int) totinc_total);
						rowhead.createCell((short) 17).setCellValue((int) pf_total);

						lstleavetc_total = lstleavetc_total + leavetc_total;
						leavetc_total = 0.0f;

						lstsplall_total = lstsplall_total + splall_total;
						splall_total = 0.0f;

						lstaddlss_total = lstaddlss_total + addlss_total;
						addlss_total = 0.0f;

						lstspecial_total = lstspecial_total + special_total;
						special_total = 0.0f;

						lstaddinc_total = lstaddinc_total + addinc_total;
						addinc_total = 0.0f;

						lsttotinc_total = lsttotinc_total + totinc_total;
						totinc_total = 0.0f;

						lstpf_total = lstpf_total + pf_total;

						rowhead.createCell((short) 18).setCellValue((int) esic_total);
						rowhead.createCell((short) 19).setCellValue((int) pt_total);
						rowhead.createCell((short) 20).setCellValue((int) tds_total);
						rowhead.createCell((short) 21).setCellValue((int) adv_total);

						lstesic_total = lstesic_total + esic_total;

						lstpt_total = lstpt_total + pt_total;
						pt_total = 0.0f;

						lsttds_total = lsttds_total + tds_total;
						tds_total = 0.0f;

						lstadv_total = lstadv_total + adv_total;
						adv_total = 0.0f;

						rowhead.createCell((short) 22).setCellValue((int) loan_total);
						rowhead.createCell((short) 23).setCellValue((int) mob_total);
						rowhead.createCell((short) 24).setCellValue((int) netc_total);
						rowhead.createCell((short) 25).setCellValue((int) CurrAdv_total);
						rowhead.createCell((short) 26).setCellValue((int) relif_total);
						rowhead.createCell((short) 27).setCellValue((int) mlwf_total);
						lstCurrAdv_total = lstCurrAdv_total + CurrAdv_total;
						CurrAdv_total = 0.0f;

						lstloan_total = lstloan_total + loan_total;
						loan_total = 0.0f;

						lstmlwf_total = lstmlwf_total + mlwf_total;
						mlwf_total = 0.0f;

						lstmob_total = lstmob_total + mob_total;
						mob_total = 0.0f;

						lstnetc_total = lstnetc_total + netc_total;
						netc_total = 0.0f;

						lstlic_total = lstlic_total + lic_total;
						lic_total = 0.0f;

						lstrelif_total = lstrelif_total + relif_total;
						relif_total = 0.0f;

						rowhead.createCell((short) 28).setCellValue((int) other_total);
						rowhead.createCell((short) 29).setCellValue((int) med_total);
						rowhead.createCell((short) 30).setCellValue((int) erp_total);

						lstother_total = lstother_total + other_total;
						other_total = 0.0f;

						lstmed_total = lstmed_total + med_total;
						med_total = 0.0f;

						lsterp_total = lsterp_total + erp_total;
						erp_total = 0.0f;

						rowhead.createCell((short) 31).setCellValue((int) totded_total);
						rowhead.createCell((short) 32).setCellValue((int) netpay_total);
						
						lsttotded_total = lsttotded_total + totded_total;
						totded_total = 0.0f;

						lstnetpay_total = lstnetpay_total + netpay_total;
						netpay_total = 0.0f;

						pf_total = 0.0f;
						esic_total = 0.0f;
						lsteepf_total += eepf_total;
						eepf_total = 0;

						lsteeps_total += eeps_total;
						eeps_total = 0;

						lsteedli_total += eedli_total;
						eedli_total = 0;

						lsteepfadmin_total += eepfadmin_total;
						eepfadmin_total = 0;

						lsteedliadmin_total += eedliadmin_total;
						eedliadmin_total = 0;

						lsteesic_total += eesic;
						eesic = 0;

						rowhead = sheet.createRow((short) i++);

						rowhead.createCell((short) 0).setCellValue("Total Employee(s) : " + br_tot_no_emp);

						rowhead = sheet.createRow((short) i++);

						rowhead.createCell((short) 0).setCellValue("");
					}

					HSSFRow rowhead = sheet.createRow((short) i++);

					rowhead.createCell((short) 0).setCellValue("Total Employee(s) : " + tot_no_emp);

								rowhead = sheet.createRow((short) i++);
					HSSFCell cell4;
					rowhead.createCell((short) 10).setCellValue("  ");
					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("------- SUMMARY -------");
					cell4.setCellStyle(my_style);

					rowhead = sheet.createRow((short) i++);

					rowhead.createCell((short) 10).setCellValue("  ");
					rowhead = sheet.createRow((short) i++);

					rowhead.createCell((short) 10).setCellValue("  ");

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("BASIC Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstbasic_total);

					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("HRA Total:");
					cell4.setCellStyle(my_style);

					rowhead.createCell((short) 8).setCellValue(lsthra_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("Medical Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue(lstmedical_total);
					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("Conv Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lstconvall_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("MIN_INS Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 23).setCellValue((int) lstminins_total);

					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("SPECIAL ALLOW Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstsplall_total);
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("EARN 1 Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue((int) lstaddlss_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("EARN 2 Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstspecial_total);

					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("ADD Income Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lstaddinc_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("Total INCOME:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 23).setCellValue((int) lsttotinc_total);

					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("PF Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstpf_total);
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("ESIC Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue((int) lstesic_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("P.T Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstpt_total);
					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("T.D.S Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lsttds_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("ADVANCE Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 23).setCellValue((int) lstadv_total);

					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("LOAN Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstloan_total);
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("MOB Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue((int) lstmob_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("NETC  Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstnetc_total);

					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("LTA Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lstleavetc_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("RELEAFE Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 23).setCellValue((int) lstrelif_total);

					rowhead = sheet.createRow((short) i++);

					cell4 = rowhead.createCell((short) 1);
					cell4.setCellValue("MED Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 3).setCellValue((int) lstmed_total);
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("ERP Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue((int) lsterp_total);
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("CURRENT ADV  Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstCurrAdv_total);

					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("MLWF Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lstmlwf_total);
					
					rowhead = sheet.createRow((short) i++);
					
					cell4 = rowhead.createCell((short) 6);
					cell4.setCellValue("");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 8).setCellValue("");
					cell4 = rowhead.createCell((short) 11);
					cell4.setCellValue("ADDED Total:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 13).setCellValue((int) lstaddded_total);
					cell4 = rowhead.createCell((short) 16);
					cell4.setCellValue("Total Deduction:");
					cell4.setCellStyle(my_style);
					rowhead.createCell((short) 18).setCellValue((int) lsttotded_total);
					cell4 = rowhead.createCell((short) 21);
					cell4.setCellValue("NET PAY Total:");
					cell4.setCellStyle(my_style);

					float tttt = 0.0f;
					try {
						// System.out.println("date===="+date);
						Statement sttt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
						rs = st.executeQuery("select sum(net_amt) from paytran where trndt between '"
								+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
								+ "'  and trncd=999  union  "
								+ "select sum(net_amt) from paytran_stage where trndt between '"
								+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "'  and trncd=999 ");

						while (rs.next()) {
							tttt += rs.getFloat(1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					rowhead.createCell((short) 23).setCellValue((int) tttt);
					DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

					Calendar calobj = Calendar.getInstance();
					HSSFRow row = sheet.createRow((short) i);
					row.createCell((short) 0).setCellValue(" ");
					row = sheet.createRow((short) i + 1);
					row.createCell((short) 0).setCellValue("Report Date And Time " + df.format(calobj.getTime()));

					
				}
				hwb.write(out1);
				out1.close();
				st.close();
				con.close();
			}
		} catch (Exception e) {
			System.out.println("into excel type");
			e.printStackTrace();
		}

	}

	/***************************************************** Prasad ******************************************************************************/

	public static void pflistexcel(String PAYREGDT, String type, String filepath, String imgpath) {
		Properties prop = new Properties();
		int brtot[] = new int[15];
		int ALLbrtot[] = new int[15];
		int PBRCD = 0;
		int gross = 0;

		try {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		} catch (Exception e) {
			System.out.println("Error in constant properties Manager " + e);
		}

		try {
			System.out.println("Into Generalised_EmpListDAO.....");
			RepoartBean repBean = new RepoartBean();

			LookupHandler lkh = new LookupHandler();
			Connection con = null;
			System.out.println(filepath);
			FileOutputStream out1 = new FileOutputStream(new File(filepath));
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("EmpPFlist");

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			System.out.println("1111.....");

			sheet.setColumnWidth((short) 0, (short) 2000);
			sheet.setColumnWidth((short) 1, (short) 10000);
			sheet.setColumnWidth((short) 2, (short) 7000);
			sheet.setColumnWidth((short) 3, (short) 4000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4000);
			sheet.setColumnWidth((short) 6, (short) 5000);
			sheet.setColumnWidth((short) 7, (short) 5000);
			sheet.setColumnWidth((short) 8, (short) 4000);
			sheet.setColumnWidth((short) 9, (short) 5000);
			sheet.setColumnWidth((short) 10, (short) 5000);
			sheet.setColumnWidth((short) 11, (short) 4000);
			sheet.setColumnWidth((short) 12, (short) 4000);
			sheet.setColumnWidth((short) 13, (short) 6000);
			sheet.setColumnWidth((short) 14, (short) 5000);
			sheet.setColumnWidth((short) 15, (short) 5000);
			/*
			 * sheet.setColumnWidth((short)17, (short)7000);
			 * sheet.setColumnWidth((short)18, (short)5000);
			 * sheet.setColumnWidth((short)19, (short)4000);
			 * sheet.setColumnWidth((short)20, (short)5000);
			 */

			HSSFCellStyle my_style = hwb.createCellStyle();
			HSSFCellStyle my_style1 = hwb.createCellStyle();

			HSSFFont my_font = hwb.createFont();
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style.setFont(my_font);

			HSSFRow rowtitle = sheet.createRow((short) 0);
			HSSFCell cell = rowtitle.createCell((short) 9);

			// System.out.println("2222.....");

			cell.setCellValue(prop.getProperty("companyName"));
			cell.setCellStyle(my_style);
			HSSFRow rowtitle1 = sheet.createRow((short) 1);
			HSSFCell cell1 = rowtitle1.createCell((short) 7);
			cell1.setCellValue(prop.getProperty("addressForReport"));
			cell1.setCellStyle(my_style);
			HSSFRow rowtitle2 = sheet.createRow((short) 2);
			HSSFCell cell2 = rowtitle2.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("contactForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle31 = sheet.createRow((short) 3);
			cell2 = rowtitle31.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("mailForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle3 = sheet.createRow((short) 4);
			cell2 = rowtitle3.createCell((short) 2);
			cell2.setCellValue("");
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle4 = sheet.createRow((short) 5);
			rowtitle4.createCell((short) 0).setCellValue("");
			HSSFRow rowtitle5 = sheet.createRow((short) 6);
			rowtitle5.createCell((short) 0).setCellValue("");

			HSSFFont blueFont = hwb.createFont();
			blueFont.setColor(HSSFColor.BLUE.index);

			HSSFCellStyle style = hwb.createCellStyle();
			// style.setFont(blueFont);
			style.setFillForegroundColor(HSSFColor.BLUE.index);

			HSSFRow head = sheet.createRow((short) 7);
			head.createCell((short) 0).setCellValue("");
			HSSFRow heading = sheet.createRow((short) 8);
			HSSFCell cell3 = heading.createCell((short) 0);

			cell3.setCellValue("");
			cell3.setCellStyle(my_style1);
			HSSFRow rowhead = sheet.createRow((short) 9);
			sheet.createFreezePane(0, 10, 0, 10);

			my_style1.setAlignment((short) 2);
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style1.setFont(my_font);

			cell3 = rowhead.createCell((short) 0);
			cell3.setCellValue("SR NO");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 1);
			cell3.setCellValue("EMPNAME");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 2);
			cell3.setCellValue("FATHER/HUSBAND'S NAME");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 3);
			cell3.setCellValue("DOB");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 4);
			cell3.setCellValue("DOJ");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 5);
			cell3.setCellValue("UAN NO");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 6);
			cell3.setCellValue("P.F No");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 7);
			cell3.setCellValue("ADHAAR CARDNO");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 8);
			cell3.setCellValue("PAN CARD NO");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 9);
			cell3.setCellValue("BANK A/C  NO");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 10);
			cell3.setCellValue("IFSC CODE NO");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 11);
			cell3.setCellValue("MOBILE NO");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 12);
			cell3.setCellValue("NCP DAYS");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 13);
			cell3.setCellValue("BASIC +DA");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 14);
			cell3.setCellValue("PF DED. AMT");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 15);
			cell3.setCellValue("GROSS. AMT");
			cell3.setCellStyle(my_style1);
			/*
			 * cell3=rowhead.createCell((short) 17); cell3.setCellValue("BANK");
			 * cell3.setCellStyle(my_style1);
			 * 
			 * cell3=rowhead.createCell((short) 18);
			 * cell3.setCellValue("Account No"); cell3.setCellStyle(my_style1);
			 * 
			 * cell3=rowhead.createCell((short) 19);
			 * cell3.setCellValue("Status"); cell3.setCellStyle(my_style1);
			 * 
			 * cell3=rowhead.createCell((short) 20);
			 * cell3.setCellValue(" Monthly CTC");
			 * cell3.setCellStyle(my_style1); cell3=rowhead.createCell((short)
			 * 21); cell3.setCellValue("EMPNO"); cell3.setCellStyle(my_style1);
			 */

			ReportDAO.OpenCon("", "", "", repBean);
			con = repBean.getCn();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			System.out.println("3333.....");

			int i = 16;
			int count = 1;
			ResultSet rs = st.executeQuery(
					"select e.salute ,e.dob, e.empno, CONVERT(INT, e.empcode) as empcode,e.fname,e.mname,e.lname,e.pfno,e.uanno, e.DOJ, "
							+ "e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO, (SELECT ( INP_AMT )  FROM   paytran_stage  WHERE  trncd IN ( 999 )  AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND empno = e.empno) AS gross,  case when exists (select f3 from OTHERDETAIL where EMPNO=e.empno) then  "
							+ "(select f3 from OTHERDETAIL where EMPNO=e.empno) else (SELECT '' AS ifsc) END AS 'ifsc', case when exists  "
							+ "(select telno from EMPAUX where EMPNO=e.empno) then (select aux.telno from EMPAUX aux where aux.EMPNO=e.empno "
							+ "and id= (select MAX(id) from EMPAUX  where EMPNO=e.empno)) else (SELECT 0 AS telno) END AS 'telno', CASE WHEN  "
							+ "EXISTS(SELECT sum(NET_AMT) FROM   paytran WHERE TRNCD in ( 101, 102, 130 ) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ "AND EMPNO = e.EMPNO)   	THEN	(SELECT sum(NET_AMT) FROM   paytran WHERE TRNCD in ( 101, 102, 130 ) AND TRNDT BETWEEN "
							+ " '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) ELSE	(SELECT 0 AS BASIC_DA) END AS 'BASIC_DA', (SELECT (NET_AMT) "
							+ "FROM   paytran WHERE TRNCD in (201) AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT)
							+ "' and '" + ReportDAO.EOM(PAYREGDT) + "' AND EMPNO = e.EMPNO) AS PF , "
							+ " CASE WHEN EXISTS(SELECT (NET_AMT) FROM   paytran WHERE TRNCD in (301) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND EMPNO = e.EMPNO)  	THEN	(SELECT sum(NET_AMT) FROM   paytran WHERE TRNCD in (301) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' " + "and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) "
							+ "ELSE	(SELECT 0 AS absentDays) END AS 'absentDays' from EMPMAST e join EMPTRAN t on e.EMPNO=t.EMPNO "
							+ "join paytran p on p.EMPNO=e.empno and p.TRNDT between '" + ReportDAO.BOM(PAYREGDT)
							+ "' and '" + ReportDAO.EOM(PAYREGDT) + "' and "
							+ "p.TRNCD=201  and p.NET_AMT>0 and t.SRNO=(select max(srno) from EMPTRAN where EMPNO=t.EMPNO) "
							+ "group by e.SALUTE ,e.dob, e.empno,  e.empcode,e.fname,e.mname,e.lname,e.pfno,e.DOJ,e.UANNO,e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO "
							+ "union "
							+ "select e.salute ,e.dob, e.empno, CONVERT(INT, e.empcode) as empcode,e.fname,e.mname,e.lname,e.pfno,e.uanno, e.DOJ, "
							+ "e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO, (SELECT ( INP_AMT )  FROM   paytran_stage  WHERE  trncd IN ( 999 )  AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND empno = e.empno) AS gross, case when exists (select f3 from OTHERDETAIL where EMPNO=e.empno) then "
							+ "(select f3 from OTHERDETAIL where EMPNO=e.empno) else (SELECT '' AS ifsc) END AS 'ifsc', case when exists "
							+ "(select telno from EMPAUX where EMPNO=e.empno) then (select aux.telno from EMPAUX aux where aux.EMPNO=e.empno "
							+ "and id= (select MAX(id) from EMPAUX  where EMPNO=e.empno)) else (SELECT 0 AS telno) END AS 'telno', CASE WHEN "
							+ "EXISTS(SELECT sum(NET_AMT) FROM   paytran_stage WHERE TRNCD in ( 101, 102, 130 ) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ "AND EMPNO = e.EMPNO)   	THEN	(SELECT sum(NET_AMT) FROM   paytran_stage WHERE TRNCD in ( 101, 102, 130 ) AND TRNDT BETWEEN "
							+ " '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) ELSE	(SELECT 0 AS BASIC_DA) END AS 'BASIC_DA', "
							+ "(SELECT (NET_AMT) FROM   paytran_stage WHERE TRNCD in (201) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) AS PF , "
							+ "CASE WHEN EXISTS(SELECT (NET_AMT) FROM   paytran_stage WHERE TRNCD in (301) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND EMPNO = e.EMPNO)  	THEN	(SELECT sum(NET_AMT) FROM   paytran_stage WHERE TRNCD in (301) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' " + " and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) "
							+ " ELSE	(SELECT 0 AS absentDays) END AS 'absentDays' from EMPMAST e join EMPTRAN t on e.EMPNO=t.EMPNO "
							+ " join paytran_stage p on p.EMPNO=e.empno and p.TRNDT between '" + ReportDAO.BOM(PAYREGDT)
							+ "' and '" + ReportDAO.EOM(PAYREGDT) + "' and "
							+ " p.TRNCD=201  and p.NET_AMT>0 and t.SRNO=(select max(srno) from EMPTRAN where EMPNO=t.EMPNO) "
							+
							// " p.TRNCD=201 and p.NET_AMT>=0 and t.SRNO=(select
							// max(srno) from EMPTRAN where EMPNO=t.EMPNO) "+
							" group by e.SALUTE ,e.dob, e.empno,  e.empcode,e.fname,e.mname,e.lname,e.pfno,e.DOJ,e.UANNO,e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO "
							+

							"union " +

							"select e.salute ,e.dob, e.empno, CONVERT(INT, e.empcode) "
							+ "as empcode,e.fname,e.mname,e.lname,e.pfno,e.uanno, "
							+ "e.DOJ,e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO, (SELECT ( INP_AMT )  FROM   paytran_stage  WHERE  trncd IN ( 999 )  AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND empno = e.empno) AS gross, "
							+ "case when exists (select f3 from OTHERDETAIL where EMPNO=e.empno) "
							+ "then (select f3 from OTHERDETAIL where EMPNO=e.empno) "
							+ "else (SELECT '' AS ifsc) END AS 'ifsc', "
							+ "case when exists (select telno from EMPAUX where EMPNO=e.empno) "
							+ "then (select aux.telno from EMPAUX aux where aux.EMPNO=e.empno and " + "id= "
							+ "(select MAX(id) from EMPAUX  where EMPNO=e.empno)) "
							+ "else (SELECT 0 AS telno) END AS 'telno', "
							+ "CASE WHEN EXISTS(SELECT sum(NET_AMT) FROM   ytdtran WHERE TRNCD in ( 101, 102, 130 ) "
							+ "AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' " + "AND EMPNO = e.EMPNO)   "
							+ "	THEN	(SELECT sum(NET_AMT) FROM   ytdtran WHERE TRNCD in ( 101, 102, 130 ) "
							+ "AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' " + "AND EMPNO = e.EMPNO) ELSE	(SELECT 0 AS BASIC_DA) END AS 'BASIC_DA', "
							+ "(SELECT (NET_AMT)  FROM   ytdtran WHERE TRNCD in (201) " + "AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ "AND EMPNO = e.EMPNO) AS PF , "
							+ "CASE WHEN EXISTS(SELECT (NET_AMT) FROM   ytdtran WHERE TRNCD in (301) "
							+ "AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' " + "AND EMPNO = e.EMPNO)  "
							+ "	THEN	(SELECT sum(NET_AMT) FROM   ytdtran WHERE TRNCD in (301) "
							+ "AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' " + "AND EMPNO = e.EMPNO) ELSE	(SELECT 0 AS absentDays) END AS 'absentDays' "
							+ "from EMPMAST e " + "join EMPTRAN t " + "on e.EMPNO=t.EMPNO " + "join ytdtran p "
							+ "on p.EMPNO=e.empno " + "and p.TRNDT between '" + ReportDAO.BOM(PAYREGDT) + "' and '"
							+ ReportDAO.EOM(PAYREGDT) + "' " + "and p.TRNCD=201  and p.NET_AMT>0 " +
							// "and p.TRNCD=201 and p.NET_AMT>=0 "+
							"and t.SRNO=(select max(srno) from EMPTRAN where EMPNO=t.EMPNO) "
							+ "group by e.SALUTE ,e.dob, e.empno,  e.empcode,e.fname,e.mname,e.lname,e.pfno,e.DOJ,e.UANNO,e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO "
							+ "order by empcode ");

			int k = 0;

			while (rs.next()) {
				k++;

				HSSFRow row = sheet.createRow((short) i++);

				row.createCell((short) 0).setCellValue("" + (count++));
				row.createCell((short) 1).setCellValue(
						"" + rs.getString("FNAME") + " " + rs.getString("MNAME") + " " + rs.getString("LNAME"));
				row.createCell((short) 2).setCellValue("" + rs.getString("MNAME") + " " + rs.getString("LNAME"));

				row.createCell((short) 3).setCellValue("" + (rs.getString("DOB")));
				row.createCell((short) 4).setCellValue("" + sdf.format(rs.getDate("DOJ")));
				row.createCell((short) 5).setCellValue("" + (rs.getString("UANNO")));

				row.createCell((short) 6).setCellValue("" + rs.getString("PFNO"));
				row.createCell((short) 7).setCellValue(rs.getString("AADHAARNUM"));
				row.createCell((short) 8).setCellValue("" + rs.getString("PANNO"));
				row.createCell((short) 9).setCellValue("" + rs.getString("ACNO"));
				String ifsc = "" + rs.getString(
						"ifsc");/* !=null?""+emp.getString("ifsc"):""; */
				if (ifsc.equalsIgnoreCase("null")) {
					row.createCell((short) 10).setCellValue("");

				} else {
					row.createCell((short) 10).setCellValue("" + rs.getString("ifsc"));
				}
				String mob = "" + rs.getString(
						"TELNO");/* !=null?""+emp.getString("ifsc"):""; */
				if (mob.equalsIgnoreCase("null")) {
					row.createCell((short) 11).setCellValue("");

				} else {
					row.createCell((short) 11).setCellValue("" + rs.getString("TELNO"));
				}

				row.createCell((short) 12).setCellValue("" + rs.getString("absentDays"));
				row.createCell((short) 13).setCellValue("" + rs.getString("BASIC_DA"));

				brtot[1] = brtot[1] + Math.round(rs.getInt("BASIC_DA"));
				gross = (rs.getString("BASIC_DA") != null ? rs.getInt("BASIC_DA") : 14);

				row.createCell((short) 14).setCellValue("" + rs.getString("PF"));
				brtot[2] = brtot[2] + Math.round(rs.getInt("PF"));
				row.createCell((short) 15).setCellValue("" + rs.getInt("gross"));
				brtot[3] = brtot[3] + Math.round(rs.getInt("gross"));

			}

			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

			Calendar calobj = Calendar.getInstance();

			/*
			 * HSSFRow row = sheet.createRow((short) i); row.createCell((short)
			 * 0).setCellValue(" "); row = sheet.createRow((short) i+1);
			 * 
			 * 
			 * row.createCell((short) 1).setCellValue("Total Employee : =  " +k
			 * ); row.createCell((short) 12).setCellValue("Total : = ");
			 * row.createCell((short)
			 * 13).setCellValue(""+trans(brtot[1],"","",true,true));
			 * row.createCell((short)
			 * 14).setCellValue(""+trans(brtot[2],"","",true,true));
			 * row.createCell((short)
			 * 15).setCellValue(""+trans(brtot[3],"","",true,true));
			 */

			/*************************************** Prasad-Start *****************************************************/

			// Connection con1 =null;
			Statement st1 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ReportDAO.OpenCon("", "", "", repBean);
			// con1 = repBean.getCn();

			int j = i;
			int count1 = 1;
			ResultSet rs1 = st1.executeQuery(
					"select e.salute ,e.dob, e.empno, CONVERT(INT, e.empcode) as empcode,e.fname,e.mname,e.lname,e.pfno,e.uanno, e.DOJ, "
							+ "e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO, (SELECT ( INP_AMT )  FROM   paytran_stage  WHERE  trncd IN ( 999 )  AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND empno = e.empno) AS gross,  case when exists (select f3 from OTHERDETAIL where EMPNO=e.empno) then  "
							+ "(select f3 from OTHERDETAIL where EMPNO=e.empno) else (SELECT '' AS ifsc) END AS 'ifsc', case when exists  "
							+ "(select telno from EMPAUX where EMPNO=e.empno) then (select aux.telno from EMPAUX aux where aux.EMPNO=e.empno "
							+ "and id= (select MAX(id) from EMPAUX  where EMPNO=e.empno)) else (SELECT 0 AS telno) END AS 'telno', CASE WHEN  "
							+ "EXISTS(SELECT sum(NET_AMT) FROM   paytran WHERE TRNCD in ( 101, 102, 130 ) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ "AND EMPNO = e.EMPNO)   	THEN	(SELECT sum(NET_AMT) FROM   paytran WHERE TRNCD in ( 101, 102, 130 ) AND TRNDT BETWEEN "
							+ " '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) ELSE	(SELECT 0 AS BASIC_DA) END AS 'BASIC_DA', (SELECT (NET_AMT) "
							+ "FROM   paytran WHERE TRNCD in (201) AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT)
							+ "' and '" + ReportDAO.EOM(PAYREGDT) + "' AND EMPNO = e.EMPNO) AS PF , "
							+ " CASE WHEN EXISTS(SELECT (NET_AMT) FROM   paytran WHERE TRNCD in (301) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND EMPNO = e.EMPNO)  	THEN	(SELECT sum(NET_AMT) FROM   paytran WHERE TRNCD in (301) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' " + "and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) "
							+ "ELSE	(SELECT 0 AS absentDays) END AS 'absentDays' from EMPMAST e join EMPTRAN t on e.EMPNO=t.EMPNO "
							+ "join paytran p on p.EMPNO=e.empno and p.TRNDT between '" + ReportDAO.BOM(PAYREGDT)
							+ "' and '" + ReportDAO.EOM(PAYREGDT) + "' and "
							+ "p.TRNCD=201  and p.NET_AMT>0 and t.SRNO=(select max(srno) from EMPTRAN where EMPNO=t.EMPNO) "
							+ "group by e.SALUTE ,e.dob, e.empno,  e.empcode,e.fname,e.mname,e.lname,e.pfno,e.DOJ,e.UANNO,e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO "
							+ "union "
							+ "select e.salute ,e.dob, e.empno, CONVERT(INT, e.empcode) as empcode,e.fname,e.mname,e.lname,e.pfno,e.uanno, e.DOJ, "
							+ "e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO, (SELECT ( INP_AMT )  FROM   paytran_stage  WHERE  trncd IN ( 999 )  AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND empno = e.empno) AS gross, case when exists (select f3 from OTHERDETAIL where EMPNO=e.empno) then "
							+ "(select f3 from OTHERDETAIL where EMPNO=e.empno) else (SELECT '' AS ifsc) END AS 'ifsc', case when exists "
							+ "(select telno from EMPAUX where EMPNO=e.empno) then (select aux.telno from EMPAUX aux where aux.EMPNO=e.empno "
							+ "and id= (select MAX(id) from EMPAUX  where EMPNO=e.empno)) else (SELECT 0 AS telno) END AS 'telno', CASE WHEN "
							+ "EXISTS(SELECT sum(NET_AMT) FROM   paytran_stage WHERE TRNCD in ( 101, 102, 130 ) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ "AND EMPNO = e.EMPNO)   	THEN	(SELECT sum(NET_AMT) FROM   paytran_stage WHERE TRNCD in ( 101, 102, 130 ) AND TRNDT BETWEEN "
							+ " '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) ELSE	(SELECT 0 AS BASIC_DA) END AS 'BASIC_DA', "
							+ "(SELECT (NET_AMT) FROM   paytran_stage WHERE TRNCD in (201) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) AS PF , "
							+ "CASE WHEN EXISTS(SELECT (NET_AMT) FROM   paytran_stage WHERE TRNCD in (301) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND EMPNO = e.EMPNO)  	THEN	(SELECT sum(NET_AMT) FROM   paytran_stage WHERE TRNCD in (301) AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' " + " and '" + ReportDAO.EOM(PAYREGDT)
							+ "' AND EMPNO = e.EMPNO) "
							+ " ELSE	(SELECT 0 AS absentDays) END AS 'absentDays' from EMPMAST e join EMPTRAN t on e.EMPNO=t.EMPNO "
							+ " join paytran_stage p on p.EMPNO=e.empno and p.TRNDT between '" + ReportDAO.BOM(PAYREGDT)
							+ "' and '" + ReportDAO.EOM(PAYREGDT) + "' and " +
							// " p.TRNCD=201 and p.NET_AMT>0 and t.SRNO=(select
							// max(srno) from EMPTRAN where EMPNO=t.EMPNO) "+
							" p.TRNCD=201  and p.NET_AMT=0 and t.SRNO=(select max(srno) from EMPTRAN where EMPNO=t.EMPNO) "
							+ " group by e.SALUTE ,e.dob, e.empno,  e.empcode,e.fname,e.mname,e.lname,e.pfno,e.DOJ,e.UANNO,e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO "
							+

							"union " +

							"select e.salute ,e.dob, e.empno, CONVERT(INT, e.empcode) "
							+ "as empcode,e.fname,e.mname,e.lname,e.pfno,e.uanno, "
							+ "e.DOJ,e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO, (SELECT ( INP_AMT )  FROM   paytran_stage  WHERE  trncd IN ( 999 )  AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ " AND empno = e.empno) AS gross, "
							+ "case when exists (select f3 from OTHERDETAIL where EMPNO=e.empno) "
							+ "then (select f3 from OTHERDETAIL where EMPNO=e.empno) "
							+ "else (SELECT '' AS ifsc) END AS 'ifsc', "
							+ "case when exists (select telno from EMPAUX where EMPNO=e.empno) "
							+ "then (select aux.telno from EMPAUX aux where aux.EMPNO=e.empno and " + "id= "
							+ "(select MAX(id) from EMPAUX  where EMPNO=e.empno)) "
							+ "else (SELECT 0 AS telno) END AS 'telno', "
							+ "CASE WHEN EXISTS(SELECT sum(NET_AMT) FROM   ytdtran WHERE TRNCD in ( 101, 102, 130 ) "
							+ "AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' " + "AND EMPNO = e.EMPNO)   "
							+ "	THEN	(SELECT sum(NET_AMT) FROM   ytdtran WHERE TRNCD in ( 101, 102, 130 ) "
							+ "AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' " + "AND EMPNO = e.EMPNO) ELSE	(SELECT 0 AS BASIC_DA) END AS 'BASIC_DA', "
							+ "(SELECT (NET_AMT)  FROM   ytdtran WHERE TRNCD in (201) " + "AND TRNDT BETWEEN '"
							+ ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT) + "' "
							+ "AND EMPNO = e.EMPNO) AS PF , "
							+ "CASE WHEN EXISTS(SELECT (NET_AMT) FROM   ytdtran WHERE TRNCD in (301) "
							+ "AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' " + "AND EMPNO = e.EMPNO)  "
							+ "	THEN	(SELECT sum(NET_AMT) FROM   ytdtran WHERE TRNCD in (301) "
							+ "AND TRNDT BETWEEN '" + ReportDAO.BOM(PAYREGDT) + "' and '" + ReportDAO.EOM(PAYREGDT)
							+ "' " + "AND EMPNO = e.EMPNO) ELSE	(SELECT 0 AS absentDays) END AS 'absentDays' "
							+ "from EMPMAST e " + "join EMPTRAN t " + "on e.EMPNO=t.EMPNO " + "join ytdtran p "
							+ "on p.EMPNO=e.empno " + "and p.TRNDT between '" + ReportDAO.BOM(PAYREGDT) + "' and '"
							+ ReportDAO.EOM(PAYREGDT) + "' " +
							// "and p.TRNCD=201 and p.NET_AMT>0 "+
							"and p.TRNCD=201  and p.NET_AMT=0 "
							+ "and t.SRNO=(select max(srno) from EMPTRAN where EMPNO=t.EMPNO) "
							+ "group by e.SALUTE ,e.dob, e.empno,  e.empcode,e.fname,e.mname,e.lname,e.pfno,e.DOJ,e.UANNO,e.AADHAARNUM,e.PANNO,t.ACNO,t.PRJ_SRNO "
							+ "order by empcode ");

			// int l = 0;
			while (rs1.next()) {
				k++;

				HSSFRow row1 = sheet.createRow((short) j++);

				row1.createCell((short) 0).setCellValue("" + (count1++));
				row1.createCell((short) 1).setCellValue(
						"" + rs1.getString("FNAME") + " " + rs1.getString("MNAME") + " " + rs1.getString("LNAME"));
				row1.createCell((short) 2).setCellValue("" + rs1.getString("MNAME") + " " + rs1.getString("LNAME"));

				row1.createCell((short) 3).setCellValue("" + (rs1.getString("DOB")));
				row1.createCell((short) 4).setCellValue("" + sdf.format(rs1.getDate("DOJ")));
				row1.createCell((short) 5).setCellValue("" + (rs1.getString("UANNO")));

				row1.createCell((short) 6).setCellValue("" + rs1.getString("PFNO"));
				row1.createCell((short) 7).setCellValue(rs1.getString("AADHAARNUM"));
				row1.createCell((short) 8).setCellValue("" + rs1.getString("PANNO"));
				row1.createCell((short) 9).setCellValue("" + rs1.getString("ACNO"));
				String ifsc = "" + rs1.getString(
						"ifsc");/* !=null?""+emp.getString("ifsc"):""; */
				if (ifsc.equalsIgnoreCase("null")) {
					row1.createCell((short) 10).setCellValue("");

				} else {
					row1.createCell((short) 10).setCellValue("" + rs1.getString("ifsc"));
				}
				String mob = "" + rs1.getString(
						"TELNO");/* !=null?""+emp.getString("ifsc"):""; */
				if (mob.equalsIgnoreCase("null")) {
					row1.createCell((short) 11).setCellValue("");

				} else {
					row1.createCell((short) 11).setCellValue("" + rs1.getString("TELNO"));
				}

				row1.createCell((short) 12).setCellValue("" + rs1.getString("absentDays"));
				row1.createCell((short) 13).setCellValue("" + rs1.getString("BASIC_DA"));

				brtot[1] = brtot[1] + Math.round(rs1.getInt("BASIC_DA"));
				gross = (rs1.getString("BASIC_DA") != null ? rs1.getInt("BASIC_DA") : 14);

				row1.createCell((short) 14).setCellValue("" + rs1.getString("PF"));
				brtot[2] = brtot[2] + Math.round(rs1.getInt("PF"));
				row1.createCell((short) 15).setCellValue("" + rs1.getInt("gross"));
				brtot[3] = brtot[3] + Math.round(rs1.getInt("gross"));

			}

			HSSFRow row1 = sheet.createRow((short) j);
			row1.createCell((short) 0).setCellValue(" ");
			row1 = sheet.createRow((short) j + 1);

			row1.createCell((short) 1).setCellValue("Total Employee : =  " + k);
			row1.createCell((short) 12).setCellValue("Total : = ");
			row1.createCell((short) 13).setCellValue("" + trans(brtot[1], "", "", true, true));
			row1.createCell((short) 14).setCellValue("" + trans(brtot[2], "", "", true, true));
			row1.createCell((short) 15).setCellValue("" + trans(brtot[3], "", "", true, true));

			/*************************************** Prasad-End *****************************************************/

			/************************/
			hwb.write(out1);
			out1.close();
			st.close();
			st1.close();
			con.close();
			// con1.close();
			/************************/

			System.out.println("Result OK.....");

			/*************************************** Prasad *****************************************************/

			/********************************************************************************************/

		}
		// }
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void EMPLIST(String date, String date1, String type, String filepath, String imagepath) {
		Properties prop = new Properties();
		try {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		} catch (Exception e) {
			System.out.println("Error in constant properties Manager " + e);
		}

		try {
			System.out.println("Into Generalised_EmpListDAO....." + date + "  " + ReportDAO.EOM(date1));
			RepoartBean repBean = new RepoartBean();

			LookupHandler lkh = new LookupHandler();
			Connection con = null;
			System.out.println(filepath);
			FileOutputStream out1 = new FileOutputStream(new File(filepath));
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("Generalise Emplist");

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			sheet.setColumnWidth((short) 0, (short) 3000);
			sheet.setColumnWidth((short) 1, (short) 3000);
			sheet.setColumnWidth((short) 2, (short) 6000);
			sheet.setColumnWidth((short) 3, (short) 3000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4000);
			sheet.setColumnWidth((short) 6, (short) 4000);
			sheet.setColumnWidth((short) 7, (short) 3000);
			sheet.setColumnWidth((short) 8, (short) 4000);
			sheet.setColumnWidth((short) 9, (short) 4000);
			sheet.setColumnWidth((short) 10, (short) 5000);
			sheet.setColumnWidth((short) 11, (short) 20000);
			sheet.setColumnWidth((short) 12, (short) 4000);
			sheet.setColumnWidth((short) 13, (short) 7000);
			sheet.setColumnWidth((short) 14, (short) 7000);
			sheet.setColumnWidth((short) 15, (short) 7000);
			sheet.setColumnWidth((short) 16, (short) 5000);
			sheet.setColumnWidth((short) 17, (short) 4000);
			sheet.setColumnWidth((short) 18, (short) 5000);
			sheet.setColumnWidth((short) 19, (short) 5000);
			sheet.setColumnWidth((short) 20, (short) 2000);

			HSSFCellStyle my_style = hwb.createCellStyle();
			HSSFCellStyle my_style1 = hwb.createCellStyle();

			HSSFFont my_font = hwb.createFont();
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style.setFont(my_font);

			HSSFRow rowtitle = sheet.createRow((short) 0);
			HSSFCell cell = rowtitle.createCell((short) 9);

			cell.setCellValue(prop.getProperty("companyName"));
			cell.setCellStyle(my_style);
			HSSFRow rowtitle1 = sheet.createRow((short) 1);
			HSSFCell cell1 = rowtitle1.createCell((short) 7);
			cell1.setCellValue(prop.getProperty("addressForReport"));
			cell1.setCellStyle(my_style);
			HSSFRow rowtitle2 = sheet.createRow((short) 2);
			HSSFCell cell2 = rowtitle2.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("contactForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle31 = sheet.createRow((short) 3);
			cell2 = rowtitle31.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("mailForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle3 = sheet.createRow((short) 4);
			cell2 = rowtitle3.createCell((short) 2);
			cell2.setCellValue("");
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle4 = sheet.createRow((short) 5);
			rowtitle4.createCell((short) 0).setCellValue("");
			HSSFRow rowtitle5 = sheet.createRow((short) 6);
			rowtitle5.createCell((short) 0).setCellValue("");

			HSSFFont blueFont = hwb.createFont();
			blueFont.setColor(HSSFColor.BLUE.index);

			HSSFCellStyle style = hwb.createCellStyle();
			// style.setFont(blueFont);
			style.setFillForegroundColor(HSSFColor.BLUE.index);

			HSSFRow head = sheet.createRow((short) 7);
			head.createCell((short) 0).setCellValue("");
			HSSFRow heading = sheet.createRow((short) 8);
			HSSFCell cell3 = heading.createCell((short) 0);

			cell3.setCellValue("");
			cell3.setCellStyle(my_style1);
			HSSFRow rowhead = sheet.createRow((short) 9);
			sheet.createFreezePane(0, 10, 0, 10);

			my_style1.setAlignment((short) 2);
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style1.setFont(my_font);

			cell3 = rowhead.createCell((short) 0);
			cell3.setCellValue("SR.NO");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 1);
			cell3.setCellValue("Emp Code");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 2);
			cell3.setCellValue("Employee Name");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 3);
			cell3.setCellValue("Gender");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 4);
			cell3.setCellValue("DOB");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 5);
			cell3.setCellValue("DOJ");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 6);
			cell3.setCellValue("DOL");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 7);
			cell3.setCellValue("AGE");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 8);
			cell3.setCellValue("Super Ann");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 9);
			cell3.setCellValue("PAN NO.");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 10);
			cell3.setCellValue("PF NO.");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 11);
			cell3.setCellValue("Project name");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 12);
			cell3.setCellValue("Project Code");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 13);
			cell3.setCellValue("Designation");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 14);
			cell3.setCellValue("Department");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 15);
			cell3.setCellValue("BANK");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 16);
			cell3.setCellValue("Account No");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 17);
			cell3.setCellValue("Status");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 18);
			cell3.setCellValue(" Monthly CTC");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 19);
			cell3.setCellValue("BASIC");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 20);
			cell3.setCellValue("EMPNO");
			cell3.setCellStyle(my_style1);

			ReportDAO.OpenCon("", "", "", repBean);
			con = repBean.getCn();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int i = 12;
			int count = 1;
			ResultSet rs = st.executeQuery(
					"with ctcs as ( select c.empno , sum(inp_amt) ctc ,(SELECT ct.INP_AMT     FROM   ctcdisplay ct where ct.TRNCD =101 and ct.EMPNO =c.EMPNO) as basic  from CTCDISPLAY c group by c.EMPNO )     SELECT distinct E.EMPNO,E.EMPCODE,E.FNAME,E.MNAME,E.LNAME,E.SALUTE,E.DOJ,E.DOL,	DATEADD(YEAR,60,E.DOB),DATEDIFF(DAY, E.DOB, GetDate()) / 365.25,E.DOB,E.gender,E.PANNO,E.PFNO,E.STATUS,"
							+ "				T.PRJ_SRNO,T.DEPT,T.DESIG,T.BANK_NAME,T.ACNO, c.ctc,c.basic FROM EMPMAST E,emptran T  "
							+ "             ,ctcs c   Where E.EMPNO = T.EMPNO"
							+ "				 and T.SRNO = (select MAX(et.SRNO) from emptran et where et.empno =E.EMPNO)	   and ((E.STATUS ='A') or(e.DOL between '"
							+ date + "' and '" + ReportDAO.EOM(date1) + "')) and ((E.STATUS ='N') or(e.DOJ between '"
							+ date + "' and '" + ReportDAO.EOM(date1)
							+ "'))   and  c.EMPNO = E.EMPNO			order by E.EMPNO ");

			System.out.println("EMPLIST : "+
					"with ctcs as ( select c.empno , sum(inp_amt) ctc ,(SELECT ct.INP_AMT     FROM   ctcdisplay ct where ct.TRNCD =101 and ct.EMPNO =c.EMPNO) as basic  from CTCDISPLAY c group by c.EMPNO )     SELECT distinct E.EMPNO,E.EMPCODE,E.FNAME,E.MNAME,E.LNAME,E.SALUTE,E.DOJ,E.DOL,	DATEADD(YEAR,60,E.DOB),DATEDIFF(DAY, E.DOB, GetDate()) / 365.25,E.DOB,E.gender,E.PANNO,E.PFNO,E.STATUS,"
							+ "				T.PRJ_SRNO,T.DEPT,T.DESIG,T.BANK_NAME,T.ACNO, c.ctc,c.basic FROM EMPMAST E,emptran T  "
							+ "             ,ctcs c   Where E.EMPNO = T.EMPNO"
							+ "				 and T.SRNO = (select MAX(et.SRNO) from emptran et where et.empno =E.EMPNO)	   and ((E.STATUS ='A') or(e.DOL between '"
							+ date + "' and '" + ReportDAO.EOM(date1) + "')) and ((E.STATUS ='N') or(e.DOJ between '"
							+ date + "' and '" + ReportDAO.EOM(date1)
							+ "'))   and  c.EMPNO = E.EMPNO			order by E.EMPNO ");
			while (rs.next()) {
				HSSFRow row = sheet.createRow((short) i++);

				row.createCell((short) 0).setCellValue("" + (count++));
				row.createCell((short) 1).setCellValue("" + rs.getString("empcode"));
				row.createCell((short) 2).setCellValue(
						"" + rs.getString("FNAME") + " " + rs.getString("MNAME") + " " + rs.getString("LNAME"));

				row.createCell((short) 3)
						.setCellValue("" + (rs.getString("gender").equalsIgnoreCase("m") ? "MALE" : "FEMALE"));
				row.createCell((short) 4).setCellValue("" + sdf.format(rs.getDate("DOB")));
				row.createCell((short) 5).setCellValue("" + sdf.format(rs.getDate("DOJ")));
				row.createCell((short) 6).setCellValue("" + rs.getString("DOL") == null ? "" : rs.getString("DOL"));
				row.createCell((short) 7).setCellValue(rs.getInt(10));
				row.createCell((short) 8).setCellValue("" + sdf.format(rs.getDate(9)));
				row.createCell((short) 9).setCellValue("" + rs.getString("PANNO"));
				row.createCell((short) 10).setCellValue("" + rs.getString("PFNO"));
				String prjquery = "select Site_Name,Project_Code from Project_Sites where SITE_ID = '"
						+ rs.getString("PRJ_SRNO") + "'";
				Connection conn = ConnectionManager.getConnectionTech();
				Statement stmt = conn.createStatement();
				ResultSet prj = stmt.executeQuery(prjquery);
				String prj_name = "";
				String prj_code = "";

				if (prj.next()) {
					prj_name = prj.getString("Site_Name");
					prj_code = prj.getString("Project_Code");
				}

				row.createCell((short) 11).setCellValue(prj_name);
				row.createCell((short) 12).setCellValue(prj_code);
				row.createCell((short) 13).setCellValue(lkh.getLKP_Desc("DESIG", rs.getInt("DESIG")));
				row.createCell((short) 14).setCellValue(lkh.getLKP_Desc("DEPT", rs.getInt("DEPT")));
				row.createCell((short) 15).setCellValue(lkh.getLKP_Desc("BANK", rs.getInt("BANK_NAME")));
				row.createCell((short) 16).setCellValue(rs.getString("ACNO"));

				row.createCell((short) 17)
						.setCellValue((rs.getString("status")).equalsIgnoreCase("A") ? "Active" : "Non-Active");

				row.createCell((short) 18).setCellValue(rs.getString("CTC"));
				row.createCell((short) 19).setCellValue(rs.getString("basic"));
				row.createCell((short) 20).setCellValue(rs.getString("EMPNO"));

			}

			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

			Calendar calobj = Calendar.getInstance();

			HSSFRow row = sheet.createRow((short) i);
			row.createCell((short) 0).setCellValue(" ");
			row = sheet.createRow((short) i + 1);
			row.createCell((short) 0).setCellValue("Report Date And Time" + df.format(calobj.getTime()));

			hwb.write(out1);
			out1.close();
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**********************************************************PRASAD START********************************************************************/
	public static void EMPLISTPFREPORT(String date1, String type, String filepath, String imagepath) {

		Properties prop = new Properties();
		try {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		} catch (Exception e) {
			System.out.println("Error in constant properties Manager " + e);
		}

		try {
			System.out.println("Into Generalised_EmpListDAO....." + date1 + "  " + ReportDAO.EOM(date1));
			RepoartBean repBean = new RepoartBean();

			LookupHandler lkh = new LookupHandler();
			Connection con = null;
			System.out.println(filepath);
			FileOutputStream out1 = new FileOutputStream(new File(filepath));
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("Generalise Emplist");

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			sheet.setColumnWidth((short) 0, (short) 3000);
			sheet.setColumnWidth((short) 1, (short) 3000);
			sheet.setColumnWidth((short) 2, (short) 6000);
			sheet.setColumnWidth((short) 3, (short) 3000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4000);
			sheet.setColumnWidth((short) 6, (short) 4000);
			sheet.setColumnWidth((short) 7, (short) 3000);
			sheet.setColumnWidth((short) 8, (short) 4000);
			sheet.setColumnWidth((short) 9, (short) 4000);
			sheet.setColumnWidth((short) 10, (short) 5000);
			sheet.setColumnWidth((short) 11, (short) 20000);
			sheet.setColumnWidth((short) 12, (short) 4000);
			sheet.setColumnWidth((short) 13, (short) 7000);
			sheet.setColumnWidth((short) 14, (short) 7000);
			sheet.setColumnWidth((short) 15, (short) 7000);
			sheet.setColumnWidth((short) 16, (short) 5000);
			sheet.setColumnWidth((short) 17, (short) 4000);
			sheet.setColumnWidth((short) 18, (short) 5000);
			sheet.setColumnWidth((short) 19, (short) 5000);
			sheet.setColumnWidth((short) 20, (short) 2000);
			sheet.setColumnWidth((short) 21, (short) 2000);

			HSSFCellStyle my_style = hwb.createCellStyle();
			HSSFCellStyle my_style1 = hwb.createCellStyle();

			HSSFFont my_font = hwb.createFont();
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style.setFont(my_font);

			HSSFRow rowtitle = sheet.createRow((short) 0);
			HSSFCell cell = rowtitle.createCell((short) 9);

			cell.setCellValue(prop.getProperty("companyName"));
			cell.setCellStyle(my_style);
			HSSFRow rowtitle1 = sheet.createRow((short) 1);
			HSSFCell cell1 = rowtitle1.createCell((short) 7);
			cell1.setCellValue(prop.getProperty("addressForReport"));
			cell1.setCellStyle(my_style);
			HSSFRow rowtitle2 = sheet.createRow((short) 2);
			HSSFCell cell2 = rowtitle2.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("contactForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle31 = sheet.createRow((short) 3);
			cell2 = rowtitle31.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("mailForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle3 = sheet.createRow((short) 4);
			cell2 = rowtitle3.createCell((short) 2);
			cell2.setCellValue("");
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle4 = sheet.createRow((short) 5);
			rowtitle4.createCell((short) 0).setCellValue("");
			HSSFRow rowtitle5 = sheet.createRow((short) 6);
			rowtitle5.createCell((short) 0).setCellValue("");

			HSSFFont blueFont = hwb.createFont();
			blueFont.setColor(HSSFColor.BLUE.index);

			HSSFCellStyle style = hwb.createCellStyle();
			// style.setFont(blueFont);
			style.setFillForegroundColor(HSSFColor.BLUE.index);

			HSSFRow head = sheet.createRow((short) 7);
			head.createCell((short) 0).setCellValue("");
			HSSFRow heading = sheet.createRow((short) 8);
			HSSFCell cell3 = heading.createCell((short) 0);

			cell3.setCellValue("");
			cell3.setCellStyle(my_style1);
			HSSFRow rowhead = sheet.createRow((short) 9);
			sheet.createFreezePane(0, 10, 0, 10);

			my_style1.setAlignment((short) 2);
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style1.setFont(my_font);

			cell3 = rowhead.createCell((short) 0);
			cell3.setCellValue("SR.NO");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 1);
			cell3.setCellValue("Emp Code");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 2);
			cell3.setCellValue("Employee Name");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 3);
			cell3.setCellValue("Gender");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 4);
			cell3.setCellValue("DOB");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 5);
			cell3.setCellValue("DOJ");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 6);
			cell3.setCellValue("DOL");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 7);
			cell3.setCellValue("AGE");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 8);
			cell3.setCellValue("Super Ann");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 9);
			cell3.setCellValue("PAN NO.");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 10);
			cell3.setCellValue("PF/UAN NO.");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 11);
			cell3.setCellValue("Project name");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 12);
			cell3.setCellValue("Project Code");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 13);
			cell3.setCellValue("Designation");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 14);
			cell3.setCellValue("Department");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 15);
			cell3.setCellValue("BANK");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 16);
			cell3.setCellValue("Account No");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 17);
			cell3.setCellValue("Status");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 18);
			cell3.setCellValue(" Monthly CTC");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 19);
			cell3.setCellValue("BASIC");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 20);
			cell3.setCellValue("PF AMT");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 21);
			cell3.setCellValue("EMPNO");
			cell3.setCellStyle(my_style1);

			ReportDAO.OpenCon("", "", "", repBean);
			con = repBean.getCn();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int i = 12;
			int count = 1;
			/*ResultSet rs = st.executeQuery(
					"with ctcs as ( select c.empno , sum(inp_amt) ctc ,(SELECT ct.INP_AMT     FROM   ctcdisplay ct where ct.TRNCD =101 and ct.EMPNO =c.EMPNO) as basic  from CTCDISPLAY c group by c.EMPNO )     SELECT distinct E.EMPNO,E.EMPCODE,E.FNAME,E.MNAME,E.LNAME,E.SALUTE,E.DOJ,E.DOL,	DATEADD(YEAR,60,E.DOB),DATEDIFF(DAY, E.DOB, GetDate()) / 365.25,E.DOB,E.gender,E.PANNO,E.PFNO,E.STATUS,"
							+ "				T.PRJ_SRNO,T.DEPT,T.DESIG,T.BANK_NAME,T.ACNO, c.ctc,c.basic FROM EMPMAST E,emptran T  "
							+ "             ,ctcs c   Where E.EMPNO = T.EMPNO"
							+ "				 and T.SRNO = (select MAX(et.SRNO) from emptran et where et.empno =E.EMPNO)	   and ((E.STATUS ='A') or(e.DOL between '"
							+  "' and '" + ReportDAO.EOM(date1) + "')) and ((E.STATUS ='N') or(e.DOJ between '"
							+  "' and '" + ReportDAO.EOM(date1)
							+ "'))   and  c.EMPNO = E.EMPNO			order by E.EMPNO ");

			System.out.println("EMPLIST : "+
					"with ctcs as ( select c.empno , sum(inp_amt) ctc ,(SELECT ct.INP_AMT     FROM   ctcdisplay ct where ct.TRNCD =101 and ct.EMPNO =c.EMPNO) as basic  from CTCDISPLAY c group by c.EMPNO )     SELECT distinct E.EMPNO,E.EMPCODE,E.FNAME,E.MNAME,E.LNAME,E.SALUTE,E.DOJ,E.DOL,	DATEADD(YEAR,60,E.DOB),DATEDIFF(DAY, E.DOB, GetDate()) / 365.25,E.DOB,E.gender,E.PANNO,E.PFNO,E.STATUS,"
							+ "				T.PRJ_SRNO,T.DEPT,T.DESIG,T.BANK_NAME,T.ACNO, c.ctc,c.basic FROM EMPMAST E,emptran T  "
							+ "             ,ctcs c   Where E.EMPNO = T.EMPNO"
							+ "				 and T.SRNO = (select MAX(et.SRNO) from emptran et where et.empno =E.EMPNO)	   and ((E.STATUS ='A') or(e.DOL between '"
							+  "' and '" + ReportDAO.EOM(date1) + "')) and ((E.STATUS ='N') or(e.DOJ between '"
							+  "' and '" + ReportDAO.EOM(date1)
							+ "'))   and  c.EMPNO = E.EMPNO			order by E.EMPNO ");*/
			
			ResultSet rs = st.executeQuery(
					"WITH ctcs "
							+ "     AS (SELECT ct.empno, "
							+ "                (SELECT pt.net_amt "
							+ "                 FROM   paytran_stage pt "
							+ "                 WHERE  pt.trncd = 199 and pt.TRNDT='"+ReportDAO.EOM(date1)+"' AND pt.empno = ct.empno) AS ctcnew, "
							+ "                 (SELECT pt.net_amt "
							+ "                 FROM   paytran_stage pt "
							+ "                 WHERE  pt.trncd = 101 and pt.TRNDT='"+ReportDAO.EOM(date1)+"' AND pt.empno = ct.empno) AS basic, "
							+ "                 (SELECT pt.net_amt "
							+ "                 FROM   paytran_stage pt "
							+ "                 WHERE  pt.trncd = 201 and pt.TRNDT='"+ReportDAO.EOM(date1)+"' AND pt.empno = ct.empno) AS pf "
							+ "         FROM   CTCDISPLAY ct "
							+ "         GROUP  BY ct.empno) "
							+ "SELECT DISTINCT E.empno, E.empcode, E.fname, E.mname, E.lname, E.salute, E.doj, E.dol, "
							+ "                Dateadd(year, 60, E.dob), Datediff(day, E.dob, Getdate()) / 365.25, "
							//+ "                E.dob, E.gender, E.panno, E.pfno, E.status, T.prj_srno, T.dept, T.desig, T.bank_name, T.acno, c.ctcnew, c.basic, c.pf "
							+ "                E.dob, E.gender, E.panno, E.UANNO, E.status, T.prj_srno, T.dept, T.desig, T.bank_name, T.acno, c.ctcnew, c.basic, c.pf "
							+ "FROM   empmast E,  emptran T, ctcs c "
							+ "WHERE  E.empno = T.empno "
							+ "       AND T.srno = (SELECT Max(et.srno) "
							+ "                     FROM   emptran et "
							+ "                     WHERE  et.empno = E.empno) "
							//+ "       AND ( ( E.status = 'A' ) "
							//+ "              OR ( e.dol <= '"+ReportDAO.EOM(date1)+"' ) ) "
							//+ "       AND ( ( E.status = 'N' ) "
							//+ "              OR ( e.doj <= '"+ReportDAO.EOM(date1)+"' ) ) "
							+ "   AND E.doj <= '"+ReportDAO.EOM(date1)+"'   "
							+ "       AND c.empno = E.empno "
							+ "ORDER  BY E.empno");

			System.out.println("EMPLIST : "+
					"WITH ctcs "
							+ "     AS (SELECT ct.empno, "
							+ "                (SELECT pt.net_amt "
							+ "                 FROM   paytran_stage pt "
							+ "                 WHERE  pt.trncd = 199 and pt.TRNDT='"+ReportDAO.EOM(date1)+"' AND pt.empno = ct.empno) AS ctcnew, "
							+ "                 (SELECT pt.net_amt "
							+ "                 FROM   paytran_stage pt "
							+ "                 WHERE  pt.trncd = 101 and pt.TRNDT='"+ReportDAO.EOM(date1)+"' AND pt.empno = ct.empno) AS basic, "
							+ "                 (SELECT pt.net_amt "
							+ "                 FROM   paytran_stage pt "
							+ "                 WHERE  pt.trncd = 201 and pt.TRNDT='"+ReportDAO.EOM(date1)+"' AND pt.empno = ct.empno) AS pf "
							+ "         FROM   CTCDISPLAY ct "
							+ "         GROUP  BY ct.empno) "
							+ "SELECT DISTINCT E.empno, E.empcode, E.fname, E.mname, E.lname, E.salute, E.doj, E.dol, "
							+ "                Dateadd(year, 60, E.dob), Datediff(day, E.dob, Getdate()) / 365.25, "
							+ "                E.dob, E.gender, E.panno, E.pfno, E.status, T.prj_srno, T.dept, T.desig, T.bank_name, T.acno, c.ctcnew, c.basic, c.pf "
							+ "FROM   empmast E,  emptran T, ctcs c "
							+ "WHERE  E.empno = T.empno "
							+ "       AND T.srno = (SELECT Max(et.srno) "
							+ "                     FROM   emptran et "
							+ "                     WHERE  et.empno = E.empno) "
							//+ "       AND ( ( E.status = 'A' ) "
							//+ "              OR ( e.dol <= '"+ReportDAO.EOM(date1)+"' ) ) "
							//+ "       AND ( ( E.status = 'N' ) "
							//+ "              OR ( e.doj <= '"+ReportDAO.EOM(date1)+"' ) ) "
							+ "   AND E.doj <= '"+ReportDAO.EOM(date1)+"'   "
							+ "       AND c.empno = E.empno "
							+ "ORDER  BY E.empno");
			
			
			while (rs.next()) {
				HSSFRow row = sheet.createRow((short) i++);

				row.createCell((short) 0).setCellValue("" + (count++));
				row.createCell((short) 1).setCellValue("" + rs.getString("empcode"));
				row.createCell((short) 2).setCellValue(
						"" + rs.getString("FNAME") + " " + rs.getString("MNAME") + " " + rs.getString("LNAME"));

				row.createCell((short) 3)
						.setCellValue("" + (rs.getString("gender").equalsIgnoreCase("m") ? "MALE" : "FEMALE"));
				row.createCell((short) 4).setCellValue("" + sdf.format(rs.getDate("DOB")));
				row.createCell((short) 5).setCellValue("" + sdf.format(rs.getDate("DOJ")));
				row.createCell((short) 6).setCellValue("" + rs.getString("DOL") == null ? "" : rs.getString("DOL"));
				row.createCell((short) 7).setCellValue(rs.getInt(10));
				row.createCell((short) 8).setCellValue("" + sdf.format(rs.getDate(9)));
				row.createCell((short) 9).setCellValue("" + rs.getString("PANNO"));
				/*row.createCell((short) 10).setCellValue("" + rs.getString("PFNO"));*/
				row.createCell((short) 10).setCellValue("" + rs.getString("UANNO"));
				String prjquery = "select Site_Name,Project_Code from Project_Sites where SITE_ID = '"
						+ rs.getString("PRJ_SRNO") + "'";
				Connection conn = ConnectionManager.getConnectionTech();
				Statement stmt = conn.createStatement();
				ResultSet prj = stmt.executeQuery(prjquery);
				String prj_name = "";
				String prj_code = "";

				if (prj.next()) {
					prj_name = prj.getString("Site_Name");
					prj_code = prj.getString("Project_Code");
				}

				row.createCell((short) 11).setCellValue(prj_name);
				row.createCell((short) 12).setCellValue(prj_code);
				row.createCell((short) 13).setCellValue(lkh.getLKP_Desc("DESIG", rs.getInt("DESIG")));
				row.createCell((short) 14).setCellValue(lkh.getLKP_Desc("DEPT", rs.getInt("DEPT")));
				row.createCell((short) 15).setCellValue(lkh.getLKP_Desc("BANK", rs.getInt("BANK_NAME")));
				row.createCell((short) 16).setCellValue(rs.getString("ACNO"));

				row.createCell((short) 17)
						.setCellValue((rs.getString("status")).equalsIgnoreCase("A") ? "Active" : "Non-Active");

				row.createCell((short) 18).setCellValue(rs.getString("ctcnew"));
				row.createCell((short) 19).setCellValue(rs.getString("basic"));
				row.createCell((short) 20).setCellValue(rs.getString("pf"));
				row.createCell((short) 21).setCellValue(rs.getString("EMPNO"));

			}

			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

			Calendar calobj = Calendar.getInstance();

			HSSFRow row = sheet.createRow((short) i);
			row.createCell((short) 0).setCellValue(" ");
			row = sheet.createRow((short) i + 1);
			row.createCell((short) 0).setCellValue("Report Date And Time" + df.format(calobj.getTime()));

			hwb.write(out1);
			out1.close();
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
	
	/**********************************************************PRASAD END********************************************************************/

	public static void ecrReportNew(String date, String date1, String filepath, String imagepath) {
		System.out.println("From date " + date);
		System.out.println("to date " + date1);

		Properties prop = new Properties();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		} catch (Exception e) {
			System.out.println("Error in constant properties Manager " + e);
		}

		try {
			System.out.println("Into ecr Report.....");
			RepoartBean repBean = new RepoartBean();
			LookupHandler lkh = new LookupHandler();
			Connection con = null;

			FileOutputStream out1 = new FileOutputStream(new File(filepath));
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("ecrReport");

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			sheet.setColumnWidth((short) 0, (short) 5000);
			sheet.setColumnWidth((short) 1, (short) 10000);
			sheet.setColumnWidth((short) 2, (short) 3000);
			sheet.setColumnWidth((short) 3, (short) 3000);
			sheet.setColumnWidth((short) 4, (short) 8000);
			sheet.setColumnWidth((short) 5, (short) 8000);
			sheet.setColumnWidth((short) 6, (short) 5000);
			sheet.setColumnWidth((short) 7, (short) 5000);
			sheet.setColumnWidth((short) 8, (short) 8000);
			sheet.setColumnWidth((short) 9, (short) 8000);
			sheet.setColumnWidth((short) 10, (short) 4000);
			sheet.setColumnWidth((short) 11, (short) 5000);
			sheet.setColumnWidth((short) 12, (short) 5000);
			sheet.setColumnWidth((short) 13, (short) 5000);
			sheet.setColumnWidth((short) 14, (short) 5000);
			sheet.setColumnWidth((short) 15, (short) 5000);
			sheet.setColumnWidth((short) 16, (short) 10000);
			sheet.setColumnWidth((short) 17, (short) 7000);
			sheet.setColumnWidth((short) 18, (short) 4000);
			sheet.setColumnWidth((short) 19, (short) 3000);
			sheet.setColumnWidth((short) 20, (short) 6000);
			sheet.setColumnWidth((short) 21, (short) 6000);
			sheet.setColumnWidth((short) 22, (short) 6000);
			sheet.setColumnWidth((short) 23, (short) 6000);
			sheet.setColumnWidth((short) 24, (short) 6000);

			HSSFCellStyle my_style = hwb.createCellStyle();
			HSSFCellStyle style = hwb.createCellStyle();

			// my_style.setAlignment((short)2);
			my_style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// my_style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

			HSSFFont my_font = hwb.createFont();
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style.setFont(my_font);
			style.setFont(my_font);

			HSSFRow rowtitle = sheet.createRow((short) 0);
			HSSFCell cell = rowtitle.createCell((short) 9);
			cell.setCellValue(prop.getProperty("companyName"));
			cell.setCellStyle(style);

			HSSFRow rowtitle1 = sheet.createRow((short) 1);
			HSSFCell cell1 = rowtitle1.createCell((short) 8);
			cell1.setCellValue(prop.getProperty("addressForReport"));
			cell1.setCellStyle(style);

			HSSFRow rowtitle2 = sheet.createRow((short) 2);
			HSSFCell cell2 = rowtitle2.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("contactForReport"));
			cell2.setCellStyle(style);

			HSSFRow rowtitle31 = sheet.createRow((short) 3);
			cell2 = rowtitle31.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("mailForReport"));
			cell2.setCellStyle(style);

			HSSFRow rowtitle3 = sheet.createRow((short) 4);
			cell2 = rowtitle3.createCell((short) 2);
			cell2.setCellValue("");

			HSSFRow rowtitle4 = sheet.createRow((short) 5);
			rowtitle4.createCell((short) 0).setCellValue("");
			HSSFRow rowtitle5 = sheet.createRow((short) 6);
			rowtitle5.createCell((short) 0).setCellValue("");

			HSSFRow head = sheet.createRow((short) 7);
			head.createCell((short) 0).setCellValue("");
			HSSFRow heading = sheet.createRow((short) 8);
			HSSFCell cell3 = heading.createCell((short) 0);

			HSSFRow rowhead = sheet.createRow((short) 9);
			rowhead.setHeight((short) 1000);

			// code for Freeze the rows
			sheet.createFreezePane(0, 10, 0, 10);
			// code for Freeze the columns
			sheet.createFreezePane(2, 10);

			// setting style property
			my_style.setAlignment((short) 2);
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style.setFont(my_font);
			my_style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
			my_style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			my_style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cell3 = rowhead.createCell((short) 0);
			cell3.setCellValue("Member ID");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 1);
			cell3.setCellValue("Member Name");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 2);
			cell3.setCellValue("EPF Wages");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 3);
			cell3.setCellValue("EPS Wages");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 4);
			cell3.setCellValue("EPF Contribution (EE Share) due");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 5);
			cell3.setCellValue("EPF Contribution (EE Share) being remitted ");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 6);
			cell3.setCellValue("EPS Contribution due");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 7);
			cell3.setCellValue("EPS Contribution being remitted");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 8);
			cell3.setCellValue("Diff EPF and EPS Contribution (ER Share) due");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 9);
			cell3.setCellValue("Diff EPF and EPS Contribution (ER Share) being remitted");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 10);
			cell3.setCellValue("NCP Days");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 11);
			cell3.setCellValue("Refund of Advances");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 12);
			cell3.setCellValue("Arrear EPF Wages");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 13);
			cell3.setCellValue("Arrear EPF EE Share");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 14);
			cell3.setCellValue("Arrear EPF ER Share ");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 15);
			cell3.setCellValue("Arrear EPS Share");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 16);
			cell3.setCellValue("Fathers/Husbands Name ");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 17);
			cell3.setCellValue("Relationship with the Member ");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 18);
			cell3.setCellValue("Date of Birth");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 19);
			cell3.setCellValue("Gender");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 20);
			cell3.setCellValue("Date of Joining EPF");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 21);
			cell3.setCellValue("Date of Joining EPS");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 22);
			cell3.setCellValue("Date of Exit from EPF");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 23);
			cell3.setCellValue(" Date of Exit from EPS");
			cell3.setCellStyle(my_style);

			cell3 = rowhead.createCell((short) 24);
			cell3.setCellValue("Reason for leaving");
			cell3.setCellStyle(my_style);

			String[] dt = date.split("-");
			ReportDAO.OpenCon("", "", "", repBean);
			con = repBean.getCn();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String table_name = "paytran_stage";

			String query = "select e.salute ,e.doj,e.pfno, e.empno,e.empcode,e.fname,e.mname,e.lname, e.fhname,e.dob,e.gender,e.PFOPENDT,e.dol,"
					+ "(select cal_amt from " + table_name + " where trncd=136 and empno=e.empno and trndt between '"
					+ ReportDAO.BOM(date) + "' and '" + ReportDAO.EOM(date) + "') EPFWAGES," + "(select cal_amt from "
					+ table_name + " where trncd=137 and empno=e.empno and trndt between '" + ReportDAO.BOM(date)
					+ "' and '" + ReportDAO.EOM(date) + "') EPSWAGES," + "(select cal_amt from " + table_name
					+ " where trncd=102 and empno=e.empno and trndt between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "') da, " + "(select cal_amt from " + table_name
					+ " where trncd=130 and empno=e.empno and trndt between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "') Earning1, " + "(select cal_amt from " + table_name
					+ " where trncd=131 and empno=e.empno and trndt between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "') Earning2, " + "(select cal_amt from " + table_name
					+ " where trncd=201 and empno=e.empno and trndt between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "') PF, " + "(select cal_amt from " + table_name
					+ " where trncd=231 and empno=e.empno and trndt between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "') EEPF, " + "(select cal_amt from " + table_name
					+ " where trncd=232 and empno=e.empno and trndt between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "') EEPS, " + "(select cal_amt from " + table_name
					+ " where trncd=233 and empno=e.empno and trndt between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "') EDLI, " + "(select cal_amt from " + table_name
					+ " where trncd=234 and empno=e.empno and trndt between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "') EEPFADMIN, " + "(select cal_amt from " + table_name
					+ " where trncd=235 and empno=e.empno and trndt between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "') EDLIADMIN "
					+ "from empmast e ,SAL_DETAILS s where s.EMPNO = e.EMPNO  and s.SAL_MONTH = '" + dt[1] + "-" + dt[2]
					+ "' and e.doj <='" + ReportDAO.EOM(date) + "' " + "and  e.EMPNO in(select distinct EMPNO from "
					+ table_name + " where TRNCD=201) order by e.empno";
			// i= 12 for start printing next row after 12th row
			int i = 12;

			System.out.println("Into ecr Report sql query....." + query);
			ResultSet rs = st.executeQuery(query);

			int epf_total = 0, eps_total = 0, epf_due_total = 0, eps_due_total = 0, diff_due_total = 0, ncp_total = 0,
					refund_total = 0;
			float ac22 = 0, ac21 = 0, ac10 = 0, ac02 = 0;

			while (rs.next()) {
				HSSFRow row = sheet.createRow((short) i++);

				row.createCell((short) 0).setCellValue(rs.getString("pfno"));
				row.createCell((short) 1).setCellValue(
						rs.getString("FNAME") + " " + rs.getString("MNAME") + " " + rs.getString("LNAME"));

				int epf_wages = (rs.getInt("EPFWAGES") + rs.getInt("da"));
				int eps_wages = (rs.getString("EPSWAGES") == null ? 0 : rs.getInt("EPSWAGES"));
				ac02 += Math.round(rs.getFloat("EEPFADMIN"));
				ac10 += Math.round(rs.getFloat("EEPS"));
				ac22 += Math.round(rs.getFloat("EDLIADMIN"));
				ac21 += Math.round(rs.getFloat("EDLI"));

				row.createCell((short) 2).setCellValue(epf_wages);

				epf_total += epf_wages;
				float eps_cont = 0;
				row.createCell((short) 3).setCellValue(eps_wages);

				eps_total += eps_wages;
				eps_cont = Math.round(rs.getInt("EEPS"));
				float epf_cont = Math.round(rs.getInt("PF"));

				epf_due_total += epf_cont;
				row.createCell((short) 4).setCellValue(epf_cont);
				row.createCell((short) 5).setCellValue(epf_cont);

				eps_due_total += eps_cont;
				row.createCell((short) 6).setCellValue(eps_cont);
				row.createCell((short) 7).setCellValue(eps_cont);

				diff_due_total += epf_cont - eps_cont;
				row.createCell((short) 8).setCellValue(epf_cont - eps_cont);
				row.createCell((short) 9).setCellValue(epf_cont - eps_cont);
				row.createCell((short) 10).setCellValue(0);
				row.createCell((short) 11).setCellValue(0);

				// Values are left to show in report

				row.createCell((short) 12)
						.setCellValue("" + rs.getString("Earning1") == null ? "" : rs.getString("Earning1"));
				// row.createCell((short) 13).setCellValue(0);
				row.createCell((short) 14)
						.setCellValue("" + rs.getString("Earning2") == null ? "" : rs.getString("Earning2"));
				// row.createCell((short) 15).setCellValue(0);*/

				row.createCell((short) 16)
						.setCellValue("" + rs.getString("mname") == null ? "" : rs.getString("mname"));

				if (!rs.getString("MNAME").equals("")) {
					row.createCell((short) 17).setCellValue("Father");
				} else {
					row.createCell((short) 17).setCellValue("");
				}

				// row.createCell((short)
				// 17).setCellValue(""+(rs.getString("pfnomirel")));
				row.createCell((short) 18).setCellValue("" + sdf.format(rs.getDate("DOB")));
				row.createCell((short) 19)
						.setCellValue("" + (rs.getString("gender").equalsIgnoreCase("m") ? "MALE" : "FEMALE"));
				row.createCell((short) 20)
						.setCellValue("" + rs.getDate("DOJ") == null ? "" : sdf.format(rs.getDate("DOJ")));
				row.createCell((short) 21)
						.setCellValue("" + rs.getDate("DOJ") == null ? "" : sdf.format(rs.getDate("DOJ")));
				row.createCell((short) 22).setCellValue(rs.getDate("DOL") == null ? "" : sdf.format(rs.getDate("DOL")));
				row.createCell((short) 23).setCellValue(rs.getDate("DOL") == null ? "" : sdf.format(rs.getDate("DOL")));

				// row.createCell((short)
				// 24).setCellValue(""+sdf.format(rs.getDate("")));

			}
			// i++ for empty row
			i++;

			HSSFRow rowTotal = sheet.createRow((short) i);
			rowTotal.createCell((short) 0).setCellValue("");
			HSSFCell cell4 = rowTotal.createCell((short) 1);
			cell4.setCellValue("GRAND TOTAL :");
			cell4.setCellStyle(style);
			rowTotal.createCell((short) 2).setCellValue(epf_total);
			rowTotal.createCell((short) 3).setCellValue(eps_total);
			rowTotal.createCell((short) 4).setCellValue(epf_due_total);
			rowTotal.createCell((short) 5).setCellValue(epf_due_total);
			rowTotal.createCell((short) 6).setCellValue(eps_due_total);
			rowTotal.createCell((short) 7).setCellValue(eps_due_total);
			rowTotal.createCell((short) 8).setCellValue(diff_due_total);
			rowTotal.createCell((short) 9).setCellValue(diff_due_total);
			rowTotal.createCell((short) 10).setCellValue(ncp_total);
			rowTotal.createCell((short) 11).setCellValue(refund_total);

			// i++ for empty row
			i++;

			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Calendar calobj = Calendar.getInstance();
			HSSFRow row = sheet.createRow((short) i);
			row.createCell((short) 0).setCellValue(" ");
			row = sheet.createRow((short) i + 1);
			row.createCell((short) 0).setCellValue("Report Date And Time" + df.format(calobj.getTime()));

			hwb.write(out1);
			out1.close();
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String trans(int p_num, String p_pic, String p_lzc, boolean p_sgn, boolean blankif0) {
		NumberFormat format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		String result = format.format(p_num).substring(4);
		int Reslength = result.length();
		int p_picLength = p_pic.length();

		if (Reslength < p_picLength) {
			result = ReportDAO.addSpaces("", p_picLength - Reslength) + result;
		}
		return result;
	}

	//// added by akshay
	public static void ESIClistexcel(String date, String type, String filepath, String imgpath) {
		Properties prop = new Properties();

		//
		RepoartBean repBean = new RepoartBean();

		Connection con = null;

		int Srno = 0;
		float nodays = 0.0f;

		String pBrcd1 = "";
		int tot_no_emp = 0;
		int br_tot_no_emp = 0;
		float totalAmount = 0.0f;
		//

		try {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		} catch (Exception e) {
			System.out.println("Error in constant properties Manager " + e);
		}

		try {

			System.out.println("Into Generalised_EmpListDAO.....");

			// LookupHandler lkh=new LookupHandler();

			System.out.println(filepath);
			FileOutputStream out1 = new FileOutputStream(new File(filepath));
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("EmpPFlist");

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			System.out.println("1111.....");

			sheet.setColumnWidth((short) 0, (short) 2000);
			sheet.setColumnWidth((short) 1, (short) 10000);
			sheet.setColumnWidth((short) 2, (short) 7000);
			sheet.setColumnWidth((short) 3, (short) 4000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4000);
			sheet.setColumnWidth((short) 6, (short) 5000);
			sheet.setColumnWidth((short) 7, (short) 5000);
			sheet.setColumnWidth((short) 8, (short) 4000);
			sheet.setColumnWidth((short) 9, (short) 5000);
			sheet.setColumnWidth((short) 10, (short) 5000);
			sheet.setColumnWidth((short) 11, (short) 4000);
			sheet.setColumnWidth((short) 12, (short) 4000);

			HSSFCellStyle my_style = hwb.createCellStyle();
			HSSFCellStyle my_style1 = hwb.createCellStyle();

			HSSFFont my_font = hwb.createFont();
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style.setFont(my_font);

			HSSFRow rowtitle = sheet.createRow((short) 0);
			HSSFCell cell = rowtitle.createCell((short) 9);

			cell.setCellValue(prop.getProperty("companyName"));
			cell.setCellStyle(my_style);
			HSSFRow rowtitle1 = sheet.createRow((short) 1);
			HSSFCell cell1 = rowtitle1.createCell((short) 7);
			cell1.setCellValue(prop.getProperty("addressForReport"));
			cell1.setCellStyle(my_style);
			HSSFRow rowtitle2 = sheet.createRow((short) 2);
			HSSFCell cell2 = rowtitle2.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("contactForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle31 = sheet.createRow((short) 3);
			cell2 = rowtitle31.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("mailForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle3 = sheet.createRow((short) 4);
			cell2 = rowtitle3.createCell((short) 2);
			cell2.setCellValue("");
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle4 = sheet.createRow((short) 5);
			rowtitle4.createCell((short) 0).setCellValue("");
			HSSFRow rowtitle5 = sheet.createRow((short) 6);
			rowtitle5.createCell((short) 0).setCellValue("");

			HSSFFont blueFont = hwb.createFont();
			blueFont.setColor(HSSFColor.BLUE.index);

			HSSFCellStyle style = hwb.createCellStyle();

			style.setFillForegroundColor(HSSFColor.BLUE.index);

			HSSFRow head = sheet.createRow((short) 7);
			head.createCell((short) 0).setCellValue("");
			// akshay

			ReportDAO.OpenCon("", "", "", repBean);
			con = repBean.getCn();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			System.out.println("3333.....");
			float Gross = 0.0f;
			float ESIC = 0.0f;
			int i = 16;
			int count = 1;
			String sql1 = "select e.salute,e.empno,CONVERT(INT, e.empcode) as empcode,e.fname,e.mname,e.lname,e.DOB,e.DOJ,e.ESICNO,e.AADHAARNUM,T.ACNO,y1.cal_amt as basic, "
					+ "y2.cal_amt as esic,T.PRJ_SRNO as PRJ_SRNO , "
					+ "case when exists (select f3 from OTHERDETAIL where EMPNO=e.empno) "
					+ "then (select f3 from OTHERDETAIL where EMPNO=e.empno) "
					+ "else (SELECT '' AS ifsc) END AS 'ifsc', "
					+ "case when exists (select telno from EMPAUX where EMPNO=e.empno) "
					+ "then (select aux.telno from EMPAUX aux where aux.EMPNO=e.empno and "
					+ "id=(select MAX(id) from EMPAUX  where EMPNO=e.empno)) "
					+ "else (SELECT 0 AS telno) END AS 'telno' "
					+ "from EMPMAST e join EMPTRAN T on  e.EMPNO = T.EMPNO join "
					+ "paytran y1 on e.EMPNO = y1.EMPNO join paytran y2 on e.EMPNO = y2.EMPNO and y1.TRNCD = 198 and y1.TRNDT "
					+ "between '" + ReportDAO.BOM(date) + "' and '" + ReportDAO.EOM(date)
					+ "' and y2.TRNCD = 221 and y2.TRNDT between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "'   and "
					+ "y2.cal_amt>0 and T.SRNO = (SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = e.EMPNO AND E2.EFFDATE <= '"
					+ ReportDAO.EOM(date) + "') " +

					"union "
					+ "select e.salute,e.empno,CONVERT(INT, e.empcode) as empcode,e.fname,e.mname,e.lname,e.DOB,e.DOJ,e.ESICNO,e.AADHAARNUM,T.ACNO,y1.cal_amt as basic, "
					+ "y2.cal_amt as esic,T.PRJ_SRNO as PRJ_SRNO , "
					+ "case when exists (select f3 from OTHERDETAIL where EMPNO=e.empno) "
					+ "then (select f3 from OTHERDETAIL where EMPNO=e.empno) "
					+ "else (SELECT '' AS ifsc) END AS 'ifsc', "
					+ "case when exists (select telno from EMPAUX where EMPNO=e.empno) "
					+ "then (select aux.telno from EMPAUX aux where aux.EMPNO=e.empno and "
					+ "id=(select MAX(id) from EMPAUX  where EMPNO=e.empno)) "
					+ "else (SELECT 0 AS telno) END AS 'telno' "
					+ "from EMPMAST e join EMPTRAN T on  e.EMPNO = T.EMPNO join "
					+ "paytran_stage y1 on e.EMPNO = y1.EMPNO join paytran_stage y2 on e.EMPNO = y2.EMPNO and y1.TRNCD = 198 and y1.TRNDT "
					+ "between '" + ReportDAO.BOM(date) + "' and '" + ReportDAO.EOM(date)
					+ "' and y2.TRNCD = 221 and y2.TRNDT between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "'   and "
					+ "y2.cal_amt>0 and T.SRNO = (SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = e.EMPNO AND E2.EFFDATE <= '"
					+ ReportDAO.EOM(date) + "') " +

					"union "
					+ "  select e.salute,e.empno,CONVERT(INT, e.empcode) as empcode,e.fname,e.mname,e.lname,e.DOB,e.DOJ,e.ESICNO,e.AADHAARNUM,T.ACNO,y1.cal_amt as basic, "
					+ "y2.cal_amt as esic,T.PRJ_SRNO as PRJ_SRNO , "
					+ "case when exists (select f3 from OTHERDETAIL where EMPNO=e.empno) "
					+ "then (select f3 from OTHERDETAIL where EMPNO=e.empno) "
					+ "else (SELECT '' AS ifsc) END AS 'ifsc', "
					+ "case when exists (select telno from EMPAUX where EMPNO=e.empno) "
					+ "then (select aux.telno from EMPAUX aux where aux.EMPNO=e.empno and "
					+ "id=(select MAX(id) from EMPAUX  where EMPNO=e.empno)) "
					+ "else (SELECT 0 AS telno) END AS 'telno' "
					+ "from EMPMAST e join EMPTRAN T on  e.EMPNO = T.EMPNO join "
					+ "YTDTRAN y1 on e.EMPNO = y1.EMPNO join YTDTRAN y2 on e.EMPNO = y2.EMPNO and y1.TRNCD = 198 and y1.TRNDT "
					+ "between '" + ReportDAO.BOM(date) + "' and '" + ReportDAO.EOM(date)
					+ "' and y2.TRNCD = 221 and y2.TRNDT between '" + ReportDAO.BOM(date) + "' and '"
					+ ReportDAO.EOM(date) + "'   and "
					+ "y2.cal_amt>0 and T.SRNO = (SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = e.EMPNO AND E2.EFFDATE <= '"
					+ ReportDAO.EOM(date) + "')  " + "order by PRJ_SRNO, empcode";

			ResultSet rs = st.executeQuery(sql1);
			System.out.println("query is" + sql1);
			int k = 0;
			// LookupHandler lkp = new LookupHandler();

			// ResultSet
			rs = st.executeQuery(sql1);
			// int i = 0;
			while (rs.next()) {

				String prj_name = null;
				String prj_code = null;
				Connection conn = ConnectionManager.getConnectionTech();
				Statement stmt = conn.createStatement();
				String prjquery = "select Site_Name,Project_Code from Project_Sites where SITE_ID = '"
						+ rs.getString("PRJ_SRNO") + "'";
				// System.out.println(prjquery);
				ResultSet prj = stmt.executeQuery(prjquery);
				if (prj.next()) {
					prj_name = prj.getString("Site_Name");
					prj_code = prj.getString("Project_Code");
				}
				pBrcd1 = rs.getString("PRJ_SRNO");
				br_tot_no_emp = 0;

				HSSFRow rowtitle10 = sheet.createRow((short) i++);
				HSSFCell cell10 = rowtitle10.createCell((short) 0);
				cell10.setCellValue("Employee's ESIC List For Project Site : " + prj_name + " (" + prj_code + ")");
				cell10.setCellStyle(my_style);
				prj.close();
				stmt.close();
				conn.close();
				HSSFRow heading = sheet.createRow((short) 8);
				HSSFCell cell3 = heading.createCell((short) 0);

				cell3.setCellValue("");
				cell3.setCellStyle(my_style1);
				HSSFRow rowhead = sheet.createRow((short) 9);
				sheet.createFreezePane(0, 10, 0, 10);

				my_style1.setAlignment((short) 2);
				my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				my_style1.setFont(my_font);

				cell3 = rowhead.createCell((short) 0);
				cell3.setCellValue("SR NO");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 1);
				cell3.setCellValue("EMPNAME");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 2);
				cell3.setCellValue("FATHER/HUSBAND'S NAME");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 3);
				cell3.setCellValue("DOB");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 4);
				cell3.setCellValue("DOJ");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 5);
				cell3.setCellValue("ESIC INSURANCENO");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 6);
				cell3.setCellValue("ADHAAR CARDNO");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 7);
				cell3.setCellValue("BANK A/C NO");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 8);
				cell3.setCellValue("IFSC NO");
				cell3.setCellStyle(my_style1);

				cell3 = rowhead.createCell((short) 9);
				cell3.setCellValue("MOBILE NO");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 10);
				cell3.setCellValue("PRESENT DAYS");
				cell3.setCellStyle(my_style1);

				cell3 = rowhead.createCell((short) 11);
				cell3.setCellValue("GORSS AMT");
				cell3.setCellStyle(my_style1);
				cell3 = rowhead.createCell((short) 12);
				cell3.setCellValue("ESIC DED.AMT");
				cell3.setCellStyle(my_style1);

				nodays = Calculate.getDays(date);
				System.out.println("No of ..." + nodays);
				while (pBrcd1.equalsIgnoreCase(rs.getString("PRJ_SRNO"))) {
					Connection con1 = null;
					con1 = ConnectionManager.getConnection();
					ResultSet rs7 = null;
					Statement statement1 = con1.createStatement();
					String q = "select " + Calculate.getDays(date) + " - DAY(DOJ)+1 from empmast where empno ="
							+ rs.getInt("EMPNO") + " and doj between cast('" + ReportDAO.BOM(date)
							+ "' as DATE) and cast('" + ReportDAO.EOM(date) + "' as DATE)";
					rs7 = statement1.executeQuery(q);

					if (rs7.next()) {
						nodays = rs7.getFloat(1);

					} else {
						nodays = Calculate.getDays(date);
					}

					// === For Date of Leaving in same Month
					rs7 = statement1.executeQuery("select " + nodays + " - (DAY(cast('" + ReportDAO.EOM(date)
							+ "' as DATE))- DAY(DOL)) from empmast where empno =" + rs.getInt("EMPNO")
							+ " and dol between cast('" + ReportDAO.BOM(date) + "' as DATE) and cast('"
							+ ReportDAO.EOM(date) + "' as DATE)");

					if (rs7.next()) {
						/*
						 * int day = rs7.getInt(1); paiddays = day;
						 */
						nodays = rs7.getFloat(1);

					} else {
						nodays = Calculate.getDays(date);
					}

					statement1.close();

					if (rs.getInt("esic") > 0) {
						i++;
					}

					Srno++;
					// SimpleDateFormat output = new
					// SimpleDateFormat("dd/MM/yyyy");
					HSSFRow row = sheet.createRow((short) i++);
					// System.out.println("9999.....");

					row.createCell((short) 0).setCellValue((count++));
					row.createCell((short) 1).setCellValue(
							rs.getString("FNAME") + " " + rs.getString("MNAME") + " " + rs.getString("LNAME"));
					row.createCell((short) 2).setCellValue(rs.getString("MNAME") + " " + rs.getString("LNAME"));

					// System.out.println("10101.....");
					row.createCell((short) 3).setCellValue((rs.getString("DOB")));
					row.createCell((short) 4).setCellValue(sdf.format(rs.getDate("DOJ")));
					row.createCell((short) 5).setCellValue((rs.getString("ESICNO")));

					// System.out.println("2020.....");

					row.createCell((short) 6).setCellValue(rs.getString("AADHAARNUM"));
					row.createCell((short) 7).setCellValue(rs.getString("ACNO"));
					String ifsc = "" + rs.getString("ifsc");
					if (ifsc.equalsIgnoreCase("null")) {

						row.createCell((short) 8).setCellValue("");

					} else {
						row.createCell((short) 8).setCellValue(rs.getString("ifsc"));
					}
					String mob = "" + rs.getString("TELNO");
					if (mob.equalsIgnoreCase("null")) {
						row.createCell((short) 9).setCellValue("");

					} else {
						row.createCell((short) 9).setCellValue("" + rs.getString("TELNO"));
					}

					float absentDays = 0;
					int empno = rs.getInt("EMPNO");

					absentDays = UtilityDAO.getAbsentDays(date, empno);

					float result = nodays - absentDays;
					row.createCell((short) 10).setCellValue(Math.abs(result));
					Gross = Gross + rs.getInt("basic");
					row.createCell((short) 11).setCellValue(rs.getInt("basic"));

					ESIC = ESIC + rs.getInt("esic");

					row.createCell((short) 12).setCellValue(rs.getInt("esic"));
					totalAmount = totalAmount + rs.getInt("esic");
					tot_no_emp = tot_no_emp + 1;
					br_tot_no_emp = br_tot_no_emp + 1;
					con1.close();
					if (!rs.next()) {
						break;
					}
					if (!pBrcd1.equals(rs.getString("PRJ_SRNO"))) {
						rs.previous();
						break;
					}

				}
				// akshay
				HSSFRow rowhead1 = sheet.createRow((short) i++);

				rowhead1.createCell((short) 10).setCellValue("TOTAL :--");

				rowhead1.createCell((short) 11).setCellValue(Gross);
				rowhead1.createCell((short) 12).setCellValue(ESIC);
				ESIC = 0.0f;
				Gross = 0.0f;

				HSSFRow rowhead2 = sheet.createRow((short) i++);
				rowhead2.createCell((short) 10).setCellValue("No Of Employees :- " + br_tot_no_emp);

				DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

			}

			// Calendar calobj = Calendar.getInstance();

			HSSFRow row = sheet.createRow((short) i++);
			row.createCell((short) 0).setCellValue(" ");
			row = sheet.createRow((short) i + 1);

			row.createCell((short) 3).setCellValue("Total No Of Employees :-" + tot_no_emp);
			row.createCell((short) 5).setCellValue("Total ESIC Amount:- " + totalAmount);

			hwb.write(out1);
			out1.close();
			st.close();
			con.close();

			System.out.println("Result OK.....");

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void empCTCCHange(String date1, String type, String filepath, String imagepath, String empType) {

		Properties prop = new Properties();
		try {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = classLoader.getResourceAsStream("constant.properties");
			prop.load(stream);
		} catch (Exception e) {
			System.out.println("Error in constant properties Manager " + e);
		}

		try {
			System.out.println("CTC Change ....." + date1 + "  " + ReportDAO.EOM(date1));
			RepoartBean repBean = new RepoartBean();

			LookupHandler lkh = new LookupHandler();
			Connection con = null;
			System.out.println(filepath);
			FileOutputStream out1 = new FileOutputStream(new File(filepath));
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("CTC Change");

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			sheet.setColumnWidth((short) 0, (short) 3000);
			sheet.setColumnWidth((short) 1, (short) 3000);
			sheet.setColumnWidth((short) 2, (short) 6000);
			sheet.setColumnWidth((short) 3, (short) 3000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4000);
			sheet.setColumnWidth((short) 6, (short) 4000);
			sheet.setColumnWidth((short) 7, (short) 3000);
			sheet.setColumnWidth((short) 8, (short) 4000);
			sheet.setColumnWidth((short) 9, (short) 4000);
			sheet.setColumnWidth((short) 10, (short) 5000);
			sheet.setColumnWidth((short) 11, (short) 20000);
			sheet.setColumnWidth((short) 12, (short) 4000);
			sheet.setColumnWidth((short) 13, (short) 7000);
			sheet.setColumnWidth((short) 14, (short) 7000);
			sheet.setColumnWidth((short) 15, (short) 7000);
			sheet.setColumnWidth((short) 16, (short) 5000);
			sheet.setColumnWidth((short) 17, (short) 4000);
			sheet.setColumnWidth((short) 18, (short) 5000);
			sheet.setColumnWidth((short) 19, (short) 5000);
			sheet.setColumnWidth((short) 20, (short) 2000);
			sheet.setColumnWidth((short) 21, (short) 2000);

			HSSFCellStyle my_style = hwb.createCellStyle();
			HSSFCellStyle my_style1 = hwb.createCellStyle();

			HSSFFont my_font = hwb.createFont();
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style.setFont(my_font);

			HSSFRow rowtitle = sheet.createRow((short) 0);
			HSSFCell cell = rowtitle.createCell((short) 9);

			cell.setCellValue(prop.getProperty("companyName"));
			cell.setCellStyle(my_style);
			HSSFRow rowtitle1 = sheet.createRow((short) 1);
			HSSFCell cell1 = rowtitle1.createCell((short) 7);
			cell1.setCellValue(prop.getProperty("addressForReport"));
			cell1.setCellStyle(my_style);
			HSSFRow rowtitle2 = sheet.createRow((short) 2);
			HSSFCell cell2 = rowtitle2.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("contactForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle31 = sheet.createRow((short) 3);
			cell2 = rowtitle31.createCell((short) 9);
			cell2.setCellValue(prop.getProperty("mailForReport"));
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle3 = sheet.createRow((short) 4);
			cell2 = rowtitle3.createCell((short) 2);
			cell2.setCellValue("");
			cell2.setCellStyle(my_style);
			HSSFRow rowtitle4 = sheet.createRow((short) 5);
			rowtitle4.createCell((short) 0).setCellValue("");
			HSSFRow rowtitle5 = sheet.createRow((short) 6);
			rowtitle5.createCell((short) 0).setCellValue("");

			HSSFFont blueFont = hwb.createFont();
			blueFont.setColor(HSSFColor.BLUE.index);

			HSSFCellStyle style = hwb.createCellStyle();
			// style.setFont(blueFont);
			style.setFillForegroundColor(HSSFColor.BLUE.index);

			HSSFRow head = sheet.createRow((short) 7);
			head.createCell((short) 0).setCellValue("");
			HSSFRow heading = sheet.createRow((short) 8);
			HSSFCell cell3 = heading.createCell((short) 0);

			cell3.setCellValue("");
			cell3.setCellStyle(my_style1);
			HSSFRow rowhead = sheet.createRow((short) 9);
			sheet.createFreezePane(0, 10, 0, 10);

			my_style1.setAlignment((short) 2);
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style1.setFont(my_font);

			cell3 = rowhead.createCell((short) 0);
			cell3.setCellValue("SR.NO");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 1);
			cell3.setCellValue("Emp Code");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 2);
			cell3.setCellValue("Employee Name");
			cell3.setCellStyle(my_style1);
			
			cell3 = rowhead.createCell((short) 3);
			cell3.setCellValue("DOJ");
			cell3.setCellStyle(my_style1);
			
			cell3 = rowhead.createCell((short) 4);
			cell3.setCellValue("OLD DATE");
			cell3.setCellStyle(my_style1);
			
			cell3 = rowhead.createCell((short) 5);
			cell3.setCellValue("OLD BASIC");
			cell3.setCellStyle(my_style1);
			cell3 = rowhead.createCell((short) 6);
			cell3.setCellValue("NEW DATE");
			cell3.setCellStyle(my_style1);

			cell3 = rowhead.createCell((short) 7);
			cell3.setCellValue("NEW BASIC");
			cell3.setCellStyle(my_style1);
			
			
			ReportDAO.OpenCon("", "", "", repBean);
			con = repBean.getCn();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int i = 12;
			int count = 1;
	
			System.out.println("hELLO DATE1" + date1);
			System.out.println("hELLO DATE2" + ReportDAO.EOM(date1));
			System.out.println("hELLO DATE3" + ReportDAO.BOM(date1));
			
			String sqlCTC1 = "select distinct E.empno,e.EMPCODE,e.DOJ,RTRIM(e.FNAME)+' '+RTRIM(e.MNAME)+' '+RTRIM(e.LNAME) as name from EMPMAST E,PAYTRAN_stage P WHERE P.EMPNO=E.EMPNO "
					+ "  AND (E.STATUS='A' AND E.DOJ<='"+ReportDAO.EOM(date1)+"') OR (E.STATUS='N' AND E.DOL>='"+ReportDAO.BOM(date1)+"') and p.TRNCD = 101 order by E.EMPNO";

			String sqlCTC2 = "";
			ResultSet rsemp = st.executeQuery(sqlCTC1);
			Statement st1 = con.createStatement();
			/*ResultSet rs2 = null;*/
			int totalEmp = 0;
			String empno="";
			String doj="";
			String name="";
			String empcode="";
			String basic = "";
			String date = "";
			
			String basic1 = "";
			String date11 = "";
			
			while (rsemp.next()) {
				
				  empno= rsemp.getString("empno");
				  doj= rsemp.getString("DOJ");
				  name= rsemp.getString("name");
				  empcode= rsemp.getString("empcode");
			
				sqlCTC2 = "select TOP 2 INP_AMT as oldbasic, MIN(TRNDT) as olddate from PAYTRAN_STAGE where empno = "+rsemp.getString("empno")+" and TRNCD = 101 and INP_AMT<>0 GROUP BY INP_AMT ORDER BY INP_AMT desc";
			System.out.println("sqlCTC2:"+sqlCTC2);
				ResultSet rs2  = st1.executeQuery(sqlCTC2);
				
					if(rs2.next())
					{
							basic = rs2.getString("oldbasic");
							date = rs2.getString("olddate");
							
							if(rs2.next())
							{
								basic1 = rs2.getString("oldbasic");
								date11 = rs2.getString("olddate");
							}
							else
							{
								basic1 = basic;
								date11 = date;
							}
							
								
							HSSFRow row = sheet.createRow((short) i++);
							row.createCell((short) 0).setCellValue("" + (count++));
							row.createCell((short) 1).setCellValue("" + empcode);
							row.createCell((short) 2).setCellValue("" +name);
							row.createCell((short) 3).setCellValue("" + doj);
							
							row.createCell((short) 4).setCellValue("" + date11 );
							row.createCell((short) 5).setCellValue("" + basic1);
							
							row.createCell((short) 6).setCellValue("" +date);
							row.createCell((short) 7).setCellValue("" + basic);
							/*rs2.next();*/
							/*if(rs2.next())
							{
								row.createCell((short) 6).setCellValue("" + rs2.getString("olddate"));
								row.createCell((short) 7).setCellValue("" + rs2.getString("oldbasic"));
							}
							else
							{
								rs2.previous();
								//rs2.next();
								row.createCell((short) 6).setCellValue("" +date);
								row.createCell((short) 7).setCellValue("" + basic);
								
								row.createCell((short) 6).setCellValue("" +rs2.getString("olddate"));
								row.createCell((short) 7).setCellValue("" +  rs2.getString("oldbasic"));
							}*/

					}
					totalEmp++;
				
			}
			System.out.println("totl:"+totalEmp);
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

			Calendar calobj = Calendar.getInstance();


			HSSFRow row1 = sheet.createRow((short) i);
			row1.createCell((short) 0).setCellValue(" ");
			row1 = sheet.createRow((short) i + 1);
			row1.createCell((short) 0).setCellValue("Total Employee : " +totalEmp);

			
			
			HSSFRow row = sheet.createRow((short) i);
			row.createCell((short) 0).setCellValue(" ");
			row = sheet.createRow((short) i + 3);
			row.createCell((short) 0).setCellValue("Report Date And Time" + df.format(calobj.getTime()));

			hwb.write(out1);
			out1.close();
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}

}
