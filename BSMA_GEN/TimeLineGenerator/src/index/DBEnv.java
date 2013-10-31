package index;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
public class DBEnv{
	private Environment myEnv;
	private Database tweetDB;
	private TupleBinding tweetBinding ;
	private TupleBinding longBinding ;
	private EnvironmentConfig myEnvConfig = new EnvironmentConfig();
	public DBEnv() {
	  
	}

	public void setUp(File envHome, boolean readOnly,
			boolean duplicatesAllowed, int cacheSize) throws DatabaseException {
	   myEnvConfig.setReadOnly(readOnly);
	   myEnvConfig.setAllowCreate(!readOnly);
	   myEnvConfig.setCacheSize(cacheSize);
	   myEnvConfig.setTransactional(false);
	   
	   myEnv = new Environment(envHome,myEnvConfig);
	   openTweetDB(readOnly,duplicatesAllowed);
	   tweetBinding= new TweetInfoBinding();
	   longBinding = new LongBinding();
	}
	
	public TupleBinding getLongBinding() {
		return longBinding;
	}

	public void setLongBinding(TupleBinding longBinding) {
		this.longBinding = longBinding;
	}

	public void openTweetDB(boolean readOnly,boolean duplicatesAllowed) {
		DatabaseConfig myDbConfig = new DatabaseConfig();
		myDbConfig.setReadOnly(readOnly);
		myDbConfig.setAllowCreate(!readOnly);
		myDbConfig.setSortedDuplicates(duplicatesAllowed);
		myDbConfig.setDuplicateComparator(TweetInfoComparator.class);
		myDbConfig.setTransactional(false);
		
				
		try {
			 tweetDB = myEnv.openDatabase(null, "tweetDB", myDbConfig);
		} catch (DatabaseException e) {
		    e.printStackTrace();
		}
	}

	public void flush() {
		tweetDB.sync();
	}
	
	public Environment getMyEnv() {
		return myEnv;
	}

	public void setMyEnv(Environment myEnv) {
		this.myEnv = myEnv;
	}


	public TupleBinding getTweetBinding() {
		return tweetBinding;
	}

	public void setTweetBinding(TupleBinding tweetBinding) {
		this.tweetBinding = tweetBinding;
	}

	public EnvironmentConfig getMyEnvConfig() {
		return myEnvConfig;
	}

	public void setMyEnvConfig(EnvironmentConfig myEnvConfig) {
		this.myEnvConfig = myEnvConfig;
	}

	public void setTweetDB(Database tweetDB) {
		this.tweetDB = tweetDB;
	}
	
	public Environment getEnvironment(){
		return myEnv;
	}
	
	public Database getTweetDB(){
		return tweetDB;
	}


	public void close() {
	   if(myEnv != null){
		   try{
			   if(tweetDB!= null){
				   tweetDB.close();
			   }
			   myEnv.cleanLog();
			   myEnv.close();
		   }catch(DatabaseException dbe){
			   System.err.println("Error closing MyDbEnv:" + dbe.toString());
			   System.exit(-1);
		   }
	   }
	}
	
}