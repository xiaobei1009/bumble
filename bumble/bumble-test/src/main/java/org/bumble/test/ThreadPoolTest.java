package org.bumble.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bumble.base.test.BumbleTest;

public class ThreadPoolTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}

	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newFixedThreadPool(1);
		threadPool.execute(new Runnable() {
			public void run() {
				try {
					int i = 0;
					while (true) {
						logger.info("111");
						Thread.sleep(1000);
						i++;
						if (i > 3) {
							break;
						}
					}
				} catch (Exception e) {}
			}
		});
		threadPool.execute(new Runnable() {
			public void run() {
				try {
					while (true) {
						logger.info("222");
						Thread.sleep(1000);
					}
				} catch (Exception e) {}
			}
		});
	}
}
