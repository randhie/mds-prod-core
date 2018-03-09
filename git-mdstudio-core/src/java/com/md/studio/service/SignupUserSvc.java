package com.md.studio.service;

import com.md.studio.domain.SiteUser;

public interface SignupUserSvc {
	
	public void createUser(SiteUser user, String inviteesCode);
	
	public void activateUser(String activationCode);

}
