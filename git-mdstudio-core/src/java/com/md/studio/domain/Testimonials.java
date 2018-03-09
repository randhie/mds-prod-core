package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

public class Testimonials implements Serializable{
	private static final long serialVersionUID = -8084900150467134330L;
	private long testimonialId;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String message;
	private String socialMediaAcct;
	private String urlAddress;
	private String browserInfo;
	private Date dateCreated;
	private Date updateDate;
	private String ipAddress;
	private long rate;
	
	
	public long getTestimonialId() {
		return testimonialId;
	}
	public void setTestimonialId(long testimonialId) {
		this.testimonialId = testimonialId;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSocialMediaAcct() {
		return socialMediaAcct;
	}
	public void setSocialMediaAcct(String socialMediaAcct) {
		this.socialMediaAcct = socialMediaAcct;
	}
	public String getUrlAddress() {
		return urlAddress;
	}
	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}
	public String getBrowserInfo() {
		return browserInfo;
	}
	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public long getRate() {
		return rate;
	}
	public void setRate(long rate) {
		this.rate = rate;
	}
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}
}
