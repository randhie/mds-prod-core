package com.md.studio.dao;

import java.util.List;

import com.md.studio.domain.EventInfo;

public interface EventInfoDao {

	public EventInfo selectEventInfo(long eventId);
	
	public EventInfo selectEventInfo(String eventName);
	
	public List<EventInfo> selectAllEventInfo(Integer limit, Integer offset);
	
	public List<EventInfo> selectExpiredEvents(Integer limit, Integer offset);
	
	public List<EventInfo> selectValidEvents(Integer limit, Integer offset);
	
	public void createEvent(EventInfo eventInfo);
	
	public void updateEvent(EventInfo eventInfo);
	
	public void deleteEvent(EventInfo eventInfo);
}
