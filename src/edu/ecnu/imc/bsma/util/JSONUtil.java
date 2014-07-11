package edu.ecnu.imc.bsma.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class JSONUtil {
	public static Map<String, String> toMap(String json) {
		Map<String, String> ret = new HashMap<String, String>();

		JSONObject obj;
		try {
			obj = new JSONObject(json);
			Iterator<String> keyIter = obj.keys();
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				String value = obj.getString(key);
				ret.put(key, value);
			}
		} catch (JSONException e) {
		}

		return ret;
	}

	public static List<String> toStringList(String json) {
		List<String> ret = new ArrayList<String>();
		try {
			JSONArray arr = new JSONArray(json);
			for (int i = 0; i < arr.length(); i++) {
				ret.add(arr.getString(i));
			}
		} catch (JSONException e) {
		}
		return ret;
	}
}
