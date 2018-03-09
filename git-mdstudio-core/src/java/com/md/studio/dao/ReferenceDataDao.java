package com.md.studio.dao;

import java.util.List;

import com.md.studio.domain.ReferenceData;

public interface ReferenceDataDao {
	
	public void insert(ReferenceData referenceData);
	
	public void update(ReferenceData referenceData);
	
	public void delete(ReferenceData referenceData);
	
	public List<ReferenceData> selectByType(String refType);
	
	public ReferenceData select(String refType, String refCode);
	
	public List<ReferenceData> selectAll();
}
