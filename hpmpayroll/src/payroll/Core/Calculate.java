package payroll.Core;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import payroll.DAO.ConnectionManager;
import payroll.DAO.EmpAttendanceHandler;
import payroll.DAO.Sal_DetailsHandler;
import payroll.DAO.TranHandler;

public class Calculate {

	public static int[][] TotAmt = new int[101][301];
	public static int[][] TotAmt1 = new int[101][1000];
	public static String[][] pAcNo = new String[301][4];
	static int cnt = 0;
	public static String v_gender="";

	public static int pay_cal(String BgnDate, String empList, String G_UserId) {
		String EmpStr = "";
		Statement st = null;
		ResultSet emp = null;
		float Wrk_Days = 0.0f;
		float Wrk_Amt = 0.0f;
		float onAmt1 = 0.0f;
		// System.out.println(empList);
		EmpStr = "select emp.*, t.acno, t.branch, t.grade from empmast emp, emptran t where t.empno = emp.empno and "
				+ "t.effdate = ( SELECT MAX(E2.EFFDATE) FROM EMPTRAN E2 WHERE e2.empno = emp.empno and E2.EFFDATE <= '"
				+ BgnDate
				+ "')"
				+ "and emp.empno in ("
				+ empList
				+ ") "
				// +"and emp.status = 'A' "
				+ "and ( dol is null or (dol >='"+ReportDAO.BOM(BgnDate)+"') )" + " order by emp.empno";

		System.out.println(EmpStr);
		Connection Cn = ConnectionManager.getConnection();
		//Connection Cn01 = ConnectionManager.getConnection();
		try {
			st = Cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			Statement st1 = Cn.createStatement();
			Statement st2 = Cn.createStatement();
			Statement st3 = Cn.createStatement();
			Statement st4 = Cn.createStatement();
			Statement del = Cn.createStatement();
			emp = st.executeQuery(EmpStr);
			System.out.println("Calculation Started....");
			if (!emp.next()) {
				System.out.println("NO employees found");
				return -1; // NO employees found
			}

			// Wrk_Days = getDays(BgnDate); // get maximum number of days in
			// month
			// ***** Chk date of joining
			emp.beforeFirst(); 

			/*
			 * if(emp.getString("DOJ") && ReportDAO.BOM(BgnDate) &&
			 * emp.getString("DOJ") <= ReportDAO.EOM(BgnDate)){ Wrk_Days =
			 * Wrk_Days - Integer.parseInt(emp.getString("DOJ").substring(0,1));
			 * }
			 */
			while (emp.next()) {			
	// akshay nikam for deduction of diff state ,IIts used to getting cuurrent site of employees. 
				Statement st08 = Cn.createStatement();
				ResultSet rs008 = null;
	//String prjn="select PRJ_SRNO from EMPTRAN where SRNO=(SELECT MAX(E2.SRNO)FROM [HCPAYROLL].[dbo].EMPTRAN E2 WHERE E2.EMPNO ="+emp.getInt("empno")+") and EMPNO="+emp.getInt("empno")+"";
				String prjn="select PRJ_SRNO,Site_State from EMPTRAN emp "+
				"left join HPM.dbo.Project_Sites prj on emp.PRJ_SRNO=Prj.SITE_ID where "+
				"SRNO=(SELECT MAX(E2.SRNO)FROM [DesaiPayroll].[dbo].EMPTRAN E2 WHERE E2.EMPNO ="+emp.getInt("empno")+")and EMPNO="+emp.getInt("empno")+"";
				System.out.println("int qu  "+prjn);//

			int prj=0;
			String state= null;//
            rs008 = st08.executeQuery(prjn);
            while (rs008.next()) {
            	prj=rs008.getInt("PRJ_SRNO");
            	state=rs008.getString("Site_State");
            	
            	
            	System.out.println("siteid  "+prj);
            	System.out.println("statestate  "+state);//
            	System.out.println("empno  "+emp.getInt("empno"));
                   }
            st08.close();
     // End here
				Wrk_Days = getDays(BgnDate);
				// System.out.println("1"+Wrk_Days);
				Statement st5 = Cn.createStatement();
				ResultSet rs7 = null;
			v_gender=emp.getString("gender").equalsIgnoreCase("M")?"MALE":"FEMALE";
				rs7 = st5.executeQuery("select " + Wrk_Days
						+ " - DAY(DOJ)+1 from empmast where empno ="
						+ emp.getInt("empno") + " and doj between cast('"
						+ ReportDAO.BOM(BgnDate) + "' as DATE) and cast('"
						+ ReportDAO.EOM(BgnDate) + "' as DATE)");
				if (rs7.next()) {
					/*
					 * int day = rs7.getInt(1); Wrk_Days = day;
					 */
					Wrk_Days = rs7.getFloat(1);
					// System.out.println(rs7.getFloat(1));
					// System.out.println("2"+Wrk_Days);
				}

				// === For Date of Leaving in same Month
				rs7 = st5.executeQuery("select " + Wrk_Days + " - (DAY(cast('"
						+ ReportDAO.EOM(BgnDate)
						+ "' as DATE))- DAY(DOL)) from empmast where empno ="
						+ emp.getInt("empno") + " and dol between cast('"
						+ ReportDAO.BOM(BgnDate) + "' as DATE) and cast('"
						+ ReportDAO.EOM(BgnDate) + "' as DATE)");
				if (rs7.next()) {
					/*
					 * int day = rs7.getInt(1); Wrk_Days = day;
					 */
					Wrk_Days = rs7.getFloat(1);
				}

				float Wrk_Days1= 0.0f;
				
				Wrk_Days1 = Wrk_Days ;
				
				int empno = emp.getInt("EMPNO");

			// deduction_insert(empno, G_UserId, BgnDate, Cn);
				//leave
				float leaveCount=0.0f;
				ResultSet resultSet=st5.executeQuery("select isnull (sum(days),0) as count from leavetran l" +
						" where l.EMPNO="+empno+""+
						 " and l.frmdt between"+ 
					"	'"+ ReportDAO.BOM(BgnDate)+"' and '"+ ReportDAO.EOM(BgnDate)+"' and "+ 
						"l.TRNTYPE='D'"+
						"and l.TODT between '"+ ReportDAO.BOM(BgnDate)+"' and '"+ ReportDAO.EOM(BgnDate)+"' "+
						"and l.STATUS='SANCTION' ");
				
				
				if(resultSet.next()){
					leaveCount=resultSet.getFloat("count");
				}
					
				

				String TrnStr = "SELECT * FROM PAYTRAN WHERE TRNCD=301 AND EMPNO="
						+ empno;

				ResultSet trn = st1.executeQuery(TrnStr);
				if (trn.next()) {
					float lopDays = trn.getFloat("INP_AMT");
					if (lopDays > Wrk_Days )
					{
						lopDays = Wrk_Days ;	
					}
					Wrk_Days = Wrk_Days - lopDays;
					
				
				
				if(leaveCount>0)
				{
					Wrk_Days = Wrk_Days + leaveCount ;
					if ( Wrk_Days > Wrk_Days1)
						Wrk_Days = Wrk_Days1;
				}
								if(leaveCount>0 && lopDays>=leaveCount && lopDays!=0)
								{
									lopDays=lopDays-leaveCount;
								}
								else if(leaveCount>0 && lopDays<=leaveCount && lopDays!=0)
								{
									lopDays=0;
								}
								
								else if(lopDays==0){
									lopDays=lopDays;
								}
								
					st2.executeUpdate("UPDATE PAYTRAN SET CAL_AMT=" + lopDays
							+ ",ARR_AMT=0, ADJ_AMT="+leaveCount+", NET_AMT=" + lopDays
							+ ",TRNDT='" + BgnDate + "',UPDDT='" + BgnDate
							+ "',STATUS='P' WHERE TRNCD=301 AND EMPNO=" + empno);
				}
				TrnStr = "SELECT PAYTRAN.* FROM PAYTRAN WHERE TRNCD=999 AND EMPNO="
						+ empno;
				trn = st1.executeQuery(TrnStr);
				if (trn.next()) {
					st2.executeUpdate("UPDATE PAYTRAN SET CAL_AMT=0, INP_AMT=0, ARR_AMT=0, ADJ_AMT=0, NET_AMT=0,STATUS='P' WHERE TRNCD=999 AND EMPNO="
							+ empno);
				}
				// String CdmStr =
				// "SELECT * FROM CDMAST WHERE GROSS_YN = 'Y' AND ISNULL(FREQUENCY,' ') <> 'C' ORDER BY TRNCD";
				String CdmStr = "(SELECT *,j='1' FROM CDMAST WHERE GROSS_YN = 'Y' AND ISNULL(FREQUENCY,' ') <> 'C' and TRNCD=199) "
						+ "union SELECT *,j='2' FROM CDMAST WHERE GROSS_YN = 'Y' AND ISNULL(FREQUENCY,' ') <> 'C' and TRNCD <> 199 order by j,SRNO,TRNCD";

				ResultSet CDM = st1.executeQuery(CdmStr);
				while (CDM.next()) {
					int trncd = CDM.getInt("TRNCD");
					
					int etype = CDM.getString("EMPTYPE") == null ? 0 : CDM
							.getInt("EMPTYPE");

					String naStr = "SELECT * FROM EMP_NA_CODE WHERE EMPNO = "
							+ empno + " AND TRNCD = " + trncd;
					ResultSet naRs = st3.executeQuery(naStr);
					if (naRs.next()) {
						if (naRs.getInt("FIXAMT") == 0) {
							Wrk_Amt = 0;
							TrnStr = "SELECT  PAYTRAN.* FROM PAYTRAN WHERE TRNCD = "
									+ trncd + " AND EMPNO = " + empno;
							trn = st4.executeQuery(TrnStr);
							if (trn.next()) {
								st2.executeUpdate("update PAYTRAN set cal_amt = "
										+ Wrk_Amt
										+ ", usrcode = '"
										+ G_UserId
										+ "',"
										+ " upddt = '"
										+ BgnDate
										+ "', trndt = '"
										+ BgnDate
										+ "',"
										+ "STATUS='P' "
										+ "where TRNCD = "
										+ trncd + " AND EMPNO = " + empno);
							}
						} else {
							Wrk_Amt = naRs.getInt("FIXAMT");
						}  
					} else {
						/*
						 * onAmt1=onAmount(trncd, empno, BgnDate,
						 * emp.getInt("DA_SCHEME"), Wrk_Days);
						 * Wrk_Amt=checkSlab(trncd, BgnDate, onAmt1,
						 * emp.getInt("DA_SCHEME"));
						 */

						//System.out.println("before onamout emptype " + etype);
						onAmt1 = onAmount(trncd, empno, BgnDate, etype,
								Wrk_Days, Cn);
						
						
						if(trncd==221){
							Wrk_Amt =(checkSlab(trncd, BgnDate, onAmt1,
									etype, empno, Cn));
							//System.out.println("THE ESIC : "+Wrk_Amt);
							Wrk_Amt=Wrk_Amt+0.49f;
							//System.out.println("Wrk_Amt :"+Wrk_Amt);
							Wrk_Amt=Math.round(Wrk_Amt);
						}
						else{
							Wrk_Amt = Math.round(checkSlab(trncd, BgnDate, onAmt1,
									etype, empno, Cn));
						}
						
					
					}
					// fpor Goa state profession tax is 0 202 is pt   & mlwf is =0. 
					if(trncd == 202 ||trncd == 211 )
					{
					   if(state.equalsIgnoreCase("Goa"))
					  {								
					 	Wrk_Amt = 0;
						}
					 }
					// GLWF for maharashtra employee's are 0 
				  
					  if (!state.equalsIgnoreCase("Goa"))
				       {
						 // System.out.println("im not in goa");
						  if(trncd == 238)
				    
					   {
						Wrk_Amt = 0;
						}
					 }
					
					if ((CDM.getString("FREQUENCY") == null ? "" : (CDM
							.getString("FREQUENCY"))).equalsIgnoreCase("H")) {
						
						if (!(getMonth(BgnDate) == 6 || getMonth(BgnDate) == 12)) {
							Wrk_Amt = 0;
						}
						// akshay nikam
					
						
						
					 /*ph sir for mlwf =0
						if ( (getMonth(BgnDate) == 6 || getMonth(BgnDate) == 12)) {
							if(trncd == 211)
							 {				
								if ( prj==38 || prj == 39 ||prj == 41 || prj== 108 ||prj == 72 )
								{
									Wrk_Amt = 0;
								}
							
						}
						}*/
						
					}
					if ((CDM.getString("FREQUENCY") == null ? "" : (CDM
							.getString("FREQUENCY"))).equalsIgnoreCase("B")) {
						TotAmt[emp.getInt("BRANCH")][CDM.getInt("TRNCD")] = TotAmt[emp
								.getInt("BRANCH")][CDM.getInt("TRNCD")]
								+ Math.round(Wrk_Amt);
						Wrk_Amt = 0;
					}
					if (Wrk_Amt != 0) {
						TrnStr = "SELECT PAYTRAN.* FROM PAYTRAN WHERE TRNCD = "
								+ trncd + " AND  EMPNO = " + empno;
						trn = st4.executeQuery(TrnStr);
						if (trn.next()) {
							st3.executeUpdate("UPDATE PAYTRAN SET CAL_AMT = "
									+ Wrk_Amt + ", USRCODE = '" + G_UserId
									+ "', UPDDT = '" + BgnDate
									+ "' , TRNDT = '" + BgnDate
									+ "' ,STATUS='P' " + "WHERE  TRNCD = "
									+ trncd + " AND EMPNO = " + empno);
						} else {
							st3.executeUpdate("INSERT INTO PAYTRAN (TRNDT, EMPNO, TRNCD, CAL_AMT, USRCODE, UPDDT, ARR_AMT, ADJ_AMT, NET_AMT, SRNO, CF_SW,STATUS) values("
									+ "'"
									+ BgnDate
									+ "',"
									+ empno
									+ ","
									+ trncd
									+ ","
									+ Wrk_Amt
									+ ",'"
									+ G_UserId
									+ "','" + BgnDate + "',0,0,0,0,'','P')");
						}
						TrnStr = "SELECT  PAYTRAN.* FROM PAYTRAN WHERE TRNCD = 999 AND EMPNO = "
								+ empno;
						if (trncd < 200) {
							trn = st4.executeQuery(TrnStr);
							if (trn.next()) {
								st3.executeUpdate("UPDATE PAYTRAN SET CAL_AMT = CAL_AMT + "
										+ Wrk_Amt
										+ " WHERE TRNCD = 999 AND EMPNO = "
										+ empno);
							} else {
								st3.executeUpdate("INSERT INTO PAYTRAN (TRNDT, EMPNO, TRNCD, CAL_AMT, USRCODE, UPDDT, ARR_AMT, ADJ_AMT, NET_AMT, SRNO, CF_SW,STATUS) values("
										+ "'"
										+ BgnDate
										+ "',"
										+ empno
										+ ",999,"
										+ Wrk_Amt
										+ ",'"
										+ G_UserId
										+ "','" + BgnDate + "',0,0,0,0,'','P')");
							} // else close
						} // if(trncd < 200)
					} // if(Wrk_Amt!=0)
					else {
						
						
						
						
						TrnStr = "SELECT  PAYTRAN.* FROM PAYTRAN WHERE TRNCD = "
								+ trncd + " AND  EMPNO = " + empno;
						ResultSet strn = st4.executeQuery(TrnStr);
						if (strn.next()) {
							
							
							st3.executeUpdate("UPDATE PAYTRAN SET CAL_AMT = "
									+ Wrk_Amt + ", NET_AMT = " + Wrk_Amt
									+ ", USRCODE = '" + G_UserId
									+ "', UPDDT = '" + BgnDate
									+ "' , TRNDT = '" + BgnDate
									+ "',STATUS='P'" + "  WHERE   TRNCD = "
									+ trncd + " AND  EMPNO = " + empno);
							
						}
						else
						{
							
							//ADDED ON 26-10-2015 FOR deductions code as zero amount 
							if(trncd >203 && trncd<299)
							{
							st3.executeUpdate("INSERT INTO PAYTRAN (TRNDT, EMPNO, TRNCD, CAL_AMT, USRCODE, UPDDT, ARR_AMT, ADJ_AMT, NET_AMT, SRNO, CF_SW,STATUS) values("
									+ "'"
									+ BgnDate
									+ "',"
									+ empno
									+ ","
									+ trncd
									+ ","
									+ Wrk_Amt
									+ ",'"
									+ G_UserId
									+ "','" + BgnDate + "',0,0,0,0,'','P')");
							}
							
						}
						
						
						
					}

					if (trncd > 200 && trncd < 300) {
						pAcNo[trncd][1] = CDM.getString("SUBSYS");
						pAcNo[trncd][2] = "" + CDM.getInt("ACNO");
					}
					del.executeUpdate("update paytran set status = 'P',TRNDT='"
							+ BgnDate + "',UPDDT='" + BgnDate
							+ "' where trncd in (127,135) and empno = " + empno);
					//del.executeUpdate("delete from PAYTRAN where INP_AMT= 0 and CAL_AMT= 0 and NET_AMT = 0 and CF_SW <> '*'and trncd <> 999 ");
				} // while 2 close
				
				// for harsh construnction EPF calculation change for 15000 
				
				Statement epf_cal=Cn.createStatement();
				String epf_cal_query="update PAYTRAN 	set " +
									"CAL_AMT=((select p1.cal_amt from PAYTRAN p1 where p1.EMPNO=st.EMPNO and  p1.TRNCD=201 and  p1.TRNDT between '"+ReportDAO.BOM(BgnDate)+"' and '"+ReportDAO.EOM(BgnDate)+"')" +
									" - ( select p2.cal_amt from PAYTRAN p2 where p2.EMPNO=st.EMPNO and p2.TRNCD=232  and  p2.TRNDT between '"+ReportDAO.BOM(BgnDate)+"' and '"+ReportDAO.EOM(BgnDate)+"'))   	" +
											"from PAYTRAN st	  where  TRNCD=231  and TRNDT between '"+ReportDAO.BOM(BgnDate)+"' and '"+ReportDAO.EOM(BgnDate)+"'	" +
											"and EMPNO in(select EMPNO from PAYTRAN 	where  EMPNO="+emp.getInt("EMPNO")+" and  TRNCD=201  and TRNDT between '"+ReportDAO.BOM(BgnDate)+"' and '"+ReportDAO.EOM(BgnDate)+"')";
				
				epf_cal.executeUpdate(epf_cal_query);
				// changes end
				
				
				
				
				
				

			} // while 1 close

			st2.close();
			st3.close();
			st4.close();

		} catch (Exception e) {
			e.printStackTrace();
			return -2; // Some Error or Exception has occurred
		}
		int flag =Update_Tran(emp, G_UserId, BgnDate, Cn);
		if (flag != 0) {
			return flag; // Salary of empno= flag has gone negative
		}
		TranHandler TH = new TranHandler();
		TH.updateFinalizeStatus(BgnDate);
		System.out.println("Process of Payroll Calculation is completed");
		try {
			
			Cn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// ------------- Method to check for ChkSlab ------------------------------
	public static float checkSlab(int trncd, String dt, float WrkAmt,
			int empType, int empno, Connection Cn) {
		float result = 0.00f;
		float slabAmt = 0.00f;
		EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
		//String monthFeb=empAttendanceHandler.getServerDate().substring(3,6);
		String monthFeb=dt.substring(3,6);
		//System.out.println("for feb"+monthFeb);
		//int yearFeb=Integer.parseInt(empAttendanceHandler.getServerDate().substring(7,11));
		int yearFeb=Integer.parseInt(dt.substring(7,11));
		int date=0;
		/*
		 * if(trncd == 202 || trncd == 201 || trncd == 221) { empType = 0; }
		 */
		
		
		
		String vempType = "" + empType;
		String SlbStr = "SELECT * FROM SLAB WHERE EMP_CAT = " + empno
				+ " AND TRNCD = " + trncd + " AND EFFDATE = "
				+ "( SELECT MIN(S.EFFDATE) FROM SLAB S WHERE S.EMP_CAT = "
				+ empno + " AND TRNCD = " + trncd + " and effdate >= '" + dt
				+ "' ) and " + +WrkAmt
				+ " BETWEEN  FRMAMT AND TOAMT ORDER BY SRNO ";
		try {
			Statement st0 = Cn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st0.executeQuery(SlbStr);
			if (!rs.next()) {
				st0.close();

			} else {
				vempType = "" + empno;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SlbStr = "SELECT * FROM SLAB WHERE EMP_CAT = " + vempType
				+ " AND TRNCD = " + trncd + " AND EFFDATE = "
				+ "( SELECT MIN(S.EFFDATE) FROM SLAB S WHERE S.EMP_CAT = "
				+ vempType + " AND TRNCD = " + trncd + " and effdate >= '" + dt
				+ "' ) and " + WrkAmt
				+ " BETWEEN  FRMAMT AND TOAMT ORDER BY SRNO ";

		if (trncd == 199 || trncd == 127) {
			result = WrkAmt;
			return result;
		}

		if (WrkAmt == 0) {
			return 0;
		}
		
		
		
		try {
			Statement st = Cn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet Slb = st.executeQuery(SlbStr);
			if (!Slb.next()) {
				System.out.println("No Slab Found into chk slab");
				return WrkAmt;
			}
			Slb.beforeFirst();
			if (Slb.next()) {
			
				
				if ((Slb.getString("FIXAMT") == null ? 0 : Slb.getInt("FIXAMT")) == 0
						&& (Slb.getString("PER") == null ? 0 : Slb
								.getFloat("PER")) == 0
						&& (Slb.getString("MINAMT") == null ? 0 : Slb
								.getInt("MINAMT")) == 0
						&& (Slb.getString("MAXAMT") == null ? 0 : Slb
								.getInt("MAXAMT")) == 0) {
					return 0;
				}
				if (Slb.getInt("FIXAMT") != 0) {
					//System.out.println("FIXED AMT +++++++====="+Slb.getInt("FIXAMT"));
					result = Slb.getInt("FIXAMT");
					
					if(monthFeb.equalsIgnoreCase("Feb")){
				    	if (Slb.getInt("TRNCD") == 202
							
							&& result > 175)
						result = 300;
					}
					java.util.Date convertedDate=null;
					java.util.Date startdate=null;
					SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yyyy");
					
			        convertedDate = (java.util.Date) sourceFormat.parse(dt);
			        startdate= (java.util.Date) sourceFormat.parse("01-Mar-2015");  // for rule since 1-march-2015

					if (v_gender.equalsIgnoreCase("FEMALE") && trncd==202 && WrkAmt<=10000 && convertedDate.after(startdate))
					{
						result=0;
					}
					System.out.println("resultttttttt"+result);
					//System.out.println("into slab= ");
					//System.out.println("trncd ="+trncd);
					//System.out.println("RETURN FIXED AMT======"+result);
					return result;
				}
				slabAmt = WrkAmt;

				if ((Slb.getString("PER") == null ? 0 : Slb.getFloat("PER")) != 0) {
					
					slabAmt = WrkAmt * Slb.getFloat("PER") / 100;
					
					
				}
				if ((Slb.getString("MINAMT") == null ? 0 : Slb.getInt("MINAMT")) != 0) {
					if (slabAmt < Slb.getInt("MINAMT")) {
						slabAmt = Slb.getInt("MINAMT");
					}
				}
				if ((Slb.getString("MAXAMT") == null ? 0 : Slb.getInt("MAXAMT")) != 0) {
					if (slabAmt > Slb.getInt("MAXAMT")) {
						slabAmt = Slb.getInt("MAXAMT");
					}
				}
				
				
				

				result = slabAmt;
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return result;
	}

	// ------------- Method to check for Onamt ------------------------------
	public static float onAmount(int trncd, int empno, String BgnDate,
			int empType, float WrkDays, Connection Cn) // added one parameter
														// WrkDays
	{
		float WrkAmt = 0.00f;
		float Basic = 0.00f;
		float result = 0.00f;
		Statement st = null,st11 = null;
		ResultSet rs = null,rs1 = null;
		if (trncd == 199 || trncd == 127) {
			try {
				st = Cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				if (trncd == 199) {
					rs = st.executeQuery("SELECT  INP_AMT FROM PAYTRAN WHERE TRNCD  = 199 AND EMPNO = "
							+ empno);
				} else {
					rs = st.executeQuery("SELECT  INP_AMT FROM PAYTRAN WHERE TRNCD  = 127 AND EMPNO = "
							+ empno);
				}

				if (rs.next()) {
					Basic = rs.getFloat("INP_AMT") * WrkDays / getDays(BgnDate);
					result = Basic;
					st.close();
					return result;
				}
				st.close();
				
				
				
				
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try
		{
			if(trncd==201||trncd==202||trncd==221 ||trncd==231||trncd==232||trncd==233||trncd==234||trncd==235||trncd==236)
			{
		st11 = Cn.createStatement();
		rs1 = st11.executeQuery("SELECT  * FROM CTCDISPLAY WHERE TRNCD  = 101 AND EMPNO = "
				+ empno);
		
		int pf=0,pt=0,esic=0;
		while(rs1.next())
		{
		pf=rs1.getInt("pf");
		esic=rs1.getInt("esic");
		pt=rs1.getInt("pt");
		}
		if((trncd==201 && pf==0)||(trncd==231 && pf==0)||(trncd==232 && pf==0)||(trncd==233 && pf==0)
||(trncd==234 && pf==0)||(trncd==235 && pf==0))		{
			result = 0.00f;
			st11.close();
			return result;
		}
		else if(trncd==202 && pt==0)
		{
			result = 0.00f;
			st11.close();
			return result;
		}
		else if((trncd==221 && esic==0)||(trncd==236 && esic==0))
		{
			result = 0.00f;
			st11.close();
			return result;
		}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String OmtStr = "";
		String vempType = "" + empType;
		OmtStr = "";

		OmtStr = "SELECT * FROM ONAMT WHERE EMP_CAT = " + empno
				+ " AND TRNCD = " + trncd;

		try {
			Statement st0 = Cn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = st0.executeQuery(OmtStr);
			if (!rs.next()) {
				// System.out.println("into !rs.next() ");
				st0.close();

			} else {
				// System.out.println("into !rs.next() else ");
				vempType = "" + empno;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		OmtStr = "SELECT * FROM ONAMT WHERE EMP_CAT = " + vempType
				+ " AND TRNCD = " + trncd;
		try {
			Statement st0 = Cn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = st0.executeQuery(OmtStr);
			if (!rs.next()) {
				st0.close();
				return 0;
			}
			rs.beforeFirst();
			ResultSet trn = null;
			while (rs.next()) {
				Statement st1 = Cn.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				trn = st1.executeQuery("SELECT * FROM PAYTRAN WHERE TRNCD="
						+ rs.getInt("ONAMTCD") + " AND EMPNO=" + empno);
				if (trn.next()) {
					/*
					 * WrkAmt = WrkAmt + (trn.getString("CAL_AMT") == null ? trn
					 * .getFloat("INP_AMT") : trn.getInt("CAL_AMT"));
					 */
					if (trncd == rs.getInt("ONAMTCD")) {
						// System.out.println("bgn"+getDays(BgnDate));
						//WrkAmt = WrkAmt	+ (trn.getString("INP_AMT") == null ? trn.getInt("CAL_AMT"):trn.getString("CAL_AMT")==null?trn.getInt("INP_AMT"):trn.getInt("cal_amt"));
						if(rs.getString("AMT_TYPE").equalsIgnoreCase("C"))
						{
						WrkAmt = WrkAmt	+ (trn.getString("INP_AMT") == null	? trn.getInt("CAL_AMT") : trn.getInt("INP_AMT"));
						WrkAmt = (WrkAmt * WrkDays) / getDays(BgnDate);
						}
						else
						{
							WrkAmt = WrkAmt	+ (trn.getString("INP_AMT") == null	? trn.getInt("CAL_AMT") : trn.getInt("INP_AMT"));
							
						}
						
					}
					/*
					 * else if(trncd == 202) { WrkAmt = WrkAmt +
					 * trn.getInt("INP_AMT"); }
					 */
					else {
						
						/*WrkAmt = WrkAmt
								+ (trn.getString("CAL_AMT") == null
										|| trn.getInt("CAL_AMT") == 0 ? trn
										.getInt("INP_AMT") : trn
										.getInt("CAL_AMT"));*/
						
						WrkAmt = WrkAmt	+ (trn.getString("CAL_AMT") == null	? trn.getInt("INP_AMT") : trn.getInt("CAL_AMT"));
						
						
						// WrkAmt = (WrkAmt * WrkDays)/getDays(BgnDate);
						
						
							
					 }					
				}				
				st1.close();
			}
			

			
			
			
			
			result = WrkAmt;
			st0.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("work amt" + result);
		return result;
	}

	public static int Update_Tran(ResultSet emp, String G_UserId,
			String BgnDate, Connection Cn) {
		ResultSet ps = null;
		String psstr = "";
		int NetTot = 0;
		int WrkAmt = 0;
		String Str1 = "";
		int Tot_allow = 0;
		int Tot_Ded = 0;
		int loan_Installment=0;
		int advanceInstallment=0;
		boolean negFlag=false;
		try {
			Statement stUP = Cn.createStatement();
			Statement st = Cn.createStatement();
			Statement st1 = Cn.createStatement();
			Statement st2 = Cn.createStatement();
			Statement st3 = Cn.createStatement();
			Sal_DetailsHandler SDH = new Sal_DetailsHandler();
			emp.beforeFirst();

			
			while (emp.next()) {
				Tot_allow = 0;
				Tot_Ded = 0;
				String TrnStr = "select  P.*,c.CF_SW from PAYTRAN p,cdmast c  where p.empno = "
						+ emp.getInt("EMPNO")
						+ " and p.trncd > 100 and p.trncd <= 295 and c.trncd = p.trncd and c.gross_yn='Y' "
						+ "and c.TRNCD not in(198,136,137,231,232,233,234,235,236,237)" +
						" " +
						" ORDER BY TRNCD";

				ResultSet trn = st.executeQuery(TrnStr);
				while (trn.next()) {
					
					int trncd = trn.getInt("TRNCD");
					if(trncd==227){
						System.out.println("ON CALCULATION"+trncd);
						String queryForLoan="if exists(select monthly_install from loan_detail where start_date between '"+ReportDAO.BOM(BgnDate)+"'" 
							+	" and '"+ReportDAO.EOM(BgnDate)+"' and EMPNO="+emp.getInt("EMPNO")+" and Active='SANCTION' and loan_code="+trncd+" )"
							+	"update PAYTRAN set"
							+" INP_AMT=(select sum(monthly_install)as monthly_install from loan_detail where start_date between " 
							+"'"+ReportDAO.BOM(BgnDate)+"' and '"+ReportDAO.EOM(BgnDate)+"'" 
							+" and EMPNO="+emp.getInt("EMPNO")+" and Active='SANCTION' and loan_code="+trncd+")"
							+",CAL_AMT=ADJ_AMT+(select sum(monthly_install)as monthly_install from loan_detail where " 
							+"start_date between '"+ReportDAO.BOM(BgnDate)+"' "
							+"and '"+ReportDAO.EOM(BgnDate)+"' and EMPNO="+emp.getInt("EMPNO")+" and Active='SANCTION' and loan_code="+trncd+"), "
							/*+"NET_AMT=(select monthly_install from loan_detail where start_date " 
							+"between '"+ReportDAO.BOM(BgnDate)+"' and '"+ReportDAO.EOM(BgnDate)+"'" 
							+"and EMPNO="+emp.getInt("EMPNO")+" and Active='SANCTION') ,  "*/
							
							/*+"  ADJ_AMT= ADJ_AMT+(select monthly_install from loan_detail where start_date " 
							+"between '"+ReportDAO.BOM(BgnDate)+"' and '"+ReportDAO.EOM(BgnDate)+"'" 
							+"and EMPNO="+emp.getInt("EMPNO")+" and Active='SANCTION') ,  "*/
							+"CF_SW='0',USRCODE="+G_UserId+",UPDDT=GETDATE(),STATUS='P' WHERE EMPNO="+emp.getInt("EMPNO")+"" 
							+" AND  TRNCD="+trncd+" ";
						System.out.println(queryForLoan);
						st1.executeUpdate(queryForLoan);
					}
					
					
			/*		ResultSet test=st.executeQuery("select * from paytran where empno=370 and trncd=226");
					System.out.println("inp_amt"+test.getFloat("inp_amt"));
					System.out.println("cal_amt"+test.getFloat("cal_amt"));
					System.out.println("net_amt"+test.getFloat("net_amt"));
					*/
					
					
					WrkAmt = 0;
					psstr = "select trncd  from slab where trncd = " + trncd;
//1			
					System.out.println("1st trncd "+trncd);

					ps = st1.executeQuery(psstr);
					if (ps.next()) {
						if ((trn.getString("CAL_AMT") == null ? 0 : trn
								.getInt("CAL_AMT")) == 0
								&& (trn.getString("INP_AMT") == null ? 0 : trn
										.getInt("INP_AMT")) != 0) {
							if (trncd != 5000) {
								WrkAmt = (trn.getString("INP_AMT") == null ? 0
										: trn.getInt("INP_AMT"))
										+ (trn.getString("ADJ_AMT") == null ? 0
												: trn.getInt("ADJ_AMT"))
										+ (trn.getString("ARR_AMT") == null ? 0
												: trn.getInt("ARR_AMT"));
								
								if(trn.getString("CF_SW").equalsIgnoreCase("*") && trn.getInt("CAL_AMT")<=0)
								{
									WrkAmt=0;
									stUP.executeUpdate("update PAYTRAN set cal_amt =0, net_amt =0  where  TRNCD = "
											+ trncd
											+ " AND  EMPNO = "
											+ emp.getInt("EMPNO"));
								}
								else
								{
								stUP.executeUpdate("update PAYTRAN set cal_amt = "
										+ (trn.getString("INP_AMT") == null ? 0
												: trn.getInt("INP_AMT"))
										+ ", net_amt = "
										+ Math.round(WrkAmt)
										+ " where  TRNCD = "
										+ trncd
										+ " AND  EMPNO = "
										+ emp.getInt("EMPNO"));
								}
							} else {
								WrkAmt = 0;
								stUP.executeUpdate("update PAYTRAN set cal_amt = 0 , net_amt = "
										+ Math.round(WrkAmt)
										+ " where  TRNCD = "
										+ trncd
										+ " AND  EMPNO = "
										+ emp.getInt("EMPNO"));
							}
						} else {
							WrkAmt = (trn.getString("CAL_AMT") == null ? 0
									: trn.getInt("CAL_AMT"))
									+ (trn.getString("ADJ_AMT") == null ? 0
											: trn.getInt("ADJ_AMT"))
									+ (trn.getString("ARR_AMT") == null ? 0
											: trn.getInt("ARR_AMT"));
							stUP.executeUpdate("update PAYTRAN set net_amt = "
									+ WrkAmt + " where  TRNCD = " + trncd
									+ " AND  EMPNO = " + emp.getInt("EMPNO"));
						}

						if (trncd < 200 && (trncd != 199 && trncd != 127)) {
							Tot_allow = Tot_allow + WrkAmt;
						} else {
							if (trncd != 199 && trncd != 127) {
								Tot_Ded = Tot_Ded + WrkAmt;
								TotAmt[emp.getInt("BRANCH")][trn
										.getInt("trncd")] = TotAmt[emp
										.getInt("BRANCH")][trn.getInt("trncd")]
										+ WrkAmt;
								//System.out.println("code"+trn.getInt("trncd")+"deduction"+WrkAmt);
							}
						}
					} else {
						if ((trn.getString("CAL_AMT") == null ? 0 : trn
								.getInt("CAL_AMT")) == 0
								&& (trn.getString("INP_AMT") == null ? 0 : trn
										.getInt("INP_AMT")) != 0) {
							WrkAmt = (trn.getString("INP_AMT") == null ? 0
									: trn.getInt("INP_AMT"))
									+ (trn.getString("ADJ_AMT") == null ? 0
											: trn.getInt("ADJ_AMT"))
									+ (trn.getString("ARR_AMT") == null ? 0
											: trn.getInt("ARR_AMT"));
  //2
							System.out.printf("2nd amount coloumn wise "+trn.getString("INP_AMT"),"  "+trn.getInt("ADJ_AMT"));
							System.out.println("WrkAmt 1 "+WrkAmt);

							stUP.executeUpdate("update PAYTRAN set cal_amt = "
									+ (trn.getString("INP_AMT") == null ? 0
											: trn.getInt("INP_AMT"))
									+ ", net_amt = " + Math.round(WrkAmt)
									+ " where TRNCD = " + trncd
									+ " AND  EMPNO = " + emp.getInt("EMPNO"));
						} else {
							WrkAmt = (trn.getString("CAL_AMT") == null ? 0
									: trn.getInt("CAL_AMT"))
									+ (trn.getString("ADJ_AMT") == null ? 0
											: trn.getInt("ADJ_AMT"))
									+ (trn.getString("ARR_AMT") == null ? 0
											: trn.getInt("ARR_AMT"));
							if(trncd!=226 && trncd!=227){
							stUP.executeUpdate("update PAYTRAN set net_amt = "
									+ WrkAmt + " where  TRNCD = " + trncd
									+ " AND  EMPNO = " + emp.getInt("EMPNO"));
							}
						}					
						if (trncd < 200 && (trncd != 199 && trncd != 127)) {
							Tot_allow = Tot_allow + WrkAmt;
						} else {
							if (trncd != 199 && trncd != 127) {
								String checkingLoanNetAmt="select net_amt from paytran where empno="+emp.getInt("EMPNO")+""
										+"and trncd=226";
								System.out.println("checkingLoanNetAmt"+checkingLoanNetAmt);
								ResultSet resultSet= st3.executeQuery(checkingLoanNetAmt);
								if (resultSet.next()) {
									loan_Installment=resultSet.getInt("net_amt");
 //////3							
									System.out.println("loan_Installment"+loan_Installment);
									System.out.println("for first timeAAAAAAA "+loan_Installment);
								}
							//	System.out.println("tot_ded"+Tot_Ded);
								//System.out.println("WrkAmt"+WrkAmt);
								//System.out.println("loan_Installment"+loan_Installment);
								if(trncd==226)
								    Tot_Ded = Tot_Ded +loan_Installment;
								else
									Tot_Ded = Tot_Ded + WrkAmt;
								
								
								
								//System.out.println("TOTAL DEDUCTION IS"+Tot_Ded);
								TotAmt[emp.getInt("BRANCH")][trn
										.getInt("trncd")] = TotAmt[emp
										.getInt("BRANCH")][trn.getInt("trncd")]
										+ WrkAmt;
							}
						}
					}
					
				}	
				TrnStr = "select  PAYTRAN.* from PAYTRAN where trncd = 999 and empno = "
						+ emp.getInt("empno");
				trn = st.executeQuery(TrnStr);
				
				if (trn.next()) {
					if ((Tot_allow - Tot_Ded) < 0) {
						System.out
								.println("Employee Number "
										+ emp.getInt("empno")
										+ " salary goes Negative Please check it and process paycal again");
						// return emp.getInt("empno");
						negFlag = true;
					}
					Str1 = "update PAYTRAN set inp_amt = " + Tot_allow
							+ ", cal_amt = " + Tot_Ded + ", adj_amt = "
							+ (Tot_allow - Tot_Ded) + ", net_amt = "
							+ (Tot_allow - Tot_Ded) + ", usrcode = '"
							+ G_UserId + "', upddt = '" + BgnDate
							+ "' , TRNDT = '" + BgnDate
							+ "' ,STATUS='P' where  trncd = 999 and empno = "
							+ emp.getInt("empno");

					stUP.executeUpdate(Str1);
					if (emp.getInt("BRANCH") == 1) {
						NetTot = NetTot + Tot_allow - Tot_Ded;
					}
				} else {
					if ((Tot_allow - Tot_Ded) < 0) {
						System.out
								.println("Employee Number "
										+ emp.getInt("empno")
										+ " salary goes Negative Please check it and process paycal again");
						// return emp.getInt("empno");
						negFlag = true;
					}

					Str1 = "insert into PAYTRAN (trndt, empno, trncd, inp_amt, cal_amt,adj_amt, net_amt, usrcode, upddt,arr_amt,srno,cf_sw,STATUS) values ("
							+ "'"
							+ BgnDate
							+ "',"
							+ emp.getInt("empno")
							+ ", 999,"
							+ Tot_allow
							+ " ,"
							+ Tot_Ded
							+ " ,"
							+ (Tot_allow - Tot_Ded)
							+ " ,"
							+ (Tot_allow - Tot_Ded)
							+ " , "
							+ " '"
							+ G_UserId
							+ "','" + BgnDate + "',0,0,'','P')";
					stUP.executeUpdate(Str1);
					if (emp.getInt("BRANCH") == 1) {
						NetTot = NetTot + Tot_allow - Tot_Ded;
					}
				}
				if (negFlag)
				{
					SDH.addSalDetails(emp.getInt("empno"),
							BgnDate.substring(3), ReportDAO.EOM(BgnDate),
							"NEGATIVE", Cn);
				System.out.println("im inn if negativve");
				}
				else
				{
					SDH.addSalDetails(emp.getInt("empno"),
							BgnDate.substring(3), ReportDAO.EOM(BgnDate),
							"PROCESSED", Cn);
				System.out.println("im inn if Processedd ");
				}

				
			
			} // Closed Outer While
			/*int i = 0;
			String CdmStr = "select * from cdmast where frequency = 'C' ";

			ResultSet CDM = st1.executeQuery(CdmStr);
			while (CDM.next()) {
				for (i = 0; i <= 20; i++) {
					String OmtStr = "select * from onamt where trncd = "
							+ CDM.getInt("trncd");

					ResultSet Omt = st2.executeQuery(OmtStr);
					if (Omt.next()) {
						String SlbStr = "select * from slab where trncd = "
								+ CDM.getInt("trncd")
								+ " and effdate = ( select min(effdate) from slab where trncd = "
								+ CDM.getInt("trncd") + " and effdate >= '"
								+ BgnDate + "') and " + WrkAmt
								+ " between  frmamt and toamt order by srno";

						ResultSet Slb = st3.executeQuery(SlbStr);
						if (Slb.next()) {
							if ((Slb.getString("PER") == null ? 0 : Slb
									.getInt("PER")) != 0
									&& TotAmt[i][Omt.getInt("ONAMTCD")] != 0) {
								TotAmt[i][CDM.getInt("TRNCD")] = Math
										.round(TotAmt[i][Omt.getInt("ONAMTCD")]
												* Slb.getFloat("PER") / 100);
							}
						}

					}
				}
				pAcNo[CDM.getInt("TRNCD")][1] = CDM.getString("SUBSYS");
				pAcNo[CDM.getInt("TRNCD")][2] = CDM.getString("acno");
			}

		*/	/*if (NetTot != 0) {
				String HopStr = "select * from hopost where trncd = 999 and brcd = 1 ";
				ResultSet Hop = st3.executeQuery(HopStr);
				if (Hop.next()) {
					stUP.executeUpdate("update hopost set subsys_cd = 'MB', ac_no = 0 , amount = "
							+ NetTot);
				} else {
					stUP.executeUpdate("insert into hopost values (1,999,0,'MB',0,"
							+ NetTot + ",'C')");
				}
			}*/
			st.close();
			st1.close();
			st2.close();
			st3.close();
			stUP.close();
			//Ho_Post(Cn);
			//Br_Post(BgnDate, Cn);
			Deduction_Cal(BgnDate, Cn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return negFlag==true?-3:0;
	}

	public static void Ho_Post(Connection Cn) {
		try {
			Statement st = Cn.createStatement();
			st.executeUpdate("truncate table autopost");
			String CdmStr = "select * from cdmast where trncd > 200 and trncd < 300 order by trncd ";
			ResultSet CDM = st.executeQuery(CdmStr);
			while (CDM.next()) {
				if (!(CDM.getString("SUBSYS") == null ? "" : CDM
						.getString("SUBSYS")).equals("")) {
					pAcNo[CDM.getInt("TRNCD")][1] = CDM.getString("SUBSYS");
					pAcNo[CDM.getInt("TRNCD")][2] = CDM.getString("acno");
					pAcNo[CDM.getInt("TRNCD")][3] = CDM.getString("FREQUENCY");
				}
			}
			Statement stUP = Cn.createStatement();
			for (int i = 1; i <= 100; i++) {
				for (int j = 201; j <= 300; j++) {
					if (TotAmt[i][j] != 0) {
						String HopStr = "select  h.* from hopost h where trncd = "
								+ j + " and brcd = " + i;
						ResultSet Hop = st.executeQuery(HopStr);
						if (Hop.next()) {
							stUP.executeUpdate("update hopost set subsys_cd = '"
									+ (pAcNo[j][1] == null ? "" : pAcNo[j][1])
									+ "', ac_no = "
									+ Integer
											.parseInt((pAcNo[j][2] == null ? "0"
													: pAcNo[j][2]))
									+ ", amount = "
									+ TotAmt[i][j]
									+ " where trncd = "
									+ j
									+ " and brcd = "
									+ i);
						} else {
							stUP.executeUpdate("insert into hopost values ("
									+ i
									+ ", "
									+ j
									+ ", 0, '"
									+ (pAcNo[j][1] == null ? "" : pAcNo[j][1])
									+ "', "
									+ Integer
											.parseInt((pAcNo[j][2] == null ? "0"
													: pAcNo[j][2])) + ", "
									+ TotAmt[i][j] + ", 'C')");
						}

						if ((pAcNo[j][3] == null ? "" : pAcNo[j][3])
								.equalsIgnoreCase("C")) {
							stUP.executeUpdate("insert into AUTOPOST  values (0,"
									+ i
									+ ", "
									+ j
									+ ", 0, '"
									+ (pAcNo[j][1] == null ? "" : pAcNo[j][1])
									+ "', "
									+ Integer
											.parseInt((pAcNo[j][2] == null ? "0"
													: pAcNo[j][2]))
									+ ", "
									+ TotAmt[i][j] + ", 'D',0)");
						}
					}
				}
			}
			st.close();
			stUP.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Br_Post(String BgnDate, Connection Cn) {
		String TrnStr = "select trn.*, t.branch, t.acno from PAYTRAN trn, emptran t where trn.trncd > 200 and trn.trncd < 1000 and t.empno = trn.empno and "
				+ " t.effdate = ( SELECT MAX(E2.EFFDATE) FROM EMPTRAN E2 WHERE e2.empno = trn.empno and E2.EFFDATE <= '"
				+ BgnDate + "')" + " order by trn.empno, trn.trncd";

		try {
			Statement st = Cn.createStatement();
			Statement stUP = Cn.createStatement();
			ResultSet trn = st.executeQuery(TrnStr);
			while (trn.next()) {
				TotAmt1[trn.getInt("BRANCH")][trn.getInt("TRNCD")] = TotAmt1[trn
						.getInt("BRANCH")][trn.getInt("TRNCD")]
						+ trn.getInt("NET_AMT");
				if (trn.getInt("TRNCD") == 999) {
					String Str1 = "insert into autopost values("
							+ trn.getInt("empno") + "," + trn.getInt("BRANCH")
							+ ",999,0,'SB','" + trn.getString("acno") + "',"
							+ trn.getInt("NET_AMT") + ",'C',0 )";
					stUP.executeUpdate(Str1);
				}
			}

			for (int i = 0; i <= 20; i++) {
				for (int j = 201; j <= 999; j++) {
					if (TotAmt1[i][j] != 0) {
						ResultSet CDM = st
								.executeQuery("select * from cdmast where trncd = "
										+ j);
						if (CDM.next()) {
							stUP.executeUpdate("insert into autopost values (0,"
									+ i
									+ ","
									+ j
									+ ",0 ,'"
									+ CDM.getString("BRSUBSYS")
									+ "' , "
									+ (CDM.getString("BRACNO") == null ? 0
											: CDM.getInt("BRACNO"))
									+ ","
									+ TotAmt1[i][j] + ", 'D',0)");
						}

					}
				}
			}
			st.close();
			stUP.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Deduction_Cal(String BgnDate, Connection Cn) {
		int total = 0;
		String dedstr = "";
		ResultSet ded = null;
		ResultSet trn = null;
		try {
			Statement st = Cn.createStatement();
			Statement stUP = Cn.createStatement();
			Statement st1 = Cn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			dedstr = "select DISTINCT SUBSYS_cd,TRNCD from dedmast  WHERE ACTYN = 'Y' order by trncd";
			ded = st.executeQuery(dedstr);
			while (ded.next()) {

				String TrnStr = "select * from PAYTRAN t,emptran e  where t.trncd = "
						+ ded.getInt("trncd")
						+ " and e.empno  = t.empno  and  "
						+ " e.effdate = ( SELECT MAX(E2.EFFDATE) FROM EMPTRAN E2 WHERE e2.empno = t.empno and E2.EFFDATE <= '"
						+ BgnDate
						+ "')"
						+ " and inp_amt <> (select sum(amount) from dedmast where empno = t.empno and trncd = t.trncd AND ACTYN = 'Y' )";

				trn = st1.executeQuery(TrnStr);
				if (trn.next()) {
					System.out
							.println("Few standard Deductions you entered are not matched with deduction master");
					trn.beforeFirst();
					while (trn.next()) {
						System.out.println(trn.getInt("EMPNO") + "\t"
								+ trn.getInt("TRNCD"));
					}
				}

				trn = null;
				TrnStr = "select * from PAYTRAN t, emptran e where t.trncd = "
						+ ded.getInt("trncd")
						+ "  and e.empno = t.empno and "
						+ "e.effdate = ( SELECT MAX(E2.EFFDATE) FROM EMPTRAN E2 WHERE e2.empno = t.empno and E2.EFFDATE <= '"
						+ BgnDate
						+ "')"
						+ " and net_amt = (select sum(amount) from dedmast where empno = t.empno and trncd = t.trncd AND ACTYN = 'Y')";

				trn = st1.executeQuery(TrnStr);
				if (trn.next()) {
					trn.beforeFirst();
					while (trn.next()) {
						total = total + trn.getInt("NET_AMT");
					}
				} else {
					trn = null;
					TrnStr = "select sum(inp_amt) from PAYTRAN where trncd = "
							+ ded.getInt("trncd");
					trn = st1.executeQuery(TrnStr);
					if (trn.next()) {
						total = (trn.getString(1) == null ? 0 : trn.getInt(1));
					}

				}

				String CdmStr = "select * from cdmast where trncd = "
						+ ded.getInt("trncd");
				ResultSet CDM = st1.executeQuery(CdmStr);
				if (CDM.next()) {
					if (total != 0) {
						if (CDM.getInt("DRACNO") != 0) {
							stUP.executeUpdate("insert into autopost values (0,999,"
									+ ded.getInt("trncd")
									+ ",0 ,'"
									+ (CDM.getString("DRSUBSYS") == null ? ""
											: CDM.getString("DRSUBSYS"))
									+ "',"
									+ (CDM.getString("DRACNO") == null ? 0
											: CDM.getInt("DRACNO"))
									+ ","
									+ total + ", 'C',0)");
						} else {
							stUP.executeUpdate("insert into autopost values (0,999,"
									+ ded.getInt("trncd")
									+ ",0 ,'"
									+ (CDM.getString("DRSUBSYS") == null ? ""
											: CDM.getString("DRSUBSYS"))
									+ "',"
									+ (CDM.getString("DRACNO") == null ? 0
											: CDM.getInt("DRACNO"))
									+ ","
									+ total + ", 'D',0)");
						}
					}
				}

			} // End While

			dedstr = "SELECT DED.*, EMP.BRANCH FROM DEDMAST DED, EMPTRAN EMP , PAYTRAN t WHERE t.empno = ded.empno and t.trncd = ded.trncd and  EMP.EMPNO = DED.EMPNO AND DED.ACTYN = 'Y' and "
					+ " EMP.effdate = ( SELECT MAX(E2.EFFDATE) FROM EMPTRAN E2 WHERE e2.empno = DED.empno and E2.EFFDATE <= '"
					+ BgnDate + "')" + "ORDER BY ded.EMPNO,ded.TRNCD";
			ded = st.executeQuery(dedstr);
			while (ded.next()) {
				stUP.executeUpdate("insert into autopost values("
						+ ded.getInt("empno") + "," + ded.getInt("BRANCH")
						+ "," + ded.getInt("trncd") + "," + ded.getInt("srno")
						+ ",'" + ded.getString("subsys_cd") + "', "
						+ ded.getInt("Ac_no") + "," + ded.getInt("amount")
						+ ", 'C',0)");
			}
			st.close();
			st1.close();
			stUP.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ------------- Method to get Number of Days in a month ----------------
	public static float getDays(String date) {
		float result = 0.0f;
		String[] dt = date.split("-"); // [0]->dd [1]->mmm [2]->yyyy
		if (dt[1].equalsIgnoreCase("Jan"))
			result = 31;
		else if (dt[1].equalsIgnoreCase("Feb")) {
			if (Integer.parseInt(dt[2]) % 4 == 0) {
				result = 29;
			} else
				result = 28;
		} else if (dt[1].equalsIgnoreCase("Mar"))
			result = 31;
		else if (dt[1].equalsIgnoreCase("Apr"))
			result = 30;
		else if (dt[1].equalsIgnoreCase("May"))
			result = 31;
		else if (dt[1].equalsIgnoreCase("Jun"))
			result = 30;
		else if (dt[1].equalsIgnoreCase("Jul"))
			result = 31;
		else if (dt[1].equalsIgnoreCase("Aug"))
			result = 31;
		else if (dt[1].equalsIgnoreCase("Sep"))
			result = 30;
		else if (dt[1].equalsIgnoreCase("Oct"))
			result = 31;
		else if (dt[1].equalsIgnoreCase("Nov"))
			result = 30;
		else if (dt[1].equalsIgnoreCase("Dec"))
			result = 31;
		return result;
	}

	public static int getMonth(String date) {
		int result = 0;
		String[] dt = date.split("-"); // [0]->dd [1]->mmm [2]->yyyy
		if (dt[1].equalsIgnoreCase("Jan"))
			result = 1;
		else if (dt[1].equalsIgnoreCase("Feb"))
			result = 2;
		else if (dt[1].equalsIgnoreCase("Mar"))
			result = 3;
		else if (dt[1].equalsIgnoreCase("Apr"))
			result = 4;
		else if (dt[1].equalsIgnoreCase("May"))
			result = 5;
		else if (dt[1].equalsIgnoreCase("Jun"))
			result = 6;
		else if (dt[1].equalsIgnoreCase("Jul"))
			result = 7;
		else if (dt[1].equalsIgnoreCase("Aug"))
			result = 8;
		else if (dt[1].equalsIgnoreCase("Sep"))
			result = 9;
		else if (dt[1].equalsIgnoreCase("Oct"))
			result = 10;
		else if (dt[1].equalsIgnoreCase("Nov"))
			result = 11;
		else if (dt[1].equalsIgnoreCase("Dec"))
			result = 12;
		return result;
	}

	public static int getYear(String date) {
		int result = 0;
		String[] dt = date.split("-");
		result = Integer.parseInt(dt[2]);
		return result;
	}

	public static String getEmpRange() {
		String result = "";
		try {
			Connection Con = ConnectionManager.getConnection();
			Statement st = Con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT MIN(EMPNO), MAX(EMPNO) FROM EMPMAST");

			if (rs.next()) {
				result = rs.getInt(1) + "-" + rs.getInt(2);
			}
			rs.close();
			st.close();

			Con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static float overTimeCalc(int trncd, int empno, String BgnDate,
			Connection Cn) {
		float result = 0.0f;
		try {
			Statement st = Cn.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT SUM(HOURS) FROM OVERTIMEMTN WHERE EMPNO="
							+ empno
							+ " AND SHIFTCODE="
							+ trncd
							+ " AND OTDATE BETWEEN '"
							+ ReportDAO.BOM(BgnDate)
							+ "' AND '" + ReportDAO.EOM(BgnDate) + "'");
			if (rs.next()) {
				result = (rs.getString(1) == null ? 0.0f : rs.getFloat(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/*
	  public static void deduction_insert(int empno,String G_UserId, String BgnDate,Connection Cn)
	  {
	  try
	  {
	  Statement dedsttr = Cn.createStatement();
	  Statement ded = Cn.createStatement();
	  Statement dedinsrt = Cn.createStatement(); 
	  Statement select = Cn.createStatement(); 
	  Statement
	  inst_update = Cn.createStatement(); 
	  String deductiontr =	  "select distinct trncd from dedmast where empno = "+empno+" and ACTYN='Y'"; 
	  ResultSet	  dedtr = dedsttr.executeQuery(deductiontr);
	  
	  while(dedtr.next())
	  {
	 String deduction = "select * from DEDMAST where EMPNO ="+empno+" and " +
	 		"REPAY_START < = '"+ReportDAO.EOM(BgnDate)+"  and TOTAL_INSTLMNTS >=0 and ACTUAL_TOTAL_AMT_PAID < SANC_AMT and ACTYN='Y' and  trncd = "+dedtr.getInt(1);
	  System.out.println(deduction); 
	  
	  ResultSet dedt = ded.executeQuery(deduction);
	  while(dedt.next())
	  {	  
	  String sel_paytran = "SELECT * FROM PAYTRAN WHERE EMPNO ="+empno+" AND TRNCD = "+dedtr.getInt("TRNCD");
	  ResultSet sel = select.executeQuery(sel_paytran);
	  
	  if(sel.next())
	  { 
	  String update_paytran = "UPDATE PAYTRAN SET INP_AMT = "+(dedt.getFloat("AMOUNT") <= (dedt.getFloat("ACTUAL_TOTAL_AMT")-dedt.getFloat("ACTUAL_TOTAL_AMT_PAID"))?dedt.getFloat("AMOUNT"):(dedt.getFloat("ACTUAL_TOTAL_AMT")-dedt.getFloat("ACTUAL_TOTAL_AMT_PAID")) ) +" where EMPNO = "+empno+" AND TRNCD = "+dedtr.getInt("TRNCD");
	  
	  dedinsrt.executeUpdate(update_paytran);
	  }
	  else
	  {
	  System.out.println("inded"); 
	  String insert = "INSERT INTO PAYTRAN " +
	  "VALUES('"+ReportDAO.EOM(BgnDate)+"',"+empno+","+dedtr.getInt("TRNCD")+","+0+","+(dedt.getFloat("AMOUNT") <= (dedt.getFloat("ACTUAL_TOTAL_AMT")-dedt.getFloat("ACTUAL_TOTAL_AMT_PAID"))?dedt.getFloat("AMOUNT"):(dedt.getFloat("ACTUAL_TOTAL_AMT")-dedt.getFloat("ACTUAL_TOTAL_AMT_PAID")) )+","+0+","+0+","+0+","+0+",'*',"+G_UserId+",'"+BgnDate+"','A')";
	  dedinsrt.executeUpdate(insert);
	  
	  //String install="UPDATE DEDMAST SET No_Of_Installment = "+(dedt.getInt("No_Of_Installment")-1)+" " +
	  //		" where empno = "+empno+"and trncd = "+dedtr.getInt("TRNCD");
	  //inst_update.executeUpdate(install); 
	  }
	  
	  }
	  
	  }
	  
	  
	  
	  
	  }catch (Exception e) 
	  {
		  e.printStackTrace();
	  }
	  
	  }
	 */
}
