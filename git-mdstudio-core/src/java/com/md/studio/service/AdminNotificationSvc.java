package com.md.studio.service;

import com.md.studio.domain.EmailMessage;
import com.md.studio.domain.SiteUser;
import com.md.studio.domain.Testimonials;

public interface AdminNotificationSvc {

	public void notifyAdminWhenSignupByEmail(SiteUser siteUser);
	
	public void notifyAdminWhenSignupBySms(SiteUser siteUser);
	
	public void notifyAdminWhenCreateTestiByEmail(Testimonials testimonial);
	
	public void notifyAdminWhenCreateTestiBySms(Testimonials testimonial);
	
	public void notifyAdminWhenContactByEmail(EmailMessage emailMessage);
	
	public void notifyAdminWhenContactBySms(EmailMessage emailMessage);
	
}
