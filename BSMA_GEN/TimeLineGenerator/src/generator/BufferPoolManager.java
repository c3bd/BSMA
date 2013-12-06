package generator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import object.FContent;
import object.Tweet;

public class BufferPoolManager{
	TreeSet<FContent> followingPool = new TreeSet<FContent>();
	TreeMap<Long,Tweet> tweets = new TreeMap<Long,Tweet>();//<mid,tweet>
	TreeMap<Integer,List<Long>> candidatePool = new TreeMap<Integer,List<Long>>();//<uid,list<mids>>
	
	
	public void addTweet(Tweet t){
		tweets.put(t.getMid(), t);
		addToCandidate(t);
		
		if(Parameter.isIndex){
			Tweet firstTweet = tweets.firstEntry().getValue();
			long timeGap = t.getTime()-firstTweet.getTime();
			
			while(timeGap > Parameter.windowSize){
				firstTweet = tweets.pollFirstEntry().getValue();
				removeTweetFromCandidate(firstTweet);
				Parameter.btree.put(firstTweet);
				firstTweet = tweets.firstEntry().getValue();
				timeGap = t.getTime()-firstTweet.getTime();
			}
		}
	}
	
	public void removeTweetFromCandidate(Tweet t){
		candidatePool.get(t.getUid()).remove(t.getMid());
	}

	public void addToCandidate(Tweet t){
		if(candidatePool.containsKey(t.getUid())){
			candidatePool.get(t.getUid()).add(t.getMid());
		}else{
			List<Long> ms = new ArrayList<Long>();
			ms.add(t.getMid());
			candidatePool.put(t.getUid(),ms);
		}
	}
	
	public void addOneRtCount(Tweet t){	
		if(tweets.containsKey(t.getMid())){
			tweets.get(t.getMid()).setRtCount(tweets.get(t.getMid()).getRtCount());
		}else if(Parameter.isIndex){
			Parameter.btree.remove(t);
			t.setRtCount(t.getRtCount()+1);
			Parameter.btree.put(t);
		}
		
		
	}
	
	public Long getFirstFollowingTime(){
		Long time =null;
		if(!followingPool.isEmpty()){
			time = getFirstFollowing().getTime();
		}
		return time;
	}

	public Map<Long, Tweet> getTweets() {
		return tweets;
	}

	public TreeMap<Integer, List<Long>> getCandidatePool() {
		return candidatePool;
	}

	public FContent getFirstFollowing(){
		return this.followingPool.first();
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
