package generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import object.ComparatorByTimeReverseOrder;
import object.FContent;
import object.Tweet;

public class TimeLineGenerator {
	
	private long mid = 0;
	private BufferPoolManager pool = new BufferPoolManager();
	private BufferedWriter file;
		
	int poolCount=0;
	int btreeCount =0;
	public TimeLineGenerator(String startTime,String endTime,int userNum, String outPath, boolean isIndex, String btreePath, int cacheSize, int windowSize) throws IOException, ParseException{

		System.out.println("Parameter start...");
		new Parameter(startTime,endTime,userNum,isIndex,btreePath,cacheSize,windowSize);
		System.out.println("Parameter end...");
		this.file = new BufferedWriter(new FileWriter(outPath+"timeline_"+userNum));
	}
	
	
	public void generateData() throws Exception{
		
		initPool();
		updatePool();
	}
	
	public void initPool() throws Exception{

		System.out.println("initPool start...");
		long start = Parameter.startTime;
		Random rr = new Random();
		
		for(int j=0;j<Parameter.userNum;j++){
			Parameter.userKeys.add(rr.nextDouble());
			Random[] r ={new Random(),new Random()};
			Parameter.userRandoms.add(r);

			Long time = nextTime(start,j);
			if(time != null){
				pool.getFollowingPool().add(new FContent(time,j));
			}
		}
			
		System.out.println("initPool end...");
	}
	
	public void updatePool() throws Exception{
		System.out.println("updatePool start...");
		
		while(!this.pool.getFollowingPool().isEmpty()){
			
			FContent f = pool.removeFirstFollowing();
			//update followingPool
			Long next = nextTime(f.getTime(),f.getUid());
			if(next!=null){
				 pool.addTofollowingPool(new FContent((Long)next,f.getUid()));
			}
		
			Tweet m = new Tweet(this.mid++,f.getTime(),f.getUid(),1);
			Tweet rtM = null;
			if(isRetweet(m)){
				rtM = getRtTweet(m);
			}

			//move tweet to CandidatesPool
			pool.addTweet(m);
			//write to File in disk
			String result = m.toString();
			if(rtM!=null){
				result = m.toString()+"\t"+rtM.toString();
			}
//			if((m.getMid()%100000)==0){
//				System.out.println(result);
//			}
			
			file.write(result);
			file.newLine();	
		}
		
		file.flush();

		System.out.println("updatePool end...");
		System.out.println("the total of microblog\uff1a"+(mid+1));
		
	}
	
	public Long nextTime(long startTime,int uid) throws ParseException{	
		double rateKey = Parameter.CDF_postRate.ceilingKey(Parameter.userKeys.get(uid));
		String[] items = Parameter.CDF_postRate.get(rateKey).split("\t");
		double pRate = Double.valueOf(items[0]);
		long time =startTime;
		while(time < Parameter.endTime){
			double r = Parameter.userRandoms.get(uid)[0].nextDouble();
			
			time += (- Math.log(r)/(Parameter.maxFactor*pRate));
			
			if(time <= Parameter.endTime ){
				if(Math.random()<(getFactor(time)/Parameter.maxFactor)){
					return time;
				}
			}else{
				return null;
			}
		}
		return null;
	}
	
	
	public  boolean isRetweet(Tweet t){
		double rateKey = Parameter.CDF_postRate.ceilingKey(Parameter.userKeys.get(t.getUid()));
		String[] items = Parameter.CDF_postRate.get(rateKey).split("\t");
		double rRate = Double.valueOf(items[1]);
		if(Parameter.userRandoms.get(t.getUid())[1].nextDouble()<= rRate){
			return true;
		}else{
			return false;
		}
	}
	
	//the function of time adjustment coefficient
	public  double getFactor(long time ){
		Calendar e=Calendar.getInstance(); 
		e.setTime(new Date(time*1000));
		double h = Parameter.hour.get(e.get(Calendar.HOUR_OF_DAY));
		double d = Parameter.day.get(e.get(Calendar.DAY_OF_WEEK)-1);
		
		return  h*d;
	}

	
	public Tweet getRtTweet(Tweet m) throws ParseException{
		Tweet result =null;
		
		TreeMap<Integer,List<Tweet>> tms = new TreeMap<Integer,List<Tweet>>();
		Map<Integer,Integer> tms_iden =new  HashMap<Integer,Integer>();
		int tms_sum = 0;
		int count = 0;
		int icount=0;
		
		Integer internal = Parameter.CPD_internal.ceilingEntry(new Random().nextDouble()).getValue();
		List<Integer> friends = new ArrayList<Integer>(Parameter.followerList.get(m.getUid()));
		friends.retainAll(pool.getCandidatePool().keySet());
		TreeSet<Tweet> followings = new TreeSet(new ComparatorByTimeReverseOrder());
		Map<Integer,Integer> identify = new HashMap<Integer,Integer>();
		Map<Integer,Integer> skip = new HashMap<Integer,Integer>();
		
		for(Integer uid:friends){
			List<Long> mids = pool.getCandidatePool().get(uid);
			if(mids.size() > 0){
				int iden = mids.size()-1;				
				Tweet firstTweet = pool.getTweets().get(mids.get(iden));
				
				if(followings.size() <= Parameter.cutLength){
					followings.add(firstTweet);
					identify.put(uid,iden-1);
				}else if(firstTweet.getTime() > followings.last().getTime()){
					followings.add(firstTweet);
					identify.remove(followings.last().getUid());
					followings.remove(followings.last());
					identify.put(uid,iden-1);
				}
			}
			
		}
		while(followings.size() > 0){
			Tweet im = followings.pollFirst();
			if( count < Parameter.cutLength){
				long timeGap = m.getTime()- im.getTime();
				if(timeGap > 0){
					count++;
					int hours =(int)Math.ceil(Double.valueOf(timeGap)/3600);
					if(icount> 0 && hours > internal){
						break;
					}
					
					if(hours == internal){
						if(icount == 0){
							tms.clear();
							tms_iden.clear();
							tms_sum = 0;
						}
						icount++;
					}
					
					Integer rtCount = im.getRtCount();
					if(tms_iden.containsKey(rtCount)){
						Integer key = tms_iden.get(rtCount);
						tms.get(key).add(im);
					}else{
						tms_sum += rtCount;
						tms_iden.put(rtCount, tms_sum);
						List<Tweet> lts = new ArrayList<Tweet>();
						lts.add(im);
						tms.put(tms_sum, lts);
					}	
				}
			}else{
				break;
			}
			
			int iden = identify.get(im.getUid());
			if(iden >= 0){
				List<Long> mids = pool.getCandidatePool().get(im.getUid());
				Tweet next = pool.getTweets().get(mids.get(iden));
				followings.add(next);
				identify.put(im.getUid(), iden-1);
			}else{
				identify.remove(im.getUid());
				if(Parameter.isIndex){
					int skipNum = 0;
					if(skip.containsKey(im.getUid())){
						skipNum = skip.get(im.getUid());
					}
					Tweet next = Parameter.btree.getTweet(im.getUid(), skipNum);
					if(next != null){
						followings.add(next);
						skip.put(im.getUid(), skipNum+1);
					}else if(skip.containsKey(im.getUid())){
						skip.remove(im.getUid());
					}
				}
			}
		}
		
		
		if(!tms.isEmpty()){
			int index = new Random().nextInt(tms_sum);
			List<Tweet> lts = tms.ceilingEntry(index).getValue();
			result = lts.get(new Random().nextInt(lts.size()));
		}
		
		if(result!=null){
			pool.addOneRtCount(result);
		}

		return result;

	}
	
	


	
}