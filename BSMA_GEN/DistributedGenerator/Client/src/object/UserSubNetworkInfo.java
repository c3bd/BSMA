package object;

import java.util.List;


public class UserSubNetworkInfo{
	List<Integer> friends;
	int size;
	List<Integer> clients;
	public UserSubNetworkInfo(){
		
	}
	public UserSubNetworkInfo(List<Integer> friends,int size,List<Integer> clients){
		this.friends = friends;
		this.size = size;
		this.clients = clients;
	}
	public List<Integer> getClients() {
		return clients;
	}
	public void setClients(List<Integer> clients) {
		this.clients = clients;
	}
	public List<Integer> getFriends() {
		return friends;
	}
	public void setFriends(List<Integer> friends) {
		this.friends = friends;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}