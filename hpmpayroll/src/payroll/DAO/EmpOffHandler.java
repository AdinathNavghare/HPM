package payroll.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import payroll.Core.Calculate;
import payroll.Core.ReportDAO;
import payroll.Model.Attend_bean;
import payroll.Model.EmpOffBean;
import payroll.Model.Lookup;
import payroll.Model.TranBean;
import payroll.Model.TransactionBean;

public class EmpOffHandler {
	Connection con = null;
	Connection conTech = null;
	Statement st = null;
	PreparedStatement prst = null;
	LookupHandler lkhp = new LookupHandler();
	Lookup lookupBean = new Lookup();

	public boolean insertOfficInfo(EmpOffBean empoffbean, String uid) {
		ResultSet rs = null;
		ResultSet rstransfer = null;
		Statement stransfer = null;
		ResultSet rsSite_check = null;
		Statement stSite_check = null;
		ResultSet Rsemptran = null;
		Statement stEmptran = null;
		String prj = "";
		String Site_status = "";
		boolean flag = false;
		String Empstatus = "";
		int srno = 0;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			stransfer = con.createStatement();
			stEmptran = con.createStatement();
			;
			stSite_check = con.createStatement();
			rstransfer = stransfer
					.executeQuery("select distinct appr_status from ATTENDANCE_STATUS where EMPNO="
							+ empoffbean.getEMPNO()
							+ " and tranevent='transfer' and att_month between '"
							+ ReportDAO.BOM(empoffbean.getEFFDATE())
							+ "' and '"
							+ ReportDAO.EOM(empoffbean.getEFFDATE()) + "' ");
			System.out
					.println("select distinct appr_status from ATTENDANCE_STATUS where EMPNO="
							+ empoffbean.getEMPNO()
							+ " and tranevent='transfer' and att_month between '"
							+ ReportDAO.BOM(empoffbean.getEFFDATE())
							+ "' and '"
							+ ReportDAO.EOM(empoffbean.getEFFDATE()) + "' ");

			while (rstransfer.next()) {

				Empstatus = rstransfer.getString(1);
				System.out.println(Empstatus);
				if (Empstatus.equalsIgnoreCase("approved")) {
					break;
				} else {
					continue;
				}

			}

			System.out
					.println("select distinct PRJ_SRNO from EMPTRAN where EMPNO="
							+ empoffbean.getEMPNO()
							+ "  and srno=(select max(srno) from emptran where empno= "
							+ empoffbean.getEMPNO() + "  ");
			Rsemptran = stEmptran
					.executeQuery("select distinct PRJ_SRNO from EMPTRAN where EMPNO="
							+ empoffbean.getEMPNO()
							+ "  and srno=(select max(srno) from emptran where empno= "
							+ empoffbean.getEMPNO() + ")  ");
			while (Rsemptran.next()) {

				prj = Rsemptran.getString(1);
				System.out.println(prj);
			}

			String Sql = "select distinct appr_status from ATTENDANCE_STATUS where site_id="
					+ empoffbean.getPrj_name()
					+ ""
					+ "	and att_month between '"
					+ ReportDAO.BOM(empoffbean.getEFFDATE())
					+ "' and '"
					+ ReportDAO.EOM(empoffbean.getEFFDATE()) + "' ";

			System.out.println("for site status check if not approved " + Sql);
			rsSite_check = stSite_check.executeQuery(Sql);

			while (rsSite_check.next()) {

				Site_status = rsSite_check.getString(1) == null ? ""
						: rsSite_check.getString(1);
				System.out.println(Site_status);
			}

			if (prj.equalsIgnoreCase(empoffbean.getPrj_name())) {
				rs = st.executeQuery("select max(SRNO) from EMPTRAN where EMPNO="
						+ empoffbean.getEMPNO() + "");
				while (rs.next()) {
					srno = rs.getInt(1);
				}
				srno = srno + 1;
				Connection conn = ConnectionManager.getConnectionTech();
				Statement stmt = conn.createStatement();
				ResultSet rst = stmt
						.executeQuery("SELECT Project_Code FROM Project_Sites WHERE Site_isdeleted = 0 and SITE_ID='"
								+ empoffbean.getPrj_name() + "'");

				int updtsrno = srno - 1;
				String update = "update EMPTRAN set STATUS=0 where EMPNO= "
						+ empoffbean.getEMPNO() + " and SRNO = " + updtsrno;
				Statement updt = con.createStatement();
				updt.executeUpdate(update);

				prst = con
						.prepareStatement("insert into EMPTRAN(EMPNO,EFFDATE,SRNO,ORDER_NO,ORDER_DT,MDESC,PRJ_SRNO,ACNO,GRADE,DESIG,DEPT,STATUS,TRNCD,PRJ_CODE,BANK_NAME,MNGR_ID,CREATED_BY,CREATED_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				prst.setInt(1, empoffbean.getEMPNO());
				prst.setString(2, empoffbean.getEFFDATE());
				prst.setInt(3, srno);
				prst.setString(4, empoffbean.getORDER_NO());
				prst.setString(5, empoffbean.getORDER_DT());
				prst.setString(6, empoffbean.getMDESC());
				prst.setInt(7, Integer.parseInt(empoffbean.getPrj_name()));
				/*
				 * if(rst.next()){ prst.setInt(7,rst.getInt(1)); } conn.close();
				 */
				prst.setString(8, empoffbean.getACNO());
				prst.setInt(9, empoffbean.getGRADE());
				prst.setInt(10, empoffbean.getDESIG());
				prst.setInt(11, empoffbean.getDEPT());
				prst.setInt(12, empoffbean.getSTATUS());
				prst.setInt(13, empoffbean.getTRNCD());
				// prst.setString(14,empoffbean.getPrj_name());
				if (rst.next()) {
					prst.setString(14, rst.getString(1));
				}
				conn.close();
				prst.setInt(15, empoffbean.getBANK_NAME());
				prst.setInt(16, empoffbean.getManagerId());
				prst.setString(17, uid);
				prst.setString(18, ReportDAO.getSysDate());
				prst.execute();
				con.close();
				flag = true;
			}

			else if (!prj.equalsIgnoreCase(empoffbean.getPrj_name())
					&& Empstatus.equalsIgnoreCase("approved")
					&& !Site_status.equalsIgnoreCase("approved")) {
				rs = st.executeQuery("select max(SRNO) from EMPTRAN where EMPNO="
						+ empoffbean.getEMPNO() + "");
				while (rs.next()) {
					srno = rs.getInt(1);
				}
				srno = srno + 1;
				Connection conn = ConnectionManager.getConnectionTech();
				Statement stmt = conn.createStatement();
				ResultSet rst = stmt
						.executeQuery("SELECT Project_Code FROM Project_Sites WHERE Site_isdeleted = 0 and SITE_ID='"
								+ empoffbean.getPrj_name() + "'");

				int updtsrno = srno - 1;
				String update = "update EMPTRAN set STATUS=0 where EMPNO= "
						+ empoffbean.getEMPNO() + " and SRNO = " + updtsrno;
				Statement updt = con.createStatement();
				updt.executeUpdate(update);

				prst = con
						.prepareStatement("insert into EMPTRAN(EMPNO,EFFDATE,SRNO,ORDER_NO,ORDER_DT,MDESC,PRJ_SRNO,ACNO,GRADE,DESIG,DEPT,STATUS,TRNCD,PRJ_CODE,BANK_NAME,MNGR_ID,CREATED_BY,CREATED_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				prst.setInt(1, empoffbean.getEMPNO());
				prst.setString(2, empoffbean.getEFFDATE());
				prst.setInt(3, srno);
				prst.setString(4, empoffbean.getORDER_NO());
				prst.setString(5, empoffbean.getORDER_DT());
				prst.setString(6, empoffbean.getMDESC());
				prst.setInt(7, Integer.parseInt(empoffbean.getPrj_name()));
				/*
				 * if(rst.next()){ prst.setInt(7,rst.getInt(1)); } conn.close();
				 */
				prst.setString(8, empoffbean.getACNO());
				prst.setInt(9, empoffbean.getGRADE());
				prst.setInt(10, empoffbean.getDESIG());
				prst.setInt(11, empoffbean.getDEPT());
				prst.setInt(12, empoffbean.getSTATUS());
				prst.setInt(13, empoffbean.getTRNCD());
				// prst.setString(14,empoffbean.getPrj_name());
				if (rst.next()) {
					prst.setString(14, rst.getString(1));
				}
				conn.close();
				prst.setInt(15, empoffbean.getBANK_NAME());
				prst.setInt(16, empoffbean.getManagerId());
				prst.setString(17, uid);
				prst.setString(18, ReportDAO.getSysDate());
				prst.execute();

				// for inserting into slab for goa and karnataka site
				ResultSet rsSlab = null;
				Statement stSlab = null;
				conTech = ConnectionManager.getConnectionTech();
				stSlab = conTech.createStatement();

				// for goa
				rsSlab = stSlab
						.executeQuery("select Site_ID from Project_Sites where Site_State like '%Goa%'  ");

				while (rsSlab.next()) {
					if (rsSlab.getString("Site_ID").equalsIgnoreCase(
							empoffbean.getPrj_name())) {
						String sql = " IF EXISTS(select * from slab where emp_cat="
								+ empoffbean.getEMPNO()
								+ " and trncd=202 ) "
								+ "  update slab set effdate='31-Dec-2099',toamt=9999999.00 where emp_cat="
								+ empoffbean.getEMPNO()
								+ " and trncd=202 and srno=1  "
								+ "  else INSERT INTO slab values ('31-Dec-2099',"
								+ empoffbean.getEMPNO()
								+ ",202,1,0,9999999.00,0,0,0,0,0)";

						System.out
								.println("Slab inserted/updated Successfully for goa"
										+ sql);

						st.execute(sql);

						sql = "update slab set effdate='"
								+ empoffbean.getEFFDATE() + "' where emp_cat="
								+ empoffbean.getEMPNO()
								+ " and trncd=202  and srno=2 ";
						System.out.println("slab changed for goa of 2 " + sql);
						st.execute(sql);

						break;
					} else {
						String sql = "  update slab set effdate='"
								+ empoffbean.getEFFDATE() + "' where emp_cat="
								+ empoffbean.getEMPNO() + " and trncd=202 ";

						System.out
								.println("Slab updated for end of slab!! Successfully for goa"
										+ sql);

						st.execute(sql);
					}
				}

				// for karnataka
				rsSlab = stSlab
						.executeQuery("select Site_ID from Project_Sites where Site_State like '%Kar%'  ");

				while (rsSlab.next()) {
					if (rsSlab.getString("Site_ID").equalsIgnoreCase(
							empoffbean.getPrj_name())) {
						String sql = " IF EXISTS(select * from slab where emp_cat="
								+ empoffbean.getEMPNO()
								+ " and trncd=202 and srno=1 ) "
								+ "  update slab set effdate='31-Dec-2099', toamt=15000.00 where emp_cat= (select empno from EMPTRAN where SRNO=(select max(srno) from EMPTRAN where EMPNO=1) and PRJ_SRNO "
								+ "  not in( SELECT  [Site_ID]     FROM [HPM].[dbo].[Project_Sites] where Site_State like '%goa%')) and trncd=202 and srno=1 "
								+ "  else INSERT INTO slab values ('31-Dec-2099',"
								+ empoffbean.getEMPNO()
								+ ",202,1,0,15000.00,0,0,0,0,0)";

						System.out
								.println("Slab inserted/updated Successfully for karnataka "
										+ sql);

						st.execute(sql);

						sql = " IF EXISTS(select * from slab where emp_cat="
								+ empoffbean.getEMPNO()
								+ " and trncd=202 and srno=2) "
								+ "  update slab set effdate='31-Dec-2099' where emp_cat= (select empno from EMPTRAN where SRNO=(select max(srno) from EMPTRAN where EMPNO=1) and PRJ_SRNO "
								+ "  not in( SELECT  [Site_ID]     FROM [HPM].[dbo].[Project_Sites] where Site_State like '%goa%')) and trncd=202 and srno=2 "
								+ "  else INSERT INTO slab values ('31-Dec-2099',"
								+ empoffbean.getEMPNO()
								+ ",202,2,15001.00,9999999.00,0,0,0,200,0)";

						System.out
								.println("Slab inserted/updated Successfully for karnataka "
										+ sql);

						st.execute(sql);
						break;
					}
					// Below Query having problem ,It's return multiple records
					else {
						String sql = "  update slab set effdate='"
								+ empoffbean.getEFFDATE()
								+ "' where emp_cat= (select empno from EMPTRAN where SRNO=(select max(srno) from EMPTRAN where EMPNO=1) and PRJ_SRNO "
								+ "  not in( SELECT  [Site_ID]     FROM [HPM].[dbo].[Project_Sites] where Site_State like '%goa%')) and trncd=202 ";

						System.out
								.println("Slab updated for end of slab!! Successfully for karnataka "
										+ sql);

						st.execute(sql);
					}
				}

				con.close();

				flag = true;
			}

			// System.out.println("we are here "+prj+" we are hre"+empoffbean.getPrj_name());

			/*
			 * if(!prj.equalsIgnoreCase(empoffbean.getPrj_name()) ){
			 * 
			 * System.out.println("else parttttttttt");
			 * rs=st.executeQuery("select max(SRNO) from EMPTRAN where EMPNO="
			 * +empoffbean.getEMPNO()+""); while(rs.next()) { srno=rs.getInt(1);
			 * } srno=srno+1; Connection conn =
			 * ConnectionManager.getConnectionTech(); Statement stmt =
			 * conn.createStatement(); ResultSet rst = stmt.executeQuery(
			 * "SELECT Project_Code FROM Project_Sites WHERE Site_isdeleted = 0 and SITE_ID='"
			 * +empoffbean.getPrj_name()+"'");
			 * 
			 * int updtsrno = srno-1; String update =
			 * "update EMPTRAN set STATUS=0 where EMPNO= "
			 * +empoffbean.getEMPNO()+" and SRNO = "+updtsrno; Statement updt
			 * =con.createStatement(); updt.executeUpdate(update);
			 * 
			 * prst = con.prepareStatement(
			 * "insert into EMPTRAN(EMPNO,EFFDATE,SRNO,ORDER_NO,ORDER_DT,MDESC,PRJ_SRNO,ACNO,GRADE,DESIG,DEPT,STATUS,TRNCD,PRJ_CODE,BANK_NAME,MNGR_ID,CREATED_BY,CREATED_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			 * ); prst.setInt(1,empoffbean.getEMPNO() );
			 * prst.setString(2,empoffbean.getEFFDATE()); prst.setInt(3,srno);
			 * prst.setString(4,empoffbean.getORDER_NO());
			 * prst.setString(5,empoffbean.getORDER_DT());
			 * prst.setString(6,empoffbean.getMDESC());
			 * prst.setInt(7,Integer.parseInt(empoffbean.getPrj_name()));
			 * if(rst.next()){ prst.setInt(7,rst.getInt(1)); } conn.close();
			 * prst.setString(8, empoffbean.getACNO());
			 * prst.setInt(9,empoffbean.getGRADE());
			 * prst.setInt(10,empoffbean.getDESIG());
			 * prst.setInt(11,empoffbean.getDEPT());
			 * prst.setInt(12,empoffbean.getSTATUS());
			 * prst.setInt(13,empoffbean.getTRNCD());
			 * //prst.setString(14,empoffbean.getPrj_name()); if(rst.next()){
			 * prst.setString(14,rst.getString(1)); } conn.close();
			 * prst.setInt(15, empoffbean.getBANK_NAME()); prst.setInt(16,
			 * empoffbean.getManagerId()); prst.setString(17,uid);
			 * prst.setString(18, ReportDAO.getSysDate()); prst.execute();
			 * con.close(); flag=true;
			 * 
			 * 
			 * }
			 */

			else {
				flag = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	public ArrayList<EmpOffBean> getEmpOfficInfo(String EMPNO) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<EmpOffBean> empofficlist = new ArrayList<EmpOffBean>();
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select * from EMPTRAN where empno='" + EMPNO
					+ "' order by srno");
			// rs=st.executeQuery("select * from emptran where empno = '"+EMPNO+"' and SRNO = (select MAX(SRNO) from emptran where empno = '"+EMPNO+"')");
			EmpOffBean empoffbean;
			while (rs.next()) {
				String Prj_Name = null;
				String Prj_Code = null;
				con = ConnectionManager.getConnectionTech();
				Statement stmt = con.createStatement();
				// ResultSet rs1 =
				// stmt.executeQuery("SELECT Site_Name, Project_Code from Project_Sites where Project_Code='"+rs.getString("PRJ_CODE")+"'");
				ResultSet rs1 = stmt
						.executeQuery("SELECT Site_Name, Project_Code from Project_Sites where Site_isdeleted = 0 and Site_ID='"
								+ rs.getString("PRJ_SRNO") + "'");
				while (rs1.next()) {
					Prj_Name = rs1.getString(1);
					Prj_Code = rs1.getString(2);
				}
				rs1.close();
				stmt.close();

				empoffbean = new EmpOffBean();
				empoffbean.setACNO(rs.getString("ACNO") == null ? "" : rs
						.getString("ACNO"));
				empoffbean.setDESIG(rs.getString("DESIG") == null ? 0 : rs
						.getInt("DESIG"));
				empoffbean.setDEPT(rs.getString("DEPT") == null ? 0 : rs
						.getInt("DEPT"));
				empoffbean.setEFFDATE(rs.getDate("EFFDATE") == null ? ""
						: dateFormat(rs.getDate("EFFDATE")));
				empoffbean.setEMPNO(rs.getString("EMPNO") == null ? 0 : rs
						.getInt("EMPNO"));
				empoffbean.setBRANCH(rs.getString("BRANCH") == null ? 0 : rs
						.getInt("BRANCH"));
				empoffbean.setGRADE(rs.getString("GRADE") == null ? 0 : rs
						.getInt("GRADE"));
				empoffbean.setORDER_DT(rs.getDate("ORDER_DT") == null ? ""
						: dateFormat(rs.getDate("ORDER_DT")));
				empoffbean.setORDER_NO(rs.getString("ORDER_NO") == null ? ""
						: rs.getString("ORDER_NO"));
				empoffbean.setSRNO(rs.getString("SRNO") == null ? 0 : rs
						.getInt("SRNO"));
				empoffbean.setSTATUS(rs.getString("STATUS") == null ? 0 : rs
						.getInt("STATUS"));
				empoffbean.setTRNCD(rs.getString("TRNCD") == null ? 0 : rs
						.getInt("TRNCD"));
				empoffbean.setMDESC(rs.getString("MDESC") == null ? "" : rs
						.getString("MDESC"));
				empoffbean.setPrj_code(Prj_Code);
				empoffbean.setPrj_name(Prj_Name);
				empoffbean.setBANK_NAME(rs.getString("BANK_NAME") == null ? 0
						: rs.getInt("BANK_NAME"));
				empofficlist.add(empoffbean);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empofficlist;
	}

	public void updateOffinfo(EmpOffBean empoffbean) {
		Statement st = null;
		con = ConnectionManager.getConnection();
		try {
			st = con.createStatement();
			st.executeUpdate("update EMPTRAN set EFFDATE='"
					+ empoffbean.getEFFDATE() + "',TRNCD="
					+ empoffbean.getTRNCD() + ",ORDER_NO='"
					+ empoffbean.getORDER_NO() + "',ORDER_DT='"
					+ empoffbean.getORDER_DT() + "',MDESC='"
					+ empoffbean.getMDESC() + "',PRJ_SRNO="
					+ empoffbean.getPrj_srno() + ",ACNO='"
					+ empoffbean.getACNO() + "',GRADE=" + empoffbean.getGRADE()
					+ ",DESIG=" + empoffbean.getDESIG() + ",DEPT="
					+ empoffbean.getDEPT() + ",BANK_NAME="
					+ empoffbean.getBANK_NAME() + ",PRJ_CODE='"
					+ empoffbean.getPrj_code() + "' where EMPNO="
					+ empoffbean.getEMPNO() + " and SRNO="
					+ empoffbean.getSRNO() + "");
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public String getDescrption(String Column, String Table, String Condition1,
			int Condition2) {
		Statement st = null;
		String descrption = "";
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select " + Column + " from " + Table
					+ " where " + Condition1 + "=" + Condition2 + "");
			while (rs.next()) {
				descrption = rs.getString(1);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return descrption;
	}

	public ArrayList<Lookup> getGradeBranchList(String tab_name) {

		ArrayList<Lookup> result = new ArrayList<Lookup>();
		Statement st = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select * from " + tab_name
					+ "  order by 3 ASC ");
			while (rs.next()) {
				Lookup lkp = new Lookup();
				lkp.setLKP_DESC(rs.getString(2));
				lkp.setLKP_SRNO(rs.getInt(1));
				result.add(lkp);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public TransactionBean getInfoEmpTran(String EMPNO) {
		TransactionBean trbn = new TransactionBean();
		try {
			con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("select e.salute,e.fname,e.mname,e.lname,e.pfno,e.panno,et.prj_srno,et.acno,et.grade,et.desig,et.dept,e.doj,e.empcode,et.prj_code,e.dol,e.uanno  from emptran et inner join empmast e on et.empno=e.empno where srno=(select max(srno) from emptran where empno="
					+ EMPNO + ") and e.empno=" + EMPNO + "");
			while (rs.next()) {
				trbn.setEmpname(new LookupHandler().getLKP_Desc("SALUTE",
						rs.getInt(1))
						+ "  "
						+ rs.getString(2)
						+ "  "
						+ rs.getString(3)
						+ "  " + rs.getString(4));
				trbn.setEmpno(Integer.parseInt(EMPNO));
				trbn.setPfno(rs.getString(5) == null ? "" : rs.getString(5));
				trbn.setPanno(rs.getString(6) == null ? "" : rs.getString(6));
				trbn.setBranch(Integer.parseInt(rs.getString(7) == null ? "0"
						: rs.getString(7)));
				trbn.setAccno(rs.getString(8) == null ? "" : rs.getString(8));
				trbn.setGrade(Integer.parseInt(rs.getString(9) == null ? "0"
						: rs.getString(9)));
				trbn.setDesg(Integer.parseInt(rs.getString(10) == null ? "0"
						: rs.getString(10)));
				trbn.setDept(Integer.parseInt(rs.getString(11) == null ? "0"
						: rs.getString(11)));
				trbn.setDOJ(rs.getString(12) == null ? "" : dateFormat(rs
						.getDate(12)));
				trbn.setEMPCODE(rs.getString(13) == null ? "" : rs
						.getString(13));
				trbn.setPrj_Code(rs.getString(14) == null ? "" : rs
						.getString(14));
				trbn.setDol(rs.getString(15) == null ? "" : dateFormat(rs
						.getDate(15)));
				trbn.setUanno(rs.getString(16) == null ? "" : rs
						.getString(16));
			}
			con.close();
		} catch (Exception e) {
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return trbn;
	}

	public ArrayList<EmpOffBean> getprojectCode()// param for getting projcet
													// code list
	{
		ArrayList<EmpOffBean> list = new ArrayList<EmpOffBean>();
		Connection con = null;
		ResultSet rs = null;

		// String query="SELECT * FROM Prj";
		// String query="SELECT * FROM Project_Sites order by Site_Name";
		// String
		// query="select rtrim(project_code)+'  '+rtrim(Site_City) as site_Add,* from Project_Sites order by Project_Code";
		String query = "select rtrim(Site_City)+'   -      '+rtrim(Site_Name) as site_Add,* from Project_Sites where Site_isdeleted = 0 and Site_Status='open' order by Site_City ASC";
		con = ConnectionManager.getConnectionTech();

		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmpOffBean emp = new EmpOffBean();

				emp.setPrj_srno(rs.getInt("Site_ID"));
				emp.setPrj_code(rs.getString("Project_Code"));
				emp.setPrj_name(rs.getString("site_Add"));
				emp.setSite_name(rs.getString("Site_Name"));
				emp.setSite_feild3(rs.getString("Site_Feild3"));

				/*
				 * System.out.print(rs.getInt("Site_ID"));
				 * System.out.print(rs.getString("Project_Code"));
				 * System.out.println(rs.getString("Site_Name"));
				 */

				list.add(emp);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<TranBean> getEmpList(String prjCode) {

		ArrayList<TranBean> list = new ArrayList<TranBean>();
		Connection con = null;
		ResultSet rs = null;

		// String query =
		// "SELECT EMPNO FROM EMPTRAN WHERE PRJ_CODE='"+prjCode+"'";
		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A'  and etn.PRJ_SRNO="+prjCode;

		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO='"+prjCode+"' and etn.SRNO = (select MAX(en.SRNO) from EMPTRAN en where en.PRJ_SRNO = '"+prjCode+"' and etn.EMPNO = en.EMPNO )";
		String query = "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO="
				+ prjCode
				+ "  AND etn.SRNO=(SELECT MAX(ET.SRNO) FROM EMPTRAN ET WHERE ET.EMPNO=emp.EMPNO) order by emp.EMPNO";

		con = ConnectionManager.getConnection();
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				TranBean trbn = new TranBean();
				trbn.setEMPNO(rs.getInt(1));
				list.add(trbn);
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<TranBean> getEmpList_Status(String prjCode, String month) {

		ArrayList<TranBean> list = new ArrayList<TranBean>();
		Connection con = null;
		ResultSet rs = null;
		String dt = "25-" + month;
		/*
		 * String query =
		 * "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on " +
		 * "emp.EMPNO = etn.EMPNO join SAL_DETAILS s on emp.EMPNO = s.EMPNO where emp.STATUS = 'A'"
		 * + " and etn.PRJ_SRNO='"+prjCode+"' " +
		 * "and etn.STATUS=1 and s.SAL_STATUS <> 'FINALIZED' and s.SAL_MONTH = '"
		 * +month+"'";
		 */

		/*
		 * String query =
		 * " SELECT E.EMPNO FROM EMPMAST E,EMPTRAN T  WHERE E.EMPNO = T.EMPNO AND T.PRJ_SRNO ="
		 * +prjCode+
		 * " AND T.SRNO = (SELECT MAX(SRNO) FROM EMPTRAN T1 WHERE T1.EMPNO = E.EMPNO)"
		 * +
		 * " AND (( E.STATUS='A' AND E.DOJ <= dateadd(dd,-(DAY(DATEADD(mm,1,CONVERT(datetime,  REPLACE('"
		 * +month+"', '-', ' '), 106))))," +
		 * " DATEADD(mm,1,CONVERT(datetime,  REPLACE('"
		 * +month+"', '-', ' '), 106)))  ) or" +
		 * " (E.STATUS ='N' And DATEPART(year,  E.DOL)=DATEPART(year,CONVERT(datetime,  REPLACE('"
		 * +month+"', '-', ' '), 106)) " +
		 * " And DATEPART(mm,  E.DOL)>=DATEPART(mm,CONVERT(datetime,  REPLACE('"
		 * +month+"', '-', ' '), 106)))) AND  " +
		 * " E.EMPNO not in (select Empno from sal_details S where S.SAL_STATUS = 'FINALIZED' AND S.SAL_MONTH='"
		 * +month+"')"+
		 * "   and E.EMPNO in (select distinct empno from paytran where trndt between '"
		 * +
		 * ReportDAO.BOM(dt)+"' and '"+ReportDAO.EOM(dt)+"' ) ORDER BY E.EMPNO";
		 */
		String query = " SELECT E.EMPNO FROM EMPMAST E,EMPTRAN T  WHERE E.EMPNO = T.EMPNO AND T.PRJ_SRNO ="
				+ prjCode
				+ " AND T.SRNO = (SELECT MAX(SRNO) FROM EMPTRAN T1 WHERE T1.EMPNO = E.EMPNO)"
				+ " AND (( E.STATUS='A' AND E.DOJ <= '"
				+ ReportDAO.EOM(dt)
				+ "')   or"
				+ " (E.STATUS ='N' And "
				+ " E.DOL>='"
				+ ReportDAO.BOM(dt)
				+ "')) AND  "
				+ " E.EMPNO not in (select Empno from sal_details S where S.SAL_STATUS = 'FINALIZED' AND S.SAL_MONTH='"
				+ month
				+ "')"
				+ "   and E.EMPNO in (select distinct empno from paytran where trndt between '"
				+ ReportDAO.BOM(dt)
				+ "' and '"
				+ ReportDAO.EOM(dt)
				+ "' ) ORDER BY E.EMPNO";
		// System.out.println(query);

		con = ConnectionManager.getConnection();
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				TranBean trbn = new TranBean();
				trbn.setEMPNO(rs.getInt(1));
				list.add(trbn);
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public EmpOffBean getEmpOfficAddInfo(String EMPNO) {
		ResultSet rs = null;
		Statement st = null;
		EmpOffBean empaddofficinfo = new EmpOffBean();
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select * from emptran where empno = '"
					+ EMPNO
					+ "' and SRNO = (select MAX(SRNO) from emptran where empno = '"
					+ EMPNO + "')");
			// rs=st.executeQuery("select * from emptran where empno = '"+EMPNO+"'");

			while (rs.next()) {
				String Prj_Name = null;
				String Prj_Code = null;
				con = ConnectionManager.getConnectionTech();
				Statement stmt = con.createStatement();
				// ResultSet rs1 =
				// stmt.executeQuery("SELECT Site_Name, Project_Code from Project_Sites where Project_Code='"+rs.getString("PRJ_CODE")+"'");
				ResultSet rs1 = stmt
						.executeQuery("SELECT Site_Name, Project_Code from Project_Sites where Site_isdeleted = 0 and Site_ID='"
								+ rs.getString("PRJ_SRNO") + "'");
				while (rs1.next()) {
					Prj_Name = rs1.getString(1);
					Prj_Code = rs1.getString(2);
				}
				rs1.close();
				stmt.close();

				/*
				 * while(rs.next()) {
				 */
				empaddofficinfo.setACNO(rs.getString("ACNO") == null ? "" : rs
						.getString("ACNO"));
				empaddofficinfo.setDESIG(rs.getString("DESIG") == null ? 0 : rs
						.getInt("DESIG"));
				empaddofficinfo.setDEPT(rs.getString("DEPT") == null ? 0 : rs
						.getInt("DEPT"));
				empaddofficinfo.setEFFDATE(rs.getDate("EFFDATE") == null ? ""
						: dateFormat(rs.getDate("EFFDATE")));
				empaddofficinfo.setEMPNO(rs.getString("EMPNO") == null ? 0 : rs
						.getInt("EMPNO"));
				empaddofficinfo.setBRANCH(rs.getString("BRANCH") == null ? 0
						: rs.getInt("BRANCH"));
				empaddofficinfo.setGRADE(rs.getString("GRADE") == null ? 0 : rs
						.getInt("GRADE"));
				empaddofficinfo.setORDER_DT(rs.getDate("ORDER_DT") == null ? ""
						: dateFormat(rs.getDate("ORDER_DT")));
				empaddofficinfo
						.setORDER_NO(rs.getString("ORDER_NO") == null ? "" : rs
								.getString("ORDER_NO"));
				empaddofficinfo.setSRNO(rs.getString("SRNO") == null ? 0 : rs
						.getInt("SRNO"));
				empaddofficinfo.setSTATUS(rs.getString("STATUS") == null ? 0
						: rs.getInt("STATUS"));
				empaddofficinfo.setTRNCD(rs.getString("TRNCD") == null ? 0 : rs
						.getInt("TRNCD"));
				empaddofficinfo.setMDESC(rs.getString("MDESC") == null ? ""
						: rs.getString("MDESC"));
				empaddofficinfo
						.setPrj_srno(rs.getString("PRJ_SRNO") == null ? 0 : rs
								.getInt("PRJ_SRNO"));
				empaddofficinfo
						.setBANK_NAME(rs.getString("BANK_NAME") == null ? 0
								: rs.getInt("BANK_NAME"));
				empaddofficinfo.setPrj_code(Prj_Code);
				empaddofficinfo.setPrj_name(Prj_Name);

			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empaddofficinfo;
	}

	public void insertInitialTran(EmpOffBean eob, int eno) {
		EmpAttendanceHandler EAH = new EmpAttendanceHandler();
		String currentdate = EAH.getServerDate();
		ResultSet resultSet = null;
		ResultSet resultSet_Slab = null;
		ResultSet attendancestatus = null;
		Statement stSlab = null;

		try {

			con = ConnectionManager.getConnection();
			st = con.createStatement();

			EmpAttendanceHandler empAttendanceHandler = new EmpAttendanceHandler();
			ArrayList<String> weekOffList = new ArrayList<String>();
			ArrayList<String> holidaysList = new ArrayList<String>();
			String today = empAttendanceHandler.getServerDate();

			st.executeUpdate("insert into emptran (EMPNO,EFFDATE,SRNO,ORDER_DT,PRJ_SRNO,STATUS,MNGR_ID,CREATED_BY,CREATED_DATE)"
					+ " values("
					+ eob.getEMPNO()
					+ ",'"
					+ eob.getEFFDATE()
					+ "',1,'"
					+ eob.getEFFDATE()
					+ "',"
					+ eob.getPrj_srno()
					+ ",1,0," + eno + ",'" + ReportDAO.getSysDate() + "')");

			System.out.println("INSERTED SUCCESSFULY........");

			// for inserting into slab for goa and karnataka site

			conTech = ConnectionManager.getConnectionTech();
			stSlab = conTech.createStatement();

			// for goa
			resultSet_Slab = stSlab
					.executeQuery("select Site_ID from Project_Sites where Site_State like '%Goa%'  ");

			while (resultSet_Slab.next()) {
				if (resultSet_Slab.getInt("Site_ID") == eob.getPrj_srno()) {
					String sql = " INSERT INTO slab values ('31-Dec-2099',"
							+ eob.getEMPNO() + ",202,1,0,9999999.00,0,0,0,0,0)";
					st.execute(sql);
					System.out.println("Slab inserted Successfully for goa"
							+ sql);
				}

			}

			// for karnataka
			resultSet_Slab = stSlab
					.executeQuery("select Site_ID from Project_Sites where Site_State like '%Kar%'  ");

			while (resultSet_Slab.next()) {
				if (resultSet_Slab.getInt("Site_ID") == eob.getPrj_srno()) {
					String sql = " INSERT INTO slab values ('31-Dec-2099',"
							+ eob.getEMPNO() + ",202,1,0,15000.00,0,0,0,0,0)";
					st.execute(sql);
					sql = " INSERT INTO slab values ('31-Dec-2099',"
							+ eob.getEMPNO()
							+ ",202,2,15001.00,9999999.00,0,0,0,200,0)";
					st.execute(sql);
					System.out
							.println("Slab inserted Successfully for karnataka 9999999.00"
									+ sql);
				}

			}

			/*
			 * attendancestatus=st.executeQuery(
			 * "select * from ATTENDANCE_STATUS where appr_DATE between '"
			 * +ReportDAO
			 * .BOM(eob.getEFFDATE())+"' and '"+ReportDAO.EOM(eob.getEFFDATE
			 * ())+"' and site_id="
			 * +eob.getPrj_srno()+" and appr_status='approved' ");
			 * if(!attendancestatus.next())//start if (for checking whether the
			 * attendance sheet of month in which empis adding is approved or
			 * not ... if approved then not adding attenedance of that emp..else
			 * adding.. {
			 */
			// for insertion of new employee in attendance
			String date = ReportDAO.BOM(eob.getEFFDATE());
			int days = (int) Calculate.getDays(eob.getEFFDATE());

			for (int x = 1; x <= days; x++) {
				SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
				Calendar c = Calendar.getInstance();

				c.setTime(format.parse(date));

				st.executeUpdate("insert into Emp_attendance (empno, site_id, attd_date, attd_val, created_by, created_date ) "
						+ "values ("
						+ eob.getEMPNO()
						+ ", "
						+ eob.getPrj_srno()
						+ " , '"
						+ date
						+ "' , ' '  , "
						+ eno + " , '" + today + "' ) ");

				/*
				 * st.executeUpdate(
				 * "IF EXISTS (SELECT empno FROM  Emp_attendance WHERE empno=" +
				 * eob.getEMPNO() + " and attd_date ='" + date + "')" +
				 * "UPDATE  Emp_attendance SET empno=" + eob.getEMPNO() +
				 * ", attd_date ='" + date + "', ATTD_VAL='"
				 * 
				 * + "' where empno=" + eob.getEMPNO() + " and site_id=" +
				 * eob.getPrj_srno() + " and attd_date ='" + date + "' else" +
				 * " INSERT INTO Emp_attendance (empno,site_id, attd_date, attd_val, created_date, created_by) VALUES("
				 * + eob.getEMPNO() + "," + eob.getPrj_srno() + ",'" +date+
				 * "',' ','"+today+"',"+eno+") ");
				 */

				c.add(Calendar.DATE, 1);
				date = format.format(c.getTime());

			}
			// commenting this code bcz sheet is getting disaproved
			// ................
			/*
			 * st.executeUpdate(
			 * "IF EXISTS (SELECT * from Attendance_status where site_id=" +
			 * eob.getPrj_srno() + " and att_month between '" +
			 * ReportDAO.BOM(eob.getEFFDATE()) + "' and '" +
			 * ReportDAO.EOM(eob.getEFFDATE()) + "')" +
			 * " update  Attendance_status set appr_status='saved', " +
			 * "updated_by= "
			 * +eno+" , updated_date= '"+currentdate+"' ,submit_date='" +
			 * ReportDAO.getSysDate() + "' where site_id=" + eob.getPrj_srno() +
			 * " and att_month between '" + ReportDAO.BOM(eob.getEFFDATE()) +
			 * "' and '" + ReportDAO.EOM(eob.getEFFDATE()) + "' " +
			 * " ELSE insert into Attendance_status  (site_id, appr_date, appr_status, att_month, submit_date, created_by, created_date) values("
			 * + eob.getPrj_srno() + ",'" + ReportDAO.getSysDate() +
			 * "','saved','" + eob.getEFFDATE() + "','" + ReportDAO.getSysDate()
			 * + "', "+eno+" , '"+currentdate+"')");
			 */

			// FOR INSERTING "NJ" OF NEW ADDED EMPLOYEES BEFORE JOINING DATE

			st.executeUpdate("update Emp_attendance set attd_val='NJ' where empno="
					+ eob.getEMPNO()
					+ " and attd_date < '"
					+ eob.getEFFDATE()
					+ "'  ");

			// FOR INSERTING WEEKLY OFFS OF NEW ADDED EMPLOYEES AFTER JOINING
			// DATE
			// resultSet=st.executeQuery("select distinct attd_date from EMP_ATTENDANCE where ATTD_VAL='WO' and ATTD_DATE > '"+eob.getEFFDATE()+"'  ");
			resultSet = st
					.executeQuery("select distinct fdate from holdmast where BRANCH in "
							+ " ('All','"
							+ eob.getPrj_srno()
							+ "') "
							+ "and fdate between  '"
							+ ReportDAO.BOM(eob.getEFFDATE())
							+ "' "
							+ " and '"
							+ ReportDAO.EOM(eob.getEFFDATE()) + "' ");
			while (resultSet.next()) {
				Attend_bean attend_bean = new Attend_bean();
				attend_bean.setDate(resultSet.getString(1));
				weekOffList.add(attend_bean.getDate());
			}
			for (int index = 0; index < weekOffList.size(); index++) {
				st.executeUpdate("update Emp_attendance set attd_val='WO' where empno="
						+ eob.getEMPNO()
						+ " and attd_date = '"
						+ weekOffList.get(index) + "'  ");
			}

			// FOR INSERTING HOLIDAYS OF NEW ADDED EMPLOYEES AFTER JOINING DATE

			resultSet = st
					.executeQuery("select distinct attd_date from EMP_ATTENDANCE where ATTD_VAL='HD' and ATTD_DATE >= '"
							+ eob.getEFFDATE()
							+ "' and site_id="
							+ eob.getPrj_srno() + "  ");
			while (resultSet.next()) {
				Attend_bean attend_bean = new Attend_bean();
				attend_bean.setDate(resultSet.getString(1));
				holidaysList.add(attend_bean.getDate());
			}
			for (int index = 0; index < holidaysList.size(); index++) {

				st.executeUpdate("update Emp_attendance set attd_val='HD' where empno="
						+ eob.getEMPNO()
						+ " and attd_date = '"
						+ holidaysList.get(index) + "'  ");
			}
			con.close();
			// }// end if (for checking whether the attendance sheet of month in
			// which empis adding is approved or not ... if approved then not
			// adding attenedance of that emp..else adding..)
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<TranBean> getEmp(String prjCode, String date) {

		ArrayList<TranBean> list = new ArrayList<TranBean>();
		Connection con = null;
		ResultSet rs = null;

		// String query =
		// "SELECT EMPNO FROM EMPTRAN WHERE PRJ_CODE='"+prjCode+"'";
		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A'  and etn.PRJ_SRNO="+prjCode;

		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO='"+prjCode+"' and etn.SRNO = (select MAX(en.SRNO) from EMPTRAN en where en.PRJ_SRNO = '"+prjCode+"' and etn.EMPNO = en.EMPNO )";
		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO="+prjCode+"  AND etn.SRNO=(SELECT MAX(ET.SRNO) FROM EMPTRAN ET WHERE ET.EMPNO=emp.EMPNO) order by emp.EMPNO";
		String sql = "select empno from EMP_ATTENDANCE where site_id='"
				+ prjCode + "' and ATTD_DATE " + "between '"
				+ ReportDAO.BOM(date) + "' and '" + ReportDAO.EOM(date)
				+ "'  group by empno order by empno";
		con = ConnectionManager.getConnection();
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				TranBean trbn = new TranBean();
				trbn.setEMPNO(rs.getInt(1));

				list.add(trbn);
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<TranBean> getEmpList3(String prjCode, int empNo) {

		ArrayList<TranBean> list = new ArrayList<TranBean>();
		Connection con = null;
		ResultSet rs = null;

		/*
		 * String query =
		 * "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on " +
		 * "emp.EMPNO = etn.EMPNO join SAL_DETAILS s on emp.EMPNO = s.EMPNO where emp.STATUS = 'A'"
		 * + " and etn.PRJ_SRNO='"+prjCode+"' " +
		 * "and etn.STATUS=1 and s.SAL_STATUS <> 'FINALIZED' and s.SAL_MONTH = '"
		 * +month+"'";
		 */

		String query = "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO='"
				+ prjCode + "' and etn.STATUS=1 order by emp.EMPNO";

		con = ConnectionManager.getConnection();
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				TranBean trbn = new TranBean();
				trbn.setEMPNO(rs.getInt(1));
				list.add(trbn);
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<TranBean> getEmpList1(int prjCode, String month) {

		ArrayList<TranBean> list = new ArrayList<TranBean>();
		Connection con = null;
		ResultSet rs = null;

		/*
		 * String query =
		 * "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on " +
		 * "emp.EMPNO = etn.EMPNO join SAL_DETAILS s on emp.EMPNO = s.EMPNO where emp.STATUS = 'A'"
		 * + " and etn.PRJ_SRNO='"+prjCode+"' " +
		 * "and etn.STATUS=1 and s.SAL_STATUS <> 'FINALIZED' and s.SAL_MONTH = '"
		 * +month+"'";
		 */

		String query = "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO='"
				+ prjCode + "' and etn.STATUS=1 order by emp.EMPNO";

		con = ConnectionManager.getConnection();
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				TranBean trbn = new TranBean();
				trbn.setEMPNO(rs.getInt(1));
				list.add(trbn);
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<TranBean> getEmpListForAttendance(String prjCode,
			String Date) {

		ArrayList<TranBean> list = new ArrayList<TranBean>();
		Connection con = null;
		ResultSet rs = null;

		// String query =
		// "SELECT EMPNO FROM EMPTRAN WHERE PRJ_CODE='"+prjCode+"'";
		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A'  and etn.PRJ_SRNO="+prjCode;

		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO='"+prjCode+"' and etn.SRNO = (select MAX(en.SRNO) from EMPTRAN en where en.PRJ_SRNO = '"+prjCode+"' and etn.EMPNO = en.EMPNO )";
		String query = "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where (emp.STATUS = 'A' and "
				+ " emp.DOJ<='"
				+ ReportDAO.EOM(Date)
				+ "'  "
				+ "   or emp.STATUS = 'N' and (emp.DOL>='"
				+ ReportDAO.BOM(Date)
				+ "' or emp.DOL=null ) ) AND  etn.PRJ_SRNO='"
				+ prjCode
				+ "'  AND "
				+ " etn.SRNO=(SELECT MAX(ET.SRNO) FROM EMPTRAN ET WHERE ET.EMPNO=emp.EMPNO  AND ET.EFFDATE<='"
				+ ReportDAO.EOM(Date) + "') order by emp.EMPNO";
		System.out.println(query);
		con = ConnectionManager.getConnection();
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				TranBean trbn = new TranBean();
				trbn.setEMPNO(rs.getInt(1));
				list.add(trbn);
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Attend_bean> getEmpListForAttendance(String prjCode,
			String Date, String empno) {

		ArrayList<Attend_bean> list = new ArrayList<Attend_bean>();
		Connection con = null;
		ResultSet rs = null;

		// String query =
		// "SELECT EMPNO FROM EMPTRAN WHERE PRJ_CODE='"+prjCode+"'";
		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A'  and etn.PRJ_SRNO="+prjCode;

		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO='"+prjCode+"' and etn.SRNO = (select MAX(en.SRNO) from EMPTRAN en where en.PRJ_SRNO = '"+prjCode+"' and etn.EMPNO = en.EMPNO )";
		/*
		 * String query =
		 * "select emp.EMPNO , etn.prj_srno from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where (emp.STATUS = 'A' and "
		 * + " emp.DOJ<='"+ReportDAO.EOM(Date)+"'  " +
		 * "   or emp.STATUS = 'N' and (emp.DOL>='"+ReportDAO.BOM(Date)
		 * +"' or emp.DOL=null ) ) AND  etn.PRJ_SRNO='"+prjCode+"'  AND " +
		 * " etn.SRNO=(SELECT MAX(ET.SRNO) FROM EMPTRAN ET WHERE ET.EMPNO=emp.EMPNO  AND ET.EFFDATE<='"
		 * +
		 * ReportDAO.EOM(Date)+"') and emp.EMPNO = "+empno+" order by emp.EMPNO"
		 * ;
		 */

		String query = "select emp.EMPNO , etn.prj_srno from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where (emp.STATUS = 'A' and "
				+ " emp.DOJ<='"
				+ ReportDAO.EOM(Date)
				+ "'  "
				+ "   or emp.STATUS = 'N' and (emp.DOL>='"
				+ ReportDAO.BOM(Date)
				+ "' or emp.DOL=null ) )   AND "
				+ " etn.SRNO=(SELECT MAX(ET.SRNO) FROM EMPTRAN ET WHERE ET.EMPNO=emp.EMPNO  AND ET.EFFDATE<='"
				+ ReportDAO.EOM(Date)
				+ "') and emp.EMPNO = "
				+ empno
				+ " order by emp.EMPNO";

		con = ConnectionManager.getConnection();
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				Attend_bean trbn = new Attend_bean();
				trbn.setEmpno(rs.getString(1));
				trbn.setSite_id(rs.getString(2));
				list.add(trbn);
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<EmpOffBean> getActiveprojectCode()// param for getting
														// projcet code list
	{
		ArrayList<EmpOffBean> list = new ArrayList<EmpOffBean>();
		Connection con = null;
		ResultSet rs = null;

		// String query="SELECT * FROM Prj";
		// String query="SELECT * FROM Project_Sites order by Site_Name";
		// String
		// query="select rtrim(project_code)+'  '+rtrim(Site_City) as site_Add,* from Project_Sites order by Project_Code";
		String query = "select rtrim(Site_City)+'   -      '+rtrim(Site_Name) as site_Add,* from Project_Sites where"
				+ " site_id in "
				+ "(SELECT  distinct T.PRJ_SRNO FROM [hpmpayroll].[dbo].EMPMAST empmast "
				+ " JOIN [hpmpayroll].[dbo].EMPTRAN t  ON t.EMPNO = empmast.EMPNO "
				+ "WHERE T.srno = (SELECT  MAX(E2.SRNO)FROM [hpmpayroll].[dbo].EMPTRAN E2 "
				+ "WHERE E2.EMPNO = empmast.EMPNO AND E2.EFFDATE <= (select max(trndt)"
				+ "from [hpmpayroll].[dbo].paytran) AND ((empmast.STATUS = 'A' "
				+ "AND empmast.DOJ <= (select max(trndt) from [hpmpayroll].[dbo].paytran)) "
				+ "OR (empmast.STATUS = 'N' AND empmast.DOL >= (select max(trndt)"
				+ "from [hpmpayroll].[dbo].paytran)))) AND T.EMPNO IN (SELECT DISTINCT EMPNO "
				+ "FROM [hpmpayroll].[dbo].PAYTRAN  )	 ) "
				+ " order by Site_City ASC";
		// System.out.println(query);
		con = ConnectionManager.getConnectionTech();

		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmpOffBean emp = new EmpOffBean();

				emp.setPrj_srno(rs.getInt("Site_ID"));
				emp.setPrj_code(rs.getString("Project_Code"));
				emp.setPrj_name(rs.getString("site_Add"));
				emp.setSite_name(rs.getString("Site_Name"));
				emp.setSite_feild3(rs.getString("Site_Feild3"));

				/*
				 * System.out.print(rs.getInt("Site_ID"));
				 * System.out.print(rs.getString("Project_Code"));
				 * System.out.println(rs.getString("Site_Name"));
				 */

				list.add(emp);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TranBean> getEmpListForAttendancewithout_singleApproveEMP(String prjCode,
			String Date) {

		ArrayList<TranBean> list = new ArrayList<TranBean>();
		Connection con = null;
		ResultSet rs = null;

		// String query =
		// "SELECT EMPNO FROM EMPTRAN WHERE PRJ_CODE='"+prjCode+"'";
		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A'  and etn.PRJ_SRNO="+prjCode;

		// String query =
		// "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO='"+prjCode+"' and etn.SRNO = (select MAX(en.SRNO) from EMPTRAN en where en.PRJ_SRNO = '"+prjCode+"' and etn.EMPNO = en.EMPNO )";
		String query = "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where (emp.STATUS = 'A' and "
				+ " emp.DOJ<='"
				+ ReportDAO.EOM(Date)
				+ "'  "
				+ "   or emp.STATUS = 'N' and (emp.DOL>='"
				+ ReportDAO.BOM(Date)
				+ "' or emp.DOL=null ) ) AND  etn.PRJ_SRNO='"
				+ prjCode
				+ "'  AND "
				+ " etn.SRNO=(SELECT MAX(ET.SRNO) FROM EMPTRAN ET WHERE ET.EMPNO=emp.EMPNO  AND ET.EFFDATE<='"
				+ ReportDAO.EOM(Date) + "') and emp.empno not in(select distinct empno from ATTENDANCE_STATUS where TRANEVENT='SingleApprove' "
				+ " and att_month between '"+ReportDAO.BOM(Date)+"' and '"+ReportDAO.EOM(Date)+"' and appr_status='approved' "
				+ "and EMPNO in(select distinct e.EMPNO from empmast e,EMPTRAN t where e.empno=t.empno "
				+ "and t.SRNO=(select MAX(srno) from EMPTRAN t1 where t1.EMPNO=e.EMPNO) "
				+ "and t.PRJ_SRNO= '"+prjCode+"')) order by emp.EMPNO";
		System.out.println(query);
		con = ConnectionManager.getConnection();
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				TranBean trbn = new TranBean();
				trbn.setEMPNO(rs.getInt(1));
				list.add(trbn);
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

}