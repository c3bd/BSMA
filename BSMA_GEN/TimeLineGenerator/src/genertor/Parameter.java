package genertor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import object.FollowShip;

public class Parameter{
	public static long windowSize = (long)48*60*60;
	
	public static List<Double> hour = new ArrayList<Double>();//the post count in hour i/the post count in hour 0   i=0,23
	public static List<Double> day = new ArrayList<Double>();//the post count in day i of week/the post average count for each day of week  i=0,6
	public static TreeMap<Double,String> CDF_postRate = new TreeMap<Double,String>(); //Sum of (tweetCount, retweetCount) VS (tweetCount, retweetCount)
	public static TreeMap<Integer,Integer> CDF_timeGap = new TreeMap<Integer,Integer>();
	public static TreeMap<Integer,Integer> timeGap = new TreeMap<Integer,Integer>();
	public static Set<FollowShip> followerList  = new HashSet<FollowShip>();
	
	public static double maxFactor ;
	public static int maxTimeGap ;

	public static List<Double> userKeys = new ArrayList<Double>();
	public static List<Random[]> userRandoms = new ArrayList<Random[]>();
	public static Random timeGapRandom = new Random();
	public static Random retweetTypeRandom = new Random();
	
	public static double pRetweetFollower = 0.8;
	public static double pRetweetSelf = 0.05;
	public static double pRetweetOther= 0.15;

	public static long startTime ;
	public static long endTime ;
	public static Integer userNum ;
	
	public Parameter(String startTime,String endTime,int userNum){
		try{
			BufferedReader hourFile = new BufferedReader(new FileReader("E://workspace/TimeLineGenerator/data/hour"));
        	BufferedReader dayFile = new BufferedReader(new FileReader("E://workspace/TimeLineGenerator/data/day"));
        	BufferedReader postRateFile = new BufferedReader(new FileReader("E://workspace/TimeLineGenerator/data/tweetRateCPD"));
        	BufferedReader timeGapFile = new BufferedReader(new FileReader("E://workspace/TimeLineGenerator/data/timegapCPD_minites"));
        	BufferedReader followerListFile = new BufferedReader(new FileReader("E://workspace/TimeLineGenerator/data/followerList"));
			
        	double maxHour = 0;
        	String line = null; 
        	while((line = hourFile.readLine()) != null ) 
            {
        		String[] linesItem = line.split("\t");
        		double ihour =Double.valueOf(linesItem[1]);
        		this.hour.add(ihour);
        		if(ihour > maxHour){
        			maxHour = ihour;
        		}
            }
        	hourFile.close();
        	
        	double maxDay = 0;
        	while((line = dayFile.readLine()) != null ) 
            {
        		String[] linesItem = line.split("\t");
        		double iday =Double.valueOf(linesItem[1]);
        		this.day.add(iday);
        		if(iday > maxDay){
        			maxDay = iday;
        		}
            }
        	dayFile.close();

        	while((line = postRateFile.readLine()) != null ) 
              {
          		String[] linesItem = line.split("\t");
          		this.CDF_postRate.put(Double.valueOf(linesItem[2]),linesItem[0]+"\t"+linesItem[1]);
              }
        	postRateFile.close();

        	while((line = timeGapFile.readLine()) != null ) 
              {
          		String[] linesItem = line.split("\t");
          		this.CDF_timeGap.put(Integer.valueOf(linesItem[0]),Integer.valueOf(linesItem[1]));
          		this.timeGap.put(Integer.valueOf(linesItem[1]),Integer.valueOf(linesItem[0]));
          		maxTimeGap = Integer.valueOf(linesItem[1]);
              }
        	timeGapFile.close();

        	while((line = followerListFile.readLine()) != null ) 
              {
          		String[] linesItem = line.split("\t");
          		followerList.add(new FollowShip(Integer.valueOf(linesItem[0]),Integer.valueOf(linesItem[1])));
              }
        	followerListFile.close();

    		this.startTime = Util.changeTimeToSeconds(startTime);
    		this.endTime = Util.changeTimeToSeconds(endTime);
    		this.userNum = userNum;
    		this.maxFactor = maxHour * maxDay;
		}
    	catch(Exception e){System.out.println("wrong!"+e.toString());}
	}
	
	public static double getRtweetTypeFactor(Integer uid1, Integer uid2){
		if(Parameter.followerList.contains(new FollowShip(uid1,uid2))){
			return Parameter.pRetweetFollower;
		}else if(uid1 == uid2){
			return Parameter.pRetweetSelf;
		}else{
			return Parameter.pRetweetOther;
		}
	}
}