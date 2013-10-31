package generatorServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import timelineGenerator.ParameterGen;
import timelineGenerator.SendTasks;

public class Server extends Thread {

	protected ServerSocket listen;

	public Server() {
		try {
			System.out.println("socket server start...");
			listen = new ServerSocket(ParameterServer.TESTPORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.run();
	}

	public void run() {
		try {
			long start = System.currentTimeMillis();
			CountDownLatch threadSignal = new CountDownLatch(
					ParameterServer.clientNum + 1);
			while(true) {
				Socket client = listen.accept();
				 Connects cc = new Connects(client, threadSignal);
				if (ParameterServer.connectNum == ParameterServer.clientNum) {
					System.out.println("connectNum: "
							+ ParameterServer.connectNum);
					System.out.println("clientNum: "
							+ ParameterServer.clientNum);
					break;
				}
			}
			new Thread(new SendTasks(threadSignal)).start();
			threadSignal.await();
			
			System.out.println("Server end!");
			System.out.println("running time: "
					+ (System.currentTimeMillis() - start) / 1000f + "s");
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
		String inputPath = "/home/yuchengcheng/distributionGen";
		
		new ParameterServer(nodeNum,userNum);
		new ParameterGen(nodeNum,userNum, startTime, endTime, inputPath);
		

		new Server();
	}
}