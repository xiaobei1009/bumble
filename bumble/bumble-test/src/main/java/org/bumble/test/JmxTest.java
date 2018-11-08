package org.bumble.test;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.jmx.Hello;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
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

        int size = MBeanServerFactory.findMBeanServer(null).size();

        logger.info("mbean server size: {}", size);

        ObjectName mbeanName = new ObjectName("org.bumble:type=Hello");
        Hello mbean = new Hello();
        mbs.registerMBean(mbean, mbeanName);

        logger.info("before mbean server invoke");
        mbs.invoke(mbeanName, "say", new Object[0], new String[0]);
        logger.info("after mbean server invoke");

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
