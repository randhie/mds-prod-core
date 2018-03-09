package com.md.studio.json;

public class JsonSuccessView extends JsonView {
	private static final String IS_SUCCESS = "isSuccess";
	private static final String REDIRECT = "redirect";
	
	public JsonSuccessView() {
		jsonData = new JsonData();
		jsonData.put(IS_SUCCESS, true);
	}
	
	public void setRedirect(String redirectUrl) {
		jsonData.put(REDIRECT, redirectUrl);
	}

}
