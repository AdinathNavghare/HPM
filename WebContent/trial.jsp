<%@page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="org.apache.taglibs.datagrid.Row"%>

<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook.*"%>    
<%@ page import="org.apache.poi.hssf.util.*"%>    
<%@ page import="java.io.*,java.util.*;" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
























/* /* /* 






//Blank workbook
HSSFWorkbook workbook = new HSSFWorkbook();
 
//Create a blank sheet
HSSFSheet sheet = workbook.createSheet("Employee Data");
  
//This data needs to be written (Object[])
Map<String, Object[]> data = new TreeMap<String, Object[]>();
data.put("1", new Object[] {"ID", "NAME", "LASTNAME"});
data.put("2", new Object[] {1, "Amit", "Shukla"});
data.put("3", new Object[] {2, "Lokesh", "Gupta"});
data.put("4", new Object[] {3, "John", "Adwards"});
data.put("5", new Object[] {4, "Brian", "Schultz"});
  
//Iterate over data and write to sheet
Set<String> keyset = data.keySet();
int rownum = 5;
for (String key : keyset)
{
    HSSFRow row = sheet.createRow(rownum++);
    Object [] objArr = data.get(key);
    int cellnum = 0;
    for (Object obj : objArr)
    {
       HSSFCell cell = row.createCell((short)cellnum++);
       if(obj instanceof String)
            cell.setCellValue((String)obj);
        else if(obj instanceof Integer)
            cell.setCellValue((Integer)obj);
    }
}
try
{
    //Write the workbook in file system
    String filepath="";
    filepath=getServletContext().getRealPath("")+ File.separator + "howtodoinjava_demo.xls";
    System.out.println(filepath);
    FileOutputStream out1 = new FileOutputStream(new File(filepath));
    workbook.write(out1);
    out1.close();
    System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
}
catch (Exception e)
{
    e.printStackTrace();
} */ 
%>
</body>
</html>