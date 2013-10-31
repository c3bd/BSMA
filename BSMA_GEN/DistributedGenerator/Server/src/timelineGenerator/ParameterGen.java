package timelineGenerator;

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
import java.util.concurrent.ConcurrentSkipListSet;

import object.ComparatorByTimeOrder;
import object.Task;

public class ParameterGen{
	
	
	public static int userNum;
	public static int clientNum;
	public static String stratTime;
	public static String endTime;
	public static String dir;
	
	public static Random randomUser = new Random();
	
	
//	public static Map<Integer,Integer> clientTask = new HashMap<Integer,Integer>();
//	public static Integer CandidateClient = null;
	public static Map<Integer,List<Integer>> cluster = new HashMap<Integer,List<Integer>>();
	public static Map<Integer,Map<Integer,Integer>> followerList = new HashMap<Integer,Map<Integer,Integer>>();
	//<uid,<cl,size>>
	public static Map<Integer,Map<Integer,Integer>> userFeedSizeInClient = new HashMap<Integer,Map<Integer,Integer>>();
	//<cl,<uid,size>>
	public static ConcurrentHashMap<Integer,List<String>> RetweetBelongToClient = new ConcurrentHashMap<Integer,List<String>>();

	public static TreeMap<Double,String> CDF_postRate = new TreeMap<Double,String>(); 
	
	public static Map<Integer,Double> userID = new HashMap<Integer,Double>();
	public static ConcurrentSkipListSet<Task> task = new ConcurrentSkipListSet<Task>(new ComparatorByTimeOrder());
	public static ConcurrentHashMap<String,Integer> tweetRtCount = new ConcurrentHashMap<String,Integer>();
	
	public static int SendRetweetEndClientNum =0;
	
	public static int cutLength=500;
	public static BufferedWriter file;
	

	
	public synchronized static void addAClientSendRetweetEnd(){
		SendRetweetEndClientNum++;
	}
	
	
	public ParameterGen(int clientNum,int userNum, String stratTime, String endTime,String dir) {
		this.clientNum = clientNum;
		this.userNum = userNum;
		this.stratTime = stratTime;
		this.endTime = endTime;
		this.dir = dir;
		try {
			file = new BufferedWriter(new FileWriter(dir+"/out/data"));
			BufferedReader clusterFile = new BufferedReader(new FileReader(dir+"/networkPartition/"+userNum+"/cluster5"));
			BufferedReader followerListFile = new BufferedReader(new FileReader(dir+"/networkPartition/"+userNum+"/followerlist"));
	    	BufferedReader postRateFile = new BufferedReader(new FileReader(dir+"/resource/userProportyCPD"));
	    	
	    	String line = null;
		  	
		  	while((line = postRateFile.readLine()) != null ) 
            {
        		String[] linesItem = line.split("\t");
        		this.CDF_postRate.put(Double.valueOf(linesItem[2]),linesItem[0]+"\t"+linesItem[1]);
        		
            }
		  	postRateFile.close();
		  	
			
			while((line = clusterFile.readLine()) != null ) 
		    {
				//System.out.println("clusterFile:"+line);
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
		  	
			
			Map<Integer,Map<Integer,Double>> ms = new HashMap<Integer,Map<Integer,Double>>();
		
			while((line = followerListFile.readLine()) != null ) 
		    {
				System.out.println("followerListFile:"+line);
				String[] linesItem = line.split("\t");
				Integer uid1= Integer.valueOf(linesItem[0]);
				Integer cl= Integer.valueOf(linesItem[1]);
				String[] uids= linesItem[2].split(",");
				double isum = 0;
				for(int i=0;i<uids.length;i++){
    				isum+=getUserPrate(Integer.valueOf(uids[i]));
				}
				if(ms.containsKey(uid1)){
					ms.get(uid1).put(cl, isum);
				}else{
					Map<Integer,Double> ims = new HashMap<Integer,Double>();
					ims.put(cl, isum);
					ms.put(uid1, ims);
				}
		    }
			followerListFile.close();
			
			
			for(Integer uid:ms.keySet()){
				double sum =0;
				Map<Integer,Integer> clients= new HashMap<Integer,Integer>();
				for(Integer cl:ms.get(uid).keySet()){
					sum+=ms.get(uid).get(cl);
				}
				for(Integer cl:ms.get(uid).keySet()){
					int size =(int)Math.floor((ms.get(uid).get(cl)/sum)*cutLength);
					clients.put(cl, size);
    				if(userFeedSizeInClient.containsKey(cl)){
    					userFeedSizeInClient.get(cl).put(uid, size);
    				}else{
    	    			Map<Integer,Integer> userFeedSize= new HashMap<Integer,Integer>();
        				userFeedSize.put(uid, size);
        				userFeedSizeInClient.put(cl, userFeedSize);
    				}
				}
				followerList.put(uid, clients);
			}
			
			
			
		/*	
		  	Map<Integer,Map<Integer,List<Integer>>> fs = new HashMap<Integer,Map<Integer,List<Integer>>>();
		  	while((line = followerListFile.readLine()) != null ) 
		    {
		  		System.out.println("followerListFile:"+line);
		    		String[] linesItem = line.split("\t");
		    		Integer uid1= Integer.valueOf(linesItem[0]);
		    		Integer uid2= Integer.valueOf(linesItem[1]);
		    		Integer clientID =belongToClient(uid2);
		    		if(fs.containsKey(uid1)){
		    			Map<Integer,List<Integer>> cls =fs.get(uid1);
		    			if(cls.containsKey(clientID)){
		    				cls.get(clientID).add(uid2);
		    			}else{
		    				List<Integer> uids = new ArrayList<Integer>();
		    				uids.add(uid2);
		    				cls.put(clientID, uids);
		    			}
		    		}else{
		    			Map<Integer,List<Integer>> cls = new HashMap<Integer,List<Integer>>();
		    			List<Integer> uids = new ArrayList<Integer>();
	    				uids.add(uid2);
	    				cls.put(clientID, uids);
	    				fs.put(uid1, cls);
		    		}
		    }
		  	followerListFile.close();
		  	
		  	
		  	for(Integer u:fs.keySet()){
		  		System.out.println("userFeedSizeInClient:"+u);
    			Map<Integer,List<Integer>> cls = fs.get(u);
    			double top_sum =0;
    			Map<Integer,Double> clr= new HashMap<Integer,Double>();
    			for(Integer cl:cls.keySet()){
    				List<Integer> li = cls.get(cl);
    				double sum =0;
    				for(Integer us:li){
    					sum+=getUserPrate(us);
    				}
    				clr.put(cl, sum);
    				top_sum+=sum;
    			}
    			Map<Integer,Integer> clients= new HashMap<Integer,Integer>();
    			for(Integer cl:cls.keySet()){
    				int size =(int)Math.floor((clr.get(cl)/top_sum)*cutLength);
    				clients.put(cl, size);
    				if(userFeedSizeInClient.containsKey(cl)){
    					userFeedSizeInClient.get(cl).put(u, size);
    				}else{
    	    			Map<Integer,Integer> userFeedSize= new HashMap<Integer,Integer>();
        				userFeedSize.put(u, size);
        				userFeedSizeInClient.put(cl, userFeedSize);
    				}
    			}
    			followerList.put(u, clients);
    		}		
		  */  		
		    	
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
	
//	public static Integer belongToClient(Integer uid){
//		Integer result=null;
//		for(Integer cl:cluster.keySet()){
//			if(cluster.get(cl).contains(uid)){
//				return result = cl;
//			}
//		}
//		return result;
//		
//	}
	
	
}