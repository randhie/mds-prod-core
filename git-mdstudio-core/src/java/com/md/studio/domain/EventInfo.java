package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

public class EventInfo implements Serializable{
	private static final long serialVersionUID = 6843754012629673336L;
	
	private long eventId;
	private String eventName;
	private String description;
	private Date eventDate;
	private String eventBannerLocation;
	private Date createDate;
	private String createdBy;
	private Date updateDate;
	private String updatedBy;
	private String eventWebUrl;
	private String eventArchiveUrl;
	private byte[] eventFile;
	
	
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public String getEventBannerLocation() {
		return eventBannerLocation;
	}
	public void setEventBannerLocation(String eventBannerLocation) {
		this.eventBannerLocation = eventBannerLocation;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getEventWebUrl() {
		return eventWebUrl;
	}
	public void setEventWebUrl(String eventWebUrl) {
		this.eventWebUrl = eventWebUrl;
	}
	public String getEventArchiveUrl() {
		return eventArchiveUrl;
	}
	public void setEventArchiveUrl(String eventArchiveUrl) {
		this.eventArchiveUrl = eventArchiveUrl;
	}
	public byte[] getEventFile() {
		return eventFile;
	}
	public void setEventFile(byte[] eventFile) {
		this.eventFile = eventFile;
	}
}
