package org.bumble.base.config;

import java.util.HashMap;
import java.util.Map;

import org.bumble.base.util.PropertyUtil;

public class LocalConfigHolder {
	
	private static final String DEFAULT_CONFIG_FILE = "bumble-default-config.properties";
	private static final String CONFIG_FILE = "bumble-config.properties";
	private static final String DOT_SEPERATOR = ".";
	private static final String SLASH_DOT_SEPERATOR = "\\.";
	
	private Map<String, String> props = null;
	private Map<String, Map<String, String>> namespacedProps = new HashMap<String, Map<String, String>>();
	
	private volatile static LocalConfigHolder instance;
	
	public static LocalConfigHolder getInstance() {
		if (instance == null) {
			instance = new LocalConfigHolder();
		}
		return instance;
	}
	
	public LocalConfigHolder() {
		try {
			props = PropertyUtil.getProperties(DEFAULT_CONFIG_FILE);
			Map<String, String> overrideProps = PropertyUtil.getProperties(CONFIG_FILE);
			props.putAll(overrideProps);
			System.out.println("[Bumble Config] : " + props.toString());
			constructProps();
		} catch (Exception e) {
			System.out.println("[Bumble Config] read failed");
		}
		
	}
	
	private void constructProps() {
		for (String key : props.keySet()) {
			String value = props.get(key);
			putProp2Namespace(key, value);
		}
	}
	
	private void putProp2Namespace(String key, String value) {
		String[] namespaceArr = key.split(SLASH_DOT_SEPERATOR);
		String accNamespace = null;
		for (int i = 0; i < namespaceArr.length; i++) {
			String namespace = namespaceArr[i];
			if (accNamespace == null) {
				accNamespace = namespace;
			} else {
				accNamespace += DOT_SEPERATOR + namespace;
			}
			Map<String, String> namespacedPropsElem = namespacedProps.get(accNamespace);
			if (namespacedPropsElem == null) {
				namespacedPropsElem = new HashMap<String, String>();
			}
			namespacedPropsElem.put(key, value);
			namespacedProps.put(accNamespace, namespacedPropsElem);
		}
	}
	
	public Map<String, String> getAllConfig() {
		return props;
	}
	
	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 * <p> 
	 * @param key
	 * @return
	 */
	public String getConfig(String key) {
		return props.get(key);
	}
	
	public Map<String, String> getConfigForNamespace(String namespace) {
		return namespacedProps.get(namespace);
	}
	
	public void overrideByArgs(String [] args) {
		for (String arg : args) {
			String[] kv = arg.split("=");
			String key = kv[0];
			String value = kv[1];
			setConfig(key, value);
		}
	}
	
	public void setConfig(String key, String value) {
		props.put(key, value);
		putProp2Namespace(key, value);
	}
}
