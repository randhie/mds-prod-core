package com.md.studio.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.PhotoUploadDirDao;
import com.md.studio.domain.PhotoUploadDirectory;

public class PhotoUploadDirDaoImpl_Ibatis extends SqlMapClientDaoSupport implements PhotoUploadDirDao {
	private static final String STMT_INSERT = "PhotoUploadDirectory.insert";
	private static final String STMT_UPDATE = "PhotoUploadDirectory.update";
	private static final String STMT_UPDATE_STATUS = "PhotoUploadDirectory.updateValidStatus";
	private static final String STMT_SELECT_BYID = "PhotoUploadDirectory.selectById";
	private static final String STMT_SELECT_ALL = "PhotoUploadDirectory.selectAll";
	private static final String STMT_SELECT_VALID = "PhotoUploadDirectory.selectAllValid";
	private static final String STMT_SELECT_INVALID = "PhotoUploadDirectory.selectAllInvalid";
	private static final String STMT_REMOVE = "PhotoUploadDirectory.delete";
	
	@Override
	public PhotoUploadDirectory selectPhotoUploadDir(long uploadId) {
		return (PhotoUploadDirectory) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BYID, uploadId);
	}

	@Override
	public List<PhotoUploadDirectory> selectAllPhotoUploadDir() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_ALL);
	}

	@Override
	public List<PhotoUploadDirectory> selectValidPhotoUploadDir() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_VALID);
	}

	@Override
	public void insert(PhotoUploadDirectory photoUploadDir) {
		getSqlMapClientTemplate().insert(STMT_INSERT, photoUploadDir);
	}

	@Override
	public void updatePhotoUploadDir(PhotoUploadDirectory photoUploadDir) {
		getSqlMapClientTemplate().update(STMT_UPDATE, photoUploadDir);
	}

	@Override
	public void updateValidStatus(PhotoUploadDirectory photoUploadDir) {
		getSqlMapClientTemplate().update(STMT_UPDATE_STATUS, photoUploadDir);
	}

	@Override
	public void removePhotoUploadDIr(PhotoUploadDirectory photoUploadDir) {
		getSqlMapClientTemplate().delete(STMT_REMOVE, photoUploadDir);

	}

	@Override
	public List<PhotoUploadDirectory> selectInvalidPhotoUploadDir() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_INVALID);
	}

}
