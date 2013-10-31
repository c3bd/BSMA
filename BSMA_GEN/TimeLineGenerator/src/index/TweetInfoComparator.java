package index;

import java.util.Comparator;

import com.sleepycat.je.DatabaseEntry;
 public class TweetInfoComparator implements Comparator<byte[]>{

	@Override
	public int compare(byte[] o1, byte[] o2) {
		// TODO Auto-generated method stub
		DatabaseEntry dbe1 = new DatabaseEntry(o1);
		DatabaseEntry dbe2 = new DatabaseEntry(o2);
		TweetInfoBinding tweetBinding = new TweetInfoBinding();
		TweetInfo t1= (TweetInfo) tweetBinding.entryToObject(dbe1);
		TweetInfo t2= (TweetInfo) tweetBinding.entryToObject(dbe2);
		
		int comp = t1.getRtCount()-t2.getRtCount();
		if(comp == 0){
			return (int) (t1.getMid()-t2.getMid());
		} else {
			return comp < 0? -1 : 1;
		}

	}
	 
 }