package object;

public class ClientInitInfo{
	String dir;
	String startTime;
	String endTime;
	Integer clientID;
	Integer userNum;
	
	public ClientInitInfo(String startTime,String endTime,String dir,int clientID,Integer userNum){
		
		this.dir = dir;
		this.startTime = startTime;
		this.endTime = endTime;
		this.clientID = clientID;
		this.userNum = userNum;
	}

	

	public Integer getUserNum() {
		return userNum;
	}



	public void setUserNum(Integer userNum) {
		this.userNum = userNum;
	}



	public void setClientID(Integer clientID) {
		this.clientID = clientID;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		String result=(startTime + "," + endTime+ "," + dir + "," + clientID + "," +userNum);
		return result;
	}



	public String getDir() {
		return dir;
	}



	public void setDir(String dir) {
		this.dir = dir;
	}



	public Integer getClientID() {
		return clientID;
	}


	
}
