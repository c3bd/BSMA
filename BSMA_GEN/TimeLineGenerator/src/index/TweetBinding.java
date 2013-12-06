package index;

import object.Tweet;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class TweetBinding extends TupleBinding{

	@Override
	public Object entryToObject(TupleInput ti) {
		// TODO Auto-generated method stub
		long mid = ti.readLong();
		long time = ti.readLong();
		int uid = ti.readInt();
		int rtCount = ti.readInt();
		
		Tweet tweet = new Tweet(mid,time,uid,rtCount);
		
		return tweet;
	}

	@Override
	public void objectToEntry(Object object, TupleOutput to) {
		// TODO Auto-generated method stub
		Tweet tweet = (Tweet)object;

		to.writeLong(tweet.getMid());
		to.writeLong(tweet.getTime());
		to.writeInt(tweet.getUid());
		to.writeInt(tweet.getRtCount());
		
	}
	
}