package com.md.studio.json.jdu;

import java.util.ArrayList;
import java.util.List;

import com.md.studio.domain.PhotoUploadDirectory;
import com.md.studio.json.JsonContainer;
import com.md.studio.json.JsonData;

public class PhotoUploadDirectoryJdu {
	public static final String PHOTO_UPLOAD_DIRECTORY = "photoUploadDirectory";
	public static final String PHOTO_UPLOAD_DIRECTORY_LIST = "photoUploadDirectoryList";
	private static final String UPLOADID = "uploadId";
	private static final String BASEFOLDER = "baseFolder";
	private static final String PARENTFOLDER = "parentFolder";
	private static final String CHILDFOLDER = "childFolder";
	private static final String CREATEDDATE = "createdDate";
	private static final String CREATEDBY = "createdBy";
	private static final String STILLVALID = "stillValid";
	private static final String VALIDFROM = "validFrom";
	private static final String VALIDTO = "validTo";
	
	public static void buildJson(PhotoUploadDirectory pud, JsonContainer container, String objName) {
		if (pud == null) {
			return;
		}
		container.put(objName, getJsonData(pud));
	}
	
	public static void buildJson(List<PhotoUploadDirectory> pud, JsonContainer container, String objName) {
		if (pud == null || pud.isEmpty()) {
			return;
		}
		
		List<JsonData> jsonData = new ArrayList<JsonData>();
		for (PhotoUploadDirectory p: pud) {
			jsonData.add(getJsonData(p));
		}
		container.put(objName, jsonData);
	}
	
	
	private static JsonData getJsonData(PhotoUploadDirectory pud) {
		JsonData j = new JsonData();
		j.put(UPLOADID, pud.getUploadId());
		j.put(BASEFOLDER, pud.getBaseFolder());
		j.put(PARENTFOLDER, pud.getParentFolder());
		j.put(CHILDFOLDER, pud.getChildFolder());
		j.put(CREATEDDATE, pud.getCreatedDate().toString());
		j.put(CREATEDBY, pud.getCreatedBy());
		j.put(STILLVALID, pud.isStillValid());
		j.put(VALIDFROM, pud.getValidFrom().toString());
		j.put(VALIDTO, pud.getValidTo().toString());
		return j;
	}
	
	

}
