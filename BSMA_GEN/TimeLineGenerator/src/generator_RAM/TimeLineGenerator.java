package generator_RAM;

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
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import object.ComparatorByTime;
import object.FContent;
import object.Tweet;

public class TimeLineGenerator {
	
	private long mid = 0;
	private BufferPoolManager pool = new BufferPoolManager();
	//DBEnv mbdb = new DBEnv();
	private BufferedWriter file;
	//private Btree btree;
		
	int poolCount=0;
	int btreeCount =0;
	public TimeLineGenerator(String startTime,String endTime,int userNum, String outPath, String btreePath) throws IOException, ParseException{

		System.out.println("Parameter start...");
		new Parameter(startTime,endTime,userNum);
		System.out.println("Parameter end...");
		this.file = new BufferedWriter(new FileWriter(outPath));
	}
	
	//生成timeline数据
	public void generateData() throws Exception{
		
		initPool();//初始化pool中的内容
		updatePool();//根据每一时刻更新pool中的内容
	}
	
	public void initPool() throws Exception{

		System.out.println("initPool start...");
		long start = Parameter.startTime;
		Random rr = new Random();
		int count =Parameter.userNum;
		
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
		//long start = Parameter.startTime;
		
		//按时间顺序将followPool内容放入candidate和candidatePool中，并更新其中内容
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
				long start = System.currentTimeMillis();
				rtM = getRtTweet(m);
				Parameter.runningTime+=(System.currentTimeMillis()-start);
			}

			//move tweet to CandidatesPool
			pool.addTweet(m);
			//write to File in disk
			String result = m.toString();
			if(rtM!=null){
				result =m.toString()+"\t"+rtM.toString();
			}
//			if((m.getMid()%100000)==0){
				System.out.println(result);
//			}
			
			file.write(result);
			file.newLine();	
		}
		
		file.flush();

		System.out.println("updatePool end...");
		System.out.println(" 一共有微博："+mid);
		
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
	
	//不同时间段有不同Lamda系数
	public  double getFactor(long time ){
		Calendar e=Calendar.getInstance(); 
		e.setTime(new Date(time*1000));
		double h = Parameter.hour.get(e.get(Calendar.HOUR_OF_DAY));
		double d = Parameter.day.get(e.get(Calendar.DAY_OF_WEEK)-1);
		
		return  h*d;
	}
	
	public Tweet getRtTweet1(Tweet m) throws ParseException{
		Tweet result =null;
		if(Parameter.retweetTypeRandom.nextDouble()<0.5){
		
			//Tweet[] lt = (Tweet[]) pool.hotspot.toArray();
			TreeMap<Integer,List<Tweet>> tm = new TreeMap<Integer,List<Tweet>>(); 
			TreeMap<Integer,Integer> tm_iden =new  TreeMap<Integer,Integer>();
			int tm_sum=0;
			int count=0;
			for(Tweet t:pool.hotspot){
				count ++;
				if(count<100){
					int rtCount = t.getRtCount();
					if(tm_iden.containsKey(rtCount)){
						tm.get(tm_iden.get(rtCount)).add(t);
					}else{
						List<Tweet> lt = new ArrayList<Tweet>();
						lt.add(t);
						tm_sum+=rtCount;
						tm_iden.put(rtCount, tm_sum);
						tm.put(tm_sum, lt);
					}
				}else{
					break;
				}
			}
			if(!tm.isEmpty()){
				List<Tweet> candi  = tm.ceilingEntry(new Random().nextInt(tm_sum)).getValue();
				//List<Tweet> candi = tm.get(index);
				result = candi.get(new Random().nextInt(candi.size()));
			}

		}else{
			Integer internal = Parameter.CPD_internal.ceilingEntry(new Random().nextDouble()).getValue();
			List<Integer> friends = new ArrayList<Integer>(Parameter.followerList.get(m.getUid()));
			friends.retainAll(pool.getCandidatePool().keySet());
			TreeSet<Tweet> followings = new TreeSet(new ComparatorByTime());
			Map<Integer,Integer> identify = new HashMap<Integer,Integer>();
			for(Integer uid:friends){
				List<Long> ls = pool.getCandidatePool().get(uid);
				if(followings.size() <= Parameter.cutLength){
					followings.add(pool.getTweets().get(ls.get(ls.size()-1)));
					identify.put(uid,ls.size()-1);
				}else if(pool.getTweets().get(ls.get(ls.size()-1)).getTime() > followings.last().getTime()){
					followings.add(pool.getTweets().get(ls.get(ls.size()-1)));
					identify.remove(followings.last().getUid());
					followings.remove(followings.last());
					identify.put(uid,ls.size()-1);
				}
			}
			//List<Tweet> lt = new ArrayList<Tweet>();
			int count =0;
			List<Tweet> ilt = new ArrayList<Tweet>();
			int icount =0;
			
			Map<Integer,List<Tweet>> lt = new HashMap<Integer,List<Tweet>>();
			while(followings.size() > 0){
				Tweet im =followings.first();
				if( count < Parameter.cutLength){
					long timeGap  = m.getTime()- im.getTime();
					if(timeGap > 0){
						count++;
						
						int minites =(int)Math.ceil(Double.valueOf(timeGap)/60);
						if(icount==0){
							if(lt.containsKey(minites)){
								lt.get(minites).add(im);
							}else{
								List<Tweet> ltt = new ArrayList<Tweet>();
								ltt.add(im);
								lt.put(minites, ltt);
							}
						}
						if(icount> 0 && minites > internal){
							break;
						}									
						if(minites == internal){
							icount++;
							ilt.add(im);
						}	
					}
				}else{
					break;
				}
				followings.remove(im);
				identify.put(im.getUid(), identify.get(im.getUid())-1);
				if(identify.get(im.getUid()) >= 0){
					List<Long> ls = pool.getCandidatePool().get(im.getUid());
					Tweet t =pool.getTweets().get(ls.get(identify.get(im.getUid())));
					followings.add(t);
				}	
			}
			
			if(!ilt.isEmpty()){
				result =  ilt.get(new Random().nextInt(ilt.size()));
			}else if(!lt.isEmpty()){
				double tisum=0;
				TreeMap<Double,Integer> re = new TreeMap<Double,Integer>();
				for(Integer ti:lt.keySet()){
					double p = Parameter.internal.ceilingEntry(ti).getValue();
					tisum+=p;
					re.put(tisum, ti);
				}
				int index = re.ceilingEntry(new Random().nextDouble()*tisum).getValue();
				List<Tweet> candi = lt.get(index);
				result = candi.get(new Random().nextInt(candi.size()));
			}
		}
		if(result!=null){
			pool.addOneRtCount(result);
		}
		return result;
	}
	
	
	public Tweet getRtTweet(Tweet m) throws ParseException{
		Tweet result =null;
		
		TreeMap<Integer,List<Tweet>> tms = new TreeMap<Integer,List<Tweet>>();
		Map<Integer,Integer> tms_iden =new  HashMap<Integer,Integer>();
		int tms_sum=0;
		int count=0;
		
		TreeMap<Integer,List<Tweet>> itms = new TreeMap<Integer,List<Tweet>>();
		Map<Integer,Integer> itms_iden =new  HashMap<Integer,Integer>();
		int itms_sum=0;
		int icount=0;
		
		Integer internal = Parameter.CPD_internal.ceilingEntry(new Random().nextDouble()).getValue();
		List<Integer> friends = new ArrayList<Integer>(Parameter.followerList.get(m.getUid()));
		friends.retainAll(pool.getCandidatePool().keySet());
		TreeSet<Tweet> followings = new TreeSet(new ComparatorByTime());
		Map<Integer,Integer> identify = new HashMap<Integer,Integer>();
		Parameter.forTime+=friends.size();
		
		for(Integer uid:friends){
			List<Long> ls = pool.getCandidatePool().get(uid);
			if(followings.size() <= Parameter.cutLength){
				followings.add(pool.getTweets().get(ls.get(ls.size()-1)));
				identify.put(uid,ls.size()-1);
			}else if(pool.getTweets().get(ls.get(ls.size()-1)).getTime() > followings.last().getTime()){
				followings.add(pool.getTweets().get(ls.get(ls.size()-1)));
				identify.remove(followings.last().getUid());
				followings.remove(followings.last());
				identify.put(uid,ls.size()-1);
			}
		}
		while(followings.size() > 0){
			Tweet im =followings.first();
			if( count < Parameter.cutLength){
				long timeGap = m.getTime()- im.getTime();
				if(timeGap > 0){
					count++;
					int hours =(int)Math.ceil(Double.valueOf(timeGap)/3600);
					if(icount> 0 && hours > internal){
						break;
					}
					Integer rtCount = im.getRtCount();
					if(itms.isEmpty()){
						if(tms_iden.containsKey(rtCount)){
							Integer key = tms_iden.get(rtCount);
							tms.get(key).add(im);
						}else{
							tms_sum+=rtCount;
							tms_iden.put(rtCount, tms_sum);
							List<Tweet> lts = new ArrayList<Tweet>();
							lts.add(im);
							tms.put(tms_sum, lts);
						}
					}
															
					if(hours == internal){
						icount++;
						if(itms_iden.containsKey(rtCount)){
							Integer key = itms_iden.get(rtCount);
							itms.get(key).add(im);
						}else{
							itms_sum+=rtCount;
							itms_iden.put(rtCount, itms_sum);
							List<Tweet> lts = new ArrayList<Tweet>();
							lts.add(im);
							itms.put(itms_sum, lts);
						}

					}	
				}
			}else{
				break;
			}
			followings.remove(im);
			identify.put(im.getUid(), identify.get(im.getUid())-1);
			if(identify.get(im.getUid()) >= 0){
				List<Long> ls = pool.getCandidatePool().get(im.getUid());
				Tweet t =pool.getTweets().get(ls.get(identify.get(im.getUid())));
				followings.add(t);
			}	
		}
		
		
		if(!itms.isEmpty()){
			int index = new Random().nextInt(itms_sum);
			List<Tweet> lts = itms.ceilingEntry(index).getValue();
			result = lts.get(new Random().nextInt(lts.size()));
		}else if(!tms.isEmpty()){
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