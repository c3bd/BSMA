package generator_RAM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import object.User;

public class test{
    public static void main(String[] args) throws ParseException, IOException {

    	try{
        	BufferedReader infile = new BufferedReader(new FileReader( "/home/yuchengcheng/workspace/TimeLineGenerator/out/data(10000)"));
        	//BufferedWriter outfile = new BufferedWriter(new FileWriter( outputpath));
        	Map<User,Integer> result = new HashMap<User,Integer>();
        	String line = null; 
        	while((line = infile.readLine())!=null ) 
            {
        		String[] linesItem = line.split("\t");
        		if(linesItem.length > 4){
        			int uid = Integer.valueOf(linesItem[2]);
        			long mid = Long.valueOf(linesItem[3]);
        			User retweet = new User(uid,mid);
        			if(result.containsKey(retweet)){
        				result.put(retweet, result.get(retweet)+1);
        			}else{
        				result.put(retweet, 1);
        			}
        		}
        	
            }
        	Iterator outItem0 = result.entrySet().iterator(); 
        	while (outItem0.hasNext()) {

        		Map.Entry entry = (Map.Entry) outItem0.next();
        	//	outfile.write(entry.getKey() + "\t" + entry.getValue());
        	//	outfile.newLine();
        		if((Integer)entry.getValue()>1){
        			System.out.println(entry.getKey() + "\t" + entry.getValue());
        		}
        		
        	}
        	
        	
        	
        	infile.close();
        //	outfile.close();
    		
    	}catch(Exception e){System.out.println(e.toString());}
    	
    	
    }
}