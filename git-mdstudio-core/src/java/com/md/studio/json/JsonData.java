package com.md.studio.json;

import java.io.Serializable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonData implements Serializable{
	private static final long serialVersionUID = 7962175971954295739L;

	private JSONObject jsonObject = new JSONObject();
	
	public void put(String attrName, Object attrValue){
		if (attrValue instanceof JsonData) {
			jsonObject.put(attrName, ((JsonData) attrValue).jsonObject);
		}
		else {
			jsonObject.put(attrName, attrValue);
		}
	}
	
	public void put(String attrName, List<JsonData> jsonList) {
		JSONArray jsonArray = new JSONArray();
		for (JsonData json: jsonList) {
			jsonArray.add(json.toJsonString());
		}
		jsonObject.put(attrName, jsonArray);
	}
	
	
	public String getJsonAttribs(String attrib){
		if (jsonObject.get(attrib) == null) {
			return null;
		}
		else {
			return jsonObject.get(attrib).toString();
		}
	}
	
	public void removeJsonAttribs(String attrib){
		if (jsonObject.get(attrib) != null) {
			jsonObject.remove(attrib);
		}
	}
	
	public String toJsonString(){
		return jsonObject.toString();
	}
}
