<%@page import="payroll.DAO.ConnectionManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

 <% 
	try{      
		 String s[] = null;
		 String fname = "";
		 String EMPNO = "";
		 String EMPCODE = "";
		 String LNAME = "";
		 Connection con = null;
		 con = ConnectionManager.getConnection();
		 Statement st = con.createStatement(); 
		 
		 String queryString="select emp.EMPNO as EMPNO,emp.EMPCODE as EMPCODE,FNAME,LNAME from EMPMAST emp inner join EMPTRAN etn on emp.EMPNO = etn.EMPNO where (emp.STATUS='A' OR( emp.STATUS='N' AND emp.DOL>=(select MAX(trndt) from ytdtran))) and etn.SRNO=(SELECT MAX(ET.SRNO) FROM EMPTRAN ET WHERE ET.EMPNO=emp.EMPNO) order by emp.EMPNO";
			System.out.println(queryString);
		 
		 ResultSet rs = st.executeQuery(queryString);
 
		
	     List li = new ArrayList();
	    
			while(rs.next()) 
 			{ 	
				fname = rs.getString("FNAME");
				EMPNO = rs.getString("EMPNO");
				EMPCODE = rs.getString("EMPCODE");
				LNAME = rs.getString("LNAME");
				System.out.println(LNAME);
 			    li.add(fname+"  "+LNAME +":"+EMPCODE+":"+EMPNO);
 			}  
			
			
			String[] str = new String[li.size()];			
			Iterator it = li.iterator();
			
			int i = 0;
			while(it.hasNext())
			{
				String p = (String)it.next();	
				str[i] = p;
				i++;
			}
		
			//jQuery related start		
				String query = (String)request.getParameter("q");
				boolean flag = false;
				int cnt = 1;
				for(int j=0; j<str.length; j++)
				{	
					/*if(str[j].toUpperCase().startsWith(query.toUpperCase()))
					{
						out.print(str[j]+"\n");
						if(cnt>=5)
							break;
						cnt++;
					}*/
					String[] lname = str[j].split("  ");
					int num = str[j].indexOf(":")+1;
					int num1 = str[j].lastIndexOf(":")+1;
					String substr = str[j].substring(num);
					String substr1 = str[j].substring(num1);
					if(str[j].toUpperCase().contains(query.toUpperCase())||str[j].toUpperCase().contains(query.toUpperCase()) || lname[1].toUpperCase().contains(query.toUpperCase()) || lname[1].toUpperCase().contains(query.toUpperCase()) || substr.contains(query.toUpperCase())||substr.toUpperCase().contains(query.toUpperCase()) || substr1.contains(query.toUpperCase()) ||substr1.toUpperCase().contains(query.toUpperCase()))
					{
						flag = true;
						out.print(str[j]+"\n");
						if(cnt >= 20)//for list display size
							break;
						cnt++;
					}
				}
				if(flag == false){
					out.print("Not Found Please Re-Enter");
				}
			//jQuery related end	
		
			
 		rs.close(); 
 		st.close(); 
		con.close();

		} 
		catch(Exception e){ 
 			e.printStackTrace(); 
 		}

//www.java4s.com
 %>