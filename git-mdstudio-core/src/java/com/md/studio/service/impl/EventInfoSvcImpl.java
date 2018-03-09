package com.md.studio.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.md.studio.dao.EventInfoDao;
import com.md.studio.domain.EventInfo;
import com.md.studio.domain.UserContext;
import com.md.studio.service.EventInfoSvc;
import com.md.studio.service.ServiceException;

public class EventInfoSvcImpl implements EventInfoSvc{
	private EventInfoDao eventInfoDao;
	private Integer defaultLimit = 1000;
	private Integer defaultOffset = 0;
	
	@Override
	public EventInfo getEventInfo(long eventId) {
		if (eventId == 0) {
			throw new ServiceException("Event not found.", "EVENTID.NOTFOUND");
		}
		
		return eventInfoDao.selectEventInfo(eventId);
	}

	@Override
	public EventInfo getEventInfo(String eventName) {
		if  (StringUtils.isBlank(eventName)) {
			throw new ServiceException("Event not found.", "EVENTNAME.NOTFOUND");
		}
		return eventInfoDao.selectEventInfo(eventName);
	}

	@Override
	public List<EventInfo> getAllEventInfo(Integer limit, Integer offset) {
		if (limit == null ) {
			limit = defaultLimit;
		}
		
		if (offset == null) {
			offset = defaultOffset;
		}
		return eventInfoDao.selectAllEventInfo(limit, offset);
	}

	@Override
	public List<EventInfo> getNonExpiredEvents(Integer limit, Integer offset) {
		if (limit == null ) {
			limit = defaultLimit;
		}
		
		if (offset == null) {
			offset = defaultOffset;
		}
		return eventInfoDao.selectValidEvents(limit, offset);
	}

	@Override
	public List<EventInfo> getExpiredEvents(Integer limit, Integer offset) {
		if (limit == null ) {
			limit = defaultLimit;
		}
		
		if (offset == null) {
			offset = defaultOffset;
		}
		return eventInfoDao.selectExpiredEvents(limit, offset);
	}

	@Override
	public void createEvent(EventInfo eventInfo) {
		if (eventInfo == null) {
			throw new ServiceException("Event not found.", "EVENTINFO.NOTFOUND");
		}
		
		if (StringUtils.isBlank(eventInfo.getEventName())) {
			throw new ServiceException("Event name is required.", "EVENTINFO.REQUIRED");
		}
		else if (StringUtils.isBlank(eventInfo.getDescription())) {
			throw new ServiceException("Event description is required.", "EVENTINFO.REQUIRED");
		}
		else if (eventInfo.getEventDate() == null) {
			throw new ServiceException("Event date is required.", "EVENTINFO.REQUIRED");
		}
		
		
		EventInfo currentEventInfo = getEventInfo(eventInfo.getEventName());
		if (currentEventInfo != null) {
			throw new ServiceException("Event name already exist.", "EVENTNAME.EXIST");
		}
		
		eventInfo.setCreateDate(new Date());
		eventInfo.setCreatedBy(StringUtils.isNotBlank(UserContext.getUserId())? UserContext.getUserId(): "");
		eventInfoDao.createEvent(eventInfo);
	}

	@Override
	public void updateEvent(EventInfo eventInfo) {
		if (eventInfo == null) {
			throw new ServiceException("Event not found.", "EVENTINFO.NOTFOUND");
		}
		
		if (StringUtils.isBlank(eventInfo.getEventName())) {
			throw new ServiceException("Event name is required.", "EVENTINFO.REQUIRED");
		}
		else if (StringUtils.isBlank(eventInfo.getDescription())) {
			throw new ServiceException("Event description is required.", "EVENTINFO.REQUIRED");
		}
		
		EventInfo currentEventInfo = getEventInfo(eventInfo.getEventId());
		if (currentEventInfo == null) {
			throw new ServiceException("Event info does not exist", "EVENTINFO.NOTFOUND");
		}
		
		if (eventInfo.getEventFile() == null && currentEventInfo.getEventFile() != null) {
			eventInfo.setEventFile(currentEventInfo.getEventFile());
		}
		eventInfo.setUpdateDate(new Date());
		eventInfo.setUpdatedBy(StringUtils.isNotBlank(UserContext.getUserId())? UserContext.getUserId(): "");
		
		eventInfoDao.updateEvent(eventInfo);
	}

	@Override
	public void removeEvent(EventInfo eventInfo) {
		eventInfoDao.deleteEvent(eventInfo);
		
	}

	public void setEventInfoDao(EventInfoDao eventInfoDao) {
		this.eventInfoDao = eventInfoDao;
	}
}
