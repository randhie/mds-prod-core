package com.md.studio.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.md.studio.dao.EventRequestDao;
import com.md.studio.domain.EmailMessage;
import com.md.studio.domain.EventRequest;
import com.md.studio.service.EmailSenderSvc;
import com.md.studio.service.EventRequestSvc;
import com.md.studio.service.ServiceException;

public class EventRequestSvcImpl implements EventRequestSvc {
	private EventRequestDao eventRequestDao;
	private EmailSenderSvc emailSenderSvc;
	private String infoEmailAddress;

	
	@Transactional
	@Override
	public void createEventRequest(EventRequest eventRequest) {
		if (eventRequest == null) {
			throw new ServiceException("Event Request not found", "REQUEST.NOTFOUND");
		}
		
		eventRequest.setCreateDate(new Date());
		eventRequestDao.insert(eventRequest);
		
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setFromEmail(eventRequest.getEmailAddress());
		emailMessage.setFromName(eventRequest.getFullName());
		emailMessage.setMessageBody(eventRequest.getMessage());
		emailMessage.setSentBy(eventRequest.getFullName());
		emailMessage.setSubject("Event Request - " + eventRequest.getFullName());
		emailMessage.setUseTemplate(false);
		emailMessage.setToEmail(infoEmailAddress);
		emailMessage.setUseShawMail(true);
		emailMessage.setMailServer();
		
		emailSenderSvc.sendEmail(emailMessage, null);
	}

	@Override
	public void updateEventRequest(EventRequest eventRequest) {
		if (eventRequest == null) {
			throw new ServiceException("Event Request not found", "REQUEST.NOTFOUND");
		}
		
		EventRequest currentEventReq = selectById(eventRequest.getRequestId());
		if (currentEventReq == null) {
			throw new ServiceException("Event Request not found. Nothing to update", "REQUEST.NOTFOUND");
		}
		
		currentEventReq.setEmailAddress(eventRequest.getEmailAddress());
		currentEventReq.setFullName(eventRequest.getFullName());
		currentEventReq.setContactNumber(eventRequest.getContactNumber());
		currentEventReq.setDesiredDate(eventRequest.getDesiredDate());
		currentEventReq.setEventLocation(eventRequest.getEventLocation());
		currentEventReq.setEventBudget(eventRequest.getEventBudget());
		currentEventReq.setNotes(eventRequest.getNotes());
		currentEventReq.setMessage(eventRequest.getMessage());
		eventRequestDao.update(currentEventReq);
	}

	@Override
	public void updateHasContacted(EventRequest eventRequest) {
		if (eventRequest == null) {
			throw new ServiceException("Event Request not found", "REQUEST.NOTFOUND");
		}
		
		EventRequest currentEventReq = selectById(eventRequest.getRequestId());
		if (currentEventReq == null) {
			throw new ServiceException("Event Request not found. Nothing to update", "REQUEST.NOTFOUND");
		}
		
		currentEventReq.setHasContacted(eventRequest.isHasContacted());
		currentEventReq.setContactedDate(eventRequest.getContactedDate());
		eventRequestDao.updateHasContacted(currentEventReq);
	}

	@Override
	public void updateHasAccomplished(EventRequest eventRequest) {
		if (eventRequest == null) {
			throw new ServiceException("Event Request not found", "REQUEST.NOTFOUND");
		}
		
		EventRequest currentEventReq = selectById(eventRequest.getRequestId());
		if (currentEventReq == null) {
			throw new ServiceException("Event Request not found. Nothing to update", "REQUEST.NOTFOUND");
		}
		
		currentEventReq.setHasAccomplished(eventRequest.isHasAccomplished());
		currentEventReq.setAccomplishedDate(eventRequest.getAccomplishedDate());
		eventRequestDao.updateHasAccomplished(currentEventReq);

	}

	
	
	@Override
	public void cancelRequestEvent(EventRequest eventRequest) {
		if (eventRequest == null) {
			throw new ServiceException("Event Request not found", "REQUEST.NOTFOUND");
		}
		
		EventRequest currentEventReq = selectById(eventRequest.getRequestId());
		if (currentEventReq == null) {
			throw new ServiceException("Event Request not found. Nothing to cancel", "REQUEST.NOTFOUND");
		}
		
		currentEventReq.setCancelledDate(new Date());
		currentEventReq.setHasCancelled(true);
		eventRequestDao.updateCancelEvent(currentEventReq);
	}

	@Override
	public void reactivateRequestEvent(EventRequest eventRequest) {
		if (eventRequest == null) {
			throw new ServiceException("Event Request not found", "REQUEST.NOTFOUND");
		}
		
		EventRequest currentEventReq = selectById(eventRequest.getRequestId());
		if (currentEventReq == null) {
			throw new ServiceException("Event Request not found. Nothing to cancel", "REQUEST.NOTFOUND");
		}
		
		currentEventReq.setHasCancelled(false);
		eventRequestDao.updateCancelEvent(currentEventReq);
		
	}

	@Override
	public EventRequest selectById(long eventId) {
		return eventRequestDao.selectById(eventId);
	}

	@Override
	public List<EventRequest> selectByEmail(String emailAddress) {
		return eventRequestDao.selectByEmail(emailAddress);
	}

	@Override
	public List<EventRequest> selectHasContacted() {
		return eventRequestDao.selectHasContacted(true);
	}

	@Override
	public List<EventRequest> selectHasNotContacted() {
		return eventRequestDao.selectHasContacted(false);
	}

	@Override
	public List<EventRequest> selectHasAccomplished() {
		return eventRequestDao.selectHasAccomplished(true);
	}

	@Override
	public List<EventRequest> selectHasNotAccomplished() {
		return eventRequestDao.selectHasAccomplished(false);
	}

	@Override
	public List<EventRequest> selectAll() {
		return eventRequestDao.selectAll();
	}

	public void setEventRequestDao(EventRequestDao eventRequestDao) {
		this.eventRequestDao = eventRequestDao;
	}
	public void setEmailSenderSvc(EmailSenderSvc emailSenderSvc) {
		this.emailSenderSvc = emailSenderSvc;
	}

	public void setInfoEmailAddress(String infoEmailAddress) {
		this.infoEmailAddress = infoEmailAddress;
	}
}
