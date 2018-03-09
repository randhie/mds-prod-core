package com.md.studio.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.md.studio.dao.UserTestimonialDao;
import com.md.studio.domain.EmailMessage;
import com.md.studio.domain.SiteUserRate;
import com.md.studio.domain.Testimonials;
import com.md.studio.service.EmailSenderSvc;
import com.md.studio.service.ServiceException;
import com.md.studio.service.UserTestimonialSvc;

public class UserTestimonialSvcImpl implements UserTestimonialSvc {
	private static final String ERROR_CODE_NOTFOUND = "TESTIMONIAL.NOTFOUND";
	private static final String ERROR_MSG_NOTFOUND = "Testimonial not found";
	private static final String ERROR_CODE_PARAM_NOTFOUND = "PARAM.NOTFOUND";
	private static final String ERROR_MSG_PARAM_NOTFOUND = " is missing";
	private static final String C_NEXTLINE = "\r\n";
	private static final String C_BAR = "|";
	private List<SiteUserRate> siteUserTestimonialRate = new ArrayList<SiteUserRate>();
	private UserTestimonialDao userTestimonialDao;
	private EmailSenderSvc emailSenderSvc;
		
	@Override
	@Transactional
	public void createTestimonial(Testimonials testimonial) {
		testimonial.setMessage(testimonial.getMessage().replaceAll(C_NEXTLINE, C_BAR));
		testimonial.setDateCreated(new Date());
		userTestimonialDao.insert(testimonial);
		
		EmailMessage email = new EmailMessage();
		email.setFromEmail(testimonial.getEmailAddress());
		email.setFromName(testimonial.getFullName());
		email.setMessageBody(testimonial.getMessage());
		email.setHtml(true);
		email.setSentBy(testimonial.getFullName());
		email.setToEmail("randyordinario@mdsnapshots.com");
		email.setSubject("Testimonial from " + testimonial.getFullName());
		email.setUseShawMail(true);
		email.setMailServer();
		emailSenderSvc.sendEmail(email, null);
		
		siteUserTestimonialRate.clear();
	}

	@Override
	public void updateTestimonial(Testimonials testimonial) {
		Testimonials currentTestimonial = getById(testimonial.getTestimonialId());
		if (currentTestimonial == null) {
			throw new ServiceException(ERROR_MSG_NOTFOUND, ERROR_CODE_NOTFOUND);
		}
		currentTestimonial.setEmailAddress(testimonial.getEmailAddress());
		currentTestimonial.setFirstName(testimonial.getFirstName());
		currentTestimonial.setLastName(testimonial.getLastName());
		currentTestimonial.setMessage(testimonial.getMessage());
		currentTestimonial.setSocialMediaAcct(testimonial.getSocialMediaAcct());
		currentTestimonial.setUrlAddress(testimonial.getUrlAddress());
		currentTestimonial.setUpdateDate(new Date());
		
		userTestimonialDao.update(currentTestimonial);
	}

	@Override
	public void removeTestimonial(String testimonialId) {
		userTestimonialDao.delete(testimonialId);
		siteUserTestimonialRate.clear();
	}

	@Override
	public void removeAllTestimonial(String emailAddress) {
		userTestimonialDao.deleteAllByEmail(emailAddress);
		siteUserTestimonialRate.clear();
	}

	@Override
	public List<Testimonials> getAllByFilter(int limit, int offset) {
		if (StringUtils.isBlank(Integer.toString(limit))) {
			limit = 50;
		}
		if (StringUtils.isBlank(Integer.toString(offset))) {
			offset = 0;
		}
		
		return userTestimonialDao.getAllTestimonials(limit, offset);
	}

	@Override
	public List<Testimonials> getAllByEmail(String emailAddress) {
		if (StringUtils.isBlank(emailAddress)) {
			throw new ServiceException("Email Address" + ERROR_MSG_PARAM_NOTFOUND, ERROR_CODE_PARAM_NOTFOUND);
		}
		return getAllByEmail(emailAddress);
	}

	@Override
	public Testimonials getById(long testimonialId) {
		if (StringUtils.isBlank(Long.toString(testimonialId))) {
			throw new ServiceException("Testimonial ID" + ERROR_MSG_PARAM_NOTFOUND, ERROR_CODE_PARAM_NOTFOUND);
		}
		return getById(testimonialId);
	}
	
	
	
	@Override
	public List<SiteUserRate> getAllTestimonialRate() {
		if (siteUserTestimonialRate.isEmpty()) {
			siteUserTestimonialRate.addAll(userTestimonialDao.getAllTestimonialRates());
			return siteUserTestimonialRate;
		}
		
		return siteUserTestimonialRate;
	}

	public void setUserTestimonialDao(UserTestimonialDao userTestimonialDao) {
		this.userTestimonialDao = userTestimonialDao;
	}
	public void setEmailSenderSvc(EmailSenderSvc emailSenderSvc) {
		this.emailSenderSvc = emailSenderSvc;
	}
}
