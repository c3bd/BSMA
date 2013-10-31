package timelineGenerator;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;


import object.Tweet;
import object.ComparatorByTimeReverseOrder;


public class Util {
	public static Tweet getRtTweet(Tweet m) throws ParseException, IOException{
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
		Set<Integer> friends = new HashSet<Integer>(Parameter.followList.get(m.getUid()));
		friends.retainAll(Parameter.pool.getCandidatePool().keySet());
		TreeSet<Tweet> followings = new TreeSet(new ComparatorByTimeReverseOrder());
		
		int size = Parameter.userFeedSize.get(m.getUid());
		
		for(Integer uid:friends){
			Entry entry =Parameter.pool.getCandidatePool().get(uid).lowerEntry(m.getTime());
			if(entry!=null){
				String mid = (String) entry.getValue();
				long time = (Long) entry.getKey();
				if(followings.size() <= size){
					followings.add(new Tweet(mid,time,uid));
				}else if(time > followings.last().getTime()){
					followings.add(new Tweet(mid,time,uid));
					followings.remove(followings.last());
				}
			}
			
		}
		while(followings.size() > 0){
			Tweet im =followings.first();
			if(count < size){
				long timeGap = m.getTime()- im.getTime();
				if(timeGap > 0){
					count++;
					int hours =(int)Math.ceil(Double.valueOf(timeGap)/3600);
					if(icount> 0 && hours > internal){
						break;
					}
					Integer rtCount = 1;
					if(Parameter.tweetRtCount.containsKey(im.getMid())){
						rtCount = Parameter.tweetRtCount.get(im.getMid());
					}
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
			Entry<Long, String> lowerTweet = Parameter.pool.getCandidatePool().get(im.getUid()).lowerEntry(im.getTime());
			if(lowerTweet!=null ){
				String mid = lowerTweet.getValue();
				Long time = lowerTweet.getKey();
				followings.add(new Tweet(mid,time,im.getUid()));
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

			if(Parameter.tweetRtCount.containsKey(result.getMid())){
				Parameter.tweetRtCount.put(result.getMid(),Parameter.tweetRtCount.get(result.getMid())+1);
			}else{
				Parameter.tweetRtCount.put(result.getMid(),2);
			}
			Parameter.rtfile.write(m.getMid()+","+result.getMid());
			Parameter.rtfile.newLine();
			Parameter.rtfile.flush();
		}

		return result;

	}
	
	public static SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Long nextTime(long startTime,Integer uid){
		String[] items =  Parameter.CDF_postRate.ceilingEntry(Parameter.userProporty.get(uid)).getValue().split("\t");
		double pRate = Double.valueOf(items[0]);
		long time = startTime;
		while(time < Parameter.endTime){
			double r = Parameter.random_userPost.get(uid).nextDouble();
			time += (-Math.log(r)/(Parameter.maxFactor*pRate));
			if(time <= Parameter.endTime){
				if(Math.random() < (getFactor(time)/Parameter.maxFactor)){
					return time;
				}
			}else{
				return null;
			}
		}
		return null;
	}
	
	public static double getFactor(long time){
		Calendar e = Calendar.getInstance();
		e.setTime(new Date(time*1000));
		double h = Parameter.hour.get(e.get(Calendar.HOUR_OF_DAY));
		double d = Parameter.day.get(e.get(Calendar.DAY_OF_WEEK)-1);
		return h*d;
	}
	
	public static boolean isRetweet(Tweet m){
		String[] items =  Parameter.CDF_postRate.ceilingEntry(Parameter.userProporty.get(m.getUid())).getValue().split("\t");
		double rRate = Double.valueOf(items[1]);
		if(Parameter.random_userRetweet.get(m.getUid()).nextDouble() <= rRate){
			return true;
		}else{
			return false;
		}
	}
	
	public static Calendar changeTimeToCalendar(String time) throws ParseException{
		Date date = fmt.parse(time);
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		return calendar;
	}
	
	public static long changeTimeToSeconds(String time) throws ParseException{
		return changeTimeToCalendar(time).getTimeInMillis()/1000;
	}
	
	public static String changeTimeToString(Long time) throws ParseException{
		return fmt.format(new Date(time*1000));
	}
	
	public static Long getNextTimeSpan(Long time) throws ParseException{
		Calendar calendar =changeTimeToCalendar(changeTimeToString(time));
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)+1);
		System.out.println(calendar.get(Calendar.MONTH));
		
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis()/1000;
	}
	

}