package com.md.studio.service;

import java.util.List;

import com.md.studio.domain.PhotoUploadDirectory;

public interface PhotoUploadDirSvc {
	
	public PhotoUploadDirectory getPhotoUploadDir(long uploadId);

	public List<PhotoUploadDirectory> getAllPhotoUploadDir();
	
	public List<PhotoUploadDirectory> getValidPhotoUploadDir();
	
	public List<PhotoUploadDirectory> getInValidPhotoUploadDir();
	
	public void createPhotoUploadDir(PhotoUploadDirectory photoUploadDir);
	
	public void updatePhotoUploadDir(PhotoUploadDirectory photoUploadDir);
	
	public void updateValidStatus(PhotoUploadDirectory photoUploadDir);
	
	public void removePhotoUploadDir(PhotoUploadDirectory photoUploadDir);
	
	 
}
