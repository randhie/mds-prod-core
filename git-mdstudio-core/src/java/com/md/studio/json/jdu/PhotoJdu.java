package com.md.studio.json.jdu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.md.studio.dto.Photo;
import com.md.studio.json.JsonData;

public class PhotoJdu {
	private static final String DIRECTORYNAME = "directoryName";
	private static final String FILENAME = "fileName";
	private static final String DATECREATED = "dateCreated";
	private static final String ALBUMCAPTIONNAME = "albumCaptionName";
	private static final String ALBUMDESCRIPTION = "albumDescription";
	private static final String PARENTDIRECTORY = "parentDirectory";
	private static final String PHOTOTYPE = "photoType";
	private static final String PHOTO = "photo";
	private static final String PHOTO_LIST = "photoList";
	
	public static void buildJson(Photo photo, JsonData container){
		container.put(PHOTO, getJsonData(photo));
	}
	
	public static void buildJson(List<Photo> photos, JsonData container) {
		if (photos == null || photos.isEmpty()) {
			return;
		}
		
		List<JsonData> jsonData = new ArrayList<JsonData>();
		for (Photo photo: photos) {
			if (!StringUtils.isBlank(photo.getAlbumCaptionName())) {
				if (StringUtils.isBlank(container.getJsonAttribs("captionName"))) {
					container.put("captionName", photo.getAlbumCaptionName());	
				}
			}
			else if (!StringUtils.isBlank(photo.getAlbumDescription())) {
				if (StringUtils.isBlank(container.getJsonAttribs("albumDescription"))) {
					container.put("albumDescription", photo.getAlbumDescription());
				}
			}
			jsonData.add(getJsonData(photo));
		}
		container.put(PHOTO_LIST, jsonData);
	}
	
	public static JsonData getJsonData(Photo photo) {
		JsonData jsonData = new JsonData();
		jsonData.put(DIRECTORYNAME, photo.getDirectoryName());
		jsonData.put(FILENAME, photo.getFileName());
		jsonData.put(DATECREATED, photo.getDateCreated() == null? null: photo.getDateCreated().toString());
		jsonData.put(ALBUMCAPTIONNAME, photo.getAlbumCaptionName());
		jsonData.put(ALBUMDESCRIPTION, photo.getAlbumDescription());
		jsonData.put(PARENTDIRECTORY, photo.getParentDirectory());
		jsonData.put(PHOTOTYPE, photo.getPhotoType());
		return jsonData;
	}
}
