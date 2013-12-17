package generatorClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

import timelineGenerator.Parameter;
import timelineGenerator.ReceiveInfo;
import timelineGenerator.ReceiveMessage;
import timelineGenerator.SendTaskResult;
import timelineGenerator.SendTweets;

public class client{
	public final static int REMOTE_PORT = 5005;
	public final static String IP = "10.11.1.208";
	
	public static void main(String argv[]) throws Exception{		
		Socket client=null;
		BufferedReader is = null;
		DataOutputStream os = null;
		try{

			long start = System.currentTimeMillis();
			client = new Socket(IP,REMOTE_PORT);
			is = new BufferedReader(new InputStreamReader(client.getInputStream()));
			os = new DataOutputStream(client.getOutputStream());
			
			new Parameter(is,os);
			
			new ReceiveInfo().receive();
			
			CountDownLatch threadSignal = new CountDownLatch(3);
			new Thread(new SendTweets(threadSignal)).start();
			new Thread(new ReceiveMessage(threadSignal)).start();
			new Thread(new SendTaskResult(threadSignal)).start();
			
			threadSignal.await();
			
			System.out.println("running time: "
					+ (System.currentTimeMillis() - start) / 1000f + "s");

			System.out.println("clientID= "+Parameter.clientID+" Send Retweet end... tweet count ="
						+Parameter.postCount+" retweet count ="+Parameter.retweetCount);
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