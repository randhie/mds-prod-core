package com.md.studio.dao;

import java.util.List;

import com.md.studio.domain.SiteUserRate;
import com.md.studio.domain.Testimonials;

public interface UserTestimonialDao {
	
	public void insert(Testimonials testimonial);
	
	public void update(Testimonials testimonial);

	public void delete(String testimonialId);
	
	public void deleteAllByEmail(String emailAddress);
	
	public Testimonials getTestimonial(long testimonialId);
	
	public List<Testimonials> getTestimonial(String emailAddress);
	
	public List<Testimonials> getAllTestimonials(int limit, int offset);
	
	public List<SiteUserRate> getAllTestimonialRates();
}
