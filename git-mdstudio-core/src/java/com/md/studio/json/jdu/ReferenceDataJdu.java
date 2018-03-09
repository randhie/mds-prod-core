package com.md.studio.json.jdu;

import java.util.ArrayList;
import java.util.List;

import com.md.studio.domain.ReferenceData;
import com.md.studio.json.JsonContainer;
import com.md.studio.json.JsonData;

public class ReferenceDataJdu {
	
	public static void buildJson(JsonContainer container, ReferenceData refData, String modelName) {
		if (refData == null) {
			return;
		}
		
		container.put(modelName, getJson(refData));
	}
	
	
	public static void buildJson(JsonContainer container, List<ReferenceData> refData, String modelName) {
		if (refData == null || refData.isEmpty()) {
			return;
		}
		
		List<JsonData> jsonData = new ArrayList<JsonData>();
		for (ReferenceData ref: refData) {
			jsonData.add(getJson(ref));
		}
		
		container.put(modelName, jsonData);
	}
	
	
	private static JsonData getJson(ReferenceData refData) {
		JsonData j = new JsonData();
		
		j.put("refType", refData.getRefType());
		j.put("refCode", refData.getRefCode());
		j.put("refvalue", refData.getRefValue());
		j.put("refValueParsed", refData.getParsePipeRefValue());
		
		return j;
	}
	
}
