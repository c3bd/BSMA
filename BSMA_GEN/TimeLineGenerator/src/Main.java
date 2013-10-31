import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;




public class Main{
	
	 public static void main(String[] args) throws ParseException, IOException {
		 long a=System.currentTimeMillis();
		 TimeLine tl = new TimeLine("2012-03-01 00:00:00","2013-3-01 00:00:00",1,"E://workspace/TimeLineGenerator/out/timeline(100)");
		 
		 
		 tl.generateData(); 
		 System.out.println("\r<br>Ö´ÐÐºÄÊ± : "+(System.currentTimeMillis()-a)/1000f+" Ãë ");
		 
	
		 
	 } 
}