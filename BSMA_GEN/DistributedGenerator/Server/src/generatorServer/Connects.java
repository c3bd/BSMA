package generatorServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import object.Pair;
import object.Tweet;
import timelineGenerator.Parameter;

public class Connects extends Thread{
	private CountDownLatch threadsSignal; 
	Socket client;
	BufferedReader is ;
	DataOutputStream os ;

	IO io;
	Integer clientID;
	public Connects(Socket s,CountDownLatch threadsSignal){
		Parameter.addConnectNum();
		this.threadsSignal = threadsSignal; 
		this.client = s;
		try{

			is = new BufferedReader(new InputStreamReader(client.getInputStream()));
	
			os = new DataOutputStream(client.getOutputStream());
			io = new IO(os,is);
		}catch (IOException e){
			try{
				client.close();
			}catch(IOException e1){
				System.out.println("Error getting socket streams:"+e1);
			}
			e.printStackTrace();
		}
		this.start();
	}
	
	public void run(){
		try{	
			this.clientID = Parameter.getAclientIden();
			Parameter.clientConmunication.put(clientID,io);
			
			io.writeClientInitInfo(Parameter.getClientInitInfo(clientID));
			io.writeUserInfo(clientID);
			io.writeSubFollowInfo(clientID);
			
			receiveMessage();
			threadsSignal.countDown();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void receiveMessage(){
		boolean isSendTweetsEnd = false;
		boolean isSendTaskResultEnd = false;
		while(true){
			String Handler;
			try {
				Handler = io.readHandler();
				if(Handler!=null){
//System.out.println("handler="+Handler);
					if(Handler.equals("Tweet")){
						
						
						Tweet m = io.readTweet();
						m.setClientID(clientID);
						//String rtmid =m.getRtMid();
						if(m.getIsRetweet()==1 && m.getRtMid() == null){
							Parameter.task.addTask(m);
						}
						Parameter.tweets.addTweet( m);
					}
					
					if(Handler.equals("TaskResult")){
						Pair tr = io.readTaskResult();
						Parameter.taskResult.addResult(tr);	
					}

					if(Handler.equals("SendTweetsEnd")){
						Parameter.SendTweetEndClients.add(clientID);
						Parameter.task.trySingalTask();
						isSendTweetsEnd = true;
					}

					if(Handler.equals("SendTaskResultEnd")){
						Parameter.SendTaskResultEndClients.add(clientID);
						isSendTaskResultEnd = true;
					}

					if(isSendTweetsEnd && isSendTaskResultEnd){
						break;
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	

}