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
		System.out.println("receive message start...");
		// TODO Auto-generated method stub
		
			while(true){
				//System.out.println("receive message start...");
				String Handler="";
				try {
					Handler =Parameter.io.readHandler();
					//System.out.println("Handler="+Handler);
					if(Handler!=null){
//System.out.println(Handler);

						if(Handler.equals("ReTweet")){
							Tweet m = Parameter.io.readReTweet();
							Parameter.tasks.add(m);
						}
						if(Handler.equals("SendTaskEnd")){
							Parameter.ReceiveTaskEnd =true;
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