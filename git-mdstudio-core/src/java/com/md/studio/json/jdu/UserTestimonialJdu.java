package com.md.studio.json.jdu;

import java.util.ArrayList;
import java.util.List;

import com.md.studio.domain.Testimonials;
import com.md.studio.json.JsonContainer;
import com.md.studio.json.JsonData;

public class UserTestimonialJdu {
	private static final String TESTIMONIAL_ID = "testimonialId";
	private static final String EMAIL_ADDRESS = "emailAddress";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String MESSAGE = "message";
	private static final String SOCIAL_MEDIA_ACCT = "socialMediaAcct";
	private static final String URL_ADDRESS = "urlAddress";
	private static final String BROWSER_INFO = "browserInfo";
	private static final String DATE_CREATED = "dateCreated";
	private static final String UPDATE_DATE = "updateDate";
	private static final String IP_ADDRESS = "ipAddress";
	private static final String REGEX_SEPARATOR = "\\|";
	
	
	public static void buildJson(JsonContainer container, Testimonials testimonials, String jsonName){
		container.put(jsonName, getJson(testimonials));
	}
	
	public static void buildJson(JsonContainer container, List<Testimonials> testimonialList, String jsonName) {
		if (testimonialList == null || testimonialList.isEmpty()) {
			return;
		}
		
		List<JsonData> jsonDataList = new ArrayList<JsonData>();
		for (Testimonials testi: testimonialList) {
			jsonDataList.add(getJson(testi));
		}
		
		container.put(jsonName, jsonDataList);
	}
	
	
	public static JsonData getJson(Testimonials testimonials) {
		JsonData jsonData = new JsonData();
		jsonData.put(TESTIMONIAL_ID, testimonials.getTestimonialId());
		jsonData.put(EMAIL_ADDRESS, testimonials.getEmailAddress());
		jsonData.put(FIRST_NAME, testimonials.getFirstName());
		jsonData.put(LAST_NAME, testimonials.getLastName());
		jsonData.put(MESSAGE, testimonials.getMessage().replaceAll(REGEX_SEPARATOR, "<br/>&nbsp;"));
		jsonData.put(SOCIAL_MEDIA_ACCT, testimonials.getSocialMediaAcct());
		jsonData.put(URL_ADDRESS, testimonials.getUrlAddress());
		jsonData.put(BROWSER_INFO, testimonials.getBrowserInfo());
		jsonData.put(DATE_CREATED, testimonials.getDateCreated() == null? "": testimonials.getDateCreated().toString());
		jsonData.put(UPDATE_DATE, testimonials.getUpdateDate() == null? "": testimonials.getUpdateDate().toString());
		jsonData.put(IP_ADDRESS, testimonials.getIpAddress());
		return jsonData;
	}
	
}
