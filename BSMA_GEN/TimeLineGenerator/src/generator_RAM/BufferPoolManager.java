package generator_RAM;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import object.ComparatorByRtCount;
import object.FContent;
import object.Tweet;

public class BufferPoolManager{
	TreeSet<FContent> followingPool = new TreeSet<FContent>();//记录所有用户某在时刻t往后的下一时刻发布的微博基本信息集合
	Map<Long,Tweet> tweets = new HashMap<Long,Tweet>();
	TreeMap<Integer,List<Long>> candidatePool = new TreeMap<Integer,List<Long>>();//<uid,<Tweet>>
	
	TreeSet<Tweet> hotspot = new TreeSet<Tweet>(new ComparatorByRtCount());
	List<Long> tweet24 = new ArrayList<Long>();
	
	public void addTweet(Tweet t){
		tweets.put(t.getMid(), t);
		addToCandidate(t);
		addToHotspot(t);
		
	}
	public void addToHotspot(Tweet t){
		hotspot.add(t);
		tweet24.add(t.getMid());
		if(!tweet24.isEmpty()){
			long timeGap = t.getTime()-tweets.get(tweet24.get(0)).getTime();
			while(timeGap>(24*3600)){
				Long mid = tweet24.remove(0);
				hotspot.remove(tweets.get(mid));
				if(!tweet24.isEmpty()){
					timeGap = t.getTime()-tweets.get(tweet24.get(0)).getTime();
				}else{
					break;
				}
			}
		}
		
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
		int count =t.getRtCount();
		if(hotspot.contains(t)){
			hotspot.remove(t);
			t.setRtCount(t.getRtCount()+1);
			hotspot.add(t);
		}
		int count1 =t.getRtCount();
		tweets.get(t.getMid()).setRtCount(tweets.get(t.getMid()).getRtCount());
		int count2 =tweets.get(t.getMid()).getRtCount();
		
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

	public void setTweets(Map<Long, Tweet> tweets) {
		this.tweets = tweets;
	}



	public TreeMap<Integer, List<Long>> getCandidatePool() {
		return candidatePool;
	}
	public void setCandidatePool(TreeMap<Integer,List<Long>> candidatePool) {
		this.candidatePool = candidatePool;
	}
	public void setFollowingPool(TreeSet<FContent> followingPool) {
		this.followingPool = followingPool;
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
