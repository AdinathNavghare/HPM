package payroll.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import payroll.Model.EmployeeBean;
import payroll.Model.Lookup;

public class EmployeeHandler {
	Connection conn;
	LookupHandler lkhp = new LookupHandler();
	Lookup lookupBean = new Lookup();

	public EmployeeBean getEmployeeInformation(String EMPNO) {
		conn = ConnectionManager.getConnection();
		ResultSet rs;
		SimpleDateFormat fromformat = new SimpleDateFormat("yyyy-MM-dd");
		fromformat.setLenient(false);
		EmployeeBean empbean = new EmployeeBean();
		try {
			Statement stmt = conn.createStatement();
			rs = stmt
					.executeQuery("select e.*,e1.PRJ_CODE from EMPMAST e,EMPTRAN e1 where e.EMPNO='"
							+ EMPNO
							+ "' "
							+ "AND e1.srno=(SELECT MAX(SRNO) FROM EMPTRAN WHERE EMPNO='"
							+ EMPNO + "' ) and e.EMPNO=e1.empno ");
			System.out
					.println("select e.*,e1.PRJ_CODE from EMPMAST e,EMPTRAN e1 where e.EMPNO='"
							+ EMPNO
							+ "' "
							+ "AND e1.srno=(SELECT MAX(SRNO) FROM EMPTRAN WHERE EMPNO='"
							+ EMPNO + "' ) and e.EMPNO=e1.empno ");
			while (rs.next()) {
				empbean.setEMPNO(rs.getString("EMPNO") == null ? 0 : rs
						.getInt("EMPNO"));
				empbean.setFNAME(rs.getString("FNAME") == null ? "" : rs
						.getString("FNAME"));
				empbean.setLNAME(rs.getString("LNAME") == null ? "" : rs
						.getString("LNAME"));
				empbean.setPFNO((rs.getString("PFNO") == null ? "--" : rs
						.getString("PFNO")));
				empbean.setDOJ(rs.getDate("DOJ") == null ? "" : dateFormat(rs
						.getDate("DOJ")));
				empbean.setBGRP((rs.getString("BGRP") == null ? "--" : rs
						.getString("BGRP")));
				empbean.setBPLACE((rs.getString("BPLACE") == null ? "--" : (rs
						.getString("BPLACE"))));
				empbean.setCASTCD(rs.getString("CASTCD") == null ? 0 : rs
						.getInt("CASTCD"));
				empbean.setCATEGORYCD(rs.getString("CATEGORYCD") == null ? 0
						: rs.getInt("CATEGORYCD"));
				empbean.setDA_SCHEME(rs.getString("DA_SCHEME") == null ? ""
						: rs.getString("DA_SCHEME"));
				empbean.setDEPAMT((int) Double.parseDouble((rs
						.getString("DEPAMT") == null ? "0" : rs
						.getString("DEPAMT"))));
				empbean.setDISABILPER(rs.getString("DISABILPER") == null ? "--"
						: rs.getString("DISABILPER"));
				empbean.setDISABILYN(rs.getString("DISABILYN") == null ? "N"
						: rs.getString("DISABILYN"));
				empbean.setDOB(rs.getDate("DOB") == null ? "" : dateFormat(rs
						.getDate("DOB")));
				empbean.setDOL(rs.getDate("DOL") == null ? "" : dateFormat(rs
						.getDate("DOL")));
				empbean.setDRVLISCNNO((rs.getString("DRVLISCNNO") == null ? "--"
						: rs.getString("DRVLISCNNO")));
				empbean.setFHNAME(rs.getString("FHNAME") == null ? "" : rs
						.getString("FHNAME"));
				empbean.setHEIGHT(rs.getString("HEIGHT") == null ? 0 : Double
						.parseDouble(rs.getString("HEIGHT")));
				empbean.setHOBBYCD(rs.getString("HOBBYCD") == null ? "" : rs
						.getString("HOBBYCD"));
				empbean.setIDENTITY((rs.getString("IDENTY") == null ? "--"
						: (rs.getString("IDENTY"))));
				empbean.setMARRIED(rs.getString("MARRIED") == null ? "U" : rs
						.getString("MARRIED"));
				empbean.setMARRIEDDT(rs.getDate("MARRIEDDT") == null ? ""
						: dateFormat(rs.getDate("MARRIEDDT")));
				empbean.setMLWFNO((rs.getString("MLWFNO") == null ? "--" : (rs
						.getString("MLWFNO"))));
				empbean.setMNAME(rs.getString("MNAME") == null ? "-" : rs
						.getString("MNAME"));
				empbean.setPFNO(rs.getString("PFNO") == null ? "-" : rs
						.getString("PFNO"));
				empbean.setUanNo(rs.getString("UANNO") == null ? "-" : rs
						.getString("UANNO"));
				empbean.setEsicNo(rs.getString("ESICNO") == null ? "-" : rs
						.getString("ESICNO"));

				empbean.setOTHERDTL((rs.getString("OTHERDTL") == null ? "----"
						: (rs.getString("OTHERDTL"))));
				empbean.setPANNO((rs.getString("PANNO")) == null ? "--" : (rs
						.getString("PANNO")));
				empbean.setPASSPORTNO((rs.getString("PASSPORTNO")) == null ? "--"
						: rs.getString("PASSPORTNO"));
				empbean.setPFNOMINEE(rs.getString("PFNOMINEE") == null ? "--"
						: rs.getString("PFNOMINEE"));
				empbean.setPFNOMIREL(rs.getString("PFNOMIREL") == null ? "--"
						: rs.getString("PFNOMIREL"));
				empbean.setPFOPENDT(rs.getDate("PFOPENDT") == null ? ""
						: dateFormat(rs.getDate("PFOPENDT")));
				empbean.setUanNo((rs.getString("UANNO")) == null ? "--" : (rs
						.getString("UANNO")));
				empbean.setEsicNo((rs.getString("ESICNO")) == null ? "--" : (rs
						.getString("ESICNO")));
				empbean.setPFNO(rs.getString("PFNO") == null ? "--" : rs
						.getString("PFNO"));
				empbean.setRESIDSTAT(rs.getString("RESIDSTAT") == null ? 0 : rs
						.getInt("RESIDSTAT"));
				empbean.setRELEGENCD(rs.getString("RELEGENCD") == null ? 0 : rs
						.getInt("RELEGENCD"));
				empbean.setRESNLFTCD(Integer
						.parseInt(rs.getString("RESNLFTCD") == null ? "0" : rs
								.getString("RESNLFTCD")));
				empbean.setSALUTE(rs.getString("SALUTE") == null ? 0 : rs
						.getInt("SALUTE"));
				empbean.setSBOND(rs.getString("SBOND") == null ? "N" : rs
						.getString("SBOND"));
				empbean.setGENDER(rs.getString("GENDER") == null ? "" : rs
						.getString("GENDER"));
				empbean.setSTATUS(rs.getString("STATUS") == null ? "" : rs
						.getString("STATUS"));
				empbean.setVEHICLEDES((rs.getString("VEHICLEDES") == null ? "--"
						: rs.getString("VEHICLEDES")));
				empbean.setWEIGHT(rs.getString("WEIGHT") == null ? 0.0 : Double
						.parseDouble(rs.getString("WEIGHT")));
				empbean.setAADHAARNUM(rs.getString("AADHAARNUM") == null ? "--"
						: rs.getString("AADHAARNUM"));
				empbean.setEMPTYPE(rs.getString("EMPLOYEE_TYPE") == null ? 0
						: rs.getInt("EMPLOYEE_TYPE"));
				empbean.setEMPCODE(rs.getString("EMPCODE"));
				System.out.println("setbeanprjcode......."
						+ rs.getString("PRJ_CODE"));
				empbean.setPrj_Code(rs.getString("PRJ_CODE") == null ? "0" : rs
						.getString("PRJ_CODE"));
				System.out.println("setbeanprjcode222...."
						+ empbean.getPrj_Code());

			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empbean;
	}

	public static String getEmpcode(int empno) {
		String empcode = "";
		try {
			Connection conn = null;
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("select empcode from EMPMAST where EMPNO="
							+ empno);

			if (rs.next()) {
				empcode = rs.getString(1);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empcode;

	}

	public String getpname(String pcode) {
		String prjname = null;
		try {
			Connection conn = null;
			conn = ConnectionManager.getConnectionTech();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("select Site_Name from Project_Sites where Project_Code='"
							+ pcode + "'");
			// System.out.println("pppppprjcode...."+pcode);
			if (rs.next()) {
				prjname = rs.getString(1);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prjname;

	}

	public void updateEmployeeInfo(EmployeeBean empbean) {
		Statement st = null;
		try {
			conn = ConnectionManager.getConnection();
			st = conn.createStatement();
			st.executeUpdate("update EMPMAST set GENDER='"
					+ empbean.getGENDER() + "',DOB='" + empbean.getDOB()
					+ "',BPLACE='" + empbean.getBPLACE() + "',DOJ='"
					+ empbean.getDOJ() + "',RESNLFTCD="
					+ empbean.getRESNLFTCD() + ",PANNO='" + empbean.getPANNO()
					+ "',PFNO='" + empbean.getPFNO() + "',PFOPENDT=CAST("
					+ empbean.getPFOPENDTQUERY() + " AS DATE),PFNOMINEE='"
					+ empbean.getPFNOMINEE() + "',PFNOMIREL='"
					+ empbean.getPFNOMIREL() + "',MARRIED='"
					+ empbean.getMARRIED() + "',MARRIEDDT='"
					+ empbean.getMARRIEDDT() + "',CASTCD="
					+ empbean.getCASTCD() + ",CATEGORYCD="
					+ empbean.getCATEGORYCD() + ",RELEGENCD='"
					+ empbean.getRELEGENCD() + "',BGRP='" + empbean.getBGRP()
					+ "',IDENTY='" + empbean.getIDENTITY() + "',HEIGHT="
					+ empbean.getHEIGHT() + ",WEIGHT=" + empbean.getWEIGHT()
					+ ",OTHERDTL='" + empbean.getOTHERDTL() + "',MLWFNO='"
					+ empbean.getMLWFNO() + "',DISABILYN='"
					+ empbean.getDISABILYN() + "',RESIDSTAT="
					+ empbean.getRESIDSTAT() + ",VEHICLEDES='"
					+ empbean.getVEHICLEDES() + "',DRVLISCNNO='"
					+ empbean.getDRVLISCNNO() + "',PASSPORTNO='"
					+ empbean.getPASSPORTNO() + "',SALUTE="
					+ empbean.getSALUTE() + ", STATUS='" + empbean.getSTATUS()
					+ "',SBOND='" + empbean.getSBOND() + "',DEPAMT="
					+ empbean.getDEPAMT() + ",AADHAARNUM='"
					+ empbean.getAADHAARNUM() + "', EMPLOYEE_TYPE="
					+ empbean.getEMPTYPE()
					+ ",UMODDATE = (select GETDATE()), UANNO='"
					+ empbean.getUanNo() + "', ESICNO='" + empbean.getEsicNo()
					+ "' where EMPNO =" + empbean.getEMPNO() + "");
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getAutoIncrement() {
		Connection con = null;
		Statement st = null;
		int EMPNO = 1;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			ResultSet rs = st.executeQuery("select max(EMPNO)+1 from EMPMAST");
			if (rs.next()) {
				EMPNO = rs.getInt(1);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPNO;
	}

	public static int getcode() {
		Connection con = null;
		Statement st = null;
		int EMPCODE = 1;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select MAX(CAST(EMPCODE AS INT)+1 ) from EMPMAST");
			if (rs.next()) {
				EMPCODE = rs.getInt(1);
				System.out.println("in method" + EMPCODE);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPCODE;
	}

	public static int getcodeserv() {
		Connection con = null;
		Statement st = null;
		int EMPCODE = 1;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select MAX(CAST(EMPCODE AS INT) ) from EMPMAST");
			if (rs.next()) {
				EMPCODE = rs.getInt(1);
				System.out.println("in method" + EMPCODE);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPCODE;
	}

	public String insertEmployeeInfo(EmployeeBean empbean) {
		Connection con = null;
		// PreparedStatement prst=null;
		LookupHandler lh = new LookupHandler();

		Lookup lkhp = new Lookup();
		lkhp = lh.getLookup("SALUTE-" + empbean.getSALUTE());
		String salute = lkhp.getLKP_DESC();
		int empno = 0;
		int empcode = 0;
		try {
			empno = getAutoIncrement();
			empcode = getcode();
			System.out.println("in query " + empcode);
			System.out.println("ESIC in Handler::  " + empbean.getEsicNo());
			System.out.println("UAN Num in Handler::  " + empbean.getUanNo());

			con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			Statement stmt = con.createStatement();
			String desc = salute + "" + empbean.getFNAME() + " "
					+ empbean.getMNAME() + " " + empbean.getLNAME();
			String insertLookup = "INSERT INTO LOOKUP VALUES('ET'," + empno
					+ ",'" + desc + "',0)";
			String str = "insert into EMPMAST(EMPNO,SALUTE,FNAME,MNAME,LNAME,GENDER,DOB,BPLACE,DOJ,RESNLFTCD,"
					+ " PANNO,PFNOMINEE,PFNOMIREL,MARRIED,MARRIEDDT,CASTCD,CATEGORYCD,RELEGENCD,BGRP,IDENTY,"
					+ " HEIGHT,WEIGHT,OTHERDTL,MLWFNO,DISABILYN,RESIDSTAT,VEHICLEDES,DRVLISCNNO,PASSPORTNO,STATUS,"
					+ " SBOND,DEPAMT,AADHAARNUM,EMPLOYEE_TYPE,PFNO,EMPCODE,UCREATEDATE,UMODDATE,PFOPENDT,UANNO,ESICNO) values "
					+
					/* " ( (select max(EMPNO)+1 from EMPMAST) " + */
					"("
					+ empno
					+ " , "
					+ empbean.getSALUTE()
					+ " "
					+ " , '"
					+ empbean.getFNAME()
					+ "' "
					+ " , '"
					+ empbean.getMNAME()
					+ "' "
					+ " , '"
					+ empbean.getLNAME()
					+ "' "
					+ " , '"
					+ empbean.getGENDER()
					+ "' "
					+ " , '"
					+ empbean.getDOB()
					+ "' "
					+ " , '"
					+ empbean.getBPLACE()
					+ "' "
					+ " , '"
					+ empbean.getDOJ()
					+ "' "
					+ " , "
					+ empbean.getRESNLFTCD()
					+ " "
					+ " , '"
					+ empbean.getPANNO()
					+ "' "
					+ " , '"
					+ empbean.getPFNOMINEE()
					+ "' "
					+ " , '"
					+ empbean.getPFNOMIREL()
					+ "' "
					+ " , '"
					+ empbean.getMARRIED()
					+ "' "
					+ " , '"
					+ empbean.getMARRIEDDT()
					+ "' "
					+ " , "
					+ empbean.getCASTCD()
					+ " "
					+ " , "
					+ empbean.getCATEGORYCD()
					+ " "
					+ " , "
					+ empbean.getRELEGENCD()
					+ " "
					+ " , '"
					+ empbean.getBGRP()
					+ "' "
					+ " , '"
					+ empbean.getIDENTITY()
					+ "' "
					+ " , "
					+ empbean.getHEIGHT()
					+ " "
					+ " , "
					+ empbean.getWEIGHT()
					+ " "
					+ " , '"
					+ empbean.getOTHERDTL()
					+ "' "
					+ " , '"
					+ empbean.getMLWFNO()
					+ "' "
					+ " , '"
					+ empbean.getDISABILYN()
					+ "' "
					+ " , "
					+ empbean.getRESIDSTAT()
					+ " "
					+ " , '"
					+ empbean.getVEHICLEDES()
					+ "' "
					+ " , '"
					+ empbean.getDRVLISCNNO()
					+ "' "
					+ " , '"
					+ empbean.getPASSPORTNO()
					+ "' "
					+ " , 'A'"
					+ " , '"
					+ empbean.getSBOND()
					+ "' "
					+ " , "
					+ empbean.getDEPAMT()
					+ " "
					+ " , '"
					+ empbean.getAADHAARNUM()
					+ "',"
					+ empno
					+ " , '"
					+ empbean.getPFNO()
					+ "'"
					+ " , "
					+ empcode
					+ " , (select GETDATE()), (select GETDATE()), CAST("
					+ empbean.getPFOPENDTQUERY()
					+ " AS DATE)"
					+ ",  '"
					+ empbean.getUanNo()
					+ "' , '"
					+ empbean.getEsicNo()
					+ "'  )";
			System.out.println(str);
			stmt.execute(str);
			st.executeUpdate(insertLookup);
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Integer.toString(empno);
	}

	public static String dateFormat(Date date) {
		String result = "";
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		result = format.format(date);
		return result;
	}

	// BY param
	public ArrayList<EmployeeBean> getBirthEmplist() {
		Connection con = ConnectionManager.getConnection();
		java.util.Date d = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		String month = sdf.format(d);
		ArrayList<EmployeeBean> alist = new ArrayList<EmployeeBean>();
		try {
			Statement st = con.createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("select EMPNO,DOB,CONVERT(NVARCHAR(100),LNAME+' '+FNAME+' '+MNAME) as name from empmast where DOB like '%-"
					+ month
					+ "-%' and STATUS='A' order by  CONVERT(NVARCHAR, CAST(dob AS DATETIME), 105)");
			while (rs.next()) {
				EmployeeBean empb = new EmployeeBean();
				empb.setEMPNO(rs.getInt("EMPNO"));
				empb.setDOB(rs.getString("DOB"));
				empb.setFNAME(rs.getString("name"));
				alist.add(empb);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alist;
	}

	/*
	 * public String getday(String dt) throws ParseException { SimpleDateFormat
	 * dtn = new SimpleDateFormat("yyyy-MM-dd"); Date dtp=dtn.parse(dt);
	 * SimpleDateFormat sdf = new SimpleDateFormat("dd"); String MyDate1 =
	 * sdf.format(dtp); return MyDate1;
	 * 
	 * }
	 */

	public ArrayList<EmployeeBean> getday() {
		Connection con = ConnectionManager.getConnection();
		ArrayList<EmployeeBean> alist = new ArrayList<EmployeeBean>();
		java.util.Date d = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dt = sdf.format(d);
		try {
			System.out.println(dt);
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select EMPNO,DOB,CONVERT(NVARCHAR(100),LNAME+' '+FNAME+' '+MNAME) as name"
							+ " from empmast where DATEPART(day,dob)= DATEPART(day, '"
							+ dt
							+ "' ) "
							+ "and DATEPART(mm,dob) =DATEPART(mm,'"
							+ dt
							+ "') and STATUS='A'");
			while (rs.next()) {
				EmployeeBean empb = new EmployeeBean();
				empb.setDOB(rs.getString("DOB"));
				empb.setFNAME(rs.getString("name"));
				alist.add(empb);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return alist;

	}

	public ArrayList<EmployeeBean> getTombd() {
		java.util.Date d = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dt = sdf.format(d);
		SimpleDateFormat date = new SimpleDateFormat("dd");
		String prdate = date.format(d);
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
		String month = sdf1.format(d);
		Connection con = ConnectionManager.getConnection();
		ArrayList<EmployeeBean> alist = new ArrayList<EmployeeBean>();
		try {
			Statement st = con.createStatement();
			ResultSet rs = null;
			Calendar calendar = Calendar.getInstance();
			int lDate = calendar.getActualMaximum(Calendar.DATE);
			String lastDate = Integer.toString(lDate);
			if (!lastDate.equals(prdate)) {
				/*
				 * rs=st.executeQuery(
				 * "select EMPNO,DOB,CONVERT(NVARCHAR(100),LNAME+' '+FNAME+' '+MNAME) as name"
				 * +
				 * " from empmast where DATEPART(day,dob)= DATEPART(day,  DATEADD(DAY, 1, GETDATE()) ) "
				 * + "and DATEPART(mm,dob) =DATEPART(mm,GETDATE())");
				 */
				System.out.println("today date is " + prdate + " month is "
						+ month);
				rs = st.executeQuery("select EMPNO,DOB,CONVERT(NVARCHAR(100),LNAME+' '+FNAME+' '+MNAME) as name"
						+ " from empmast where DATEPART(day,dob)= DATEPART(day,  DATEADD(DAY, 1, '"
						+ dt
						+ "') ) "
						+ "and DATEPART(mm,dob) =DATEPART(mm,'"
						+ dt + "') and STATUS='A'");
			} else {
				System.out.println("last date of " + month + " is " + lastDate);
				rs = st.executeQuery("select EMPNO,DOB,CONVERT(NVARCHAR(100),LNAME+' '+FNAME+' '+MNAME) as name"
						+ " from empmast where DATEPART(day,dob)= DATEPART(day,  DATEADD(DAY, 1, '"
						+ dt
						+ "') ) "
						+ "and DATEPART(mm,dob) =DATEPART(mm,DATEADD(MONTH, 1, '"
						+ dt + "') ) and STATUS='A'");

			}
			while (rs.next()) {
				EmployeeBean empb = new EmployeeBean();
				empb.setDOB(rs.getString("DOB"));
				empb.setFNAME(rs.getString("name"));
				alist.add(empb);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return alist;
	}

	public static String getSalaryMonth(int empno) {

		String salaryDate = "";
		try {
			Connection conn = null;
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();
			String query = "select replace((convert(varchar,  max(trndt), 106)), ' ', '-') from PAYTRAN_STAGE where EMPNO="
					+ empno + "";
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);

			if (rs.next()) {
				salaryDate = rs.getString(1);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salaryDate;

	}

	public EmployeeBean getMaxEmployeeInformation() {
		conn = ConnectionManager.getConnection();
		ResultSet rs;
		SimpleDateFormat fromformat = new SimpleDateFormat("yyyy-MM-dd");
		fromformat.setLenient(false);
		EmployeeBean empbean = new EmployeeBean();

		try {
			Statement stmt = conn.createStatement();
			rs = stmt
					.executeQuery("select * from EMPMAST where EMPNO = (select MAX(EMPNO) from EMPMAST)");
			System.out.println(rs);
			String empmaxname = "";
			if (rs.next()) {

				empbean.setEMPNO(rs.getInt("EMPNO"));
				empbean.setFNAME(rs.getString("FNAME"));
				empbean.setLNAME(rs.getString("LNAME"));
				empbean.setMNAME(rs.getString("MNAME"));
				empbean.setDOJ(rs.getString("DOJ"));
				empbean.setEMPCODE(rs.getString("EMPCODE"));

			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empbean;
	}

	public String getDOJ(String empno) {
		String DOJ = "";
		try {
			Connection conn = null;
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();
			String query = "select DOJ from empmast where EMPNO=" + empno + " ";
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);

			if (rs.next()) {
				DOJ = rs.getString("DOJ");
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DOJ;

	}

	public String getsitename(String pcode) {
		String prjname = null;
		try {
			Connection conn = null;
			conn = ConnectionManager.getConnectionTech();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("select isnull(Site_Feild3,Project_Code) as site from Project_Sites where Project_Code='"
							+ pcode + "'");

			if (rs.next()) {
				prjname = rs.getString(1);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prjname;

	}

}