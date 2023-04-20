
package payroll.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import payroll.Core.Calculate;
import payroll.Core.ReportDAO;
import payroll.Model.AttendStatusBean;
import payroll.Model.Attend_bean;
import payroll.Model.AttendanceBean;
import payroll.Model.CompBean;
import payroll.Model.EmpOffBean;
import payroll.Model.TranBean;

public class EmpAttendanceHandler {
	SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
	
	
	public ArrayList getEmpAttend(String date, int site_id, int mgr,	String state,ArrayList<TranBean> tran ) {
	//	System.out.println("into EmpAttendanceHandler"+date+site_id+mgr+state);
		
		ArrayList Emp_al = new ArrayList();
		EmpOffHandler eoffhdlr = new EmpOffHandler();
		EmpOffBean eoffbn = new EmpOffBean();
		//ArrayList<TranBean> tran = new ArrayList<TranBean>();
// System.out.println("trasn size"+tran.size());
	/*	if (state.equalsIgnoreCase("edit")) {
			eoffbn = eoffhdlr.getEmpOfficAddInfo(Integer.toString(mgr));
			tran = eoffhdlr.getEmpList(Integer.toString(eoffbn.getPrj_srno()));
			//tran = eoffhdlr.getEmp(Integer.toString(site_id),date);
		} else if (state.equalsIgnoreCase("view"))

		{
		
			tran = eoffhdlr.getEmpList(Integer.toString(site_id));
			
		
		}*/
		float days = Calculate.getDays(date);

	

		Connection conn = ConnectionManager.getConnection();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = null;
			for (TranBean tbean : tran) {
				ArrayList<TranBean> Emp_bean = new ArrayList<TranBean>();
				int count = 0;
				String date1 = ReportDAO.BOM(date);
				rs = st.executeQuery("select * from Emp_attendance where empno="
						+ tbean.getEMPNO()
						/*+ " and site_id="
						+ site_id*/
						+ " and attd_date between '"
						+ ReportDAO.BOM(date)
						+ "' and  '"
						+ ReportDAO.EOM(date)
						+ "' order by attd_date");
			
				while (rs.next()) {
					TranBean ab = new TranBean();
					ab.setEMPNO(rs.getInt(1));
					ab.setSite_id(rs.getString(2));
					ab.setDate(rs.getString(3));
					ab.setVal(rs.getString(4));
					Emp_bean.add(ab);
					count++;
				}
				if (count <= 1) {
					for (int x = 1; x <= days; x++) {
						TranBean ab = new TranBean();
						ab.setEMPNO(tbean.getEMPNO());
						ab.setSite_id(Integer.toString(site_id));

						try {
								
							ab.setDate(date1);
							ab.setVal("");
							Emp_bean.add(ab);

							SimpleDateFormat format = new SimpleDateFormat(
									"dd-MMM-yyyy");
							Calendar c = Calendar.getInstance();
							c.setTime(format.parse(date1));
							c.add(Calendar.DATE, 1);
							date1 = format.format(c.getTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

				Emp_al.add(Emp_bean);
			}
			// //System.out.println("Size of Emp_all in handlr= "+Emp_al.size());
			conn.close();

		} catch (SQLException e) {
			// //System.out.println("error into EmpAttendanceHandler");
			e.printStackTrace();
		}

		return Emp_al;
	}

	
	public ArrayList getEmpAttend(String date,String empNo,int mgr,String state)
	{
		System.out.println("getEmpAttendApproval");
	
		ArrayList Emp_al=new ArrayList();
		ArrayList<Attend_bean> attend = new ArrayList<Attend_bean>();
 
		
		float days = Calculate.getDays(date);



		Connection conn = ConnectionManager.getConnection();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = null;
			
				
				int count = 0;
				String date1 = ReportDAO.BOM(date);
				rs = st.executeQuery("select * from Emp_attendance where empno="
						+ empNo
						+ " and attd_date between '"
						+ ReportDAO.BOM(date)
						+ "' and  '"
						+ ReportDAO.EOM(date)
						+ "' order by attd_date");
			
				while (rs.next()) {
					Attend_bean ab = new Attend_bean();
					ab.setEmpno(rs.getString(1));
					
					ab.setDate(rs.getString(3));
					
					ab.setVal(rs.getString(4));
					ab.setStatus(rs.getString(9));
					attend.add(ab);
					count++;
				}
	
			
			Emp_al.add(attend);
			conn.close();

		} catch (SQLException e) {
			// //System.out.println("error into EmpAttendanceHandler");
			e.printStackTrace();
		}

		return Emp_al;
	}
	
	
	public boolean insertEmpAttendance(ArrayList emp_al, int eno,String status, String roleId) throws ParseException {
		

		System.out.println("into insertEmpAttendance");
		SiteWeekOffHandler siteWeekOffHandler=new SiteWeekOffHandler();
		ArrayList<String> list=new ArrayList<String>();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar c1 = Calendar.getInstance(); // today
	
		String date=sdf.format(c1.getTime());
		String BomDt = ReportDAO.BOM(date);
		String EomDt= ReportDAO.EOM(date);
		
		int month=0;
		
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String currentdate=EAH.getServerDate();
	
		try {
			Connection conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);

			Statement st = conn.createStatement();
			Statement st1 = conn.createStatement();
			Statement ad= conn.createStatement();
			ResultSet rs = null;
			
			if(status.equals("transfer")){
				

				
				System.out.println("Inside if for creation of sheet in handler");
			
				for (int j = 0; j < emp_al.size(); j++) {
					ArrayList<TranBean> Emp_bean = (ArrayList<TranBean>) emp_al.get(j);
					
					for (int c = 0; c < Emp_bean.size(); c++) {
						TranBean ab = new TranBean();
						ab = Emp_bean.get(c);
						st.executeUpdate("IF EXISTS (SELECT empno FROM  Emp_attendance WHERE empno="
								+ ab.getEMPNO()
								//SITE_ID IS COMMENTED FOR PROPER WORKING OF TRANSFER
								/*+ " and site_id="
								+ ab.getSite_id()*/
								+ " and attd_date ='"
								+ ab.getDate()
								+ "')"
								+ "UPDATE  Emp_attendance SET empno="
								+ ab.getEMPNO()
								+ ", attd_date ='"
								+ ab.getDate()
								+ "', ATTD_VAL='"
								+ ab.getVal()
									+ "' , site_id="+ ab.getSite_id()+" where empno="
								+ ab.getEMPNO()
								+ " and attd_date ='"
								+ ab.getDate()
								+ "' else"
								+ " INSERT INTO Emp_attendance (empno,site_id, attd_date, attd_val, created_date, created_by) VALUES("
								+ ab.getEMPNO()
								+ ","
								+ ab.getSite_id()
								+ ",'"
								+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+") ");
						
						ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" where  site_id="+ ab.getSite_id()+" and attd_date between '"+ReportDAO.BOM(ab.getDate())+"'and '"+ReportDAO.EOM(ab.getDate())+"'  ");

						//ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" ");
					/*	ad.executeUpdate("insert into EMP_ATTENDANCE_HIST (empno, site_id, attd_date, attd_val, created_date, created_by,updated_by,updated_date) VALUES("
								+ ab.getEMPNO()
								+ ","
								+ ab.getSite_id()
								+ ",'"
								+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+","+eno+",'"+currentdate+"')");*/
					//ad.executeUpdate("update emp_attendance set attd_val='P' where attd_val='' and attd_date between '"+BomDt+"'and '"+date+"'");

					}
						

				}
			
			
			}
			
		if(status.equals("firstTimeSaved") /*|| roleId.equals("1")*/){
			
			System.out.println("Inside if for first time saved of sheet in handler");
		
			for (int j = 0; j < emp_al.size(); j++) {
				ArrayList<TranBean> Emp_bean = (ArrayList<TranBean>) emp_al.get(j);
				
				for (int c = 0; c < Emp_bean.size(); c++) {
					TranBean ab = new TranBean();
					ab = Emp_bean.get(c);
					st.executeUpdate("IF EXISTS (SELECT empno FROM  Emp_attendance WHERE empno="
							+ ab.getEMPNO()
							//SITE_ID IS COMMENTED FOR PROPER WORKING OF TRANSFER
							/*+ " and site_id="
							+ ab.getSite_id()*/
							+ " and attd_date ='"
							+ ab.getDate()
							+ "')"
							+ "UPDATE  Emp_attendance SET empno="
							+ ab.getEMPNO()
							+ ", attd_date ='"
							+ ab.getDate()
							+ "', ATTD_VAL='"
							+ ab.getVal()
								+ "' , site_id="+ ab.getSite_id()+" where empno="
							+ ab.getEMPNO()
							+ " and attd_date ='"
							+ ab.getDate()
							+ "' else"
							+ " INSERT INTO Emp_attendance (empno,site_id, attd_date, attd_val, created_date, created_by) VALUES("
							+ ab.getEMPNO()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+") ");
					
					
					ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" where  site_id="+ ab.getSite_id()+" and attd_date between '"+ReportDAO.BOM(ab.getDate())+"'and '"+ReportDAO.EOM(ab.getDate())+"'  ");
				/*	ad.executeUpdate("insert into EMP_ATTENDANCE_HIST (empno, site_id, attd_date, attd_val, created_date, created_by,updated_by,updated_date) VALUES("
							+ ab.getEMPNO()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+","+eno+",'"+currentdate+"')");*/
				//ad.executeUpdate("update emp_attendance set attd_val='P' where attd_val='' and attd_date between '"+BomDt+"'and '"+date+"'");

				}
					

			}
		}
		
		
		
if(status.equals("firstTimeSaved") && roleId.equals("1")){
			
			System.out.println("Inside if for first time saved of sheet in handler");
		
			for (int j = 0; j < emp_al.size(); j++) {
				ArrayList<TranBean> Emp_bean = (ArrayList<TranBean>) emp_al.get(j);
				
				for (int c = 0; c < Emp_bean.size(); c++) {
					TranBean ab = new TranBean();
					ab = Emp_bean.get(c);
				
					/*System.out.println("IF EXISTS (SELECT empno FROM  Emp_attendance WHERE empno="
							+ ab.getEMPNO()
							//SITE_ID IS COMMENTED FOR PROPER WORKING OF TRANSFER
							+ " and site_id="
							+ ab.getSite_id()
							+ " and attd_date ='"
							+ ab.getDate()
							+ "')"
							+ "UPDATE  Emp_attendance SET empno="
							+ ab.getEMPNO()
							+ ", attd_date ='"
							+ ab.getDate()
							+ "', ATTD_VAL='"
							+ ab.getVal()
								+ "' , site_id="+ ab.getSite_id()+" where empno="
							+ ab.getEMPNO()
							+ " and attd_date ='"
							+ ab.getDate()
							+ "' else"
							+ " INSERT INTO Emp_attendance (empno,site_id, attd_date, attd_val, created_date, created_by) VALUES("
							+ ab.getEMPNO()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+") ");
					*/
					st.executeUpdate("IF EXISTS (SELECT empno FROM  Emp_attendance WHERE empno="
							+ ab.getEMPNO()
							//SITE_ID IS COMMENTED FOR PROPER WORKING OF TRANSFER
							/*+ " and site_id="
							+ ab.getSite_id()*/
							+ " and attd_date ='"
							+ ab.getDate()
							+ "')"
							+ "UPDATE  Emp_attendance SET empno="
							+ ab.getEMPNO()
							+ ", attd_date ='"
							+ ab.getDate()
							+ "', ATTD_VAL='"
							+ ab.getVal()
								+ "' , site_id="+ ab.getSite_id()+" where empno="
							+ ab.getEMPNO()
							+ " and attd_date ='"
							+ ab.getDate()
							+ "' else"
							+ " INSERT INTO Emp_attendance (empno,site_id, attd_date, attd_val, created_date, created_by) VALUES("
							+ ab.getEMPNO()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+") ");
					
					ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" where  site_id="+ ab.getSite_id()+" and attd_date between '"+ReportDAO.BOM(ab.getDate())+"'and '"+ReportDAO.EOM(ab.getDate())+"'  ");

					//ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+"  ");
				/*	ad.executeUpdate("insert into EMP_ATTENDANCE_HIST (empno, site_id, attd_date, attd_val, created_date, created_by,updated_by,updated_date) VALUES("
							+ ab.getEMPNO()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+","+eno+",'"+currentdate+"')");*/
				//ad.executeUpdate("update emp_attendance set attd_val='P' where attd_val='' and attd_date between '"+BomDt+"'and '"+date+"'");
					
				//FOR EMPLOYEE WISE WEEKOFF CHANGE	
					/*System.out.println("select LKP_SRNO" +
							" from LOOKUP  where LKP_CODE='day'"+
							"and LKP_SRNO=(select day from weekoff where"+
							  " SITE_ID="+ab.getSite_id()+" and EMPNO="+ab.getEMPNO()+"" +
							  		" and MONTH_WO='"+ab.getDate().substring(3,6)+"' and" +
							 " YEAR_WO="+ab.getDate().substring(7,11)+")");*/
					
					
					ResultSet resultSet=st.executeQuery("select LKP_SRNO" +
							" from LOOKUP  where LKP_CODE='day'"+
							"and LKP_SRNO=(select day from weekoff where"+
							  " SITE_ID="+ab.getSite_id()+" and EMPNO="+ab.getEMPNO()+"" +
							  		" and MONTH_WO='"+ab.getDate().substring(3,6)+"' and" +
							 " YEAR_WO="+ab.getDate().substring(7,11)+")" );
			/*	System.out.println("SELECT LKP_SRNO AS MONTH FROM LOOKUP WHERE " +
							"LKP_CODE='MON' AND LKP_DISC=" +
							"(select MONTH_WO from WEEKOFF where EMPNO="+ab.getEMPNO()+" " +
							"AND MONTH_WO='"+ab.getDate().substring(3,6)+"'" +
									" AND YEAR_WO="+ab.getDate().substring(7,11)+")" );*/
					ResultSet resultSet1=st1.executeQuery("SELECT LKP_SRNO AS MONTH FROM LOOKUP WHERE " +
							"LKP_CODE='MON' AND LKP_DISC=" +
							"(select MONTH_WO from WEEKOFF where EMPNO="+ab.getEMPNO()+" " +
							"AND MONTH_WO='"+ab.getDate().substring(3,6)+"'" +
									" AND YEAR_WO="+ab.getDate().substring(7,11)+")" );
					while(resultSet1.next()){
						month=resultSet1.getInt("MONTH")-1;
					
				while(resultSet.next()){
				
				//	System.out.println("lkp_srno for "+ab.getEMPNO()+ ": "+resultSet.getInt("LKP_SRNO") );
					if(resultSet.getInt("LKP_SRNO")!=1){
			/*	System.out.println("update emp_attendance set attd_val=' ' " +
							"where attd_val='WO' and attd_date= '"+ab.getDate()+"' " +
							"and empno="+ab.getEMPNO()+" and attd_date ='"+ ab.getDate()
							+ "' ");	*/	
						
					ad.executeUpdate("update emp_attendance set attd_val=' ' " +
							"where attd_val='WO' and attd_date= '"+ab.getDate()+"' " +
							"and empno="+ab.getEMPNO()+" ");
				
					list=siteWeekOffHandler.getWeekOffDays(Integer.parseInt(ab.getDate().substring(7,11)),
							month,resultSet.getInt("LKP_SRNO"));
						for(String string:list){
							
							if(Integer.parseInt(string)==
									Integer.parseInt((ab.getDate().substring(0,2)))){
								ad.executeUpdate("update emp_attendance set attd_val='WO' " +
										"where attd_date= '"+ab.getDate()+"' " +
										"and empno="+ab.getEMPNO()+" ");
							}
						}	 
					
					} //  END OF IF inner if
					}// END OF outer IF
					
					}
			
					//FOR EMPLOYEE WISE WEEKOFF CHANGE END	
					
				}
			
			}
			
		}
		else{

			
			System.out.println("Inside else for single date data saving in handler");
			for (int j = 0; j < emp_al.size(); j++) {
				ArrayList<TranBean> Emp_bean = (ArrayList<TranBean>) emp_al.get(j);
				ResultSet resultSet=null;
				 {
					TranBean ab = new TranBean();
					ab = Emp_bean.get(j);
					
					//transferred employee code start from here..!!!!!!
	  int numberOfDaysInMonth=(int) Calculate.getDays(ab.getDate());// 31,30,29,28		
	 
	
		resultSet=st.executeQuery(" select EMPNO ,COUNT(*) COUNT  from EMP_ATTENDANCE  where ATTD_DATE between" +
						"  '"+ReportDAO.BOM(ab.getDate())+" ' and '"+ReportDAO.EOM(ab.getDate())+"'   " +
						" and empno='"+ab.getEMPNO()+ " ' group by EMPNO");
     
		if(resultSet.next()==false){
			
							 String beginningOfMOnth=ReportDAO.BOM(ab.getDate());
							 
							 ArrayList<String> weekOffList= new ArrayList<String>();
							ArrayList<String> holidaysList= new ArrayList<String>();
							 
							for(int index=1;index<=numberOfDaysInMonth;index++){
						
								st.execute(" INSERT INTO Emp_attendance (empno,site_id, attd_date, attd_val, created_date, created_by) VALUES("
											+ ab.getEMPNO()
											+ " , "
											+ ab.getSite_id()
											+ " , '"
											+ beginningOfMOnth + "' , ' ' , '"+currentdate+"' , "+eno+") ");
								
								SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
								Calendar c = Calendar.getInstance();    
								c.setTime(format.parse(beginningOfMOnth));
								c.add(Calendar.DATE, 1);
								beginningOfMOnth=format.format(c.getTime());
								
								
							}
							//FOR INSERTING WEEKLY OFFS OF TRANSFERED EMPLOYEES 
							
							resultSet=st.executeQuery("select distinct attd_date from EMP_ATTENDANCE where ATTD_VAL='WO' and ATTD_DATE between" +
									" '"+ReportDAO.BOM(ab.getDate())+" ' and   '" + ReportDAO.EOM(ab.getDate())+  "'   ");
							while(resultSet.next()){
								Attend_bean attend_bean=new Attend_bean();
								attend_bean.setDate(resultSet.getString("attd_date"));
								weekOffList.add(attend_bean.getDate());
							}
							for(int index=0;index<weekOffList.size();index++)
							{
								st.executeUpdate("update Emp_attendance set attd_val='WO' where empno="+ab.getEMPNO()+" and attd_date = '"+weekOffList.get(index)+"'  ");	
							}
							
							//FOR INSERTING HOLIDAYS OF TRANSFERED EMPLOYEES
							
							resultSet=st.executeQuery("select distinct attd_date from EMP_ATTENDANCE where ATTD_VAL='HD' and ATTD_DATE between" +
									" '"+ReportDAO.BOM(ab.getDate())+" ' and   '" + ReportDAO.EOM(ab.getDate())+  "'    ");
							while(resultSet.next()){
								Attend_bean attend_bean=new Attend_bean();
								attend_bean.setDate(resultSet.getString("attd_date"));
								holidaysList.add(attend_bean.getDate());
							}
							for(int index=0;index<holidaysList.size();index++)
							{
								
								st.executeUpdate("update Emp_attendance set attd_val='HD' where empno="+ab.getEMPNO()+" and attd_date = '"+holidaysList.get(index)+"'  ");	
							}
			
			
		         }
				
		//transferred employees code ends here			
		
		
		
					
					st.executeUpdate("IF EXISTS (SELECT empno FROM  Emp_attendance WHERE empno="
							+ ab.getEMPNO()
						/*	+ " and site_id="
							+ ab.getSite_id()*/
							+ " and attd_date ='"
							+ ab.getDate()
							+ "')"
							+ "UPDATE  Emp_attendance SET empno="
							+ ab.getEMPNO()
							+ ", attd_date ='"
							+ ab.getDate()
							+ "', site_id="+ab.getSite_id()+" "
									+", ATTD_VAL='"
							+ ab.getVal()
							+ "' where empno="
							+ ab.getEMPNO()
							/*+ " and site_id="
							+ ab.getSite_id()*/
							+ " and attd_date ='"
							+ ab.getDate()
							+ "' else"
							+ " INSERT INTO Emp_attendance (empno,site_id, attd_date, attd_val, created_date, created_by) VALUES("
							+ ab.getEMPNO()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+") ");
					ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" where  site_id="+ ab.getSite_id()+" and attd_date = '"+ab.getDate()+"' and empno="+ab.getEMPNO()+"  "); 

	
					
					
					//ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" where attd_date='"+	 ab.getDate()+"' ");
				/*	ad.executeUpdate("insert into EMP_ATTENDANCE_HIST (empno, site_id, attd_date, attd_val, created_date, created_by,updated_by,updated_date) VALUES("
							+ ab.getEMPNO()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+","+eno+",'"+currentdate+"')");*/
				//ad.executeUpdate("update emp_attendance set attd_val='P' where attd_val='' and attd_date between '"+BomDt+"'and '"+date+"'");
					
					//
					
					
				}
					

			}
		
		}
		/*	Statement st11=conn.createStatement();
			st11.executeUpdate("update EMP_ATTENDANCE_HIST  set BATCH_NO=(select (case when (SELECT MAX(BATCH_NO) FROM EMP_ATTENDANCE_HIST) IS NULL " 
					+" then  1 else MAX(BATCH_NO+1) end) from EMP_ATTENDANCE_HIST) where BATCH_NO IS NULL");*/
			conn.commit();
			
				
			flag = true;

		} catch (SQLException e) {
			// System.out.println("error into insertEmpAttendance");
			e.printStackTrace();
		}
		return flag;
	}

	public String setAttendanceStatus(String date, int site_id, String status,int eno) {

		// System.out.println("into attendanceApproval");
		// System.out.println(site_id);
		// System.out.println(date);
		// System.out.println(status);
		float halfday1=0;
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String currentdate=EAH.getServerDate();
		Connection conn = ConnectionManager.getConnection();
		try {
			conn.setAutoCommit(false);
			if (status.equalsIgnoreCase("approved")) {

				
				String dt[] = date.split("-");
				Statement st5 = conn.createStatement();
			

					Statement st1 = conn.createStatement();
					Statement st2 = conn.createStatement();
					String fromDate=ReportDAO.BOM(date);
					String toDate=ReportDAO.EOM(date);
			
					
					try {
						EmpOffHandler eoffhdlr= new EmpOffHandler();
						ArrayList<TranBean> tran = new ArrayList<TranBean>();
						//commented by Aniket bcz====> single approved emp also get here for selected month so,
						//it will insert 301 and make sal_details "AutoInst" and once 301 is inserted at time of calculation
						//it will also make entry for other trncd and make sal_details PROCESSED then this emp again ready for 
						//finalize even if that one already finalized...
						
						//old method call
						//tran = eoffhdlr.getEmpListForAttendance(""+site_id,date);
					
						tran = eoffhdlr.getEmpListForAttendancewithout_singleApproveEMP(""+site_id,date);
						
						for (TranBean tb : tran){
						System.out.println("BEFORE         SPC");
						java.sql.CallableStatement cst1= conn.prepareCall("{call SPC_CHECKING_SUNDAY_ABSENT(?,?,?) }");
						cst1.setString(1,fromDate);
						cst1.setString(2,toDate);
						cst1.setInt(3,tb.getEMPNO())  ;
						
						System.out.println("01"+fromDate);
						System.out.println("02"+toDate);
				
						
						ResultSet rs = cst1.executeQuery();
						
						    while (rs.next()) {
						    	halfday1=0;
						    	Statement statement = conn.createStatement();
						    	
						        String EMPLOYEENUMBER = rs.getString(1);
						        String ATTENDANCE_COUNT = rs.getString(2);
						        
						        System.out.println("Generated employeeId: " + EMPLOYEENUMBER+""+ATTENDANCE_COUNT);
						        
						        String sql="if exists(select * from PAYTRAN where TRNCD=301 and EMPNO="+EMPLOYEENUMBER+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) "
										+"update PAYTRAN  set  INP_AMT="+ATTENDANCE_COUNT+",USRCODE="+eno+"   where  TRNCD=301 and EMPNO="+ EMPLOYEENUMBER+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'"
										+" else  "
										+ "INSERT INTO PAYTRAN VALUES('"+ReportDAO.EOM(date)+"' ,"
										+ EMPLOYEENUMBER + ",301,0,"
										+ ATTENDANCE_COUNT + ",0,0,0," + ATTENDANCE_COUNT
										+ ",'0',"+eno+",'"+ReportDAO.getSysDate()+"','P')";
						        
						        statement.execute(sql);
						        System.out.println("1st absent cnt insert sql...."+sql);
						        
						        ResultSet rs1 = st1
										.executeQuery("SELECT count(*) as halfdaycnt FROM EMP_ATTENDANCE where ATTD_DATE between '"
												+ ReportDAO.BOM(date)
												+ "' and '"
												+ ReportDAO.EOM(date)
												+ "' and EMPNO="
												+ EMPLOYEENUMBER
												+ " and ATTD_VAL='H' ");
						        if (rs1.next()) {
									/*	if (rs1.getString("ATTD_VAL").equalsIgnoreCase("A")) {
											totDed++;

										} */
						        	halfday1=	Integer.parseInt(rs1.getString("halfdaycnt"));
											
						        	
										
						        }
						        
						        Statement st3 = conn.createStatement();
								
									if (halfday1 > 0) {
										
										
										halfday1=halfday1/2;
										
										float ab=Float.parseFloat(ATTENDANCE_COUNT);
										
										halfday1+=ab;
										
										String chkquery=("delete from PAYTRAN where TRNCD=301 and EMPNO="+EMPLOYEENUMBER+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ");
										st3.execute(chkquery);
										String dd= String.valueOf(halfday1);
										System.out.println("inside /2 for empno.."+EMPLOYEENUMBER+" halfday is..."+halfday1+"...INSERTING VALUE....dddddd..."+dd);
										String sql1="if exists(select * from PAYTRAN where TRNCD=301 and EMPNO="+EMPLOYEENUMBER +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) "
													+"update PAYTRAN  set  INP_AMT="+ dd +",USRCODE="+eno+"   where  TRNCD=301 and EMPNO="+EMPLOYEENUMBER +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'"
													+" else  "
													+ "INSERT INTO PAYTRAN VALUES('"+ReportDAO.EOM(date)+"',"
													+ EMPLOYEENUMBER + ",301,0,"+ dd +",0,0,0,"+ dd +",'0',"+eno+",'"+ReportDAO.getSysDate()+"','P')";
										
										st3.executeUpdate(sql1);
										
						        
									}
						        
						        
						       /* statement.execute(sql);
						        System.out.println("1st sql...."+sql);*/
						        sql="update sal_details set sal_status='AutoInst' where empno="+EMPLOYEENUMBER+" and sal_month = '"+ReportDAO.BOM(date).substring(3,11)+"' ";
						        statement.execute(sql);
						        System.out.println("2nd sql(autoinsert) 2 all...."+sql);
						        //to delete 301 if record is already finalized for particular employee
						        sql="delete from paytran where trncd=301  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'  " +
						        		" and empno=(select distinct empno from paytran_stage where EMPNO="+EMPLOYEENUMBER+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) ";
						        
						        System.out.println("3rd sql deleting from paytran for 301"+sql);
						        statement.execute(sql);
						        
						      } 
						}System.out.println("for approved site emp==="+tran.size());
			         
					}
					catch (SQLException e) {
					   e.printStackTrace();
					}
					
					Statement stt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					ResultSet rs=stt.executeQuery("select distinct empno from EMP_ATTENDANCE where empno  not in(select distinct empno from EMP_ATTENDANCE where empno in(select distinct empno from EMP_ATTENDANCE where ATTD_DATE between '"
									+ ReportDAO.BOM(date)+ "' and '"+ ReportDAO.EOM(date)+ "' and site_id="+site_id+"  and attd_val='H'  )and ATTD_DATE between '"
									+ ReportDAO.BOM(date)+ "' and '"+ ReportDAO.EOM(date)+ "' and site_id="+site_id+"  and ATTD_VAL='A') and ATTD_VAL='H' and ATTD_DATE between '"
									+ ReportDAO.BOM(date)+ "' and '"+ ReportDAO.EOM(date)+ "' and site_id="+site_id+" order by EMPNO");
					/*System.out.println("!!!!!!!!!!!!!!!!!!!! query foe selecting employee of that site having only half day for employee in that month..."+"select distinct empno from EMP_ATTENDANCE where empno  not in(select distinct empno from EMP_ATTENDANCE where empno in(select distinct empno from EMP_ATTENDANCE where ATTD_DATE between '"
									+ ReportDAO.BOM(date)+ "' and '"+ ReportDAO.EOM(date)+ "' and site_id="+site_id+"  and attd_val='H'  )and ATTD_DATE between '"
									+ ReportDAO.BOM(date)+ "' and '"+ ReportDAO.EOM(date)+ "' and site_id="+site_id+"  and ATTD_VAL='A') and ATTD_VAL='H' and ATTD_DATE between '"
									+ ReportDAO.BOM(date)+ "' and '"+ ReportDAO.EOM(date)+ "' and site_id="+site_id+" order by EMPNO"); */
					Statement st4 = conn.createStatement();
					if(rs.next())
					{
						
						rs.previous();
						while(rs.next())
						{
							boolean fg = checksingleapprove(rs.getString("empno"),date);
							
							//if not already having single approved attendance for same month
							if(fg==false){
							halfday1=0;
							ResultSet rs1 = st1
									.executeQuery("SELECT count(*) as halfdaycnt FROM EMP_ATTENDANCE where ATTD_DATE between '"
											+ ReportDAO.BOM(date)
											+ "' and '"
											+ ReportDAO.EOM(date)
											+ "' and EMPNO="
											+ rs.getInt("empno")
											+ " and ATTD_VAL='H' ");
					        if (rs1.next()) {
								/*	if (rs1.getString("ATTD_VAL").equalsIgnoreCase("A")) {
										totDed++;

									} */
					        	halfday1=	Integer.parseInt(rs1.getString("halfdaycnt"));
										
					        	
									
					        }else{
					        	halfday1=0;
					        	}
					       // System.out.println("@@@@@@@@@This emp having only half days..empno-------->"+rs.getInt("empno")+"... and halfdaycount for this emp-------->"+halfday1);
					        halfday1=halfday1/2;
					        String sql1="if exists(select * from PAYTRAN where TRNCD=301 and EMPNO="+rs.getInt("empno") +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) "
									+"update PAYTRAN  set  INP_AMT="+ halfday1 +",USRCODE="+eno+"   where  TRNCD=301 and EMPNO="+rs.getInt("empno") +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'"
									+" else  "
									+ "INSERT INTO PAYTRAN VALUES('"+ReportDAO.EOM(date)+"',"
									+ rs.getInt("empno") + ",301,0,"+ halfday1 +",0,0,0,"+ halfday1 +",'0',"+eno+",'"+ReportDAO.getSysDate()+"','P')";
						
						st4.executeUpdate(sql1);
						//System.out.println("emp having only halfday so inserting/update for  him---->"+sql1);
					        
						sql1="update sal_details set sal_status='AutoInst' where empno="+ rs.getInt("empno")+" and sal_month = '"+ReportDAO.BOM(date).substring(3,11)+"' ";
						st4.execute(sql1);
				        System.out.println("2nd sql(autoinsert) 2 all...."+sql1);
				        //to delete 301 if record is already finalized for particular employee
				        sql1="delete from paytran where trncd=301  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'  " +
				        		" and empno=(select distinct empno from paytran_stage where EMPNO="+ rs.getInt("empno")+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) ";
				        
				        System.out.println("3rd sql deleting from paytran for 301"+sql1);
				        st4.execute(sql1);
							}
						}
					}
					
					//if by some condition site get saved auto then this code not work proper bcz it add the old cnt of attendsnce + new attendance cnt for that employee 2017-10-09  
		
		/*			ResultSet rs = st2.executeQuery("select distinct empno from EMP_ATTENDANCE where ATTD_DATE between '"
									+ ReportDAO.BOM(date)
									+ "' and '"
									+ ReportDAO.EOM(date)
									+ "' " +
									"and site_id="
									+ site_id + "" +
								" order by EMPNO ");
					
					while (rs.next()) {
						float totDed = 0;
						float halfday=0;
						float totPl = 0;
						float totCr = 0;
						float bal = 0;
						ResultSet rs1 = st1
								.executeQuery("SELECT EMPNO,ATTD_DATE,ATTD_VAL FROM EMP_ATTENDANCE where ATTD_DATE between '"
										+ ReportDAO.BOM(date)
										+ "' and '"
										+ ReportDAO.EOM(date)
										+ "' and EMPNO="
										+ rs.getInt("empno")
										+ " order by ATTD_DATE");
						while (rs1.next()) {
							if (rs1.getString("ATTD_VAL").equalsIgnoreCase("A")) {
								totDed++;

							} 
							 if (rs1.getString("ATTD_VAL").equalsIgnoreCase("H")) {
								halfday++;

							}
							
							else if (rs1.getString("ATTD_VAL")
									.equalsIgnoreCase("L")) {

								Statement st = conn.createStatement();
								ResultSet rs5 = null;
								rs5 = st.executeQuery("SELECT TOP 1 * FROM LEAVEBAL WHERE LEAVECD=1 AND EMPNO="
										+ rs1.getInt("EMPNO")
										+ " ORDER BY BALDT DESC");
								if (rs5.next()) {
									Statement st4 = conn.createStatement();
									String leave = "insert into LEAVETRAN (LEAVECD,EMPNO,TRNDATE,TRNTYPE,APPLDT,FRMDT,TODT,STATUS,DAYS) VALUES (1,"
											+ rs1.getInt("EMPNO")
											+ ",'"
											+ rs1.getString("ATTD_DATE")
											+ "','D','"
											+ rs1.getString("ATTD_DATE")
											+ "','"
											+ rs1.getString("ATTD_DATE")
											+ "','"
											+ rs1.getString("ATTD_DATE")
											+ "','SANCTION',1)";
									st4.execute(leave);
									totPl++;
									bal = rs5.getFloat("BAL");
									totCr = rs5.getFloat("TOTCR");
									// String leave2
									// ="insert into LEAVEBAL VALUES ('"+rs1.getString("ATTD_DATE")+"',"+rs5.getInt("EMPNO")+",1,"+rs5.getFloat("BAL")+"-1,"+rs5.getFloat("TOTCR")+",1)";
									// st4.execute(leave2);
								} else {
									Statement st4 = conn.createStatement();
									String leave = "insert into LEAVETRAN (LEAVECD,EMPNO,TRNDATE,TRNTYPE,APPLDT,FRMDT,TODT,STATUS,DAYS) VALUES (1,"
											+ rs1.getInt("EMPNO")
											+ ",'"
											+ rs1.getString("ATTD_DATE")
											+ "','D','"
											+ rs1.getString("ATTD_DATE")
											+ "','"
											+ rs1.getString("ATTD_DATE")
											+ "','"
											+ rs1.getString("ATTD_DATE")
											+ "','SANCTION',1)";
									st4.execute(leave);
									totPl++;
								}
							}
						}
						System.out.println("for empno.."+rs.getInt("empno")+" halfday is..."+halfday);
						Statement st3 = conn.createStatement();
						if (totPl != 0) {
							bal = bal - totPl;
							String leave2 = "insert into LEAVEBAL VALUES ('25-"
									+ dt[1] + "-" + dt[2] + "',"
									+ rs.getInt("EMPNO") + ",1," + bal + ","
									+ totCr + "," + totPl + ")";
							st3.execute(leave2);
						}
						
						if (halfday != 0) {

							halfday=halfday/2;
							
							String absentdel=("select distinct EMPNO  from EMP_ATTENDANCE where  ATTD_DATE between "+
														" '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and EMPNO ="+ rs.getString("empno") +" and ATTD_VAL !='A' ") ;
							
							String chkquery=("delete from PAYTRAN where TRNCD=301 and EMPNO="+ rs.getString("empno") +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ");
							st3.execute(chkquery);
							
							System.out.println("inside /2 for empno.."+rs.getInt("empno")+" halfday is..."+halfday);
							String sql="if exists(select * from PAYTRAN where TRNCD=301 and EMPNO="+ rs.getString("empno") +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) "
										+"update PAYTRAN  set  INP_AMT=INP_AMT+"+halfday+"   where  TRNCD=301 and EMPNO="+ rs.getString("empno") +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'"
										+" else  "
										+ "INSERT INTO PAYTRAN VALUES('"+ReportDAO.EOM(date)+"',"
										+ rs.getString("empno") + ",301,0,"
										+ halfday + ",0,0,0," + halfday
										+ ",'0','','"+ReportDAO.getSysDate()+"','P')";
							
							st3.execute(sql);
							
						     sql="update sal_details set sal_status='AutoInst' where empno="+rs.getString("empno")+" and sal_month = '"+ReportDAO.BOM(date).substring(3,11)+"' ";
						     st3.execute(sql);
							
							
							  sql="delete from paytran where trncd=301  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'  " +
						        		" and empno=(select distinct empno from paytran_stage where EMPNO="+rs.getString("empno")+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) ";
						        
						        System.out.println("for each emp for empno ..."+rs.getString("empno")+" deleting from paytran for halfdays "+sql);
						        st3.execute(sql);
							
						}
						
                       

					}*/

					Statement st = conn.createStatement();
			//		System.out.println("EMPO"+eno);
					st.executeUpdate("IF EXISTS (SELECT * from Attendance_status where site_id="
							+ site_id
							+ " and att_month between '"
							+ ReportDAO.BOM(date)
							+ "' and '"
							+ ReportDAO.EOM(date)
							+ "')"
							+ " update  Attendance_status set appr_status='"
							+ status
							+ "', updated_by= "+eno+" , updated_date= '"+currentdate+"' ,submit_date='"
							+ ReportDAO.getSysDate()
							+ "' where site_id="
							+ site_id
							+ " and att_month between '"
							+ ReportDAO.BOM(date)
							+ "' and '"
							+ ReportDAO.EOM(date)
							+ "' "
							+ " ELSE insert into Attendance_status  (site_id, appr_date, appr_status, att_month, submit_date, created_by, created_date) values("
							+ site_id
							+ ",'"
							+ ReportDAO.getSysDate()
							+ "','"
							+ status
							+ "','"
							+ date
							+ "','"
							+ ReportDAO.getSysDate() + "', "+eno+" , '"+currentdate+"')");
					
					
					System.out.println("last set att_status..approved");
				
				Statement pre = conn.createStatement();
			/*	String Pr = "update EMP_ATTENDANCE set ATTD_VAL='P' where ATTD_VAL='' and ATTD_DATE between '"
						+ ReportDAO.BOM(date)
						+ "' and '"
						+ ReportDAO.EOM(date)
						+ "'";
				pre.execute(Pr);*/
			} else {
				System.out.println("changing status for sheet to pending for site.."+site_id+" and status.."+status+" sheet date.."+date);
				Statement st = conn.createStatement();
				st.executeUpdate("IF EXISTS (SELECT * from Attendance_status where site_id="
						+ site_id
						+ " and att_month between '"
						+ ReportDAO.BOM(date)
						+ "' and '"
						+ ReportDAO.EOM(date)
						+ "')"
						+ " update  Attendance_status set appr_status='"
						+ status
						+ "', updated_by= "+eno+" , updated_date= '"+currentdate+"', submit_date='"
						+ ReportDAO.getSysDate()
						+ "' where site_id="
						+ site_id
						+ " and att_month between '"
						+ ReportDAO.BOM(date)
						+ "' and '"
						+ ReportDAO.EOM(date)
						+ "' "
						+ " ELSE insert into Attendance_status (site_id, appr_date, appr_status, att_month, submit_date, created_by, created_date) values("
						+ site_id
						+ ",'"
						+ ReportDAO.getSysDate()
						+ "','"
						+ status
						+ "','"
						+ date
						+ "' ,'"
						+ ReportDAO.getSysDate() + "' ,"+eno+", '"+currentdate+"' )");

			}

			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				System.out.println("Error in closing connection");
				e.printStackTrace();
			}
		}
		return status;
	}

	public String getAttendanceStatus(String date, int site_id) {

		// System.out.println("into getAttendanceStatus");
		ResultSet rs = null;
		int count = 0;
		String status = "";
		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
		
			rs = st.executeQuery("SELECT appr_status from Attendance_status where site_id="
					+ site_id
					+ " and att_month between '"
					+ ReportDAO.BOM(date)
					+ "' and '"
					+ ReportDAO.EOM(date)
					+ "'");
			
			while (rs.next()) {
				status = rs.getString(1);
				count++;
			}
			if (count < 1) {
				//status = "saved";
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	public ArrayList<AttendStatusBean> getAllProjectAttendanceStatus(
			ArrayList<EmpOffBean> projlist, String status, String Date) {
		ArrayList<AttendStatusBean> proj_attend_state = new ArrayList<AttendStatusBean>();
		ResultSet rs = null;

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			/*
			 * for(EmpOffBean lkb :projlist) {
			 */int count = 0;
			if (status.equalsIgnoreCase("all")) {
				rs = st.executeQuery("SELECT * from Attendance_status where appr_status<>'saved' and " +
						"att_month between '"+ReportDAO.BOM(Date)+"' and '"+ReportDAO.EOM(Date)+"' order by att_month,appr_status ");// and
																										// att_month
																										// between
																										// '"+ReportDAO.BOM(date)+"'
																										// and
																										// '"+ReportDAO.EOM(date)+"'
																										// ");
			} else if(status.equalsIgnoreCase("left")) {
				rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
			}
			else if(status.equalsIgnoreCase("transfer")) {
				rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
			}
			else if(status.equalsIgnoreCase("SingleApprove")) {
				rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
			}
			
			else {

				rs = st.executeQuery("SELECT * from Attendance_status where  appr_status='"
												+ status + "' and att_month between  '"+ReportDAO.BOM(Date)+"'  and  '"+ReportDAO.EOM(Date)+"'  " +
												"and empno is  null order by att_month ");// and att_month
															// between
															// '"+ReportDAO.BOM(date)+"'
															// and
															// '"+ReportDAO.EOM(date)+"'
															// ");
			}
			while (rs.next()) {
				AttendStatusBean asb = new AttendStatusBean();
				asb.setSite_id(rs.getInt(1));
				asb.setAppr_DATE(format.format(rs.getDate(2)));
				asb.setAppr_status(rs.getString(3));
				asb.setAtt_month(format.format(rs.getDate(4)));
				asb.setSubmit_date(format.format(rs.getDate(5)));
				asb.setEmpno(rs.getInt(10));
				asb.setTranEvent(rs.getString(11));
				proj_attend_state.add(asb);
				count++;
			}
			/*
			 * if(count<1) // if value not found in table place status 0 default
			 * { AttendStatusBean asb=new AttendStatusBean();
			 * asb.setSite_id(lkb.getPrj_srno()); asb.setAppr_DATE("");
			 * asb.setAppr_status(""); asb.setAtt_month("");
			 * proj_attend_state.add(asb); }
			 */

			/* } */
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return proj_attend_state;
	}

	
	public ArrayList<AttendStatusBean> getAllAttendanceStatus(ArrayList<EmpOffBean> projlist, String status,String date,String disableaprove,String siteid) {
		ArrayList<AttendStatusBean> proj_attend_state = new ArrayList<AttendStatusBean>();
		ResultSet rs = null;

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			int count = 0;
			
			if (status.equalsIgnoreCase("all")) {
				rs = st.executeQuery("SELECT * from Attendance_status where att_month between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' order by att_month,appr_status ");
			} 
			else if(status.equalsIgnoreCase("left")) {
				rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
			}
			else if(status.equalsIgnoreCase("transfer")) {
				rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
			}
			
			else if(status.equalsIgnoreCase("SingleApprove")) {
				rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
			}
			
			else {
				if(!disableaprove.equalsIgnoreCase("true"))
				{
				rs = st.executeQuery("SELECT * from Attendance_status where   att_month between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and  appr_status='"+ status + "'");
				
				System.out.println("for saved status..SELECT * from Attendance_status where   att_month between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and  appr_status='"
						+ status + "' and empno is null order by att_month ");
				}
				else
				{
					rs = st.executeQuery("select * from ATTENDANCE_STATUS where EMPNO in( select distinct empno from EMP_ATTENDANCE where ATTD_DATE between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and site_id='"+siteid+"' and EMP_STATUS in('left','transfer')) and att_month between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and appr_status='pending' ");
						System.out.println("select * from ATTENDANCE_STATUS where EMPNO in( select distinct empno from EMP_ATTENDANCE where ATTD_DATE between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and site_id='"+siteid+"' and EMP_STATUS in('left','transfer')) and att_month between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and appr_status='pending' ");
				}
			}
			while (rs.next()) {
				AttendStatusBean asb = new AttendStatusBean();
				asb.setSite_id(rs.getInt(1));
				asb.setAppr_DATE(format.format(rs.getDate(2)));
				asb.setAppr_status(rs.getString(3));
				asb.setAtt_month(format.format(rs.getDate(4)));
				asb.setSubmit_date(format.format(rs.getDate(5)));
				asb.setEmpno(rs.getInt(10));
				asb.setTranEvent(rs.getString(11));
				proj_attend_state.add(asb);
				count++;
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
   /* System.out.println("size of proj_attend_state "+proj_attend_state.size());*/
		return proj_attend_state;
	}

	
	
	
	
	
	
	
	
	

	public boolean leftEmpAttendance(ArrayList emp_al, int eno) {
	
		
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar c1 = Calendar.getInstance(); // today
	
		
		
		
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String currentdate=EAH.getServerDate();

		try {
			
			Connection conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);

			Statement st = conn.createStatement();
			Statement ad= conn.createStatement();
			ResultSet rs = null;
			for (int j = 0; j < emp_al.size(); j++) {
			
				ArrayList<Attend_bean> Emp_bean = (ArrayList<Attend_bean>) emp_al.get(j);

				for (int c = 0; c < Emp_bean.size(); c++) {
					Attend_bean ab = new Attend_bean();
					ab = Emp_bean.get(c);
          
          String BomDt = ReportDAO.BOM(ab.getDate());
  		String EomDt= ReportDAO.EOM(ab.getDate());
  		System.out.println("leftEmpAttendance");
					st.executeUpdate("update emp_attendance set EMP_STATUS='left' where EMPNO="+ab.getEmpno()+"  and site_id="+ab.getSite_id()+" and  attd_date between '"+BomDt+ "' and  '"+EomDt+  "'     ");
  	//	st.executeUpdate("update emp_attendance set EMP_STATUS='left' where EMPNO="+ab.getEmpno()+"   and  attd_date between '"+BomDt+ "' and  '"+EomDt+  "'     ");
			/*		ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" ");*/
		

				}
					

			}
		
			conn.commit();
			
				
			flag = true;

		} catch (SQLException e) {
			// System.out.println("error into insertEmpAttendance");
			e.printStackTrace();
		}
		return flag;
	}
	
	
	
public boolean singleEmpAttendance(ArrayList emp_al, int eno) {
	
		
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar c1 = Calendar.getInstance(); // today
	
		
		
		
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String currentdate=EAH.getServerDate();

		try {
			
			Connection conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);

			Statement st = conn.createStatement();
			Statement ad= conn.createStatement();
			ResultSet rs = null;
			for (int j = 0; j < emp_al.size(); j++) {
			
				ArrayList<Attend_bean> Emp_bean = (ArrayList<Attend_bean>) emp_al.get(j);

				for (int c = 0; c < Emp_bean.size(); c++) {
					Attend_bean ab = new Attend_bean();
					ab = Emp_bean.get(c);
          
          String BomDt = ReportDAO.BOM(ab.getDate());
  		String EomDt= ReportDAO.EOM(ab.getDate());
  		System.out.println("singleEmpAttendance");
					st.executeUpdate("update emp_attendance set EMP_STATUS='SingleApprove' where EMPNO="+ab.getEmpno()+"  and site_id="+ab.getSite_id()+" and  attd_date between '"+BomDt+ "' and  '"+EomDt+  "'     ");
  	//	st.executeUpdate("update emp_attendance set EMP_STATUS='left' where EMPNO="+ab.getEmpno()+"   and  attd_date between '"+BomDt+ "' and  '"+EomDt+  "'     ");
			/*		ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" ");*/
		

				}
					

			}
		
			conn.commit();
			
				
			flag = true;

		} catch (SQLException e) {
			// System.out.println("error into insertEmpAttendance");
			e.printStackTrace();
		}
		return flag;
	}
	
	
public boolean transferEmpAttendance(ArrayList emp_al, int eno) {
	
	
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	Calendar c1 = Calendar.getInstance(); // today

	
	
	
	EmpAttendanceHandler EAH=new EmpAttendanceHandler();
	String currentdate=EAH.getServerDate();

	try {
		
		Connection conn = ConnectionManager.getConnection();
		conn.setAutoCommit(false);

		Statement st = conn.createStatement();
		Statement ad= conn.createStatement();
		ResultSet rs = null;
		for (int j = 0; j < emp_al.size(); j++) {
		
			ArrayList<Attend_bean> Emp_bean = (ArrayList<Attend_bean>) emp_al.get(j);

			for (int c = 0; c < Emp_bean.size(); c++) {
				Attend_bean ab = new Attend_bean();
				ab = Emp_bean.get(c);
      
      String BomDt = ReportDAO.BOM(ab.getDate());
		String EomDt= ReportDAO.EOM(ab.getDate());
		
				st.executeUpdate("update emp_attendance set EMP_STATUS='transfer' where EMPNO="+ab.getEmpno()+"  and site_id="+ab.getSite_id()+" and  attd_date between '"+BomDt+ "' and  '"+EomDt+  "'     ");
		//st.executeUpdate("update emp_attendance set EMP_STATUS='transfer' where EMPNO="+ab.getEmpno()+"  and  attd_date between '"+BomDt+ "' and  '"+EomDt+  "'     ");
		/*		ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" ");*/
	

			}
				

		}
	
		conn.commit();
		
			
		flag = true;

	} catch (SQLException e) {
		// System.out.println("error into insertEmpAttendance");
		e.printStackTrace();
	}
	return flag;
}
	

	
	
	
	
	
	public String getServerDate() {

		Connection conn = ConnectionManager.getConnection();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String today = "";
		try {
			conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			String s = "select getdate() as date";
			ResultSet rslt = st.executeQuery(s);
			if (rslt.next()) {
				today = sdf.format(rslt.getDate("date"));
			}

			conn.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return today;
	}
	
	public String setLeftStatus(ArrayList Emp_left,String date,String status, int eno) throws SQLException{
	
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String currentdate=EAH.getServerDate();
		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
		String tranEvent="left";
		status="pending";
		for (int j = 0; j < Emp_left.size(); j++) {
			
			ArrayList<Attend_bean> Emp_bean = (ArrayList<Attend_bean>) Emp_left.get(j);

			for (int c = 0; c < Emp_bean.size(); c++) {
				Attend_bean ab = new Attend_bean();
				ab = Emp_bean.get(c);
				
      String BomDt = ReportDAO.BOM(ab.getDate());
		String EomDt= ReportDAO.EOM(ab.getDate());
		
		st.executeUpdate("IF EXISTS (SELECT * from Attendance_status where  empno="+ab.getEmpno()+"   and tranevent='left' )"
												+"IF EXISTS (SELECT * from Attendance_status where  empno="+ab.getEmpno()+"   and tranevent='left'   and appr_status='rejected')" 
													+"IF EXISTS (SELECT * from Attendance_status where  empno="+ab.getEmpno()+"   and tranevent='left'   and appr_status='pending')"
													+"update  Attendance_status set appr_status='pending' ,"
													+" updated_by="+eno+",updated_date= '"+currentdate+"',submit_date='"+ReportDAO.getSysDate()+"',att_month='"+date+"' "
													+"where appr_status='pending' and empno="+ab.getEmpno()+"   and tranevent='left' "
													
													+"ELSE insert into Attendance_status  ( appr_date, appr_status, att_month, submit_date, created_by, created_date,empno,tranevent) values('"
													+ ReportDAO.getSysDate()+ "','"
													+ status+ "','"
													+ date+ "','"
													+ ReportDAO.getSysDate() + "', "+eno+" , '"+currentdate+"',"+ab.getEmpno()+",'"+tranEvent+"')"
												
												+"ELSE update  Attendance_status set appr_status='pending' ,"
												+" updated_by="+eno+",updated_date= '"+currentdate+"',submit_date='"+ReportDAO.getSysDate()+"',att_month='"+date+"' "
												+"where appr_status='pending' and empno="+ab.getEmpno()+"   and tranevent='left' "
				+"ELSE insert into Attendance_status  ( appr_date, appr_status, att_month, submit_date, created_by, created_date,empno,tranevent) values('"
				+ ReportDAO.getSysDate()
				+ "','"
				+ status
				+ "','"
				+ date
				+ "','"
				+ ReportDAO.getSysDate() + "', "+eno+" , '"+currentdate+"',"+ab.getEmpno()+",'"+tranEvent+"')");
	
			}
		}
		return status;
	}

	
	
	public String setSingleApproveStatus(ArrayList Emp_left,String date,String status, int eno) throws SQLException{
		
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String currentdate=EAH.getServerDate();
		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
		String tranEvent="SingleApprove";
		status="pending";
		for (int j = 0; j < Emp_left.size(); j++) {
			
			ArrayList<Attend_bean> Emp_bean = (ArrayList<Attend_bean>) Emp_left.get(j);

			for (int c = 0; c < Emp_bean.size(); c++) {
				Attend_bean ab = new Attend_bean();
				ab = Emp_bean.get(c);
				
      String BomDt = ReportDAO.BOM(ab.getDate());
		String EomDt= ReportDAO.EOM(ab.getDate());
		
		System.out.println("IF EXISTS (SELECT * from Attendance_status where  empno="+ab.getEmpno()+"  and tranevent='SingleApprove' AND appr_status = 'pending' and att_month between  '"+BomDt+ "' and  '"+EomDt+  "' )"
				+"update  Attendance_status set appr_status='pending' ,"
				 +" updated_by="+eno+",updated_date= '"+currentdate+"',submit_date='"+ReportDAO.getSysDate()+"'"
				  +"where att_month between  '"+BomDt+ "' and  '"+EomDt+  "'   and  "
				  +"appr_status='pending' and empno="+ab.getEmpno()+"   and tranevent='SingleApprove' "
				+"ELSE insert into Attendance_status  ( appr_date, appr_status, att_month, submit_date, created_by, created_date,empno,tranevent) values('"
				+ ReportDAO.getSysDate()
				+ "','"
				+ status
				+ "','"
				+ date
				+ "','"
				+ ReportDAO.getSysDate() + "', "+eno+" , '"+currentdate+"',"+ab.getEmpno()+",'"+tranEvent+"')");
		
		st.executeUpdate("IF EXISTS (SELECT * from Attendance_status where  empno="+ab.getEmpno()+"  and tranevent='SingleApprove' AND appr_status = 'pending' and att_month between  '"+BomDt+ "' and  '"+EomDt+  "' )"
				+"update  Attendance_status set appr_status='pending' ,"
				 +" updated_by="+eno+",updated_date= '"+currentdate+"',submit_date='"+ReportDAO.getSysDate()+"'"
				  +"where att_month between  '"+BomDt+ "' and  '"+EomDt+  "'   and  "
				  +"appr_status='pending' and empno="+ab.getEmpno()+"   and tranevent='SingleApprove' "
				+"ELSE insert into Attendance_status  ( appr_date, appr_status, att_month, submit_date, created_by, created_date,empno,tranevent) values('"
				+ ReportDAO.getSysDate()
				+ "','"
				+ status
				+ "','"
				+ date
				+ "','"
				+ ReportDAO.getSysDate() + "', "+eno+" , '"+currentdate+"',"+ab.getEmpno()+",'"+tranEvent+"')");
	
			}
		}
		return status;
	}
	
	
	public String setTransferStatus(ArrayList Emp_tran,String date,String status, int eno) throws SQLException{
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String currentdate=EAH.getServerDate();
		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
		String tranEvent="transfer";
		status="pending";
		for (int j = 0; j < Emp_tran.size(); j++) {

			ArrayList<Attend_bean> Emp_bean = (ArrayList<Attend_bean>) Emp_tran.get(j);

			for (int c = 0; c < Emp_bean.size(); c++) {
				Attend_bean ab = new Attend_bean();
				ab = Emp_bean.get(c);
				
      String BomDt = ReportDAO.BOM(ab.getDate());
		String EomDt= ReportDAO.EOM(ab.getDate());
		
		st.executeUpdate("IF EXISTS (SELECT * from Attendance_status where  empno="+ab.getEmpno()+"   and tranevent='transfer' and att_month between  '"+BomDt+ "' and  '"+EomDt+  "' and appr_status='pending' )"
				+"update  Attendance_status set appr_status='pending' ,"
				 +" updated_by="+eno+",updated_date= '"+currentdate+"',submit_date='"+ReportDAO.getSysDate()+"', att_month='"+date+"' "
				  +"where att_month between  '"+BomDt+ "' and  '"+EomDt+  "'   and  "
				  +"appr_status='pending' and empno="+ab.getEmpno()+"  and tranevent='transfer' "
				+"ELSE insert into Attendance_status  ( appr_date, appr_status, att_month, submit_date, created_by, created_date,empno,tranevent) values('"
				+ ReportDAO.getSysDate()
				+ "','"
				+ status
				+ "','"
				+ date
				+ "','"
				+ ReportDAO.getSysDate() + "', "+eno+" , '"+currentdate+"',"+ab.getEmpno()+",'"+tranEvent+"')");
	
			/*System.out.println("transfer query...IF EXISTS (SELECT * from Attendance_status where  empno="+ab.getEmpno()+"   and tranevent='transfer' and att_month between  '"+BomDt+ "' and  '"+EomDt+  "' and appr_status='pending' )"
					+"update  Attendance_status set appr_status='pending' ,"
					 +" updated_by="+eno+",updated_date= '"+currentdate+"',submit_date='"+ReportDAO.getSysDate()+"' , att_month='"+date+"' "
					  +"where att_month between  '"+BomDt+ "' and  '"+EomDt+  "'   and  "
					  +"appr_status='pending' and empno="+ab.getEmpno()+"  and tranevent='transfer' "
					+"ELSE insert into Attendance_status  ( appr_date, appr_status, att_month, submit_date, created_by, created_date,empno,tranevent) values('"
					+ ReportDAO.getSysDate()
					+ "','"
					+ status
					+ "','"
					+ date
					+ "','"
					+ ReportDAO.getSysDate() + "', "+eno+" , '"+currentdate+"',"+ab.getEmpno()+",'"+tranEvent+"')");*/
			}
		}
		return status;
	}
	public ArrayList getEmpAttendApproval(String date, int empNo, String statusApprove,String event) {
		System.out.println("getEmpAttendApproval");
	
		ArrayList Emp_al=new ArrayList();
		ArrayList<Attend_bean> attend = new ArrayList<Attend_bean>();
 
	
		float days = Calculate.getDays(date);



		Connection conn = ConnectionManager.getConnection();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = null;
			
				
				int count = 0;
				String date1 = ReportDAO.BOM(date);
				rs = st.executeQuery("select * from Emp_attendance where empno="
						+ empNo
						+ " and attd_date between '"
						+ ReportDAO.BOM(date)
						+ "' and  '"
						+ ReportDAO.EOM(date)
						+ "' and EMP_STATUS='"+event+"' order by attd_date");
			
				while (rs.next()) {
					Attend_bean ab = new Attend_bean();
					ab.setEmpno(rs.getString(1));
					
					ab.setDate(rs.getString(3));
				
					ab.setVal(rs.getString(4));
					ab.setStatus(rs.getString(9));
					attend.add(ab);
					count++;
				}
	
			
			Emp_al.add(attend);
			conn.close();

		} catch (SQLException e) {
			// //System.out.println("error into EmpAttendanceHandler");
			e.printStackTrace();
		}

		return Emp_al;
	}
	
	public String setAttendanceTransferApproved(String date, int empNo,String status,int eno ) throws SQLException{
	

		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
System.out.println("in transfer..");
		String sql=("update  Attendance_status set appr_status='approved' ,updated_by="+eno+",updated_date= '"+ReportDAO.getSysDate()+"' where empno="+empNo+" and tranevent='"+status+"'  "
				  +"and att_month = '"+date+ "'   and  "
				  +"appr_status='pending'  ");
		
		System.out.println("updating transfer..... "+sql);
		st.executeUpdate(sql);
		
		String transfer="UPDATE EMP_ATTENDANCE SET ATTD_VAL=' ' , SITE_ID=NULL WHERE EMPNO="+empNo+" " 
							+	"AND ATTD_DATE BETWEEN  '"+date+ "' AND '"+ReportDAO.EOM(date)+ "' AND ATTD_VAL!='WO' AND ATTD_VAL!='HD' AND ATTD_VAL!='NJ' AND ATTD_VAL!='LT'   ";
	
		st.executeUpdate(transfer);
		
		
		conn.commit();
  	   status="approved";
		
		return status;
	}
	
	public String setAttendanceSingleEmpApproved(String date, int empNo,String status,int eno ) throws SQLException{
		

		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
		Statement rs = conn.createStatement();
		
		int Site_id = 0;

		String sql ="update  Attendance_status set appr_status='approved' ,updated_by="+eno+",updated_date= '"+ReportDAO.getSysDate()+"' where empno="+empNo+" and tranevent='"+status+"'  "
				 
				  +"and att_month = '"+date+ "'   and  "
				  +"appr_status='pending'  ";
				
	System.out.println("signle aprv qurey.. "+sql);
	System.out.println("signle aprv date.. "+date+"...empno.."+empNo+"..status.."+status);
		st.executeUpdate(sql);
	
		conn.commit();
	   status="approved";
		
	   ResultSet rst = null;
	   Statement stmt=conn.createStatement();
	


	   EmpOffHandler eoffhdlr= new EmpOffHandler();
		ArrayList<TranBean> tran = new ArrayList<TranBean>();
	
                        java.sql.CallableStatement cst1= conn.prepareCall("{call SPC_CHECKING_SUNDAY_ABSENT(?,?,?) }");
						cst1.setString(1,ReportDAO.BOM(date));
						cst1.setString(2,ReportDAO.EOM(date));
					 
						cst1.setInt(3,empNo)  ;  
						
						ResultSet rs2 = cst1.executeQuery();
	   
						  while (rs2.next()) {
						    	
						    	Statement statement = conn.createStatement();
						    	
						        String EMPLOYEENUMBER = rs2.getString(1);
						        String ATTENDANCE_COUNT = rs2.getString(2);
						        
						    
						        System.out.println("ATTENDANCE_COUNT...."+ATTENDANCE_COUNT+"  for empno.."+EMPLOYEENUMBER);
						        String sqlLeft="if exists(select * from PAYTRAN where TRNCD=301 and EMPNO="+EMPLOYEENUMBER+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) "
										+"update PAYTRAN  set  INP_AMT="+ATTENDANCE_COUNT+"   where  TRNCD=301 and EMPNO="+ EMPLOYEENUMBER+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'"
										+" else  "
										+ "INSERT INTO PAYTRAN VALUES('"+ReportDAO.EOM(date)+"' ,"
										+ EMPLOYEENUMBER + ",301,0,"
										+ ATTENDANCE_COUNT + ",0,0,0," + ATTENDANCE_COUNT
										+ ",'0','','"+ReportDAO.getSysDate()+"','P')";
							
						        statement.execute(sqlLeft);
						        
						        
						        sql="update sal_details set sal_status='AutoInst' where empno="+EMPLOYEENUMBER+" and sal_month = '"+ReportDAO.BOM(date).substring(3,11)+"' ";
						        statement.execute(sql);
						        
						      } 
	   
					
						  
						 // for halfday
						  
						  Statement st2 = conn.createStatement();
						  Statement st1 = conn.createStatement();
						  
						  
						  /*ResultSet rs3 = st2.executeQuery("select distinct empno from EMP_ATTENDANCE where ATTD_DATE between '"
									+ ReportDAO.BOM(date)
									+ "' and '"
									+ ReportDAO.EOM(date)
									+ "' " +
								//	"and site_id="
								//	+ Site_id + "" +
								" order by EMPNO ");
						  
						  System.out.println("hf......select distinct empno from EMP_ATTENDANCE where ATTD_DATE between '"
									+ ReportDAO.BOM(date)
									+ "' and '"
									+ ReportDAO.EOM(date)
									+ "' " +
									"and site_id="
									+ Site_id + "" +
								" order by EMPNO ");
					
					while (rs3.next()) {*/
						float totDed = 0;
						float halfday=0;
						float totPl = 0;
						float totCr = 0;
						float bal = 0;
						ResultSet rs1 = st1
								.executeQuery("SELECT EMPNO,ATTD_DATE,ATTD_VAL FROM EMP_ATTENDANCE where ATTD_DATE between '"
										+ ReportDAO.BOM(date)
										+ "' and '"
										+ ReportDAO.EOM(date)
										+ "' and EMPNO="
										+ empNo
										+ " order by ATTD_DATE");
						while (rs1.next()) {
					
							 if (rs1.getString("ATTD_VAL").equalsIgnoreCase("H")) {
								halfday++;

							}
													
						}
						Statement st3 = conn.createStatement();
						System.out.println("halfday......"+halfday);
						
						if (halfday != 0) {

							halfday=halfday/2;
							
							System.out.println("halfday....222.."+halfday);
							 sql="if exists(select * from PAYTRAN where TRNCD=301 and EMPNO="+ empNo +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) "
										+"update PAYTRAN  set  INP_AMT=INP_AMT+"+halfday+"   where  TRNCD=301 and EMPNO="+ empNo +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'"
										+" else  "
										+ "INSERT INTO PAYTRAN VALUES('"+ReportDAO.EOM(date)+"',"
										+ empNo + ",301,0,"
										+ halfday + ",0,0,0," + halfday
										+ ",'0','','"+ReportDAO.getSysDate()+"','P')";
							
							st3.execute(sql);
							
						    sql="update sal_details set sal_status='AutoInst' where empno="+ empNo +" and sal_month = '"+ReportDAO.BOM(date).substring(3,11)+"' ";
						    st3.execute(sql);
					        
					       
							
						}
						
                     

					//}
						  
						  
						  
		return status;
	}
	
	
public String setAttendanceLeftApproved(String date, int empNo,String status,int eno ) throws SQLException{
		

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			Statement rs = conn.createStatement();
			
			int Site_id = 0;
			System.out.println("query for left////");
			String sql ="update  Attendance_status set appr_status='approved' ,updated_by="+eno+",updated_date= '"+ReportDAO.getSysDate()+"' where empno="+empNo+" and tranevent='"+status+"'  "
					 
					  +"and att_month between '"+ReportDAO.BOM(date)+ "'  and '"+ReportDAO.EOM(date)+"'  and  "
					  +"appr_status='pending'  ";
					
			System.out.println("query for left"+sql);

			st.executeUpdate(sql);
			String setLeft="update emp_attendance set attd_val= 'LT' where empno= "+empNo+" and " +
					"attd_date>'"+date+"'  and EMP_STATUS='"+status+"'";		
			rs.executeUpdate(setLeft);
			conn.commit();
   status="approved";
			
   ResultSet rst = null;
   
   // no need for single employee left as we r calling spc for single emp
 /*  Statement stmt=conn.createStatement();
   String query=" select prj_srno from emptran where EMPNO="+empNo+" and SRNO=(select MAX(Srno) from EMPTRAN where EMPNO="+empNo+" )";
   rst=stmt.executeQuery(query);
   while(rst.next())
   {
			   Site_id=rst.getInt("prj_srno");
   }*/


   EmpOffHandler eoffhdlr= new EmpOffHandler();
			ArrayList<TranBean> tran = new ArrayList<TranBean>();
		
			//tran = eoffhdlr.getEmpListForAttendance(""+Site_id,date);
			
			Statement statement=null;
			String EMPLOYEENUMBER="";
			
							/*for (TranBean tb : tran){*/

							java.sql.CallableStatement cst1= conn.prepareCall("{call SPC_CHECKING_SUNDAY_ABSENT(?,?,?) }");
							cst1.setString(1,ReportDAO.BOM(date));
							cst1.setString(2,ReportDAO.EOM(date));
							//cst1.setInt(3,tb.getEMPNO())  ;  
							cst1.setInt(3,empNo)  ;   
							
							ResultSet rs2 = cst1.executeQuery();
							  while (rs2.next()) {
							    	
							    	 statement = conn.createStatement();
							    	
							        EMPLOYEENUMBER = rs2.getString(1);
							        String ATTENDANCE_COUNT = rs2.getString(2);
							        
							        //System.out.println("Generated employeeId: " + EMPLOYEENUMBER+""+ATTENDANCE_COUNT);
							        
							        String sqlLeft="if exists(select * from PAYTRAN where TRNCD=301 and EMPNO="+EMPLOYEENUMBER+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) "
											+"update PAYTRAN  set  INP_AMT="+ATTENDANCE_COUNT+"   where  TRNCD=301 and EMPNO="+ EMPLOYEENUMBER+"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'"
											+" else  "
											+ "INSERT INTO PAYTRAN VALUES('"+ReportDAO.EOM(date)+"' ,"
											+ EMPLOYEENUMBER + ",301,0,"
											+ ATTENDANCE_COUNT + ",0,0,0," + ATTENDANCE_COUNT
											+ ",'0','','"+ReportDAO.getSysDate()+"','P')";
								
							        statement.execute(sqlLeft);
							        
							        //statement.executeUpdate("update EMPMAST SET DOL = '"+date+"',STATUS = 'N', UMODDATE = (select GETDATE()) where EMPNO ="+EMPLOYEENUMBER+"");			 
							        
							        sql="update sal_details set sal_status='AutoInst' where empno="+EMPLOYEENUMBER+" and sal_month = '"+ReportDAO.BOM(date).substring(3,11)+" '";
							        System.out.println(sql);
							        statement.execute(sql);
							        
							      } 
								/*}*/
   
							  
							  
							// for halfday
							  
							  Statement st2 = conn.createStatement();
							  Statement st1 = conn.createStatement();
							  
							  
							  ResultSet rs3 = st2.executeQuery("select distinct empno from EMP_ATTENDANCE where ATTD_DATE between '"
										+ ReportDAO.BOM(date)
										+ "' and '"
										+ ReportDAO.EOM(date)
										+ "' " +
										"and site_id="
										+ Site_id + "" +
									" order by EMPNO ");
						
						while (rs3.next()) {
							float totDed = 0;
							float halfday=0;
							float totPl = 0;
							float totCr = 0;
							float bal = 0;
							ResultSet rs1 = st1
									.executeQuery("SELECT EMPNO,ATTD_DATE,ATTD_VAL FROM EMP_ATTENDANCE where ATTD_DATE between '"
											+ ReportDAO.BOM(date)
											+ "' and '"
											+ ReportDAO.EOM(date)
											+ "' and EMPNO="
											+ rs3.getInt("empno")
											+ " order by ATTD_DATE");
							while (rs1.next()) {
						
								 if (rs1.getString("ATTD_VAL").equalsIgnoreCase("H")) {
									halfday++;

								}
														
							}
							Statement st3 = conn.createStatement();
						
							
							if (halfday != 0) {

								halfday=halfday/2;
								
								
								 sql="if exists(select * from PAYTRAN where TRNCD=301 and EMPNO="+ rs3.getString("empno") +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' ) "
											+"update PAYTRAN  set  INP_AMT=INP_AMT+"+halfday+"   where  TRNCD=301 and EMPNO="+ rs3.getString("empno") +"  and  TRNDT between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"'"
											+" else  "
											+ "INSERT INTO PAYTRAN VALUES('"+ReportDAO.EOM(date)+"',"
											+ rs3.getString("empno") + ",301,0,"
											+ halfday + ",0,0,0," + halfday
											+ ",'0','','"+ReportDAO.getSysDate()+"','P')";
								
								st3.execute(sql);
								
							    sql="update sal_details set sal_status='AutoInst' where empno="+ rs3.getString("empno") +" and sal_month = '"+ReportDAO.BOM(date).substring(3,11)+"' ";
							    st3.execute(sql);
						        
						       
								
							}
							
	                     

						}
							  
							  
							  
							  
				
							
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	
	public String setEmpStatusRejected(String date, int empNo,String status,int eno ) throws SQLException{

		
		
		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
	
		String sql="update  Attendance_status set appr_status='rejected' ,updated_by="+eno+",updated_date= '"+ReportDAO.getSysDate()+"' where empno="+empNo+" and tranevent='"+status+"' "/*and "
				 +" submit_date between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"'"*/
				  +"and att_month = '"+date+"'   and  "
				  +"appr_status='pending'  ";
				
		System.out.println("reject update...."+sql);
		st.executeUpdate(sql);
		conn.commit();
	   status="rejected";
		
		return status;
	}

	
	public boolean assignSite(ArrayList siteSelectedList, int empNo)
	{
		
		
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar c1 = Calendar.getInstance(); // today

		
		
		
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String currentdate=EAH.getServerDate();

		try {
			
			Connection conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);

			Statement st = conn.createStatement();
			Statement ad= conn.createStatement();
			ResultSet rs = null;
			
			ArrayList<TranBean> tranList =	 (ArrayList<TranBean>) siteSelectedList.get(0);
			TranBean tranBean1 = tranList.get(0);
int empnum=tranBean1.getEMPNO();
 
 st.executeUpdate("delete  ATTENDANCE_SITE_RIGHTS where empno="+empnum);
 
			for (int j = 0; j < siteSelectedList.size(); j++) {
			
			 tranList = (ArrayList<TranBean>) siteSelectedList.get(j);

				for (int c = 0; c < tranList.size(); c++) {
					TranBean tranBean = new TranBean();
					tranBean = tranList.get(c);
	  
					
			st.executeUpdate("insert into ATTENDANCE_SITE_RIGHTS (empno, site_id , created_by, created_date)" +
					" values("+tranBean.getEMPNO()+" , "+tranBean.getSite_id()+" , "+empNo+", '"+currentdate+"' ) ");
			
		

				}
					

			}
		
			conn.commit();
			
				
			flag = true;

		} catch (SQLException e) {
			// System.out.println("error into insertEmpAttendance");
			e.printStackTrace();
		}
		return flag;
}

	public ArrayList<TranBean> getAssignedSitesList(int empNo) throws SQLException {
		ArrayList<TranBean> Emp_bean = new ArrayList<TranBean>();
		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
		ResultSet rs = null;
		
		rs=st.executeQuery("select site_id from ATTENDANCE_SITE_RIGHTS where empno="+empNo+ " ");
		while (rs.next()) {
			TranBean ab = new TranBean();
		
			ab.setSite_id(rs.getString("site_id"));
			
			Emp_bean.add(ab);
		}
		return Emp_bean;
	}
	
	public boolean deleteAssignedSitesList(int empNo) throws SQLException {
		boolean flag = false;
		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
		
		
		st.executeUpdate("delete from ATTENDANCE_SITE_RIGHTS where empno="+empNo+ " ");
		conn.commit();
		
		
		flag = true;
		return true;
	}
	
	public ArrayList<CompBean> getAssignweek(String Project) throws SQLException {
		ArrayList<CompBean> Emp_bean = new ArrayList<CompBean>();
		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
		ResultSet rs = null;
		
		rs=st.executeQuery("select RepeatHold, HTEXT from HOLDMAST where BRANCH= '"+Project+"' ");
		while (rs.next()) {
			CompBean ab = new CompBean();
		
			ab.setWoffday((rs.getString("RepeatHold")));
			ab.setAltsat((rs.getString("HTEXT")));
			
			Emp_bean.add(ab);
		}
		return Emp_bean;
	}
	
	
	public String getstatus(String site_id,String date) throws SQLException{
		String status="";
		Connection conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
		ResultSet rs = null;
		
		rs=st.executeQuery("select appr_status from attendance_status where site_id= '"+site_id+"' and " +
				"att_month between '"+ReportDAO.BOM(date)+"' and   '"+ReportDAO.EOM(date)+ "' ");
		while (rs.next()) {
			status=rs.getString("appr_status");
		}
		return status; 
	}
	
	
	public boolean weekoff(CompBean cb) throws IOException, SQLException{
		boolean result = true;
		Connection con = null;
	EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
		String d=cb.getWoffday();	
		String day1="";
		String day2="";

if(d.length()==8)
{
 day1= cb.getWoffday().substring(1,4);
 day2= cb.getWoffday().substring(5,8);
}
if(d.length()==4)
{
	day1= cb.getWoffday().substring(1,4);
}
//System.out.println("value of the day"+day1+"  value of the day 2"+day2);

con= ConnectionManager.getConnection();
Statement dt = con.createStatement();


dt.execute(" delete from HOLDMAST  where branch = '"+cb.getCname()+"' " +
	"and  RepeatHold= 'sun' or RepeatHold='mon' or RepeatHold='tue'  "+
	"	 or RepeatHold= 'wed' or RepeatHold= 'thu' or RepeatHold= 'fri' "+
	"	 or RepeatHold= 'sat'  "+
	"	and  htext='1' or htext ='2' or htext='3' or htext='4' or htext='5'  ");

if(day1.equals("sun")|| day2.equals("sun"))
{
SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); // today
String y=empAttendanceHandler.getServerDate();
//String y=sdf.format(c1.getTime());
String years=y.substring(7,11);


int year=Integer.parseInt(years);
//Connection con=null;

Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
// this is a sunday
String frm="";
frm=format.format(cal.getTime());
//System.out.println("From	:"+frm);
try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();
//	System.out.println("in addsunday mast method");
st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold)" +
		" values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','Weekly OFF','Weekly Off','sun','No','No','No','No')");

con.close();
}	
catch(Exception e)
{
e.printStackTrace();
}
System.out.println("the value of the "+i+" sunday is "+format.format(cal.getTime()));
cal.add(Calendar.DAY_OF_MONTH, 7);   
} else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}

}


if(day1.equals("mon")|| day2.equals("mon"))
{

SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); // today

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);


int year=Integer.parseInt(years);


Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {

String frm="";
frm=format.format(cal.getTime());

try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold) " +
		"values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','Weekly OFF','Weekly Off','mon','No','No','No','No')");

con.close();
}	


catch(Exception e)
{
e.printStackTrace();
}
System.out.println("the value of the "+i+" monday is "+format.format(cal.getTime()));
cal.add(Calendar.DAY_OF_MONTH, 7);   
} else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}

}

if(day1.equals("tue")|| day2.equals("tue"))
{

SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); // today

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);


int year=Integer.parseInt(years);
//Connection con=null;

Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {

String frm="";
frm=format.format(cal.getTime());

try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold) " +
		"values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','Weekly OFF','Weekly Off','tue','No','No','No','No')");

con.close();
}	


catch(Exception e)
{
e.printStackTrace();
}
System.out.println("the value of the "+i+" monday is "+format.format(cal.getTime()));
cal.add(Calendar.DAY_OF_MONTH, 7);   
} else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}

}

if(day1.equals("wed")|| day2.equals("wed"))
{

SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); // today

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);


int year=Integer.parseInt(years);
//Connection con=null;

Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {

String frm="";
frm=format.format(cal.getTime());

try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();



st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold) " +
		"values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','Weekly OFF','Weekly Off','wed','No','No','No','No')");

con.close();
}	


catch(Exception e)
{
e.printStackTrace();
}

cal.add(Calendar.DAY_OF_MONTH, 7);   
} else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}

}

if(day1.equals("thu")|| day2.equals("thu"))
{

SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); 

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);


int year=Integer.parseInt(years);
//Connection con=null;

Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {

String frm="";
frm=format.format(cal.getTime());

try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold) " +
		"values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','Weekly OFF','Weekly Off','thu','No','No','No','No')");

con.close();
}	

catch(Exception e)
{
e.printStackTrace();
}

cal.add(Calendar.DAY_OF_MONTH, 7);   
} else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}

}
if(day1.equals("fri")|| day2.equals("fri"))
{

SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); // today

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);


int year=Integer.parseInt(years);
//Connection con=null;

Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
// this is a sunday
String frm="";
frm=format.format(cal.getTime());
//System.out.println("From	:"+frm);
try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold) " +
		"values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','Weekly OFF','Weekly Off','fri','No','No','No','No')");

con.close();
}	
catch(Exception e)
{
e.printStackTrace();
}
System.out.println("the value of the "+i+" monday is "+format.format(cal.getTime()));
cal.add(Calendar.DAY_OF_MONTH, 7);   
} else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}

}
if(day1.equals("sat")|| day2.equals("sat"))
{

SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); // today

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);
int year=Integer.parseInt(years);
//Connection con=null;

Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

String frm="";
frm=format.format(cal.getTime());

try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold) " +
		"values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','Weekly OFF','Weekly Off','sat','No','No','No','No')");

con.close();
}	
catch(Exception e)
{
e.printStackTrace();
}

cal.add(Calendar.DAY_OF_MONTH, 7);   
} else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}
}

//for inserting alternate saturdays

String s=cb.getAltsat(); 
String s1="";
String s2="";
String s3="";
String s4="";
String s5="";

if(s.length()==10)
{
s1=cb.getAltsat().substring(1,2);
s2=cb.getAltsat().substring(3,4);
s3=cb.getAltsat().substring(5,6);
s4=cb.getAltsat().substring(7,8);
s5=cb.getAltsat().substring(9,10);
}

if(s.length()==8)
{
s1=cb.getAltsat().substring(1,2);
s2=cb.getAltsat().substring(3,4);
s3=cb.getAltsat().substring(5,6);
s4=cb.getAltsat().substring(7,8);				
}

if(s.length()==6)
{
s1=cb.getAltsat().substring(1,2);
s2=cb.getAltsat().substring(3,4);
s3=cb.getAltsat().substring(5,6);
}

if(s.length()==4)
{
s1=cb.getAltsat().substring(1,2);
s2=cb.getAltsat().substring(3,4);
}
if(s.length()==2)
{
s1=cb.getAltsat().substring(1,2);
}

if(s1.equals("1") )
{

SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); 

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);
int year=Integer.parseInt(years);

Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

String frm="";
frm=format.format(cal.getTime());

try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold) " +
		"values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','1','Weekly Off','No','No','No','No','No')");
con.close();
}	
catch(Exception e)
{
e.printStackTrace();
}

// System.out.println("the value of the "+i+" saturday is "+format.format(cal.getTime()));
cal.add(Calendar.MONTH,1);
cal.set(Calendar.DAY_OF_MONTH, 1);
} 

else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}
}

if(s1.equals("2")|| s2.equals("2"))
{
SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); 

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);
int year=Integer.parseInt(years);


Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

String frm="";
cal.add(Calendar.DATE, 7);
frm=format.format(cal.getTime());

try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold)" +
		" values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','2','Weekly Off','No','No','No','No','No')");
con.close();
}	
catch(Exception e)
{
e.printStackTrace();
}

// System.out.println("the value of the "+i+" saturday is "+format.format(cal.getTime()));
cal.add(Calendar.MONTH,1);
cal.set(Calendar.DAY_OF_MONTH, 1);
} 

else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}
}


if(s1.equals("3")|| s2.equals("3")|| s3.equals("3"))
{
SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); 

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);
int year=Integer.parseInt(years);


Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

String frm="";
cal.add(Calendar.DATE, 14);
frm=format.format(cal.getTime());

try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold)" +
		" values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','3','Weekly Off','No','No','No','No','No')");
con.close();
}	
catch(Exception e)
{
e.printStackTrace();
}

// System.out.println("the value of the "+i+" saturday is "+format.format(cal.getTime()));
cal.add(Calendar.MONTH,1);
cal.set(Calendar.DAY_OF_MONTH, 1);
} 

else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}
}

if(s1.equals("4")|| s2.equals("4")|| s3.equals("4")||s4.equals("4"))
{
SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); 

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);
int year=Integer.parseInt(years);

Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

String frm="";
cal.add(Calendar.DATE, 21);
frm=format.format(cal.getTime());

try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold) " +
		"values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','4','Weekly Off','No','No','No','No','No')");
con.close();
}	
catch(Exception e)
{
e.printStackTrace();
}

// System.out.println("the value of the "+i+" saturday is "+format.format(cal.getTime()));
cal.add(Calendar.MONTH,1);
cal.set(Calendar.DAY_OF_MONTH, 1);
} 

else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}
}
if(s1.equals("5")|| s2.equals("5")|| s3.equals("5")||s4.equals("5")|| s5.equals("5"))
{
SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
String DATE_FORMAT = "yyyy MM dd";

SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
Calendar c1 = Calendar.getInstance(); 

String y=empAttendanceHandler.getServerDate();
String years=y.substring(7,11);
int year=Integer.parseInt(years);

Calendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1);
for (int i = 0, inc = 1; i <366 && cal.get(Calendar.YEAR) == year; i+=inc) {
if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && (cal.get(Calendar.WEEK_OF_MONTH)==5)) {

String frm="";

frm=format.format(cal.getTime());
try
{
con= ConnectionManager.getConnection();
Statement st = con.createStatement();

st.execute("insert into HOLDMAST(HOLDNAME,FDATE,TDATE,BRANCH,HTEXT,HTYPE,RepeatHold,HDAY,HATTENDENCES,HSMS,OptionalHold) " +
		"values('Week OFF','"+frm+"' ,'"+frm+"' ,'"+cb.getCname()+"','5','Weekly Off','No','No','No','No','No')");
con.close();
}	
catch(Exception e)
{
e.printStackTrace();
}
// System.out.println("the value of the "+i+" saturday is "+format.format(cal.getTime()));
cal.add(Calendar.MONTH,1);
cal.set(Calendar.DAY_OF_MONTH, 1);
} 

else {
cal.add(Calendar.DAY_OF_MONTH, 1);
}
}
}



		return result;
		
	}

	public ArrayList<AttendStatusBean> getAllAttendanceStatus(ArrayList<EmpOffBean> projlist, String status,int eno,int site,String role) {
		ArrayList<AttendStatusBean> proj_attend_state = new ArrayList<AttendStatusBean>();
		ResultSet rs = null;

		try {
			Connection conn = ConnectionManager.getConnection();
			EmpAttendanceHandler EAH= new EmpAttendanceHandler(); 
			String serverdate =EAH.getServerDate();
			Statement st = conn.createStatement();
			int count = 0;
			if(!role.equals("1")){
				System.out.println("inside role id not one");
			
			if (status.equalsIgnoreCase("all")) {
				rs = st.executeQuery("select * from ATTENDANCE_STATUS where  att_month < '"+ReportDAO.EOM(serverdate)+"' and site_id="+site+" " +
						"or site_id in( select site_id from ATTENDANCE_SITE_RIGHTS where EMPNO="+eno+")  " +
						"order by att_month,appr_status ");
			} 
			else if(status.equalsIgnoreCase("left")) {
				rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
			}
			else if(status.equalsIgnoreCase("transfer")) {
				rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
			}
			
			
			else if(status.equalsIgnoreCase("SingleApprove")) {
				rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
			}
			else {
				/*rs = st.executeQuery("SELECT * from Attendance_status where  appr_status='"
						+ status + "' and empno is  null order by att_month ");*/
				rs = st.executeQuery("select * from ATTENDANCE_STATUS where appr_status='"+status+"' and empno is null and att_month < '"+ReportDAO.EOM(serverdate)+"' and  site_id="+site+" " +
						"or site_id in( select site_id from ATTENDANCE_SITE_RIGHTS where EMPNO="+eno+")  " +
						"order by att_month,appr_status ");
			}
			} 
		/*	
			else{

				if (status.equalsIgnoreCase("all")) {
					rs = st.executeQuery("select * from ATTENDANCE_STATUS where site_id="+site+" " +
							"or site_id in( select site_id from ATTENDANCE_SITE_RIGHTS where EMPNO="+eno+") " +
							"order by att_month,appr_status ");
				} 
				else if(status.equalsIgnoreCase("left")) {
					rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
				}
				else if(status.equalsIgnoreCase("transfer")) {
					rs = st.executeQuery("SELECT * from Attendance_status where tranevent='"+status+"'  order by empno");
				}
				else {
					rs = st.executeQuery("SELECT * from Attendance_status where  appr_status='"
							+ status + "' and empno is  null order by att_month ");
					rs = st.executeQuery("select * from ATTENDANCE_STATUS where appr_status='"+status+"' and empno is null and site_id="+site+" " +
							"or site_id in( select site_id from ATTENDANCE_SITE_RIGHTS where EMPNO="+eno+")" +
							"order by att_month,appr_status ");
				}
				
				
			}*/
			while (rs.next()) {
				AttendStatusBean asb = new AttendStatusBean();
				asb.setSite_id(rs.getInt(1));
				asb.setAppr_DATE(format.format(rs.getDate(2)));
				asb.setAppr_status(rs.getString(3));
				asb.setAtt_month(format.format(rs.getDate(4)));
				asb.setSubmit_date(format.format(rs.getDate(5)));
				asb.setEmpno(rs.getInt(10));
				asb.setTranEvent(rs.getString(11));
				proj_attend_state.add(asb);
				count++;
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
   /* System.out.println("size of proj_attend_state "+proj_attend_state.size());*/
		return proj_attend_state;
	}

	public boolean addAttendanceForTransfered(String date,int prjCode){
		
		Connection connection = ConnectionManager.getConnection();
		ArrayList<TranBean> employeeList=new ArrayList<TranBean>();
		ResultSet resultset = null;
		boolean flag=false;
		
		try {
			Statement statement = connection.createStatement();
			
			resultset= statement.executeQuery("select emp.EMPNO, etn.PRJ_SRNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where (emp.STATUS = 'A' and "+ 
" emp.DOJ<='"+ReportDAO.EOM(date)+"'  or emp.STATUS = 'N' and (emp.DOL>='"+ReportDAO.BOM(date)+"' or emp.DOL=null ) ) AND  emp.EMPNO "+
	" not in (select distinct EMPNO from EMP_ATTENDANCE where site_id= "+prjCode+" and ATTD_DATE between '"+ReportDAO.BOM(date)+" ' and '"+ReportDAO.EOM(date)+"  ')"+
	"and  etn.PRJ_SRNO="+prjCode+"  AND  "+
	 " etn.SRNO=(SELECT MAX(ET.SRNO) FROM EMPTRAN ET WHERE ET.EMPNO=emp.EMPNO  AND ET.EFFDATE<='"+ReportDAO.EOM(date)+"')  ");
			
			
			while(resultset.next()){
				
				TranBean  tranBean= new TranBean();
				tranBean.setEMPNO(resultset.getInt("EMPNO"));
				tranBean.setSite_id(resultset.getString("PRJ_SRNO"));
				employeeList.add(tranBean);
		
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			if(employeeList.size()!=0){
			flag=true;
			}
		
		return flag;
		
		
	}
	public boolean insertSingleEmpAttendance(ArrayList emp_al, int eno,String status, String roleId) throws ParseException {

		System.out.println("into insertSingleEmpAttendance in empattendancehandler");
	
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar c1 = Calendar.getInstance(); // today
	
		String date=sdf.format(c1.getTime());
		String BomDt = ReportDAO.BOM(date);
		String EomDt= ReportDAO.EOM(date);
		
		
		
		EmpAttendanceHandler EAH=new EmpAttendanceHandler();
		String currentdate=EAH.getServerDate();
	
		try {
			Connection conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);

			Statement st = conn.createStatement();
			Statement ad= conn.createStatement();
			ResultSet rs = null;
			
			
			
		if(status.equals("firstTimeSaved") || roleId.equals("1")){
			
			System.out.println("Inside if for first time saved of sheet in handler");
		
			for (int j = 0; j < emp_al.size(); j++) {
				ArrayList<Attend_bean> Emp_bean = (ArrayList<Attend_bean>) emp_al.get(j);
				
				for (int c = 0; c < Emp_bean.size(); c++) {
					
					
					Attend_bean ab = new Attend_bean();
					ab = Emp_bean.get(c);
					/*System.out.println("IF EXISTS (SELECT empno FROM  Emp_attendance WHERE empno="
							+ ab.getEmpno()
							//SITE_ID IS COMMENTED FOR PROPER WORKING OF TRANSFER
							+ " and site_id="
							+ ab.getSite_id()
							+ " and attd_date ='"
							+ ab.getDate()
							+ "')"
							+ "UPDATE  Emp_attendance SET empno="
							+ ab.getEmpno()
							+ ", attd_date ='"
							+ ab.getDate()
							+ "', ATTD_VAL='"
							+ ab.getVal()
								+ "' , site_id="+ ab.getSite_id()+" where empno="
							+ ab.getEmpno()
							+ " and attd_date ='"
							+ ab.getDate()
							+ "' else"
							+ " INSERT INTO Emp_attendance (empno,site_id, attd_date, attd_val, created_date, created_by) VALUES("
							+ ab.getEmpno()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+") ");*/
					
					st.executeUpdate("IF EXISTS (SELECT empno FROM  Emp_attendance WHERE empno="
							+ ab.getEmpno()
							//SITE_ID IS COMMENTED FOR PROPER WORKING OF TRANSFER
							/*+ " and site_id="
							+ ab.getSite_id()*/
							+ " and attd_date ='"
							+ ab.getDate()
							+ "')"
							+ "UPDATE  Emp_attendance SET empno="
							+ ab.getEmpno()
							+ ", attd_date ='"
							+ ab.getDate()
							+ "', ATTD_VAL='"
							+ ab.getVal()
								+ "' , site_id="+ ab.getSite_id()+" where empno="
							+ ab.getEmpno()
							+ " and attd_date ='"
							+ ab.getDate()
							+ "' else"
							+ " INSERT INTO Emp_attendance (empno,site_id, attd_date, attd_val, created_date, created_by) VALUES("
							+ ab.getEmpno()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+") ");
					
					
					ad.executeUpdate("update emp_attendance set updated_date= '"+currentdate+"', updated_by="+eno+" where  empno="+ab.getEmpno()+" and attd_date between '"+ReportDAO.BOM(ab.getDate())+"'and '"+ReportDAO.EOM(ab.getDate())+"' ");
				/*	ad.executeUpdate("insert into EMP_ATTENDANCE_HIST (empno, site_id, attd_date, attd_val, created_date, created_by,updated_by,updated_date) VALUES("
							+ ab.getEMPNO()
							+ ","
							+ ab.getSite_id()
							+ ",'"
							+ ab.getDate() + "','" + ab.getVal() + "','"+currentdate+"',"+eno+","+eno+",'"+currentdate+"')");*/
				//ad.executeUpdate("update emp_attendance set attd_val='P' where attd_val='' and attd_date between '"+BomDt+"'and '"+date+"'");

				}
					

			}
		}
		
	
		/*	Statement st11=conn.createStatement();
			st11.executeUpdate("update EMP_ATTENDANCE_HIST  set BATCH_NO=(select (case when (SELECT MAX(BATCH_NO) FROM EMP_ATTENDANCE_HIST) IS NULL " 
					+" then  1 else MAX(BATCH_NO+1) end) from EMP_ATTENDANCE_HIST) where BATCH_NO IS NULL");*/
			conn.commit();
			
				
			flag = true;

		} catch (SQLException e) {
			// System.out.println("error into insertEmpAttendance");
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public Integer  getLeftWOFFList(String lvdate)  throws ParseException
	{
		//ArrayList<AttendanceBean> list =new ArrayList<AttendanceBean>();
		 int flag = 0;
	   try
	   {
		 //  System.out.println("7777"+lvdate);
	    	 Connection conn=ConnectionManager.getConnection();
	    	 String attenlist=("select distinct ATTD_DATE from EMP_ATTENDANCE  where ATTD_DATE ='"+ lvdate+"' and(ATTD_VAL= 'WO' or ATTD_VAL= 'HD') ");
	    	 Statement stmt=conn.createStatement();
	    	 ResultSet rslt=stmt.executeQuery(attenlist);
	    	
			// AttendanceBean Abean;
			 AttendanceBean getattBean; //= new AttendanceBean();
			 while(rslt.next())
			 {
				 
				// System.out.println("9999"+lvdate);
				// getattBean=new AttendanceBean();	
				// getattBean.setATTD_DATE(rslt.getString("ATTD_DATE")==null?"":rslt.getString("ATTD_DATE"));
				 
				// list.add(getattBean);
				// System.out.println("9090"+list);
				 conn.commit();
				 flag=1;
			 }
			 conn.close();
	    } 
	   	catch (SQLException e) 
	   	{
		   e.printStackTrace();
		}
	  // return flag; 
	return flag;
	}

	
	public String  checkpending(String fordate,String siteid)  throws ParseException
	{
		
		 String  flag = "";
	   try
	   {
		 
	    	 Connection conn=ConnectionManager.getConnection();
	    	// String checkstatus=("select empno from ATTENDANCE_STATUS where site_id  is null and appr_status ='pending' and  att_month between '"+ReportDAO.BOM(fordate)+"' and '"+ReportDAO.EOM(fordate)+"' ");
	    	 
	    	 String checkstatus=("select srno,empno,PRJ_SRNO from emptran where empno in(select EMPNO from ATTENDANCE_STATUS where site_id  is null and appr_status ='pending'  and att_month between '"+ReportDAO.BOM(fordate)+"' and '"+ReportDAO.EOM(fordate)+"' ) and srno in(select max(srno) from emptran where empno in(select EMPNO from ATTENDANCE_STATUS where site_id  is null and appr_status ='pending'  and att_month between '"+ReportDAO.BOM(fordate)+"' and '"+ReportDAO.EOM(fordate)+"' )) group by empno,PRJ_SRNO,SRNO ");
	    	 Statement stmt=conn.createStatement();
	    	 ResultSet rslt=stmt.executeQuery(checkstatus);
	    	 System.out.println("checkingstatus...."+checkstatus);
	    
			 while(rslt.next())
			 {
				 //System.out.println("inhandler siteod...."+Integer.parseInt(rslt.getString("PRJ_SRNO")));
				 System.out.println("inhandler siteod...."+Integer.parseInt(siteid));
				 if(Integer.parseInt(rslt.getString("PRJ_SRNO"))==Integer.parseInt(siteid))
				 {
				
				 System.out.println("for check siteid is.."+Integer.parseInt(siteid)+" with emp found siteid.."+Integer.parseInt(rslt.getString("PRJ_SRNO"))+"..empno..."+rslt.getString("empno"));
				 
				 flag="true";
				 break;
				 }
				 else if(!flag.equalsIgnoreCase("true"))
				 {
				 flag="false";
				 }
			 }
			 conn.close();
	    } 
	   	catch (SQLException e) 
	   	{
		   e.printStackTrace();
		}

	return flag;
	}
	
	
	public String  checkblankattendance(String empno,String sheetdate,String leftdate)  
	{
		
		 String  flag = "";
		 String[] date=sheetdate.split("-");
		 String date1="01-"+date[1]+"-"+date[2];
		 String[] leftdateday=leftdate.split("-");
		 int day=Integer.parseInt(leftdateday[0]);
		 String forprevious ="01-"+leftdateday[1]+"-"+leftdateday[2];
		 String checkstatus="";
		// System.out.println("sheetdate.....in handler..."+sheetdate);
		 //System.out.println("fromdate of sheet.....in handler..."+date1);
	   try
	   {
		 
	    	 Connection conn=ConnectionManager.getConnection();
	    	// String checkstatus=("select empno from ATTENDANCE_STATUS where site_id  is null and appr_status ='pending' and  att_month between '"+ReportDAO.BOM(fordate)+"' and '"+ReportDAO.EOM(fordate)+"' ");
	    	 if(day==30&&(!leftdateday[1].equalsIgnoreCase(date[1])))
	    	 {
	    		 checkstatus=("select count(*) as EMPTYVAL from EMP_ATTENDANCE where EMPNO="+empno+" and attd_date between '"+forprevious+"' and '"+leftdate+"' and ATTD_VAL=' '  ");
		    	  
	    	 }
	    	 else
	    	 {
	    	  checkstatus=("select count(*) as EMPTYVAL from EMP_ATTENDANCE where EMPNO="+empno+" and attd_date between '"+ReportDAO.BOM(date1)+"' and '"+leftdate+"' and ATTD_VAL=' '  ");
	    	 }
	    	  Statement stmt=conn.createStatement();
	    	 ResultSet rslt=stmt.executeQuery(checkstatus);
	    	// System.out.println("checkingstatus...."+checkstatus);
	    
			 if(rslt.next())
			 {
				
				flag=rslt.getString("EMPTYVAL");
			}
			
			 
			 conn.close();
	    } 
	   	catch (SQLException e) 
	   	{
		   e.printStackTrace();
		}

	return flag;
	}
	
	public boolean  checksingleapprove(String empno,String date)  
	{
		boolean flag= false;
		try
		   {
				Connection conn = ConnectionManager.getConnection();
				Statement ST_apprv = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet check_single_approve=null;
				check_single_approve = ST_apprv.executeQuery("select empno from ATTENDANCE_STATUS where TRANEVENT='SingleApprove' "
						+ " and att_month between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and appr_status='approved' and empno="+empno+" ");
					if(check_single_approve.next())
					{
						flag = true;
					}
		   
		   }catch (SQLException e) 
		   	{
			   e.printStackTrace();
			}
		return flag;
	}

}
