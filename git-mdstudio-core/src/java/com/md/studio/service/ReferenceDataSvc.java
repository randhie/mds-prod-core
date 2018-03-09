package com.md.studio.service;

import java.util.List;

import com.md.studio.domain.ReferenceData;

public interface ReferenceDataSvc {
	
	public void create(ReferenceData referenceData);
	
	public void remove(ReferenceData referenceData);
	
	public void update(ReferenceData referenceData);
	
	public List<ReferenceData> getByRefType(String refType);
	
	public ReferenceData getRefData(String refType, String refCode);
	
	public List<ReferenceData> getAll();
}
