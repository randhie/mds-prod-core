package com.md.studio.domain;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class EmailMessage implements Serializable {
	private static final long serialVersionUID = 7648912494322229797L;
	private static final String HTML_ELEMENT = "<html><head><title>MDSnapShots.com</title></head><body>${msg}</body/></html>";
	private static final String SUFFIX_EMAIL_MDSNAPSHOTS = "mdsnapshots.com";
	
	private String fromEmail;
	private String fromName;
	private String toEmail;
	private String subject;
	private String template;
	private String messageBody;
	private Map<String, String> messageParams;
	private boolean isHtml = true;
	private boolean useTemplate = false;
	private String sentBy;
	private File[] attachment;
	private boolean useMailJet = false;
	private boolean useInternalEmail = false;
	private boolean useShawMail = false;
	
	
	public boolean isUseShawMail() {
		return useShawMail;
	}
	public void setUseShawMail(boolean useShawMail) {
		this.useShawMail = useShawMail;
	}
	public boolean isUseInternalEmail() {
		return useInternalEmail;
	}
	public void setUseInternalEmail(boolean useInternalEmail) {
		this.useInternalEmail = useInternalEmail;
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		String msgBody = messageBody.replaceAll("(\r\n)", "<br />");
		this.messageBody = StringUtils.replace(HTML_ELEMENT, "${msg}", msgBody);
	}
	public Map<String, String> getMessageParams() {
		return messageParams;
	}
	public void setMessageParams(Map<String, String> messageParams) {
		this.messageParams = messageParams;
	}
	public boolean isHtml() {
		return isHtml;
	}
	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}
	public boolean isUseTemplate() {
		return useTemplate;
	}
	public void setUseTemplate(boolean useTemplate) {
		this.useTemplate = useTemplate;
	}
	public String getSentBy() {
		return sentBy;
	}
	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}
	public File[] getAttachment() {
		return attachment;
	}
	public void setAttachment(File[] attachment) {
		this.attachment = attachment;
	}
	public boolean isUseMailJet() {
		return useMailJet;
	}
	public void setUseMailJet(boolean useMailJet) {
		this.useMailJet = useMailJet;
	}
	
	public boolean isFromEmailBlank(){
		if (StringUtils.isBlank(fromEmail) && StringUtils.isBlank(fromName)) {
			return true;
		}
		return false;
	}
	
	public void setMailServer() {
		if (StringUtils.endsWith(this.getFromEmail(), SUFFIX_EMAIL_MDSNAPSHOTS) &&
			StringUtils.endsWith(this.getToEmail(), SUFFIX_EMAIL_MDSNAPSHOTS)) {
			
			this.setToEmail("randolph.ordinario@gmail.com");
		}
	}
}
