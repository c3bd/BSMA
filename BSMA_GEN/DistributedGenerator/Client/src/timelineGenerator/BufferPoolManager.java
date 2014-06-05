package timelineGenerator;

import java.text.ParseException;
import java.util.TreeMap;
import java.util.TreeSet;

import object.FContent;
import object.Tweet;

public class BufferPoolManager{
	TreeSet<FContent> followingPool = new TreeSet<FContent>();
	TreeMap<Integer,TreeMap<Long,String>> candidatePool = new TreeMap<Integer,TreeMap<Long,String>>();//<uid,<Tweet>>
	
	
	public void addTweet(Tweet t){
		addToCandidate(t);
		
	}
	public synchronized void addToCandidate(Tweet t){
		if(candidatePool.containsKey(t.getUid())){
			candidatePool.get(t.getUid()).put(t.getTime(), t.getMid());
			
		}else{
			TreeMap<Long,String> ts = new TreeMap<Long,String>();
			ts.put(t.getTime(), t.getMid());
			candidatePool.put(t.getUid(), ts);
		}
	}	

	
	public Long getFirstFollowingTime(){
		Long time =null;
		if(!followingPool.isEmpty()){
			time = getFirstFollowing().getTime();
		}
		return time;
	}




	public void setFollowingPool(TreeSet<FContent> followingPool) {
		this.followingPool = followingPool;
	}

	public FContent getFirstFollowing(){
		if(!followingPool.isEmpty()){
			return this.followingPool.first();
		}else{
			return null;
		}
		
	}
	
	public TreeMap<Integer, TreeMap<Long, String>> getCandidatePool() {
		return candidatePool;
	}
	public void setCandidatePool(
			TreeMap<Integer, TreeMap<Long, String>> candidatePool) {
		this.candidatePool = candidatePool;
	}
	public FContent removeFirstFollowing(){
		return this.followingPool.pollFirst();
	}
	
	
	public void addTofollowingPool(FContent t) throws ParseException{
		followingPool.add(t);
	}


	public TreeSet<FContent> getFollowingPool() {
		return followingPool;
	}



}
