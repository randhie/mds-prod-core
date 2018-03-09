package com.md.studio.dao;

import java.util.List;

import com.md.studio.domain.EmailTemplate;

public interface EmailTemplateDao {
	
	public void insert(EmailTemplate emailTemplate);
	
	public void delete(EmailTemplate emailTemplate);
	
	public EmailTemplate selectByCode(String templateCode);
	
	public List<EmailTemplate> selectAll();

}
