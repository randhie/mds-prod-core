package com.md.studio.service;

import java.util.Date;
import java.util.List;

import com.md.studio.domain.SiteUserActivityLog;

public interface SiteUserActivityLogSvc {
	
	public void recordActivityLog(SiteUserActivityLog log);
	
	public List<SiteUserActivityLog> getActivityLog(Date fromDate, Date afterDate, Integer limit, Integer offset);
	
	
	// Queue listener 
	public void processActivityLog(SiteUserActivityLog log);
	
}
