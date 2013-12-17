package generatorServer;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import object.ClientInitInfo;
import object.Pair;
import object.SubFollowInfo;
import object.Tweet;
import timelineGenerator.Parameter;

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



	public void writeRetweet(Tweet m) throws Exception {
		writeLock.lock();
		try {
			os.writeBytes("Retweet" + "\n");
			os.writeBytes(m.toString() + "\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
		// System.out.println("send ReTweet:"+m.toString());
	}

	public void writeTweet(Tweet m) throws Exception {
		writeLock.lock();
		try {
			os.writeBytes("Tweet" + "\n");
			os.writeBytes(m.toString() + "\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
	}

	public Tweet readTweet() throws Exception {
		readLock.lock();
		try{
			String[] infor = is.readLine().split(",");
			String mid = infor[0];
			long time = Long.valueOf(infor[1]);
			Integer uid = Integer.valueOf(infor[2]);
			Integer isRetweet = Integer.valueOf(infor[3]);
			Tweet m = new Tweet(mid, time, uid,isRetweet);
			if(infor.length > 4){
				String rtMid = infor[4];
				m.setRtMid(rtMid);
			}
			return m;
		}finally{
			readLock.unlock();
		}
		
	}
	
	public void writeTaskResult(Pair tr) throws Exception {
		writeLock.lock();
		try {
			os.writeBytes("TaskResult" + "\n");
			os.writeBytes(tr.toString() + "\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
	}

	
	public void writeSendTweetsEnd() throws Exception {
		writeLock.lock();
		try {
			os.writeBytes("SendTweetsEnd" + "\n");
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

	public Pair readTaskResult() throws Exception {
		// TODO Auto-generated method stub
		readLock.lock();
		try{
			String[] infor = is.readLine().split(",");
			return new Pair(infor[0],infor[1]);
		}finally{
			readLock.unlock();
		}
	}

	public void writeSubFollowInfo(Integer clientID) throws Exception {
		// TODO Auto-generated method stub
		
		writeLock.lock();
		try {
			os.writeBytes("SubFollowInfo"+"\n");
			Map<Integer,SubFollowInfo> subFollowInfo = Parameter.followInfoInClient.get(clientID);
			for(Integer uid:subFollowInfo.keySet()){
				SubFollowInfo sfi = subFollowInfo.get(uid);
				os.writeBytes(uid+",");
				os.writeBytes(sfi.toString());
				os.writeBytes("/#");
			}
			os.writeBytes("\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
	}

	public void writeTask(Tweet m) throws Exception {
		// TODO Auto-generated method stub
		writeLock.lock();
		try {
			os.writeBytes("Task"+"\n");
			os.writeBytes(m.toString()+"\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
	}

	public void writeSendTaskEnd() throws IOException {
		// TODO Auto-generated method stub
		writeLock.lock();
		try {
			os.writeBytes("SendTaskEnd"+"\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
	}

	public void writeUserInfo(Integer clientID) throws IOException {
		// TODO Auto-generated method stub
		writeLock.lock();
		try {
			os.writeBytes("UserInfo"+"\n");
			List<Integer> uids =Parameter.cluster.get(clientID);
			for(Integer uid:uids){
				os.writeBytes(uid+","+Parameter.getUserProportyIden(uid)+"/#");
			}
			os.writeBytes("\n");
			os.flush();
		} finally {
			writeLock.unlock();
		}
	}


}