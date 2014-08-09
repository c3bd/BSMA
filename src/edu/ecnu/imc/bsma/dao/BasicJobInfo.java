package edu.ecnu.imc.bsma.dao;

import java.sql.SQLException;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import rpc.SubJob;

public class BasicJobInfo extends SubJob {
	public byte state = JobInfo.WAITING;// waiting, running, finish, cancel
	JobInfo jobInfo;
	Dao dao;

	public BasicJobInfo(Dao dao, Byte state) {
		this.dao = dao;
		this.state = state;
	}

	public BasicJobInfo(JobInfo jobInfo, SubJob subJob, Dao dao) {
		super(subJob);
		this.jobInfo = jobInfo;
		this.dao = dao;
	}

	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}

	/**
	 * @return the state
	 */
	public byte getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 * @throws SQLException
	 */
	public void setState(byte state) throws SQLException {
		this.state = state;
		if (dao != null) {
			dao.updateSubJobStatus(this);
		}
	}
	
	public void setStateLocal(byte state) {
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
