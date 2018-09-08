package org.bumble.base.test;

import org.bumble.base.log.LogInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BumbleTest {
	
	protected static Logger logger = null; 
	
	static {
		LogInitializer li = new LogInitializer();
		li.init();
		
		logger = LoggerFactory.getLogger(BumbleTest.class);
	}
	
	public abstract void template(String[] args) throws Exception;
}
