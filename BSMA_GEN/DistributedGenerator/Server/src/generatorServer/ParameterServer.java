package generatorServer;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import object.Client;
import object.ClientInitInfo;
import object.ClientTask;
import timelineGenerator.ParameterGen;

public class ParameterServer{
	public final static int TESTPORT=5003;
	
	public static int userNum;
	
	
	public static int clientNum;
	public static int connectNum=0;
	
	public static List<Integer> clientIdenCand = new ArrayList<Integer>();
	public static ConcurrentHashMap<Integer,IO> clientConmunication = new ConcurrentHashMap<Integer,IO>();
	//public static TreeSet<ClientTask> clientTask = new TreeSet<ClientTask>();
	
	public static synchronized void addConnectNum(){
		connectNum++;
	}
	public static synchronized Integer getAclientIden(){
		return clientIdenCand.remove(0);
	}
	
	public ParameterServer(int clientNum,int userNum){
		this.clientNum = clientNum;
		this.userNum = userNum;
		for(int i=0;i<clientNum;i++){
			clientIdenCand.add(i);
			//clientTask.add(new ClientTask(i,0));
		}
	}


	
	public static ClientInitInfo getClientInitInfo(Integer clientIden){
		return new ClientInitInfo(ParameterGen.stratTime,ParameterGen.endTime, ParameterGen.dir,clientIden,userNum);
	}
	
//	public static int getClientGenUserNum(Integer clientIden){
//		
//		return ParameterGen.cluster.get(clientIden).size();
//		
//	}
	
	
	
	
}