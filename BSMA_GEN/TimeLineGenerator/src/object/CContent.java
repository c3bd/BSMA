package object;

import java.text.ParseException;

import genertor.Util;


public class CContent implements Comparable {
	Integer uid;
	long mid;
	int rtCount;
	
	public CContent(Integer uid, long mid,int rtCount){
		this.uid = uid;
		this.mid = mid;
		this.rtCount = rtCount;
	}
	
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public CContent(){
		
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

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		CContent other = (CContent) arg0;
		int comp = this.rtCount-other.getRtCount();
		if(comp == 0){
			return (int) (this.mid-other.getMid());
		} else {
			return comp < 0? -1 : 1;
		}
	}
}