package org.bumble.registry.support;

import java.util.HashMap;
import java.util.Map;

import org.bumble.registry.IRegistryFactory;
import org.bumble.registry.Registry;

import org.bumble.config.ConfigCenterConst;
import org.bumble.config.ConfigChangedNotifier;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

public abstract class AbstractRegistryFactory implements IRegistryFactory {
	
	private Registry registry = null;
	private Boolean configChanged = false;
	
	public Registry getRegistry() {
		
		if (registry != null && configChanged == false) {
			return registry;
		}
		
		if (registry != null && configChanged) {
			registry.exit();
		}
		
		Configurator configurator = ConfiguratorFactory.getConfigurator();
		configurator.watchOnConfig(new String[] {
			ConfigCenterConst.Registry.NS, 
			ConfigCenterConst.Registry.RELOAD_FLAG
		}, new ConfigChangedNotifier() {
			public void doNotify(String key, String value) {
				registry = doGetRegistry(getConfig());
			}
		});
		registry = doGetRegistry(getConfig());
		
		return registry;
	}
	
	private Map<String, String> getConfig() {
		Configurator configurator = ConfiguratorFactory.getConfigurator();
		
		Map<String, String> defaultParamPair = getDefaultParamPair();
		
		Map<String, String> config = new HashMap<String, String>();
		
		for (String paramName : defaultParamPair.keySet()) {
			String defaultValue = defaultParamPair.get(paramName);
			configurator.setConfigIfNotExist(new String[] {
				ConfigCenterConst.Registry.NS,
				paramName
			}, defaultValue);
			
			String paramVal = configurator.getConfig(new String[] {
				ConfigCenterConst.Registry.NS, 
				paramName
			});
			config.put(paramName, paramVal);
		}
		return config;
	}
	
	protected abstract Map<String, String> getDefaultParamPair();
	protected abstract Registry doGetRegistry(Map<String, String> config);
}
