package org.bumble.config.zookeeper.test;

import org.bumble.config.ConfigChangedNotifier;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

public class TestSubSequentConfig {
	public static void main( String[] args ) throws InterruptedException
    {
        System.out.println( "TestSubSequentConfig" );
        
        Configurator configurator = ConfiguratorFactory.getConfigurator();
        configurator.setConfig(new String[] {"clients", "bumble-springcloud-demo1", "enable-bumble-tx"}, "true");
        configurator.setConfig(new String[] {"clients", "bumble-springcloud-demo2", "enable-bumble-tx"}, "true");
        
        String allConfigStr = configurator.getAllConfig(new ConfigChangedNotifier() {

			public void doNotify(String key, String value) {
				System.out.println("config changed");
			}
        });
        System.out.println(allConfigStr);
        Thread.sleep(5000);
        
        configurator.setConfig(new String[] {"kkk"}, "222");
        
        Thread.sleep(50000);
    }
}
