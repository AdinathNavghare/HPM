package payroll.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import payroll.Model.TranBean;

public class BonusHandler {
	
	
	
	
public ArrayList<TranBean> getBonusList(){
		
		ArrayList<TranBean> list= new ArrayList<TranBean>();
		Connection con = null;
		ResultSet rs = null;
		
		
		String query = "Select * from  bonustran";
		
		con = ConnectionManager.getConnection();
		try
		{
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while(rs.next())
			{
			 TranBean tb = new TranBean();
			 
			 tb.setTRNDT(rs.getString("TRNDT"));
			 tb.setEMPNO(rs.getInt("EMPNO"));
			 tb.setNET_AMT(rs.getFloat("NET_AMT"));
			 tb.setUSRCODE(rs.getString("USRCODE"));
			 tb.setUPDDT(rs.getString("UPDDT"));
			
				
				
				
				list.add(tb);
			}
			rs.close();
			st.close();
			con.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return list;
	}

}
