package com.md.studio.utils;

import org.springframework.jms.core.JmsTemplate;

public class JmsMessageSender {
	private JmsTemplate queueJmsTemplate;

	public void sendToQueue(String destination, Object message){
		queueJmsTemplate.convertAndSend(destination, message);
	}

	
	public void sendToQueue(String destination){
		queueJmsTemplate.convertAndSend(destination);
	}

	
	public void setQueueJmsTemplate(JmsTemplate queueJmsTemplate) {
		this.queueJmsTemplate = queueJmsTemplate;
	}
}
