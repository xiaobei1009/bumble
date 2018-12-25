package org.bumble.config;

import java.util.Map;

public interface Configurator {
	
	String ROOT = "root";
	
	String CONFIG_NS = "bumble.config";
	
	/**
	 * Set value for a configuration key chain
	 * 
	 * @param keyChain
	 * @param value
	 */
	void setConfig(String[] keyChain, String value);
	
	/**
	 * Set value for a configuration key chain if it does not exist
	 * 
	 * @param keyChain
	 * @param value
	 */
	void setConfigIfNotExist(String[] keyChain, String value);
	
	/**
	 * Judge if a configuration exists
	 * 
	 * @param keyChain
	 * @return
	 */
	boolean configExists(String[] keyChain);
	
	/**
	 * Get a value of a configuration key chain
	 * 
	 * @param keyChain
	 * @return
	 */
	String getConfig(String[] keyChain);
	
	/**
	 * Get a value of a configuration key chain
	 * and watch on this configuration for changes
	 * 
	 * @param keyChain
	 * @param notifier
	 * @return
	 */
	String getConfig(String[] keyChain, ConfigChangedNotifier notifier);
	
	/**
	 * Override all the configuration by a json string
	 * 
	 * @param json
	 */
	void setAllConfig(String json);
	
	/**
	 * get all the configuration
	 * 
	 * @return
	 */
	String getAllConfig();
	
	/**
	 * Get all the configuration
	 * and watch on the configuration for any change
	 * 
	 * @param notifier
	 * @return
	 */
	String getAllConfig(ConfigChangedNotifier notifier);
	
	/**
	 * Clear all the configuration
	 * 
	 */
	void clear();
	
	/**
	 * Watch on a configuration key chain
	 * the callback will be notified if the 
	 * corresponding configuration value is changed
	 * 
	 * @param keyChain
	 * @param notifier
	 */
	void watchOnConfig(String[] keyChain, ConfigChangedNotifier notifier);
	
	/**
	 * Get subsequent configuration and watch
	 * 
	 * @param keyChain
	 * @param notifier
	 * @return
	 */
	String getSubsequentConfigAndWatch(String[] keyChain, ConfigChangedNotifier notifier);
	
	/**
	 * Get name spaced configuration map
	 * 
	 * @param keyChain
	 * @param notifier
	 * @return
	 */
	Map<String, String> getNamespacedConfig(String[] keyChain, ConfigChangedNotifier notifier);

	/**
	 * Shutdown the configurator client
	 *
	 */
	void shutdown();
}
