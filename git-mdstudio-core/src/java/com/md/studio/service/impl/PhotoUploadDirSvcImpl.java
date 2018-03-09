package com.md.studio.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.md.studio.dao.PhotoUploadDirDao;
import com.md.studio.domain.PhotoUploadDirectory;
import com.md.studio.domain.UserContext;
import com.md.studio.service.PhotoUploadDirSvc;
import com.md.studio.service.ServiceException;

public class PhotoUploadDirSvcImpl implements PhotoUploadDirSvc {
	private PhotoUploadDirDao photoUploadDirDao;

	@Override
	public PhotoUploadDirectory getPhotoUploadDir(long uploadId) {
		return photoUploadDirDao.selectPhotoUploadDir(uploadId);
	}

	@Override
	public List<PhotoUploadDirectory> getAllPhotoUploadDir() {
		return photoUploadDirDao.selectAllPhotoUploadDir();
	}

	@Override
	public List<PhotoUploadDirectory> getValidPhotoUploadDir() {
		List<PhotoUploadDirectory> pud = photoUploadDirDao.selectValidPhotoUploadDir();
		List<PhotoUploadDirectory> filtered = new ArrayList<PhotoUploadDirectory>();
		
		if (pud != null) {
			Date currentDate = new Date();
			for (PhotoUploadDirectory p: pud) {
				if (currentDate.after(p.getValidFrom()) &&
					currentDate.before(p.getValidTo())) {
					filtered.add(p);
					continue;
				}
				else if (currentDate.after(p.getValidFrom()) &&
						currentDate.after(p.getValidTo())) {
					p.setStillValid(false);
					updateValidStatus(p);
				}
			}
		}
		return filtered;
	}

	@Override
	public List<PhotoUploadDirectory> getInValidPhotoUploadDir() {
		return photoUploadDirDao.selectInvalidPhotoUploadDir();
	}
	

	@Override
	public void createPhotoUploadDir(PhotoUploadDirectory photoUploadDir) {
		photoUploadDir.setCreatedBy(UserContext.getUserId());
		photoUploadDir.setCreatedDate(new Date());
		photoUploadDirDao.insert(photoUploadDir);
	}

	@Override
	public void updatePhotoUploadDir(PhotoUploadDirectory photoUploadDir) {
		PhotoUploadDirectory photoUploadDirCurrent = getPhotoUploadDir(photoUploadDir.getUploadId());
		if (photoUploadDirCurrent == null) {
			throw new ServiceException("Cannot find existing Photo Upload Directory", "PHOTOUPLOAD.NOTEXIST");
		}
		
		photoUploadDirCurrent.setBaseFolder(photoUploadDir.getBaseFolder());
		photoUploadDirCurrent.setParentFolder(photoUploadDir.getParentFolder());
		photoUploadDirCurrent.setChildFolder(photoUploadDir.getChildFolder());
		photoUploadDirCurrent.setStillValid(true);
		photoUploadDirCurrent.setValidFrom(photoUploadDir.getValidFrom());
		photoUploadDirCurrent.setValidTo(photoUploadDir.getValidTo());
		photoUploadDirDao.updatePhotoUploadDir(photoUploadDirCurrent);
	}

	@Override
	public void updateValidStatus(PhotoUploadDirectory photoUploadDir) {
		if (photoUploadDir == null) {
			throw new ServiceException("Cannot find existing Photo Upload Directory", "PHOTOUPLOAD.NOTEXIST");
		}
		photoUploadDirDao.updateValidStatus(photoUploadDir);
	}

	@Override
	public void removePhotoUploadDir(PhotoUploadDirectory photoUploadDir) {
		PhotoUploadDirectory photoUploadDirCurrent = getPhotoUploadDir(photoUploadDir.getUploadId());
		if (photoUploadDirCurrent == null) {
			throw new ServiceException("Cannot find existing Photo Upload Directory", "PHOTOUPLOAD.NOTEXIST");
		}
		
		photoUploadDirDao.removePhotoUploadDIr(photoUploadDirCurrent);

	}

	public void setPhotoUploadDirDao(PhotoUploadDirDao photoUploadDirDao) {
		this.photoUploadDirDao = photoUploadDirDao;
	}
}
