package com.md.studio.service;

import java.util.List;

import com.md.studio.domain.EmailTemplate;

public interface EmailTemplateSvc {
	
	public void createTemplate(EmailTemplate emailTemplate);
	
	public void removeTemplate(EmailTemplate emailTemplate);
	
	public void updateTemplate(EmailTemplate emailTemplate);
	
	public EmailTemplate getTemplateByCode(String templateCode);
	
	public List<EmailTemplate> getAllTemplates();

}
