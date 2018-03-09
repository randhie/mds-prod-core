package com.md.studio.json.jdu;

import java.util.ArrayList;
import java.util.List;

import com.md.studio.domain.EventInfo;
import com.md.studio.json.JsonContainer;
import com.md.studio.json.JsonData;

public class EventInfoJdu {
	private static final String EVENT_ID = "eventId";
	private static final String EVENT_NAME = "eventName";
	private static final String DESCRIPTION = "description";
	private static final String EVENT_DATE = "eventDate";
	private static final String EVENT_BANNER_LOCATION = "bannerLocation";
	private static final String CREATE_DATE = "createDate";
	private static final String CREATED_BY = "createdBy";
	private static final String UPDATE_DATE = "updateDate";
	private static final String UPDATED_BY = "updatedBy";
	private static final String EVENT_WEB_URL = "eventWebUrl";
	private static final String EVENT_ARCHIVE_URL = "eventArchiveUrl";
	
	public static final String EVENT_INFO = "eventInfo";
	public static final String EVENT_INFO_LIST = "eventInfoList";

	
	public static void buildJson(EventInfo eventInfo, JsonContainer container, String objName) {
		if (eventInfo == null) {
			return;
		}
		container.put(objName, getJson(eventInfo));
	}
	
	public static void buildJson(List<EventInfo> eventInfo, JsonContainer container, String objName) {
		if (eventInfo == null || eventInfo.isEmpty()) {
			return;
		}
		
		List<JsonData> jsonDataList = new ArrayList<JsonData>();
		for (EventInfo evt: eventInfo) {
			jsonDataList.add(getJson(evt));
		}
		
		container.put(objName, jsonDataList);
	}
	
	public static JsonData getJson(EventInfo eventInfo) {
		JsonData j = new JsonData();
		j.put(EVENT_ID, eventInfo.getEventId());
		j.put(EVENT_NAME, eventInfo.getEventName());
		j.put(DESCRIPTION, eventInfo.getDescription());
		j.put(EVENT_DATE, eventInfo.getEventDate().toString() == null? "": eventInfo.getEventDate().toString());
		j.put(EVENT_BANNER_LOCATION, eventInfo.getEventBannerLocation());
		j.put(CREATE_DATE, eventInfo.getCreateDate().toString() == null? "": eventInfo.getCreateDate().toString());
		j.put(CREATED_BY, eventInfo.getCreatedBy());
		j.put(UPDATE_DATE, eventInfo.getUpdateDate().toString() == null? "": eventInfo.getUpdateDate().toString());
		j.put(UPDATED_BY, eventInfo.getUpdatedBy());
		j.put(CREATE_DATE, eventInfo.getCreateDate().toString() == null? "": eventInfo.getCreateDate().toString());
		j.put(EVENT_WEB_URL, eventInfo.getEventWebUrl());
		j.put(EVENT_ARCHIVE_URL, eventInfo.getEventArchiveUrl());
		return j;
	}
}
