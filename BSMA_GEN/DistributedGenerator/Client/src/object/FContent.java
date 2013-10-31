package object;



public class FContent implements Comparable {
	long time;
	int uid;
	
	public FContent(long time, int uid){
		this.time = time;
		this.uid = uid;
	}
	

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		FContent other = (FContent) arg0;
		if(this.time > other.getTime())
			return 1;
		else if(this.time < other.getTime())
			return -1;
		else 
			return this.uid-other.getUid();
			
		
	}



	public long getTime() {
		return time;
	}



	public void setTime(long time) {
		this.time = time;
	}



	public int getUid() {
		return uid;
	}



	public void setUid(int uid) {
		this.uid = uid;
	}


	
}