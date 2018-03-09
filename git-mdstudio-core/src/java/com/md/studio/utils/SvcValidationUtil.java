package com.md.studio.utils;

import java.io.Serializable;

public class SvcValidationUtil implements Serializable {
	private static final long serialVersionUID = -7437381771985555114L;
	private String field;
	private String code;
	private String errorMsg;
	
	
	public SvcValidationUtil(String field, String errorMsg) {
		this.field = field;
		this.errorMsg = errorMsg;
	}
	
	public SvcValidationUtil(String field, String code, String errorMsg) {
		this.field = field;
		this.code = code;
		this.errorMsg = errorMsg;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
