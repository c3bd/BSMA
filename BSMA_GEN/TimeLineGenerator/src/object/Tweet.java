package object;

import generator.Util;

import java.text.ParseException;



public class Tweet{
	private Long mid;
	private Long time;
	private Integer uid;
	private Integer rtCount;
	
	public Integer getRtCount() {
		return rtCount;
	}

	public void setRtCount(Integer rtCount) {
		this.rtCount = rtCount;
	}

	public Tweet(Long mid,Long time,Integer uid,Integer rtCount){
		this.mid = mid;
		this.time = time;
		this.uid = uid;
		this.rtCount = rtCount;
	}
	public Tweet(Tweet t){
		this.mid = t.getMid();
		this.time = t.getTime();
		this.uid = t.getUid();
		this.rtCount = t.getRtCount();
	}

	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	
	public String toString(){
		String result ="";
		try {
			result = mid+"\t"+Util.changeTimeToString(time)+" \t"+uid;
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}


}