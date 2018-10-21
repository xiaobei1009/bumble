package org.bumble.core.test;

public class ThreadTest {
	
	public static Boolean isTest = false;
	
	public static void main( String[] args ) throws Exception
    {
//		final Clock c = new Clock();
//		
//		Thread t1 = new Thread(new Runnable() {
//			public void run() {
//				try {
//					while (true) {
//						c.setTime(System.currentTimeMillis());
//						Thread.sleep(1000);
//					}
//				} catch (Exception e)  {}
//			}
//		});
//		
//		Thread t2 = new Thread(new Runnable() {
//			public void run() {
//				try {
//					while (true) {
//						Long time = c.getTime();
//						System.out.println(time);
//						Thread.sleep(1000);
//					}
//				} catch (Exception e)  {}
//			}
//		});
		
		Thread t3 = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						ThreadTest.isTest = !ThreadTest.isTest;
						Thread.sleep(3000);
					}
				} catch (Exception e)  {}
			}
		});
		
		Thread t4 = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						System.out.println(ThreadTest.isTest);
						Thread.sleep(1000);
					}
				} catch (Exception e)  {}
			}
		});
		
//		t1.start();
//		t2.start();
		t3.start();
		t4.start();
    }
}
