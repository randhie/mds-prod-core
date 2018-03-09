package com.md.studio.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.md.studio.dao.SiteUserActivityLogDao;
import com.md.studio.domain.PhotoGatherer;
import com.md.studio.domain.PhotoInfo;
import com.md.studio.domain.SiteUserActivityLog;
import com.md.studio.domain.UserContext;
import com.md.studio.service.PhotoGathererSvc;
import com.md.studio.service.ServiceException;
import com.md.studio.service.SiteUserActivityLogSvc;
import com.md.studio.utils.JmsMessageSender;
import com.md.studio.utils.WhoIsApiUtil;

public class SiteUserActivityLogSvcImpl implements SiteUserActivityLogSvc {
	private static final Logger LOGGER = Logger.getLogger(SiteUserActivityLogSvcImpl.class);
	
	private static final String ERROR_MSG_LOGNOTFOUND = "User activity log not found";
	private static final String ERROR_MSG_PHOTONOTFOUND = "Photo not found";
	private static final String ERROR_CODE_LOGOUTNFOUND = "LOG.NOTFOUND";
	private String destionationQueueProcessLog;
	private WhoIsApiUtil whoIsApiUtil;

	private JmsMessageSender jmsMessageSender;
	private SiteUserActivityLogDao siteUserActivityLogDao;
	private PhotoGathererSvc photoGathererSvc;
	
	@Override
	public void recordActivityLog(SiteUserActivityLog log) {
		if (log == null) {
			throw new ServiceException(ERROR_MSG_LOGNOTFOUND, ERROR_CODE_LOGOUTNFOUND);
		}

		if (StringUtils.isNotBlank(UserContext.getUserId())) {
			log.setUserId(UserContext.getUserId());
		}
		
		jmsMessageSender.sendToQueue(destionationQueueProcessLog, log);
	}

	@Override
	public List<SiteUserActivityLog> getActivityLog(Date fromDate, Date afterDate, Integer limit, Integer offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processActivityLog(SiteUserActivityLog log) {
		if (log == null) {
			LOGGER.error(ERROR_MSG_LOGNOTFOUND);
			return;
		}
		
		PhotoInfo photoInfo = photoGathererSvc.getPhoto(log.getPhotoId());
		if (photoInfo == null) {
			LOGGER.info(ERROR_MSG_PHOTONOTFOUND);
			return;
		}
		
		log.setCategoryId(photoInfo.getCategoryId());
		log.setFileName(photoInfo.getFileName());
		log.setDateAccess(new Date());
		
		String categoryId = Long.toString(photoInfo.getCategoryId());
		List<PhotoGatherer> photoGatherer = photoGathererSvc.getPhotoGatherer(Integer.valueOf(categoryId), photoInfo.getPhotoType());
		if (photoGatherer != null) {
			log.setCategory(photoGatherer.get(0).getCategory());
			log.setDirectory(photoGatherer.get(0).getDirectory());
		}
		
//		String response = whoIsApiUtil.processWhoIsIp(log.getIpAddress());
//		log.setWhoIsRawInfo(response);
		siteUserActivityLogDao.insert(log);
	}

	public void setJmsMessageSender(JmsMessageSender jmsMessageSender) {
		this.jmsMessageSender = jmsMessageSender;
	}
	public void setSiteUserActivityLogDao(SiteUserActivityLogDao siteUserActivityLogDao) {
		this.siteUserActivityLogDao = siteUserActivityLogDao;
	}
	public void setDestionationQueueProcessLog(String destionationQueueProcessLog) {
		this.destionationQueueProcessLog = destionationQueueProcessLog;
	}
	public void setPhotoGathererSvc(PhotoGathererSvc photoGathererSvc) {
		this.photoGathererSvc = photoGathererSvc;
	}
	public void setWhoIsApiUtil(WhoIsApiUtil whoIsApiUtil) {
		this.whoIsApiUtil = whoIsApiUtil;
	}
}
