package org.bumble.config;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractConfigurator implements Configurator {
	
	public void setConfigIfNotExist(String[] keyChain, String value) {
		boolean configExists = this.configExists(keyChain);
		if (!configExists) {
			this.setConfig(keyChain, value);
		}
	}
	
	private Map<String, Map<String, String>> configMap = new HashMap<String, Map<String, String>>();
	
	private void putMap4Namespace(String namespace, String key, String value) {
		Map<String, String> map = configMap.get(namespace);
		if (map == null) {
			map = new HashMap<String, String>();
		}
		map.put(key, value);
		configMap.put(namespace, map);
		
		String[] nsArr = namespace.split("\\.");
		
		if (nsArr.length > 1) {
			String newNs = join(nsArr, ".", 0, nsArr.length - 1);
			putMap4Namespace(newNs, key, value);
		}
	}
	
	/**
	 * Borrow from apache common
	 * <br>
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * A {@code null} separator is the same as an empty String ("").
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     *
     * <pre>
     * StringUtils.join(null, *, *, *)                = null
     * StringUtils.join([], *, *, *)                  = ""
     * StringUtils.join([null], *, *, *)              = ""
     * StringUtils.join(["a", "b", "c"], "--", 0, 3)  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], "--", 1, 3)  = "b--c"
     * StringUtils.join(["a", "b", "c"], "--", 2, 3)  = "c"
     * StringUtils.join(["a", "b", "c"], "--", 2, 2)  = ""
     * StringUtils.join(["a", "b", "c"], null, 0, 3)  = "abc"
     * StringUtils.join(["a", "b", "c"], "", 0, 3)    = "abc"
     * StringUtils.join([null, "", "a"], ',', 0, 3)   = ",,a"
     * </pre>
     *
     * @param array  the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from.
     * @param endIndex the index to stop joining from (exclusive).
     * @return the joined String, {@code null} if null array input; or the empty string
     * if {@code endIndex - startIndex <= 0}. The number of joined entries is given by
     * {@code endIndex - startIndex}
     * @throws ArrayIndexOutOfBoundsException ife<br>
     * {@code startIndex < 0} or <br>
     * {@code startIndex >= array.length()} or <br>
     * {@code endIndex < 0} or <br>
     * {@code endIndex > array.length()}
     */
	private String join(final Object[] array, String separator, final int startIndex, final int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        
        final int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }

        final StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
	
	private void addConfig2Map(String namespace, JSONObject jsnObj) {
		for (String key : jsnObj.keySet()) {
			Object entry = jsnObj.get(key);
			String subNs = namespace + "." + key;
			if (entry instanceof String) {
				putMap4Namespace(namespace, subNs, entry.toString());
			} else {
				addConfig2Map(subNs, (JSONObject)entry);
			}
		}
	}
	
	private String keyChain2Ns(String[] keyChain) {
		String ret = Configurator.ROOT;
		for (String key : keyChain) {
			ret += "." + key;
		}
		return ret;
	}
	
	public Map<String, String> getNamespacedConfig(String[] keyChain, ConfigChangedNotifier notifier) {
		String jsonStr = getSubsequentConfigAndWatch(keyChain, notifier);
		JSONObject jsnObj = JSONObject.parseObject(jsonStr);
		String namespace = keyChain2Ns(keyChain);
		
		addConfig2Map(namespace, jsnObj);
		
		return configMap.get(namespace);
	}
}
