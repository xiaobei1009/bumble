package org.bumble.test;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.jmx.Hello;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author : shenxiangyu
 * @date :
 */
public class JmxTest extends BumbleTest {
    @Override
    public void template(String[] args) throws Exception {

    }

    public static void main(String[] args) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = new ObjectName("org.bumble:type=Hello");
        Hello mbean = new Hello();
        mbs.registerMBean(mbean, mbeanName);

        Thread t = new Thread(() -> {
            while (true) {
                mbean.say();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        System.out.println("Waiting for incoming requests...");
        Thread.sleep(Long.MAX_VALUE);
    }
}
