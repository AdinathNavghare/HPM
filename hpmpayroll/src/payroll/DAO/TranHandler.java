package payroll.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import payroll.Core.ReportDAO;
import payroll.Core.Utility;
import payroll.Model.ItStandardBean;
import payroll.Model.SAL_Details;
import payroll.Model.TranBean;

public class TranHandler
{
	// added by akshay nikam for 226 deduction one to many loans 
	public void update_loan_details(int eno,int trncd,float amount,String dt)
	{
		String S ="";
		String S1 ="";
		float install_amount =0;
		float total_monthly_amt=0;
		float paid_amt=0;
		float paid_amt_temp=0;
		float actual_pay=0;
		float paytran_val=0;
		float paytran_val1=0;
		int flag=0;
		float total_paid=0;
		int no_loan_buffer=0;

		int no_loan=0;
		int no_loan1=0;
		
		String M ="";
		
		Connection con = null;
			
		
		try{
		con = ConnectionManager.getConnection();
	    Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		Statement st1 = con.createStatement();

	    Statement st2 = con.createStatement();
		EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
 
		 S="select * from loan_detail where empno="+eno+" and loan_code ='"+trncd+"' and Active='sanction' order by Actual_pay" ;
	   

		ResultSet rs = st2.executeQuery(S);
        System.out.println("   s=="+S);
        ResultSet rs1=st1.executeQuery("select SUM(monthly_install) as total_install,count(loan_no)as no_loan  from loan_detail where EMPNO='"+eno+"' and loan_code=226 and Active='sanction' ");
        
        
        
        
        
        
          while(rs1.next())
	      {
        	  total_monthly_amt=Float.parseFloat(rs1.getString("total_install"));
        	  no_loan=rs1.getInt("no_loan");
        	  
	      }
          System.out.println(" 1  total_monthly_amt=="+total_monthly_amt);
          no_loan_buffer=no_loan;
          System.out.println(" 2 Loan Number=="+no_loan);
      
		while(rs.next())
		{
			
			actual_pay=Float.parseFloat(rs.getString("Actual_pay"));

			install_amount=Float.parseFloat(rs.getString("monthly_install"));
			
		
			if (total_monthly_amt>install_amount)
			{
				
				
				total_paid= Float.parseFloat(rs.getString("total_paid")==null?"0.00":rs.getString("total_paid"));

				
				total_paid = total_paid +install_amount;
				  System.out.println(" 2.5 total_paid =="+total_paid);
				  System.out.println(" 3.0 install_amount =="+install_amount);
			if(actual_pay==total_paid)
			 {
				//paytran_val = total_monthly_amt - install_amount ;
				 M= "Update Loan_detail set total_paid=convert(float,'"+total_paid+"' ),Active='NIL' where empno='"+rs.getInt("EMPNO")+"' and loan_no="+rs.getString("loan_no")+"and loan_code ='"+trncd+"' ";
			     System.out.println("   m1=="+M);
			     st.executeUpdate(M);
			     
			     ResultSet rs2=st1.executeQuery("select isnull(SUM(monthly_install),0) as total_install from loan_detail where EMPNO='"+rs.getInt("EMPNO")+"' and loan_code=226 and Active='sanction' ");
			        
			        
		          while(rs2.next())
			      {
		        	  paytran_val =Float.parseFloat(rs2.getString("total_install"));
		        	
		        	  
			      }
		          System.out.println(" 17 Paytran_val is =="+paytran_val);
			//    S1 = "select SUM(monthly_install) as total_install from loan_detail where EMPNO='"+eno+"' and loan_code=226 and Active='sanction' ";

			     
			     
			     String nextMonthDate=ReportDAO.EOM("01-"+dt);
			        System.out.println(" 11 dt =="+dt);
				    Date tempDate=new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
					  Calendar calendar = Calendar.getInstance();
					tempDate=simpleDateFormat.parse(nextMonthDate);
					  System.out.println(" 22 Next_Month_Date =="+nextMonthDate);
					  System.out.println(" 33 Temp_Date =="+tempDate);
					tempDate=simpleDateFormat.parse(nextMonthDate);
					  calendar.setTime(tempDate);
					  calendar.add(Calendar.MONTH,1 );
					  nextMonthDate= simpleDateFormat.format(calendar.getTime());
					 nextMonthDate=ReportDAO.EOM(nextMonthDate);	
					 System.out.println(" 44 Temp_Date =="+tempDate);
			  
				st.executeUpdate(" update paytran set inp_amt='"+paytran_val+"', trndt= '"+nextMonthDate+"' , upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno='"+rs.getInt("EMPNO")+"' and trncd=226   ");
		     
			     
			 }
			 else
			  {
			 M= "Update Loan_detail set total_paid=convert(float,'"+total_paid+"' ) where empno = "+rs.getInt("EMPNO")+" and loan_no="+rs.getString("loan_no")+"and loan_code ='"+trncd+"' ";
		     System.out.println("   m2=="+M);
		     st.executeUpdate(M);
			  }

			
			 total_monthly_amt= total_monthly_amt - install_amount;
		     System.out.println("   amt-=="+total_monthly_amt);

			}
				
				
			else
			{
				total_paid= Float.parseFloat(rs.getString("total_paid")==null?"0.00":rs.getString("total_paid"));
				System.out.println(" 7.5 total_paid=="+total_paid);
				paid_amt_temp = total_monthly_amt+total_paid;
				System.out.println(" 8 total_monthly_amount in 2nd else if=="+total_monthly_amt);
				System.out.println(" 8.5 actual Pay in 2nd else if=="+actual_pay);
				System.out.println(" 9 Paid_amount in 2nd else if=="+paid_amt);
				System.out.println(" 10 Paid_amount_temp in 2nd else if=="+paid_amt_temp);
				 if(actual_pay == paid_amt_temp)
				{
					
				    M= "Update Loan_detail set total_paid=convert(float,'"+paid_amt_temp+"' ),Active='NIL' where empno='"+rs.getInt("EMPNO")+"' and loan_no="+rs.getString("loan_no")+"and loan_code ='"+trncd+"' ";
				     System.out.println("   m3=="+M);
				     st.executeUpdate(M);
// added from oldd
				     
				     
				     String nextMonthDate=ReportDAO.EOM("01-"+dt);
				        System.out.println(" 11 dt =="+dt);
					    Date tempDate=new Date();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
						  Calendar calendar = Calendar.getInstance();
						tempDate=simpleDateFormat.parse(nextMonthDate);
						  System.out.println(" 22 Next_Month_Date =="+nextMonthDate);
						  System.out.println(" 33 Temp_Date =="+tempDate);
						tempDate=simpleDateFormat.parse(nextMonthDate);
						  calendar.setTime(tempDate);
						  calendar.add(Calendar.MONTH,1 );
						  nextMonthDate= simpleDateFormat.format(calendar.getTime());
						 nextMonthDate=ReportDAO.EOM(nextMonthDate);	
						 System.out.println(" 44 Temp_Date =="+tempDate);
				     
				     
						 ResultSet rs3 = st1.executeQuery( "select isnull(SUM(monthly_install),0) as total_install,count(loan_no)as no_loan  from loan_detail where EMPNO='"+rs.getInt("EMPNO")+"' and loan_code=226 and Active='sanction' ");
						 
						 while(rs3.next())
					      {
							 paytran_val1 = Float.parseFloat(rs3.getString("total_install"));
							 no_loan1 =rs3.getInt("no_loan");
				        	 
					      }
						 System.out.println(" 15 Paytran_val1 =="+paytran_val1);
						 System.out.println(" 16 No_of_loan =="+no_loan1);
				     if(no_loan1 > 0)
				     {
				    		st.executeUpdate(" update paytran set inp_amt='"+paytran_val1+"', trndt= '"+nextMonthDate+"' , upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno='"+rs.getInt("EMPNO")+"' and trncd=226   ");
						     
				     }
				     else
				     {
				    	 st.executeUpdate("update paytran set CF_SW='0' , inp_amt=0.00, net_amt=0.00, cal_amt=0.00, trndt= '"+nextMonthDate+"' , upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno='"+rs.getInt("EMPNO")+"' and trncd=226   ");
				     }
				   }
				 else
				 {
					// (convert(float, '"+paid_amt+"')+convert(float,'"+total_paid+"' ))
				 M= "Update Loan_detail set total_paid=convert(float,'"+paid_amt_temp+"' ) where empno = '"+rs.getInt("EMPNO")+"' and loan_no="+rs.getString("loan_no")+"and loan_code ='"+trncd+"' ";
			     System.out.println("   m4=="+M);
			     st.executeUpdate(M);
			     
				 }

				// st.executeUpdate(M);
				// break;
			}
	
		}
//		 st.close();
			
        rs.close();
		st2.close();
		con.close();
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
	}
	//end here
	public String addTransaction(TranBean TB)
	{
		String result= "false";
		Connection con= ConnectionManager.getConnection();
		try
		{
			Statement st1 = con.createStatement();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String check = "select trncd from paytran where trncd = "+TB.getTRNCD()+" and  empno = "+TB.getEMPNO();
			String sql="INSERT INTO PAYTRAN VALUES( '"+ReportDAO.EOM(TB.getTRNDT())+"' ,"
						+TB.getEMPNO()+","
						+TB.getTRNCD()+","
						+TB.getSRNO()+","
						+TB.getINP_AMT()+","
						+TB.getCAL_AMT()+","
						+TB.getADJ_AMT()+","
						+TB.getARR_AMT()+","
						+TB.getNET_AMT()+","
						+"'"+TB.getCF_SW()+"',"
						+"'"+TB.getUSRCODE()+"',"
						+"'"+TB.getUPDDT()+"',"
						+"'"+TB.getSTATUS()+"'"
						+")"; 
			ResultSet chk = st.executeQuery(check);
			
		 System.out.println("  aaaaaa  "+check);
		 System.out.println("  aaaaaa  "+sql);

			String Query="update SAL_DETAILS set SAL_STATUS='AutoInst' where EMPNO="+TB.getEMPNO()+" and SAL_MONTH='"+TB.getTRNDT().substring(3,11)+"' ";
			
			if(chk.first()){
				result = "present";
			}
			else{
				System.out.println(sql);
				System.out.println(Query);
				st1.execute(sql);
				st1.execute(Query);
				
				
				
				
				result = "true";
			}
			 System.out.println("  aaaaaa  "+Query);

			con.close();
		}
		catch(SQLException e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;		
	}

	public String addCTCDISPLAY(TranBean TB)
	{
		String result= "false";
		Connection con= ConnectionManager.getConnection();
		try
		{
			Statement st1 = con.createStatement();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			Statement st2 = con.createStatement();
			String check = "select * from CTCDISPLAY where empno = "+TB.getEMPNO()+" and TRNCD = "+TB.getTRNCD();
			String sql="INSERT INTO CTCDISPLAY VALUES("
						+TB.getEMPNO()+","
						+TB.getTRNCD()+","
						+TB.getSRNO()+","
						+TB.getINP_AMT()+","
						+TB.getADJ_AMT()+","
						+TB.getNET_AMT()+","
						+TB.getPf()+","
						+TB.getEsic()+","
						+TB.getPt()+","
						+TB.getUpdatedby()+",'"
						+TB.getUpdate()+" ' "
						+ ")"; 
			String updte = "update CTCDISPLAY set VALUETYPE = "+TB.getSRNO()+" ," +
					" VALUE = "+TB.getINP_AMT()+ "," +
					" DEPENDON = "+TB.getADJ_AMT()+"," +
					" INP_AMT = "+TB.getNET_AMT()+"," +
					" PF = "+TB.getPf()+"," +
					" PT = "+TB.getPt()+"," +
					" ESIC = "+TB.getEsic()+", updated_by="+TB.getUpdatedby()+", updated_date='"+TB.getUpdate()+"' where empno = "+TB.getEMPNO()+" and TRNCD = "+TB.getTRNCD();
      
			ResultSet chk = st.executeQuery(check);
			if(chk.next()){
				System.out.println(updte);
				
				String hist="insert into ctcdisplay_history (EMPNO,TRNCD ,VALUETYPE ,VALUE ,DEPENDON ,INP_AMT ,PF ,ESIC ,PT ) " +
						 " select EMPNO,TRNCD ,VALUETYPE ,VALUE ,DEPENDON ,INP_AMT ,PF ,ESIC ,PT " +
						 " from ctcdisplay where  empno = "+TB.getEMPNO()+" and TRNCD = "+TB.getTRNCD();
				
				
				System.out.println(hist);
				String modifiedby="update ctcdisplay_history set modifyby="+TB.getUpdatedby()+"  where empno = "+TB.getEMPNO()+" and TRNCD = "+TB.getTRNCD();
				
				st2.executeUpdate(hist);
				st2.executeUpdate(modifiedby);
				st2.executeUpdate(updte);
			
				result = "present";
			}
			else{
				System.out.println(sql);
				st1.execute(sql);
				
				result = "true";
			}
			
			con.close();
		}
		catch(SQLException e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;		
	}
	
	public  ArrayList<TranBean> getCTCDISPLAY(int empno)
	{
		ArrayList<TranBean> trlist = new ArrayList<TranBean>();
		Connection con= ConnectionManager.getConnection();
		TranBean tb;
		try
		{
			Statement st = con.createStatement();
			ResultSet rs= null;
			rs = st.executeQuery("SELECT * FROM CTCDISPLAY  WHERE empno = "+empno);
			while(rs.next())
			{
				tb = new TranBean();
				tb.setEMPNO(rs.getInt("EMPNO"));
				tb.setTRNCD(rs.getInt("TRNCD"));
				tb.setSRNO(rs.getInt("VALUETYPE"));
				tb.setINP_AMT(rs.getFloat("VALUE"));
				tb.setADJ_AMT(rs.getFloat("DEPENDON"));
				tb.setNET_AMT(rs.getFloat("INP_AMT"));
				tb.setPf(rs.getFloat("PF"));
				tb.setPt(rs.getFloat("PT"));
				tb.setEsic(rs.getFloat("ESIC"));
				tb.setUpdatedby(rs.getInt("updated_by"));
				tb.setUpdate(rs.getString("updated_date"));
				trlist.add(tb);
			}
			con.close();
		}
		catch(Exception e)
		{
			try
			{
				con.close();
				e.printStackTrace();
			} 
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return trlist;
	}

	public boolean checkAndInsertTran(TranBean TB)
	{
		boolean result=false;
		Connection con= ConnectionManager.getConnection();
		try
		{
			Statement st1 = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs = st2.executeQuery("SELECT * FROM PAYTRAN WHERE EMPNO="+TB.getEMPNO()+" AND TRNCD="+TB.getTRNCD());
			if(rs.next())
			{	
				st1.executeUpdate("UPDATE PAYTRAN SET SRNO="+TB.getSRNO()+", INP_AMT="+TB.getINP_AMT()+"," +
								" CAL_AMT="+TB.getCAL_AMT()+",ADJ_AMT="+TB.getADJ_AMT()+",ARR_AMT="+TB.getARR_AMT()+"," +
								" NET_AMT="+TB.getNET_AMT()+",CF_SW='"+TB.getCF_SW()+"',TRNDT=GETDATE(),UPDDT=GETDATE(),USRCODE='"+TB.getUSRCODE()+"'" +
								" WHERE EMPNO="+TB.getEMPNO()+" AND TRNCD="+TB.getTRNCD());
			}
			else
			{
				String sql="INSERT INTO PAYTRAN VALUES("
						+"GETDATE(),"
						+TB.getEMPNO()+","
						+TB.getTRNCD()+","
						+TB.getSRNO()+","
						+TB.getINP_AMT()+","
						+TB.getCAL_AMT()+","
						+TB.getADJ_AMT()+","
						+TB.getARR_AMT()+","
						+TB.getNET_AMT()+","
						+"'"+TB.getCF_SW()+"',"
						+"'"+TB.getUSRCODE()+"',"
						+"GETDATE()"
						+")"; 
			st1.execute(sql);
			}	
			result = true;
			con.close();
		}
		catch(SQLException e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;		
	}
	// i want some changes in method..... 
	
	public int addNewTransaction(TranBean TB)
	{
		int result=0;
		Connection con= ConnectionManager.getConnection();
		int srnomax=0;
		try
		{
			Statement st2 = con.createStatement();
			ResultSet rs1=st2.executeQuery("select * from PAYTRAN where EMPNO='"+TB.getEMPNO()+"' and TRNCD='"+TB.getTRNCD()+"'");
			if(rs1.next())
			{
				result=2;	
			}
			else
			{
				Statement st1 = con.createStatement();
				String srno ="select max(srno) from paytran";
				ResultSet rs=st1.executeQuery(srno);
				if(rs.next())
				{
					srnomax=rs.getInt(1);
				}
				srnomax=srnomax+1;
				String sql="INSERT INTO PAYTRAN VALUES("
							+"'"+TB.getTRNDT()+"',"
							+TB.getEMPNO()+","
							+TB.getTRNCD()+","
							+srnomax+","
							+TB.getINP_AMT()+","
							+TB.getCAL_AMT()+","
							+TB.getADJ_AMT()+","
							+TB.getARR_AMT()+","
							+TB.getNET_AMT()+","
							+"'"+TB.getCF_SW()+"',"
							+"'"+TB.getUSRCODE()+"',"
							+"'"+TB.getUPDDT()+"',"
							+"'"+TB.getSTATUS()+"'"
							+")"; 
				st1.execute(sql);
				result=1;
				con.close();
			}
		}
		catch(SQLException e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;		
	}
	
	
	//--------- Method for adding transaction to YTDTRAN table----
	public boolean addToYTDTran(int empno)
	{
		boolean result=false;
		Connection con= ConnectionManager.getConnection();
		try
		{
			Statement st= con.createStatement();
			String sql="INSERT INTO YTDTRAN VALUE(SELECT * FROM PAYTRAN WHERE EMPNO ="+empno+")"; 
			st.execute(sql);
			st.executeUpdate("DELETE FROM PAYTRAN WHERE EMPNO="+empno+" AND NVL(CF_SW,0) <>'*'");
			result=true;
			con.close();
		}
		catch(SQLException e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return result;		
	}
	
	//------ Method for adding all transactions from TRAN to YTDTRAN--------
	
	public boolean addAllTranToYTDTRAN()
	{
		boolean result=false;
		Connection con= ConnectionManager.getConnection();
		try
		{
			Statement st= con.createStatement();
			String sql="INSERT INTO YTDTRAN VALUE(SELECT * FROM PAYTRAN )"; 
			st.execute(sql);
			result=true;
			con.close();
		}
		catch(SQLException e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return result;		
	}
	
	public boolean addInvestTran(TranBean tb)
	{
		boolean result=false;
		Connection con= ConnectionManager.getConnection();
		ResultSet rs=null;
		int srno = 0;
		try
		{
			Statement st= con.createStatement();
			rs=st.executeQuery("select max(SRNO) from INVTRAN where EMPNO="+tb.getEMPNO()+"");
			while(rs.next())
			{
				srno=rs.getString(1)!=null?rs.getInt(1):0;
			}
			srno = srno+1;
			String sql="INSERT INTO INVTRAN (TRNDT,EMPNO,TRNCD,INP_AMT,SRNO) VALUES ('"+tb.getTRNDT()+"',"+tb.getEMPNO()+","+tb.getTRNCD()+",'"+tb.getINP_AMT()+"',"+srno+")"; 
			st.execute(sql);
			result=true;
			con.close();
		}
		catch(SQLException e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;		
	}
	
	public boolean DeleteInvestTran(int empno,int srno)
	{
		boolean result=false;
		Connection con= ConnectionManager.getConnection();
		try
		{
			Statement st = con.createStatement();
			String sql = "delete from INVTRAN where EMPNO = "+empno+" and SRNO = "+srno+"";
			st.execute(sql);
			result=true;
			con.close();
		}
		catch(SQLException e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;		
	}
	
	public ArrayList<TranBean> getTranInfo(String Empno,String tran)
	{
		ArrayList<TranBean> trlist = new ArrayList<TranBean>();
		Connection con= ConnectionManager.getConnection();
		int empno = Integer.parseInt(Empno);
		TranBean tb;
		try
		{
			Statement st = con.createStatement();
			ResultSet rs= null;
			if(tran.equalsIgnoreCase("tran"))
			{
				rs= st.executeQuery("Select * from PAYTRAN Where EMPNO="+empno+" ORDER BY TRNCD,UPDDT DESC");
			}
			else
			{
				rs= st.executeQuery("Select * from INVTRAN Where EMPNO="+empno+" ORDER BY SRNO ASC");
			}
			while(rs.next())
			{
				tb = new TranBean();
				tb.setADJ_AMT(rs.getString("ADJ_AMT")!=null?rs.getFloat("ADJ_AMT"):0);
				tb.setARR_AMT(rs.getString("ARR_AMT")!=null?rs.getFloat("ARR_AMT"):0);
				tb.setCAL_AMT(rs.getString("CAL_AMT")!=null?rs.getFloat("CAL_AMT"):0);
				tb.setCF_SW(rs.getString("CF_SW")==null?"--":rs.getString("CF_SW"));
				tb.setEMPNO(rs.getString("EMPNO")!=null?rs.getInt("EMPNO"):0);
				tb.setINP_AMT(rs.getString("INP_AMT")!=null?rs.getFloat("INP_AMT"):0);
				tb.setNET_AMT(rs.getString("NET_AMT")!=null?rs.getFloat("NET_AMT"):0);
				tb.setSRNO(rs.getString("SRNO")!=null?rs.getInt("SRNO"):0);
				tb.setTRNCD(rs.getString("TRNCD")!=null?rs.getInt("TRNCD"):0);
				tb.setTRNDT(rs.getString("TRNDT")!=null?EmpOffHandler.dateFormat(rs.getDate("TRNDT")):"");
				tb.setUPDDT(rs.getString("UPDDT")!=null?EmpOffHandler.dateFormat(rs.getDate("UPDDT")):"");
				tb.setUSRCODE(rs.getString("USRCODE")!=null?rs.getString("USRCODE"):"");
				trlist.add(tb);
			}
			con.close();
		}
		catch(Exception e)
		{
			try
			{
				con.close();
			} 
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return trlist;
	}

	public TranBean getSingleTranInfo(int Empno,int trncd,int srno,String trndate)//empno,trncd,srno,trndate
	{
		Connection con= ConnectionManager.getConnection();//TRNDT EMPNO SRNO TRNCD
		TranBean tb = new TranBean();
		try
		{
			Statement st = con.createStatement();
			String sql="Select * from PAYTRAN Where EMPNO="+Empno+" and SRNO="+srno+" and TRNCD="+trncd+" and TRNDT <='"+trndate+"'";
			ResultSet rs= st.executeQuery(sql);
			while(rs.next())
			{
				tb.setADJ_AMT(rs.getString("ADJ_AMT")!=null?rs.getFloat("ADJ_AMT"):0);
				tb.setARR_AMT(rs.getString("ARR_AMT")!=null?rs.getFloat("ARR_AMT"):0);
				tb.setCAL_AMT(rs.getString("CAL_AMT")!=null?rs.getFloat("CAL_AMT"):0);
				tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));
				tb.setEMPNO(rs.getString("EMPNO")!=null?rs.getInt("EMPNO"):0);
				tb.setINP_AMT(rs.getString("INP_AMT")!=null?rs.getFloat("INP_AMT"):0);
				tb.setNET_AMT(rs.getString("NET_AMT")!=null?rs.getFloat("NET_AMT"):0);
				tb.setSRNO(rs.getString("SRNO")!=null?rs.getInt("SRNO"):0);
				tb.setTRNCD(rs.getString("TRNCD")!=null?rs.getInt("TRNCD"):0);
				tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("TRNDT")));
				tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
				tb.setUSRCODE(rs.getString("USRCODE")!=null?rs.getString("USRCODE"):"");
			}
			con.close();
		}
		catch(Exception e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
		}
		return tb;
	}
	
	public TranBean getPaySlipTran(int Empno,int trncd,int srno,String trndate,String frmTable)//empno,trncd,srno,trndate
	{
		
		Connection con= ConnectionManager.getConnection();//TRNDT EMPNO SRNO TRNCD
		TranBean tb = new TranBean();
		ResultSet rs;
		try
		{
			Statement st = con.createStatement();
			String paytrsql="Select * from "+frmTable+" Where EMPNO="+Empno+" and SRNO="+srno+" and TRNCD="+trncd+" and TRNDT ='"+trndate+"'";
		//	System.out.println("test query "+ paytrsql);
			
			rs = st.executeQuery(paytrsql);
			while(rs.next())
			{
				tb.setADJ_AMT(rs.getString("ADJ_AMT")!=null?rs.getFloat("ADJ_AMT"):0);
				tb.setARR_AMT(rs.getString("ARR_AMT")!=null?rs.getFloat("ARR_AMT"):0);
				tb.setCAL_AMT(rs.getString("CAL_AMT")!=null?rs.getFloat("CAL_AMT"):0);
				tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));
				tb.setEMPNO(rs.getString("EMPNO")!=null?rs.getInt("EMPNO"):0);
				tb.setINP_AMT(rs.getString("INP_AMT")!=null?rs.getFloat("INP_AMT"):0);
				tb.setNET_AMT(rs.getString("NET_AMT")!=null?rs.getFloat("NET_AMT"):0);
				tb.setSRNO(rs.getString("SRNO")!=null?rs.getInt("SRNO"):0);
				tb.setTRNCD(rs.getString("TRNCD")!=null?rs.getInt("TRNCD"):0);
				tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("TRNDT")));
				tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
				tb.setUSRCODE(rs.getString("USRCODE")!=null?rs.getString("USRCODE"):"");
			}
			con.close();
		}
		catch(Exception e)
		{
			try
			{
				con.close();
			}
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return tb;
	}
	

	public boolean UpdateTransaction(TranBean tb)
	{
		boolean result=false;
		Connection con= ConnectionManager.getConnection();
		Date dt1 = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		String dt = format.format(dt1);
		try
		{
			Statement st = con.createStatement();
			Statement st1 = con.createStatement();
			String sql1="INSERT TRANLOG SELECT * FROM PAYTRAN P WHERE P.TRNDT='"+tb.getTRNDT()+"' AND P.TRNCD="+tb.getTRNCD()+" AND P.SRNO="+tb.getSRNO()+" AND P.EMPNO="+tb.getEMPNO();
			st1.execute(sql1);
			String sql="UPDATE PAYTRAN SET "
						+"ADJ_AMT="+tb.getADJ_AMT()+", "
						+"INP_AMT="+tb.getINP_AMT()+", "
						+"NET_AMT="+tb.getNET_AMT()+", "
						+"CF_SW='"+tb.getCF_SW()+"', "
						+"UPDDT='"+dt+"' "
					    +"WHERE TRNDT='"+tb.getTRNDT()+"' AND TRNCD="+tb.getTRNCD()+" AND SRNO="+tb.getSRNO()+" AND EMPNO="+tb.getEMPNO();
			st.executeUpdate(sql);
			result=true;
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public boolean old_finalizeTran(String emplist,String dt,String user)
	{
		boolean flag = false;
		Connection con = null;
		try
		{
			con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			String sql = "INSERT INTO YTDTRAN (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT) ";
			sql = sql + "SELECT TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,TRNDT FROM PAYTRAN WHERE EMPNO IN ("+emplist+") ORDER BY EMPNO, TRNCD";
			st.executeUpdate(sql);
			st.executeUpdate("DELETE FROM PAYTRAN WHERE EMPNO IN ("+emplist+") AND CF_SW <> '*'");
			st.executeUpdate("UPDATE PAYTRAN SET TRNDT=DATEADD(MM,1,TRNDT),UPDDT=DATEADD(MM,1,TRNDT),CAL_AMT=0,NET_AMT=0,ADJ_AMT=0,ARR_AMT=0 WHERE EMPNO IN ("+emplist+")");
			Sal_DetailsHandler SDH = new Sal_DetailsHandler();
			SAL_Details salData = new SAL_Details();
			salData.setEMPNO(0);
			salData.setSAL_MONTH(dt);
			salData.setSAL_STATUS("FINALIZED");
			salData.setFINALIZED_BY(user);
			SDH.updateSalDetails(emplist, salData, con);
			flag = true;
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	public boolean finalizeTran(String emplist,String dt,int empno)
	{
		
		
		//int flagcheck =Calculate.pay_cal(dt, emplist,Integer.toString(empno) );
	
			
		boolean flag = false;
		String dt1=dt;
		float salaryPost=0.0f;
		Connection con = null;
		try
		{
			con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			System.out.println("user"+empno);
			String flag_st = "update paytran set STATUS = 'F',USRCODE='"+empno+"',UPDDT='"+ReportDAO.getSysDate()+"' where empno in ("+emplist+") and empno not in (select distinct empno from paytran where trncd=999 and net_amt<0)";
			st.executeUpdate(flag_st);
			String sql = "INSERT INTO PAYTRAN_STAGE (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,STATUS) ";
			sql = sql + "SELECT TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,STATUS FROM PAYTRAN WHERE EMPNO IN ("+emplist+") and empno not in (select distinct empno from paytran where trncd=999 and net_amt<0) ORDER BY EMPNO, TRNCD";
			st.executeUpdate(sql);
			//BY HEMANT FOR 998 in finalize
			String empnoList[]=emplist.split(",");
			ResultSet resultSet=null;
			dt="25-"+dt;
			 for (int x=0;x<empnoList.length;x++)
				         {
		
			System.out.println("select SUM(NET_AMT) as salaryPost from PAYTRAN_STAGE " +
					"where TRNDT='"+ReportDAO.EOM(dt)+"' and "+
              "trncd in (999,225,227) and empno="+empnoList[x]+" group by EMPNO order by empno");
			 resultSet=st.executeQuery("select SUM(NET_AMT) as salaryPost from PAYTRAN_STAGE " +
					"where TRNDT='"+ReportDAO.EOM(dt)+"' and "+
              "trncd in (999,225,227) and empno="+empnoList[x]+" group by EMPNO order by empno");
			 if(resultSet.next())
			 {
				 salaryPost=resultSet.getFloat("salaryPost");
			 }
			 System.out.println("INSERT INTO PAYTRAN_STAGE (TRNDT,EMPNO,TRNCD,SRNO," +
			 		"INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) values"+
			 	"('"+ReportDAO.EOM(dt)+"',"+empnoList[x]+",998,0,"+salaryPost+","+salaryPost+"" +
 		","+salaryPost+",0,"+salaryPost+",'','"+ReportDAO.EOM(dt)+"','F')");
			 st.executeUpdate("INSERT INTO PAYTRAN_STAGE (TRNDT,EMPNO,TRNCD,SRNO," +
			 		"INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) values"+
			 	"('"+ReportDAO.EOM(dt)+"',"+empnoList[x]+",998,0,"+salaryPost+","+salaryPost+"" +
 		","+salaryPost+",0,"+salaryPost+",'','"+ReportDAO.EOM(dt)+"','F')");
				         }
			//BY HEMANT FOR 998 in finalize
			 
			st.executeUpdate("DELETE FROM PAYTRAN WHERE EMPNO IN ("+emplist+") AND CF_SW <> '*' and TRNCD not in (select distinct TRNCD from CDMAST where TRNCD between 201 and 299 and PSLIPYN like 'Y') and empno not in (select distinct empno from paytran where trncd=999 and net_amt<0) ");
			String sql_temp = "INSERT INTO PAYTRAN_TEMP (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,STATUS) ";
			sql_temp = sql_temp + "SELECT TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,STATUS FROM PAYTRAN WHERE EMPNO IN ("+emplist+") and empno not in (select distinct empno from paytran where trncd=999 and net_amt<0)  ORDER BY EMPNO, TRNCD";
			st.executeUpdate(sql_temp);
			st.executeUpdate("DELETE FROM PAYTRAN WHERE EMPNO IN ("+emplist+") and empno not in (select distinct empno from paytran where trncd=999 and net_amt<0) ; update PAYTRAN_TEMP set INP_AMT=0.0,CAL_AMT=0.0,ADJ_AMT=0.0,ARR_AMT=0.0,NET_AMT=0.0 where CF_SW<> '*' and empno in ("+emplist+") and empno not in (select distinct empno from paytran where trncd=999 and net_amt<0) ");
			System.out.println("-----------------update on temp completed------------------------");
			
			Sal_DetailsHandler SDH = new Sal_DetailsHandler();
			SAL_Details salData = new SAL_Details();
			salData.setEMPNO(0);
			salData.setSAL_MONTH(dt.substring(3,11));
			salData.setSAL_STATUS("FINALIZED");
			System.out.println("status : "+salData.getSAL_STATUS());
			salData.setFINALIZED_BY(Integer.toString(empno));
			SDH.updateSalDetails(emplist, salData, con);
			insert_deduction(emplist,con);
//flag = true;
			
			System.out.println("-----------------finalise completed------------------------");
			
			String date1 = dt;
			SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
			Date dd = sdf.parse(date1);
			
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(dd);
			cal.add(Calendar.MONTH, 1);
			String [] datesplit=sdf.format(cal.getTime()).split("-");
			String next_date=datesplit[1]+"-"+datesplit[2];
			
			String paytran_check = "select * from paytran where STATUS not in ('F','N') and EMPNO IN ("+emplist+") and empno not in (select distinct empno from paytran where trncd=999 and net_amt<0) ";
			ResultSet rs = st.executeQuery(paytran_check);
			if(rs.next()==false){
				System.out.println("-----------------IMPORT CHECK------------------------");
				
				String sql1 = "select distinct empno from PAYTRAN_TEMP where EMPNO not in(select EMPNO from EMPMAST where " +
						"DATEPART(mm,DOL)=DATEPART(mm,CONVERT(datetime,  REPLACE('"+dt+"', '-', ' '), 106))and " +
						"DATEPART(YYYY,DOL)=DATEPART(YYYY,CONVERT(datetime,  REPLACE('"+dt+"', '-', ' '), 106))) and EMPNO IN ("+emplist+")  " ;
				//System.out.println(sql1);
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();
				Statement st3 = con.createStatement();
				ResultSet pay_inst = st1.executeQuery(sql1);
				while(pay_inst.next()){
					System.out.println("-----------------IMPORT into paytran------------------------");
					
					String sql_temp1 = "INSERT INTO PAYTRAN (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) ";
					//sql_temp1 = sql_temp1 + "SELECT DATEADD(MM,1,TRNDT),EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,DATEADD(MM,1,TRNDT),'P' FROM PAYTRAN_TEMP WHERE EMPNO = "+pay_inst.getInt("EMPNO")+" ORDER BY EMPNO, TRNCD";
					sql_temp1 = sql_temp1 + "SELECT  CONVERT(VARCHAR(25),DATEADD(dd,-(DAY(DATEADD(mm,1,DATEADD(MM,1,TRNDT)))),DATEADD(mm,1,DATEADD(MM,1,TRNDT))),21),EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,CONVERT(VARCHAR(25),DATEADD(dd,-(DAY(DATEADD(mm,1,DATEADD(MM,1,TRNDT)))),DATEADD(mm,1,DATEADD(MM,1,TRNDT))),21),'P' FROM PAYTRAN_TEMP WHERE EMPNO = "+pay_inst.getInt("EMPNO")+" ORDER BY EMPNO, TRNCD";
					
					st2.executeUpdate(sql_temp1);
					 SDH = new Sal_DetailsHandler();
					SDH.addSalDetails(pay_inst.getInt("EMPNO"),next_date,sdf.format(cal.getTime()), "AutoInst", con);
				}				
				/*String dateupdate = "update SALARY_MONTH SET SALDATE = '"+sdf.format(cal.getTime())+"'";
				System.out.println("-----------------IMPORT update sal month------------------------");
				
				st3.execute(dateupdate);
				*/st3.execute("delete from PAYTRAN_TEMP where EMPNO IN ("+emplist+")");
			
				System.out.println("-----------------IMPORT COMPLETED------------------------");
				
				st3.execute("update  PAYTRAN set cal_amt = 0.00, adj_amt = 0.00 , net_amt = 0.00  where trncd = 225 or trncd = 226 ");

				String empno1[]=emplist.split(",");	
				float amount=0.0f;
				float total_paid=0.0f; 
				float actual_pay=0.0f; 
				float monthly_instl=0.0f;
				ResultSet resultSet2=null;
				EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
		      //   dt="01-"+dt;
		         for (int y=0;y<empno1.length;y++)
		         {
				
		        	 total_paid =0;
						monthly_instl =0;
						actual_pay =0;	
						amount=0;
						String sqlOnPaytranStage="";
						
						//Here No need of this code.......@niket
				/*String sqlOnPaytranStage="select NET_AMT from paytran_stage  where empno="+empno1[y]+" and trncd=226 and trndt='"+ReportDAO.EOM(dt)+"' ";

				resultSet=st.executeQuery(sqlOnPaytranStage);
				if(resultSet.next())
				{
					 amount=Float.parseFloat(resultSet.getString("NET_AMT"));
				}
				 System.out.println("00 Started Amount is:-"+amount);
				 System.out.println(" Started:- Query "+sqlOnPaytranStage);*/

				//if has loan then only update....for 226
				 ResultSet loan_details11 =st.executeQuery("select SUM(monthly_install) as total_installment,count(loan_no) as total_no_loan from loan_detail where empno="+Integer.parseInt(empno1[y])+" and Active='sanction' and loan_code='226' ");
				System.out.println("---------STARTING ######### 226 ##############-------------");
				 if(loan_details11.next()){
					
					 float total_installment = loan_details11.getFloat("total_installment");
					 int no_of_loan = loan_details11.getInt("total_no_loan");
					 update_AnyLoan(Integer.parseInt(empno1[y]),226,dt1);
					
				 }
				
				 //old code..... NO NEED
				 //update_loan_details(Integer.parseInt(empno1[y]),226,amount,dt1);

				
				
				//
				String sqlOnLoan="select total_paid,actual_pay,monthly_install from loan_detail where empno="+empno1[y]+" and Active='SANCTION' and loan_code=226  ";
				ResultSet resultSet1;
				/*resultSet1=st.executeQuery(sqlOnLoan);
				if(resultSet1.next())
				{
				 total_paid = resultSet1.getFloat("total_paid");
				 actual_pay = resultSet1.getFloat("actual_pay");
				 monthly_instl=resultSet1.getFloat("monthly_install");
				if(total_paid!=actual_pay){
					st.executeUpdate("update loan_detail set total_paid="+total_paid+" + "+amount+",updateddate=GETDATE() where empno="+empno1[y]+"  and Active='SANCTION' and loan_code=226  ");
					}
				}
				resultSet2=st.executeQuery(sqlOnLoan);
					if(resultSet2.next()){
	
						total_paid=resultSet2.getFloat("total_paid");
						actual_pay=resultSet2.getFloat("actual_pay");
						monthly_instl=resultSet2.getFloat("monthly_install");
					
						if(total_paid==actual_pay){
						
					st.executeUpdate("update loan_detail set Active='NIL' ,loan_end_date= '"+ReportDAO.EOM(dt)+"' where empno="+empno1[y]+" and Active='SANCTION' and loan_code=226 " );
					String nextMonthDate=ReportDAO.EOM(dt);
				    Date tempDate=new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
					  Calendar calendar = Calendar.getInstance();
					tempDate=simpleDateFormat.parse(nextMonthDate);
					tempDate=simpleDateFormat.parse(nextMonthDate);
					  calendar.setTime(tempDate);
					  calendar.add(Calendar.MONTH,1 );
					  nextMonthDate= simpleDateFormat.format(calendar.getTime());
					 nextMonthDate=ReportDAO.EOM(nextMonthDate);				
				st.executeUpdate("update paytran set CF_SW='0' , inp_amt=0.00, net_amt=0.00, cal_amt=0.00, trndt= '"+nextMonthDate+"' , upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno="+empno1[y]+" and trncd=226    ");
					}
					
					else if( ( actual_pay - total_paid ) <  monthly_instl  )
			  			
			  		{
						Connection conn = ConnectionManager.getConnection();
						Statement st5 = conn.createStatement();
						 st5.executeUpdate("update paytran set CF_SW='0'  , inp_amt=" +( actual_pay - total_paid ) +", net_amt=" +( actual_pay - total_paid ) +", cal_amt=" +( actual_pay - total_paid ) +", upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno="+empno1[y]+" and trncd=226    ");
			  		}
				}*/
				
			
					//FOR ADVANCE SALARY
					 total_paid =0;
						monthly_instl =0;
						actual_pay =0;	
						amount=0;
				 sqlOnPaytranStage="select NET_AMT from paytran_stage  where empno="+empno1[y]+" and trncd=227 and trndt='"+ReportDAO.EOM(dt)+"' ";
				 resultSet=st.executeQuery(sqlOnPaytranStage);
				if(resultSet.next())
				{
					 amount=Float.parseFloat(resultSet.getString("NET_AMT"));
				}
				 sqlOnLoan="select total_paid,actual_pay,monthly_install from loan_detail where empno="+empno1[y]+" and Active='SANCTION' and loan_code=227  ";
				 resultSet1=st.executeQuery(sqlOnLoan);
				
				if(resultSet1.next())
				{
				 total_paid = resultSet1.getFloat("total_paid");
				 actual_pay = resultSet1.getFloat("actual_pay");
				 monthly_instl=resultSet1.getFloat("monthly_install");
				if(total_paid!=actual_pay){
					st.executeUpdate("update loan_detail set total_paid="+total_paid+" + "+amount+",updateddate=GETDATE() where empno="+empno1[y]+"  and Active='SANCTION' and loan_code=227  ");
					}
				}
				resultSet2=st.executeQuery(sqlOnLoan);
					if(resultSet2.next()){
						total_paid=resultSet2.getFloat("total_paid");
						actual_pay=resultSet2.getFloat("actual_pay");
						monthly_instl=resultSet2.getFloat("monthly_install");
					
						if(total_paid==actual_pay){
						
					st.executeUpdate("update loan_detail set Active='NIL' ,loan_end_date= '"+ReportDAO.EOM(dt)+"' where empno="+empno1[y]+" and Active='SANCTION' and loan_code=227 " );
					String nextMonthDate=ReportDAO.EOM(dt);
				    Date tempDate=new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
					  Calendar calendar = Calendar.getInstance();
					tempDate=simpleDateFormat.parse(nextMonthDate);
					tempDate=simpleDateFormat.parse(nextMonthDate);
					  calendar.setTime(tempDate);
					  calendar.add(Calendar.MONTH,1 );
					  nextMonthDate= simpleDateFormat.format(calendar.getTime());
					 nextMonthDate=ReportDAO.EOM(nextMonthDate);				
				st.executeUpdate("update paytran set CF_SW='0' , inp_amt=0.00, net_amt=0.00, cal_amt=0.00, trndt= '"+nextMonthDate+"' , upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno="+empno1[y]+" and trncd=227");
					}
					
					else if( ( actual_pay - total_paid ) <  monthly_instl  )
			  			
			  		{
						Connection conn = ConnectionManager.getConnection();
						Statement st5 = conn.createStatement();
						 st5.executeUpdate("update paytran set CF_SW='0'  , inp_amt=" +( actual_pay - total_paid ) +", net_amt=" +( actual_pay - total_paid ) +", cal_amt=" +( actual_pay - total_paid ) +", upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno="+empno1[y]+" and trncd=227");
			  		}
				}
					//FOR ADVANCE SALARY ENDS HERE
				flag = true;
			
			}
		         //for deleting from paytran of  left employees
				   String deleteEmp=" delete from paytran where empno in " +
				   		"	(SELECT empno from EMPMAST where DOL   between '"+ReportDAO.BOM(dt)+"' and '"+ReportDAO.EOM(dt)+"') and trndt > '"+ReportDAO.EOM(dt)+"' ";
				   Connection conn = ConnectionManager.getConnection();
					Statement st5 = conn.createStatement();
					System.out.println(deleteEmp);
					st5.execute(deleteEmp);
					//end here for deleting from paytran of  left employees
					
					
				   flag = true;
			
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public void insert_deduction(String emplist,Connection con){
		try
		{
			Statement st = con.createStatement();
			String sql = "select distinct EMPNO,TRNDT from PAYTRAN_STAGE where EMPNO in ("+emplist+")";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				Statement st_adv = con.createStatement();
				Statement st_mob = con.createStatement();
				Statement st_oth = con.createStatement();
				Statement st_tds = con.createStatement();
				Statement st_loan = con.createStatement();
				String sql_adv = "select * from PAYTRAN_STAGE where EMPNO = "+rs.getInt("EMPNO")+" and trncd = 225" ;
				ResultSet rs_adv = st_adv.executeQuery(sql_adv);
				if(rs_adv.next()== false){
					String insert_adv = "INSERT INTO PAYTRAN_STAGE values ('"+rs.getString("TRNDT")+"',"+rs.getInt("EMPNO")+",225,0,0,0,0,0,0,'','','"+rs.getString("TRNDT")+"','F')";
					st_adv.execute(insert_adv);
				}
				String sql_mob = "select * from PAYTRAN_STAGE where EMPNO = "+rs.getInt("EMPNO")+" and trncd = 223" ;
				ResultSet rs_mob = st_mob.executeQuery(sql_mob);
				if(rs_mob.next()== false){
					String insert_adv = "INSERT INTO PAYTRAN_STAGE values ('"+rs.getString("TRNDT")+"',"+rs.getInt("EMPNO")+",223,0,0,0,0,0,0,'','','"+rs.getString("TRNDT")+"','F')";
					st_adv.execute(insert_adv);
				}
				String sql_oth = "select * from PAYTRAN_STAGE where EMPNO = "+rs.getInt("EMPNO")+" and trncd = 212" ;
				ResultSet rs_oth = st_oth.executeQuery(sql_oth);
				if(rs_oth.next()== false){
					String insert_oth = "INSERT INTO PAYTRAN_STAGE values ('"+rs.getString("TRNDT")+"',"+rs.getInt("EMPNO")+",212,0,0,0,0,0,0,'','','"+rs.getString("TRNDT")+"','F')";
					st_adv.execute(insert_oth);
				}
				String sql_tds = "select * from PAYTRAN_STAGE where EMPNO = "+rs.getInt("EMPNO")+" and trncd = 228" ;
				ResultSet rs_tds = st_tds.executeQuery(sql_tds);
				if(rs_tds.next()== false){
					String insert_tds = "INSERT INTO PAYTRAN_STAGE values ('"+rs.getString("TRNDT")+"',"+rs.getInt("EMPNO")+",228,0,0,0,0,0,0,'','','"+rs.getString("TRNDT")+"','F')";
					st_adv.execute(insert_tds);
				}
				String sql_loan = "select * from PAYTRAN_STAGE where EMPNO = "+rs.getInt("EMPNO")+" and trncd = 226" ;
				ResultSet rs_loan = st_loan.executeQuery(sql_loan);
				if(rs_loan.next()== false){	
					String insert_loan = "INSERT INTO PAYTRAN_STAGE values ('"+rs.getString("TRNDT")+"',"+rs.getInt("EMPNO")+",226,0,0,0,0,0,0,'','','"+rs.getString("TRNDT")+"','F')";
					st_adv.execute(insert_loan);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static int nextMonthImport(String date,String next_date){
		Connection con = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			con = ConnectionManager.getConnection();
			con.setAutoCommit(false);
			Statement st = con.createStatement();
			Statement st1 = con.createStatement();
			Statement st2 = con.createStatement();
			Statement st3 = con.createStatement();
			String date1 = "25-"+date;
			Date dd = sdf.parse(date1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dd);
			cal.add(Calendar.MONTH, 1);
			String paytran_check = "select * from paytran where STATUS not in ('F','N') ";
			ResultSet rs = st.executeQuery(paytran_check);
			if(rs.next()==false){
				String sql = "select distinct empno from PAYTRAN_TEMP where EMPNO not in(select EMPNO from EMPMAST where " +
						"DATEPART(mm,DOL)=DATEPART(mm,CONVERT(datetime,  REPLACE('"+date+"', '-', ' '), 106))and " +
						"DATEPART(YYYY,DOL)=DATEPART(YYYY,CONVERT(datetime,  REPLACE('"+date+"', '-', ' '), 106)))" ;
				System.out.println(sql);
				ResultSet pay_inst = st1.executeQuery(sql);
				while(pay_inst.next()){
					String sql_temp = "INSERT INTO PAYTRAN (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) ";
					sql_temp = sql_temp + "SELECT DATEADD(MM,1,TRNDT),EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,DATEADD(MM,1,TRNDT),'P' FROM PAYTRAN_TEMP WHERE EMPNO = "+pay_inst.getInt("EMPNO")+" ORDER BY EMPNO, TRNCD";
					st2.executeUpdate(sql_temp);
					Sal_DetailsHandler SDH = new Sal_DetailsHandler();
					SDH.addSalDetails(pay_inst.getInt("EMPNO"),next_date,sdf.format(cal.getTime()), "AutoInst", con);
				}				
				String dateupdate = "update SALARY_MONTH SET SALDATE = '"+sdf.format(cal.getTime())+"'";
				st3.execute(dateupdate);
				st3.execute("delete from PAYTRAN_TEMP");
				return  -0;
			}
			else{
				return  -2;
			}
			
		}
		catch(Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally{
			try {
				con.commit();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 99;
	}
	
	public String getFinalizeStatus()
	{
		String result="";
		Connection con = null;
		try
		{
			con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM FINALIZE");			
			if(rs.next())
			{
				result = Utility.dateFormat(rs.getDate(1)) + ":" +rs.getString(2);
			}
			else
			{
				result = "Nothing:TRUE";
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public void updateFinalizeStatus(String date)
	{
		Connection con = null;
		try
		{
			con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			st.executeUpdate("DELETE FROM FINALIZE");
			st.executeUpdate("INSERT INTO FINALIZE VALUES('"+date+"','FALSE')");
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	public  ArrayList<TranBean> getTranDetail(int key)
	{
		ArrayList<TranBean> trlist = new ArrayList<TranBean>();
		Connection con= ConnectionManager.getConnection();
		TranBean tb;
		try
		{
			Statement st = con.createStatement();
			ResultSet rs= null;
			Statement st1 = con.createStatement();
			ResultSet rs1=null;
			rs1 = st1.executeQuery("SELECT EMPNO FROM EMPMAST WHERE STATUS='A' ORDER BY EMPNO");
			/*rs= st.executeQuery("Select * from PAYTRAN Where TRNCD="+key+"");*/
			while(rs1.next())
			{
				rs= st.executeQuery("Select * from PAYTRAN Where EMPNO="+rs1.getInt("EMPNO")+" AND TRNCD="+key+"");
				tb = new TranBean();
				if(rs.next())
				{
					tb.setADJ_AMT(rs.getFloat("ADJ_AMT"));
					tb.setARR_AMT(rs.getFloat("ARR_AMT"));
					tb.setCAL_AMT(rs.getFloat("CAL_AMT"));
					tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));
					tb.setEMPNO(rs.getInt("EMPNO"));
					tb.setINP_AMT(rs.getFloat("INP_AMT"));
					tb.setNET_AMT(rs.getFloat("NET_AMT"));
					tb.setSRNO(rs.getInt("SRNO"));
					tb.setTRNCD(rs.getInt("TRNCD"));
					tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("TRNDT")));
					tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
					tb.setUSRCODE(rs.getString("USRCODE"));
				}
				else
				{
					tb.setEMPNO(rs1.getInt("EMPNO"));
					tb.setTRNCD(key);
				}
				trlist.add(tb);
			}
			con.close();
		}
		catch(Exception e)
		{
			try
			{
				con.close();
				e.printStackTrace();
			} 
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return trlist;
	}
	
	
	
	
	
	public  ArrayList<TranBean> getTranDetail(int key,String date)
	{
		ArrayList<TranBean> trlist = new ArrayList<TranBean>();
		Connection con= ConnectionManager.getConnection();
		TranBean tb;
		try
		{
			Statement st = con.createStatement();
			ResultSet rs= null;
			Statement st1 = con.createStatement();
			ResultSet rs1=null;
			rs1 = st1.executeQuery("SELECT e.EMPNO FROM EMPMAST e,Emptran t  WHERE e.empno=t.empno" +
					" and t.SRNO=(select MAX(SRNO) from EMPTRAN where EMPNO=t.EMPNO and" +
					" EFFDATE<='"+ReportDAO.EOM(date)+"') AND" +
					" (( E.STATUS='A' AND E.DOJ <=  '"+ReportDAO.EOM(date)+"'  ) or (E.STATUS ='N' And  E.DOL>='"+ReportDAO.BOM(date)+"')) and t.empno in(select empno from paytran where empno=t.empno and  trncd=199 and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"')  ORDER BY EMPNO");
			/*rs= st.executeQuery("Select * from PAYTRAN Where TRNCD="+key+"");*/
		
			while(rs1.next())
			{
				rs= st.executeQuery("Select * from PAYTRAN Where EMPNO="+rs1.getInt("EMPNO")+" AND TRNCD="+key+" and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"'");
				tb = new TranBean();
				if(rs.next())
				{
					tb.setADJ_AMT(rs.getFloat("ADJ_AMT"));
					tb.setARR_AMT(rs.getFloat("ARR_AMT"));
					tb.setCAL_AMT(rs.getFloat("CAL_AMT"));
					tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));
					tb.setEMPNO(rs.getInt("EMPNO"));
					tb.setINP_AMT(rs.getFloat("INP_AMT"));
					tb.setNET_AMT(rs.getFloat("NET_AMT"));
					tb.setSRNO(rs.getInt("SRNO"));
					tb.setTRNCD(rs.getInt("TRNCD"));
					tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("TRNDT")));
					tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
					tb.setUSRCODE(rs.getString("USRCODE"));
				}
				else
				{
					tb.setEMPNO(rs1.getInt("EMPNO"));
					tb.setTRNCD(key);
				}
				trlist.add(tb);
			}
			con.close();
		}
		catch(Exception e)
		{
			try
			{
				con.close();
				e.printStackTrace();
			} 
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return trlist;
	}
	
	
	
	public  TranBean getTranByEmpno1(int empno,int trncd)
	{
		Connection con= ConnectionManager.getConnection();
		TranBean tb = null;
		try
		{
			Statement st = con.createStatement();
			ResultSet rs= null;
			String query="Select * from PAYTRAN Where EMPNO="+empno+" AND TRNCD="+trncd;
			rs= st.executeQuery(query);
			if(rs.next())
			{
				tb = new TranBean();
				tb.setADJ_AMT(rs.getFloat("ADJ_AMT"));
				tb.setARR_AMT(rs.getFloat("ARR_AMT"));
				tb.setCAL_AMT(rs.getFloat("CAL_AMT"));
				tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));
				//tb.setEMPNO(rs.getInt("EMPNO"));
				tb.setEMPNO(empno);
				tb.setINP_AMT(rs.getFloat("INP_AMT"));
				tb.setNET_AMT(rs.getFloat("NET_AMT"));
				tb.setSRNO(rs.getInt("SRNO"));
				//tb.setTRNCD(rs.getInt("TRNCD"));
				tb.setTRNCD(trncd);
				tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("TRNDT")));
				tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
				tb.setUSRCODE(rs.getString("USRCODE"));
			}
			else
			{
				tb = new TranBean();
				tb.setEMPNO(empno);
				tb.setTRNCD(trncd);
			}
			con.close();
		}
		catch(Exception e)
		{
			try
			{
				con.close();
			} 
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return tb;
	}
	
	
	
	
	public  ArrayList<TranBean> getTranByEmpno(int empno,int trncd)
	{
		ArrayList<TranBean> alist=new ArrayList<>();
		Connection con= ConnectionManager.getConnection();
		TranBean tb;
		try
		{
			Statement st = con.createStatement();
			ResultSet rs= null;
			String query="Select * from PAYTRAN Where EMPNO="+empno+" AND TRNCD="+trncd+" ORDER BY TRNCD";
			rs= st.executeQuery(query);
			while(rs.next())
			{
				tb = new TranBean();
				tb.setADJ_AMT(rs.getFloat("ADJ_AMT"));
				tb.setARR_AMT(rs.getFloat("ARR_AMT"));
				tb.setCAL_AMT(rs.getFloat("CAL_AMT"));
				tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));
				tb.setEMPNO(rs.getInt("EMPNO"));
				tb.setINP_AMT(rs.getFloat("INP_AMT"));
				tb.setNET_AMT(rs.getFloat("NET_AMT"));
				tb.setSRNO(rs.getInt("SRNO"));
				tb.setTRNCD(rs.getInt("TRNCD"));
				tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("TRNDT")));
				tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
				tb.setUSRCODE(rs.getString("USRCODE"));
				alist.add(tb);
			}
			con.close();
		}
		catch(Exception e)
		{
			try
			{
				con.close();
			} 
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return alist;
	}
	
	//To get the empbean according to emp & trncd
	public  TranBean getTranByEmpno1(int empno,int trncd,String date)
	{
		Connection con= ConnectionManager.getConnection();
		TranBean tb = null;
		try
		{
			Statement st = con.createStatement();
			ResultSet rs= null;
			String query="Select * from PAYTRAN Where EMPNO="+empno+" AND TRNCD="+trncd+ " AND TRNDT between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' ";
			rs= st.executeQuery(query);
			if(rs.next())
			{
				tb = new TranBean();
				tb.setADJ_AMT(rs.getFloat("ADJ_AMT"));
				tb.setARR_AMT(rs.getFloat("ARR_AMT"));
				tb.setCAL_AMT(rs.getFloat("CAL_AMT"));
				tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));
				//tb.setEMPNO(rs.getInt("EMPNO"));
				tb.setEMPNO(empno);
				tb.setINP_AMT(rs.getFloat("INP_AMT"));
				tb.setNET_AMT(rs.getFloat("NET_AMT"));
				tb.setSRNO(rs.getInt("SRNO"));
				//tb.setTRNCD(rs.getInt("TRNCD"));
				tb.setTRNCD(trncd);
				tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("TRNDT")));
				tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
				tb.setUSRCODE(rs.getString("USRCODE"));
			}
			else
			{
				tb = new TranBean();
				tb.setEMPNO(empno);
				tb.setTRNCD(trncd);
			}
			con.close();
		}
		catch(Exception e)
		{
			try
			{
				con.close();
			} 
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return tb;
	}

	
	public boolean updatetranAmount(TranBean tb1)
	{
		Connection con= ConnectionManager.getConnection();
		boolean result=false;
		try
		{
			Statement st = con.createStatement();
			String update="UPDATE PAYTRAN set INP_AMT="+tb1.getINP_AMT()+", UPDDT = '"+tb1.getTRNDT()+"',STATUS='A' where EMPNO="+tb1.getEMPNO()+" and TRNCD="+tb1.getTRNCD()+" ";
			st.executeUpdate(update);
			result=true;
			con.close();
		}	
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean getAvailability(int empno, int trncd)
	{
		Connection con= ConnectionManager.getConnection();
		boolean result=false;
		try
		{
			Statement st = con.createStatement();
			String sql = "SELECT * FROM PAYTRAN WHERE EMPNO="+empno+" AND TRNCD ="+trncd;
			ResultSet rs = st.executeQuery(sql);
			if(rs.next())
			{
				result = true;
			}
			con.close();
		}	
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public ArrayList<ItStandardBean> getITStandardList()
	{
		ArrayList<ItStandardBean>  Itstdlist = new ArrayList<ItStandardBean>();
		ItStandardBean itsbean;
		Connection con=null;
		try
		{
			con=ConnectionManager.getConnection();
			Statement st =con.createStatement();
			ResultSet rs = st.executeQuery("select * from ITSTANDARD");
			while(rs.next())
			{
				itsbean = new ItStandardBean();
				itsbean.setTRCD(rs.getInt("TRNCD"));
				itsbean.setAMOUNT(rs.getInt("AMOUNT"));
				Itstdlist.add(itsbean);
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return Itstdlist;
	}
	
	public int[] getITSList()
	{
		int result[] = null;
		try
		{
			Connection con = ConnectionManager.getConnection();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT DISTINCT(TRNCD) FROM ITSTANDARD ORDER BY TRNCD");
			if(rs.next())
			{
				rs.last();
				result = new int[rs.getRow()];
				rs.beforeFirst();
				int i=0;
				while(rs.next())
				{
					result[i] = rs.getInt(1);
					i++;
				}
			}
			else
			{
				result = new int[0];
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public static ArrayList<TranBean> getNagtiveSalaryList(String dt)
	{
		ArrayList<TranBean> nslist = new ArrayList<TranBean>();
		Connection con =ConnectionManager.getConnection();
		Statement st;
		ResultSet rs;
		TranBean trbn;
		try
		{
			st=con.createStatement();
			rs=st.executeQuery("select P.EMPNO,E.FNAME,E.MNAME,E.LNAME from EMPMAST E,PAYTRAN P where P.EMPNO=E.EMPNO and trncd=999 and NET_AMT<0 and TRNDT <='"+dt+"' ");
			while(rs.next())
			{
				trbn= new TranBean();
				trbn.setEMPNO(rs.getInt("EMPNO"));
				trbn.setEMPNAME(rs.getString("FNAME")+" "+rs.getString("LNAME"));
				nslist.add(trbn);
			}
			rs.close();
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return nslist;
	}
	
	public static StringBuilder getNagtiveSalaryMonth()
	{
		Connection con =ConnectionManager.getConnection();
		Statement st,st1;
		ResultSet rs,rs1 = null;
		StringBuilder result=new StringBuilder();
		try
		{
			st=con.createStatement();
			st1=con.createStatement();
			String query="select count(EMPNO)as NO_EMP ,SAL_MONTH  from SAL_DETAILS where SAL_STATUS='Negative' group by SAL_MONTH";
			rs=st.executeQuery(query);
			result.append("<ul id=\"atree\"><li style=\"cursor: pointer;\"><b>Negtive List</b>");
			while(rs.next())
			{
				result.append("<ul ><li style=\"cursor: pointer;\"><b>"+rs.getString("SAL_MONTH").toUpperCase()+"("+rs.getString("NO_EMP")+")</b>");
				String q1="select E.FNAME,E.LNAME,E.EMPNO from EMPMAST E,SAL_DETAILS S where S.EMPNO=E.EMPNO and SAL_STATUS='Negative' and SAL_MONTH='"+rs.getString("SAL_MONTH")+"'";
				rs1=st1.executeQuery(q1);
				result.append("<ul>");
				while(rs1.next())
				{
					result.append("<li style=\"cursor: pointer;\"><font color=\"#4583EC\"><a onclick=\"getTrnsation("+rs1.getInt("EMPNO")+")\">"+rs1.getString("FNAME")+" "+rs1.getString("LNAME")+"</a></font></li>");
				}
				result.append("</ul>");
				result.append("</li></ul>");
				rs1.close();
			}
			result.append("</ul>");
			rs.close();
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static StringBuilder getNoCTCEmp()
	{
		Connection con =ConnectionManager.getConnection();
		Statement st,st1;
		ResultSet rs,rs1 = null;
		StringBuilder result=new StringBuilder();
		try
		{
			st=con.createStatement();
			st1=con.createStatement();
			/*String query="select count(EMPNO)as NO_EMP ,SAL_MONTH  from SAL_DETAILS where SAL_STATUS='Negative' group by SAL_MONTH";
			System.out.println(query);
			rs=st.executeQuery(query);*/
			result.append("<ul id=\"atree\"><li style=\"cursor: pointer;\">> CTC NOT AVAILABLE<ul>");
			/*while(rs.next())
			{*/
				result.append("");//+rs.getString("SAL_MONTH").toUpperCase()+"("+rs.getString("NO_EMP")+")</b>");
				String q1="select fname,lname,empno from EMPMAST where status ='A' " +
							"and  DOL is null and" +
							" empno not in(select distinct(empno) from CTCDISPLAY)";
				
				rs1=st1.executeQuery(q1);
				result.append("<ul>");
				while(rs1.next())
				{
					result.append("<li style=\"cursor: pointer;\"><font size='2' color=\"#4583EC\"><a href='CTC.jsp'>"+rs1.getString("FNAME")+" "+rs1.getString("LNAME")+"</a></font></li>");
				}
				result.append("</ul>");
				result.append("</li></ul>");
				rs1.close();
			/*}*/
			result.append("</ul>");
			/*rs.close();*/
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	public static StringBuilder getNotFinalized()
	{
		Connection con =ConnectionManager.getConnection();
		Statement st,st1;
		ResultSet rs,rs1 = null;
		StringBuilder result=new StringBuilder();
		try
		{
			st=con.createStatement();
			st1=con.createStatement();
			String query="select count(EMPNO)as NO_EMP ,SAL_MONTH  from SAL_DETAILS where SAL_STATUS in ('PROCESSED','AutoInst') group by SAL_MONTH";
			rs=st.executeQuery(query);
			result.append("<ul id=\"atree\"><li style=\"cursor: pointer;\"><b>Not Finalized </b>");
			while(rs.next())
			{
				result.append("<ul ><li style=\"cursor: pointer;\"><b>"+rs.getString("SAL_MONTH").toUpperCase()+"("+rs.getString("NO_EMP")+")</b>");
				String q1="select E.FNAME,E.LNAME,E.EMPNO from EMPMAST E,SAL_DETAILS S where S.EMPNO=E.EMPNO and SAL_STATUS in ('PROCESSED','AutoInst') and SAL_MONTH='"+rs.getString("SAL_MONTH")+"'";
				rs1=st1.executeQuery(q1);
				result.append("<ul>");
				while(rs1.next())
				{
					result.append("<li style=\"cursor: pointer;\"><font color=\"#4583EC\"><a onclick=\"getTrnsation("+rs1.getInt("EMPNO")+")\">"+rs1.getString("FNAME")+" "+rs1.getString("LNAME")+"</a></font></li>");
				}
				result.append("</ul>");
				result.append("</li></ul>");
				rs1.close();
			}
			result.append("</ul>");
			rs.close();
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<String> getCTCTranValues(int empno,int type)
	{
		ArrayList<String> result = new ArrayList<String>();
		result.add("");
		try
		{
			Connection con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT C.TRNCD,(SELECT CASE WHEN P.INP_AMT=0 THEN P.CAL_AMT WHEN P.INP_AMT IS NULL THEN P.CAL_AMT ELSE P.INP_AMT END FROM PAYTRAN P WHERE P.TRNCD=C.TRNCD AND P.EMPNO="+empno+")" +
											" FROM CDMAST C WHERE C.CDTYPE="+type+" ORDER BY C.TRNCD");
			
			String temp="";
			while(rs.next())
			{
				temp = rs.getString(2);
				result.add(temp!=null?temp.substring(0, temp.indexOf('.')):temp);
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;    
	}
	
	
	public boolean updateEmpTran(int trncd, Float[] values, String [] values1)
	{
		Connection con = ConnectionManager.getConnection();
		boolean result = false;
		
		
		try 
		{
			Statement st = con.createStatement();
			Statement st1 = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs1;
			for(int k=0;k<values1.length;k++)
			{
				String query="SELECT E.EMPNO,P.TRNDT FROM EMPMAST E,PAYTRAN p"+
				 
				 "WHERE p.empno=E.EMPNO and E.empno="+values1[k]+" And P.TRNCD=199"+

				 "ORDER BY EMPNO	"; //  P.TRNCD=199 for get the current salary month date of employee
				
				
			//ResultSet rs = st1.executeQuery("SELECT EMPNO FROM EMPMAST WHERE STATUS='A' and empno="+values1[k]+" ORDER BY EMPNO");
				ResultSet rs = st1.executeQuery(query);	
			while(rs.next())
				
			{
				
				if(values[k]>=0)
				{
						rs1 = st2.executeQuery("SELECT * FROM PAYTRAN WHERE EMPNO="+rs.getInt("EMPNO")+" AND TRNCD="+trncd);
						if(rs1.next())
						{
							String update = "UPDATE PAYTRAN set INP_AMT="+values[k]+", UPDDT = GETDATE(),STATUS = 'A' where EMPNO = "+rs.getInt("EMPNO")+" and TRNCD="+trncd+" ";
							st.executeUpdate(update);
						}
						else
						{
							String update = "INSERT INTO PAYTRAN(TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) " +
									"VALUES ('"+rs.getString("TRNDT")+"', "+rs.getInt("EMPNO")+","+trncd+",0,"+values[k]+",0,0,0,0,0, GETDATE(),'A')";
							st.executeUpdate(update);
						}
				}
			
				
			}
			}
			result = true;
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public boolean updateEmpTranNew1(int trncd, Float[] values, ArrayList<TranBean> projEmpNolist)
	{
		Connection con = ConnectionManager.getConnection();
		boolean result = false;
		int i = 0;
		
		try 
		{
			Statement st = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs1 = null;
			for(TranBean tbn : projEmpNolist)
			{ 
				if(values[i] >= 0)
				{
						rs1 = st2.executeQuery("SELECT * FROM PAYTRAN WHERE EMPNO="+tbn.getEMPNO()+" AND TRNCD="+trncd);
						if(rs1.next())
						{
							String update = "UPDATE PAYTRAN set INP_AMT="+values[i]+", UPDDT = GETDATE(),STATUS = 'A' where EMPNO = "+tbn.getEMPNO()+" and TRNCD="+trncd+" ";
							st.executeUpdate(update);
						}
						else
						{
							String update = "INSERT INTO PAYTRAN(TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) " +
									"VALUES ((SELECT TRNDT FROM PAYTRAN WHERE EMPNO="+tbn.getEMPNO()+" AND TRNCD=199), "+tbn.getEMPNO()+","+trncd+",0,"+values[i]+",0,0,0,0,0, GETDATE(),'A')";
							st.executeUpdate(update);
						}
				}
				i++;
			}
			result = true;
			rs1.close();
			st2.close();
			st.close();
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//public boolean updateSiteEmpTranNew(Float[] mobded, Float[] lopded, ArrayList<TranBean> projEmpNolist, String updatedBy)
	public boolean updateSiteEmpTranNew(Float[] lopded, ArrayList<TranBean> projEmpNolist, String updatedBy)
	{
		Connection con = ConnectionManager.getConnection();
		boolean result = false;
		int i = 0;
		
		try 
		{
			Statement st = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs1 = null;
			for(TranBean tbn : projEmpNolist)
			{ 
				/*if(mobded[i] >= 0)
				{
						rs1 = st2.executeQuery("SELECT * FROM PAYTRAN WHERE EMPNO="+tbn.getEMPNO()+" AND TRNCD=223");
						if(rs1.next())
						{
							String update = "UPDATE PAYTRAN set INP_AMT="+mobded[i]+", UPDDT = GETDATE(), USRCODE='"+updatedBy+"' where EMPNO = "+tbn.getEMPNO()+" and TRNCD=223 ";
							st.executeUpdate(update);
						}
						else
						{
							String update = "INSERT INTO PAYTRAN(TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT) " +
									"VALUES (GETDATE(), "+tbn.getEMPNO()+",223,0,"+mobded[i]+",0,0,0,0,'"+updatedBy+"', GETDATE())";		
							st.executeUpdate(update);
						}
				}*/
				if(lopded[i] >= 0)
				{
						rs1 = st2.executeQuery("SELECT * FROM PAYTRAN WHERE EMPNO="+tbn.getEMPNO()+" AND TRNCD=301");
						if(rs1.next())
						{
							String update = "UPDATE PAYTRAN set INP_AMT="+lopded[i]+", UPDDT = GETDATE(), USRCODE='"+updatedBy+"',STATUS='A' where EMPNO = "+tbn.getEMPNO()+" and TRNCD=301 ";
							st.executeUpdate(update);
						}
						else
						{
							String update = "INSERT INTO PAYTRAN(TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) " +
									"VALUES ((SELECT TRNDT FROM PAYTRAN WHERE EMPNO="+tbn.getEMPNO()+" AND TRNCD=199), "+tbn.getEMPNO()+",301,0,"+lopded[i]+",0,0,0,0,'"+updatedBy+"', GETDATE(),'A')";
							st.executeUpdate(update);
						}
				}
				i++;
			}
			result = true;
			rs1.close();
			st2.close();
			st.close();
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getSalaryDate()
	{
		SimpleDateFormat format=new SimpleDateFormat("dd-MMM-yyyy");
		
		Connection con = ConnectionManager.getConnection();
		String dt="";
		
		try 
		{
			Statement st = con.createStatement();
			
			ResultSet rs1 = st.executeQuery("SELECT CONVERT(varchar(10),min (SALDATE),120) from SALARY_MONTH");
			
			while(rs1.next())
			{
				dt=format.format(rs1.getDate(1));
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return dt;
	}
	
	public String getSalaryDate(String empno)
	{
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy");
		EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
		Connection con = ConnectionManager.getConnection();
		String dt="";
		
		try 
		{
			Statement st = con.createStatement();
			//ResultSet rs1 = st.executeQuery("SELECT CONVERT(varchar(10),max (trndt),120) from paytran_stage where empno="+empno+"");
			ResultSet rs1 = st.executeQuery("SELECT max(trndt) from paytran_stage where empno="+empno+"");
			
			while(rs1.next())
			{
				
				dt=rs1.getString(1)==null?ReportDAO.EOM(empAttendanceHandler.getServerDate()):rs1.getString(1);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("the converted date is "+dt);
		Date date = new Date();
		 String currentdate = simpleDateFormat.format(date);  
		System.out.println("the converted date is "+currentdate);
		return currentdate;
	}
	
	public String getSalaryDateForLeft(String EMPNO)
	{
		SimpleDateFormat format=new SimpleDateFormat("dd-MMM-yyyy");
		
		Connection con = ConnectionManager.getConnection();
		String dt="";
		
		try 
		{
			Statement st = con.createStatement();
			
			ResultSet rs1 = st.executeQuery("SELECT max(trndt) from paytran_stage where empno="+EMPNO+" ");
			
			while(rs1.next())
			{
				dt=format.format(rs1.getDate(1));
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return dt;
	}
	public static void ctc_change_PayTran(int empno){
		String sql_insert = "INSERT INTO CTC_CHANGE_PAYTRAN (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,CHANGE_DATE,STATUS) " +
					 "SELECT TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,'"+ReportDAO.getSysDate()+"',STATUS " +
					 "FROM PAYTRAN where EMPNO = "+empno;
		String sql_delete = "delete from PAYTRAN where trncd in(101,102,103,104,105,108,129,138,140,141,199)and EMPNO = "+empno;
		try{
		Connection con = ConnectionManager.getConnection();
		Statement st =con.createStatement();
		st.execute(sql_insert);
		st.execute(sql_delete);
		//System.out.println(sql_insert);
		//System.out.println(sql_delete);
		st.close();
		con.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public  ArrayList getTranforSalalryStruct(int empno, int year)
	{
		//System.out.println("into getTranforSalalryStruct() of Tran Handlr");
		ArrayList list=new ArrayList();
		ArrayList list_in=new ArrayList();
		ArrayList list_ded=new ArrayList();
		//float[] array =new float[12];
		ArrayList<Float> total_in=new ArrayList<Float>(Collections.nCopies(12,0.0f));
		ArrayList<Float> total_ded=new ArrayList<Float>(Collections.nCopies(12, 0.0f));
		ArrayList<Integer> cds= new ArrayList<Integer>(Arrays.asList(101,108,103,104,105,106,126,128,107,130,131,132,133,202,201,228,205,212,225));
		Date dd=new Date();
		String yy[]=ReportDAO.getSysDate().split("-");
		int yyyy=Integer.parseInt(yy[2]);
		int mm=dd.getMonth();
		String startdate ="",enddate="",tempdate="";
		startdate="1-Apr-"+year; 
		enddate="31-Mar-"+(year+1);
		tempdate=startdate;
		float tempval=0;
		
		TranBean tb;
		
		try
		{
			
			Connection con= ConnectionManager.getConnection();
			/*Statement ss=con.createStatement();
			ResultSet rr=ss.executeQuery("select distinct(trncd) from paytran where empno="+empno+" order by trncd");
			int index=0;
			System.out.println("size of cds= "+cds.size());
			while(rr.next())
			{
				cds.add(rr.getInt(1));				
			}
			System.out.println("size of cds= "+cds.size());*/
			for(int i=0;i<cds.size();i++)
			{
				tempval=0;
				int count=0;
			ArrayList<TranBean> alist=new ArrayList<>();
			
		/*	if(mm>=4)
			{
				 startdate=yyyy+"-04-01";enddate=(yyyy+1)+"-03-31";tempdate=startdate;
			}
			else
			{
				startdate=(yyyy-1)+"-04-01";enddate=(yyyy+1)+"-03-31";tempdate=startdate;
			}
			*/
			int cd=cds.get(i);
			Statement st = con.createStatement();
			String monthForCheck="";
			ResultSet rs= null;
			//String query="SELECT trncd,trndt,net_amt FROM ytdtran  WHERE empno = "+empno+" and trncd="+cd+" and trndt BETWEEN '"+startdate+"' AND '"+enddate+"'  order by trndt";
			String query=" SELECT trncd,month_name,net_amt FROM ( select y.trncd, disc," +
					"  sum(case datepart(MM,trndt) when 04 then net_amt else  0 end) apr," +
					"  sum(case datepart(MM,trndt) when 05 then net_amt else  0 end) may," +
					"  sum(case datepart(MM,trndt) when 06 then net_amt else  0 end) jun," +
					"  sum(case datepart(MM,trndt) when 07 then net_amt else  0 end) jul," +
					"  sum(case datepart(MM,trndt) when 08 then net_amt else  0 end) aug," +
					"  sum(case datepart(MM,trndt) when 09 then net_amt else  0 end) sep," +
					"  sum(case datepart(MM,trndt) when 10 then net_amt else  0 end) oct," +
					"  sum(case datepart(MM,trndt) when 11 then net_amt else  0 end) nov," +
					"  sum(case datepart(MM,trndt) when 12 then net_amt else  0 end) dec," +
					"  sum(case datepart(MM,trndt) when 01 then net_amt else  0 end) jan," +
					"  sum(case datepart(MM,trndt) when 02 then net_amt else  0 end) feb," +
					"  sum(case datepart(MM,trndt) when 03 then net_amt else  0 end) mar" +
					" from empmast e,CDMAST c,paytran_stage y where e.empno = "+empno+" and e.empno = y.empno " +
					"and y.trncd = c.trncd and y.trndt BETWEEN '"+startdate+"' AND '"+enddate+"'  " +
					"and y.trncd="+cd+"  " +
					"group by  e.empno,fname,lname,disc,y.trncd ) p UNPIVOT " +
					"   ( net_amt FOR month_name  IN      ( apr,may,jun,jul,aug,sep,oct,nov,dec,jan,feb,mar ) )AS unpvt ";
			
			
			/*String query=" SELECT trncd,month_name,net_amt FROM ( select y.trncd, disc, " +
					"   case when datepart(MM,y.trndt)='04' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+")  end as apr ," +
					"   case when datepart(MM,y.trndt)='05' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+") end as may , " +
					"   case when datepart(MM,y.trndt)='06' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+")  end as jun  ," +
					"   case when datepart(MM,y.trndt)='07' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+") end as jul  ," +
					"   case when datepart(MM,y.trndt)='08' then net_amt  " +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+") end as Aug  ," +
					"   case when datepart(MM,y.trndt)='09' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+")  end as sep ," +
					"   case when datepart(MM,y.trndt)='10' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+")  end as oct  ," +
					"   case when datepart(MM,y.trndt)='11' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+")  end as nov  ," +
					"   case when datepart(MM,y.trndt)='12' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+")  end as dec ," +
					"   case when datepart(MM,y.trndt)='01' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+")  end as jan ," +
					"   case when datepart(MM,y.trndt)='02' then net_amt" +
					"   else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+")   end as feb  ," +
					"   case when datepart(MM,y.trndt)='03' then net_amt" +
					"     else (select INP_AMT from CTCDISPLAY where EMPNO="+empno+" and trncd="+cd+")  end as mar" +
						
					" from empmast e,CDMAST c,paytran_stage y where e.empno = "+empno+" and e.empno = y.empno " +
					"and y.trncd = c.trncd and y.trndt BETWEEN '"+startdate+"' AND '"+enddate+"'  " +
					"and y.trncd="+cd+"  " +
					" group by  e.empno,fname,lname,disc,y.trncd" +
					" ) p UNPIVOT " +
					"   ( net_amt FOR month_name  IN      ( apr,may,jun,jul,aug,sep,oct,nov,dec,jan,feb,mar ) )AS unpvt ";*/
			
			System.out.println("inside tran handler query for tds "+query);
			rs= st.executeQuery(query);
			
			
			while(rs.next())
			{
				tb = new TranBean();
				/*//tb.setADJ_AMT(rs.getFloat("ADJ_AMT"));
				tb.setARR_AMT(rs.getFloat("ARR_AMT"));
				tb.setCAL_AMT(rs.getFloat("CAL_AMT"));
				tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));*/
				//tb.setEMPNO(rs.getInt("EMPNO"));
				//tb.setINP_AMT(rs.getFloat("INP_AMT"));
				tb.setNET_AMT(rs.getFloat("NET_AMT"));
				tempval=rs.getFloat("NET_AMT");
				//tb.setSRNO(rs.getInt("SRNO"));
				tb.setTRNCD(rs.getInt("TRNCD"));
				//tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("month_name")));
				//tempdate=rs.getString("month_name");
				//monthForCheck=rs.getString("month_name").substring(5,7);
				//System.out.println("monthForCheck value++++++++"+monthForCheck);
				
				//tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
				//tb.setUSRCODE(rs.getString("USRCODE"));
				if(cd>=101 && cd<=200 || cd==522)
				{
					total_in.set(count, (total_in.get(count) + tempval));
					//System.out.println("monthForCheck count value ++++++++"+count+"   ");
					count++;
					
				}
				if(cd>=201 && cd!=522)
				{
					total_ded.set(count, (total_ded.get(count) + tempval));
					count++;
				}
				alist.add(tb);
			}
			
			
			
			/*while(rs.next())
			{
				tb = new TranBean();
				//tb.setADJ_AMT(rs.getFloat("ADJ_AMT"));
				tb.setARR_AMT(rs.getFloat("ARR_AMT"));
				tb.setCAL_AMT(rs.getFloat("CAL_AMT"));
				tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));
				//tb.setEMPNO(rs.getInt("EMPNO"));
				//tb.setINP_AMT(rs.getFloat("INP_AMT"));
				tb.setNET_AMT(rs.getFloat("NET_AMT"));
				tempval=rs.getFloat("NET_AMT");
				//tb.setSRNO(rs.getInt("SRNO"));
				tb.setTRNCD(rs.getInt("TRNCD"));
				tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("TRNDT")));
				tempdate=rs.getString("TRNDT");
				monthForCheck=rs.getString("TRNDT").substring(5,7);
				//System.out.println("monthForCheck value++++++++"+monthForCheck);
				
				//tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
				//tb.setUSRCODE(rs.getString("USRCODE"));
				if(cd>=101 && cd<=200 || cd==522)
				{
					total_in.set(count, (total_in.get(count) + tempval));
					//System.out.println("monthForCheck count value ++++++++"+count+"   ");
					count++;
					
				}
				if(cd>=201 && cd!=522)
				{
					total_ded.set(count, (total_ded.get(count) + tempval));
					count++;
				}
				alist.add(tb);
			}*/
			
			if(alist.size()<12)
			{
				//System.out.println("into else part");
				 rs= null;
				 query="SELECT trncd,trndt,net_amt FROM paytran  WHERE empno = "+empno+" and trncd="+cd+" and trndt BETWEEN '"+tempdate+"' AND '"+enddate+"'  order by trndt";
				rs= st.executeQuery(query);
				while(rs.next())
				{
					tb = new TranBean();
					//tb.setADJ_AMT(rs.getFloat("ADJ_AMT"));
					//tb.setARR_AMT(rs.getFloat("ARR_AMT"));
					//tb.setCAL_AMT(rs.getFloat("CAL_AMT"));
					////tb.setCF_SW(rs.getString("CF_SW")==null?"-":rs.getString("CF_SW"));
					//tb.setEMPNO(rs.getInt("EMPNO"));
					//tb.setINP_AMT(rs.getFloat("INP_AMT"));
					tb.setNET_AMT(rs.getFloat("NET_AMT"));
					tempval=rs.getFloat("NET_AMT");
					//tb.setSRNO(rs.getInt("SRNO"));
					tb.setTRNCD(rs.getInt("TRNCD"));
					tb.setTRNDT(EmpOffHandler.dateFormat(rs.getDate("TRNDT")));
					//tb.setUPDDT(EmpOffHandler.dateFormat(rs.getDate("UPDDT")));
					//tb.setUSRCODE(rs.getString("USRCODE"));
					
					if(cd>=101 && cd<=200 || cd==522)
					{
						total_in.set(count, (total_in.get(count) + tempval));
						count++;
					}
					if(cd>=201 && cd!=522)
					{
						total_ded.set(count, (total_ded.get(count) + tempval));
						count++;
					}
					
					
					alist.add(tb);
				}
			
				while(alist.size()<12)	
				{
				//	System.out.println("into empty else");
					tb = new TranBean();
					if(cds.get(i)==228||cds.get(i)==130||cds.get(i)==131||cds.get(i)==132||cds.get(i)==133||cds.get(i)==225)
					{
						tb.setNET_AMT(0);
						tempval=0;
					}
					else
					{
					tb.setNET_AMT(tempval);
					}
					tb.setTRNCD(cds.get(i));
					tb.setTRNDT("");
					if(cd>=101 && cd<=200 || cd==522)
					{
						total_in.set(count, (total_in.get(count) + tempval));
						count++;
					}
					if(cd>=201 && cd!=522)
					{
						total_ded.set(count, (total_ded.get(count) + tempval));
						count++;
					}
					alist.add(tb);
				}
				
			}
			if(cd<=200 ||cd==522 ){list_in.add(alist);}
			if(cd>200 && cd!=522 ){list_ded.add(alist);}
		
			}
			con.close();
			
			}
		catch(Exception e)
		{
				e.printStackTrace();
			
		}
	
	ArrayList<TranBean> alist=new ArrayList<>();
	ArrayList<TranBean> arlist=new ArrayList<>();
	ArrayList<TranBean> arllist=new ArrayList<>();
	float totalforremain=0;
	for(int c=0;c<total_in.size();c++)
	{
		TranBean b=new TranBean();
		TranBean d=new TranBean();
		TranBean e=new TranBean();
		b.setNET_AMT(total_in.get(c));
		if(b.getNET_AMT()!=0)
		{
			totalforremain=b.getNET_AMT();
			//System.out.println("inside if "+totalforremain);
		}
		else
		{
			//System.out.println("inside else"+totalforremain);
			b.setNET_AMT(totalforremain);
		}
		d.setNET_AMT(total_ded.get(c));
		e.setNET_AMT(total_in.get(c)-total_ded.get(c));
		alist.add(b);
		arlist.add(d);
		arllist.add(e);
		
	}

	list_in.add(alist);
	list_ded.add(arlist);
	list_ded.add(arllist);
	list.add(list_in);
	list.add(list_ded);
	
	return list;
	}
	
	public boolean setAdvncInPAYTRAN(String date)
	{
		boolean flag=false;
		try
		{
		Connection con= ConnectionManager.getConnection();
		Statement st=con.createStatement();
		Statement st1=con.createStatement();
		Statement st2=con.createStatement();
		System.out.println("INTO setAdvncInPAYTRAN()");
		ResultSet rs=st.executeQuery("SELECT empno,SUM(INP_AMT) from PAYTRAN_ADVANCE where trndt between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' and status='erp' group by empno");
			while(rs.next())
			{
				int empno=rs.getInt(1);
				float amt=rs.getFloat(2);
				
				st1.executeUpdate("update PAYTRAN_ADVANCE set status='in' where empno="+empno+" and trndt between '"+ReportDAO.BOM(date)+"' and  '"+ReportDAO.EOM(date)+"' and status='erp' ");
				System.out.println("update into ADVNCE");
				st2.executeUpdate("insert into  PAYTRAN values('"+date+"',"+empno+",225,0,"+amt+",0.00,0.00,0.00,0.00,0,NULL,'"+ReportDAO.getSysDate()+"','A')");
				System.out.println("insert into PAYTRAN");
				
			}
		st.close();
		con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return flag;
	}
	

	

	public static String currentSalMonth(String empno)
	{
		Connection con= ConnectionManager.getConnection();
		String month = "";
		try
		{
			Statement st = con.createStatement();
			String sql="Select max(SAL_PAID_DATE) FROM SAL_DETAILS where EMPNO ="+empno+" and SAL_STATUS ='finalized' ";
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()) {
				month = rs.getString(1);
			}
			con.close();
		}	
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return month;
	}
	
	public static ArrayList<TranBean> getReleaseSal(String date, String prno, int range,int range2)
	{
		ArrayList<TranBean> result = new ArrayList<TranBean>();
		TranBean trbn;
		try
		{
			Connection con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = null;
			ResultSet rs_sal = null;
			Statement rs_st = con.createStatement();
			String actDate[] = date.split("-");
			LookupHandler lkp = new LookupHandler();
			if(prno.equalsIgnoreCase("all")){
				rs = st.executeQuery("select * from PAYTRAN_STAGE where TRNDT between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"'  and TRNCD =999 and status='F'" +
						" order by empno, net_amt");
			}else{
				rs = st.executeQuery("select * from PAYTRAN_STAGE p, EMPTRAN e where p.empno=e.empno and" +
						" e.prj_srno="+prno+" and p.TRNDT between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and p.TRNCD =999 and p.status='F'" +
						" and e.SRNO = (SELECT MAX(E2.SRNO) FROM EMPTRAN E2 WHERE E2.EMPNO = p.EMPNO AND " +
						"E2.EFFDATE <= '"+ReportDAO.EOM(date)+"') order by p.empno,p.net_amt");
			}						
			while(rs.next())
			{
				String sql="select net_amt  from PAYTRAN_STAGE p where p.empno = "+rs.getInt("empno")+" and trncd=199 " +
						"  and p.TRNDT between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' ";
				//System.out.println("for ctc  +++ "+sql);
				rs_sal = rs_st.executeQuery(sql );
				int ctc=0;
				while(rs_sal.next())
				{
					 ctc=rs_sal.getInt("net_amt");	
				}
				
				if(ctc> range && ctc <=range2)
				
						/*if(range==10 && rs.getInt("net_amt")<=15000)*/{
					trbn = new TranBean();
					trbn.setEMPNO(rs.getInt("empno"));
					trbn.setEMPNAME(lkp.getLKP_Desc("ET", rs.getInt("empno")));
					trbn.setINP_AMT(rs.getFloat("inp_amt"));
					trbn.setCAL_AMT(rs.getFloat("cal_amt"));
					trbn.setNET_AMT(rs.getFloat("net_amt"));
					result.add(trbn);
				}/*else if(range==20 && rs.getInt("net_amt")>15000 ){
					trbn = new TranBean();
					trbn.setEMPNO(rs.getInt("empno"));
					trbn.setEMPNAME(lkp.getLKP_Desc("ET", rs.getInt("empno")));
					trbn.setINP_AMT(rs.getFloat("inp_amt"));
					trbn.setCAL_AMT(rs.getFloat("cal_amt"));
					trbn.setNET_AMT(rs.getFloat("net_amt"));
					result.add(trbn);
				}*//*else if(range==30 && rs.getInt("net_amt")>20000 && rs.getInt("net_amt")<=30000){
					trbn = new TranBean();
					trbn.setEMPNO(rs.getInt("empno"));
					trbn.setEMPNAME(lkp.getLKP_Desc("ET", rs.getInt("empno")));
					trbn.setINP_AMT(rs.getFloat("inp_amt"));
					trbn.setCAL_AMT(rs.getFloat("cal_amt"));
					trbn.setNET_AMT(rs.getFloat("net_amt"));
					result.add(trbn);
				}else if(range==40 && rs.getInt("net_amt")>30000 && rs.getInt("net_amt")<=40000){
					trbn = new TranBean();
					trbn.setEMPNO(rs.getInt("empno"));
					trbn.setEMPNAME(lkp.getLKP_Desc("ET", rs.getInt("empno")));
					trbn.setINP_AMT(rs.getFloat("inp_amt"));
					trbn.setCAL_AMT(rs.getFloat("cal_amt"));
					trbn.setNET_AMT(rs.getFloat("net_amt"));
					result.add(trbn);
				}else if(range==50 && rs.getInt("net_amt")>40000 && rs.getInt("net_amt")<=50000){
					trbn = new TranBean();
					trbn.setEMPNO(rs.getInt("empno"));
					trbn.setEMPNAME(lkp.getLKP_Desc("ET", rs.getInt("empno")));
					trbn.setINP_AMT(rs.getFloat("inp_amt"));
					trbn.setCAL_AMT(rs.getFloat("cal_amt"));
					trbn.setNET_AMT(rs.getFloat("net_amt"));
					result.add(trbn);
				}else if(range==51 && rs.getInt("net_amt")>50000){
					trbn = new TranBean();
					trbn.setEMPNO(rs.getInt("empno"));
					trbn.setEMPNAME(lkp.getLKP_Desc("ET", rs.getInt("empno")));
					trbn.setINP_AMT(rs.getFloat("inp_amt"));
					trbn.setCAL_AMT(rs.getFloat("cal_amt"));
					trbn.setNET_AMT(rs.getFloat("net_amt"));
					result.add(trbn);
				}*/else if(range==0 && range2==0){
					trbn = new TranBean();
					trbn.setEMPNO(rs.getInt("empno"));
					trbn.setEMPNAME(lkp.getLKP_Desc("ET", rs.getInt("empno")));
					trbn.setINP_AMT(rs.getFloat("inp_amt"));
					trbn.setCAL_AMT(rs.getFloat("cal_amt"));
					trbn.setNET_AMT(rs.getFloat("net_amt"));
					result.add(trbn);
				}
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;    
	}
	public boolean releaseSalary(String eno, String date) {
		boolean flag = false;
	/*	float amount=0.0f;
		float total_paid=0.0f; 
		float actual_pay=0.0f; 
		float monthly_instl=0.0f;
		EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();
		ResultSet resultSet2=null;*/
		float salaryPost=0.0f;
		try
		{
			Connection con = ConnectionManager.getConnection();
			//Connection conn = ConnectionManager.getConnection();
			con.setAutoCommit(false);
			Statement st = con.createStatement();
			//String actDate[] = date.split("-");
			//st.executeUpdate("delete from PAYTRAN_STAGE where INP_AMT= 0 and CAL_AMT= 0 and NET_AMT = 0 and CF_SW <> '*' and empno in("+eno+") and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' ");

			//FOR 998 BY HEMANT STARTS HERE
			String empno[]=eno.split(",");
			/* for (int x=0;x<empno.length;x++)
	         {
			ResultSet resultSet=st.executeQuery("select SUM(NET_AMT) as salaryPost from PAYTRAN_STAGE " +
					"where TRNDT='"+ReportDAO.EOM(date)+"' and "+
              "trncd in (999,225,227) and empno="+empno[x]+" group by EMPNO order by empno");
			 if(resultSet.next())
			 {
				 salaryPost=resultSet.getFloat("salaryPost");
			 }
			 st.executeUpdate("INSERT INTO PAYTRAN_STAGE (TRNDT,EMPNO,TRNCD,SRNO," +
			 		"INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) values"+
			 	"('"+ReportDAO.EOM(date)+"',"+empno[x]+",998,0,"+salaryPost+","+salaryPost+"" +
 		","+salaryPost+",0,"+salaryPost+",'','"+ReportDAO.EOM(date)+"','F')");
	         }*/
			
			//FOR 998 BY HEMANT ENDS HERE 
			
			flag = st.execute("Update PAYTRAN_STAGE set status='R' where trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and empno in("+eno+")");
			//System.out.println("Update PAYTRAN_STAGE set status='R' where trndt='25-"+actDate[1]+"-"+actDate[2]+"' and empno in("+eno+")");
			String sql_temp = "INSERT INTO YTDTRAN (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) ";
			sql_temp = sql_temp + "SELECT TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,TRNDT,'F' FROM PAYTRAN_STAGE WHERE EMPNO IN ("+eno+") and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' ORDER BY EMPNO, TRNCD";
			System.out.println(sql_temp);
			
			st.execute(sql_temp);
			
	        
			
			
		/*	String sqlOnPaytranStage="select CAL_AMT,empno from paytran_stage  where empno in ("+eno+") and trncd=226 and trndt='"+ReportDAO.EOM(date)+"' ";
			ResultSet resultSet=st.executeQuery(sqlOnPaytranStage);
			while(resultSet.next())
			{
				total_paid =0;
				monthly_instl =0;
				actual_pay =0;		
				 amount=Float.parseFloat(resultSet.getString("CAL_AMT"));
				 String sqlOnLoan="select total_paid,actual_pay,monthly_install from loan_detail where empno="+resultSet.getString("empno")+" and Active='SANCTION'  ";
					ResultSet resultSet1=st.executeQuery(sqlOnLoan);
					
					if(resultSet1.next())
					{
					 total_paid = resultSet1.getFloat("total_paid");
					 actual_pay = resultSet1.getFloat("actual_pay");
					 monthly_instl = resultSet1.getFloat("monthly_install");
					if(total_paid!=actual_pay){
						st.executeUpdate("update loan_detail set total_paid="+total_paid+" + "+amount+",updateddate=GETDATE() where empno="+resultSet.getString("empno")+"  and Active='SANCTION'  ");
						}
					}
					resultSet2=st.executeQuery(sqlOnLoan);
						if(resultSet2.next()){
							total_paid=resultSet2.getFloat("total_paid");
							actual_pay=resultSet2.getFloat("actual_pay");
						if(total_paid==actual_pay){
						st.executeUpdate("update loan_detail set Active='NIL' ,loan_end_date= '"+ReportDAO.EOM(date)+"' where empno="+resultSet.getString("empno")+" and Active='SANCTION' " );
						String nextMonthDate=ReportDAO.EOM(date);
					  	st.executeUpdate("update paytran set CF_SW='0'  , upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno="+resultSet.getString("empno")+" and trncd=226    ");
					  
					  	}
						else if( ( actual_pay - total_paid ) <  monthly_instl  )
				  			
				  		{
							st.executeUpdate("update paytran set CF_SW='0'  , inp_amt=" +( actual_pay - total_paid ) +" upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno="+resultSet.getString("empno")+" and trncd=226    ");
				  		}
				}
				 			 
			}*/
			
			

			/*String empno1[]=eno.split(",");	
	      
	         
	         for (int y=0;y<empno1.length;y++)
	         {
			
	        	 total_paid =0;
					monthly_instl =0;
					actual_pay =0;	
					amount=0;
			String sqlOnPaytranStage="select CAL_AMT from paytran_stage  where empno="+empno1[y]+" and trncd=226 and trndt='"+ReportDAO.EOM(date)+"' ";
			ResultSet resultSet=st.executeQuery(sqlOnPaytranStage);
			if(resultSet.next())
			{
				 amount=Float.parseFloat(resultSet.getString("CAL_AMT"));
			}
			String sqlOnLoan="select total_paid,actual_pay,monthly_install from loan_detail where empno="+empno1[y]+" and Active='SANCTION'  ";
			ResultSet resultSet1=st.executeQuery(sqlOnLoan);
			
			if(resultSet1.next())
			{
			 total_paid = resultSet1.getFloat("total_paid");
			 actual_pay = resultSet1.getFloat("actual_pay");
			 monthly_instl=resultSet1.getFloat("monthly_install");
			if(total_paid!=actual_pay){
				st.executeUpdate("update loan_detail set total_paid="+total_paid+" + "+amount+",updateddate=GETDATE() where empno="+empno1[y]+"  and Active='SANCTION'  ");
				}
			}
			resultSet2=st.executeQuery(sqlOnLoan);
				if(resultSet2.next()){
					total_paid=resultSet2.getFloat("total_paid");
					actual_pay=resultSet2.getFloat("actual_pay");
					monthly_instl=resultSet2.getFloat("monthly_install");
				if(total_paid==actual_pay){
				st.executeUpdate("update loan_detail set Active='NIL' ,loan_end_date= '"+ReportDAO.EOM(date)+"' where empno="+empno1[y]+" and Active='SANCTION' " );
				String nextMonthDate=ReportDAO.EOM(date);
			    Date tempDate=new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				  Calendar calendar = Calendar.getInstance();
				tempDate=simpleDateFormat.parse(nextMonthDate);
				tempDate=simpleDateFormat.parse(nextMonthDate);
				  calendar.setTime(tempDate);
				  calendar.add(Calendar.MONTH,1 );
				  nextMonthDate= simpleDateFormat.format(calendar.getTime());
				 nextMonthDate=ReportDAO.EOM(nextMonthDate);				
			st.executeUpdate("update paytran set CF_SW='0' , inp_amt=0.00, net_amt=0.00, cal_amt=0.00, trndt= '"+nextMonthDate+"' , upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno="+empno1[y]+" and trncd=226    ");
				}
				
				else if( ( actual_pay - total_paid ) <  monthly_instl  )
		  			
		  		{
					Statement st5 = conn.createStatement();
					 st5.executeUpdate("update paytran set CF_SW='0'  , inp_amt=" +( actual_pay - total_paid ) +", net_amt=" +( actual_pay - total_paid ) +", cal_amt=" +( actual_pay - total_paid ) +", upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno="+empno1[y]+" and trncd=226    ");
		  		}
			}
			
	         }*/
					
			int batchid=1;
			Statement st3=con.createStatement();
			ResultSet rs=st3.executeQuery("select isnull(max(BATCHNO),0)+1 from RELEASE_BATCH where trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' ");
			if(rs.next())
			{
				batchid=rs.getInt(1);
			}
		
			
         Statement st2=con.createStatement();
         
         for (int x=0;x<empno.length;x++)
         {
         st2.execute("insert into RELEASE_BATCH (empno,TRNDT,UPDDT,USRCODE,BATCHNO)"+
         			  " values("+empno[x]+",'"+ReportDAO.EOM(date)+"',GETDATE(),0,"+batchid+")");
         }
  			flag=true;
			
			con.commit();
			con.close();
		
	}
		catch(Exception e)
		{
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}
	public static ArrayList<TranBean> getSalChange(String empno,String date)
	{
		ArrayList<TranBean> result = new ArrayList<TranBean>();
		TranBean trbn;
		try
		{
			Connection con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			String actDate[] = date.split("-");
			//ResultSet rs = st.executeQuery("select * from PAYTRAN_STAGE where TRNDT ='25-"+actDate[1]+"-"+actDate[2]+"' and TRNCD not in (127,135) and empno ="+empno+" order by trncd");
			ResultSet rs = st.executeQuery("select ps.* from PAYTRAN_STAGE ps left outer join CDMAST cm on ps.TRNCD = cm.TRNCD  where ps.TRNDT  between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and cm.PSLIPYN ='Y' and ps.TRNCD not in (127,135) and ps.empno ="+empno+" order by ps.trncd");
			
			while(rs.next())
			{
				trbn = new TranBean();
				trbn.setTRNDT(rs.getString("TRNDT"));
				trbn.setTRNCD(rs.getInt("trncd"));
				trbn.setINP_AMT(rs.getFloat("inp_amt"));
				trbn.setCAL_AMT(rs.getFloat("cal_amt"));
				trbn.setADJ_AMT(rs.getFloat("ADJ_AMT"));
				trbn.setNET_AMT(rs.getFloat("net_amt"));		
				result.add(trbn);
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;    
	}
	public boolean updateSalChange(String empno, String date, ArrayList<TranBean> updated, float ded, float net)
	{
		boolean flag = false;
		try
		{
			Connection con = ConnectionManager.getConnection();
			ResultSet resultSet=null;
			Statement st = con.createStatement();
			String actDate[] = date.split("-");
			float salaryPost=0.0f;
			//System.out.println("insert into PAYTRAN_STAGE_HISTORY (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,CHANGE_DATE) (select TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,'"+ReportDAO.getSysDate()+"' from PAYTRAN_STAGE where TRNDT ='25-"+actDate[1]+"-"+actDate[2]+"' and empno ="+empno+")");
			st.execute("insert into PAYTRAN_STAGE_HISTORY (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,CHANGE_DATE) (select TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,'"+ReportDAO.getSysDate()+"' from PAYTRAN_STAGE where TRNDT  between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and empno ="+empno+")");
			for(TranBean tbn : updated){
				//System.out.println("update PAYTRAN_STAGE set INP_amt="+tbn.getINP_AMT()+",cal_amt="+tbn.getCAL_AMT()+",net_amt="+tbn.getNET_AMT()+" where trncd="+tbn.getTRNCD()+" and TRNDT ='25-"+actDate[1]+"-"+actDate[2]+"' and empno ="+empno);
				st.executeUpdate("update PAYTRAN_STAGE set INP_amt="+tbn.getINP_AMT()+",cal_amt="+tbn.getCAL_AMT()+",net_amt="+tbn.getNET_AMT()+" where trncd="+tbn.getTRNCD()+" and TRNDT  between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and empno ="+empno);
			}
			//System.out.println("update PAYTRAN_STAGE set cal_amt="+ded+",adj_amt="+net+",net_amt="+net+" where trncd=999 and TRNDT ='25-"+actDate[1]+"-"+actDate[2]+"' and empno ="+empno);
			st.executeUpdate("update PAYTRAN_STAGE set cal_amt="+ded+",adj_amt="+net+",net_amt="+net+" where trncd=999 and TRNDT between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and empno ="+empno);
			
///// akshay- for adding amt into 998 
			
			 resultSet=st.executeQuery("select SUM(NET_AMT) as salaryPost from PAYTRAN_STAGE " +
					"where TRNDT='"+ReportDAO.EOM(date)+"' and "+
              "trncd in (999,225,227) and empno ="+empno);
			 System.out.println( "select SUM(NET_AMT) as salaryPost from PAYTRAN_STAGE " +
					"where TRNDT='"+ReportDAO.EOM(date)+"' and "+
              "trncd in (999,225,227) and empno ="+empno);
			 if(resultSet.next())
			 {
				 salaryPost=resultSet.getFloat("salaryPost");
			 }
				st.executeUpdate("update PAYTRAN_STAGE set inp_amt ="+salaryPost+",cal_amt="+salaryPost+",adj_amt="+salaryPost+",net_amt="+salaryPost+" where trncd=998 and TRNDT between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and empno ="+empno);
				
				
				System.out.println(salaryPost);
				System.out.println("update PAYTRAN_STAGE set inp_amt ="+salaryPost+",cal_amt="+salaryPost+",adj_amt="+salaryPost+",net_amt="+salaryPost+" where trncd=998 and TRNDT between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"' and empno ="+empno);
//// @@kshay			
			flag=true;
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;    
	}
	
	
	public boolean updateEmpTran(int trncd, Float[] values)
	{
		Connection con = ConnectionManager.getConnection();
		boolean result = false;
		int i = 0;
		
		try 
		{
			Statement st = con.createStatement();
			Statement st1 = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs1;
			ResultSet rs = st1.executeQuery("SELECT EMPNO FROM EMPMAST WHERE STATUS='A' ORDER BY EMPNO");
			while(rs.next())
			{
				if(values[i]>=0)
				{
						rs1 = st2.executeQuery("SELECT * FROM PAYTRAN WHERE EMPNO="+rs.getInt("EMPNO")+" AND TRNCD="+trncd);
						if(rs1.next())
						{
							String update = "UPDATE PAYTRAN set INP_AMT="+values[i]+", UPDDT = GETDATE(),STATUS = 'A' where EMPNO = "+rs.getInt("EMPNO")+" and TRNCD="+trncd+" ";
							st.executeUpdate(update);
						}
						else
						{
							String update = "INSERT INTO PAYTRAN(TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) " +
									"VALUES ((SELECT TRNDT FROM PAYTRAN WHERE EMPNO="+rs.getInt("EMPNO")+" AND TRNCD=199), "+rs.getInt("EMPNO")+","+trncd+",0,"+values[i]+",0,0,0,0,0, GETDATE(),'A')";
							st.executeUpdate(update);
						}
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
	
	public boolean updateEmpTran(int trncd, Float[] values,String date)
	{
		
		System.out.println("inside updateEmpTran");
		Connection con = ConnectionManager.getConnection();
		boolean result = false;
		int i = 0;
		
		try 
		{
			Statement st = con.createStatement();
			Statement st1 = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs1;
			
			String query = "SELECT e.EMPNO FROM EMPMAST e,Emptran t  WHERE e.empno=t.empno" +
					" and t.SRNO=(select MAX(SRNO) from EMPTRAN where EMPNO=t.EMPNO and" +
					" EFFDATE<='"+ReportDAO.EOM(date)+"') AND" +
							" (( E.STATUS='A' AND E.DOJ <=  '"+ReportDAO.EOM(date)+"'  ) or (E.STATUS ='N' And  E.DOL>='"+ReportDAO.BOM(date)+"')) and t.empno in(select empno from paytran where empno=t.empno and  trncd=199 and trndt between '"+ReportDAO.BOM(date)+"' and '"+ReportDAO.EOM(date)+"')  ORDER BY EMPNO" ;
			
			//ResultSet rs = st1.executeQuery("SELECT EMPNO FROM EMPMAST WHERE STATUS='A' ORDER BY EMPNO");
			ResultSet rs = st1.executeQuery(query);
		
			while(rs.next())
			{
				if(values[i]>=0)
				{
						rs1 = st2.executeQuery("SELECT * FROM PAYTRAN WHERE EMPNO="+rs.getInt("EMPNO")+" AND TRNCD="+trncd);
						System.out.println("inside while in updateEmpTran");
						if(rs1.next())
						{
							String update = "UPDATE PAYTRAN set INP_AMT="+values[i]+", UPDDT = GETDATE(),STATUS = 'A' where EMPNO = "+rs.getInt("EMPNO")+" and TRNCD="+trncd+" ";
							System.out.println("inside while in updateEmpTran in first" +update);
							st.executeUpdate(update);
						}
						else
						{
							String update = "INSERT INTO PAYTRAN(TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) " +
									"VALUES ( '"+ReportDAO.EOM(date)+"', "+rs.getInt("EMPNO")+","+trncd+",0,"+values[i]+",0,0,0,0,0, GETDATE(),'A')";
							System.out.println("inside while in updateEmpTran in second" +update);
							st.executeUpdate(update);
						}
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

	public boolean updateEmpTran_mob(int trncd, Float[] values, String [] values1)
	{
		Connection con = ConnectionManager.getConnection();
		boolean result = false;
		int i = 0;
		
		try 
		{
			Statement st = con.createStatement();
			Statement st1 = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs1;
			for(int k=0;k<values1.length;k++)
			{
			ResultSet rs = st1.executeQuery("SELECT EMPNO FROM EMPMAST WHERE STATUS='A' and empno=(select empno from empmast where empcode="+values1[k]+") ORDER BY EMPNO");
			while(rs.next())
				
			{
				
				if(values[k]>=0)
				{
						rs1 = st2.executeQuery("SELECT * FROM PAYTRAN WHERE EMPNO="+rs.getInt("EMPNO")+" AND TRNCD="+trncd);
						if(rs1.next())
						{
							String update = "UPDATE PAYTRAN set INP_AMT="+values[k]+", UPDDT = GETDATE(),STATUS = 'P' where EMPNO = "+rs.getInt("EMPNO")+" and TRNCD="+trncd+" ";
							st.executeUpdate(update);
						}
						else
						{
							String update = "INSERT INTO PAYTRAN(TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) " +
									"VALUES (( SELECT TRNDT FROM PAYTRAN WHERE EMPNO="+rs.getInt("EMPNO")+" AND TRNCD=199), "+rs.getInt("EMPNO")+","+trncd+",0,"+values[k]+",0,0,0,0,0, GETDATE(),'P')";
							st.executeUpdate(update);
						}
				}
			
				
			}
			}
			result = true;
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}


public static int LastSalaryFortdsDisplay(int emp, int code)
{

	//System.out.println("emp no         qq "+emp+"   badshah code "+code);
	int value=0;
	
	Connection con = ConnectionManager.getConnection();
	/*if(code==0)
	{
		code=999;
	}*/
	
	try 
	{
		Statement st = con.createStatement();
		
		String query="select sum(net_amt) from PAYTRAN_STAGE where EMPNO="+emp+" and trncd="+code+" and TRNDT=(select MAX(trndt) from PAYTRAN_STAGE where EMPNO="+emp+"  )";
		//System.out.println("query for LastSalaryFortdsDisplay "+query);
		ResultSet rs1 = st.executeQuery(query);
		
		while(rs1.next())
		{
			value= rs1.getInt(1);
		}
		
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return value;

	
	
	
	
	
	
}




public boolean updateEmpTran_MedINSU(int trncd, Float[] values, String [] values1)
{
	Connection con = ConnectionManager.getConnection();
	boolean result = false;
	int i = 0;
	
	try 
	{
		Statement st = con.createStatement();
		Statement st1 = con.createStatement();
		Statement st2 = con.createStatement();
		ResultSet rs1;
		for(int k=0;k<values1.length;k++)
		{
		ResultSet rs = st1.executeQuery("SELECT EMPNO FROM EMPMAST WHERE STATUS='A' and empno=(select empno from empmast where empcode="+values1[k]+") ORDER BY EMPNO");
		while(rs.next())
			
		{
			
			if(values[k]>=0)
			{
					rs1 = st2.executeQuery("SELECT * FROM PAYTRAN WHERE EMPNO="+rs.getInt("EMPNO")+" AND TRNCD="+trncd);
					if(rs1.next())
					{
						String update = "UPDATE PAYTRAN set INP_AMT="+values[k]+", UPDDT = GETDATE(),STATUS = 'P',CF_SW='*' where EMPNO = "+rs.getInt("EMPNO")+" and TRNCD="+trncd+" ";
						st.executeUpdate(update);
					}
					else
					{
						String update = "INSERT INTO PAYTRAN(TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,UPDDT,STATUS) " +
								"VALUES (( SELECT TRNDT FROM PAYTRAN WHERE EMPNO="+rs.getInt("EMPNO")+" AND TRNCD=199), "+rs.getInt("EMPNO")+","+trncd+",0,"+values[k]+",0,0,0,0,'*', GETDATE(),'P')";
						st.executeUpdate(update);
					}
			}
		
			
		}
		}
		result = true;
		con.close();
	} 
	catch (SQLException e) {
		e.printStackTrace();
	}
	return result;
}
public boolean updateLoanInstallment(String empno, String[] loan_no,int[] loan_amt,int[] pri_amt,int[] intr_amt,String uid) 
{
	
	Connection con = ConnectionManager.getConnection();
	
	boolean flag	=false;
	String TRNDT	=	ReportDAO.getServerDate();
	String BOM		= ReportDAO.BOM(TRNDT);
	String EOM		= ReportDAO.EOM(TRNDT);
	String Sal_Date="";
	int loanamt=0;
	int pri=0;
	int intr=0;
	int i=0;
	int cnt=0;
	int total_amt=0;
	String SQL = "";
	try{
		Statement st = con.createStatement();	
		Statement st1 = con.createStatement();	
		ResultSet PAY_TRNDT =st1.executeQuery("select max(TRNDT)as trndt,LEFT(datename(m,max(TRNDT)),3)+'-'+cast(datepart(yyyy,max(TRNDT)) as varchar) as SAL_MonthYear from paytran where empno="+empno+" and trncd=101");
		if(PAY_TRNDT.next()){
			BOM = PAY_TRNDT.getString("trndt");EOM = PAY_TRNDT.getString("trndt");
			Sal_Date	= PAY_TRNDT.getString("SAL_MonthYear")	;
		}
			for(String loan_noo:loan_no)
				{
					loanamt	=	(int)loan_amt[i];
					pri	=	(int)pri_amt[i];
					intr	=	(int)intr_amt[i];
					System.out.println("loanno=="+loan_noo+"...installment=="+loanamt);
					 SQL = ""
						+ "if exists(select * from loan_history where empno="+empno+" and loan_no="+loan_noo+" and trndt ='"+EOM+"' ) "
						+ "update loan_history set installment_amt="+loanamt+",principal="+pri+",intrest="+intr+",updated_by="+uid+",updated_date='"+TRNDT+"' where empno="+empno+" and loan_no="+loan_noo+" and trndt ='"+EOM+"' "
						+ "else "
						+ "insert into loan_history (trndt,empno,loan_no,installment_amt,principal,intrest,updated_by,updated_date,status) values "
						+ "('"+EOM+"',"+empno+","+loan_noo+","+loanamt+","+pri+","+intr+","+uid+",'"+TRNDT+"','P') ";
					System.out.println("iiiii=="+i+"--"+SQL);
					i++;
					st.executeUpdate(SQL);
					cnt++;
					total_amt+=loanamt;
				}
		System.out.println("this is total installment of all loan=="+total_amt);
		if(cnt>0){
			
			SQL	=	""
					+ " if exists(select * from PAYTRAN where empno="+empno+" and "
					+ " trncd=226 and trndt ='"+EOM+"' ) "
					+ " update paytran set INP_AMT="+total_amt+",CAL_AMT="+total_amt+",NET_AMT="+total_amt+", "
					+ " usrcode="+uid+",upddt='"+TRNDT+"' where empno="+empno+" and trncd=226 "
					+ " and trndt ='"+EOM+"' "
					+ " else "
					+ " insert into PAYTRAN (trndt,empno,trncd,srno,INP_AMT,CAL_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,STATUS) "
					+ " values ('"+EOM+"',"+empno+",226,0,"+total_amt+","+total_amt+","+total_amt+",'*',"+uid+",'"+TRNDT+"','P') ";
			st.executeUpdate(SQL);
			st.executeUpdate("update SAL_DETAILS set SAL_STATUS='AutoInst' where EMPNO="+empno+" and SAL_MONTH='"+Sal_Date+"' ");
			System.out.println("SALDETAILS UPDATED..SO CALCULATE AGAIN!!!");
			flag =true;
		}
		else{
			flag =false;
		}
	}
	catch(Exception e)
	{
		System.out.println("some error is occured.. please check here!!!");
		e.printStackTrace();
	}
	return flag;
}

	// This is for (226)Multiple loan Deduction/Monthwise.... Added By $----@NIKET
	public void update_AnyLoan(int eno,int trncd,String dt)
	{
		String SQL ="";
		int LD_LoanNo=0;
		int LH_LoanNo=0;
		float LH_install_amount=0;
		float LD_paid_amt=0;
		float NEW_paid_amt=0;
		float NEW_Month_Import=0;
		String TRNDT	=	ReportDAO.getServerDate();
		String BOM=ReportDAO.BOM("01-"+dt);
		String EOM=ReportDAO.EOM("01-"+dt);
		float LD_Actual_Pay=0;
		float LD_All_Installment=0;
		String Next_Month ="";
		
		Connection con = null;
		try{
		con = ConnectionManager.getConnection();
	    Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		Statement st1 = con.createStatement();
		Statement st2 = con.createStatement();
		Statement st3 = con.createStatement();
		Statement st4 = con.createStatement();
		Statement st5 = con.createStatement();
		Statement st6 = con.createStatement();
		EmpAttendanceHandler empAttendanceHandler=new EmpAttendanceHandler();

		SQL="select * from loan_detail where empno="+eno+" and loan_code ='"+trncd+"' and Active='sanction' order by loan_no" ;
		System.out.println("1::Loan Details-->"+SQL);
		ResultSet rs = st.executeQuery(SQL);
    
		System.out.println("2::Loan Details-->select * from loan_history where empno="+eno+" " +
									"and TRNDT between '"+BOM+"' and '"+EOM+"' and STATUS = 'P' order by loan_no");
		ResultSet rs1=st1.executeQuery("select * from loan_history where empno="+eno+" " +
														"and TRNDT between '"+BOM+"' and '"+EOM+"' and STATUS = 'P' order by loan_no");
		//ResultSet rs1=st1.executeQuery("select SUM(monthly_install) as total_install,count(loan_no)as no_loan  from loan_detail where EMPNO='"+eno+"' and loan_code=226 and Active='sanction' ");
		
		while(rs1.next() && rs.next())
	    {
			LD_LoanNo	 =	rs.getInt("loan_no");
			LH_LoanNo =	rs1.getInt("loan_no");
			System.out.println("3::LD_LoanNo=="+LD_LoanNo+"::LH_LoanNo=="+LH_LoanNo);
			if(LD_LoanNo==LH_LoanNo)
			{
				LH_install_amount	= 	rs1.getFloat("installment_amt");
				LD_paid_amt	=	rs.getFloat("total_paid");
				NEW_paid_amt	=	Math.round(LD_paid_amt+LH_install_amount);
				LD_Actual_Pay	=	rs.getFloat("Actual_pay");
				System.out.println("4::LH_install_amount=="+LH_install_amount+"::LD_paid_amt=="+LD_paid_amt+"::NEW_paid_amt=="+NEW_paid_amt+"::LD_Actual_Pay=="+LD_Actual_Pay);
				if(NEW_paid_amt == LD_Actual_Pay){
					System.out.println("5::Loan Details-->"+"Update loan_detail set total_paid="+NEW_paid_amt+",Active='NIL' where empno="+eno+" and loan_no="+LD_LoanNo+" ");
				st3.executeUpdate("Update loan_detail set total_paid="+NEW_paid_amt+",Active='NIL' where empno="+eno+" and loan_no="+LD_LoanNo+" ");
				}else{
					System.out.println("6::Loan Details-->"+"Update loan_detail set total_paid="+NEW_paid_amt+" where empno="+eno+" and loan_no="+LD_LoanNo+" ");
				st3.executeUpdate("Update loan_detail set total_paid="+NEW_paid_amt+" where empno="+eno+" and loan_no="+LD_LoanNo+" ");
				}
				System.out.println("7::Loan Details-->"+"Update loan_history set Status='F' where empno="+eno+" and loan_no="+LH_LoanNo+"  and trndt between '"+BOM+"' and '"+EOM+"' and status='P' ");
				st3.executeUpdate("Update loan_history set Status='F' where empno="+eno+" and loan_no="+LH_LoanNo+"  and trndt between '"+BOM+"' and '"+EOM+"' and status='P' ");
				
			}
     	  }
			//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd");
			//BOM=simpleDateFormat.parse(BOM).toString();
			System.out.println("8::BOM-->"+BOM);
			
			ResultSet nextmonth =st4.executeQuery("select max(TRNDT)as next_month from paytran where empno="+eno+" and trncd=101");
			if(nextmonth.next()){
				Next_Month=nextmonth.getString("next_month");
				System.out.println("9::Next_Month-->"+Next_Month);
			}
			
			
			
			//here import for next month
			ResultSet next_month_Install_import = st.executeQuery(SQL);
			if(next_month_Install_import.next())
			{
				LD_All_Installment=0;
				next_month_Install_import.previous();
				while(next_month_Install_import.next())
				{
					LD_LoanNo	=	next_month_Install_import.getInt("loan_no");
					LD_Actual_Pay	=	next_month_Install_import.getFloat("Actual_pay");
					LD_paid_amt		=	next_month_Install_import.getFloat("total_paid");
					NEW_Month_Import 	=	Math.round(((int)LD_Actual_Pay) - ((int)LD_paid_amt));
					LH_install_amount	= 	next_month_Install_import.getFloat("monthly_install");
					NEW_Month_Import = (NEW_Month_Import<LH_install_amount)?NEW_Month_Import:LH_install_amount;
					LD_All_Installment  += NEW_Month_Import;
					SQL = ""
							+ "if exists(select * from loan_history where empno="+eno+" and loan_no="+LD_LoanNo+" and trndt ='"+Next_Month+"' and status='P') "
							+ "update loan_history set installment_amt="+NEW_Month_Import+",updated_date='"+TRNDT+"' where empno="+eno+" and loan_no="+LD_LoanNo+" and trndt ='"+Next_Month+"' and status='P' "
							+ "else "
							+ "insert into loan_history (trndt,empno,loan_no,installment_amt,updated_date,status) values "
							+ "('"+Next_Month+"',"+eno+","+LD_LoanNo+","+NEW_Month_Import+",'"+TRNDT+"','P') ";
						System.out.println("EMPNO===>"+eno+"---IMPORT LOAN_NO=="+LD_LoanNo+"FOR MONTH--"+Next_Month);
						
						st3.executeUpdate(SQL);
						
					
				}
			
					System.out.println("11::LD_All_Installment-->"+LD_All_Installment);
					//int no_of_loan = loan_details.getInt("total_no_loan");
					System.out.println("12::Loan Details-->"+" update paytran set inp_amt='"+LD_All_Installment+"',  upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno='"+eno+"' and trndt= '"+Next_Month+"' and trncd=226   ");
					st3.executeUpdate(" update paytran set inp_amt='"+LD_All_Installment+"',  upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno='"+eno+"' and trndt= '"+Next_Month+"' and trncd=226   ");
			}
			else
			{
				System.out.println("13::Loan Details-->"+"update paytran set CF_SW='0' , inp_amt=0.00, net_amt=0.00, cal_amt=0.00,  upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno='"+eno+"' and trndt= '"+Next_Month+"' and trncd=226   ");
				st3.executeUpdate("update paytran set CF_SW='0' , inp_amt=0.00, net_amt=0.00, cal_amt=0.00,  upddt= '"+empAttendanceHandler.getServerDate()+"'  where empno='"+eno+"' and trndt= '"+Next_Month+"' and trncd=226   ");
			}
				   
			 rs.close();
			 st2.close();
			 con.close();
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
	}




}