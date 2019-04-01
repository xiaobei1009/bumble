package org.bumble.registry;

import org.bumble.registry.data.RegistryData;

public interface RegistryChangedNotifier {
	/**
	 * it will be notified with new registry data when the registry is changed
	 * <p>
	 * 
	 * @param registryData
	 */
	void doNotify(RegistryData registryData);
}
