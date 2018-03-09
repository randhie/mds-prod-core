package com.md.studio.service.impl;
import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;

import com.md.studio.dao.EmailLogDao;
import com.md.studio.domain.EmailLog;
import com.md.studio.domain.EmailMessage;
import com.md.studio.domain.EmailTemplate;
import com.md.studio.dto.EmailProcessDto;
import com.md.studio.service.EmailSenderSvc;
import com.md.studio.service.EmailTemplateSvc;
import com.md.studio.service.ServiceException;
import com.md.studio.utils.JmsMessageSender;

public class EmailSenderSvcImpl implements EmailSenderSvc {
	private static final Logger LOGGER = Logger.getLogger(EmailSenderSvcImpl.class);
	private JavaMailSender javaMailSender;
	private JavaMailSender javaMailJetSender;
	private JavaMailSender javaInternalMailSender;
	private String tempFolder;
	private boolean async = true;
	private String emailQueueName;
	private String url;
	private JmsMessageSender jmsMessageSender;

	private EmailTemplateSvc emailTemplateSvc;
	private EmailLogDao emailLogDao;
	
	@Override
	public void sendEmail(EmailMessage emailMessage, List<MultipartFile> attachments) {
		populateEmailTemplate(emailMessage);
		
		EmailProcessDto emailProcess = new EmailProcessDto();
		if (attachments != null && attachments.size() > 0) {
			for (MultipartFile mpf: attachments) {
				if (mpf.getSize() == 0) {
					continue;
				}
				long sizeInMb = mpf.getSize() / (1024 * 1024);
				if (sizeInMb > 10) {
					throw new ServiceException("File is too big. Please upload file less than 10MB. File size uploaded is " + sizeInMb + "MB", "FILE.SIZENOTALLOWED");
				}
			}
			emailProcess.setAttachments(attachments);
		}
		
		emailProcess.setEmailMessage(emailMessage);
		
		if (async) {
			jmsMessageSender.sendToQueue(emailQueueName, emailProcess);
		}
		else {
			processEmail(emailProcess);
		}
	}
	
	
	private void populateEmailTemplate(EmailMessage emailMessage) {
		if (!emailMessage.isUseTemplate()) {
			return;
		}
		
		EmailTemplate emailTemplate = emailTemplateSvc.getTemplateByCode(emailMessage.getTemplate());
		if (emailTemplate == null) {
			return;
		}
		
		emailMessage.setSubject(emailTemplate.getEmailSubject());
		Map<String, String> messageParams = emailMessage.getMessageParams();
		messageParams.put("supportEmail", emailTemplate.getEmailFrom());
		messageParams.put("url", url);
		
		if (emailMessage.isFromEmailBlank()) {
			emailMessage.setFromEmail(emailTemplate.getEmailFrom());
			emailMessage.setFromName(emailTemplate.getEmailFromName());
			emailMessage.setSentBy(emailTemplate.getEmailFromName());
		}
				
		VelocityContext vc = new VelocityContext(emailMessage.getMessageParams());
		StringWriter sw = new StringWriter();
		Velocity.evaluate(vc, sw, emailTemplate.getTemplateCode(), emailTemplate.getTemplateHtml());
		emailMessage.setMessageBody(sw.toString());
	}
	
	
	
	
	@Override
	public void processEmail(EmailProcessDto emailProcessDto) {
		EmailMessage emailMessage = emailProcessDto.getEmailMessage();
		List<MultipartFile> attachments = emailProcessDto.getAttachments();
		MimeMessage message = null;
		
		if (emailMessage.isUseInternalEmail()) {
			message = javaInternalMailSender.createMimeMessage();
			LOGGER.info("Sending Email using local mail server");
		}
		else if (emailMessage.isUseShawMail()) {
			message = javaMailSender.createMimeMessage();
			LOGGER.info("Sending Email using Shaw mail server");
		}
		else  {
			message = javaMailJetSender.createMimeMessage();
			LOGGER.info("Sending Email using MailJet");	
		}
		
		try {
			MimeMessageHelper msgHelper = new MimeMessageHelper(message, true);
			msgHelper.setFrom(emailMessage.getFromEmail(), emailMessage.getFromName());
			msgHelper.setTo(emailMessage.getToEmail());
			msgHelper.setSubject(emailMessage.getSubject());
			msgHelper.setText(emailMessage.getMessageBody(), emailMessage.isHtml());
			if (attachments != null && attachments.size() > 0) {
				for (MultipartFile mpf: attachments) {
					if (mpf.getSize() == 0) {
						continue;
					}
					mpf.transferTo(new File(tempFolder + "/" + mpf.getOriginalFilename()));
					
					try {
						Thread.sleep(1000);
					}catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}

					File tempFile = new File(tempFolder + "/" + mpf.getOriginalFilename());
					msgHelper.addAttachment(tempFile.getName(), tempFile);
				}
			}
			
			if (emailMessage.isUseMailJet()) {
				javaMailJetSender.send(message);
			}
			else {
				javaMailSender.send(message);	
			}
			
			
			if (attachments != null && attachments.size() > 0 ) {
				removeTempFiles(attachments);	
			}
			
			EmailLog emailLog = new EmailLog();
			emailLog.setDateSent(new Date());
			emailLog.setEmailMessage(emailMessage.getMessageBody());
			emailLog.setEmailSubject(emailMessage.getSubject());
			emailLog.setFromEmail(emailMessage.getFromEmail());
			emailLog.setFromName(emailMessage.getFromName());
			emailLog.setToEmail(emailMessage.getToEmail());
			emailLog.setToName(emailMessage.getToEmail());
			emailLog.setWithAttachments(attachments != null? true: false);
			emailLogDao.insert(emailLog);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	
	private void removeTempFiles(List<MultipartFile> attachments) {
		if (attachments != null && attachments.size() > 0) {
			for (MultipartFile mpf: attachments) {
				if (mpf.getSize() == 0) {
					continue;
				}
				File tempFile = new File(tempFolder + "/" + mpf.getOriginalFilename());
				tempFile.delete();
			}
		}
	}
	
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}
	public void setAsync(boolean async) {
		this.async = async;
	}
	public void setEmailQueueName(String emailQueueName) {
		this.emailQueueName = emailQueueName;
	}
	public void setJmsMessageSender(JmsMessageSender jmsMessageSender) {
		this.jmsMessageSender = jmsMessageSender;
	}
	public void setJavaMailJetSender(JavaMailSender javaMailJetSender) {
		this.javaMailJetSender = javaMailJetSender;
	}
	public void setJavaInternalMailSender(JavaMailSender javaInternalMailSender) {
		this.javaInternalMailSender = javaInternalMailSender;
	}
	public void setEmailTemplateSvc(EmailTemplateSvc emailTemplateSvc) {
		this.emailTemplateSvc = emailTemplateSvc;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setEmailLogDao(EmailLogDao emailLogDao) {
		this.emailLogDao = emailLogDao;
	}
}
