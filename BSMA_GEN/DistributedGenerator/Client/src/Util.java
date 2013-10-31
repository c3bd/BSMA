

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



public class Util {
	public static void PartitionNetwork(String inputDir,String outputDir,String fileName,int userNum,int clientNum) throws IOException{
		//Map<Integer,Map<Integer,FriendsInfo>> followerList = new HashMap<Integer,Map<Integer,FriendsInfo>>();
		BufferedReader followerListFile = new BufferedReader(new FileReader(inputDir+"/"+fileName));
		BufferedReader clusterFile = new BufferedReader(new FileReader(inputDir+"/cluster.txt"));
		BufferedWriter out = new BufferedWriter(new FileWriter(outputDir+"/network"));
		BufferedWriter out1 = new BufferedWriter(new FileWriter(outputDir+"/network0"));
		BufferedWriter out2 = new BufferedWriter(new FileWriter(outputDir+"/network1"));
		BufferedWriter out3 = new BufferedWriter(new FileWriter(outputDir+"/network2"));
		
		Map<Integer,List<Integer>> cluster = new HashMap<Integer,List<Integer>>();
		String line;
		while((line = clusterFile.readLine()) != null ) 
	    {
	    	String[] linesItem = line.split("\t");
	    	if(linesItem[1].equals("6234") ||linesItem[1].equals("9575")||linesItem[1].equals("9021")){
	    		if(cluster.containsKey(0)){
	    			cluster.get(0).add(Integer.valueOf(linesItem[0]));
	    		}else{
	    			List<Integer> li = new ArrayList<Integer>();
	    			li.add(Integer.valueOf(linesItem[0]));
	    			cluster.put(0, li);
	    		}
	    	}
	    	if(linesItem[1].equals("8916")){
	    		if(cluster.containsKey(1)){
	    			cluster.get(1).add(Integer.valueOf(linesItem[0]));
	    		}else{
	    			List<Integer> li = new ArrayList<Integer>();
	    			li.add(Integer.valueOf(linesItem[0]));
	    			cluster.put(1, li);
	    		}
	    	}
	    	if(linesItem[1].equals("4606")){
	    		if(cluster.containsKey(2)){
	    			cluster.get(2).add(Integer.valueOf(linesItem[0]));
	    		}else{
	    			List<Integer> li = new ArrayList<Integer>();
	    			li.add(Integer.valueOf(linesItem[0]));
	    			cluster.put(2, li);
	    		}
	    	}	
	    }
		clusterFile.close();
		
		
		
	
	  	TreeMap<Integer,TreeMap<Integer,List<Integer>>> fs = new TreeMap<Integer,TreeMap<Integer,List<Integer>>>();
	  	while((line = followerListFile.readLine()) != null ) 
	    {
	    		String[] linesItem = line.split("\t");
	    		Integer uid1= Integer.valueOf(linesItem[0]);
	    		Integer uid2= Integer.valueOf(linesItem[1]);
	    		Integer clientIden = beyongToClient(uid2,cluster);
	    		if(fs.containsKey(uid1)){
	    			TreeMap<Integer,List<Integer>> ms = fs.get(uid1);
	    			if(ms.containsKey(clientIden)){
	    				ms.get(clientIden).add(uid2);
	    			}else{
	    				List<Integer> li = new ArrayList<Integer>();
	    				li.add(uid2);
	    				ms.put(clientIden, li);
	    			}
	    		}else{
	    			TreeMap<Integer,List<Integer>> ms = new TreeMap<Integer,List<Integer>>();
	    			List<Integer> li = new ArrayList<Integer>();
    				li.add(uid2);
    				ms.put(clientIden, li);
    				fs.put(uid1, ms);
	    		}
	    		
	    }
	  	followerListFile.close();
	  	for(Integer f:fs.keySet()){
	  		Map<Integer,List<Integer>> ms = fs.get(f);
	  		out.write(f+"\t");
	  		for(Integer cl:ms.keySet()){
	  			out.write(cl+",");
	  			if(cl==0){
	  				out1.write(f+"\t");
	  				for(Integer u:ms.get(cl)){
	  					out1.write(u+",");
	  				}
	  				out1.newLine();
	  			}
	  			if(cl==1){
	  				out2.write(f+"\t");
	  				for(Integer u:ms.get(cl)){
	  					out2.write(u+",");
	  				}
	  				out2.newLine();
	  			}
	  			if(cl==2){
	  				out3.write(f+"\t");
	  				for(Integer u:ms.get(cl)){
	  					out3.write(u+",");
	  				}
	  				out3.newLine();
	  			}
	  		}
	  		out.newLine();
	  	}
	  	out.flush();
	  	out.close();
	  	out1.flush();
	  	out1.close();
	  	out2.flush();
	  	out2.close();
	  	out3.flush();
	  	out3.close();
	}
	
	public static Integer beyongToClient(Integer uid,Map<Integer,List<Integer>> cluster){
		int result=0;
		for(Integer cl:cluster.keySet()){
			if(cluster.get(cl).contains(uid)){
				result = cl;
			}
		}
		return result;
	}

}