package org.bumble.config;

public interface ConfigChangedNotifier {
	/**
	 * it will be notified when the configuration is changed
	 * watched key and value will be retrieved.
	 * <p>
	 * 
	 * @param key
	 * @param value
	 */
	void doNotify(String key, String value);
}
