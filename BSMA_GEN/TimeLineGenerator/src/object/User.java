package object;

import java.util.Random;
import java.util.TreeMap;

public class User{
	Integer uid;
	long mid;
	
	public User(Integer uid, long mid){
		this.uid = uid;
		this.mid = mid;
	}
	
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}


	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", mid=" + mid + "]";
	}
	
}