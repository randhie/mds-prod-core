package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

public class EmailTemplate implements Serializable {
	private static final long serialVersionUID = 4490141491404585251L;
	
	private String templateCode;
	private String descriptions;
	private String createdBy;
	private Date createdDate;
	private String templateHtml;
	private String emailSubject;
	private String emailFrom;
	private String emailFromName;
	
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getTemplateHtml() {
		return templateHtml;
	}
	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public String getEmailFromName() {
		return emailFromName;
	}
	public void setEmailFromName(String emailFromName) {
		this.emailFromName = emailFromName;
	}
}
