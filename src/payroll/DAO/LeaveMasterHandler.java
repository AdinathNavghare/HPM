package payroll.DAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import payroll.Model.EmpOffBean;
import payroll.Model.LeaveMassBean;
import payroll.Model.LeaveMasterBean;
import payroll.Model.Lookup;

public class LeaveMasterHandler {

	public  boolean addLeave(LeaveMasterBean lbean) 
	{
		boolean result=false;
		try
		{
			Connection con=ConnectionManager.getConnection();
			String insertquery="INSERT INTO  LEAVETRAN values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement Pstat=(PreparedStatement) con.prepareStatement(insertquery);
			EmpOffHandler eoh=new EmpOffHandler();
			EmpOffBean eob= eoh.getEmpOfficAddInfo(Integer.toString(lbean.getEMPNO()));
			int prjsrno=eob.getPrj_srno();
			Pstat.setInt(1,lbean.getLEAVECD());
			Pstat.setInt(2,lbean.getEMPNO());
		    Pstat.setString(3, lbean.getTRNDATE());
		    Pstat.setString(4, lbean.getTRNTYPE());
		    Pstat.setString(5, lbean.getAPPLNO());
		    Pstat.setInt(6, lbean.getBRCODE());
		    Pstat.setInt(7, lbean.getLEAVEPURP());
		    Pstat.setString(8, lbean.getLREASON());
		    Pstat.setString(9, lbean.getLADDR());
		    Pstat.setDouble(10, lbean.getLTELNO());
		    Pstat.setString(11, lbean.getAPPLDT());
		    Pstat.setString(12, lbean.getFRMDT());
		    Pstat.setString(13, lbean.getTODT());
		    Pstat.setString(14, lbean.getSANCAUTH());
		    Pstat.setString(15, lbean.getOPR_CD());
		    Pstat.setString(16, lbean.getOFF_CD());
		    Pstat.setInt(17, lbean.getSTATUS());
		    Pstat.setString(18, lbean.getSUBSTITUTE());
		    Pstat.setFloat(19, lbean.getNODAYS());
		    Pstat.setInt(20,prjsrno);
		    Pstat.executeUpdate();
		    result=true;
		   con.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}

	public  ArrayList<LeaveMasterBean> leaveDisplay(int empno1)
	{
		ResultSet rs=null;  
		ArrayList<LeaveMasterBean> List1 =new ArrayList<LeaveMasterBean>();
		try
		{
			Connection con=ConnectionManager.getConnection();
			Statement st=con.createStatement();
			LookupHandler objDesc=new LookupHandler();
			String Result1;
			String query="SELECT * FROM LEAVETRAN   WHERE empno='"+empno1+"' and TRNTYPE<>'C' AND LEAVEPURP<>5 order by trndate desc";
			rs=st.executeQuery(query);
			while(rs.next())
			{
				LeaveMasterBean lbean1=new LeaveMasterBean();
				lbean1.setLEAVECD(rs.getString("leavecd")==null?0:rs.getInt("leavecd"));
				lbean1.setEMPNO(rs.getString("empno")==null?0:rs.getInt("empno"));
				lbean1.setTRNDATE(rs.getDate("trndate")==null?"":dateFormat(rs.getDate("trndate")));
				lbean1.setTRNTYPE(rs.getString("TRNTYPE")==null?"":rs.getString("TRNTYPE"));
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
				lbean1.setSTATUS(rs.getString("STATUS")==null?0:rs.getInt("STATUS"));
				lbean1.setSUBSTITUTE(rs.getString("SUBSTITUTE")==null?"":rs.getString("SUBSTITUTE"));
				lbean1.setNODAYS(rs.getString("DAYS")==null?0:rs.getFloat("DAYS"));
				
				List1.add(lbean1);
			}
			con.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
		return List1;
	}

	public  ArrayList<LeaveMasterBean> getList(int empno) throws SQLException
	{
		ResultSet rs1=null; 
		ArrayList<LeaveMasterBean> leavelist=new ArrayList<LeaveMasterBean>();
		Connection con=ConnectionManager.getConnection();
		Statement st=null;
		String query="select top 1  * from leavebal where empno="+empno+" and baldt <= getdate() order by baldt desc";
     	try
     	{
			st = con.createStatement();
			rs1=st.executeQuery(query);
			while(rs1.next())
			{
				LeaveMasterBean bean=new LeaveMasterBean();
			
				    bean.setBALDT(rs1.getDate("BALDT")==null?"":dateFormat(rs1.getDate("BALDT")));
					bean.setEMPNO(rs1.getString("EMPNO")==null?0:rs1.getInt(("EMPNO")));
					bean.setLEAVECD(rs1.getString("LEAVECD")==null?0:rs1.getInt("LEAVECD"));
					bean.setBAL(rs1.getString("BAL")==""?0L:rs1.getLong("BAL"));
					bean.setTOTCR(rs1.getString("TOTCR")==""?0.0f:rs1.getFloat("TOTCR"));
					bean.setTOTDR(rs1.getString("TOTDR")==""?0.0f:rs1.getFloat("TOTDR"));
				
				
				leavelist.add(bean);
			}
			con.close();
		}
     	catch (SQLException e) 
     	{
			e.printStackTrace();
		}
		return leavelist;
	}

	public  ArrayList<LeaveMasterBean> searchLeave(LeaveMasterBean searchbean) 
	{
		ResultSet rs4=null;
		String Result2;
		LookupHandler objDesc1=new LookupHandler();
		int leavecode=searchbean.getLEAVECD();
		
		ArrayList<LeaveMasterBean> searchlist=new ArrayList<LeaveMasterBean>();
		Connection con=ConnectionManager.getConnection();
		try
		{
			LookupHandler objDesc=new LookupHandler();
			String Result1;
		     Statement st=con.createStatement();
		     String searchquery="SELECT * FROM LEAVETRAN WHERE EMPNO='"+searchbean.getEMPNO()+"' AND LEAVECD = '"+searchbean.getLEAVECD()+"' AND TRNDATE  BETWEEN '"+searchbean.getFRMDT()+"' AND '"+searchbean.getTODT()+"' AND TRNTYPE<>'C' ORDER BY TRNDATE DESC";
	         String  searchquery1="SELECT * FROM LEAVETRAN WHERE EMPNO='"+searchbean.getEMPNO()+"' AND TRNDATE  BETWEEN '"+searchbean.getFRMDT()+"' AND '"+searchbean.getTODT()+"' AND TRNTYPE<>'C' ORDER BY TRNDATE DESC";
	         if(leavecode == 0)
		     {
		    	 rs4=st.executeQuery(searchquery1);
		     }
		     else
		     {
		    	 rs4=st.executeQuery(searchquery); 
		     }
		     while(rs4.next())
	         {
	        	 LeaveMasterBean lbean1=new LeaveMasterBean();
	        	 lbean1.setLEAVECD(rs4.getString("leavecd")==null?0:rs4.getInt("leavecd"));
					lbean1.setEMPNO(rs4.getString("empno")==null?0:rs4.getInt("empno"));
					lbean1.setTRNDATE(rs4.getDate("trndate")==null?"":dateFormat(rs4.getDate("trndate")));
					lbean1.setTRNTYPE(rs4.getString("TRNTYPE")==null?"":rs4.getString("TRNTYPE"));
					lbean1.setAPPLNO(rs4.getString("applno")==null?"":rs4.getString("applno"));
					lbean1.setBRCODE(rs4.getString("brcode")==null?0:rs4.getInt("brcode"));

					lbean1.setLEAVEPURP(rs4.getString("leavepurp")==null?0:rs4.getInt("leavepurp"));
					lbean1.setLREASON(rs4.getString("LREASON")==null?"--":rs4.getString("LREASON"));
					lbean1.setLADDR(rs4.getString("LADDR")==null?"--":rs4.getString("LADDR"));
					lbean1.setLTELNO(rs4.getString("LTELNO")==null?0:rs4.getLong("LTELNO"));
					lbean1.setAPPLDT(rs4.getDate("APPLDT")==null?"":dateFormat(rs4.getDate("APPLDT")));
					lbean1.setFRMDT(rs4.getDate("FRMDT")==null?"":dateFormat(rs4.getDate("FRMDT")));
					lbean1.setTODT(rs4.getDate("TODT")==null?"":dateFormat(rs4.getDate("TODT")));
					Lookup lkhp = new Lookup();
					lkhp = objDesc.getLookup("SAUTH-"+rs4.getInt("SANCAUTH"));
					Result1 = lkhp.getLKP_DESC();
					lbean1.setSANCAUTH(Result1);
					lbean1.setOPR_CD(rs4.getString("OPR_CD")==null?"":rs4.getString("OPR_CD"));
					lbean1.setOFF_CD(rs4.getString("OFF_CD")==null?"":rs4.getString("OFF_CD"));
					lbean1.setSTATUS(rs4.getString("STATUS")==null?0:rs4.getInt("STATUS"));
					lbean1.setSUBSTITUTE(rs4.getString("SUBSTITUTE")==null?"":rs4.getString("SUBSTITUTE"));
					lbean1.setNODAYS(rs4.getString("DAYS")==null?0:rs4.getFloat("DAYS"));
	        	 searchlist.add(lbean1);
	          }
		     con.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return searchlist;
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

	public ArrayList<LeaveMasterBean> getlast(int empno) 
	{
		ArrayList<LeaveMasterBean> listc=new ArrayList<LeaveMasterBean>();
		Connection con=ConnectionManager.getConnection();
		ResultSet rs4;
		String Result3;
		LookupHandler objDesc1=new LookupHandler();
		String query="SELECT distinct * from LEAVETRAN a where a.EMPNO='"+empno+"' AND TRNDATE=(select distinct max(TRNDATE) from LEAVETRAN b where EMPNO='"+empno+"' AND LEAVECD=a.LEAVECD) AND STATUS = 41 AND STATUS <> 98";
		try
		{
			LookupHandler objDesc=new LookupHandler();
			String Result1;
			Statement st=con.createStatement();
			rs4=st.executeQuery(query);
			while(rs4.next())
			{
				LeaveMasterBean lbean1=new LeaveMasterBean();
				lbean1.setLEAVECD(rs4.getString("leavecd")==null?0:rs4.getInt("leavecd"));
				lbean1.setEMPNO(rs4.getString("empno")==null?0:rs4.getInt("empno"));
				lbean1.setTRNDATE(rs4.getDate("trndate")==null?"":dateFormat(rs4.getDate("trndate")));
				lbean1.setTRNTYPE(rs4.getString("TRNTYPE")==null?"":rs4.getString("TRNTYPE"));
				lbean1.setAPPLNO(rs4.getString("applno")==null?"":rs4.getString("applno"));
				lbean1.setBRCODE(rs4.getString("brcode")==null?0:rs4.getInt("brcode"));

				lbean1.setLEAVEPURP(rs4.getString("leavepurp")==null?0:rs4.getInt("leavepurp"));
				lbean1.setLREASON(rs4.getString("LREASON")==null?"--":rs4.getString("LREASON"));
				lbean1.setLADDR(rs4.getString("LADDR")==null?"--":rs4.getString("LADDR"));
				lbean1.setLTELNO(rs4.getString("LTELNO")==null?0:rs4.getLong("LTELNO"));
				lbean1.setAPPLDT(rs4.getDate("APPLDT")==null?"":dateFormat(rs4.getDate("APPLDT")));
				lbean1.setFRMDT(rs4.getDate("FRMDT")==null?"":dateFormat(rs4.getDate("FRMDT")));
				lbean1.setTODT(rs4.getDate("TODT")==null?"":dateFormat(rs4.getDate("TODT")));
				Lookup lkhp = new Lookup();
				lkhp = objDesc.getLookup("SAUTH-"+rs4.getInt("SANCAUTH"));
				Result1 = lkhp.getLKP_DESC();
				lbean1.setSANCAUTH(Result1);
				lbean1.setOPR_CD(rs4.getString("OPR_CD")==null?"":rs4.getString("OPR_CD"));
				lbean1.setOFF_CD(rs4.getString("OFF_CD")==null?"":rs4.getString("OFF_CD"));
				lbean1.setSTATUS(rs4.getString("STATUS")==null?0:rs4.getInt("STATUS"));
				lbean1.setSUBSTITUTE(rs4.getString("SUBSTITUTE")==null?"":rs4.getString("SUBSTITUTE"));
				lbean1.setNODAYS(rs4.getString("DAYS")==null?0:rs4.getFloat("DAYS"));
				listc.add(lbean1);	 
			}
			con.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return listc;
	}

	public  boolean upDateStatus(LeaveMasterBean cancelbean) 
	{
		boolean flag=false;
		Connection con=ConnectionManager.getConnection();
		ResultSet rs7=null;
		ResultSet rs8=null;
		try
		{
			String update="UPDATE LEAVETRAN SET STATUS='"+cancelbean.getSTATUS()+"' WHERE EMPNO ='"+cancelbean.getEMPNO()+"' AND LEAVECD='"+cancelbean.getLEAVECD()+"' AND APPLNO='"+cancelbean.getAPPLNO()+"'";
			String noofday="SELECT DATEDIFF(day,'"+cancelbean.getFRMDT()+"','"+cancelbean.getTODT()+"') AS DiffDate";
			String getballeave="SELECT distinct * from LEAVEBAL a where a.EMPNO='"+cancelbean.getEMPNO()+"' AND BALDT=(select distinct max(BALDT) from LEAVEBAL b where EMPNO='"+cancelbean.getEMPNO() +"' AND LEAVECD='"+cancelbean.getLEAVECD()+"')";
			Statement st3=con.createStatement();
			Statement st1=con.createStatement();
			int p=st1.executeUpdate(update);
			rs7=st3.executeQuery(noofday);
			rs7.next();
			int canceldays=Integer.parseInt(rs7.getString(1));
			rs8=st1.executeQuery(getballeave);
			int bal=0;
			int  tocr=0;
			int todr = 0;
			while(rs8.next())
			{
				String  baldate=rs8.getString("BALDT");
				bal = Integer.parseInt(rs8.getString("BAL")==null?"0":rs8.getString("BAL"));
				tocr=Integer.parseInt(rs8.getString("TOTCR")==null?"0":rs8.getString("TOTCR"));
			    todr=Integer.parseInt(rs8.getString("TOTDR")==null?"0":rs8.getString("TOTDR"));
			}
			bal=bal+canceldays;
			todr=todr-canceldays;
			String updateLeavBal="UPDATE LEAVEBAL  SET BAL='"+bal+"',TOTDR='"+todr+"' WHERE EMPNO='"+cancelbean.getEMPNO()+"' AND LEAVECD='"+cancelbean.getLEAVECD()+"' AND BALDT=(select distinct max(BALDT) from LEAVEBAL b where EMPNO='"+cancelbean.getEMPNO()+"' AND LEAVECD='"+cancelbean.getLEAVECD()+"')";
			st3.executeUpdate(updateLeavBal);
			if(p==1)
			{
				flag=true;
			}
			con.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return flag;
	}

	public  boolean addSubstitute(LeaveMasterBean subBean) 
	{
		boolean flag2=false;
		String query="INSERT INTO SUBSTITUTE VALUES(?,?,?,?,?,?,?) ";
		Connection con=ConnectionManager.getConnection();
		try
		{
			PreparedStatement ps=con.prepareStatement(query);
			Statement st = con.createStatement();
			int srno =0;
			String maxsrno="select max(srno) from substitute where applno='"+subBean.getAPPLNO()+"'";
			ResultSet rs = st.executeQuery(maxsrno);
			while(rs.next())
			{
				srno = rs.getInt(1);
			}
        	srno=srno+1;
			ps.setInt(1,subBean.getEMPNO());
			ps.setString(2,subBean.getAPPLNO());
			ps.setInt(3,srno);
			ps.setInt(4, subBean.getSUBSTCD());
			ps.setString(5, subBean.getFRMDT());
			ps.setString(6, subBean.getTODT());
			ps.setInt(7, subBean.getSTATUS());
			ps.executeUpdate();
			flag2=true;
			con.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return flag2;
	}

	public String getempName(int empno) 
	{
		Connection con=ConnectionManager.getConnection();
		String sqlName="SELECT FNAME, MNAME, LNAME FROM EMPMAST WHERE EMPNO='"+empno+"'";
		ResultSet rs9=null;
		String empname="";
		//System.out.println(sqlName);
		try
		{
			Statement st=con.createStatement();
			rs9=st.executeQuery(sqlName);
			rs9.next();
			//empname=(rs9.getString("FNAME"))+" "+(rs9.getString("LNAME"));
			empname=(rs9.getString("FNAME"))+" "+(rs9.getString("MNAME"))+" "+(rs9.getString("LNAME"));
			con.close();
		}
		catch( SQLException e)
		{
			e.printStackTrace();
		}
		return empname;
	}
	
	////for empcode  and transactiondate by akshay
	
	public String getempCode (int empno) 
	{
	Connection con=ConnectionManager.getConnection();
	String sqlName="SELECT EMPCODE FROM EMPMAST WHERE EMPNO='"+empno+"'";
	ResultSet rs9=null;
	String empcd="";
	//System.out.println(sqlName);
	try
	{
		Statement st=con.createStatement();
		rs9=st.executeQuery(sqlName);
		rs9.next();
		//empname=(rs9.getString("FNAME"))+" "+(rs9.getString("LNAME"));
		empcd=(rs9.getString("EMPCODE"));
		con.close();
	}
	catch( SQLException e)
	{
		e.printStackTrace();
	}
	return empcd;

	
}
	
	public String gettrndt (int empno) 
	{
	Connection con=ConnectionManager.getConnection();
	String sqlName="SELECT TRNDT FROM PAYTRAN WHERE EMPNO='"+empno+"'";
	ResultSet rs9=null;
	String trndt="";
	//System.out.println(sqlName);
	try
	{
		Statement st=con.createStatement();
		rs9=st.executeQuery(sqlName);
		rs9.next();
		//empname=(rs9.getString("FNAME"))+" "+(rs9.getString("LNAME"));
		trndt=(rs9.getString("TRNDT"));
		con.close();
	}
	catch( SQLException e)
	{
		e.printStackTrace();
	}
	return trndt;

	
}
	
	
	

	public ArrayList<LeaveMasterBean> getsubsList(LeaveMasterBean subBean1) 
	{
		LeaveMasterBean subBean=new LeaveMasterBean();
		subBean=subBean1;
		ArrayList<LeaveMasterBean> substList=new ArrayList<LeaveMasterBean>();
		String sql="SELECT * FROM SUBSTITUTE WHERE EMPNO='"+subBean.getEMPNO()+"' AND APPLNO='"+ subBean.getAPPLNO()+"' AND STATUS <>98";
		ResultSet Rs=null;
		try
		{
			Connection con=ConnectionManager.getConnection();
			Statement st=con.createStatement();
			Rs=st.executeQuery(sql);
			while(Rs.next())
			{
				LeaveMasterBean sub =new LeaveMasterBean();
				sub.setEMPNO(Rs.getString(1)==null?0:Rs.getInt(1));
				sub.setAPPLNO(Rs.getString(2)==null?"":Rs.getString(2));
				sub.setSRNO(Rs.getString(3)==null?0:Rs.getInt(3));
				sub.setSUBSTCD(Rs.getString(4)==null?0:Rs.getInt(4));
				sub.setFRMDT(Rs.getDate(5)==null?"":dateFormat(Rs.getDate(5)));
				sub.setTODT(Rs.getDate(6)==null?"":dateFormat(Rs.getDate(6)));
				sub.setSTATUS(Rs.getString(7)==null?0:Rs.getInt(7));
				substList.add(sub);
			}
			con.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return substList;
	}

	public boolean editSubstitute(LeaveMasterBean editbean) 
	{
		boolean flag=false;
		String updateQuery="UPDATE SUBSTITUTE SET SUBSTCD='"+editbean.getSUBSTCD()+"',FROMDATE='"+editbean.getFRMDT()+"',TODATE='"+ editbean.getTODT()+"' WHERE EMPNO='"+editbean.getEMPNO()+"'AND APPLNO='"+editbean.getAPPLNO()+"' AND SRNO='"+editbean.getSRNO()+"'";
		try
		{
			Connection con=ConnectionManager.getConnection();
			Statement st=con.createStatement();
			st.executeUpdate(updateQuery);
			flag=true;
			con.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}

	public boolean deleteSubstitute( LeaveMasterBean dlbean)
	{
		boolean f=false;
		Connection con=ConnectionManager.getConnection();
		String deleteQuery="UPDATE SUBSTITUTE SET STATUS='"+dlbean.getSTATUS()+"' WHERE EMPNO='"+dlbean.getEMPNO()+"' AND SRNO='"+dlbean.getSRNO()+"' AND SUBSTCD='"+dlbean.getSUBSTCD()+"'";
		try
		{
			Statement st=con.createStatement();
			st.executeUpdate(deleteQuery);
			f=true;
			con.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return f;
	}

	public ArrayList<LeaveMasterBean> getSanctionList() 
	{
		ArrayList<LeaveMasterBean> sanctionlist=new ArrayList<>();
		String sanctionquery="SELECT * FROM LEAVETRAN WHERE STATUS=1 AND ROWNUM <= 20";
		Connection conn=ConnectionManager.getConnection();
		try
		{
			LookupHandler objDesc=new LookupHandler();
			String Result1;
			Statement sts=conn.createStatement();
			ResultSet rs4=sts.executeQuery(sanctionquery);
			while(rs4.next())
			{
				LeaveMasterBean lbean1= new LeaveMasterBean();
				lbean1.setLEAVECD(rs4.getString("leavecd")==null?0:rs4.getInt("leavecd"));
				lbean1.setEMPNO(rs4.getString("empno")==null?0:rs4.getInt("empno"));
				lbean1.setTRNDATE(rs4.getDate("trndate")==null?"":dateFormat(rs4.getDate("trndate")));
				lbean1.setTRNTYPE(rs4.getString("TRNTYPE")==null?"":rs4.getString("TRNTYPE"));
				lbean1.setAPPLNO(rs4.getString("applno")==null?"":rs4.getString("applno"));
				lbean1.setBRCODE(rs4.getString("brcode")==null?0:rs4.getInt("brcode"));

				lbean1.setLEAVEPURP(rs4.getString("leavepurp")==null?0:rs4.getInt("leavepurp"));
				lbean1.setLREASON(rs4.getString("LREASON")==null?"--":rs4.getString("LREASON"));
				lbean1.setLADDR(rs4.getString("LADDR")==null?"--":rs4.getString("LADDR"));
				lbean1.setLTELNO(rs4.getString("LTELNO")==null?0:rs4.getLong("LTELNO"));
				lbean1.setAPPLDT(rs4.getDate("APPLDT")==null?"":dateFormat(rs4.getDate("APPLDT")));
				lbean1.setFRMDT(rs4.getDate("FRMDT")==null?"":dateFormat(rs4.getDate("FRMDT")));
				lbean1.setTODT(rs4.getDate("TODT")==null?"":dateFormat(rs4.getDate("TODT")));
				Lookup lkhp = new Lookup();
				lkhp = objDesc.getLookup("SAUTH-"+rs4.getInt("SANCAUTH"));
				Result1 = lkhp.getLKP_DESC();
				lbean1.setSANCAUTH(Result1);
				lbean1.setOPR_CD(rs4.getString("OPR_CD")==null?"":rs4.getString("OPR_CD"));
				lbean1.setOFF_CD(rs4.getString("OFF_CD")==null?"":rs4.getString("OFF_CD"));
				lbean1.setSTATUS(rs4.getString("STATUS")==null?0:rs4.getInt("STATUS"));
				lbean1.setSUBSTITUTE(rs4.getString("SUBSTITUTE")==null?"":rs4.getString("SUBSTITUTE"));
				lbean1.setNODAYS(rs4.getString("DAYS")==null?0:rs4.getFloat("DAYS"));
				sanctionlist.add(lbean1);
			} 
			conn.close();
		}
		 catch( Exception e)
		 {
			 e.printStackTrace();
		}
		return sanctionlist;
	  }

	
	/* THIS method is write for sanctioning leave application it update status and also entry into leavebal table*/
	public boolean setSanction(String team) throws SQLException 
	{
		String key[] = team.split(":");
		String applno= key[0];
		int empno1 =Integer.parseInt(key[1]);
		boolean flag=false;
		Connection conn=ConnectionManager.getConnection();
		conn.setAutoCommit(false);
		String sql="UPDATE LEAVETRAN SET STATUS=41 WHERE EMPNO='"+empno1+"' AND APPLNO='"+applno+"'";
		String selectdetail="select * from leavetran where EMPNO='"+empno1+"' AND APPLNO='"+applno+"'";
		String fromdate="";
		String todate="";
		String leavecd="";
		try
		{
			Statement stmt=conn.createStatement();
			stmt.executeUpdate(sql);
			ResultSet rslt=stmt.executeQuery(selectdetail);
			while(rslt.next())
			{
				leavecd=rslt.getString("LEAVECD");
				fromdate=dateFormat(rslt.getDate("FRMDT"));
				todate=dateFormat(rslt.getDate("TODT"));
			}
			Statement stmt6=conn.createStatement(); 
			String days = "SELECT DATEDIFF(day,'"+fromdate+"','"+todate+"') AS DiffDate";
			ResultSet rs1;
			int day = 0;
			rs1=stmt6.executeQuery(days);
			rs1.next();
			if (rs1 != null)
			{
				day =Integer.parseInt( rs1.getString(1))+1;
			} 
			ResultSet rs3;
			String querybal="SELECT distinct bal,totdr,TOTCR FROM LEAVEBAL a where a.EMPNO='"+empno1+"' AND BALDT=(select distinct max(BALDT) from LEAVEBAL b where EMPNO='"+empno1+"' AND LEAVECD ='"+leavecd+"')";
			rs3=stmt6.executeQuery(querybal);
			int bal=0;
			int totdr=0;
			int tcredit=50;
			while(rs3.next())
			{
				bal=Integer.parseInt(rs3.getString(1)==null?"0":rs3.getString(1));
				totdr=Integer.parseInt(rs3.getString(2)==null?"0":rs3.getString(2));
				tcredit=Integer.parseInt(rs3.getString(3)==null?"0":rs3.getString(3));
			}
			day=day+totdr;
			bal=tcredit-day;
			String Baldate=getDateMethod();
			String query2="INSERT INTO LEAVEBAL VALUES('"+Baldate+"','"+empno1+"','"+leavecd+"','"+bal+"','"+tcredit+"','"+day+"')";
			stmt6.executeUpdate(query2);
			conn.commit();
			flag=true;
			conn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}

	public ArrayList<LeaveMasterBean> getSanctionedList()
	{
		ArrayList<LeaveMasterBean> sanclist =new ArrayList<>();
        String sanctionquery="SELECT * FROM LEAVETRAN WHERE STATUS=41 AND ROWNUM <= 20";
        Connection conn=ConnectionManager.getConnection();
        try
        {
        	String Result1;
			LookupHandler objDesc=new LookupHandler();
        	Statement sts=conn.createStatement();
        	ResultSet rs4=sts.executeQuery(sanctionquery);
			while(rs4.next())
			{
				LeaveMasterBean lbean1= new LeaveMasterBean();
				
				
				lbean1.setLEAVECD(rs4.getString("leavecd")==null?0:rs4.getInt("leavecd"));
				lbean1.setEMPNO(rs4.getString("empno")==null?0:rs4.getInt("empno"));
				lbean1.setTRNDATE(rs4.getDate("trndate")==null?"":dateFormat(rs4.getDate("trndate")));
				lbean1.setTRNTYPE(rs4.getString("TRNTYPE")==null?"":rs4.getString("TRNTYPE"));
				lbean1.setAPPLNO(rs4.getString("applno")==null?"":rs4.getString("applno"));
				lbean1.setBRCODE(rs4.getString("brcode")==null?0:rs4.getInt("brcode"));

				lbean1.setLEAVEPURP(rs4.getString("leavepurp")==null?0:rs4.getInt("leavepurp"));
				lbean1.setLREASON(rs4.getString("LREASON")==null?"--":rs4.getString("LREASON"));
				lbean1.setLADDR(rs4.getString("LADDR")==null?"--":rs4.getString("LADDR"));
				lbean1.setLTELNO(rs4.getString("LTELNO")==null?0:rs4.getLong("LTELNO"));
				lbean1.setAPPLDT(rs4.getDate("APPLDT")==null?"":dateFormat(rs4.getDate("APPLDT")));
				lbean1.setFRMDT(rs4.getDate("FRMDT")==null?"":dateFormat(rs4.getDate("FRMDT")));
				lbean1.setTODT(rs4.getDate("TODT")==null?"":dateFormat(rs4.getDate("TODT")));
				Lookup lkhp = new Lookup();
				lkhp = objDesc.getLookup("SAUTH-"+rs4.getInt("SANCAUTH"));
				Result1 = lkhp.getLKP_DESC();
				lbean1.setSANCAUTH(Result1);
				lbean1.setOPR_CD(rs4.getString("OPR_CD")==null?"":rs4.getString("OPR_CD"));
				lbean1.setOFF_CD(rs4.getString("OFF_CD")==null?"":rs4.getString("OFF_CD"));
				lbean1.setSTATUS(rs4.getString("STATUS")==null?0:rs4.getInt("STATUS"));
				lbean1.setSUBSTITUTE(rs4.getString("SUBSTITUTE")==null?"":rs4.getString("SUBSTITUTE"));
				lbean1.setNODAYS(rs4.getString("DAYS")==null?0:rs4.getFloat("DAYS"));
				sanclist.add(lbean1);
			}
			conn.close();
        }
        catch( Exception e)
        {
        	e.printStackTrace();
		}
        return sanclist;
	}

	public ArrayList<LeaveMasterBean> getSearch(LeaveMasterBean sancBean1) 
	{
		ArrayList<LeaveMasterBean> list3 =new ArrayList<>();
		String type=sancBean1.getSerachtype();
		Connection con=ConnectionManager.getConnection();
		String searchAll="SELECT * FROM LEAVETRAN  WHERE EMPNO between '"+sancBean1.getEMPNO()+"' AND '"+sancBean1.getEMPNO2()+"' AND FRMDT between '"+sancBean1.getFRMDT()+"'AND '"+sancBean1.getTODT()+"' AND  TODT between '"+sancBean1.getFRMDT()+"'AND '"+sancBean1.getTODT()+"'";
		String searchSanction="SELECT * FROM LEAVETRAN  WHERE EMPNO between '"+sancBean1.getEMPNO()+"' AND '"+sancBean1.getEMPNO2()+"' AND FRMDT between '"+sancBean1.getFRMDT()+"'AND '"+sancBean1.getTODT()+"' AND  TODT between '"+sancBean1.getFRMDT()+"'AND '"+sancBean1.getTODT()+"' AND  STATUS=41";
		String searchPending="SELECT * FROM LEAVETRAN  WHERE EMPNO between '"+sancBean1.getEMPNO()+"' AND '"+sancBean1.getEMPNO2()+"' AND FRMDT between '"+sancBean1.getFRMDT()+"'AND '"+sancBean1.getTODT()+"' AND  TODT between '"+sancBean1.getFRMDT()+"'AND '"+sancBean1.getTODT()+"' AND  STATUS=1";
		try
		{
			ResultSet rs4=null;
			Statement stmt1=con.createStatement();
			if(type.equals("All"))
			{
				rs4=stmt1.executeQuery(searchAll) ;
			}
			if(type.equals("pending"))
			{
				rs4=stmt1.executeQuery(searchPending);  
			}
			if(type.equals("sanction"))
			{
				rs4=stmt1.executeQuery(searchSanction) ; 
			}
			while(rs4.next())
			{
				LeaveMasterBean lbean1=new LeaveMasterBean();
				lbean1.setLEAVECD(rs4.getString("leavecd")==null?0:rs4.getInt("leavecd"));
				lbean1.setEMPNO(rs4.getString("empno")==null?0:rs4.getInt("empno"));
				lbean1.setTRNDATE(rs4.getDate("trndate")==null?"":dateFormat(rs4.getDate("trndate")));
				lbean1.setTRNTYPE(rs4.getString("TRNTYPE")==null?"":rs4.getString("TRNTYPE"));
				lbean1.setAPPLNO(rs4.getString("applno")==null?"":rs4.getString("applno"));
				lbean1.setBRCODE(rs4.getString("brcode")==null?0:rs4.getInt("brcode"));

				lbean1.setLEAVEPURP(rs4.getString("leavepurp")==null?0:rs4.getInt("leavepurp"));
				lbean1.setLREASON(rs4.getString("LREASON")==null?"--":rs4.getString("LREASON"));
				lbean1.setLADDR(rs4.getString("LADDR")==null?"--":rs4.getString("LADDR"));
				lbean1.setLTELNO(rs4.getString("LTELNO")==null?0:rs4.getLong("LTELNO"));
				lbean1.setAPPLDT(rs4.getDate("APPLDT")==null?"":dateFormat(rs4.getDate("APPLDT")));
				lbean1.setFRMDT(rs4.getDate("FRMDT")==null?"":dateFormat(rs4.getDate("FRMDT")));
				lbean1.setTODT(rs4.getDate("TODT")==null?"":dateFormat(rs4.getDate("TODT")));
				Lookup lkhp = new Lookup();
				String Result1;
				LookupHandler objDesc=new LookupHandler();
				lkhp = objDesc.getLookup("SAUTH-"+rs4.getInt("SANCAUTH"));
				Result1 = lkhp.getLKP_DESC();
				lbean1.setSANCAUTH(Result1);
				lbean1.setOPR_CD(rs4.getString("OPR_CD")==null?"":rs4.getString("OPR_CD"));
				lbean1.setOFF_CD(rs4.getString("OFF_CD")==null?"":rs4.getString("OFF_CD"));
				lbean1.setSTATUS(rs4.getString("STATUS")==null?0:rs4.getInt("STATUS"));
				lbean1.setSUBSTITUTE(rs4.getString("SUBSTITUTE")==null?"":rs4.getString("SUBSTITUTE"));
				lbean1.setNODAYS(rs4.getString("DAYS")==null?0:rs4.getFloat("DAYS"));
				list3.add(lbean1);
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list3;
	}
	
	public String getDateMethod()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = new Date();
		String yourDate = dateFormat.format(date);
		return yourDate;
	}
	
	public boolean addleavecdDetail(LeaveMassBean objbean)
	{
		boolean flag=false;
		Connection con = ConnectionManager.getConnection();
		Statement st;
		int SRNO=1;
		
		String insert="INSERT INTO LEAVEMASS values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try 
		{
			st = con.createStatement();
			ResultSet rs = st.executeQuery("select max(SRNO) from LEAVEMASS");
			if(rs.next())
			{
				SRNO = rs.getInt(1) + 1;		
			}
			PreparedStatement pstmt=con.prepareStatement(insert);
			pstmt.setString(1,objbean.getEFFDATE());
			pstmt.setInt(2, objbean.getLEAVECD());
			pstmt.setString(3, objbean.getFREQUENCY());
		    pstmt.setFloat(4, objbean.getCRLIM());
			pstmt.setFloat(5,objbean.getMAXCUMLIM());
			pstmt.setFloat(6,objbean.getMAXCF());
			pstmt.setFloat(7,objbean.getMINLIM());
			pstmt.setString(8, objbean.getFBEGINDATE());
			pstmt.setString(9, objbean.getFENDDATE());
			pstmt.setString(10, objbean.getLEAVEDES());
			pstmt.setString(11, objbean.getCONS_HOLIDAYS());
			pstmt.setString(12, objbean.getLEAVEINCASH());
			pstmt.setInt(13, SRNO);
			pstmt.setString(14, objbean.getWEEKOFF());
			
			pstmt.executeUpdate();
			flag=true;
			con.close();
		}
		catch(Exception e)
		{
	        e.printStackTrace();
		}
		return flag;
	}

	public ArrayList<LeaveMassBean> getleavetypeList(String sql)
	{
	    ArrayList<LeaveMassBean> alist=new ArrayList<>();
	    Connection con = ConnectionManager.getConnection();
	    try
	    {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next())
			{
				LeaveMassBean lbean=new LeaveMassBean();
				lbean.setEFFDATE(rs.getDate("EFFDATE")==null?"":dateFormat(rs.getDate("EFFDATE")));
				lbean.setLEAVECD(rs.getString("LEAVECD")==null?0:rs.getInt("LEAVECD"));
				lbean.setFREQUENCY(rs.getString("FREQUENCY")==null?"":rs.getString("FREQUENCY"));
				lbean.setCRLIM(rs.getString("CRLIM")==null?0:rs.getFloat("CRLIM"));
				lbean.setMAXCUMLIM(rs.getString("MAXCUMLIM")==null?0:rs.getFloat("MAXCUMLIM"));
				lbean.setMAXCF(rs.getString("MAXCF")==null?0:rs.getFloat("MAXCF"));
				lbean.setMINLIM(rs.getString("MINLIM")==null?0:rs.getFloat("MINLIM"));
				lbean.setFBEGINDATE(rs.getString("FBEGINDATE")==null?"":dateFormat(rs.getDate("FBEGINDATE")));
				lbean.setFENDDATE(rs.getString("FENDDATE")==null?"":dateFormat(rs.getDate("FENDDATE")));
				lbean.setLEAVEDES(rs.getString("LEAVEDES")==null?"":rs.getString("LEAVEDES"));
				lbean.setCONS_HOLIDAYS(rs.getString("CONS_HOLIDAYS")==null?"":rs.getString("CONS_HOLIDAYS"));
				lbean.setLEAVEINCASH(rs.getString("LEAVEINCASH")==null?"":rs.getString("LEAVEINCASH"));
				lbean.setSRNO(rs.getString("SRNO")==null?0:rs.getInt("SRNO"));
				lbean.setWEEKOFF(rs.getString("CONS_WEEK_OFF")==null?"":rs.getString("CONS_WEEK_OFF"));
				alist.add(lbean);
			}
			con.close();
	    } 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
		return alist;
	}

	public boolean editLeavetype(LeaveMassBean objbean)
	{
		boolean flag=false;
		Connection con=ConnectionManager.getConnection();
		String update="update leavemass set " +
				"EFFDATE='"+objbean.getEFFDATE()+"'," +
				"FREQUENCY='"+objbean.getFREQUENCY() +"'," +
				"CRLIM='"+objbean.getCRLIM()+"'," +
				"MAXCUMLIM='"+objbean.getMAXCUMLIM()+"'," +
				"MAXCF='"+objbean.getMAXCF() +"'," +
				"MINLIM='"+objbean.getMINLIM() +"'," +
				"FBEGINDATE='"+objbean.getFBEGINDATE() +"'," +
				"FENDDATE='"+objbean.getFENDDATE() +"'," +
				"LEAVEDES='"+objbean.getLEAVEDES()+"'," +
				"LEAVEINCASH='"+objbean.getLEAVEINCASH()+"'," +
				"CONS_HOLIDAYS='"+objbean.getCONS_HOLIDAYS()+"'," +
				"CONS_WEEK_OFF='"+objbean.getWEEKOFF()+"'" +
				"where SRNO='"+objbean.getSRNO()+"'";
		try
		{
			Statement stmt=con.createStatement();
			stmt.executeUpdate(update);
			flag=true;
			con.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return flag;
	}
	public boolean dataCheckExist(int lcode, String bgdate, String enddate)
	{
		boolean flag=false;
		Connection con=ConnectionManager.getConnection();
		ResultSet rs;
		try
		{
			Statement st = con.createStatement();
			String query="Select * from LEAVEMASS where  LEAVECD= "+lcode ; 
					//" and  FBEGINDATE  between '"+bgdate+"' and '"+enddate+"'";
			rs=st.executeQuery(query);
			if(rs.next())
			{
				flag=true;
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
}
