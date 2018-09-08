package org.bumble.config;

import org.bumble.base.util.ReflectUtil;

public class ConfiguratorFactory {
	
	private static IConfiguratorFactory configuratorFactory = null;
	
	public static Configurator getConfigurator() {
		if (configuratorFactory == null) {
			configuratorFactory = (IConfiguratorFactory) ReflectUtil.getInstance(IConfiguratorFactory.class);
		}
		return configuratorFactory.getConfigurator();
	};
}
