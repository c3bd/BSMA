package index;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class TweetInfoBinding extends TupleBinding{

	@Override
	public Object entryToObject(TupleInput ti) {
		// TODO Auto-generated method stub
		long mid = ti.readLong();
		int uid = ti.readInt();
		int rtCount = ti.readInt();
		
		TweetInfo tweet = new TweetInfo(mid,uid,rtCount);
		
		return tweet;
	}

	@Override
	public void objectToEntry(Object object, TupleOutput to) {
		// TODO Auto-generated method stub
		TweetInfo tweet = (TweetInfo)object;

		to.writeLong(tweet.getMid());
		to.writeInt(tweet.getUid());
		to.writeInt(tweet.getRtCount());
		
	}
	
}