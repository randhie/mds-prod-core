package com.md.studio.json;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.md.studio.utils.SvcValidationUtil;

public class JsonErrorView extends JsonView{
	private static final String IS_SUCCESS = "isSuccess";
	private static final String ERROR = "error";
	private static final String ERROR_CODE = "code";
	private static final String ERROR_MSG = "msg";
	private static final String ERROR_FIELD = "field";
	private static final String FIELD_ERRORS = "fieldErrors";
	
	public JsonErrorView() {
		init();
	}
	
	public JsonErrorView(String errorCode, String errorMsg) {
		init();
		setGlobalError(errorCode, errorMsg);
	}
	
	public JsonErrorView(BindingResult errors) {
		init();
		setFieldErrors(errors);
	}
	
	
	public JsonErrorView(List<SvcValidationUtil> errors) {
		init();
		setGlobalError(errors);
	}
	
	
	private void setGlobalError(List<SvcValidationUtil> errors) {
		List<JsonData> fieldErrors = new ArrayList<JsonData>();
		for (SvcValidationUtil svc: errors) {
			JsonData jsonError = new JsonData();
			jsonError.put(ERROR_FIELD, svc.getField());
			jsonError.put(ERROR_CODE, svc.getCode());
			jsonError.put(ERROR_MSG, svc.getErrorMsg());
			fieldErrors.add(jsonError);
		}
		jsonData.put(FIELD_ERRORS, fieldErrors);
	}
	
	
	private void init() {
		jsonData = new JsonData();
		jsonData.put(IS_SUCCESS, false);
	}
	
	private void setGlobalError(String errorCode, String errorMsg) {
		JsonData globalError = new JsonData();
		globalError.put(ERROR_CODE, errorCode);
		globalError.put(ERROR_MSG, errorMsg);
		jsonData.put(ERROR, globalError);
	}
	
	private void setFieldErrors(BindingResult errors) {
		List<JsonData> fieldErrors = new ArrayList<JsonData>();
		for (FieldError error: errors.getFieldErrors()) {
			JsonData jsonError = new JsonData();
			jsonError.put(ERROR_FIELD, error.getField());
			jsonError.put(ERROR_CODE, error.getCode());
			jsonError.put(ERROR_MSG, error.getDefaultMessage());
			fieldErrors.add(jsonError);
		}
		jsonData.put(FIELD_ERRORS, fieldErrors);
		
	}
	

}
