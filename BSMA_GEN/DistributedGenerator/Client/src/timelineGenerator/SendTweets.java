package timelineGenerator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

import object.FContent;
import object.Tweet;

public class SendTweets implements Runnable{
	long mid=0;
	CountDownLatch threadSignal;
	public SendTweets(CountDownLatch threadSignal){
		this.threadSignal = threadSignal;
	}
	@Override
	public  void run() {
		Thread.currentThread().setName("sendTweets");
		// TODO Auto-generated method stub
		System.out.println("Send Retweet start...");
		int count =0;
		try {
			while(!Parameter.pool.getFollowingPool().isEmpty()){
				FContent f = Parameter.pool.removeFirstFollowing();
				Parameter.task.trySingalTaskCon();
				Long next = Util.nextTime(f.getTime(),f.getUid());
				if(next !=null){
					Parameter.pool.addTofollowingPool(new FContent(next,f.getUid()));
				}
				Tweet m = new Tweet(getNextMid(),f.getTime(),f.getUid());
				Parameter.pool.addTweet(m);
				if(Util.isRetweet(m)){
					m.setIsRetweet(1);
					count++;
					int size = 0;
					if(Parameter.subFollowInfo.containsKey(m.getUid())){
						size = Parameter.subFollowInfo.get(m.getUid()).getFeedSize();
					}
					double p = (double)size/(double)Parameter.cut_length;
					if(Math.random() <= p){
						Tweet retweet = Util.getRtTweet(m);
						if(retweet != null){
							m.setRtMid(retweet.getMid());
						}
					}
					Parameter.io.writeTweet(m);
				}else{
					m.setIsRetweet(0);
					Parameter.io.writeTweet(m);
				}
				
			}
			Parameter.io.writeSendTweetsEnd();
			System.out.println("SendTweetsEnd");
			Parameter.postCount = mid;
			Parameter.retweetCount = count;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
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