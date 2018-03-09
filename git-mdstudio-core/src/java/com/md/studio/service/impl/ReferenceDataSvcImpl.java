package com.md.studio.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.md.studio.dao.ReferenceDataDao;
import com.md.studio.domain.ReferenceData;
import com.md.studio.service.ReferenceDataSvc;
import com.md.studio.service.Refreshable;
import com.md.studio.service.ServiceException;

public class ReferenceDataSvcImpl implements ReferenceDataSvc, Refreshable {
	private Map<String, ReferenceData> referenceDatListCache = new HashMap<String, ReferenceData>();
	private ReferenceDataDao referenceDataDao;

	@Override
	public void create(ReferenceData referenceData) {
		ReferenceData currentRefData = getRefData(referenceData.getRefType(), referenceData.getRefCode());
		if (currentRefData == null) {
			referenceDataDao.insert(referenceData);
			refresh(null);
		}
		else {
			throw new ServiceException("Reference Data Exist", "REFDATA.EXIST");
		}

	}

	@Override
	public void remove(ReferenceData referenceData) {
		referenceDataDao.delete(referenceData);
		refresh(null);
	}

	@Override
	public void update(ReferenceData referenceData) {
		ReferenceData currentRefData = getRefData(referenceData.getRefType(), referenceData.getRefCode());
		if (currentRefData == null) {
			throw new ServiceException("Reference Data Does not exist", "REFDATA.NOTEXIST");
		}
		referenceDataDao.update(referenceData);
		refresh(null);
	}

	@Override
	public List<ReferenceData> getByRefType(String refType) {
		return referenceDataDao.selectByType(refType);
	}

	@Override
	public ReferenceData getRefData(String refType, String refCode) {
		if (referenceDatListCache.containsKey(refType + "_" + refCode)) {
			return referenceDatListCache.get(refType + "_" + refCode);
		}
		
		ReferenceData referenceData = referenceDataDao.select(refType, refCode);
		if (referenceData != null) {
			referenceDatListCache.put(refType + "_" + refCode, referenceData);	
		}
		
		return referenceData;
	}

	@Override
	public List<ReferenceData> getAll() {
		return referenceDataDao.selectAll();
	}

	public void setReferenceDataDao(ReferenceDataDao referenceDataDao) {
		this.referenceDataDao = referenceDataDao;
	}

	@Override
	public void refresh(String arg) {
		referenceDatListCache.clear();
	}
}
