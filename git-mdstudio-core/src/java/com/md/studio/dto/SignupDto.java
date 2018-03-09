package com.md.studio.dto;

import java.io.Serializable;

public class SignupDto implements Serializable {
	private static final long serialVersionUID = -2592034046415265302L;
	
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String passcode;
	private String confirmPassCode;
	private String inviteesCode;

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
	public String getConfirmPassCode() {
		return confirmPassCode;
	}
	public void setConfirmPassCode(String confirmPassCode) {
		this.confirmPassCode = confirmPassCode;
	}
	public String getInviteesCode() {
		return inviteesCode;
	}
	public void setInviteesCode(String inviteesCode) {
		this.inviteesCode = inviteesCode;
	}
}
