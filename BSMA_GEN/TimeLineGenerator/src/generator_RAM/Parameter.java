package generator_RAM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import object.FollowShip;

public class Parameter{
	public static long windowSize = (long)48*60*60;
	
	public static List<Double> hour = new ArrayList<Double>();//the post count in hour i/the post count in hour 0   i=0,23
	public static List<Double> day = new ArrayList<Double>();//the post count in day i of week/the post average count for each day of week  i=0,6
	public static TreeMap<Double,String> CDF_postRate = new TreeMap<Double,String>(); //Sum of (tweetCount, retweetCount) VS (tweetCount, retweetCount)
	public static TreeMap<Integer,Double> internal = new TreeMap<Integer,Double>();	
	public static TreeMap<Double,Integer> CPD_internal = new TreeMap<Double,Integer>();	
	public static Map<Integer,List<Integer>> followerList  = new HashMap<Integer,List<Integer>>();

	public static double maxFactor ;
	public static int maxTimeGap ;

	public static List<Double> userKeys = new ArrayList<Double>();
	public static List<Random[]> userRandoms = new ArrayList<Random[]>();
	
	public static Random retweetTypeRandom = new Random();
	public static long runningTime=0;
	public static long forTime=0;
	
	public static int cutLength = 500;

	public static long startTime ;
	public static long endTime ;
	public static Integer userNum ;
	
	public Parameter(String startTime,String endTime,int userNum) throws ParseException{
		this.startTime = Util.changeTimeToSeconds(startTime);
		this.endTime = Util.changeTimeToSeconds(endTime);
		this.userNum = userNum;
    	String line = null; 
		try{
			BufferedReader hourFile = new BufferedReader(new FileReader("/home/yuchengcheng/workspace/TimeLineGenerator/data/hour"));
        	BufferedReader dayFile = new BufferedReader(new FileReader("/home/yuchengcheng/workspace/TimeLineGenerator/data/day"));
        	BufferedReader postRateFile = new BufferedReader(new FileReader("/home/yuchengcheng/workspace/TimeLineGenerator/data/userProportyCPD"));
        	BufferedReader internalCDFFile = new BufferedReader(new FileReader("/home/yuchengcheng/workspace/TimeLineGenerator/data/retweetInternalCPD_hour"));
        	BufferedReader followerListFile = new BufferedReader(new FileReader("/home/yuchengcheng/workspace/TimeLineGenerator/data/network100000"));
      	 	       	 	
        	double maxHour = 0;
        	while((line = hourFile.readLine()) != null ) 
            {
        		String[] linesItem = line.split("\t");
        		double ihour =Double.valueOf(linesItem[1]);
        		this.hour.add(ihour);
        		if(ihour>maxHour){
        			maxHour=ihour;
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

        	while((line = internalCDFFile.readLine()) != null ) 
              {
          		String[] linesItem = line.split("\t");
          		this.internal.put(Integer.valueOf(linesItem[1]),Double.valueOf(linesItem[0]));
          		this.CPD_internal.put(Double.valueOf(linesItem[0]),Integer.valueOf(linesItem[1]));
              }
        	internalCDFFile.close();

        	while((line = followerListFile.readLine()) != null ) 
              {
          		String[] linesItem = line.split("\t");
          		Integer uid1= Integer.valueOf(linesItem[0]);
          		Integer uid2= Integer.valueOf(linesItem[1]);
          		if(followerList.containsKey(uid1)){
          			followerList.get(uid1).add(uid2);
          		}else{
          			List<Integer> ls = new ArrayList<Integer>();
          			ls.add(uid2);
          			followerList.put(uid1, ls);
          		}
          		
          		
              }
        	followerListFile.close();
   


    		
    		this.maxFactor = maxHour * maxDay;
		}
    	catch(Exception e){System.out.println(line+" wrong!"+e.toString());}
	}
	
}