package com.md.studio.utils;
import static com.md.studio.utils.WebConstants.*;

import org.apache.commons.validator.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.md.studio.domain.Testimonials;

public class ValidationUtil implements Validator{
	private EmailValidator emailValidator = EmailValidator.getInstance();
	
	@Override
	public boolean supports(Class<?> target) {
		return Testimonials.class.isAssignableFrom(target);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Testimonials testimonial = (Testimonials)obj;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", MSGCODE_REQUIRED, "Email Address Missing");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", MSGCODE_REQUIRED, "First name cannot be blank");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", MSGCODE_REQUIRED, "Last name cannot be blank");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message", MSGCODE_REQUIRED, "Message cannot be blank");

		if (!errors.hasFieldErrors("emailAddress")) {
			if (!emailValidator.isValid(testimonial.getEmailAddress())) {
				errors.rejectValue("emailAddress", null, "Invalid email address");
			}
		}
	}
}
