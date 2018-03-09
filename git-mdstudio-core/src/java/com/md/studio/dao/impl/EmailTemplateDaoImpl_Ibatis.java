package com.md.studio.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.EmailTemplateDao;
import com.md.studio.domain.EmailTemplate;

public class EmailTemplateDaoImpl_Ibatis extends SqlMapClientDaoSupport implements EmailTemplateDao {
	private static final String STMT_INSERT = "EmailTemplate.insert";
	private static final String STMT_DELETE = "EmailTemplate.delete";
	private static final String STMT_SELECTBYCODE = "EmailTemplate.selectByCode";
	private static final String STMT_SELECTALL = "EmailTemplate.selectAll";
	
	@Override
	public void insert(EmailTemplate emailTemplate) {
		getSqlMapClientTemplate().insert(STMT_INSERT, emailTemplate);

	}

	@Override
	public void delete(EmailTemplate emailTemplate) {
		getSqlMapClientTemplate().delete(STMT_DELETE, emailTemplate);

	}

	@Override
	public EmailTemplate selectByCode(String templateCode) {
		return (EmailTemplate) getSqlMapClientTemplate().queryForObject(STMT_SELECTBYCODE, templateCode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EmailTemplate> selectAll() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECTALL);
	}

}
