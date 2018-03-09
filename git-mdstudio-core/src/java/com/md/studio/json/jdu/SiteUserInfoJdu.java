package com.md.studio.json.jdu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.md.studio.domain.SiteUser;
import com.md.studio.json.JsonContainer;
import com.md.studio.json.JsonData;

public class SiteUserInfoJdu {
	public static final int BASIC_USERINFO = 0;
	public static final int ESSENTIAL_USERINFO = 1;
	public static final int ALL_USERINFO = 2;
	
	public static void buildJson(JsonContainer container, SiteUser siteUser, String modelName, int infoLevel) {
		if (siteUser == null) {
			return;
		}
		
		container.put(modelName, getJson(siteUser, infoLevel));
		
	}
	
	public static void buildJson(JsonContainer container, List<SiteUser> siteUser, String modelName, int infoLevel) {
		if (siteUser == null || siteUser.isEmpty()) {
			return;
		}
		
		List<JsonData> jsonData = new ArrayList<JsonData>();
		for (SiteUser user: siteUser) {
			jsonData.add(getJson(user, infoLevel));
		}
		
		container.put(modelName, jsonData);
	}
	
	
	private static JsonData getJson(SiteUser siteUser, int infoLevel) {
		JsonData j = new JsonData();
		
		switch (infoLevel) {
		case ALL_USERINFO:
			j.put("signupBrowser", siteUser.getSignupBrowser());
			j.put("lastLoginBrowser", siteUser.getLastLoginBrowser());
			j.put("hasInvited", siteUser.isHasInvited());
			j.put("activateCode", siteUser.getActivationCode());
			j.put("referralCode", siteUser.getReferralCode());
		case ESSENTIAL_USERINFO:
			j.put("signupDate", siteUser.getSignupDate() != null? siteUser.getSignupDate().toString(): "");
			j.put("signupIpAddress", siteUser.getSignupIpAddress());
			j.put("lastLogin", siteUser.getLastLogin() != null? siteUser.getLastLogin().toString(): "");
			j.put("loginIpAddress", siteUser.getLoginIpAddress());
			j.put("dateActivated", siteUser.getDateActivated() != null ? siteUser.getDateActivated(): "");
		case BASIC_USERINFO:
			j.put("userId", siteUser.getUserId());
			j.put("emailAddress", siteUser.getEmailAddress());
			j.put("firstName", siteUser.getFirstName());
			j.put("lastName", siteUser.getLastName());
			j.put("userStatus", siteUser.getUserStatus());
			j.put("authAlbums", StringUtils.isBlank(siteUser.getAuthAlbums())? " " : siteUser.getAuthAlbums());
			j.put("authUploads", StringUtils.isBlank(siteUser.getAuthUploads())? " ": siteUser.getAuthUploads());
			j.put("isAdmin", siteUser.isAdmin());
		default:
			break;
		}
		return j;
	}

}
