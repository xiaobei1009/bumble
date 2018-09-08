package org.bumble.demo.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class MyServletContextListener implements ServletContextListener {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.debug("servlet context listener init...");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}

}
