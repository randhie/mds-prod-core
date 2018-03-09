package com.md.studio.service.impl;
import static com.md.studio.utils.WebConstants.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;

import com.md.studio.dao.SiteUserDao;
import com.md.studio.domain.EmailMessage;
import com.md.studio.domain.SiteUser;
import com.md.studio.domain.UserContext;
import com.md.studio.domain.enums.SiteUserStatus;
import com.md.studio.service.EmailSenderSvc;
import com.md.studio.service.ServiceException;
import com.md.studio.service.SiteUserInfoSvc;

public class SiteUserInfoSvcImpl implements SiteUserInfoSvc {
	private SiteUserDao siteUserDao;
	private EmailSenderSvc emailSenderSvc;
	private String passCodeEmailTemplate;

	@Override
	public SiteUser getUserByEmail(String emailAddress) {
		return siteUserDao.selectByEmail(emailAddress);
	}

	@Override
	public SiteUser getUserById(String userId) {
		return siteUserDao.selectById(userId);
	}
	
	@Override
	public SiteUser getUserByActivationCode(String activationCode) {
		return siteUserDao.selectByActivationCode(activationCode);
	}
	
	

	@Override
	public List<SiteUser> getAllSiteUser() {
		return siteUserDao.selectAllUser();
	}

	@Override
	public void createUser(SiteUser user) {
		SiteUser currentUser = getUserByEmail(user.getEmailAddress());
		if (currentUser != null) {
			throw new ServiceException("This customer already exists", "EMAIL.EXIST");
		}
		
		boolean idExist = true;
		boolean activationCodeExist = true;
		
		while(activationCodeExist) {
			String activationCode = RandomStringUtils.randomAlphanumeric(30);
			currentUser = getUserByActivationCode(activationCode);
			
			if (currentUser == null) {
				user.setActivationCode(activationCode);
				activationCodeExist = false;
			}
		}
		
		while(idExist) {
			String idGenerated = RandomStringUtils.randomAlphanumeric(23);
			SiteUser existingUser = getUserById(SITEUSER_ID_PREFIX + idGenerated);
			if (existingUser == null) {
				idExist = false;
				user.setUserId(SITEUSER_ID_PREFIX + idGenerated);
				user.setSignupDate(new Date());
				user.setUserStatus(SiteUserStatus.PENDINGACTIVATION);
				siteUserDao.insert(user);
				
				if (UserContext.isAdmin()) {
					activateUser(user.getActivationCode());
				}
			}
		}
	}
	

	@Override
	public void updatePassCode(SiteUser user, String newPassCode, String currentPassCode) {
		if (user == null) {
			throw new ServiceException("User not found. Nothing to update", "USER.NOTFOUND");
		}

		SiteUser currentUser = getUserById(user.getUserId());
		if (currentUser == null) {
			throw new ServiceException("User not found. Nothing to update", "USER.NOTFOUND");
		}
		
		if (currentPassCode == null || currentPassCode.isEmpty() || newPassCode == null || newPassCode.isEmpty()) {
			throw new ServiceException("Cannot find password to update", "PASSWORD.NOTFOUND");
		}
		
		boolean isPasswordValid = isPassCodeValid(currentUser.getEmailAddress(), currentPassCode);
		if (isPasswordValid) {
			currentUser.setPasscode(newPassCode);
			siteUserDao.updatePassCode(currentUser);
		}
		else {
			throw new ServiceException("Current password doesn't match our record.", "PASSWORD.NOTMATCH");
		}
	}

	@Override
	public void assignAuthFolder(String userId, String authAlbums) {
		if (userId == null || userId.isEmpty()) {
			throw new ServiceException("Missing user ID. Login Required", "USERID.NOTFOUND");
		}
		SiteUser user = getUserById(userId);
		if (user == null) {
			throw new ServiceException("User not found associate with " + userId + " email address.", "USER.NOTFOUND");
		}
		
		user.setAuthAlbums(authAlbums);
		siteUserDao.update(user);
	}
	
	@Override
	public void activateUser(String activationCode) {
		if (activationCode == null || activationCode.isEmpty()) {
			throw new ServiceException("Missing parameter to activate account", "ACTIVATION.NOTFOUND");
		}
		
		SiteUser user = getUserByActivationCode(activationCode);
		if (user == null) {
			throw new ServiceException("Activation code incorrect. Please contact support.", "ACTIVATION.INCORRECT");
		}
		else if (user != null && user.getUserStatus() != SiteUserStatus.PENDINGACTIVATION) {
			throw new ServiceException("Cannot reuse activation code.", "ACTIVATION.REUSED");
		}
		
		user.setDateActivated(new Date());
		user.setUserStatus(SiteUserStatus.ACTIVE);
		siteUserDao.update(user);
	}

	@Override
	public boolean isPassCodeValid(String emailAddress, String rawPassCode) {
		if (emailAddress == null || emailAddress.isEmpty() || rawPassCode == null || rawPassCode.isEmpty()) {
			throw new ServiceException("Parameter Missing", "PARAM.NOTFOUND");
		}
		
		SiteUser user = getUserByEmail(emailAddress);
		if (user == null) {
			throw new ServiceException("Email address and/or password does not match our record.", "USER.NOTFOUND");
		}
		
		String encPassCode = siteUserDao.selectPassCode(rawPassCode, user.getPasscode());
		if (encPassCode.equals(user.getPasscode())) {
			return true;
		}
		return false;
	}
	
	
	@Override
	public void resetPassCode(SiteUser user) {
		if (user == null) {
			throw new ServiceException("This customer already exists", "EMAIL.EXIST");
		}
		
		String newPassCode = RandomStringUtils.randomAlphanumeric(20);
		user.setPasscode(newPassCode);
		siteUserDao.updatePassCode(user);
		
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setTemplate(passCodeEmailTemplate);
		emailMessage.setToEmail(user.getEmailAddress());
		emailMessage.setUseShawMail(true);
		emailMessage.setMailServer();
		
		Map<String, String> messageParams = new HashMap<String, String>();
		messageParams.put("firstName", user.getFirstName());
		messageParams.put("temporaryPassword", user.getPasscode());
		
		emailMessage.setMessageParams(messageParams);
		emailMessage.setUseTemplate(true);
		emailSenderSvc.sendEmail(emailMessage, null);
	}

	@Override
	public void updateLastLogin(SiteUser siteUser) {
		siteUser.setLastLogin(new Date());
		siteUserDao.updateLastLogin(siteUser);
	}
	
	
	

	@Override
	public void updatePassCodeByAdmin(SiteUser user, String newPassCode) {
		if (user == null) {
			throw new ServiceException("Site User not found.", "SITEUSER.NOTFOUND");
		}
		
		if (StringUtils.isBlank(newPassCode)) {
			throw new ServiceException("New password not found.", "PW.NOTFOUND");
		}
		
		user.setPasscode(newPassCode);
		siteUserDao.updatePassCode(user);
	}
	
	

	@Override
	public void updateSiteUserInfo(SiteUser user) {
		if (user == null) {
			throw new ServiceException("Site User not found.", "SITEUSER.NOTFOUND");
		}
		
		SiteUser currentSiteUser = getUserByEmail(user.getUserId());
		if (currentSiteUser == null) {
			throw new ServiceException("Site User not found.", "SITEUSER.NOTFOUND");
		}
		
		currentSiteUser.setEmailAddress(user.getEmailAddress());
		currentSiteUser.setFirstName(user.getFirstName());
		currentSiteUser.setLastName(user.getLastName());
		currentSiteUser.setAdmin(user.isAdmin());
		siteUserDao.update(currentSiteUser);
	}

	@Override
	public void updateAuthorizedAlbum(SiteUser user, String authAlbums) {
		if (user == null) {
			throw new ServiceException("Site User not found.", "SITEUSER.NOTFOUND");
		}
		
		if (StringUtils.isBlank(authAlbums)) {
			user.setAuthAlbums("NONE");
		}
		else {
			user.setAuthAlbums(authAlbums.trim());
		}
		
		siteUserDao.updateAuthUpload(user);
	}

	@Override
	public void updateAuthUpload(SiteUser user) {
		siteUserDao.updateAuthUpload(user);
	}
	
	

	@Override
	public void updateIsAdmin(SiteUser user, Boolean isAdmin) {
		if (user == null) {
			throw new ServiceException("Site User not found.", "SITEUSER.NOTFOUND");
		}
		
		if (isAdmin == null) {
			isAdmin = false;
		}
		
		SiteUser currentUser = getUserById(user.getUserId());
		if (currentUser == null) {
			throw new ServiceException("Site User not found.", "SITEUSER.NOTFOUND");
		}
		currentUser.setAdmin(isAdmin);
		siteUserDao.update(currentUser);
		
	}

	@Override
	public void updateUserStatus(SiteUser user, SiteUserStatus status) {
		if (user == null) {
			throw new ServiceException("Site User not found.", "SITEUSER.NOTFOUND");
		}
		
		if (status == null) {
			status = SiteUserStatus.INACTIVE;
		}
		
		SiteUser currentUser = getUserById(user.getUserId());
		if (currentUser == null) {
			throw new ServiceException("Site User not found.", "SITEUSER.NOTFOUND");
		}
		
		currentUser.setUserStatus(status);
		siteUserDao.updateUserStatus(currentUser);
	}

	public void setSiteUserDao(SiteUserDao siteUserDao) {
		this.siteUserDao = siteUserDao;
	}
	public void setEmailSenderSvc(EmailSenderSvc emailSenderSvc) {
		this.emailSenderSvc = emailSenderSvc;
	}
	public void setPassCodeEmailTemplate(String passCodeEmailTemplate) {
		this.passCodeEmailTemplate = passCodeEmailTemplate;
	}
}
