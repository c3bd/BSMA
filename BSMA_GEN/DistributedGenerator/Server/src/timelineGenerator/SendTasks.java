package timelineGenerator;

import generatorServer.ParameterServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import object.Task;

public class SendTasks implements Runnable{
	private CountDownLatch threadsSignal; 
	public SendTasks(CountDownLatch threadsSignal){
		this.threadsSignal = threadsSignal; 
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("send tasks start...");
		while(true){
			//System.out.println("send tasks start...");
			try {
				if(!ParameterGen.task.isEmpty()){
					Task retweet =ParameterGen.task.pollFirst();
					Integer clientID = chooseClient(retweet);
					ParameterServer.clientConmunication.get(clientID).writeReTweet(retweet.getM());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(ParameterGen.task.isEmpty() &&
					ParameterGen.SendRetweetEndClientNum == ParameterGen.clientNum){
				for(int cl =0;cl<ParameterServer.clientNum;cl++){
					try {
						ParameterServer.clientConmunication.get(cl).writeSendTaskEnd();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				break;
			}	
		}
		threadsSignal.countDown();
		System.out.println("send task end...");
	}
	
	public Integer chooseClient(Task m){
		Map<Integer,Integer> sizeInClient = ParameterGen.followerList.get(m.getM().getUid());
		TreeMap<Integer,Integer> ms = new TreeMap<Integer,Integer>();
		int sum =0;
		
		for(Integer client:sizeInClient.keySet()){
			if(m.getClientID()!=client ){
				Integer size =sizeInClient.get(client);
				sum +=size;
				ms.put(sum, client);
			}
		}
		return ms.ceilingEntry(new Random().nextInt(sum)).getValue();
	}
	
}