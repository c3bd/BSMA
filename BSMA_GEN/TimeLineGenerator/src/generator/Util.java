package generator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import object.FContent;

public class Util {
	public static SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Calendar changeTimeToCalendar(String time) throws ParseException{
		Date date = fmt.parse(time);
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		return calendar;
	}
	
	public static long changeTimeToSeconds(String time) throws ParseException{
		return changeTimeToCalendar(time).getTimeInMillis()/1000;
	}
	
	public static String changeTimeToString(Long time) throws ParseException{
		return fmt.format(new Date(time*1000));
	}


}