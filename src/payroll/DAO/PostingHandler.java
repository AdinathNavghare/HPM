package payroll.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import payroll.Model.AutopostBean;
import payroll.Model.HoPostBean;

public class PostingHandler
{

	public ArrayList<HoPostBean> gethopostlist()
	{
		ArrayList<HoPostBean> hplist = new ArrayList<HoPostBean>();
		HoPostBean hpb ;
		try
		{
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("Select * from HOPOST order by BRCD,TRNCD"); 
			while(rs.next())
			{
				hpb = new HoPostBean();
				hpb.setBRCD(rs.getString("BRCD")!=null?rs.getInt("BRCD"):0);
				hpb.setTRNCD(rs.getString("TRNCD")!=null?rs.getInt("TRNCD"):0);
				hpb.setSRNO(rs.getString("SRNO")!=null?rs.getInt("SRNO"):0);
				hpb.setSUBSYS_CD(rs.getString("SUBSYS_CD")!=null?rs.getString("SUBSYS_CD"):"");
				hpb.setAC_NO(rs.getString("AC_NO")!=null?rs.getInt("AC_NO"):0);
				hpb.setAMOUNT(rs.getString("AMOUNT")!=null?rs.getInt("AMOUNT"):0);
				hpb.setVOUC_TYPE(rs.getString("VOUC_TYPE")!=null?rs.getString("VOUC_TYPE"):"");
				hplist.add(hpb);
			}
			conn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return hplist;
	}
	
	public ArrayList<AutopostBean> getAutoPostList(int branch)
	{
		ArrayList<AutopostBean> list = new ArrayList<AutopostBean>();
		Connection con = null;
		try
		{
			con = ConnectionManager.getConnection();
			Statement st = con.createStatement();
			String sql=null;
			if(branch==0)
			{
				sql = "SELECT * FROM AUTOPOST ORDER BY 1,2,3,4";
			}
			else
			{
				sql = "SELECT * FROM AUTOPOST WHERE BRCD = "+branch+" ORDER BY 1,2,3,4";
			}
			ResultSet rs = st.executeQuery(sql);
			AutopostBean APB = null;
			while(rs.next())
			{
				APB = new AutopostBean();
				
				APB.setEMPNO(rs.getString("EMPNO")!=null?rs.getInt("EMPNO"):0);
				APB.setBRCD(rs.getString("BRCD")!=null?rs.getInt("BRCD"):0);
				APB.setTRNCD(rs.getString("TRNCD")!=null?rs.getInt("TRNCD"):0);
				APB.setSRNO(rs.getString("SRNO")!=null?rs.getInt("SRNO"):0);
				APB.setSUBSYS_CD(rs.getString("SUBSYS_CD")!=null?rs.getString("SUBSYS_CD"):"");
				APB.setAC_NO(rs.getString("AC_NO")!=null?rs.getInt("AC_NO"):0);
				APB.setAMOUNT(rs.getString("AMOUNT")!=null?rs.getInt("AMOUNT"):0);
				APB.setVOUC_TYPE(rs.getString("VOUC_TYPE")!=null?rs.getString("VOUC_TYPE"):"");
				APB.setINST_NO(rs.getString("INST_NO")!=null?rs.getInt("INST_NO"):0);
				list.add(APB);
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public int[] getBranches()
	{
		int[] list = null;
		Connection con = null;
		int count = 0;
		try
		{
			con = ConnectionManager.getConnection();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT DISTINCT(BRCD) FROM AUTOPOST ORDER BY BRCD");
			if(rs.next())
			{
				rs.last();
				count = rs.getRow();
				rs.beforeFirst();
				list = new int[count];
				int i =0;
				while(rs.next())
				{
					list[i] = rs.getString(1)!=null?rs.getInt(1):0;
					i++;
				}
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
}
