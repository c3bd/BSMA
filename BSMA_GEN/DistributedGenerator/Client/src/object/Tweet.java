package object;

import java.text.ParseException;

import timelineGenerator.Util;


public class Tweet{
	private String mid;

	private Long time;
	private Integer uid;
	private Integer isRetweet;
	private String rtMid = null;

	public Tweet(String mid,Long time,Integer uid){
		this.mid = mid;
		this.time = time;
		this.uid = uid;
	}
	public Tweet(String mid,Long time,Integer uid,Integer isRetweet){
		this.mid = mid;
		this.time = time;
		this.uid = uid;
		this.isRetweet = isRetweet;
	}
	public Tweet(String mid,Long time,Integer uid,Integer isRetweet,String rtMid){
		this.mid = mid;
		this.time = time;
		this.uid = uid;
		this.isRetweet = isRetweet;
		this.rtMid = rtMid;
	}
	public Tweet(Tweet t){
		this.mid = t.getMid();
		this.time = t.getTime();
		this.uid = t.getUid();
		this.isRetweet = t.getIsRetweet();
		this.rtMid = t.getRtMid();
	}

	public Integer getIsRetweet() {
		return isRetweet;
	}
	public void setIsRetweet(Integer isRetweet) {
		this.isRetweet = isRetweet;
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

	public String getRtMid() {
		return rtMid;
	}
	public void setRtMid(String rtMid) {
		this.rtMid = rtMid;
	}
	
	public String toString(){
		String result =mid+","+time+","+uid+","+isRetweet;
		if(isRetweet == 1 && rtMid != null && !rtMid.equals("null")){
			result += ","+rtMid;
		}
		return result;
	}

	public String toOutString(){
		String result="";
		try {
			result = mid+","+Util.changeTimeToString(time)+","+uid;
			if(rtMid != null && !rtMid.equals("null")){
				result += ","+rtMid;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}