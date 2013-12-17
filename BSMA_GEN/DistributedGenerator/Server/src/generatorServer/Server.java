package generatorServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import timelineGenerator.GenerateTimeline;
import timelineGenerator.Parameter;
import timelineGenerator.SendTasks;


public class Server extends Thread {

	protected ServerSocket listen;
	public final static int TESTPORT=5005;

	public Server() {
		try {
			System.out.println("socket server start...");
			listen = new ServerSocket(TESTPORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.run();
	}

	public void run() {
		try {
			long start = System.currentTimeMillis();
			CountDownLatch threadSignal = new CountDownLatch(
					Parameter.clientNum + 2);
			while(true) {
				Socket client = listen.accept();
				new Connects(client, threadSignal);
				if (Parameter.connectNum == Parameter.clientNum) {
					break;
				}
			}
			new Thread(new GenerateTimeline(threadSignal)).start();
			new Thread(new SendTasks(threadSignal)).start();
			threadSignal.await();
			
			System.out.println("Server end!");
			System.out.println("running time: "
					+ (System.currentTimeMillis() - start) / 1000f + "s");
			System.out.println("the total number of tweet is "+ Parameter.tweetCount
					+" and the number of retweet is "+ Parameter.retweetCount);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		
		int nodeNum = 3;
		int userNum = 1000;
		String startTime = "2012-01-01 00:00:00";
		String endTime = "2013-01-01 00:00:00";
		String serverPath = "/home/yuchengcheng";
		String clientPath = "/home/yuchengcheng";
		
		
		new Parameter(nodeNum,userNum, startTime, endTime, serverPath, clientPath);
		

		new Server();
	}
}