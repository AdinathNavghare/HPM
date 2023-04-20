<%@page import="java.io.*, java.sql.*,java.util.*,payroll.DAO.ConnectionManager"%>
<html>
<table border="1" id="customers" >
<tr><td colspan="5"><h3>Your Document Attachments Are</h3></td></tr>
<tr><th>Download File</th><th>Document Name</th><th>Document Type</th><th>File Name</th><th>Document Description</th>
<%
try
{
	
final String SAVE_DIR1 = "uploadFiles";
String appPath1 = request.getServletContext().getRealPath("");
//constructs path of the directory to save uploaded file
String savePath1 = appPath1 + File.separator + SAVE_DIR1;

Connection con = ConnectionManager.getConnection();
Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
ResultSet rs = st.executeQuery("select * from ATTACHMENT  where EMPNO="+session.getAttribute("empno"));

%>


<%
if(!rs.next())
{
%>
	<tr><td colspan="5">There Is No Attachment</td></tr>
<%
}

rs.beforeFirst();
while(rs.next())
{
	if(!(rs.getString("filename").equals("")))
	{
	
	%>
	<tr><td><a href="download?act=emp&f=<%=rs.getString("srno")%>">Download File</a></td><td><%=rs.getString("doc_name")==null?"Not Mention":rs.getString("doc_name")%></td><td><%=rs.getString("doc_type")==null?"Not Mention":rs.getString("doc_type")%></td><td><%=rs.getString("filename")==null?"Not Mention":rs.getString("filename")%></td><td><%=rs.getString("doc_desciption")==null?"Not Mention":rs.getString("doc_desciption")%></td></tr>
	<%
	}
}


        
        
%>

     <%
}catch(Exception e)
{
e.printStackTrace();	
}
%>
</table>
</html>