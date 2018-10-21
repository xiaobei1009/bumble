package org.bumble.test;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.model.SyncProcessor;

public class ReentrantLockTest2 extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}

	public static void main(String[] args) throws Exception {
		final SyncProcessor p = new SyncProcessor();
		Thread t = new Thread(new Runnable() {

			public void run() {
				logger.info("Before signal");
				try {
					p.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.info("After signal");
			}
			
		});
		t.start();
		
		Thread t2 = new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				logger.info("Signal");
				p.signal();
			}
			
		});
		t2.start();
	}
}
