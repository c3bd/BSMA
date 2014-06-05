package timelineGenerator;


import java.util.concurrent.CountDownLatch;

import object.TaskResult;
import object.Tweet;

public class SendTaskResult implements Runnable{
	CountDownLatch threadSignal;
	public SendTaskResult(CountDownLatch threadSignal){
		this.threadSignal= threadSignal;
	}
	@Override
	public void run() {
		Thread.currentThread().setName("sendTaskResult");
		// TODO Auto-generated method stub
		System.out.println(" send taskResult start...");
		while(true){
			try {
				Tweet m = Parameter.task.getFirstTask();

				//System.out.println("get a Task :"+m.toOutString());
				if(m != null){
					Tweet retweet = Util.getRtTweet(m);
					String rtmid = "null";
					if(retweet != null){
						rtmid = retweet.getMid();
					}
					TaskResult info = new TaskResult(m.getMid(),rtmid);
					Parameter.io.writeTaskResult(info);
					//System.out.println("send Task Result:"+info.toString());
				}else{
					Parameter.io.writeSendTaskResultEnd();
					break;
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println(Thread.currentThread().getName()+":"+e);
			}
		}
		threadSignal.countDown();
		System.out.println("send taskResult end...");
	}
	
}