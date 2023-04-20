<%@page import="payroll.Model.EmpOffBean"%>
<%@page import="payroll.DAO.EmpOffHandler"%>
<%@page import="payroll.Model.TranBean"%>
<%@page import="payroll.DAO.ConnectionManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

 <% 
	try{  
		int eno = (Integer)session.getAttribute("EMPNO");
		
		EmpOffHandler eoffhdlr = new EmpOffHandler();
		EmpOffBean eoffbn = new EmpOffBean();
		eoffbn = eoffhdlr.getEmpOfficAddInfo(Integer.toString(eno));
		
		session.setAttribute("prj","");
		
		 String s[] = null;
		 String pname = "";
		 String pid = "";
		 String pcode = "";
		 String city = "";
	   String  totalSites="";
			String sites="";
		 Connection conn = null;
		 conn = ConnectionManager.getConnection();
		 Statement st1 = conn.createStatement(); 
	 ArrayList<TranBean> sitelist= new ArrayList<TranBean>();
	 String query1="select site_id  from ATTENDANCE_SITE_RIGHTS where empno="+eno+" ";
	 System.out.println("empno quey"+ query1);
		 ResultSet rs1=st1.executeQuery("select site_id  from ATTENDANCE_SITE_RIGHTS where empno="+eno+" ");
	
		 while(rs1.next()) 
			{
			 
			  TranBean ab = new TranBean();
				
				ab.setSite_id(rs1.getString("site_id"));
				
				sitelist.add(ab); 
			 
				 System.out.println(" sites are here "+rs1.getString("site_id") );
			}
		 
		 
		 for( TranBean tb : sitelist)
		 {
			 
			 sites=tb.getSite_id();
	
			 totalSites += tb.getSite_id()+",";
		 }
		 
		 if (totalSites.length() > 0 && totalSites.charAt(totalSites.length()-1)==',') {
			 totalSites = totalSites.substring(0, totalSites.length()-1);
		    }
		
		 
		/*  for(int i=0; i<sitelist.size(); i++)
		   {
			 TranBean ab = new TranBean();
			   if(i==(sitelist.size())-1) {
				   sites += ab.getSite_id();
			   } else {
				   sites += ab.getSite_id()+",";
			   }
		   } */
		 
		 System.out.println("siueeeeee" +totalSites);
		 
		 Connection con = null;
		 con = ConnectionManager.getConnectionTech();
		 Statement st = con.createStatement(); 
		 ResultSet rs = st.executeQuery("select Site_ID, Project_Code,Site_City,Site_Name from Project_Sites where Site_isdeleted = 0 and site_id in ("+totalSites+", "+eoffbn.getPrj_srno()+" ) and  Site_Status='Open' order by Site_City ASC ");
		
	     List li = new ArrayList();
	    
			while(rs.next()) 
 			{ 	
				pname = rs.getString("Site_Name");
				pid = rs.getString("Site_ID");
				pcode = rs.getString("Project_Code");
				city = rs.getString("Site_City");
 			    li.add(city+":"+pname+":"+pcode+":"+pid);
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
					String[] whole = str[j].split(":");
					
					
					String ct=whole[0];
					String nm=whole[1];
					String prjcode=whole[2];
					String id=(whole[3]);
					
					
					/* int num = str[j].indexOf(":")+1;
					int num1 = str[j].lastIndexOf(":")+1;
					String substr = str[j].substring(num);
					String substr1 = str[j].substring(num1); */
					if(str[j].toUpperCase().startsWith(query.toUpperCase()) || str[j].toUpperCase().contains(query.toUpperCase())|| nm.toUpperCase().startsWith(query.toUpperCase()) || nm.toUpperCase().contains(query.toUpperCase())|| ct.startsWith(query.toUpperCase())|| ct.toUpperCase().contains(query.toUpperCase()) || prjcode.startsWith(query.toUpperCase()) || id.startsWith(query.toUpperCase()) )
					{
						flag = true;
						out.print(str[j]+"\n");
						session.setAttribute("prj", str[j]);
						if(cnt >= 20)//for list display size
							break;
						cnt++;
								
					}
					
					if(query.equalsIgnoreCase("%"))
					{
						out.print(str[j]+"\n");
						
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