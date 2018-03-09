package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

public class EventRequest implements Serializable {
	private static final long serialVersionUID = -6039831268516482633L;

	private long requestId;
	private String emailAddress;
	private String fullName;
	private String contactNumber;
	private Date desiredDate;
	private String eventLocation;
	private String eventBudget;
	private String message;
	private String notes;
	
	private Date createDate;
	private boolean hasContacted;
	private boolean hasAccomplished;
	private Date contactedDate;
	private Date accomplishedDate;
	private Date cancelledDate;
	private boolean hasCancelled;
	
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Date getDesiredDate() {
		return desiredDate;
	}
	public void setDesiredDate(Date desiredDate) {
		this.desiredDate = desiredDate;
	}
	public String getEventLocation() {
		return eventLocation;
	}
	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}
	public String getEventBudget() {
		return eventBudget;
	}
	public void setEventBudget(String eventBudget) {
		this.eventBudget = eventBudget;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public boolean isHasContacted() {
		return hasContacted;
	}
	public void setHasContacted(boolean hasContacted) {
		this.hasContacted = hasContacted;
	}
	public boolean isHasAccomplished() {
		return hasAccomplished;
	}
	public void setHasAccomplished(boolean hasAccomplished) {
		this.hasAccomplished = hasAccomplished;
	}
	public Date getContactedDate() {
		return contactedDate;
	}
	public void setContactedDate(Date contactedDate) {
		this.contactedDate = contactedDate;
	}
	public Date getAccomplishedDate() {
		return accomplishedDate;
	}
	public void setAccomplishedDate(Date accomplishedDate) {
		this.accomplishedDate = accomplishedDate;
	}
	public Date getCancelledDate() {
		return cancelledDate;
	}
	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	public boolean isHasCancelled() {
		return hasCancelled;
	}
	public void setHasCancelled(boolean hasCancelled) {
		this.hasCancelled = hasCancelled;
	}
}
