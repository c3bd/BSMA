package timelineGenerator;

import generatorServer.IO;
import object.Task;
import object.Tweet;

public class ReceiveMessage implements Runnable{
	Integer clientID;

	IO io;
	public ReceiveMessage(IO io,Integer clientID){
		this.io =io;
		this.clientID =clientID;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("receiveMessage start...");
		//boolean receiveMessageEnd = false;
			while(true){
				//System.out.println("receiveMessage start...");
				String Handler;
				try {
					Handler = io.readHandler();
					if(Handler!=null){
						if(Handler.equals("NonLocalTask")){
							Tweet m = io.readNonLocalTask();
							ParameterGen.task.add(new Task(m,clientID));
							
						}

						if(Handler.equals("SendReTweetEnd")){
							ParameterGen.addAClientSendRetweetEnd();
							break;
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			//threadsSignal.countDown();
			System.err.println("Receive Message end...");
		
		
	}

}