package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

public class SiteUserActivityLog implements Serializable {
	private static final long serialVersionUID = -5912174784640067973L;
	private long logId;
	private Date dateAccess;
	private String ipAddress;
	private String browserInfo;
	private String urlAccess;
	private String category;
	private String directory;
	private String fileName;
	private long categoryId;
	private long photoId;
	private String userId;
	private String whoIsRawInfo;
	
	public long getLogId() {
		return logId;
	}
	public void setLogId(long logId) {
		this.logId = logId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getBrowserInfo() {
		return browserInfo;
	}
	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}
	public String getUrlAccess() {
		return urlAccess;
	}
	public void setUrlAccess(String urlAccess) {
		this.urlAccess = urlAccess;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getDateAccess() {
		return dateAccess;
	}
	public void setDateAccess(Date dateAccess) {
		this.dateAccess = dateAccess;
	}
	public String getWhoIsRawInfo() {
		return whoIsRawInfo;
	}
	public void setWhoIsRawInfo(String whoIsRawInfo) {
		this.whoIsRawInfo = whoIsRawInfo;
	}
}
