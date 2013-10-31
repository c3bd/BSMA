package genertor;

import java.util.Random;

public class Main{
    public static void main(String[] args) throws Exception {
    	
    	long start =Runtime.getRuntime().freeMemory();
//    	System.out.println("maxMemory="+start);
    	String startTime = "2012-03-01 00:00:00";
    	String endTime = "2013-03-01 00:00:00";
    	int userNum = 10000;
    	String outPath = "E://workspace/TimeLineGenerator/out/generator/data(1000)";
    	String btreePath = "E://workspace/TimeLineGenerator/index";
    	
    	long a=System.currentTimeMillis();
    	
    	TimeLineGenerator tlg = new TimeLineGenerator(startTime,endTime,userNum,outPath,btreePath); 
    	
    	tlg.generateData();
    	
    	System.out.println("\r<br>耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
    	long end =Runtime.getRuntime().freeMemory();
    	System.out.println("使用内存"+(end-start));
    }
}