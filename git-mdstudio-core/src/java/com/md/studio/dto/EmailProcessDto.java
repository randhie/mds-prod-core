package com.md.studio.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.md.studio.domain.EmailMessage;

public class EmailProcessDto implements Serializable {
	private static final long serialVersionUID = 2431622665057091281L;
	private List<MultipartFile> attachments;
	private EmailMessage emailMessage;

	public EmailMessage getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(EmailMessage emailMessage) {
		this.emailMessage = emailMessage;
	}
	public List<MultipartFile> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<MultipartFile> attachments) {
		this.attachments = attachments;
	}
}
