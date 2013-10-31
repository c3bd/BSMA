package generatorClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timelineGenerator.Parameter;
import timelineGenerator.ReceiveMessage;
import timelineGenerator.ReceiveUserProporty;
import timelineGenerator.SendRetweet;
import timelineGenerator.ProcessReceivedRetweet;

public class client{
	public final static int REMOTE_PORT=5003;
	
	public static void main(String argv[]) throws Exception{
		Socket client=null;
		BufferedReader is = null;
		DataOutputStream os = null;
		try{
			client = new Socket("10.11.1.209",REMOTE_PORT);
			is = new BufferedReader(new InputStreamReader(client.getInputStream()));
			os = new DataOutputStream(client.getOutputStream());
			
			new Parameter(is,os);
			
			long start = System.currentTimeMillis();
			new ReceiveUserProporty().receive();
			
			CountDownLatch threadSignal = new CountDownLatch(3);
			new Thread(new SendRetweet(threadSignal)).start();
			new Thread(new ProcessReceivedRetweet(threadSignal)).start();
			new Thread(new ReceiveMessage(threadSignal)).start();
			
			threadSignal.await();
			try {
				Parameter.file.flush();
				Parameter.file.close();
				Parameter.rtfile.flush();
				Parameter.rtfile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("clientID= "+Parameter.clientID+" Send Retweet end... tweet count ="+Parameter.postCount+" retweet count ="+Parameter.retweetCount);
			System.out.println("running time: "
					+ (System.currentTimeMillis() - start) / 1000f + "s");
			is.close();
			os.close();
			client.close();
			
		}catch(UnknownHostException el){
			System.out.println("Unknown Host:"+el);
		}catch(IOException el){
			System.out.println("Error io:"+el);
		}
	}
}