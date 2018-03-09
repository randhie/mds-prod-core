package com.md.studio.utils;

import static com.md.studio.utils.WebConstants.MSGCODE_REQUIRED;

import org.apache.commons.validator.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.md.studio.domain.EventRequest;

public class EventRequestValidator implements Validator {
	private EmailValidator emailValidator = EmailValidator.getInstance();
	
	@Override
	public boolean supports(Class<?> target) {
		return EventRequest.class.isAssignableFrom(target);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		EventRequest eventRequest = (EventRequest) obj;
		
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", MSGCODE_REQUIRED, "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", MSGCODE_REQUIRED, "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactNumber", MSGCODE_REQUIRED, "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventLocation", MSGCODE_REQUIRED, "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventBudget", MSGCODE_REQUIRED, "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message", MSGCODE_REQUIRED, "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "notes", MSGCODE_REQUIRED, "required");
		

		if (!errors.hasFieldErrors("emailAddress")) {
			if (!emailValidator.isValid(eventRequest.getEmailAddress())) {
				errors.rejectValue("emailAddress", null, "Invalid email address");
			}
		}
		
		if (eventRequest.getDesiredDate() == null) {
			errors.rejectValue("desiredDate", "field.required", "required");
		}
	}

}
