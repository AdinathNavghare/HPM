package payroll.DAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.ibm.icu.math.BigDecimal;

import payroll.Core.ReportDAO;
import payroll.Model.EmpOffBean;
import payroll.Model.EmployeeBean;
import payroll.Model.EncashmentBean;

public class EncashmentHandler {
	
	
	public ArrayList<EncashmentBean> getprojectCode()//param for getting projcet code list
	{
		ArrayList<EncashmentBean> list= new ArrayList<EncashmentBean>();
		Connection con=null;
		ResultSet rs;

		//String query="SELECT * FROM Prj";
		//String query="SELECT * FROM Project_Sites order by Site_Name";
	//	String query="select rtrim(project_code)+'  '+rtrim(Site_City) as site_Add,* from Project_Sites order by Project_Code";
		String query="select rtrim(Site_City)+'   -      '+rtrim(Site_Name) as site_Add,* from Project_Sites where Site_isdeleted = 0 and Site_Status='open' order by Site_City ASC";
		con = ConnectionManager.getConnectionTech();

		
		try
		{
			Statement st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next())
			{
				EncashmentBean emp=new EncashmentBean();

				emp.setPrj_srno(rs.getInt("Site_ID"));
				emp.setPrj_code(rs.getString("Project_Code"));
				emp.setPrj_name(rs.getString("site_Add"));
				emp.setSite_name(rs.getString("Site_Name"));
				emp.setSite_feild3(rs.getString("Site_Feild3"));
				
				
				/*System.out.print(rs.getInt("Site_ID"));
				System.out.print(rs.getString("Project_Code"));
				System.out.println(rs.getString("Site_Name"));*/

				
				list.add(emp);
			}
			con.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean SaveTransaction(ArrayList<EncashmentBean> list)
	{
		
		int i=0;
		Connection con = null;
		ResultSet rs = null;
		Statement st=null;
		EncashmentBean ebn=new EncashmentBean();
		try
		{
			System.out.println(" in SaveTransaction of handler...");
			System.out.println(" size of updating list is..."+list.size());
			
			/*con = ConnectionManager.getConnection();
			st = con.createStatement();
			String query="select * from EncashmentOnLeave order by convert(int,e.EMPCODE)";
			rs=st.executeQuery(query);*/
		for(EncashmentBean ebn1:list)
		{
			String nextyear=(String)(Integer.parseInt(ebn1.getYear())+1+"-05-31");
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String trndt=EncashmentHandler.gettrndt();
			/*String query="IF EXISTS(select * from paytran where trncd='146' and trndt='"+trndt+"') " +
			" select * from paytran where trncd='146' and trndt='"+trndt+"' " +
					"ELSE (select * from Paytran_stage where trndt between '"+ebn1.getYear()+"-06-01"+"' and '"+nextyear+"' and trncd='146')";
			
			
			System.out.println("saving edited records..for year.........."+ebn1.getYear());
			System.out.println("IF EXISTS(select * from paytran where trncd='146' and trndt='"+trndt+"') " +
			" select * from paytran where trncd='146' and trndt='"+trndt+"' " +
					"ELSE (select * from Paytran_stage where trndt between '"+ebn1.getYear()+"-06-01"+"' and '"+nextyear+"' and trncd='146')");

			
			//	String query="select * from paytran where EMPNO="+ebn1.getEmpno()+" and TRNCD='146' and TRNDT='"+trndt+"' ";
			rs=st.executeQuery(query);
			if(rs.next())
			{
				System.out.println("status is already P....No Edit For this Year");
				continue;
			}
			else
			{*/
			String sql="IF EXISTS(select * from EncashmentOnLeave where EmpCode='"+ebn1.getEmpCode()+"' and Year='"+ebn1.getYear()+"-06-01"+"' ) " +
			"update EncashmentOnLeave set Year='"+ebn1.getYear()+"-06-01"+"',"+"Empno="+ebn1.getEmpno()+","+"EmpCode='"+ebn1.getEmpCode()+"',"+
			"OnHO="+ebn1.getOnHo()+","+"LeaveOnHO="+ebn1.getLeaveOnHO()+"," +"OnOsite="+ebn1.getOnOsite()+","+
			"LeaveOnOS="+ebn1.getLeaveOnOS()+","+"Basic='"+ebn1.getBasic()+"',"+"Encashment='"+ebn1.getEncashment()+"',"+"Status='E' where EmpCode='"+ebn1.getEmpCode()+"' and Year='"+ebn1.getYear()+
		  	"' else insert into EncashmentOnLeave (Year,Empno,EmpCode,OnHO,LeaveOnHO,OnOsite,LeaveOnOS,Basic,Encashment,Status) values('"
			+ebn1.getYear()+"-06-01"+"',"+ebn1.getEmpno()+",'"+ebn1.getEmpCode()+"',"+ebn1.getOnHo()+","+ebn1.getLeaveOnHO()+","+ebn1.getOnOsite()+","+ebn1.getLeaveOnOS()+",'"+
			ebn1.getBasic()+"','"+ebn1.getEncashment()+"','"+ebn1.getStatus()+"')";
			
			
			/*String sql="IF EXISTS(select * from EncashmentOnLeave where EmpCode='"+ebn1.getEmpCode()+"' and Year='"+ebn1.getYear()+"' ) " +
					"update EncashmentOnLeave set Year='"+ebn1.getYear()+"',"+"Empno="+ebn1.getEmpno()+","+"EmpCode='"+ebn1.getEmpCode()+"',"+
					"OnHO="+ebn1.getOnHo()+","+"LeaveOnHO="+ebn1.getLeaveOnHO()+"," +"OnOsite="+ebn1.getOnOsite()+","+
					"LeaveOnOS="+ebn1.getLeaveOnOS()+","+"Basic='"+ebn1.getBasic()+"',"+"Encashment='"+ebn1.getEncashment()+"',"+"Status='E' where EmpCode='"+ebn1.getEmpCode()+"' and Year='"+ebn1.getYear()+
				  	"' else insert into EncashmentOnLeave (Year,Empno,EmpCode,OnHO,LeaveOnHO,OnOsite,LeaveOnOS,Basic,Encashment,Status) values('"
					+ebn1.getYear()+"',"+ebn1.getEmpno()+",'"+ebn1.getEmpCode()+"',"+ebn1.getOnHo()+","+ebn1.getLeaveOnHO()+","+ebn1.getOnOsite()+","+ebn1.getLeaveOnOS()+",'"+
					ebn1.getBasic()+"','"+ebn1.getEncashment()+"','"+ebn1.getStatus()+"')";*/
			
			
			
			/*String query="insert into EncashmentOnLeave (Empno,EmpCode,OnHO,LeaveOnHO,OnOsite,LeaveOnOS,Basic,Encashment) values("
			+ebn1.getEmpno()+","+ebn1.getEmpCode()+","+ebn1.getOnHo()+","+ebn1.getLeaveOnHO()+","+ebn1.getOnOsite()+","+ebn1.getLeaveOnOS()+","+
			ebn1.getBasic()+","+ebn1.getEncashment()+")";*/
			
		
			i++;
			st.executeUpdate(sql);
			//}	
			
		}
		System.out.println("record updated.."+i);
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
		return true;
	}
	
	
	public  static String gettrndt()
	{
		String trndt="";
		try{
		Connection con = null;
		ResultSet rs = null;
		Statement st=null;
		
		con = ConnectionManager.getConnection();
		
			 st = con.createStatement();
			 String query="select trndt from paytran where trncd='199'";
			rs = st.executeQuery(query);
			rs.next();
			trndt=rs.getString("trndt");
		}	
			catch(Exception e)
			{
				e.printStackTrace();
			}	
			return trndt;
	}
	
	public ArrayList<EncashmentBean> getUpdaterecord(String foryear)
	{
		
		int i=0;
		Connection con = null;
		ResultSet rs = null;
		Statement st=null;
		EncashmentBean ebn=new EncashmentBean();
		ArrayList<EncashmentBean> list3=new ArrayList<EncashmentBean>(); 
		try
		{
		
			
			String query="select rtrim(e.FNAME)+'		'+rtrim(e.MNAME)+'		'+rtrim(e.LNAME) as NAME,e1.* from EMPMAST e,EncashmentOnLeave e1 where e.EMPNO=e1.Empno and e1.Year='"+foryear+"-06-01"+"' and e.STATUS='a' order by convert(int,e1.empcode)";
			
			con = ConnectionManager.getConnection();
			
				 st = con.createStatement();
				rs = st.executeQuery(query);
				while(rs.next())
				{
					System.out.println("in encash getUpdaterecord.........."+rs.getString("Year"));
					
					EncashmentBean eBean = new EncashmentBean();
					eBean.setYear(rs.getString("Year"));
					eBean.setEmpno(rs.getInt("Empno"));
					eBean.setEmpCode(rs.getInt("EmpCode"));
					eBean.setName(rs.getString("NAME").toUpperCase());
					eBean.setOnHo(rs.getInt("OnHO"));
					eBean.setLeaveOnHO(rs.getInt("LeaveOnHO"));
					eBean.setOnOsite(rs.getInt("OnOsite"));
					eBean.setLeaveOnOS(rs.getInt("LeaveOnOS"));
					eBean.setBasic(Float.parseFloat(rs.getString("Basic")));
					eBean.setEncashment(Float.parseFloat(rs.getString("Encashment")));
					eBean.setStatus(rs.getString("Status"));
					list3.add(eBean);
					
				}
				
				con.close();
			
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		return list3;
	}

	
	public ArrayList<EncashmentBean> getUpdaterecord1(String prjcode,String foryear)
	{
		
		int i=0;
		Connection con = null;
		ResultSet rs = null;
		Statement st=null;
		EncashmentBean ebn=new EncashmentBean();
		ArrayList<EncashmentBean> list3=new ArrayList<EncashmentBean>(); 
		try
		{
		
	
			String query="select y1.*,rtrim(y2.FNAME)+'		'+rtrim(y2.MNAME)+'		'+rtrim(y2 .LNAME) as FNAME from encashmentonleave y1 ,EMPMAST y2 where  y1.Empno=y2.EMPNO  and  y1.Empno  in (select  e.EMPNO from EMPMAST e,EMPTRAN e1 where e.empno=e1.empno and e.STATUS='A' and e1.STATUS =1 and e1.PRJ_CODE='"+prjcode+"') and y2.STATUS='A'  and y1.year='"+foryear+"-06-01"+""+"' order by convert(int,y1.EmpCode)";
			
			con = ConnectionManager.getConnection();
			
				 st = con.createStatement();
				rs = st.executeQuery(query);
				while(rs.next())
				{
					System.out.println("in encash getUpdaterecord..........");
					EncashmentBean eBean = new EncashmentBean();
					eBean.setYear(rs.getString("Year"));
					eBean.setEmpCode(rs.getInt("EmpCode"));
					eBean.setName(rs.getString("FNAME").toUpperCase());
					eBean.setOnHo(rs.getInt("OnHO"));
					eBean.setLeaveOnHO(rs.getInt("LeaveOnHO"));
					eBean.setOnOsite(rs.getInt("OnOsite"));
					eBean.setLeaveOnOS(rs.getInt("LeaveOnOS"));
					eBean.setBasic(Float.parseFloat(rs.getString("Basic")));
					eBean.setEncashment(Float.parseFloat(rs.getString("Encashment")));
					eBean.setStatus(rs.getString("Status"));
					list3.add(eBean);
					
				}
				
				con.close();
			
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		return list3;
	}

	public int checkrecord(String foryear)
	{
		int status=-1;
		Connection con = null;
		//ResultSet rs = null;
		Statement st=null;
		EncashmentBean ebn=new EncashmentBean();
		ArrayList<EncashmentBean> list3=new ArrayList<EncashmentBean>(); 
		try
		{
			System.out.println("incheck record...");
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			ResultSet rs = null;
				foryear=(String)foryear+"-06-01";
				String query="select rtrim(e.FNAME)+'		'+rtrim(e.MNAME)+'		'+rtrim(e.LNAME) as NAME,e1.EmpCode,e1.OnHO,e1.LeaveOnHO,e1.OnOsite,e1.LeaveOnOS,e1.basic,e1.encashment from EMPMAST e,EncashmentOnLeave e1 where e.EMPNO=e1.Empno and e1.Year='"+foryear+"' and e.STATUS='a' order by convert(int,e.EMPCODE)";
				rs=st.executeQuery(query);
				if(rs.next())
				{
					status=1;
				}
				else
					status=0;
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
		
		return status;
	}
	
	public ArrayList<EncashmentBean> geteditrecord(ArrayList<EncashmentBean> list)
	{
		
		int i=0;
		Connection con = null;
		//ResultSet rs = null;
		Statement st=null;
		EncashmentBean ebn=new EncashmentBean();
		ArrayList<EncashmentBean> list3=new ArrayList<EncashmentBean>(); 
		try
		{
			System.out.println("in edit record...edit record fetching...");
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			for(EncashmentBean tbn:list)
			{
				ResultSet rs = null;
				ebn=new EncashmentBean();
				String query="select rtrim(e.FNAME)+'		'+rtrim(e.MNAME)+'		'+rtrim(e.LNAME) as NAME,e1.* from EMPMAST e,EncashmentOnLeave e1 where e.EMPNO=e1.Empno and e1.EmpCode='"+tbn.getEmpCode()+"' and e1.Year='"+tbn.getYear()+"' and e.status='a' order by convert(int,e1.EmpCode)";
				System.out.println(query);
				rs=st.executeQuery(query);
				while(rs.next())
				{
					System.out.println("in encash geteditrecord..........");
					EncashmentBean eBean = new EncashmentBean();
					eBean.setYear(rs.getString("Year"));
					eBean.setEmpno(rs.getInt("Empno"));
					eBean.setEmpCode(rs.getInt("EmpCode"));
					eBean.setName(rs.getString("NAME").toUpperCase());
					eBean.setOnHo(rs.getInt("OnHO"));
					eBean.setLeaveOnHO(rs.getInt("LeaveOnHO"));
					eBean.setOnOsite(rs.getInt("OnOsite"));
					eBean.setLeaveOnOS(rs.getInt("LeaveOnOS"));
					eBean.setBasic(Float.parseFloat(rs.getString("Basic")));
					eBean.setEncashment(Float.parseFloat(rs.getString("Encashment")));
					eBean.setStatus("Status");
					list3.add(eBean);
					
				}
				i++;
			}
			System.out.println("total record for edit.........."+i);
			con.close();
			
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		return list3;
	}
	
	
	public ArrayList<EncashmentBean> editencashment(ArrayList<EncashmentBean> list)
	{
		
		int i=0,j=0,k=0;
		
		int hoday=0;
		//EncashmentBean ebn=new EncashmentBean();
		ArrayList<EncashmentBean> elist3=new ArrayList<EncashmentBean>();
		System.out.println("in encash handler-editencashment and record length is.."+list.size());
		for(EncashmentBean tbn:list)
		{
			EncashmentBean ebn=new EncashmentBean();
			/*for( j=0;j<=k;j++)
			{*/
				//noof[i][j]=noofdays[i][j];
				System.out.println("-----editencashment HOdays..."+tbn.getOnHo());
				System.out.println("-----editencashment empcode..."+tbn.getEmpCode());
				System.out.println("-----editencashment leaveHOdays..."+tbn.getLeaveOnHO());
				
				double day= (tbn.getOnHo());
				float  day1=(float)(day*19/365); 
				 hoday=Math.round(day1);
			//}
				//noof[i][j]=noofdays[i][j];
				System.out.println("-----editencashment OSdays..."+tbn.getOnOsite());
				System.out.println("-----editencashment leaveOsdays..."+tbn.getLeaveOnOS());
				double day2= (tbn.getOnOsite());
				float  day11=(float)(day2*30/365); 
				int osdayleav=Math.round(day11);
				
				int totaldays=hoday+osdayleav;
				System.out.println("leave total days ... "+totaldays);
				System.out.println("in updating encash.....calculating..year..."+tbn.getYear());
				float encash=(totaldays*(tbn.getBasic()/26));
				ebn.setYear(tbn.getYear());
				ebn.setEmpno(tbn.getEmpno());
				ebn.setEmpCode(tbn.getEmpCode());
				ebn.setName(tbn.getName().toUpperCase());
				ebn.setOnHo(tbn.getOnHo());
				ebn.setLeaveOnHO(hoday);
				ebn.setOnOsite( tbn.getOnOsite());
				ebn.setLeaveOnOS(osdayleav);
				ebn.setBasic(tbn.getBasic());
				ebn.setEncashment(encash);
				ebn.setStatus(tbn.getStatus());
				elist3.add(ebn);
				i++;
		}
		
		/*for( i=0;i<noof.length;i++)
		{
			for( j=0;j<=k;j++)
			{
				noofHO[i]=noof[i][j];
				System.out.println("encash handler-editencashment HOdays..."+noofHO[i]);
			}
				noofOs[i]=noof[i][j];
				System.out.println("encash handler-editencashment OSdays..."+noofOs[i]);
		}
		for(EncashmentBean tbn:list)
		{
			EncashmentBean eBean = new EncashmentBean();
		double day= noofHO[k];
		float  day1=(float)(day*19/365); 
		int hoday=Math.round(day1);
		
      
		System.out.println("daysssssshoo..."+hoday);
		double day2= noofOs[k];
		float  day11=(float)(day2*30/365); 
		int osdayleav=Math.round(day11);
		
		
		
		
		//float encash=0;
		int totaldays=hoday+osdayleav;
		System.out.println("leave total days ... "+totaldays);
		float encash=(totaldays*(tbn.getBasic()/30));
		
		
		eBean.setEmpCode(tbn.getEmpCode());
		eBean.setName(tbn.getName().toUpperCase());
		eBean.setOnHo(noofHO[k]);
		eBean.setLeaveOnHO(hoday);
		eBean.setOnOsite(noofOs[k]);
		eBean.setLeaveOnOS(osdayleav);
		eBean.setBasic(tbn.getBasic());
		eBean.setEncashment(encash);
		elist3.add(eBean);
		}*/
		
		/*encash=encash/365;
		System.out.println("total leave days ... "+encash);
		encash=Math.round(encash*(basic/30));*/
		
	return elist3;
	}

	
	public String getprojname(String siteid)
	{
		System.out.println("in getprojectname");
		Connection con=null;
		ResultSet rs;
		String str="";
		String query="select Project_Code from Project_Sites where Site_ID="+siteid+"";
		con = ConnectionManager.getConnectionTech();

		
		try
		{
			Statement st=con.createStatement();
			rs=st.executeQuery(query);
			if(rs==null)
			{
				System.out.println("nulllll");
			}
			else
			{
				System.out.println("not   nulllll");
			while(rs.next())
			{
				str=rs.getString("Project_Code");
				System.out.println("Project codeis..."+str);
			
			}
			}
			con.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return str;
	}
	
	
	
	public String getpname(String pcode)
	{
		String prjname=null;
		try
		{
			Connection conn=null;	
			conn=ConnectionManager.getConnectionTech();
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select Site_Name from Project_Sites where Project_Code='"+pcode+"'");
			
			if(rs.next())
			{
				prjname=rs.getString(1);
			}
			
			
			conn.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		return prjname;
		
	}
	
	
public  ArrayList<EncashmentBean> getEncashEmpList(String prjCode){
		System.out.println("in getEncashEmpList");
		ArrayList<EncashmentBean> getlist= new ArrayList<EncashmentBean>();
		Connection con = null;
		ResultSet rs = null;
		String query="";
		int i=0;
		System.out.println("in getEncashEmpList prjname is.."+prjCode);
		//String query = "SELECT EMPNO FROM EMPTRAN WHERE PRJ_CODE='"+prjCode+"'";
		//String query = "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A'  and etn.PRJ_SRNO="+prjCode;
		
//		String query = "select emp.EMPNO from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where emp.STATUS = 'A' and etn.PRJ_SRNO='"+prjCode+"' and etn.SRNO = (select MAX(en.SRNO) from EMPTRAN en where en.PRJ_SRNO = '"+prjCode+"' and etn.EMPNO = en.EMPNO )";
		//String query = "select e.EMPNO,e.FNAME,e.DOJ from empmast e  where Project_Code='"+prjCode+"'";
		if(!prjCode.equalsIgnoreCase("ALL"))
		{
			query="select distinct(e1.EMPNO),e1.EMPCODE,rtrim(e1.FNAME)+'		'+rtrim(e1.MNAME)+'		'+rtrim(e1.LNAME) as FNAME,e1.DOJ from EMPMAST e1,EMPTRAN e2  where e2.empno=e1.empno and e2.PRJ_CODE='"+prjCode+"' and e1.Status='a'  order by empno";
		}
		else
		{
			query="select  e1.EMPCODE,p.inp_amt,e1.EMPNO,rtrim(e1.FNAME)+'		'+rtrim(e1.MNAME)+'		'+rtrim(e1.LNAME) as FNAME,e1.DOJ from paytran p, EMPMAST e1  where  e1.EMPNO=p.EMPNO and trncd=199 and e1.Status='a'  order by convert(int,e1.EMPCODE)";
		}
			System.out.println("in getEncashEmpList1111");
		con = ConnectionManager.getConnection();
		try
		{
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while(rs.next())
			{
				System.out.println("in whileeee");
				EncashmentBean eBean = new EncashmentBean();
				eBean.setEmpno(rs.getInt("EMPNO"));
				eBean.setEmpCode(Integer.parseInt(rs.getString("EMPCODE")));
				eBean.setName(rs.getString("FNAME"));
				eBean.setBasic(rs.getInt("inp_amt"));
				//eBean.set(rs.getString("FNAME").toUpperCase());
				
				getlist.add(eBean);
				i++;
				
			}
		
			//rs.close();
			//st.close();
			con.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		System.out.println("TOTAL EMPLIST...."+i);
		return getlist;
	}
	

public ArrayList<EncashmentBean> calculateEncashment(ArrayList<EncashmentBean> getlist,String foryear)
{
	int DaysInMonths=0;
	int i=0;
			System.out.println("in getNoOfDays");
			int date=Integer.parseInt(foryear);
		String fdate1="";
		String fdate2="";
		String fdate3="";
		int hoday=0,osdayleav=0;
		double dayy=0,day2=0;
		float encash=0,day1=0,day11=0;
		int day=0,totaldays=0;
		String md="";
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		Statement st1;
		int noofdaysho=0;
		int noofdaysos=0;
		int flag=-1;
		int Days=0;
		String oldprj="";
		ArrayList<EncashmentBean> calculatedlist=new ArrayList<EncashmentBean>();
		
		try
		{
		for(EncashmentBean ebn:getlist)
		{
			EncashmentBean eBean=new EncashmentBean();
		String query="select distinct(PRJ_CODE),SRNO,ORDER_DT from EMPTRAN where EMPNO="+ebn.getEmpno()+" and PRJ_CODE!='null' order by SRNO desc";
		System.out.println("select distinct(PRJ_CODE),ORDER_DT from EMPTRAN where EMPNO="+ebn.getEmpno()+"");
		con = ConnectionManager.getConnection();
		
			Statement st = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, 
					   ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(query);
			if(!rs.next())
			{
				noofdaysho=0;
				noofdaysos=0;
			}
			else
			{
				noofdaysho=0;
				noofdaysos=0;
				rs.previous();
			while(rs.next())
			{
				fdate1=rs.getString("ORDER_DT");
				System.out.println("fdate1 "+fdate1);
				fdate2=foryear+"-06-01";
				 st1 = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, 
						   ResultSet.CONCUR_READ_ONLY);
				String query1="select DATEDIFF(d,'"+fdate2+"', '"+fdate1+"') as days";
				
				rs1 = st1.executeQuery(query1);
				rs1.next();
				Days=Integer.parseInt((rs1.getString("days")));
				System.out.println("noOfDays between "+fdate2+" and "+fdate1+" is "+Days);
				if(Days<=0)
				{
					oldprj=rs.getString("PRJ_CODE");
					Days=365;
					flag=0;
					System.out.println("in noOfDays<=0");
					
					break;
				}	

				else
				{
					System.out.println("in noOfDays>>0");
					if(!rs.next())
					{
						System.out.println("this is last record....");
						flag=3;
						rs.previous();
						break;
					}
					rs.previous();
					continue;
				}
			
			}
			if(flag==3)
			{
				Statement st2 = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, 
						   ResultSet.CONCUR_READ_ONLY);
				String dt=foryear+"-06-01";
				//int dt1=Integer.parseInt(dt);
				String checkdate="select DATEDIFF(d,'"+dt+"', '"+fdate1+"') as day";
				ResultSet dtchk = st2.executeQuery(checkdate);
				dtchk.next();
				int dtdiff=Integer.parseInt((dtchk.getString("day")));
				if(dtdiff>365)
				{
					flag=-1;
				}
				else
				{
				fdate2=rs.getString("ORDER_DT");
				oldprj=rs.getString("PRJ_CODE");
				}
			}
			else
			{
			  oldprj=rs.getString("PRJ_CODE");
			fdate2=foryear+"-06-01";
			}
			System.out.println("flag!=0");
				if(!rs.previous())
				{
					if(flag!=0)
					{
					rs.next();
					fdate1=rs.getString("ORDER_DT");
					fdate2=date+1+"-05-31";
					Statement st2 = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, 
							   ResultSet.CONCUR_READ_ONLY);
					String query1="select DATEDIFF(d,'"+fdate1+"', '"+fdate2+"') as days";
					
					rs1 = st2.executeQuery(query1);
					rs1.next();
					Days=Integer.parseInt((rs1.getString("days")));
					if(Days<=0)
					{
						if(rs.getString("PRJ_CODE").equalsIgnoreCase("HC - 049"))
								{
								noofdaysho+=0;
				
								}
						else{
							noofdaysos+=0;
									}
					}
					else
					{
						
						if(rs.getString("PRJ_CODE").equalsIgnoreCase("HC - 049"))
						{
						noofdaysho+=Days+1;
		
						}
						else{
							noofdaysos+=Days+1;
							}
					}
				}
					else
					{
						fdate1=foryear+"-06-01";
						fdate2=date+1+"-05-31";
						Statement st2 = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, 
								   ResultSet.CONCUR_READ_ONLY);
						String query1="select DATEDIFF(d,'"+fdate1+"', '"+fdate2+"') as days";
						
						rs1 = st2.executeQuery(query1);
						rs1.next();
						Days=Integer.parseInt((rs1.getString("days")));
								if(oldprj.equalsIgnoreCase("HC - 049"))
									{
									noofdaysho=Days+1;
					
									}
									else{
										noofdaysos=Days+1;
										}
					}
				}
				else
				{
					rs.next();
					int dtdiff=0;
					String query1="";
				while(rs.previous())
				{
					
					if(flag==-1)
					{
						noofdaysho=0;
						noofdaysos=0;
						break;
					}
					
					fdate1=rs.getString("ORDER_DT");
					System.out.println("fdate1... "+fdate1);
					System.out.println("in getEncashmentAmnt1  "+fdate1);
					
					Statement st2 = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, 
							   ResultSet.CONCUR_READ_ONLY);
					String dt=foryear+"-06-01";
					//int dt1=Integer.parseInt(dt);
					String checkdate="select DATEDIFF(d,'"+dt+"', '"+fdate1+"') as day";
					ResultSet dtchk = st2.executeQuery(checkdate);
					dtchk.next();
					dtdiff=Integer.parseInt((dtchk.getString("day")));
					if(dtdiff>=0&&dtdiff<365)
					{
					 query1="select DATEDIFF(d,'"+fdate2+"', '"+fdate1+"') as days";
					
					rs1 = st2.executeQuery(query1);
					rs1.next();
					Days=Integer.parseInt((rs1.getString("days")));
					System.out.println("noOfDays between "+fdate2+" and "+fdate1+" is "+Days);
					
						/*if(oldprj.equalsIgnoreCase("HC - 049"))
						{
						noofdays[0]=365;
						System.out.println("noOfDays..."+noofdays[0]);
						}
						else
						{
							noofdays[1]=365;
							System.out.println("noOfDays..."+noofdays[1]);
						}
					}*/
					
						if(Days>=0)
						{
							/*if(Days>=365)
							{
								if(oldprj.equalsIgnoreCase("HC - 049"))
								{
								noofdaysho=365;
								flag=-1;
								System.out.println("noOfDays..."+noofdaysho);
								break;
								}
								else
								{
									noofdaysos=365;
									flag=-1;
									System.out.println("noOfDays..."+noofdaysos);
									break;
								}
							}*/
							if(Days==364)
							{
								if(oldprj.equalsIgnoreCase("HC - 049"))
								{
								noofdaysho=365;
								System.out.println("noOfDays..."+noofdaysho);
								}
								else
								{
									noofdaysos=365;
									System.out.println("noOfDays..."+noofdaysos);
								}
							}
							else
							{
								if(oldprj.equalsIgnoreCase("HC - 049"))
								{
								noofdaysho+=Days;
								System.out.println("noOfDays..."+noofdaysho);
								}
								else
								{
									noofdaysos+=Days;
									System.out.println("noOfDays..."+noofdaysos);
								}
							}
						}
						/*else
						{
							fdate1=date+1+"-03-31";
							 st2 = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, 
									   ResultSet.CONCUR_READ_ONLY);
							 query1="select DATEDIFF(d,'"+fdate2+"', '"+fdate1+"') as days";
							
							rs1 = st2.executeQuery(query1);
							rs1.next();
							Days=Integer.parseInt((rs1.getString("days")));
							System.out.println("noOfDays between "+fdate2+" and "+fdate1+" is "+Days);
							if(Days==364)
							{
								
							
								if(oldprj.equalsIgnoreCase("HC - 049"))
								{
								noofdaysho=365;
								System.out.println("noOfDays..."+noofdaysho);
								}
								else
								{
									noofdaysos=365;
									System.out.println("noOfDays..."+noofdaysos);
								}
							}
							else
							{
								if(oldprj.equalsIgnoreCase("HC - 049"))
								{
								noofdaysho+=Days;
								System.out.println("noOfDays..."+noofdaysho);
								}
								else
								{
									noofdaysos+=Days;
									System.out.println("noOfDays..."+noofdaysos);
								}
							}
						}*/
					
					if(!rs.previous())
					{
						rs.next();
					
					oldprj=rs.getString("PRJ_CODE");
					fdate2=fdate1;
					flag=1;
					break;
					}
					else
					{
						rs.next();
						
						oldprj=rs.getString("PRJ_CODE");
						fdate2=fdate1;
						flag=1;
						continue;
					}
					}
					
					else
					{
						fdate1=date+1+"-05-31";
						 st2 = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, 
								   ResultSet.CONCUR_READ_ONLY);
						 query1="select DATEDIFF(d,'"+fdate2+"', '"+fdate1+"') as days";
						
						rs1 = st2.executeQuery(query1);
						rs1.next();
						Days=Integer.parseInt((rs1.getString("days")));
						System.out.println("noOfDays between "+fdate2+" and "+fdate1+" is "+Days);
						if(Days==364)
						{
							
						
							if(oldprj.equalsIgnoreCase("HC - 049"))
							{
							noofdaysho=365;
							System.out.println("noOfDays..."+noofdaysho);
							}
							else
							{
								noofdaysos=365;
								System.out.println("noOfDays..."+noofdaysos);
							}
						}
						else
						{
							if(oldprj.equalsIgnoreCase("HC - 049"))
							{
							noofdaysho+=Days+1;
							System.out.println("noOfDays..."+noofdaysho);
							}
							else
							{
								noofdaysos+=Days+1;
								System.out.println("noOfDays..."+noofdaysos);
							}
						}
						if(!rs.previous())
						{
							rs.next();
						flag=-1;
						break;
						}
						else
						{
							flag=-1;
							break;
						}
						/*else 
						{
							rs.next();
							fdate2=fdate1;
							oldprj=rs.getString("PRJ_CODE");
							continue;
						}*/
					}
				}
			if(flag==1 ||flag==3 || flag==0)
			{
				if(flag==1)
				{
					rs.next();
				}
			fdate1=date+1+"-05-31";
			Statement st2 = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, 
					   ResultSet.CONCUR_READ_ONLY);
			 query1="select DATEDIFF(d,'"+fdate2+"', '"+fdate1+"') as days";
			System.out.println("in whileeee2");
			rs1 = st2.executeQuery(query1);
			rs1.next();
			Days=Integer.parseInt((rs1.getString("days")));
			System.out.println("noOfDays between "+fdate2+" and "+fdate1+" is "+Days);
			}
			if(flag!=-1)
			{
								if(Days>=0)
								{
									
								
										if(oldprj.equalsIgnoreCase("HC - 049"))
										{
											noofdaysho+=Days+1;
											if(noofdaysho==364)
											{
												noofdaysho=365;
											}
											System.out.println("noOfDays..."+noofdaysho);
										}
										else
										{
											noofdaysos+=Days+1;
											if(noofdaysos==364)
											{
												noofdaysos=365;
											}
											System.out.println("noOfDays..."+noofdaysos);
										}
									}
									else{
										if(oldprj.equalsIgnoreCase("HC - 049"))
										{
											noofdaysho=0;
											System.out.println("noOfDays..."+noofdaysho);
										}
										else
										{
											noofdaysos=0;
											System.out.println("noOfDays..."+noofdaysos);
										}
											}
			}
			
				
				}
			}
			
			
			 dayy= noofdaysho;
			  day1=(float)(dayy*19/365); 
			 hoday=Math.round(day1);
			
	      
			System.out.println("HOLEAVE days..."+hoday);
			day2= noofdaysos;
			  day11=(float)(day2*30/365); 
			 osdayleav=Math.round(day11);
			 System.out.println("OSLEAVE days..."+osdayleav);
			
			
			
			//float encash=0;
			 totaldays=hoday+osdayleav;
			System.out.println("leave total days ... "+totaldays);
			 encash=(totaldays*(ebn.getBasic()/26));
			
			/*encash=encash/365;
			System.out.println("total leave days ... "+encash);
			encash=Math.round(encash*(basic/30));*/
			System.out.println("total encash ... "+encash);
			
			BigDecimal encashment=new BigDecimal(encash);
			encashment=encashment.setScale(2,BigDecimal.ROUND_HALF_EVEN);
			
			eBean.setYear(foryear);
			eBean.setEmpno(ebn.getEmpno());
			eBean.setEmpCode(ebn.getEmpCode());
			eBean.setName(ebn.getName());
			eBean.setOnHo(noofdaysho);
			eBean.setLeaveOnHO(hoday);
			eBean.setOnOsite(noofdaysos);
			eBean.setLeaveOnOS(osdayleav);
			eBean.setBasic(ebn.getBasic());
			eBean.setEncashment(encashment.floatValue());
			eBean.setStatus("C");
			calculatedlist.add(eBean);
			i++;
			
		}
}catch (SQLException e) 
{
	e.printStackTrace();
}
		System.out.println("TOTAL EMPLIST...."+i);
return calculatedlist;
}

public boolean posttransaction(ArrayList<EncashmentBean> list)
{
	int i=0;
	Connection con = null;
	ResultSet rs = null;
	Statement st=null;
	EncashmentBean ebn=new EncashmentBean();
	try
	{
		System.out.println(" in posttransaction of handler...");
		/*con = ConnectionManager.getConnection();
		st = con.createStatement();
		String query="select * from EncashmentOnLeave order by convert(int,e.EMPCODE)";
		rs=st.executeQuery(query);*/
		String trndt=EncashmentHandler.gettrndt();
		for(EncashmentBean ebn1:list)
		{
			System.out.println("empno.."+ebn1.getEmpno());
			System.out.println("year.."+ebn1.getYear());
		}
	for(EncashmentBean ebn1:list)
	{
		con = ConnectionManager.getConnection();
		st = con.createStatement();
		
	/*String dt=ebn1.getYear();
	int eno=ebn1.getEmpno();
	String upddt=ebn1.getYear();*/
	
	
	

			
		/*String sql="IF EXISTS(select * from paytran where EMPNO="+ebn1.getEmpno()+" and TRNDT='"+ebn1.getYear()+"' and TRNCD=146 ) " +
		"update paytran set TRNDT='"+ebn1.getYear()+"',"+"EMPNO="+ebn1.getEmpno()+","+"TRNCD=146"+","+
		"SRNO=0,"+"INP_AMT='"+ebn1.getBasic()+"'," +"CAL_AMT='"+ebn1.getEncashment()+"',"+
		"ADJ_AMT=0.00"+","+"ARR_AMT='"+ebn1.getEncashment()+"',"+"NET_AMT='"+ebn1.getEncashment()+"',"+"CF_SW=0"+","+"USRCODE=0"+","+
		"UPDDT='"+ebn1.getYear()+"',"+"STATUS='P' where EMPNO="+ebn1.getEmpno()+" and TRNCD=146 and TRNDT='"+ebn1.getYear()+
	  	"' else insert into paytran (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,STATUS) values('"
		+ebn1.getYear()+"',"+ebn1.getEmpno()+",146,0,'"+ebn1.getBasic()+"','"+ebn1.getEncashment()+"',0.00,'"+ebn1.getEncashment()+"','"+
		ebn1.getEncashment()+"',0,0,'"+ebn1.getYear()+"','P')";*/
		
		//last used....
		/*String sql=("insert into paytran (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,STATUS) values('"+ebn1.getYear()+"',"+ebn1.getEmpno()+",146,0,"+ebn1.getBasic()+","+ebn1.getEncashment()+","+0.00+","+ebn1.getEncashment()+","+
		ebn1.getEncashment()+",0,'0','"+ebn1.getYear()+"', 'P' )");*/
		
		
		String sql=("insert into paytran (TRNDT,EMPNO,TRNCD,SRNO,INP_AMT,CAL_AMT,ADJ_AMT,ARR_AMT,NET_AMT,CF_SW,USRCODE,UPDDT,STATUS) values('"+trndt+"',"+ebn1.getEmpno()+",146,0,"+ebn1.getEncashment()+","+ebn1.getEncashment()+","+0.00+","+0.00+","+
				ebn1.getEncashment()+",0,'0','"+ReportDAO.getSysDate()+"', 'P' )");
		
	
		i++;
		st.executeUpdate(sql);
			
		String UpdateEncash_Status="Update EncashmentOnLeave set Status='P' where Year='"+ebn1.getYear()+"' and empno="+ebn1.getEmpno()+"";
		st.executeUpdate(UpdateEncash_Status);
		
	}
	System.out.println("record updated.."+i);
	}catch(Exception e)
	{
		e.printStackTrace();
	}	
	return true;
}
public static String checkpost(String foryear)
{
	String flag="";
	String nextyear=(String)(Integer.parseInt(foryear)+1+"-05-31");
	foryear=(String)foryear+"-06-01";
	Connection con = null;
	//ResultSet rs = null;
	Statement st=null;
	ResultSet rs=null;
	try
	{
		System.out.println("in check posted record for year... "+foryear);
		con = ConnectionManager.getConnection();
		st = con.createStatement();
		//last used...
		String trndt=EncashmentHandler.gettrndt();
		/*String query="select * from paytran where trncd='146' and trndt='"+foryear+"'";*/
		String query="IF EXISTS(select * from paytran where trncd='146' and trndt='"+trndt+"') " +
				" select * from paytran where trncd='146' and trndt='"+trndt+"' " +
						"ELSE (select * from Paytran_stage where trndt between'"+foryear+"' and '"+nextyear+"' and trncd='146')";
		rs=st.executeQuery(query);
		if(rs.next())
		{
			System.out.println("IN DISABLEPOST");
			flag="disablepost";
			System.out.println("year..."+rs.getString("TRNDT"));
			System.out.println("trncd..."+rs.getString("TRNCD"));
			System.out.println("encash...."+rs.getString("INP_AMT"));
		}
		else
		{
			System.out.println("IN ENABLEPOST");
			flag="enablepost";
		}
	}catch(Exception e)
	{
		e.printStackTrace();
	}	
	return flag;	
}


public boolean editSaveTransaction(ArrayList<EncashmentBean> list,String foryear)
{
	
	int i=0;
	Connection con = null;
	ResultSet rs = null;
	Statement st=null;
	EncashmentBean ebn=new EncashmentBean();
	try
	{
		System.out.println(" in SaveTransaction of handler...");
		System.out.println(" size of updating list is..."+list.size());
		
		/*con = ConnectionManager.getConnection();
		st = con.createStatement();
		String query="select * from EncashmentOnLeave order by convert(int,e.EMPCODE)";
		rs=st.executeQuery(query);*/
		//String foryear="2015";
		String nextyear=(String)(Integer.parseInt(foryear)+1+"-05-31");
		con = ConnectionManager.getConnection();
		st = con.createStatement();
		String trndt=EncashmentHandler.gettrndt();
		
		String query="IF EXISTS(select * from paytran where trncd='146' and trndt='"+trndt+"') " +
				" select * from paytran where trncd='146' and trndt='"+trndt+"' " +
						"ELSE (select * from EncashmentOnLeave where Year='"+foryear+"-06-01"+"' and Status='P')";

		/*  -----------second option for edit ------------------------*/
		/*System.out.println("saving edited records..for year.........."+foryear+"-06-01");
		System.out.println("IF EXISTS(select * from paytran where trncd='146' and trndt='"+trndt+"') " +
		" select * from paytran where trncd='146' and trndt='"+trndt+"' " +
				"ELSE (select * from Paytran_stage where trndt between'"+foryear+"-06-01"+"' and '"+nextyear+"' and trncd='146')");*/

		
		//	String query="select * from paytran where EMPNO="+ebn1.getEmpno()+" and TRNCD='146' and TRNDT='"+trndt+"' ";
		rs=st.executeQuery(query);
		if(rs.next())
		{
			System.out.println("status is already P....No Edit For this Year");
		}
		else
		{
		
				for(EncashmentBean ebn1:list)
				{
		
		String sql="IF EXISTS(select * from EncashmentOnLeave where EmpCode='"+ebn1.getEmpCode()+"' and Year='"+ebn1.getYear()+"' ) " +
		"update EncashmentOnLeave set Year='"+ebn1.getYear()+"',"+"Empno="+ebn1.getEmpno()+","+"EmpCode='"+ebn1.getEmpCode()+"',"+
		"OnHO="+ebn1.getOnHo()+","+"LeaveOnHO="+ebn1.getLeaveOnHO()+"," +"OnOsite="+ebn1.getOnOsite()+","+
		"LeaveOnOS="+ebn1.getLeaveOnOS()+","+"Basic='"+ebn1.getBasic()+"',"+"Encashment='"+ebn1.getEncashment()+"',"+"Status='E' where EmpCode='"+ebn1.getEmpCode()+"' and Year='"+ebn1.getYear()+
	  	"' else insert into EncashmentOnLeave (Year,Empno,EmpCode,OnHO,LeaveOnHO,OnOsite,LeaveOnOS,Basic,Encashment,Status) values('"
		+ebn1.getYear()+"',"+ebn1.getEmpno()+",'"+ebn1.getEmpCode()+"',"+ebn1.getOnHo()+","+ebn1.getLeaveOnHO()+","+ebn1.getOnOsite()+","+ebn1.getLeaveOnOS()+",'"+
		ebn1.getBasic()+"','"+ebn1.getEncashment()+"','"+ebn1.getStatus()+"')";
		
		
		/*String sql="IF EXISTS(select * from EncashmentOnLeave where EmpCode='"+ebn1.getEmpCode()+"' and Year='"+ebn1.getYear()+"' ) " +
				"update EncashmentOnLeave set Year='"+ebn1.getYear()+"',"+"Empno="+ebn1.getEmpno()+","+"EmpCode='"+ebn1.getEmpCode()+"',"+
				"OnHO="+ebn1.getOnHo()+","+"LeaveOnHO="+ebn1.getLeaveOnHO()+"," +"OnOsite="+ebn1.getOnOsite()+","+
				"LeaveOnOS="+ebn1.getLeaveOnOS()+","+"Basic='"+ebn1.getBasic()+"',"+"Encashment='"+ebn1.getEncashment()+"',"+"Status='E' where EmpCode='"+ebn1.getEmpCode()+"' and Year='"+ebn1.getYear()+
			  	"' else insert into EncashmentOnLeave (Year,Empno,EmpCode,OnHO,LeaveOnHO,OnOsite,LeaveOnOS,Basic,Encashment,Status) values('"
				+ebn1.getYear()+"',"+ebn1.getEmpno()+",'"+ebn1.getEmpCode()+"',"+ebn1.getOnHo()+","+ebn1.getLeaveOnHO()+","+ebn1.getOnOsite()+","+ebn1.getLeaveOnOS()+",'"+
				ebn1.getBasic()+"','"+ebn1.getEncashment()+"','"+ebn1.getStatus()+"')";*/
		
		
		
		/*String query="insert into EncashmentOnLeave (Empno,EmpCode,OnHO,LeaveOnHO,OnOsite,LeaveOnOS,Basic,Encashment) values("
		+ebn1.getEmpno()+","+ebn1.getEmpCode()+","+ebn1.getOnHo()+","+ebn1.getLeaveOnHO()+","+ebn1.getOnOsite()+","+ebn1.getLeaveOnOS()+","+
		ebn1.getBasic()+","+ebn1.getEncashment()+")";*/
		
	
		i++;
		st.executeUpdate(sql);
		}
		}
		System.out.println("no of records updated.."+i);
	}
	
	catch(Exception e)
	{
		e.printStackTrace();
	}	
	return true;
}


}
	


