package com.md.studio.event;

import java.io.Serializable;

public class SiteUserEvent implements Serializable {
	private static final long serialVersionUID = 7447380472828651800L;

	private String userId;
	private SiteUserEventType eventType;
	private Object eventObject;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public SiteUserEventType getEventType() {
		return eventType;
	}
	public void setEventType(SiteUserEventType eventType) {
		this.eventType = eventType;
	}
	public Object getEventObject() {
		return eventObject;
	}
	public void setEventObject(Object eventObject) {
		this.eventObject = eventObject;
	}
}
