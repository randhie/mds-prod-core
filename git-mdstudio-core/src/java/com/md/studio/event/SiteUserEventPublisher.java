package com.md.studio.event;

public interface SiteUserEventPublisher {

	public void publishEvent(SiteUserEvent siteUserEvent);
	
	public void publishEvent(String userId, String eventType, String eventBeanId, Object eventObj);
}
