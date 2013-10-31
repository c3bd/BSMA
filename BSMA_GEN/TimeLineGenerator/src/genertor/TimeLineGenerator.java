package genertor;

import index.Btree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import object.CContent;
import object.FContent;
import object.FollowShip;
import object.Tweet;

public class TimeLineGenerator {
	
	private long mid = 0;
	private BufferPoolManager pool = new BufferPoolManager();
	//DBEnv mbdb = new DBEnv();
	private BufferedWriter file;
	private Btree btree;
		
	int poolCount=0;
	int btreeCount =0;
	public TimeLineGenerator(String startTime,String endTime,int userNum, String outPath, String btreePath) throws IOException{
		new Parameter(startTime,endTime,userNum);
		this.file = new BufferedWriter(new FileWriter(outPath));
		this.btree = new Btree(btreePath,1024*1024);
	}
	
	//生成timeline数据
	public void generateData() throws Exception{
		
		initPool();//初始化pool中的内容
		updatePool();//根据每一时刻更新pool中的内容
	}
	
	public void initPool() throws Exception{
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
	}
	
	public void updatePool() throws Exception{
		long start = Parameter.startTime;
		
		//按时间顺序将followPool内容放入candidate和candidatePool中，并更新其中内容
		while(!this.pool.getFollowingPool().isEmpty()){
			
			FContent f = pool.removeFirstFollowing();
			//update followingPool
			Long next = nextTime(f.getTime(),f.getUid());
			if(next!=null){
				 pool.addTofollowingPool(new FContent((Long)next,f.getUid()));
			}
		
			Tweet m = new Tweet(this.mid++,f.getTime(),f.getUid(),0);
			Tweet rtM = null;
			if(isRetweet(m)){
				int timeGap = getTimeGap((int) (m.getTime()-Parameter.startTime));
				//m.setRtMid(getRtMid(m,timeGap));
				rtM=getRtMid(m,timeGap);
			}
			String s = m.toString();

			if(pool.getFirstFollowingTime() != m.getTime()){
				Long firstTime = pool.getFirstCandidateTime();
				if(firstTime!=null &&(m.getTime()-firstTime) > Parameter.windowSize){
					//update CandidatesPool,remove candidates beyond windowSize, and put into btree
					while(firstTime != null && firstTime < (m.getTime()-Parameter.windowSize)){
						pool.removeFirstCandidateByTime(btree);
						firstTime = pool.getFirstCandidateTime();
					}
				}
			}
			//move tweet to CandidatesPool
			pool.addToCandidatePool(m);
			//write to File in disk
			String result = m.toString();
			if(rtM!=null){
				result =m.toString()+"\t"+rtM.toString();
			}
			file.write(result);
			file.newLine();	
		}
		
		file.flush();
		double pRate =0.0;
		for(int j=0;j<Parameter.userNum;j++){
			double rateKey = Parameter.CDF_postRate.ceilingKey(Parameter.userKeys.get(j));
			String[] items = Parameter.CDF_postRate.get(rateKey).split("\t");
			pRate += Double.valueOf(items[0]);
		}
		System.out.println("poolCount："+poolCount+"btreeCount："+btreeCount);
		System.out.println("原始："+pRate*365*24*3600+" 一共有微博："+mid);
		
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
	
	public Tweet getRtMid(Tweet m,long timeGap){
		Long mid = null;
		Tweet tweet = null;
		Long firstTime =pool.getFirstCandidateTime();
		if(firstTime == null){
			return null;
		}else{
			boolean InWindowSize = timeGap <= (m.getTime()-pool.getFirstCandidateTime());
			if(InWindowSize){
				tweet = getRtTweetFromPool(m, m.getTime()-timeGap);
				System.out.println("pool: "+tweet.toString());poolCount++;
			}else{
				tweet = getRtTweetFromBtree(m, m.getTime()-timeGap);
				System.out.println("btree: "+tweet.toString());btreeCount++;
			}
			if(tweet!=null){
				mid = tweet.getMid();
			}
			return tweet;
		}
		
	}
	
	public Tweet getRtTweetFromPool(Tweet m,long time){
		double sum=0;
		TreeMap<Double,Tweet> cpd = new TreeMap<Double,Tweet>();
		Tweet result = null;
		Long t =pool.getCandidatePool().ceilingKey(time);
		if(t == null || t == m.getTime()){
			t =pool.getCandidatePool().floorKey(time);
		}
		if(t == null){
			return null;
		}
		Set<CContent> contents = pool.getCandidatePool().get(t);
		int size = contents.size();
		for(CContent content:contents){
			content.getUid();
			double key =Parameter.getRtweetTypeFactor(m.getUid(),content.getUid())*(content.getRtCount()+1);
			sum+=key;
			Tweet value = new Tweet(content.getMid(),time,content.getUid(),content.getRtCount());
			cpd.put(sum,value);
		}	
		if(!cpd.isEmpty()){
			result = cpd.get(cpd.ceilingKey(new Random().nextDouble()*sum));
			pool.addRtCount(result,1);
		}
		return result;
	}
	
	public Tweet getRtTweetFromBtree(Tweet m,long time){
		double sum=0;
		TreeMap<Double,Tweet> cpd = new TreeMap<Double,Tweet>();
		Tweet result = btree.getMid(time,m);
		
		if(pool.getCandidatePool().containsKey(time)){
			Set<CContent> contents = pool.getCandidatePool().get(time);
			for(CContent content:contents){
				content.getUid();
				double key = Parameter.getRtweetTypeFactor(m.getUid(),content.getUid())*(content.getRtCount()+1);
				sum+=key;
				Tweet value = new Tweet(content.getMid(),time,content.getUid(),content.getRtCount());
				cpd.put(sum,value);
			}
		}
		if(!cpd.isEmpty()){
			result = cpd.get(cpd.ceilingKey(new Random().nextDouble()*sum));
			pool.addRtCount(result,1);
		}
		return result;
	}

	
	
	public int getTimeGap(int timeRange){
		
		int maxRange = Parameter.maxTimeGap*60;
		if(timeRange < Parameter.maxTimeGap*60){
			int x=(int) Math.ceil(timeRange/60);
			if(x == 0){
				x=1;
			}
			maxRange = Parameter.timeGap.get(Parameter.timeGap.floorKey(x));
		}

		
		int r = Parameter.timeGapRandom.nextInt(maxRange);
		int key = Parameter.CDF_timeGap.ceilingKey(r);
		int timeGap = Parameter.CDF_timeGap.get(key);
		timeGap = timeGap*60-new Random().nextInt(60);
		return timeGap;
	}
	
}