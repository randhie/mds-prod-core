package com.md.studio.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.md.studio.dao.InvitesDao;
import com.md.studio.domain.EmailMessage;
import com.md.studio.domain.Invites;
import com.md.studio.domain.SiteUser;
import com.md.studio.service.EmailSenderSvc;
import com.md.studio.service.ServiceException;
import com.md.studio.service.SignupUserSvc;
import com.md.studio.service.SiteUserInfoSvc;
import com.restfb.util.StringUtils;

public class SignupUserSvcImpl implements SignupUserSvc {
	private SiteUserInfoSvc siteUserInfoSvc;
	
	private InvitesDao invitesDao;
	private EmailSenderSvc emailSenderSvc;
	private String templateName;
	private String url;
	private String supportEmail;
	
	@Transactional
	@Override
	public void createUser(SiteUser user, String inviteesCode) {
		if (user == null) {
			throw new ServiceException("Cannot find user to create", "SITEUSER.NOTFOUND");
		}
		
		try {
			if (!StringUtils.isBlank(inviteesCode)) {
				Invites invites = invitesDao.selectByInviteeCode(inviteesCode);
				if (invites != null) {
					user.setHasInvited(true);
				}
			}
			// create user
			siteUserInfoSvc.createUser(user);
			
			// send activation code to the user
			EmailMessage emailMessage = new EmailMessage();
			emailMessage.setTemplate(templateName);
			emailMessage.setToEmail(user.getEmailAddress());
			emailMessage.setUseShawMail(true);
			emailMessage.setMailServer();
			
			Map<String, String> messageParams = new HashMap<String, String>();
			messageParams.put("firstName", user.getFirstName());
			messageParams.put("activationCode", user.getActivationCode());
			messageParams.put("supportEmail", supportEmail);
			messageParams.put("url", url);
			
			emailMessage.setMessageParams(messageParams);
			emailMessage.setUseTemplate(true);
			emailSenderSvc.sendEmail(emailMessage, null);
			
		}
		catch(ServiceException se) {
			throw new ServiceException(se.getMessage(), se.getErrorCode());
		}
	}

	@Override
	public void activateUser(String activationCode) {
		try {
			siteUserInfoSvc.activateUser(activationCode);
		}
		catch(ServiceException se) {
			throw new ServiceException(se.getMessage(), se.getErrorCode());
		}
	}

	
	public void setSiteUserInfoSvc(SiteUserInfoSvc siteUserInfoSvc) {
		this.siteUserInfoSvc = siteUserInfoSvc;
	}
	public void setInvitesDao(InvitesDao invitesDao) {
		this.invitesDao = invitesDao;
	}
	public void setEmailSenderSvc(EmailSenderSvc emailSenderSvc) {
		this.emailSenderSvc = emailSenderSvc;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}
}
