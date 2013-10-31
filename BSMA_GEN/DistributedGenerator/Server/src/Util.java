

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
	public static Map<Integer,List<Integer>> cluster = new HashMap<Integer,List<Integer>>();

	public static void PartitionNetwork(String input,String dir,String fileName) throws IOException{
			BufferedReader followerListFile = new BufferedReader(new FileReader(input+"/"+fileName));

			BufferedReader clusterFile = new BufferedReader(new FileReader(dir+"/cluster100000"));

		//	BufferedWriter clusterSizeOut = new BufferedWriter(new FileWriter(dir+"/cluster"));
			BufferedWriter clusterOut = new BufferedWriter(new FileWriter(dir+"/cluster5"));
			for(int i=0;i<5;i++){
				List<Integer> li= new ArrayList<Integer>();
				cluster.put(i, li);
			}
			
			
			String line;
			int count=0;
			int count2=0;
			int count3=0;
			while((line = clusterFile.readLine()) != null ) 
		    {
				System.out.println("cluster:"+line);
		    	String[] linesItem = line.split("\t");
		    	String iden = linesItem[1];
		    	Integer id = Integer.valueOf(linesItem[0]);
		    	if(iden.equals("97255")){
		    			cluster.get(0).add(id);
		    		
		    	}else if(iden.equals("93576")){
		    		
		    			cluster.get(1).add(id);
		    		
		    	}else if(iden.equals("70553")){
		    		count++;
		    		if(count<30000){
		    			cluster.get(2).add(id);
		    		}else if(count>=30000&& count <43000){
		    			cluster.get(1).add(id);
		    		}else{
		    			cluster.get(0).add(id);
		    		}
		    	}else{
		    		cluster.get(0).add(id);
		    	}
		    }
			clusterFile.close();
			for(Integer cl:cluster.keySet()){
				clusterOut.write(cl+"\t");
				for(Integer ids:cluster.get(cl)){
					clusterOut.write(ids+",");
				}
				clusterOut.newLine();
				
			}

			clusterOut.flush();
			clusterOut.close();
			
			
			
			
		  	TreeMap<Integer,Map<Integer,List<Integer>>> fs = new TreeMap<Integer,Map<Integer,List<Integer>>>();
		  	while((line = followerListFile.readLine()) != null ) 
		    {
		  		System.out.println("followerlist:"+line);
		    		String[] linesItem = line.split("\t");
		    		Integer uid1= Integer.valueOf(linesItem[0]);
		    		Integer uid2= Integer.valueOf(linesItem[1]);
		    		Integer clientIden = belongToClient(uid2);
		    		if(fs.containsKey(uid1)){
		    			Map<Integer,List<Integer>> ms = fs.get(uid1);
		    			if(ms.containsKey(clientIden)){
		    				ms.get(clientIden).add(uid2);
		    			}else{
		    				List<Integer> li = new ArrayList<Integer>();
		    				li.add(uid2);
		    				ms.put(clientIden, li);
		    			}
		    		}else{
		    			Map<Integer,List<Integer>> ms = new HashMap<Integer,List<Integer>>();
		    			List<Integer> li = new ArrayList<Integer>();
	    				li.add(uid2);
	    				ms.put(clientIden, li);
	    				fs.put(uid1, ms);
		    		}
		    		
		    }
		  	followerListFile.close();
		  	
		  	
		  	
		  	BufferedWriter followerlist = new BufferedWriter(new FileWriter(dir+"/followerlist"));
		  	BufferedWriter out = new BufferedWriter(new FileWriter(dir+"/network"));
			BufferedWriter out0 = new BufferedWriter(new FileWriter(dir+"/network0"));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(dir+"/network1"));
			BufferedWriter out2 = new BufferedWriter(new FileWriter(dir+"/network2"));
			BufferedWriter out3 = new BufferedWriter(new FileWriter(dir+"/network3"));
			BufferedWriter out4 = new BufferedWriter(new FileWriter(dir+"/network4"));
		  	for(Integer f:fs.keySet()){

				System.out.println("out:"+f);
		  		Map<Integer,List<Integer>> ms = fs.get(f);
		  		
		  		
		  		out.write(f+"\t");
		  		for(Integer cl:ms.keySet()){
		  			followerlist.write(f+"\t");
		  			followerlist.write(cl+"\t");
		  			
		  			for(Integer u:ms.get(cl)){
		  				followerlist.write(u+",");
	  				}
		  			followerlist.newLine();
		  			
		  			out.write(cl+",");
		  			
		  			if(cl==0){
		  				out0.write(f+"\t");
		  				for(Integer u:ms.get(cl)){
		  					out0.write(u+",");
		  				}
		  				out0.newLine();
		  			}
		  			if(cl==1){
		  				out1.write(f+"\t");
		  				for(Integer u:ms.get(cl)){
		  					out1.write(u+",");
		  				}
		  				out1.newLine();
		  			}
		  			if(cl==2){
		  				out2.write(f+"\t");
		  				for(Integer u:ms.get(cl)){
		  					out2.write(u+",");
		  				}
		  				out2.newLine();
		  			}
		  			if(cl==3){
		  				out3.write(f+"\t");
		  				for(Integer u:ms.get(cl)){
		  					out3.write(u+",");
		  				}
		  				out3.newLine();
		  			}
		  			if(cl==4){
		  				out4.write(f+"\t");
		  				for(Integer u:ms.get(cl)){
		  					out4.write(u+",");
		  				}
		  				out4.newLine();
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
		  	out4.flush();
		  	out4.close();
		  	out0.flush();
		  	out0.close();
		  	followerlist.flush();
		  	followerlist.close();
		  	/**/
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
}