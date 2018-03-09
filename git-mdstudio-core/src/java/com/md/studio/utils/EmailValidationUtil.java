package com.md.studio.utils;

import static com.md.studio.utils.WebConstants.*;
import org.apache.commons.validator.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.md.studio.domain.EmailMessage;

public class EmailValidationUtil implements Validator {
	private EmailValidator emailValidator = EmailValidator.getInstance();
	
	@Override
	public boolean supports(Class<?> target) {
		return EmailMessage.class.isAssignableFrom(target);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		EmailMessage emailMessage = (EmailMessage) obj;

		ValidationUtils.rejectIfEmpty(errors, "fromEmail", MSGCODE_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, "fromName", MSGCODE_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, "toEmail", MSGCODE_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, "subject", MSGCODE_REQUIRED);
		
		if (!errors.hasFieldErrors("fromEmail")) {
			if (!emailValidator.isValid(emailMessage.getFromEmail())) {
				errors.reject("fromEmail", null, "Invalid email Address");
			}
		}
		
		if (!errors.hasFieldErrors("toEmail")) {
			if (!emailValidator.isValid(emailMessage.getToEmail())) {
				errors.reject("toEmail", null, "Invalid email Address");
			}
		}
		
	}

}
