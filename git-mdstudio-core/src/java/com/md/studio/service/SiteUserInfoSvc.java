package com.md.studio.service;

import java.util.List;

import com.md.studio.domain.SiteUser;
import com.md.studio.domain.enums.SiteUserStatus;

public interface SiteUserInfoSvc {
	
	public SiteUser getUserByEmail(String emailAddress);
	
	public SiteUser getUserById(String userId);
	
	public SiteUser getUserByActivationCode(String activationCode);
	
	public List<SiteUser> getAllSiteUser();
	
	public void createUser(SiteUser user);
	
	public void updatePassCode(SiteUser user, String newPassCode, String currentPassCode);
	
	public void updatePassCodeByAdmin(SiteUser user, String newPassCode);
	
	public void updateAuthUpload(SiteUser user);
	
	public void updateSiteUserInfo(SiteUser user);
	
	public void updateAuthorizedAlbum(SiteUser user, String authAlbums);
	
	public void updateIsAdmin(SiteUser user, Boolean isAdmin);
	
	public void updateUserStatus(SiteUser user, SiteUserStatus status);
	
	public void resetPassCode(SiteUser user);
	
	public void assignAuthFolder(String userId, String authAlbums);
	
	public void activateUser(String activationCode);
	
	public void updateLastLogin(SiteUser siteUser);
	
	public boolean isPassCodeValid(String emailAddress, String rawPassCode);
	
}
