package payroll.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import payroll.Core.Calculate;
import payroll.Core.ReportDAO;
import payroll.Model.EmpOffBean;
import payroll.Model.LMB;
import payroll.Model.LeaveBalBean;
import payroll.Model.Lookup;
import payroll.Model.TranBean;

public class LMH {
	
	public static float getLeaveDays(String frmdate, String todate, int lvcode, String halfDay){
		
		float TotalDays = 0.0f;
		try{
			Connection con=ConnectionManager.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = null;
		
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		
			Date fromDate = formatter.parse(frmdate);
			Date toDate = formatter.parse(todate);
		
			rs = st.executeQuery("SELECT DATEDIFF(day,'"+frmdate+"','"+todate+"')+1 AS DiffDate");
		
			while(rs.next()){
				TotalDays = Float.parseFloat(rs.getString(1));
			}
			rs.close();
			
			if(halfDay.equalsIgnoreCase("yes")){ // To check for halfday
				TotalDays = 0.5f;
				return TotalDays;
			}
			
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);

			/*ResultSet rs1 = null;
			rs1 = st.executeQuery("SELECT WEEK_OFF, ALTER_SAT FROM COMP_DETAILS");
			String weekoff = null;
			String alterSat = null;
			if(rs1.next()){
				weekoff = rs1.getString(1);
				alterSat = rs1.getString(2);
			}
			rs1.close();
			
			ResultSet rs2 = null;
			rs2 = st.executeQuery("SELECT CONS_HOLIDAYS, CONS_WEEK_OFF FROM LEAVEMASS WHERE LEAVECD="+lvcode);
			String ConsHol = null;
			String ConsWeekOff = null;
			if(rs2.next()){
				ConsHol = rs2.getString(1);
				ConsWeekOff = rs2.getString(2);
			}
			rs2.close();
			
			ResultSet rs3 = null;
			rs3 = st.executeQuery("SELECT FDATE FROM HOLDMAST");
			List<Date> results = new ArrayList<Date>();
			while(rs3.next()){
				results.add(rs3.getDate(1));
			}
			rs3.close();*/
			
			/*if(ConsHol.charAt(0) == 'N'){ // To check for Holiday
				
				while (c.getTime().before(toDate)) {
					
					for(Date date : results){
						
						if(c.getTime().equals(date)){
							TotalDays = TotalDays - 1.0f;
						}
					}
				c.add(Calendar.DATE, 1);	
				}
			}*/
		/*	
			c.setTime(fromDate);
			if(ConsWeekOff.charAt(0) == 'N'){ // To check for Week Off
				
				if( weekoff.contains("sat") && weekoff.contains("sun")){
					
					if(alterSat.contains("two") && alterSat.contains("four")){ // For second & fourth sat
				
						while (c.getTime().before(toDate)) {
							
							if (c.get(Calendar.WEEK_OF_MONTH) == 2 || c.get(Calendar.WEEK_OF_MONTH) == 4) {
								
								if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
									TotalDays = TotalDays - 1.0f;
									
								}
							}
							if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {							
									TotalDays = TotalDays - 1.0f;
									
							}							
						c.add(Calendar.DATE, 1);
						}
					}
					else if(alterSat.contains("one") && alterSat.contains("three")){ // For first & third sat
						
						while (c.getTime().before(toDate)) {
							
							if (c.get(Calendar.WEEK_OF_MONTH) == 1 || c.get(Calendar.WEEK_OF_MONTH) == 3) {
								
								if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
									TotalDays = TotalDays - 1.0f;
									
								}
							}
							if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
									TotalDays = TotalDays - 1.0f;
									
							}							
						c.add(Calendar.DATE, 1);
						}
					}
					else{
						while (c.getTime().before(toDate)) {
																						
								if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
									TotalDays = TotalDays - 1.0f;
									
								}							
						c.add(Calendar.DATE, 1);
						}
					}
				}
				else if(weekoff.contains("sun")){ // checking for sunday 
					
					while (c.getTime().before(toDate)) {
						
						if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
							TotalDays = TotalDays - 1.0f;
							
						}					
					c.add(Calendar.DATE, 1);
					}
				}			
			}
			*/
			st.close();
			con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return TotalDays;
	}
	
	public  ArrayList<LMB> getList(int empno)
	{
		ResultSet rs=null; 
		ArrayList<LMB> leavelist=new ArrayList<LMB>();
		Connection con=ConnectionManager.getConnection();
		Statement st=null;
		Statement st1=null;
		ResultSet rs1 = null;
		//String query="SELECT distinct * from LEAVEBAL a where a.EMPNO='"+empno+"' AND BALDT<=(select distinct max(BALDT) from LEAVEBAL b where EMPNO='"+empno+"' AND LEAVECD=a.LEAVECD)";
     	try
     	{
			st1 = con.createStatement();
			rs1= st1.executeQuery("SELECT DISTINCT(LEAVECD) FROM LEAVEBAL WHERE EMPNO="+empno+" ORDER BY LEAVECD");
			while(rs1.next())
			{
				st = con.createStatement();
				rs=st.executeQuery("SELECT TOP 1 * FROM LEAVEBAL WHERE LEAVECD="+rs1.getInt(1)+" AND EMPNO="+empno+" ORDER BY BALDT DESC");
				
				if(rs.next())
				{
					LMB bean=new LMB();
					bean.setBALDT(rs.getDate("BALDT")==null?"":dateFormat(rs.getDate("BALDT")));
					bean.setEMPNO(rs.getString("EMPNO")==null?0:rs.getInt(("EMPNO")));
					bean.setLEAVECD(rs.getString("LEAVECD")==null?0:rs.getInt("LEAVECD"));
					bean.setBAL(rs.getString("BAL")==""?0.0f:rs.getFloat("BAL"));
					bean.setTOTCR(rs.getString("TOTCR")==""?0.0f:rs.getFloat("TOTCR"));
					bean.setTOTDR(rs.getString("TOTDR")==""?0.0f:rs.getFloat("TOTDR"));
					leavelist.add(bean);
				}
				rs.close();
				st.close();
			}
			rs1.close();
			st1.close();
			con.close();
		}
     	catch (Exception e) 
     	{
			e.printStackTrace();
		}
		return leavelist;
	}
	
	public  ArrayList<LMB> leaveDisplay(int empno1,String leaveType)
	{
		ResultSet rs=null;  
		ArrayList<LMB> List1 =new ArrayList<LMB>();
		try
		{
			String query = "";
			
			Connection con=ConnectionManager.getConnection();
			Statement st=con.createStatement();
			LookupHandler objDesc=new LookupHandler();
			String Result1;
			if(leaveType.equalsIgnoreCase("PENDING")){
				 query="SELECT * FROM LEAVETRAN   WHERE empno='"+empno1+"'  AND STATUS = 'PENDING' order by trndate desc";
			}else if(leaveType.equalsIgnoreCase("SANCTION")){
				 query="SELECT * FROM LEAVETRAN   WHERE empno='"+empno1+"'  AND STATUS = 'SANCTION' order by trndate desc";
			}else if(leaveType.equalsIgnoreCase("CANCEL")){
				 query="SELECT * FROM LEAVETRAN   WHERE empno='"+empno1+"'  AND STATUS = 'CANCEL' order by trndate desc";
			}else
			     query="SELECT * FROM LEAVETRAN   WHERE empno='"+empno1+"' AND STATUS !='1' order by trndate desc";
			
			
			rs=st.executeQuery(query);
			while(rs.next())
			{
				LMB lbean1=new LMB();
				lbean1.setLEAVECD(rs.getString("leavecd")==null?0:rs.getInt("leavecd"));
				lbean1.setEMPNO(rs.getString("empno")==null?0:rs.getInt("empno"));
				lbean1.setTRNDATE(rs.getDate("trndate")==null?"":dateFormat(rs.getDate("trndate")));
				lbean1.setTRNTYPE(rs.getString("trntype").charAt(0));
				lbean1.setAPPLNO(rs.getString("applno")==null?"":rs.getString("applno"));
				lbean1.setBRCODE(rs.getString("brcode")==null?0:rs.getInt("brcode"));
				lbean1.setLEAVEPURP(rs.getString("leavepurp")==null?0:rs.getInt("leavepurp"));
			
				lbean1.setLREASON(rs.getString("LREASON")==null?"--":rs.getString("LREASON"));
				lbean1.setLADDR(rs.getString("LADDR")==null?"--":rs.getString("LADDR"));
				lbean1.setLTELNO(rs.getString("LTELNO")==null?0:rs.getLong("LTELNO"));
				lbean1.setAPPLDT(rs.getDate("APPLDT")==null?"":dateFormat(rs.getDate("APPLDT")));
				lbean1.setFRMDT(rs.getDate("FRMDT")==null?"":dateFormat(rs.getDate("FRMDT")));
				lbean1.setTODT(rs.getDate("TODT")==null?"":dateFormat(rs.getDate("TODT")));
				
				Lookup lkhp = new Lookup();
				lkhp = objDesc.getLookup("SAUTH-"+rs.getInt("SANCAUTH"));
				Result1 = lkhp.getLKP_DESC();
				lbean1.setSANCAUTH(Result1);
				lbean1.setOPR_CD(rs.getString("OPR_CD")==null?"":rs.getString("OPR_CD"));
				lbean1.setOFF_CD(rs.getString("OFF_CD")==null?"":rs.getString("OFF_CD"));
				lbean1.setSTATUS(rs.getString("STATUS")==null?"":rs.getString("STATUS"));
				lbean1.setSUBSTITUTE(rs.getString("SUBSTITUTE")==null?"":rs.getString("SUBSTITUTE"));
				lbean1.setNODAYS(rs.getString("DAYS")==null?0:rs.getFloat("DAYS"));
				List1.add(lbean1);
			}
			rs.close();
			st.close();
			con.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
		return List1;
	}
	
	public  boolean addLeave(LMB lbean) 
	{
		boolean result = true;
		try
		{	
			Connection con=ConnectionManager.getConnection();
			float TotalDays = LMH.getLeaveDays(lbean.getFRMDT(), lbean.getTODT(), lbean.getLEAVECD(), lbean.getHALFDAY());
			System.out.println("TotalDays"+TotalDays);
			Statement st = con.createStatement();
			ResultSet rs1 = null;
			rs1=st.executeQuery("SELECT TOP 1 * FROM LEAVEBAL WHERE LEAVECD="+lbean.getLEAVECD()+" AND EMPNO="+lbean.getEMPNO()+" and baldt<='"+ReportDAO.EOM(lbean.getTRNDATE())+"'  ORDER BY BALDT DESC");
			float balance = 0.0f;
			if(rs1.next())
			{
				balance = rs1.getFloat("BAL");
			}
			rs1.close();
			st.close();
			
			float days = balance - TotalDays;	// To check the leave balance after deduction of applied leave days.
			
			if((days >= 0 || lbean.getLEAVECD() == 4) && result == true){
				
				Statement st1 = con.createStatement();
				ResultSet rs2 = null;
				rs2 = st1.executeQuery("SELECT MAX(APPLNO)+1 FROM LEAVETRAN");
				int applno = 0;
				if(rs2.next()){
					applno = rs2.getInt(1);
				}
				String insertquery="INSERT INTO LEAVETRAN (EMPNO,LEAVECD,TRNDATE,TRNTYPE,APPLNO,FRMDT,TODT,LREASON,LADDR,LTELNO,DAYS,STATUS,PRJ_SRNO,APPLDT) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement Pstat = con.prepareStatement(insertquery);
				Pstat.setInt(1,lbean.getEMPNO());
				Pstat.setInt(2,lbean.getLEAVECD());
				Pstat.setString(3, lbean.getTRNDATE());
			    Pstat.setString(4, "D");
			    Pstat.setInt(5, applno);
			    Pstat.setString(6, lbean.getFRMDT());
			    Pstat.setString(7, lbean.getTODT());
			    Pstat.setString(8, lbean.getLREASON());
			    Pstat.setString(9, lbean.getLADDR());
			    Pstat.setDouble(10, lbean.getLTELNO());
			    Pstat.setFloat(11, TotalDays);
			    Pstat.setString(12, "PENDING");
			    Pstat.setString(13, lbean.getPrj_srno());
			    Pstat.setString(14, lbean.getTRNDATE());
			   
			    Pstat.executeUpdate();
			    result = true;
			    Pstat.close();
			}
			else{
				result = false;	
			}
			
		    con.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
			
	}
	
	/* This method has written to credit or debit the uninformed leaves */
	public boolean addExtraLeave(LMB lbean) 
	{
		boolean result=false;
		try
		{
			Connection con=ConnectionManager.getConnection();
			Statement st = con.createStatement();
			float TotalDays = LMH.getLeaveDays(lbean.getFRMDT(), lbean.getTODT(), lbean.getLEAVECD(), lbean.getHALFDAY());
		    
		    ResultSet rs3;
			String querybal="SELECT top 1 baldt, bal,totdr,TOTCR FROM LEAVEBAL a where a.EMPNO='"+lbean.getEMPNO()+"' AND a.LEAVECD = '" + lbean.getLEAVECD() +"' AND BALDT=(select distinct max(BALDT) from LEAVEBAL b where EMPNO='"+lbean.getEMPNO()+"' AND LEAVECD ='"+lbean.getLEAVECD()+"')";
		    //String querybal = "SELECT TOP 1 baldt, bal, totdr, totcr FROM LEAVEBAL WHERE LEAVECD="+lbean.getLEAVECD()+" AND EMPNO="+lbean.getEMPNO()+"   and   ORDER BY BALDT DESC";
		    rs3 = st.executeQuery(querybal);
		    String balDate = null;
			float balance = 0;
			float totdr = 0;
			float tcredit = 0;
			if(rs3.next()){
				final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				final Date date = format.parse(rs3.getString(1));
				final Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				balDate = format.format(calendar.getTime()); 
				//balDate = lbean.getFRMDT();
				System.out.println("hello "+balDate);
				
				balance = rs3.getFloat(2);
				totdr = rs3.getFloat(3);						
				tcredit = rs3.getFloat(4);
			}
			rs3.close();
			
			if(balDate==null){
				balDate=lbean.getFRMDT();
			}
			
			
		    if(lbean.getTRNTYPE() == 'C'){ // To credit leaves in account
		    	
		    	if(lbean.getLEAVECD() == 4){
		    		
		    		if(totdr == 0){
		    			totdr = TotalDays;
		    		
		    		}
		    		else if (totdr != 0){
		    			totdr = totdr - TotalDays;
		    			
		    		}
		    	}
		    	else if(lbean.getLEAVECD() == 5){
		    		
		    		if(tcredit == 0){
		    			tcredit = TotalDays;
		    			balance = TotalDays;
		    		}
		    		else{
		    			tcredit = tcredit + TotalDays;
		    			balance = balance + TotalDays;
		    		}
		    	}
		    	
		    	
		    	else  {	
		    		if(balance!=0){
		    		balance = balance + TotalDays;
		    		tcredit = tcredit + TotalDays;
		    		
		    		}
		    		else{
		    			totdr=0;
		    			tcredit=TotalDays;
		    			balance=TotalDays;
		    		}
		    		if(tcredit == 0){
		    			tcredit =  TotalDays;
		    			
		    		}
		    		
		    		
		    		
		    		System.out.println("value of tcredit 3 "+totdr);
		    	}
		    	
				String query2 = "INSERT INTO LEAVEBAL VALUES('"+balDate+"','"+lbean.getEMPNO()+"','"+lbean.getLEAVECD()+"','"+balance+"','"+tcredit+"','"+totdr+"')";
				
				
				st.executeUpdate(query2);
				
		    }
		    else if(lbean.getTRNTYPE() == 'D'){ // To debit leaves from account
		    	
		    	if(lbean.getLEAVECD() == 4){
		    		totdr = totdr + TotalDays;
		    	}
		    	else if(lbean.getLEAVECD() == 5){
	    		
	    			totdr = totdr + TotalDays;
	    			balance = balance - TotalDays;
	    		}
		    	else{
		    		balance = balance - TotalDays;
		    		totdr = totdr + TotalDays;	 
		    	}
				
				String query2 = "INSERT INTO LEAVEBAL VALUES('"+balDate+"','"+lbean.getEMPNO()+"','"+lbean.getLEAVECD()+"','"+balance+"','"+tcredit+"','"+totdr+"')";
				st.executeUpdate(query2);
				
		    }
		    Statement st1 = con.createStatement();
			ResultSet rs2 = null;
			rs2 = st1.executeQuery("SELECT MAX(APPLNO)+1 FROM LEAVETRAN");
			int applno = 0;
			if(rs2.next()){
				applno = rs2.getInt(1);
			}
			
		    String insertquery = "INSERT INTO LEAVETRAN (EMPNO,LEAVECD,TRNDATE,TRNTYPE,APPLNO,FRMDT,TODT,LREASON,DAYS,STATUS,PRJ_SRNO) values(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement Pstat = con.prepareStatement(insertquery);
			EmpOffHandler eoh=new EmpOffHandler();
			EmpOffBean eob= eoh.getEmpOfficAddInfo(Integer.toString(lbean.getEMPNO()));
			int prjsrno=eob.getPrj_srno();
			Pstat.setInt(1,lbean.getEMPNO());
			Pstat.setInt(2,lbean.getLEAVECD());
			Pstat.setString(3, lbean.getTRNDATE());
		    Pstat.setString(4, String.valueOf(lbean.getTRNTYPE()));
		    Pstat.setInt(5, applno);
		    Pstat.setString(6, lbean.getFRMDT());
		    Pstat.setString(7, lbean.getTODT());
		    Pstat.setString(8, lbean.getLREASON());
		    Pstat.setFloat(9, TotalDays);
		    Pstat.setString(10, "SANCTION");
		    Pstat.setInt(11, prjsrno);
		   
		    Pstat.executeUpdate();
		    result=true;
		    Pstat.close();
		    st.close();
		    con.close();
		    
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean editLeave(LMB lbean) 
	{
		boolean result=false;
		try
		{		
			Connection con=ConnectionManager.getConnection();
			float TotalDays = LMH.getLeaveDays(lbean.getFRMDT(), lbean.getTODT(), lbean.getLEAVECD(), lbean.getHALFDAY());
			
			Statement st1 = null;
			ResultSet rs1= null;
			st1 = con.createStatement();
			rs1 = st1.executeQuery("SELECT TOP 1 * FROM LEAVEBAL WHERE LEAVECD="+lbean.getLEAVECD()+" AND EMPNO="+lbean.getEMPNO()+" ORDER BY BALDT DESC");
			float balance = 0.0f;
			if(rs1.next())
			{
				balance = rs1.getFloat("BAL");
			}
			rs1.close();
			st1.close();
			
			float days = balance - TotalDays;
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT DISTINCT(LEAVECD) FROM LEAVETRAN WHERE EMPNO="+lbean.getEMPNO()+"ORDER BY LEAVECD");
			while(rs.next()){
				
				if(lbean.getLEAVECD() == rs.getInt(1) && days >= 0.0f || lbean.getLEAVECD() == 4){
					
					String insertquery="Update LEAVETRAN set EMPNO = "+lbean.getEMPNO()+"," +
															"LEAVECD = "+lbean.getLEAVECD()+"," +
															"TRNDATE = '"+lbean.getTRNDATE()+"'," +
															"TRNTYPE = 'D'," +
															"FRMDT = '"+lbean.getFRMDT()+"'," +
															"TODT = '"+lbean.getTODT()+"'," +
															"LREASON ='"+lbean.getLREASON()+"'," +
															"LADDR = '"+lbean.getLADDR()+"'," +
															"LTELNO = "+lbean.getLTELNO()+"," +
															"DAYS = "+TotalDays+"," +
															"STATUS ='PENDING'" +
															"where APPLNO="+lbean.getAPPLNO();
					
					Statement stmt=con.createStatement();
					stmt.executeUpdate(insertquery);
					result = true;
					break;
				}
				else{
					result = false;
				}
			}
			rs.close();
			st.close();
		    con.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
			
	}
			
	public  ArrayList<LMB> getLeaveApp(int empno,int appno)
	{
		ResultSet rs=null;  
		ArrayList<LMB> List1 =new ArrayList<LMB>();
		try
		{
			String query = "";
			
			Connection con=ConnectionManager.getConnection();
			Statement st=con.createStatement();
			LookupHandler objDesc=new LookupHandler();
			String Result1;
		    query="SELECT * FROM LEAVETRAN WHERE empno="+empno+" and APPLNO = "+appno;
					
			rs=st.executeQuery(query);
			while(rs.next())
			{
				LMB lbean1=new LMB();
				lbean1.setLEAVECD(rs.getString("leavecd")==null?0:rs.getInt("leavecd"));
				lbean1.setEMPNO(rs.getString("empno")==null?0:rs.getInt("empno"));
				lbean1.setTRNDATE(rs.getDate("trndate")==null?"":dateFormat(rs.getDate("trndate")));
				lbean1.setTRNTYPE(rs.getString("trntype").charAt(0));
				lbean1.setAPPLNO(rs.getString("applno")==null?"":rs.getString("applno"));
				lbean1.setBRCODE(rs.getString("brcode")==null?0:rs.getInt("brcode"));
				lbean1.setLEAVEPURP(rs.getString("leavepurp")==null?0:rs.getInt("leavepurp"));
				lbean1.setLREASON(rs.getString("LREASON")==null?"--":rs.getString("LREASON"));
				lbean1.setLADDR(rs.getString("LADDR")==null?"--":rs.getString("LADDR"));
				lbean1.setLTELNO(rs.getString("LTELNO")==null?0:rs.getLong("LTELNO"));
				lbean1.setAPPLDT(rs.getDate("APPLDT")==null?"":dateFormat(rs.getDate("APPLDT")));
				lbean1.setFRMDT(rs.getDate("FRMDT")==null?"":dateFormat(rs.getDate("FRMDT")));
				lbean1.setTODT(rs.getDate("TODT")==null?"":dateFormat(rs.getDate("TODT")));
				
				Lookup lkhp = new Lookup();
				lkhp = objDesc.getLookup("SAUTH-"+rs.getInt("SANCAUTH"));
				Result1 = lkhp.getLKP_DESC();
				lbean1.setSANCAUTH(Result1);
				lbean1.setOPR_CD(rs.getString("OPR_CD")==null?"":rs.getString("OPR_CD"));
				lbean1.setOFF_CD(rs.getString("OFF_CD")==null?"":rs.getString("OFF_CD"));
				lbean1.setSTATUS(rs.getString("STATUS")==null?"":rs.getString("STATUS"));
				lbean1.setSUBSTITUTE(rs.getString("SUBSTITUTE")==null?"":rs.getString("SUBSTITUTE"));
				lbean1.setNODAYS(rs.getString("DAYS")==null?0:rs.getFloat("DAYS"));
				List1.add(lbean1);
			}
			rs.close();
			st.close();
			con.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
		return List1;
	}

	public static String dateFormat(Date dates)
	{
		String result="";
		if(dates==null)
		{
			result="";
			return result;
		}
		SimpleDateFormat format= new SimpleDateFormat("dd-MMM-yyyy");
		result=format.format(dates);
		return result;
	}
	
	
	public ArrayList<LMB> getLeaveAppList(LMB lBean,String searchType) 
	{
		ArrayList<LMB> list =new ArrayList<>();
		String searchQuery = null;
		Connection con=ConnectionManager.getConnection();
		System.out.println(lBean.getEMPNO());
		System.out.println(lBean.getEMPNO2());
		System.out.println(lBean.getFRMDT());
		System.out.println(lBean.getTODT());
		System.out.println(lBean.getSTATUS());
		
		if(searchType.equalsIgnoreCase("custom")){
			String status = "";
			if(lBean.getSTATUS().equalsIgnoreCase("ALL")){
				System.out.println("if");
				searchQuery= "SELECT * FROM LEAVETRAN WHERE EMPNO between " +
				 		" '"+lBean.getEMPNO()+"'  AND  '"+lBean.getEMPNO2()+"'  AND  TRNDATE " +
				 		" between '"+lBean.getFRMDT()+"' AND '"+lBean.getTODT()+"' ";
			}
			else{
			 status = "AND STATUS = '"+lBean.getSTATUS()+"'";
			 searchQuery= "SELECT * FROM LEAVETRAN WHERE EMPNO between " +
			 		" '"+lBean.getEMPNO()+"'  AND  '"+lBean.getEMPNO2()+"'  AND  TRNDATE " +
			 		" between '"+lBean.getFRMDT()+"' AND '"+lBean.getTODT()+"' "+status;
			 System.out.println("else");
			 System.out.println(searchQuery);
			}
		}
		else{
			 searchQuery="SELECT * FROM LEAVETRAN WHERE STATUS='"+lBean.getSTATUS()+"'" ;	
			 System.out.println("if else");
		}
		 
		try
		{
			ResultSet rslist=null;
			Statement stmt1=con.createStatement();
			
			rslist=stmt1.executeQuery(searchQuery) ; 
			LMB leaveBean= null;
			
			while(rslist.next())
			{
				leaveBean=new LMB();
				leaveBean.setLEAVECD(rslist.getString("LEAVECD")==null?0:rslist.getInt("LEAVECD"));
				leaveBean.setEMPNO(rslist.getString("EMPNO")==null?0:rslist.getInt("EMPNO"));
				leaveBean.setTRNDATE(rslist.getDate("TRNDATE")==null?"":dateFormat(rslist.getDate("TRNDATE")));
				leaveBean.setTRNTYPE(rslist.getString("TRNTYPE").charAt(0));
				leaveBean.setAPPLNO(rslist.getString("APPLNO")==null?"":rslist.getString("APPLNO"));
				leaveBean.setBRCODE(rslist.getString("BRCODE")==null?0:rslist.getInt("BRCODE"));
				leaveBean.setLEAVEPURP(rslist.getString("leavepurp")==null?0:rslist.getInt("leavepurp"));
				leaveBean.setLREASON(rslist.getString("LREASON")==null?"":rslist.getString("LREASON"));
				leaveBean.setLADDR(rslist.getString("LADDR")==null?"":rslist.getString("LADDR"));
				leaveBean.setLTELNO(rslist.getString("LTELNO")==null?0:rslist.getLong("LTELNO"));
				leaveBean.setAPPLDT(rslist.getDate("APPLDT")==null?"":dateFormat(rslist.getDate("APPLDT")));
				leaveBean.setFRMDT(rslist.getDate("FRMDT")==null?"":dateFormat(rslist.getDate("FRMDT")));
				leaveBean.setTODT(rslist.getDate("TODT")==null?"":dateFormat(rslist.getDate("TODT")));
				String Result1;
				Lookup lkhp = new Lookup();
				LookupHandler objDesc1=new LookupHandler();
				lkhp = objDesc1.getLookup("SAUTH-"+rslist.getInt("SANCAUTH"));
				Result1 = lkhp.getLKP_DESC();
				leaveBean.setSANCAUTH(Result1);
				leaveBean.setSTATUS(rslist.getString("STATUS")==null?"":rslist.getString("STATUS"));
				leaveBean.setNODAYS(rslist.getString("DAYS")==null?0:rslist.getFloat("DAYS"));
				
				list.add(leaveBean);
			}
			rslist.close();
			stmt1.close();
			con.close();
		}
		catch(Exception e)
		{  
			e.printStackTrace();
		}
		return list;
	}
	
	
	/* This method has written for sanctioning leave application.
	   It updates status in leavetran table & make an entry into leavebal table of balance deduction */
	public boolean setSanction(int empno,int appNo) throws SQLException{
		
		Connection con = ConnectionManager.getConnection();
		String sql = "UPDATE LEAVETRAN SET STATUS='SANCTION' WHERE EMPNO="+empno+" AND APPLNO="+appNo;
		String selectdetail = "SELECT * FROM LEAVETRAN WHERE EMPNO="+empno+" AND APPLNO="+appNo;
		
		boolean flag = false;
		float bal = 0.0f;
		String leavecd = "";
		float leaveDays = 0.0f;
		String Baldate = null;
		
		try{
			
			Statement stmt=con.createStatement();
			ResultSet rslt=stmt.executeQuery(selectdetail);
			while(rslt.next()){
				
				leavecd = rslt.getString("LEAVECD");
				leaveDays = rslt.getFloat("DAYS");
				Baldate = rslt.getString("TODT");
			}
			
				
			ResultSet balChkRS = null;
			Statement st = con.createStatement();
			String balChk = "SELECT TOP 1 bal-"+leaveDays+" FROM LEAVEBAL WHERE LEAVECD="+leavecd+" AND EMPNO="+empno+" ORDER BY BALDT DESC";
			System.out.println("SELECT TOP 1 bal-"+leaveDays+" FROM LEAVEBAL WHERE LEAVECD="+leavecd+" AND EMPNO="+empno+" ORDER BY BALDT DESC");
			balChkRS = st.executeQuery(balChk);
			
			if(balChkRS.next()){
				
			    	bal = balChkRS.getFloat(1);
			}
			
			if(bal >= 0.0f || Integer.parseInt(leavecd) == 4){
				
				stmt.executeUpdate(sql);
				Statement stmt6=con.createStatement();
				Statement stmt7=con.createStatement();
				ResultSet rs3;
				String querybal = "SELECT distinct bal,totdr,TOTCR FROM LEAVEBAL a where a.EMPNO='"+empno+"' AND a.LEAVECD = '" + leavecd +"' AND BALDT=(select distinct max(BALDT) from LEAVEBAL b where EMPNO='"+empno+"' AND LEAVECD ='"+leavecd+"' and BALDT <='"+Baldate+"' )";
				rs3 = stmt6.executeQuery(querybal);
				
				float balance = 0.0f;
				float totdr = 0.0f;
				float tcredit = 0.0f;
				if(rs3.next()){
					balance = rs3.getFloat(1);					
					totdr = rs3.getFloat(2);										
					tcredit = rs3.getFloat(3);					
				}
				
				float totaldays = 0.0f;
				if(Integer.parseInt(leavecd) == 4){
					totaldays = totdr + leaveDays;
				}
				else{
					balance = balance - leaveDays;
					totaldays = totdr + leaveDays;		
				}
				
				//String Baldate = getDateMethod();				
				String query2 = "INSERT INTO LEAVEBAL VALUES('"+Baldate+"','"+empno+"','"+leavecd+"','"+balance+"','"+tcredit+"','"+totaldays+"')";
				//System.out.println("INSERT INTO LEAVEBAL VALUES('"+Baldate+"','"+empno+"','"+leavecd+"','"+(balance-totaldays)+"','"+tcredit+"','"+(balance-totaldays)+"')");
				stmt6.executeUpdate(query2);
				
				
				/*stmt7.execute(" update LEAVEBAL set bal=(bal-"+totdr+"),TOTDR=(TOTDR+"+totdr+") where empno="+empno+" and leavecd="+leavecd+" and baldt>'"+Baldate+"'");
				System.out.println(" update LEAVEBAL set bal=(bal-"+totdr+"),TOTDR=(TOTDR+"+totdr+") where empno="+empno+" and leavecd="+leavecd+" and baldt>'"+Baldate+"'");
				*/
				
				stmt7.execute(" update LEAVEBAL set bal=(bal-"+leaveDays+"),TOTDR=(TOTDR+"+leaveDays+") where empno="+empno+" and leavecd="+leavecd+" and baldt>'"+Baldate+"'");
				System.out.println(" update LEAVEBAL set bal=(bal-"+leaveDays+"),TOTDR=(TOTDR+"+leaveDays+") where empno="+empno+" and leavecd="+leavecd+" and baldt>'"+Baldate+"'");
				
				
				
				con.commit();
				flag = true;
			}
			else{
				flag = false;
			}
			con.close();
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean cancelLeaveApp(int empno,int appNo){
		
		boolean flag=false;
		Connection conn=ConnectionManager.getConnection();
		String sql="UPDATE LEAVETRAN SET STATUS='CANCEL' WHERE EMPNO="+empno+" AND APPLNO="+appNo;
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
			stmt.close();
			conn.close();
			
			flag = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
		return flag;
	}
	
	
	public String getDateMethod()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = new Date();
		String yourDate = dateFormat.format(date);
		return yourDate;
	}
	
	public static ArrayList<LeaveBalBean> getEmpLeaves(){
		
		Connection con = ConnectionManager.getConnection();
		Statement st = null;
		ResultSet rs = null;
		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String now = format.format(today);
		
		ArrayList<LeaveBalBean> leaveBal = new ArrayList<LeaveBalBean>();
		
		try{
			st = con.createStatement();
			rs = st.executeQuery("select e.EMPNO , EMPCODE, SALUTE, FNAME, MNAME, LNAME, DOJ from EMPMAST e " +
									"where NOT EXISTS(select EMPNO from LEAVEBAL l where E.EMPNO = l.EMPNO ) and STATUS='A' order by empcode");
			while(rs.next()){
				
				Date jdate = rs.getDate(7);
				
				Calendar cal = Calendar.getInstance(); 
				cal.setTime(jdate);
				
				int date = cal.get(Calendar.DATE);
				int month = cal.get(Calendar.MONTH);
				int joinYear = cal.get(Calendar.YEAR);
				
				Calendar cal1 = Calendar.getInstance();
				int Year = cal1.get(Calendar.YEAR);
				
				month = month+4; //why 4 ? 3 for 3 months after joining & 1 to match with 12(months) bcoz calendar month starts with 0(jan) to 11(dec) 
				
				int CalMonth = (12-month)+3;//3 for from jan to mar
				float leaves =0.0f;
				leaves = (float)(1.75f*CalMonth);
				
				if(date > 0 && date <= 10){ // for emp joining date between 1 to 10
					leaves = leaves + 1.75f;
				}
				else if(date > 10 && date <= 20){ // for emp joining date between 10 to 20
					leaves = leaves + 1.0f;
				}
				else if(date > 20){ // for emp joining date after 20
					leaves = leaves + 0.75f;
				}
				
				/* To get the total days from joining to till date */
				Statement stmt = con.createStatement();
				ResultSet rs1 = stmt.executeQuery("SELECT DATEDIFF(day,'"+jdate+"','"+now+"') AS DiffDate");
				int days = 0;
				if(rs1.next()){
					days = Integer.parseInt(rs1.getString(1));
				}
				rs1.close();
				stmt.close();
				
				if(joinYear == Year && days > 90){
					LeaveBalBean lbean = new LeaveBalBean();
					lbean.setEMPNO(rs.getInt(1));
					lbean.setEMPCODE(rs.getString("EMPCODE"));
					lbean.setSALUTE(rs.getInt("SALUTE"));
					lbean.setFNAME(rs.getString("FNAME"));
					lbean.setMNAME(rs.getString("MNAME"));
					lbean.setLNAME(rs.getString("LNAME"));
					lbean.setLeaves(leaves);
				
					leaveBal.add(lbean);
				}
				else if(joinYear != Year){
					LeaveBalBean lbean = new LeaveBalBean();
					lbean.setEMPNO(rs.getInt(1));
					lbean.setEMPCODE(rs.getString("EMPCODE"));
					lbean.setSALUTE(rs.getInt("SALUTE"));
					lbean.setFNAME(rs.getString("FNAME"));
					lbean.setMNAME(rs.getString("MNAME"));
					lbean.setLNAME(rs.getString("LNAME"));
					lbean.setLeaves(21);
				
					leaveBal.add(lbean);
				}
			}
			
		rs.close();
		st.close();
		con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return leaveBal;
	}

	public static boolean checkTransactions(int empno, int leavecd){
		boolean flag = false;
		Connection con = ConnectionManager.getConnection();
		Statement st = null;
		ResultSet rs = null;
		try{
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM LEAVETRAN WHERE EMPNO="+empno+" AND LEAVECD="+leavecd+" AND TRNTYPE='D'");
			if(rs.next()){
				flag = true;
			}
			else{
				flag = false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}

	public static float displayEmpLeaves(int empno){
		
		Connection con = ConnectionManager.getConnection();
		Statement st = null;
		ResultSet rs = null;
		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String now = format.format(today);
		float leaves =0.0f;
		
		try{
			st = con.createStatement();
			rs = st.executeQuery("select DOJ from EMPMAST where empno = "+empno);

			if(rs.next()){
				
				Date jdate = rs.getDate(1);
				
				Calendar cal = Calendar.getInstance(); 
				cal.setTime(jdate);
				
				int date = cal.get(Calendar.DATE);
				int month = cal.get(Calendar.MONTH);
				int joinYear = cal.get(Calendar.YEAR);
				
				Calendar cal1 = Calendar.getInstance();
				int Year = cal1.get(Calendar.YEAR);
				
				month = month+4; //why 4 ? 3 for 3 months after joining & 1 to match with 12(months) bcoz calendar month starts with 0(jan) to 11(dec) 
				
				int CalMonth = (12-month)+3;//3 for from jan to mar
				
				leaves = (float)(1.75f*CalMonth);
				
				if(date > 0 && date <= 10){ // for emp joining date between 1 to 10
					leaves = leaves + 1.75f;
				}
				else if(date > 10 && date <= 20){ // for emp joining date between 10 to 20
					leaves = leaves + 1.0f;
				}
				else if(date > 20){ // for emp joining date after 20
					leaves = leaves + 0.75f;
				}
				
				/* To get the total days from joining to till date */
				Statement stmt = con.createStatement();
				ResultSet rs1 = stmt.executeQuery("SELECT DATEDIFF(day,'"+jdate+"','"+now+"') AS DiffDate");
				int days = 0;
				if(rs1.next()){
					days = Integer.parseInt(rs1.getString(1));
				}
				rs1.close();
				stmt.close();
				
				if(joinYear == Year && days > 90){
					return leaves;
					
				}
				else if(joinYear != Year){
					leaves = 21;
					
				}
			}
			
		rs.close();
		st.close();
		con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return leaves;
	}

	
	
	public boolean updateLeaveBal(ArrayList<TranBean> projEmpNolist,Float[] values,String date)
	{
		Connection con = ConnectionManager.getConnection();
		Statement st=null,st1=null;
		ResultSet rs=null;
		boolean result = false;
		int i = 0;
		float days=Calculate.getDays(date);
		try 
		{
			PreparedStatement pst1=null;
			st=con.createStatement();
			st1=con.createStatement();
			
			for(TranBean tbn : projEmpNolist)
			{ 
					
				rs=st.executeQuery("select totdr from leavebal where empno="+tbn.getEMPNO()+" and baldt BETWEEN '"+ReportDAO.BOM(date)+"' AND '"+ReportDAO.EOM(date)+"'");
				if(rs.next())
				{
					if(values[i]==0)
					{
						st1.executeUpdate("delete from leavebal WHERE EMPNO="+tbn.getEMPNO()+" AND BALDT BETWEEN '"+ReportDAO.BOM(date)+"' AND '"+ReportDAO.EOM(date)+"'");
					}
					else
					{
					st1.executeUpdate("update leavebal set totdr="+values[i]+" ,baldt='"+date+"' WHERE EMPNO="+tbn.getEMPNO()+" AND BALDT BETWEEN '"+ReportDAO.BOM(date)+"' AND '"+ReportDAO.EOM(date)+"'");
					}
				}
				
				
				if(values[i]>0)
				{
				pst1 = con.prepareStatement(" IF EXISTS(SELECT * FROM leavebal WHERE EMPNO="+tbn.getEMPNO()+" AND BALDT BETWEEN '"+ReportDAO.BOM(date)+"' AND '"+ReportDAO.EOM(date)+"')" +
									"update leavebal set totdr="+values[i]+" ,baldt='"+date+"' WHERE EMPNO="+tbn.getEMPNO()+" AND BALDT BETWEEN '"+ReportDAO.BOM(date)+"' AND '"+ReportDAO.EOM(date)+"'" +
									" ELSE" +
									" INSERT INTO leavebal VALUES ('"+date+"', "+tbn.getEMPNO()+",1,0,0,"+values[i]+")");
				pst1.execute();
						
				}
				i++;
			}
			result = true;
			
			
			
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public float getLeaveBal(int empno,String date)
	{
		
		float leaves=0.0f;
		Connection con= ConnectionManager.getConnection();
		
		try
		{
			Statement st = con.createStatement();
			ResultSet rs= null;
			String query="Select totdr from leavebal Where EMPNO="+empno+" AND baldt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"'";
			rs= st.executeQuery(query);
			while(rs.next())
			{
				leaves+=rs.getFloat("totdr");
			}
			con.close();
		} 
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		
		
	return leaves;
	}	
	
	
	
	
	
}
