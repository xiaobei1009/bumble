package org.bumble.registry.zookeeper.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bumble.registry.Registry;
import org.bumble.registry.RegistryChangedNotifier;
import org.bumble.registry.RegistryFactory;
import org.bumble.registry.data.RegistryData;

public class WatchRegistryTest {
	
	public static void main( String[] args ) throws InterruptedException
    {
		Logger logger = LoggerFactory.getLogger(WatchRegistryTest.class);
		
        System.out.println( "Run Watch Registry Test" );
        
        Registry registry = RegistryFactory.getRegistry();
        
        Thread.sleep(1000);
        
        System.out.println("watch start");
        registry.watchOnRegistry(new RegistryChangedNotifier() {

			public void doNotify(RegistryData registryData) {
				System.out.println("do notified called...");
			}
        	
        });
        System.out.println("watch end");
        Thread.sleep(1000);
        
        System.out.println("register start");
        registry.register("m1", "1.2.3.4:1234");
        
        System.out.println("register end");
        Thread.sleep(1000);
        
        System.out.println("bind start");
        registry.bindClientToManager("s1", "1.1.1", "m1");
        System.out.println("bind end");
        Thread.sleep(1000);
        
        System.out.println("unbind start");
        registry.unbindClientFromManager("s1", "m1");
        System.out.println("unbind end");
        Thread.sleep(1000);
        
        System.out.println("unregister start");
        registry.unregister("m1");
        System.out.println("unregister end");
        
        Thread.sleep(5000);
        logger.info("end");
        
    }
}
