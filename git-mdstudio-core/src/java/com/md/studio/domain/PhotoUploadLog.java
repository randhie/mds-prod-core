package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

public class PhotoUploadLog implements Serializable {
	private static final long serialVersionUID = 6081505062046026521L;
	
	private long uploadId;
	private String userId;
	private String baseFolder;
	private String parentFolder;
	private String childFolder;
	private String fileName;
	private Date uploadDate;
	private long photoUploadDirId;
	
	public long getUploadId() {
		return uploadId;
	}
	public void setUploadId(long uploadId) {
		this.uploadId = uploadId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBaseFolder() {
		return baseFolder;
	}
	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}
	public String getParentFolder() {
		return parentFolder;
	}
	public void setParentFolder(String parentFolder) {
		this.parentFolder = parentFolder;
	}
	public String getChildFolder() {
		return childFolder;
	}
	public void setChildFolder(String childFolder) {
		this.childFolder = childFolder;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public long getPhotoUploadDirId() {
		return photoUploadDirId;
	}
	public void setPhotoUploadDirId(long photoUploadDirId) {
		this.photoUploadDirId = photoUploadDirId;
	}
}
