package com.md.studio.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReferenceData implements Serializable {
	private static final long serialVersionUID = -6953684121760081832L;
	
	private String refType;
	private String refCode;
	private String refValue;
	
	
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}
	public String getRefCode() {
		return refCode;
	}
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	public String getRefValue() {
		return refValue;
	}
	public void setRefValue(String refValue) {
		this.refValue = refValue;
	}
	
	
	public List<String> getParsePipeRefValue() {
		List<String> refValueList = new ArrayList<String>();
		if (this.refValue.contains("|")) {
			String[] refValues = this.refValue.trim().split("\\|");
			for (String val: refValues) {
				refValueList.add(val);
			}
		}
		return refValueList;
	}
}
