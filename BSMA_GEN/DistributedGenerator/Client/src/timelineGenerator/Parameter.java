package timelineGenerator;

import generatorClient.IO;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import object.ClientInitInfo;
import object.SubFollowInfo;
import object.Task;
import object.UserInfo;

public class Parameter{
	public static List<Double> hour = new ArrayList<Double>();//the post count in hour i/the post count in hour 0   i=0,23
	public static List<Double> day = new ArrayList<Double>();//the post count in day i of week/the post average count for each day of week  i=0,6
	public static TreeMap<Double,String> CDF_postRate = new TreeMap<Double,String>(); //Sum of (tweetCount, retweetCount) VS (tweetCount, retweetCount)
	public static TreeMap<Double,Integer> CPD_internal = new TreeMap<Double,Integer>();
	
	public static Map<Integer,UserInfo> userInfo= new HashMap<Integer,UserInfo>();//<uid,<proporty,feedsize,followlist>>
	public static Map<Integer,SubFollowInfo> subFollowInfo = new HashMap<Integer,SubFollowInfo>();
	
	public static Set<Integer> cluster = new HashSet<Integer>();
	
	public static Task task = new Task();
	public static boolean isWaitProcessTasks = false;
	
	public static ConcurrentHashMap<String,Integer> tweetRtCount = new ConcurrentHashMap<String,Integer>();
	
	public static Integer cut_length = 500;
	public static long postCount =0;
	public static Integer retweetCount =0;
	
	public static IO io;
	public static  BufferPoolManager pool ;

	public static long startTime ;
	public static long endTime ;
	public static String path ;
	public static Integer clientID;
	public static Integer userNum;
	
	public static double maxFactor ;
	
	public static boolean ReceiveTaskEnd =false;
		
	public Parameter(BufferedReader is,DataOutputStream os){
		this.io = new IO(os,is);
		this.pool = new BufferPoolManager();
		try {
			String Handler = io.readHandler();
			if(Handler.equals("ClientInitInfo")){
				ClientInitInfo infor = io.readClientInitInfo();
				this.startTime = Util.changeTimeToSeconds(infor.getStartTime());
				this.endTime = Util.changeTimeToSeconds(infor.getEndTime());
				this.path =infor.getDir();
				this.clientID = infor.getClientID();
				this.userNum= infor.getUserNum();
			}
			
			String line =null;
			BufferedReader dayFile = new BufferedReader(new FileReader(path+"/distributionGen/data/day"));
			BufferedReader hourFile = new BufferedReader(new FileReader(path+"/distributionGen/data/hour"));
        	BufferedReader postRateFile = new BufferedReader(new FileReader(path+"/distributionGen/data/userProportyCPD"));
        	BufferedReader internalCDFFile = new BufferedReader(new FileReader(path+"/distributionGen/data/retweetInternalCPD_hour"));
        	        
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
        		this.CPD_internal.put(Double.valueOf(linesItem[0]),Integer.valueOf(linesItem[1]));
            }
      	internalCDFFile.close();

      	this.maxFactor = maxHour * maxDay;
      	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
	


}