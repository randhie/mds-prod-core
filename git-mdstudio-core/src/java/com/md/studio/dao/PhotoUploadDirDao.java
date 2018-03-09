package com.md.studio.dao;

import java.util.List;

import com.md.studio.domain.PhotoUploadDirectory;

public interface PhotoUploadDirDao {

	public PhotoUploadDirectory selectPhotoUploadDir(long uploadId);
	
	public List<PhotoUploadDirectory> selectAllPhotoUploadDir();
	
	public List<PhotoUploadDirectory> selectInvalidPhotoUploadDir();
	
	public List<PhotoUploadDirectory> selectValidPhotoUploadDir();
	
	public void insert(PhotoUploadDirectory photoUploadDir);
	
	public void updatePhotoUploadDir(PhotoUploadDirectory photoUploadDir);
	
	public void updateValidStatus(PhotoUploadDirectory photoUploadDir);
	
	public void removePhotoUploadDIr(PhotoUploadDirectory photoUploadDir);
	
}
