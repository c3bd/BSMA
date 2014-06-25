package edu.ecnu.imc.bsma.dao;

import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class QueryRunningReport {
	public int queryId;
	public double avgLatency;

	public JSONObject asJson() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("qId", queryId);
		obj.put("avgLatency", avgLatency);

		return obj;
	}
}