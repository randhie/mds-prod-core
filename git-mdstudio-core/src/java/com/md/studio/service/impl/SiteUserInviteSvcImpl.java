package com.md.studio.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.md.studio.dao.InvitesDao;
import com.md.studio.domain.EmailMessage;
import com.md.studio.domain.Invites;
import com.md.studio.domain.SiteUser;
import com.md.studio.service.EmailSenderSvc;
import com.md.studio.service.ServiceException;
import com.md.studio.service.SiteUserInviteSvc;

public class SiteUserInviteSvcImpl implements SiteUserInviteSvc {
	private EmailValidator emailValidator = EmailValidator.getInstance();
	private static final Logger LOGGER = Logger.getLogger(SiteUserInviteSvcImpl.class);
	private InvitesDao invitesDao;
	private EmailSenderSvc emailSenderSvc;
	private String templateName;
	private String url;

	@Transactional
	@Override
	public void invite(SiteUser inviter, String inviteesEmailAdd) {
		if (!emailValidator.isValid(inviteesEmailAdd)) {
			throw new ServiceException("Email Address not valid.", "EMAIL.NOTVALID");
		}
		
		try {
			String inviteeCode = String.valueOf(System.currentTimeMillis());
			Invites invites = invitesDao.select(inviter.getUserId(), inviteesEmailAdd);
			if (invites != null) {
				processInvites(inviter, invites);
				LOGGER.info("Email: " + inviteesEmailAdd + " was already invited by " + inviter.getFullName() + ". Sending email invites again.");
				return;
			}
			
			invites = new Invites();
			invites.setInviteeCode(inviteeCode);
			invites.setInviteeEmailAddress(inviteesEmailAdd);
			invites.setUserId(inviter.getUserId());
			invites.setInviteDate(new Date());
			
			invitesDao.insert(invites);
			processInvites(inviter, invites);
			LOGGER.info("Email invites to " + inviteesEmailAdd + " was sent by " + inviter.getFullName());
		}catch(ServiceException se) {
			throw new ServiceException(se.getMessage(), se.getErrorCode());
		}

	}
	
	
	private void processInvites(SiteUser inviter, Invites invites) {
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setTemplate(templateName);
		emailMessage.setToEmail(invites.getInviteeEmailAddress());
		emailMessage.setUseShawMail(true);
		
		if (inviter != null) {
			emailMessage.setFromEmail(inviter.getEmailAddress());
			emailMessage.setFromName(inviter.getFullName());
			emailMessage.setSentBy(inviter.getFullName());
		}
		
		Map<String, String> messageParams = new HashMap<String, String>();
		messageParams.put("emailAddress", invites.getInviteeEmailAddress());
		messageParams.put("inviteesCode", invites.getInviteeCode());
		messageParams.put("url", url);
		
		emailMessage.setMessageParams(messageParams);
		emailMessage.setUseTemplate(true);
		emailSenderSvc.sendEmail(emailMessage, null);
	}
	
	public void setEmailSenderSvc(EmailSenderSvc emailSenderSvc) {
		this.emailSenderSvc = emailSenderSvc;
	}
	public void setInvitesDao(InvitesDao invitesDao) {
		this.invitesDao = invitesDao;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
