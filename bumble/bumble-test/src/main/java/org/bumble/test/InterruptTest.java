package org.bumble.test;

import org.bumble.base.test.BumbleTest;

public class InterruptTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}
	
	public static void main(String[] args) throws Exception {
		final Thread t = new Thread(new Runnable() {

			public void run() {
				logger.info("t1 run start");
				
				for (int i = 0; i < 10; i++) {
					logger.info("t1 loop[{}]", i);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						//e.printStackTrace();
						break;
					}
				}
				
				logger.info("t1 end");
			}
			
		});
		t.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		t.interrupt();
		
	}
}
