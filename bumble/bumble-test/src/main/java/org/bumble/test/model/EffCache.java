package org.bumble.test.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EffCache {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private AtomicBoolean isWriting = new AtomicBoolean(false);
	private Lock writeLock = new ReentrantLock();
	private Condition writeCondition = writeLock.newCondition();
	
	private Map<String, String> cache = new HashMap<String, String>();
	
	@SuppressWarnings("static-access")
	public void set(String key, String value) {
		writeLock.lock();
		isWriting.set(true);
		
		try {
			logger.error("before set value & sleep 2s");
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cache.put(key, value);
		
		isWriting.set(false);
		writeCondition.signalAll();
		logger.error("after set value & signal all threads");
		
		writeLock.unlock();
	}
	
	public String get(String key) {
		if (isWriting.get()) {
			writeLock.lock();
			try {
				writeCondition.await();
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
			writeLock.unlock();
		}
		
		return cache.get(key);
	}
}
