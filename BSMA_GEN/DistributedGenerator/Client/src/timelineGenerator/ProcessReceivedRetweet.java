package timelineGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import object.Tweet;
import object.ComparatorByTimeReverseOrder;

public class ProcessReceivedRetweet implements Runnable{
	CountDownLatch threadSignal;
	public ProcessReceivedRetweet(CountDownLatch threadSignal){
		this.threadSignal= threadSignal;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(" send taskResult start...");
		while(true){
			//System.out.println(" send taskResult start...");
			try {
				if(!Parameter.tasks.isEmpty()){
					//System.out.println("get a tasks");
					while(true){
						if((Parameter.pool.followingPool.isEmpty() && 
								Parameter.pool.candidatePool.isEmpty()) 
								||(!Parameter.pool.followingPool.isEmpty() && 
										Parameter.tasks.first().getTime() >Parameter.pool.getFirstFollowingTime())){
							
							java.lang.Thread.currentThread().sleep(1);
							System.out.println("wait 1...");
							
						}else{
							break;
						}
					}
					
					Tweet m =Parameter.tasks.pollFirst();
					//System.out.println("Task: "+m.toOutString());
					Util.getRtTweet(m);
					
				}
				if(Parameter.tasks.isEmpty() && Parameter.ReceiveTaskEnd){
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println("process received:"+e);
			}
		}
		
		
		threadSignal.countDown();
		System.out.println(" send taskResult end...");
	}
	
}