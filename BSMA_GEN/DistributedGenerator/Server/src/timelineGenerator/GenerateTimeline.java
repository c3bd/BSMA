package timelineGenerator;

import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import object.ComparatorByTimeOrder;
import object.Tweet;


public class GenerateTimeline implements Runnable{
	private CountDownLatch threadsSignal; 
	
	public GenerateTimeline(CountDownLatch threadsSignal){
		this.threadsSignal = threadsSignal;
	}
	
	public void run(){
		Thread.currentThread().setName("GenerateTimeline");
		try{
			TreeSet<Tweet> following = new TreeSet<Tweet>(new ComparatorByTimeOrder());
			for(int cl = 0;cl<Parameter.clientNum;cl++){
				Tweet first = Parameter.tweets.getFirstTask(cl);
				following.add(first);
			}
			
			while(!following.isEmpty()){
				Tweet first = following.pollFirst();
				Parameter.tweetCount++;
				
				if(first.getIsRetweet()==1 && first.getRtMid() == null){
					String rtMid = Parameter.taskResult.getRtMid(first.getMid());
					if(rtMid!=null && !rtMid.equals("null")){
						Parameter.retweetCount++;
						first.setRtMid(rtMid);
					}
				}
				
				Parameter.file.write(first.toOut());
				Parameter.file.newLine();
				
				int size = Parameter.tweets.size(first.getClientID());
				if(size > 0 || !Parameter.SendTweetEndClients.contains(first.getClientID())){
					Tweet next = Parameter.tweets.getFirstTask(first.getClientID());
					following.add(next);
				}

			}
			Parameter.file.flush();
			Parameter.file.close();
			threadsSignal.countDown();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}