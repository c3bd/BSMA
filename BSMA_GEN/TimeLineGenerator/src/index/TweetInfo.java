package index;

import java.text.ParseException;

import object.CContent;
import genertor.Util;


public class TweetInfo{
	long mid;
	int uid;
	int rtCount;
	public TweetInfo(long mid,int uid,int rtCount){
		this.mid = mid;
		this.uid = uid;
		this.rtCount = rtCount;
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public int getRtCount() {
		return rtCount;
	}
	public void setRtCount(int rtCount) {
		this.rtCount = rtCount;
	}
	public void addRtCount(int value){
		this.rtCount = rtCount+value;
	}
}