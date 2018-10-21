package org.bumble.core.test;

public class ThreadTest2 {
	
	public static void main( String[] args ) throws Exception
    {
		final ClockFactory cf = new ClockFactory();
		
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Clock c = new Clock();
						c.setTime(Math.round(Math.random() * 1000));
						cf.addClock(c);
						Thread.sleep(3000);
					}
				} catch (Exception e)  {}
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						System.out.println(cf.toString());
						Thread.sleep(1000);
					}
				} catch (Exception e)  {}
			}
		});
		t1.start();
		t2.start();
    }
}
