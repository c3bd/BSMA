package index;

import genertor.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import object.Tweet;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryCursor;


public class Btree implements IBtree{
	DBEnv dbenv;
	String dbpath;
	
	public Btree(String dbpath, int cacheSize) {
		init(dbpath, cacheSize);
	}
	
	public void init(String dbpath, int cacheSize) {
		this.dbpath = dbpath;
		this.dbenv = new DBEnv();
		dbenv.setUp(new File(dbpath), false, true, cacheSize);
	}
	@Override
	public void put(Long key, TweetInfo tweet) throws Exception {
		// TODO Auto-generated method stub
		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry theData = new DatabaseEntry();
		dbenv.getLongBinding().objectToEntry(key, theKey);
		dbenv.getTweetBinding().objectToEntry(tweet, theData);
		Cursor cursor = dbenv.getTweetDB().openCursor(null, null);
		OperationStatus retVal = null;

		try {
			
			if (cursor.getSearchBoth(theKey, theData, LockMode.DEFAULT) == OperationStatus.NOTFOUND){
				retVal = cursor.put(theKey, theData);
				
			}
			
		} catch (Exception dbe) {
			try {
				System.out.println("Error putting entry " + theKey.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
	}
	


	@Override
	public void remove(Long key, TweetInfo tweet) throws Exception {
		// TODO Auto-generated method stub
		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry theData = new DatabaseEntry();
		dbenv.getLongBinding().objectToEntry(key, theKey);
		dbenv.getTweetBinding().objectToEntry(tweet, theData);
		Cursor cursor = dbenv.getTweetDB().openCursor(null, null);
		try {
			dbenv.getTweetBinding().objectToEntry(tweet, theData);
			OperationStatus ops = cursor.getSearchBoth(theKey, theData,	LockMode.DEFAULT);
			if (ops == OperationStatus.SUCCESS) {
				cursor.delete();
			} else {
				System.out.println("deleting failed" + ops.toString());
			}
		} catch (DatabaseException dbe) {
			try {
				System.out.println("Error removing entry " + theKey.toString());
			} catch (Exception willNeverOccur) {
			}
			throw dbe;
		} finally {
			cursor.close();
			cursor = null;
		}
	}

	@Override
	public Tweet getMid(Long key, Tweet m) {
		// TODO Auto-generated method stub
		Tweet tweet = null;
		TreeMap<Double,TweetInfo> result = new TreeMap<Double,TweetInfo>();
		Cursor cursor = dbenv.getTweetDB().openCursor(null,null);
		double sum =0;
		try{
			DatabaseEntry searchKey = new DatabaseEntry();
			dbenv.getLongBinding().objectToEntry(key, searchKey);
			DatabaseEntry foundData = new DatabaseEntry();
			OperationStatus retVal = cursor.getSearchKeyRange(searchKey, foundData, LockMode.DEFAULT);
			if(retVal == OperationStatus.NOTFOUND){
				retVal = cursor.getLast(searchKey, foundData, LockMode.DEFAULT);
			}
			while(retVal == OperationStatus.SUCCESS){
				TweetInfo t = (TweetInfo)dbenv.getTweetBinding().entryToObject(foundData);
				double item = Parameter.getRtweetTypeFactor(m.getUid(), t.getUid())*(t.getRtCount()+1);
				sum+=item;
				result.put(sum, t);
				retVal = cursor.getNextDup(searchKey, foundData, LockMode.DEFAULT);
			}
			if(!result.isEmpty()){ 
				TweetInfo t = result.get(result.ceilingKey(new Random().nextDouble()*sum));
				Long time = (Long)dbenv.getLongBinding().entryToObject(searchKey);
				tweet = new Tweet(t.getMid(),time,t.getUid(),t.getRtCount());
				remove(time,t);
				t.addRtCount(1);
				put(time,t);
			}
				
		}catch(Exception e){
			System.out.println("Error on tweetInfo secondary cursor:" );
			System.out.println(e.toString() );
			e.printStackTrace();
		}finally{
			cursor.close();
		}
		
		return tweet;
	}
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		this.dbenv.flush();
		this.dbenv.close();
	}
	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		dbenv.flush();
	}
	
}