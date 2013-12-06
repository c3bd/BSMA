package generator;

import java.util.Random;

public class Main{
    public static void main(String[] args) throws Exception {
    	
//    	long start =Runtime.getRuntime().freeMemory();
    	String startTime = "2012-01-01 00:00:00";
    	String endTime = "2013-01-01 00:00:00";
    	int userNum = 1000;
    	String outPath = "E://workspace/TimeLineGenerator/out/";
    	boolean isIndex = false;
    	String btreePath = "E://workspace/TimeLineGenerator/index";
    	int cacheSize = 1024*1024;
    	int windowSize = 48*60*60;
    	
    	TimeLineGenerator tlg = new TimeLineGenerator(startTime,endTime,userNum,outPath,isIndex,btreePath,cacheSize,windowSize); 
    	
    	long a=System.currentTimeMillis();
    	tlg.generateData();
    	
    	System.out.println("\r<br>running time : "+(System.currentTimeMillis()-a)/1000f+" seconds ");
    }
}