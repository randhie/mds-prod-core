package com.md.studio.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.md.studio.domain.ReferenceData;
import com.md.studio.service.ReferenceDataSvc;
import com.md.studio.service.ServiceException;
import com.md.studio.utils.JmsMessageSender;

public class SiteUserEventMonitor implements SiteUserEventListener, SiteUserEventPublisher, ApplicationContextAware {
	private String REFTYPE_HANDLER = "EVENTHANDLER";
	private String eventQueueName;
	private JmsMessageSender jmsMessageSender;
	private ApplicationContext ctx;
	private ReferenceDataSvc referenceDataSvc;

	@Override
	public void publishEvent(SiteUserEvent siteUserEvent) {
		if (siteUserEvent == null) {
			throw new ServiceException("Event not found", "EVENT.NOTFOUND");
		}
		jmsMessageSender.sendToQueue(eventQueueName, siteUserEvent);
	}

	@Override
	public void publishEvent(String userId, String eventType, String eventBeanId, Object eventObj) {
		SiteUserEvent siteUserEvent = new SiteUserEvent();
		siteUserEvent.setUserId(userId);
		siteUserEvent.setEventType(SiteUserEventType.valueOf(eventType));
		siteUserEvent.setEventObject(eventObj);
		
		publishEvent(siteUserEvent);
	}

	@Override
	public void handleSiteUserEvent(SiteUserEvent siteUserEvent) {
		ReferenceData refData = referenceDataSvc.getRefData(REFTYPE_HANDLER, siteUserEvent.getEventType().toString());
		if (refData == null || refData.getRefValue() == null) {
			return;
		}
		
		Object bean = null;
		List<Object> beans = new ArrayList<Object>();
		
		if (refData.getParsePipeRefValue().isEmpty()) {
			bean = ctx.getBean(refData.getRefValue());
			
			if (bean == null) {
				return;
			}
			beans.add(bean);
		}
		else {
			for (String beanId: refData.getParsePipeRefValue()) {
				bean = ctx.getBean(beanId);
				
				if (bean == null) {
					continue;
				}
				beans.add(bean);
				bean = new Object();
			}
		}

		if (beans.isEmpty()) {
			return;
		}
		
		for (Object ojb: beans) {
			if (ojb instanceof SiteUserEventHandler) {
				((SiteUserEventHandler) ojb).processEvent(siteUserEvent);
			}
		}
	}

	
	
	public void setEventQueueName(String eventQueueName) {
		this.eventQueueName = eventQueueName;
	}
	public void setJmsMessageSender(JmsMessageSender jmsMessageSender) {
		this.jmsMessageSender = jmsMessageSender;
	}
	public void setReferenceDataSvc(ReferenceDataSvc referenceDataSvc) {
		this.referenceDataSvc = referenceDataSvc;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.ctx = ctx;
	}

}
