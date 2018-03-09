package com.md.studio.service;

import com.md.studio.domain.SiteUser;

public interface SiteUserInviteSvc {

	public void invite(SiteUser inviter, String inviteesEmailAdd);
}
