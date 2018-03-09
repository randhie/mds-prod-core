package com.md.studio.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

public class WhoIsApiUtil {
	private static final Logger LOGGER = Logger.getLogger(WhoIsApiUtil.class);
	private static final String HTTP_RESPONSE = "Successfull HttpResponse ===> ";
	private static final String HTTP_ERROR_RESPONSE = "Error Response ===> ";
	private static final String HTTP_ERROR_REQUEST = "Could not make whois ip request.";
	private HttpClient client;

	private String domainAttrib;
	private String usernameAttrib;
	private String passwordAttrib;
	private String outputformatAttrib;
	
	private String uri;
	private String username;
	private String password;
	private String outputFormat;
	
	public String processWhoIsIp(String ipAddress) {
		if (StringUtils.isBlank(ipAddress)) {
			return null;
		}
		
		String response = null;
		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(uri);
			postMethod.addParameter(domainAttrib, ipAddress);
			postMethod.addParameter(usernameAttrib, username);
			postMethod.addParameter(passwordAttrib, password);
			postMethod.addParameter(outputformatAttrib, outputFormat);
			
			int httpStatus = client.executeMethod(postMethod);
			if (httpStatus == HttpStatus.SC_OK) {
				String responseBody = postMethod.getResponseBodyAsString();
				if (StringUtils.isNotBlank(responseBody)) {
					response = responseBody;
				}
//				LOGGER.info(HTTP_RESPONSE + responseBody);
			}
			else {
				LOGGER.error(HTTP_ERROR_RESPONSE + httpStatus);
			}
			
		}
		catch (Exception e) {
			LOGGER.error(HTTP_ERROR_REQUEST, e);
		}
		finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}

//		try {
//			JSONObject json = new JSONObject(response);
//			System.out.println(json);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return response;
	}


	public void setClient(HttpClient client) {
		this.client = client;
	}
	public void setDomainAttrib(String domainAttrib) {
		this.domainAttrib = domainAttrib;
	}
	public void setUsernameAttrib(String usernameAttrib) {
		this.usernameAttrib = usernameAttrib;
	}
	public void setPasswordAttrib(String passwordAttrib) {
		this.passwordAttrib = passwordAttrib;
	}
	public void setOutputformatAttrib(String outputformatAttrib) {
		this.outputformatAttrib = outputformatAttrib;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
}
