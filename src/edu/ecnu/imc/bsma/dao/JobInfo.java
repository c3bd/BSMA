package edu.ecnu.imc.bsma.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class JobInfo {
	public int jobID;
	public String dbWrapper;
	public int workload;
	public List<BasicJobInfo> subJobs;
	public Map<String, String> props;

	public JobInfo(JSONObject obj) throws JSONException {
		jobID = obj.getInt("id");
		JSONArray arr = obj.getJSONArray("subjobs");
		for (int i = 0; i < arr.length(); i++) {
			subJobs.add(new BasicJobInfo(arr.getJSONObject(i)));
		}

		JSONObject map = obj.getJSONObject("props");
		arr = map.names();
		for (int i = 0; i < arr.length(); i++) {
			String key = arr.getString(i);
			props.put(key, map.getString(key));
		}
	}

	private static HashMap<Integer, String> workloads = new HashMap<Integer, String>();
	static {
		workloads.put(0, "edu.ecnu.imc.bsma.workloads.CoreWorkload");
	}

	public String getWorkload(int idx) {
		return workloads.get(idx);
	}
}
