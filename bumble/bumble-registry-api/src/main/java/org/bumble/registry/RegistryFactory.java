package org.bumble.registry;

import org.bumble.base.util.ReflectUtil;

public class RegistryFactory {

	private static IRegistryFactory registryFactory = null;
	
	public static Registry getRegistry() {
		
		if (registryFactory == null) {
			registryFactory = (IRegistryFactory)ReflectUtil.getInstance(IRegistryFactory.class);
		}
		
		return registryFactory.getRegistry();
	}
	
}
