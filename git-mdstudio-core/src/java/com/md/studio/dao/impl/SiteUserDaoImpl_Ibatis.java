package com.md.studio.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.SiteUserDao;
import com.md.studio.domain.SiteUser;

public class SiteUserDaoImpl_Ibatis extends SqlMapClientDaoSupport implements SiteUserDao {
	private static final String STMT_INSERT = "SiteUser.insert";
	private static final String STMT_UPDATE = "SiteUser.update";
	private static final String STMT_UPDATE_PASSCODE = "SiteUser.updatePasscode";
	private static final String STMT_UPDATE_USERSTATUS = "SiteUser.updateUserStatus";
	private static final String STMT_UPDATE_LASTLOGIN = "SiteUser.updateLastLogin";	
	private static final String STMT_UPDATE_AUTHUPLOAD = "SiteUser.updateAuthUpload";
	private static final String STMT_SELECT_PASSCODE = "SiteUser.selectPasscode";
	private static final String STMT_SELECT_BYEMAIL = "SiteUser.selectByEmail";
	private static final String STMT_SELECT_BYID = "SiteUser.selectById";
	private static final String STMT_SELECT_ALL = "SiteUser.selectAll";
	private static final String STMT_SELECT_BY_ACTIVATIONCODE = "SiteUser.selectByActivationCode";
	
	private static final String PARAM_PASSCODE = "passcode";
	private static final String PARAM_ENC_PASSCODE = "encPassCode";
	
	@Override
	public void insert(SiteUser user) {
		getSqlMapClientTemplate().insert(STMT_INSERT, user);
	}

	@Override
	public void update(SiteUser user) {
		getSqlMapClientTemplate().update(STMT_UPDATE, user);
	}

	@Override
	public void updatePassCode(SiteUser user) {
		getSqlMapClientTemplate().update(STMT_UPDATE_PASSCODE, user);
	}

	@Override
	public String selectPassCode(String rawPassCode, String encPassCode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(PARAM_PASSCODE, rawPassCode);
		params.put(PARAM_ENC_PASSCODE, encPassCode);
		return (String) getSqlMapClientTemplate().queryForObject(STMT_SELECT_PASSCODE, params);
	}

	@Override
	public SiteUser selectByEmail(String emailAddress) {
		return (SiteUser) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BYEMAIL, emailAddress);
	}

	@Override
	public SiteUser selectById(String userId) {
		return (SiteUser) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BYID, userId);
	}

	@Override
	public SiteUser selectByActivationCode(String activationCode) {
		return (SiteUser) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BY_ACTIVATIONCODE, activationCode);
	}

	
	@Override
	public List<SiteUser> selectAllUser() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_ALL);
	}

	@Override
	public void updateUserStatus(SiteUser user) {
		getSqlMapClientTemplate().update(STMT_UPDATE_USERSTATUS, user);
		
	}

	@Override
	public void updateLastLogin(SiteUser user) {
		getSqlMapClientTemplate().update(STMT_UPDATE_LASTLOGIN, user);
		
	}

	@Override
	public void updateAuthUpload(SiteUser user) {
		getSqlMapClientTemplate().update(STMT_UPDATE_AUTHUPLOAD, user);
	}
}
