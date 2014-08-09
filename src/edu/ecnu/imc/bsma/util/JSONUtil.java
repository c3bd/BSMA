package edu.ecnu.imc.bsma.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class JSONUtil {
	public static Map<String, String> toMap(String json) {
		Map<String, String> clazzMap = new HashMap<String, String>();
		return (Map<String, String>) JSONObject.toBean(
				JSONObject.fromObject(json), Map.class);

		/*
		 * Map<String, String> ret = new HashMap<String, String>();
		 * 
		 * JSONObject obj; try { obj = new JSONObject(json); Iterator<String>
		 * keyIter = obj.keys(); while (keyIter.hasNext()) { String key =
		 * keyIter.next(); String value = obj.getString(key); ret.put(key,
		 * value); } } catch (JSONException e) { }
		 * 
		 * return ret;
		 */
	}

	public static List<String> toStringList(String json) {
		return (List<String>) JSONArray.toList(JSONArray.fromObject(json),
				List.class);
		/*
		 * List<String> ret = new ArrayList<String>(); try { JSONArray arr = new
		 * JSONArray(json); for (int i = 0; i < arr.length(); i++) {
		 * ret.add(arr.getString(i)); } } catch (JSONException e) { } return
		 * ret;
		 */
	}

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("a", "b");
		System.out.println(JSONUtil
				.toMap(JSONObject.fromObject(map).toString()));
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		System.out.println(JSONArray.fromObject(list).toString());
		System.out.println(list);
		System.out.println(JSONUtil.toStringList(JSONArray.fromObject(list)
				.toString()));
	}
}
