package com.md.studio.dao.impl;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.EmailLogDao;
import com.md.studio.domain.EmailLog;

public class EmailLogDaoImpl_Ibatis extends SqlMapClientDaoSupport implements EmailLogDao {
	private static final String STMT_INSERT = "EmailLog.insert";
	
	@Override
	public void insert(EmailLog emailLog) {
		getSqlMapClientTemplate().insert(STMT_INSERT, emailLog);
	}

}
