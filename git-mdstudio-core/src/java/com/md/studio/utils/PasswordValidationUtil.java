package com.md.studio.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PasswordValidationUtil {
	
	public static List<SvcValidationUtil> validatePassword(String currentPw, String newPw, String newPwConfirm){
		List<SvcValidationUtil> errors = new ArrayList<SvcValidationUtil>();
		
		if (StringUtils.isBlank(currentPw)) {
			errors.add(new SvcValidationUtil("currentPw", null, "Password required"));
		}
		if (StringUtils.isBlank(newPw)) {
			errors.add(new SvcValidationUtil("newPw", null, "New password required"));
		}
		if (StringUtils.isBlank(newPwConfirm)) {
			errors.add(new SvcValidationUtil("newPwConfirm", null, "New password required"));
		}
		else if (!newPw.equals(newPwConfirm)) {
			errors.add(new SvcValidationUtil("newPwConfirm", null, "New password entered not match"));
		}
		
		return errors;
	}
}
