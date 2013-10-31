package object;


public class Task{
	Tweet m;
	Integer ClientID;
	public Task(Tweet m,Integer internal){
		this.m = m;
		this.ClientID = ClientID;
	}

	public Tweet getM() {
		return m;
	}
	public void setM(Tweet m) {
		this.m = m;
	}

	public Integer getClientID() {
		return ClientID;
	}

	public void setClientID(Integer clientID) {
		ClientID = clientID;
	}
	
}