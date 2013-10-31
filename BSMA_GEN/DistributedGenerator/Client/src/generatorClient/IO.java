package generatorClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import object.ClientInitInfo;
import object.Tweet;

public class IO {
	private Lock writeLock = new ReentrantLock();
	private Lock readLock = new ReentrantLock();

	public DataOutputStream os;
	public BufferedReader is;

	public IO(DataOutputStream os, BufferedReader is) {
		this.is = is;
		this.os = os;
	}

	public String readHandler() throws Exception {
		readLock.lock();
		try{
			return is.readLine();
		}finally{
			readLock.unlock();
		}
	}

	public void writeUserProportyIden(double proporty)
			throws Exception {
		writeLock.lock();
		try {
			os.writeBytes("UserProportyIden" + "\n");
			os.writeBytes(String.valueOf(proporty) + "\n");
			os.flush();
		}finally{
			writeLock.unlock();
		}
	}

	public double readUserProportyIden() throws Exception {
		readLock.lock();
		try{
			return Double.valueOf(is.readLine());
		}finally{
			readLock.unlock();
		}
	}

	public void writeClientInitInfo(ClientInitInfo info)
			throws Exception {
		writeLock.lock();
		try {
			os.writeBytes("ClientInitInfo" + "\n");
			os.writeBytes(info.toString() + "\n");
			os.flush();
		}finally{
			writeLock.unlock();
		}
	}

	public ClientInitInfo readClientInitInfo() throws Exception {
		readLock.lock();
		try{
			String[] infor = is.readLine().split(",");
			String startTime = infor[0];
			String endTime = infor[1];
			String dir = infor[2];
			Integer clientID = Integer.valueOf(infor[3]);
			Integer userNum = Integer.valueOf(infor[4]);
			return new ClientInitInfo(startTime, endTime, dir,clientID,userNum);
				
			
		}finally{
			readLock.unlock();
		}
		

	}

	
	public void writeReTweet(Tweet m) throws Exception {
		writeLock.lock();
		try {
			os.writeBytes("ReTweet" + "\n");
			os.writeBytes(m.toString() + "\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
		// System.out.println("send ReTweet:"+m.toString());
	}

	public Tweet readReTweet() throws Exception {
		readLock.lock();
		try{
			String[] infor = is.readLine().split(",");
			String mid = infor[0];
			long time = Long.valueOf(infor[1]);
			Integer uid = Integer.valueOf(infor[2]);
			Tweet m = new Tweet(mid, time, uid);
			return m;
		}finally{
			readLock.unlock();
		}
		
	}
	
	public void writeNonLocalTask(Tweet m) throws Exception {
		writeLock.lock();
		try {
			os.writeBytes("NonLocalTask" + "\n");
			os.writeBytes(m.toString() + "\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
		// System.out.println("send ReTweet:"+m.toString());
	}

	public Tweet readNonLocalTask() throws Exception {
		readLock.lock();
		try{
			String[] infor = is.readLine().split(",");
			String mid = infor[0];
			long time = Long.valueOf(infor[1]);
			Integer uid = Integer.valueOf(infor[2]);
			Tweet m = new Tweet(mid, time, uid);
			return m;
		}finally{
			readLock.unlock();
		}
		
	}
	
	
	public void writeSendRetweetEnd() throws Exception {
		writeLock.lock();
		System.err.println("writeSendRetweetEnd");
		try {
			os.writeBytes("SendReTweetEnd" + "\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
	}

	public void writeSendTaskResultEnd() throws Exception {
		writeLock.lock();
		try {
			os.writeBytes("SendTaskResultEnd" + "\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
	}
	
	
	public  void writeUserFeedSize( Map<Integer,Integer> userFeedSize) throws Exception{
		writeLock.lock();
		try{
			os.writeBytes("UserFeedSize"+"\n");
			String output="";
			for(Integer uid:userFeedSize.keySet()){
				output+=(uid+","+userFeedSize.get(uid)+",");
			}
			os.writeBytes(output+"\n");
			os.flush();
		}finally{
			writeLock.unlock();
		}
	}
	
	public Map<Integer,Integer> readUserFeedSize() throws Exception{
		readLock.lock();
		try{
			String item[] = is.readLine().split(",");
			Map<Integer,Integer> userFeedSize = new HashMap<Integer,Integer>();
			int index =0;
			System.out.println(item.length);
			for(int i=0;i<item.length/2;i++){
				userFeedSize.put(Integer.valueOf(item[index++]), Integer.valueOf(item[index++]));
			}
			return userFeedSize;
		}finally{
			readLock.unlock();
		}
	}
	
	
}