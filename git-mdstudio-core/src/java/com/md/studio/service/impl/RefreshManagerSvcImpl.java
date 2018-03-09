package com.md.studio.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.md.studio.service.RefreshManagerSvc;
import com.md.studio.service.Refreshable;

public class RefreshManagerSvcImpl implements RefreshManagerSvc, ApplicationContextAware {
	private static final Logger LOGGER = Logger.getLogger(RefreshManagerSvcImpl.class);
	private ApplicationContext ctx;
	
	@Override
	public void processRefreshRequest(String arg) {
		if (StringUtils.isEmpty(arg)) {
			LOGGER.info("Refresh request contains an empty Argument");
			return;
		}
		
		String beanId = arg.trim();
		Object bean = null;
		
		try {
			bean = ctx.getBean(beanId);
		}
		catch(NoSuchBeanDefinitionException e) {
			LOGGER.info("Received request to refresh bean '" + beanId + "'. NO SUCH BEAN in context ... ignoring");
			return;
		}
		catch (Exception e) {
			LOGGER.info("Unexpected exception while attempting to refresh bean " + beanId, e);
		}
		
		if (bean instanceof Refreshable) {
			LOGGER.info("Invoking refresh method of bean " + beanId);
			((Refreshable) bean).refresh(null);
		}
		else {
			LOGGER.info("Receieved request to refresh bean '" + beanId + "'. Bean is NOT REFRESHABLE ... ignoring");
		}
		
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.ctx = ctx;
		
	}

	

}
