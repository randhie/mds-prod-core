package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

import com.md.studio.domain.enums.SiteUserStatus;

public class SiteUser implements Serializable {
	private static final long serialVersionUID = 698982910338072750L;

	private String userId;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String passcode;
	private Date signupDate;
	private String signupIpAddress;
	private Date lastLogin;
	private String loginIpAddress;
	private Date dateActivated;
	private SiteUserStatus userStatus;
	private String activationCode;
	private String authAlbums;
	private String referralCode;
	private boolean hasInvited = false;
	private boolean admin = false;
	private String authUploads;
	private String signupBrowser;
	private String lastLoginBrowser;
	
	
	
	public boolean isHasInvited() {
		return hasInvited;
	}
	public void setHasInvited(boolean hasInvited) {
		this.hasInvited = hasInvited;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPasscode() {
		return passcode;
	}
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	public Date getSignupDate() {
		return signupDate;
	}
	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}
	public String getSignupIpAddress() {
		return signupIpAddress;
	}
	public void setSignupIpAddress(String signupIpAddress) {
		this.signupIpAddress = signupIpAddress;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getLoginIpAddress() {
		return loginIpAddress;
	}
	public void setLoginIpAddress(String loginIpAddress) {
		this.loginIpAddress = loginIpAddress;
	}
	public Date getDateActivated() {
		return dateActivated;
	}
	public void setDateActivated(Date dateActivated) {
		this.dateActivated = dateActivated;
	}
	public SiteUserStatus getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(SiteUserStatus userStatus) {
		this.userStatus = userStatus;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public String getAuthAlbums() {
		return authAlbums;
	}
	public void setAuthAlbums(String authAlbums) {
		this.authAlbums = authAlbums;
	}
	public String getReferralCode() {
		return referralCode;
	}
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public String getFullName () {
		return firstName + " " + lastName;
	}
	public String getAuthUploads() {
		return authUploads;
	}
	public void setAuthUploads(String authUploads) {
		this.authUploads = authUploads;
	}
	public String getSignupBrowser() {
		return signupBrowser;
	}
	public void setSignupBrowser(String signupBrowser) {
		this.signupBrowser = signupBrowser;
	}
	public String getLastLoginBrowser() {
		return lastLoginBrowser;
	}
	public void setLastLoginBrowser(String lastLoginBrowser) {
		this.lastLoginBrowser = lastLoginBrowser;
	}
}	
