package edu.ecnu.imc.bsma.dao;

import java.util.Map;

import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class BasicJobInfo {
	public int subJobID;
	public int threadCount;
	public int opCount;
	public Map<Integer, Double> queryFracs;

	public BasicJobInfo() {

	}

	public BasicJobInfo(JSONObject obj) throws JSONException {
		subJobID = obj.getInt("id");
		threadCount = obj.getInt("tNum");
		opCount = obj.getInt("ops");

		JSONObject map = obj.getJSONObject("qfrac");
		JSONArray arr = map.names();
		for (int i = 0; i < arr.length(); i++) {
			int qID = arr.getInt(i);
			queryFracs.put(qID, map.getDouble(Integer.toString(qID)));
		}

	
	}

	public JSONObject asJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("id", subJobID);
		obj.put("tNum", threadCount);
		obj.put("ops", opCount);
		obj.put("qfrac", queryFracs);

		return obj;
	}
}
