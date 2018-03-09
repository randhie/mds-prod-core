package com.md.studio.service;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -325436951898813996L;
	private String errorCode;
	
	public ServiceException(String msg) {
		super(msg);
	}
	
	public ServiceException(String msg, String errorCode){
		super(msg);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
