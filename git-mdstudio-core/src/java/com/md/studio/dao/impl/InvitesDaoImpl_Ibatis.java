package com.md.studio.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.InvitesDao;
import com.md.studio.domain.Invites;

public class InvitesDaoImpl_Ibatis extends SqlMapClientDaoSupport implements InvitesDao {
	private static final String STMT_SELECT_ALLINVITES = "invitee.selectAllInvites";
	private static final String STMT_SELECT_BYID = "invitee.selectById";
	private static final String STMT_SELECT_BYINVITEES_EMAIL = "invitee.selectByEmail";
	private static final String STMT_SELECT = "invitee.select";
	private static final String STMT_SELECT_BYINVITEE_CODE = "invitee.selectByInviteeCode";
	private static final String STMT_INSERT = "invitee.insert";
	
	private static final String PARAM_USERID = "userId";
	private static final String PARAM_INVITEES_EMAIL = "inviteeEmailAddress";
	
	@Override
	public List<Invites> selectAllInvites() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_ALLINVITES);
	}

	@Override
	public List<Invites> selectAllById(String userId) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_BYID, userId);
	}

	@Override
	public List<Invites> selectByInviteesEmail(String emailAddress) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_BYINVITEES_EMAIL, emailAddress);
	}

	@Override
	public Invites select(String userId, String inviteesEmail) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(PARAM_USERID, userId);
		params.put(PARAM_INVITEES_EMAIL, inviteesEmail);
		return (Invites) getSqlMapClientTemplate().queryForObject(STMT_SELECT, params);
	}

	@Override
	public Invites selectByInviteeCode(String inviteeCode) {
		return (Invites) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BYINVITEE_CODE, inviteeCode);
	}

	@Override
	public void insert(Invites invite) {
		getSqlMapClientTemplate().insert(STMT_INSERT, invite);

	}

}
