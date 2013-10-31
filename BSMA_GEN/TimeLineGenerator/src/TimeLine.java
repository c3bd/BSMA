
import genertor.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class TimeLine{

	long userNum = 0;
	Calendar start = null; 
	Calendar end = null; 
	String outputPath = null;
 	private List<Double> hour = new ArrayList<Double>();//the post count in hour i/the post count in hour 0   i=0,23
 	private List<Double> day = new ArrayList<Double>();//the post count in day i of week/the post average count for each day of week  i=0,6
 	public  TreeMap<Double,String> rate = new TreeMap<Double,String>();//Sum of (tweetCount, retweetCount) VS (tweetCount, retweetCount)
 	public static TreeMap<Integer,String> rate1 = new TreeMap<Integer,String>(); 
 	
	public TimeLine(String strat, String end, long userNum, String outputPath) throws ParseException{
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = fmt.parse(strat);
		Date endDate = fmt.parse(end);
		Calendar sd = Calendar.getInstance(); 
		Calendar ed = Calendar.getInstance(); 
		sd.setTime(startDate);
		ed.setTime(endDate);
		
		this.userNum = userNum;
		this.start = sd;
		this.end = ed;
		this.outputPath = outputPath;
		init();
	}
	
	public void init(){
		try{
			BufferedReader hourFile = new BufferedReader(new FileReader("E://workspace/TimeLineGenerator/data/hour"));
        	BufferedReader dayFile = new BufferedReader(new FileReader("E://workspace/TimeLineGenerator/data/day"));
        	BufferedReader rateFile = new BufferedReader(new FileReader("E://workspace/TimeLineGenerator/data/tweetRateCDF"));
        	BufferedReader rateFile1 = new BufferedReader(new FileReader("E://workspace/TimeLineGenerator/data/retweetJoinDistributionSum"));
        	
        	String line = null; 
        	while((line = hourFile.readLine()) != null ) 
            {
        		String[] linesItem = line.split("\t");
        		hour.add(Double.valueOf(linesItem[1]));
            }
        	hourFile.close();
        	
        	while((line = dayFile.readLine()) != null ) 
            {
        		String[] linesItem = line.split("\t");
        		day.add(Double.valueOf(linesItem[1]));
            }
        	dayFile.close();

        	while((line = rateFile.readLine()) != null ) 
              {
          		String[] linesItem = line.split("\t");
          		rate.put(Double.valueOf(linesItem[2]),linesItem[0]+"\t"+linesItem[1]);
              }
          	rateFile.close();
          	
        	while((line = rateFile1.readLine()) != null ) 
            {
        		String[] linesItem = line.split("\t");
        		rate1.put(Integer.valueOf(linesItem[2]),linesItem[0]+"\t"+linesItem[1]);
            }
        	rateFile1.close();
		}
    	catch(Exception e){System.out.println(e.toString());}
	}
	


	public void generateData() throws IOException, ParseException{
		BufferedWriter file = new BufferedWriter(new FileWriter(outputPath));
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Random r_tweet = new Random();
		boolean flag_rewteet = false; 
		Double P_tweet= 0.0;//be used to computed the rate of post tweet(tweetCount) 
		Double P_retweet = 0.0; // the rate of retweet (retweetCount/tweetCount)
		int mid = 0;
		Random r = new Random(0);
		double x=0.0;
		
		for(int i = 0; i < userNum; i++){
			
			double key = this.rate.ceilingKey(r.nextDouble());
			String[] items = this.rate.get(key).split("\t");
			P_tweet = Double.valueOf(items[0]);
			x+=P_tweet;
			long startTime = start.getTimeInMillis()/1000;
			long endTime = end.getTimeInMillis()/1000;
			Long time = startTime;

			Random r1 = new Random();
			while(time!=null && time<endTime){
				time = nextTime(time,endTime,P_tweet,r1);
				if(time!=null){
					String tweet = (mid++) + "\t" + fmt.format(new Date(time*1000)) + "\t" + i  ;
					file.write(tweet);
					file.newLine();
				}
			}

		}
		file.flush();
		file.close();
		System.out.println("原始数据"+x*365*24*3600+"一共有微博："+mid);

	//	System.out.println(pl.toString());
	}
	
	public Long nextTime(long startTime,long end,double pRate,Random x) throws ParseException{

		double lambda = 1.047616043279569*1.6440474607609101; 
		//long end = Util.changeTimeToSeconds(Parameter.endTime);
		//double rateKey = Parameter.rate.ceilingKey(Parameter.seeds.get(uid));
		//String[] items = Parameter.rate.get(rateKey).split("\t");
		//double pRate = Double.valueOf(items[0]);
		long time =startTime;
		while(time < end){
			double r = x.nextDouble();
			time += (- Math.log(r)/(lambda*pRate));
			if(time <= end ){
				if(Math.random()<(getFactor(time)/lambda)){
					return time;
				}
			}else{
				return null;
			}
		}
		return null;
		
		
		
	}

	public double getFactor(long time ){
		Calendar e=Calendar.getInstance(); 
		e.setTime(new Date(time*1000));
		double h = hour.get(e.get(Calendar.HOUR_OF_DAY));
		double d = day.get(e.get(Calendar.DAY_OF_WEEK)-1);
		
		return  h*d;
	}
	

	public long getUserNum() {
		return userNum;
	}

	public void setUserNum(long userNum) {
		this.userNum = userNum;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}



}