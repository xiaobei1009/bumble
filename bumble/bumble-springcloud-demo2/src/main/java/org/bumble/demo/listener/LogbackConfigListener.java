
package org.bumble.demo.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.bumble.base.log.LogInitializer;

import ch.qos.logback.ext.spring.web.WebLogbackConfigurer;

@WebListener
public class LogbackConfigListener implements ServletContextListener {
	
	public void contextDestroyed(ServletContextEvent event) {
		WebLogbackConfigurer.shutdownLogging(event.getServletContext());
	}
	
	public void contextInitialized(ServletContextEvent event) {
		LogInitializer logIntializer = new LogInitializer();
		logIntializer.init();
		WebLogbackConfigurer.initLogging(event.getServletContext());
	}	
}
