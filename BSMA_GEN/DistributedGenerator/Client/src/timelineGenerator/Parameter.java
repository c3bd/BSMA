package timelineGenerator;

import generatorClient.IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import object.ClientInitInfo;
import object.ComparatorByTimeOrder;
import object.Tweet;

public class Parameter{
	public static List<Double> hour = new ArrayList<Double>();//the post count in hour i/the post count in hour 0   i=0,23
	public static List<Double> day = new ArrayList<Double>();//the post count in day i of week/the post average count for each day of week  i=0,6
	public static TreeMap<Double,String> CDF_postRate = new TreeMap<Double,String>(); //Sum of (tweetCount, retweetCount) VS (tweetCount, retweetCount)
	public static TreeMap<Double,Integer> CPD_internal = new TreeMap<Double,Integer>();
	public static Map<Integer,List<Integer>> cluster = new HashMap<Integer,List<Integer>>();
	
	public static Map<Integer,Integer> userFeedSize= new HashMap<Integer,Integer>();
	public static Map<Integer,Double> userProporty = new HashMap<Integer,Double>();
	public static Map<Integer,Random> random_userPost = new HashMap<Integer,Random>();
	public static Map<Integer,Random> random_userRetweet = new HashMap<Integer,Random>();
	public static Map<Integer,List<Integer>> followList = new HashMap<Integer,List<Integer>>();
	public static Map<Integer,List<Integer>> userAllocationClient = new HashMap<Integer,List<Integer>>();
	
	//public static ConcurrentHashMap<String,SubLocalTask> SubSingaltasks = new ConcurrentHashMap<String,SubLocalTask>();
	//public static ConcurrentSkipListSet<String> Singaltasks = new ConcurrentSkipListSet<String>();
	public static ConcurrentSkipListSet<Tweet> tasks = new ConcurrentSkipListSet<Tweet>(new ComparatorByTimeOrder());
	public static ConcurrentHashMap<String,Integer> tweetRtCount = new ConcurrentHashMap<String,Integer>();
	
	public static Integer cut_length = 500;
	public static long postCount =0;
	public static Integer retweetCount =0;
	
	public static IO io;
	
	public static  BufferPoolManager pool ;
	public static  BufferedWriter file;
	public static  BufferedWriter rtfile;

	public static long runningTime=0;
	public static long forTime=0;
	
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
				System.out.println("startTime: "+startTime);
				this.endTime = Util.changeTimeToSeconds(infor.getEndTime());
				System.out.println("endTime: "+endTime);
				this.path =infor.getDir();
				this.clientID = infor.getClientID();
				System.out.println("path: "+path);
				this.userNum= infor.getUserNum();
				this.file = new BufferedWriter(new FileWriter(path+"/out/data"+clientID));
				this.rtfile = new BufferedWriter(new FileWriter(path+"/out/rtdata"+clientID));
				
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String line =null;
		try {
			BufferedReader dayFile = new BufferedReader(new FileReader(path+"/resource/day"));
			BufferedReader hourFile = new BufferedReader(new FileReader(path+"/resource/hour"));
        	BufferedReader postRateFile = new BufferedReader(new FileReader(path+"/resource/userProportyCPD"));
        	BufferedReader internalCDFFile = new BufferedReader(new FileReader(path+"/resource/retweetInternalCPD_hour"));
        	
        	
        	
        	BufferedReader clusterFile = new BufferedReader(new FileReader(path+"/networkPartition/"+userNum+"/cluster5"));
			BufferedReader followerListFile = new BufferedReader(new FileReader(path+"/networkPartition/"+userNum+"/network"+clientID));
        	BufferedReader userAllocationClientFile = new BufferedReader(new FileReader(path+"/networkPartition/"+userNum+"/network"));
        
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
        	while((line = followerListFile.readLine()) != null ) 
    	    {
    	    		String[] linesItem = line.split("\t");
    	    		Integer u= Integer.valueOf(linesItem[0]);
    	    		String[] friendIDs= linesItem[1].split(",");
    	    		List<Integer> friends = new ArrayList<Integer>();
    	    		for(String id:friendIDs){
    	    			friends.add(Integer.valueOf(id));
    	    		}
    	    		followList.put(u, friends);
    	    }
    	  	followerListFile.close();
        	
    	  	while((line = userAllocationClientFile.readLine()) != null ) 
    	    {
    	    		String[] linesItem = line.split("\t");
    	    		Integer u= Integer.valueOf(linesItem[0]);
    	    		String[] clientIDs= linesItem[1].split(",");
    	    		List<Integer> clients = new ArrayList<Integer>();
    	    		for(String id:clientIDs){
    	    			clients.add(Integer.valueOf(id));
    	    		}
    	    		userAllocationClient.put(u, clients);
    	    }
    	  	userAllocationClientFile.close();
    	  	
    	  	while((line = internalCDFFile.readLine()) != null ) 
            {
        		String[] linesItem = line.split("\t");
        		this.CPD_internal.put(Double.valueOf(linesItem[0]),Integer.valueOf(linesItem[1]));
            }
      	internalCDFFile.close();
      	
      	while((line = clusterFile.readLine()) != null ) 
	    {
	    	String[] linesItem = line.split("\t");
	    	Integer cl = Integer.valueOf(linesItem[0]);
	    	if(!cluster.containsKey(cl)){
	    		List<Integer> li = new ArrayList<Integer>();
	    		cluster.put(cl, li);
	    	}
	    	String[] ids = linesItem[1].split(",");
	    	for(int i =0;i<ids.length;i++){
	    		cluster.get(cl).add(Integer.valueOf(ids[i]));
	    	}
	    	
	    }
		clusterFile.close();
      	
      	
			
    		this.maxFactor = maxHour * maxDay;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
	}
	


}