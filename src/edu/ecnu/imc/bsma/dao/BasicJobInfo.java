package edu.ecnu.imc.bsma.dao;

import rpc.SubJob;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class BasicJobInfo extends SubJob {
	public static final byte WAITING = 0;
	public static final byte RUNNING = 0;
	public static final byte STOP = 0;
	public static final byte CANCEL = 0;

	public byte state = WAITING;// waiting, running, stop, cancel
	JobInfo jobInfo;

	public BasicJobInfo(JobInfo jobInfo, SubJob subJob) {
		super(subJob);
		this.jobInfo = jobInfo;
	}

	public JobInfo getJobInfo() {
		return jobInfo;
	}

	/**
	 * @return the state
	 */
	public byte getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(byte state) {
		this.state = state;
	}

	public BasicJobInfo(JSONObject obj) throws JSONException {
		subJobID = obj.getInt("id");
		threadNum = (short) obj.getInt("tNum");
		opCount = obj.getInt("ops");
	}

	public JSONObject asJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("id", subJobID);
		obj.put("tNum", threadNum);
		obj.put("ops", opCount);

		return obj;
	}
}
