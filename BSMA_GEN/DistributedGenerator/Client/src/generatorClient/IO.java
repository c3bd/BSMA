package generatorClient;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import object.ClientInitInfo;
import object.SubFollowInfo;
import object.TaskResult;
import object.Tweet;
import object.UserInfo;
import timelineGenerator.Parameter;
import timelineGenerator.Util;

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
			if(infor.length>4){
				String rtMid = infor[4];
				m.setRtMid(rtMid);
			}
			return m;
		}finally{
			readLock.unlock();
		}
		
	}
	
	public void writeTaskResult(TaskResult tr) throws Exception {
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
	
	
	public void readSubFollowInfo() throws Exception{
		readLock.lock();
		try{
			String line = is.readLine();			
			String item[] = line.split("/#");
			
			for(int i=0;i<item.length;i++){
				String its[] = item[i].split(",");
				Integer uid = Integer.valueOf(its[0]);
				Integer feedSize = Integer.valueOf(its[1]);
				List<Integer> followlist = new ArrayList<Integer>();
				for(int j = 2;j<its.length;j++){
					followlist.add(Integer.valueOf(its[j]));
				}
				Parameter.subFollowInfo.put(uid, new SubFollowInfo(feedSize,followlist));
				
			}
		}finally{ 
			readLock.unlock();
		}
		
	}
	
	
	public void readUserInfo() throws IOException, ParseException{
		readLock.lock();
		try{
			String[] infor = is.readLine().split("/#");
			for(int i=0;i<infor.length;i++){
				String[] its = infor[i].split(",");
				Integer uid = Integer.valueOf(its[0]);
				Double iden = Double.valueOf(its[1]);
				Parameter.userInfo.put(uid, new UserInfo(iden));
				Util.initPool(uid);
			}
		}finally{
			readLock.unlock();
		}
	}
	
}