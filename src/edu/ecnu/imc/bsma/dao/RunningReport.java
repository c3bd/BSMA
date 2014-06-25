package edu.ecnu.imc.bsma.dao;

import java.util.ArrayList;
import java.util.List;

import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

/**
 * the output of the query status during the runtime of a benchmark job
 * 
 * @author xiafan
 * 
 */
public class RunningReport {
	public int subJobId;
	public int seqId;
	public long time;
	public int totalOps;
	public double throughput;

	public List<QueryRunningReport> qStatus = new ArrayList<QueryRunningReport>();

	public JSONObject asJson() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("id", subJobId);
		obj.put("seq", seqId);
		obj.put("time", time);
		obj.put("totalOps", totalOps);
		obj.put("throughput", throughput);

		JSONArray arr = new JSONArray();
		obj.put("qStatus", arr);
		for (QueryRunningReport status : qStatus) {
			arr.put(status.asJson());
		}

		return obj;
	}

	public static void main(String[] args) throws JSONException {
		RunningReport rp = new RunningReport();
		rp.subJobId = 1;
		rp.seqId = 1;
		rp.time = 1;
		;
		rp.totalOps = 1;
		rp.throughput = 1;

		QueryRunningReport qRt = new QueryRunningReport();
		qRt.avgLatency = 1;
		qRt.queryId = 1;
		System.out.println(qRt.asJson().toString());
		rp.qStatus.add(qRt);
		System.out.println(rp.asJson().toString());
	}
}
