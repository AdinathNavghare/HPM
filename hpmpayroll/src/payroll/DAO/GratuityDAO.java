package payroll.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import payroll.Model.EmployeeBean;
import payroll.Model.OnAmtBean;
import payroll.Model.SlabBean;

public class GratuityDAO 
{

public void getGratuityAMt(String empno)		
{
	
	String action = "first";
	String[] employee = null;
	CodeMasterHandler CMH = new CodeMasterHandler();
	OnAmtHandler OAH = new OnAmtHandler();
	EmployeeHandler EH = new EmployeeHandler();
	SlabHandler SH = new SlabHandler();
	EmployeeBean emp = new EmployeeBean();
	ArrayList<Integer> values = new ArrayList<Integer>();
	ArrayList<OnAmtBean> codes = new ArrayList<OnAmtBean>();
	String leavingdate="";
	String years="";
	String diff="";
	String minYears="";
	String daysPerYear="";
	String maxamount="";
	int diffYear=0;
	int month=0;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	Date dt = new Date();
	Date dt1 = new Date();
	try
	{	
			Connection con=ConnectionManager.getConnection();
			emp = EH.getEmployeeInformation(empno);
			
			values = OAH.getOnAmtValues(Integer.parseInt(empno), 144);	// replaced trncd 9 to 	144 for Gratuity
			codes = OAH.getOnAmtList(0, 144);
			
			ArrayList<SlabBean> SlabList = SH.getSlabs(0, 144);	//	replaced trncd 9 to 	 144 for Gratuity
			SlabBean SB = SlabList.get(0);
			
			minYears = String.valueOf(SB.getMINAMT());
			daysPerYear = String.valueOf(SB.getFIXAMT());
			maxamount=String.valueOf(SB.getMAXAMT());
			
			leavingdate = emp.getDOL()==null || emp.getDOL()==""?sdf.format(dt):emp.getDOL();
			
			String S_Date="";
			
			PreparedStatement pst2=con.prepareStatement("select * from GratuityDetails where empno="+empno+" and " +
					"trndt =(select max(trndt) from GratuityDetails where empno="+empno+" )");
          	ResultSet rs=pst2.executeQuery();
          	
          	if(rs.next())
          	{
          		S_Date= sdf.format(rs.getDate("trndt"));
          	}
          	else
          	{
          		S_Date=emp.getDOJ();
          	}
			dt = sdf.parse(S_Date);
			dt1 = sdf.parse(leavingdate);
			
			float dateDiff = (dt1.getTime() - dt.getTime())/(1000*60*60*24*365F);
			diff=String.valueOf((dt1.getTime() - dt.getTime())/(1000*60*60*24*365F));
			diffYear =(int)Math.floor(dateDiff);			
			month =(int) Math.floor((dateDiff-diffYear)*12);	
			
			int i=0;
          	int total = 0;
          	for(OnAmtBean OAB:codes)
          	{      
          		total+= values.get(i);       
          		i++;
          	}
	
          	if(Float.parseFloat(diff) >= Integer.parseInt(minYears))
        	{
    			total=(int) ((total/26)*(Integer.parseInt(daysPerYear))*Float.parseFloat(diff));   
    		}
 
          	con.setAutoCommit(false);
          	PreparedStatement pst=con.prepareStatement("insert into gratuityDetails(EMPNO,trndt,upddt,amount,update_by) " +
          			" values("+empno+",GETDATE() ,GETDATE(),"+total+",'')");
          	pst.execute();
          	
          	// To check employee status...         	
          	PreparedStatement status=con.prepareStatement("Select status from EMPMAST where EMPNO = "+empno+"  ");
          	ResultSet st = status.executeQuery();
          	String st_value ="";
        	if(st.next())
          	{
        		 st_value =	st.getString("status");
          	}
        	
     //     System.out.println("Result Set value:  "+st_value);         	          	
          	if(st_value.equalsIgnoreCase("N"))
          	{
          		//TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,STATUS	 	
	          	PreparedStatement pst1=con.prepareStatement(" IF EXISTS ( select * from PAYTRAN_STAGE where empno="+empno+" and trncd =144 )" +
	          			"  update PAYTRAN_STAGE set inp_amt="+total+" where empno="+empno+" and trncd =144 " +
	          			"   ELSE " +
	          			"	 insert into PAYTRAN_STAGE values(GETDATE(),"+empno+",144,0,"+total+","+total+",0,0,"+total+",'',0,GETDATE(),'N') ");
	          	pst1.execute();
          	}
          	else{
          		PreparedStatement pst1=con.prepareStatement(" IF EXISTS ( select * from PAYTRAN where empno="+empno+" and trncd =144 )" +
	          			"  update paytran set inp_amt="+total+" where empno="+empno+" and trncd =144 " +
	          			"   ELSE " +                    
	          			"	 insert into paytran values(GETDATE(),"+empno+",144,0,"+total+","+total+",0,0,"+total+",'',0,GETDATE(),'N') ");
	          	pst1.execute();         		
          	}
			con.commit();
		}	
	
	catch(Exception e){			e.printStackTrace();}	
	
}
	
}
