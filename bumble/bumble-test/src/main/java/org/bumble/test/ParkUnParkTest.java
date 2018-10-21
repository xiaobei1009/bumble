package org.bumble.test;

import java.lang.reflect.Field;

import org.bumble.base.test.BumbleTest;

public class ParkUnParkTest extends BumbleTest {
	
	@Override
	public void template(String[] args) throws Exception {
		
	}
	
	@SuppressWarnings("restriction")
	public static void main(String[] args) throws Exception {
		
		Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe"); //Internal reference
		f.setAccessible(true);
		final sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
		
		final Thread t = new Thread(new Runnable() {

			public void run() {
				logger.info("t1 run start");
				unsafe.park(false, 0L);
				//Thread.currentThread().interrupt();
				logger.info("t1 continue");
			}
			
		});
		t.start();
		

		Thread t2 = new Thread(new Runnable() {

			public void run() {
				logger.info("t2 run start");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				logger.info("t2 awake t1");
				unsafe.unpark(t);
			}
			
		});
		t2.start();		
	}
}
