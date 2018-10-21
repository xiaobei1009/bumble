package org.bumble.config.zookeeper.test;

import org.bumble.config.ConfigChangedNotifier;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

public class ConfigChangeWatcher implements Runnable {

	public void run() {

		Configurator configurator = ConfiguratorFactory.getConfigurator();
		
		configurator.getConfig(new String[] {"kkk"}, new ConfigChangedNotifier() {
			public void doNotify(String key, String value) {
				System.out.println("notified");
			}
		});
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
