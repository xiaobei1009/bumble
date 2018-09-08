package org.bumble.config;

import java.util.Map;

public interface Configurator {
	
	public final String ROOT = "root";
	
	public final String CONFIG_NS = "bumble.config";
	
	/**
	 * Set value for a configuration key chain
	 * 
	 * @param keyChain
	 * @param value
	 */
	public void setConfig(String[] keyChain, String value);
	
	/**
	 * Set value for a configuration key chain if it does not exist
	 * 
	 * @param keyChain
	 * @param value
	 */
	public void setConfigIfNotExist(String[] keyChain, String value);
	
	/**
	 * Judge if a configuration exists
	 * 
	 * @param keyChain
	 * @return
	 */
	public boolean configExists(String[] keyChain);
	
	/**
	 * Get a value of a configuration key chain
	 * 
	 * @param key
	 * @return
	 */
	public String getConfig(String[] keyChain);
	
	/**
	 * Get a value of a configuration key chain
	 * and watch on this configuration for changes
	 * 
	 * @param key
	 * @return
	 */
	public String getConfig(String[] keyChain, ConfigChangedNotifier notifier);
	
	/**
	 * Override all the configuration by a json string
	 * 
	 * @param json
	 */
	public void setAllConfig(String json);
	
	/**
	 * get all the configuration
	 * 
	 * @return
	 */
	public String getAllConfig();
	
	/**
	 * Get all the configuration
	 * and watch on the configuration for any change
	 * 
	 * @param notifier
	 * @return
	 */
	public String getAllConfig(ConfigChangedNotifier notifier);
	
	/**
	 * Clear all the configuration
	 * 
	 */
	public void clear();
	
	/**
	 * Watch on a configuration key chain
	 * the callback will be notified if the 
	 * corresponding configuration value is changed
	 * 
	 * @param key
	 * @param notifier
	 */
	public void watchOnConfig(String[] keyChain, ConfigChangedNotifier notifier);
	
	/**
	 * Get subsequent configuration and watch
	 * 
	 * @param keyChain
	 * @param notifier
	 * @return
	 */
	public String getSubsequentConfigAndWatch(String[] keyChain, ConfigChangedNotifier notifier);
	
	/**
	 * Get name spaced configuration map
	 * 
	 * @param namespace
	 * @param notifier
	 * @return
	 */
	public Map<String, String> getNamespacedConfig(String[] keyChain, ConfigChangedNotifier notifier);
}
