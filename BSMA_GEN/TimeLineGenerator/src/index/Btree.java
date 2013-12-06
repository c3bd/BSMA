package index;

import generator.Parameter;

import java.io.File;
import java.io.IOException;
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
	public void put(Tweet t)  {
		// TODO Auto-generated method stub
		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry theData = new DatabaseEntry();
		dbenv.getLongBinding().objectToEntry(t.getMid(), theKey);
		dbenv.getTweetBinding().objectToEntry(t, theData);
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
			cursor.close();
		}
	}

	@Override
	public void remove(Tweet t) {
		// TODO Auto-generated method stub
		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry theData = new DatabaseEntry();
		dbenv.getLongBinding().objectToEntry(t.getMid(), theKey);
		dbenv.getTweetBinding().objectToEntry(t, theData);
		Cursor cursor = dbenv.getTweetDB().openCursor(null, null);
		try {
			//dbenv.getTweetBinding().objectToEntry(t, theData);
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
	public Tweet getTweet(Integer uid, int skipNum) {
		Tweet tweet = null;
		DatabaseEntry searchkey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();
		dbenv.getIntegerBinding().objectToEntry(uid, searchkey);
		
		Cursor cursor = dbenv.getTweetDB().openCursor(null, null);
		try{
			OperationStatus retVal = cursor.getSearchKey(searchkey, foundData, LockMode.DEFAULT);
			
			if(retVal == OperationStatus.SUCCESS){
				if(skipNum == 0){
					tweet = (Tweet)dbenv.getTweetBinding().entryToObject(foundData);
				}else{
					long result = cursor.skipNext(skipNum, searchkey, foundData, LockMode.DEFAULT);
					if(result > 0){
						tweet = (Tweet)dbenv.getTweetBinding().entryToObject(foundData);
					}
				}
			}
		}catch(Exception e){
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