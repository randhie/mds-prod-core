package com.md.studio.dao;

import java.util.List;

import com.md.studio.domain.Invites;

public interface InvitesDao {
	
	public List<Invites> selectAllInvites();
	
	public List<Invites> selectAllById(String userId);
	
	public List<Invites> selectByInviteesEmail(String emailAddress);
	
	public Invites select(String userId, String inviteesEmail);
	
	public Invites selectByInviteeCode(String inviteeCode);
	
	public void insert(Invites invite);
	
}
