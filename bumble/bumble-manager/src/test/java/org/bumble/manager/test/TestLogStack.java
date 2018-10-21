package org.bumble.manager.test;

import org.bumble.base.log.LogInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogStack {
	static LogInitializer li = new LogInitializer();
	
	private static Logger logger = LoggerFactory.getLogger(TestLogStack.class);
			
	public static void main( String[] args ) throws Exception
    {
		li.init();
		
		
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					throw new Exception("aaa");
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.trace(e.getMessage(), e);
				}
			}
		});
		t.start();
    }
}
