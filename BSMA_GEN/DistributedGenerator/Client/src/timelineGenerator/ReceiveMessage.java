package timelineGenerator;

import java.util.concurrent.CountDownLatch;

import object.Tweet;

public class ReceiveMessage implements Runnable{
	CountDownLatch threadSignal;
	public ReceiveMessage(CountDownLatch threadSignal){
		this.threadSignal = threadSignal;
	}
	@Override
	public void run() {
		Thread.currentThread().setName("receiveMessage");
		System.out.println("receive message start...");
		// TODO Auto-generated method stub
			while(true){
				//System.out.println("receive message running...");
				String Handler="";
				try {
					Handler =Parameter.io.readHandler();
					
					if(Handler!=null){				
						if(Handler.equals("Task")){
							Tweet m = Parameter.io.readTweet();
							Parameter.task.addTask(m);
						}
						if(Handler.equals("SendTaskEnd")){
							Parameter.ReceiveTaskEnd =true;
							Parameter.task.trySingalTaskEmpty();
							break;
						}
						
					}
			
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.err.println("receive message:"+e);
				}
			}	
			
			
			threadSignal.countDown();
			System.out.println("receive message end...");
		
		
	}
}