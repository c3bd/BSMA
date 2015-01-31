package edu.ecnu.imc.bsma.dao;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import rpc.Job;
import rpc.Query;
import rpc.SubJob;
import edu.ecnu.imc.bsma.util.Config;
import edu.ecnu.imc.bsma.util.JarLoader;

/**
 * a job may consists of multiple sub-jobs, web ui pass json format storing the
 * jobinfo
 * 
 * @author xiafan
 * 
 */
public class JobInfo extends Job {
	public static final byte WAITING = 0;
	public static final byte RUNNING = 1;
	public static final byte FINISH = 2;
	public static final byte CANCEL = 3;

	public byte state = WAITING;
	public String msg = "";
	Dao dao;

	public JobInfo(Dao dao, byte state) {
		this.dao = dao;
		this.state = state;
	}

	public JobInfo(Job job, Dao dao) {
		super(job);
		this.dao = dao;

		/**
		 * rather strange here
		 */
		List<SubJob> tmp = new ArrayList<SubJob>(subJobs);
		if (tmp != null) {
			subJobs.clear();
			for (SubJob subJob : tmp) {
				this.addToSubJobs(subJob);
			}
		}
	}

	public JobInfo(JSONObject obj) throws JSONException {
		jobID = obj.getInt("id");
		JSONArray arr = obj.getJSONArray("subjobs");

		for (int i = 0; i < arr.size(); i++) {
			subJobs.add(new BasicJobInfo(arr.getJSONObject(i)));
		}

		JSONObject map = obj.getJSONObject("queries");
		arr = map.names();
		for (int i = 0; i < arr.size(); i++) {
			JSONObject qObj = arr.getJSONObject(i);
			queries.add(new Query((byte) qObj.getInt("type"), qObj
					.getDouble("frac"), qObj.getString("type")));
		}

		map = obj.getJSONObject("props");
		arr = map.names();
		for (int i = 0; i < arr.size(); i++) {
			String key = arr.getString(i);
			props.put(key, map.getString(key));
		}
	}

	public int numOfJobs() {
		return subJobs.size();
	}

	int idx = -1;

	/**
	 * 返回下一条需要执行的任务
	 * 
	 * @return
	 * @throws SQLException
	 */
	public BasicJobInfo runNextJob() throws SQLException {
		if (idx != -1) {
			((BasicJobInfo) subJobs.get(idx)).setState(FINISH);
		}

		while (++idx < subJobs.size()) {
			BasicJobInfo ret = (BasicJobInfo) subJobs.get(idx);
			if (ret.getState() == WAITING) {
				ret.setState(RUNNING);
				return ret;
			}
		}
		return null;
	}

	public BasicJobInfo getCurJob() {
		if (idx < 0 || idx >= subJobs.size())
			return null;
		return (BasicJobInfo) subJobs.get(idx);
	}

	public void start() throws SQLException {
		setState(RUNNING);
		idx = -1;
	}

	/**
	 * @throws SQLException
	 * 
	 */
	public void finish() throws SQLException {
		msg = "success";
		setState(FINISH);
		idx = -1;
	}

	/**
	 * 整个任务失败，更新状态
	 * 
	 * @throws SQLException
	 */
	public void cancel(String errMsg) throws SQLException {
		msg = errMsg;
		setState(CANCEL);
		for (int i = idx; 0 <= i && i < subJobs.size(); i++) {
			((BasicJobInfo) subJobs.get(i)).setState(CANCEL);
		}
	}

	public void setState(byte state) throws SQLException {
		this.state = state;
		if (dao != null) {
			dao.updateJobState(this);
		}
	}

	private static HashMap<Integer, String> workloads = new HashMap<Integer, String>();
	public static final int COREWORKLOAD = 0;
	static {
		workloads.put(COREWORKLOAD, "edu.ecnu.imc.bsma.workloads.CoreWorkload");
	}

	public String getWorkload() {
		return workloads.get(COREWORKLOAD);
	}

	public static String getWorkload(int idx) {
		return workloads.get(idx);
	}

	private static HashMap<Byte, String> dbImpls = new HashMap<Byte, String>();
	public static final byte TEST_DB = -1;
	public static final byte HIVE_DB = 0;
	public static final byte SHARK_DB = 1;
	public static final byte MONETDB_DB = 2;
	public static final byte NEO4J_DB = 3;
	public static final byte MYSQL_DB = 4;
	public static final byte USER_CUSTDB = Byte.MAX_VALUE;

	static {
		dbImpls.put(TEST_DB, "edu.ecnu.imc.bsma.db.TestDBImpl");
		dbImpls.put(HIVE_DB, "edu.ecnu.imc.bsma.db.HiveDBImpl");
		dbImpls.put(SHARK_DB, "edu.ecnu.imc.bsma.db.SparkDBImpl");
		dbImpls.put(MONETDB_DB, "edu.ecnu.imc.bsma.db.MonetDBImpl");
		dbImpls.put(NEO4J_DB, "edu.ecnu.imc.bsma.db.Neo4jDBImpl");
		dbImpls.put(MYSQL_DB, "edu.ecnu.imc.bsma.db.MySQLDBImpl");
	}

	public static String getDBImpl(byte idx) {
		return dbImpls.get(idx);
	}

	public boolean isCustDB() {
		return dbImpl == USER_CUSTDB;
	}

	Class dbClass = null;

	public String getDBImpl() {
		String ret = getDBImpl(dbImpl);
		if (ret == null) {
			ret = custDbImpl;
		}
		return ret;
	}

	/**
	 * load the db class
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 */
	public Class getDBClass() throws ClassNotFoundException {
		if (dbClass == null) {
			if (this.isCustDB()) {
				try {
					JarLoader.loadClass(Config.instance.getJarDir(), jars,
							this.getCustDbImpl());
				} catch (MalformedURLException e) {
					// TODO copy jar files
				}
			} else {
				ClassLoader classLoader = JobInfo.class.getClassLoader();
				dbClass = classLoader.loadClass(getDBImpl());
			}
		}
		return dbClass;
	}

	public Properties getProperties(Properties properties) {
		Properties ret = new Properties(properties);
		if (props != null) {
			for (Entry<String, String> entry : props.entrySet()) {
				ret.setProperty(entry.getKey(), entry.getValue());
			}
		}
		for (Query query : queries) {
			ret.setProperty(String.format("query%dproportion", query.getQID()),
					Double.toString(query.getFrac()));
		}
		return ret;
	}

	public byte getState() {
		return state;
	}

	public void save() throws SQLException {
		dao.insertJobInfo(this);
	}

	public void setBasicJobs(List<BasicJobInfo> subJobs) {
		for (BasicJobInfo basic : subJobs) {
			this.addToSubJobs(basic);
		}
	}

	@Override
	public void addToSubJobs(SubJob subJob) {
		super.addToSubJobs(new BasicJobInfo(this, subJob, dao));
	}

	public void addBasicJob(BasicJobInfo subjob) {
		subjob.setJobInfo(this);
		super.addToSubJobs(subjob);
	}

	public boolean haveExited() {
		return state == CANCEL || state == FINISH;
	}

	/**
	 * 退出时的信息
	 * 
	 * @return
	 */
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Query getQuery(int i) {
		Query ret = null;
		for (Query query : queries) {
			if (query.getQID() == i) {
				ret = query;
				break;
			}
		}
		return ret;
	}

	@Override
	public List<String> getJars() {
		if (jars == null) {
			return new ArrayList<String>();
		}
		return jars;
	}
}
