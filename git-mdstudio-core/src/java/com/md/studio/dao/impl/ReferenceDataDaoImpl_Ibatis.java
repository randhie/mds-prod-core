package com.md.studio.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.ReferenceDataDao;
import com.md.studio.domain.ReferenceData;

public class ReferenceDataDaoImpl_Ibatis extends SqlMapClientDaoSupport implements ReferenceDataDao {
	private static final String STMT_INSERT = "ReferenceData.insert";
	private static final String STMT_SELECTBYTYPE = "ReferenceData.selectByType";
	private static final String STMT_SELECTBYTYPE_ANDCODE = "ReferenceData.selectByTypeAndCode";
	private static final String STMT_SELECTALL = "ReferenceData.selectAll";
	private static final String STMT_UPDATE = "ReferenceData.update";
	private static final String STMT_DELETE = "ReferenceData.delete";
	
	private static final String PARAMS_REFTYPE = "refType";
	private static final String PARAMS_REFCODE = "refCode";

	@Override
	public void insert(ReferenceData referenceData) {
		getSqlMapClientTemplate().insert(STMT_INSERT, referenceData);
	}

	@Override
	public void update(ReferenceData referenceData) {
		getSqlMapClientTemplate().update(STMT_UPDATE, referenceData);
	}

	@Override
	public void delete(ReferenceData referenceData) {
		getSqlMapClientTemplate().delete(STMT_DELETE, referenceData);
	}

	@Override
	public List<ReferenceData> selectByType(String refType) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECTBYTYPE, refType);
	}

	@Override
	public ReferenceData select(String refType, String refCode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(PARAMS_REFTYPE, refType);
		params.put(PARAMS_REFCODE, refCode);
		
		return (ReferenceData) getSqlMapClientTemplate().queryForObject(STMT_SELECTBYTYPE_ANDCODE, params);
	}

	@Override
	public List<ReferenceData> selectAll() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECTALL);
	}

}
