package edu.ecnu.imc.bsma.dao;

import java.util.List;

import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class JobInfo {
	public int jobID;
	public List<BasicJobInfo> subJobs;

	public JobInfo(JSONObject obj) throws JSONException {
		jobID = obj.getInt("id");
		JSONArray arr = obj.getJSONArray("subjobs");
		for (int i = 0; i < arr.length(); i++) {
			subJobs.add(new BasicJobInfo(arr.getJSONObject(i)));
		}
	}
}
