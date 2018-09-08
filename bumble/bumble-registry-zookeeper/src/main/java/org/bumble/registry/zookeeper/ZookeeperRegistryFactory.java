package org.bumble.registry.zookeeper;

import java.util.HashMap;
import java.util.Map;

import org.bumble.config.ConfigCenterConst;
import org.bumble.registry.Registry;
import org.bumble.registry.support.AbstractRegistryFactory;

public class ZookeeperRegistryFactory extends AbstractRegistryFactory {
	
	private static final String REGISTRY_ZK_ADDR_DEFAULT_V = "10.18.5.110:2181,10.18.5.110:2182,10.18.5.110:2183";
	private static final String REGISTRY_ZK_TIMOUT_DEFAULT_V = "5000";
	
	@Override
	protected Map<String, String> getDefaultParamPair() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(
			ConfigCenterConst.Registry.ADDRESS, 
			REGISTRY_ZK_ADDR_DEFAULT_V);
		paramMap.put(
				ConfigCenterConst.Registry.TIMEOUT,
			REGISTRY_ZK_TIMOUT_DEFAULT_V);
		return paramMap;
	}
	
	@Override
	protected Registry doGetRegistry(Map<String, String> config) {
		Registry registry = new ZookeeperRegistry(config);
		return registry;
	}
}
