package org.bumble.registry.zookeeper.test;

import org.bumble.base.test.BumbleTest;

public class ThreadTest extends BumbleTest {
	
	@SuppressWarnings("deprecation")
	public static void main( String[] args ) throws Exception
    {
		class TestRunnable implements Runnable {
			Integer i = 0;
			public void run() {
				while (true) {
					try {
						logger.info(String.valueOf(i++));
						Thread.sleep(1000);
					} catch (Exception e) {
						
					}
				}
			}
		}
		
		TestRunnable r = new TestRunnable();
		Thread t = new Thread(r);
		t.start();
		
		Thread.sleep(5000);
		t.stop();
    }

	@Override
	public void template(String[] args) throws Exception {
		
	}
}
