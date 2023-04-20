package payroll.Core;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import payroll.DAO.ConnectionManager;
import payroll.DAO.EmpOffHandler;
import payroll.DAO.EmployeeHandler;
import payroll.DAO.LMH;
import payroll.DAO.LeaveMasterHandler;
import payroll.DAO.UsrHandler;
import payroll.Model.EmpOffBean;
import payroll.Model.EmployeeBean;
import payroll.Model.LeaveMassBean;

public class Utility {
	//-------- Method to format the date in DD-MON-YYYY format -----------
	public static String dateFormat(Date date)
	{
		if(date==null)
		{
			return "---";
		}
		String result="";
		SimpleDateFormat format=new SimpleDateFormat("dd-MMM-yyyy");
		result=format.format(date);
		return result;
	}
	
	public static void makeFile(String fileName)
	{
		System.out.println("Creating File");
		File file = new File(fileName);
		try
		{
			FileWriter FW = new FileWriter(file);
			FW.close();
			System.out.println("File Created");
		}
		catch(Exception e)
		{
			//UsrHandler.senderrormail(e,"utility.makeFile");
			System.out.println("File Not Created");
		}
	}
	
	public void autoLeaveCredit(String emp,int srno,String month)
	{
		try
		{
			Connection con = ConnectionManager.getConnection();
			LeaveMasterHandler LMH = new LeaveMasterHandler();
			ArrayList<LeaveMassBean> list =  LMH.getleavetypeList("SELECT * FROM LEAVEMASS WHERE SRNO ="+srno);
			String[] empl = emp.split(",");
			int[] empList = new int[empl.length]; 
			for(int i=0;i<empl.length;i++)
			{
				empList[i] = Integer.parseInt(empl[i]);
			}
/*
LeaveMass table			
	EFFDATE | LEAVECD |  FREQUENCY  |  CRLIM  |  MAXCUMLIM  |  MAXCF |  MINLIM |  FBEGINDATE |  FENDDATE | LEAVEDES | CONS_HOLIDAYS |
	LEAVEINCASH | SRNO

LeaveBal
	 BALDT | EMPNO | LEAVECD | BAL | TOTCR | TOTDR

LeaveTran
  LEAVECD | EMPNO | TRNDATE | TRNTYPE | APPLNO | BRCODE | LEAVEPURP | LREASON | LADDR | LTELNO | APPLDT | FRMDT | TODT | SANCAUTH |
  OPR_CD  | OFF_CD  | STATUS | SUBSTITUTE | DAYS
			
*/			if(list.size() > 0)
			{
				LeaveMassBean lbean = list.get(0);
				float leaveNum = lbean.getCRLIM();
				float factor = 0;
				if(leaveNum > 0)
				{	
					if(lbean.getFREQUENCY().equalsIgnoreCase("M"))	//Monthly
					{
						if(leaveNum >=12)
						{
							factor = 12;
						}
						else
						{
							factor = leaveNum;
						}
					}
					else if(lbean.getFREQUENCY().equalsIgnoreCase("Q"))	//Quarterly
					{
						if(leaveNum >=3)
						{
							factor = 3;
						}
						else
						{
							factor = leaveNum;
						}
					}
					else if(lbean.getFREQUENCY().equalsIgnoreCase("H"))	//Half Yearly
					{
						if(leaveNum >=2)
						{
							factor = 2;
						}
						else
						{
							factor = leaveNum;
						}
					}
					else if(lbean.getFREQUENCY().equalsIgnoreCase("Y"))	//Yearly
					{
						factor = 1;
					}
					creditMethod(empList,factor,lbean,leaveNum,month,con);
				}
			}
			con.close();
		}
		catch(Exception e)
		{
			//UsrHandler.senderrormail(e,"utility.addleavecredit");
			e.printStackTrace();
		}
	}
	
	public void creditMethod(int[] empList,float factor,LeaveMassBean lbean,float leaveNum,String month,Connection con)
	{
		float leaves = 0.0f;
		float restLeaves=0.0f;
		leaves = leaveNum / factor;
		restLeaves = leaveNum % factor;
		System.out.println("value of factor "+ factor);
	
		float[] leaveVals = new float[ Math.round(factor)];
		
		System.out.println((leaveVals.length));
		int fact =Math.round(factor);
		float fact1=factor;
		
		/*
		 //only for checking
		  while(factor>0)
		{
			System.out.println("value of factor inside while "+ factor);
			System.out.println("fact 1 value "+fact1);
		
			factor= (float) (factor-1);
			fact1=fact1+1;
			
			leaveVals[ fact1]=leaves;
		}*/
		
		for(int i=1;i<=fact;i++)
		{
			
			//System.out.println("fact1"+fact1+"fact  "+fact+" value "+i);
			if(fact1<1){
							leaveVals[i-1] = fact1;
						}
			else{
				leaveVals[i-1] = leaves;
				}
			fact1=fact1-1;
			//System.out.println("+++ "+leaves);
		}
	
		if(restLeaves>0)
		{
			for(int i=0;i<restLeaves;i++)
			{
				leaveVals[i]++;
			}
		}
		int incr =0;
		if(lbean.getFREQUENCY().equalsIgnoreCase("M"))
		{
			incr = 1;
		}
		if(lbean.getFREQUENCY().equalsIgnoreCase("Q"))
		{
			incr = 3;
		}
		if(lbean.getFREQUENCY().equalsIgnoreCase("H"))
		{
			incr = 6;
		}
		if(lbean.getFREQUENCY().equalsIgnoreCase("Y"))
		{
			incr = 12;
		}
		
		String[] dates = getDates(factor,incr,month,con);
		
		try
		{
			Statement st = con.createStatement();
			Statement st1 = con.createStatement();
			ResultSet rs = null;
			ResultSet rs1 = null;
			for(int i =0;i<empList.length;i++)
			{
				//float totLeave = LMH.displayEmpLeaves(empList[i]);
				//float fPointLeave = totLeave - (long)totLeave ;
				
				for(int j =0;j<factor;j++)
				{
					//rs1 = st1.executeQuery("SELECT * FROM LEAVETRAN WHERE LEAVECD="+lbean.getLEAVECD()+" AND EMPNO="+empList[i]+" AND TRNDATE='"+dates[j]+"' AND TRNTYPE='C' AND LEAVEPURP=5 AND DAYS="+leaveVals[j]+" AND APPLDT='"+dates[j]+"' AND FRMDT='"+dates[j]+"' AND TODT=CONVERT(NVARCHAR,DATEADD(DAY,"+(leaveVals[j]-1)+",'"+dates[j]+"'),23) AND STATUS=1");
					rs1 = st1.executeQuery("SELECT * FROM LEAVETRAN WHERE LEAVECD="+lbean.getLEAVECD()+" AND EMPNO="+empList[i]+" AND TRNTYPE='C' AND LEAVEPURP=5 AND APPLDT='"+dates[j]+"' AND FRMDT='"+dates[j]+"' AND STATUS=1");
					if(rs1.next())
					{
						System.out.println("Record Already present for Employee "+empList[i]);
					}	
					else
					{	
						
						
						
						Statement st2 = con.createStatement();
						ResultSet rs2 = null;
						rs2 = st2.executeQuery("select isnull((SELECT MAX(APPLNO)+1 FROM LEAVETRAN),1)");
						int applno = 0;
						if(rs2.next()){
							applno = rs2.getInt(1);
						}
						
						EmployeeHandler eh=new EmployeeHandler();
						EmployeeBean ebean=eh.getEmployeeInformation(Integer.toString(empList[i]));
						String dt=ebean.getDOJ();
						
						if(lbean.getFREQUENCY().equalsIgnoreCase("Y"))
						{
						float months=0;
						float month_diff=0;
						//System.out.println(days+"//////////////////"+lbean.getFBEGINDATE());
						ResultSet rs11 = st.executeQuery("SELECT DATEDIFF(MONTH, '"+lbean.getFBEGINDATE()+"', '"+dt+"') as month");
						//System.out.println("''''''''''SELECT DATEDIFF(MONTH, '"+lbean.getFBEGINDATE()+"', '"+dt+"') as month");
						if(rs11.next())
						{
						months=rs11.getInt("month");
						//System.out.println("month in DOJ check :"+month);
						}
						rs11.close();
						if (months>0)
						{
							
							rs11=st.executeQuery("SELECT DATEDIFF(MONTH, '"+dt+"', DATEADD(yy, DATEDIFF(yy,0,'"+lbean.getFBEGINDATE()+"') + 1, 0)) as month_diff");
							//System.out.println("...........SELECT DATEDIFF(MONTH, '"+dt+"', DATEADD(yy, DATEDIFF(yy,0,'"+lbean.getFBEGINDATE()+"') + 1, 0)) as month_diff");
							if(rs11.next())
							{
								month_diff=rs11.getInt("month_diff");
								//System.out.println("month in DIFFERANCE check :"+month_diff);
							}
							rs11.close();
							
							if(month_diff>0)
							{
								//System.out.println(month_diff+"DAYS---before-----"+days);
								leaveNum=(int) (leaveNum*month_diff/12);
								
								//System.out.println("DAYS---after-----"+days);
							}
							
							
						}
							
							
						}
						EmpOffHandler eoh=new EmpOffHandler();
						EmpOffBean eob= eoh.getEmpOfficAddInfo(Integer.toString(empList[i]));
						int prjsrno=eob.getPrj_srno();

						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
							st.executeUpdate("INSERT INTO LEAVETRAN(PRJ_SRNO,APPLNO,LEAVECD,EMPNO,TRNDATE,TRNTYPE,LEAVEPURP,DAYS,APPLDT,FRMDT,TODT,STATUS) " +
										 "VALUES("+prjsrno+","+applno+","+lbean.getLEAVECD()+","+empList[i]+",'"+dates[j]+"','C',5,"+(leaveVals[j])+",'"+dates[j]+"','"+dates[j]+"',CONVERT(NVARCHAR,DATEADD(DAY,"+(leaveVals[j]-1)+",'"+dates[j]+"'),23),1)");
						
							rs = st.executeQuery("SELECT TOP 1 * FROM LEAVEBAL WHERE BALDT < '"+dates[j]+"' AND EMPNO = "+empList[i]+" AND LEAVECD = "+lbean.getLEAVECD()+" ORDER BY BALDT DESC");
											
							if(rs.next())
							{
								st1.executeUpdate("INSERT INTO LEAVEBAL VALUES('"+dates[j]+"',"+empList[i]+","+lbean.getLEAVECD()+","+(rs.getInt("BAL")+leaveVals[j])+","+(rs.getInt("TOTCR")+leaveVals[j])+","+rs.getInt("TOTDR")+")");
							}
							else
							{
								st1.executeUpdate("INSERT INTO LEAVEBAL VALUES('"+dates[j]+"',"+empList[i]+","+lbean.getLEAVECD()+","+(leaveVals[j])+","+(leaveVals[j])+",0)");
							}
					}
				}
			}
		}
		catch(Exception e)
		{
			//UsrHandler.senderrormail(e,"utility.creditmethod");
			e.printStackTrace();
		}
	}
	
	public String[] getDates(float factor,int freq,String date,Connection con)
	{
		int fact =Math.round(factor);
		String[] result = new String[fact];
	
		try
		{
			Statement st = con.createStatement();
			String sql="SELECT ";
			for(int i =0;i<fact;i++)
			{
				sql+=" Convert(nvarchar,dateadd(day,"+(i*freq)+",'"+date+"'),23),";
			}
			sql = sql.substring(0, sql.length()-1);
			ResultSet rs = st.executeQuery(sql);
			
			if(rs.next())
			{
				for(int i=0;i<fact;i++)
				{
					result[i] = rs.getString(i+1);
				}
			}
			
			rs.close();
			st.close();
		}
		catch(Exception e)
		{
			//UsrHandler.senderrormail(e,"utility.getDates");
			e.printStackTrace();
		}
		return result;
	}
	
	public void autoLeaveDebit(String emp,int srno)
	{
		try
		{
			Connection con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			Statement stUP = con.createStatement();
			ResultSet lbal = null;
			ResultSet ltran = null;
			LeaveMasterHandler LMH = new LeaveMasterHandler();
			ArrayList<LeaveMassBean> list =  LMH.getleavetypeList("SELECT * FROM LEAVEMASS WHERE SRNO = "+srno);
			String[] empl = emp.split(",");
			int[] empList = new int[empl.length]; 
			for(int i=0;i<empl.length;i++)
			{
				empList[i] = Integer.parseInt(empl[i]);
			}
			if(list.size() > 0)
			{
				LeaveMassBean lbean = list.get(0);
				float maxCF = lbean.getMAXCF();
				for(int i=0;i<empList.length;i++)
				{
					int leaveTaken = 0;
					int bal = 0;
					int totCR=0;
					int totDR = 0;
					float avail = 0;
					float toCF = 0;
					float toElap = 0;
					ltran = st.executeQuery("SELECT ISNULL(SUM(DAYS),0) FROM LEAVETRAN WHERE EMPNO="+empList[i]+" AND LEAVECD="+lbean.getLEAVECD()+" AND TRNTYPE='D' AND STATUS='SANCTION' AND FRMDT >= '"+lbean.getFBEGINDATE()+"' AND TODT <='"+lbean.getFENDDATE()+"'");
					if(ltran.next())
					{
						leaveTaken = ltran.getInt(1);
					}
					lbal = st.executeQuery("SELECT TOP 1 * FROM LEAVEBAL WHERE EMPNO ="+empList[i]+" AND LEAVECD ="+lbean.getLEAVECD()+" AND BALDT <= '"+lbean.getFENDDATE()+"' ORDER BY BALDT DESC");
					if(lbal.next())
					{
						bal = lbal.getInt("BAL");
						totCR = lbal.getInt("TOTCR");
						totDR = lbal.getInt("TOTDR");
					}
					
					
					if (lbean.getFREQUENCY().equalsIgnoreCase("Y"))
					{
						if(leaveTaken < lbean.getCRLIM())
						{
							avail = lbean.getCRLIM() - leaveTaken;
						}
					}
					else if (lbean.getFREQUENCY().equalsIgnoreCase("H"))
					{
						if(leaveTaken < (lbean.getCRLIM()*2))
						{
							avail = (lbean.getCRLIM()*2) - leaveTaken;
						}
					}
					else if (lbean.getFREQUENCY().equalsIgnoreCase("Q"))
					{
						if(leaveTaken < (lbean.getCRLIM()*4))
						{
							avail = (lbean.getCRLIM()*4) - leaveTaken;
						}
					}
					else if (lbean.getFREQUENCY().equalsIgnoreCase("M"))
					{
						if(leaveTaken < (lbean.getCRLIM()*12))
						{
							avail = (lbean.getCRLIM()*12) - leaveTaken;
						}
					}
						
					
					/*if(leaveTaken < lbean.getCRLIM())
					{
						avail = lbean.getCRLIM() - leaveTaken;
					}*/
					
					if(avail >= maxCF)
					{
						toCF = maxCF;
						toElap = avail - maxCF;
					}
					else
					{
						toCF = avail;
						toElap = 0;
					}
					if(bal>0)
					{
						if(bal >= lbean.getMAXCUMLIM())
						{
							
							if (lbean.getFREQUENCY().equalsIgnoreCase("Y"))
							{
							//toElap = toElap + (toCF<(bal-lbean.getMAXCUMLIM())? toCF:(bal-lbean.getMAXCUMLIM()) );
								toElap = (toCF<(bal-lbean.getMAXCUMLIM())? toCF:(bal-lbean.getMAXCUMLIM()) );
							toCF = 0;
							}
							else
							{
							toElap=bal-toCF;
							}
						
						}
						else
						{
						
							if((bal+toCF)>lbean.getMAXCUMLIM())
							{
								toElap = toElap +( (bal + toCF) - lbean.getMAXCUMLIM() );
								toCF = lbean.getMAXCUMLIM() - bal;
								toElap=toCF;
							}
							
							
							
						}
					}
					if(toElap>0)
					{
						ltran = st.executeQuery("SELECT * FROM LEAVETRAN WHERE LEAVECD="+lbean.getLEAVECD()+" AND EMPNO="+empList[i]+" AND TRNDATE='"+lbean.getFENDDATE()+"'" +
												" AND TRNTYPE='D' AND LEAVEPURP=6 AND DAYS="+toElap+" AND APPLDT='"+lbean.getFENDDATE()+"' AND FRMDT=CONVERT(NVARCHAR,DATEADD(DAY,"+((toElap-1) * (-1))+",'"+lbean.getFENDDATE()+"'),23) AND TODT='"+lbean.getFENDDATE()+"' AND STATUS=1");
						if(ltran.next())
						{
							System.out.println("Already Debeted");
						}
						else
						{	
							
							
							
							Statement st1 = con.createStatement();
							ResultSet rs2 = null;
							rs2 = st1.executeQuery("SELECT MAX(APPLNO)+1 FROM LEAVETRAN");
							int applno = 0;
							if(rs2.next()){
								applno = rs2.getInt(1);
							}
							EmpOffHandler eoh=new EmpOffHandler();
							EmpOffBean eob= eoh.getEmpOfficAddInfo(Integer.toString(empList[i]));
							int prjsrno=eob.getPrj_srno();
							
							
							
							
							stUP.executeUpdate("INSERT INTO LEAVETRAN(PRJ_SRNO,APPLNO,LEAVECD,EMPNO,TRNDATE,TRNTYPE,LEAVEPURP,DAYS,APPLDT,FRMDT,TODT,STATUS) " +
												" VALUES("+prjsrno+","+applno+","+lbean.getLEAVECD()+","+empList[i]+",'"+lbean.getFENDDATE()+"','D',6,"+toElap+",'"+lbean.getFENDDATE()+"',CONVERT(NVARCHAR,DATEADD(DAY,"+((toElap-1) * (-1))+",'"+lbean.getFENDDATE()+"'),23),'"+lbean.getFENDDATE()+"',1)");
						}
					}
					lbal = st.executeQuery("SELECT * FROM LEAVEBAL WHERE BALDT='"+lbean.getFENDDATE()+"' AND EMPNO="+empList[i]+" AND LEAVECD="+lbean.getLEAVECD()+" AND BAL="+(bal-toElap)+" AND TOTCR="+(totCR)+" AND TOTDR="+(totDR+toElap)+"");
					if(lbal.next())
					{
						System.out.println("Record present");
					}
					else
					{	
						stUP.executeUpdate("INSERT INTO LEAVEBAL VALUES('"+lbean.getFENDDATE()+"',"+empList[i]+","+lbean.getLEAVECD()+","+(bal-toElap)+","+(totCR)+","+(totDR+toElap)+")");
					}
				}
			}
			con.close();
		}
		catch(Exception e)
		{
			//UsrHandler.senderrormail(e,"utility.leaveautodebit");
			e.printStackTrace();
		}
	}
}