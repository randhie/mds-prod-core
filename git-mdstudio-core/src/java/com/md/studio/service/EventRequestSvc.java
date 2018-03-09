package com.md.studio.service;

import java.util.List;

import com.md.studio.domain.EventRequest;

public interface EventRequestSvc {

	public void createEventRequest(EventRequest eventRequest);
	
	public void updateEventRequest(EventRequest eventRequest);
	
	public void updateHasContacted(EventRequest eventRequest);
	
	public void updateHasAccomplished(EventRequest eventRequest);
	
	public void cancelRequestEvent(EventRequest eventRequest);
	
	public void reactivateRequestEvent(EventRequest eventRequest);
	
	public EventRequest selectById(long eventId);
	
	public List<EventRequest> selectByEmail(String emailAddress);
	
	public List<EventRequest> selectHasContacted();
	
	public List<EventRequest> selectHasNotContacted();
	
	public List<EventRequest> selectHasAccomplished();
	
	public List<EventRequest> selectHasNotAccomplished();
	
	public List<EventRequest> selectAll();
}
