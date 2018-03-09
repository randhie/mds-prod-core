package com.md.studio.service;

import java.util.List;

import com.md.studio.domain.SiteUserRate;
import com.md.studio.domain.Testimonials;

public interface UserTestimonialSvc {
	
	public void createTestimonial(Testimonials testimonial);
	
	public void updateTestimonial(Testimonials testimonial);
	
	public void removeTestimonial(String testimonialId);
	
	public void removeAllTestimonial(String emailAddress);
	
	public List<Testimonials> getAllByFilter(int limit, int offset);
	
	public List<Testimonials> getAllByEmail(String emailAddress);
	
	public Testimonials getById(long testimonialId);
	
	public List<SiteUserRate> getAllTestimonialRate();

}
