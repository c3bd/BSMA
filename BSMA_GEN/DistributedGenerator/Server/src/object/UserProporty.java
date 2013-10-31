package object;

import java.util.List;
import java.util.Map;

public class UserProporty{
	double userProportyID;
	Map<Integer,Integer> feedSizeInClient;
	public UserProporty(double userProportyID,Map<Integer,Integer> feedSizeInClient){
		this.userProportyID = userProportyID;
		this.feedSizeInClient = feedSizeInClient;
	}
	@Override
	public String toString() {
		String result = String.valueOf(userProportyID);
		for(Integer client:feedSizeInClient.keySet()){
			result+=(","+client+","+feedSizeInClient.get(client));
		}
		return result;
	}
	public double getUserProportyID() {
		return userProportyID;
	}
	public void setUserProportyID(double userProportyID) {
		this.userProportyID = userProportyID;
	}
	public Map<Integer, Integer> getFeedSizeInClient() {
		return feedSizeInClient;
	}
	public void setFeedSizeInClient(Map<Integer, Integer> feedSizeInClient) {
		this.feedSizeInClient = feedSizeInClient;
	}
	
}