package org.bumble.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownWorker {
	
	private static Logger logger = LoggerFactory.getLogger(ShutdownWorker.class);
	
	public static void work(final WorkNeedToShutdown work) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
		        try {
		        	work.shutdown();
		        	logger.info(work.getClass().getName() + " exit gracefully...");
		        } catch (Exception e) {
		        	
		        }
			}
		});
	}
}
