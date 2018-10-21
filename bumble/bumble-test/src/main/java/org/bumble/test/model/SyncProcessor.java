package org.bumble.test.model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncProcessor {
	
	ReentrantLock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	
	public void await() throws InterruptedException {
		lock.lock();
		condition.await();
		lock.unlock();
	}
	
	public void signal() {
		lock.lock();
		condition.signal();
		lock.unlock();
	}
	
	public void testLock(String msg) {
		lock.lock();
		try {
			Logger logger = LoggerFactory.getLogger(getClass());
			int i = 0;
			while (i < 5) {
				i++;
				logger.info(msg);
				Thread.sleep(1000);
			}
		} catch (Exception e) {}
		lock.unlock();
	}
	
	public void testLock2(String msg) {
		lock.lock();
		try {
			Logger logger = LoggerFactory.getLogger(getClass());
			int i = 0;
			while (i < 5) {
				i++;
				logger.info(msg);
				Thread.sleep(1000);
			}
		} catch (Exception e) {}
		lock.unlock();
	}
}
