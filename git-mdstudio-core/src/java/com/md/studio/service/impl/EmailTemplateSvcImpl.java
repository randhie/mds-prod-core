package com.md.studio.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.md.studio.dao.EmailTemplateDao;
import com.md.studio.domain.EmailTemplate;
import com.md.studio.service.EmailTemplateSvc;
import com.md.studio.service.Refreshable;
import com.md.studio.service.ServiceException;

public class EmailTemplateSvcImpl implements EmailTemplateSvc, Refreshable {
	private Map<String, EmailTemplate> emailTemplateCache = new HashMap<String, EmailTemplate>();
	private EmailTemplateDao emailTemplateDao;

	
	@Override
	public void createTemplate(EmailTemplate emailTemplate) {
		if (emailTemplate == null) {
			throw new ServiceException("Email template is null.", "EMAILTEMPLATE.NOTFOUND");
		}
		
		EmailTemplate currentTemplate = emailTemplateDao.selectByCode(emailTemplate.getTemplateCode());
		if (currentTemplate != null) {
			throw new ServiceException("Email template exist.", "EMAILTEMPLATE.EXIST");
		}
		
		if (emailTemplateCache.containsKey(emailTemplate.getTemplateCode())) {
			emailTemplateCache.remove(emailTemplate.getTemplateCode());
		}
		
		emailTemplate.setCreatedDate(new Date());
		emailTemplateDao.insert(emailTemplate);
		emailTemplateCache.put(emailTemplate.getTemplateCode(), emailTemplate);
	}

	@Override
	public void removeTemplate(EmailTemplate emailTemplate) {
		if (emailTemplate == null) {
			throw new ServiceException("Email template is null.", "EMAILTEMPLATE.NOTFOUND");
		}
		
		if (emailTemplateCache.containsKey(emailTemplate.getTemplateCode())) {
			emailTemplateCache.remove(emailTemplate.getTemplateCode());
		}
		emailTemplateDao.delete(emailTemplate);
	}

	
	@Transactional
	@Override
	public void updateTemplate(EmailTemplate emailTemplate) {
		if (emailTemplate == null) {
			throw new ServiceException("Email template is null.", "EMAILTEMPLATE.NOTFOUND");
		}
		
		try {
			removeTemplate(emailTemplate);
			createTemplate(emailTemplate);
		}catch(ServiceException se) {
			throw new ServiceException(se.getMessage(), se.getErrorCode());
		}
	}

	@Override
	public EmailTemplate getTemplateByCode(String templateCode) {
		if (emailTemplateCache.containsKey(templateCode)) {
			return emailTemplateCache.get(templateCode);
		} 
		
		EmailTemplate emailTemplate = emailTemplateDao.selectByCode(templateCode);
		if (emailTemplate != null) {
			emailTemplateCache.put(emailTemplate.getTemplateCode(), emailTemplate);
		}
		return emailTemplate;
	}

	@Override
	public List<EmailTemplate> getAllTemplates() {
		List<EmailTemplate> emailTemplateList = null;
		if (emailTemplateCache.isEmpty()) {
			emailTemplateList = emailTemplateDao.selectAll();
			
			if (emailTemplateList != null) {
				for (EmailTemplate emailTemp: emailTemplateList) {
					emailTemplateCache.put(emailTemp.getTemplateCode(), emailTemp);
				}	
			}
			else {
				emailTemplateList = new ArrayList<EmailTemplate>();
			}
		}
		return emailTemplateList;
	}

	@Override
	public void refresh(String arg) {
		emailTemplateCache.clear();
		getAllTemplates();
	}

	public void setEmailTemplateDao(EmailTemplateDao emailTemplateDao) {
		this.emailTemplateDao = emailTemplateDao;
	}
}
