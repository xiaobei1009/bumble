package org.bumble.test;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.model.EffCache;

public class MultiReadOneWriteTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}

	public static void main(String[] args) throws Exception {
		final EffCache cache = new EffCache();
		
		Runnable rr = new Runnable() {

			public void run() {
				while (true) {

					String value = cache.get("aa");
					logger.error("{}, get value = {}", Thread.currentThread().getName(), value);
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		};
		
		Runnable wr = new Runnable() {

			public void run() {
				while (true) {
					cache.set("aa", String.valueOf(Math.random()));
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		};
		
		Thread wt = new Thread(wr, "Write Thread");
		wt.start();
		
		for (int i = 0; i < 50; i++) {
			Thread rt = new Thread(rr, "Read Thread-" + String.valueOf(i));
			rt.start();
		}
		
		
	}
}
