package com.md.studio.domain;

import java.io.Serializable;
import java.util.Date;

public class Invites implements Serializable {
	private static final long serialVersionUID = -9167437582776735071L;
	
	private String userId;
	private String inviteeEmailAddress;
	private String inviteeCode;
	private Date inviteDate;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInviteeEmailAddress() {
		return inviteeEmailAddress;
	}
	public void setInviteeEmailAddress(String inviteeEmailAddress) {
		this.inviteeEmailAddress = inviteeEmailAddress;
	}
	public String getInviteeCode() {
		return inviteeCode;
	}
	public void setInviteeCode(String inviteeCode) {
		this.inviteeCode = inviteeCode;
	}
	public Date getInviteDate() {
		return inviteDate;
	}
	public void setInviteDate(Date inviteDate) {
		this.inviteDate = inviteDate;
	}
}
