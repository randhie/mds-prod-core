package com.md.studio.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.validation.BindingResult;

import com.md.studio.domain.SiteUser;
import com.md.studio.dto.SignupDto;

public class SiteUserValidation {
	private static EmailValidator validateEmail = EmailValidator.getInstance();

	public static void validateSignupForm(BindingResult errors, SignupDto siteUser) {
		
		if (!validateEmail.isValid(siteUser.getEmailAddress())) {
			errors.rejectValue("emailAddress", null, "Email address not valid");
		}
		if (StringUtils.isBlank(siteUser.getPasscode())) {
			errors.rejectValue("passcode", null, "Password cannot be blank");
		}
		if (StringUtils.isBlank(siteUser.getConfirmPassCode())) {
			errors.rejectValue("confirmPassCode", null, "Confirm Password cannot be blank");
		}
		if (!siteUser.getPasscode().equals(siteUser.getConfirmPassCode())) {
			errors.rejectValue("passcode", null, "Password and Confirm password didn't match");
		}
		if (StringUtils.isBlank(siteUser.getFirstName())) {
			errors.rejectValue("firstName", null, "First name cannot be blank");
		}
		if (StringUtils.isBlank(siteUser.getLastName())) {
			errors.rejectValue("lastName", null, "Last name cannot be blank");
		}
		
	}
}
