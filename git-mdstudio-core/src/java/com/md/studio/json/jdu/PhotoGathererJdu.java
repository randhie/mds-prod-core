package com.md.studio.json.jdu;

import java.util.ArrayList;
import java.util.List;

import com.md.studio.domain.PhotoGatherer;
import com.md.studio.json.JsonContainer;
import com.md.studio.json.JsonData;

public class PhotoGathererJdu {
	
	public static void buildJson(JsonContainer container, List<PhotoGatherer> photoGatherer, String modelName, boolean justList) {
		if (photoGatherer == null || photoGatherer.isEmpty()) {
			return;
		}
		
		List<JsonData> jsonDataList = new ArrayList<JsonData>();
		for (PhotoGatherer pg: photoGatherer) {
			jsonDataList.add(getJson(pg, justList));
		}
		
		container.put(modelName, jsonDataList);
	}
	
	
	public static void buildJson(JsonContainer container, PhotoGatherer photoGatherer, String modelName, boolean justList) {
		if (photoGatherer == null) {
			return;
		}
		
		container.put(modelName, getJson(photoGatherer, justList));
	}
	
	
	
	public static JsonData getJson(PhotoGatherer photoGatherer, boolean justList) {
		JsonData j = new JsonData();
		j.put("categoryId", photoGatherer.getCategoryId());
		j.put("category", photoGatherer.getCategory());
		j.put("directory", photoGatherer.getDirectory());
		j.put("dateCreated", photoGatherer.getDateCreated().toString());
		j.put("albumDescription", photoGatherer.getAlbumDescription());
		j.put("albumCaptionName", photoGatherer.getAlbumCaptionName());
		j.put("categoryType", photoGatherer.getCategoryType());
		PhotoInfoJdu.buildJson(photoGatherer.getPhotoInfoList(), j, justList);

		return j;
	}
	
	

}
