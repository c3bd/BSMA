package object;

import java.util.List;

public class SubFollowInfo{
	
	List<Integer> followlist;
	int feedSize;
	public SubFollowInfo(int feedSize, List<Integer> followlist){
		this.followlist = followlist;
		this.feedSize = feedSize;
	}
	
	public List<Integer> getFollowlist() {
		return followlist;
	}
	public void setFollowlist(List<Integer> followlist) {
		this.followlist = followlist;
	}
	public int getFeedSize() {
		return feedSize;
	}
	public void setFeedSize(int feedSize) {
		this.feedSize = feedSize;
	}
	
	@Override
	public String toString() {
		String result = String.valueOf(feedSize);
		for(Integer uid:followlist){
			result+=(","+uid);
		}
		return result;
	}
}