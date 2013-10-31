package object;

public class ClientTask implements Comparable{
	Integer client;
	Integer taskCount;
	public ClientTask(Integer client,Integer taskCount){
		this.client= client;
		this.taskCount = taskCount;
	}
	public Integer getClient() {
		return client;
	}
	public void setClient(Integer client) {
		this.client = client;
	}
	public Integer getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(Integer taskCount) {
		this.taskCount = taskCount;
	}
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		ClientTask ct = (ClientTask) arg0;
		if(ct.taskCount==this.taskCount){
			return this.client.compareTo(ct.client);
		}else{
			return this.taskCount.compareTo(ct.taskCount);
		}
	}
}