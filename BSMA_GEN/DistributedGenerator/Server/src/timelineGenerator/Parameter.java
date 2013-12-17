package timelineGenerator;

import generatorServer.IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import object.ClientInitInfo;
import object.SubFollowInfo;
import object.Task;
import object.TaskResult;
import object.TweetsInClients;

public class Parameter{

	public static int clientNum;
	public static int userNum;
	public static String stratTime;
	public static String endTime;
	public static String serverDir;
	public static String clientDir;
	public static TreeMap<Double,String> CDF_postRate = new TreeMap<Double,String>(); 
	
	public static Random randomUser = new Random();
		
	public static int connectNum=0;
	
	public static List<Integer> clientIdenCand = new ArrayList<Integer>();
	public static ConcurrentHashMap<Integer,IO> clientConmunication = new ConcurrentHashMap<Integer,IO>();
	
	public static Map<Integer,List<Integer>> cluster = new HashMap<Integer,List<Integer>>();
	public static Map<Integer,Map<Integer,SubFollowInfo>> followInfoInClient = new HashMap<Integer,Map<Integer,SubFollowInfo>>();//<cl,<uid,subFollowInfo>>

	public static Map<Integer,Double> userID = new HashMap<Integer,Double>();
	public static Task task = new Task();
	public static TaskResult taskResult = new TaskResult();
	public static TweetsInClients tweets;

	public static List<Integer> SendTweetEndClients = new ArrayList<Integer>();
	public static List<Integer> SendTaskResultEndClients = new ArrayList<Integer>();
	
	public static int cutLength = 500;
	public static BufferedWriter file;
	
	public static int tweetCount = 0;
	public static int retweetCount = 0;
	
	public Parameter(int clientNum,int userNum, String stratTime, String endTime,String serverDir,String clientDir) {

		this.clientNum = clientNum;
		this.userNum = userNum;
		this.stratTime = stratTime;
		this.endTime = endTime;
		this.serverDir = serverDir;
		this.clientDir = clientDir;
		this.tweets =new TweetsInClients(clientNum);
		for(int i=0;i<clientNum;i++){
			clientIdenCand.add(i);
		}
		
		
		try {
			file = new BufferedWriter(new FileWriter(serverDir+"/distributionGen/out/data"));
			BufferedReader clusterFile = new BufferedReader(new FileReader(serverDir+"/distributionGen/networkPartition/"+userNum+"/cluster"));
			BufferedReader followerListFile = new BufferedReader(new FileReader(serverDir+"/distributionGen/networkPartition/"+userNum+"/network"+userNum));
	    	BufferedReader postRateFile = new BufferedReader(new FileReader(serverDir+"/distributionGen/data/userProportyCPD"));
	    	
	    	String line = null;
		  	
		  	while((line = postRateFile.readLine()) != null ) 
            {
        		String[] linesItem = line.split("\t");
        		this.CDF_postRate.put(Double.valueOf(linesItem[2]),linesItem[0]+"\t"+linesItem[1]);
        		
            }
		  	postRateFile.close();
		  	
			
			while((line = clusterFile.readLine()) != null ) 
		    {
		    	String[] linesItem = line.split("\t");
		    	Integer cl = Integer.valueOf(linesItem[0]);
		    	String[] ids =linesItem[1].split(",");
		    	if(!cluster.containsKey(cl)){
		    		List<Integer> li = new ArrayList<Integer>();
		    		cluster.put(cl, li);
		    	}
		    	for(int i =0;i<ids.length;i++){
		    		cluster.get(cl).add(Integer.valueOf(ids[i]));
		    	}
		    	
		    }
			clusterFile.close();
		  	
			Map<Integer,Map<Integer,SubFollowInfo>> followInfo = new HashMap<Integer,Map<Integer,SubFollowInfo>>();
			/*<uid,<cl,followlist>>*/
		  	while((line = followerListFile.readLine()) != null ) 
		    {
		    		String[] linesItem = line.split("\t");
		    		Integer uid1= Integer.valueOf(linesItem[0]);
		    		Integer uid2= Integer.valueOf(linesItem[1]);
		    		Integer clientIden = belongToClient(uid2);
		    		if(followInfo.containsKey(uid1)){
		    			Map<Integer,SubFollowInfo> ms = followInfo.get(uid1);
		    			if(ms.containsKey(clientIden)){
		    				ms.get(clientIden).getFollowlist().add(uid2);
		    			}else{
		    				List<Integer> li = new ArrayList<Integer>();
		    				li.add(uid2);
		    				SubFollowInfo sfi = new SubFollowInfo(0,li);
		    				ms.put(clientIden, sfi);
		    			}
		    		}else{
		    			Map<Integer,SubFollowInfo> ms = new HashMap<Integer,SubFollowInfo>();
		    			List<Integer> li = new ArrayList<Integer>();
	    				li.add(uid2);
	    				SubFollowInfo sfi = new SubFollowInfo(0,li);
	    				ms.put(clientIden, sfi);
	    				followInfo.put(uid1, ms);
		    		}
		    		
		    }
		  	followerListFile.close();
			
			Map<Integer,Map<Integer,Double>> ms = new HashMap<Integer,Map<Integer,Double>>();//<uid,<cl,pRate_sum>>
			
			for(Integer uid:followInfo.keySet()){
				Map<Integer,SubFollowInfo> followlistInClients = followInfo.get(uid);
				double sum = 0;
				for(Integer cl:followlistInClients.keySet()){
					List<Integer> uids = followlistInClients.get(cl).getFollowlist();
					double isum = 0;
					for(int i=0;i<uids.size();i++){
	    				isum += getUserPrate(uids.get(i));
					}
					
					if(!ms.containsKey(uid)){
						ms.put(uid, new HashMap<Integer,Double>());
					}
					ms.get(uid).put(cl, isum);
					sum += isum;
				}
				
				for(Integer cl:followlistInClients.keySet()){
					int size =(int)Math.floor((ms.get(uid).get(cl)/sum)*cutLength);
					followlistInClients.get(cl).setFeedSize(size);
					
					if(!followInfoInClient.containsKey(cl)){
						followInfoInClient.put(cl, new HashMap<Integer,SubFollowInfo>());
					}
					followInfoInClient.get(cl).put(uid, followlistInClients.get(cl));
				}
			}
			
		    	
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static synchronized double getUserProportyIden(Integer uid){
		if(!userID.containsKey(uid)){
			userID.put(uid, randomUser.nextDouble());
		}
		return userID.get(uid);
	}
	
	public static double getUserPrate(Integer uid){
		return Double.valueOf(CDF_postRate.ceilingEntry(getUserProportyIden(uid)).getValue().split("\t")[0]);
	
	}
	
	public static Integer belongToClient(Integer uid){
		Integer result=null;
		for(Integer cl:cluster.keySet()){
			if(cluster.get(cl).contains(uid)){
				return result = cl;
			}
		}
		return result;
		
	}
	public static synchronized void addConnectNum(){
		connectNum++;
	}
	public static synchronized Integer getAclientIden(){
		return clientIdenCand.remove(0);
	}
	
	public static ClientInitInfo getClientInitInfo(Integer clientIden){
		return new ClientInitInfo(Parameter.stratTime,Parameter.endTime, Parameter.clientDir,clientIden,userNum);
	}
}