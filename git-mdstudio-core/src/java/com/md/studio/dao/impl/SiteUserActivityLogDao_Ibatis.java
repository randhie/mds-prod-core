package com.md.studio.dao.impl;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.SiteUserActivityLogDao;
import com.md.studio.domain.SiteUserActivityLog;

public class SiteUserActivityLogDao_Ibatis extends SqlMapClientDaoSupport implements SiteUserActivityLogDao {

	private static final String STMT_INSERT = "SiteUserActivityLog.insert";

	@Override
	public void insert(SiteUserActivityLog log) {
		getSqlMapClientTemplate().insert(STMT_INSERT, log);
		
	}

	

}
