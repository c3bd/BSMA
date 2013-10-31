package object;

import java.text.ParseException;

import timelineGenerator.Util;


public class Tweet{
	private String mid;
	private Long time;
	private Integer uid;
	
	public Tweet(String mid,Long time,Integer uid){
		this.mid = mid;
		this.time = time;
		this.uid = uid;
	}
	public Tweet(Tweet t){
		this.mid = t.getMid();
		this.time = t.getTime();
		this.uid = t.getUid();
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
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
		String result =mid+","+time+","+uid;
		return result;
	}

	public String toOutString(){
		String result="";
		try {
			result = mid+","+Util.changeTimeToString(time)+","+uid;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}