package org.bumble.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bumble.base.test.BumbleTest;

public class HighSpeedCacheTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}
	
	public static void main(String[] args) throws Exception {
		final Map<String, String> map = new HashMap<String, String>();
		final Lock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		
		Thread w = new Thread(new Runnable() {
			public void run() {
				try {
					logger.info("w-start");
					Thread.sleep(1000);
					
					logger.info("w-signal");
					
					lock.lock();
					condition.signalAll();
					lock.unlock();
					
					logger.info("w-end");
				} catch (Exception e) {}
			}
		});
		w.start();
		
		Thread r1 = new Thread(new Runnable() {
			public void run() {
				try {
					logger.info("r1-start");
					
					lock.lock();
					condition.await();
					lock.unlock();
					
					logger.info("r1-end");
				} catch (Exception e) {}
			}
		});
		r1.start();
		
		Thread r2 = new Thread(new Runnable() {
			public void run() {
				try {
					logger.info("r2-start");
					
					lock.lock();
					condition.await();
					lock.unlock();
					
					logger.info("r2-end");
				} catch (Exception e) {}
			}
		});
		r2.start();
	}
}
