package object;



public class Pair{
	@Override
	public String toString() {
		return mid + "," + rtMid;
	}
	String mid;
	String rtMid;
	public Pair(String mid,String rtMid){
		this.mid = mid;
		this.rtMid = rtMid;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getRtMid() {
		return rtMid;
	}
	public void setRtMid(String rtMid) {
		this.rtMid = rtMid;
	}
}