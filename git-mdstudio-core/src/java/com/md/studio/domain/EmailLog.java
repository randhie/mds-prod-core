package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

public class EmailLog implements Serializable {
	private static final long serialVersionUID = -5239054912235937330L;

	private long emailLogId;
	private String fromEmail;
	private String fromName;
	private String toEmail;
	private String toName;
	private String emailSubject;
	private String emailMessage;
	private Date dateSent;
	private boolean withAttachments;
	
	public long getEmailLogId() {
		return emailLogId;
	}
	public void setEmailLogId(long emailLogId) {
		this.emailLogId = emailLogId;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}
	public Date getDateSent() {
		return dateSent;
	}
	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}
	public boolean isWithAttachments() {
		return withAttachments;
	}
	public void setWithAttachments(boolean withAttachments) {
		this.withAttachments = withAttachments;
	}
}
