package edu.ecnu.imc.bsma.dao;


import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * the output of the query status during the runtime of a benchmark job
 * 
 * @author xiafan
 * 
 */
public class RunningReport {
	public int subJobId;
	public long time;
	public int totalOps;
	public float curThroughput;
	public List<QueryRunningReport> qStatus = new ArrayList<QueryRunningReport>();

	public JSONObject asJson() throws JSONException {
		return JSONObject.fromObject(this);
	}

	public int getSubJobId() {
		return subJobId;
	}

	public void setSubJobId(int subJobId) {
		this.subJobId = subJobId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getTotalOps() {
		return totalOps;
	}

	public void setTotalOps(int totalOps) {
		this.totalOps = totalOps;
	}

	public float getCurThroughput() {
		return curThroughput;
	}

	public void setCurThroughput(float curThroughput) {
		this.curThroughput = curThroughput;
	}

	public List<QueryRunningReport> getqStatus() {
		return qStatus;
	}

	public void setqStatus(List<QueryRunningReport> qStatus) {
		this.qStatus = qStatus;
	}

	public static void main(String[] args) throws JSONException {
		RunningReport rp = new RunningReport();
		rp.subJobId = 1;
		// rp.seqId = 1;
		rp.time = 1;

		rp.totalOps = 1;
		// rp.throughput = 1;

		QueryRunningReport qRt = new QueryRunningReport();
		qRt.avgLatency = 1;
		qRt.queryId = 1;
		System.out.println(qRt.asJson().toString());
		rp.qStatus.add(qRt);
		System.out.println(rp.asJson().toString());
		System.out.println(JSONObject.fromObject(rp));
	}
}
