package com.md.studio.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.md.studio.domain.EmailMessage;
import com.md.studio.dto.EmailProcessDto;

public interface EmailSenderSvc {
	
	public void sendEmail(EmailMessage emailMessage, List<MultipartFile> attachments);
	
	public void processEmail(EmailProcessDto emailProcessDto);

}
