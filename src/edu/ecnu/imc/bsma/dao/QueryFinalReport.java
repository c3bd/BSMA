package edu.ecnu.imc.bsma.dao;

import net.sf.json.JSONObject;

public class QueryFinalReport {
	public int subjobid; // identifier
	public int queryid;
	public int latency50;
	public int latency75;
	public int latency95;
	public int latency99;
	public float avglatency;
	public float minlatency;
	public float maxlatency;

	public int getSubjobid() {
		return subjobid;
	}

	public void setSubjobid(int subjobid) {
		this.subjobid = subjobid;
	}

	public int getQueryid() {
		return queryid;
	}

	public void setQueryid(int queryid) {
		this.queryid = queryid;
	}

	public int getLatency50() {
		return latency50;
	}

	public void setLatency50(int latency50) {
		this.latency50 = latency50;
	}

	public int getLatency75() {
		return latency75;
	}

	public void setLatency75(int latency75) {
		this.latency75 = latency75;
	}

	public int getLatency95() {
		return latency95;
	}

	public void setLatency95(int latency95) {
		this.latency95 = latency95;
	}

	public int getLatency99() {
		return latency99;
	}

	public void setLatency99(int latency99) {
		this.latency99 = latency99;
	}

	public float getAvglatency() {
		return avglatency;
	}

	public void setAvglatency(float avglatency) {
		this.avglatency = avglatency;
	}

	public float getMinlatency() {
		return minlatency;
	}

	public void setMinlatency(float minlatency) {
		this.minlatency = minlatency;
	}

	public float getMaxlatency() {
		return maxlatency;
	}

	public void setMaxlatency(float maxlatency) {
		this.maxlatency = maxlatency;
	}

	public JSONObject asJson() {
		return JSONObject.fromObject(this);
	}

	public static void main(String[] args) {
		System.out.println(new QueryFinalReport().asJson());
	}
}
