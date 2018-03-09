package com.md.studio.json.jdu;

import java.util.ArrayList;
import java.util.List;

import com.md.studio.domain.PhotoInfo;
import com.md.studio.dto.Photo;
import com.md.studio.json.JsonContainer;
import com.md.studio.json.JsonData;

public class PhotoInfoJdu {
	
	public static void buildJson(JsonContainer container, List<PhotoInfo> photoInfo, String modelName) {
		if (photoInfo == null || photoInfo.isEmpty()) {
			return;
		}
		
		List<JsonData> jsonData = new ArrayList<JsonData>();
		for (PhotoInfo pi: photoInfo) {
			jsonData.add(getJson(pi));
		}
		
		container.put(modelName, jsonData);
	}
	
	public static void buildJson(JsonContainer container, PhotoInfo photoInfo, String modelName) {
		if (photoInfo == null) {
			return;
		}
		
		container.put(modelName, getJson(photoInfo));
	}
	
	
	public static void buildJson(List<PhotoInfo> photoInfo, JsonData jsonData, boolean justList) {
		if (photoInfo == null || photoInfo.isEmpty()) {
			return;
		}
		
		List<String> photoList = new ArrayList<String>();
		if (justList) {
			for (PhotoInfo photo: photoInfo) {
				photoList.add(photo.getFileName());
				photoList.add(Long.toString(photo.getPhotoId()));
			}
			jsonData.put("photoInfoList", photoList);
			return;
		}
		
		List<JsonData> jsonDataList = new ArrayList<JsonData>();		
		for (PhotoInfo photo: photoInfo) {
			jsonDataList.add(getJson(photo));
		}
		
		jsonData.put("photoInfoList", jsonDataList);
	}
	
	public static JsonData getJson(PhotoInfo photoInfo) {
		JsonData j = new JsonData();
		j.put("photoId", photoInfo.getPhotoId());
		j.put("categoryId", photoInfo.getCategoryId());
		j.put("fileName", photoInfo.getFileName());
		j.put("photoType", photoInfo.getPhotoType());
		return j;
	}

}
