package edu.ecnu.imc.bsma.dao;

import rpc.SubJob;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class BasicJobInfo extends SubJob {

	public BasicJobInfo(SubJob subJob) {
		super(subJob);
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
