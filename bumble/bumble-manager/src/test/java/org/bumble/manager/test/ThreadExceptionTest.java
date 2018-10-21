package org.bumble.manager.test;

import org.bumble.base.test.BumbleTest;

public class ThreadExceptionTest extends BumbleTest {
	
	public static void main(String[] args) throws Exception {
		try {
			Thread thread = new Thread(new Runnable() {

				public void run() {
					throw new RuntimeException("xxx");
				}
				
			});
			thread.start();
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
	@Override
	public void template(String[] args) throws Exception {
		
	}
}
