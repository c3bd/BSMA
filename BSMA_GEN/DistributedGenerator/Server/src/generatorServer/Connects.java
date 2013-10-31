package generatorServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import object.Client;
import timelineGenerator.ReceiveMessage;
import timelineGenerator.SendTasks;
import timelineGenerator.SendUserProporty;


public class Connects extends Thread{
	private CountDownLatch threadsSignal; 
	Socket client;
	BufferedReader is ;
	DataOutputStream os ;

	IO io;
	public Connects(Socket s,CountDownLatch threadsSignal){
		ParameterServer.addConnectNum();
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
			Integer clientID = ParameterServer.getAclientIden();
			ParameterServer.clientConmunication.put(clientID,io);
			io.writeClientInitInfo(ParameterServer.getClientInitInfo(clientID));
System.out.println("send: ClientInitInfo ");
			
			new SendUserProporty(io,clientID).send();
			
			new Thread(new ReceiveMessage(io,clientID)).start();
			
			
			threadsSignal.countDown();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	

}