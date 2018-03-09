package com.md.studio.dao;

import java.util.List;

import com.md.studio.domain.EventRequest;

public interface EventRequestDao {
	
	public void insert(EventRequest eventRequest);
	
	public void update(EventRequest eventRequest);
	
	public void updateHasContacted(EventRequest eventRequest);
	
	public void updateHasAccomplished(EventRequest eventRequest);
	
	public void updateCancelEvent(EventRequest eventRequest);
	
	public EventRequest selectById(long eventId);
	
	public List<EventRequest> selectByEmail(String emailAddress);
	
	public List<EventRequest> selectHasContacted(Boolean hasContacted);
	
	public List<EventRequest> selectHasAccomplished(Boolean hasAccomplished);
	
	public List<EventRequest> selectAll();
	
}
