package genertor;

import index.Btree;
import index.TweetInfo;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import object.FContent;
import object.CContent;
import object.Tweet;

public class BufferPoolManager{
	//TreeMap<FContent,Long> candidatesPool  = new TreeMap<FContent,Long>();//<<time,uid>,mid>
	TreeSet<FContent> followingPool = new TreeSet<FContent>();//记录所有用户某在时刻t往后的下一时刻发布的微博基本信息集合
	TreeMap<Long,Set<CContent>> candidatePool = new TreeMap<Long,Set<CContent>>();//<time,<uid,<mid,rtCount>>>
	
	public void addToCandidatePool(Tweet t){
		if(candidatePool.containsKey(t.getTime())){
			candidatePool.get(t.getTime()).add(new CContent(t.getUid(),t.getMid(),0));
		}else{
			Set<CContent> contents = new HashSet<CContent>();
			contents.add( new CContent(t.getUid(),t.getMid(),0));
			candidatePool.put(t.getTime(), contents);
		}
	}
	
	public Long getFirstCandidateTime(){
		Long time =null;
		if(!candidatePool.isEmpty()){
			time = candidatePool.firstKey();
		}
		return time;
	}
	
	public Long getFirstFollowingTime(){
		Long time =null;
		if(!followingPool.isEmpty()){
			time = getFirstFollowing().getTime();
		}
		return time;
	}

	public void removeFirstCandidateByTime(Btree btree) throws Exception{
		if(!candidatePool.isEmpty()){
			
			long time =candidatePool.firstKey();
			Set<CContent> contents = candidatePool.remove(time);
			for(CContent content:contents){
				TweetInfo t = new TweetInfo(content.getMid(),content.getUid(),content.getRtCount());
				//btree.put(time, t);
			}
		}
	}
	
	public boolean addRtCount(Tweet t,int value){
		CContent content = new CContent(t.getUid(),t.getMid(),t.getRtCount());
		if(candidatePool.containsKey(t.getTime()) && candidatePool.get(t.getTime()).contains(content)){
			candidatePool.get(t.getTime()).remove(content);
			CContent con = new CContent(t.getUid(),t.getMid(),t.getRtCount()+value);
			candidatePool.get(t.getTime()).add(con);
			return true;
		}
		return false;
	}
		


	
	public TreeMap<Long, Set<CContent>> getCandidatePool() {
		return candidatePool;
	}

	public void setCandidatePool(TreeMap<Long, Set<CContent>> candidatePool) {
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
