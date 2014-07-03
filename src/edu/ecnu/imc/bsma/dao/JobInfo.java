package edu.ecnu.imc.bsma.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import rpc.Job;
import rpc.Query;
import rpc.SubJob;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

/**
 * a job may consists of multiple sub-jobs, web ui pass json format storing the
 * jobinfo
 * 
 * @author xiafan
 * 
 */
public class JobInfo extends Job {
	// public int jobID;
	// public byte dbImpl; // the implementation of database access class
	// public List<Query> queries;
	// public List<BasicJobInfo> subJobs;
	// public Map<String, String> props;
	// public String userDbImpl; // user provided db access implementation
	List<BasicJobInfo> jobUnits = new ArrayList<BasicJobInfo>();

	public JobInfo(Job job) {
		super(job);
		for (SubJob subJob : job.getSubJobs()) {
			jobUnits.add(new BasicJobInfo(this, subJob));
		}
	}

	public List<BasicJobInfo> getJobUnits() {
		return jobUnits;
	}

	public JobInfo(JSONObject obj) throws JSONException {
		jobID = obj.getInt("id");
		JSONArray arr = obj.getJSONArray("subjobs");

		for (int i = 0; i < arr.length(); i++) {
			subJobs.add(new BasicJobInfo(arr.getJSONObject(i)));
		}

		JSONObject map = obj.getJSONObject("queries");
		arr = map.names();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject qObj = arr.getJSONObject(i);
			queries.add(new Query((byte) qObj.getInt("type"), qObj
					.getDouble("frac")));
		}

		map = obj.getJSONObject("props");
		arr = map.names();
		for (int i = 0; i < arr.length(); i++) {
			String key = arr.getString(i);
			props.put(key, map.getString(key));
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
	public static final byte HIVE_DB = 0;
	public static final byte SHARK_DB = 1;
	public static final byte MONETDB_DB = 2;
	static {
		dbImpls.put(HIVE_DB, "edu.ecnu.imc.bsma.dbimpl.HiveDBImpl");
		dbImpls.put(SHARK_DB, "edu.ecnu.imc.bsma.dbimpl.SparkDBImpl");
		dbImpls.put(MONETDB_DB, "edu.ecnu.imc.bsma.dbimpl.MonetDBImpl");
	}

	public static String getDBImpl(byte idx) {
		return dbImpls.get(idx);
	}

	public static final String CUST_DBIMPL = "db.";

	public String getDBImpl() {
		String ret = getDBImpl(dbImpl);
		if (ret == null) {
			ret = custDbImpl;
		}
		return ret;
	}

	public Properties getProperties() {
		Properties ret = new Properties();
		for (Entry<String, String> entry : props.entrySet()) {
			ret.setProperty(entry.getKey(), entry.getValue());
		}
		return ret;
	}
}
