package com.md.studio.json.jdu;

import java.util.ArrayList;
import java.util.List;

import com.md.studio.domain.EventRequest;
import com.md.studio.json.JsonContainer;
import com.md.studio.json.JsonData;

public class EventRequestJdu {
	private static final String REQUEST_ID = "requestId";
	private static final String EMAIL_ADDRESS = "emailAddress";
	private static final String FULLNAME = "fullName";
	private static final String CONTACT_NUMBER = "contactNumber";
	private static final String DESIRED_DATE = "desiredDate";
	private static final String EVENT_LOCATION = "eventLocation";
	private static final String EVENT_BUDGET = "eventBudget";
	private static final String MESSAGE = "message";
	private static final String NOTES = "notes";
	private static final String CREATE_DATE = "createDate";
	private static final String HAS_CONTACTED = "hasContacted";
	private static final String HAS_ACCOMPLISHED = "hasAccomplished";
	private static final String CONTACTED_DATE = "contactedDate";
	private static final String CANCELLED_DATE = "cancelledDate";
	private static final String HAS_CANCELLED = "hasCancelled";
	public static final String EVENT_REQUEST = "eventRequest";
	public static final String EVENT_REQUEST_LIST = "eventRequestList";
	
	
	public static void buildJson(EventRequest eventRequest, JsonContainer container, String objName) {
		if (eventRequest == null) {
			return;
		}
		container.put(objName, getJson(eventRequest));
	}
	
	public static void buildJson(List<EventRequest> eventRequestList, JsonContainer container, String objName) {
		if (eventRequestList == null || eventRequestList.isEmpty()) {
			return;
		}
		
		List<JsonData> jsonDataList = new ArrayList<JsonData>();
		for (EventRequest evt: eventRequestList) {
			jsonDataList.add(getJson(evt));
		}
		
		container.put(objName, jsonDataList);
	}
	
	
	
	public static JsonData getJson(EventRequest eventRequest) {
		JsonData j = new JsonData();
		j.put(REQUEST_ID, eventRequest.getRequestId());
		j.put(EMAIL_ADDRESS, eventRequest.getEmailAddress());
		j.put(FULLNAME, eventRequest.getFullName());
		j.put(CONTACT_NUMBER, eventRequest.getContactNumber());
		j.put(DESIRED_DATE, eventRequest.getDesiredDate().toString());
		j.put(EVENT_LOCATION, eventRequest.getEventLocation());
		j.put(EVENT_BUDGET, eventRequest.getEventBudget());
		j.put(MESSAGE, eventRequest.getMessage());
		j.put(NOTES, eventRequest.getNotes());
		j.put(CREATE_DATE, eventRequest.getCreateDate().toString());
		j.put(HAS_CONTACTED, eventRequest.isHasContacted());
		j.put(HAS_ACCOMPLISHED, eventRequest.isHasAccomplished());
		j.put(CONTACTED_DATE, eventRequest.getContactedDate().toString());
		j.put(CANCELLED_DATE, eventRequest.getCancelledDate().toString());
		j.put(HAS_CANCELLED, eventRequest.isHasAccomplished());
		return j;
	}
	
	
}
