package object;

public class FollowShip{
	Integer uid;
	Integer fUid;
	public FollowShip(Integer uid,Integer fUid){
		this.uid = uid;
		this.fUid = fUid;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getfUid() {
		return fUid;
	}
	public void setfUid(Integer fUid) {
		this.fUid = fUid;
	}
}