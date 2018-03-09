package com.md.studio.dao;

import java.util.List;

import com.md.studio.domain.SiteUser;

public interface SiteUserDao {
	
	public void insert(SiteUser user);
	
	public void update(SiteUser user);
	
	public void updatePassCode(SiteUser user);
	
	public void updateUserStatus(SiteUser user);
	
	public void updateLastLogin(SiteUser user);
	
	public void updateAuthUpload(SiteUser user);
	
	public String selectPassCode(String rawPassCode, String encPassCode);
	
	public SiteUser selectByEmail(String emailAddress);
	
	public SiteUser selectById(String userId);
	
	public List<SiteUser> selectAllUser();
	
	public SiteUser selectByActivationCode(String activationCode);
	
}
