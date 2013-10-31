package generator_RAM;

import java.util.Random;

public class Main{
    public static void main(String[] args) throws Exception {
    	
    	long start =Runtime.getRuntime().freeMemory();
//    	System.out.println("maxMemory="+start);
    	String startTime = "2012-01-01 00:00:00";
    	String endTime = "2013-01-01 00:00:00";
    	int userNum = 100000;
    	String outPath = "/home/yuchengcheng/workspace/TimeLineGenerator/out/data(100000)minite";
    	String btreePath = "/home/yuchengcheng/workspace/TimeLineGenerator/index";
    	
    	
    	
    	TimeLineGenerator tlg = new TimeLineGenerator(startTime,endTime,userNum,outPath,btreePath); 
    	
    	long a=System.currentTimeMillis();
    	tlg.generateData();
    	
    	System.out.println("\r<br>��ʱ : "+(System.currentTimeMillis()-a)/1000f+" �� ");
    	long end =Runtime.getRuntime().freeMemory();
    	System.out.println("runing time ="+Parameter.runningTime);
    	System.out.println("for time ="+Parameter.forTime);
    	System.out.println("ʹ���ڴ�"+(end-start));
    	System.out.println("runing time ="+Parameter.runningTime);
    }
}