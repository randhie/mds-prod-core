package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

public class PhotoUploadDirectory implements Serializable {
	private static final long serialVersionUID = 7615895617548414227L;
	
	private long uploadId;
	private String baseFolder;
	private String parentFolder;
	private String childFolder;
	private Date createdDate;
	private String createdBy;
	private boolean stillValid = true;
	private Date validFrom;
	private Date validTo;
	
	public long getUploadId() {
		return uploadId;
	}
	public void setUploadId(long uploadId) {
		this.uploadId = uploadId;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public boolean isStillValid() {
		return stillValid;
	}
	public void setStillValid(boolean stillValid) {
		this.stillValid = stillValid;
	}
	public Date getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	public Date getValidTo() {
		return validTo;
	}
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
}
