package org.bumble.test;

import org.bumble.base.test.BumbleTest;

/**
 * @author : shenxiangyu
 * @date : 2018-11-07
 */
public class ThreadJoinTest extends BumbleTest {
    @Override
    public void template(String[] args) throws Exception {

    }

    private static Thread newThread(String key) {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.err.println("T" + key +"." + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return t;
    }

    public static void main(String[] args) throws Exception {
        Thread t1 = newThread("1");

        Thread t2 = newThread("2");

        t1.start();
        t1.join();

        t2.start();
    }
}
