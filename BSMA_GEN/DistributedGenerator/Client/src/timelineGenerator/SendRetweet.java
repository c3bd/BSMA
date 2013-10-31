package timelineGenerator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import object.FContent;
import object.Tweet;
import object.ComparatorByTimeReverseOrder;

public class SendRetweet implements Runnable{
	long mid=0;
	CountDownLatch threadSignal;
	public SendRetweet(CountDownLatch threadSignal){
		this.threadSignal = threadSignal;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Send Retweet start...");
		int count =0;
		try {
			System.out.println("Send Retweet start...");
			while(!Parameter.pool.getFollowingPool().isEmpty()){
				//System.out.println("get first following.");
				FContent f = Parameter.pool.removeFirstFollowing();
				//System.out.println(f.getTime()+","+f.getUid());
				Long next = Util.nextTime(f.getTime(),f.getUid());
				if(next !=null){
					Parameter.pool.addTofollowingPool(new FContent(next,f.getUid()));
				}
				Tweet m = new Tweet(getNextMid(),f.getTime(),f.getUid());
				Parameter.pool.addTweet(m);
//	System.out.println(m.toOutString());
				
				Parameter.file.write(m.toOutString());
				
				Parameter.file.newLine();
				if(Util.isRetweet(m)){
					count++;
					List<Integer> clients = Parameter.userAllocationClient.get(m.getUid());
					if(clients.contains(Parameter.clientID)){
						if(clients.size()==1){
							Util.getRtTweet(m);
						}else{
							int size = Parameter.userFeedSize.get(m.getUid());
							double p = size/Parameter.cut_length;
							if(Math.random() <= p){
								Util.getRtTweet(m);
							}else{
								Parameter.io.writeNonLocalTask(m);
							}
						}
					}else{
						Parameter.io.writeNonLocalTask(m);
					}
				}
			}
			Parameter.io.writeSendRetweetEnd();
			Parameter.postCount=mid;
			Parameter.retweetCount = count;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("send retweet:"+e);
		}
		threadSignal.countDown();
		
	}
	

	public String getNextMid(){
		String result = "";
		try {
			result = InetAddress.getLocalHost().getHostName()+(this.mid++);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}


	
	

	
}