package index;
import java.io.File;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
public class DBEnv{
	private Environment myEnv;
	private Database tweetDB;
//	private SecondaryDatabase tweetSecDB;
	private TupleBinding tweetBinding ;
	private TupleBinding longBinding ;
	public TupleBinding getIntegerBinding() {
		return IntegerBinding;
	}

	public void setIntegerBinding(TupleBinding integerBinding) {
		IntegerBinding = integerBinding;
	}


	private TupleBinding IntegerBinding ;
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
	   tweetBinding= new TweetBinding();
	   longBinding = new LongBinding();
	   IntegerBinding = new IntegerBinding();
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
		//myDbConfig.setDuplicateComparator(TweetComparator.class);
		myDbConfig.setTransactional(false);
		
		SecondaryConfig mySecondaryconfig = new SecondaryConfig();
		mySecondaryconfig.setReadOnly(readOnly);
		mySecondaryconfig.setAllowCreate(!readOnly);
		mySecondaryconfig.setSortedDuplicates(duplicatesAllowed);
		mySecondaryconfig.setTransactional(false);
				
		try {
			 tweetDB = myEnv.openDatabase(null, "tweetDB", myDbConfig);
//			 TimeKeyCreator keyCreator = new TimeKeyCreator();
//			 mySecondaryconfig.setKeyCreator(keyCreator);
//			 tweetSecDB = myEnv.openSecondaryDatabase(null, "tweetSecDB", tweetDB, mySecondaryconfig);
		} catch (DatabaseException e) {
		    e.printStackTrace();
		}
	}

//	public SecondaryDatabase getTweetSecDB() {
//		return tweetSecDB;
//	}
//
//	public void setTweetSecDB(SecondaryDatabase tweetSecDB) {
//		this.tweetSecDB = tweetSecDB;
//	}
//
//	public TupleBinding getIntegerBinding() {
//		return IntegerBinding;
//	}
//
//	public void setIntegerBinding(TupleBinding integerBinding) {
//		IntegerBinding = integerBinding;
//	}

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
			  // tweetSecDB.close();
			   tweetDB.close();
			   myEnv.cleanLog();
			   myEnv.close();
		   }catch(DatabaseException dbe){
			   System.err.println("Error closing MyDbEnv:" + dbe.toString());
			   System.exit(-1);
		   }
	   }
	}
	
}