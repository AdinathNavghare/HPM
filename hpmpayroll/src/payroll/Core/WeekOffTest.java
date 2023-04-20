package payroll.Core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WeekOffTest {
   public static void main (String args[]){
	    int month=10;
	    int day=6;
	    int year=2016;
	   Calendar cal = new GregorianCalendar(year, month, 1);
	   SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy");
	   ArrayList<String> list=new ArrayList<String>();
	   for (int i = 0, inc = 1; i <31 && cal.get(Calendar.MONTH) ==(month) ; i+=inc) {

	   if (cal.get(Calendar.DAY_OF_WEEK) == day) {
		  list.add(format.format(cal.getTime()).substring(0, 2));
		  cal.add(Calendar.DAY_OF_MONTH, 7);   
	   } else {
		   cal.add(Calendar.DAY_OF_MONTH, 1);
	   }
	   }
   System.out.println(list);
   }
}
