package index;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import object.Tweet;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class TimeKeyCreator implements SecondaryKeyCreator{
	
	@Override
	public boolean createSecondaryKey(SecondaryDatabase secDb,
			DatabaseEntry keyEntry, DatabaseEntry dataEntry, DatabaseEntry resultEntry) {
		// TODO Auto-generated method stub
		if(dataEntry == null){
			return false;
		}else{
			Tweet t = (Tweet) new TweetBinding().entryToObject(dataEntry);
			try {
				resultEntry.setData(t.getTime().toString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}
	
}

