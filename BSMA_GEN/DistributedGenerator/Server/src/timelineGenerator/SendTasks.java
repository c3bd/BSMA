package timelineGenerator;


import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import object.Tweet;

public class SendTasks implements Runnable{
	private CountDownLatch threadsSignal; 
	public SendTasks(CountDownLatch threadsSignal){
		this.threadsSignal = threadsSignal; 
	}
	@Override
	public void run() {
		Thread.currentThread().setName("SendTasks");
		// TODO Auto-generated method stub
		System.out.println("send tasks start...");
		try {
			while(true){
				Tweet retweet =Parameter.task.getFirstTask();
				if(retweet != null){
					Integer clientID = chooseClient(retweet);
					Parameter.clientConmunication.get(clientID).writeTask(retweet);
				}else{
					for(int cl =0;cl < Parameter.clientNum;cl++){
						Parameter.clientConmunication.get(cl).writeSendTaskEnd();
					}
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		threadsSignal.countDown();
		System.out.println("send task end...");
	}
	
	public Integer chooseClient(Tweet m){
		TreeMap<Integer,Integer> ms = new TreeMap<Integer,Integer>();
		int sum =0;
		for(Integer cl:Parameter.followInfoInClient.keySet()){
			boolean isContainUid = Parameter.followInfoInClient.get(cl).containsKey(m.getUid());
			if(isContainUid && m.getClientID()!=cl){
				Integer size =Parameter.followInfoInClient.get(cl).get(m.getUid()).getFeedSize();
				sum +=size;
				ms.put(sum, cl);
			}
		}
		
		return ms.ceilingEntry(new Random().nextInt(sum)).getValue();
	}
	
}