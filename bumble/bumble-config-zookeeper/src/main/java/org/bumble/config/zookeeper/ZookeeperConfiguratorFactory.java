package org.bumble.config.zookeeper;

import org.bumble.config.Configurator;
import org.bumble.config.IConfiguratorFactory;

public class ZookeeperConfiguratorFactory implements IConfiguratorFactory {

	private Configurator configurator = null;
	
	public Configurator getConfigurator() {
		if (configurator == null) {
			configurator = new ZookeeperConfigurator();
		}
		return configurator;
	}
}
