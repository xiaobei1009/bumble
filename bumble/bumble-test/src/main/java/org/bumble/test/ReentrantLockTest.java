package org.bumble.test;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.model.SyncProcessor;

public class ReentrantLockTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}

	public static void main(String[] args) throws Exception {
		final SyncProcessor p = new SyncProcessor();
		
		Thread t = new Thread(new Runnable() {

			public void run() {
				p.testLock("Test in sub thread");
			}
			
		});
		t.start();
		
		p.testLock2("Test in main thread");
	}
}
