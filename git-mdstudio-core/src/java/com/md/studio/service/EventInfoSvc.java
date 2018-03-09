package com.md.studio.service;

import java.util.List;

import com.md.studio.domain.EventInfo;

public interface EventInfoSvc {

	public EventInfo getEventInfo(long eventId);
	
	public EventInfo getEventInfo(String eventName);
	
	public List<EventInfo> getAllEventInfo(Integer limit, Integer offset);
	
	public List<EventInfo> getNonExpiredEvents(Integer limit, Integer offset);
	
	public List<EventInfo> getExpiredEvents(Integer limit, Integer offset);
	
	public void createEvent(EventInfo eventInfo);
	
	public void updateEvent(EventInfo eventInfo);
	
	public void removeEvent(EventInfo eventInfo);
}
