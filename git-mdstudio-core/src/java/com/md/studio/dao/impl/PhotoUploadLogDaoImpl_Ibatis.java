package com.md.studio.dao.impl;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.PhotoUploadLogDao;
import com.md.studio.domain.PhotoUploadLog;

public class PhotoUploadLogDaoImpl_Ibatis extends SqlMapClientDaoSupport implements PhotoUploadLogDao {
	private static final String STMT_INSERT = "PhotoUploadLog.insert";

	@Override
	public void insert(PhotoUploadLog uploadLog) {
		getSqlMapClientTemplate().insert(STMT_INSERT, uploadLog);
	}

}
