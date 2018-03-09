package com.md.studio.event.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.md.studio.domain.PhotoUploadDirectory;
import com.md.studio.domain.SiteUser;
import com.md.studio.event.SiteUserEvent;
import com.md.studio.event.SiteUserEventHandler;
import com.md.studio.service.PhotoUploadDirSvc;
import com.md.studio.service.SiteUserInfoSvc;

public class EventHandler_CleanSiteUserUploadAuth implements SiteUserEventHandler {
	private static final Logger LOGGER = Logger.getLogger(EventHandler_CleanSiteUserUploadAuth.class);
	private SiteUserInfoSvc siteUserInfoSvc;
	private PhotoUploadDirSvc photoUploadDirSvc;
	
	@Override
	public void processEvent(SiteUserEvent siteUserEvent) {
		if (siteUserEvent == null) {
			return;
		}
		
		if (siteUserEvent.getEventObject() instanceof SiteUser) {
			List<PhotoUploadDirectory> photoUploadDirectoryList = photoUploadDirSvc.getInValidPhotoUploadDir();
			if (photoUploadDirectoryList == null || photoUploadDirectoryList.isEmpty()) {
				return;
			}
			
			SiteUser siteUser = (SiteUser) siteUserEvent.getEventObject();
			
			if (StringUtils.isBlank(siteUser.getAuthUploads())) {
				return;
			}

			List<String> uploadAuthList = new ArrayList<String>();
			if (siteUser.getAuthUploads().contains(",")) {
				String[] uploadAuth = siteUser.getAuthUploads().trim().split(",");
				for (String upload: uploadAuth) {
					uploadAuthList.add(upload);
				}
			}
			else {
				uploadAuthList.add(siteUser.getAuthUploads());
			}
			
			
			if (uploadAuthList.isEmpty()) {
				return;
			}
			else if (siteUser.getAuthUploads().contains("ALL")) {
				return;
			}
			
			List<String> needToRemove = new ArrayList<String>();
			for (PhotoUploadDirectory pud: photoUploadDirectoryList) {
				for (String uploadAuth: uploadAuthList) {
					if (pud.getUploadId() == Long.valueOf(uploadAuth)) {
						needToRemove.add(uploadAuth);
					}
				}
			}
			
			if (needToRemove.isEmpty()) {
				return;
			}
			
			List<String> newUploadAuth = new ArrayList<String>();
			for (String toRemove: needToRemove) {
				for (String uploadAuth: uploadAuthList) {
					if (toRemove.equals(uploadAuth)) {
						continue;
					}
					newUploadAuth.add(uploadAuth);
				}
			}
			
			String newUpload = "";
			int ctr = 0;
			for (String uploadAuth: newUploadAuth) {
				ctr++;
				newUpload += uploadAuth;
				if (newUploadAuth.size() > ctr) {
					newUpload += ",";
				}
			}
			
			siteUser.setAuthUploads(newUpload.trim());
			siteUserInfoSvc.updateAuthUpload(siteUser);
		}
		else {
			LOGGER.info("Cannot run event due to incorrect domain object. Expecting SiteUser, and it got " + siteUserEvent.getEventObject().getClass());
		}
	}

	
	
	public void setSiteUserInfoSvc(SiteUserInfoSvc siteUserInfoSvc) {
		this.siteUserInfoSvc = siteUserInfoSvc;
	}
	public void setPhotoUploadDirSvc(PhotoUploadDirSvc photoUploadDirSvc) {
		this.photoUploadDirSvc = photoUploadDirSvc;
	}
}
