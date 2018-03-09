package com.md.studio.service.impl;

import com.md.studio.domain.EmailMessage;
import com.md.studio.domain.SiteUser;
import com.md.studio.domain.Testimonials;
import com.md.studio.service.AdminNotificationSvc;
import com.md.studio.service.EmailSenderSvc;

public class AdminNotificationSvcImpl implements AdminNotificationSvc {
	private EmailSenderSvc emailSenderSvc;
	private String signupNotificationTemplate;
	private String signupNotificationSmsTemplate;
	private String testiNotificationTemplate;
	private String testiNotificationSmsTemplate;
	private String contactNotificationTemplate;
	private String contactNotificationSmsTemplate;

	@Override
	public void notifyAdminWhenSignupByEmail(SiteUser siteUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAdminWhenSignupBySms(SiteUser siteUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAdminWhenCreateTestiByEmail(Testimonials testimonial) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAdminWhenCreateTestiBySms(Testimonials testimonial) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAdminWhenContactByEmail(EmailMessage emailMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAdminWhenContactBySms(EmailMessage emailMessage) {
		// TODO Auto-generated method stub
		
	}

	public void setEmailSenderSvc(EmailSenderSvc emailSenderSvc) {
		this.emailSenderSvc = emailSenderSvc;
	}

	public void setSignupNotificationTemplate(String signupNotificationTemplate) {
		this.signupNotificationTemplate = signupNotificationTemplate;
	}

	public void setSignupNotificationSmsTemplate(
			String signupNotificationSmsTemplate) {
		this.signupNotificationSmsTemplate = signupNotificationSmsTemplate;
	}

	public void setTestiNotificationTemplate(String testiNotificationTemplate) {
		this.testiNotificationTemplate = testiNotificationTemplate;
	}

	public void setTestiNotificationSmsTemplate(String testiNotificationSmsTemplate) {
		this.testiNotificationSmsTemplate = testiNotificationSmsTemplate;
	}

	public void setContactNotificationTemplate(String contactNotificationTemplate) {
		this.contactNotificationTemplate = contactNotificationTemplate;
	}

	public void setContactNotificationSmsTemplate(
			String contactNotificationSmsTemplate) {
		this.contactNotificationSmsTemplate = contactNotificationSmsTemplate;
	}
}

