package org.bumble.test;

import java.util.concurrent.CountDownLatch;

import org.bumble.base.test.BumbleTest;

public class CountDownLatchTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}
	public static void main(String[] args) throws Exception {
		final CountDownLatch cdl = new CountDownLatch(3);
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					logger.info("before await");
					cdl.await();
					logger.info("after await");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(3000);
					
					while(true) {
						Long cdlCount = cdl.getCount();
						logger.info("remaining count: " + String.valueOf(cdlCount));
						if (cdlCount == 0) {
							break;
						}
						
						logger.info("count down");
						
						cdl.countDown();
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t2.start();
	}
}
