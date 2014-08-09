package edu.ecnu.imc.bsma.dao;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class QueryRunningReport {
	public int queryId;
	public double avgLatency;

	public JSONObject asJson() throws JSONException {
		// JSONObject obj = new JSONObject();
		// obj.put("qId", queryId);
		// obj.put("avgLatency", avgLatency);
		return JSONObject.fromObject(this);
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public double getAvgLatency() {
		return avgLatency;
	}

	public void setAvgLatency(double avgLatency) {
		this.avgLatency = avgLatency;
	}

	public static void main(String[] args) {
		QueryRunningReport t = new QueryRunningReport();
		System.out.println(t.asJson());
	}
}